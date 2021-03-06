package com.zhan.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhan.common.utils.PageUtils;
import com.zhan.gulimall.coupon.entity.SeckillSkuNoticeEntity;

import java.util.Map;

/**
 * 秒杀商品通知订阅
 *
 * @author zhanjg
 * @email 2392323345@qq.com
 * @date 2020-12-07 00:20:25
 */
public interface SeckillSkuNoticeService extends IService<SeckillSkuNoticeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

