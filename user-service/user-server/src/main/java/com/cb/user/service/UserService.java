package com.cb.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cb.user.pojo.domain.User;
import com.cb.user.pojo.dto.LoginDto;
import com.cb.user.pojo.vo.UserLoginVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param loginDto 登录dto
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> userLogin(LoginDto loginDto);

    /**
     * 用户注册
     *
     * @param user    用户
     * @param captcha 验证码
     * @return boolean
     */
    boolean userRegister(User user, Integer captcha);

    String updateUserAvatar(Integer userId, MultipartFile file);

    boolean updateUserInfo(Integer userId, User user);

    boolean logout();

    UserLoginVO getUserInfo(Integer userId);

    boolean createVIP(Integer userId, Integer cycle);
}
