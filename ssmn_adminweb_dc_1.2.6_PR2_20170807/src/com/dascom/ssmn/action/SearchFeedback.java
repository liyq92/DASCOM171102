package com.dascom.ssmn.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dascom.OPadmin.action.AdminFormAction;
import com.dascom.OPadmin.util.ExportExcel;
import com.dascom.OPadmin.util.Page;
import com.dascom.ssmn.dao.zydcUserDao;
import com.dascom.ssmn.entity.SsmnZyCancelUser;
import com.dascom.ssmn.entity.SsmnZyChannel;
import com.dascom.ssmn.entity.SsmnEnablenumber;
import com.dascom.ssmn.entity.SsmnZyFeedback;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.ssmn.entity.ZydcRecord;
import com.dascom.OPadmin.dao.IAdminAuthority;
import com.dascom.OPadmin.dao.IAdminOperator;
import com.dascom.OPadmin.dao.impl.AdminAuthorityImpl;
import com.dascom.OPadmin.dao.impl.AdminOperatorImpl;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.ssmn.util.Constants;
import com.dascom.ssmn.dao.FeedBackDao;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.UtilFunctionDao;

public class SearchFeedback extends AdminFormAction{
	private static final Logger logger = Logger.getLogger(SearchFeedback.class);
	private zydcUserDao uDao = zydcUserDao.getInstance();
	private FeedBackDao fDao = FeedBackDao.getInstance();
	private LevelDao lDao = LevelDao.getInstance();
	private IAdminOperator operatorDao = new AdminOperatorImpl();
	private IAdminAuthority authDao = new AdminAuthorityImpl();
		
	
	@Override
	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		TyadminOperators opera = (TyadminOperators)req.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		//权限不能从session中取，因为权限功能，随时要改变（在权限管理中）
		List<String> authMethodList = authDao.findByGroupId(opera.getGroupId().longValue(),(String)req.getSession().getAttribute(Constants.SERVICEKEY_IN_SESSION));
		String fcontent = req.getParameter("feedcontent");
		String starttime = req.getParameter("startdate");
		String endtime = req.getParameter("enddate");
		int recordCount = 0;
		String stype = req.getParameter("type");
		
		String temp = req.getParameter("page");
		Page p = sysUtil.setPageInfo(req, temp, Constants.RECORD_PER_PAGE);
		SsmnZyLevel  operabranc = lDao.getBranchactiongroupNameById(opera.getLevelid()+"");
		
		String branName = new UtilFunctionDao().getLevelName(4, operabranc);//行动组
		req.setAttribute("branName", branName);
		
		//添加反馈内容
		if(stype != null && stype.equals("addview"))
		{
			
			if(operabranc != null)
			{
				String sbranch =  getArea(operabranc);
				
				req.setAttribute("branch", sbranch);
			}
			req.setAttribute("opera", opera.getId().getOpeno());
			req.setAttribute("feedcontent", fcontent);
		    req.setAttribute("startdate", starttime);
		    req.setAttribute("enddate", endtime);
			return mapping.findForward("formAddView");
		}
		//添加
		if(stype != null && stype.equals("add"))
		{
			String sopear = req.getParameter("opera");
			String sname = req.getParameter("uname");
			String smsisdn = req.getParameter("umsisdn");
			String sfcontent = req.getParameter("fcontent");
			SsmnZyFeedback zyfb = new SsmnZyFeedback();
			zyfb.setName(sname);
			zyfb.setOpeno(sopear);
			zyfb.setMsisdn(smsisdn);
			zyfb.setFeedbackcontent(sfcontent);
	
			boolean isok = fDao.addFeedback(zyfb);
			if(isok)
				req.setAttribute("msg", "添加成功!");
			else
				req.setAttribute("msg", "添加失败!");
		}
		//删除
		if(stype != null && stype.equals("delete"))
		{
			String uid = req.getParameter("uid");
			if(uid != null && !"".equals(uid))
			{
				boolean isok = fDao.deleFeedback(uid);
				if(isok)
					req.setAttribute("msg", "删除成功!");
				else
					req.setAttribute("msg", "删除失败!");
			}
		}
		
