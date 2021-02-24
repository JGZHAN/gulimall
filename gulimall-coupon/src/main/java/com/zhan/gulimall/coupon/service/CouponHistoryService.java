package com.zhan.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhan.common.utils.PageUtils;
import com.zhan.gulimall.coupon.entity.CouponHistoryEntity;

import java.util.Map;

/**
 * 优惠券领取历史记录
 *
 * @author zhanjg
 * @email 2392323345@qq.com
 * @date 2020-12-07 00:20:25
 */
public interface CouponHistoryService extends IService<CouponHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

