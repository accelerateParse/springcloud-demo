package com.prey.cloud.auth.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.prey.cloud.pojo.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author prey
 * @description:
 **/
@Service
@Slf4j
public class JwtService {

    public static final long TOKEN_EXP_TIME = 60000;
    public static final String USERNAME = "username";
    // 生成环境不能这么用
    public static final String KEY = "changeIt";
    public static final String ISSUER = "changeIt";


    /**
     * @description: 利用jwt提供的算法生成token   这边的生成、鉴定用的是对称性的加密解密比较low
     */
    public String token(Account acct){
        Date now = new Date();
        // 可以选用其他的生成算法
        Algorithm algorithm = Algorithm.HMAC256(KEY);
        String token = JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + TOKEN_EXP_TIME))
                .withClaim(USERNAME, acct.getUsername())
                .sign(algorithm);
        log.info("jwt generated user={}", acct.getUsername());
        return token;
    }

    /**
     * @description: 利用jwt鉴定token合法性
     */
    public Boolean verify(String token, String username){
        log.info("verifying jwt - username={}",username);
        try {
            Algorithm algorithm = Algorithm.HMAC256(KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .withClaim(USERNAME,username)
                    .build();
            verifier.verify(token);
            return true;
        }catch (Exception e) {
            log.error("auth failed", e);
            return false;
        }
    }
}
