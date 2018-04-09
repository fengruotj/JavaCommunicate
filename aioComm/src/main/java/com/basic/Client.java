package com.basic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * locate com.basic
 * Created by mastertj on 2018/4/9.
 */
public class Client implements Runnable{

    private AsynchronousSocketChannel asc=null;
    private ByteBuffer writeBuffer=ByteBuffer.allocate(1024);
    private ByteBuffer readBuffer=ByteBuffer.allocate(1024);
    public Client() {
        try {
            asc=AsynchronousSocketChannel.open();
            asc.connect(new InetSocketAddress("localhost",8379));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read(){
        try {
            readBuffer.clear();
            asc.read(readBuffer).get();
            readBuffer.flip();
            byte[] bytes=new byte[readBuffer.remaining()];
            readBuffer.get(bytes);
            String str=new String (bytes,"UTF-8");
            System.out.println("收到服务器发送的响应："+str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(String data){
        try {
            writeBuffer.clear();
            writeBuffer.put(data.getBytes());
            writeBuffer.flip();
            asc.write(writeBuffer);
            this.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while (true){

        }
    }

    public static void main(String[] args) throws InterruptedException {
        Client client1=new Client();
        Client client2=new Client();
        Client client3=new Client();
        new Thread(client1).start();
        new Thread(client2).start();
        new Thread(client3).start();

        client1.write("hello i am client1");
        client2.write("hello i am client2");
        client3.write("hello i am client3");
    }
}
