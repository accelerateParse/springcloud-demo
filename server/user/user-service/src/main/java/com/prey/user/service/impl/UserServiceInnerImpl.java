package com.prey.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prey.user.mapper.UserMapper;
import com.prey.user.pojo.User;
import com.prey.user.service.inner.UserServiceInner;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author prey
 * @description:
 **/
public class UserServiceInnerImpl implements UserServiceInner {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean accountIsExist(String account) {
        User user = userMapper.selectOne(
                // 使用mybatis-plus的条件构造器查询用户
                new QueryWrapper<User>().eq("account",account)
        );
        if(user != null){
            return false;
        }
        return true;
    }
}
