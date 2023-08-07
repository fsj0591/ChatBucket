package com.cb.common.web.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.StringUtils;
import com.cb.common.web.pojo.UserInfo;
import com.cb.common.web.utils.UserInfoContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class TransmitUserInfoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        this.initUserInfo((HttpServletRequest) request);
        filterChain.doFilter(request, response);
    }

    //获取调用方传来的head中的用户信息
    private void initUserInfo(HttpServletRequest request) {
        String user = request.getHeader("USER_INFO");
        if (StringUtils.isNotBlank(user)) {
            byte[] userBytes = Base64.getDecoder().decode(user);
            String userJson = new String(userBytes, StandardCharsets.UTF_8);
            UserInfo userInfo = JSON.parseObject(userJson, UserInfo.class);
            //将UserInfo放入上下文中
            UserInfoContext.setUser(userInfo);
        }
    }
}
