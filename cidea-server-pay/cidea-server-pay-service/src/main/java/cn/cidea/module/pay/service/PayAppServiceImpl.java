package cn.cidea.module.pay.service;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.module.pay.dal.mysql.IPayAppMapper;
import cn.cidea.module.pay.dataobject.convert.PayAppConvert;
import cn.cidea.module.pay.dataobject.dto.PayAppDTO;
import cn.cidea.module.pay.dataobject.dto.PayAppSaveDTO;
import cn.cidea.module.pay.dataobject.entity.PayApp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author: CIdea
 */
@Slf4j
@Service
public class PayAppServiceImpl extends ServiceImpl<IPayAppMapper, PayApp> implements IPayAppService{

    @Override
    public PayApp submit(PayAppSaveDTO saveDTO) {
        PayApp entity = PayAppConvert.INSTANCE.toEntity(saveDTO);
        LocalDateTime now = LocalDateTime.now();
        entity.setUpdateTime(now);
        if(entity.getId() == null){
            entity.setId(IdWorker.getId());
            entity.setCreateTime(now);
            baseMapper.insert(entity);
        } else {
            baseMapper.updateById(entity);
        }
        return entity;
    }

    @Override
    public void updateStatus(Long id, Boolean enabled) {
        baseMapper.updateById(new PayApp().setId(id).setEnabled(enabled));
    }

    @Override
    public PayApp getAndValidById(Long id) {
        return baseMapper.validById(id);
    }

    @Override
    public void delete(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public IPage<PayApp> listByAdmin(PayAppDTO dto, IPage<PayApp> page) {
        page = baseMapper.selectPage(page, new QueryWrapper<>());
        return page;
    }
}
