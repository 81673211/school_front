package com.school.biz.dao.base;

import java.util.List;
import java.util.Map;

/**
 * <p>Title: BaseDao</p>
 * <p>Description: 基础dao接口，定义单表的增删该查方法</p>
 * 
 * @author liliang
 * 
 */
public interface BaseDao {
	
	int deleteByPrimaryKey(Long id);

    int insert(Object record);

    int insertSelective(Object record);

    Object selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Object record);

    int updateByPrimaryKey(Object record);

    List selectByParams(Map<String, Object> param);
}
