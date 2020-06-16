package com.imooc.core.authtication;

public class SecurityConstants {
    //loginpage配置的参数  登陆之前 任何没有做认证的请求都会进入到这个配置的方法里面
    public static final String DEFAULT_LOGIN_PROCESSING_URL = "/authentication/require";
    //usernamepasswordfilter来处理这个请求 这个请求需要在验证码校验类里面被拦截
    public static final String DEFAULT_LOGIN_PROCESSING_URL_FORM = "/authentication/form";
    //这个请求需要在验证码校验类里面被拦截
    public static final String DEFAULT_LOGIN_PROCESSING_URL_MOBILE = "/authentication/mobile";
    //短信验证码创建的url
    public static final String DEFAULT_SMS_CODE_CREATE_URL = "/code/sms";
    //图片验证码创建的url
    public static final String DEFAULT_IMAGE_CODE_CREATE_URL = "/code/image";

    public static final String DEFAULT_SUCCESS_URL = "/toIndex";

    public static final String DEFAULT_FAIL_URL = "/toFail";

    public static final String REGIST_URL = "/user/regist";

    public static final String QQ_LOGIN = "/auth/qq";

    public static final String SESSION_INVALID = "/session/invalid";

    public static final String LOGIN_ERROR = "/loginerror";

    public static final String DEFAULT_PARAMETER_NAME_OPENID = "openId";

    public static final String DEFAULT_PARAMETER_NAME_PROVIDERID = "providerId";

    public static final String DEFAULT_LOGIN_PROCESSING_URL_OPENID = "/authentication/openid";
}
