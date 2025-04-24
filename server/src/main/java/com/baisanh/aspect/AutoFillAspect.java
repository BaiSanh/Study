package com.baisanh.aspect;

import com.baisanh.annotation.AutoFill;
import com.baisanh.constant.AutoFillConstant;
import com.baisanh.context.BaseContext;
import com.baisanh.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * AOP 切面
     * 切入点
     */
    @Pointcut("execution(* com.baisanh.mapper.*.*(..)) && @annotation(com.baisanh.annotation.AutoFill)")
    public void autoFillPointCat(){

    }
    /**
     * 前置通知，在方法执行前执行
     * @param joinPoint
     */
    @Before("autoFillPointCat()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始自动填充");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            log.warn("没有找到参数");
            return;
        }
        Object entity = args[0];

        Long currentId = BaseContext.getCurrentId();
        LocalDateTime now = LocalDateTime.now();

        if (operationType == OperationType.INSERT) {
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
                log.info("自动填充成功");
            } catch (Exception e) {
                log.error("自动填充失败", e);
            }
        } else if (operationType == OperationType.UPDATE) {
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
                log.info("自动填充成功");
            } catch (Exception e) {
                log.error("自动填充失败", e);
            }
        }
    }
}
