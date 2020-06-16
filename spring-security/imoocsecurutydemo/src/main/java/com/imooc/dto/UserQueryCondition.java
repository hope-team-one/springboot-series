package com.imooc.dto;

import io.swagger.annotations.ApiModelProperty;

public class UserQueryCondition {
    private String username;
    @ApiModelProperty(value = "用户年龄起始值")
    private int age;
    @ApiModelProperty(value = "用户年龄末尾值")
    private int ageTo;
    private String anything;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(int ageTo) {
        this.ageTo = ageTo;
    }

    public String getAnytihing() {
        return anything;
    }

    public void setAnytihing(String anything) {
        this.anything = anything;
    }
}
