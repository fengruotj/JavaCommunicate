package com.basic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * locate com.basic
 * Created by mastertj on 2018/4/8.
 */
public class Server implements Runnable {
    //1.多用复用器（管理所有的通道）
    private Selector selector;
    //建一个缓冲区
    private ByteBuffer readBuffer=ByteBuffer.allocate(1024);
    private ByteBuffer writeBuffer=ByteBuffer.allocate(1024);

    private Server(int prot){
        try {
            //1.打开多路复用器通道
            this.selector=Selector.open();
            //2.打开服务器通道
            ServerSocketChannel ssc=ServerSocketChannel.open();
            //3.绑定端口
            ssc.bind(new InetSocketAddress(prot));
            //4.设置服务器通过为非阻塞模式
            ssc.configureBlocking(false);
            //5.将服务器通道绑定到selector上去,并且监听阻塞监听状态
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server start :"+prot);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void run() {
        while (true){
            try {
                //1.必须让多路复用器开始监听
                this.selector.select();
                //2.返回多路复用器已经选择的结果集
                Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
                //3.进行遍历
                while (iterator.hasNext()){
                    //4.获取一个选择的元素
                    SelectionKey next = iterator.next();
                    //直接从容器中移除就行
                    iterator.remove();
                    if(next.isValid()){
                        if(next.isAcceptable()){
                            this.accept(next);
                        }

                        if(next.isConnectable()){
                            this.connect(next);
                        }

                        if(next.isReadable()){
                            this.read(next);
                        }
//
//                        if(next.isWritable()){
//                            this.write(next);
//                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void write(SelectionKey next) {
        //System.out.println("--------------write-------------");
        try {
            if(writeBuffer.position()!=0){
                SocketChannel socketChannel= (SocketChannel) next.channel();
                socketChannel.configureBlocking(false);
                writeBuffer.flip();
                socketChannel.write(writeBuffer);
                writeBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(SelectionKey next) {
        System.out.println("--------------read-------------");
        try {
            readBuffer.clear();
            SocketChannel socketChannel= (SocketChannel) next.channel();
            socketChannel.configureBlocking(false);
            int count = socketChannel.read(readBuffer);
            if(count<0){
                socketChannel.close();
                next.cancel();
                return;
            }
            readBuffer.flip();
            byte[] bytes=new byte[readBuffer.remaining()];
            readBuffer.get(bytes);
            String result=new String (bytes,"UTF-8");
            System.out.print("Client send data: "+result);
            socketChannel.register(this.selector,SelectionKey.OP_WRITE);
            String reuslt="服务器响应";
            writeBuffer.put(reuslt.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void connect(SelectionKey next) {
        System.out.println("--------------connect-------------");
    }

    private void accept(SelectionKey next) {
        System.out.println("--------------accept-------------");
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) next.channel();
            //2.执行阻塞方法（等待客户端的通道）
            SocketChannel socketChannel = ssc.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector,SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new Server(8765)).start();
    }
}
