package com.imooc.app.social.impl;

import com.imooc.core.social.SocialAuthenticationFilterProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class AppSocialAuthenticationFilterPostProcessor implements SocialAuthenticationFilterProcessor {

    @Autowired
    private AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    @Override
    public void process(SocialAuthenticationFilter socialAutheticationFilter) {
        socialAutheticationFilter.setAuthenticationSuccessHandler(imoocAuthenticationSuccessHandler);
    }
}
