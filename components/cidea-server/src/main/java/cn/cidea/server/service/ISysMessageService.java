package cn.cidea.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import cn.cidea.server.dataobject.entity.SysMessage;
import org.springframework.validation.annotation.Validated;

/**
 * (SysMessage)表服务接口
 *
 * @author yechangfei
 * @since 2022-04-27 13:53:04
 */
@Validated
@Transactional(readOnly = true)
public interface ISysMessageService extends IService<SysMessage> {

}
