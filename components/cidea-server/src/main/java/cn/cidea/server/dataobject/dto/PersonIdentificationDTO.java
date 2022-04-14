package cn.cidea.server.dataobject.dto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 个人证件(PersonIdentification)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:09:37
 */
@Data
@NoArgsConstructor
public class PersonIdentificationDTO {
    /**
     *
     */
    private Long id;
    /**
     * 个人信息ID
     */
    private Long personId;
    /**
     * 证件号
     */
    private String number;
    /**
     * 证件类型：1-身份证
     */
    private Integer type;
    /**
     * 证件有效期，开始区间
     */
    private Date startTime;
    /**
     * 证件有效期，结束区间
     */
    private Date endTime;
}
