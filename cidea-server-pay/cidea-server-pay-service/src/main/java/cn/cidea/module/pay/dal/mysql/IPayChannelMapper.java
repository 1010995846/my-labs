package cn.cidea.module.pay.dal.mysql;

import cn.cidea.module.pay.dataobject.entity.PayChannel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Mapper
public interface IPayChannelMapper extends BaseMapper<PayChannel> {

    default PayChannel selectByAppIdAndCode(Long appId, String code) {
        return selectOne(new QueryWrapper<PayChannel>().lambda().eq(PayChannel::getAppId, appId).eq(PayChannel::getCode, code));
    }

    default List<PayChannel> selectListByAppIds(Collection<Long> appIds){
        return selectList(new QueryWrapper<PayChannel>().lambda().in(PayChannel::getAppId, appIds));
    }

    default List<PayChannel> selectListByAppId(Long appId, Boolean enabled) {
        return selectList(new QueryWrapper<PayChannel>().lambda()
                .eq(PayChannel::getAppId, appId)
                .eq(PayChannel::getEnabled, enabled));
    }

    @Select("SELECT COUNT(*) FROM pay_channel WHERE update_time > #{maxUpdateTime}")
    long selectCountByUpdateTimeGt(LocalDateTime maxUpdateTime);

}
