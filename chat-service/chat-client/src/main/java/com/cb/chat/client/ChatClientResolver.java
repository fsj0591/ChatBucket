package com.cb.chat.client;

import com.cb.common.web.pojo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChatClientResolver implements ChatClient{
    @Override
    public UserInfo test() {
        log.error("comment 服务异常：getPostCommentList 请求失败");
        return null;
    }
}
