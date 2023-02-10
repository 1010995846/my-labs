package cn.cidea.module.pm.controller;


import cn.cidea.framework.web.core.api.Request;
import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.pm.dataobject.convert.PmProductConvert;
import cn.cidea.module.pm.dataobject.dto.PmProductDTO;
import cn.cidea.module.pm.dataobject.dto.PmProductSaveDTO;
import cn.cidea.module.pm.service.IPmProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 产品(PmProduct)表控制层
 *
 * @author CIdea
 * @since 2023-01-09 16:48:09
 */
@RestController
@RequestMapping(value = "/product")
public class PmProductController {
    /**
     * 服务对象
     */
    @Resource
    private IPmProductService pmProductService;

    @PostMapping(value = "/getOne")
    public Response<PmProductDTO> getOne(@RequestBody Request<Long> request) {
        return Response.success(PmProductConvert.INSTANCE.toDTO(pmProductService.getById(request.getData())));
    }

    @PostMapping(value = "/save")
    public Response<PmProductDTO> save(@RequestBody Request<PmProductSaveDTO> request) {
        return Response.success(PmProductConvert.INSTANCE.toDTO(pmProductService.save(request.getData())));
    }

    @PostMapping(value = "/delete")
    public Response delete(@RequestBody Request<List<Long>> request) {
        this.pmProductService.removeByIds(request.getData());
        return Response.success();
    }
}

