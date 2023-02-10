package cn.cidea.module.admin.dataobject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 个人信息(Person)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:25
 */
@Data
@NoArgsConstructor
public class PersonDTO {
    /**
     *
     */
    private Integer id;
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
}
