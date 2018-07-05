package com.example.pattern.strategy;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by MintQ on 2018/7/5.
 * vip用户
 */
@Service("vipMember")
public class VipMember implements Strategy{
    @Override
    public BigDecimal calculatePrice() {
        return new BigDecimal("80");
    }
}
