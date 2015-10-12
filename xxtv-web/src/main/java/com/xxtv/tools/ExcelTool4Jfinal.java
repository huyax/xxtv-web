package com.xxtv.tools;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

@SuppressWarnings("all")
public class ExcelTool4Jfinal
{
	/**
	 * 通用导出excel
	 * 
	 * @param datas 数据对象是{map/Model/Record/javaBean}
	 * @param percentNoConvert 不需要转成百分比的字段 比如收入和支出
	 * @param caseJson 比如状态字段 "{\"state\":{\"1\":\"启用\",\"2\":\"禁用\"}}"
	 * @param contr Jfinal的Controller 对象
	 */
	public static void renderXLS(List<?> datas, List<String> percentNoConvert, String caseJson, Controller contr)
	{
		Object data = null;
		if (datas != null && datas.size() != 0)
		{
			data = datas.get(0);
		}
		List dd = new ArrayList(datas);
		Map<String, Object> element = null;
		if (data instanceof Model)
		{
			dd = new ArrayList();
			for (Object obj : datas)
			{
				String[] names = ((Model) obj).getAttrNames();
				Object[] values = ((Model) obj).getAttrValues();
				element = new HashMap<String, Object>();
				for (int i = 0; i < names.length; i++)
				{
					element.put(names[i], values[i]);
				}
				dd.add(element);
			}
		}
		else if (data instanceof Record)
		{
			dd = new ArrayList();
			for (Object obj : datas)
			{
				dd.add(((Record) obj).getColumns());
			}
		}
		contr.renderFile(ExcelTool.exportExcel(dd, contr.getPara("title"), contr.getPara("columns"), percentNoConvert, caseJson));
	}

	/**
	 * 通用导入excel
	 * 
	 * @param model 数据对象类型是{map/Model/Record/javaBean}
	 * @param caseJson 反向 比如状态字段 "{\"state\":{\"1\":\"启用\",\"2\":\"禁用\"}}" 反向就是 "{\"state\":{\"启用\":\"1\",\"禁用\":\"2\"}}"
	 * @param contr Jfinal的Controller 对象
	 * @param nickNameTable 如果dao查询数据库用到了别名 页面上的filed也是用的别名 那这里要把对应关系{nickName,column}
	 * @return 返回传入model类型的List数据
	 * @throws Exception
	 */
	public static <E> List<E> importXLS(E model, String caseJson, Controller contr, Map<String, String> nickNameTable) throws Exception
	{
		String columnsJson = contr.getPara("columns");
		columnsJson = URLDecoder.decode(columnsJson, "UTF-8");
		File excelFile = contr.getFile().getFile();

		ArrayList<E> models = new ArrayList<E>();
		if (model instanceof Model || model instanceof Record)
		{
			ArrayList<HashMap> datas = ExcelTool.importExcel(excelFile, columnsJson, new HashMap(), caseJson);
			for (int i = 0; i < datas.size(); i++)
			{
				model = (E) model.getClass().newInstance();
				if (model instanceof Model)
				{
					//					Table table = TableMapping.me().getTable((Class<? extends Model>) model.getClass());
					// 了解 Model的put和set
					if (nickNameTable == null)
					{
						((Model) model).setAttrs(datas.get(i));
					}
					else
					{
						for (Object key : datas.get(i).keySet())
						{
							String tableColumn = nickNameTable.get(key);
							((Model) model).set(tableColumn, datas.get(i).get(key));
						}
					}
				}
				else if (model instanceof Record)
				{
					((Record) model).setColumns(datas.get(i));
				}
				models.add(model);
			}
			//			for (HashMap hashMap : datas) {
			//				model = (E) model.getClass().newInstance();
			//				if(model instanceof Model){
			////					Table table = TableMapping.me().getTable((Class<? extends Model>) model.getClass());
			//					// 了解 Model的put和set
			////					((Model)model).setAttrs(hashMap);
			//				}else if(model instanceof Record){
			//					((Record)model).setColumns(hashMap);
			//				}
			//				models.add(model);
			//			}
			return models;
		}

		return ExcelTool.importExcel(excelFile, columnsJson, model, caseJson);
	}
}
