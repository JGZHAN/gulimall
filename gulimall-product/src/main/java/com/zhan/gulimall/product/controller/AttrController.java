package com.zhan.gulimall.product.controller;

import com.zhan.common.utils.PageUtils;
import com.zhan.common.utils.R;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.zhan.gulimall.product.entity.ProductAttrValueEntity;
import com.zhan.gulimall.product.service.ProductAttrValueService;
import com.zhan.gulimall.product.vo.AttrRespVo;
import com.zhan.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zhan.gulimall.product.entity.AttrEntity;
import com.zhan.gulimall.product.service.AttrService;


/**
 * 商品属性
 *
 * @author zhanjg
 * @email 2392323345@qq.com
 * @date 2020-12-06 01:14:26
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;
    @Autowired
    private ProductAttrValueService productAttrValueService;

    /**
     * /product/attr/update/{spuId}
     */
    @PostMapping("/update/{spuId}")
    public R updateSpuId(@PathVariable("spuId") Long spuId,@RequestBody List<ProductAttrValueEntity> list ){

        productAttrValueService.updateBaseAttrBySpuId(spuId,list);

        return R.ok();
    }


    /**
 * /product/attr/base/listforspu/{spuId}
 */
    @GetMapping("/base/listforspu/{spuId}")
    public R listforSpuId(@PathVariable("spuId") Long spuId){

        List<ProductAttrValueEntity> list=productAttrValueService.listforSpuId(spuId);
        return R.ok().put("data",list);
    }

//    /product/attr/base/list/{catelogId}
//    /product/attr/sale/list/{catelogId}
    /**
     * 列表
     */
    @RequestMapping("/{attrType}/list/{catelogId}")
    //@RequiresPermissions("product:attr:list")
    public R baseList(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId,
                      @PathVariable("attrType") String type){
        
        PageUtils page = attrService.queryBaseAttrPage(params,catelogId,type);



        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
//		AttrEntity attr = attrService.getById(attrId);
        AttrRespVo respVo=attrService.getAttrInfo(attrId);
        return R.ok().put("attr", respVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attr){
//		attrService.updateById(attr);

        attrService.updateAttr(attr);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
