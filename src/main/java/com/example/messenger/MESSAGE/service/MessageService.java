package com.example.messenger.MESSAGE.service;

import com.example.messenger.CONVERSATION.entity.Conversation;
import com.example.messenger.CONVERSATION.entity.ConversationVisibility;
import com.example.messenger.CONVERSATION.repository.ConversationRepository;
import com.example.messenger.MESSAGE.dto.CreateMessageRequest;
import com.example.messenger.MESSAGE.dto.GetMessagesResponse;
import com.example.messenger.MESSAGE.dto.MessageResponse;
import com.example.messenger.MESSAGE.entity.Message;
import com.example.messenger.MESSAGE.entity.MessageDeletion;
import com.example.messenger.MESSAGE.repository.GetMessageWithMemberForDeletion;
import com.example.messenger.MESSAGE.repository.MessageDeletionRepository;
import com.example.messenger.MESSAGE.repository.MessageRepository;
import com.example.messenger.USER.entity.User;
import com.example.messenger.USER.service.CurrentUserProvider;
import com.example.messenger.MEMBER.entity.Member;
import com.example.messenger.MEMBER.entity.MemberStatus;
import com.example.messenger.EXCEPTION.AccessDeniedException;
import com.example.messenger.EXCEPTION.ConversationNotFoundException;
import com.example.messenger.EXCEPTION.MessageNotFoundException;
import com.example.messenger.EXCEPTION.UnprocessableRequestException;
import com.example.messenger.MEMBER.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final CurrentUserProvider currentUserProvider;
    private final MemberRepository memberRepository;
    private final MessageDeletionRepository messageDeletionRepository;

    public MessageService(
            MessageRepository messageRepository,
            ConversationRepository conversationRepository,
            CurrentUserProvider currentUserProvider,
            MemberRepository memberRepository,
            MessageDeletionRepository messageDeletionRepository
    ) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.currentUserProvider = currentUserProvider;
        this.memberRepository = memberRepository;
        this.messageDeletionRepository = messageDeletionRepository;
    }

    @Transactional
    public void createMessage(long conversationId, CreateMessageRequest request) {

        User currentUser = currentUserProvider.getCurrentUser();

        Member member = memberRepository.findByUserIdAndConversationId(currentUser.getId(), conversationId)
                .orElseThrow( () -> new AccessDeniedException("") );

        if (member.getStatus() != MemberStatus.ACTIVE)
            throw new AccessDeniedException("");

        Message message = new Message();
        message.setConversation(member.getConversation());
        message.setSender(member);
        message.setText(request.getText());

        messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public GetMessagesResponse getMessages(long conversationId, Instant lastCreatedAt, Long lastMessageId) {

        User currentUser = currentUserProvider.getCurrentUser();

        Optional<Member> member = memberRepository.findByUserIdAndConversationId(currentUser.getId(), conversationId);

        Optional<Conversation> conversation = conversationRepository.findById(conversationId);

        if (
                conversation.isEmpty()
                || conversation.get().getDeletedAt() != null
                || (
                        conversation.get().getVisibility() != ConversationVisibility.OPEN
                                && (member.isEmpty() || member.get().getStatus() != MemberStatus.ACTIVE)
                )
        )
            throw new ConversationNotFoundException("");

        List<Message> messages;

        if (lastCreatedAt == null && lastMessageId == null) {
            if (member.isEmpty())
                messages = messageRepository.findLastestByConversation(conversationId);
            else
                messages = messageRepository.findLastestByConversationForMember(member.get().getId(), conversationId);
        }
        else if (lastCreatedAt != null && lastMessageId != null) {
            if (member.isEmpty())
                messages = messageRepository.findByConversationWithKeySet(conversationId, lastCreatedAt, lastMessageId);
            else
                messages = messageRepository.findByConversationWithKeySetForMember(member.get().getId(), conversationId, lastCreatedAt, lastMessageId);
        }
        else
            throw new UnprocessableRequestException("Request is conflicting");

        boolean hasMore = (messages.size() == 51);

        if (hasMore)
            messages = messages.subList(0, 50);

        List<MessageResponse> messagesResponse =
                messages
                .stream()
                .map(MessageResponse::new)
                .toList();

        return new GetMessagesResponse(
                messagesResponse,
                hasMore,
                (hasMore ? messagesResponse.getLast().getCreatedAt() : null),
                (hasMore ? messagesResponse.getLast().getId() : null)
        );
    }

    @Transactional
    public void deleteMessages(long conversationId, long messageId) {

        User currentUser = currentUserProvider.getCurrentUser();

        Message message = messageRepository.findForDelete(currentUser.getId(), conversationId, messageId)
                .orElseThrow(() -> new MessageNotFoundException(""));

        message.setText("");
        message.setDeletedAt(Instant.now());

        messageRepository.save(message);
    }

    @Transactional
    public void deleteForMeMessages(long conversationId, long messageId) {

        User currentUser = currentUserProvider.getCurrentUser();

        GetMessageWithMemberForDeletion messageMember = messageRepository.findForDeleteForMe(currentUser.getId(), conversationId, messageId)
                .orElseThrow(() -> new MessageNotFoundException(""));

        MessageDeletion md = new MessageDeletion();
        md.setMessage(messageMember.getMessage());
        md.setMember(messageMember.getMember());

        messageDeletionRepository.save(md);
    }
}
