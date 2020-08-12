package com.prey.user.service.inner;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author prey
 * @description: 用户接口   使用feign组件可以让其他有user-api依赖的微服务将本类方法本地调用
 **/
@FeignClient("user-service")
@RequestMapping("user-center-api")
public interface UserServiceInner {

    /**
     * @param:
     * @description: 查看账号是否重复
     */
    @GetMapping("user/exist")
    public Boolean accountIsExist(@RequestParam(value = "account") String account);


}
