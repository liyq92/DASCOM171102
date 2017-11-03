package com.dascom.OPadmin.action;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dascom.OPadmin.dao.IAdminGroup;
import com.dascom.OPadmin.dao.impl.AdminGroupImpl;
import com.dascom.OPadmin.entity.TyadminGroups;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.OPadmin.util.ExportExcel;
import com.dascom.ssmn.util.Constants;

public class GroupSearchAction extends AdminFormAction {
	private static final Logger logger = Logger
			.getLogger(GroupSearchAction.class);

	private IAdminGroup groupDao = new AdminGroupImpl();
	private IOperatorLogService logServ = new OperatorLogServImpl();
	public GroupSearchAction() {
		super();
	}
	private String[] title={"组名","组描述", "创建时间", "创建人"};
	
	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {

		HttpSession session = req.getSession();
		TyadminOperators operator = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);
		Long level = (Long)session.getAttribute("level");
		
		int isExport=0;
		String keyWord = req.getParameter("groupName").trim();
		logger.debug("操作员" + operator.getId().getOpeno() + "查询分组，关键字：" + keyWord);
		List<TyadminGroups> gpList = new ArrayList<TyadminGroups>();
		if("".equals(keyWord)||keyWord==null)
		{			
			gpList = groupDao.findAllGroups(operator.getId().getServicekey(),level);
		}
		else
		{
			gpList = groupDao.findByGroupName(keyWord, operator.getId().getServicekey(),level);
		}
		this.logServ.log(operator, Constants.LOG_TYPE_SEARCH_GROUP, "操作员" + operator.getId().getOpeno() + "查询分组，关键字：" + keyWord);
		
		if(gpList!=null && gpList.size()>0){
			isExport = 1;
		}
		req.setAttribute("groupList", gpList);
		req.setAttribute("groupName", keyWord);
		req.setAttribute("isExport", isExport);
		
		/*String method = req.getParameter("method");
		if(method != null && !"".equals(method) && "exportcdr".equals(method)){
			String[][] data = new String[gpList.size()][4];
			TyadminGroups cdr = null;
			for (int i = 0; i < gpList.size(); i++) {
				cdr = (TyadminGroups)gpList.get(i);
				
				data[i][0] = cdr.getGroupName();
				data[i][1] = cdr.getDescription();
				data[i][2] = cdr.getCreateDateByString();
				data[i][3] = cdr.getOperatorId();
			}
			
			String filename = "群组_"+new SimpleDateFormat("yyyyMMdd").format(new Date());
			try{
				ExportExcel.WebExportExcel(req, res, title, data, filename);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
			
			
		}*/
		
		return mapping.findForward(this.formView);
	}

	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		
		HttpSession session = req.getSession();
		TyadminOperators operator = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);
		Long level = (Long)session.getAttribute("level");
		
		req.setAttribute("isExport", 1);
		req.setAttribute("groupList", groupDao.findAllGroups(operator.getId().getServicekey(),level));
		return super.processGet(mapping, form, req, res);
		
	}

}
