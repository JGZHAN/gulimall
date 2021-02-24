package com.zhan.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.zhan.common.valid.AddGroup;
import com.zhan.common.valid.ListValue;
import com.zhan.common.valid.UpdateGroup;
import com.zhan.common.valid.UpdateShowStatusGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author zhanjg
 * @email 2392323345@qq.com
 * @date 2020-12-06 01:14:26
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	@NotNull(message = "修改必须指定品牌ID" ,groups = {UpdateGroup.class})
	@Null(message = "新增不能指定ID",groups = {AddGroup.class})
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "新增必须提交品牌名" ,groups = {AddGroup.class,})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotEmpty(message = "logo地址不可以为空",groups = {AddGroup.class})
	@URL(message = "logo地址请提交合法的URL地址",groups = {AddGroup.class} )
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(groups = {UpdateShowStatusGroup.class,AddGroup.class})
	@ListValue(value = {0,1},groups = {UpdateShowStatusGroup.class,AddGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotEmpty
	@Pattern(regexp="^[a-zA-Z]$",message = "检索首字母必须是一个字母",groups = {AddGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull
	@Min(value = 0,message = "排序必须大于等于0",groups = {AddGroup.class})
	private Integer sort;

}
