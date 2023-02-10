package cn.cidea.module.admin.service.system;


import cn.cidea.module.admin.dataobject.entity.SysMessage;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * (SysMessage)表服务接口
 *
 * @author yechangfei
 * @since 2022-04-27 13:53:04
 */
@Validated
@Transactional(readOnly = true)
public interface ISysMessageService extends IService<SysMessage> {

    void resend(@NotNull Long id, Boolean retry, JSONObject content);

}
