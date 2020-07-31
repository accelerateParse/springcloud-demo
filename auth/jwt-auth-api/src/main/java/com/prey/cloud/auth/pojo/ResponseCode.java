package com.prey.cloud.auth.pojo;

/**
 * @author prey
 * @description:
 **/
public enum ResponseCode {
    SUCCESS(1),USER_NOT_FOUND(2),INCORRECT_PWD(3);

    private int code;

    private ResponseCode(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
