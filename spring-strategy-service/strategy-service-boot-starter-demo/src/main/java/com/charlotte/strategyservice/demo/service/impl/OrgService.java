package com.charlotte.strategyservice.demo.service.impl;

import com.charlotte.strategyservice.annotation.StrategyMaster;
import com.charlotte.strategyservice.demo.service.IOrgService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Charlotte
 */
@StrategyMaster
@Transactional(readOnly = false)
public class OrgService implements IOrgService {

    @Override
    public String getName(String id) {
        return "defaultOrg";
    }
}
