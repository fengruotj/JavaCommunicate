package com.basic.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * locate com.basic.nio
 * Created by 79875 on 2017/4/18.
 * echoServer 是一个NIO编写的服务器。它接受客户端过来的字符串，不经任何处理再次返回给客户端
 */
public class EchoServer {
    private String bindAdrees;
    private int prot;
    private InetSocketAddress address;
    private ServerSocketChannel acceptChannel;
    private int backLogLength=100;

    public EchoServer(String bindAdrees, int prot) {
        this.bindAdrees = bindAdrees;
        this.prot = prot;
    }

    public EchoServer() {
    }

    private Selector selector; //Selector 用来监控ServerSocketChannel 和Socket Channel 中注册的特殊事件
    public Selector initSelector() throws IOException {
        address = new InetSocketAddress(bindAdrees, prot);
        acceptChannel=ServerSocketChannel.open();
        //创建一个ServerSocketChannel 并将之设为非阻塞模式
        acceptChannel.configureBlocking(false);
        // 将Server  Socket 绑定到address地址上，并设置监听队列长度
        acceptChannel.socket().bind(address,backLogLength);
        Selector selector=Selector.open();
        //向 Selector 注册ServerSocketChannel，注册事件为SelectionKey.OP_ACCEPT
        acceptChannel.register(selector, SelectionKey.OP_ACCEPT);
        return selector;
    }

    /**
     * 接受新客户端的连接请求
     * @param key SelectionKey，用于跟踪注册事件，在SelectionKey 中定义了4种事件
     *            SelectionKey.OP_ACCEPT 接受连接就绪事件
     *            SelectionKey.OP_CONNECT 连接就绪事件
     *            SelectionKey.OP_READ 读就绪事件
     *            SelectionKey.OP_WRITE 写就绪事件
     */
    private void doAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel= (ServerSocketChannel) key.channel();//返回与当前SelectionKey关联的SelectableChannel对象
        //接受连接请求并将之设置为非阻塞模式
        SocketChannel socketChannel=serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        // 将新的SocketChannel 注册到Selector中，一旦有需要读或者写的数据，就会通知相应的程序
        SelectionKey clinetKey=socketChannel.register(selector,SelectionKey.OP_READ);
        ByteBuffer byteBuffer=ByteBuffer.allocate(8192);//8KB
        clinetKey.attach(byteBuffer);// SelectionKey 关联一个Object对象。每个SelectionKey只能关联一个对象
    }

    /**
     *  处理读数据请求
     * @param key SelectionKey，用于跟踪注册事件，在SelectionKey 中定义了4种事件
     */
    private void receive(SelectionKey key) throws IOException {
        SocketChannel socketChannel= (SocketChannel) key.channel();
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();//获取当前SelectionKey关联的对象
        int numread = socketChannel.read(byteBuffer);
        if(numread>0){
            byteBuffer.flip();
            key.interestOps(SelectionKey.OP_WRITE);//切换至OP_WRITE
        }
    }

    /**
     * 处理写数据请求
     * @param key SelectionKey，用于跟踪注册事件，在SelectionKey 中定义了4种事件
     */
    private void send(SelectionKey key) throws IOException {
        SocketChannel socketChannel= (SocketChannel) key.channel();
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();//获取当前SelectionKey关联的对象
        socketChannel.write(byteBuffer);
        if(byteBuffer.remaining()==0){
            byteBuffer.clear();
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    /**
     *  监听客户端事件，并调用对应事件处理函数
     */
    public void run(){
        while (true){
            SelectionKey key=null;
            try {
                selector.select();//处于阻塞专题，知道有新的事件发生。
                Iterator<SelectionKey> iterator=selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    if(!key.isValid()){
                        continue;
                    }
                    if(key.isAcceptable()){
                        doAccept(key);
                    }else if(key.isReadable()){
                        receive(key);
                    }else if(key.isWritable()){
                        send(key);
                    }
                    key=null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        EchoServer echoServer=new EchoServer("localhost",9999);
        echoServer.run();
    }
}
