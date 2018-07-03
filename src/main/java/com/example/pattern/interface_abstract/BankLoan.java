package com.example.pattern.interface_abstract;

import java.util.Map;

/**
 * Created by MintQ on 2018/7/3.
 * 背景：
 *      我们看一个银行借款的例子，中国人民银行管理并监督所有的银行，它制定了一个规范，所有的银行在进行借款时都必须遵守这个规范（基本接口）；
 *
 * Version~0.1.0:
 * 中国人民银行定义了一个操作接口：
 *
 */
public interface BankLoan {
    /**
     * 银行借款操作
     * 每个公司都需要实现该方法，并针对自己公司员工的情况进行特殊操作，
     * 比如判断该员工是否有资格，能借款多少等等
     * @return
     */
    public Map<String, String> loanOperation(String userId);

}
