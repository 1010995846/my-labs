package cn.cidea.lab.utils.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Charlotte
 */
@Data
@Accessors(chain = true)
public class UserDTO {

    private String id;

    private String name;

    private String deptId;

    private String deptName;

    private String deleted;
}
