package cn.cidea.module.pay.controller.app;

import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.pay.dataobject.entity.PayChannel;
import cn.cidea.module.pay.service.IPayChannelService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/pay/channel")
@Validated
public class AppPayChannelController {

    @Resource
    private IPayChannelService channelService;

    @GetMapping("/get-enable-code-list")
    public Response<Set<String>> getEnableChannelCodeList(@RequestParam("appId") Long appId) {
        List<PayChannel> channels = channelService.getEnableList(appId);
        return Response.success(channels.stream().map(PayChannel::getCode).collect(Collectors.toSet()));
    }

}
