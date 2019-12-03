package com.steam.game.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;


public class MessageHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println("get msg from client:"+msg);

        BinaryWebSocketFrame frame  = (BinaryWebSocketFrame) msg;
        ByteBuf buf = frame.content();

        byte[] byteArray = new byte[buf.readableBytes()];
        buf.readBytes(byteArray);

        System.out.println("get...");

        for(byte b:byteArray){
            System.out.print(b);
            System.out.print(", ");
        }
        System.out.println();
    }
}
