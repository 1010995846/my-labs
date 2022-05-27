ChannelHandler

用来处理 Channel 的各种事件。这里的事件很广泛，比如可以是连接、数据读写、异常、数据转换等等



ChannelInitializer

用于 Channel 创建时，实现自定义的初始化逻辑

在每一个客户端与服务端建立完成连接时，服务端会创建一个 Channel 与之对应。此时，NettyServerHandlerInitializer 会进行执行 `#initChannel(Channel c)` 方法，进行自定义的初始化。

调用 Channel 的 `#pipeline()` 方法，获得客户端 Channel 对应的 [ChannelPipeline](https://github.com/YunaiV/netty/blob/4.1/transport/src/main/java/io/netty/channel/ChannelPipeline.java)。ChannelPipeline 由一系列的 ChannelHandler 组成，又或者说是 ChannelHandler 链。这样， Channel 所有上所有的事件都会经过 ChannelPipeline，被其上的 ChannelHandler 所处理。



**数据的读写**需要对消息进行序列化和反序列化

> TODO  Protobuf序列化

MessageToByteEncoder、ByteToMessageDecoder分别实现

```java
public abstract class MessageToByteEncoder<I> extends ChannelOutboundHandlerAdapter {
    /**
     * 将msg写入out
     */
	protected abstract void encode(ChannelHandlerContext ctx, I msg, ByteBuf out) throws Exception;
}
```

```java
public abstract class MessageToByteEncoder<I> extends ChannelOutboundHandlerAdapter {
	/**
	 * 从in中读取并加入out
	 */
    protected abstract void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception;
}
```



粘包和拆包，操作系统在发送 TCP 数据的时候，底层会有一个缓冲区，例如 1024 个字节大小

- 如果一次请求发送的数据量**比较小**，没达到缓冲区大小，TCP 则会将多个请求合并为同一个请求进行发送，这就形成了**粘包**问题。

- 如果一次请求发送的数据量**比较大**，超过了缓冲区大小，TCP 就会将其拆分为多次发送，这就是**拆包**，也就是将一个大的包拆分为多个小包进行发送。

解决方案

1. 固定长度，空位补全

   > 考虑到整包可能超过固定长度，需把最后一位作为标识位判断是否分包

2. 分隔符

   > 1的上位方案，无需补全多余字符，仅需一个分隔符
   >
   > 但有分隔符被使用的风险
   >
   > 具体案例： HTTP、WebSocket、Redis

3. 消息头，在消息头中描述消息的参数，长度等

   > 1的上位方案，动态长度
   >
   > 具体案例：mysql的varchar



Reactor模型

医保流水号

- 互联网医院线上，线上问诊医保支付

- 门户，接口平台取TODO
- 纯线下，不支持



