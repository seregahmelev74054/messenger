package com.example.messenger.message.dto;

import com.example.messenger.message.entity.Message;
import com.example.messenger.message.entity.MessageDeletion;

public interface GetMessagesWithDeletion {

    Message getMessage();
    MessageDeletion getMessageDeletion();

}
