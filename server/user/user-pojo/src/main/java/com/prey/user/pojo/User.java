package com.prey.user.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long userId;
    @ApiModelProperty(value = "用户名", name = "username", example = "Jack", required = true)
    private String username;
    @ApiModelProperty(value = "账号", name = "account", example = "13300000000", required = true)
    private String account;
    private String password;
}
