package com.steam.game.encoder;

import com.google.protobuf.GeneratedMessageV3;
import com.steam.game.protocol.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class  MessageEncoder extends ChannelOutboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(MessageEncoder.class);

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
        }else {
            logger.info("无法识别的消息类型，myClazz：{}",msg.getClass().getName());
            return;
        }

        byte[] byteArray = ((MessageProtocol.UserEntryResult) msg).toByteArray();

        ByteBuf byteBuf = ctx.alloc().buffer();//alloc？
        byteBuf.writeShort(0);
        byteBuf.writeShort(msgCode);
        byteBuf.writeBytes(byteArray);


        BinaryWebSocketFrame frame = new BinaryWebSocketFrame(byteBuf);
        super.write(ctx,frame,promise);
    }
}
