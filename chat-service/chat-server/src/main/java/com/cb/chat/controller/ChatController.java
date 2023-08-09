package com.cb.chat.controller;

import com.alibaba.nacos.common.utils.StringUtils;
import com.cb.chat.exception.ChatException;
import com.cb.chat.model.completion.chat.ChatMessage;
import com.cb.chat.pojo.UserConversation;
import com.cb.chat.service.ChatService;
import com.cb.common.core.result.RestResult;
import com.cb.common.web.utils.UserInfoContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("gpt")
public class ChatController {

    @Resource
    ChatService chatService;

    /**
     * 对话
     *
     * @param content      内容
     * @param userId       用户id
     * @param conversation 谈话
     * @return {@link RestResult}<{@link ChatMessage}>
     */
    @RequestMapping("/postChatCompletion")
    public RestResult<ChatMessage> postChatCompletion(String content, String userId, @RequestParam(required = false) Integer conversation) {
        Timestamp vip = UserInfoContext.getUser().getVip();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(vip.after(now)) {
            if (StringUtils.isNotBlank(content) && StringUtils.isNotBlank(userId)) {
                return RestResult.ok(chatService.postChatCompletion(content, userId, conversation));
            } else {
                throw new ChatException("参数不能为空");
            }
        }else {
            throw new ChatException("您的会员已过期");
        }
    }

    /**
     * 创建会话
     *
     * @param userId 用户id
     * @return {@link RestResult}<{@link Boolean}>
     */
    @RequestMapping("/createNewCompletion")
    public RestResult<Boolean> createChatCompletion(Integer userId){
        if(userId==null){
            throw new ChatException("参数异常");
        }
        if(chatService.createChatCompletion(userId)){
            return RestResult.ok(true, "创建成功");
        }else {
            return RestResult.fail("创建失败");
        }
    }

    /**
     * 用户会话列表
     *
     * @param userId 用户id
     * @return {@link RestResult}<{@link List}<{@link UserConversation}>>
     */
    @RequestMapping("/completion")
    public RestResult<List<UserConversation>> completionList(Integer userId){
        if(userId==null){
            throw new ChatException("参数异常");
        }
        return RestResult.ok(chatService.getCompletionList(userId));
    }

}
