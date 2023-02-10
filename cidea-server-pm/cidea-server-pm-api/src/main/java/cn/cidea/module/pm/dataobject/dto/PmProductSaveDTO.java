package cn.cidea.module.pm.dataobject.dto;


import lombok.Data;

/**
 * 产品(PmProduct)表实体类
 *
 * @author CIdea
 * @since 2023-01-09 17:03:16
 */
@Data
public class PmProductSaveDTO extends PmProductUpdateDTO {

    /**
     * 租户ID
     */
    private Long tenantId;

}

