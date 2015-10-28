package com.xxtv.web.controller;


import java.io.UnsupportedEncodingException;
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
		try {
			name = new String(name.getBytes("iso8859-1"), "UTF-8");
			type = new String(type.getBytes("iso8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
