package com.zhan.gulimall.ware.feign;

import com.zhan.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ZHAN jgZHAN
 * @create 2021-02-24 1:12
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {

    /**
     * /product/spuinfo/info/{id}
     * 1:让所有请求过网关
     *      1：@FeignClient("gulimall-gateway"):给gulimall-gateway所在的机器发送请求
     *      2：/api/product/spuinfo/info/{id}
     * 2:直接让后台指定服务处理
     *      1：@FeignClient("gulimall-product")
     *      2: /product/spuinfo/info/{id}
     *
     * @param id
     * @return
     */
    @RequestMapping("/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);

}
