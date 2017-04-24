package com.basic.proxy;

/**
 * locate com.basic.proxy
 * Created by 79875 on 2017/4/17.
 * 真实对象，被动态代理类调用的对象
 */
public class RealSubject implements Subject{

    @Override
    public void rent() {
        System.out.println("i am want to rent my home!");
    }

    @Override
    public String hello(String str) {
        System.out.println("hello !"+ str);
        return "hello"+str;
    }
}
