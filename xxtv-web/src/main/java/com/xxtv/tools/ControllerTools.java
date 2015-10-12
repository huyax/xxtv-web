package com.xxtv.tools;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Model;

public class ControllerTools
{

	/**
	 * 通过前台传的json字符传获取对象
	 * dataJson必须是map形式且key必须和model的表的字段一致
	 * 
	 * @param dataJson
	 * @param modelClass
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> T getModelByJson(String dataJson, Class<T> modelClass)
	{
		Object model = null;
		try
		{
			model = modelClass.newInstance();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		if ((model instanceof Model))
		{
			Map<String, Object> map = JSON.parseObject(dataJson, Map.class);
			((Model) model).setAttrs(map);
		}
		else
		{
			model = JSON.parseObject(dataJson, modelClass);
		}
		return (T) model;
	}
}
