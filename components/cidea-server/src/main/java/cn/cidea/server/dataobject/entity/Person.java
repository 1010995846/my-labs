package cn.cidea.server.dataobject.entity;

import cn.cidea.server.mybatis.handlers.FastjsonNullDefaultTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.List;

/**
 * 个人信息(Person)表实体类
 *
 * @author yechangfei
 * @since 2022-04-14 13:41:18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
public class Person extends Model<Person> {

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     *
     */
    private String name;

    /**
     * 标签，json数组
     */
    @TableField(typeHandler = FastjsonNullDefaultTypeHandler.class)
    private List<Long> tags;

    /**
     * 教育背景，json对象
     */
    private EducationalBackground educationalBackground;

    @Data
    public static class EducationalBackground implements Serializable {

        private String university;

    }

}
