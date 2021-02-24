package com.zhan.gulimall.product.service.impl;

import com.sun.xml.internal.bind.v2.TODO;
import com.zhan.common.to.SkuReductionTo;
import com.zhan.common.to.SpuBoundTo;
import com.zhan.common.utils.PageUtils;
import com.zhan.common.utils.Query;
import com.zhan.common.utils.R;
import com.zhan.gulimall.product.entity.*;
import com.zhan.gulimall.product.feign.CouponFeignService;
import com.zhan.gulimall.product.service.*;
import com.zhan.gulimall.product.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zhan.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService infoDescService;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService attrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     *
//     * TODO 高级部分完善
     * @param spuInfoVo
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuInfoVo) {
//        1保存spu基本信息。pms_spu_info
        SpuInfoEntity baseSpuInfo=new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfoVo,baseSpuInfo);
        baseSpuInfo.setCreateTime(new Date());
        baseSpuInfo.setUpdateTime(new Date());
        this.saveBaseSpuInfo(baseSpuInfo);

//        2保存spu描述图片。pms_spu_info_desc
        List<String> decript = spuInfoVo.getDecript();

        SpuInfoDescEntity spuInfoDescEntity=new SpuInfoDescEntity();

        spuInfoDescEntity.setSpuId(baseSpuInfo.getId());
        spuInfoDescEntity.setDecript(String.join(",",decript));
        infoDescService.saveSpuInfoDesc(spuInfoDescEntity);


//        3保存spu图片集。pms_spu_images
        List<String> images = spuInfoVo.getImages();
        spuImagesService.saveImages(baseSpuInfo.getId(),images);


//        4 保存spu规格参数。pms_product_attr_value
        List<BaseAttrs> baseAttrs = spuInfoVo.getBaseAttrs();
        List<ProductAttrValueEntity> attrValueEntities = baseAttrs.stream().map((attrs) -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attrs.getAttrId());
            AttrEntity byId = attrService.getById(attrs.getAttrId());
            valueEntity.setAttrName(byId.getAttrName());
            valueEntity.setAttrValue(attrs.getAttrValues());
            valueEntity.setQuickShow(attrs.getShowDesc());
            valueEntity.setSpuId(baseSpuInfo.getId());
            return valueEntity;

        }).collect(Collectors.toList());
        attrValueService.saveProductAttr(attrValueEntities);


//        5保存spu的积分信息。  gulimall_sms-》sms_spu_bounds
        Bounds bounds = spuInfoVo.getBounds();
        SpuBoundTo spuBoundTo=new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(baseSpuInfo.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if(r.getCode()!=0){
            log.error("远程spu积分信息保存失败");
        }

//        6保存当前spu对应的所有sku信息

        List<Skus> skus = spuInfoVo.getSkus();
        skus.forEach(sku -> {
            String defaultImg="";
            List<Images> skuImages = sku.getImages();
            for (Images skuImage : skuImages) {
                if (skuImage.getDefaultImg()==1){
                    defaultImg=skuImage.getImgUrl();
                }
            }
//            private String skuName;
//            private BigDecimal price;
//            private String skuTitle;
//            private String skuSubtitle;
            SkuInfoEntity skuInfoEntity=new SkuInfoEntity();
            BeanUtils.copyProperties(sku,skuInfoEntity);
            skuInfoEntity.setBrandId(baseSpuInfo.getBrandId());
            skuInfoEntity.setCatalogId(baseSpuInfo.getCatalogId());
            skuInfoEntity.setSaleCount(0L);
            skuInfoEntity.setSkuDefaultImg(defaultImg);
//        6.1 保存sku的基本信息。pms_sku_info
            skuInfoService.saveSkuInfo(skuInfoEntity);

            Long skuId=skuInfoEntity.getSkuId();

            List<SkuImagesEntity> skuImagesEntities = sku.getImages().stream().map((img) -> {
                SkuImagesEntity imagesEntity = new SkuImagesEntity();
                imagesEntity.setSkuId(skuId);
                imagesEntity.setDefaultImg(img.getDefaultImg());
                imagesEntity.setImgUrl(img.getImgUrl());

                return imagesEntity;
            }).filter(entity->{
                //返回true则是需要，返回false则被过滤掉
                return StringUtils.isNotEmpty(entity.getImgUrl());
            }).collect(Collectors.toList());


//        6.2保存sku的图片信息。pms_sku_images
            skuImagesService.saveBatch(skuImagesEntities);
//            TODO 没有图片的路径，无需保存
            List<Attr> attrs = sku.getAttr();
            List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attrs.stream().map((attr) -> {
                SkuSaleAttrValueEntity saleAttrValueEntity = new SkuSaleAttrValueEntity();
                BeanUtils.copyProperties(attr, saleAttrValueEntity);
                saleAttrValueEntity.setSkuId(skuId);
                return saleAttrValueEntity;
            }).collect(Collectors.toList());


//        6.3保存sku的销售属性信息。pms_sku_sale_attr_value
            skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

//        6.4 SQL的优惠、满减的信息。gulimall_sms-》sms_sku_ladder、sms_sku_full_reduction、sms_member_price

            SkuReductionTo skuReductionTo=new SkuReductionTo();
            BeanUtils.copyProperties(sku,skuReductionTo);
            skuReductionTo.setSkuId(skuId);
            if(skuReductionTo.getFullCount()>0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0"))==1){
                R r1 = couponFeignService.saveSpuReduction(skuReductionTo);
                if(r1.getCode()!=0){
                    log.error("远程sku优惠、满减等信息保存失败");
                }

            }



        });



    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    @Override
    public PageUtils queryPageCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();


        String key = (String) params.get("key");

        if(StringUtils.isNotEmpty(key)){
            wrapper.and(w->{
               w.eq("id",key).or().like("spu_name",key);
            });
        }
//        status=1 and (id=1 or spu_name like xxx)
        String status = (String) params.get("status");

        if(StringUtils.isNotEmpty(status)){
            wrapper.eq("publish_status",status);
        }
        String brandId = (String) params.get("brandId");
        if(StringUtils.isNotEmpty(brandId) && !"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if(StringUtils.isNotEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }
        /**
         *    page: 1,//当前页码
         *    limit: 10,//每页记录数
         *    sidx: 'id',//排序字段
         *    order: 'asc/desc',//排序方式
         *    key: '华为',//检索关键字
         *    catelogId: 6,//三级分类id
         *    brandId: 1,//品牌id
         *    status: 0,//商品状态
         */


        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}