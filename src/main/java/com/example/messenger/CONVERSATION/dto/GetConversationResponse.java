package com.example.messenger.CONVERSATION.dto;

import com.example.messenger.CONVERSATION.entity.Conversation;
import com.example.messenger.CONVERSATION.entity.ConversationType;
import com.example.messenger.CONVERSATION.entity.ConversationVisibility;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class GetConversationResponse {

    public GetConversationResponse(Conversation conversation) {
        this.id = conversation.getId();
        this.title = conversation.getTitle();
        this.type = conversation.getType();
        this.visibility = conversation.getVisibility();
        this.createdAt = conversation.getCreatedAt();
    }

    private long id;

    private String title;

    private ConversationType type;

    private ConversationVisibility visibility;

    private LocalDateTime createdAt;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ConversationType getType() {
        return type;
    }

    public ConversationVisibility getVisibility() {
        return visibility;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
