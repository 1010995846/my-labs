package cn.cidea.module.pm.service.impl;

import cn.cidea.module.pm.dal.mysql.IPmPropMapper;
import cn.cidea.module.pm.dataobject.dto.PmPropSaveDTO;
import cn.cidea.module.pm.dataobject.entity.PmProp;
import cn.cidea.module.pm.service.IPmPropService;
import cn.cidea.module.pm.dataobject.convert.PmPropConvert;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * (PmProp)表服务实现类
 *
 * @author CIdea
 * @since 2023-01-09 13:58:26
 */
@Service
public class PmPropServiceImpl extends ServiceImpl<IPmPropMapper, PmProp> implements IPmPropService {

    @Override
    public PmProp save(PmPropSaveDTO dto) {
        boolean isNew = dto.getId() == null;
        PmProp entity;
        if(isNew){
            entity = PmPropConvert.INSTANCE.toSave(dto);
        } else {
            entity = PmPropConvert.INSTANCE.toUpdate(dto);
        }

        Date updateTime = new Date();
        entity.setUpdateTime(updateTime);
        if(isNew){
            entity.setId(IdWorker.getIdStr());
            entity.setCreateTime(updateTime);
            baseMapper.insert(entity);
        } else {
            baseMapper.updateById(entity);
        }
        return entity;
    }
}

