package com.zhan.gulimall.product.service.impl;

import com.google.j2objc.annotations.AutoreleasePool;
import com.zhan.common.utils.PageUtils;
import com.zhan.common.utils.Query;
import com.zhan.gulimall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zhan.gulimall.product.dao.BrandDao;
import com.zhan.gulimall.product.entity.BrandEntity;
import com.zhan.gulimall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key= (String) params.get("key");

        if(StringUtils.isNotEmpty(key)){
            QueryWrapper<BrandEntity> wrapper = new QueryWrapper<BrandEntity>().eq("brand_id", key).or().like("name", key).or().
                    like("descript", key).or().like("first_letter", key);
            IPage<BrandEntity> page = this.page(new Query<BrandEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        }else{
            IPage<BrandEntity> page = this.page(
                    new Query<BrandEntity>().getPage(params),
                    new QueryWrapper<BrandEntity>()
            );

            return new PageUtils(page);
        }

    }

    @Transactional
    @Override
    public void updateDetal(BrandEntity brand) {
        this.updateById(brand);
        if (StringUtils.isNotEmpty(brand.getName())){
            categoryBrandRelationService.updateBrand(brand.getBrandId(),brand.getName());
        }
    }

}