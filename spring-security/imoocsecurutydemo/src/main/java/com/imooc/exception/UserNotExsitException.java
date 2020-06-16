package com.imooc.exception;

public class UserNotExsitException extends RuntimeException{
    private static final long serialVersionUID = -6112780192479692859L;

    private String id;

    public UserNotExsitException(String id){
        super("user not exist");
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
