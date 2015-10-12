package com.xxtv.core.config;

import org.beetl.core.GroupTemplate;
import org.beetl.ext.jfinal.BeetlRenderFactory;
import org.beetl.ext.tag.TrimHtml;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.xxtv.base.common.MyBeetlRenderFactory;
import com.xxtv.base.interceptor.LoginInterceptor;
import com.xxtv.core.plugin.annotation.ControlPlugin;
import com.xxtv.tools.DevConstants;
import com.xxtv.tools.SysConstants;

/**
 * API引导式配置
 */
public class SystemConfig extends JFinalConfig
{

	/**
	 * 配置常量
	 */
	@Override
	public void configConstant(Constants me)
	{
		// 加载少量必要配置，随后可用getProperty(...)获取值
		loadPropertyFile(DevConstants.DB_CONFIG_FILENAME);
		//		me.setMainRenderFactory(new MyFreeMarkerRenderFactory());
		me.setMainRenderFactory(new MyBeetlRenderFactory());
		GroupTemplate groupTemplate = BeetlRenderFactory.groupTemplate;
		groupTemplate.registerTag("compress", TrimHtml.class);
		me.setBaseViewPath("/pager/biz-logic");
		me.setDevMode(SysConstants.DEBUG);
	}

	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes me)
	{
		// 自动扫描Controller
		new ControlPlugin(me).start();
	}

	/**
	 * 配置插件
	 */
	@Override
	public void configPlugin(Plugins me)
	{
		// 配置缓存
		me.add(new EhCachePlugin());

	}

	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me)
	{
		// 登录权限拦截器
		//me.add(new LoginInterceptor());
	}

	/**
	 * 配置处理器
	 */
	@Override
	public void configHandler(Handlers me)
	{

	}

}
