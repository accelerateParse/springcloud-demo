package com.prey.user.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author prey
 * @description:
 **/
@Data
@Builder
@ApiModel(value = "用户封装类", description = "简略的用户对象")
public class User {

    private Long userId;
    @ApiModelProperty(value = "用户名", name = "username", example = "Jack", required = true)
    private String username;
    @ApiModelProperty(value = "账号", name = "account", example = "13300000000", required = true)
    private String account;
    private String password;
}
