package com.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

import javax.net.ssl.SSLEngine;

public class HTTPInitializerPipeline extends ChannelInitializer<SocketChannel> {
    private static final int MAX_BUFFER_SIZE = 512 * 1024;

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        SSLEngine engine = SecureSocketSslContextFactory
                .getClientContext().createSSLEngine();
        engine.setUseClientMode(true);
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(MAX_BUFFER_SIZE));
        pipeline.addLast("wsServerCompressionHandler", new WebSocketServerCompressionHandler());
        pipeline.addLast(
                "wsServerProtocolChat",
                new ChannelStateAwareWebSocketServerProtocolHandler(
                        "/test", null, true, MAX_BUFFER_SIZE, false, true));
        pipeline.addLast("wsFrameAggregator", new WebSocketFrameAggregator(MAX_BUFFER_SIZE));
        pipeline.addLast("wsFrameToByteBuffDecoder", new WebSocketFrameToByteBufDecoder());
        pipeline.addLast("appDecoder", new AppDecoder());
        pipeline.addLast("appEncoder", new AppEncoder());
        pipeline.addLast("appHandler", new AppHandler());

    }
}