package cn.cidea.framework.chain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Charlotte
 */
@Data
@Accessors(chain = true)
public class SysLog {

    /**
     * 从简自增
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Boolean success;

    private String target;

    private String method;

    private String args;

    private String msg;

    private Date createTime;
}
