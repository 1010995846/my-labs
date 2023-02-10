package cn.cidea.module.pm.service.impl;

import cn.cidea.module.pm.dal.mysql.IPmCatMapper;
import cn.cidea.module.pm.dataobject.dto.PmCatSaveDTO;
import cn.cidea.module.pm.dataobject.entity.PmCat;
import cn.cidea.module.pm.service.IPmCatService;
import cn.cidea.module.pm.dataobject.convert.PmCatConvert;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 品类(PmCat)表服务实现类
 *
 * @author CIdea
 * @since 2023-01-09 16:48:01
 */
@Service
public class PmCatServiceImpl extends ServiceImpl<IPmCatMapper, PmCat> implements IPmCatService {

    @Override
    public PmCat save(PmCatSaveDTO dto) {
        boolean isNew = dto.getId() == null;
        PmCat entity;
        if (isNew) {
            entity = PmCatConvert.INSTANCE.toSave(dto);
        } else {
            entity = PmCatConvert.INSTANCE.toUpdate(dto);
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

