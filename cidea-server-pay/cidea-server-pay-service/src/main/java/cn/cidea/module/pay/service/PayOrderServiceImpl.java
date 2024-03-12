package cn.cidea.module.pay.service;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.framework.web.core.exception.BizException;
import cn.cidea.framework.web.core.utils.ServletUtils;
import cn.cidea.module.pay.client.PayClient;
import cn.cidea.module.pay.client.PayClientFactory;
import cn.cidea.module.pay.config.PayProperties;
import cn.cidea.module.pay.dal.mysql.IPayOrderExtensionMapper;
import cn.cidea.module.pay.dal.mysql.IPayOrderMapper;
import cn.cidea.module.pay.dal.redis.PayNoRedisDAO;
import cn.cidea.module.pay.dataobject.convert.PayOrderConvert;
import cn.cidea.module.pay.dataobject.convert.PayOrderExtensionConvert;
import cn.cidea.module.pay.dataobject.dto.*;
import cn.cidea.module.pay.dataobject.entity.PayApp;
import cn.cidea.module.pay.dataobject.entity.PayChannel;
import cn.cidea.module.pay.dataobject.entity.PayOrder;
import cn.cidea.module.pay.dataobject.entity.PayOrderExtension;
import cn.cidea.module.pay.dataobject.enums.PayNotifyTypeEnum;
import cn.cidea.module.pay.dataobject.enums.PayOrderStatusEnum;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 支付订单(PayOrder)表服务实现类
 *
 * @author CIdea
 * @since 2023-11-30 09:53:09
 */
@Slf4j
@Service
public class PayOrderServiceImpl extends ServiceImpl<IPayOrderMapper, PayOrder> implements IPayOrderService {

    @Resource
    private IPayAppService appService;
    @Resource
    private IPayChannelService channelService;
    @Resource
    private IPayOrderExtensionMapper orderExtensionMapper;
    @Resource
    private PayClientFactory payClientFactory;
    @Resource
    private PayNoRedisDAO noRedisDAO;
    @Resource
    private PayProperties payProperties;

    @Override
    public PayOrder create(PayOrderSaveDTO dto) {
        // 校验 App
        PayApp app = appService.getAndValidById(dto.getAppId());

        // 查询对应的支付交易单是否已经存在。如果是，则直接返回
        PayOrder order = baseMapper.selectByAppIdAndMerchantOrderId(dto.getAppId(), dto.getMerchantOrderId());
        if (order != null) {
            // 理论来说，不会出现这个情况
            log.warn("[createOrder][appId({}) merchantOrderId({}) 已经存在对应的支付单({})]", order.getAppId(), order.getMerchantOrderId(), JSONObject.toJSONString(order));
            return order;
        }

        // 创建支付交易单
        LocalDateTime now = LocalDateTime.now();
        order = PayOrderConvert.INSTANCE.toSave(dto);
        order.setStatus(PayOrderStatusEnum.WAITING);
        order.setExpireTime(now.plusMinutes(10));
        // 商户相关字段
        // order.setNotifyUrl(app.getOrderNotifyUrl());
        baseMapper.insert(order);
        return order;
    }

