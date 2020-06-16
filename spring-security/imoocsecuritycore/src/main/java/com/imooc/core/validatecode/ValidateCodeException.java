package com.imooc.core.validatecode;


import org.springframework.security.core.AuthenticationException;
//Auth是在身份认证过程中异常的基类
public class ValidateCodeException extends AuthenticationException {
    private static final long serialVersionUID = -7285211528095468156L;

    public ValidateCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
