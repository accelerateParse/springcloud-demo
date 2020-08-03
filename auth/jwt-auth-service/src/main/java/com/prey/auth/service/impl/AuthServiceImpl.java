package com.prey.auth.service.impl;

import com.prey.auth.pojo.Account;
import com.prey.auth.pojo.AuthResponse;
import com.prey.auth.pojo.ResponseCode;
import com.prey.auth.service.AuthService;
import com.prey.auth.util.RequestContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author prey
 * @description:
 **/
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RedisTemplate redisTemplate;


    public static final String RefreshToken = "RefreshToken";

    public static final String Authorization = "Authorization";

    public static final String USER_TOKEN = "USER_TOKEN-";



    /**
     * @param:
     * @description: 登录时颁发token
     */
    @Override
    public AuthResponse tokenize(String userId){

        Account account = Account.builder()
                .userId(userId)
                .build();

        HttpServletResponse response = RequestContextUtil.getHttpServletResponse();
        // 生成token 并存储到redis中
        String token = jwtService.token(account);
        // 用于重置的即将失效token 的 refreshToken
        String refreshToken = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(refreshToken,account);
        redisTemplate.opsForValue().set(USER_TOKEN,account);

        // 神不知鬼不觉将token放入请求头，不用前端知道,前端连 userId也不需要知道
        response.setHeader(RefreshToken, refreshToken);
        response.setHeader(Authorization, token);

        return  AuthResponse.builder().account(account).code(ResponseCode.SUCCESS.getCode()).build();
    }

    /**
     * @param:
     * @description: 利用 refreshToken 直接重置token
     */
    @Override
    public AuthResponse refresh(String refreshToken){

        Account account = (Account) redisTemplate.opsForValue().get(refreshToken);
        if(account == null){
            return AuthResponse.builder().code(ResponseCode.USER_NOT_FOUND.getCode()).build();
        }
        return tokenize(account.getUserId());
    }

    @Override
    public AuthResponse delete(Account account) {
        AuthResponse token = new AuthResponse();
        token.setCode(ResponseCode.SUCCESS.getCode());
        if(account.isSkipVerification()){
            redisTemplate.delete(USER_TOKEN + account.getUserId());

        }else {
            HttpServletRequest request = RequestContextUtil.getHttpServletRequest();
            token = verify(request.getHeader(Authorization),account.getUserId());
            if(token.getCode() == ResponseCode.SUCCESS.getCode()){
                redisTemplate.delete(USER_TOKEN );
                redisTemplate.delete(request.getHeader(RefreshToken));
            }else {
                token.setCode(ResponseCode.USER_NOT_FOUND.getCode());
            }
        }
        return token;
    }

    @Override
    public String getUserId() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            if(request != null) {
                String token = request.getHeader("Authorization");
                if(StringUtils.isNotBlank(token)){
                 Account account =   (Account) redisTemplate.opsForValue().get(token);
                 return account.getUserId();
                }
            }
        }
        return Strings.EMPTY;
    }

    /**
     * @param:
     * @description: 鉴定token合法性
     */
    @Override
    public AuthResponse verify( String token, String userId){
        Boolean result = jwtService.verify(token, userId);
        return AuthResponse.builder()
                .code(result ? ResponseCode.SUCCESS.getCode() : ResponseCode.USER_NOT_FOUND.getCode())
                .build();
    }



}
