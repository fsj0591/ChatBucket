package com.cb.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cb.user.exception.UserException;
import com.cb.user.mapper.UserMapper;
import com.cb.user.pojo.domain.User;
import com.cb.user.pojo.dto.LoginDto;
import com.cb.user.pojo.vo.UserLoginVO;
import com.cb.user.service.UserService;
import com.cb.user.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

//    @Resource
//    UserMapper userMapper;

    @Override
    public Map<String, Object> userLogin(LoginDto loginDto){

        if (StringUtils.isNotBlank(loginDto.getPhone()) && StringUtils.isNotBlank(loginDto.getPassword())) {
            //1.根据手机号查询信息
            User user = getOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, loginDto.getPhone()));
            if (user == null) {
                throw new UserException("用户信息不存在");
            }
            //2.验证密码
            String salt = user.getSalt();
            String password = loginDto.getPassword();
            String value = DigestUtils.md5DigestAsHex((password + salt).getBytes());
            System.out.println(value);
            if (!value.equals(user.getPassword())) {
                throw new UserException("密码错误");
            }
            //3.返回用户信息
            String token = JwtUtil.createJwt("userId", user.getId().longValue());
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            UserLoginVO userLoginVO = new UserLoginVO();
            BeanUtils.copyProperties(user, userLoginVO);
            map.put("user", userLoginVO);
            return map;
        }else {
            //游客登录
            Map<String,Object> map = new HashMap<>();
            map.put("token", JwtUtil.createJwt());
            return map;
        }
    }
}
