package com.baisanh.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * C端用户登录
 */
@Data
public class UserLoginDTO implements Serializable {

    // 微信登录凭证
    @ApiModelProperty("微信登录凭证")
    private String code;

    // 手机号
    @ApiModelProperty("手机号")
    private String phone;

    // 密码
    @ApiModelProperty("密码")
    private String password;

}
