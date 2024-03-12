package cn.cidea.module.pay.controller.admin;


import cn.cidea.framework.web.core.api.Request;
import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.pay.dataobject.convert.PayOrderConvert;
import cn.cidea.module.pay.dataobject.dto.PayOrderDTO;
import cn.cidea.module.pay.dataobject.dto.PayOrderSaveDTO;
import cn.cidea.module.pay.dataobject.entity.PayOrder;
import cn.cidea.module.pay.dataobject.factory.PayOrderFactory;
import cn.cidea.module.pay.service.IPayOrderService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.db.PageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 支付订单(PayOrder)表控制层
 *
 * @author CIdea
 * @since 2023-11-30 09:53:08
 */
@RestController
@RequestMapping("payOrder")
public class PayOrderController {
    /**
     * 服务对象
     */
    @Resource
    private IPayOrderService service;
    @Resource
    private PayOrderFactory factory;

    /**
     * 订单详情
     * @param request
     * @return
     */
    @PostMapping(value = "/getOne")
    public Response<PayOrderDTO> getOne(@RequestBody Request<Long> request) {
        return Response.success(PayOrderConvert.INSTANCE.toDTO(service.getById(request.getData())));
    }
    @PostMapping(value = "/get-detail")
    public Response<PayOrderDTO> getDetail(@RequestBody Request<Long> request) {
        PayOrder order = service.getById(request.getData());
        factory.build(order).app().extension();
        return Response.success(PayOrderConvert.INSTANCE.toDTO(order));
    }

    /**
     * 提交支付订单
     * @param request
     * @return
     */
    @PostMapping(value = "/submit")
    public Response<PayOrderDTO> submit(@RequestBody Request<PayOrderSaveDTO> request) {
        return Response.success(PayOrderConvert.INSTANCE.toDTO(service.submit(request.getData())));
    }

    /**
     * 取消订单
     * @param request
     * @return
     */
    @PostMapping(value = "/cancel")
    public Response cancel(@RequestBody Request<PayOrderDTO> request) {
        this.service.cancel(request.getData().getId());
        return Response.success();
    }

    /**
     * 删除订单
     * @param request
     * @return
     */
    @PostMapping(value = "/delete")
    public Response delete(@RequestBody Request<List<Long>> request) {
        this.service.removeByIds(request.getData());
        return Response.success();
    }

    @GetMapping("/page")
    // @PreAuthorize("@ss.hasPermission('pay:order:query')")
    public Response<IPage<PayOrderDTO>> getOrderPage(@Valid PayOrderDTO dto) {
        IPage<PayOrder> page = new Page<>();
        page = service.listByAdmin(dto, page);
        factory.build(page.getRecords()).app();
        return Response.success(page.convert(PayOrderConvert.INSTANCE::toDTO));
    }
}

