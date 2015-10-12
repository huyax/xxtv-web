package com.xxtv.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.beetl.core.BeetlKit;

public class CodeGeneratorTools
{

	public static void main(String[] args)
	{
		String[] tableArr = {"app_version"};
		String[] classNameArr = {"AppVersionModel"};
		String[] classNameSmallArr = {"appVersion"};

		String srcFolder = "src";
		String packageBase = "com.xxtv.web";

		for (int i = 0; i < tableArr.length; i++)
		{
			String tableName = tableArr[i];
			String className = classNameArr[i];
			String classNameSmall = classNameSmallArr[i];
			//			validator(srcFolder, packageBase, className, classNameSmall);
			model(srcFolder, packageBase, className, classNameSmall, tableName);
			//			controller(srcFolder, packageBase, className, classNameSmall);
		}
		System.out.println("自动生成代码完成");
	}

	/**
	 * xxx.sql.xml
	 * 
	 * @param srcFolder
	 * @param packageBase
	 * @param classNameSmall
	 */
	public static void sql(String srcFolder, String packageBase, String classNameSmall)
	{
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String packages = packageBase + ".model";
		paraMap.put("namespace", srcFolder + "." + classNameSmall);

		String filePath = System.getProperty("user.dir") + "/" + srcFolder + "/" + packages.replace(".", "/") + "/" + classNameSmall + ".sql.xml";
		readFile("sql.html", paraMap, filePath);
	}

	/**
	 * 生成Controller
	 * 
	 * @param packageBase
	 */
	public static void controller(String srcFolder, String packageBase, String className, String classNameSmall)
	{
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String packages = packageBase + ".controller";
		paraMap.put("basePackage", packageBase);
		paraMap.put("package", packages);
		paraMap.put("className", className);
		paraMap.put("classNameSmall", classNameSmall);

		String filePath = System.getProperty("user.dir") + "/" + srcFolder + "/" + packages.replace(".", "/") + "/" + className + "Controller.java";
		readFile("controller.html", paraMap, filePath);
	}

	/**
	 * 生成Model
	 * 
	 * @param packageBase
	 */
	public static void model(String srcFolder, String packageBase, String className, String classNameSmall, String tableName)
	{
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String packages = packageBase + ".model";
		paraMap.put("package", packages);
		paraMap.put("className", className);
		paraMap.put("tableName", tableName);

		String filePath = System.getProperty("user.dir") + "/" + srcFolder + "/" + packages.replace(".", "/") + "/" + className + ".java";
		readFile("model.html", paraMap, filePath);
	}

	/**
	 * 生成Service
	 * 
	 * @param packageBase
	 */
	public static void service(String srcFolder, String packageBase, String className, String classNameSmall)
	{
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String packages = packageBase + ".service";
		paraMap.put("package", packages);
		paraMap.put("className", className);
		paraMap.put("classNameSmall", classNameSmall);

		String filePath = System.getProperty("user.dir") + "/" + srcFolder + "/" + packages.replace(".", "/") + "/" + className + "Service.java";
		readFile("service.html", paraMap, filePath);
	}

	/**
	 * 生成validator
	 * 
	 * @param packageBase
	 */
	public static void validator(String srcFolder, String packageBase, String className, String classNameSmall)
	{
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String packages = packageBase + ".validator";
		paraMap.put("package", packages);
		paraMap.put("className", className);
		paraMap.put("classNameSmall", classNameSmall);

		String filePath = System.getProperty("user.dir") + "/" + srcFolder + "/" + packages.replace(".", "/") + "/" + className + "Validator.java";
		readFile("validator.html", paraMap, filePath);
	}

	/**
	 * 生成
	 * 
	 * @param templateFileName
	 * @param paraMap
	 * @param filePath
	 */
	public static void readFile(String templateFileName, Map<String, Object> paraMap, String filePath)
	{
		try
		{
			Class<?> classes = Class.forName("com.yingmob.tools.CodeGeneratorTools");

			InputStream controllerInputStream = classes.getResourceAsStream(templateFileName);
			int count = 0;
			while (count == 0)
			{
				count = controllerInputStream.available();
			}

			byte[] bytes = new byte[count];
			int readCount = 0; // 已经成功读取的字节的个数
			while (readCount < count)
			{
				readCount += controllerInputStream.read(bytes, readCount, count - readCount);
			}

			String template = new String(bytes);

			String javaSrc = BeetlKit.render(template, paraMap);

			File file = new File(filePath);
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(javaSrc);
			output.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
