package com.xxtv.web.controller;

import java.io.UnsupportedEncodingException;

import com.xxtv.base.common.BaseController;
import com.xxtv.core.plugin.annotation.Control;

@Control(controllerKey = "/live")
public class LiveController extends BaseController {

	public void index() {
		dota2();
	}

	public void dota2() {
		String nick = getPara("nick");
		try {
			nick = new String(nick.getBytes("iso8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setAttr("uid", getPara("uid"));
		setAttr("nick", nick);
		setAttr("id", getPara("id"));
		render("live/dota2");
	}

	public void lol() {
		String sNick = getPara("sNick");
		try {
			sNick = new String(sNick.getBytes("iso8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setAttr("lChannelId", getPara("lChannelId"));
		setAttr("sNick", sNick);
		setAttr("lSubchannel", getPara("lSubchannel"));
		setAttr("sAvatarUrl", getPara("sAvatarUrl"));
		render("live/lol");
	}
}
