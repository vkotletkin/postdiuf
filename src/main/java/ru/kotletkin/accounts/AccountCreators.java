package ru.kotletkin.accounts;

import org.telegram.telegrambots.meta.api.objects.message.Message;

public class AccountCreators {

  public static ChatMessageSenderAccount createChatMessageSender(Message message) {
    long id = message.getFrom().getId();
    String firstNameUser = message.getFrom().getFirstName();
    String lastNameUser = message.getFrom().getLastName();
    String userNameUser = message.getFrom().getUserName();
    boolean botStatus = message.getFrom().getIsBot();
    long chatID = message.getChatId();
    String messageText = message.getText();

    return new ChatMessageSenderAccount(
        userNameUser, firstNameUser, lastNameUser, id, botStatus, chatID, messageText);
  }
}
