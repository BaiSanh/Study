package com.baisanh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baisanh.constant.MessageConstant;
import com.baisanh.constant.PasswordConstant;
import com.baisanh.context.BaseContext;
import com.baisanh.dto.UserDTO;
import com.baisanh.dto.UserLoginDTO;
import com.baisanh.entity.User;
import com.baisanh.exception.BaseException;
import com.baisanh.exception.LoginFailedException;
import com.baisanh.mapper.UserMapper;
import com.baisanh.properties.WeChatProperties;
import com.baisanh.service.UserService;
import com.baisanh.utils.FileUtil;
import com.baisanh.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    public User wxLogin(UserLoginDTO userLoginDTO) {
        String openid = getOpenid(userLoginDTO.getCode());

        //判断openid是否为空，如果为空表示登录失败，抛出业务异常
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断当前用户是否为新用户
        User user = userMapper.getByOpenid(openid);

        //如果是新用户，自动完成注册
        if(user == null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        //返回这个用户对象
        return user;
    }

    /**
     * 调用微信接口服务，获取微信用户的openid
     * @param code
     * @return
     */
    private String getOpenid(String code){
        //调用微信接口服务，获得当前微信用户的openid
        Map<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }

    /**
     * 浏览器登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User browserLogin(UserLoginDTO userLoginDTO) {
        String phone = userLoginDTO.getPhone();
        String password = userLoginDTO.getPassword();

        User user = userMapper.getByPhone(phone);
        if(user == null){
            user = user.builder()
                    .phone(phone)
                    .password(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()))
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        if(!user.getPassword().equals(password)){
            throw new LoginFailedException(MessageConstant.PHONE_PASSWORD_ERROR);
        }
        return user;
    }

    @Override
    public User getCurrentUserInfo() {
        Long userId = BaseContext.getCurrentId();
        return userMapper.getById(userId);
    }

    @Override
    public void updateUserInfo(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setId(BaseContext.getCurrentId());
        userMapper.update(user);
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(oldPassword.getBytes()))) {
            throw new BaseException("原密码错误");
        }

        user.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        userMapper.update(user);
    }

    @Override
    public String uploadAvatar(MultipartFile file) {
        // 实现文件上传逻辑，返回文件访问URL
        String fileName = FileUtil.upload(file);
        Long userId = BaseContext.getCurrentId();
        userMapper.updateAvatar(userId, fileName);
        return fileName;
    }

    @Override
    public void register(UserDTO userDTO) {
        User user = userMapper.getByPhone(userDTO.getPhone());
        if (user != null) {
            throw new BaseException("手机号已注册");
        }

        user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(DigestUtils.md5DigestAsHex(userDTO.getPassword().getBytes()));
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);
    }
}
