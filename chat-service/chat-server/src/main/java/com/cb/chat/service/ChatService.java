package com.cb.chat.service;

import com.cb.chat.model.completion.chat.ChatMessage;
import com.cb.chat.pojo.UserConversation;

import java.util.List;

public interface ChatService {
    ChatMessage postChatCompletion(String content, String userId, Integer conversation);

    boolean createChatCompletion(Integer userId);

    List<UserConversation> getCompletionList(Integer userId);
}
