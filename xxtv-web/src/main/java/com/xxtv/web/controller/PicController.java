package com.xxtv.web.controller;

import com.jfinal.plugin.activerecord.Page;
import com.xxtv.base.common.BaseController;
import com.xxtv.core.plugin.annotation.Control;
import com.xxtv.web.model.PictureCatelogModel;
import com.xxtv.web.model.PictureMapModel;

@Control(controllerKey = "/pic")
public class PicController  extends BaseController{
	
	public void index() {
		int cate = getPara("cate") == null ? 2 : Integer.parseInt(getPara("cate"));
		setAttr("cates", PictureCatelogModel.dao.getAll());
		setAttr("cate", cate);
		int pageNum = getPara("page") == null ? 1 : getParaToInt("page");
		
		Page<PictureMapModel> page = PictureMapModel.dao.paginate(pageNum, 24,
				"select *  ", "from picture_map map  where map.catelogs="+cate);
		setAttr("list", page.getList());
		setAttr("totalPage", page.getTotalPage());
		setAttr("currentPage", pageNum);
		render("picture");
	}
}
