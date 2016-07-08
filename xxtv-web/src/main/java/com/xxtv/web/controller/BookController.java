package com.xxtv.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xxtv.base.common.BaseController;
import com.xxtv.core.kit.MongoKit;
import com.xxtv.core.plugin.annotation.Control;

/**
 * @author huwei
 * @date 2016年1月4日
 *
 */
@Control(controllerKey = "/book")
public class BookController  extends BaseController{

	public void index() {
		setAttr("menu", "book");
		int cate = getPara("cate") == null ? 1 :Integer.parseInt(getPara("cate"));
		List<Record> rs = MongoKit.findAll("novel_cats");
		setAttr("cates", rs);
		setAttr("cate", cate);
		int pageNum = getPara("page") == null ? 1 : getParaToInt("page");
		
		Map<String,Object> filter = new HashMap<String, Object>();
		filter.put("catelog", cate);
		Page<Record> page = MongoKit.paginate("novel_name", pageNum, 10, filter);
		
		
		setAttr("list", page.getList());
		setAttr("totalPage", page.getTotalPage());
		setAttr("currentPage", pageNum);
		render("book");
	}
	public void cateList(){
		setAttr("menu", "book");
		int id = getParaToInt("id");
		Map<String,Object> filter = new HashMap<String, Object>();
		filter.put("book_id", id);
		
		Map<String,Object> filter2 = new HashMap<String, Object>();
		filter2.put("_id", id);
		
		List<Record> rs = MongoKit.findByCondition("novel_text", filter);
		List<Record> rs2 = MongoKit.findByCondition("novel_name", filter2);
		

		setAttr("book",rs2.get(0));
		if (rs != null && rs.size() > 0) {
			setAttr("total", rs.size());
			setAttr("totalNums", rs.get(0).get("totalNums"));
			setAttr("property", rs.get(0).get("property"));
			setAttr("list", rs);
		}
		render("book_cateList");
	}
   public void chapter(){
	   setAttr("menu", "book");
		int id = getParaToInt("id");
		Map<String,Object> filter = new HashMap<String, Object>();
		filter.put("_id", id);
		List<Record> rs = MongoKit.findByCondition("novel_text", filter);
		setAttr("book",rs.get(0));
		render("book/index");
   }
   
}
