package com.prey.enums;


public enum ResponseCode {

    SUCCESS(0, "SUCCESS"),
    ERROR(100, "ERROR"),
    ILLEGAL_ARGUMENT(2, "参数错误"),
    NEED_LOGIN(10, "需要登录"),
    UNKNOWN_EXCEPTION(-1, "未知错误");

    private final int code;
    private final String desc;


    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
