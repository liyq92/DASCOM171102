package com.dascom.OPadmin.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IAuthorityService;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.impl.AuthorityServImpl;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.OPadmin.util.SysUtil;
import com.dascom.ssmn.util.Constants;

public abstract class AdminFormAction extends FormAction {

	private static final Logger logger = Logger.getLogger(AdminFormAction.class);
	protected IOperatorLogService logServ = new OperatorLogServImpl();

	protected IAuthorityService authServ = new AuthorityServImpl();

	protected SysUtil sysUtil = new SysUtil();

	protected List<String> authedMethods = new ArrayList<String>();

	protected String permissionDenyView = "permissionDenyView";

	public AdminFormAction() {
		super();
	}

	protected boolean validateSession(HttpServletRequest req,
			HttpServletResponse res) {
		HttpSession session = req.getSession();
		
		TyadminOperators operator = (TyadminOperators) session
				.getAttribute(Constants.OPERATOR_IN_SESSION);

		logger.debug("check operator status in session");

		return operator != null;
	}

	
}
