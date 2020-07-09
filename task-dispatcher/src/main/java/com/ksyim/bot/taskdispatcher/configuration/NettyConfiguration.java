package com.ksyim.bot.taskdispatcher.configuration;

import com.ksyim.bot.taskdispatcher.configuration.netty.DispatchChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class NettyConfiguration {

    @Bean
    public EventLoopGroup loopGroup() throws InterruptedException {
        EventLoopGroup loopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(loopGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new DispatchChannelInitializer());

            Channel channel = bootstrap.connect("127.0.0.1", 5000).sync().channel();

            loopGroup.scheduleAtFixedRate(
                    () -> {
                        HttpRequest request = new DefaultHttpRequest(
                                HttpVersion.HTTP_1_1, HttpMethod.GET, "127.0.0.1:8000");
                        request.headers().set(HttpHeaderNames.HOST, "127.0.0.1:8000");
                        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);

                        channel.writeAndFlush(request);
                    },
                    500,
                    500,
                    TimeUnit.MILLISECONDS
            );

        } finally {
            loopGroup.shutdownGracefully();
        }

        return loopGroup;
    }
}
