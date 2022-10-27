package com.flash.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("自动填充insert数据  {}", metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        Long employeeId = BaseContext.getCurrentUserId();
        metaObject.setValue("createUser", employeeId);
        metaObject.setValue("updateUser", employeeId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("自动填充update数据  {}", metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentUserId());

    }

}
