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
//@Table(tableName = "book_chapter")
public class BookChapterModel {
	private static final Logger			LOGGER	= LoggerFactory.getLogger(BookChapterModel.class);

	public static final BookChapterModel	dao		= new BookChapterModel();
	/*
	public List<BookChapterModel> getBook(int id){
		String sql = "SELECT * FROM book_chapter where book_id =? order by id";
		return dao.find(sql,id);
	}
	*/
}
