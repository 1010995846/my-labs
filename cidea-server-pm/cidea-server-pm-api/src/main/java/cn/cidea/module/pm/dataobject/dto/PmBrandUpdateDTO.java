package cn.cidea.module.pm.dataobject.dto;


import java.io.Serializable;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 品牌(PmBrand)表实体类
 *
 * @author CIdea
 * @since 2023-01-09 17:03:15
 */
@Data
public class PmBrandUpdateDTO implements Serializable {


    private Long id;
    /**
     * 名称
     */
    @NotBlank
    private String name;
    /**
     * 图片
     */
    private String picUrl;
    /**
     * 描述
     */
    private String intro;
    /**
     * 是否禁用
     */
    private Boolean disabled;
    /**
     * 排序
     */
    private Integer sort;

}

