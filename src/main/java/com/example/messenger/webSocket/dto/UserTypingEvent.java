package com.example.messenger.webSocket.dto;

public class UserTypingEvent extends Event {

    private long userId;
    private long conversationId;

    public UserTypingEvent(long userId, long conversationId) {

        super(EventType.USER_TYPING);

        this.userId = userId;
        this.conversationId = conversationId;
    }

    public long getUserId() {
        return userId;
    }

    public long getConversationId() {
        return conversationId;
    }
}
