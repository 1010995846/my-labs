package cn.cidea.module.pay.service;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.framework.web.core.exception.BizException;
import cn.cidea.module.pay.client.PayClientConfig;
import cn.cidea.module.pay.client.PayClientFactory;
import cn.cidea.module.pay.dal.mysql.IPayChannelMapper;
import cn.cidea.module.pay.dataobject.dto.PayChannelSaveDTO;
import cn.cidea.module.pay.dataobject.convert.PayChannelConvert;
import cn.cidea.module.pay.dataobject.entity.PayChannel;
import cn.cidea.module.pay.dataobject.enums.PayChannelEnum;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 支付渠道 Service 实现类
 *
 * @author aquan
 */
@Service
@Slf4j
@Validated
public class PayChannelServiceImpl extends ServiceImpl<IPayChannelMapper, PayChannel> implements IPayChannelService {

    @Getter // 为了方便测试，这里提供 getter 方法
    @Setter
    private volatile List<PayChannel> cache;
    @Resource
    private PayClientFactory clientFactory;
    @Resource
    private Validator validator;

    @PostConstruct
    public void initLocalCache() {
        initLocalCache(null);
    }
    /**
     * 初始化 {@link #clientFactory} 缓存
     */
    public void initLocalCache(Collection<Long> ids) {
        // 注意：忽略自动多租户，因为要全局初始化缓存
        // 第一步：查询数据
        List<PayChannel> channels;
        if(CollectionUtils.isEmpty(ids)){
            channels = baseMapper.selectList(new QueryWrapper<>());
        } else {
            channels = baseMapper.selectBatchIds(ids);
        }
        log.info("[initLocalCache][缓存支付渠道，数量为:{}]", channels.size());

        // 第二步：构建缓存：创建或更新支付 Client
        channels.forEach(channel -> clientFactory.createOrUpdatePayClient(channel.getId(), channel.getCode(), channel.getConfig()));
        this.cache = channels;
    }

    /**
     * 通过定时任务轮询，刷新缓存
     *
     * 目的：多节点部署时，通过轮询”通知“所有节点，进行刷新
     */
    @Scheduled(initialDelay = 60, fixedRate = 60, timeUnit = TimeUnit.SECONDS)
    public void refreshLocalCache() {
        // 注意：忽略自动多租户，因为要全局初始化缓存
        // 情况一：如果缓存里没有数据，则直接刷新缓存
        if (CollUtil.isEmpty(cache)) {
            initLocalCache();
            return;
        }

        // 情况二，如果缓存里数据，则通过 updateTime 判断是否有数据变更，有变更则刷新缓存
        LocalDateTime maxTime = cache.stream().map(PayChannel::getUpdateTime).max(LocalDateTime::compareTo).get();
        if (baseMapper.selectCountByUpdateTimeGt(maxTime) > 0) {
            initLocalCache();
        }
    }

    @Override
    public PayChannel submit(PayChannelSaveDTO saveDTO) {
        // 断言是否有重复的
        PayChannel db = getByAppIdAndCode(saveDTO.getAppId(), saveDTO.getCode());
        if (db != null && !db.getId().equals(saveDTO.getId())) {
            throw BizException.error(Assert.BIZ, "已存在相同的渠道");
        }

        // 新增渠道
        PayChannel entity = PayChannelConvert.INSTANCE.toEntity(saveDTO)
                .setConfig(parseConfig(saveDTO.getCode(), saveDTO.getConfig()));
        LocalDateTime now = LocalDateTime.now();
        entity.setUpdateTime(now);
        if(entity.getId() == null){
            entity.setId(IdWorker.getId());
            baseMapper.insert(entity);
        } else {
            baseMapper.updateById(entity);
        }

        // 刷新缓存
        initLocalCache(Collections.singleton(entity.getId()));
        return entity;
    }

    /**
     * 解析并校验配置
     *
     * @param code      渠道编码
     * @param configObj 配置
     * @return 支付配置
     */
    private PayClientConfig parseConfig(String code, JSONObject configObj) {
        // 解析配置
        Class<? extends PayClientConfig> payClass = PayChannelEnum.getByCode(code).getConfigClass();
        if (ObjectUtil.isNull(payClass)) {
            throw BizException.error(Assert.BIZ, "支付渠道的配置不存在");
        }
        PayClientConfig config = configObj.toJavaObject(payClass);
        Assert.VALID.nonNull(config, "支付配置");
        // 验证参数
        config.validate(validator);
        return config;
    }

    @Override
    public void delete(Long id) {
        // 校验存在
        validateExists(id);
        // 删除
        baseMapper.deleteById(id);
        // 刷新缓存
        initLocalCache();
    }

    private PayChannel validateExists(Long id) {
        PayChannel channel = baseMapper.selectById(id);
        Assert.VALID.nonNull(channel, "支付渠道的配置不存在");
        return channel;
    }

    @Override
    public PayChannel get(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<PayChannel> listByAppIds(Collection<Long> appIds) {
        return baseMapper.selectListByAppIds(appIds);
    }

    @Override
    public PayChannel getByAppIdAndCode(Long appId, String code) {
        return baseMapper.selectByAppIdAndCode(appId, code);
    }

    @Override
    public PayChannel validAppIdAndCode(Long appId, String code) {
        PayChannel channel = baseMapper.selectByAppIdAndCode(appId, code);
        Assert.VALID.nonNull(channel, "支付渠道的配置不存在");
        Assert.VALID.isTrue(channel.getEnabled(), "支付渠道已经禁用");
        return channel;
    }


    @Override
    public PayChannel valid(Long id) {
        PayChannel channel = baseMapper.selectById(id);
        valid(channel);
        return channel;
    }

    @Override
    public PayChannel valid(Long appId, String code) {
        PayChannel channel = baseMapper.selectByAppIdAndCode(appId, code);
        valid(channel);
        return channel;
    }

    private void valid(PayChannel channel) {
        Assert.VALID.nonNull(channel, "支付渠道的配置不存在");
        Assert.VALID.isTrue(channel.getEnabled(), "支付渠道未启用");
    }

    @Override
    public List<PayChannel> getEnableList(Long appId) {
        return baseMapper.selectListByAppId(appId, true);
    }

}
