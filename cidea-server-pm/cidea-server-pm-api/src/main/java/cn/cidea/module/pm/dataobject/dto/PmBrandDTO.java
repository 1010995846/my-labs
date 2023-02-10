package cn.cidea.module.pm.dataobject.dto;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 品牌(PmBrand)表实体类
 *
 * @author CIdea
 * @since 2023-01-09 17:03:14
 */
@Data
public class PmBrandDTO implements Serializable {


    private Long id;
    /**
     * 名称
     */
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
     * 首字母
     */
    private String firstLetter;
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

}

