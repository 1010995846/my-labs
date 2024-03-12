package cn.cidea.module.pay.service;

import cn.cidea.module.pay.dal.mysql.IPayOrderExtensionMapper;
import cn.cidea.module.pay.dataobject.convert.PayOrderExtensionConvert;
import cn.cidea.module.pay.dataobject.dto.PayOrderExtensionSaveDTO;
import cn.cidea.module.pay.dataobject.entity.PayOrderExtension;
import cn.cidea.module.pay.service.IPayOrderExtensionService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * order每次发起支付的记录和凭证(PayOrderExtension)表服务实现类
 *
 * @author CIdea
 * @since 2023-11-30 09:53:13
 */
@Service
public class PayOrderExtensionServiceImpl extends ServiceImpl<IPayOrderExtensionMapper, PayOrderExtension> implements IPayOrderExtensionService {

    @Override
    public PayOrderExtension save(PayOrderExtensionSaveDTO dto) {
        boolean isNew = dto.getId() == null;
        PayOrderExtension entity;
        if (isNew) {
            entity = PayOrderExtensionConvert.INSTANCE.toSave(dto);
        } else {
            entity = PayOrderExtensionConvert.INSTANCE.toUpdate(dto);
        }

        LocalDateTime updateTime = LocalDateTime.now();
        entity.setUpdateTime(updateTime);
        if (isNew) {
            entity.setId(IdWorker.getId());
            entity.setCreateTime(updateTime);
            baseMapper.insert(entity);
        } else {
            baseMapper.updateById(entity);
        }
        return entity;
    }
}

