package com.steam.game.decoder;


import com.google.protobuf.Message;
import com.steam.game.protocol.MessageProtocol;

/**
 * 消息识别器
 */
public class MessageRecognizer {


    /**
     * 构建私有构造器
     */
    private MessageRecognizer(){}


    /**
     * 消息创建器
     * @param msgCode
     * @return
     */
    public static Message.Builder getBuilder(int msgCode){

        switch (msgCode){
            //解码
            case MessageProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
                return MessageProtocol.UserEntryCmd.newBuilder();

            //编码
            case MessageProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
                return MessageProtocol.WhoElseIsHereCmd.newBuilder();

            case MessageProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE:
                return MessageProtocol.UserMoveToCmd.newBuilder();

            default:
                return null;
        }
    }
}
