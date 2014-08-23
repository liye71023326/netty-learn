package com.demon.netty.nio.chapter12;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ThreadLocalRandom;

/**
 * Created by demon
 * Date: 14-8-23 上午11:16.
 */
public class ChineseProverServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    // 谚语列表
    private static final String[] DICTIONARY = {"只要功夫深，铁棒磨成针。", "旧时王谢堂前燕，飞入寻常百姓家。", "洛阳亲友如相问，" +
            "一片冰心在玉壶。", "一寸光阴一寸金，寸金难买寸光阴。", "老骥伏枥，志在千里。烈士墓年，壮心不已。"};

    private String nextQueue() {
        int quoteId = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
        return DICTIONARY[quoteId];
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        String req = msg.content().toString(CharsetUtil.UTF_8);
        System.out.println(req);
        if ("谚语字典查询?".equals(req)) {
            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("谚语查询结果: " + nextQueue(), CharsetUtil.UTF_8), msg.sender()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
