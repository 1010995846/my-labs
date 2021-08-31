package com.charlotte.strategyservice.demo.service;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Charlotte
 */
@Validated
public interface IOrgService {

    String getName(@NotBlank String id);

}
