package com.dascom.OPadmin.action;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.OPadmin.util.Page;
import com.dascom.ssmn.util.Constants;


public class LogSearchAction extends AdminFormAction {


	private IOperatorLogService logServ = new OperatorLogServImpl();
	// --------------------------------------------------------- Methods

	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {

		//req.setAttribute("operationType", logServ.searchLogType(""));
	    HttpSession session = req.getSession();
	    TyadminOperators operator = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);
		
	    
		String opeNo = req.getParameter("opeNo");		
		String logType=req.getParameter("logType");
		String startTime = req.getParameter("startTime");
		String stopTime = req.getParameter("stopTime");
		String temp = req.getParameter("page");
        
		Map<String,String> param = new HashMap<String,String>();
		param.put("opeNo", opeNo);
		param.put("logType", logType);
		param.put("startTime", startTime);
		param.put("stopTime", stopTime);
		Page p = this.sysUtil.setPageInfo(req, temp);
		
		this.sysUtil.setBackParam(req, param);
		
		int iCount = logServ.getTotalCount(opeNo,operator.getId().getServicekey(),startTime, stopTime,logType);
		req.setAttribute("logList", logServ.searchLog(opeNo,operator.getId().getServicekey(),startTime,stopTime,logType,"*",p));
		req.setAttribute("recordCount", new Integer(iCount));
		req.setAttribute("operationType", logServ.searchLogType(""));
		if(temp==null)
		{
			this.logServ.log(operator, Constants.LOG_TYPE_SCH_LOG, operator.getId().getOpeno() + "查询" + opeNo + "从" + startTime
					+ "到" + stopTime + "的" + logType + "日志：");
		}
		return mapping.findForward(this.formView);
	}
	
	
	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {

		req.setAttribute("operationType", logServ.searchLogType(""));
		req.setAttribute("recordCount", new Integer(0));
		return super.processGet(mapping, form, req, res);
	}
}
