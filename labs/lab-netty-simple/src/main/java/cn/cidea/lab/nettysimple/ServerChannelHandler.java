package cn.cidea.lab.nettysimple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author: CIdea
 */
@ChannelHandler.Sharable
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[server]register: " + ctx.name());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[server]active: " + ctx.name());
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)  throws Exception
    {
        // 处理收到的数据，并反馈消息到到客户端
        ByteBuf in = (ByteBuf) msg;
        System.out.println("[server]rec: " + in.toString(CharsetUtil.UTF_8));

        // 写入并发送信息到远端（客户端）
        ctx.writeAndFlush(Unpooled.copiedBuffer("I'm server", CharsetUtil.UTF_8));
        // 只有pipeline会触发ChannelHandler
        ctx.pipeline().writeAndFlush(Unpooled.copiedBuffer("I'm server pipeline", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        //出现异常的时候执行的动作（打印并关闭通道）
        cause.printStackTrace();
        ctx.close();
    }
}
