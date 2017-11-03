package com.dascom.OPadmin.action;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.IOperatorService;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.OPadmin.service.impl.OperatorServImpl;
import com.dascom.ssmn.util.Constants;
import com.dascom.ssmn.action.CookieUtil;

public class OperatorLoginAction extends Action {
	
	private IOperatorService operatorServ = new OperatorServImpl();
	private IOperatorLogService logServ = new OperatorLogServImpl();

	private static final Logger logger = Logger.getLogger(OperatorLoginAction.class);

	public OperatorLoginAction() {
		super();
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {				
		
		String name = req.getParameter("nameLogin");
		String password = req.getParameter("password");	
		String rempaw = req.getParameter("rempaw");
		req.setAttribute("rempaw", rempaw);
		if (name ==null || password==null  ) 
		{	
			return mapping.findForward("loginView");
		}
		if(rempaw !=null && rempaw.length()>0 && rempaw.equals("1"))//记住密码
		{
			CookieUtil.saveCookie(name, password, res);
		}else
		{
			//清空cookie
			CookieUtil.clearCookie(res);
		}
		
		String service = Constants.servicekey_dc;
		TyadminOperators ope = (TyadminOperators) req.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		//如果登录名字和缓存中的一样,则不登录,如果不一样,先删除缓存,再登录
		if(ope != null && ope.getId().getOpeno().equals(name))
			return mapping.findForward("frameView");
		else
		{//这里先删除一下
			req.getSession().removeAttribute(Constants.OPERATOR_IN_SESSION);
		}		
		
		String rtCode = operatorServ.checkOperatorLogin(name, password, service);
				
		if (rtCode.equals("")) {
			logger.debug("操作员" + name + "登陆系统。");
			TyadminOperators operator = operatorServ.lookForDetail(name,service);
			operator.setLoginName(name);
			
			req.getSession().setAttribute(Constants.OPERATOR_IN_SESSION,operator);
			req.getSession().setAttribute(Constants.SERVICEKEY_IN_SESSION, service);
			logServ.log(operator, Constants.LOG_TYPE_OPERATOR_LOGIN,"操作员" + name + "登陆系统。");
			return mapping.findForward("frameView");
		} else {
			req.setAttribute("name", name);
			req.setAttribute("password", password);
//			req.setAttribute("servicekey", service);			
			req.setAttribute("msg",rtCode );
			return mapping.findForward("loginView");
		}
	}
}
