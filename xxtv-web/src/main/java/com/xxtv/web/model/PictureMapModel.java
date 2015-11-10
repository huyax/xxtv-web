package com.xxtv.web.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Model;
import com.xxtv.core.plugin.annotation.Table;
import com.xxtv.tools.EhcacheConstants;

/**
 * @author huwei
 * @date 2015年10月30日
 *
 */
@SuppressWarnings("serial")
@Table(tableName = "picture_map")
public class PictureMapModel extends Model<PictureMapModel>{
	private static final Logger			LOGGER	= LoggerFactory.getLogger(PictureMapModel.class);

	public static final PictureMapModel	dao		= new PictureMapModel();
	
	public List<PictureMapModel> getRandom()
	{
		String sql = "SELECT * FROM picture_map where catelogs = 9 ORDER BY RAND() LIMIT 6";
		return dao.find(sql);
	}

	public List<PictureMapModel> getIndexRandom(int i) {
		String sql = "SELECT * FROM picture_map ORDER BY RAND() LIMIT ?";
		return dao.find(sql,i);
	}
}

