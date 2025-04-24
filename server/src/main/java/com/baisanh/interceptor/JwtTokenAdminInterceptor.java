package com.baisanh.interceptor;

import com.baisanh.constant.JwtClaimsConstant;
import com.baisanh.context.BaseContext;
import com.baisanh.properties.JwtProperties;
import com.baisanh.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            // 当前拦截到的不是动态方法，直接放行
            return true;
        }

        // 尝试解析 adminToken
        String adminToken = request.getHeader(jwtProperties.getAdminTokenName());
        if (adminToken != null) {
            try {
                log.info("admin jwt校验:{}", adminToken);
                Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), adminToken);
                Long adminId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
                log.info("当前管理员登录id：{}", adminId);
                BaseContext.setCurrentId(adminId);
                return true;
            } catch (Exception ex) {
                log.warn("admin jwt校验失败:{}", adminToken, ex);
            }
        }

        // 如果没有有效的令牌，响应401状态码
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}
