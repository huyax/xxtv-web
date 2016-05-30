package com.xxtv.web.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Model;
import com.xxtv.core.plugin.annotation.Table;
import com.xxtv.tools.EhcacheConstants;

@SuppressWarnings("serial")
@Table(tableName = "live_interface")
public class LiveInterfaceModel extends Model<LiveInterfaceModel>
{

	public static final LiveInterfaceModel	dao		= new LiveInterfaceModel();

	private static final Logger				LOGGER	= LoggerFactory.getLogger(LiveInterfaceModel.class);

//	public LiveInterfaceModel getLiveInterface(String platform, String game)
//	{
//		String sql = "select * from live_interface where platform=? and game=?";
//		return dao.findFirstByCache(EhcacheConstants.LIVE_INTERFACE, platform + "_" + game, sql, platform, game);
//	}

	public String getCateInterface(String platform)
	{
		String sql = "select url from live_interface where platform=? and type=0";
		LiveInterfaceModel model = dao.findFirstByCache(EhcacheConstants.LIVE_INTERFACE, platform, sql, platform);
		return model.get("url");
	}
}
