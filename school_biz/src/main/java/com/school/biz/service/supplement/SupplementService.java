package com.school.biz.service.supplement;

import java.math.BigDecimal;
import java.util.List;

import com.school.biz.dao.supplement.SupplementMapper;
import com.school.biz.domain.entity.supplement.SupplementInfo;
import com.school.biz.service.base.BaseService;

/**
 * @author jame
 * @date 2018/9/10
 * @desc description
 */
public interface SupplementService extends BaseService<SupplementInfo, SupplementMapper> {
    List<SupplementInfo> selectByIds(List ids);

    void updateIsPay(Long id);

    boolean checkIsPayOff(Long expressId);

    List<SupplementInfo> selectNotPayByExpress(Long expressId, Integer expressType);

    BigDecimal getNotPayAmout(Long expressId, Integer expressType);
}
