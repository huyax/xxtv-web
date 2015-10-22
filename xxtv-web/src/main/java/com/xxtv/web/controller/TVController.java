package com.xxtv.web.controller;


import java.util.List;

import com.xxtv.base.common.BaseController;
import com.xxtv.core.plugin.annotation.Control;
import com.xxtv.web.model.TVModel;

@Control(controllerKey = "/tv")
public class TVController extends BaseController{
	
	public void index()
	{	
		String name = getPara("name");
		String type = getPara("type");
		if(name == null && type == null){
			name = "湖南卫视";
			type = "卫视";
		}
		if(name == null && type != null){
			if(type == "卫视"){
				name = "湖南卫视";
			}
			if(type == "CCTV"){
				name = "CCTV-1";
			}
		}
		List<TVModel> lists = TVModel.dao.getTvByType(type);
		TVModel tvModel = TVModel.dao.getTv(name);
		setAttr("name",name);
		setAttr("type",type);
		setAttr("lists",lists);
		setAttr("tv",tvModel);
		render("tv");
	}
}
