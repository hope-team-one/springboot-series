package com.imooc.browser.session;

import com.imooc.core.properties.SecurityProperties;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * session失效处理方法
 */
public class InvalidSessionStrategyImpl extends AbstractSeesion implements InvalidSessionStrategy {
    public InvalidSessionStrategyImpl(SecurityProperties securityProperties) {
        super(securityProperties);
    }
    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        onSessionValid(request,response);
    }
}
