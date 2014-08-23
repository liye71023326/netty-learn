package com.demon.netty.nio.chapter13;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by ye.li
 * Date: 14-8-23 下午3:18.
 */
public class FileServerHandler extends SimpleChannelInboundHandler<String>{

    private static final String CR = System.getProperty("line.separator");

    public void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
        File file = new File(msg);
        if (file.exists()) {
            if (!file.isFile()) {
                ctx.writeAndFlush("Not a file : " + file + CR);
                return;
            }
            ctx.write(file + " " + file.length() + CR);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            FileRegion fileRegion = new DefaultFileRegion(randomAccessFile.getChannel(), 0, randomAccessFile.length());
            ctx.write(fileRegion);
            ctx.writeAndFlush(CR);
            randomAccessFile.close();
        }else {
            ctx.writeAndFlush("File not found: " + file + CR);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
