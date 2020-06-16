package com.imooc.browser;

import com.google.gson.Gson;
import com.imooc.browser.support.SimpleResponse;
import com.imooc.browser.support.SocialUserInfo;
import com.imooc.core.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BrowserSecurityController {
    //从请求的缓存中拿到引发跳转的请求
    private RequestCache requestCache = new HttpSessionRequestCache();
    private Logger logger = LoggerFactory.getLogger(getClass());
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();//做跳转
    @Autowired
    private SecurityProperties securityProperties;
//    @Autowired
//    private ProviderSignInUtils providerSignInUtils;

    /**
     * 当需要身份认证时，跳转到这里
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)//401 未授权
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);//拿到引发跳转的请求
        if (savedRequest != null) {//不为空 有这个请求
            String target = savedRequest.getRedirectUrl();//拿到引发跳转的url
            logger.info("引发跳转的请求是：" + target);
            if (StringUtils.endsWithIgnoreCase(target, ".html")) {//如果以html结尾 说明是html请求
                redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
            }
        }
//        throw new UserNotExsitException("不存在");
        return new SimpleResponse("访问的服务需要身份认证，请引导用户到登陆页");
    }

    /**
     * session失效造成的认证失败后，访问的url
     *
     * @param request
     * @param response
     */
    @RequestMapping("/session/invalid")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity sessionInvalid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String target = request.getRequestURL().toString();//拿到引发跳转的url
        String v = request.getHeader("X-Requested-With");
        if (target != null) {//不为空 有这个请求
            if (StringUtils.endsWithIgnoreCase(target, ".html")) {//如果以html结尾 说明是html请求
                redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
            }else {
                if (v != null && v.equals("XMLHttpRequest")) {//说明是ajax的请求 我们需要返回json格式的数据
                    Map<String, Object> map = new HashMap<>();
                    map.put("code", 300);
                    map.put("msg", "success");
//                response.setContentType("application/json;charset=UTF-8");
//                PrintWriter out = response.getWriter();
//                out.write(new Gson().toJson(map));
//                out.flush();
//                out.close();
                    return ResponseEntity.ok(map);
                } else {
                    redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
                }
            }
        }
        return ResponseEntity.ok(null);
    }

    @RequestMapping("/toIndex")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String toIndex() {
        System.out.println("成功登陆");
        return "";
    }

    @RequestMapping("/toFail")
    @ResponseBody
    public String toFail() {
        System.out.println("aaaaa");
        return "aaa";
    }

    /**
     * 给注册页面获取用户的信息
     * @param request
     * @return
     */
//    @GetMapping("/social/user")
//    @ResponseBody
//    public SocialUserInfo getSocialUserInfo(HttpServletRequest request){
//        SocialUserInfo userInfo = new SocialUserInfo();
//        //从session里面拿到connect
//        Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
//        userInfo.setProviderId(connection.getKey().getProviderId());
//        userInfo.setProviderUserId(connection.getKey().getProviderUserId());
//        userInfo.setHeadimg(connection.getImageUrl());
//        userInfo.setNickname(connection.getDisplayName());
//        return userInfo;
//    }

    @RequestMapping("/test")
    public void test(HttpServletRequest request,HttpServletResponse response){
        String v = request.getHeader("X-Requested-With");
        System.out.println(v);
    }
}
