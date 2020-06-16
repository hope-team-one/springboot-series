package com.imooc.browser.session;

import com.google.gson.Gson;
import com.imooc.core.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class AbstractSeesion {

    private SecurityProperties securityProperties;
    private static final boolean createNewSession = true;
    protected void onSessionValid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!request.getRequestURI().equals("/code/image")&&!request.getRequestURI().equals("/code/sms")) {//这两个不需要判断session
            if (createNewSession) {
                request.getSession();
            }
            //拿到引发跳转的url0
            String target = request.getRequestURL().toString();
            String v = request.getHeader("X-Requested-With");
            if (target != null) {//不为空 有这个请求
                if (StringUtils.endsWithIgnoreCase(target, ".html")) {//如果以html结尾 说明是html请求 重定向到我们配置的登陆的界面 如果用户有自定义这个登陆界面并且配置了 那么就会重定向到用户配置的这个自定义界面
                    response.sendRedirect(securityProperties.getBrowser().getLoginPage());
                } else {
                    if (v != null && v.equals("XMLHttpRequest")) {//说明是ajax的请求 我们需要返回json格式的数据
                        Map<String, Object> map = new HashMap<>();
                        map.put("code", 300);
                        map.put("msg", "session已经失效了");
                        response.setContentType("application/json;charset=UTF-8");
                        PrintWriter out = response.getWriter();
                        out.write(new Gson().toJson(map));
                        out.flush();
                        out.close();
                    } else {
                        response.sendRedirect(securityProperties.getBrowser().getLoginPage());
                    }
                }
            }
        }
    }

    public AbstractSeesion(SecurityProperties securityProperties){
        this.securityProperties = securityProperties;
    }
}
