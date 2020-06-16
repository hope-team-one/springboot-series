package com.imooc.browser.exception;

import org.springframework.security.core.AuthenticationException;

public class PassWordNotMathException extends AuthenticationException {
    public PassWordNotMathException(String msg, Throwable t) {
        super(msg, t);
    }

    public PassWordNotMathException(String msg) {
        super(msg);
    }
}
