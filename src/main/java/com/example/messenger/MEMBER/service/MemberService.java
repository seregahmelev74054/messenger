package com.example.messenger.MEMBER.service;

import com.example.messenger.EXCEPTION.MemberNotFoundException;
import com.example.messenger.EXCEPTION.MessageNotFoundException;
import com.example.messenger.EXCEPTION.UnprocessableRequestException;
import com.example.messenger.MEMBER.entity.Member;
import com.example.messenger.MEMBER.repository.MemberRepository;
import com.example.messenger.MESSAGE.entity.Message;
import com.example.messenger.MESSAGE.repository.MessageRepository;
import com.example.messenger.USER.entity.User;
import com.example.messenger.USER.service.CurrentUserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final CurrentUserProvider currentUserProvider;
    private final MessageRepository messageRepository;

    public MemberService(
            MemberRepository memberRepository,
            CurrentUserProvider currentUserProvider,
            MessageRepository messageRepository
    ) {
        this.memberRepository = memberRepository;
        this.currentUserProvider = currentUserProvider;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public void patchLastReadMessageId(
            long conversationId,
            long lastReadMessageId
    ) {

        User currentUser = currentUserProvider.getCurrentUser();

        Message message = messageRepository.findById(lastReadMessageId)
                .orElseThrow(() -> new MessageNotFoundException(""));

        Member member = memberRepository.findByUserIdAndConversationId(currentUser.getId(), conversationId)
                .orElseThrow(() -> new MemberNotFoundException(""));

        if (message.getConversation().getId() != member.getConversation().getId())
            throw new MessageNotFoundException("");

        Optional<Message> oldMessageOptional = messageRepository.findById(member.getLastReadMessageId());

        if (oldMessageOptional.isEmpty()) {
            member.setLastReadMessageId(lastReadMessageId);
            return;
        }

        Message oldMessage = oldMessageOptional.get();

        if (
                message.getCreatedAt().isBefore(oldMessage.getCreatedAt())
                || (message.getCreatedAt().equals(oldMessage.getCreatedAt()) && message.getId() < oldMessage.getId())
        )
            throw new UnprocessableRequestException("");

        member.setLastReadMessageId(lastReadMessageId);
    }

}
