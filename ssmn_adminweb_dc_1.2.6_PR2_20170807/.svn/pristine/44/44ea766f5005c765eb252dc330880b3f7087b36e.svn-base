package com.dascom.OPadmin.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.dascom.OPadmin.dao.IAdminOperator;
import com.dascom.OPadmin.dao.impl.AdminOperatorImpl;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.OPadmin.util.ExportExcel;
import com.dascom.OPadmin.util.Page;
import com.dascom.init.ConfigMgr;
import com.dascom.ssmn.util.Constants;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.dao.zydcUserDao;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.OPadmin.entity.TyadminOperatorLogs;

public class OperatorLoginSearchAction extends AdminFormAction {

	private IAdminOperator operatorDao = new AdminOperatorImpl();
	private LevelDao leDao = LevelDao.getInstance();
	private static final Logger logger = Logger
			.getLogger(OperatorLoginSearchAction.class);
	private IOperatorLogService logServ = new OperatorLogServImpl();
	private zydcUserDao uDao = zydcUserDao.getInstance();

	
	public OperatorLoginSearchAction() {
		super();
	}

	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		int icount =0;
		HttpSession session = req.getSession();
		Long level = (Long)session.getAttribute("level");
		TyadminOperators operator = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);
		Long operalevelid = operator.getLevelid();
		SsmnZyLevel zylevel = null;

        String stropeno = req.getParameter("openo1");
        String starttime = req.getParameter("startdate");
        String endtime = req.getParameter("enddate");
        String sislogin = req.getParameter("islogin");
        String bdparam = (String) req.getParameter("bdbox1");//事业部
        String zoneparam = (String) req.getParameter("zonebox1");//战区
		String areaparam = (String) req.getParameter("areabox1");//片区
		String bagparam = (String) req.getParameter("bagbox1");//行动组
        String sopenoInfo = req.getParameter("openo2");//查详情参数
        String selectpagetemp = req.getParameter("pagetemp");//暂存当前页数
        String stype = req.getParameter("type");
        String slevelid = "";//获取操作员的级别，根据级别将条件选择下拉表置灰
        
        try {
			zylevel = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(operalevelid);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		String dischannel="0";//是否区分A+ 和渠道
		dischannel =ConfigMgr.getDistChannel();//是否区分 A+ 和渠道
		req.setAttribute("dischannel", dischannel);
		
        if(stype !=null && !"".equals(stype) && stype.equals("1"))//查详情
        {
        	String selectopeno1 = req.getParameter("selectopeno1");
        	String selectstartdate = req.getParameter("selectstartdate");
        	String selectenddate = req.getParameter("selectenddate");
        	String selectislogin = req.getParameter("selectislogin");
        	String selectbdparam = req.getParameter("selectbdparam");
        	
        	String temp = req.getParameter("page");
			Page p = sysUtil.setPageInfo(req, temp, 20);
        	List<TyadminOperatorLogs> loginfo = operatorDao.findOpenoLoginDetailByOpeno(sopenoInfo,selectstartdate,selectenddate,p.getFirstResult()-1,p.getSize());
        	icount = operatorDao.findOpenoLoginDetailByOpenoCount(sopenoInfo,selectstartdate,selectenddate);
        	Iterator<TyadminOperatorLogs> it = loginfo.iterator();
			while(it.hasNext())
			{
				TyadminOperatorLogs ip = it.next();
				if(ip.getBusinessdepartment() == null || ip.getBusinessdepartment().equals(""))
					ip.setBusinessdepartment("全部");
				if(ip.getWarzone() == null || ip.getWarzone().equals(""))
					ip.setWarzone("全部");
				if(ip.getArea() == null || ip.getArea().equals(""))
					ip.setArea("全部");
				if(ip.getBranchactiongroup() == null || ip.getBranchactiongroup().equals(""))
					ip.setBranchactiongroup("全部");

				//查找听录音次数,根据时间查次数
				String sendTim = operatorDao.getLastTime(ip.getLastLoginTime(),ip.getOpeno());
				if(ConfigMgr.getDistChannel().equals("1"))//是否区分 A+ 和渠道
				{
					//区分
					int countCdr = operatorDao.findCdrCount(ip.getOpeno(),ip.getLastLoginTime(),sendTim,0,"A+");
					ip.setCdrCountA(countCdr);
					
					int countCdrC = operatorDao.findCdrCount(ip.getOpeno(),ip.getLastLoginTime(),sendTim,0,"渠道");
					ip.setCdrCountChannel(countCdrC);
				}else
				{
					int countCdr = operatorDao.findCdrCount(ip.getOpeno(),ip.getLastLoginTime(),sendTim,0,"");
					ip.setCdrCountTotal(countCdr);
				}
				//备注次数
				int remarkc =operatorDao.findCdrCount(ip.getOpeno(),ip.getLastLoginTime(),sendTim,0, "备注次数");
				ip.setCdrRemarkCount(remarkc);
			}
			
			req.setAttribute("selectopeno1", selectopeno1);
			req.setAttribute("selectstartdate", selectstartdate);
			req.setAttribute("selectenddate", selectenddate);
			req.setAttribute("selectbdparam", selectbdparam);		
			req.setAttribute("selectislogin", selectislogin);
			req.setAttribute("openo2", sopenoInfo);
			req.setAttribute("loginfo", loginfo);
			req.setAttribute("page", p);
			req.setAttribute("recordCount", icount);
			
			return mapping.findForward("formDetailView");
			
        	
        }else
        {
			List<String> bdlist = new ArrayList<String>();
			List<String> wzlist = new ArrayList<String>();
			List<String> arealist = new ArrayList<String>();
			List<String> baglist = new ArrayList<String>();
			String opeBd = null;
			String opeZone = null;
			String opeArea = null;
			String opeBag = null;
			
			opeBd = zylevel.getBusinessdepartment();
			opeZone = zylevel.getWarzone();
			opeArea = zylevel.getArea();
			opeBag = zylevel.getBranchactiongroup();
			
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
					
			List<TyadminOperatorLogs> opeList=new ArrayList<TyadminOperatorLogs>();
			
			String temp = req.getParameter("page");
			Page p = sysUtil.setPageInfo(req, temp, 10);
			
			if((!"".equals(stropeno) && stropeno !=null) ||  
					(starttime !=null && !"".equals(starttime))
					|| (endtime !=null && !"".equals(endtime)) ||
					(sislogin !=null && !"0".equals(sislogin)) || (bdparam !=null && !"".equals(bdparam))
					|| (zoneparam !=null && !"".equals(zoneparam)) || (areaparam !=null && !"".equals(areaparam))
					|| (bagparam !=null && !"".equals(bagparam) ))
			{
				opeList = operatorDao.findOpenoLoginDetail(starttime, endtime, sislogin, bdparam,zoneparam,areaparam,bagparam, stropeno,zylevel, p.getFirstResult()-1, p.getSize());
			    icount = operatorDao.countOpenoLoginDetail(starttime, endtime, sislogin, bdparam,zoneparam,areaparam,bagparam, stropeno,zylevel);
	
			}

			Iterator<TyadminOperatorLogs> it = opeList.iterator();
			
			while(it.hasNext())
			{
				TyadminOperatorLogs ip = it.next();
				if(ip.getBusinessdepartment() == null || ip.getBusinessdepartment().equals(""))
					ip.setBusinessdepartment("全部");
				if(ip.getWarzone() == null || ip.getWarzone().equals(""))
					ip.setWarzone("全部");
				if(ip.getArea() == null || ip.getArea().equals(""))
					ip.setArea("全部");
				if(ip.getBranchactiongroup() == null || ip.getBranchactiongroup().equals(""))
					ip.setBranchactiongroup("全部");
				
				//有登录次数的才增查听录音的次数,没有的不去查
				if(ip.getLoginCount()>0)
				{
					if(ConfigMgr.getDistChannel().equals("1"))//是否区分 A+ 和渠道
					{
						//区分
						int countCdrA = operatorDao.findCdrCount(ip.getOpeno(),starttime,endtime,1,"听录音次数A+");
						ip.setCdrCountA(countCdrA);
						int countCdrCh = operatorDao.findCdrCount(ip.getOpeno(),starttime,endtime,1,"听录音次数渠道");
						ip.setCdrCountChannel(countCdrCh);
					}else
					{
						int countCdr = operatorDao.findCdrCount(ip.getOpeno(),starttime,endtime,1,"");
						ip.setCdrCountTotal(countCdr);
					}
					//查备注次数
					int remarkc =operatorDao.findCdrCount(ip.getOpeno(),starttime,endtime,1, "备注次数");
					ip.setCdrRemarkCount(remarkc);
				}
				
			}
			
			slevelid = new UtilFunctionDao().getTyadminOperatorLevelid(zylevel);
			req.setAttribute("openo1", stropeno);
			req.setAttribute("startdate", starttime);
			req.setAttribute("enddate", endtime);
			req.setAttribute("recordCount", icount+"");
			req.setAttribute("pagetemp", temp);
			req.setAttribute("bdparam", StringUtils.isBlank(bdparam)?opeBd:bdparam);
			req.setAttribute("zoneparam", StringUtils.isBlank(zoneparam)?opeZone:zoneparam);
			req.setAttribute("areaparam", StringUtils.isBlank(areaparam)?opeArea:areaparam);
			req.setAttribute("bagparam", StringUtils.isBlank(bagparam)?opeBag:bagparam);
			req.setAttribute("bdlist", bdlist);	
			req.setAttribute("wzlist", wzlist);
			req.setAttribute("arealist", arealist);
			req.setAttribute("baglist", baglist);
			req.setAttribute("islogin", sislogin);
			req.setAttribute("opelist", opeList);
			req.setAttribute("page", p);
			req.setAttribute("slevelid", slevelid);
			
			return mapping.findForward(this.formView);
        }		
		
	}

	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
        
		return this.processSubmit(mapping, form, req, res);
	}
	
	public String dateAdd1(String sDate)
	{
		String sres = "";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");/*** 加一天*/
		Calendar calendar=null;
		try {
			Date dd = df.parse(sDate);
			calendar = Calendar.getInstance();
			calendar.setTime(dd);
			calendar.add(Calendar.DAY_OF_MONTH, 1);//加一天
			System.out.println("增加一天之后：" + df.format(calendar.getTime()));
			sres = df.format(calendar.getTime());
			} catch (ParseException e) 
			{e.printStackTrace();}/*** 减一天*/
		return sres;
	}
}
