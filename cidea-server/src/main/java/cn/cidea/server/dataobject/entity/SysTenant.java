package cn.cidea.server.dataobject.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * 租户表
 * 类似下游的各个使用商(SysTenant)表实体类
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:23
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysTenant extends Model<SysTenant> {

    /**
     * 租户编号
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 租户名
     */
    private String name;

    /**
     * 联系人的用户编号
     */
    private Long contactUserId;

    /**
     * 租户状态，是否禁用
     */
    private Boolean disabled;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

}
