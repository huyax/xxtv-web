package com.xxtv.base.common;

import java.io.File;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.xxtv.tools.EhcacheConstants;
import com.xxtv.tools.SysConstants;

public class ResCache
{

	/**
	 * 获取web应用的根路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getRootPath()
	{
		String rootPath = CacheKit.get(EhcacheConstants.RESOURCE, "rootPath");
		if (StrKit.isBlank(rootPath))
		{
			String url = ResCache.class.getResource("").getPath().replaceAll("%20", " ");
			int index = url.indexOf("WEB-INF");
			if (index == -1)
			{// 正对Maven本地调试
				index = url.indexOf("target");
				if (index != -1)
				{
					rootPath = url.substring(0, index) + "/src/main/webapp/";
				}
			}
			else
			{
				rootPath = url.substring(0, index);
			}
			CacheKit.put(EhcacheConstants.RESOURCE, "rootPath", rootPath);
		}
		return rootPath;
	}

	/**
	 * 根据view名称获取对应JS文件的路径
	 * 
	 * @param beanName view名称
	 * @return
	 */
	public static String getResJsPath(String beanName)
	{
		String jsPath = CacheKit.get(EhcacheConstants.RESOURCE, "js@" + beanName);
		if (StrKit.isBlank(jsPath))
		{
			String resPath = getRootPath() + "assets/";
			jsPath = SysConstants.VIEW_BASE_PATH + "/js/" + beanName + ".js";
			if (new File(resPath + jsPath.replace('/', File.separatorChar)).exists())
			{
				CacheKit.put(EhcacheConstants.RESOURCE, "js@" + beanName, jsPath);
			}
			else
			{
				jsPath = null;
			}
		}
		return jsPath;
	}

	/**
	 * 根据view名称获取对应CSS文件的路径
	 * 
	 * @param beanName view名称
	 * @return
	 */
	public static String getResCssPath(String beanName)
	{
		String cssPath = CacheKit.get(EhcacheConstants.RESOURCE, "css@" + beanName);
		if (StrKit.isBlank(cssPath))
		{
			String resPath = getRootPath() + "assets/";
			cssPath = SysConstants.VIEW_BASE_PATH + "/css/" + beanName + ".css";
			if (new File(resPath + cssPath.replace('/', File.separatorChar)).exists())
			{
				CacheKit.put(EhcacheConstants.RESOURCE, "css@" + beanName, cssPath);
			}
			else
			{
				cssPath = null;
			}
		}
		return cssPath;
	}

}
