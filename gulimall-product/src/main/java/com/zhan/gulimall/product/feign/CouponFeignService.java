package com.zhan.gulimall.product.feign;

import com.zhan.common.to.SkuReductionTo;
import com.zhan.common.to.SpuBoundTo;
import com.zhan.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ZHAN jgZHAN
 * @create 2021-02-08 15:32
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveInfo")
    R saveSpuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
