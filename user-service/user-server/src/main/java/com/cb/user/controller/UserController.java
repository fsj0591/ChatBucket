package com.cb.user.controller;

import com.alibaba.nacos.common.utils.StringUtils;
import com.cb.common.core.result.RestResult;
import com.cb.user.pojo.domain.Mail;
import com.cb.user.pojo.domain.User;
import com.cb.user.pojo.dto.LoginDto;
import com.cb.user.service.UserService;
import com.cb.user.util.MailUtils;
import com.sun.istack.internal.NotNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    MailUtils mailUtils;

    /**
     * 用户登录
     *
     * @param loginDto 登录dto
     * @return {@link RestResult}<{@link Map}<{@link String}, {@link Object}>>
     */
    @GetMapping("login")
    public RestResult<Map<String, Object>> userLogin(LoginDto loginDto){
        return RestResult.ok(userService.userLogin(loginDto));
    }

    /**
     * 用户注册
     *
     * @param user    用户
     * @param captcha 验证码
     * @return {@link RestResult}<{@link Boolean}>
     */
    @PostMapping("register")
    public RestResult<Boolean> userRegister(User user, @NotNull Integer captcha){
        if(StringUtils.isNotBlank(user.getMail()) && StringUtils.isNotBlank(user.getPassword())){
            if(userService.userRegister(user, captcha)){
                return RestResult.ok(true, "注册成功");
            }else {
                return RestResult.fail("注册失败");
            }
        }else {
            return RestResult.fail("缺少内容");
        }
    }

    /**
     * 发送邮箱验证码
     *
     * @param email 用户邮箱
     * @return 发送结果
     */
    @PostMapping("send/code")
    public RestResult<?> sendUserMail(String email) {
        Mail mail = new Mail();
        mail.setTo(email);
        mailUtils.sendVerificationCode(mail);
        return RestResult.ok(null, "发送成功");
    }


    /**
     * 注销
     *
     * @return {@link RestResult}<{@link ?}>
     */
    @PostMapping("logout")
    public RestResult<?> logout(){
        return RestResult.ok(userService.logout());
    }

}
