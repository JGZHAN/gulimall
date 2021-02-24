package com.zhan.gulimall.member.feign;

import com.zhan.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ZHAN jgZHAN
 * @create 2020-12-08 21:04
 */
@Service
@FeignClient(name="gulimall-coupon")
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    public R memberCoupons();
}
