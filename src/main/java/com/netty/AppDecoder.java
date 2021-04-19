package com.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class AppDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String len = in.readCharSequence(1, StandardCharsets.UTF_8).toString();
        String str = in.readCharSequence(Integer.parseInt(len), StandardCharsets.UTF_8).toString();
        out.add(new AppHandler.Type(str));
    }
}
