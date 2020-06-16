package com.imooc.browser.session;

import com.imooc.core.properties.SecurityProperties;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户被顶了登录 session失效走这里
 * 例如A用户在A机器登录 B用户在B机器登录后 A用户在A机器操作 就会走到这里来
 */
public class SessionInformationExpiredStrategyImpl extends AbstractSeesion implements SessionInformationExpiredStrategy{
    public SessionInformationExpiredStrategyImpl(SecurityProperties securityProperties) {
        super(securityProperties);
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent eventØ) throws IOException{
        HttpServletRequest request = eventØ.getRequest();
        HttpServletResponse response = eventØ.getResponse();
        onSessionValid(request,response);
    }
}
