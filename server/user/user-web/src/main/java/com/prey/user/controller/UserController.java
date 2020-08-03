package com.prey.user.controller;

import com.prey.auth.pojo.AuthResponse;
import com.prey.auth.service.AuthService;
import com.prey.enums.ResponseCode;
import com.prey.pojo.JSONResult;
import com.prey.user.pojo.User;
import com.prey.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author prey
 * @description:
 **/
@RestController
@RequestMapping("user")
@Api(tags = "用户接口")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @ApiOperation("登录")
    @PutMapping("/login")
    public JSONResult login(@RequestParam(value = "account",required = true)String account,
                            @RequestParam(value = "password",required = true)String password){
        User user = userService.login(account,password);
        if(user == null){
            JSONResult.errorMsg("账号或密码错误");
        }
        // 颁发token
        AuthResponse response = authService.tokenize(user.getUserId().toString());
        if(response.getCode() == ResponseCode.ERROR.getCode()){
            log.error("用户{}创建token失败",account);
            return JSONResult.errorMsg("意外错误请重试");
        }
        return JSONResult.ok(user);
    }

    @ApiOperation("修改用户名")
    @PutMapping("/username")
    public JSONResult updateUsername(@RequestParam(value = "username")String username){
        // 通过 请求的header获取 token 再获取 userId
        String userId = authService.getUserId();
        if(StringUtils.isBlank(userId)){
            return JSONResult.errorMsg("无法获取登录信息");
        }
        Boolean res = userService.updateUsername(userId,username);
        if(res){
            return JSONResult.ok("修改成功");
        }
        return  JSONResult.errorMsg("修改失败");
    }


}
