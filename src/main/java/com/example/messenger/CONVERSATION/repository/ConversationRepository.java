package com.example.messenger.CONVERSATION.repository;

import com.example.messenger.CONVERSATION.entity.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByDirectValue(String directValue);

    @Query("SELECT c FROM Conversation c " +
            "INNER JOIN Member m " +
            "ON c.id = m.conversation.id " +
            "WHERE m.user.id = :userId " +
            "AND c.deletedAt IS NULL " +
            "AND m.status = ACTIVE " +
            "ORDER BY c.updatedAt DESC")
    Page<Conversation> findByActiveMember(@Param("userId") long userId, Pageable pageable);

    @Query("SELECT c FROM Conversation c " +
            "WHERE c.deletedAt IS NULL " +
            "AND (c.visibility = OPEN " +
            "OR EXISTS (SELECT 1 FROM Member m " +
            "WHERE m.conversation.id = c.id " +
            "AND m.user.id = :userId " +
            "AND m.status = ACTIVE)) " +
            "ORDER BY c.updatedAt DESC")
    Page<Conversation> findAllAccessFully(@Param("userId") long userId, Pageable pageable);

    @Query("SELECT c FROM Conversation c " +
            "LEFT JOIN Member m " +
            "ON c.id = m.conversation.id " +
            "AND m.user.id = :userId " +
            "WHERE " +
            "c.id = :conId " +
            "AND (m.status = ACTIVE " +
            "OR c.visibility = OPEN) " +
            "AND c.deletedAt IS NULL " +
            "ORDER BY c.updatedAt DESC")
    Optional<Conversation> findAccessFullyById(@Param("conId") long conId, @Param("userId") long userId);
}
