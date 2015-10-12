package com.xxtv.base.common;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beetl.core.Template;
import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.xxtv.tools.ExcelTool4Jfinal;

public class BaseController extends Controller
{

	//	private static final Logger LOG = Logger.getLogger(BaseController.class);
	protected final String	DATA			= "d";

	protected final String	ERROR			= "e";

	protected final String	RESULT			= "r";

	protected final String	MESSAGE			= "m";

	private int				defaultPageSize	= 40;

	/**
	 * 创建分页对象
	 * 
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public Pager createPager(int pageSize, int pageNo)
	{
		Pager pager = null;
		if (pageSize <= 0)
		{
			pageSize = defaultPageSize;
		}
		if (pageNo <= 0)
		{
			pageNo = 1;
		}
		pager = new Pager(pageSize, pageNo);
		return pager;
	}

	/**
	 * 创建分页对象
	 * 
	 * @param request
	 * @return
	 */
	public Pager createPager()
	{
		Pager pager = null;
		String pageSize = getPara("rows");
		String pageNo = getPara("page");
		if (StrKit.isBlank(pageSize))
		{
			pageSize = String.valueOf(defaultPageSize);
		}
		if (StrKit.isBlank(pageNo))
		{
			pageNo = "1";
		}
		pager = new Pager(Integer.valueOf(pageSize), Integer.valueOf(pageNo));
		return pager;
	}

	public Pager createPager(String... params)
	{
		Pager pager = createPager();

		String param = null;
		for (String paramName : params)
		{
			param = getPara(paramName);
			if (!StrKit.isBlank(param))
			{
				pager.addParam(paramName, param);
			}
		}
		return pager;
	}

	/**
	 * 获取result Map对象
	 * 
	 * @return
	 */
	public Map<String, Object> getResultMap()
	{
		return new HashMap<String, Object>();
	}

	/**
	 * 生成表格数据格式
	 * 
	 * @param pager
	 * @return
	 */
	public Map<String, Object> getGridData(Pager pager)
	{
		Map<String, Object> result = getResultMap();
		result.put("total", pager.getTotalRecord());
		result.put("rows", pager.getResult());
		return result;
	}

	/**
	 * 获取用户真实ip地址
	 * request.getRemoteAddr(); nginx做跳转不能获取
	 * 
	 * @param request
	 * @return
	 */
	public static String getRealIpAddr(HttpServletRequest request)
	{
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("WL-Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getRemoteAddr();
		return ip;
	}

	public void renderExcel(String template)
	{

		renderExcel(template, template);

	}

	public void renderExcel(String template, String fileName)
	{

		Template t = BeetlRenderFactory.groupTemplate.getTemplate("template/" + template + ".html");

		HttpServletRequest request = getRequest();
		Enumeration<String> attrs = request.getAttributeNames();
		while (attrs.hasMoreElements())
		{
			String attrName = attrs.nextElement();
			t.binding(attrName, request.getAttribute(attrName));
		}

		try
		{
			HttpServletResponse response = getResponse();
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xls");
			response.setContentType("application/msexcel");
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
			t.renderTo(writer);
			writer.close();
			response.flushBuffer();
			renderNull();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			renderError(404);
		}
	}

	/**
	 * 通用导出excel
	 * 
	 * @param datas 数据对象是{map/Model/Record/javaBean}
	 * @param percentNoConvert 不需要转成百分比的字段 比如收入和支出
	 * @param caseJson 比如状态字段 "{\"state\":{\"1\":\"启用\",\"2\":\"禁用\"}}"
	 */
	@SuppressWarnings("all")
	public void renderXLS(List<?> datas, List<String> percentNoConvert, String caseJson)
	{
		ExcelTool4Jfinal.renderXLS(datas, percentNoConvert, caseJson, this);
	}

	/**
	 * 通用导入excel
	 * 
	 * @param model 数据对象类型是{map/Model/Record/javaBean}
	 * @param caseJson 反向 比如状态字段 "{\"state\":{\"1\":\"启用\",\"2\":\"禁用\"}}" 反向就是 "{\"state\":{\"启用\":\"1\",\"禁用\":\"2\"}}"
	 * @param nickNameTable 如果dao查询数据库用到了别名 页面上的filed也是用的别名 那这里要把对应关系{nickName,column}
	 * @return 返回传入model类型的List数据
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	public <E> List<E> importXLS(E model, String caseJson, Map<String, String> nickNameTable) throws Exception
	{
		return ExcelTool4Jfinal.importXLS(model, caseJson, this, nickNameTable);
	}
}
