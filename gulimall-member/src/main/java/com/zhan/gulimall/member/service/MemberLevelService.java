package com.zhan.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhan.common.utils.PageUtils;
import com.zhan.gulimall.member.entity.MemberLevelEntity;

import java.util.Map;

/**
 * 会员等级
 *
 * @author zhanjg
 * @email 2392323345@qq.com
 * @date 2020-12-07 23:45:39
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

