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
		String url = LiveInterfaceModel.dao.getCateInterface(platformKey);
		Map result = (Map) JSON.parse(HttpKit.get(url));
		List mapList = JSON.parseArray("" + result.get("data"), HashMap.class);
		CacheKit.put(EhcacheConstants.GAME_CATALOG, platformKey, mapList);
		return mapList;
	}

	public synchronized static List getYYGameCata(String platformKey)
	{
		Object obj = CacheKit.get(EhcacheConstants.GAME_CATALOG, platformKey);
		if (obj != null)
		{
			return (List) obj;
		}
		String url = LiveInterfaceModel.dao.getCateInterface(platformKey);
		Map result = (Map) JSON.parse(HttpKit.get(url));
		List mapList = JSON.parseArray("" + result.get("data"), HashMap.class);
		List dataList = new ArrayList();
		/*for (int i = 0; i < mapList.size(); i++)
		{
			List tempList = JSON.parseArray("" + ((Map) mapList.get(i)).get("itemList"), HashMap.class);
			dataList.addAll(tempList);
		}*/
		
		List tempList = JSON.parseArray("" + ((Map) mapList.get(0)).get("itemList"), HashMap.class);
		dataList.addAll(tempList);
		
		CacheKit.put(EhcacheConstants.GAME_CATALOG, platformKey, dataList);
		return mapList;
	}
}
