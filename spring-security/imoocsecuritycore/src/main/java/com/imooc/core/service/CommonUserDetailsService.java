package com.imooc.core.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CommonUserDetailsService extends UserDetailsService {
    UserDetails loadByPhone(String phone);
}
