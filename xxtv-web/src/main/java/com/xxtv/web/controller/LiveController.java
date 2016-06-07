package com.xxtv.web.controller;

import com.xxtv.base.common.BaseController;
import com.xxtv.core.plugin.annotation.Control;

@Control(controllerKey = "/live")
public class LiveController extends BaseController
{

	public void douyu()
	{
		setAttr("menu", "live");
		String nick = getPara("nick");
		setAttr("uid", getPara("uid"));
		setAttr("nick", nick);
		setAttr("id", getPara("id"));
		render("live/douyu");
	}
	public void panda()
	{
		setAttr("menu", "live");
		String nick = getPara("nick");
		setAttr("pic", getPara("pic"));
		setAttr("nick", nick);
		setAttr("id", getPara("id"));
		render("live/panda");
	}
}
