package cn.cidea.module.pm.controller;


import cn.cidea.framework.web.core.api.Request;
import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.pm.dataobject.dto.PmMfrSaveDTO;
import cn.cidea.module.pm.service.IPmMfrService;
import cn.cidea.module.pm.dataobject.convert.PmMfrConvert;
import cn.cidea.module.pm.dataobject.dto.PmMfrDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 产商(PmMfr)表控制层
 *
 * @author CIdea
 * @since 2023-01-09 16:48:05
 */
@RestController
@RequestMapping(value = "/mfr")
public class PmMfrController {
    /**
     * 服务对象
     */
    @Resource
    private IPmMfrService pmMfrService;

    @PostMapping(value = "/getOne")
    public Response<PmMfrDTO> getOne(@RequestBody Request<Long> request) {
        return Response.success(PmMfrConvert.INSTANCE.toDTO(pmMfrService.getById(request.getData())));
    }

    @PostMapping(value = "/save")
    public Response<PmMfrDTO> save(@RequestBody Request<PmMfrSaveDTO> request) {
        return Response.success(PmMfrConvert.INSTANCE.toDTO(pmMfrService.save(request.getData())));
    }

    @PostMapping(value = "/delete")
    public Response delete(@RequestBody Request<List<Long>> request) {
        this.pmMfrService.removeByIds(request.getData());
        return Response.success();
    }
}

