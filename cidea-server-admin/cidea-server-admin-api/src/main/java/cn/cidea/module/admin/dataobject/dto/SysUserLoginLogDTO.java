package cn.cidea.module.admin.dataobject.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录记录(SysUserLoginLog)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:29
 */
@Data
@NoArgsConstructor
public class SysUserLoginLogDTO implements Serializable {
    /**
     *
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * IP
     */
    private Integer ip;
    /**
     * 时间
     */
    private Date time;
    /**
     * 是否成功
     */
    private Integer successful;
    /**
     * 备注
     */
    private String remark;
}
