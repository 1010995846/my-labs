package cn.cidea.module.pm.service.impl;

import cn.cidea.module.pm.dal.mysql.IPmProductSkuMapper;
import cn.cidea.module.pm.dataobject.convert.PmProductSkuConvert;
import cn.cidea.module.pm.dataobject.dto.PmProductSkuSaveDTO;
import cn.cidea.module.pm.dataobject.entity.PmProductSku;
import cn.cidea.module.pm.service.IPmProductSkuService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * (PmProductSku)表服务实现类
 *
 * @author CIdea
 * @since 2023-01-09 16:48:18
 */
@Service
public class PmProductSkuServiceImpl extends ServiceImpl<IPmProductSkuMapper, PmProductSku> implements IPmProductSkuService {

    @Override
    public PmProductSku save(PmProductSkuSaveDTO dto) {
        boolean isNew = dto.getId() == null;
        PmProductSku entity;
        if (isNew) {
            entity = PmProductSkuConvert.INSTANCE.toSave(dto);
        } else {
            entity = PmProductSkuConvert.INSTANCE.toUpdate(dto);
        }

        Date updateTime = new Date();
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

