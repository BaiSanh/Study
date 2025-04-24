package com.baisanh.controller.user;

import com.baisanh.entity.Shop;
import com.baisanh.result.Result;
import com.baisanh.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;
    
    @Autowired
    private ShopService shopService;

    /**
     * 获取店铺的营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    public Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取到店铺的营业状态为：{}",status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }
    
    /**
     * 获取店铺信息
     * @return
     */
    @GetMapping("/info")
    @ApiOperation("获取店铺信息")
    public Result<Shop> getShopInfo(){
        Shop shop = shopService.getShopInfo();
        log.info("获取店铺信息：{}", shop);
        return Result.success(shop);
    }
    
    /**
     * 获取店铺公告
     * @return
     */
    @GetMapping("/notice")
    @ApiOperation("获取店铺公告")
    public Result<String> getShopNotice(){
        String notice = shopService.getShopNotice();
        log.info("获取店铺公告：{}", notice);
        return Result.success(notice);
    }
}
