package ru.kotletkin;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import ru.kotletkin.config.AppConfig;
import ru.kotletkin.service.MessagesBrokerBot;

@Slf4j
public class Main {

    public static void main(String[] args) {

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {

            botsApplication.registerBot(
                    AppConfig.BOT_TOKEN, new MessagesBrokerBot());

            System.out.println("MessageBrokerBot started!");

            Thread.currentThread().join();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
