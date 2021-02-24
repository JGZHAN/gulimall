package com.zhan.gulimall.ware.service.impl;

import com.zhan.common.constant.WareConstant;
import com.zhan.gulimall.ware.entity.PurchaseDetailEntity;
import com.zhan.gulimall.ware.service.PurchaseDetailService;
import com.zhan.gulimall.ware.service.WareSkuService;
import com.zhan.gulimall.ware.vo.MergeVo;
import com.zhan.gulimall.ware.vo.PurchaseDoneVo;
import com.zhan.gulimall.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhan.common.utils.PageUtils;
import com.zhan.common.utils.Query;

import com.zhan.gulimall.ware.dao.PurchaseDao;
import com.zhan.gulimall.ware.entity.PurchaseEntity;
import com.zhan.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {



    @Autowired
    PurchaseDetailService purchaseDetailService;

    @Autowired
    WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }


    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {

        Long purchaseId = mergeVo.getPurchaseId();
        if(purchaseId==null){
            PurchaseEntity purchaseEntity=new PurchaseEntity();


            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATE.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId=purchaseEntity.getId();
        }
        List<Long> items = mergeVo.getItems();


//      TODO 确认采购单id为0或1才可以合并

        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map((item) -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);

        PurchaseEntity purchaseEntity=new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);

    }

    /**
     *
     * @param ids
     */
    @Transactional
    @Override
    public void received(List<Long> ids) {

//        1确认当前采购单是新建或者以分配
        List<PurchaseEntity> collect = ids.stream().map(id -> {
            PurchaseEntity byId = this.getById(id);
            return byId;
        }).filter(item -> {
            if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATE.getCode() ||
                    item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            }
            return false;
        }).map(item -> {
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());

//        2改变采购单状态
        this.updateBatchById(collect);

//        3改变采购项状态
        collect.forEach((item)->{
            List<PurchaseDetailEntity> list=purchaseDetailService.listDetailBypurchaseId(item.getId());
            List<PurchaseDetailEntity> collect1 = list.stream().map(item1 -> {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setId(item1.getId());
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return purchaseDetailEntity;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(collect1);

        });

    }

    @Transactional
    @Override
    public void done(PurchaseDoneVo doneVo) {


//      2：改变采购项状态
        Boolean flag=true;
        List<PurchaseItemDoneVo> items = doneVo.getItems();
        List<PurchaseDetailEntity> updates=new ArrayList<>();
        for (PurchaseItemDoneVo item:items){
            PurchaseDetailEntity entity = new PurchaseDetailEntity();
            if (item.getStatus()==WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()){
                flag=false;
            }else {
//      3：将采购成功的进行入库
                PurchaseDetailEntity entity1=purchaseDetailService.getById(item.getItemId());

                wareSkuService.addStock(entity1.getSkuId(),entity1.getWareId(),entity1.getSkuNum());
            }
            entity.setStatus(item.getStatus());
            entity.setId(item.getItemId());
            updates.add(entity);
        }
        purchaseDetailService.updateBatchById(updates);

        //         1: 改变采购单状态

        Long id = doneVo.getId();
        PurchaseEntity purchaseEntity=new PurchaseEntity();
        purchaseEntity.setUpdateTime(new Date());
        purchaseEntity.setStatus(flag?WareConstant.PurchaseStatusEnum.FINISH.getCode():WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setId(id);

        this.updateById(purchaseEntity);

    }

}