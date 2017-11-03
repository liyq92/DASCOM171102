package com.dascom.OPadmin.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.ssmn.util.Constants;

public class OperatorLogoutAction extends Action {
	private IOperatorLogService logServ = new OperatorLogServImpl();
	public OperatorLogoutAction() {
		super();
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		HttpSession session = req.getSession();
		Object o = session.getAttribute(Constants.OPERATOR_IN_SESSION);
		
		if (o == null){
			return mapping.findForward("indexView");
		}
		else{
			TyadminOperators ope = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);
			logServ.log(ope,Constants.LOG_TYPE_OPERATOR_LOGOUT, "操作员" + ope.getId().getOpeno() + "退出系统。");
			session.removeAttribute(Constants.OPERATOR_IN_SESSION);
			session.invalidate();
		}
		
		return mapping.findForward("loginView");
	}
}
