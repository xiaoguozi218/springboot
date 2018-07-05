package com.example.pattern.strategy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by MintQ on 2018/7/5.
 * 3.策略控制器
 *
 */
@Component
public class StrategyContext {

    /**
     *使用线程安全的ConcurrentHashMap存储所有实现Strategy接口的Bean
     *key:beanName
     *value：实现Strategy接口Bean
     */
    private final Map<String, Strategy> strategyMap = new ConcurrentHashMap<>();

    /**
     * 注入所有实现了Strategy接口的Bean
     * @param strategyMap
     */
    @Autowired
    public StrategyContext(Map<String, Strategy> strategyMap) {
        this.strategyMap.clear();
        strategyMap.forEach((k, v)-> this.strategyMap.put(k, v));
    }

    /**
     * 计算价格
     * @param memberLevel   会员等级
     * @return              价格
     */
    public BigDecimal calculatePrice(String memberLevel) {
        if (!StringUtils.isEmpty(memberLevel)) {
            return strategyMap.get(memberLevel).calculatePrice();
        }
        return null;
    }

}
