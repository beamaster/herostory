package com.steam.game;

import com.steam.game.handler.MessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class ServerMain {

    /**
     *
     * http://cdn0001.afrxvk.cn/hero_story/demo/step010/index.html?serverAddr=127.0.0.1:12345&userId=123
     */
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();// linked
        EventLoopGroup workerGroup = new NioEventLoopGroup();// read write

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception{
                channel.pipeline().addLast(
                        new HttpServerCodec(),
                        new HttpObjectAggregator(65535),
                        new WebSocketServerProtocolHandler("/websocket"),
                        new MessageHandler()
                );
            }
        });

        try {
            ChannelFuture future = bootstrap.bind(12345).sync();

            if(future.isSuccess()){
                System.out.println("start successful...");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
