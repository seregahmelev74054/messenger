package com.example.messenger.message.controller;

import com.example.messenger.message.dto.CreateMessageRequest;
import com.example.messenger.message.dto.GetMessagesResponse;
import com.example.messenger.message.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/api/v1/conversations/{conversationId}/messages")
    public ResponseEntity<Void> createMessage(
            @PathVariable long conversationId,
            @Valid @RequestBody CreateMessageRequest request
    ) {

        messageService.createMessage(conversationId, request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/api/v1/conversations/{conversationId}/messages")
    public ResponseEntity<GetMessagesResponse> getMessage(
            @PathVariable long conversationId,
            @RequestParam(name = "lastCreatedAt", required = false) Instant lastCreatedAt,
            @RequestParam(name = "lastId", required = false) Long lastMessageId
    ) {

        return ResponseEntity.ok(messageService.getMessages(conversationId, lastCreatedAt, lastMessageId));
    }

    @DeleteMapping("/api/v1/conversations/{conversationId}/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable long conversationId,
            @PathVariable long messageId
    ) {

        messageService.deleteMessages(conversationId, messageId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/api/v1/conversations/{conversationId}/messages/{messageId}/me")
    public ResponseEntity<Void> deleteForMeMessage(
            @PathVariable long conversationId,
            @PathVariable long messageId
    ) {

        messageService.deleteForMeMessages(conversationId, messageId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
