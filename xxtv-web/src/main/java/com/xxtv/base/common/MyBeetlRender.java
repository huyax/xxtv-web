package com.xxtv.base.common;

import org.beetl.core.GroupTemplate;
import org.beetl.ext.jfinal.BeetlRender;

import com.xxtv.tools.SysConstants;

public class MyBeetlRender extends BeetlRender
{

	private String baseView;

	public MyBeetlRender(GroupTemplate gt, String view)
	{
		super(gt, view);
		baseView = view.replaceAll(".html", "");
		//		gt.getSharedVars().put("display", new DisplayDirective());
		//		getConfiguration().setSharedVariable("display", new DisplayDirective());
	}

	@Override
	public void render()
	{
		// 静态资源版本（缓存控制）
		request.setAttribute("v", "?v=" + System.currentTimeMillis());
		request.setAttribute("base", request.getContextPath());
		request.setAttribute("url", request.getRequestURI().replaceAll(request.getContextPath(), ""));
		request.setAttribute("debug", SysConstants.DEBUG);
		request.setAttribute("isCompress", SysConstants.HTML_IS_COMPRESS);
		// 用户信息
		request.setAttribute("user", request.getSession().getAttribute(SysConstants.SESSION_USER));
		// JS CSS文件自动引入
		request.setAttribute("jsPath", ResCache.getResJsPath(baseView));
		request.setAttribute("cssPath", ResCache.getResCssPath(baseView));
		super.render();
	}

}
