package com.imooc.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Date;
//让过滤器起作用
//@Component
public class TimeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("my timeFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("my timeFilter start");
        long start = new Date().getTime();
        //掉后续的过滤器
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println(new Date().getTime()-start);
        System.out.println("my timeFilter finish");
    }

    @Override
    public void destroy() {
        System.out.println("my timeFilter ded");
    }
}
