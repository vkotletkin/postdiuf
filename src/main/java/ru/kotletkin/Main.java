package ru.kotletkin;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import ru.kotletkin.bots.MessagesBrokerBot;

public class Main {
    public static void main(String[] args) {

        // Register our bot
        String botToken = System.getenv("TELEGRAM_TOKEN");

        String pathToSaveMessages = "./messages";

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new MessagesBrokerBot(botToken, pathToSaveMessages));
            System.out.println("MessageBrokerBot started!");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}