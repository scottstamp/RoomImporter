package com.hypermine.arcturus.roomimporter.networking;

import com.eu.habbo.Emulator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;

/**
 * Created by scott on 3/14/2017.
 */
@ChannelHandler.Sharable
public class RoomMessageHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx)
    {
        Emulator.getLogging().logDebugLine("Somebody likes you!");
        // deny connection? ctx.close(); return;
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) { ctx.close(); }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        try
        {
            Emulator.getThreading().run(new RoomChannelReadHandler(ctx, msg));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        if (cause instanceof Exception)
            if (!(cause instanceof IOException))
                Emulator.getLogging().logErrorLine(cause);

        ctx.close();
    }
}
