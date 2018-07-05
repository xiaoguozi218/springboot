package com.example.pattern.strategy;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by MintQ on 2018/7/5.
 * 2.实现接口
 * 普通用户
 */
@Service("generalMember")
public class GeneralMember implements Strategy{

    @Override
    public BigDecimal calculatePrice() {
        return new BigDecimal("100");
    }
}
