package com.baisanh.service;

import com.baisanh.dto.UserDTO;
import com.baisanh.dto.UserLoginDTO;
import com.baisanh.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);

    /**
     * 浏览器登录
     * @param userLoginDTO
     * @return
     */
    User browserLogin(UserLoginDTO userLoginDTO);

    /**
     * 获取当前登录用户信息
     */
    User getCurrentUserInfo();

    /**
     * 更新用户信息
     */
    void updateUserInfo(UserDTO userDTO);

    /**
     * 修改密码
     */
    void updatePassword(String oldPassword, String newPassword);

    /**
     * 上传头像
     */
    String uploadAvatar(MultipartFile file);

    /**
     * 用户注册
     */
    void register(UserDTO userDTO);
}
