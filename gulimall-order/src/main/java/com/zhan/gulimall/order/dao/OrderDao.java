package com.zhan.gulimall.order.dao;

import com.zhan.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author zhanjg
 * @email 2392323345@qq.com
 * @date 2020-12-07 23:54:49
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
