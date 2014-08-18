package com.demon.netty.nio.chapter7;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by demon
 * Date: 14-8-18 下午11:49.
 */
public class SubReqClientHandler extends ChannelHandlerAdapter{

    public SubReqClientHandler(){

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 100; i++) {
            ctx.write(subReq(i));
        }
        ctx.flush();
    }

    private SubscribeReq subReq(int i){
        SubscribeReq req = new SubscribeReq();
        req.setAddress("北京市朝阳区");
        req.setPhoneNumber("185********");
        req.setProductName("Netty 权威指南");
        req.setSubReqID(i);
        req.setUserName("LiYe");
        return req;

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Receive server response : [" + msg + "]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
