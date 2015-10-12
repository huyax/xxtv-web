package com.xxtv.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 依赖fastjson poi
 * 
 * @author roy
 *
 */
@SuppressWarnings("all")
public class ExcelTool
{
	/**
	 * 导出数据到excel
	 * 
	 * @param datas 数据对象list[javaBean或Map]
	 * @param title 报表标题
	 * @param columnsJson 属性字段组合的json字符串
	 * @param percentNoConvert 不需要转成百分比的字段 比如收入和支出
	 * @param caseJson 比如状态字段 "{\"state\":{\"1\":\"启用\",\"2\":\"禁用\"}}"
	 * @return excel文件对象
	 */
	public static File exportExcel(List<?> datas, String title, String columnsJson, List<String> percentNoConvert, String caseJson)
	{
		HashMap anyTemp = null;
		if (caseJson != null && !"".equals(caseJson))
		{
			anyTemp = JSON.parseObject(caseJson, HashMap.class);
		}

		ArrayList<String> fields = new ArrayList<String>();
		ArrayList<String> columns = new ArrayList<String>();
		JSONArray jsonArray = JSON.parseArray(columnsJson);

		for (int i = 0; i < jsonArray.size(); i++)
		{
			JSONObject o = jsonArray.getJSONObject(i);
			if ("0".equals(o.getString("export")))
			{
				continue;
			}
			fields.add(o.getString("field"));
			columns.add(o.getString("title"));
		}

		if (fields.size() == 0)
		{
			try
			{
				File f = File.createTempFile("空文件", ".txt");
				FileOutputStream fout = new FileOutputStream(f);
				fout.write("没有选择列".getBytes());
				fout.flush();
				fout.close();
				return f;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER); // 创建一个居中格式

		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);
		if (datas.size() > 65533)
		{
			int i = datas.size() - 65533;
			int y = (int) Math.ceil(i / 65533f);
			for (int j = 0; j < y + 1; j++)
			{
				HSSFSheet sheet = wb.createSheet(title + j);
				processTitle(title, columns, wb, sheet);
				if (j == 0)
				{
					//第一个sheet
					writeToRow(datas, percentNoConvert, anyTemp, fields, sheet, style, format, 0);
				}
				else
				{
					//中间的sheet
					writeToRow(datas, percentNoConvert, anyTemp, fields, sheet, style, format, j * 65533);
				}
				processWidth(sheet);
			}
		}
		else
		{
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = wb.createSheet(title);
			processTitle(title, columns, wb, sheet);
			writeToRow(datas, percentNoConvert, anyTemp, fields, sheet, style, format, 0);
			processWidth(sheet);
		}

		// 第六步，将文件存到指定位置
		File file = null;
		try
		{
			String fileName = new SimpleDateFormat("yyyyMMdd_kkmmss").format(new Date());
			file = File.createTempFile(title + fileName, ".xls");
			FileOutputStream fout = new FileOutputStream(file);
			wb.write(fout);
			fout.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return file;
	}

	private static void processWidth(HSSFSheet sheet)
	{
		//设置列宽
		if (sheet.getRow(2) != null)
		{
			for (int i = 0; i < sheet.getRow(2).getLastCellNum(); i++)
			{
				int cellType = sheet.getRow(2).getCell(i).getCellType();
				switch (cellType)
				{
				case Cell.CELL_TYPE_BLANK :
					sheet.setColumnWidth(i, (int) 35.7 * 200);
					break;
				case Cell.CELL_TYPE_BOOLEAN :
					sheet.setColumnWidth(i, (int) 35.7 * 200);
					break;
				case Cell.CELL_TYPE_NUMERIC :
					if (DateUtil.isCellDateFormatted(sheet.getRow(2).getCell(i)))
					{
						sheet.setColumnWidth(i, (int) 35.7 * 200);
					}
					else
					{
						sheet.setColumnWidth(i, (int) 35.7 * 200);
					}
					break;
				case Cell.CELL_TYPE_STRING :
					String value = sheet.getRow(2).getCell(i).getStringCellValue();
					//12字符100px  一个汉字2个字符
					for (int j = 0; j < value.length(); j++)
					{
						char c = value.charAt(j);
						int x = value.length() * 2 / 12;
						if (c >= 19968 && c <= 40869)
						{
							sheet.setColumnWidth(i, (int) 35.7 * 100 * x + (int) 35.7 * 60);
							break;
						}
						else
						{
							sheet.setColumnWidth(i, (int) 35.7 * 100 * value.length() / 12 + (int) 35.7 * 60);
						}
					}
					if (value == null || "".equals(value))
					{
						sheet.setColumnWidth(i, (int) 35.7 * 100 + (int) 35.7 * 60);
					}
					break;
				default :
					sheet.setColumnWidth(i, (int) 35.7 * 200);
				}
			}
		}
	}

	private static void processTitle(String title, ArrayList<String> columns, HSSFWorkbook wb, HSSFSheet sheet)
	{
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow(0);

		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style2.setAlignment(CellStyle.ALIGN_CENTER);
		style2.setTopBorderColor(HSSFColor.RED.index);
		style2.setRightBorderColor(HSSFColor.RED.index);
		style2.setBottomBorderColor(HSSFColor.RED.index);
		style2.setLeftBorderColor(HSSFColor.RED.index);

		HSSFFont font = wb.createFont();
		font.setFontName("楷体");
		font.setFontHeightInPoints((short) 16);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style2.setFont(font);

		HSSFCellStyle style3 = wb.createCellStyle();
		style3.setFillForegroundColor(HSSFColor.YELLOW.index);
		style3.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style3.setAlignment(CellStyle.ALIGN_CENTER);
		style3.setTopBorderColor(HSSFColor.RED.index);
		style3.setRightBorderColor(HSSFColor.RED.index);
		style3.setBottomBorderColor(HSSFColor.RED.index);
		style3.setLeftBorderColor(HSSFColor.RED.index);

		if (columns.size() < 6)
		{
			for (int i = 0; i < columns.size() + 6; i++)
			{
				row.createCell(i).setCellStyle(style2);
			}
		}
		else
		{
			for (int i = 0; i < columns.size(); i++)
			{
				row.createCell(i).setCellStyle(style2);
			}
		}

		//cell00
		row.getCell(0).setCellValue(title);

		if (columns.size() < 6)
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columns.size() - 1 + 6));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columns.size() - 1));

		HSSFRow row2 = sheet.createRow(1);
		for (int i = 0; i < columns.size(); i++)
		{
			HSSFCell cell1x = row2.createCell(i);
			cell1x.setCellValue(columns.get(i));
			cell1x.setCellStyle(style3);
		}
	}

	private static void writeToRow(List<?> datas, List<String> percentNoConvert, HashMap anyTemp, ArrayList<String> fields, HSSFSheet sheet,
			HSSFCellStyle style, NumberFormat format, int starIndex)
	{
		int curEndIndex = starIndex + 65533;
		if (curEndIndex > datas.size())
		{
			curEndIndex = datas.size();
		}
		List<?> newDatas = datas.subList(starIndex, curEndIndex);
		for (int i = 0; i < newDatas.size(); i++)
		{
			HSSFRow rowx = sheet.createRow(i + 2);
			Object object = newDatas.get(i);
			for (int j = 0; j < fields.size(); j++)
			{
				HSSFCell cellxx = rowx.createCell(j);
				//				cellxx.setCellValue(datas.get(i).get(fields.get(j))+"");
				Object value = null;
				if (object instanceof Map)
				{
					value = ((Map) object).get(fields.get(j));
				}
				else
				{
					// javabean
					value = refeltFieldGet(object, fields.get(j));
				}
				//				value = ((Map) object).get(fields.get(j));
				if (value instanceof Boolean)
				{
					/**
					 * // Map<String, List<Map<String, String>>> anyTemp || caseJson
					 * 
					 * Map<String, Map<String, String>> anyTemp = new HashMap<String, Map<String,String>>();
					 * Map<String, String> temp = new HashMap<String, String>();
					 * temp.put("1", "启用");
					 * temp.put("2", "禁用");
					 * anyTemp.put("state", temp);
					 * 
					 * "{\"state\":{\"1\":\"启用\",\"2\":\"禁用\"}}"
					 * 
					 * if(anyTemp!=null){
					 * Map<String, String> temp = anyTemp.get(fields.get(j));
					 * if(temp!=null){
					 * String tempValue = temp.get(value.toString());
					 * if(tempValue!=null&&!"".equals(tempValue)){
					 * cellxx.setCellValue(tempValue);
					 * }
					 * }
					 * }
					 */
					if (anyTemp != null)
					{
						Object temp = anyTemp.get(fields.get(j));
						if (temp != null)
						{
							JSONObject jo = JSON.parseObject(temp.toString());
							String tempValue = jo.getString(value.toString());
							if (tempValue != null && !"".equals(tempValue))
							{
								cellxx.setCellValue(tempValue);
							}
						}
					}
					else
					{
						if ((boolean) value == true)
						{
							cellxx.setCellValue(1);
						}
						else
						{
							cellxx.setCellValue(0);
						}
					}
				}
				//				else if (value instanceof Double) {
				//					cellxx.setCellValue((Double) value);
				//				} 
				else
				{
					if (value == null)
					{
						value = "";
					}

					String regex = "[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+";
					boolean isDouble = Pattern.matches(regex, value + "");
					if (isDouble)
					{
						try
						{
							if (null != percentNoConvert)
							{
								boolean flag = false;
								for (String field : percentNoConvert)
								{
									if (field.equals(fields.get(j)))
									{
										flag = true;
										break;
									}
								}

								if (flag)
								{
									//									value = (value+"").substring(0, (value+"").indexOf("."));
									Double db = Double.valueOf(value + "");
									String dbStr = format.format(db);
									cellxx.setCellValue(dbStr);
								}
								else
								{
									Double db = Double.valueOf(value + "");
									String dbStr = format.format(db * 100);
									//									db = Double.valueOf(dbStr);
									cellxx.setCellValue(dbStr + "%");
								}

							}
							else
							{
								Double db = Double.valueOf(value + "");
								String dbStr = format.format(db * 100);
								//								db = Double.valueOf(dbStr);
								cellxx.setCellValue(dbStr + "%");
							}
						}
						catch (NumberFormatException e)
						{
							cellxx.setCellValue(value + "");
						}
					}
					else if (anyTemp != null)
					{
						/*
						 * Map<String, String> temp = anyTemp.get(fields.get(j));
						 * if(temp!=null){
						 * String tempValue = temp.get(value.toString());
						 * if(tempValue!=null&&!"".equals(tempValue)){
						 * cellxx.setCellValue(tempValue);
						 * }
						 * }else{
						 * cellxx.setCellValue(value + "");
						 * }
						 */
						Object temp = anyTemp.get(fields.get(j));
						if (temp != null)
						{
							JSONObject jo = JSON.parseObject(temp.toString());
							String tempValue = jo.getString(value.toString());
							if (tempValue != null && !"".equals(tempValue))
							{
								cellxx.setCellValue(tempValue);
							}
						}
						else
						{
							cellxx.setCellValue(value + "");
						}
					}
					else
					{
						cellxx.setCellValue(value + "");
					}
				}
				cellxx.setCellStyle(style);
			}
		}
	}

	/**
	 * 导入excel转换成map list
	 * 
	 * @param excelFile excel文件对象
	 * @param columnsJson 属性字段组合的json字符串 客户端上传文件的时候在queryurl中的json值 页面需要进行encodeURIComponent(encodeURIComponent(json));
	 * @param model 数据对象类型是[map或javaBean]
	 * @param caseJson 反向 比如状态字段 "{\"state\":{\"1\":\"启用\",\"2\":\"禁用\"}}" 反向就是 "{\"state\":{\"启用\":\"1\",\"禁用\":\"2\"}}"
	 * @return 返回传入model类型的List数据
	 * @throws Exception
	 */
	public static <E> ArrayList<E> importExcel(File excelFile, String columnsJson, E model, String caseJson) throws Exception
	{
		HashMap anyTemp = null;
		if (caseJson != null && !"".equals(caseJson))
		{
			anyTemp = JSON.parseObject(caseJson, HashMap.class);
		}

		ArrayList<String> fields = new ArrayList<String>();
		HashMap<String, String> fieldColumns = new HashMap<String, String>();

		columnsJson = URLDecoder.decode(columnsJson, "UTF-8");

		JSONArray jsonArray = JSON.parseArray(columnsJson);

		for (int i = 0; i < jsonArray.size(); i++)
		{
			JSONObject o = jsonArray.getJSONObject(i);
			fieldColumns.put(o.getString("title"), o.getString("field"));
		}

		FileInputStream finput = new FileInputStream(excelFile);
		HSSFWorkbook wb = new HSSFWorkbook(finput);
		HSSFSheet sheet0 = wb.getSheetAt(0);

		HSSFRow row1 = sheet0.getRow(1);
		for (int i = 0; i < row1.getLastCellNum(); i++)
		{
			fields.add(fieldColumns.get(row1.getCell(i).getStringCellValue()));
		}

		ArrayList<E> models = new ArrayList<E>();

		for (int i = 2; i <= sheet0.getLastRowNum(); i++)
		{
			HSSFRow rowx = sheet0.getRow(i);
			if (rowx == null)
			{
				continue;
			}

			model = (E) model.getClass().newInstance();
			if (model instanceof Map)
			{
				for (int j = 0; j < rowx.getLastCellNum(); j++)
				{
					HSSFCell cellx = rowx.getCell(j);
					int cellType = cellx.getCellType();
					switch (cellType)
					{
					case Cell.CELL_TYPE_BLANK :
						((Map) model).put(fields.get(j), null);
						break;
					case Cell.CELL_TYPE_BOOLEAN :
						// 判断是不是enum值反向
						if (anyTemp != null)
						{
							Object temp = anyTemp.get(fields.get(j));
							if (temp != null)
							{
								JSONObject jo = JSON.parseObject(temp.toString());
								String tempValue = jo.getString(cellx.getBooleanCellValue() + "");
								if (tempValue != null && !"".equals(tempValue))
								{
									((Map) model).put(fields.get(j), tempValue);
								}
							}
						}
						else
						{
							((Map) model).put(fields.get(j), cellx.getBooleanCellValue());
						}
						break;
					case Cell.CELL_TYPE_NUMERIC :
						//百分比也被解析成了number类型
						if (DateUtil.isCellDateFormatted(cellx))
						{
							Date date = cellx.getDateCellValue();
							//								String value = new SimpleDateFormat("yyyy-MM-dd").format(date);
							((Map) model).put(fields.get(j), date);
						}
						else
						{
							((Map) model).put(fields.get(j), cellx.getNumericCellValue());
						}
						break;
					case Cell.CELL_TYPE_STRING :
						// 判断是不是double、int、百分比、enum值反向
						String value = cellx.getStringCellValue();

						if (anyTemp != null)
						{
							Object temp = anyTemp.get(fields.get(j));
							if (temp != null)
							{
								JSONObject jo = JSON.parseObject(temp.toString());
								String tempValue = jo.getString(value);
								if (tempValue != null && !"".equals(tempValue))
								{
									((Map) model).put(fields.get(j), tempValue);
								}
							}
							else
							{
								String doubleRegx = "^\\d+\\.\\d+$";
								String intRegx = "^\\d+$";
								//									String percentRegx = "^\\d+\\.\\d+%$|^\\d+%$";

								boolean isDouble = Pattern.matches(doubleRegx, value);
								boolean isInt = Pattern.matches(intRegx, value);
								//									boolean isPercent = Pattern.matches(percentRegx, value);

								if (isDouble)
								{
									try
									{
										Double db = Double.valueOf(value);
										((Map) model).put(fields.get(j), db);
									}
									catch (Exception e)
									{
										((Map) model).put(fields.get(j), value);
									}
								}
								else if (isInt)
								{
									try
									{
										Integer v = Integer.valueOf(value);
										((Map) model).put(fields.get(j), v);
									}
									catch (Exception e)
									{
										((Map) model).put(fields.get(j), value);
									}
								}
								/*
								 * else if(isPercent){
								 * try {
								 * value = value.substring(0, value.length()-1);
								 * Double db = Double.valueOf(value);
								 * ((Map)model).put(fields.get(j), db);
								 * } catch (Exception e) {
								 * ((Map)model).put(fields.get(j), value);
								 * }
								 * }
								 */
								else
								{
									((Map) model).put(fields.get(j), value);
								}

							}
						}
						else
						{
							String doubleRegx = "^\\d+\\.\\d+$";
							String intRegx = "^\\d+$";
							//								String percentRegx = "^\\d+\\.\\d+%$|^\\d+%$";

							boolean isDouble = Pattern.matches(doubleRegx, value);
							boolean isInt = Pattern.matches(intRegx, value);
							//								boolean isPercent = Pattern.matches(percentRegx, value);

							if (isDouble)
							{
								try
								{
									Double db = Double.valueOf(value);
									((Map) model).put(fields.get(j), db);
								}
								catch (Exception e)
								{
									((Map) model).put(fields.get(j), value);
								}
							}
							else if (isInt)
							{
								try
								{
									Integer v = Integer.valueOf(value);
									((Map) model).put(fields.get(j), v);
								}
								catch (Exception e)
								{
									((Map) model).put(fields.get(j), value);
								}
							}
							/*
							 * else if(isPercent){
							 * try {
							 * value = value.substring(0, value.length()-1);
							 * Double db = Double.valueOf(value);
							 * ((Map)model).put(fields.get(j), db);
							 * } catch (Exception e) {
							 * ((Map)model).put(fields.get(j), value);
							 * }
							 * }
							 */
							else
							{
								((Map) model).put(fields.get(j), value);
							}
						}
						break;
					}
				}
			}
			else
			{
				// javabean   通过反射来对对应的field设置值
				for (int j = 0; j < rowx.getLastCellNum(); j++)
				{
					HSSFCell cellx = rowx.getCell(j);
					int cellType = cellx.getCellType();
					switch (cellType)
					{
					case Cell.CELL_TYPE_BLANK :
						refeltFieldSet(model, fields.get(j), null);
						break;
					case Cell.CELL_TYPE_BOOLEAN :
						// 判断是不是enum值反向
						if (anyTemp != null)
						{
							Object temp = anyTemp.get(fields.get(j));
							if (temp != null)
							{
								JSONObject jo = JSON.parseObject(temp.toString());
								String tempValue = jo.getString(cellx.getBooleanCellValue() + "");
								if (tempValue != null && !"".equals(tempValue))
								{
									refeltFieldSet(model, fields.get(j), tempValue);
								}
							}
						}
						else
						{
							refeltFieldSet(model, fields.get(j), cellx.getBooleanCellValue());
						}
						break;
					case Cell.CELL_TYPE_NUMERIC :
						//百分比也被解析成了number类型
						if (DateUtil.isCellDateFormatted(cellx))
						{
							Date date = cellx.getDateCellValue();
							refeltFieldSet(model, fields.get(j), date);
						}
						else
						{
							String regex = "^\\d+\\.0+$|^\\d+$";
							boolean isInt = Pattern.matches(regex, cellx.getNumericCellValue() + "");
							if (isInt)
							{
								String dvStr = cellx.getNumericCellValue() + "";
								int dotIndex = dvStr.indexOf(".");
								if (dotIndex != -1)
								{
									String subStr = dvStr.substring(0, dotIndex);
									refeltFieldSet(model, fields.get(j), Integer.parseInt(subStr));
								}
								else
								{
									refeltFieldSet(model, fields.get(j), Integer.parseInt(dvStr));
								}
							}
							else
							{
								refeltFieldSet(model, fields.get(j), cellx.getNumericCellValue());
							}
						}
						break;
					case Cell.CELL_TYPE_STRING :
						// 判断是不是double、int、百分比、enum值反向
						String value = cellx.getStringCellValue();

						if (anyTemp != null)
						{
							Object temp = anyTemp.get(fields.get(j));
							if (temp != null)
							{
								JSONObject jo = JSON.parseObject(temp.toString());
								String tempValue = jo.getString(value);
								if (tempValue != null && !"".equals(tempValue))
								{
									refeltFieldSet(model, fields.get(j), tempValue);
								}
							}
						}
						else
						{
							String doubleRegx = "^\\d+\\.\\d+$";
							String intRegx = "^\\d+$";

							boolean isDouble = Pattern.matches(doubleRegx, value);
							boolean isInt = Pattern.matches(intRegx, value);

							if (isDouble)
							{
								try
								{
									Double db = Double.valueOf(value);
									refeltFieldSet(model, fields.get(j), db);
								}
								catch (Exception e)
								{
									refeltFieldSet(model, fields.get(j), value);
								}
							}
							else if (isInt)
							{
								try
								{
									Integer v = Integer.valueOf(value);
									refeltFieldSet(model, fields.get(j), v);
								}
								catch (Exception e)
								{
									refeltFieldSet(model, fields.get(j), value);
								}
							}
							else
							{
								refeltFieldSet(model, fields.get(j), value);
							}
						}
						break;
					}
				}
			}
			models.add(model);
		}
		return models;
	}

	private static <M> Object refeltFieldGet(M model, String field)
	{
		Field[] fields = model.getClass().getDeclaredFields();
		for (Field f : fields)
		{
			f.setAccessible(true);
			if (field.equals(f.getName()))
			{
				//				Class<?> type = f.getType();
				try
				{
					return f.get(model);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;
	}

	private static <M> void refeltFieldSet(M model, String field, Object value)
	{
		Field[] fields = model.getClass().getDeclaredFields();
		for (Field f : fields)
		{
			f.setAccessible(true);
			if (field.equals(f.getName()))
			{
				try
				{
					f.set(model, value);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				break;
			}
		}
	}

	public static void main(String[] args)
	{
		/*NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(5);
		Double db = Double.valueOf(1.0 + "");
		String dbStr = format.format(db);
		System.out.println(dbStr);*/

		/*int i = Character.MAX_VALUE+1;
		System.out.println(i);*/

		int i = (int) Math.ceil(17429 / 65535f);
		System.out.println(i);
	}

}
