package com.school.util.core.mybatis;

import java.lang.reflect.Field;

/**
 * Title: ReflectHelper <BR>
 * Description: MyBatis分页插件相关 <BR>
 * Copyright: Copyright (c) 2014-2015<BR>
 * 
 * @author liliang
 * @date 2016-3-13
 * @version 1.0
 */
public class ReflectHelper {
	public static Field getFieldByFieldName(Object obj, String fieldName) {  
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass  
                .getSuperclass()) {  
            try {  
                return superClass.getDeclaredField(fieldName);  
            } catch (NoSuchFieldException e) {  
            }  
        }  
        return null;  
    }  
  
    
    public static Object getValueByFieldName(Object obj, String fieldName)  
            throws SecurityException, NoSuchFieldException,  
            IllegalArgumentException, IllegalAccessException {  
        Field field = getFieldByFieldName(obj, fieldName);  
        Object value = null;  
        if(field!=null){  
            if (field.isAccessible()) {  
                value = field.get(obj);  
            } else {  
                field.setAccessible(true);  
                value = field.get(obj);  
                field.setAccessible(false);  
            }  
        }  
        return value;  
    }  
  
    
    public static void setValueByFieldName(Object obj, String fieldName,  
            Object value) throws SecurityException, NoSuchFieldException,  
            IllegalArgumentException, IllegalAccessException {  
        Field field = obj.getClass().getDeclaredField(fieldName);  
        if (field.isAccessible()) {  
            field.set(obj, value);  
        } else {  
            field.setAccessible(true);  
            field.set(obj, value);  
            field.setAccessible(false);  
        }  
    }  
}
