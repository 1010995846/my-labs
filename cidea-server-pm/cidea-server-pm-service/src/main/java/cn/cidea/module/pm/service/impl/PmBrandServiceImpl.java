package cn.cidea.module.pm.service.impl;

import cn.cidea.module.pm.dal.mysql.IPmBrandMapper;
import cn.cidea.module.pm.dataobject.entity.PmBrand;
import cn.cidea.module.pm.dataobject.convert.PmBrandConvert;
import cn.cidea.module.pm.dataobject.dto.PmBrandSaveDTO;
import cn.cidea.module.pm.service.IPmBrandService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 品牌(PmBrand)表服务实现类
 *
 * @author CIdea
 * @since 2023-01-09 16:47:57
 */
@Service
public class PmBrandServiceImpl extends ServiceImpl<IPmBrandMapper, PmBrand> implements IPmBrandService {

    @Override
    public PmBrand save(PmBrandSaveDTO dto) {
        boolean isNew = dto.getId() == null;
        PmBrand entity;
        if (isNew) {
            entity = PmBrandConvert.INSTANCE.toSave(dto);
        } else {
            entity = PmBrandConvert.INSTANCE.toUpdate(dto);
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

