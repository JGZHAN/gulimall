package com.zhan.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhan.common.utils.PageUtils;
import com.zhan.gulimall.ware.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zhanjg
 * @email 2392323345@qq.com
 * @date 2021-02-23 15:52:09
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PurchaseDetailEntity> listDetailBypurchaseId(Long id);
}

