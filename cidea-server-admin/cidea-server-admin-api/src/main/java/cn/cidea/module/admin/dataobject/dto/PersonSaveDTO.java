package cn.cidea.module.admin.dataobject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
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
public class PersonSaveDTO implements Serializable {
    /**
     *
     */
    private Integer id;
    /**
     *
     */
    @NotBlank
    private String name;
    /**
     * 教育背景，json对象
     */
    private EducationalBackgroundDTO educationalBackground;

    private List<PersonIdentificationDTO> identifications;
}
