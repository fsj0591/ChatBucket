package com.cb.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cb.resource.service.FileStorageService;
import com.cb.user.exception.UserException;
import com.cb.user.mapper.UserMapper;
import com.cb.user.pojo.domain.User;
import com.cb.user.pojo.dto.LoginDto;
import com.cb.user.pojo.vo.UserLoginVO;
import com.cb.user.service.UserService;
import com.cb.user.util.JwtUtil;
import com.cb.user.util.SaltUtils;
import io.jsonwebtoken.Claims;
import io.minio.errors.MinioException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.cb.user.constant.MailConstant.CODE_KEY_PREFIX;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    UserMapper userMapper;

    @Resource
    RedisTemplate<String, Integer> redisTemplate;

    @Resource
    FileStorageService fileStorageService;

    @Override
    public Map<String, Object> userLogin(LoginDto loginDto){

        if (StringUtils.isNotBlank(loginDto.getMail()) && StringUtils.isNotBlank(loginDto.getPassword())) {
            //1.根据邮箱查询信息
            User user = getOne(Wrappers.<User>lambdaQuery().eq(User::getMail, loginDto.getMail()));
            if (user == null) {
                throw new UserException("用户信息不存在");
            }
            //2.验证密码
            String salt = user.getSalt();
            String password = loginDto.getPassword();
            String value = DigestUtils.md5DigestAsHex((password + salt).getBytes());
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

    @Override
    public boolean userRegister(User user, Integer captcha) {
        //判断用户是否已经注册
        User exist = getOne(Wrappers.<User>lambdaQuery().eq(User::getMail, user.getMail()));
        if(exist!=null){
            throw new UserException("用户已注册");
        }

        // 判断验证码是否相同
        String key = CODE_KEY_PREFIX + user.getMail();
        Integer authcode = redisTemplate.opsForValue().get(key);
        if (authcode == null) {
            throw new UserException("验证码已过期，请重新发送");
        }
        if (!authcode.equals(captcha)) {
            throw new UserException("验证码错误");
        }

        //存入用户信息
        //md5加盐存入密码
        String password = user.getPassword();
        String salt = SaltUtils.getSalt();
        user.setSalt(salt);
        user.setPassword(DigestUtils.md5DigestAsHex((password + salt).getBytes()));
        return userMapper.insert(user)>0;
    }

    @Override
    public String updateUserAvatar(Integer userId, MultipartFile file){
        User check = userMapper.selectById(userId);
        if (check == null) {
            throw new UserException("获取用户信息异常");
        }
        // 进行头像保存，获取文件名
        String url;
        try {
            url = fileStorageService.uploadImg(file);
        } catch (MinioException | IOException e) {
            throw new RuntimeException(e);
        }
        User user = new User();
        user.setId(userId);
        user.setImage(url);
        userMapper.updateById(user);
        return url;
    }

    @Override
    public boolean updateUserInfo(Integer userId, User user) {
        if (user.getPassword() != null || user.getImage() != null || user.getIdentity() != null || user.getMail() != null || user.getSalt() != null) {
            throw new UserException("不合法的参数传入");
        }
        return userMapper.updateById(user)>0;
    }

    @Override
    public boolean logout(HttpServletRequest request) {

        return true;
    }

    @Override
    public UserLoginVO getUserInfo(Integer userId) {
        User user = userMapper.selectById(userId);
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, userLoginVO);
        return userLoginVO;
    }
}
