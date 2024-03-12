package cn.cidea.module.pay.client.impl.mock;


import cn.cidea.module.pay.client.impl.AbstractPayClient;
import cn.cidea.module.pay.dataobject.dto.PayOrderRespDTO;
import cn.cidea.module.pay.dataobject.dto.PayOrderUnifiedReqDTO;
import cn.cidea.module.pay.dataobject.dto.PayRefundRespDTO;
import cn.cidea.module.pay.dataobject.dto.PayRefundUnifiedReqDTO;
import cn.cidea.module.pay.dataobject.enums.PayChannelEnum;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 模拟支付的 PayClient 实现类
 *
 * 模拟支付返回结果都是成功，方便大家日常流畅
 *
 * @author jason
 */
public class MockPayClient extends AbstractPayClient<MockPayClientConfig> {

    private static final String MOCK_RESP_SUCCESS_DATA = "MOCK_SUCCESS";

    public MockPayClient(Long channelId, MockPayClientConfig config) {
        super(channelId, PayChannelEnum.MOCK.getCode(), config);
    }

    @Override
    protected void doInit() {
    }

    @Override
    protected PayOrderRespDTO doUnifiedOrder(PayOrderUnifiedReqDTO reqDTO) {
        return PayOrderRespDTO.successOf("MOCK-P-" + reqDTO.getOutTradeNo(), "", LocalDateTime.now(),
                reqDTO.getOutTradeNo(), MOCK_RESP_SUCCESS_DATA);
    }

    @Override
    protected PayOrderRespDTO doGetOrder(String outTradeNo) {
        return PayOrderRespDTO.successOf("MOCK-P-" + outTradeNo, "", LocalDateTime.now(),
                outTradeNo, MOCK_RESP_SUCCESS_DATA);
    }

    @Override
    protected PayRefundRespDTO doUnifiedRefund(PayRefundUnifiedReqDTO reqDTO) {
        return PayRefundRespDTO.successOf("MOCK-R-" + reqDTO.getOutRefundNo(), LocalDateTime.now(),
                reqDTO.getOutRefundNo(), MOCK_RESP_SUCCESS_DATA);
    }

    @Override
    protected PayRefundRespDTO doGetRefund(String outTradeNo, String outRefundNo) {
        return PayRefundRespDTO.successOf("MOCK-R-" + outRefundNo, LocalDateTime.now(),
                outRefundNo, MOCK_RESP_SUCCESS_DATA);
    }

    @Override
    protected PayRefundRespDTO doParseRefundNotify(Map<String, String> params, String body) {
        throw new UnsupportedOperationException("模拟支付无退款回调");
    }

    @Override
    protected PayOrderRespDTO doParseOrderNotify(Map<String, String> params, String body) {
        throw new UnsupportedOperationException("模拟支付无支付回调");
    }

}
