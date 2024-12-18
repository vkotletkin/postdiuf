package ru.kotletkin.accounts;

import org.telegram.telegrambots.meta.api.objects.message.Message;

public class AccountCreators {

  public static ChatMessageSenderAccount createChatMessageSender(Message message) {
    return new ChatMessageSenderAccount(
        message.getFrom().getUserName(),
        message.getFrom().getFirstName(),
        message.getFrom().getLastName(),
        message.getFrom().getId(),
        message.getFrom().getIsBot(),
        message.getChatId(),
        message.getText());
  }
}
