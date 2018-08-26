package com.school.biz.service.express;

import com.school.biz.domain.entity.order.OrderInfo;

/**
 * @author jame
 * @date 2018/8/26
 * @desc description
 */
public interface ExpressService {

    /**
     * 支付之后的快件状态以及相关流程运转
     *
     * @param orderInfo
     */
    void updateExpressByPay(OrderInfo orderInfo) throws RuntimeException;


    /**
     * 退款之后的快件状态以及相关流程运转
     *
     * @param orderInfo
     */
    void updateExpressByRefund(OrderInfo orderInfo) throws RuntimeException;
}