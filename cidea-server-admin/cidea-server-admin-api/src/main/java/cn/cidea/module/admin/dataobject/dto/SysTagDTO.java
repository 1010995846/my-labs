package cn.cidea.module.admin.dataobject.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (SysTag)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:27
 */
@Data
@NoArgsConstructor
public class SysTagDTO implements Serializable {
    /**
     *
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 名称
     */
    private String name;
}
