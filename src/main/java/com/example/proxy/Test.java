package com.example.proxy;

import com.example.proxy.cglib.CglibDynmProxy;
import com.example.proxy.jdk_dynamic.JDKDynmProxy;
import com.example.proxy.jdk_static.EmpoleeImpl;
import com.example.proxy.jdk_static.ICompanyServce;
import com.example.proxy.jdk_static.StaticProxy;

/**
 * Created by MintQ on 2018/6/12.
 */
public class Test {

    public static void main(String[] args) {
        EmpoleeImpl em = new EmpoleeImpl(); //被代理类
//        StaticProxy sp = new StaticProxy(em);
//        sp.doWork();

//        ICompanyServce company = (ICompanyServce) new JDKDynmProxy().getInstance(em);
//        company.doWork();

        ICompanyServce cglib = (ICompanyServce) new CglibDynmProxy().getInstance(EmpoleeImpl.class);
        cglib.doWork();
        EmpoleeImpl empolee = (EmpoleeImpl) new CglibDynmProxy().getInstance(EmpoleeImpl.class);
        empolee.doWork();
    }

}
