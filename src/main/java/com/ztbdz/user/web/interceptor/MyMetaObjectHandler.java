package com.ztbdz.user.web.interceptor;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("id", UUID.randomUUID().getMostSignificantBits(), metaObject);
        this.setFieldValByName("createDate", new Date(), metaObject);
        this.setFieldValByName("updateDate", new Date(), metaObject);
        this.setFieldValByName("isDelete", 0, metaObject);
        this.setFieldValByName("isStop", 0, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateDate", new Date(), metaObject);
    }
}
