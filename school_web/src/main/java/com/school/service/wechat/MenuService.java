package com.school.service.wechat;

import com.school.util.wechat.button.Menu;

/**
 *
 * <b>Description:.</b><br> 
 * @author <b>sil.zhou</b>
 * <br><b>ClassName:</b> 
 * <br><b>Date:</b> 14/06/2018 12:45
 */
public interface MenuService {

    void create(Menu menu);

    Menu get();
}
