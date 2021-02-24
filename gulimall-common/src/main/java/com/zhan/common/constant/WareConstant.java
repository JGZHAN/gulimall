package com.zhan.common.constant;

import lombok.Getter;

/**
 * @author ZHAN jgZHAN
 * @create 2021-02-23 18:38
 */
public class WareConstant {
    @Getter
    public enum PurchaseStatusEnum{
        CREATE(0,"新建"),ASSIGNED(1,"已分配"),
        RECEIVE(2,"已领取"),FINISH(3,"已完成"),
        HASERROR(4,"有异常");
        private int code;
        private String msg;
        PurchaseStatusEnum(int code,String msg){
            this.code=code;
            this.msg=msg;
        }

    }
    @Getter
    public enum PurchaseDetailStatusEnum{
        CREATE(0,"新建"),ASSIGNED(1,"已分配"),
        BUYING(2,"正在采购"),FINISH(3,"已完成"),
        HASERROR(4,"有异常");
        private int code;
        private String msg;
        PurchaseDetailStatusEnum(int code,String msg){
            this.code=code;
            this.msg=msg;
        }

    }
}
