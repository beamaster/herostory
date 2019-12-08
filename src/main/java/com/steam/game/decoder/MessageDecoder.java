package com.steam.game.decoder;

import com.google.protobuf.GeneratedMessageV3;
import com.steam.game.encoder.MessageEncoder;
import com.steam.game.protocol.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageDecoder extends ChannelInboundHandlerAdapter {


    private static Logger logger = LoggerFactory.getLogger(MessageDecoder.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if( !(msg instanceof BinaryWebSocketFrame)){
            return;
        }


        //WebSocket 二进制消息回通过HttpServerCodec 解码成 BinaryWebSocketFrame
        BinaryWebSocketFrame frame  = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();

        //?为啥用short
        byteBuf.readShort();//读取消息的长度

        int msgCode = byteBuf.readShort();//读取消息的编号

        //拿到消息体
        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        /************解码开始***********/

        GeneratedMessageV3 cmd = null;

        switch (msgCode){
            //解码
            case MessageProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
                cmd = MessageProtocol.UserEntryCmd.parseFrom(msgBody);
                break;

                //编码
            case MessageProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
                cmd = MessageProtocol.WhoElseIsHereCmd.parseFrom(msgBody);
                break;
                /*
             case MessageProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT:
                cmd = MessageProtocol.WhoElseIsHereResult.parseFrom(msgBody);
                break;*/
        }


        if (cmd == null) {

            return;
        }
        ctx.fireChannelRead(cmd);//为何是ctx












    }
}
