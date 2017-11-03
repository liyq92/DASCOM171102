package com.dascom.OPadmin.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dascom.OPadmin.dao.impl.AdminGroupImpl;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.entity.TyadminOperatorsId;
import com.dascom.OPadmin.service.IOperatorService;
import com.dascom.OPadmin.service.impl.OperatorServImpl;
import com.dascom.OPadmin.util.Md5PasswordEncoder;
import com.dascom.ssmn.util.Constants;

public class ReginfoModAction extends AdminFormAction {
	private IOperatorService operatorServ = new OperatorServImpl();
	private AdminGroupImpl groupDao = new AdminGroupImpl();
	private static final Logger logger = Logger.getLogger(OperatorModAction.class);

	public ReginfoModAction() {
		super();
	}

	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
	    HttpSession session = req.getSession();
	    
	    TyadminOperators operator = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);
	    Long level = (Long)session.getAttribute("level");
	    
		String openo = req.getParameter("openo");
		String opepwd = req.getParameter("opepwd");
		String oldopepwd = req.getParameter("oldopepwd");
		String pswdConfirm = req.getParameter("pswdConfirm");
		
		if(!operator.getOpepwd().equals(new Md5PasswordEncoder().encodePassword(oldopepwd, 12345))){
			req.setAttribute("msg", "原始密码错误,请重新输入！");
			req.setAttribute("opermodify",operatorServ.lookForDetail(openo,(String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION))); 
			return mapping.findForward(this.formView);
		}
		
		if (!opepwd.equals(pswdConfirm)) {
			this.doSaveMessage(req, Constants.MESSAGE_DIFF_PASSWORD);
			req.setAttribute("openo", openo);
			req.setAttribute("grouplist",groupDao.findAllGroups(operator.getId().getServicekey(),level));
			req.setAttribute("opermodify",operatorServ.lookForDetail(openo,(String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION))); 
			return mapping.findForward(this.formView);
		}
		       
		TyadminOperators operMod = new TyadminOperators();
		TyadminOperatorsId operId = new TyadminOperatorsId();
		operId.setOpeno(openo);
		operId.setServicekey((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION));
		operMod.setId(operId);
		operMod.setOpepwd(opepwd);
		operMod.setGroupId(operator.getGroupId());
		operMod.setAgentInfo(operator.getAgentInfo());
		operMod.setNote(operator.getNote());
		
		
		//operMod.setServicekey(operator.getServicekey());
		req.setAttribute("opermodify", operMod);
	
		String rtCode = operatorServ.modOperator(operMod,"");
		//this.doSaveMessage(req, Constants.MESSAGE_MOD_OPERATOR_CUSSE);
		
		if (rtCode.equals(Constants.MESSAGE_MOD_OPERATOR_CUSSE)) {
			logger.debug("修改操作员成功，帐号：" + operMod.getId().getOpeno() + "，分组："
					+ groupDao.searchByGroupId(operMod.getGroupId()).getGroupName());
			//this.logServ.logModOperator(operMod, operator, operMod);
				operator.setOpepwd(new Md5PasswordEncoder().encodePassword(opepwd, "12345"));
				req.getSession().setAttribute(Constants.OPERATOR_IN_SESSION, operator);
				req.setAttribute("msg", Constants.MESSAGE_MOD_OPERATOR_CUSSE);
				return mapping.findForward(this.formView);
			} else if (rtCode.equals(Constants.MESSAGE_OPERATOR_EXIST)) {
				req.setAttribute("opermodify", operMod);
				req.getSession().setAttribute("aler", new Integer(1));
				req.setAttribute("grouplist",groupDao.findAllGroups(operator.getId().getServicekey(),level));
				req.setAttribute("msg", Constants.MESSAGE_OPERATOR_EXIST);
				return mapping.findForward(this.formView);
			} else {
				return mapping.findForward(this.errorView);
			}
		
	}

	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		
		HttpSession session = req.getSession();
		
		TyadminOperators operator = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);
	       
		req.getSession().setAttribute("aler", new Integer(0));
		String openo=operator.getId().getOpeno();
		req.setAttribute("title", "修改操作员");
		TyadminOperators operMod = operatorServ.lookForDetail(openo,(String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION));
		req.setAttribute("opermodify", operMod);
		return super.processGet(mapping, form, req, res);
	}
}
