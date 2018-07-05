package com.example.pattern.strategy;

import java.math.BigDecimal;

/**
 * Created by MintQ on 2018/7/5.
 *
 * 1.策略接口
 *
 * 计算价格的接口
 */
public interface Strategy {
    /**
     * 计算价格
     * @return
     */
    public BigDecimal calculatePrice();
}
