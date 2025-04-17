package cn.cidea.lab.nettysimple;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 抽象类{@link SimpleChannelInboundHandler}是{@link ChannelInboundHandlerAdapter}的子类
 * 重写了{@link SimpleChannelInboundHandler#channelRead(ChannelHandlerContext, Object)}方法
 * 当与声明的泛型匹配时执行用户实现的{@link SimpleChannelInboundHandler#channelRead0(ChannelHandlerContext, Object)}方法
 * 否则调用{@link ChannelHandlerContext#fireChannelRead(Object)}传递到下一个处理器，如果最终没人处理会抛 onUnhandledInboundException
 * 因此{@link SimpleChannelInboundHandler}一般用在特定消息类型的处理上，{@link ChannelInboundHandlerAdapter}起到类似前置或兜底的作用
 * @author: CIdea
 */
@ChannelHandler.Sharable
public class ClientChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        System.out.println("[client]rec: " + byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
