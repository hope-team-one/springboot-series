package com.imooc.core.validatecode;

import com.imooc.core.validatecode.ValidateCode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

@Component
public interface ValidateCodeGenerator {
    ValidateCode createImageCode(ServletWebRequest request);
}
