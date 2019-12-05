package com.steam.game.encoder;

import com.google.protobuf.GeneratedMessageV3;
import com.steam.game.protocol.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
//import lombok.extern.slf4j.Slf4j;


//@Slf4j
public class MessageEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg == null || !(msg instanceof GeneratedMessageV3)) {
            super.write(ctx, msg, promise);
            return;
        }

        int msgCode = -1;

        if(msg instanceof MessageProtocol.UserEntryResult){

            msgCode = MessageProtocol.MsgCode.USER_ENTRY_RESULT_VALUE;
        }else {

            //("无法识别的消息类型，myClazz：{}",msg.getClass().getName())
            return;
        }

        byte[] byteArray = ((MessageProtocol.UserEntryResult) msg).toByteArray();

        ByteBuf byteBuf = ctx.alloc().buffer();//alloc？
        byteBuf.writeShort(0);
    }
}
