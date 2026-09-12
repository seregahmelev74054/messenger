package com.example.messenger.MESSAGE.entity;

import com.example.messenger.MEMBER.entity.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "message_deletions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"message_id", "member_id"})
        }
)
public class MessageDeletion {

    public MessageDeletion() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private LocalDateTime deletedAt;
    @PrePersist
    void onCreate() {
        deletedAt = LocalDateTime.now();
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
