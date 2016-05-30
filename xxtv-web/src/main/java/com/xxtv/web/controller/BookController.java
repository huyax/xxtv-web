package com.xxtv.web.controller;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.xxtv.base.common.BaseController;
import com.xxtv.core.plugin.annotation.Control;
import com.xxtv.web.model.BookCatelogsModel;
import com.xxtv.web.model.BookChapterModel;
import com.xxtv.web.model.BookModel;

/**
 * @author huwei
 * @date 2016年1月4日
 *
 */
@Control(controllerKey = "/book")
public class BookController  extends BaseController{
/*
	public void index() {
		setAttr("menu", "book");
		String cate = getPara("cate") == null ? "djph" : getPara("cate");
		setAttr("cates", BookCatelogsModel.dao.getAll());
		setAttr("cate", cate);
		System.out.println(cate);
		int pageNum = getPara("page") == null ? 1 : getParaToInt("page");
		String sql;
		if(cate.equals("djph")){
		sql = "from book  order by hits desc";
		}else{
		sql = "from book  where iden='"+cate+"' order by hits desc";
		}
		Page<BookModel> page = BookModel.dao.paginate(pageNum, 10,
				"select  *  ", sql);
		setAttr("list", page.getList());
		setAttr("totalPage", page.getTotalPage());
		setAttr("currentPage", pageNum);
		render("book");
	}
	public void cateList(){
		setAttr("menu", "book");
		int id = getParaToInt("id");
		List<BookChapterModel> list = BookChapterModel.dao.getBook(id);
		BookModel book = BookModel.dao.findById(id);
		if(null != list && list.size() >0){
		setAttr("book",book);
		setAttr("description",list.get(0).getStr("desc"));
		setAttr("total",list.size());
		setAttr("list",list);
		}
		render("book_cateList");
	}
   public void chapter(){
	   setAttr("menu", "book");
		int id = getParaToInt("id");
		BookChapterModel model = BookChapterModel.dao.findById(id);
		setAttr("book",model);
		render("book/index");
   }
   */
}
