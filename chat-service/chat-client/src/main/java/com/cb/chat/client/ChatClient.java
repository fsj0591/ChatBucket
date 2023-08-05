package com.cb.chat.client;

import com.cb.common.web.pojo.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "chat-server", fallback = ChatClientResolver.class)
public interface ChatClient {

    @RequestMapping("gpt/test")
    UserInfo test();

}
