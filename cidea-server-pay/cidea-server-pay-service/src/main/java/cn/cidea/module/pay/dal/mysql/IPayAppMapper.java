package cn.cidea.module.pay.dal.mysql;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.module.pay.dataobject.entity.PayApp;
import cn.cidea.module.pay.dataobject.entity.PayChannel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Mapper
public interface IPayAppMapper extends BaseMapper<PayApp> {

    default PayApp validById(Long id){
        PayApp app = selectById(id);
        Assert.VALID.nonNull(app, "支付应用ID非法");
        return app;
    }
}
