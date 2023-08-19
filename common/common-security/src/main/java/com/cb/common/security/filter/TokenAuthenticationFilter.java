package com.cb.common.security.filter;

import com.cb.common.security.pojo.SecurityUser;
import com.cb.common.security.utils.JwtUtil;
import com.cb.common.web.pojo.UserInfo;
import com.cb.common.web.utils.UserInfoContext;
import io.jsonwebtoken.Claims;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        //当没有token时直接放行
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        Integer userId;
        try {
            // 解析jwt拿到jwt的载荷跟其余信息
            Claims claims = JwtUtil.parseJwt(token);
            userId = (Integer) claims.get("userId");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //获取用户信息
        String redisKey = "login:" + userId;
        SecurityUser user = (SecurityUser) redisTemplate.opsForValue().get(redisKey);
        if(Objects.isNull(user)){
            throw new RuntimeException("用户未登录");
        }
        //存储用户信息到用户上下文中
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getUser().getId());
        userInfo.setImage(user.getUser().getImage());
        userInfo.setName(user.getUser().getName());
        userInfo.setVip(user.getUser().getVip());
        UserInfoContext.setUser(userInfo);
        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
