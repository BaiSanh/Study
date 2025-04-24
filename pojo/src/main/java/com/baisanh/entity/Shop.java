package com.baisanh.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 店铺信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 店铺名称
    private String name;
    
    // 店铺地址
    private String address;
    
    // 店铺电话
    private String phone;
    
    // 营业时间
    private String businessHours;
    
    // 店铺介绍
    private String introduction;
    
    // 店铺LOGO
    private String logo;
    
    // 店铺公告
    private String notice;
    
    // 创建时间
    private LocalDateTime createTime;
    
    // 更新时间
    private LocalDateTime updateTime;
} 