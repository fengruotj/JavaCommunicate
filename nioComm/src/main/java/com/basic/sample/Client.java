package com.basic.sample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * locate com.basic.sample
 * Created by mastertj on 2018/4/8.
 */
public class Client implements Runnable{
    private BlockingQueue<String> words;
    private Random random;

    public static void main(String[] args) {
        //种多个线程发起Socket客户端连接请求
        for(int i=0; i<10; i++){
            Client c = new Client();
            c.init();
            new Thread(c).start();
        }
    }

    @Override
    public void run() {
        SocketChannel channel = null;
        Selector selector = null;
        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            //请求连接
            channel.connect(new InetSocketAddress("localhost", 8383));
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_CONNECT);
            boolean isOver = false;

            while(! isOver){
                selector.select();
                Iterator ite = selector.selectedKeys().iterator();
                while(ite.hasNext()){
                    SelectionKey key = (SelectionKey) ite.next();
                    ite.remove();

                    if(key.isConnectable()){
                        if(channel.isConnectionPending()){
                            if(channel.finishConnect()){
                                //只有当连接成功后才能注册OP_READ事件
                                key.interestOps(SelectionKey.OP_READ);

                                channel.write(CharsetHelper.encode(CharBuffer.wrap(getWord())));
                                sleep();
                            }
                            else{
                                key.cancel();
                            }
                        }
                    }
                    else if(key.isReadable()){
                        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                        channel.read(byteBuffer);
                        byteBuffer.flip();
                        CharBuffer charBuffer = CharsetHelper.decode(byteBuffer);
                        String answer = charBuffer.toString();
                        System.out.println(Thread.currentThread().getId() + "---" + answer);

                        String word = getWord();
                        if(word != null){
                            channel.write(CharsetHelper.encode(CharBuffer.wrap(word)));
                        }
                        else{
                            isOver = true;
                        }
                        sleep();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if(channel != null){
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(selector != null){
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void init() {
        words = new ArrayBlockingQueue<String>(5);
        try {
            words.put("hi");
            words.put("who");
            words.put("what");
            words.put("where");
            words.put("bye");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        random = new Random();
    }

    private String getWord(){
        return words.poll();
    }

    private void sleep() {
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleep(long l) {
        try {
            TimeUnit.SECONDS.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
