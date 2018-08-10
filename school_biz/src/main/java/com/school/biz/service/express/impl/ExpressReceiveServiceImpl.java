package com.school.biz.service.express.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.school.biz.dao.customer.CustomerMapper;
import com.school.biz.dao.express.ExpressCompanyMapper;
import com.school.biz.dao.express.ExpressReceiveMapper;
import com.school.biz.dao.region.RegionMapper;
import com.school.biz.domain.entity.customer.Customer;
import com.school.biz.domain.entity.express.Express;
import com.school.biz.domain.entity.express.ExpressCompany;
import com.school.biz.domain.entity.express.ExpressReceive;
import com.school.biz.domain.entity.region.Region;
import com.school.biz.enumeration.DistributionTypeEnum;
import com.school.biz.enumeration.ReceiveExpressStatusEnum;
import com.school.biz.exception.ExpressException;
import com.school.biz.service.base.impl.BaseServiceImpl;
import com.school.biz.service.express.ExpressReceiveService;
import com.school.biz.service.order.OrderInfoService;
import com.school.biz.util.Log;

/**
 * @author jame
 */
@Service
@Transactional(rollbackFor = ExpressException.class)
public class ExpressReceiveServiceImpl extends BaseServiceImpl<ExpressReceive, ExpressReceiveMapper>
        implements ExpressReceiveService {

    @Autowired
    private ExpressCompanyMapper expressCompanyMapper;
    @Autowired
    private ExpressReceiveMapper expressReceiveMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private RegionMapper regionMapper;
    @Autowired
    private OrderInfoService orderInfoService;

    @Override
    public void createReceiveExpress(ExpressReceive expressReceive) {
        try {
            boxExpressCompany(expressReceive);
            boxCustomer(expressReceive);
            Long count = expressReceiveMapper.insertSelective(expressReceive);
            if (!(count > 0L)) {
                String message =
                        "create receive express error,when insert table 'express_receive' the number of affected rows is 0";
                Log.error.error(message);
                throw new ExpressException(message);
            }
        } catch (Exception e) {
            String message = "throw exception when create receive express";
            Log.error.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    private void boxCustomer(ExpressReceive expressReceive) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", expressReceive.getReceiverPhone());
        List<Customer> list = customerMapper.selectByParams(map);
        expressReceive.setCustomerId(list.get(0).getId());
    }

    @Override
    public void modifyReceiveExpress(ExpressReceive expressReceive) {
        //配送
        if (expressReceive.getExpressWay() == DistributionTypeEnum.DISTRIBUTION.getFlag()) {
            expressReceive.setExpressStatus(ReceiveExpressStatusEnum.WAIT_INTO_BOX.getFlag());
        } else {
            expressReceive.setExpressStatus(ReceiveExpressStatusEnum.WAIT_SELF.getFlag());
        }
        //0自提；1入柜，null表示还未选择过
        Integer expressWay = expressReceiveMapper.selectByPrimaryKey(expressReceive.getId())
                                                 .getExpressWay();
        if ((expressWay == null || expressWay != DistributionTypeEnum.DISTRIBUTION.getFlag())
            && expressReceive.getExpressWay() == 1) {
            //修改后选择入柜方式才生成订单
            orderInfoService.createReceiveOrder(expressReceive.getId());
        }
        if (!(expressReceiveMapper.updateByPrimaryKeySelective(expressReceive) > 0)) {
            String message =
                    "modify receive express error,when update table 'express_receive' the number of affected rows is 0";
            Log.error.error(message);
            throw new RuntimeException(message);
        }
    }

    @Override
    public ExpressReceive getReceiveExpress(Long id) {
        ExpressReceive expressReceive = expressReceiveMapper.selectByPrimaryKey(id);
        if (expressReceive == null) {
            String message =
                    "get receive express error,when select table 'express_receive' the number of affected rows is 0,id="
                    + id;
            Log.error.error(message);
            throw new RuntimeException(message);
        }
//            initProvinceCityDistrict(expressReceive);
        return expressReceive;
    }

    @Override
    public void updateReceiveExpressStatus(Long id, Integer status) {
        //TODO 快件状态检查，检查状态是否遵循定义的流程，如果按照非流程处理则抛出异常,比如：修改快件状态时，需要去提前检查快件当前状态是否满足修改的条件
        ExpressReceive expressReceive = new ExpressReceive();
        expressReceive.setId(id);
        expressReceive.setExpressStatus(status);
        int count = expressReceiveMapper.updateByPrimaryKeySelective(expressReceive);
        if (count <= 0) {
            String msg =
                    "update receive express status failed,when update table 'express_receive' the number of affected rows is 0";
            Log.error.error(msg);
            throw new RuntimeException(msg);
        }
    }

    @Override
    public void updateReceiveExpress(Long id, Integer status, Integer expressWay) {
        ExpressReceive expressReceive = new ExpressReceive();
        expressReceive.setId(id);
        expressReceive.setExpressStatus(status);
        expressReceive.setExpressWay(expressWay);
        int count = expressReceiveMapper.updateByPrimaryKeySelective(expressReceive);
        if (count <= 0) {
            String msg =
                    "update receive express status failed,when update table 'express_receive' the number of affected rows is 0";
            Log.error.error(msg);
            throw new RuntimeException(msg);
        }
    }

    @Override
    public void bindCustomerByPhone(String phone, Long customerId) {
        expressReceiveMapper.bindCustomerByPhone(phone, customerId);
    }

    @Override
    public List<ExpressReceive> selectExpressList(Integer[] status, String phone) {
        Map<String, Object> param = new HashMap<>();
        param.put("status", status);
        param.put("phone", phone);
        List<ExpressReceive> receiveList = expressReceiveMapper.selectByParams(param);
        if (!receiveList.isEmpty()) {
            for (ExpressReceive expressReceive : receiveList) {
//                    initProvinceCityDistrict(expressReceive);
            }
        }
        return receiveList;
    }

    @Override
    public ExpressReceive initProvinceCityDistrict(ExpressReceive expressReceive) {
        Region province = (Region) regionMapper.selectByPrimaryKey(expressReceive.getSenderProvinceId());
        Region city = (Region) regionMapper.selectByPrimaryKey(expressReceive.getSenderCityId());
        Region district = (Region) regionMapper.selectByPrimaryKey(expressReceive.getSenderDistrictId());
        expressReceive.setSenderProvince(province.getAreaName());
        expressReceive.setSenderCity(city.getAreaName());
        expressReceive.setSenderDistrict(district.getAreaName());
        return expressReceive;
    }

    /**
     * 封装快件对应的物流公司
     *
     * @param expressSend
     */
    private void boxExpressCompany(Express expressSend) throws ExpressException {
        Map<String, Object> param = new HashMap<>();
        if (expressSend.getCompanyCode() != null) {
            param.put("companyCode", expressSend.getCompanyCode());
        } else if (expressSend.getCompanyName() != null) {
            param.put("companyName", expressSend.getCompanyName());
        }
        List<ExpressCompany> list = expressCompanyMapper.selectByParams(param);
        if (list.isEmpty()) {
            String errorMsg = "未找到对应的快递公司，快递公司编号:" + expressSend.getCompanyCode() + "，快递公司名称:" + expressSend
                    .getCompanyName();
            Log.error.error(errorMsg);
            throw new ExpressException(errorMsg);
        }
        ExpressCompany expressCompany = list.get(0);
        expressSend.setCompanyId(expressCompany.getId());
        expressSend.setCompanyCode(expressCompany.getCode());
        expressSend.setCompanyName(expressCompany.getName());
    }

    @Override
    public List<ExpressReceive> queryPage(Map<String, Object> paramMap) {
        return expressReceiveMapper.queryForManagerPage(paramMap);
    }

    @Override
    public void saveOrUpdate(ExpressReceive expressReceive) {
        if(expressReceive.getId() == null){
            this.save(expressReceive);
        }else{
            this.update(expressReceive);
        }
    }
}
