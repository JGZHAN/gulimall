package com.zhan.gulimall.product.service.impl;

import com.zhan.common.constant.ProducConstant;
import com.zhan.common.utils.PageUtils;
import com.zhan.common.utils.Query;
import com.zhan.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.zhan.gulimall.product.dao.AttrGroupDao;
import com.zhan.gulimall.product.dao.CategoryDao;
import com.zhan.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.zhan.gulimall.product.entity.AttrGroupEntity;
import com.zhan.gulimall.product.entity.CategoryEntity;
import com.zhan.gulimall.product.service.CategoryService;
import com.zhan.gulimall.product.vo.AttrRespVo;
import com.zhan.gulimall.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zhan.gulimall.product.dao.AttrDao;
import com.zhan.gulimall.product.entity.AttrEntity;
import com.zhan.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    CategoryDao categoryDao;
    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity=new AttrEntity();
        System.out.println(attrEntity.getAttrId());
        BeanUtils.copyProperties(attr,attrEntity);
        //保存基本数据
        this.save(attrEntity);

        System.out.println(attrEntity.getAttrId());
        System.out.println(attr.getAttrGroupId());
        //保存关联属性数据
        if (attr.getAttrType()== ProducConstant.AttrEnum.ATTR_TYPE_BASE.getCode()&&attr.getAttrGroupId()!=null){

            System.out.println("attr.getAttrGroupId()"+attr.getAttrGroupId());
            AttrAttrgroupRelationEntity relationEntity=new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            relationDao.insert(relationEntity);
        }


    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {

        QueryWrapper<AttrEntity> queryWrapper=
                new QueryWrapper<AttrEntity>().eq(
                        "attr_type","base".equalsIgnoreCase(type)?
                                ProducConstant.AttrEnum.ATTR_TYPE_BASE.getCode() :
                                ProducConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

        if(catelogId!=0){
            queryWrapper.eq("catelog_id",catelogId);
        }
        String key= (String) params.get("key");
        if(StringUtils.isNotEmpty(key)){
            queryWrapper.and((wrapper)->{
                wrapper.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> page=this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper);
        PageUtils pageUtils=new PageUtils(page);
        List<AttrEntity> records=page.getRecords();
        List<AttrRespVo> respVos = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

//            设置分类和分组的名字
            if("base".equalsIgnoreCase(type)){
                AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));

                if (relationEntity!=null&&relationEntity.getAttrGroupId()!=null){
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }


            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity!=null){
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;

        }).collect(Collectors.toList());
        pageUtils.setList(respVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrEntity attrEntity=this.getById(attrId);
        AttrRespVo respVo=new AttrRespVo();
        BeanUtils.copyProperties(attrEntity,respVo);

        //设置分组信息
        if (attrEntity.getAttrType()== ProducConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity=relationDao.selectOne(
                    new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attrId));
            if (attrAttrgroupRelationEntity!=null){
                respVo.setAttrGroupId(attrAttrgroupRelationEntity.getAttrGroupId());
                AttrGroupEntity attrGroupEntity=attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
                if (attrGroupEntity!=null){
                    respVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }

        //设置分类信息
        respVo.setCatelogPath(categoryService.findCatelogPath(respVo.getCatelogId()));
        CategoryEntity categoryEntity=categoryDao.selectById(respVo.getCatelogId());
        if (categoryEntity!=null){
            respVo.setCatelogName(categoryEntity.getName());
        }
//        respVo.setCatelogPath();
//        respVo.setAttrGroupId();

        return respVo;
    }

    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity=new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.updateById(attrEntity);
        if (attr.getAttrType()== ProducConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            //修改关联表的分组id
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attr.getAttrId());
            System.out.println("111111111111111111111111111111111"+attr.getAttrId());
            int count = relationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            if (count > 0) {
                relationDao.update(relationEntity, new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            } else {
                relationDao.insert(relationEntity);
            }
        }
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        List<Long> attrIds = relationEntities.stream().map((attr) -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        List<AttrEntity> entities =null;
        if (attrIds!=null&&attrIds.size()>0){
            entities = this.listByIds(attrIds);
        }

        return entities;
    }

}