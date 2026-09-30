package com.example.messenger.MESSAGE.dto;

import com.example.messenger.MESSAGE.entity.Message;

public class LastMessageByConversation {

    LastMessageByConversation(Message message) {
        this.conId = message.getConversation().getId();
        this.message = new MessageResponse(message);
    }

    private long conId;
    private MessageResponse message;


}
