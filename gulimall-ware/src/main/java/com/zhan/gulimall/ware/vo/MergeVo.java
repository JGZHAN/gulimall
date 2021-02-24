package com.zhan.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author ZHAN jgZHAN
 * @create 2021-02-23 18:23
 */
@Data
public class MergeVo {
    private Long purchaseId;//: 1, //整单id
    private List<Long> items;//:[1,2,3,4] //合并项集合
}
