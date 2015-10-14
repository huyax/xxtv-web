package com.xxtv.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.xxtv.base.common.BaseController;
import com.xxtv.core.plugin.annotation.Control;
import com.xxtv.tools.CacheUtil;
import com.xxtv.tools.EhcacheConstants;
import com.xxtv.web.model.LiveInterfaceModel;

@Control(controllerKey = "/")
public class IndexController extends BaseController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(IndexController.class);

	public void index() {
		douyu();
	}

	public void yy() {
		String itemId = getPara(0);
		String platformKey = "yy";
		if (itemId == null) {
			// 默认游戏lol
			itemId = "1";
		}

		setAttr("itemId", itemId);
		// 直播列表
		String url = LiveInterfaceModel.dao.getLiveInterface(platformKey,
				itemId).getStr("url");
		Map result = (Map) JSON.parse(HttpKit.get(url));
		String message = (String) result.get("message");
		if ("success".equals(message)) {
			List mapList = JSON.parseArray("" + result.get("data"),
					HashMap.class);
			setAttr("list", mapList);
		}

		// 游戏分类
		Object obj = CacheKit.get(EhcacheConstants.GAME_CATALOG, platformKey);
		if (obj == null) {
			setAttr("cateList", CacheUtil.getYYGameCata(platformKey));
		} else {
			setAttr("cateList", (List) obj);
		}

		render(platformKey);
	}

	public void douyu() {
		String gameKey = getPara(0);
		String cateId = getPara("cateId");
		String platformKey = "douyu";

		if (cateId == null && "dota2".equals(gameKey)) {
			cateId = "3";
		}
		setAttr("cateId", cateId == null ? "1" : cateId);
		setAttr("platformKey", platformKey);
		if (gameKey == null) {
			gameKey = "lol";
		}
		setAttr("gameKey", gameKey);
		// 直播列表
		String url = LiveInterfaceModel.dao.getLiveInterface(platformKey,
				gameKey).getStr("url");
		Map result = (Map) JSON.parse(HttpKit.get(url));
		int error = ((Integer) result.get("error")).intValue();
		if (error == 0) {
			List mapList = JSON.parseArray("" + result.get("data"),
					HashMap.class);
			setAttr("list", mapList);
		}

		// 游戏分类
		Object obj = CacheKit.get(EhcacheConstants.GAME_CATALOG, platformKey);
		if (obj == null) {
			setAttr("cateList", CacheUtil.getDouyuGameCata(platformKey));
		} else {
			setAttr("cateList", (List) obj);
		}

		render(platformKey);
	}

}
