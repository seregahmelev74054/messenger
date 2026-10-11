package com.example.messenger.webSocket.service;

import com.example.messenger.EXCEPTION.MemberNotFoundException;
import com.example.messenger.MEMBER.entity.Member;
import com.example.messenger.MEMBER.entity.MemberStatus;
import com.example.messenger.MEMBER.repository.MemberRepository;
import com.example.messenger.MEMBER.service.MemberService;
import com.example.messenger.webSocket.dto.UserTypingEvent;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ChatWebSocketService {

    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate messagingTemplate;

    ChatWebSocketService(
            MemberRepository memberRepository,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.memberRepository = memberRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void userTyping(
            long conversationId,
            Principal principal
    ) {

        long userId = Long.parseLong(principal.getName());

        Member member = memberRepository.findByUserIdAndConversationId(userId, conversationId)
                .orElseThrow(() -> new MemberNotFoundException(""));

        if (member.getStatus().equals(MemberStatus.ACTIVE)) {

            messagingTemplate.convertAndSend(
                    "/topic/conversations/" + conversationId,
                    new UserTypingEvent(userId, conversationId)
            );

        }
    }

}
