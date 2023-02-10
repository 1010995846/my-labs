package cn.cidea.module.pm.controller;


import cn.cidea.framework.web.core.api.Request;
import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.pm.dataobject.convert.PmPropConvert;
import cn.cidea.module.pm.dataobject.dto.PmPropDTO;
import cn.cidea.module.pm.dataobject.dto.PmPropSaveDTO;
import cn.cidea.module.pm.service.IPmPropService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (PmProp)表控制层
 *
 * @author CIdea
 * @since 2023-01-09 14:24:30
 */
@RestController
@RequestMapping(value = "/prop")
public class PmPropController {
    /**
     * 服务对象
     */
    @Resource
    private IPmPropService pmPropService;

    @PostMapping(value = "/getOne")
    public Response<PmPropDTO> getOne(@RequestBody Request<Long> request) {
        return Response.success(PmPropConvert.INSTANCE.toDTO(pmPropService.getById(request.getData())));
    }

    @PostMapping(value = "/save")
    public Response<PmPropDTO> save(@RequestBody Request<PmPropSaveDTO> request) {
        return Response.success(PmPropConvert.INSTANCE.toDTO(pmPropService.save(request.getData())));
    }

    @PostMapping(value = "/delete")
    public void delete(@RequestBody Request<List<Long>> request) {
        this.pmPropService.removeByIds(request.getData());
    }
}

