package com.flash.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.flash.reggie.common.BaseContext;
import com.flash.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        // 获取本次请求的uri
        String requestURI = request.getRequestURI();
        log.info("开始过滤请求 {}", requestURI);
        // 定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        // 判断请求是否不要需要进行处理
        if(check(urls, requestURI)){
            log.info("请求无需处理 {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        // 判断是否员工登录状态
        if(request.getSession().getAttribute("employee") != null){
            Long employeeId = (Long)request.getSession().getAttribute("employee");
            log.info("用户已经登录 {}", employeeId);
            // 设置线程当前正在运行的员工id
            BaseContext.setCurrentUserId(employeeId);
            // 继续执行其他过滤器
            filterChain.doFilter(request, response);
            return;
        }

        // 判断是否用户登录状态
        if(request.getSession().getAttribute("user") != null){
            Long userId = (Long)request.getSession().getAttribute("user");
            log.info("用户已经登录 {}", userId);
            // 设置线程当前正在运行的员工id
            BaseContext.setCurrentUserId(userId);
            // 继续执行其他过滤器
            filterChain.doFilter(request, response);
            return;
        }

        // 若未登录，则通过输出流的方式 返回结果
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    private boolean check(String[] urls, String requestUri){
        for(String url : urls){
            if(PATH_MATCHER.match(url, requestUri)){
                return true;
            }
        }
        return false;
    }

}
