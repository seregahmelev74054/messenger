package com.example.messenger.MEMBER.repository;

import com.example.messenger.CONVERSATION.entity.Conversation;
import com.example.messenger.MEMBER.entity.Member;
import com.example.messenger.USER.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserAndConversation(User user, Conversation conversation);

    List<Member> findByConversation(Conversation conversation);

    @Query(
            "SELECT m FROM Member m " +
            "WHERE m.user.id = :userId " +
            "AND m.conversation.id = :conId "

    )
    Optional<Member> findByUserIdAndConversationId(@Param("userId") long userId, @Param("conId") long conId);

    @Query("SELECT m FROM Member m " +
            "INNER JOIN Conversation c " +
            "ON c.id = m.conversation.id " +
            "WHERE m.user.id = :userId " +
            "AND c.deletedAt IS NULL " +
            "AND m.status = ACTIVE " +
            "ORDER BY c.updatedAt DESC, c.id desc " +
            "limit 51")
    List<Member> findActiveMember(@Param("userId") long userId);

    @Query(
            nativeQuery = true,

            value = "SELECT * FROM members m " +
                    "INNER JOIN conversations c " +
                    "ON c.id = m.conversation_id " +
                    "WHERE m.user_id = :userId " +
                    "AND c.deleted_at IS NULL " +
                    "AND m.status = ACTIVE " +
                    "and (c.updated_at, c.id) < (:lastUpdatedAt, :lastConId) " +
                    "ORDER BY c.updated_at DESC, c.id desc " +
                    "limit 51")
    List<Member> findActiveMemberWithKeySet(@Param("userId") long userId, @Param("lastUpdatedAt") Instant lastUpdatedAt, @Param("lastConId") long lastConId);


}
