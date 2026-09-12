package com.example.messenger.CONVERSATION.service;

import com.example.messenger.CONVERSATION.dto.CreateConversationRequest;
import com.example.messenger.CONVERSATION.dto.GetConversationResponse;
import com.example.messenger.CONVERSATION.entity.Conversation;
import com.example.messenger.CONVERSATION.entity.ConversationType;
import com.example.messenger.CONVERSATION.entity.ConversationVisibility;
import com.example.messenger.CONVERSATION.repository.ConversationRepository;

import com.example.messenger.USER.entity.User;
import com.example.messenger.USER.repository.UserRepository;
import com.example.messenger.USER.service.CurrentUserProvider;

import com.example.messenger.MEMBER.entity.Member;
import com.example.messenger.EXCEPTION.*;
import com.example.messenger.MEMBER.repository.MemberRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final CurrentUserProvider currentUserProvider;

    ConversationService(
            ConversationRepository conversationRepository,
            UserRepository userRepository,
            MemberRepository memberRepository,
            CurrentUserProvider currentUserProvider
    ) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional
    public long createConversation(CreateConversationRequest request) {

        User currentUser = currentUserProvider.getCurrentUser();
        long currentUserId = currentUser.getId();

        if (request.getType() == ConversationType.DIRECT) {

            if (request.getVisibility() != ConversationVisibility.PRIVATE)
                throw new UnprocessableRequestException("Request is conflicting");

            long companionId = request.getCompanionId()
                    .orElseThrow(() -> new UnprocessableRequestException("Request is uncompleted"));

            User companion = userRepository.findById(companionId)
                    .orElseThrow(() -> new UserDoesNotExistException("Companion does not exist"));

            String directValue = Math.min(currentUserId, companionId) +
                    ":" +
                    Math.max(currentUserId, companionId);

            Optional<Conversation> existConversation = conversationRepository.findByDirectValue(directValue);

            if (existConversation.isPresent() && (existConversation.get().getDeletedAt() == null)) {
                return existConversation.get().getId();
            }

            Conversation conversation = new Conversation();

            conversation.setTitle(request.getTitle());
            conversation.setType(ConversationType.DIRECT);
            conversation.setDirectValue(directValue);
            conversation.setVisibility(ConversationVisibility.PRIVATE);

            Member currentUserMember = new Member();
            currentUserMember.setUser(currentUser);
            currentUserMember.setConversation(conversation);

            if (companionId == currentUserId) {
                conversationRepository.save(conversation);
                memberRepository.save(currentUserMember);
                return conversation.getId();
            }

            Member companionMember = new Member();
            companionMember.setUser(companion);
            companionMember.setConversation(conversation);

            conversationRepository.save(conversation);
            memberRepository.save(currentUserMember);
            memberRepository.save(companionMember);

            return conversation.getId();

        }
        if (request.getType() == ConversationType.GROUP) {
            Conversation conversation = new Conversation();

            conversation.setTitle(request.getTitle());
            conversation.setType(ConversationType.GROUP);
            conversation.setVisibility(request.getVisibility());

            Member currentUserMember = new Member();
            currentUserMember.setUser(currentUser);
            currentUserMember.setConversation(conversation);

            conversationRepository.save(conversation);
            memberRepository.save(currentUserMember);

            return conversation.getId();
        }

        throw new UnprocessableRequestException("Request is conflicting");
    }

    @Transactional(readOnly = true)
    public Page<GetConversationResponse> getMyConversations(Pageable pageable) {

        User currentUser = currentUserProvider.getCurrentUser();

        Page<Conversation> conversations = conversationRepository.findByActiveMember(currentUser.getId(), pageable);

        return conversations.map(GetConversationResponse::new);
    }

    @Transactional(readOnly = true)
    public Page<GetConversationResponse> getConversations(Pageable pageable) {

        User currentUser = currentUserProvider.getCurrentUser();

        Page<Conversation> conversations = conversationRepository.findAllAccessFully(currentUser.getId(), pageable);

        return conversations.map(GetConversationResponse::new);
    }

    @Transactional(readOnly = true)
    public GetConversationResponse getConversation(long conversationId) {

        User currentUser = currentUserProvider.getCurrentUser();

        Conversation conversation = conversationRepository.findAccessFullyById(conversationId, currentUser.getId())
                .orElseThrow(() -> new ConversationNotFoundException(""));

        return new GetConversationResponse(conversation);
    }

}
