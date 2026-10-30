package com.example.messenger.message.repository;

import com.example.messenger.member.entity.Member;
import com.example.messenger.message.entity.Message;
import com.example.messenger.message.entity.MessageDeletion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageDeletionRepository extends JpaRepository<MessageDeletion, Long> {

    boolean existsByMessageAndMember(Message message, Member member);

}
