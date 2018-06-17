package com.school.web.customer;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.school.util.wechat.WechatMessageUtil;
import com.school.web.base.BaseEasyWebController;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * <b>Description:.</b><br> 
 * @author <b>sil.zhou</b>
 * <br><b>ClassName:</b> 
 * <br><b>Date:</b> 11/06/2018 17:22
 */
@Controller
@RequestMapping("/customer")
@Slf4j
public class CustomerController extends BaseEasyWebController {

    @RequestMapping("/profile/edit")
    public void edit(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("-------------/customer/profile/edit---------");
        String code = request.getParameter("code");
        log.info("-------------code---------:{}", code);
        response.sendRedirect("/index.html");
    }


}