    @Override
    public PayOrder submit(@Valid PayOrderUpdateDTO dto) {
        Long id = dto.getId();
        PayOrder order = baseMapper.validById(id);
        if (PayOrderStatusEnum.isSuccess(order.getStatus())) {
            // 校验状态，发现已支付
            throw new BizException("订单已支付，请刷新页面");
        }
        if (!PayOrderStatusEnum.WAITING.equals(order.getStatus())) {
            // 校验状态，必须是待支付
            throw new BizException("支付订单不处于待支付");
        }
        if (order.getExpireTime().isAfter(LocalDateTime.now())) {
            // 校验是否过期
            throw new BizException("支付订单已经过期");
        }

        List<PayOrderExtension> extensionList = orderExtensionMapper.selectListByOrderId(id);
        extensionList.forEach(extension -> {
            // 情况一：校验数据库中的 orderExtension 是不是已支付
            if (PayOrderStatusEnum.isSuccess(extension.getStatus())) {
                log.warn("[validateOrderCanSubmit][order({}) 的 extension({}) 已支付，可能是数据不一致]", id, extension.getId());
                throw new BizException("订单已支付，请等待支付结果");
            }
            // 情况二：调用三方接口，查询支付单状态，是不是已支付
            PayClient payClient = payClientFactory.getPayClient(extension.getChannelId());
            if (payClient == null) {
                log.error("[validateOrderCanSubmit][渠道编号({}) 找不到对应的支付客户端]", extension.getChannelId());
                return;
            }
            PayOrderRespDTO respDTO = payClient.getOrder(extension.getNo());
            if (respDTO != null && PayOrderStatusEnum.isSuccess(respDTO.getStatus())) {
                log.warn("[validateOrderCanSubmit][order({}) 的 PayOrderRespDTO({}) 已支付，可能是回调延迟]",
                        id, JSONObject.toJSONString(respDTO));
                throw new BizException("订单已支付，请等待支付结果");
            }
        });

        // 1.2 校验支付渠道是否有效
        PayChannel channel = validateChannelCanSubmit(order.getAppId(), dto.getChannelCode());
        PayClient client = payClientFactory.getPayClient(channel.getId());

        // 2. 插入 PayOrderExtensionDO
        String no = noRedisDAO.generate(payProperties.getOrderNoPrefix());
        PayOrderExtension orderExtension = PayOrderExtensionConvert.INSTANCE.toEntity(dto)
                // .setUserIp(ServletUtils.getClientIP())
                .setOrderId(order.getId())
                .setNo(no)
                .setChannelId(channel.getId())
                // .setChannelCode(channel.getCode())
                .setStatus(PayOrderStatusEnum.WAITING);
        orderExtensionMapper.insert(orderExtension);

        // 3. 调用三方接口
        PayOrderUnifiedReqDTO unifiedOrderReqDTO = PayOrderConvert.INSTANCE.convert2(dto)
                .setUserIp(ServletUtils.getClientIP())
                // 商户相关的字段
                .setOutTradeNo(orderExtension.getNo()) // 注意，此处使用的是 PayOrderExtensionDO.no 属性！
                .setSubject(order.getSubject()).setBody(order.getBody())
                .setNotifyUrl(payProperties.getOrderNotifyUrl() + "/" + channel.getId())
                .setReturnUrl(dto.getReturnUrl())
                // 订单相关字段
                .setAmt(order.getAmt()).setExpireTime(order.getExpireTime());
        PayOrderRespDTO unifiedOrderResp = client.unifiedOrder(unifiedOrderReqDTO);

        // 4. 如果调用直接支付成功，则直接更新支付单状态为成功。例如说：付款码支付，免密支付时，就直接验证支付成功
        if (unifiedOrderResp != null) {
            SpringUtil.getBean(getClass()).notifyOrder(channel, unifiedOrderResp);
            // 如有渠道错误码，则抛出业务异常，提示用户
            if (StrUtil.isNotEmpty(unifiedOrderResp.getChannelErrorCode())) {
                throw new BizException("发起支付报错，错误码：{}，错误提示：{}");
            }
            // 此处需要读取最新的状态
            order = baseMapper.selectById(order.getId());
        }
        return order;
    }


