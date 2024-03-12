package cn.cidea.module.admin.dataobject.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author: CIdea
 */
@Getter
@AllArgsConstructor
public enum IdentificationType implements Serializable {

    身份证(1),
    ;

    private Integer value;

}
