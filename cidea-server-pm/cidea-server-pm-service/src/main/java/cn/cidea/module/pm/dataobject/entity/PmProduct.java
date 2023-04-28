package cn.cidea.module.pm.dataobject.entity;


import cn.cidea.module.pm.constant.FieldAnalyzer;
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
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 产品(PmProduct)表实体类
 *
 * @author CIdea
 * @since 2023-01-09 17:03:16
 */
@Data
@TableName(autoResultMap = true)
@Document(indexName = "product", // 索引名
        shards = 1, // 默认索引分区数
        replicas = 0, // 每个分区的备份数
        refreshInterval = "-1" // 刷新间隔
)
public class PmProduct extends Model<PmProduct> implements Serializable {

    @Id
    @TableId
    private Long id;

    /**
     * 编码
     */
    @Field(analyzer = FieldAnalyzer.IK_SMART, type = FieldType.Text)
    private String code;
    /**
     * 产品名
     */
    @Field(analyzer = FieldAnalyzer.IK_SMART, type = FieldType.Text)
    private String name;
    /**
     * 通用名
     */
    private String generalName;
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
    @Field(analyzer = FieldAnalyzer.IK_MAX_WORD, type = FieldType.Object)
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
    private List<PmSku> skuList;

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}

