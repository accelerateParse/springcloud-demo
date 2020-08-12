package com.prey.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author prey
 * @description:
 **/
@EnableDiscoveryClient
@EnableSwagger2
@EnableFeignClients(basePackages = {"com.prey.user.service"})
@MapperScan(basePackages = "com.prey.user.mapper")
@ComponentScan(basePackages = {"com.prey"})
@SpringBootApplication
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);

    }
}
