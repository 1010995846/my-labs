package cn.cidea.module.pay.controller.admin;

import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.pay.dataobject.convert.PayAppConvert;
import cn.cidea.module.pay.dataobject.dto.PayAppDTO;
import cn.cidea.module.pay.dataobject.dto.PayAppSaveDTO;
import cn.cidea.module.pay.dataobject.entity.PayApp;
import cn.cidea.module.pay.dataobject.factory.PayAppFactory;
import cn.cidea.module.pay.service.IPayAppService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/pay/app")
@Validated
public class PayAppController {

    @Resource
    private IPayAppService appService;
    @Resource
    private PayAppFactory factory;

    @PostMapping("/submit")
    // @PreAuthorize("@ss.hasPermission('pay:app:create')")
    public Response<PayAppDTO> submit(@Valid @RequestBody PayAppSaveDTO saveDTO) {
        return Response.success(PayAppConvert.INSTANCE.toDTO(appService.submit(saveDTO)));
    }

    @PutMapping("/update-status")
    // @PreAuthorize("@ss.hasPermission('pay:app:update')")
    public Response updateAppStatus(@Valid @RequestBody PayAppSaveDTO saveDTO) {
        appService.updateStatus(saveDTO.getId(), saveDTO.getEnabled());
        return Response.success();
    }

    @DeleteMapping("/delete")
    // @PreAuthorize("@ss.hasPermission('pay:app:delete')")
    public Response deleteApp(@RequestParam("id") Long id) {
        appService.delete(id);
        return Response.success();
    }

    @GetMapping("/get")
    // @PreAuthorize("@ss.hasPermission('pay:app:query')")
    public Response<PayAppDTO> getApp(@RequestParam("id") Long id) {
        PayApp app = appService.getAndValidById(id);
        return Response.success(PayAppConvert.INSTANCE.toDTO(app));
    }

    @GetMapping("/page")
    // @PreAuthorize("@ss.hasPermission('pay:app:query')")
    public Response<IPage<PayAppDTO>> getAppPage(@Valid PayAppDTO dto) {
        // 得到应用分页列表
        IPage<PayApp> page = new Page<>();
        page = appService.listByAdmin(dto, page);
        // 得到所有的应用编号，查出所有的渠道
        factory.build(page.getRecords()).channel();
        return Response.success(page.convert(PayAppConvert.INSTANCE::toDTO));
    }

}
