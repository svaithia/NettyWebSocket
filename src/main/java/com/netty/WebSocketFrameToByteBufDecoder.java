package com.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import java.util.List;

public class WebSocketFrameToByteBufDecoder extends MessageToMessageDecoder<WebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out)
            throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            ByteBuf content = msg.content();
            content.retain();
            out.add(content);
        } else {
            throw new IllegalArgumentException("Only support BinaryWebSocketFrames");
        }
    }
}
