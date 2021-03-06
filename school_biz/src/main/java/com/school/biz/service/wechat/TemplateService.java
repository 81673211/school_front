package com.school.biz.service.wechat;

import com.school.biz.domain.bo.wechat.template.Template;
import com.school.biz.domain.entity.express.Express;
import com.school.biz.enumeration.PushMessageEnum;

/**
 *
 * <b>Description:.</b><br> 
 * @author <b>sil.zhou</b>
 * <br><b>ClassName:</b> 
 * <br><b>Date:</b> 07/08/2018 10:16
 */
public interface TemplateService {

    /**
     * <b>Description:消息模板，注意keyword的顺序</b><br>
     * <b>Author:fred</b>
     * <br><b>Date:07/08/2018 14:29.</b>
     * <br><b>BackLog:</b>
     */
    void send(Template template);

    void send(String templateType, String openId, Express express, int expressType);

    void send(PushMessageEnum pushMessageEnum, String createTime, String openId);
}
