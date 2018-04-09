package com.basic;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * locate com.basic
 * Created by mastertj on 2018/4/9.
 */
public class Server {
    //创建一个线程池
    private ExecutorService service=Executors.newCachedThreadPool();
    //创建一个线程组
    private AsynchronousChannelGroup channelGroup;
    //服务器通道
    public AsynchronousServerSocketChannel assc;

    public Server(int port) {
        try {
            //创建一个线程组
            channelGroup=AsynchronousChannelGroup.withCachedThreadPool(service,1);
            //创建服务器通道
            assc=AsynchronousServerSocketChannel.open(channelGroup);
            //进行绑定
            assc.bind(new InetSocketAddress(port));

            System.out.println("Server start , port: "+port);
            //进行阻塞
            assc.accept(this,new ServerCompleteHandler());
            //一直阻塞不让服务区停止
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server= new Server(8379);
    }
}
