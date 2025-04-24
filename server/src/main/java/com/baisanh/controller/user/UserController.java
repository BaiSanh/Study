package com.baisanh.controller.user;

import com.baisanh.constant.JwtClaimsConstant;
import com.baisanh.dto.UserDTO;
import com.baisanh.dto.UserLoginDTO;
import com.baisanh.entity.User;
import com.baisanh.properties.JwtProperties;
import com.baisanh.result.Result;
import com.baisanh.service.UserService;
import com.baisanh.utils.JwtUtil;
import com.baisanh.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Api(tags = "C端用户相关接口")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("微信登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("微信用户登录：{}",userLoginDTO.getCode());

        //微信登录
        User user = userService.wxLogin(userLoginDTO);

        //为微信用户生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }

    /**
     * 浏览器登录
     */
    @PostMapping("/browserLogin")
    @ApiOperation("浏览器登录")
    public Result<UserLoginVO> browserLogin(@RequestBody UserLoginDTO userLoginDTO){
        log.info("浏览器用户登录：{}",userLoginDTO.getPhone());

        User user = userService.browserLogin(userLoginDTO);

        //生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public Result<String> logout(){return Result.success();}

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    @ApiOperation("获取用户信息")
    public Result<User> getUserInfo() {
        User user = userService.getCurrentUserInfo();
        return Result.success(user);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    @ApiOperation("更新用户信息")
    public Result updateUserInfo(@RequestBody UserDTO userDTO) {
        userService.updateUserInfo(userDTO);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    @ApiOperation("修改密码")
    public Result updatePassword(@RequestBody Map<String, String> map) {
        String oldPassword = map.get("oldPassword");
        String newPassword = map.get("newPassword");
        userService.updatePassword(oldPassword, newPassword);
        return Result.success();
    }

    /**
     * 上传头像
     */
    @PostMapping("/avatar")
    @ApiOperation("上传头像")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String avatarUrl = userService.uploadAvatar(file);
        return Result.success(avatarUrl);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result register(@RequestBody UserDTO userDTO) {
        userService.register(userDTO);
        return Result.success();
    }
}
