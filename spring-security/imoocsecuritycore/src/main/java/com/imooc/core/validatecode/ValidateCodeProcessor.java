package com.imooc.core.validatecode;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;
@Component
public interface ValidateCodeProcessor {
    //验证码放入session时的前缀
    public static final String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";
    /**
     * 创建校验码
     */
    void create(ServletWebRequest request) throws Exception;

    void validate(ServletWebRequest request) throws ServletRequestBindingException;
}
