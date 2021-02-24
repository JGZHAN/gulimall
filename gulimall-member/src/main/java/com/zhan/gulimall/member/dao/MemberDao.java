package com.zhan.gulimall.member.dao;

import com.zhan.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author zhanjg
 * @email 2392323345@qq.com
 * @date 2020-12-07 23:45:39
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
