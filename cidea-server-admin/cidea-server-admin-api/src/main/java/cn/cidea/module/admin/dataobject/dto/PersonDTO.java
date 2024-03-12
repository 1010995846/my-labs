package cn.cidea.module.admin.dataobject.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 个人信息(Person)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:25
 */
@Data
@NoArgsConstructor
public class PersonDTO implements Serializable {
    /**
     *
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     *
     */
    private String name;
    /**
     * 标签，json数组
     */
    private List<Long> tags;
    /**
     * 教育背景，json对象
     */
    private EducationalBackgroundDTO educationalBackground;

    private List<PersonIdentificationDTO> identifications;
}
