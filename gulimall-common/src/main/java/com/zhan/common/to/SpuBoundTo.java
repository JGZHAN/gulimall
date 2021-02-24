package com.zhan.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ZHAN jgZHAN
 * @create 2021-02-22 8:55
 */
@Data
public class SpuBoundTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
