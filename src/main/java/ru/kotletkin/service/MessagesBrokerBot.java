package ru.kotletkin.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.kotletkin.config.AppConfig;
import ru.kotletkin.dto.ChatMessageSenderAccount;
import ru.kotletkin.service.config.S3Config;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.kotletkin.config.AppConfig.S3_BUCKET;

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
        if (!bucketExists(S3_BUCKET)) {
            s3Client.createBucket(request -> request.bucket(S3_BUCKET));
        }
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
                    .languageCode(message.getFrom().getLanguageCode())
                    .date(message.getDate())
                    .id(message.getFrom().getId())
                    .botStatus(message.getFrom().getIsBot())
                    .chatID(message.getChatId())
                    .chatType(message.getChat().getType())
                    .chatTitle(message.getChat().getTitle())
                    .message(message.getText())
                    .build();
            log.debug("Assembling a class object for message - success");

            String filename = UUID.randomUUID() + ".json";
            log.info("For a message from a user: {} generated filename: {}", message.getFrom().getUserName(), filename);

            executorService.execute(() -> {

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(S3_BUCKET)
                        .key(filename)
                        .contentType("application/json")
                        .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromString(gson.toJson(chatMessageSenderAccount)));
                log.info("Put file to S3. Bucket: {}, filename: {}", S3_BUCKET, filename);
            });

            SendMessage sendMessage = generateTelegramAnswer(chatMessageSenderAccount.getChatID(),
                    chatMessageSenderAccount.getMessage());

            // commented to save data
//            try {
//                telegramClient.execute(sendMessage);
//            } catch (TelegramApiException e) {
//                log.error(e.getMessage(), e);
//            }
        }
    }

    private SendMessage generateTelegramAnswer(long chatID, String message) {
        return SendMessage.builder().chatId(chatID).text(message).build();
    }

    private boolean bucketExists(String bucketName) {
        try {
            s3Client.headBucket(request -> request.bucket(bucketName));
            return true;
        } catch (NoSuchBucketException exception) {
            return false;
        }
    }
}
