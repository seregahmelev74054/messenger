package com.example.messenger.MESSAGE.repository;

import com.example.messenger.CONVERSATION.entity.Conversation;
import com.example.messenger.MESSAGE.entity.Message;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
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

}
