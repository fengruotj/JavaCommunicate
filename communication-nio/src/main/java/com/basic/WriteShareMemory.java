package com.basic;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * locate com.basic
 * Created by MasterTj on 2019/3/7.
 * 从共享内存中读取数据
 * swap.mm 的第一个字节作了读写标志，分别是 0-可写，1-正在写，2-可读
 */
public class WriteShareMemory {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        RandomAccessFile raf = new RandomAccessFile("d:/swap.mm", "rw");
        FileChannel fc = raf.getChannel();
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, 1024);

        //清除文件内容
        for(int i=0;i<1024;i++){
            mbb.put(i,(byte)0);
        }

        //从文件的第二个字节开始，依次写入 A-Z 字母，第一个字节指明了当前操作的位置
        for(int i=0;i<26;i++){
            int index = i+2;
            int flag = mbb.get(0); //可读标置第一个字节为 0
            while (flag!=0){
                //不是可写标示 0，则重复循环，等待
                flag = mbb.get(0);
                Thread.sleep(100);
            }

            mbb.put(0,(byte)1); //正在写数据，标志第一个字节为 1
            mbb.put(1,(byte)(index)); //写数据的位置

            System.out.println("程序 WriteShareMemory："+System.currentTimeMillis() +
                    "：位置：" + index +" 写入数据：" + (char)(i+65));

            mbb.put(index,(byte)(i+65));//index 位置写入数据
            mbb.put(0,(byte)2); //置可读数据标志第一个字节为 2
            Thread.sleep(513);
        }
    }
}
