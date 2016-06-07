package com.xxtv.web.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.xxtv.core.kit.MongoKit;
import com.xxtv.tools.DateTools;

/** 
* @author  huwei 
* @date 创建时间：2016年6月6日 上午11:00:14 
* @parameter  
*/
public class MovieService {

	public static List<Record> topMovie(){
		BasicDBObject filter_dbobject = new BasicDBObject();
		filter_dbobject.append("catelog",new BasicDBObject("$gte",1));
		filter_dbobject.append("catelog",new BasicDBObject("$lte",9));
		DBCursor cursor = MongoKit.getCollection("movie").find(filter_dbobject).limit(18).sort(new BasicDBObject("pub_date",-1));
		List<Record> list = MongoKit.toRecords(cursor);
		for(int i = 0; i < list.size(); i++){
			int catelog = list.get(i).getInt("catelog");
			list.get(i).set("pub_date",DateTools.formatDate(list.get(i).getDate("pub_date")));
			switch(catelog){
			case 1:list.get(i).set("catelog", "喜剧片");break;
			case 2:list.get(i).set("catelog", "动作片");break;
			case 3:list.get(i).set("catelog", "爱情片");break;
			case 4:list.get(i).set("catelog", "科幻片");break;
			case 5:list.get(i).set("catelog", "恐怖片");break;
			case 6:list.get(i).set("catelog", "战争片");break;
			case 7:list.get(i).set("catelog", "纪录片");break;
			case 8:list.get(i).set("catelog", "剧情片");break;
			case 9:list.get(i).set("catelog", "3D电影");break;
			}
		}
		return list;
	}
}
