package com.example.pattern.interface_abstract;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MintQ on 2018/7/3.
 *
 * 所有的银行都必须遵守这个接口，进行具体的实现：
 *
 *  我们发现，每当一家银行做此操作时，都会做一些校验，并且他们都是类似的，这不仅繁琐，而且有些银行容易遗漏一些校验操作；
 *  虽然校验的逻辑大同小异，但是每个银行都可能自己定义自己的方法名、判断自己的业务逻辑，这样的开发低效且中国人民银行不方便管理，为此，人行需要调整自己的规范，统一各个银行的行为；
 *
 */
public class ICBC extends CommonOpretion implements BankLoan{

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

        map.put("amount", "100000"); //应根据userId，在系统进行一系列判断计算，得出能借款金额，此处为了方便直接写死
        map.put("rankLevel", "A"); //应根据userId，在系统进行一系列判断计算，得出用户等级，此处为了方便直接写死
        //.....其它一些列操作

        return map;

    }


}
