package com.imooc.core.validatecode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class ValidteCodeController {
    /**
     * 同样使用依赖搜索  来搜索ValidateCodeProcessor的实现类并且放到map中  然后key的值是对应实现类的类名(首字母变小写)
     * 可以根据这个规则  传入不同的type 然后使用type+CodeProcessor得到具体的实现类 type可以是image/sms
     * imageCodeProcessor/smsCodeProcessor 就可以得到对应的实现类 就可以调用到对应实现类里面方法了
     */
    @Autowired
    private Map<String , ValidateCodeProcessor> validateCodeProcessors;

    /**
     * 创建验证码，根据验证码类型不同，调用不用的接口实现
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    @GetMapping("/code/{type}")
    @ResponseBody
    public String createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {
        //根据传入的type 决定使用那个ValidateCodeProcessor的实现类实例对象  然后根据这个实例对象  调用到他父类里面的create方法
        //在create方法里面 会根据你是哪个实例调用的这个create方法 来决定send方法使用哪个实例的send方法
        validateCodeProcessors.get(type+"CodeProcessor").create(new ServletWebRequest(request,response));
        return "创建成功";
    }
//    @GetMapping("/code/image")
//    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        ImageCode imageCode = (ImageCode) imageCodeGenerator.createImageCode(new ServletWebRequest(request));
//        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageCode);
//        //将生成图片写入到请求响应中
//        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
//    }


//    @GetMapping("/code/sms")
//    public void createSms(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        ValidateCode validateCode = smsCodeGenerator.createImageCode(new ServletWebRequest(request));
//        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, validateCode);
//        //将生成图片写入到请求响应中
//        String mobile = "";
//        smsCodeSend.send(mobile,validateCode.getCode());
//    }



}
