package cn.cidea.module.order.service.impl;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.module.order.dal.mysql.IOrderMapper;
import cn.cidea.module.order.dataobject.convert.OrderConvert;
import cn.cidea.module.order.dataobject.dto.OrderSaveDTO;
import cn.cidea.module.order.dataobject.entity.Order;
import cn.cidea.module.order.dataobject.enums.TradeOrderState;
import cn.cidea.module.order.dataobject.enums.OrderStateEvent;
import cn.cidea.module.order.service.IOrderService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: CIdea
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<IOrderMapper, Order> implements IOrderService {

    @Resource
    private OrderStateListener stateListener;

    @Override
    public Order save(OrderSaveDTO saveDTO) {
        Order entity = OrderConvert.INSTANCE.toEntity(saveDTO);
        Date updateTime = new Date();
        entity.setUpdateTime(updateTime);
        if (StringUtils.isBlank(entity.getId())) {
            entity.setId(IdWorker.getIdStr())
                    .setStatus(TradeOrderState.UNPAID);
            entity.setCreateTime(updateTime);
            baseMapper.insert(entity);
        } else {
            baseMapper.updateById(entity);
        }
        return entity;
    }

    @Override
    public void payed(String id) {
        Order order = getAndValidById(id);
        log.info("线程名称：{},尝试支付，订单号：{}", Thread.currentThread().getName(), id);
        if (!stateListener.sendEvent(order, OrderStateEvent.PAYED)) {
            log.error("线程名称：{},支付失败, 状态异常，订单信息：{}", Thread.currentThread().getName(), order);
            throw new RuntimeException("支付失败, 订单状态异常");
        }
    }

    public void deliver(String id) {
        Order order = getAndValidById(id);
        log.info("线程名称：{},尝试发货，订单号：{}", Thread.currentThread().getName(), id);
        if (!stateListener.sendEvent(order, OrderStateEvent.DELIVERY)) {
            log.error("线程名称：{},发货失败, 状态异常，订单信息：{}", Thread.currentThread().getName(), order);
            throw new RuntimeException("发货失败, 订单状态异常");
        }
    }

    public void receive(String id) {
        Order order = getAndValidById(id);
        log.info("线程名称：{},尝试收货，订单号：{}", Thread.currentThread().getName(), id);
        if (!stateListener.sendEvent(order, OrderStateEvent.RECEIVED)) {
            log.error("线程名称：{},收货失败, 状态异常，订单信息：{}", Thread.currentThread().getName(), order);
            throw new RuntimeException("收货失败, 订单状态异常");
        }
    }

    public Order getAndValidById(String id){
        Order order = baseMapper.selectById(id);
        Assert.VALID.nonNull(order, "订单ID非法");
        return order;
    }

}
