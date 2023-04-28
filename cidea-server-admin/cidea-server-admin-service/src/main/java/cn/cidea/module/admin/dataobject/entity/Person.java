package cn.cidea.module.admin.dataobject.entity;

import cn.cidea.framework.mybatisplus.plugin.handlers.FastjsonNullDefaultTypeHandler;
import cn.cidea.module.admin.dataobject.dto.EducationalBackgroundDTO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;
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
    private Long id;

    /**
     *
     */
    private String name;

    private Boolean certified;

    /**
     * 标签，json数组
     */
    @TableField(typeHandler = FastjsonNullDefaultTypeHandler.class)
    private List<Long> tags;

    /**
     * 教育背景，json对象
     */
    @TableField(typeHandler = FastjsonNullDefaultTypeHandler.class)
    private EducationalBackgroundDTO educationalBackground;

    private Date createTime;

    private Date updateTime;

    @TableField(exist = false)
    private List<PersonIdentification> identifications;
}
