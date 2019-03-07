package com.basic;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
/**
 * locate com.basic
 * Created by MasterTj on 2019/3/7.
 * "共享内存" 读出数据
 * swap.mm 的第一个字节作了读写标志，分别是 0-可写，1-正在写，2-可读
 */
public class ReadShareMemory {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        RandomAccessFile raf = new RandomAccessFile("d:/swap.mm", "rw");
        FileChannel fc = raf.getChannel();
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
        int lastIndex = 0;

        for(int i=1;i<27;i++){
            int flag = mbb.get(0); //取读写数据的标志
            int index = mbb.get(1); //读取数据的位置,2 为可读

            while (flag != 2 || index == lastIndex){ //假如不可读，或未写入新数据时重复循环
                flag = mbb.get(0);
                index = mbb.get(1);
                Thread.sleep(100);
            }

            lastIndex = index;
            System.out.println("程序 ReadShareMemory：" + System.currentTimeMillis() +
                    "：位置：" + index +" 读出数据：" + (char)mbb.get(index));

            mbb.put(0,(byte)0); //置第一个字节为可读标志为 0
        }
    }
}
