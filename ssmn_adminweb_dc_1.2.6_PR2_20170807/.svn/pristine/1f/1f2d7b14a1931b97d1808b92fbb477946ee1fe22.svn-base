package com.dascom.OPadmin.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.IOperatorService;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.OPadmin.service.impl.OperatorServImpl;
import com.dascom.ssmn.util.Constants;

public class OperatorDelAction extends AdminFormAction {
	private IOperatorService operatorServ = new OperatorServImpl();
	private static final Logger logger = Logger
			.getLogger(OperatorDelAction.class);
	private IOperatorLogService logServ = new OperatorLogServImpl();
	
	public OperatorDelAction() {
		super();
	}

	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {

		return processGet(mapping, form, req, res);
	}

	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		
		HttpSession session = req.getSession();
		TyadminOperators operator = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);
	       
		
		String openo = req.getParameter("opeNo");

		if (openo == null || "".equals(openo)) {
			return mapping.findForward(this.formView);
		}
		TyadminOperators operatorDeleted = operatorServ.lookForDetail(openo,(String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION));
		String rtCode=operatorServ.deleteOperator(operatorDeleted.getId());
    	
		if (!rtCode.equals(Constants.MESSAGE_DEL_OPERATOR_CUSSE)) {
			
			return mapping.findForward(this.errorView);
		} else {
			req.setAttribute("msg", Constants.MESSAGE_DEL_OPERATOR_CUSSE);
			logger.debug("删除操作员：" + openo);
			logServ.log(operator,Constants.LOG_TYPE_DEL_OPERATOR, "删除操作员：" + openo);
		}
		return super.processGet(mapping, form, req, res);
	}
}
