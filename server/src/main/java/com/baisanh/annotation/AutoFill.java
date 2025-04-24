package com.baisanh.annotation;

import com.baisanh.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，实现自动填充功能
 * 指定目标元素类型：
 * 使用 @Target(ElementType.METHOD) 指定该注解只能应用于方法。
 * 指定保留策略：
 * 使用 @Retention(RetentionPolicy.RUNTIME) 指定该注解在运行时保留，可以在运行时通过反射获取。
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //数据库操作类型INSERT UPDATE
    OperationType value();
}
