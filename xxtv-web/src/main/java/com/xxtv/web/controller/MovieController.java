package com.xxtv.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xxtv.base.common.BaseController;
import com.xxtv.core.kit.MongoKit;
import com.xxtv.core.plugin.annotation.Control;
import com.xxtv.tools.DateTools;
import com.xxtv.web.service.MovieService;

@Control(controllerKey = "/movie")
public class MovieController extends BaseController {

	public void index() {
		setAttr("menu", "movie");
		int cate = getPara("cate") == null ? 1 : Integer.parseInt(getPara("cate"));
		setAttr("cates", MongoKit.findAll("catelogs"));
		setAttr("cate", cate);
		int pageNum = getPara("page") == null ? 1 : getParaToInt("page");
		
		Map<String,Object> filter = new HashMap<String, Object>();
		filter.put("catelog", cate);
		Page<Record> page = MongoKit.paginate("movie", pageNum, 24, filter);
		for(Record r : page.getList()){
			r.set("pub_date", DateTools.formatDate(r.getDate("pub_date")));
		}
		setAttr("list", page.getList());
		setAttr("totalPage", page.getTotalPage());
		setAttr("currentPage", pageNum);
		render("movie");
	}
	
	
	public void detail() {
		List<Record> movieTop = MovieService.topMovie();
		setAttr("list", movieTop);
		setAttr("menu", "movie");
		int id = getParaToInt("id");
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("_id", id);
		List<Record> rs = MongoKit.findByCondition("movie",filter);
		if(null != rs && rs.size() > 0){
			setAttr("movie",rs.get(0));
			render("movie/index");
		}else{
			render("index");
		}
	}
}
