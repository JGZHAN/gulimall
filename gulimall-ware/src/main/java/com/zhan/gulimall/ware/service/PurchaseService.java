package com.zhan.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhan.common.utils.PageUtils;
import com.zhan.gulimall.ware.entity.PurchaseEntity;
import com.zhan.gulimall.ware.vo.MergeVo;
import com.zhan.gulimall.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author zhanjg
 * @email 2392323345@qq.com
 * @date 2021-02-23 15:52:09
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    void received(List<Long> ids);

    void done(PurchaseDoneVo DoneVo);
}

