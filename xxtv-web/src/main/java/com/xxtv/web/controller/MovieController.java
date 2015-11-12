package com.xxtv.web.controller;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.xxtv.base.common.BaseController;
import com.xxtv.core.plugin.annotation.Control;
import com.xxtv.web.model.CatelogModel;
import com.xxtv.web.model.MovieModel;

@Control(controllerKey = "/movie")
public class MovieController extends BaseController {

	public void index() {
		setAttr("menu", "movie");
		int cate = getPara("cate") == null ? 1 : Integer.parseInt(getPara("cate"));
		setAttr("cates", CatelogModel.dao.getAll());
		setAttr("cate", cate);
		int pageNum = getPara("page") == null ? 1 : getParaToInt("page");
		
		Page<MovieModel> page = MovieModel.dao.paginate(pageNum, 24,
				"select movie.*,catelogs.name cateName  ", "from movie left join catelogs on movie.catelog=catelogs.id where movie.catelog="+cate+" order by movie.pub_date desc");
		setAttr("list", page.getList());
		setAttr("totalPage", page.getTotalPage());
		setAttr("currentPage", pageNum);
		render("movie");
	}
	
	
	public void detail() {
		List<MovieModel> movieTop = MovieModel.dao.top();
		setAttr("list", movieTop);
		setAttr("menu", "movie");
		int id = getParaToInt("id");
		setAttr("movie",MovieModel.dao.findById(id));
		render("movie/index");
	}
}
