package com.example.messenger.message.dto;

import com.example.messenger.message.entity.Message;

public class LastMessageByConversation {

    LastMessageByConversation(Message message) {
        this.conId = message.getConversation().getId();
        this.message = new MessageResponse(message);
    }

    private long conId;
    private MessageResponse message;


}
