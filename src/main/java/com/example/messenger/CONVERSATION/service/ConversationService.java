package com.example.messenger.CONVERSATION.service;

import com.example.messenger.CONVERSATION.dto.CreateConversationRequest;
import com.example.messenger.CONVERSATION.dto.GetConversationResponse;
import com.example.messenger.CONVERSATION.dto.GetMyConversationsResponse;
import com.example.messenger.CONVERSATION.dto.MyConversation;
import com.example.messenger.CONVERSATION.entity.Conversation;
import com.example.messenger.CONVERSATION.entity.ConversationType;
import com.example.messenger.CONVERSATION.entity.ConversationVisibility;
import com.example.messenger.CONVERSATION.repository.ConversationRepository;

import com.example.messenger.MESSAGE.dto.MessageResponse;
import com.example.messenger.MESSAGE.entity.Message;
import com.example.messenger.MESSAGE.repository.MessageRepository;
import com.example.messenger.USER.entity.User;
import com.example.messenger.USER.repository.UserRepository;
import com.example.messenger.USER.service.CurrentUserProvider;

import com.example.messenger.MEMBER.entity.Member;
import com.example.messenger.EXCEPTION.*;
import com.example.messenger.MEMBER.repository.MemberRepository;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final CurrentUserProvider currentUserProvider;
    private final MessageRepository messageRepository;

    ConversationService(
            ConversationRepository conversationRepository,
            UserRepository userRepository,
            MemberRepository memberRepository,
            CurrentUserProvider currentUserProvider,
            MessageRepository messageRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.currentUserProvider = currentUserProvider;
        this.messageRepository = messageRepository;
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
    public GetMyConversationsResponse getMyConversations(Instant lastUpdatedAt, Long lastConversationId) {

        User currentUser = currentUserProvider.getCurrentUser();

        List<Member> members;

        if (lastUpdatedAt == null && lastConversationId == null)
            members = memberRepository.findActiveMember(currentUser.getId());
        else if (lastUpdatedAt != null && lastConversationId != null)
            members = memberRepository.findActiveMemberWithKeySet(currentUser.getId(), lastUpdatedAt, lastConversationId);
        else
            throw new UnprocessableRequestException("");

        if (members.isEmpty())
            throw new ConversationNotFoundException("");

        GetMyConversationsResponse response = new GetMyConversationsResponse();

        if (members.size() == 51) {
            response.setNextLastId(members.getLast().getConversation().getId());
            response.setNextLastUpdatedAt(members.getLast().getConversation().getUpdatedAt());

            members = members.subList(0, 50);
        }

        List<Long> conIds = new ArrayList<>();
        List<Long> memIds = new ArrayList<>();

        for (Member member : members) {
            conIds.add(member.getConversation().getId());
            memIds.add(member.getId());
        }

        List<Tuple> lastMessages = messageRepository.latestByConversations(conIds, memIds);

        List<Tuple> unreadCountList = messageRepository.unreadCountByConversations(memIds);
        Map<Long, Long> unreadCount =
                unreadCountList.stream()
                        .collect(Collectors.toMap(
                                t -> t.get(0, Long.class),
                                t -> t.get(1, Long.class)
                        ));


        List<MyConversation> myConversations = new ArrayList<>();

        for (int i = 0; i < members.size(); i++) {

            long conId = conIds.get(i);

            Tuple lastMessage = lastMessages.get(i);

            myConversations.add(
                    new MyConversation(
                            conId,
                            new MessageResponse(
                                    lastMessage.get(1, Long.class),
                                    lastMessage.get(2, Long.class),
                                    lastMessage.get(3, String.class),
                                    lastMessage.get(4, String.class),
                                    lastMessage.get(5, Instant.class),
                                    lastMessage.get(6, Instant.class)
                            ),
                            members.get(i).getConversation().getUpdatedAt(),
                            unreadCount.getOrDefault(conId, 0L)
                    )
            );
        }

        response.setConversations(myConversations);

        return response;
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
