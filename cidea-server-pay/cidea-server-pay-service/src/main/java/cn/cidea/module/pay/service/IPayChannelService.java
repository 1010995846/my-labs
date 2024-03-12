package cn.cidea.module.pay.service;

import cn.cidea.module.pay.dataobject.dto.PayChannelSaveDTO;
import cn.cidea.module.pay.dataobject.entity.PayChannel;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 支付渠道 Service 接口
 *
 * @author aquan
 */
public interface IPayChannelService {

    /**
     * 创建支付渠道
     *
     * @param saveDTO 创建信息
     * @return 编号
     */
    PayChannel submit(@Valid PayChannelSaveDTO saveDTO);

    /**
     * 删除支付渠道
     *
     * @param id 编号
     */
    void delete(Long id);

    /**
     * 获得支付渠道
     *
     * @param id 编号
     * @return 支付渠道
     */
    PayChannel get(Long id);

    /**
     * 根据支付应用 ID 集合，获得支付渠道列表
     *
     * @param appIds 应用编号集合
     * @return 支付渠道列表
     */
    List<PayChannel> listByAppIds(Collection<Long> appIds);

    /**
     * 根据条件获取渠道
     *
     * @param appId      应用编号
     * @param code       渠道编码
     * @return 数量
     */
    PayChannel getByAppIdAndCode(Long appId, String code);

    PayChannel validAppIdAndCode(Long appId, String code);

    /**
     * 支付渠道的合法性
     *
     * 如果不合法，抛出 {@link ServiceException} 业务异常
     *
     * @param id 渠道编号
     * @return 渠道信息
     */
    PayChannel valid(Long id);

    /**
     * 支付渠道的合法性
     *
     * 如果不合法，抛出 {@link ServiceException} 业务异常
     *
     * @param appId 应用编号
     * @param code 支付渠道
     * @return 渠道信息
     */
    PayChannel valid(Long appId, String code);

    /**
     * 获得指定应用的开启的渠道列表
     *
     * @param appId 应用编号
     * @return 渠道列表
     */
    List<PayChannel> getEnableList(Long appId);

}
