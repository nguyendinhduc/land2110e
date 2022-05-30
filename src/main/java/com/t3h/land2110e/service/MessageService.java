package com.t3h.land2110e.service;

import com.t3h.land2110e.repository.MessageEntityRepository;
import com.t3h.land2110e.security.AuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private MessageEntityRepository messageEntityRepository;

    public Object getMessages(int friendId){
        return messageEntityRepository.getMessages(
                AuthorizationFilter.getCurrentUserId(),
                friendId
        );
    }
}
