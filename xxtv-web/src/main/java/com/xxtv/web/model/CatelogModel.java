package com.xxtv.web.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Model;
import com.xxtv.core.plugin.annotation.Table;
import com.xxtv.tools.EhcacheConstants;

@SuppressWarnings("serial")
@Table(tableName = "catelogs")
public class CatelogModel extends Model<CatelogModel>
{
	private static final Logger			LOGGER	= LoggerFactory.getLogger(CatelogModel.class);

	public static final CatelogModel	dao		= new CatelogModel();
	
	public List<CatelogModel> getAll()
	{
		String sql = "select * from catelogs";
		return dao.findByCache(EhcacheConstants.MOVIE,"catelogs", sql);
	}
}
