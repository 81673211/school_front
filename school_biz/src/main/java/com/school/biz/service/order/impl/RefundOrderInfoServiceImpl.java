package com.school.biz.service.order.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.school.biz.dao.order.RefundOrderInfoMapper;
import com.school.biz.domain.entity.order.RefundOrderInfo;
import com.school.biz.enumeration.RefundOrderStatusEnum;
import com.school.biz.service.base.impl.BaseServiceImpl;
import com.school.biz.service.order.RefundOrderInfoService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class RefundOrderInfoServiceImpl extends BaseServiceImpl<RefundOrderInfo, RefundOrderInfoMapper> implements RefundOrderInfoService {
	
	@Autowired
	private RefundOrderInfoMapper refundOrderInfoMapper;
	
	@Override
	public void refundOrderUpdateToSuccess(RefundOrderInfo refundOrderInfo) {
		if (refundOrderInfo == null) {
            return;
        }
        refundOrderInfo.setStatus(RefundOrderStatusEnum.SUCCESS.getCode());
        refundOrderInfo.setSucTime(new Date());
        refundOrderInfoMapper.updateByPrimaryKeySelective(refundOrderInfo);
	}

	@Override
	public void refundOrderUpdateToRefunding(RefundOrderInfo refundOrderInfo) {
		if (refundOrderInfo == null) {
            return;
        }
        refundOrderInfo.setStatus(RefundOrderStatusEnum.REFUNDING.getCode());
        refundOrderInfo.setSucTime(new Date());
        refundOrderInfoMapper.updateByPrimaryKeySelective(refundOrderInfo);
	}

	@Override
	public void refundOrderUpdateToFailed(RefundOrderInfo refundOrderInfo) {
		if (refundOrderInfo == null) {
            return;
        }
        refundOrderInfo.setStatus(RefundOrderStatusEnum.FAILED.getCode());
        refundOrderInfo.setSucTime(new Date());
        refundOrderInfoMapper.updateByPrimaryKeySelective(refundOrderInfo);
	}

	@Override
	public List<RefundOrderInfo> getNotRefundOrder() {
		return refundOrderInfoMapper.getNotRefundOrder();
	}

}