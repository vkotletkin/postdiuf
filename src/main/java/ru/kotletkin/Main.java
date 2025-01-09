package ru.kotletkin;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import ru.kotletkin.bot.MessagesBrokerBot;

@Slf4j
public class Main {

    public static String PATH_TO_SAVE_MESSAGES = "./messages";
    public static String BOT_TOKEN = System.getenv("TELEGRAM_TOKEN");

    public static void main(String[] args) {

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {

            botsApplication.registerBot(
                    BOT_TOKEN, new MessagesBrokerBot(BOT_TOKEN, PATH_TO_SAVE_MESSAGES));

            System.out.println("MessageBrokerBot started!");

            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
