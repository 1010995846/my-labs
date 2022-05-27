package cn.cidea.lab.netty.server;

import cn.cidea.lab.netty.codec.Invocation;
import cn.cidea.lab.netty.core.Message;
import cn.cidea.lab.netty.message.chat.ChatRedirectRequest;
import cn.cidea.lab.netty.message.chat.ChatRequest;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 客户端 Channel 注册器。提供两种功能：
 * 1. 客户端 Channel 的管理
 * 2. 向客户端 Channel 发送消息
 * @author Charlotte
 */
@Component
public class NettyChannelRegistrar {

    /**
     * {@link Channel#attr(AttributeKey)} 属性中，表示 Channel 对应的用户
     */
    private static final AttributeKey<String> CHANNEL_ATTR_KEY_USER = AttributeKey.newInstance("user");

    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Channel 映射
     */
    private ConcurrentMap<ChannelId, Channel> channels = new ConcurrentHashMap<>();
    /**
     * 用户与 Channel 的映射。
     *
     * 通过它，可以获取用户对应的 Channel。这样，我们可以向指定用户发送消息。
     */
    private ConcurrentMap<String, Channel> userChannels = new ConcurrentHashMap<>();

    private ConcurrentMap<Long, Set<String>> groups = new ConcurrentHashMap<>();

    /**
     * 添加 Channel 到 {@link #channels} 中
     *
     * @param channel Channel
     */
    public void add(Channel channel) {
        channels.put(channel.id(), channel);
        log.info("[add][一个连接({})加入]", channel.id());
    }

    /**
     * 添加指定用户到 {@link #userChannels} 中
     *
     * @param channel Channel
     * @param user 用户
     */
    public void addUser(Channel channel, String user) {
        Channel existChannel = channels.get(channel.id());
        if (existChannel == null) {
            log.error("[addUser][连接({}) 不存在]", channel.id());
            return;
        }
        // 设置属性
        channel.attr(CHANNEL_ATTR_KEY_USER).set(user);
        // 添加到 userChannels
        userChannels.put(user, channel);
    }

    /**
     * 将 Channel 从 {@link #channels} 和 {@link #userChannels} 中移除
     *
     * @param channel Channel
     */
    public void remove(Channel channel) {
        // 移除 channels
        channels.remove(channel.id());
        // 移除 userChannels
        if (channel.hasAttr(CHANNEL_ATTR_KEY_USER)) {
            userChannels.remove(channel.attr(CHANNEL_ATTR_KEY_USER).get());
        }
        log.info("[remove][一个连接({})离开]", channel.id());
    }

    public void send(String user, Message message) {
        Invocation invocation = new Invocation(message);
        // 获得用户对应的 Channel
        Channel channel = userChannels.get(user);
        if (channel == null) {
            log.error("[send][连接{}不存在]", user);
            return;
        }
        if (!channel.isActive()) {
            log.error("[send][连接({})未激活]", channel.id());
            return;
        }
        // TODO 发送消息
        channel.writeAndFlush(invocation);
    }

    public void sendAll(){
        for (Channel channel : channels.values()) {
            if (!channel.isActive()) {
                log.error("[send][连接({})未激活]", channel.id());
                return;
            }
            // TODO 发送消息
            // channel.writeAndFlush();
        }
    }

    public void editGroup(Long groupId, List<String> userList) {
        Set<String> userSet = groups.get(groupId);
        if(userSet == null){
            userSet = new HashSet<>(userList);
            groups.put(groupId, userSet);
        } else {
            userSet.addAll(userList);
        }
    }

    public void sendToGroup(Long groupId, String message) {
        Set<String> userSet = groups.get(groupId);
        for (String user : userSet) {
            ChatRedirectRequest request = new ChatRedirectRequest();
            request.setFromUser(String.valueOf(groupId));
            request.setMessage(message);
            send(user, request);
        }
    }
}
