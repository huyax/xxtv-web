package com.xxtv.base.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.xxtv.tools.SysConstants;

public class LoginInterceptor implements Interceptor
{

	//	private static final Logger LOG = Logger.getLogger(LoginInterceptor.class);

	@Override
	public void intercept(Invocation ai)
	{
		Controller controller = ai.getController();
		if (SysConstants.CONTROLLER_UNLIMIT_URL_LIST.contains(ai.getControllerKey())
				|| SysConstants.ACTION_UNLIMIT_URL_LIST.contains(ai.getActionKey()))
		{
			ai.invoke();
		}
		else
		{
			if (null != controller.getSession().getAttribute(SysConstants.SESSION_USER))
			{
				ai.invoke();
			}
			else
			{
				controller.redirect("/");
			}
		}
	}

}
