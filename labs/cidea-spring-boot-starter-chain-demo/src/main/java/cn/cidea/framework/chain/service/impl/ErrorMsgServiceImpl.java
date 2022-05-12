package cn.cidea.framework.chain.service.impl;

import cn.cidea.framework.chain.service.IMsgChainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Charlotte
 */
@Slf4j
@Service
public class ErrorMsgServiceImpl implements IMsgChainService {

    @Override
    public void alter() {
        throw new RuntimeException("推送异常");
    }
}
