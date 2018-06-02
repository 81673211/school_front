package com.school.service.security.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.dao.security.RoleMapper;
import com.school.domain.entity.security.Role;
import com.school.domain.entity.user.AdminUser;
import com.school.domain.vo.security.MenuJson;
import com.school.service.base.impl.BaseServiceImpl;
import com.school.service.security.RoleService;
import com.school.util.core.utils.StrUtil;

/** 
 * @author  作者：方梁
 * @date 创建时间：2016年10月26日 下午1:41:45
 * @description   
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, RoleMapper> implements RoleService {
	@Autowired
	private RoleMapper roleMapper;
	
	@Override
	public List<Role> queryPage(Map<String, Object> map) {
		return roleMapper.queryPage(map);
	}

	@Override
	public Role saveOrUpdate(Role role) {
		if(StrUtil.isBlank(role.getId())){
			this.save(role);
		}else{
			Role r = this.get(role.getId());
			this.update(role);
		}
		return role;
	}

	@Override
	public List<Role> getAll() {
		return roleMapper.getAll();
	}

	@Override
	public List<MenuJson> getZNodes(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", id);
		return roleMapper.getZNodes(map);
	}

	@Override
	public void saveRoleResource(Long roleId, String resIds) {
		//先删除该role原有的资源
		roleMapper.delResourceByRoleId(roleId);
		//再存储新的资源
		String[] ids = resIds.split(",");
		List<String> list = Arrays.asList(ids);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		map.put("list", list);
		roleMapper.saveRoleResourceOnce(map);
	}

	@Override
	public boolean checkIsNotExist(Long roleId, String roleName) {
		if(roleId != null){
			// 如果是修改，但是没修改登录账号，则直接返回false
			Role role = roleMapper.selectByPrimaryKey(roleId);
			if(role != null && roleName.equals(role.getRoleName())){
				return true;
			}
		}
		return roleMapper.checkIsNotExist(roleName);
	}

}
