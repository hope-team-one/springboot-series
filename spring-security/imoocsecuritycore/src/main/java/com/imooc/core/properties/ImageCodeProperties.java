package com.imooc.core.properties;

/**
 * 图形验证码默认配置
 */
public class ImageCodeProperties extends SmsCodeProperties{
    //如果用户在配置文件里面配置了值，那么这里的默认值就会被用户配置的值覆盖 优先使用用户配置的值
    public ImageCodeProperties(){
        setLength(4);
    }
    private int width = 67;
    private int height = 23;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
