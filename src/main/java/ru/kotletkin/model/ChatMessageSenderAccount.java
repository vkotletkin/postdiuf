package ru.kotletkin.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageSenderAccount {
    final String userName;
    final String firstName;
    final String lastName;
    final long id;
    final boolean botStatus;
    final long chatID;
    final String message;
}
