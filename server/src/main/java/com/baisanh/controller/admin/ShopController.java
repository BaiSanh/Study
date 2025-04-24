package com.baisanh.controller.admin;

import com.baisanh.entity.Shop;
import com.baisanh.result.Result;
import com.baisanh.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;
    
    @Autowired
    private ShopService shopService;

    /**
     * 设置店铺的营业状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺的营业状态")
    public Result setStatus(@PathVariable Integer status){
        log.info("设置店铺的营业状态为：{}",status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(KEY,status);
        return Result.success();
    }

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
     * 更新店铺信息
     * @param shop
     * @return
     */
    @PutMapping("/info")
    @ApiOperation("更新店铺信息")
    public Result updateShopInfo(@RequestBody Shop shop){
        log.info("更新店铺信息：{}", shop);
        shopService.updateShopInfo(shop);
        return Result.success();
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
    
    /**
     * 更新店铺公告
     * @param params 包含notice字段的Map
     * @return
     */
    @PutMapping("/notice")
    @ApiOperation("更新店铺公告")
    public Result updateShopNotice(@RequestBody Map<String, String> params){
        String notice = params.get("notice");
        log.info("更新店铺公告：{}", notice);
        shopService.updateShopNotice(notice);
        return Result.success();
    }
}
