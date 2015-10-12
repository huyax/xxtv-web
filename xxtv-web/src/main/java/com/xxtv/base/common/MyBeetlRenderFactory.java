package com.xxtv.base.common;

import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.jfinal.render.Render;

public class MyBeetlRenderFactory extends BeetlRenderFactory
{

	@Override
	public Render getRender(String view)
	{
		return new MyBeetlRender(groupTemplate, view + getViewExtension());
	}

	@Override
	public String getViewExtension()
	{
		return ".html";
	}

}
