package com.zhan.gulimall.product.dao;

import com.zhan.gulimall.product.entity.CommentReplayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 * 
 * @author zhanjg
 * @email 2392323345@qq.com
 * @date 2020-12-06 01:14:26
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplayEntity> {
	
}
