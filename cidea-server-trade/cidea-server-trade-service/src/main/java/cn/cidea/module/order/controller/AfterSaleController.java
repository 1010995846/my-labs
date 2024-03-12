package cn.cidea.module.order.controller;

import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.order.dataobject.dto.AfterSaleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/trade/after-sale")
@Validated
@Slf4j
public class AfterSaleController {

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('trade:after-sale:query')")
    public Response getAfterSalePage(@Valid AfterSaleDTO pageVO) {
        return null;
    }

    @PutMapping("/agree")
    @PreAuthorize("@ss.hasPermission('trade:after-sale:agree')")
    public Response agreeAfterSale(@RequestParam("id") String id) {
        return null;
    }

    @PutMapping("/disagree")
    @PreAuthorize("@ss.hasPermission('trade:after-sale:disagree')")
    public Response disagreeAfterSale(@RequestBody AfterSaleDTO confirmReqVO) {
        return null;
    }

    @PutMapping("/receive")
    @PreAuthorize("@ss.hasPermission('trade:after-sale:receive')")
    public Response<Boolean> receiveAfterSale(@RequestParam("id") String id) {
        return null;
    }

    @PutMapping("/refuse")
    @PreAuthorize("@ss.hasPermission('trade:after-sale:receive')")
    public Response<Boolean> refuseAfterSale(AfterSaleDTO refuseReqVO) {
        return null;
    }

    @PostMapping("/refund")
    @PreAuthorize("@ss.hasPermission('trade:after-sale:refund')")
    public Response<Boolean> refundAfterSale(@RequestParam("id") Long id) {
        return null;
    }

}
