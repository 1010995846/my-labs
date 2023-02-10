package cn.cidea.module.pm.service.impl;

import cn.cidea.module.pm.dal.mysql.IPmMfrMapper;
import cn.cidea.module.pm.dataobject.dto.PmMfrSaveDTO;
import cn.cidea.module.pm.dataobject.entity.PmMfr;
import cn.cidea.module.pm.service.IPmMfrService;
import cn.cidea.module.pm.dataobject.convert.PmMfrConvert;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 产商(PmMfr)表服务实现类
 *
 * @author CIdea
 * @since 2023-01-09 16:48:06
 */
@Service
public class PmMfrServiceImpl extends ServiceImpl<IPmMfrMapper, PmMfr> implements IPmMfrService {

    @Override
    public PmMfr save(PmMfrSaveDTO dto) {
        boolean isNew = dto.getId() == null;
        PmMfr entity;
        if (isNew) {
            entity = PmMfrConvert.INSTANCE.toSave(dto);
        } else {
            entity = PmMfrConvert.INSTANCE.toUpdate(dto);
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

