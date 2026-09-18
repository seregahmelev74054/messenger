package com.example.messenger.MESSAGE.dto;

import com.example.messenger.MEMBER.dto.MemberResponse;
import com.example.messenger.MESSAGE.entity.Message;

import java.time.Instant;

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

    private Instant createdAt;

    private Instant deletedAt;

    public long getId() {
        return id;
    }

    public MemberResponse getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
