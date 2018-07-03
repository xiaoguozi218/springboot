package com.example.pattern.interface_abstract;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MintQ on 2018/7/3.
 *
 * 所有的银行都必须遵守这个接口，进行具体的实现：
 *
 * Version~0.3.0:   具体银行不再需要实现接口：
 *
 */
public class ABC extends CommonOpretion {
    //根据数据库查询是否存在，根据自己公司的用户表查询
    @Override
    public boolean queryUserExist(String userId){
        return true; //return false;
    }

    //根据数据库查询数据查询判断
    @Override
    public boolean queryUserIllegal(String userId){
        return true; //return false;
    }

    //根据数据库查询相关信息
    @Override
    public Map<String, String> getResultMap(String userId){
        Map<String, String> map = new HashMap<String, String>();

        map.put("name", "詹姆斯"); //此处为了方便直接写死
        map.put("amount", "100000"); //应根据userId，在系统进行一系列判断计算，得出能借款金额，此处为了方便直接写死
        map.put("rankLevel", "A"); //应根据userId，在系统进行一系列判断计算，得出用户等级，此处为了方便直接写死
        //.....其它一些列操作

        return map;

    }

}
