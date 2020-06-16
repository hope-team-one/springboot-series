package com.imooc.web.config;

import com.imooc.filter.TimeFilter;
import com.imooc.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟第三方过滤器的情况，将第三方过滤器加入到过滤器中
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private TimeInterceptor timeInterceptor;
    //将自定义拦截器生效
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeInterceptor);
    }
    //如果你使用了异步的编程，那么对那个方法的拦截就需要使用下面这方式
    //
    //用来配置异步支持的 当我们使用异步的时候 拦截器要用这两个来定义
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        //这两个里面传入的是自定义的拦截类 各自需要实现各自的接口
        configurer = configurer.registerCallableInterceptors();
        configurer = configurer.registerDeferredResultInterceptors();
        super.configureAsyncSupport(configurer);
    }

    @Bean
    public FilterRegistrationBean timeFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        TimeFilter timeFilter = new TimeFilter();
        registrationBean.setFilter(timeFilter);
        List<String> url = new ArrayList<>();//可以指定过滤器在哪写url上起作用
        url.add("/*");
        registrationBean.setUrlPatterns(url);
        return registrationBean;
    }
}
