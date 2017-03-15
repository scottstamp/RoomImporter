package com.hypermine.arcturus.roomimporter.networking;

import com.eu.habbo.Emulator;
import com.eu.habbo.networking.gameserver.GameByteDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by scott on 3/14/2017.
 */
public class RoomServer {
    private final ServerBootstrap serverBootstrap;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    private final String host;
    private final int port;

    public RoomServer(String host, int port) throws Exception
    {
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup(1);

        this.serverBootstrap = new ServerBootstrap();

        this.host = host;
        this.port = port;
    }

    public void initialize()
    {
        this.serverBootstrap.group(this.bossGroup, this.workerGroup);
        this.serverBootstrap.channel(NioServerSocketChannel.class);
        final RoomMessageHandler roomMessageHandler = new RoomMessageHandler();
        this.serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>()
        {
            @Override
            public void initChannel(SocketChannel ch) throws Exception
            {
                ch.pipeline().addLast("logger", new LoggingHandler());
                //ch.pipeline().addLast("bytesDecoder", new GameByteDecoder());
                ch.pipeline().addLast(roomMessageHandler);
                ch.pipeline().addLast("idlehandler", new IdleStateHandler(0, 0, 60)
                {
                    @Override
                    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception
                    {
                        ctx.close();
                    }
                });
            }
        });
        this.serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        this.serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        this.serverBootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
        this.serverBootstrap.childOption(ChannelOption.SO_RCVBUF, 1024000);
        this.serverBootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1024000));
        this.serverBootstrap.childOption(ChannelOption.ALLOCATOR, new UnpooledByteBufAllocator(false));
    }

    public void connect()
    {
        ChannelFuture channelFuture = this.serverBootstrap.bind(this.host, this.port);

        while (!channelFuture.isDone());

        if (!channelFuture.isSuccess())
        {
            Emulator.getLogging().logErrorLine("[RoomImporter] Failed to connect to the host (" + this.host + ":" + this.port + ").");
        }
        else
        {
            Emulator.getLogging().logStart("[RoomImporter] Started on " + this.host + ":" + this.port);
        }
    }

    public void stop()
    {
        this.workerGroup.shutdown();
        this.bossGroup.shutdown();
    }
}
