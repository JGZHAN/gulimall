package com.zhan.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author ZHAN jgZHAN
 * @create 2021-02-23 23:58
 */
@Data
public class PurchaseDoneVo {
    private Long id;
    private List<PurchaseItemDoneVo> items;
}
