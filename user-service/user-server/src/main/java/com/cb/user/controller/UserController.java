package com.cb.user.controller;

import com.cb.common.core.result.RestResult;
import com.cb.user.pojo.dto.LoginDto;
import com.cb.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("login")
    public RestResult<Map<String, Object>> userLogin(LoginDto loginDto){
        return RestResult.ok(userService.userLogin(loginDto));
    }

}
