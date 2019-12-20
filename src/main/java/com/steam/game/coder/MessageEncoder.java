package com.steam.game.decoder;

import com.google.protobuf.GeneratedMessageV3;
import com.steam.game.protocol.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class  MessageEncoder extends ChannelOutboundHandlerAdapter {


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg == null || !(msg instanceof GeneratedMessageV3)) {
            super.write(ctx, msg, promise);
            return;
        }


        int msgCode = -1;

        if(msg instanceof MessageProtocol.UserEntryResult){
            msgCode = MessageProtocol.MsgCode.USER_ENTRY_RESULT_VALUE;
        }else if (msg instanceof MessageProtocol.WhoElseIsHereResult){
            msgCode = MessageProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE;
        }else if (msg instanceof MessageProtocol.UserMoveToResult){
            msgCode = MessageProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE;
        }else if (msg instanceof MessageProtocol.UserQuitResult){
            msgCode = MessageProtocol.MsgCode.USER_QUIT_RESULT_VALUE;
        }else {
            log.info("无法识别的消息类型，myClazz：{}",msg.getClass().getName());
            return;
        }

//        byte[] byteArray = ((MessageProtocol.UserEntryResult) msg).toByteArray();
        byte[] byteArray = ((GeneratedMessageV3) msg).toByteArray();//encode所有消息的父类，否则就不完整

        ByteBuf byteBuf = ctx.alloc().buffer();//alloc？
        byteBuf.writeShort(0);
        byteBuf.writeShort(msgCode);
        byteBuf.writeBytes(byteArray);


        BinaryWebSocketFrame frame = new BinaryWebSocketFrame(byteBuf);
        super.write(ctx,frame,promise);
    }
}
