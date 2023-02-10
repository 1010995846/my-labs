package cn.cidea.module.pm.controller;


import cn.cidea.framework.web.core.api.Request;
import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.pm.dataobject.dto.PmCatSaveDTO;
import cn.cidea.module.pm.service.IPmCatService;
import cn.cidea.module.pm.dataobject.convert.PmCatConvert;
import cn.cidea.module.pm.dataobject.dto.PmCatDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 品类(PmCat)表控制层
 *
 * @author CIdea
 * @since 2023-01-09 16:48:00
 */
@RestController
@RequestMapping(value = "/cat")
public class PmCatController {
    /**
     * 服务对象
     */
    @Resource
    private IPmCatService pmCatService;

    @PostMapping(value = "/getOne")
    public Response<PmCatDTO> getOne(@RequestBody Request<Long> request) {
        return Response.success(PmCatConvert.INSTANCE.toDTO(pmCatService.getById(request.getData())));
    }

    @PostMapping(value = "/save")
    public Response<PmCatDTO> save(@RequestBody Request<PmCatSaveDTO> request) {
        return Response.success(PmCatConvert.INSTANCE.toDTO(pmCatService.save(request.getData())));
    }

    @PostMapping(value = "/delete")
    public Response delete(@RequestBody Request<List<Long>> request) {
        this.pmCatService.removeByIds(request.getData());
        return Response.success();
    }
}

