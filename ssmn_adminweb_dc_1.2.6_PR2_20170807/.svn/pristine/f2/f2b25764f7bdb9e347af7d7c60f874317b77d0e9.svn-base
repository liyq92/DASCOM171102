package com.dascom.OPadmin.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.IAdminAuthority;
import com.dascom.OPadmin.dao.IAdminOperator;
import com.dascom.OPadmin.dao.impl.AdminAuthorityImpl;
import com.dascom.OPadmin.dao.impl.AdminOperatorImpl;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.OPadmin.util.ExportExcel;
import com.dascom.OPadmin.util.Page;
import com.dascom.ssmn.util.Constants;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.dao.zydcUserDao;
import com.dascom.ssmn.entity.SsmnZyLevel;

public class OperatorSearchAction extends AdminFormAction {

	private IAdminOperator operatorDao = new AdminOperatorImpl();
	private LevelDao leDao = LevelDao.getInstance();
	private static final Logger logger = Logger
			.getLogger(OperatorSearchAction.class);
	private IOperatorLogService logServ = new OperatorLogServImpl();
	private zydcUserDao uDao = zydcUserDao.getInstance();
	public IAdminAuthority authDao = new AdminAuthorityImpl();

	private String[] title={"操作员帐号","真实姓名", "所属级别", "创建时间", "创建人"};

