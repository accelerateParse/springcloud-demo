package com.prey.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.prey.auth.service.AuthService;
import com.prey.pojo.JSONResult;
import com.prey.user.mapper.UserMapper;
import com.prey.user.pojo.User;
import com.prey.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author prey
 * @description:
 **/
@RestController
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthService authService;


    @Override
    // 指定熔断降级的方法
    @HystrixCommand(fallbackMethod = "loginFail")
    public User passport(String account, String password) {
        User user = userMapper.selectOne(
                // 使用mybatis-plus的条件构造器查询用户
                new QueryWrapper<User>().eq("account",account)
                .eq("password",password)
        );
        return user;
    }


    @Override
    public Boolean signUp(User user) {
        return null;
    }

    @Override
    public Boolean updateName(String userId,String username) {
        // mybatis-plus 默认字段为空不进行修改，只需要填主键id和对应修改的字段，然后传入updateById方法
        User user = User.builder().userId(Long.parseLong(userId)).username(username).build();
        userMapper.updateById(user);
        return true;
    }

    /**
     * @param:
     * @description: 登录熔断降级方法
     */
    @HystrixCommand(fallbackMethod = "loginFailAgain")
    public JSONResult loginFail(){
        return JSONResult.errorMsg("验证码出错");
    }

    /**
     * @param:
     * @description: 登录熔断多级降级方法
     */
    public JSONResult loginFailAgain(){
        return JSONResult.errorMsg("验证码又又又出错了");
    }
}
