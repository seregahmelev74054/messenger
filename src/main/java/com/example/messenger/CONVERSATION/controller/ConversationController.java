package com.example.messenger.CONVERSATION.controller;

import com.example.messenger.CONVERSATION.dto.CreateConversationRequest;
import com.example.messenger.CONVERSATION.dto.GetConversationResponse;
import com.example.messenger.CONVERSATION.service.ConversationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping("/api/v1/conversations")
    public ResponseEntity<Long> createConversation(@Valid @RequestBody CreateConversationRequest request) {

        Long response = conversationService.createConversation(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/me/conversations")
    public Page<GetConversationResponse> getMyConversations(Pageable pageable) {
        return conversationService.getMyConversations(pageable);
    }

    @GetMapping("/api/v1/conversations")
    public Page<GetConversationResponse> getAllConversations(Pageable pageable) {
        return conversationService.getConversations(pageable);
    }

    @GetMapping("/api/v1/conversations/{conversationId}")
    public ResponseEntity<GetConversationResponse> getConversation(@PathVariable long conversationId) {
        return ResponseEntity.ok(conversationService.getConversation(conversationId));
    }
}
