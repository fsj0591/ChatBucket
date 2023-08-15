package com.cb.chat.config;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import com.cb.chat.exception.ChatException;
import com.cb.chat.model.completion.chat.ChatCompletionRequest;
import com.cb.chat.model.completion.chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.cb.chat.config.Constant.API_KEY_PREFIX;


@Slf4j
@Component
public class OpenAiApi {

    @Value("${open.ai.url}")
    private String url;

    @Value("${open.ai.token}")
    private String token;

    @Value("${token.num}")
    private int tokenNum;

    /**
     * gpt请求参数实体封装
     *
     * @param list   列表
     * @param userId 用户id
     * @return {@link String}
     */
    public String ChatCompletionRequestBuilder(List<ChatMessage> list, String userId){
        //封装gpt请求参数实体类
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(list)
                .user(userId)
                .max_tokens(tokenNum)
                .temperature(1.0)
                .build();
        return JSONObject.toJSONString(chatCompletionRequest);
    }

    /**
     * gpt请求发送
     *
     * @param path   路径
     * @param list   列表
     * @param userId 用户id
     * @return {@link String}
     */
    public String post(String path, List<ChatMessage> list, String userId) {
        try {
            String body = HttpRequest.post(url + path)
                    .header("Authorization", API_KEY_PREFIX+token)
                    .header("Content-Type", "application/json")
                    .timeout(10000) // 连接超时时间为5秒
                    .setReadTimeout(10000) // 读取超时时间为5秒
                    .body(ChatCompletionRequestBuilder(list, userId))
                    .execute()
                    .body();
            if(!StringUtils.isNotBlank(body)){
                throw new ChatException("返回异常");
            }
            log.info(body);
            return body;
        }catch (HttpException | ConversionException httpException){
            log.info(httpException.getMessage());
        }
        return null;
    }

}