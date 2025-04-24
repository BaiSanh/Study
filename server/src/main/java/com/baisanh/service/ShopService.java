package com.baisanh.service;

import com.baisanh.entity.Shop;

public interface ShopService {

    /**
     * 获取店铺信息
     * @return
     */
    Shop getShopInfo();

    /**
     * 更新店铺信息
     * @param shop
     */
    void updateShopInfo(Shop shop);

    /**
     * 获取店铺公告
     * @return
     */
    String getShopNotice();

    /**
     * 更新店铺公告
     * @param notice
     */
    void updateShopNotice(String notice);
} 