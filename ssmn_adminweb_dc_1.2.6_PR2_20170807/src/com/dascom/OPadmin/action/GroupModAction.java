package com.dascom.OPadmin.action;

import java.io.UnsupportedEncodingException;
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
import com.dascom.OPadmin.service.IAuthorityService;
import com.dascom.OPadmin.service.IGroupService;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.impl.AuthorityServImpl;
import com.dascom.OPadmin.service.impl.GroupServImpl;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.OPadmin.util.ArrayTransfer;
import com.dascom.ssmn.util.Constants;

public class GroupModAction extends AdminFormAction {
	private static final Logger logger = Logger.getLogger(GroupModAction.class);

	private IGroupService groupServ = new GroupServImpl();
	private IAdminAuthority authDao = new AdminAuthorityImpl();
	private IAuthorityService authServ = new AuthorityServImpl();
	private IOperatorLogService logServ = new OperatorLogServImpl();

	public GroupModAction() {
		super();
	}

	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		
		HttpSession session = req.getSession();
		TyadminOperators operator = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);
	       
		String gpName = req.getParameter("groupName");
		String gpId = req.getParameter("groupId");
		String desc = req.getParameter("description");
		String rankstr ="0";// req.getParameter("rank");
		Long rank=null;

		if(rankstr!=null)
			rank=Long.valueOf(rankstr);

		String[] rights = req.getParameterValues("chk");
		Long groupId = new Long(gpId);
		
		TyadminGroups group = new TyadminGroups();
		group.setId(groupId);
		group.setGroupName(gpName);
		group.setDescription(desc);
		if(rank!=null)
			group.setRank(rank);
		group.setServicekey((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION));
		long[] authLong = new long[rights.length];
		ArrayTransfer.transArrayFromStr2Long(rights, authLong);
		

		String rtCode = groupServ.modGroup(group, authLong);
		if (rtCode.equals(Constants.MESSAGE_MOD_GROUP_CUSSE)) {
			logger.debug("修改权限组成功，组名：" + group.getGroupName());
			logServ.log(operator,Constants.LOG_TYPE_MOD_GROUP, "修改分组成功，组名：" + group.getGroupName());
			req.getSession().setAttribute("flag", "1");
			req.setAttribute("msg", "修改权限组成功，组名：" + group.getGroupName());
			return mapping.findForward("successView");
		} else if (rtCode.equals(Constants.MESSAGE_GROUP_EXIST)) {
			logger.debug("修改权限组名已存在，组名：" + group.getGroupName());
			req.setAttribute("groupName", gpName);
			req.setAttribute("description", desc);
			req.setAttribute("authid", rights);
			req.setAttribute("authList", authDao.findServiceAuths((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION)));
			req.setAttribute("msg", "修改权限组名已存在，组名：" + group.getGroupName());
			doSaveMessage(req, rtCode);
			return mapping.findForward(this.formView);
		} else {
			return mapping.findForward(this.errorView);
		}
	}

	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		HttpSession session = req.getSession();
		
		String gpId = req.getParameter("groupid");
		Long groupId = new Long(gpId);

		TyadminGroups group = groupServ.lookForDetail(groupId);
		req.setAttribute("group", group);
		req.setAttribute("groupAuthList", authServ.searchByGroupId(groupId));
		req.setAttribute("fullAuthList", authDao.findServiceAuths((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION)));
		return super.processGet(mapping, form, req, res);
	}
}
