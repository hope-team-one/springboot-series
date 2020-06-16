package com.imooc.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;

public interface SocialAuthenticationFilterProcessor {

    void process(SocialAuthenticationFilter socialAutheticationFilter);
}
