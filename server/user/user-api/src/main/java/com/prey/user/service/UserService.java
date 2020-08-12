package com.prey.user.service;


import com.prey.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author prey
 * @description: 用户接口
 **/
public interface UserService {

    /**
     * @param:
     * @description: 输入账号密码登录
     */
    public User passport(@RequestParam(value = "account") String account,
                      @RequestParam(value = "password") String password);


    /**
     * @param:
     * @description: 注册
     */
    public Boolean signUp(@RequestBody User user);

    /**
     * @param:
     * @description: 修改用户名
     */
    public Boolean updateName(@RequestParam(value = "userId") String userId, @RequestParam(value = "username") String username);
}
