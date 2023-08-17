package com.cb.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cb.common.security.pojo.SecurityUser;
import com.cb.common.web.utils.UserInfoContext;
import com.cb.resource.service.FileStorageService;
import com.cb.user.constant.TimeConstant;
import com.cb.user.exception.UserException;
import com.cb.user.mapper.UserMapper;
import com.cb.user.pojo.domain.Cycle;
import com.cb.user.pojo.domain.User;
import com.cb.user.pojo.dto.LoginDto;
import com.cb.user.pojo.vo.UserLoginVO;
import com.cb.user.service.UserService;
import com.cb.user.util.JwtUtil;
import io.minio.errors.MinioException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.cb.user.constant.MailConstant.CODE_KEY_PREFIX;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    UserMapper userMapper;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    FileStorageService fileStorageService;

    @Resource
    AuthenticationManager authenticationManager;

    @Override
    public Map<String, Object> userLogin(LoginDto loginDto){

        if (StringUtils.isNotBlank(loginDto.getMail()) && StringUtils.isNotBlank(loginDto.getPassword())) {
            //1.根据邮箱查询信息
            User user = getOne(Wrappers.<User>lambdaQuery().eq(User::getMail, loginDto.getMail()));
            if (user == null) {
                throw new UserException("用户信息不存在");
            }
            //使用AuthenticationManager进行用户认证
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getMail(), loginDto.getPassword());
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            //登录失败报异常
            if(Objects.isNull(authenticate)){
                throw new RuntimeException("登录失败");
            }
            //3.返回用户信息
            String token = JwtUtil.createJwt("userId", user.getId().longValue());
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            UserLoginVO userLoginVO = new UserLoginVO();
            BeanUtils.copyProperties(user, userLoginVO);
            map.put("user", userLoginVO);
            //redis存储
            String redisKey = "login:" + user.getId();
            SecurityUser securityUser = (SecurityUser) authenticate.getPrincipal();
            redisTemplate.opsForValue().set(redisKey, securityUser);
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
        Integer authcode = (Integer) redisTemplate.opsForValue().get(key);
        if (authcode == null) {
            throw new UserException("验证码已过期，请重新发送");
        }
        if (!authcode.equals(captcha)) {
            throw new UserException("验证码错误");
        }
        //存入用户信息
        //BCrypt存入密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //设置用户会员
        user.setVip(new Timestamp(System.currentTimeMillis()));
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
        System.out.println(UserInfoContext.getUser());
        userMapper.updateById(user);
        return url;
    }

    @Override
    public boolean updateUserInfo(Integer userId, User user) {
        if (user.getPassword() != null || user.getImage() != null || user.getIdentity() != null || user.getMail() != null) {
            throw new UserException("不合法的参数传入");
        }
        return userMapper.updateById(user)>0;
    }

    @Override
    public boolean logout() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        SecurityUser user = (SecurityUser) authenticationToken.getPrincipal();
        Integer userId = user.getUser().getId();
        String redisKey = "login:" + userId;
        redisTemplate.delete(redisKey);
        return true;
    }

    @Override
    public UserLoginVO getUserInfo(Integer userId) {
        User user = userMapper.selectById(userId);
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, userLoginVO);
        return userLoginVO;
    }

    @Override
    public boolean createVIP(Integer userId, Integer cycle) {
        //判断投票周期
        if (!(cycle.equals(Cycle.HALF.getValue()) || cycle.equals(Cycle.QUARTER.getValue()) || cycle.equals(Cycle.DAY.getValue()) || cycle.equals(Cycle.WEEK.getValue()) || cycle.equals(Cycle.MONTH.getValue()) || cycle.equals(Cycle.YEAR.getValue()))) {
            throw new UserException("错误的周期");
        }
        int row;
        User user = userMapper.selectById(userId);
        if(isVIPExpired(user)){
            //没过期
            Timestamp vip = user.getVip();
            long time = vip.getTime() + (TimeConstant.day * cycle);
            Timestamp newTime = new Timestamp(time);
            row = userMapper.createVIP(userId, newTime);
        }else {
            //过期
            Timestamp now = new Timestamp(System.currentTimeMillis());
            row = userMapper.createVIP(userId, new Timestamp(now.getTime() + (TimeConstant.day * cycle)));
        }
        return row>0;
    }

    public boolean isVIPExpired(User user){
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp vip = user.getVip();
        return now.before(vip);
    }

}
