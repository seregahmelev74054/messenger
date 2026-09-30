package com.example.messenger.MESSAGE.repository;

import com.example.messenger.CONVERSATION.entity.Conversation;
import com.example.messenger.MESSAGE.entity.Message;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByConversation(Conversation conversation, Pageable pageable);

    @Query(
            nativeQuery = true,
            value =
                    "select * " +
                    "from messages m " +
                    "where not exists( " +
                            "select 1 " +
                            "from message_deletions md " +
                            "where md.message_id = m.id and md.member_id = :memId " +
                    ") " +
                    "and m.conversation_id = :conId " +
                    "and (m.created_at, m.id) < (:lastCreatedAt, :lastMessageId) " +
                    "order by  m.created_at desc, m.id desc " +
                    "limit 51"
    )
    List<Message> findByConversationWithKeySetForMember(@Param("memId") long memberId, @Param("conId") long conId, @Param("lastCreatedAt") Instant lastCreatedAt, @Param("lastMessageId") long lastMessageId);

    @Query(
            nativeQuery = true,
            value =
                    "select * " +
                    "from messages m " +
                    "where not exists( " +
                            "select 1 " +
                            "from message_deletions md " +
                            "where md.message_id = m.id and md.member_id = :memId " +
                    ") " +
                    "and m.conversation_id = :conId " +
                    "order by  m.created_at desc, m.id desc " +
                    "limit 51"
    )
    List<Message> findLastestByConversationForMember(@Param("memId") long memberId, @Param("conId") long conId);


    @Query(
            nativeQuery = true,
            value =
                    "select * " +
                    "from messages m " +
                    "where " +
                    "m.conversation_id = :conId " +
                    "and (m.created_at, m.id) < (:lastCreatedAt, :lastMessageId) " +
                    "order by  m.created_at desc, m.id desc " +
                    "limit 51"
    )
    List<Message> findByConversationWithKeySet(@Param("conId") long conId, @Param("lastCreatedAt") Instant lastCreatedAt, @Param("lastMessageId") long lastMessageId);

    @Query(
            nativeQuery = true,
            value =
                    "select * " +
                    "from messages m " +
                    "where " +
                    "m.conversation_id = :conId " +
                    "order by  m.created_at desc, m.id desc " +
                    "limit 51"
    )
    List<Message> findLastestByConversation(@Param("conId") long conId);

    @Query(
            value = "select mess " +
                    "from Message mess " +
                    "join Member mem " +
                    "on mem.user.id = :userId " +
                    "and mem.conversation.id = :conId " +
                    "and mem.status = ACTIVE " +
                    "where " +
                    "mess.id = :messId " +
                    "and mess.deletedAt is null " +
                    "and (mem = mess.sender)"
    )
    Optional<Message> findForDelete(@Param("userId") long userId , @Param("conId") long conversationId, @Param("messId") long messageId);

    @Query(
            value = "select mess as message, mem as member " +
                    "from Message mess " +
                    "join Member mem " +
                    "on mem.user.id = :userId " +
                    "and mem.conversation.id = :conId " +
                    "and mem.status = ACTIVE " +
                    "where " +
                    "mess.id = :messId " +
                    "and mess.conversation.id = :conId " +
                    "and not exists(select md from MessageDeletion md where md.message = mess and md.member = mem)"
    )
    Optional<GetMessageWithMemberForDeletion> findForDeleteForMe(@Param("userId") long userId , @Param("conId") long conversationId, @Param("messId") long messageId);


    @Query(
    nativeQuery = true,
    value = """
        SELECT
            c.id,
            m.id,
            m.sender_member_id,
            u.display_name,
            m.text,
            m.created_at,
            m.deleted_at
        FROM conversations c
        LEFT JOIN LATERAL (
            SELECT m.*
            FROM messages m
            WHERE m.conversation_id = c.id
              AND NOT EXISTS (
                  SELECT 1
                  FROM message_deletions md
                  WHERE md.message_id = m.id
                    AND md.member_id IN :memIds
              )
            ORDER BY m.created_at DESC, m.id DESC
            LIMIT 1
        ) m ON true
        LEFT JOIN members mem
            ON mem.id = m.sender_member_id
        LEFT JOIN users u
            ON u.id = mem.user_id
        WHERE c.id IN :conIds
        ORDER BY c.updated_at DESC, c.id DESC;
    """
    )
    List<Tuple> latestByConversations(
            @Param("conIds") List<Long> conIds,
            @Param("memIds") List<Long> memIds
    );

    @Query("""
    select mem.conversation.id, count(mes)
    from Member mem
    left join Message mes
        on mes.conversation.id = mem.conversation.id
        and mes.id > mem.lastReadMessageId
        and not exists (
            select 1
            from MessageDeletion md
            where md.message.id = mes.id
              and md.member.id = mem.id
        )
    where mem.id in :memIds
    group by mem.conversation.id
""")
    List<Tuple> unreadCountByConversations(
            @Param("memIds") List<Long> memIds
    );
}
