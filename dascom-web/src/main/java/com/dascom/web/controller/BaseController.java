package com.dascom.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dascom.web.bean.TyadminAuthorities;
import com.dascom.web.bean.TyadminOperators;
import com.dascom.web.service.TyadminOperatorsService;
import com.dascom.web.util.BaseConsts;
import com.dascom.web.util.CookieUtil;
import com.jfinal.core.Controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseController extends Controller {
	
	private TyadminOperatorsService toService = TyadminOperatorsService.tos;

	public void frameTop(){
		HttpServletRequest req = getRequest();
		TyadminOperators ope = (TyadminOperators) req.getSession().getAttribute(BaseConsts.OPERATOR_IN_SESSION);
		req.setAttribute("ope", ope);
		render("/jsp/frame/top-main.jsp");
	}
	public void leftMenu(){
		HttpServletRequest req = getRequest();
		String service = BaseConsts.SERVICE_KEY;
		TyadminOperators ope = (TyadminOperators) req.getSession().getAttribute(BaseConsts.OPERATOR_IN_SESSION);
		List<TyadminAuthorities> findMethodList = toService.findMethodList(ope.getGroup_Id(),service);
		
		List<String > authMethodList = new ArrayList<String>();
		for (TyadminAuthorities string : findMethodList) {
			
			authMethodList.add(string.getAuth_method());
		}
		req.setAttribute("authMethodList", authMethodList);
		render("/jsp/frame/leftmain_ssmn_dc.jsp");
		return ;
	}
	public void frameMain(){
		render("/jsp/frame/main.jsp");
	}

	public void operatorLogin(){
		
		HttpServletRequest req = getRequest();
		HttpServletResponse res = getResponse();
		
		String name = req.getParameter("nameLogin");
		String password = req.getParameter("password");	
		String rempaw = req.getParameter("rempaw");
		req.setAttribute("rempaw", rempaw);
		
		if (name ==null || password==null  ) 
		{	
			render("/jsp/login.jsp");
			return ;
		}
		if(rempaw !=null && rempaw.length()>0 && rempaw.equals("1"))//记住密码
		{
			CookieUtil.saveCookie(name, password, res);
		}else
		{
			//清空cookie
			CookieUtil.clearCookie(res);
		}
		
		String service = BaseConsts.SERVICE_KEY;
		TyadminOperators ope = (TyadminOperators) req.getSession().getAttribute(BaseConsts.OPERATOR_IN_SESSION);
		//如果登录名字和缓存中的一样,则不登录,如果不一样,先删除缓存,再登录
		if(ope != null && ope.getOpeno().equals(name))
			render("/jsp/login.jsp");
		else
		{//这里先删除一下
			req.getSession().removeAttribute(BaseConsts.OPERATOR_IN_SESSION);
		}		
		
		log.info("登录信息：" + name +"||" +password + "||" +service);
		
		TyadminOperators rtCode = null;
		try {
			rtCode = toService.checkOperatorLogin(name, password, service);
			log.info(rtCode.toString());		
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != rtCode) {
			log.debug("操作员" + name + "登陆系统。");
			
			
			req.getSession().setAttribute(BaseConsts.OPERATOR_IN_SESSION,rtCode);
			req.getSession().setAttribute(BaseConsts.SERVICEKEY_IN_SESSION, service);
			//logServ.log(operator, BaseConsts.LOG_TYPE_OPERATOR_LOGIN,"操作员" + name + "登陆系统。");
			
			render("/jsp/frame/frame.jsp");
			return ;
		} else {
			req.setAttribute("name", name);
			req.setAttribute("password", password);
//			req.setAttribute("servicekey", service);			
			req.setAttribute("msg",rtCode );
			render("/jsp/login.jsp");
		}
		
		render("/jsp/login.jsp");
	}

	public void index(){
		render("/index.jsp");
	}
}
