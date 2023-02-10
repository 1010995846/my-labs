package cn.cidea.module.pm.controller;


import cn.cidea.framework.web.core.api.Request;
import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.pm.dataobject.convert.PmProductSkuConvert;
import cn.cidea.module.pm.dataobject.dto.PmProductSkuDTO;
import cn.cidea.module.pm.dataobject.dto.PmProductSkuSaveDTO;
import cn.cidea.module.pm.service.IPmProductSkuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (PmProductSku)表控制层
 *
 * @author CIdea
 * @since 2023-01-09 16:48:17
 */
@RestController
@RequestMapping(value = "/product/sku")
public class PmProductSkuController {
    /**
     * 服务对象
     */
    @Resource
    private IPmProductSkuService pmProductSkuService;

    @PostMapping(value = "/getOne")
    public Response<PmProductSkuDTO> getOne(@RequestBody Request<Long> request) {
        return Response.success(PmProductSkuConvert.INSTANCE.toDTO(pmProductSkuService.getById(request.getData())));
    }

    @PostMapping(value = "/save")
    public Response<PmProductSkuDTO> save(@RequestBody Request<PmProductSkuSaveDTO> request) {
        return Response.success(PmProductSkuConvert.INSTANCE.toDTO(pmProductSkuService.save(request.getData())));
    }

    @PostMapping(value = "/delete")
    public Response delete(@RequestBody Request<List<Long>> request) {
        this.pmProductSkuService.removeByIds(request.getData());
        return Response.success();
    }
}

