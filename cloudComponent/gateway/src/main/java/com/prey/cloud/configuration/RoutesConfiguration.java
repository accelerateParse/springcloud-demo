package com.prey.cloud.configuration;

import com.prey.cloud.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author prey
 * @description: 路由配置
 **/
@Configuration
public class RoutesConfiguration {

    @Autowired
    private KeyResolver hostNameResolver;

    @Autowired
    @Qualifier("redisLimiterUser")
    private RateLimiter rateLimiter;



    // 以下是路由配置方式   校验jwt token 还可以在微服务本地
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, AuthFilter authFilter){
        return  builder.routes()

                .route(r -> r.path("/signIn/**","/passport/**","/userInfo/**")
                        .filters(f -> f.requestRateLimiter(
                                c ->{
                                    c.setKeyResolver(hostNameResolver);
                                    c.setRateLimiter(rateLimiter);
                                }
                        ))
                        .uri("lb://USER-SERVICE")
                )
                .route(r -> r.path("/orders/**")
                        .uri("lb://ORDER-SERVICE")
                )
                .build();

    }
}
