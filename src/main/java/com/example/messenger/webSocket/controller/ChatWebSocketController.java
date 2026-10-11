package com.example.messenger.webSocket.controller;

import com.example.messenger.webSocket.service.ChatWebSocketService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatWebSocketController {

    private final ChatWebSocketService chatWebSocketService;

    ChatWebSocketController(
            ChatWebSocketService chatWebSocketService
    ) {
        this.chatWebSocketService = chatWebSocketService;
    }

    @MessageMapping("/conversations/{conversationId}/typing")
    public void userTyping(
            @DestinationVariable long conversationId,
            Principal principal
    ) {

        chatWebSocketService.userTyping(conversationId, principal);

    }

}
