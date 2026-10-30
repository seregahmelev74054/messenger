package com.example.messenger.member.service;

import com.example.messenger.conversation.entity.Conversation;
import com.example.messenger.conversation.entity.ConversationType;
import com.example.messenger.conversation.entity.ConversationVisibility;
import com.example.messenger.conversation.repository.ConversationRepository;
import com.example.messenger.exception.*;
import com.example.messenger.member.entity.Member;
import com.example.messenger.member.entity.MemberRole;
import com.example.messenger.member.entity.MemberStatus;
import com.example.messenger.member.repository.MemberRepository;
import com.example.messenger.message.entity.Message;
import com.example.messenger.message.repository.MessageRepository;
import com.example.messenger.user.entity.User;
import com.example.messenger.user.repository.UserRepository;
import com.example.messenger.user.service.CurrentUserProvider;
import com.example.messenger.webSocket.dto.MessageReadEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final CurrentUserProvider currentUserProvider;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    public MemberService(
            MemberRepository memberRepository,
            CurrentUserProvider currentUserProvider,
            MessageRepository messageRepository,
            SimpMessagingTemplate messagingTemplate,
            ConversationRepository conversationRepository,
            UserRepository userRepository
    ) {
        this.memberRepository = memberRepository;
        this.currentUserProvider = currentUserProvider;
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
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

            MessageReadEvent event = new MessageReadEvent(member.getId(), lastReadMessageId);

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            messagingTemplate.convertAndSend(
                                    "/topic/conversations/" + conversationId,
                                    event
                            );
                        }
                    }
            );

            return;
        }

        Message oldMessage = oldMessageOptional.get();

        if (
                message.getCreatedAt().isBefore(oldMessage.getCreatedAt())
                || (message.getCreatedAt().equals(oldMessage.getCreatedAt()) && message.getId() < oldMessage.getId())
        )
            throw new UnprocessableRequestException("");

        member.setLastReadMessageId(lastReadMessageId);

        MessageReadEvent event = new MessageReadEvent(member.getId(), lastReadMessageId);

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        messagingTemplate.convertAndSend(
                                "/topic/conversations/" + conversationId,
                                event
                        );
                    }
                }
        );
    }

    @Transactional
    public void inviteMember(
            long addingUserId,
            long conversationId
    ){
        User currentUser = currentUserProvider.getCurrentUser();

        Member currentMember = memberRepository.findByUserIdAndConversationId(currentUser.getId(), conversationId)
                .orElseThrow(() -> new MemberNotFoundException(""));

        if (currentMember.getStatus() != MemberStatus.ACTIVE)
            throw new AccessDeniedException("");

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException(""));

        if ((conversation.getDeletedAt() == null) && (conversation.getType() == ConversationType.GROUP) && ((conversation.getVisibility() == ConversationVisibility.OPEN) || (currentMember.getRole() != MemberRole.MEMBER))) {

            Optional<User> addingUser = userRepository.findById(addingUserId);

            if (addingUser.isPresent()) {

                Optional<Member> addingUserMember = memberRepository.findByUserIdAndConversationId(addingUserId, conversationId);

                if (addingUserMember.isEmpty()) {

                    Member addingMember = new Member();
                    addingMember.setUser(addingUser.get());
                    addingMember.setConversation(conversation);
                    addingMember.setStatus(MemberStatus.ACTIVE);
                    addingMember.setRole(MemberRole.MEMBER);

                    memberRepository.save(addingMember);
                }
                else if (addingUserMember.get().getStatus() != MemberStatus.BANNED) {
                    addingUserMember.get().setStatus(MemberStatus.ACTIVE);
                }
                else if (addingUserMember.get().getStatus() == MemberStatus.BANNED) {
                    if (currentMember.getRole() != MemberRole.MEMBER)
                        addingUserMember.get().setStatus(MemberStatus.ACTIVE);
                    else
                        throw new AccessDeniedException("");
                }

            }
            else
                throw new UserDoesNotExistException("");

        }
        else
            throw new AccessDeniedException("");

    }

    @Transactional
    public void addMember(
            long conversationId
    ){
        User currentUser = currentUserProvider.getCurrentUser();

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException(""));

        if ((conversation.getDeletedAt() == null) && (conversation.getType() == ConversationType.GROUP) && (conversation.getVisibility() == ConversationVisibility.OPEN)) {

            Optional<Member> currentMember = memberRepository.findByUserIdAndConversationId(currentUser.getId(), conversationId);

            if (currentMember.isEmpty()) {

                Member addingMember = new Member();
                addingMember.setUser(currentUser);
                addingMember.setConversation(conversation);
                addingMember.setStatus(MemberStatus.ACTIVE);
                addingMember.setRole(MemberRole.MEMBER);

                memberRepository.save(addingMember);

            }
            else if (currentMember.get().getStatus() == MemberStatus.LEFT)
                currentMember.get().setStatus(MemberStatus.ACTIVE);

            else if (currentMember.get().getStatus() == MemberStatus.BANNED)
                throw new AccessDeniedException("");

        }
        else
            throw new AccessDeniedException("");

    }

    @Transactional
    public void leaveConversation(
            long conversationId
    ) {
        User currentUser = currentUserProvider.getCurrentUser();

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException(""));

        Member member = memberRepository.findByUserIdAndConversationId(currentUser.getId(), conversationId)
                .orElseThrow(() -> new MemberNotFoundException(""));

        if ((conversation.getDeletedAt() == null) && (conversation.getType() == ConversationType.GROUP)) {

            if (member.getRole() != MemberRole.OWNER)
                member.setStatus(MemberStatus.LEFT);

            else
                throw new UnprocessableRequestException("Conversation must not be without owner");

        }
    }


}
