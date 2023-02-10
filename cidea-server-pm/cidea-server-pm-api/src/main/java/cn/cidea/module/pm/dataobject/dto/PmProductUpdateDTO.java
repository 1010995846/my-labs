package cn.cidea.module.pm.dataobject.dto;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 产品(PmProduct)表实体类
 *
 * @author CIdea
 * @since 2023-01-09 17:03:16
 */
@Data
public class PmProductUpdateDTO implements Serializable {


    private Long id;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    @NotBlank
    private String name;
    /**
     * 描述
     */
    private String intro;
    /**
     * 品牌ID
     */
    private Long brandId;
    /**
     * 厂商ID
     */
    private Long mfrId;
    /**
     * 图片
     */
    private List<String> picUrls;
    /**
     * 属性值
     */
    private Map<String, PmProductValDTO> properties;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 是否禁用
     */
    private Boolean disabled;

}

