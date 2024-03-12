package cn.cidea.module.pay.service;

import cn.cidea.module.pay.dataobject.dto.PayAppDTO;
import cn.cidea.module.pay.dataobject.dto.PayAppSaveDTO;
import cn.cidea.module.pay.dataobject.entity.PayApp;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付应用 Service 接口
 *
 * @author CIdea
 */
@Transactional(readOnly = true)
public interface IPayAppService {


    PayApp getAndValidById(Long id);

    @Transactional
    PayApp submit(PayAppSaveDTO saveDTO);

    @Transactional
    void updateStatus(Long id, Boolean enabled);

    @Transactional
    void delete(Long id);

    IPage<PayApp> listByAdmin(PayAppDTO dto, IPage<PayApp> page);
}
