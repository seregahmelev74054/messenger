package com.example.messenger.CONVERSATION.entity;

import jakarta.persistence.*;

import java.time.Instant;


@Entity
@Table(
        name = "conversations",
        uniqueConstraints = {@UniqueConstraint(columnNames = "directValue")}
)
public class Conversation {

    public Conversation() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private ConversationType type;

    private String directValue;

    @Enumerated(EnumType.STRING)
    private ConversationVisibility visibility;

    private Instant createdAt;

    private Instant deletedAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }

    private Instant updatedAt;

    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }


    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ConversationType getType() {
        return type;
    }

    public String getDirectValue() {
        return directValue;
    }

    public ConversationVisibility getVisibility() {
        return visibility;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(ConversationType type) {
        this.type = type;
    }

    public void setDirectValue(String directValue) {
        this.directValue = directValue;
    }

    public void setVisibility(ConversationVisibility visibility) {
        this.visibility = visibility;
    }
}
