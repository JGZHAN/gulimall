package com.zhan.gulimall.product.service.impl;

import com.zhan.common.constant.ProducConstant;
import com.zhan.common.utils.PageUtils;
import com.zhan.common.utils.Query;
import com.zhan.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.zhan.gulimall.product.dao.AttrDao;
import com.zhan.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.zhan.gulimall.product.entity.AttrEntity;
import com.zhan.gulimall.product.service.AttrService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zhan.gulimall.product.dao.AttrGroupDao;
import com.zhan.gulimall.product.entity.AttrGroupEntity;
import com.zhan.gulimall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    AttrDao attrDao;
    @Autowired
    AttrService attrService;
    @Autowired
    AttrAttrgroupRelationDao relationDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
//
//        if (catelogId==0){
//            IPage<AttrGroupEntity> iPage=this.page(new Query<AttrGroupEntity>().getPage(params),
//                    new QueryWrapper<AttrGroupEntity>());
////            String key= (String) params.get("key");
////            QueryWrapper<AttrGroupEntity> queryWrapper=new QueryWrapper<AttrGroupEntity>();
////            if (StringUtils.isNotEmpty(key)){
////                queryWrapper.and((obj)->{
////                    obj.eq("attr_group_id",key).or().like("attr_group_name",key);
////                });
////            }
////            IPage<AttrGroupEntity> iPage=this.page(new Query<AttrGroupEntity>().getPage(params),queryWrapper);
//            return new PageUtils(iPage);
//        }else {
//            String key= (String) params.get("key");
//            QueryWrapper<AttrGroupEntity> queryWrapper=new QueryWrapper<AttrGroupEntity>().eq("catelog_id",catelogId);
//            if (StringUtils.isNotEmpty(key)){
//                queryWrapper.and((obj)->{
//                    obj.eq("attr_group_id",key).or().like("attr_group_name",key);
//                });
//            }
//            IPage<AttrGroupEntity> iPage=this.page(new Query<AttrGroupEntity>().getPage(params),queryWrapper);
//            return new PageUtils(iPage);
//        }
        QueryWrapper<AttrGroupEntity> queryWrapper=new QueryWrapper<AttrGroupEntity>();
        String key= (String) params.get("key");
        if (StringUtils.isNotEmpty(key)){
            queryWrapper.and((obj)->{
                obj.eq("attr_group_id",key).or().like("attr_group_name",key);
            });
        }
        if (catelogId==0){

        }else {
           queryWrapper.eq("catelog_id",catelogId);
        }
        IPage<AttrGroupEntity> iPage=this.page(new Query<AttrGroupEntity>().getPage(params),queryWrapper);
        return new PageUtils(iPage);



    }

    @Override
    public PageUtils getNoRelationAttr(Long attrgroupId, Map<String, Object> params) {
        //1,当前分组只能关联所属分类的属性
        AttrGroupEntity attrGroupEntity = this.baseMapper.selectById(attrgroupId);
        Long catelogId=attrGroupEntity.getCatelogId();
        //2，当前分组只能关联其他分组没有关联的属性

        //2.1,当前分类的其他分组
        List<AttrGroupEntity> groupEntities = this.baseMapper.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));

        List<Long> groupIds = groupEntities.stream().map((entityClass) -> {
            return entityClass.getAttrGroupId();
        }).collect(Collectors.toList());
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(
                new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", groupIds));
        List<Long> attrIds = relationEntities.stream().map((relationEntity) -> {
            return relationEntity.getAttrId();
        }).collect(Collectors.toList());
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().
                eq("catelog_id",catelogId).
                eq("attr_type", ProducConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (attrIds!=null&&attrIds.size()>0){

            queryWrapper.notIn("attr_id", attrIds);
        }
        String key= (String) params.get("key");
        if (StringUtils.isNotEmpty(key)){
            queryWrapper.and((wrapper)->{
                wrapper.eq("attr_id",key).
                        or().like("attr_name",key).
                            or().like("value_select",key);
            });
        }
        IPage<AttrEntity> iPage=attrService.page(new Query<AttrEntity>().getPage(params),queryWrapper);


        return new PageUtils(iPage);
    }


}