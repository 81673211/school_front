package com.school.web.controller.wechat;

import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.school.biz.service.wechat.WxPayService;

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
	@RequestMapping(value = "/pay")
	public ModelAndView wxpay(String orderNo) {
		try {
			ModelAndView mv = new ModelAndView();
			// 统一下单
			if (StringUtils.isBlank(orderNo)) {
				throw new RuntimeException("订单号异常");
			}
			log.info("WxPayController wxpay统一下单，orderNo=" + orderNo);
			TreeMap<String, String> resultMap = wxPayService.doUnifiedOrder(orderNo);
			// 避免关键字package，将package换成pkg
			resultMap.put("pkg", resultMap.get("package"));
			resultMap.remove("package");
			mv.setViewName("wxpay/wxpay");
			mv.addObject("resultMap",resultMap);
			return mv;
		} catch (Exception e) {
			log.error("订单号:{}, 支付失败，原因:{}", orderNo, e.getMessage());
			throw new RuntimeException(e.getMessage());
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
			String inputLine;
			StringBuilder notifyXmlSb = new StringBuilder();
			try {
				while ((inputLine = request.getReader().readLine()) != null) {
					notifyXmlSb.append(inputLine);
				}
				request.getReader().close();
			} catch (Exception e) {
				log.debug("xml获取失败：" + e);
				throw new RuntimeException(e.getMessage());
			}
			String resXml = wxPayService.wxPayNotify(notifyXmlSb.toString());
			//向微信输出处理结果，如果成功（SUCCESS），微信就不会继续调用了，否则微信会连续调用8次
            log.info("resXml:{}", resXml);
			return "SUCCESS";
		} catch (Exception e) {
			log.error("微信支付通知失败:" + e.getMessage());
			return null;
		}
	}

}
