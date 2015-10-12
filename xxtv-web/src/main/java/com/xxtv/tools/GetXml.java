package com.xxtv.tools;

import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.XMLOutputter;

/**
 * @author chenguiyong
 * @version 创建时间：2014-6-17 下午6:35:14
 *          类说明
 */
public class GetXml
{
	public void BuildXMLDoc() throws IOException, JDOMException
	{
		// 创建根节点 list;
		Element root = new Element("data");
		DocType docType = new DocType("plist", "-//Apple//DTD PLIST 1.0//EN", "http://www.apple.com/DTDs/PropertyList-1.0.dtd");

		// 根节点添加到文档中；
		Document Doc = new Document(root, docType);
		// 创建节点 users;
		Element elements11 = new Element("users");

		// 创建users节点下的user;
		Element elements12 = new Element("user");

		elements11.addContent(elements12);
		// 给 user 节点添加属性 id;
		//elements.setAttribute("id", "" + i);
		// 创建节点下的属性，同事也可以遍历创建
		elements12.addContent(new Element("a").setText("xuehui"));
		elements12.addContent(new Element("b").setText("28"));
		elements12.addContent(new Element("c").setText("Male"));
		elements12.addContent(new Element("d").setText("Male"));
		elements12.addContent(new Element("e").setText("Male"));
		// 给父节点list添加users子节点;
		root.addContent(elements11);

		// 创建节点 security;
		Element elements21 = new Element("s");

		elements21.addContent(new Element("m").setText("xuehui"));
		elements21.addContent(new Element("n").setText("28"));
		elements21.addContent(new Element("o").setText("Male"));
		// 给父节点list添加security子节点;
		root.addContent(elements21);
		XMLOutputter XMLOut = new XMLOutputter();
		// 输出 user.xml 文件；
		XMLOut.output(Doc, new FileOutputStream("d:/user.xml"));
	}

	public static void main(String[] args)
	{
		try
		{
			GetXml x = new GetXml();
			System.out.println("生成 mxl 文件...");
			x.BuildXMLDoc();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 创建xml文件
	 * 
	 * @Author lx 2014-6-18 下午2:34:48
	 * @param rootElement 根元素
	 * @param xmlPatch xml文件路径
	 * @param docType
	 */
	public static void createXml(Element rootElement, String xmlPatch, DocType docType)
	{
		try
		{
			Document doc = new Document(rootElement, docType);
			XMLOutputter out = new XMLOutputter();
			FileOutputStream fos = new FileOutputStream(xmlPatch);
			out.outputString(doc);
			out.output(doc, fos);
			fos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
