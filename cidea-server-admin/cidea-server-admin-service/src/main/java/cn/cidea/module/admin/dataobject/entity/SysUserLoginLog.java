package cn.cidea.module.admin.dataobject.entity;

import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * 用户登录记录(SysUserLoginLog)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysUserLoginLog extends Model<SysUserLoginLog> {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * {@link LoginTypeEnum}
     */
    private Integer type;

    private String username;

    private String userAgent;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * IP
     */
    private Integer ip;
    /**
     * 时间
     */
    private Date time;
    /**
     * 登录结果
     * {@link LoginResultEnum}
     */
    private Integer result;
    /**
     * 备注
     */
    private String remark;
}
