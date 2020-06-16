package com.imooc.browser.exception;

import com.google.gson.Gson;
import com.imooc.browser.LoginType;
import com.imooc.browser.support.SimpleResponse;
import com.imooc.core.authtication.SecurityConstants;
import com.imooc.core.properties.SecurityProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义的登录失败处理器，不进行页面跳转，而是返回json格式的数据
 * 错误信息返回回去不打印，请求有时候出现两次，请求由post变get都是因为前台使用button onclick造成的
 */
@Component
public class MyAuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private SecurityProperties securityProperties;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Logger logger = LoggerFactory.getLogger(getClass());

    //登陆失败就会进入到这里
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.info("成功进入自定义登录失败处理器");
//        super.setDefaultFailureUrl(SecurityConstants.DEFAULT_FAIL_URL);
//        super.onAuthenticationFailure(request,response,exception);
//        if (LoginType.JSON.getType().equals(securityProperties.getBrowser().getLoginPage())) {
//            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(exception.getMessage())));
//        } else {
////            super.setUseForward(true);//允许使用转发
////            super.setDefaultFailureUrl("/loginerror");
//            super.onAuthenticationFailure(request, response, exception);
//        }
        String v = request.getHeader("X-Requested-With");
        if(v!=null&&v.equals("XMLHttpRequest")){//说明是ajax的请求 我们需要返回json格式的数据
            Map<String, Object> map = new HashMap<>();
            map.put("code",300);
            map.put("msg",exception.getMessage());
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write(new Gson().toJson(map));
            out.flush();
            out.close();
        }else{
            //登录使用ajax请求 并且这个自定义异常处理器只会在登录的时候起作用 别的时候不会进入到这里
            //登录之前报错是进入到loginpage定义的方法 所以登录只有ajax请求 也只用处理ajax请求
        }

    }
}
