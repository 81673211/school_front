package com.school.biz.domain.vo.express;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class ExpressSendVo {
	private Long id;
    private String code;
    private Long companyId;
    private String companyCode;
    private String companyName;
    private Date createdTime;
    private Boolean isDeleted;
    private Integer expressStatus;
    private Integer expressWay;//快件方式：入柜/自己
    private Long customerId;
    private Date endTime;
    private BigDecimal serviceAmt;
    private Integer expressType;
	private String senderPhone;
    private String senderName;
    private String senderAddr;
    private String receiverPhone;
    private String receiverName;
    private String receiverAddr;
    private Long receiverProvinceId;
    private Long receiverCityId;
    private Long receiverDistrictId;

    @JSONField(serialize = false)
    private String receiverProvince;
    @JSONField(serialize = false)
    private String receiverCity;
    @JSONField(serialize = false)
    private String receiverDistrict;
    @JSONField(serialize = false)
    private String idCard;
    
    private BigDecimal totalAmt;
    
    private BigDecimal totalRefundAmt;
}
