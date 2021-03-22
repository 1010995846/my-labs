package com.charlotte.core.mq.service;

import com.charlotte.core.mq.dto.SysMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Charlotte
 */
@FeignClient(name = "msg-service", path = "/sys/msg")
public interface IMsgService {

    @PostMapping("/ack")
    void ack(SysMessageDto messageDto);

}
