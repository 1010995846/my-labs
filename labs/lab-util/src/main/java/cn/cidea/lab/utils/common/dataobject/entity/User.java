package cn.cidea.lab.utils.common.dataobject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Charlotte
 */
@Data
@Accessors(chain = true)
public class User {

    private String id;

    private String name;

    private String deptId;

    private Boolean deleted;
}
