package com.demon.netty.nio.chapter4;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;


/**
 * Created by demon
 * Date: 14-8-17 下午9:48.
 */
public class TimeClient {

    public void connect(int port, String host) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChildChannelHandler());

            // 发起异步连接操作ChannelFuture
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            //等待客户端链路关闭
            channelFuture.channel().closeFuture().sync();
        }finally {
            // 优雅退出， 释放NIO线程租
            group.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            // 解决粘包问题
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
            ch.pipeline().addLast(new StringDecoder());
            // end
            ch.pipeline().addLast(new TimeClientHandler());

        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        if (args != null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){

            }
        }
        new TimeClient().connect(port, "127.0.0.1");
    }


}
