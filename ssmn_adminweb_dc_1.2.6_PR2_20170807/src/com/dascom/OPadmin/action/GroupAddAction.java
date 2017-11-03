package com.dascom.OPadmin.action;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dascom.OPadmin.dao.IAdminAuthority;
import com.dascom.OPadmin.dao.impl.AdminAuthorityImpl;
import com.dascom.OPadmin.entity.TyadminGroups;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IGroupService;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.impl.GroupServImpl;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.OPadmin.util.ArrayTransfer;
import com.dascom.ssmn.util.Constants;

public class GroupAddAction extends AdminFormAction {
	private static final Logger logger = Logger.getLogger(GroupAddAction.class);

	private IGroupService groupServ = new GroupServImpl();
	private IAdminAuthority authDao = new AdminAuthorityImpl();
	private IOperatorLogService logServ = new OperatorLogServImpl();

	
	public GroupAddAction() {
		super();
	}

	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		
		HttpSession session = req.getSession();
		TyadminOperators operator = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);

		String gpName = req.getParameter("groupName");
		String desc = req.getParameter("description");
		String rank = "0";//req.getParameter("rank");
		
		
		String[] rights = req.getParameterValues("chk");	
		
		TyadminGroups group = new TyadminGroups();
		group.setGroupName(gpName);
		group.setDescription(desc);
		group.setRank(Long.valueOf(rank));		
		group.setServicekey((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION));
		group.setCreateDate(new Date());
		group.setOperatorId(operator.getId().getOpeno());
		
		if(rights == null){
			req.setAttribute("groupName", gpName);
			req.setAttribute("description", desc);
			//req.setAttribute("keyList", dicServ.getServiceKeyByOpe(operator.getServicekey()));
			req.setAttribute("msg","请选择权限组权限后保存!");
			return mapping.findForward(this.formView);
		}
		long[] authLong = new long[rights.length];
		
		ArrayTransfer.transArrayFromStr2Long(rights, authLong);
		String rtCode = groupServ.addGroup(group, authLong);
		if (rtCode.equals(Constants.MESSAGE_ADD_GROUP_CUSSE)) {
			logger.debug("增加权限组成功，组名：" + group.getGroupName());
			logServ.log(operator, Constants.LOG_TYPE_ADD_GROUP, "增加分组成功，组名：" + group.getGroupName());
			req.setAttribute("msg", "增加权限组成功，组名：" + group.getGroupName());
			return mapping.findForward("successView");
		} else if (rtCode.equals(Constants.MESSAGE_GROUP_EXIST)) {
			req.setAttribute("groupName", gpName);
			req.setAttribute("description", desc);
			logger.debug("权限组重复定义!组名:"+gpName);
			
			req.setAttribute("authList",authDao.findServiceAuths((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION)) );			
			req.setAttribute("groupAuthList", rights);
			req.setAttribute("msg","权限组重复定义!");
			return mapping.findForward(this.formView);
		} else {
			logger.info("增加分组失败，组名：" + group.getGroupName());
			req.setAttribute("msg", "增加分组失败，组名：" + group.getGroupName());
			return mapping.findForward(this.errorView);
		}
		
	}
	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		HttpSession session = req.getSession();
		req.setAttribute("authList", authDao.findServiceAuths((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION)));
		return super.processGet(mapping, form, req, res);
	}
}
