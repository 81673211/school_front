package com.school.biz.domain.entity.region;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author jame
 */
@Data
public class Region {
    private Long id;
    private String areaName;
    private Long parentId;

    private BigDecimal sfFirFee;
    private BigDecimal ysFirFee;
    private BigDecimal ztFirFee;
    private BigDecimal ydFirFee;
    private BigDecimal bsFirFee;
    private BigDecimal otherFirFee;
}
