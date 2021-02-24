package com.zhan.gulimall.product.service.impl;

import com.zhan.common.utils.PageUtils;
import com.zhan.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zhan.gulimall.product.dao.ProductAttrValueDao;
import com.zhan.gulimall.product.entity.ProductAttrValueEntity;
import com.zhan.gulimall.product.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveProductAttr(List<ProductAttrValueEntity> attrValueEntities) {
        this.saveBatch(attrValueEntities);
    }

    @Override
    public List<ProductAttrValueEntity> listforSpuId(Long spuId) {

        List<ProductAttrValueEntity> list = this.list(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));

        return list;
    }

    @Transactional
    @Override
    public void updateBaseAttrBySpuId(Long spuId, List<ProductAttrValueEntity> list) {


        //1：删除pms_product_attr_value表spuid为spuId的字段
        this.remove(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",spuId));

        //2：插入list
        List<ProductAttrValueEntity> collect = list.stream().map(item -> {
            item.setSpuId(spuId);
            return item;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }

}