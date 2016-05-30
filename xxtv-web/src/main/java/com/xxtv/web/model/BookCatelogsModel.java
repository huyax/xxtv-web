package com.xxtv.web.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Model;
import com.xxtv.core.plugin.annotation.Table;

/**
 * @author huwei
 * @date 2016年1月4日
 *
 */
//@SuppressWarnings("serial")
//@Table(tableName = "book_catelogs")
public class BookCatelogsModel {
	private static final Logger			LOGGER	= LoggerFactory.getLogger(BookCatelogsModel.class);

	public static final BookCatelogsModel	dao		= new BookCatelogsModel();
	
//	public List<BookCatelogsModel> getAll(){
//		String sql = "SELECT * FROM book_catelogs";
//		return dao.find(sql);
//	}
}
