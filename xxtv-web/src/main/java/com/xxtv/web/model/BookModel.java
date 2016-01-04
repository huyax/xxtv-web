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
@SuppressWarnings("serial")
@Table(tableName = "book")
public class BookModel  extends Model<BookModel>{
	private static final Logger			LOGGER	= LoggerFactory.getLogger(BookModel.class);

	public static final BookModel	dao		= new BookModel();
	
	public List<BookModel> getCatelogs(){
		String sql = "SELECT DISTINCT catelogs FROM book";
		return dao.find(sql);
	}
}