    /**
     * 通知并更新订单的支付结果
     *
     * @param channel 支付渠道
     * @param notify 通知
     */
    @Transactional(rollbackFor = Exception.class) // 注意，如果是方法内调用该方法，需要通过 getSelf().notifyPayOrder(channel, notify) 调用，否则事务不生效
    public void notifyOrder(PayChannel channel, PayOrderRespDTO notify) {
        // 情况一：支付成功的回调
        if (PayOrderStatusEnum.isSuccess(notify.getStatus())) {
            notifyOrderSuccess(channel, notify);
            return;
        }
        // 情况二：支付失败的回调
        if (PayOrderStatusEnum.isClosed(notify.getStatus())) {
            notifyOrderClosed(channel, notify);
        }
        // 情况三：WAITING：无需处理
        // 情况四：REFUND：通过退款回调处理
    }
    private void notifyOrderSuccess(PayChannel channel, PayOrderRespDTO notify) {
        // 1. 更新 PayOrderExtensionDO 支付成功
        PayOrderExtension orderExtension = updateOrderSuccess(notify);
        // 2. 更新 PayOrderDO 支付成功
        Boolean paid = updateOrderSuccess(channel, orderExtension, notify);
        if (paid) { // 如果之前已经成功回调，则直接返回，不用重复记录支付通知记录；例如说：支付平台重复回调
            return;
        }

        // 3. 插入支付通知记录
        notifyService.createPayNotifyTask(PayNotifyTypeEnum.ORDER.getType(),
                orderExtension.getOrderId());
    }

    /**
     * 更新 PayOrderDO 支付成功
     *
     * @param channel 支付渠道
     * @param orderExtension 支付拓展单
     * @param notify 通知回调
     * @return 是否之前已经成功回调
     */
    private Boolean updateOrderSuccess(PayChannel channel, PayOrderExtension orderExtension,
                                       PayOrderRespDTO notify) {
        // 1. 判断 PayOrderDO 是否处于待支付
        PayOrder order = baseMapper.selectById(orderExtension.getOrderId());
        Assert.VALID.nonNull(order, "ORDER_NOT_FOUND");
        if (PayOrderStatusEnum.isSuccess(order.getStatus()) // 如果已经是成功，直接返回，不用重复更新
                && Objects.equals(order.getExtensionId(), orderExtension.getId())) {
            log.info("[updateOrderExtensionSuccess][order({}) 已经是已支付，无需更新]", order.getId());
            return true;
        }
        if (!PayOrderStatusEnum.WAITING.equals(order.getStatus())) {
            // 校验状态，必须是待支付
            throw new BizException("ORDER_STATUS_IS_NOT_WAITING");
        }

        // 2. 更新 PayOrderDO
        int updateCounts = baseMapper.updateByIdAndStatus(order.getId(), PayOrderStatusEnum.WAITING.getStatus(),
                PayOrderDO.builder().status(PayOrderStatusEnum.SUCCESS.getStatus())
                        .channelId(channel.getId()).channelCode(channel.getCode())
                        .successTime(notify.getSuccessTime()).extensionId(orderExtension.getId()).no(orderExtension.getNo())
                        .channelOrderNo(notify.getChannelOrderNo()).channelUserId(notify.getChannelUserId())
                        .channelFeeRate(channel.getFeeRate()).channelFeePrice(MoneyUtils.calculateRatePrice(order.getAmt(), channel.getFeeRate()))
                        .build());
        if (updateCounts == 0) { // 校验状态，必须是待支付
            throw new BizException("ORDER_STATUS_IS_NOT_WAITING");
        }
        log.info("[updateOrderExtensionSuccess][order({}) 更新为已支付]", order.getId());
        return false;
    }
    /**
     * 更新 PayOrderExtensionDO 支付成功
     *
     * @param notify 通知
     * @return PayOrderExtensionDO 对象
     */
    private PayOrderExtension updateOrderSuccess(PayOrderRespDTO notify) {
        // 1. 查询 PayOrderExtensionDO
        PayOrderExtension orderExtension = orderExtensionMapper.selectByNo(notify.getOutTradeNo());
        Assert.VALID.nonNull(orderExtension, "ORDER_EXTENSION_NOT_FOUND");
        if (PayOrderStatusEnum.isSuccess(orderExtension.getStatus())) { // 如果已经是成功，直接返回，不用重复更新
            log.info("[updateOrderExtensionSuccess][orderExtension({}) 已经是已支付，无需更新]", orderExtension.getId());
            return orderExtension;
        }
        if (ObjectUtil.notEqual(orderExtension.getStatus(), PayOrderStatusEnum.WAITING)) { // 校验状态，必须是待支付
            throw new BizException("ORDER_EXTENSION_STATUS_IS_NOT_WAITING");
        }

        // 2. 更新 PayOrderExtensionDO
        int updateCounts = orderExtensionMapper.updateByIdAndStatus(orderExtension.getId(), orderExtension.getStatus(),
                new PayOrderExtension().setStatus(PayOrderStatusEnum.SUCCESS).setChannelNotifyData(JSONObject.toJSONString(notify)));
        if (updateCounts == 0) { // 校验状态，必须是待支付
            throw new BizException("ORDER_EXTENSION_STATUS_IS_NOT_WAITING");
        }
        log.info("[updateOrderExtensionSuccess][orderExtension({}) 更新为已支付]", orderExtension.getId());
        return orderExtension;
    }

