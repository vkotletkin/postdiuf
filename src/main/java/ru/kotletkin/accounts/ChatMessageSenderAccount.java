package ru.kotletkin.accounts;

public class ChatMessageSenderAccount extends Account {

    private final boolean botStatus;
    private final long chatID;
    private final String message;

    public ChatMessageSenderAccount(String userName, String firstName, String lastName, long id,
        boolean botStatus, long chatID, String message) {
        super(userName, firstName, lastName, id);
        this.botStatus = botStatus;
        this.chatID = chatID;
        this.message = message;
    }

    public boolean getBotStatus() {
        return botStatus;
    }

    public long getChatID() {
        return chatID;
    }

    public String getMessage() {
        return message;
    }
}
