package com.basic.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * locate com.basic.proxy
 * Created by 79875 on 2017/4/17.
 * 动态代理类 调用真实对象
 */
public class DynamicProxy implements InvocationHandler{
    //这就是我们的真实对象
    private Object objOriginal;

    //    构造方法，给我们要代理的真实对象赋初值
    public DynamicProxy(Object objOriginal) {
        this.objOriginal = objOriginal;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("-----------invoke before-----------");
        System.out.println("Method :"+method);
        //System.out.println("proxy object: "+proxy.toString());
        System.out.println("real object: "+this.objOriginal);
        Object object=method.invoke(this.objOriginal,args);
        System.out.println("-----------invoke after-----------");
        return object;
    }
}
