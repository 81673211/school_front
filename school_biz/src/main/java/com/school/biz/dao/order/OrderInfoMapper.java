package com.school.biz.dao.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.school.biz.dao.base.BaseDao;
import com.school.biz.domain.entity.order.OrderInfo;

public interface OrderInfoMapper extends BaseDao {
    int deleteByPrimaryKey(Long id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);
    
    List<OrderInfo> queryPage(Map<String, Object> paramMap);
    
    OrderInfo findByOrderNo(String orderNo);

    OrderInfo findByExpressIdAndType(@Param("expressId") Long expressId, @Param("expressType") int expressType);

	List<OrderInfo> getNotPayOrder(Map<String, Object> map);
}