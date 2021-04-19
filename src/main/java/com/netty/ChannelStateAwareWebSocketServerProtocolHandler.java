package com.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.List;


public class ChannelStateAwareWebSocketServerProtocolHandler
        extends io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler {

    private static final AttributeKey<WebSocketServerHandshaker> HANDSHAKER_ATTR_KEY =
            AttributeKey.valueOf(WebSocketServerHandshaker.class, "HANDSHAKER");

    private static final int MAX_BUFFER_SIZE = 512 * 1024;

    public ChannelStateAwareWebSocketServerProtocolHandler(
            String websocketPath,
            String subprotocols,
            boolean allowExtensions,
            int maxFrameSize,
            boolean allowMaskMismatch,
            boolean checkStartsWith) {
        super(
                websocketPath,
                subprotocols,
                allowExtensions,
                maxFrameSize,
                allowMaskMismatch,
                checkStartsWith);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out)
            throws Exception {

        if (frame instanceof CloseWebSocketFrame) {
            frame.retain();
            ChannelPromise promise =
                    ctx.channel()
                            .newPromise()
                            .addListener(
                                    future -> {
                                        System.err.println(
                                                ">>>>>>>><<<<<< LEAK TEST: operationComplete: " + future.toString());
                                    });
            ctx.channel().writeAndFlush(frame, promise).addListener(ChannelFutureListener.CLOSE);
            return;
        }

        if (frame instanceof PingWebSocketFrame) {
            frame.content().retain();
            ctx.writeAndFlush(new PongWebSocketFrame(frame.content()));
            return;
        }

        if (frame instanceof PongWebSocketFrame) {
            return;
        }

        out.add(frame.retain());
    }

}
