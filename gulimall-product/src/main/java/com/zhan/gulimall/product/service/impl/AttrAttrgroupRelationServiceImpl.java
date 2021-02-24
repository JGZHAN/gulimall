package com.zhan.gulimall.product.service.impl;

import com.zhan.gulimall.product.entity.AttrEntity;
import com.zhan.gulimall.product.entity.AttrGroupEntity;
import com.zhan.gulimall.product.service.AttrGroupService;
import com.zhan.gulimall.product.service.AttrService;
import com.zhan.gulimall.product.vo.AttrGroupRelationVo;
import com.zhan.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhan.common.utils.PageUtils;
import com.zhan.common.utils.Query;

import com.zhan.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.zhan.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.zhan.gulimall.product.service.AttrAttrgroupRelationService;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {



    @Autowired
    AttrGroupService attrGroupService;
    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void deleteRelation(AttrGroupRelationVo[] relationVos) {
//        this.baseMapper.delete(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",1L).eq("attr_group_id",1L));

        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(relationVos).stream().map((item) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        this.baseMapper.deleteBatchRelation(entities);
    }

    @Override
    public void saveBatch(List<AttrGroupRelationVo> relationVos) {
        List<AttrAttrgroupRelationEntity> relationEntities = relationVos.stream().map((relationVo) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(relationVo, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        this.saveBatch(relationEntities);
    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {

        List<AttrGroupEntity> attrGroupEntities = attrGroupService.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<AttrGroupWithAttrsVo> vos = attrGroupEntities.stream().map(entity -> {
            AttrGroupWithAttrsVo vo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(entity, vo);
            List<AttrEntity> attr = attrService.getRelationAttr(vo.getAttrGroupId());
            vo.setAttrs(attr);
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }

}