		List<SsmnZyFeedback> flist = fDao.getFeedbackList(fcontent,starttime,endtime,p.getFirstResult()-1,p.getSize(),opera.getId().getServicekey(),opera.getId().getOpeno(),opera.getLevelid());
		for(int i = 0;i<flist.size();i++)
		{
			SsmnZyFeedback zy = flist.get(i);
			List<TyadminOperators> tyop = operatorDao.findByOpeno(zy.getOpeno(), opera.getId().getServicekey());
			if(tyop != null && tyop.size()>0)
			{
				SsmnZyLevel  op = lDao.getBranchactiongroupNameById(tyop.get(0).getLevelid()+"");
				String sbr = getArea(op);
				flist.get(i).setBranchactiongroup(sbr);
			}
		}
		
		
		recordCount = fDao.getFeedbackCount(fcontent,starttime,endtime,opera.getId().getServicekey(),opera.getId().getOpeno(),opera.getLevelid());
		
	    req.setAttribute("flist",flist);
	    req.setAttribute("page", p);
	    req.setAttribute("recordCount", recordCount);
	    req.setAttribute("feedcontent", fcontent);
	    req.setAttribute("startdate", starttime);
	    req.setAttribute("enddate", endtime);
	  
		return mapping.findForward(super.formView);
	}
	
	
	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {

		return this.processSubmit(mapping, form, req, res);
	}
	
	
	public String getArea(SsmnZyLevel operabran)
	{
		String sbranch = "";
	
		if(operabran != null)
		{
			
			if(operabran.getProvincecity() != null && !"".equals(operabran.getProvincecity()) && (operabran.getCompany() == null || "".equals(operabran.getCompany())))//省级
			{
				 
				sbranch =operabran.getProvincecity();
			} 
			if(operabran.getProvincecity() != null && !"".equals(operabran.getProvincecity()) && operabran.getCompany() != null && !"".equals(operabran.getCompany()) && 
					(operabran.getBusinessdepartment() == null || "".equals(operabran.getBusinessdepartment())))//公司级
			{
				sbranch =operabran.getCompany();
			}
			if(operabran.getProvincecity() != null && !"".equals(operabran.getProvincecity()) 
					&& operabran.getCompany() != null && !"".equals(operabran.getCompany()) && 
					operabran.getBusinessdepartment() != null && !"".equals(operabran.getBusinessdepartment())
					&& (operabran.getWarzone() == null || "".equals(operabran.getWarzone())))//事业部级
			{
				sbranch=operabran.getBusinessdepartment();
			}
			if(operabran.getProvincecity() != null && !"".equals(operabran.getProvincecity()) 
					&& operabran.getCompany() != null && !"".equals(operabran.getCompany()) && 
					operabran.getBusinessdepartment() != null && !"".equals(operabran.getBusinessdepartment())
					&& operabran.getWarzone() != null && !"".equals(operabran.getWarzone())
					&& (operabran.getArea() == null || "".equals(operabran.getArea())))//战区级
			{
				sbranch=operabran.getWarzone();
			}
			if(operabran.getProvincecity() != null && !"".equals(operabran.getProvincecity()) 
					&& operabran.getCompany() != null && !"".equals(operabran.getCompany()) && 
					operabran.getBusinessdepartment() != null && !"".equals(operabran.getBusinessdepartment())
					&& operabran.getWarzone() != null && !"".equals(operabran.getWarzone()) && 
					operabran.getArea() != null && !"".equals(operabran.getArea()) 
					&& (operabran.getBranchactiongroup() == null || "".equals(operabran.getBranchactiongroup())))//片区级
			{
				sbranch=operabran.getArea();
			}
			if(operabran.getProvincecity() != null && !"".equals(operabran.getProvincecity()) 
					&& operabran.getCompany() != null && !"".equals(operabran.getCompany()) && 
					operabran.getBusinessdepartment() != null && !"".equals(operabran.getBusinessdepartment())
					&& operabran.getWarzone() != null && !"".equals(operabran.getWarzone()) && 
					operabran.getArea() != null && !"".equals(operabran.getArea())
					&& operabran.getBranchactiongroup() != null && !"".equals(operabran.getBranchactiongroup()))//行动组级 ...写的好恶心...
			{
				sbranch=operabran.getBranchactiongroup();
			}
		}
	return sbranch;
	}
}
