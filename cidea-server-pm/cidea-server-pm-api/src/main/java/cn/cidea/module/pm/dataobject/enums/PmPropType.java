package cn.cidea.module.pm.dataobject.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author: CIdea
 */
@Getter
@AllArgsConstructor
public enum PmPropType implements Serializable {

    TEXT,
    OPT,
    ;

}
