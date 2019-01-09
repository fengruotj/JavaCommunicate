package com.basic;

import org.junit.Test;

import java.nio.IntBuffer;

/**
 * locate com.basic
 * Created by mastertj on 2018/4/8.
 */
public class TestBuffer {
    //1.基本操作
    @Test
    public void basicOperator(){
        //创建指定长度的缓冲区
        IntBuffer buf=IntBuffer.allocate(10);
        buf.put(13);//position 的位置:0->1
        buf.put(21);//position 的位置:1->2
        buf.put(35);//position 的位置:2->3
        buf.flip();
        System.out.println("buf复位操作: "+buf);
        System.out.println("容量为: "+buf.capacity());
        System.out.println("限制为: "+buf.limit());
    }

    @Test
    public void getData(){
        IntBuffer buf=IntBuffer.allocate(10);
        buf.put(13);//position 的位置:0->1
        buf.put(21);//position 的位置:1->2
        buf.put(35);//position 的位置:2->3
        buf.flip();//复位
        System.out.println("获取下标为1的元素"+buf.get(1));
        System.out.println("buf position的位置不变 "+buf);

        buf.put(1,4);
        System.out.println(buf.get(1));

        for(int i=0;i<buf.limit();i++){
            //调用get方法会使其缓冲区的位置（position）向后递增一位
            System.out.print(buf.get()+"\t");
        }
        System.out.println(buf);
    }

    @Test
    public void wrap(){
        //2.wrap方法调用

        // wrap方法会包裹一个数组：一般这种用法不会先初始化缓存对象的长度，因为没有意义，最好还是会被wrap所包裹的数组覆盖掉
        // 并且wrap方法修改缓冲区对象的时候，数组本身也会跟着发生变化
        int[] aar=new int[]{1,2,5};
        IntBuffer buf=IntBuffer.wrap(aar);
        System.out.println(buf);
    }

    @Test
    public void otherFunctions(){
        int[] aar=new int[]{1,2,5};
        IntBuffer buf=IntBuffer.allocate(10);
        buf.put(aar);
        buf.flip();
        System.out.println("buf: "+buf);
        //一种复制的方法
        IntBuffer buf1=buf.duplicate();
        System.out.println("buf: "+buf1);

        System.out.println("可读数据为: "+buf.remaining());
        int array[]=new int[buf.remaining()];
        buf.get(array);
        for(int i=0;i<array.length;i++){
            System.out.print(array[i]+"\t");
        }
    }
}
