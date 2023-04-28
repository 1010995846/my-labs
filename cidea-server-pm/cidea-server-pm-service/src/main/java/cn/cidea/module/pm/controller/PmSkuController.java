package cn.cidea.module.pm.controller;


import cn.cidea.framework.web.core.api.Request;
import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.pm.dataobject.convert.PmSkuConvert;
import cn.cidea.module.pm.dataobject.dto.PmSkuDTO;
import cn.cidea.module.pm.dataobject.dto.PmSkuSaveDTO;
import cn.cidea.module.pm.service.IPmSkuService;
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
@RequestMapping(value = "/sku")
public class PmSkuController {
    /**
     * 服务对象
     */
    @Resource
    private IPmSkuService pmProductSkuService;

    @PostMapping(value = "/getOne")
    public Response<PmSkuDTO> getOne(@RequestBody Request<Long> request) {
        return Response.success(PmSkuConvert.INSTANCE.toDTO(pmProductSkuService.getById(request.getData())));
    }

    @PostMapping(value = "/save")
    public Response<PmSkuDTO> save(@RequestBody Request<PmSkuSaveDTO> request) {
        return Response.success(PmSkuConvert.INSTANCE.toDTO(pmProductSkuService.save(request.getData())));
    }

    @PostMapping(value = "/delete")
    public Response delete(@RequestBody Request<List<Long>> request) {
        this.pmProductSkuService.removeByIds(request.getData());
        return Response.success();
    }
}