	public OperatorSearchAction() {
		super();
	}

	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		int isExport=0;
		int count;
		HttpSession session = req.getSession();
		Long level = (Long)session.getAttribute("level");
		TyadminOperators operator = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);
		List<String> authMethodList = authDao.findByGroupId(operator.getGroupId().longValue(),(String)req.getSession().getAttribute(Constants.SERVICEKEY_IN_SESSION));
		Long operalevelid = operator.getLevelid();
		SsmnZyLevel zylevel = null;
		String slevelid = "";//获取操作员的级别，根据级别将条件选择下拉表置灰

        String keyWord = req.getParameter("openo");

        String bdparam = (String) req.getParameter("bdbox1");//事业部
		String zoneparam = (String) req.getParameter("zonebox1");//战区
		String areaparam = (String) req.getParameter("areabox1");//片区
		String bagparam = (String) req.getParameter("bagbox1");//行动组
		
		List<String> bdlist = new ArrayList<String>();
		List<String> wzlist = new ArrayList<String>();
		List<String> arealist = new ArrayList<String>();
		List<String> baglist = new ArrayList<String>();
		
		String opeBd = null;
		String opeZone = null;
		String opeArea = null;
		String opeBag = null;
		
		try {
			zylevel = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(operalevelid);
			opeBd = zylevel.getBusinessdepartment();
			opeZone = zylevel.getWarzone();
			opeArea = zylevel.getArea();
			opeBag = zylevel.getBranchactiongroup();
		} catch (DaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//区域级别的名字是配置 的，这里要根据配置显示 
		String businName = new UtilFunctionDao().getLevelName(1, zylevel);//事业部
		String warName = new UtilFunctionDao().getLevelName(2, zylevel);//战区
		String areaName = new UtilFunctionDao().getLevelName(3, zylevel);//片区
		String branName = new UtilFunctionDao().getLevelName(4, zylevel);//行动组
		req.setAttribute("businName", businName);
		req.setAttribute("warName", warName);
		req.setAttribute("areaName", areaName);
		req.setAttribute("branName", branName);
		
		if (opeBd == null) { 
			// 操作员对应省份和公司下的所有事业部都可选
			bdlist = leDao.getLevelByTypeAndID(1, operalevelid);
		} else { // 只能选择与操作员对应的事业部
			bdlist.add(StringUtils.isBlank(bdparam)?opeBd:bdparam);
		}
		// 根据businessdepartment查出wzlist下级菜单
		if (opeZone == null) {
			wzlist = leDao.getSelectLevel(1, operalevelid, StringUtils.isBlank(bdparam)?opeBd:bdparam, null, null);
		} else { 
			wzlist.add(StringUtils.isBlank(zoneparam)?opeZone:zoneparam);
		}
		if (opeArea == null) {
			arealist = leDao.getSelectLevel(2, operalevelid, StringUtils.isBlank(bdparam)?opeBd:bdparam, 
					StringUtils.isBlank(zoneparam)?opeZone:zoneparam, null);
		} else {
			arealist.add(StringUtils.isBlank(areaparam)?opeArea:areaparam);
		}
		if (opeBag == null) {
			baglist = leDao.getSelectLevel(3, operalevelid, StringUtils.isBlank(bdparam)?opeBd:bdparam, 
					StringUtils.isBlank(zoneparam)?opeZone:zoneparam, StringUtils.isBlank(areaparam)?opeArea:areaparam);
		} else {
			baglist.add(StringUtils.isBlank(bagparam)?opeBag:bagparam);
		}
		
		logger.debug("操作员" + operator.getId().getOpeno() + "查询操作员，关键字：" + keyWord);
		List<TyadminOperators> opeList=new ArrayList<TyadminOperators>();
		
		String temp = req.getParameter("page");
		Page p = sysUtil.setPageInfo(req, temp, 10);
		
		if(("".equals(keyWord)||keyWord==null) && (bdparam==null || "".equals(bdparam)) && (zoneparam==null || "".equals(zoneparam)) && (areaparam==null || "".equals(areaparam)) && (bagparam==null || "".equals(bagparam)) )
		{
	        opeList = operatorDao.findByServiceKey((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),level.toString(),null ,null,null,null,null,zylevel,p.getFirstResult()-1, p.getSize());
	        count = operatorDao.findcountByServiceKey((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),level.toString(),null,null,null,null,null,zylevel);

		}
		else
		{
			opeList = operatorDao.findByServiceKey((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),level.toString(),keyWord,bdparam,zoneparam,areaparam,bagparam,zylevel, p.getFirstResult()-1, p.getSize());
		    count = operatorDao.findcountByServiceKey((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),level.toString(),keyWord,bdparam,zoneparam,areaparam,bagparam,zylevel);

		}
		if(opeList!=null && opeList.size()>0){
			isExport = 1;
		}
		//this.logServ.logSearchOperator(operator, keyWord);
		logServ.log(operator, Constants.LOG_TYPE_SEARCH_OPERATOR, "操作员" + operator.getId().getOpeno() + "查询操作员，关键字：" + keyWord);
		//将groupname 换成行动组名
		Iterator<TyadminOperators> it = opeList.iterator();
		while(it.hasNext())
		{
			TyadminOperators ip = it.next();
			if(ip.getId().getOpeno().equals(operator.getId().getOpeno()))
			{
				//自己是超级管理员，去掉
				it.remove();
				continue;
			}
				if(ip.getBranchactiongroup() == null || "".equals(ip.getBranchactiongroup()))
					ip.setBranchactiongroup("全部");
				if(ip.getArea() == null || "".equals(ip.getArea()))
					ip.setArea("全部");
				if(ip.getWarzone() == null || "".equals(ip.getWarzone()))
					ip.setWarzone("全部");
				if(ip.getBusinessdepartment() == null || "".equals(ip.getBusinessdepartment()))
					ip.setBusinessdepartment("全部");
				if(ip.getCompany() == null || "".equals(ip.getCompany()))
					ip.setCompany("全部");
			
		}
		slevelid = uDao.getoperalevel();
		req.setAttribute("openo1", keyWord);
		req.setAttribute("isExport", isExport);
		req.setAttribute("opelist", opeList);
		req.setAttribute("recordCount", count);
		req.setAttribute("pagetemp", temp);
		req.setAttribute("bdparam", StringUtils.isBlank(bdparam)?opeBd:bdparam);
		req.setAttribute("zoneparam", StringUtils.isBlank(zoneparam)?opeZone:zoneparam);
		req.setAttribute("areaparam", StringUtils.isBlank(areaparam)?opeArea:areaparam);
		req.setAttribute("bagparam", StringUtils.isBlank(bagparam)?opeBag:bagparam);
		req.setAttribute("bdlist", bdlist);
		req.setAttribute("wzlist", wzlist);
		req.setAttribute("arealist", arealist);
		req.setAttribute("baglist", baglist);
		req.setAttribute("slevelid", slevelid);
		req.setAttribute("authMethodList", authMethodList);
		

		String method = req.getParameter("method");
		if(method != null && !"".equals(method) && "exportcdr".equals(method)){
			List<TyadminOperators> List=new ArrayList<TyadminOperators>();
			if(("".equals(keyWord)||keyWord==null) && (bdparam==null || "".equals(bdparam)) && (zoneparam==null || "".equals(zoneparam)) && (areaparam==null || "".equals(areaparam)) && (bagparam==null || "".equals(bagparam)) )
			{
			    List = operatorDao.findByServiceKey((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),level.toString(),null,null,null,null,null,zylevel,p.getNextindex(),p.getSize());
			  
			}
			else
			{
				List = operatorDao.findByServiceKey((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),level.toString(),keyWord,bdparam,zoneparam,areaparam,bagparam,zylevel,p.getNextindex(),p.getSize());
			
			}
			
			Iterator<TyadminOperators> itlist = List.iterator();
			while(itlist.hasNext())
			{
				TyadminOperators ip = itlist.next();
				if(ip.getId().getOpeno().equals(operator.getId().getOpeno()))
				{
					//自己是超级管理员，去掉
					itlist.remove();
					continue;
				}
				if(ip.getBranchactiongroup() == null || "".equals(ip.getBranchactiongroup()))
					ip.setBranchactiongroup("全部");
				if(ip.getArea() == null || "".equals(ip.getArea()))
					ip.setArea("全部");
				if(ip.getWarzone() == null || "".equals(ip.getWarzone()))
					ip.setWarzone("全部");
				if(ip.getBusinessdepartment() == null || "".equals(ip.getBusinessdepartment()))
					ip.setBusinessdepartment("全部");
				if(ip.getCompany() == null || "".equals(ip.getCompany()))
					ip.setCompany("全部");
			}
		
		String[][] data = new String[List.size()][6];
		TyadminOperators cdr = null;
		for (int i = 0; i < List.size(); i++) {
			cdr = (TyadminOperators)List.get(i);
			
			data[i][0] = cdr.getId().getOpeno();
			data[i][4] = cdr.getAgentInfo();
			data[i][1] = cdr.getNote();
			data[i][2] = cdr.getCreate_timebystring();
			
			data[i][3] = cdr.getCreateUser();
		}
		
		String filename = "操作员名单";
		try{
			ExportExcel.WebExportExcel(req, res, title, data, filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		}
		
		//如果编辑页面传过来的,要添加提示信息
		String srtCode = req.getParameter("rtCode");
		if(srtCode !=null && !"".equals(srtCode))
			req.setAttribute("msg", srtCode);
		return mapping.findForward(this.formView);
	}

	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		
		HttpSession session = req.getSession();
		Long level = (Long)session.getAttribute("level");
		TyadminOperators operator = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);
		List<String> authMethodList = authDao.findByGroupId(operator.getGroupId().longValue(),(String)req.getSession().getAttribute(Constants.SERVICEKEY_IN_SESSION));
		String temp = req.getParameter("page");
		Page p = sysUtil.setPageInfo(req, temp, 10);
		List<TyadminOperators> opeList=new ArrayList<TyadminOperators>();
		req.setAttribute("isExport", 1);
		SsmnZyLevel zylevel = null;
		Long operalevelid = operator.getLevelid();
		String slevelid = "";//获取操作员的级别，根据级别将条件选择下拉表置灰
		String keyWord = req.getParameter("openo");

        
        String bdparam = (String) req.getParameter("bdbox1");//事业部
		String zoneparam = (String) req.getParameter("zonebox1");//战区
		String areaparam = (String) req.getParameter("areabox1");//片区
		String bagparam = (String) req.getParameter("bagbox1");//行动组
		
		List<String> bdlist = new ArrayList<String>();
		List<String> wzlist = new ArrayList<String>();
		List<String> arealist = new ArrayList<String>();
		List<String> baglist = new ArrayList<String>();
		
		String opeBd = null;
		String opeZone = null;
		String opeArea = null;
		String opeBag = null;
		
		try {
			zylevel = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(operalevelid);
			opeBd = zylevel.getBusinessdepartment();
			opeZone = zylevel.getWarzone();
			opeArea = zylevel.getArea();
			opeBag = zylevel.getBranchactiongroup();
		} catch (DaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//区域级别的名字是配置 的，这里要根据配置显示 
		String businName = new UtilFunctionDao().getLevelName(1, zylevel);//事业部
		String warName = new UtilFunctionDao().getLevelName(2, zylevel);//战区
		String areaName = new UtilFunctionDao().getLevelName(3, zylevel);//片区
		String branName = new UtilFunctionDao().getLevelName(4, zylevel);//行动组
		req.setAttribute("businName", businName);
		req.setAttribute("warName", warName);
		req.setAttribute("areaName", areaName);
		req.setAttribute("branName", branName);
		
		if (opeBd == null) { 
			// 操作员对应省份和公司下的所有事业部都可选
			bdlist = leDao.getLevelByTypeAndID(1, operalevelid);
		} else { // 只能选择与操作员对应的事业部
			bdlist.add(StringUtils.isBlank(bdparam)?opeBd:bdparam);
		}
		// 根据businessdepartment查出wzlist下级菜单
		if (opeZone == null) {
			wzlist = leDao.getSelectLevel(1, operalevelid, StringUtils.isBlank(bdparam)?opeBd:bdparam, null, null);
		} else { 
			wzlist.add(StringUtils.isBlank(zoneparam)?opeZone:zoneparam);
		}
		if (opeArea == null) {
			arealist = leDao.getSelectLevel(2, operalevelid, StringUtils.isBlank(bdparam)?opeBd:bdparam, 
					StringUtils.isBlank(zoneparam)?opeZone:zoneparam, null);
		} else {
			arealist.add(StringUtils.isBlank(areaparam)?opeArea:areaparam);
		}
		if (opeBag == null) {
			baglist = leDao.getSelectLevel(3, operalevelid, StringUtils.isBlank(bdparam)?opeBd:bdparam, 
					StringUtils.isBlank(zoneparam)?opeZone:zoneparam, StringUtils.isBlank(areaparam)?opeArea:areaparam);
		} else {
			baglist.add(StringUtils.isBlank(bagparam)?opeBag:bagparam);
		}
        
		opeList = operatorDao.findByServiceKey((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),level.toString(),keyWord,bdparam,zoneparam,areaparam,bagparam,zylevel,p.getFirstResult()-1, p.getSize());
       
		Iterator<TyadminOperators> it = opeList.iterator();
		while(it.hasNext())
		{
			TyadminOperators ip = it.next();
			if(ip.getId().getOpeno().equals(operator.getId().getOpeno()))
			{
				//自己是超级管理员，去掉
				it.remove();
				continue;
			}
			if(ip.getBranchactiongroup() == null || "".equals(ip.getBranchactiongroup()))
				ip.setBranchactiongroup("全部");
			if(ip.getArea() == null || "".equals(ip.getArea()))
				ip.setArea("全部");
			if(ip.getWarzone() == null || "".equals(ip.getWarzone()))
				ip.setWarzone("全部");
			if(ip.getBusinessdepartment() == null || "".equals(ip.getBusinessdepartment()))
				ip.setBusinessdepartment("全部");
			if(ip.getCompany() == null || "".equals(ip.getCompany()))
				ip.setCompany("全部");
		}
		slevelid = uDao.getoperalevel();
		req.setAttribute("opelist",opeList );
        int count = operatorDao.findcountByServiceKey((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),level.toString(),null,null,null,null,null,zylevel);
        req.setAttribute("recordCount", count-1);//要把自己减掉
        req.setAttribute("openo1", keyWord);
		req.setAttribute("pagetemp", temp);
		req.setAttribute("bdparam", StringUtils.isBlank(bdparam)?opeBd:bdparam);
		req.setAttribute("zoneparam", StringUtils.isBlank(zoneparam)?opeZone:zoneparam);
		req.setAttribute("areaparam", StringUtils.isBlank(areaparam)?opeArea:areaparam);
		req.setAttribute("bagparam", StringUtils.isBlank(bagparam)?opeBag:bagparam);
		req.setAttribute("bdlist", bdlist);
		req.setAttribute("wzlist", wzlist);
		req.setAttribute("arealist", arealist);
		req.setAttribute("baglist", baglist);
		req.setAttribute("slevelid", slevelid);
		req.setAttribute("authMethodList", authMethodList);
		
		
		return super.processGet(mapping, form, req, res);
	}
}
