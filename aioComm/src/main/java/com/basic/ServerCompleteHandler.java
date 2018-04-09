package com.basic;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * locate com.basic
 * Created by mastertj on 2018/4/9.
 */
public class ServerCompleteHandler implements CompletionHandler<AsynchronousSocketChannel,Server>{

    @Override
    public void completed(AsynchronousSocketChannel result, Server attachment) {
        //当有下一个客户端接入的时候，直接调用Server的accept方法，进行反复执行下去，保证多个户端可以阻塞
        attachment.assc.accept(attachment,this);
        read(result);
    }

    private void read(AsynchronousSocketChannel assc) {
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
        assc.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                try {
                    //进行读取之后，充值标志位
                    attachment.flip();
                    //获取读取的字节数
                    System.out.println("Server -> 服务器收到的数据长度: "+result);
                    //获取读取的数据
                    byte[] bytes=new byte[attachment.remaining()];
                    attachment.get(bytes);
                    String str=new String(bytes,"UTF-8");
                    System.out.println("收到服务器的数据："+str);
                    String response="服务器响应！！！";
                    writedata(assc,response);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            private void writedata(AsynchronousSocketChannel assc, String response) {
                try {
                    ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
                    byteBuffer.put(response.getBytes());
                    byteBuffer.flip();
                    Future<Integer> future = assc.write(byteBuffer);
                    Integer integer = future.get();
                    System.out.println("服务器-> 发送数据的字节数："+integer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });
    }

    @Override
    public void failed(Throwable exc, Server attachment) {
        exc.printStackTrace();
    }
}
