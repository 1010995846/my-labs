package cn.cidea.lab.nettysimple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;

/**
 * {@link ChannelPipeline}是Netty处理请求的责任链
 * {@link io.netty.channel.ChannelHandler}则是具体处理请求的处理器
 * @author: CIdea
 */
@AllArgsConstructor
public class Server {

    private final Integer port;

    private void run() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        bootstrap.group(bossGroup, workerGroup )
                .channel(NioServerSocketChannel.class)
                .localAddress(port)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ServerChannelHandler())
                                .addLast(new ServerChannelOutHandler());
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.bind().sync();
            System.out.println("在" + channelFuture.channel().localAddress() + "上开启监听");

            // 阻塞操作，closeFuture()开启了一个channel的监听器（这期间channel在进行各项工作），直到链路断开
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully().sync();//关闭EventLoopGroup并释放所有资源，包括所有创建的线程
            workerGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Server(9110).run();
    }

}
