package com.xxtv.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.HttpKit;
import com.xxtv.base.common.BaseController;
import com.xxtv.core.plugin.annotation.Control;

@Control(controllerKey = "/")
public class IndexController extends BaseController
{
	private static final Logger	LOGGER	= LoggerFactory.getLogger(IndexController.class);

	public void index()
	{
		try
		{
			String url = "http://www.douyutv.com/api/v1/live/3?aid=android&auth=5b29ce8060866f98416614e8476e14d5&client_sys=android&limit=200&offset=0&time=1444565894";
			Map result = (Map) JSON.parse(HttpKit.get(url));
			int error = ((Integer) result.get("error")).intValue();
			if (error == 0)
			{
				List mapList = JSON.parseArray("" + result.get("data"), HashMap.class);
				setAttr("list", mapList);
			}
			render("index");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("index异常：{}",e);
			}
			render("index");
		}
	}

	public void live()
	{
		setAttr("uid", getPara("uid"));
		setAttr("nick", getPara("nick"));
		setAttr("id", getPara("id"));
		render("live");
	}
}
