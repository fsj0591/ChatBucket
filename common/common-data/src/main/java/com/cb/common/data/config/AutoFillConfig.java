package com.cb.common.data.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
@Slf4j
@Component
public class AutoFillConfig implements MetaObjectHandler {

    /**
     * 插入时的填充策略
     */
    @Override
    public void insertFill(MetaObject metaObject)
    {
        log.info("start insert fill.....");
        this.setFieldValByName("createdTime", new Timestamp(System.currentTimeMillis()), metaObject);
    }

    /**
     * 更新时的填充策略
     */
    @Override
    public void updateFill(MetaObject metaObject)
    {
        log.info("start update fill.....");
        this.setFieldValByName("updatedTime", new Timestamp(System.currentTimeMillis()), metaObject);
    }

}
