package com.example.messenger;

import com.example.messenger.conversation.entity.Conversation;
import com.example.messenger.conversation.entity.ConversationType;
import com.example.messenger.conversation.entity.ConversationVisibility;
import com.example.messenger.conversation.repository.ConversationRepository;
import com.example.messenger.member.entity.Member;
import com.example.messenger.member.repository.MemberRepository;
import com.example.messenger.message.entity.Message;
import com.example.messenger.message.repository.MessageRepository;
import com.example.messenger.user.entity.User;
import com.example.messenger.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringBootTest
public class DbInvariantTest extends TestPostgresDb {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MessageRepository messageRepository;


    @Test
    void when_messageMemberConversation_equals_messageConversation_then_allRight() {

        User user = new User();
        user.setEmail("email");
        user.setLogin("login");
        user.setDisplayName("displayName");
        user.setPasswordHash("passwordHash");
        userRepository.save(user);


        Conversation rightConversation = new Conversation();
        rightConversation.setVisibility(ConversationVisibility.OPEN);
        rightConversation.setType(ConversationType.GROUP);
        rightConversation.setTitle("title");
        conversationRepository.save(rightConversation);

        Conversation wrongConversation = new Conversation();
        wrongConversation.setVisibility(ConversationVisibility.OPEN);
        wrongConversation.setType(ConversationType.GROUP);
        wrongConversation.setTitle("title");
        conversationRepository.save(wrongConversation);

        Member member = new Member();
        member.setUser(user);
        member.setConversation(rightConversation);
        memberRepository.save(member);

        Message message = new Message();
        message.setSender(member);
        message.setConversation(rightConversation);

        assertEquals(
                message,
                messageRepository.saveAndFlush(message)
        );
    }

    @Test
    void when_messageMemberConversation_does_not_equal_messageConversation_then_error() {

        User user = new User();
        user.setEmail("email");
        user.setLogin("login");
        user.setDisplayName("displayName");
        user.setPasswordHash("passwordHash");
        userRepository.save(user);


        Conversation rightConversation = new Conversation();
        rightConversation.setVisibility(ConversationVisibility.OPEN);
        rightConversation.setType(ConversationType.GROUP);
        rightConversation.setTitle("title");
        conversationRepository.save(rightConversation);

        Conversation wrongConversation = new Conversation();
        wrongConversation.setVisibility(ConversationVisibility.OPEN);
        wrongConversation.setType(ConversationType.GROUP);
        wrongConversation.setTitle("title");
        conversationRepository.save(wrongConversation);

        Member member = new Member();
        member.setUser(user);
        member.setConversation(rightConversation);
        memberRepository.save(member);

        Message message = new Message();
        message.setSender(member);
        message.setConversation(wrongConversation);

        assertThrowsExactly(
                DataIntegrityViolationException.class,
                () -> messageRepository.saveAndFlush(message)
        );


    }



}
