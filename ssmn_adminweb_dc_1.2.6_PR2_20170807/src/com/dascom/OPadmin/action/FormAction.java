package com.dascom.OPadmin.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IAuthorityService;
import com.dascom.OPadmin.service.impl.AuthorityServImpl;
import com.dascom.ssmn.util.Constants;

public abstract class FormAction extends Action {

	private IAuthorityService authServ = new AuthorityServImpl();
	
	protected String formView = "formView";
	
	protected String errorView = "errorView";
	
	protected String indexView = "indexView";
	
	protected String successView = "successView";

	/**
	 * @return Returns the indexView.
	 */
	public String getIndexView() {
		return indexView;
	}

	/**
	 * @return Returns the errorView.
	 */
	public String getErrorView() {
		return errorView;
	}

	/**
	 * @param errorView The errorView to set.
	 */
	public void setErrorView(String errorView) {
		this.errorView = errorView;
	}

	/**
	 * @param indexView The indexView to set.
	 */
	public void setIndexView(String indexView) {
		this.indexView = indexView;
	}

	/**
	 * @return Returns the formView.
	 */
	public String getFormView() {
		return formView;
	}

	/**
	 * @param formView
	 *            The formView to set.
	 */
	public void setFormView(String formView) {
		this.formView = formView;
	}

	public FormAction() {
		super();
	}

	protected boolean isFormSubmit(HttpServletRequest req) {
		return "post".equalsIgnoreCase(req.getMethod());
	}

	
	/**
	 * 实现权限的集中控制
	 * @param req
	 * @return false:无权限; true:有权限.
	 */
	protected boolean hasAuth(HttpServletRequest req){
		boolean rtcode = false;
		HttpSession session = req.getSession();
		//TyadminOperators operator = new TyadminOperators();		
		TyadminOperators operator = (TyadminOperators) session
				.getAttribute(Constants.OPERATOR_IN_SESSION);
	
		Long groupId = operator.getGroupId();

		String reqpath = req.getServletPath().trim();

		int potindex = reqpath.indexOf(".");
		String subURI = reqpath.substring(1, potindex);
		
		
		if (Constants.COMMON_AUTHS.indexOf(subURI) >=0) {
			rtcode = true;
		} else {	
			if(authServ.hasAuthMethod(groupId,subURI)){
				rtcode = true;
			}
		}
		return rtcode;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		

		if(!validateSession(req, res)){
			req.getSession().invalidate();
			return mapping.findForward("timeOutView");
		}
		

		if(!hasAuth(req)){
			return mapping.findForward("autherrorView");
		}

		if (!isFormSubmit(req)) {
			return processGet(mapping, form, req, res);
		}
		return processSubmit(mapping, form, req, res);
	}

	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		return mapping.findForward(formView);
	}
	
	public void doSaveMessage(HttpServletRequest req, String key){
		if(key == null || "".equals(key)){
			return ;
		}
		ActionMessages messages = new ActionMessages();
		ActionMessage message = new ActionMessage(key);
		messages.add(ActionMessages.GLOBAL_MESSAGE,message);
		saveMessages(req, messages);
	}
	
	public void doSaveMessage(HttpServletRequest req, String key, Object[] values){
		if(key == null || "".equals(key)){
			return ;
		}
		ActionMessages messages = new ActionMessages();
		ActionMessage message = new ActionMessage(key, values);
		messages.add(ActionMessages.GLOBAL_MESSAGE,message);
		saveMessages(req, messages);
	}
	
	public void doSaveMessage(HttpSession session, String key){
		if(key == null || "".equals(key)){
			return ;
		}
		ActionMessages messages = new ActionMessages();
		ActionMessage message = new ActionMessage(key);
		messages.add(ActionMessages.GLOBAL_MESSAGE,message);
		saveMessages(session, messages);
	}

	public abstract ActionForward processSubmit(ActionMapping mapping,
			ActionForm form, HttpServletRequest req, HttpServletResponse res);
	
	protected abstract boolean validateSession(HttpServletRequest req, HttpServletResponse res);

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}
}
