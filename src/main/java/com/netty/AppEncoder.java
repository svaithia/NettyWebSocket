package com.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;

import java.nio.charset.StandardCharsets;

public class AppEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise channelPromise) throws Exception {
        if (msg instanceof AppMessage) {
            AppMessage appMessage = (AppMessage) msg;
            ByteBuf buf = ctx.alloc().buffer(appMessage.text.length() + 1);
            buf.writeInt(appMessage.text.length());
            buf.writeCharSequence(appMessage.text, StandardCharsets.UTF_8);
            ctx.writeAndFlush(buf, channelPromise);
            return;
        }
        if (msg instanceof CloseWebSocketFrame) {
            throw new Exception("here's an exception");
        }

        ctx.writeAndFlush(msg, channelPromise);
    }
}
