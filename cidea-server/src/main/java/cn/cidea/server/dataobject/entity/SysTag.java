package cn.cidea.server.dataobject.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * (SysTag)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysTag extends Model<SysTag> {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    /**
     * 名称
     */
    private String name;
}
