package com.example.messenger.MESSAGE.repository;

import com.example.messenger.MESSAGE.entity.Message;
import com.example.messenger.MEMBER.entity.Member;

public interface GetMessageWithMemberForDeletion {

    Message getMessage();
    Member getMember();

}
