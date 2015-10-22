package com.xxtv.web.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Model;
import com.xxtv.core.plugin.annotation.Table;
import com.xxtv.tools.EhcacheConstants;

@SuppressWarnings("serial")
@Table(tableName = "tv")
public class TVModel extends Model<TVModel>{
	
	public static final TVModel	dao		= new TVModel();

	private static final Logger	LOGGER	= LoggerFactory.getLogger(TVModel.class);
	
	public TVModel getTv(String name){
		String sql = "select * from tv where name=?";
		return dao.findFirstByCache(EhcacheConstants.TV, name , sql, name);
	}
	public List<TVModel> getTvByType(String type){
		String sql = "select * from tv where type= '"+ type +"'";
		return dao.findByCache(EhcacheConstants.TV,type, sql);
    }
}
