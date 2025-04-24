package com.baisanh.mapper;

import com.baisanh.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * 根据手机号查询用户
     * @param phone
     * @return
     */
    @Select("select * from user where phone = #{phone}")
    User getByPhone(String phone);

    /**
     * 插入数据
     * @param user
     */
    void insert(User user);

    @Select("select * from user where id = #{id}")
    User getById(Long userId);

    /**
     * 根据动态条件统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    void update(User user);

    @Update("update user set avatar = #{avatar} where id = #{userId}")
    void updateAvatar(Long userId, String avatar);
}
