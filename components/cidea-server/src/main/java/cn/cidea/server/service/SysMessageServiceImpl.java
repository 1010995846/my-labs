package cn.cidea.server.service;

import cn.cidea.server.dal.mysql.ISysMessageMapper;
import cn.cidea.server.dataobject.entity.SysMessage;
import cn.cidea.server.service.ISysMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * (SysMessage)表服务实现类
 *
 * @author yechangfei
 * @since 2022-04-27 13:53:04
 */
@Slf4j
@Service
public class SysMessageServiceImpl extends ServiceImpl<ISysMessageMapper, SysMessage> implements ISysMessageService {

}
