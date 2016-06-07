package com.xxtv.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.xxtv.base.common.BaseController;
import com.xxtv.core.plugin.annotation.Control;
import com.xxtv.tools.CacheUtil;
import com.xxtv.tools.EhcacheConstants;
import com.xxtv.web.service.MovieService;

@Control(controllerKey = "/")
public class IndexController extends BaseController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(IndexController.class);

	public void index() {
		setAttr("menu", "index");
		//取最新电影
		List<Record> movieTop = MovieService.topMovie();
		setAttr("movieTop", movieTop);
		
		//首页放斗鱼，不要虎牙了
		String douyu_lol = "http://capi.douyucdn.cn/api/v1/live/1";
		Map result = (Map) JSON.parse(HttpKit.get(douyu_lol));
		Integer errorCode = (Integer) result.get("error");
		if (0 == errorCode) {
			List mapList = JSON.parseArray("" + result.get("data"),
					HashMap.class);
			setAttr("liveTop", mapList.subList(0, 8));
		}
		// 斗鱼颜值
		String douyu_yan = "http://capi.douyucdn.cn/api/v1/getVerticalRoom";
		Map result2 = (Map) JSON.parse(HttpKit.get(douyu_yan));
		Integer errorCode2 = (Integer) result2.get("error");
		if (0 == errorCode2) {
			List mapList2 = JSON.parseArray("" + result2.get("data"), HashMap.class);
			setAttr("liveTop2", mapList2.subList(0, 8));
		}
		render("index");
	}


	public void douyu() {
		setAttr("menu", "live");
		String cateId = getPara("cateId") == null ? "0" : getPara("cateId");
		String platformKey = "douyu";

		setAttr("cateId", cateId);
		setAttr("platformKey", platformKey);
		String url = "";
		if("0".equals(cateId)){
			url = "http://capi.douyucdn.cn/api/v1/getVerticalRoom";
		}else{
			url = "http://capi.douyucdn.cn/api/v1/live/" + cateId;
		}
		    
		// 直播列表
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

	public void panda() {
		setAttr("menu", "live");
		String cate = getPara("cate") == null ? "lol" : getPara("cate");
		String platformKey = "panda";

		setAttr("cate", cate);
		setAttr("platformKey", platformKey);
		String url = "http://api.m.panda.tv/ajax_get_live_list_by_cate?pagenum=40&cate="+cate;
		    
		// 直播列表
		Map result = (Map) JSON.parse(HttpKit.get(url));
		int error = ((Integer) result.get("errno")).intValue();
		if(0 == error){
			List mapList = JSON.parseArray(JSON.parseObject(JSON.parseObject(HttpKit.get(url)).get("data").toString()).get("items").toString(),
					HashMap.class);
			setAttr("list", mapList);
		}

		// 游戏分类
		Object obj = CacheKit.get(EhcacheConstants.GAME_CATALOG, platformKey);
		if (obj == null) {
			setAttr("cateList", CacheUtil.getPandaGameCata(platformKey));
		} else {
			setAttr("cateList", (List) obj);
		}

		render(platformKey);
	}
	
}
