package com.xxtv.web.controller;

import com.jfinal.plugin.ehcache.CacheKit;
import com.xxtv.base.common.BaseController;
import com.xxtv.core.plugin.annotation.Control;
import com.xxtv.tools.EhcacheConstants;

@Control(controllerKey = "/cache")
public class CacheController extends BaseController {
	public void index() {
		CacheKit.removeAll(EhcacheConstants.LIVE_INTERFACE);
		renderJson("flush all!");
	}
}
