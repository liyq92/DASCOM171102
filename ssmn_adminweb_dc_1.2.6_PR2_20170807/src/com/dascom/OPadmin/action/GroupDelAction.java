package com.dascom.OPadmin.action;



import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IGroupService;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.impl.GroupServImpl;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.ssmn.util.Constants;

public class GroupDelAction extends AdminFormAction {
	private static final Logger logger = Logger.getLogger(GroupDelAction.class);
	private IGroupService groupServ = new GroupServImpl();
	private IOperatorLogService logServ = new OperatorLogServImpl();
	
	public GroupDelAction() {
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
	       
		String gpId = req.getParameter("groupid");
		String gpname = req.getParameter("groupname");
		
		Long groupId = new Long(gpId);
		if (gpId == null || "".equals(gpId)) {
			return mapping.findForward(this.formView);
		}
		//String servicekey = groupServ.searchGroup(groupId).getServiceKey();
		String rtCode = groupServ.deleteGroup(groupId);
		if (!rtCode.equals(Constants.MESSAGE_DEL_GROUP_CUSSE)) {
			return mapping.findForward(this.errorView);
		}
		logger.debug("删除分组：" + gpname);
		logServ.log(operator,Constants.LOG_TYPE_DEL_GROUP, "删除分组：" + gpname);
		//this.logServ.logDelOpeGroup(operator, servicekey, gpname);
		return super.processGet(mapping, form, req, res);
	}

}
