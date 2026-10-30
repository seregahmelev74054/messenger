package com.example.messenger.webSocket.dto;

public class UserTypingEvent extends Event {

    private long userId;
    private long conversationId;
    private String userDisplayName;

    public UserTypingEvent(long userId, String userDisplayName, long conversationId) {

        super(EventType.USER_TYPING);

        this.userId = userId;
        this.userDisplayName = userDisplayName;
        this.conversationId = conversationId;
    }

    public long getUserId() {
        return userId;
    }

    public long getConversationId() {
        return conversationId;
    }

    public String getUserDisplayName() {return userDisplayName;}
}
