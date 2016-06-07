package com.xxtv.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.xxtv.web.model.LiveInterfaceModel;

@SuppressWarnings("all")
public class CacheUtil
{
	private static final Logger	LOGGER	= LoggerFactory.getLogger(CacheUtil.class);

	public synchronized static List getDouyuGameCata(String platformKey)
	{
		Object obj = CacheKit.get(EhcacheConstants.GAME_CATALOG, platformKey);
		if (obj != null)
		{
			return (List) obj;
		}
		String url = "http://capi.douyucdn.cn/api/v1/getColumnDetail";
		Map result = (Map) JSON.parse(HttpKit.get(url));
		List mapList = JSON.parseArray("" + result.get("data"), HashMap.class);
		CacheKit.put(EhcacheConstants.GAME_CATALOG, platformKey, mapList);
		return mapList;
	}
	
	public synchronized static List getPandaGameCata(String platformKey)
	{
		Object obj = CacheKit.get(EhcacheConstants.GAME_CATALOG, platformKey);
		if (obj != null)
		{
			return (List) obj;
		}
		String url = "http://api.m.panda.tv/ajax_get_all_subcate";
		Map result = (Map) JSON.parse(HttpKit.get(url));
		List mapList = JSON.parseArray("" + result.get("data"), HashMap.class);
		CacheKit.put(EhcacheConstants.GAME_CATALOG, platformKey, mapList);
		return mapList;
	}

}
