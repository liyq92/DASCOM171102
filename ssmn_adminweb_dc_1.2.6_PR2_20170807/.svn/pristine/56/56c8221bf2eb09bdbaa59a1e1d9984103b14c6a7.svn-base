package com.dascom.OPadmin.action;

import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dascom.OPadmin.entity.TyadminGroups;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.IAdminAuthority;
import com.dascom.OPadmin.dao.IAdminGroup;
import com.dascom.OPadmin.dao.impl.AdminAuthorityImpl;
import com.dascom.OPadmin.dao.impl.AdminGroupImpl;

import com.dascom.ssmn.util.Constants;

public class MenuAction extends Action {
	
	public IAdminAuthority authDao = new AdminAuthorityImpl();
	private IAdminGroup groupDao = new AdminGroupImpl();
	
	public MenuAction() {
		super();
	}
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		
		TyadminOperators operator = (TyadminOperators) req.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);

		List<String> authMethodList = authDao.findByGroupId(operator.getGroupId().longValue(),(String)req.getSession().getAttribute(Constants.SERVICEKEY_IN_SESSION));
		req.getSession().setAttribute(Constants.AUTHOR_IN_SESSION, authMethodList);
		TyadminGroups group=null;
		try {
			group = (TyadminGroups)groupDao.getByPrimaryKey(operator.getGroupId());
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		req.getSession().setAttribute("openo", operator.getId().getOpeno());
		req.setAttribute("authMethodList", authMethodList);
		req.getSession().setAttribute("level", group.getRank());
		if(authMethodList.contains("auditmsg")){
			req.getSession().setAttribute("auditmsg", "1");
		} else {
			req.getSession().setAttribute("auditmsg", "0");
		}
		return mapping.findForward("leftmenu_dc3g");
	}

}
