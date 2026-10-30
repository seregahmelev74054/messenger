package com.example.messenger.webSocket.dto;

import com.example.messenger.message.dto.MessageResponse;

public class NewMessageEvent extends Event {

    public NewMessageEvent(MessageResponse message) {
        super(EventType.NEW_MESSAGE);
        this.message = message;
    }

    private MessageResponse message;


    public MessageResponse getMessage() {
        return message;
    }

    public void setMessage(MessageResponse message) {
        this.message = message;
    }
}
