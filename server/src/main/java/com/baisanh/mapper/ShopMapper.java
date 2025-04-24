package com.baisanh.mapper;

import com.baisanh.entity.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ShopMapper {

    /**
     * 查询店铺信息
     * @return
     */
    @Select("select * from shop limit 1")
    Shop getShopInfo();

    /**
     * 更新店铺信息
     * @param shop
     */
    void update(Shop shop);

    /**
     * 更新店铺公告
     * @param notice
     */
    @Update("update shop set notice = #{notice}, update_time = now() where id = 1")
    void updateNotice(String notice);

    /**
     * 查询店铺公告
     * @return
     */
    @Select("select notice from shop limit 1")
    String getNotice();
} 