package cn.cidea.module.pm.dataobject.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * (PmProp)表实体类
 *
 * @author CIdea
 * @since 2023-01-09 17:03:17
 */
@Data
public class PmPropUpdateDTO implements Serializable {


    private String id;
    /**
     * 名称
     */
    @NotBlank
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 是否禁用
     */
    private Boolean disabled;

}

