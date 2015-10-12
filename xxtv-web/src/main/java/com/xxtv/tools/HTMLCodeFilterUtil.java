package com.xxtv.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 过滤输入的内容中包含的html标签（不能使用在富文本编辑框输入的内容）
 * <p>文件名称: HTMLCodeFilterUtil.java</p>
 * <p>文件描述: 本类描述</p>
 * <p>内容摘要: </p>
 * <p>其他说明: </p>
 * <p>完成日期：2015年9月17日</p>
 * <p>修改记录0：无</p>
 * @version 1.0
 * @author  曾芸杰
 */
public class HTMLCodeFilterUtil
{

	public static String HTMLCodeFilter(String inputString)
	{
		String htmlStr = inputString; // 含html标签的字符串   
		String textStr = "";

		try
		{
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>   
			// }   
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>   
			// }   
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式   
			String regEx_html1 = "<[^>]+";
			Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			Matcher m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签   

			Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			Matcher m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签   

			Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			Matcher m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签   

			Pattern p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
			Matcher m_html1 = p_html1.matcher(htmlStr);
			htmlStr = m_html1.replaceAll(""); // 过滤html标签   
			if (null == htmlStr || "".equals(htmlStr))
			{
				textStr = "null";
			}
			else
			{
				textStr = htmlStr;
			}

		}
		catch (Exception e)
		{
			System.err.println("HTMLCodeFilter: " + e.getMessage());
		}

		return textStr;// 返回文本字符串 
	}

	public static void main(String[] args)
	{
		String str = "<p><font size=\"2\"><span class=\" mce_class=\"font-size: 10.5pt\">"
				+ "依据绩abc效管理体系的规定，公司决定于</span><span class=\" mce_class=\"font-size: 10.5pt\">"
				+ "2008</span><span class=\" mce_class=\"font-size: 10.5pt\">年</span><span class=\" "
				+ "mce_class=\"font-size: 10.5pt\">12</span><span class=\" mce_class=\"font-size: 10.5pt\">"
				+ "月</span><span class=\" mce_class=\"font-size: 10.5pt\">22</span><span class=\" "
				+ "mce_class=\"font-size: 10.5pt\">日</span><span class=\" mce_class=\"font-size: 10.5pt\">"
				+ "\"-2009</span><span class=\" mce_class=\"font-size: 10.5pt\">年</span><span class=\" "
				+ "mce_class=\"font-size: 10.5pt\">1</span><span class=\" mce_class=\"font-size: 10.5pt\">"
				+ "月</span><span class=\" mce_class=\"font-size: 10.5pt\"> 23& </span><span class=\" "
				+ "mce_class=\"font-size: 10.5pt\">日期间进行</span><span class=\" mce_class=\"font-size: "
				+ "10.5pt\">2008</span><span class=\" mce_class=\"font-size: 10.5pt\">年年度绩效考评工作，" + "具体事项如下：<input/></span></font></p>";
		String string = HTMLCodeFilter(str);
		System.out.println(string);
	}
}
