package com.xxtv.web.controller;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xxtv.base.common.BaseController;
import com.xxtv.core.kit.MongoKit;
import com.xxtv.core.plugin.annotation.Control;
import com.xxtv.web.model.MovieModel;

@Control(controllerKey = "/search")
public class SearchController extends BaseController {

	public void index() {
		setAttr("menu", "movie");
		String q = getPara("q");
		if (StrKit.isBlank(q)) {
			redirect("/movie");
		} else {
			
			int pageNum = getPara("page") == null ? 1 : getParaToInt("page");
			Map<String,Object> like = new HashMap<String,Object>();
			like.put("name", q);
			Page<Record> page = MongoKit.paginate("movie", pageNum, 100, null, like);
			setAttr("list", page.getList());
			setAttr("totalPage", page.getTotalPage());
			setAttr("currentPage", pageNum);
			setAttr("total", page.getTotalRow());

			setAttr("q", q);
			render("search");
		}

	}
}
