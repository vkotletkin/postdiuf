package ru.kotletkin.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.kotletkin.config.AppConfig;
import ru.kotletkin.dto.ChatMessageSenderAccount;
import ru.kotletkin.service.config.S3Config;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class MessagesBrokerBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final Gson gson;
    private final ExecutorService executorService;
    private final S3Client s3Client;

    public MessagesBrokerBot() {
        telegramClient = new OkHttpTelegramClient(AppConfig.BOT_TOKEN);
        gson = new Gson();
        executorService = Executors.newVirtualThreadPerTaskExecutor();
        s3Client = S3Config.getS3Client();
        s3Client.createBucket(request -> request.bucket(AppConfig.S3_BUCKET));
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            Message message = update.getMessage();
            log.info("A new message has been received from the user: {}", message.getFrom().getUserName());

            log.debug("Assembling a class object for message");
            ChatMessageSenderAccount chatMessageSenderAccount = ChatMessageSenderAccount.builder()
                    .userName(message.getFrom().getUserName())
                    .firstName(message.getFrom().getFirstName())
                    .lastName(message.getFrom().getLastName())
                    .id(message.getFrom().getId())
                    .botStatus(message.getFrom().getIsBot())
                    .chatID(message.getChatId())
                    .message(message.getText())
                    .build();
            log.debug("Assembling a class object for message - success");

            String filename = UUID.randomUUID() + ".json";
            log.info("For a message from a user: {} generated filename: {}", message.getFrom().getUserName(), filename);

            executorService.execute(() -> System.out.println(filename));

            SendMessage sendMessage = generateTelegramAnswer(chatMessageSenderAccount.getChatID(),
                    chatMessageSenderAccount.getMessage());

            try {
                telegramClient.execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private SendMessage generateTelegramAnswer(long chatID, String message) {
        return SendMessage.builder().chatId(chatID).text(message).build();
    }
}
