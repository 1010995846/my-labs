package cn.cidea.framework.web.core.service;


import cn.cidea.framework.web.core.dto.ApiErrorLogCreateReqDTO;

import javax.validation.Valid;

/**
 * API 错误日志 Framework Service 接口
 */
public interface ApiErrorLogFrameworkService {

    /**
     * 创建 API 错误日志
     *
     * @param createDTO 创建信息
     */
    void createApiErrorLogAsync(@Valid ApiErrorLogCreateReqDTO createDTO);

}
