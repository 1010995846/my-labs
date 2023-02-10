package cn.cidea.module.pm.controller;


import cn.cidea.framework.web.core.api.Request;
import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.pm.service.IPmBrandService;
import cn.cidea.module.pm.dataobject.convert.PmBrandConvert;
import cn.cidea.module.pm.dataobject.dto.PmBrandDTO;
import cn.cidea.module.pm.dataobject.dto.PmBrandSaveDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 品牌(PmBrand)表控制层
 *
 * @author CIdea
 * @since 2023-01-09 16:47:54
 */
@RestController
@RequestMapping(value = "/brand")
public class PmBrandController {
    /**
     * 服务对象
     */
    @Resource
    private IPmBrandService pmBrandService;

    @PostMapping(value = "/getOne")
    public Response<PmBrandDTO> getOne(@RequestBody Request<Long> request) {
        return Response.success(PmBrandConvert.INSTANCE.toDTO(pmBrandService.getById(request.getData())));
    }

    @PostMapping(value = "/save")
    public Response<PmBrandDTO> save(@RequestBody Request<PmBrandSaveDTO> request) {
        return Response.success(PmBrandConvert.INSTANCE.toDTO(pmBrandService.save(request.getData())));
    }

    @PostMapping(value = "/delete")
    public Response delete(@RequestBody Request<List<Long>> request) {
        this.pmBrandService.removeByIds(request.getData());
        return Response.success();
    }
}

