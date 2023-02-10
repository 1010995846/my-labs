package cn.cidea.module.pm.dataobject.entity;


import cn.cidea.module.pm.dataobject.dto.PmProductValDTO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;

/**
 * 产品(PmProduct)表实体类
 *
 * @author CIdea
 * @since 2023-01-09 17:03:16
 */
@Data
@TableName(autoResultMap = true)
public class PmProduct extends Model<PmProduct> implements Serializable {


    @TableId
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
    @TableField(exist = false)
    private PmBrand brand;
    /**
     * 厂商ID
     */
    private Long mfrId;
    @TableField(exist = false)
    private PmMfr mfr;
    /**
     * 图片
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> picUrls;
    /**
     * 属性值
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private Map<String, String> propVal;
    @TableField(exist = false)
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


    @TableField(exist = false)
    private List<PmProductSku> skuList;

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}

