package cn.cidea.module.pm.dataobject.dto;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 品类(PmCat)表实体类
 *
 * @author CIdea
 * @since 2023-01-09 17:03:15
 */
@Data
public class PmCatDTO implements Serializable {


    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 上级
     */
    private Long parentId;
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
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}

