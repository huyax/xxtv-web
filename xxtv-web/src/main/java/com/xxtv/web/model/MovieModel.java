package com.xxtv.web.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.xxtv.core.plugin.annotation.Table;

@SuppressWarnings("serial")
@Table(tableName = "movie")
public class MovieModel extends Model<MovieModel> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MovieModel.class);

	public static final MovieModel dao = new MovieModel();

	/**
	 * 全文索引
	 * 
	 * @param q
	 * @return
	 */
	public Page<MovieModel> search(int page, int size, String q) {
		/*
		 * return dao.paginate(page, size, "SELECT id,name,pub_date ",
		 * "FROM `movie`WHERE MATCH (name)AGAINST (?)",q);
		 */

		return dao.paginate(page, size, "SELECT id,name,pub_date ",
				"FROM `movie`WHERE name like '%" + q + "%'");
	}

	public List<MovieModel> top() {
		String sql = "select movie.*,catelogs.name cateName from movie left join catelogs on movie.catelog=catelogs.id  where movie.catelog between 1 and 9 order by movie.pub_date desc limit 18";
		return dao.find(sql);
	}
}
