package com.example.pattern.strategy;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by MintQ on 2018/7/5.
 *
 * 超级会员
 */
@Service("superMember")
public class SuperMember implements Strategy{
    @Override
    public BigDecimal calculatePrice() {
        return new BigDecimal("1");
    }
}
