package com.school.service.express.impl;

import com.school.dao.customer.CustomerMapper;
import com.school.dao.express.ExpressCompanyMapper;
import com.school.dao.express.ExpressReceiveMapper;
import com.school.dao.region.RegionMapper;
import com.school.domain.entity.customer.Customer;
import com.school.domain.entity.express.Express;
import com.school.domain.entity.express.ExpressCompany;
import com.school.domain.entity.express.ExpressReceive;
import com.school.domain.entity.region.Region;
import com.school.enumeration.DistributionTypeEnum;
import com.school.enumeration.ExpressTypeEnum;
import com.school.enumeration.ReceiveExpressStatusEnum;
import com.school.exception.ExpressException;
import com.school.exception.ExpressStatusException;
import com.school.service.base.impl.BaseServiceImpl;
import com.school.service.calc.CalcCostService;
import com.school.service.express.ExpressReceiveService;
import com.school.service.order.OrderInfoService;
import com.school.util.core.Log;
import com.school.vo.BaseVo;
import com.school.vo.request.OrderCreateVo;
import com.school.vo.request.ReceiveExpressCreateVo;
import com.school.vo.request.ReceiveExpressModifyVo;
import com.school.vo.response.ReceiveExpressListResponseVo;
import com.school.vo.response.ReceiveExpressResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jame
 */
@Service
@Transactional(rollbackFor = ExpressException.class)
public class ExpressReceiveServiceImpl extends BaseServiceImpl<ExpressReceive, ExpressReceiveMapper> implements ExpressReceiveService {

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
    @Autowired
    private CalcCostService calcCostService;

    @Override
    public void createReceiveExpress(ReceiveExpressCreateVo expressVo) throws ExpressException {
        try {
            ExpressReceive expressReceive = converterVo2Po(expressVo, ExpressReceive.class);
            boxExpressCompany(expressReceive);
            boxCustomer(expressReceive);
            Long count = expressReceiveMapper.insertSelective(expressReceive);
            if (!(count > 0L)) {
                String message = "create receive express error,when insert table 'express_receive' the number of affected rows is 0";
                Log.error.error(message);
                throw new ExpressException(message);
            }
        } catch (Exception e) {
            String message = "throw exception when create receive express";
            Log.error.error(message, e);
            throw new ExpressException(message, e);
        }
    }


    private void boxCustomer(ExpressReceive expressReceive) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", expressReceive.getReceiverPhone());
        List<Customer> list = customerMapper.selectByParams(map);
        expressReceive.setCustomerId(list.get(0).getId());
    }

    @Override
    public void modifyReceiveExpress(ReceiveExpressModifyVo expressVo) throws ExpressException {
        try {
            ExpressReceive expressReceive = converterVo2Po(expressVo, ExpressReceive.class);
            //配送
            if (expressReceive.getExpressWay() == DistributionTypeEnum.DISTRIBUTION.getFlag()) {
                expressReceive.setExpressStatus(ReceiveExpressStatusEnum.WAIT_INTO_BOX.getFlag());
            } else {
                expressReceive.setExpressStatus(ReceiveExpressStatusEnum.WAIT_SELF.getFlag());
            }
            //0自提；1入柜，null表示还未选择过
            Integer expressWay = expressReceiveMapper.selectByPrimaryKey(expressReceive.getId()).getExpressWay();
            if ((expressWay == null || expressWay != DistributionTypeEnum.DISTRIBUTION.getFlag()) && expressReceive.getExpressWay() == 1) {
                //修改后选择入柜方式才生成订单
                OrderCreateVo vo = new OrderCreateVo();
                vo.setExpressId(expressReceive.getId());
                orderInfoService.createReceiveOrder(vo);
            }
            if (!(expressReceiveMapper.updateByPrimaryKeySelective(expressReceive) > 0)) {
                String message = "modify receive express error,when update table 'express_receive' the number of affected rows is 0";
                Log.error.error(message);
                throw new ExpressException(message);
            }
        } catch (Exception e) {
            String message = "throw exception when modify receive express";
            Log.error.error(message, e);
            throw new ExpressException(message, e);
        }
    }


    @Override
    public BaseVo getReceiveExpress(Long id) throws ExpressException {
        try {
            ExpressReceive expressReceive = expressReceiveMapper.selectByPrimaryKey(id);
            if (expressReceive == null) {
                String message = "get receive express error,when select table 'express_receive' the number of affected rows is 0,id=" + id;
                Log.error.error(message);
                throw new ExpressException(message);
            }
            initProvinceCityDistrict(expressReceive);
            return converterPo2Vo(expressReceive, new ReceiveExpressResponseVo());
        } catch (Exception e) {
            String message = "throw exception when get receive express";
            Log.error.error(message, e);
            throw new ExpressException(message, e);
        }
    }


    @Override
    public void updateReceiveExpressStatus(Long id, Integer status) throws ExpressException {
        try {
            expressStatusCheck(id, ExpressTypeEnum.RECEIVE.getFlag(), true, status);
        } catch (ExpressStatusException e) {
            String msg = "update receive express status error,express status check failed";
            Log.error.error(msg, e);
            throw new ExpressException(msg, e);
        }
        ExpressReceive expressReceive = new ExpressReceive();
        expressReceive.setId(id);
        expressReceive.setExpressStatus(status);
        int count = expressReceiveMapper.updateByPrimaryKeySelective(expressReceive);
        if (!(count > 0)) {
            String msg = "update receive express status failed,when update table 'express_receive' the number of affected rows is 0";
            Log.error.error(msg);
            throw new ExpressException(msg);
        }
    }


    @Override
    public List<BaseVo> selectExpressList(Integer[] status, String phone) throws ExpressException {
        List<BaseVo> list = new ArrayList<>();
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("status", status);
            param.put("phone", phone);
            List<ExpressReceive> receiveList = expressReceiveMapper.selectByParams(param);
            if (!receiveList.isEmpty()) {
                for (ExpressReceive expressReceive : receiveList) {
//                    initProvinceCityDistrict(expressReceive);
                    ReceiveExpressListResponseVo vo = converterPo2Vo(expressReceive, new ReceiveExpressListResponseVo());
                    vo.setDistributionCost(calcCostService.calcReceiveDistributionCost(expressReceive));
                    list.add(vo);
                }
            }
        } catch (Exception e) {
            String msg = "throw exception when get receive express list";
            Log.error.error(msg, e);
            throw new ExpressException(e);
        }
        return list;
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
            String errorMsg = "未找到对应的快递公司，快递公司编号:" + expressSend.getCompanyCode() + "，快递公司名称:" + expressSend.getCompanyName();
            Log.error.error(errorMsg);
            throw new ExpressException(errorMsg);
        }
        ExpressCompany expressCompany = list.get(0);
        expressSend.setCompanyId(expressCompany.getId());
        expressSend.setCompanyCode(expressCompany.getCode());
        expressSend.setCompanyName(expressCompany.getName());
    }

}
