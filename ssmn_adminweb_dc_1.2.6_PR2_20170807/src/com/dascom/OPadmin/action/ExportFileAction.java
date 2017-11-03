package com.dascom.OPadmin.action;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;



import com.dascom.OPadmin.entity.TyadminLogs;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.OPadmin.util.ExportExcel;
import com.dascom.ssmn.util.Constants;


public class ExportFileAction extends AdminFormAction {
	private static final Logger logger = Logger.getLogger(ExportFileAction.class);
	// --------------------------------------------------------- Methods
	private IOperatorLogService logServ = new OperatorLogServImpl();
	
	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		    
		HttpSession session = req.getSession();
	    TyadminOperators operator = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);
	    int filetitle=Integer.parseInt(req.getParameter("flag"));
	    
		req.setAttribute("operationType", logServ.searchLogType((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION)));
		String opeNo = "";
		if(filetitle == 1)
			opeNo = req.getParameter("opeNo");
		else
			opeNo = operator.getId().getOpeno();
		
	
		
		String logType=req.getParameter("logType");
		//String serviceKey = req.getParameter("serviceKey");
		String startTime = req.getParameter("startTime");
		String stopTime = req.getParameter("stopTime");
		
		Map<String,String> param = new HashMap<String,String>();
		param.put("opeNo", opeNo);
		param.put("logType", logType);
		param.put("startTime", startTime);
		param.put("stopTime", stopTime);
		
		this.sysUtil.setBackParam(req, param);
		int iCount = logServ.getTotalCount(opeNo.trim(),(String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),startTime, stopTime,logType);
		String[] title={"操作员","操作员组","操作时间","操作类型" ,"操作描述"};
		List<TyadminLogs> logList=logServ.searchAllLog(opeNo,(String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),startTime,stopTime,logType);

		String[][] data = new String[logList.size()][5];
		TyadminLogs book = null;
		String filename = null;
		for (int i = 0; i < logList.size(); i++) {
			book = (TyadminLogs)logList.get(i);
			data[i][0] = book.getOpeno();
			data[i][1] = book.getGroupName();
			
			DateFormat df = DateFormat.getDateTimeInstance();
			data[i][2]= df.format(book.getLogTime());
			data[i][3] = book.getLogType();
			data[i][4] = book.getLogDes();
		}

		if(filetitle==1){
			filename = "日志信息查询_";
		}else if(filetitle==2)
		{
			filename = new String(opeNo+"_");
			
		}
		try{
			ExportExcel.WebExportExcel(req, res, title, data, filename);
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		req.setAttribute("logList", logServ.searchAllLog(opeNo,(String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),startTime,stopTime,logType));
		req.setAttribute("recordCount", new Integer(iCount));
		String des = "导出" + opeNo + "从" + startTime + "到" + stopTime + "的" + logType + "操作日志信息";
		logger.info(des);
		this.logServ.log(operator, com.dascom.ssmn.util.Constants.LOG_TYPE_EXP_OPE_INFO, des);

		return mapping.findForward(this.formView);
	}
	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		
		req.setAttribute("operationType", logServ.searchLogType(""));
		return super.processGet(mapping, form, req, res);
	}
}

