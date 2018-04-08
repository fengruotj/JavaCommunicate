package com.basic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * locate com.basic
 * Created by mastertj on 2018/4/8.
 */
public class Client implements Runnable{
    private  ByteBuffer writeBuffer=ByteBuffer.allocate(1024);
    private ByteBuffer readBuffer=ByteBuffer.allocate(1024);
    private SocketChannel socketChannel=null;
    private Selector selector=null;

    public Client() {
        InetSocketAddress address=new InetSocketAddress("localhost",8765);
        try {
            selector=Selector.open();
            socketChannel=SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE|SelectionKey.OP_CONNECT);
            socketChannel.connect(address);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Client client=new Client();
        new Thread(client).start();
    }

    @Override
    public void run() {
        while (true){
            try {
                //1.必须让多路复用器开始监听
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    //直接从容器中移除就行
                    iterator.remove();
                    if (next.isValid()) {
                        if (next.isReadable()) {
                            this.read(next);
                        }
                        if(next.isWritable()){
                            this.write(next);
                        }
                        if(next.isConnectable()){
                            this.connect(next);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connect(SelectionKey next) {
        //System.out.println("connect");
        try {
            while (true){
                byte[] bytes=new byte[1024];
                System.in.read(bytes);
                writeBuffer.put(bytes);
                writeBuffer.flip();
                socketChannel.write(writeBuffer);
                writeBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(SelectionKey next) {
        System.out.println("--------------write-------------");
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
            System.out.print("receive data from server "+result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
