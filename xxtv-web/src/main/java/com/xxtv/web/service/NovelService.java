package com.xxtv.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.jfinal.plugin.activerecord.Record;
import com.xxtv.core.kit.MongoKit;

/** 
* @author  huwei 
* @date 创建时间：2016年7月7日 下午4:05:47 
* @parameter  
*/
public class NovelService {
	
	
	public  static List<Record> topNovel(){
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("catelog", 1);
		List<Record> rs = MongoKit.findByCondition("novel_name", filter);
		return rs.subList(0, 18);

	}
	

}