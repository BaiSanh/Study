package com.baisanh.service.impl;

import com.baisanh.entity.Shop;
import com.baisanh.mapper.ShopMapper;
import com.baisanh.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    /**
     * 获取店铺信息
     * @return
     */
    public Shop getShopInfo() {
        log.info("获取店铺信息");
        return shopMapper.getShopInfo();
    }

    /**
     * 更新店铺信息
     * @param shop
     */
    public void updateShopInfo(Shop shop) {
        log.info("更新店铺信息: {}", shop);
        shopMapper.update(shop);
    }

    /**
     * 获取店铺公告
     * @return
     */
    public String getShopNotice() {
        log.info("获取店铺公告");
        return shopMapper.getNotice();
    }

    /**
     * 更新店铺公告
     * @param notice
     */
    public void updateShopNotice(String notice) {
        log.info("更新店铺公告: {}", notice);
        shopMapper.updateNotice(notice);
    }
}