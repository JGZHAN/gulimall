package com.zhan.gulimall.product.vo;

import lombok.Data;

/**
 * @author ZHAN jgZHAN
 * @create 2021-01-25 1:48
 */
@Data
public class AttrRespVo extends AttrVo {

    private String catelogName;
    private String groupName;
    private Long[] catelogPath;
}
