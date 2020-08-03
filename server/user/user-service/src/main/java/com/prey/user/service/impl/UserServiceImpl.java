package com.prey.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prey.auth.service.AuthService;
import com.prey.user.mapper.UserMapper;
import com.prey.user.pojo.User;
import com.prey.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author prey
 * @description:
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthService authService;


    @Override
    public User login(String account, String password) {
        User user = userMapper.selectOne(
                // 使用mybatis-plus的条件构造器查询用户
                new QueryWrapper<User>().eq("account",account)
                .eq("password",password)
        );
        return user;
    }

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

    @Override
    public Boolean signUp(User user) {
        return null;
    }

    @Override
    public Boolean updateUsername(String userId,String username) {
        // mybatis-plus 默认字段为空不进行修改，只需要填主键id和对应修改的字段，然后传入updateById方法
        User user = User.builder().userId(Long.parseLong(userId)).username(username).build();
        userMapper.updateById(user);
        return true;
    }
}
