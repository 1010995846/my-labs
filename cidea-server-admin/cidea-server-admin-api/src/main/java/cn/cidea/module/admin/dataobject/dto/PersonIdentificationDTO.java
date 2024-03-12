package cn.cidea.module.admin.dataobject.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 个人证件(PersonIdentification)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:09:37
 */
@Data
@NoArgsConstructor
public class PersonIdentificationDTO implements Serializable {
    /**
     *
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 证件号
     */
    @NotBlank
    private String number;
    /**
     * 证件类型：1-身份证
     */
    @NotNull
    private Integer type;
    /**
     * 证件有效期，开始区间
     */
    private Date validityStart;
    /**
     * 证件有效期，结束区间
     */
    private Date validityEnd;
}
