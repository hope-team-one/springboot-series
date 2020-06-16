package com.imooc.browser.exception;

import com.google.gson.Gson;
import com.imooc.core.authtication.SecurityConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义登录成功处理器，不进行页面跳转，而是返回json格式的数据
 * 如果ajax请求进行重定向或者是转发的话，是不会生效的
 */
@Component
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        System.out.println("自定义成功处理逻辑--------------------------");

//        //创建一个SecurityContext对象，并且设置上一步得到的Authentication
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        HttpSession session = request.getSession();
//
//        //将上一部得到的SecurityContext对象放入session中。到此，自定义用户信息的处理已经完成
//        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        System.out.println("--------------结束认证");
//        System.out.println("将用户信息存储在session中");
// 设置默认登陆成功的跳转地址 可以写成controller方法 去controller里面加载数据然后放到model里面 返回一个视图
//        super.setDefaultTargetUrl("/index111.html");
//        super.onAuthenticationSuccess(request, response, authentication); // 重定向到你上一次请求的url里面
        String v = request.getHeader("X-Requested-With");
        if(v!=null&&v.equals("XMLHttpRequest")) {//说明是ajax的请求 我们需要返回json格式的数据
            Map<String, Object> map = new HashMap<>();
            map.put("code", 200);
            map.put("msg", "success");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write(new Gson().toJson(map));
            out.flush();
            out.close();
        }else{
            super.onAuthenticationSuccess(request, response, authentication); // 重定向到你上一次请求的url里面
        }
    }
}
