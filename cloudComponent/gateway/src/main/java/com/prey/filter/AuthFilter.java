package com.prey.filter;


import com.prey.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author prey
 * @description: 整合jwt-auth-service 通过鉴定token实现过滤器
 **/
@Component("AuthFilter")
@Slf4j
public class AuthFilter implements GatewayFilter, Ordered {
    private static  final String AUTH = "Authorization";
    private static  final String USERNAME = "auth-user-name";

    @Autowired
    private AuthService authService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Auth start");
        //  这里里之前 需要先在用户登录时将token、username存入请求header
        ServerHttpRequest request =  exchange.getRequest();
        HttpHeaders header = request.getHeaders();
        String token = header.getFirst(AUTH);
        String username = header.getFirst(USERNAME);
        ServerHttpResponse response = exchange.getResponse();

        if(StringUtils.isBlank(token)){
            log.error("token not found");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        AuthResponse resp = authService.verify(token, username);
        if( resp.getCode() != 1L){
            log.error("invalid token");
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        ServerHttpRequest.Builder mutate = request.mutate();
        mutate.header("user-name",username);
        ServerHttpRequest buildRequest = mutate.build();

        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add("username",username);
        return chain.filter(exchange.mutate()
                .request(buildRequest)
                .response(response)
                .build());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
