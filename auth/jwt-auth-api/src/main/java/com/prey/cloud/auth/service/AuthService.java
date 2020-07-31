package com.prey.cloud.auth.service;

import com.prey.cloud.auth.pojo.Account;
import com.prey.cloud.auth.pojo.AuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author prey
 * @description:
 **/
@FeignClient("jwt-auth-service")
@RequestMapping("auth-service")
public interface AuthService {

    /**
     * @param: username password
     * @description: 登录时创建token
     */
    @PostMapping("/token")
    @ResponseBody
    public AuthResponse tokenize(@RequestParam("userId")String userId);
    /**
     * @param:
     * @description: 鉴定token是否合法
     */
    @GetMapping("/verify")
    public AuthResponse verify(@RequestParam("token") String token,
                               @RequestParam("username") String username);

    /**
     * @param:
     * @description: 给快过期的的token重新替换一份新的
     */
    @GetMapping("/refresh")
    public AuthResponse refresh(@RequestParam("token") String token);

    /**
     * @param:
     * @description: 清除token
     */
    @DeleteMapping("/clear")
    public AuthResponse delete(@RequestBody Account account);
}
