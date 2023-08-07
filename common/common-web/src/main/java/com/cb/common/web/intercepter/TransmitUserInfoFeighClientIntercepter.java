package com.cb.common.web.intercepter;

import com.alibaba.fastjson.JSON;
import com.cb.common.web.pojo.UserInfo;
import com.cb.common.web.utils.UserInfoContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TransmitUserInfoFeighClientIntercepter implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        UserInfo userInfo = UserInfoContext.getUser();
        if (userInfo !=null){
            //fegin调用时把用户信息存到header
            try {
                String json = JSON.toJSONString(userInfo);
                byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
                String jsonBase64 = Base64.getEncoder().encodeToString(jsonBytes);
                requestTemplate.header("USER_INFO", jsonBase64);
            } finally {
                UserInfoContext.remove();
            }
        }
    }
}
