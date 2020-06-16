package com.imooc.code;

import com.imooc.core.validatecode.image.ImageCode;
import com.imooc.core.validatecode.ValidateCodeGenerator;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
//用户这样写 可以直接覆盖掉我们内部默认的验证码生成器 实现用户自定义生成器
//@Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {
    @Override
    public ImageCode createImageCode(ServletWebRequest request) {
        System.out.println("用户自定义的验证码生成器");
        return null;
    }
}
