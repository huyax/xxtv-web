package com.xxtv.core.plugin.annotation;

import java.util.List;

import com.jfinal.config.Routes;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import com.xxtv.base.common.BaseController;
import com.xxtv.core.kit.ClassSearcherKit;

/**
 * 扫描Controller上的注解，绑定Controller和controllerKey
 * 
 */
public class ControlPlugin implements IPlugin
{

	protected final Logger	log	= Logger.getLogger(getClass());

	private Routes			me;

	public ControlPlugin(Routes me)
	{
		this.me = me;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public boolean start()
	{
		// 查询所有继承BaseController的类
		List<Class<? extends BaseController>> controllerClasses = ClassSearcherKit.of(BaseController.class).search();
		//		List<String> jars = new ArrayList<>();
		//		jars.add("littleant.jar");
		//		ToolClassSearcher.of(BaseController.class).includeAllJarsInLib(true).injars(jars).search();// 可以指定查找jar包，jar名称固定，避免扫描所有文件
		// 循环处理自动注册映射
		for (Class controller : controllerClasses)
		{
			// 获取注解对象
			Control control = (Control) controller.getAnnotation(Control.class);
			if (control == null)
			{
				log.error(controller.getName() + "继承了Controller，但是没有注解绑定映射路径");
				continue;
			}

			// 获取映射路径数组
			String[] controllerKeys = control.controllerKey();
			String viewPath = control.viewPath();
			for (String controllerKey : controllerKeys)
			{
				controllerKey = controllerKey.trim();
				if (controllerKey.equals(""))
				{
					log.error(controller.getName() + "注解错误，映射路径为空");
					continue;
				}
				// 注册映射
				me.add(controllerKey, controller, viewPath);
			}
		}
		return true;
	}

	@Override
	public boolean stop()
	{
		return true;
	}

}
