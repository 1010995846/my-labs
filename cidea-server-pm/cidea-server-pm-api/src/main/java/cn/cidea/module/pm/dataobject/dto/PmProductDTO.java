package cn.cidea.module.pm.dataobject.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 产品(PmProduct)表实体类
 *
 * @author CIdea
 * @since 2023-01-09 17:03:16
 */
@Data
public class PmProductDTO implements Serializable {

    private Long id;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 描述
     */
    private String intro;
    /**
     * 品牌ID
     */
    private Long brandId;
    private PmBrandDTO brand;
    /**
     * 厂商ID
     */
    private Long mfrId;
    private PmMfrDTO mfr;
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
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    private List<PmSkuDTO> skuList;

}

