package com.cb.user.controller;

import com.cb.common.core.result.RestResult;
import com.cb.user.exception.UserException;
import com.cb.user.pojo.domain.User;
import com.cb.user.pojo.vo.UserLoginVO;
import com.cb.user.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("user/info")
public class UserInfoController {

    @Resource
    UserService userService;

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return {@link RestResult}<{@link ?}>
     */
    @GetMapping()
    public RestResult<UserLoginVO> getUserInfo(@RequestParam Integer userId){
        if(userId==null){
            throw new UserException("获取用户信息异常");
        }
        return RestResult.ok(userService.getUserInfo(userId));
    }

    /**
     * 更新头像
     *
     * @param userId 用户id
     * @param file   文件
     * @return {@link RestResult}<{@link String}>
     */
    @PostMapping("avatar")
    @PreAuthorize("@ssc.hasAuthority('user')")
    public RestResult<String> updateAvatar(@RequestHeader Integer userId, MultipartFile file){
        if(file.isEmpty()){
            throw new UserException("上传头像不能为空");
        }
        // 存入并获取新头像路径
        String newAvatar = userService.updateUserAvatar(userId, file);
        return RestResult.ok(newAvatar, "上传成功");
    }


    /**
     * 更新用户基本信息
     *
     * @param userId 用户id
     * @return {@link RestResult}<{@link Boolean}>
     */
    @PostMapping("update")
    public RestResult<Boolean> updateUserInfo(@RequestHeader Integer userId, User user){
        if(userId==null){
            throw new UserException("获取用户信息异常");
        }
        return RestResult.ok(userService.updateUserInfo(userId, user));
    }


    /**
     * 创建贵宾
     *
     * @param userId 用户id
     * @param cycle  周期
     * @return {@link RestResult}<{@link Boolean}>
     */
    @PostMapping("vip")
    public RestResult<Boolean> createVIP(@RequestHeader Integer userId, Integer cycle){
        if(userId==null){
            throw new UserException("获取用户信息异常");
        }
        return RestResult.ok(userService.createVIP(userId, cycle));
    }

}
