package com.example.messenger.MEMBER.repository;

import com.example.messenger.CONVERSATION.entity.Conversation;
import com.example.messenger.MEMBER.entity.Member;
import com.example.messenger.USER.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
