package com.dascom.OPadmin.action;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.OPadmin.util.Page;
import com.dascom.ssmn.util.Constants;


public class MyLogSearchAction extends AdminFormAction{
	private IOperatorLogService logServ = new OperatorLogServImpl();
	//private IServiceKey dicServ = new ServiceKeyImpl();
	private static final Logger logger = Logger
			.getLogger(MyLogSearchAction.class);
	public MyLogSearchAction(){
		super();
	}

	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res){
		
		HttpSession session = req.getSession();

	    TyadminOperators operator = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);
	    
		String logType = req.getParameter("logType");
		String startTime = req.getParameter("startTime");
		String stopTime = req.getParameter("stopTime");
		String opeNo=operator.getId().getOpeno();

		String temp = req.getParameter("page");
		
		Map<String,String> param = new HashMap<String,String>();
		param.put("opeNo", opeNo);
		param.put("logType", logType);
		param.put("startTime", startTime);
		param.put("stopTime", stopTime);
		Page p = this.sysUtil.setPageInfo(req, temp);
		this.sysUtil.setBackParam(req, param);
		
		int iCount = logServ.getTotalCount(opeNo.trim(),"",startTime, stopTime,logType);
		req.setAttribute("logList", logServ.searchLog(opeNo,"",startTime,stopTime,logType,"*",p));
		req.setAttribute("recordCount", new Integer(iCount));
		req.setAttribute("operationType", logServ.searchLogType(operator.getId().getServicekey()));
		logger.debug("page :" + temp);
		
		if(temp==null)
		{
			//this.logServ.myLogSchLog(operator,dicServ.getManagementDic().getDicvalue(),startTime,stopTime,logType);
			this.logServ.log(operator, Constants.LOG_TYPE_SCH_MYLOG, operator.getId().getOpeno() + "查询个人" + "从" + startTime + "到"
					+ stopTime + "的" + logType + "日志：");
		}
		
		return mapping.findForward(this.formView);

	} 

	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res){
		HttpSession session = req.getSession();

	    TyadminOperators operator = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);
		req.setAttribute("operationType", logServ.searchLogType(operator.getId().getServicekey()));
		req.setAttribute("recordCount", new Integer(0));
		return super.processGet(mapping, form, req, res);
	}

}
