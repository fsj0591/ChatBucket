package com.cb.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cb.user.pojo.domain.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {

    @Select("select role from user_role where user_id = #{id}")
    List<String> selectRole(@Param("id") Integer id);
}
