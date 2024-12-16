package ru.kotletkin.bots;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

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

    public MessagesBrokerBot(String token, String pathToSaveMessages) throws IOException {
        telegramClient = new OkHttpTelegramClient(token);
        this.pathToSaveMessages = Paths.get(pathToSaveMessages);
        Files.createDirectories(this.pathToSaveMessages);
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            long userID = update.getMessage().getFrom().getId();
            String firstNameUser = update.getMessage().getFrom().getFirstName();
            String lastNameUser = update.getMessage().getFrom().getLastName();
            String userNameUser = update.getMessage().getFrom().getUserName();
            boolean isBotUser = update.getMessage().getFrom().getIsBot();


            String uuidToAppend = UUID.randomUUID().toString();
            String filename = Paths.get(pathToSaveMessages.toString(), uuidToAppend).toString();


            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                writer.write(message_text);
            } catch (IOException e) {
                e.printStackTrace();
            }

            SendMessage message = SendMessage // Create a message object
                    .builder()
                    .chatId(chat_id)
                    .text(message_text)
                    .build();
            try {
                telegramClient.execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
