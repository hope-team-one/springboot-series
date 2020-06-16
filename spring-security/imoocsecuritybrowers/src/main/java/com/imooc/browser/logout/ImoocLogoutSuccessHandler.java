package com.imooc.browser.logout;

import com.google.gson.Gson;
import com.imooc.core.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ImoocLogoutSuccessHandler implements LogoutSuccessHandler {
    private SecurityProperties securityProperties;

    public ImoocLogoutSuccessHandler(SecurityProperties securityProperties){
        this.securityProperties = securityProperties;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("退出成功");
        //用户配置了登出成功后请求的url
        if(!StringUtils.isBlank(securityProperties.getBrowser().getSignOutUrl())){
            response.sendRedirect(securityProperties.getBrowser().getSignOutUrl());
        }else{
            String v = request.getHeader("X-Requested-With");
            //说明是ajax的请求 返回json数据 因为可能会是前后端分离项目 前端通过ajax访问后台处理完 需要返回一个退出成功的标识给前端 前端来做跳转
            if(v != null && v.equals("XMLHttpRequest")){
                Map<String, Object> map = new HashMap<>();
                map.put("code", 200);
                map.put("msg", "success");
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.write(new Gson().toJson(map));out.flush();out.close();
            }else{//都不是则跳转到登陆界面
                response.sendRedirect(securityProperties.getBrowser().getLoginPage());
            }
        }

    }
}
