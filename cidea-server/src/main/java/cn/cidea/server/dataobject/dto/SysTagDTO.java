package cn.cidea.server.dataobject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (SysTag)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:27
 */
@Data
@NoArgsConstructor
public class SysTagDTO {
    /**
     *
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
}
