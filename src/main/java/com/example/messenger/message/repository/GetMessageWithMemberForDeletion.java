package com.example.messenger.message.repository;

import com.example.messenger.message.entity.Message;
import com.example.messenger.member.entity.Member;

public interface GetMessageWithMemberForDeletion {

    Message getMessage();
    Member getMember();

}
