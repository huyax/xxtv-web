package com.xxtv.web.controller;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.xxtv.base.common.BaseController;
import com.xxtv.core.plugin.annotation.Control;
import com.xxtv.web.model.PictureCatelogModel;
import com.xxtv.web.model.PictureMapModel;
import com.xxtv.web.model.PictureModel;

@Control(controllerKey = "/pic")
public class PicController  extends BaseController{
	
	public void index() {
		setAttr("menu", "pic");
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
	public void picture() {
		setAttr("menu", "pic");
		int id = getParaToInt("id");
		int pageNum = getPara("page") == null ? 1 : getParaToInt("page");		
		Page<PictureModel> page = PictureModel.dao.paginate(pageNum, 1,
				"select pic.* ", " FROM picture pic where pic.map_id="+id);
		setAttr("list", page.getList());
		setAttr("totalPage", page.getTotalPage());
		setAttr("currentPage", pageNum);
		setAttr("id", id);
		//随机推荐5条的
		List<PictureMapModel> list = PictureMapModel.dao.getRandom();
		setAttr("list2", list);
		render("pic/index");
	}
}
