package com.school.web.vo.request;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author jame
 * @date 2018/8/5
 * @desc 寄件计算vo
 */
@Validated
@Data
public class SendExpressCalcVo {
    @NotNull(message = "收件人省份不为空")
    private Long receiverProvinceId;    //收件人省份ID
    @NotNull(message = "收件人城市不为空")
    private Long receiverCityId;//收件人城市ID
    @NotNull(message = "收件人区县不为空")
    private Long receiverDistrictId;        //收件人区县ID
    private Long companyId;    //快递公司
    @Min(value = 1, message = "填写的重量最小为1KG")
    private BigDecimal expressWeight;
}
