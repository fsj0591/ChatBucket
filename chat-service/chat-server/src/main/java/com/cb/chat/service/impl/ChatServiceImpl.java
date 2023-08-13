package com.cb.chat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cb.chat.config.Constant;
import com.cb.chat.config.OpenAiApi;
import com.cb.chat.mapper.UserContextMapper;
import com.cb.chat.mapper.UserConversationMapper;
import com.cb.chat.model.completion.chat.ChatCompletionChoice;
import com.cb.chat.model.completion.chat.ChatMessage;
import com.cb.chat.model.completion.chat.ChatMessageRole;
import com.cb.chat.pojo.UserContext;
import com.cb.chat.pojo.UserConversation;
import com.cb.chat.service.ChatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    OpenAiApi openAiApi;

    @Resource
    UserContextMapper userContextMapper;

    @Resource
    UserConversationMapper userConversationMapper;

    @Override
    public ChatMessage postChatCompletion(String content, String userId, Integer conversation) {
        //封装发送的消息存到list中
        final List<ChatMessage> list = userContextMapper.selectMessageForUser(conversation);
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.USER.value(), content);
        //消息入库
        UserContext userContext = new UserContext();
        userContext.setRole(ChatMessageRole.USER.value());
        userContext.setConversation(conversation);
        userContext.setContent(content);
        userContextMapper.insert(userContext);
        //消息添加至上下文
        list.add(systemMessage);
        //获取gpt返回的内容
        String json = openAiApi.post(Constant.CREATE_CHAT_COMPLETION, list, userId);
        //获取gpt返回的内容
        JSONObject jsonObject = JSONObject.parseObject(json);
        List<ChatCompletionChoice> choices = jsonObject.getJSONArray("choices").toJavaList(ChatCompletionChoice.class);
        //把内容封装到ChatMessage对象中
        ChatMessage context = new ChatMessage(choices.get(0).getMessage().getRole(), choices.get(0).getMessage().getContent());
        //消息入库
        UserContext aiContext = new UserContext();
        aiContext.setRole(context.getRole());
        aiContext.setContent(context.getContent());
        aiContext.setConversation(conversation);
        userContextMapper.insert(aiContext);
        return context;
    }

    @Override
    public boolean createChatCompletion(Integer userId) {
        UserConversation userConversation = new UserConversation();
        userConversation.setUserId(userId);
        return userConversationMapper.insert(userConversation)>0;
    }

    @Override
    public List<UserConversation> getCompletionList(Integer userId) {
        LambdaQueryWrapper<UserConversation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserConversation::getUserId, userId);
        return userConversationMapper.selectList(queryWrapper);
    }

}
