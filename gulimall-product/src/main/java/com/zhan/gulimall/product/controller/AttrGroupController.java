package com.zhan.gulimall.product.controller;

import com.zhan.common.utils.PageUtils;
import com.zhan.common.utils.R;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.zhan.gulimall.product.entity.AttrEntity;
import com.zhan.gulimall.product.service.AttrAttrgroupRelationService;
import com.zhan.gulimall.product.service.AttrService;
import com.zhan.gulimall.product.service.CategoryService;
import com.zhan.gulimall.product.vo.AttrGroupRelationVo;
import com.zhan.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zhan.gulimall.product.entity.AttrGroupEntity;
import com.zhan.gulimall.product.service.AttrGroupService;


/**
 * 属性分组
 *
 * @author zhanjg
 * @email 2392323345@qq.com
 * @date 2020-12-06 01:14:26
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService relationService;

//    /product/attrgroup/{catelogId}/withattr

    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId){

        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVos=relationService.getAttrGroupWithAttrsByCatelogId(catelogId);

        return R.ok().put("data",attrGroupWithAttrsVos);
    }



//    /product/attrgroup/attr/relation
    @PostMapping("/attr/relation")
    public R addreRelation(@RequestBody List<AttrGroupRelationVo> relationVos){

        relationService.saveBatch(relationVos);


        return R.ok();
    }


//    /product/attrgroup/{attrgroupId}/noattr/relation

    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                            @RequestParam Map<String,Object> params){
        PageUtils pageUtils=attrGroupService.getNoRelationAttr(attrgroupId,params);

        return R.ok().put("page",pageUtils);
    }


//    /product/attrgroup/attr/relation/delete

    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] relationVos){

        relationService.deleteRelation(relationVos);
        return R.ok();

    }


    //    /product/attrgroup/{attrgroupId}/attr/relation
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> entities= attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",entities);
    }
    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId){
//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params,catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        Long catelogId = attrGroup.getCatelogId();
        Long[] catelogPath=categoryService.findCatelogPath(catelogId);

        attrGroup.setCatelogPath(catelogPath);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
