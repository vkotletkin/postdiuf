package ru.kotletkin.bot;

import com.google.gson.Gson;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.kotletkin.model.ChatMessageSenderAccount;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class MessagesBrokerBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final Path pathToSaveMessages;

    private final Gson gson;

    public MessagesBrokerBot(String token, String pathToSaveMessages) throws IOException {
        telegramClient = new OkHttpTelegramClient(token);
        this.pathToSaveMessages = Paths.get(pathToSaveMessages);
        Files.createDirectories(this.pathToSaveMessages);
        gson = new Gson();
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            Message message = update.getMessage();

            ChatMessageSenderAccount chatMessageSenderAccount = ChatMessageSenderAccount.builder()
                    .userName(message.getFrom().getUserName())
                    .firstName(message.getFrom().getFirstName())
                    .lastName(message.getFrom().getLastName())
                    .id(message.getFrom().getId())
                    .botStatus(message.getFrom().getIsBot())
                    .chatID(message.getChatId())
                    .message(message.getText())
                    .build();

            String filename = generateFilenamePath();

            saveInfoToFile(chatMessageSenderAccount, filename);

            SendMessage sendMessage = generateTelegramAnswer(chatMessageSenderAccount.getChatID(), chatMessageSenderAccount.getMessage());

            try {
                telegramClient.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private String generateFilenamePath() {
        return Paths.get(pathToSaveMessages.toString(), UUID.randomUUID().toString() + ".json").toString();
    }

    private void saveInfoToFile(ChatMessageSenderAccount account, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(gson.toJson(account));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SendMessage generateTelegramAnswer(long chatID, String message) {
        return SendMessage.builder().chatId(chatID).text(message).build();
    }
}
