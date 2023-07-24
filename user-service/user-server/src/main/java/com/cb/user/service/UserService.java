package com.cb.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cb.user.pojo.domain.User;
import com.cb.user.pojo.dto.LoginDto;

import java.util.Map;

public interface UserService extends IService<User> {
    Map<String, Object> userLogin(LoginDto loginDto);
}
