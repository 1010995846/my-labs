package cn.cidea.module.pm.dataobject.dto;


import java.io.Serializable;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 产商(PmMfr)表实体类
 *
 * @author CIdea
 * @since 2023-01-09 17:03:16
 */
@Data
public class PmMfrUpdateDTO implements Serializable {


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
     * 排序
     */
    private Integer sort;
    /**
     * 是否禁用
     */
    private Boolean disabled;

}

