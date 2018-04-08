package com.basic.reflect;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by dello on 2016/6/7.
 */
public class MethodTest {
    public void print(String a,String b){
        System.out.println(a.toUpperCase()+","+b.toLowerCase());
    }

    public void print(int a,int b){
        System.out.println(a+b);
    }

    public void print(){
        System.out.println("print-------");
    }
    @Test
    public void Test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        long startTime=System.currentTimeMillis();
        Class c=MethodTest.class;

        Object returnobject;

        //获取方法名称和参数列表
        MethodTest methodTest=new MethodTest();
        Method printstring = c.getDeclaredMethod("print", String.class, String.class);
        returnobject=printstring.invoke(methodTest,"abc","fgh");

        Method printint=c.getDeclaredMethod("print",int.class,int.class);
        returnobject= printint.invoke(methodTest,5,6);

        Method print=c.getDeclaredMethod("print");
        print.invoke(methodTest);

        long endTime=System.currentTimeMillis();
        System.out.println("use Time:"+(endTime-startTime));
    }
}
