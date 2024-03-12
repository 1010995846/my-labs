package cn.cidea.module.pay.controller.app;

import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.pay.dataobject.convert.PayOrderConvert;
import cn.cidea.module.pay.dataobject.dto.PayOrderDTO;
import cn.cidea.module.pay.dataobject.dto.PayOrderSaveDTO;
import cn.cidea.module.pay.dataobject.dto.PayOrderUpdateDTO;
import cn.cidea.module.pay.dataobject.entity.PayOrder;
import cn.cidea.module.pay.service.IPayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/pay/order")
@Validated
@Slf4j
public class AppPayOrderController {

    @Resource
    private IPayOrderService payOrderService;

    @GetMapping("/get")
    public Response<PayOrderDTO> get(@RequestParam("id") Long id) {
        return Response.success(PayOrderConvert.INSTANCE.toDTO(payOrderService.getById(id)));
    }

    @PostMapping("/create")
    public Response<PayOrderDTO> create(@RequestBody PayOrderSaveDTO dto) {
        PayOrder order = payOrderService.create(dto);
        return Response.success(PayOrderConvert.INSTANCE.toDTO(order));
    }
    @PostMapping("/submit")
    public Response<PayOrderDTO> submit(@RequestBody PayOrderUpdateDTO dto) {
        PayOrder order = payOrderService.submit(dto);
        return Response.success(PayOrderConvert.INSTANCE.toDTO(order));
    }

}
