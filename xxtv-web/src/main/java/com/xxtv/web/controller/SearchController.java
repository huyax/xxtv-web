package com.xxtv.web.controller;

import java.io.UnsupportedEncodingException;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.xxtv.base.common.BaseController;
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
			try {
				q = new String(q.getBytes("iso8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int pageNum = getPara("page") == null ? 1 : getParaToInt("page");
			Page<MovieModel> page = MovieModel.dao.search(pageNum, 100, q);
			setAttr("list", page.getList());
			setAttr("totalPage", page.getTotalPage());
			setAttr("currentPage", pageNum);
			setAttr("total", page.getTotalRow());

			setAttr("q", q);
			render("search");
		}

	}
}
