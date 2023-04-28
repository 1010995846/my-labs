package cn.cidea.module.pm.dataobject.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * (PmProductSku)表实体类
 *
 * @author CIdea
 * @since 2023-01-09 17:03:16
 */
@Data
public class PmSku extends Model<PmSku> implements Serializable {


    @TableId
    private Long id;

    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 售卖规格
     */
    private Map<String, String> sellSpec;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 条形码
     */
    private String bar;
    /**
     * 图片
     */
    private List<String> picUrls;
    /**
     * 租户ID
     */
    private Long tenantId;
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

