package com.school.service.user.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.constant.Constants;
import com.school.dao.user.AdminUserMapper;
import com.school.domain.entity.user.AdminUser;
import com.school.domain.vo.user.AdminUserVo;
import com.school.service.base.impl.BaseServiceImpl;
import com.school.service.user.AdminUserService;
import com.school.util.core.utils.DigestUtil;
import com.school.util.core.utils.MD5Util;

@Service
public class AdminUserServiceImpl extends BaseServiceImpl<AdminUser, AdminUserMapper> implements AdminUserService {

	@Autowired
	private AdminUserMapper adminUserMapper;
	
	@Override
	public AdminUser getLoginUserByLoginNameAndLoginPassword(String userName,
			String password) {
		 // md5加密登录密码
        String md5Password = DigestUtil.md5Hex(password);
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("userName", userName);
        map.put("password", md5Password);
        
        return adminUserMapper.getLoginUserByLoginNameAndLoginPassword(map);
	}

	@Override
	public List<AdminUserVo> queryPage(Map<String, Object> paramMap) {
		return adminUserMapper.queryPage(paramMap);
	}

	@Override
	public void saveOrUpdate(AdminUser adminUser) {
		if(adminUser.getId() == null){
			// 默认密码：123456
			adminUser.setPassword(MD5Util.getMD5(Constants.ADMIN_USER_DEFAULT_PASSWORD));
			this.save(adminUser);
		}else{
			this.update(adminUser);
		}
	}

	@Override
	public AdminUserVo detail(Long id) {
		return adminUserMapper.detail(id);
	}

	@Override
	public boolean checkIsNotExist(Long userId,String loginName) {
		if(userId != null){
			// 如果是修改，但是没修改登录账号，则直接返回false
			AdminUser user = adminUserMapper.selectByPrimaryKey(userId);
			if(user != null && loginName.equals(user.getLoginName())){
				return true;
			}
		}
		return adminUserMapper.checkIsNotExist(loginName);
	}

	@Override
	public AdminUser findByLoginName(String loginName) {
		return adminUserMapper.findByLoginName(loginName);
	}

}
