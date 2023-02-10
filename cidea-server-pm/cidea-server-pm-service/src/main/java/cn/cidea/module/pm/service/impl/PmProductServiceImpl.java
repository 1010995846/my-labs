package cn.cidea.module.pm.service.impl;

import cn.cidea.module.pm.dataobject.convert.PmProductConvert;
import cn.cidea.module.pm.dataobject.dto.PmProductSaveDTO;
import cn.cidea.module.pm.dataobject.entity.PmProduct;
import cn.cidea.module.pm.service.IPmProductService;
import cn.cidea.module.pm.dal.mysql.IPmProductMapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 产品(PmProduct)表服务实现类
 *
 * @author CIdea
 * @since 2023-01-09 16:48:13
 */
@Service
public class PmProductServiceImpl extends ServiceImpl<IPmProductMapper, PmProduct> implements IPmProductService {

    @Override
    public PmProduct save(PmProductSaveDTO dto) {
        boolean isNew = dto.getId() == null;
        PmProduct entity;
        if (isNew) {
            entity = PmProductConvert.INSTANCE.toSave(dto);
        } else {
            entity = PmProductConvert.INSTANCE.toUpdate(dto);
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

