package com.example.messenger.MESSAGE.repository;

import com.example.messenger.MEMBER.entity.Member;
import com.example.messenger.MESSAGE.entity.Message;
import com.example.messenger.MESSAGE.entity.MessageDeletion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageDeletionRepository extends JpaRepository<MessageDeletion, Long> {

    boolean existsByMessageAndMember(Message message, Member member);

}
