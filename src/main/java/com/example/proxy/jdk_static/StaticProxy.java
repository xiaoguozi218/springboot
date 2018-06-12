package com.example.proxy.jdk_static;

/**
 * Created by MintQ on 2018/6/12.
 *
 * 添加一个静态代理类Proxy
 */
public class StaticProxy implements ICompanyServce {

    private ICompanyServce companyServce;

    public StaticProxy(ICompanyServce companyServce) {
        this.companyServce = companyServce;
    }

    @Override
    public void doWork() {
        System.out.println(" start work ....");
        companyServce.doWork();
        System.out.println(" end work ....");
    }
}
