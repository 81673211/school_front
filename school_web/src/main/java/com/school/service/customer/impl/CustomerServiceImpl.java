package com.school.service.customer.impl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.school.cache.RedisKeyNS;
import com.school.dao.customer.CustomerMapper;
import com.school.domain.entity.customer.Customer;
import com.school.service.customer.CustomerService;
import com.school.service.message.SmsService;
import com.school.util.VerifyCodeUtil;
import com.school.web.customer.request.CustomerProfileEditRequest;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * <b>Description:.</b><br> 
 * @author <b>sil.zhou</b>
 * <br><b>ClassName:</b> 
 * <br><b>Date:</b> 12/06/2018 15:50
 */
@Service
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    public static final int EXPIRE_TIME = 5;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void subscribe(String openId) {
        Customer customer = customerMapper.selectByOpenId(openId);
        if (customer != null) {
            if (!customer.isSubscribed()) {
                customer.setSubscribed(true);
                customer.setSubscribedTime(new Date());
                customerMapper.updateByPrimaryKey(customer);
            }
        } else {
            customer = new Customer();
            customer.setOpenId(openId);
            customer.setSubscribed(true);
            customer.setSubscribedTime(new Date());
            customerMapper.insert(customer);
        }
    }

    @Override
    public void unsubscribe(String openId) {
        Customer customer = customerMapper.selectByOpenId(openId);
        if (customer != null && customer.isSubscribed()) {
            customer.setSubscribed(false);
            customerMapper.updateByPrimaryKey(customer);
        }
    }

    @Override
    public Customer getByOpenId(String openId) {
        return customerMapper.selectByOpenId(openId);
    }

    @Override
    public void sendVerifyCode(String phone) {
        String cacheKey = RedisKeyNS.CUSTOMER_PROFILE_VERIFY_CODE + phone;
        String verifyCode = redisTemplate.opsForValue().get(cacheKey);
        if (verifyCode != null) {
            redisTemplate.opsForValue().set(cacheKey, verifyCode, EXPIRE_TIME, TimeUnit.MINUTES);
            smsService.sendVerifyCode(phone, verifyCode);
        } else {
            verifyCode = VerifyCodeUtil.obtain();
            log.info("verify_code:{}", verifyCode);
            redisTemplate.opsForValue().set(cacheKey, verifyCode, EXPIRE_TIME, TimeUnit.MINUTES);
            smsService.sendVerifyCode(phone, verifyCode);
        }
    }

    @Override
    public void update(CustomerProfileEditRequest request) {
        Customer customer = getByOpenId(request.getOpenId());
        String phone = customer.getPhone();
        checkVerifyCode(phone, request.getCode());
        String name = request.getName();
        if (StringUtils.isNotBlank(name)) {
            customer.setName(name);
        }
        String email = request.getEmail();
        if (StringUtils.isNotBlank(email)) {
            customer.setEmail(email);
        }
        String addr = request.getAddr();
        if (StringUtils.isNotBlank(addr)) {
            customer.setAddr(addr);
        }
        if (StringUtils.isNotBlank(phone)) {
            customer.setPhone(phone);
        }
        customerMapper.updateByPrimaryKey(customer);
    }

    private void checkVerifyCode(String phone, String reqVerifyCode) {
        if (StringUtils.isBlank(phone)) {
            if (StringUtils.isBlank(reqVerifyCode)) {
                throw new RuntimeException("验证码不能为空");
            }
            String cacheKey = RedisKeyNS.CUSTOMER_PROFILE_VERIFY_CODE + phone;
            String verifyCode = redisTemplate.opsForValue().get(cacheKey);
            if (verifyCode == null) {
                throw new RuntimeException("无效的验证码");
            }
            if (!reqVerifyCode.equals(verifyCode)) {
                throw new RuntimeException("验证码错误");
            }
        }
    }
}
