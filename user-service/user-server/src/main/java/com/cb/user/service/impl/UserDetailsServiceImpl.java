package com.cb.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cb.common.security.pojo.SecurityUser;
import com.cb.user.exception.UserException;
import com.cb.user.mapper.RoleMapper;
import com.cb.user.mapper.UserMapper;
import com.cb.user.pojo.domain.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    UserMapper userMapper;

    @Resource
    RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getMail, username);
        User user = userMapper.selectOne(queryWrapper);
        if(Objects.isNull(user)){
            throw new UserException("没有该用户");
        }
        com.cb.common.security.pojo.User sUser = new com.cb.common.security.pojo.User();
        BeanUtils.copyProperties(user, sUser);
        List<String> list = roleMapper.selectRole(user.getId());
        return new SecurityUser(sUser, list);
    }
}
