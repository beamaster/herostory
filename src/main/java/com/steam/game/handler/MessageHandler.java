package com.steam.game.handler;

import com.steam.game.protocol.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;


public class MessageHandler extends SimpleChannelInboundHandler<Object> {

    /**
     * 客户端信道数组，必须用static，否则无法实现群发
     * 全体广播
     */
    private static final ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        _channelGroup.add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println("get msg from client:"+msg);


        if(msg instanceof MessageProtocol.UserEntryCmd) {

            //从指令对象中获取userId和heroAvatar
            MessageProtocol.UserEntryCmd cmd = (MessageProtocol.UserEntryCmd) msg;

            int userId = cmd.getUserId();
            String heroAvatar = cmd.getHeroAvatar();



            MessageProtocol.UserEntryResult.Builder resultBuilder = MessageProtocol.UserEntryResult.newBuilder();

            resultBuilder.setUserId(userId);
            resultBuilder.setHeroAvatar(heroAvatar);


            //构建消息结果病发送
            MessageProtocol.UserEntryResult newResult = resultBuilder.build();
            _channelGroup.writeAndFlush(newResult);
        }
    }

    /*
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
    */
}
