package cn.cidea.module.pm.dataobject.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * (PmProp)表实体类
 *
 * @author CIdea
 * @since 2023-01-09 17:03:17
 */
@Data
public class PmProp extends Model<PmProp> implements Serializable {


    @TableId
    private String id;

    /**
     * 名称
     */
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
    /**
     * 是否删除
     */
    private Boolean deleted;
    /**
     * 删除时间
     */
    private Date deleteTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}

