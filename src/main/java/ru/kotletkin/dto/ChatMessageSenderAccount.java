package ru.kotletkin.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChatMessageSenderAccount {
    String userName;
    String firstName;
    String lastName;
    long id;
    boolean botStatus;
    long chatID;
    String message;
}