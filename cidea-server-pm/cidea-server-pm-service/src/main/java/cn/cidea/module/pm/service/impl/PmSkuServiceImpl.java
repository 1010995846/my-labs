package cn.cidea.module.pm.service.impl;

import cn.cidea.module.pm.dal.mysql.IPmSkuMapper;
import cn.cidea.module.pm.dataobject.convert.PmSkuConvert;
import cn.cidea.module.pm.dataobject.dto.PmSkuSaveDTO;
import cn.cidea.module.pm.dataobject.entity.PmSku;
import cn.cidea.module.pm.service.IPmSkuService;
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
public class PmSkuServiceImpl extends ServiceImpl<IPmSkuMapper, PmSku> implements IPmSkuService {

    @Override
    public PmSku save(PmSkuSaveDTO dto) {
        boolean isNew = dto.getId() == null;
        PmSku entity;
        if (isNew) {
            entity = PmSkuConvert.INSTANCE.toSave(dto);
        } else {
            entity = PmSkuConvert.INSTANCE.toUpdate(dto);
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

