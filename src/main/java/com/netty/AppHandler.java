package com.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class AppHandler extends ChannelInboundHandlerAdapter {

    public static class Type {
        String s;
        public Type(String s) {
            this.s = s;
        }
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Type t = (Type) msg;
        System.out.println("AppHandler#channelRead(): " + t.s);
        ChannelPromise promise = ctx.channel().newPromise().addListener(future -> {
            System.err.println(">>>>>>>><<<<<< LEAK TEST: operationComplete: " + future.toString());
        });
    }

}
