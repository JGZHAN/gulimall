package com.zhan.gulimall.product.vo;

import com.zhan.gulimall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author ZHAN jgZHAN
 * @create 2021-01-27 0:30
 */
@Data
public class AttrGroupWithAttrsVo {
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;
}
