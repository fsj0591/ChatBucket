package com.cb.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cb.user.pojo.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
