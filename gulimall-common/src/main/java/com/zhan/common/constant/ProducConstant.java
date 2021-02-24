package com.zhan.common.constant;

import lombok.Getter;

/**
 * @author ZHAN jgZHAN
 * @create 2021-01-25 20:27
 */
public class ProducConstant {
    @Getter
    public enum AttrEnum{
        ATTR_TYPE_BASE(1,"基本属性"),ATTR_TYPE_SALE(0,"销售属性");
        private int code;
        private String msg;
        AttrEnum(int code,String msg){
            this.code=code;
            this.msg=msg;
        }

    }
}
