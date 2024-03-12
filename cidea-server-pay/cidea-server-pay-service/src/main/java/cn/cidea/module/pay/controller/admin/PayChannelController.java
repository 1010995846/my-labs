package cn.cidea.module.pay.controller.admin;

import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.pay.dataobject.convert.PayChannelConvert;
import cn.cidea.module.pay.dataobject.dto.PayChannelDTO;
import cn.cidea.module.pay.dataobject.dto.PayChannelSaveDTO;
import cn.cidea.module.pay.dataobject.entity.PayChannel;
import cn.cidea.module.pay.service.IPayChannelService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/pay/channel")
@Validated
public class PayChannelController {

    @Resource
    private IPayChannelService channelService;

    @PostMapping("/submit")
    // @PreAuthorize("@ss.hasPermission('pay:channel:create')")
    public Response<PayChannelDTO> submit(@Valid @RequestBody PayChannelSaveDTO saveDTO) {
        return Response.success(PayChannelConvert.INSTANCE.toDTO(channelService.submit(saveDTO)));
    }

    @DeleteMapping("/delete")
    // @PreAuthorize("@ss.hasPermission('pay:channel:delete')")
    public Response delete(@RequestParam("id") Long id) {
        channelService.delete(id);
        return Response.success();
    }

    @GetMapping("/get")
    // @PreAuthorize("@ss.hasPermission('pay:channel:query')")
    public Response<PayChannelDTO> get(@RequestParam(value = "id", required = false) Long id,
                                       @RequestParam(value = "appId", required = false) Long appId,
                                       @RequestParam(value = "code", required = false) String code) {
        PayChannel channel = null;
        if (id != null) {
            channel = channelService.get(id);
        } else if (appId != null && code != null) {
            channel = channelService.getByAppIdAndCode(appId, code);
        }
        return Response.success(PayChannelConvert.INSTANCE.toDTO(channel));
    }

    @GetMapping("/get-enable-code-list")
    public Response<Set<String>> getEnableChannelCodeList(@RequestParam("appId") Long appId) {
        List<PayChannel> channels = channelService.getEnableList(appId);
        return Response.success(channels.stream().map(PayChannel::getCode).collect(Collectors.toSet()));
    }

}
