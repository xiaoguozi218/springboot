package com.example.pattern.interface_abstract;

import java.util.Map;

/**
 * Created by MintQ on 2018/7/3.
 *
 * Version~0.2.0:

     中国人民银行修改自己的规范：

     之前的接口不变，新增一个抽象类：
 *
 *  看看这个抽象类的逻辑，你可以看出来，把所有的公共逻辑都写在里面，具体的业务操作方法是抽象的，由具体的银行去实现即可，看下银行的实现：
 *
 *  每个银行只要继承这个抽象类，并实现对应的业务逻辑方法即可，一些校验都统一放入抽象类中判断，这样，每个银行的实现逻辑清晰、规范、不会遗漏校验，
 *  当然如果抽象类中的某些抽象方法，有些银行不想做具体实现，可以默认返回，当然这是一个缺陷，欢迎读者提出更好的解决方案。
 *
 *  这个时候每个银行又要实现抽象类，又要实现接口，他们感觉很麻烦且没有必要，这个时候中国银行又进行调整：
 *
 *  Version~0.3.0:
    顶层接口还是不变，抽象类修改：由抽象类实现接口
    到此一个体现接口和抽象类结合使用的简单例子就说完了，谢谢～
 *
 *
 */
public abstract class CommonOpretion implements BankLoan{

    @Override
    public Map<String, String> loanOperation(String userId) {

        //所有银行借款必须的校验
        if(userId==null || userId.isEmpty()){
            System.out.println("userId为空！");
            return null;
        }
        //所有银行借款必须的校验
        if(userId.length()!=32){
            System.out.println("userId格式错误，应该为32位！"+userId);
            return null;
        }
        //所有银行借款必须的校验
        if(!queryUserExist(userId)){
            System.out.println("用户不存在："+userId);
            return null;
        }
        //所有银行借款必须的校验
        if(!queryUserIllegal(userId)){
            System.out.println("用户没有资格进行借款："+userId);
            return null;
        }

        //业务逻辑
        Map<String, String> map = getResultMap(userId);
        return map;

    }

    //根据数据库查询是否存在，根据自己公司的用户表查询
    protected abstract boolean queryUserExist(String userId);

    //根据数据库查询数据查询判断
    protected abstract boolean queryUserIllegal(String userId);

    //根据数据库查询相关信息
    protected abstract Map<String, String> getResultMap(String userId);

}
