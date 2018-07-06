package com.school.web.wechat;

import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.school.service.wechat.WxPayService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/wxpay")
public class WxPayController {

	@Autowired
	private WxPayService wxPayService;
	
	/**
	 * 微信支付
	 */
	@RequestMapping("/pay")
	public ModelAndView wxpay(String orderNo) throws Exception{
		try {
			if(StringUtils.isBlank(orderNo)){
				throw new Exception("订单号不能为空");
			}
			
			ModelAndView mv = new ModelAndView();
			// 统一下单
			TreeMap<String, String> resultMap = wxPayService.doUnifiedOrder(orderNo);
			// 避免关键字package，将package换成pkg
			resultMap.put("pkg", resultMap.get("package"));
			resultMap.remove("package");
			mv.setViewName("wxpay/wxpay");
			mv.addObject("resultMap",resultMap);
			return mv;
		} catch (Exception e) {
			log.error("订单"+orderNo+"支付失败，原因：" + e.getMessage());
			throw new Exception("订单"+orderNo+"支付失败，原因：" + e.getMessage());
		}
	}
	
	/**
	 * 支付通知
	 */
	@ResponseBody
	@RequestMapping("/notify")
	public String wxNotify(HttpServletRequest request){
	    try {
	    	log.info("===============>>>>微信支付回调获取数据开始");  
			String resXml = wxPayService.wxPayNotify(request);
			//向微信输出处理结果，如果成功（SUCCESS），微信就不会继续调用了，否则微信会连续调用8次
			return "SUCCESS";
		} catch (Exception e) {
			log.error("微信支付通知失败:" + e.getMessage());
			return null;
		}
	}
}