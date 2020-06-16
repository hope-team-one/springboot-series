package com.imooc.core.validatecode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class ValidateCodeProcessorHandler{
    //依赖搜索  获取到所有ValidateCodeProcessor的实现类
    @Autowired
    private Map<String , ValidateCodeProcessor> validateCodeProcessors;

    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type){
        ValidateCodeProcessor validateCodeProcessor = validateCodeProcessors.get(type.getType()+"CodeProcessor");
        return validateCodeProcessor;
    }
}
