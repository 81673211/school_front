/** 1.快递管理 */
/** 收件列表 */
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (1, '收件列表', '/express/expressReceive/list.do', 0,(select mi.id from menu_info mi where mi.resource_name = '收件列表'),NULL);

insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (2, '收件详情', '/express/expressReceive/detail.do', (select ri.id from resource_info ri where ri.res_url='/express/expressReceive/list.do'),(select mi.id from menu_info mi where mi.resource_name = '收件列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (3, '收件编辑', '/express/expressReceive/edit.do', (select ri.id from resource_info ri where ri.res_url='/express/expressReceive/list.do'),(select mi.id from menu_info mi where mi.resource_name = '收件列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (4, '收件保存', '/express/expressReceive/save.do', (select ri.id from resource_info ri where ri.res_url='/express/expressReceive/list.do'),(select mi.id from menu_info mi where mi.resource_name = '收件列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (5, '收件删除', '/express/expressReceive/del.do', (select ri.id from resource_info ri where ri.res_url='/express/expressReceive/list.do'),(select mi.id from menu_info mi where mi.resource_name = '收件列表'),NULL);

/** 寄件列表 */
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (31, '寄件列表', '/express/expressSend/list.do', 0,(select mi.id from menu_info mi where mi.resource_name = '寄件列表'),NULL);

insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (32, '寄件详情', '/express/expressSend/detail.do', (select ri.id from resource_info ri where ri.res_url='/express/expressSend/list.do'),(select mi.id from menu_info mi where mi.resource_name = '寄件列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (33, '寄件编辑', '/express/expressSend/edit.do', (select ri.id from resource_info ri where ri.res_url='/express/expressSend/list.do'),(select mi.id from menu_info mi where mi.resource_name = '寄件列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (34, '寄件保存', '/express/expressSend/save.do', (select ri.id from resource_info ri where ri.res_url='/express/expressSend/list.do'),(select mi.id from menu_info mi where mi.resource_name = '寄件列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (35, '寄件删除', '/express/expressSend/del.do', (select ri.id from resource_info ri where ri.res_url='/express/expressSend/list.do'),(select mi.id from menu_info mi where mi.resource_name = '寄件列表'),NULL);


/** 2.订单管理 */
/** 订单列表 */
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (100, '订单列表', '/order/list.do', 0,(select mi.id from menu_info mi where mi.resource_name = '订单管理'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (101, '订单详情', '/order/detail.do', (select ri.id from resource_info ri where ri.res_url='/order/list.do'),(select mi.id from menu_info mi where mi.resource_name = '订单管理'),NULL);


/** 3.快递公司管理 */
/** 快递公司列表 */
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (201, '快递公司列表', '/expressCompany/list.do', 0,(select mi.id from menu_info mi where mi.resource_name = '快递公司管理'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (202, '快递公司详情', '/expressCompany/detail.do', (select ri.id from resource_info ri where ri.res_url='/expressCompany/list.do'),(select mi.id from menu_info mi where mi.resource_name = '快递公司管理'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (203, '快递公司编辑', '/expressCompany/edit.do', (select ri.id from resource_info ri where ri.res_url='/expressCompany/list.do'),(select mi.id from menu_info mi where mi.resource_name = '快递公司管理'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (204, '快递公司保存', '/expressCompany/save.do', (select ri.id from resource_info ri where ri.res_url='/expressCompany/list.do'),(select mi.id from menu_info mi where mi.resource_name = '快递公司管理'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (205, '快递公司删除', '/expressCompany/del.do', (select ri.id from resource_info ri where ri.res_url='/expressCompany/list.do'),(select mi.id from menu_info mi where mi.resource_name = '快递公司管理'),NULL);

/** 4.客户管理 */
/** 客户列表 */
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (301, '客户列表', '/customer/list.do', 0,(select mi.id from menu_info mi where mi.resource_name = '客户管理'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (302, '客户详情', '/customer/detail.do', (select ri.id from resource_info ri where ri.res_url='/customer/list.do'),(select mi.id from menu_info mi where mi.resource_name = '客户管理'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (303, '客户编辑', '/customer/edit.do', (select ri.id from resource_info ri where ri.res_url='/customer/list.do'),(select mi.id from menu_info mi where mi.resource_name = '客户管理'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (304, '客户保存', '/customer/save.do', (select ri.id from resource_info ri where ri.res_url='/customer/list.do'),(select mi.id from menu_info mi where mi.resource_name = '客户管理'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (305, '客户删除', '/customer/del.do', (select ri.id from resource_info ri where ri.res_url='/customer/list.do'),(select mi.id from menu_info mi where mi.resource_name = '客户管理'),NULL);


/** 8.权限管理 */
/** 用户列表 */
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (420, '用户列表', '/permission/adminUser/list.do', 0,(select mi.id from menu_info mi where mi.resource_name = '用户列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (421, '用户详情', '/permission/adminUser/detail.do', (select ri.id from resource_info ri where ri.res_url='/permission/adminUser/list.do'),(select mi.id from menu_info mi where mi.resource_name = '用户列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (422, '用户编辑', '/permission/adminUser/edit.do', (select ri.id from resource_info ri where ri.res_url='/permission/adminUser/list.do'),(select mi.id from menu_info mi where mi.resource_name = '用户列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (423, '用户保存', '/permission/adminUser/save.do', (select ri.id from resource_info ri where ri.res_url='/permission/adminUser/list.do'),(select mi.id from menu_info mi where mi.resource_name = '用户列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (424, '用户删除', '/permission/adminUser/del.do', (select ri.id from resource_info ri where ri.res_url='/permission/adminUser/list.do'),(select mi.id from menu_info mi where mi.resource_name = '用户列表'),NULL);

/** 角色列表 */
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (440, '角色列表', '/permission/role/list.do', 0,(select mi.id from menu_info mi where mi.resource_name = '角色列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (441, '角色详情', '/permission/role/detail.do', (select ri.id from resource_info ri where ri.res_url='/permission/role/list.do'),(select mi.id from menu_info mi where mi.resource_name = '角色列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (442, '角色编辑', '/permission/role/edit.do', (select ri.id from resource_info ri where ri.res_url='/permission/role/list.do'),(select mi.id from menu_info mi where mi.resource_name = '角色列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (443, '角色保存', '/permission/role/save.do', (select ri.id from resource_info ri where ri.res_url='/permission/role/list.do'),(select mi.id from menu_info mi where mi.resource_name = '角色列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (444, '角色删除', '/permission/role/del.do', (select ri.id from resource_info ri where ri.res_url='/permission/role/list.do'),(select mi.id from menu_info mi where mi.resource_name = '角色列表'),NULL);

/** 权限列表 */
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (460, '权限列表', '/permission/resourceInfo/list.do', 0,(select mi.id from menu_info mi where mi.resource_name = '权限列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (461, '权限详情', '/permission/resourceInfo/detail.do', (select ri.id from resource_info ri where ri.res_url='/permission/resourceInfo/list.do'),(select mi.id from menu_info mi where mi.resource_name = '权限列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (462, '权限编辑', '/permission/resourceInfo/edit.do', (select ri.id from resource_info ri where ri.res_url='/permission/resourceInfo/list.do'),(select mi.id from menu_info mi where mi.resource_name = '权限列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (463, '权限保存', '/permission/resourceInfo/save.do', (select ri.id from resource_info ri where ri.res_url='/permission/resourceInfo/list.do'),(select mi.id from menu_info mi where mi.resource_name = '权限列表'),NULL);
insert into resource_info (id, res_name, res_url, parent_res_id, menu_id, res_remark) values (464, '权限删除', '/permission/resourceInfo/del.do', (select ri.id from resource_info ri where ri.res_url='/permission/resourceInfo/list.do'),(select mi.id from menu_info mi where mi.resource_name = '权限列表'),NULL);
