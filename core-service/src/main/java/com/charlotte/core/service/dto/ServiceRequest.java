package com.charlotte.core.service.dto;

import lombok.Data;

/**
 * @author Charlotte
 */
@Data
public class ServiceRequest {

    private String reqId;

    private Object param;

    public ServiceRequest sub(){
        ServiceRequest request = new ServiceRequest();
        request.setReqId(this.reqId);
        return request;
    }

}
