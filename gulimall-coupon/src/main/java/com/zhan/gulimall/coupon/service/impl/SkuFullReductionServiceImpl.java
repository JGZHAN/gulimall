package com.zhan.gulimall.coupon.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.zhan.common.to.MemberPrice;
import com.zhan.common.to.SkuReductionTo;
import com.zhan.gulimall.coupon.entity.MemberPriceEntity;
import com.zhan.gulimall.coupon.entity.SkuLadderEntity;
import com.zhan.gulimall.coupon.service.MemberPriceService;
import com.zhan.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhan.common.utils.PageUtils;
import com.zhan.common.utils.Query;

import com.zhan.gulimall.coupon.dao.SkuFullReductionDao;
import com.zhan.gulimall.coupon.entity.SkuFullReductionEntity;
import com.zhan.gulimall.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    SkuLadderService skuLadderService;

    @Autowired
    MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveInfo(SkuReductionTo skuReductionTo) {
//       SQL的优惠、满减的信息。gulimall_sms-》sms_sku_ladder、sms_sku_full_reduction、sms_member_price
//        sms_sku_ladder
        SkuLadderEntity skuLadderEntity=new SkuLadderEntity();
        skuLadderEntity.setId(skuReductionTo.getSkuId());
        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
        if (skuLadderEntity.getFullCount()>0){

            skuLadderService.save(skuLadderEntity);
        }
//        sms_sku_full_reduction
        SkuFullReductionEntity fullReductionEntity=new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo,fullReductionEntity);
        if(fullReductionEntity.getFullPrice().compareTo(new BigDecimal("0"))==1){

            this.save(fullReductionEntity);
        }

//        sms_member_price
        List<MemberPrice> memberPrices = skuReductionTo.getMemberPrice();
        List<MemberPriceEntity> memberPriceEntities = memberPrices.stream().map((item) -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
            memberPriceEntity.setMemberLevelId(item.getId());
            memberPriceEntity.setMemberLevelName(item.getName());
            memberPriceEntity.setMemberPrice(item.getPrice());
            memberPriceEntity.setAddOther(1);
            return memberPriceEntity;
        }).filter(item->{
            return item.getMemberPrice().compareTo(new BigDecimal("0"))==1;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(memberPriceEntities);


    }

}