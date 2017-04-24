package com.basic.nio.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

/**
 * locate com.basic.nio.test
 * Created by 79875 on 2017/4/18.
 *  NIO 简单应用 Sum服务器程序
 */
public class SumServer {
    private ByteBuffer _buffer = ByteBuffer.allocate(8);
    private IntBuffer _intBuffer = _buffer.asIntBuffer();
    private SocketChannel _clientChannel = null;
    private ServerSocketChannel _serverChannel = null;

    public void start() {
        try {
            openChannel();
            waitForConnection();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
    private void openChannel() throws IOException {
        _serverChannel = ServerSocketChannel.open();
        _serverChannel.socket().bind(new InetSocketAddress(10001));
        _serverChannel.configureBlocking(false);// 设置为非阻塞形式
        System.out.println("服务器通道已经打开");
    }
    /*
     * private void waitForConnection() throws IOException { while (true) {
     * _clientChannel = _serverChannel.accept(); if (_clientChannel != null) {
     * System.out.println("新的连接加入"); processRequest(); _clientChannel.close(); } } }
     */
    private void waitForConnection() throws IOException {
        Selector acceptSelector = SelectorProvider.provider().openSelector();
        /*
         * 在服务器套接字上注册selector并设置为接受accept方法的通知。
         * 这就告诉Selector，套接字想要在accept操作发生时被放在ready表 上，因此，允许多元非阻塞I/O发生。
         */
        SelectionKey acceptKey = _serverChannel.register(acceptSelector,
                SelectionKey.OP_ACCEPT);
        int keysAdded = 0;
        /* select方法在任何上面注册了的操作发生时返回 */
        while ((keysAdded = acceptSelector.select()) > 0) {
            // 某客户已经准备好可以进行I/O操作了，获取其ready键集合
            Set readyKeys = acceptSelector.selectedKeys();
            Iterator i = readyKeys.iterator();
            // 遍历ready键集合，并处理加法请求
            while (i.hasNext()) {
                SelectionKey sk = (SelectionKey) i.next();
                i.remove();
                if (sk.isAcceptable()) {
                    ServerSocketChannel nextReady = (ServerSocketChannel) sk
                            .channel();
                    // 接受加法请求并处理它
                    _clientChannel = nextReady.accept();
                    //  Socket _clientSocket = _clientChannel.socket();
                    processRequest();
                    //  _clientSocket.close();
                }
            }
        }
    }
    private void processRequest() throws IOException {
        _buffer.clear();
        _clientChannel.read(_buffer);
        int result = _intBuffer.get(0) + _intBuffer.get(1);
        _buffer.flip();
        _buffer.clear();
        _intBuffer.put(0, result);
        _clientChannel.write(_buffer);
    }
    public static void main(String[] args) {
        new SumServer().start();
    }
} // SumServer
