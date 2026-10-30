package com.example.messenger.member.entity;


import com.example.messenger.conversation.entity.Conversation;
import com.example.messenger.user.entity.User;
import jakarta.persistence.*;


@Entity
@Table(
        name = "members",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "conversation_id"}),
                @UniqueConstraint(columnNames = {"id", "conversation_id"})

        },
        indexes = {
                @Index(name = "idx_members_conversation_id", columnList = "conversation_id"),
                @Index(name = "idx_members_user_id", columnList = "user_id")
        }
)
public class Member {

    public Member() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @PrePersist
    void onCreate() {
        if (status == null) status = MemberStatus.ACTIVE;
        if (role == null) role = MemberRole.MEMBER;
        if (lastReadMessageId == null) lastReadMessageId = 0L;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    private Long lastReadMessageId;

    public Long getLastReadMessageId() {
        return lastReadMessageId;
    }

    public void setLastReadMessageId(Long lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }

    public long getId() {
        return id;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public MemberRole getRole() {
        return role;
    }

    public void setRole(MemberRole role) {
        this.role = role;
    }
}
