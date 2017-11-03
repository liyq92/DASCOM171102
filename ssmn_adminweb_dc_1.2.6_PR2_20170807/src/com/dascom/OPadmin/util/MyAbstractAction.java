package com.dascom.OPadmin.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class MyAbstractAction extends Action 
{
	@Override
	public ActionForward execute(ActionMapping mapping,
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		request.setCharacterEncoding("utf-8");
		ActionForward actionForward = null;
		response.setContentType("text/html; charset=utf-8");
		try
		{			
			actionForward = this.hanleRequest(mapping, form, request, response);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		
		return actionForward;
	}
	
	protected abstract ActionForward hanleRequest(	ActionMapping mapping, 
													ActionForm form, 
													HttpServletRequest request, 
													HttpServletResponse response) 
													throws Exception ;

}
