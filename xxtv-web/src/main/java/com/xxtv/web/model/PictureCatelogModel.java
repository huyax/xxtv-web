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
@Table(tableName = "picture_catelogs")
public class PictureCatelogModel extends Model<PictureCatelogModel>{
	private static final Logger	LOGGER	= LoggerFactory.getLogger(PictureCatelogModel.class);

	public static final PictureCatelogModel dao	= new PictureCatelogModel();
	
	public List<PictureCatelogModel> getAll()
	{
		String sql = "select * from picture_catelogs where pid is not null";
		return dao.findByCache(EhcacheConstants.PIC,"picCatelogs", sql);
	}
}
