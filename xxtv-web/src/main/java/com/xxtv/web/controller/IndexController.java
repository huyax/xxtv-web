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
public class IndexController extends BaseController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(IndexController.class);

	public void index() {
		render("index");
	}

	public void dota2() {
		try {
			String url = "http://www.douyutv.com/api/v1/live/3?aid=android&auth=5b29ce8060866f98416614e8476e14d5&client_sys=android&limit=200&offset=0&time=1444565894";
			Map result = (Map) JSON.parse(HttpKit.get(url));
			int error = ((Integer) result.get("error")).intValue();
			if (error == 0) {
				List mapList = JSON.parseArray("" + result.get("data"),
						HashMap.class);
				setAttr("list", mapList);
			}
			render("dota2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("index异常：{}", e);
			}
			render("dota2");
		}
	}

	public void lol() {
		try {
			String url = "http://api.m.huya.com/gamelabel/labellive?limit=40&platform=android&labelid=6&page=1&gameid=1&version=3.2.2";
			Map result = (Map) JSON.parse(HttpKit.get(url));
			Map dataMap = (Map) JSON.parse("" + result.get("data"));
			List mapList = JSON.parseArray("" + dataMap.get("lives"),
					HashMap.class);
			setAttr("list", mapList);
			render("lol");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("index异常：{}", e);
			}
			render("lol");
		}
	}

}
