package com.basic.netty.test.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * locate com.basic.netty.test.handler
 * Created by 79875 on 2017/4/18.
 * 客户端请求处理Handler
 */
public class SimpleClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("SimpleClientHandler.channelRead");
        ByteBuf result = (ByteBuf) msg;
        System.out.println(result.readableBytes());
        byte[] result1 = new byte[result.readableBytes()];
//        result.readBytes(result1);
//        System.out.println("Server said:" + new String(result1));
        result.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }


    // 连接成功后，向server发送消息
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String msg = "hello Server!";
        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());
        encoded.writeBytes(msg.getBytes());
        ctx.write(encoded);
        ctx.flush();
        Thread.sleep(10000);
        String msg2 = "hello Server! second";
        ByteBuf encoded2 = ctx.alloc().buffer(msg2.getBytes().length);
        encoded.writeBytes(msg2.getBytes());
        ctx.write(encoded2);
        ctx.flush();
    }
}
