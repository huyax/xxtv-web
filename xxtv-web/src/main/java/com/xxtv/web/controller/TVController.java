package com.xxtv.web.controller;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;
import com.xxtv.base.common.BaseController;
import com.xxtv.core.kit.MongoKit;
import com.xxtv.core.plugin.annotation.Control;

@Control(controllerKey = "/tv")
public class TVController extends BaseController {

	public void index() {
		setAttr("menu", "tv");
		String name = getPara("name");
		if (name == null) {
			name = "深圳卫视";
		}
		List<Record> rs = MongoKit.findAll("tv");
		setAttr("tvlist", rs);
		setAttr("name", name);
		for(int i = 0; i < rs.size(); i++){
			if(name.equals(rs.get(i).get("name"))){
				setAttr("tv", rs.get(i));
			}
		}
		render("tv");
	}
}
