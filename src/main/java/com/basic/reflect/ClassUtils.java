package com.basic.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by dello on 2016/6/7.
 */
public class ClassUtils {
    /**
     *  打印內的信息和打印内的成员信息
     * @param object
     */
    public static void printClassMessage(Object object){
        //获取类的类型，传递的是哪个子类的对象，c就是该子类的类类型
        Class c=object.getClass();
        System.out.println("类的名称:"+c.getName());
        System.out.println("类的简称:"+c.getSimpleName());

        /**
         * Method类 Method对象
         * 一个成员方法就是Method对象
         * getMethods()就是获取所有的public方法包括从父类继承的方法
         * getDeclaredMethods() 获取所有该类自己的方法，无论访问权限
         */
        printMethodMessage(object);

        /**
         * 获取成员变量信息
         * 成员变量也是一个对象，java.lang.reflect.Field.
         * Field封装了关于成员变量的操作
         * getFields()方法获取的是所有public的成员变量，包括从父类继承的成员变量
         * getDeclaredFields()方法获取的是该类自己声明的成员变量现象
         */
        printFieldMessage(object);
    }

    public static void printMethodMessage(Object object) {
        Class c = object.getClass();
        Method[] methods = c.getMethods();  //c.getDeclaredMethods()
        for(Method method:methods){
            System.out.println();
            System.out.println("-----------------------------------------------");
            //方法的返回值类型
            Class<?> returnType = method.getReturnType();
            System.out.println("方法返回值类型:"+returnType.getSimpleName());
            System.out.println("方法的名称:"+returnType.getSimpleName());

            //获取方法的参数类型
            Class<?>[] parameterTypes = method.getParameterTypes();
            for(Class pClass:parameterTypes){
                System.out.print("方法的参数:");
                System.out.print(pClass.getSimpleName()+",");
            }

            System.out.println();
            System.out.println("-----------------------------------------------");
        }
    }

    /**
     * 获取成员变量的信息，获取自己声明的
     * @param object
     */
    public static void printFieldMessage(Object object) {
        Class c=object.getClass();
        Field[] Fields = c.getDeclaredFields();
        for(Field field:Fields){
           System.out.println("成员变量的名字:"+field.getName()+" 成员变量的类型:"+field.getType().getSimpleName());
        }
    }

    /**
     * 获取构造函数的信息
     * @param object
     */
    public static void printConstrustMeassage(Object object){
        Class c=object.getClass();
        /**
         * 构造函数也是对象 java.lang.constructor 封装了构造函数信息
         *  getConstructors()   获取所有共有的构造方法,包括从父类继承的方法
         *  getDeclaredConstructors() 获取所有自己声明的构造方法
         */
        Constructor[] constructors = c.getConstructors();
        for(Constructor constructor:constructors){
            Class[] parameterTypes = constructor.getParameterTypes();
            System.out.print("构造函数的参数类型:");
            for(Class types:parameterTypes){
                System.out.print(types.getSimpleName()+",");
            }
            System.out.println();
        }
    }
}
