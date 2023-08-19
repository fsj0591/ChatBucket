package com.cb.common.security.expression;

import com.cb.common.security.pojo.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ssc")
public class PermissionService {

    public boolean hasAuthority(String authority){
        //获取当前用户的权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        List<String> permissions = user.getPermissions();
        //判断用户权限集合中是否存在authority
        return permissions.contains(authority);
    }

}
