package com.imooc.browser.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;

//@Configuration
//public class MyDaoAuthenticationProvider extends DaoAuthenticationProvider {
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Primary
//    @Override
//    protected void doAfterPropertiesSet() throws Exception {
//        setUserDetailsService(userDetailsService);
//    }
//}
