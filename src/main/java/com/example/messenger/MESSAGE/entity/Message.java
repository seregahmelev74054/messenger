package com.example.messenger.MESSAGE.entity;

import com.example.messenger.CONVERSATION.entity.Conversation;
import com.example.messenger.MEMBER.entity.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(
        name = "messages",
        //uniqueConstraints = {@UniqueConstraint(columnNames = "login"), @UniqueConstraint(columnNames = "email")}
        indexes = {
                @Index(name = "idx_message_conversationId_createdAt_id", columnList = "conversation_id, created_at, id")
        }
)
public class Message {

    public Message() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_member_id", nullable = false)
    private Member sender;

    private String text;

    private LocalDateTime createdAt;
    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }

    private LocalDateTime deletedAt;


    public long getId() {
        return id;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public Member getSender() {
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


    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public void setSender(Member sender) {
        this.sender = sender;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
