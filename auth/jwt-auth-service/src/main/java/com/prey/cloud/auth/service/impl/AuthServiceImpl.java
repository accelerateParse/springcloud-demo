package com.prey.cloud.auth.service.impl;

import com.prey.cloud.pojo.Account;
import com.prey.cloud.pojo.AuthResponse;
import com.prey.cloud.pojo.ResponseCode;
import com.prey.cloud.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * @author prey
 * @description:
 **/
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @param:
     * @description: 登录时颁发token
     */
    @Override
    @PostMapping("/login")
    @ResponseBody
    public AuthResponse login(@RequestParam String username,
                              @RequestParam String password){
        Account account = Account.builder()
                .username(username)
                .build();
        // todo 验证 username、password

        // 生成token 并存储到redis中
        String token = jwtService.token(account);
        redisTemplate.opsForValue().set(account.getRefreshToken(),account);

        account.setToken(token);

        // 设置下token即将过期重置用于重置的refreshToken
        account.setRefreshToken(UUID.randomUUID().toString());
        return  AuthResponse.builder().account(account).code(ResponseCode.SUCCESS.getCode()).build();
    }

    /**
     * @param:
     * @description: 利用 refreshToken 直接重置token
     */
    @Override
    @PostMapping("/refresh")
    @ResponseBody
    public AuthResponse refresh(@RequestParam String refreshToken){

        Account account = (Account) redisTemplate.opsForValue().get(refreshToken);
        if(account == null){
            return AuthResponse.builder().code(ResponseCode.USER_NOT_FOUND.getCode()).build();
        }
        String jwt = jwtService.token(account);
        account.setToken(jwt);
        account.setRefreshToken(UUID.randomUUID().toString());
        redisTemplate.delete(refreshToken);
        redisTemplate.opsForValue().set(account.getRefreshToken(),account);
        return AuthResponse.builder().account(account).code(ResponseCode.SUCCESS.getCode()).build();
    }

    @Override
    @PostMapping("/remove")
    @ResponseBody
    public AuthResponse delete(Account account) {
        AuthResponse token = new AuthResponse();
        token.setCode(ResponseCode.SUCCESS.getCode());
        if(account.isSkipVerification()){
            redisTemplate.delete("USER_TOKEN" + account.getUsername());

        }else {
            token = verify(account.getToken(),account.getUsername());
            if(token.getCode() == ResponseCode.SUCCESS.getCode()){
                redisTemplate.delete("USER_TOKEN" + account.getUsername());
                redisTemplate.delete(account.getRefreshToken());
            }else {
                token.setCode(ResponseCode.USER_NOT_FOUND.getCode());
            }
        }
        return token;
    }

    /**
     * @param:
     * @description: 鉴定token合法性
     */
    @Override
    @PostMapping("/verify")
    @ResponseBody
    public AuthResponse verify(@RequestParam String token, @RequestParam String username){
        Boolean result = jwtService.verify(token, username);
        return AuthResponse.builder()
                .code(result ? ResponseCode.SUCCESS.getCode() : ResponseCode.USER_NOT_FOUND.getCode())
                .build();
    }

}
