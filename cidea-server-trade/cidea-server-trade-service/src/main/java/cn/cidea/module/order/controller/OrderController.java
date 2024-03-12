package cn.cidea.module.order.controller;

import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.order.dataobject.dto.OrderDTO;
import cn.cidea.module.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/trade/order")
@Validated
@Slf4j
public class OrderController {

    @Resource
    private IOrderService orderService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public Response getOrderPage(OrderDTO dto) {
        return null;
    }

    @GetMapping("/getOne")
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public Response getOrderDetail(@RequestParam("id") Long id) {
        return null;
    }

    @PostMapping("/delivery")
    @PreAuthorize("@ss.hasPermission('trade:order:delivery')")
    public Response deliveryOrder(@RequestBody OrderDTO dto) {
        return null;
    }

}
