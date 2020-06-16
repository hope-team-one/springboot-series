package com.imooc.core.validatecode;

import com.imooc.core.authtication.SecurityConstants;
import com.imooc.core.properties.SecurityProperties;
import com.imooc.core.validatecode.image.ImageCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//这个父类保证过滤器每次只调用一次
//实现init的作用是在其他参数都初始化完之后 去初始化我们的urls的值
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    AuthenticationFailureHandler authenticationFailureHandler;
    /**
     * 存放所有需要校验码的url
     */
    private Map<String,ValidateCodeType> urlMap = new HashMap<String,ValidateCodeType>();

    private Set<String> urls = new HashSet<>();
    private SecurityProperties securityProperties;
    private ValidateCodeProcessorHandler validateCodeProcessorHandler;
    /**
     * 为urlmap赋值  图片验证和短信验证统一在一个方法里面进行
     * @throws ServletException
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        //将需要进行验证码校验的图片验证请求url加入到map中
        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM,ValidateCodeType.IMAGE_CODE_TYPE);
        addUrlToMap(securityProperties.getCode().getImage().getUrl(),ValidateCodeType.IMAGE_CODE_TYPE);
        //将需要进行验证码校验的短信验证请求url加入到map中
        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,ValidateCodeType.SMS_CODE_TYPE);
        addUrlToMap(securityProperties.getCode().getSms().getUrl(),ValidateCodeType.SMS_CODE_TYPE);
    }

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        ValidateCodeType validateCodeType = getValidateCodeType(request);//根据请求url判断是不是需要进行验证码验证的请求
        if(validateCodeType!=null){
            try {
                String v = request.getHeader("X-Requested-With");
                validateCodeProcessorHandler.findValidateCodeProcessor(validateCodeType).validate(new ServletWebRequest(request,httpServletResponse));
                filterChain.doFilter(request,httpServletResponse);
            } catch (ValidateCodeException e) {
                //把错误信息返回回去  他在这个错误处理器里面肯定获取了这个异常信息并且打印在页面
                //这个对象实际上是我自己定义的异常处理器，因为我将我自定义的异常处理器继承了这个类，然后将自定义的通过set方法，赋值给了这个对象
                authenticationFailureHandler.onAuthenticationFailure(request, httpServletResponse, e);
                return;
            }
        } else {//不是登陆请求
            filterChain.doFilter(request, httpServletResponse);
        }
    }

    /**
     * 获取校验码类型，判断请求是否需要进行校验，不需要返回null
     * @param request
     * @return
     */
    public ValidateCodeType getValidateCodeType(HttpServletRequest request){
        String uri = request.getRequestURI();
        if(urlMap.containsKey(uri)){//说明当前请求需要进行验证码校验
            return urlMap.get(uri);//得到当前key的value
        }
        return null;
    }

    public SessionStrategy getSessionStrategy() {
        return sessionStrategy;
    }

    public void setSessionStrategy(SessionStrategy sessionStrategy) {
        this.sessionStrategy = sessionStrategy;
    }

    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public Set<String> getUrls() {
        return urls;
    }

    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }

    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public ValidateCodeProcessorHandler getValidateCodeProcessorHandler() {
        return validateCodeProcessorHandler;
    }

    public void setValidateCodeProcessorHandler(ValidateCodeProcessorHandler validateCodeProcessorHandler) {
        this.validateCodeProcessorHandler = validateCodeProcessorHandler;
    }

    protected void addUrlToMap(String urlString, ValidateCodeType type){
        if(StringUtils.isNotBlank(urlString)){
            String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString,",");
            for (String url : urls) {//循环传入的所有url，放入到urlmap里面
                urlMap.put(url,type);
            }
        }
    }
}
