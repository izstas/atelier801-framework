package com.atelier801.transformice.client;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.channel.ChannelHandler.Sharable;

@Sharable
final class LoggingHandler extends ChannelDuplexHandler {
    final static LoggingHandler INSTANCE = new LoggingHandler();

    private static final Logger logger = LoggerFactory.getLogger(LoggingHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.trace("{} ACTIVE", ctx.channel());
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.trace("{} INACTIVE", ctx.channel());
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.trace("{} READ: {}", ctx.channel(), msg);
        ctx.fireChannelRead(msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        logger.trace("{} WRITE: {}", ctx.channel(), msg);
        ctx.write(msg, promise);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("{} EXCEPTION", ctx.channel(), cause);
        ctx.fireExceptionCaught(cause);
    }
}
