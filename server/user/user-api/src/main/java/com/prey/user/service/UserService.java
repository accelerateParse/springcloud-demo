package com.prey.user.service;


import com.prey.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author prey
 * @description: 用户接口
 **/
@FeignClient("user-service")
@RequestMapping("user-api")
public interface UserService {

    /**
     * @param:
     * @description: 输入账号密码登录
     */
    @PostMapping("/login")
    public User login(@RequestParam(value = "account") String account,
                      @RequestParam(value = "password") String password);

    /**
     * @param:
     * @description: 查看账号是否重复
     */
    @GetMapping("/accountIsExist")
    public Boolean accountIsExist(@RequestParam(value = "account") String account);


    /**
     * @param:
     * @description: 注册
     */
    @GetMapping("/signUp")
    public Boolean signUp(@RequestBody User user);

    /**
     * @param:
     * @description: 修改用户名
     */
    @PutMapping("/username")
    public Boolean updateUsername(@RequestParam(value = "userId") String userId, @RequestParam(value = "username") String username);
}
