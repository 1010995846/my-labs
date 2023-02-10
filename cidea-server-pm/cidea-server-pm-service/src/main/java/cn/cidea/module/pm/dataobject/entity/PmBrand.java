package cn.cidea.module.pm.dataobject.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

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
public class PmBrand extends Model<PmBrand> implements Serializable {


    @TableId
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
     * 是否删除
     */
    private Boolean deleted;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 删除时间
     */
    private Date deleteTime;

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}

