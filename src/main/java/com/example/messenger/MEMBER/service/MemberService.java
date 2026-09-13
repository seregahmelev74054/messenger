package com.example.messenger.MEMBER.service;

import com.example.messenger.EXCEPTION.MemberNotFoundException;
import com.example.messenger.MEMBER.entity.Member;
import com.example.messenger.MEMBER.repository.MemberRepository;
import com.example.messenger.USER.entity.User;
import com.example.messenger.USER.service.CurrentUserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final CurrentUserProvider currentUserProvider;

    public MemberService(
            MemberRepository memberRepository,
            CurrentUserProvider currentUserProvider
    ) {
        this.memberRepository = memberRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional
    public void patchLastReadMessageId(
            long conversationId,
            long lastReadMessageId
    ) {

        User currentUser = currentUserProvider.getCurrentUser();

        Member member = memberRepository.findByUserIdAndConversationId(currentUser.getId(), conversationId)
                .orElseThrow(() -> new MemberNotFoundException(""));

        member.setLastReadMessageId(lastReadMessageId);
    }

}
