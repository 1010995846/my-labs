package cn.cidea.module.pm.dataobject.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: CIdea
 */
@Data
public class PmProductValDTO implements Serializable {

    private PmPropDTO prop;

    private String val;

}