    private void notifyOrderClosed(PayChannel channel, PayOrderRespDTO notify) {
        updateOrderExtensionClosed(channel, notify);
    }

    private void updateOrderExtensionClosed(PayChannel channel, PayOrderRespDTO notify) {
        // 1. 查询 PayOrderExtensionDO
        PayOrderExtension orderExtension = orderExtensionMapper.selectByNo(notify.getOutTradeNo());
        if (orderExtension == null) {
            throw new BizException("ORDER_EXTENSION_NOT_FOUND");
        }
        if (PayOrderStatusEnum.isClosed(orderExtension.getStatus())) { // 如果已经是关闭，直接返回，不用重复更新
            log.info("[updateOrderExtensionClosed][orderExtension({}) 已经是支付关闭，无需更新]", orderExtension.getId());
            return;
        }
        // 一般出现先是支付成功，然后支付关闭，都是全部退款导致关闭的场景。这个情况，我们不更新支付拓展单，只通过退款流程，更新支付单
        if (PayOrderStatusEnum.isSuccess(orderExtension.getStatus())) {
            log.info("[updateOrderExtensionClosed][orderExtension({}) 是已支付，无需更新为支付关闭]", orderExtension.getId());
            return;
        }
        if (ObjectUtil.notEqual(orderExtension.getStatus(), PayOrderStatusEnum.WAITING)) { // 校验状态，必须是待支付
            throw new BizException("ORDER_EXTENSION_STATUS_IS_NOT_WAITING");
        }

        // 2. 更新 PayOrderExtensionDO
        int updateCounts = orderExtensionMapper.updateByIdAndStatus(orderExtension.getId(), orderExtension.getStatus(),
                PayOrderExtensionDO.builder().status(PayOrderStatusEnum.CLOSED.getStatus()).channelNotifyData(toJsonString(notify))
                        .channelErrorCode(notify.getChannelErrorCode()).channelErrorMsg(notify.getChannelErrorMsg()).build());
        if (updateCounts == 0) { // 校验状态，必须是待支付
            throw new BizException("ORDER_EXTENSION_STATUS_IS_NOT_WAITING");
        }
        log.info("[updateOrderExtensionClosed][orderExtension({}) 更新为支付关闭]", orderExtension.getId());
    }



    private PayChannel validateChannelCanSubmit(Long appId, String channelCode) {
        // 校验 App
        PayApp app = appService.getAndValidById(appId);
        // 校验支付渠道是否有效
        PayChannel channel = channelService.validAppIdAndCode(appId, channelCode);
        PayClient client = payClientFactory.getPayClient(channel.getId());
        if (client == null) {
            log.error("[validatePayChannelCanSubmit][渠道编号({}) 找不到对应的支付客户端]", channel.getId());
            throw new BizException(Assert.VALID.getCode(), "支付渠道的配置不存在");
        }
        return channel;
    }


    @Override
    public void cancel(Long id) {

    }

    @Override
    public IPage<PayOrder> listByAdmin(PayOrderDTO dto, IPage<PayOrder> page) {
        return baseMapper.selectPage(page, new QueryWrapper<>());
    }
}

