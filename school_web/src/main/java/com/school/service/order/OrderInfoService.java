package com.school.service.order;

import com.school.dao.order.OrderInfoMapper;
import com.school.domain.entity.order.OrderInfo;
import com.school.exception.OrderException;
import com.school.service.base.BaseService;
import com.school.vo.request.OrderCreateVo;

public interface OrderInfoService extends BaseService<OrderInfo, OrderInfoMapper> {

	OrderInfo findByOrderNo(String orderNo);

	void createSendOrder(OrderCreateVo vo) throws OrderException;

	void createReceiveOrder(OrderCreateVo vo) throws OrderException;
}