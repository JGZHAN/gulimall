package com.zhan.common.exception;

/**
 * @author ZHAN jgZHAN
 * @create 2021-01-18 15:04
 */
public enum BizCodeEnume {
    UNKONW_EXCEPTION(10000,"系统未知异常"),
    VAILD_EXCEPTION(10001,"参数格式检验失败");
    private int code;
    private String msg;
    BizCodeEnume(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
