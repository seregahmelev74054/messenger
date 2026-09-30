package com.example.messenger.CONVERSATION.dto;

import com.example.messenger.MESSAGE.dto.MessageResponse;

import java.time.Instant;

public class MyConversation {

    public MyConversation(long conversationId, MessageResponse lastMessage, Instant updatedAt, long unreadCount) {
        this.conversationId = conversationId;
        this.lastMessage = lastMessage;
        this.updatedAt = updatedAt;
        this.unreadCount = unreadCount;
    }

    private long conversationId;

    private MessageResponse lastMessage;

    private Instant updatedAt;

    private long unreadCount;

    public long getConversationId() {
        return conversationId;
    }

    public MessageResponse getLastMessage() {
        return lastMessage;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public long getUnreadCount() {
        return unreadCount;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public void setLastMessage(MessageResponse lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUnreadCount(long unreadCount) {
        this.unreadCount = unreadCount;
    }
}
