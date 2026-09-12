package com.example.messenger.MESSAGE.dto;

import com.example.messenger.MESSAGE.entity.Message;
import com.example.messenger.MESSAGE.entity.MessageDeletion;

public interface GetMessagesWithDeletion {

    Message getMessage();
    MessageDeletion getMessageDeletion();

}
