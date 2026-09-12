package com.example.messenger.MESSAGE.dto;

import com.example.messenger.MEMBER.dto.MemberResponse;
import com.example.messenger.MESSAGE.entity.Message;

import java.time.LocalDateTime;

public class MessageResponse {

    public MessageResponse(Message message) {
        this.id = message.getId();
        this.sender = new MemberResponse(message.getSender());
        this.text = message.getText();
        this.createdAt = message.getCreatedAt();
        this.deletedAt = message.getDeletedAt();
    }

    private long id;

    private MemberResponse sender;

    private String text;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    public long getId() {
        return id;
    }

    public MemberResponse getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
}
