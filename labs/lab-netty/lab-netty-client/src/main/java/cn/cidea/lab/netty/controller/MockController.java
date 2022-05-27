package cn.cidea.lab.netty.controller;

import cn.cidea.framework.web.core.api.Response;
import cn.cidea.lab.netty.AuthContext;
import cn.cidea.lab.netty.client.NettyClient;
import cn.cidea.lab.netty.message.auth.AuthRequest;
import cn.cidea.lab.netty.message.chat.ChatGroupCreateRequest;
import cn.cidea.lab.netty.message.chat.ChatGroupRequest;
import cn.cidea.lab.netty.service.ChatMockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模拟客户端
 */
@RestController
@RequestMapping("/mock")
public class MockController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private NettyClient nettyClient;
    @Autowired
    private ChatMockService chatMockService;

    @PostMapping("/login")
    public Response mock(String name) {
        if(name.equals(AuthContext.getName())){
            return Response.fail(500, "已登录");
        }
        AuthRequest request = new AuthRequest();
        request.setToken(name);
        nettyClient.send(request);
        return Response.success();
    }

    @PostMapping("/chat/send")
    public Response mock(String toUser, String message) {
        chatMockService.sendTo(toUser, message);
        return Response.success();
    }

    /**
     * 编辑群组人员
     * @param request
     * @return
     */
    @PostMapping("/chat/group/edit")
    public Response chatGroup(@RequestBody ChatGroupCreateRequest request) {
        chatMockService.group(request);
        return Response.success();
    }

    /**
     * 向群组发送消息
     * @return
     */
    @PostMapping("/chat/group/send")
    public Response chatSendToGroup(@RequestBody ChatGroupRequest request) {
        chatMockService.sendToGroup(request);
        return Response.success();
    }
}
