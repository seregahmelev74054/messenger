package com.example.messenger.MESSAGE.dto;

import com.example.messenger.MEMBER.dto.MemberResponse;
import com.example.messenger.MESSAGE.entity.Message;

import java.time.Instant;

public class MessageResponse {

    public MessageResponse(Message message) {
        if (message != null) {
            this.id = message.getId();
            this.sender = new MemberResponse(message.getSender());
            this.text = message.getText();
            this.createdAt = message.getCreatedAt();
            this.deletedAt = message.getDeletedAt();
        }
        else {
            this.id = 0;
            this.sender = null;
            this.text = "no messages yet";
            this.createdAt = null;
            this.deletedAt = null;
        }
    }


    public MessageResponse(Long id, Long senderId, String userDN, String text, Instant createdAt, Instant deletedAt) {
        this.id = id != null ? id : 0;
        this.sender = senderId != null ? new MemberResponse(senderId, userDN) : null;
        this.text = text != null ? text : "no messages yet";
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
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
