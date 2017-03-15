package com.hypermine.arcturus.roomimporter.networking;

import com.eu.habbo.Emulator;
import com.eu.habbo.util.crypto.ZIP;
import com.google.gson.Gson;
import com.hypermine.arcturus.roomimporter.handlers.ImportHandler;
import com.hypermine.arcturus.roomimporter.payloads.RoomData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by scott on 3/14/2017.
 */
public class RoomChannelReadHandler implements Runnable {
    private final ChannelHandlerContext ctx;
    private final Object msg;

    public RoomChannelReadHandler(ChannelHandlerContext ctx, Object msg)
    {
        this.ctx = ctx;
        this.msg = msg;
    }

    public void run()
    {
        ByteBuf m = (ByteBuf) msg;
        Emulator.getLogging().logDebugLine("Packet handled");
        int length = m.readInt();
        Emulator.getLogging().logDebugLine("Length: " + length);
        ByteBuf data = Unpooled.wrappedBuffer(m.readBytes(m.readableBytes()));
        String json = new String(ZIP.inflate(data.array()));

        Gson gson = new Gson();
        RoomData roomData = gson.fromJson(json, RoomData.class);
        ImportHandler.handle(roomData);

        data.release();
        m.release();
    }
}
