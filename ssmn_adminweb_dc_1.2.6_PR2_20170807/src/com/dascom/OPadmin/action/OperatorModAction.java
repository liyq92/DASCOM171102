package com.dascom.OPadmin.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.IAdminGroup;
import com.dascom.OPadmin.dao.IAdminOperator;
import com.dascom.OPadmin.dao.impl.AdminGroupImpl;
import com.dascom.OPadmin.dao.impl.AdminOperatorImpl;
import com.dascom.OPadmin.entity.TyadminGroups;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.entity.TyadminOperatorsId;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.IOperatorService;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.OPadmin.service.impl.OperatorServImpl;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.util.Constants;


public class OperatorModAction extends AdminFormAction {
	private IOperatorService operatorServ = new OperatorServImpl();
	private IAdminGroup groupDao = new AdminGroupImpl();
	private LevelDao leveldao = LevelDao.getInstance();
	private IOperatorLogService logServ = new OperatorLogServImpl();
	private IAdminOperator operatorDao = new AdminOperatorImpl();

	public OperatorModAction() {
		super();
	}

	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		
		HttpSession session = req.getSession();
		Long level = (Long)session.getAttribute("level");
		TyadminOperators opera = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION); 
		String openo = req.getParameter("openomod");
		String openoOld = req.getParameter("openomodOld");//修改前的账号
		String opepwd = req.getParameter("opepwd");
		String agentinfo = req.getParameter("agentinfomod");
		String note = req.getParameter("note");
		String gpId = req.getParameter("groupid");
		String bdbox1 = req.getParameter("bdbox2");//事业部
		String zonebox1 = req.getParameter("zonebox2");//战区
		String areabox1 = req.getParameter("areabox2");//片区
		String bagbox1 = req.getParameter("bagbox2");//行动组
		String temp = req.getParameter("page");
		//查询条件传带
		String openo1 = req.getParameter("");
		
		List<TyadminGroups> grouplist = groupDao.findAllGroups((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),level);
		//去掉超极管理员
		Iterator<TyadminGroups> it = grouplist.iterator();
		while(it.hasNext())
		{
			TyadminGroups gr = it.next();
			if(gr.getGroupName() != null && gr.getGroupName().trim().equals("超级管理员"))
				it.remove();
		}
		
		String bdparam = req.getParameter("bdbox2");//事业部
		String zoneparam = req.getParameter("zonebox2");//战区
		String areaparam = req.getParameter("areabox2");//片区
		String bagparam = req.getParameter("bagbox2");//行动组
		
		//查询条件参数传带
		String sopen1 = req.getParameter("seopeno1");
		String selectbdbox1 = req.getParameter("selectbdbox1");
		String selectzonebox1 = req.getParameter("selectzonebox1");
		String selectareabox1 = req.getParameter("selectareabox1");
		String selectbagbox1 = req.getParameter("selectbagbox1");
		String pagetemp = req.getParameter("pagetemp");
		
		List<String> bdlist = new ArrayList<String>();//事业部
		List<String> wzlist = new ArrayList<String>();//战区
		List<String> arealist = new ArrayList<String>();//片区
		List<String> baglist = new ArrayList<String>();//行动组
		String opeBd = null;
		String opeZone = null;
		String opeArea = null;
		String opeBag = null;
		SsmnZyLevel leveld = null;
		Long levelid = null;
		SsmnZyLevel szlevel = null;
		String rtCode= "";
		
		try {
			szlevel = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
			leveld = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
			opeBd = leveld.getBusinessdepartment();
			opeZone = leveld.getWarzone();
			opeArea = leveld.getArea();
			opeBag = leveld.getBranchactiongroup();
		} catch (DaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//区域级别的名字是配置 的，这里要根据配置显示 
		String businName = new UtilFunctionDao().getLevelName(1, szlevel);//事业部
		String warName = new UtilFunctionDao().getLevelName(2, szlevel);//战区
		String areaName = new UtilFunctionDao().getLevelName(3, szlevel);//片区
		String branName = new UtilFunctionDao().getLevelName(4, szlevel);//行动组
		req.setAttribute("businName", businName);
		req.setAttribute("warName", warName);
		req.setAttribute("areaName", areaName);
		req.setAttribute("branName", branName);
		
		//获取区域列表
		// 操作员对应省份和公司下的所有事业部都可选
		bdlist = leveldao.getLevelByTypeAndID(1, opera.getLevelid());
		
		// 根据businessdepartment查出wzlist下级菜单
		
		wzlist = leveldao.getSelectLevel(1, opera.getLevelid(), StringUtils.isBlank(bdparam)?opeBd:bdparam, null, null);
	
		arealist = leveldao.getSelectLevel(2, opera.getLevelid(), StringUtils.isBlank(bdparam)?opeBd:bdparam, 
				StringUtils.isBlank(zoneparam)?opeZone:zoneparam, null);
	
		baglist = leveldao.getSelectLevel(3, opera.getLevelid(), StringUtils.isBlank(bdparam)?opeBd:bdparam, 
				StringUtils.isBlank(zoneparam)?opeZone:zoneparam, StringUtils.isBlank(areaparam)?opeArea:areaparam);
		
		//如果账号名字有修改,需要判断操作员账号是否存在,如果存在,直接返回操作员已经存在
		TyadminOperators operMod =null;
		int modeFlag = 2;//0:账号名字没有发生变化 1:名字发生变化,但不存在,可以修改 2:名字发生变化,已经存在,不可修改
		if(openo !=null && !"".equals(openo) && !openo.equals(openoOld)) 
		{
			List<TyadminOperators> tempList = operatorDao.findByOpeno(openo,(String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION));
			if(tempList.size() > 0)
			{
				operMod = tempList.get(0);
				rtCode = Constants.MESSAGE_OPERATOR_EXIST;
				modeFlag = 2;//名字有变化,并且已经存在
			}else
				modeFlag=1;//名字有变化,并且不存在
		}
		else
			modeFlag = 0;//账号名字没有做修改
	
		if(modeFlag !=2)//执行修改操作
		{
			levelid = leveldao.getLevelidByarea(bdbox1, zonebox1, areabox1, bagbox1);
			boolean issaveok = true;
			if(levelid == null)
			{
				//创建该区域，并返回id 
				//获取最大的id值，因为数据是手工导入的，没法用序列
				//Long lid = leveldao.getMaxLevelidByarea();
				SsmnZyLevel zl = new SsmnZyLevel();
				//zl.setId(lid);
				zl.setProvincecity(szlevel.getProvincecity());
				zl.setCompany(szlevel.getCompany());
				if(bdbox1 == null || "".equals(bdbox1))
					zl.setBusinessdepartment("");
				else
					zl.setBusinessdepartment(bdbox1);
				if(zonebox1 == null || "".equals(zonebox1))
					zl.setWarzone("");
				else
					zl.setWarzone(zonebox1);
				if(areabox1 == null || "".equals(areabox1))
					zl.setArea("");
				else
					zl.setArea(areabox1);
				if(bagbox1 == null || "".equals(bagbox1))
					zl.setBranchactiongroup("");
				else
					zl.setBranchactiongroup(bagbox1);
				issaveok =leveldao.saveLevel(zl);
				//levelid = lid;
				levelid = leveldao.getLevelidByarea(bdbox1, zonebox1, areabox1, bagbox1);
			}
	
			operMod = new TyadminOperators();
			
			if(issaveok && levelid != null)
			{
				TyadminOperatorsId operModId =null;
				if(modeFlag ==1)//名字有变化,并且不存在,先按旧的名字查出来
					operModId =new TyadminOperatorsId(openoOld,(String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION));
				else ////账号名字没有做修改
					operModId =new TyadminOperatorsId(openo,(String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION));
				operMod.setId(operModId);		
				operMod.setOpepwd(opepwd);
				operMod.setGroupId(new Long(gpId));		
				operMod.setAgentInfo(agentinfo);
				operMod.setNote(note);
				operMod.setLevelid(levelid);
				if(modeFlag ==1)//名字有变化,并且不存在
					rtCode = operatorServ.modOperator(operMod,openo);
				else
					rtCode = operatorServ.modOperator(operMod,"");//名字没有变化
			}
		}
		if (rtCode.equals(Constants.MESSAGE_MOD_OPERATOR_CUSSE)) {
			//logger.debug("修改操作员成功，帐号：" + operMod.getId().getOpeno() + "，分组："+ groupDao.searchByGroupId(operMod.getGroupId()).getGroupName());
			this.logServ.log(opera, Constants.LOG_TYPE_MOD_OPERATOR,"修改操作员成功，帐号：" + operMod.getId().getOpeno() + "，分组："+ groupDao.searchByGroupId(operMod.getGroupId()).getGroupName() );
			//req.setAttribute("openo", sopen1);
			//req.setAttribute("agentinfo", sagentinfo1);
			//req.setAttribute("page", pagetemp);
			String surl = "/operatorSearch.do?rtCode="+rtCode+"";
			if(sopen1 != null && !"".equals(sopen1))
				surl +="&openo="+sopen1;
			if(selectbdbox1 != null && !"".equals(selectbdbox1))
				surl +="&bdbox1="+selectbdbox1;
			if(selectzonebox1 != null && !"".equals(selectzonebox1))
				surl +="&zonebox1="+selectzonebox1;
			if(selectareabox1 != null && !"".equals(selectareabox1))
				surl +="&areabox1="+selectareabox1;
			if(selectbagbox1 != null && !"".equals(selectbagbox1))
				surl +="&bagbox1="+selectbagbox1;
			if(pagetemp != null && !"".equals(pagetemp))
				surl +="&page="+pagetemp;
			return new ActionForward (surl);
		} else if (rtCode.equals(Constants.MESSAGE_OPERATOR_EXIST)) {
			
			/*req.setAttribute("opermodify", operMod);
			req.setAttribute("grouplist",grouplist );
			req.setAttribute("bdparam", bdparam);
			req.setAttribute("zoneparam", zoneparam);
			req.setAttribute("areaparam", areaparam);
			req.setAttribute("bagparam", bagparam);
			req.setAttribute("bdlist", bdlist);
			req.setAttribute("wzlist", wzlist);
			req.setAttribute("arealist", arealist);
			req.setAttribute("baglist", baglist);
			req.setAttribute("groupId", operMod.getGroupId());
			req.setAttribute("openo1", sopen1);
			//req.setAttribute("seagentinfo1", sagentinfo1);
			req.setAttribute("pagetemp", pagetemp);
			doSaveMessage(req, rtCode);*/
			//已经存在,跳转到列表页面,这样做的原因是:opera这个是主键,如果修改后,多次点保存后,
			//程序已经认不出是哪个了,所以这里的处理是跳转到列表页面,把查询条件给返回回去,	
			String surl = "/operatorSearch.do?rtCode="+rtCode;
			if(sopen1 != null && !"".equals(sopen1))
				surl +="&openo="+sopen1;
			if(selectbdbox1 != null && !"".equals(selectbdbox1))
				surl +="&bdbox1="+selectbdbox1;
			if(selectzonebox1 != null && !"".equals(selectzonebox1))
				surl +="&zonebox1="+selectzonebox1;
			if(selectareabox1 != null && !"".equals(selectareabox1))
				surl +="&areabox1="+selectareabox1;
			if(selectbagbox1 != null && !"".equals(selectbagbox1))
				surl +="&bagbox1="+selectbagbox1;
			if(pagetemp != null && !"".equals(pagetemp))
				surl +="&page="+pagetemp;
			
			return new ActionForward (surl);
		} else {
			return mapping.findForward(this.errorView);
		}
	}
	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		HttpSession session = req.getSession();
		Long level = (Long)session.getAttribute("level");
		String openo = req.getParameter("opeNo");
		req.setAttribute("title", "修改操作员");
		TyadminOperators opera = (TyadminOperators)req.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		TyadminOperators operMod = operatorServ.lookForDetail(openo,(String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION));
		
		List<TyadminGroups> grouplist = groupDao.findAllGroups((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),level);
		//去掉超极管理员
		Iterator<TyadminGroups> it = grouplist.iterator();
		while(it.hasNext())
		{
			TyadminGroups gr = it.next();
			if(gr.getGroupName() != null && gr.getGroupName().trim().equals("超级管理员"))
				it.remove();
		}
		String temp = req.getParameter("page");
		String bdparam = req.getParameter("bdbox2");//事业部
		String zoneparam = req.getParameter("zonebox2");//战区
		String areaparam = req.getParameter("areabox2");//片区
		String bagparam = req.getParameter("bagbox2");//行动组
		
		//查询条件参数传带
		String sopen1 = req.getParameter("seopeno1");
		String selectbdbox1 = req.getParameter("selectbdbox1");
		String selectzonebox1 = req.getParameter("selectzonebox1");
		String selectareabox1 = req.getParameter("selectareabox1");
		String selectbagbox1 = req.getParameter("selectbagbox1");
		String pagetemp = req.getParameter("pagetemp");
		
		List<String> bdlist = new ArrayList<String>();//事业部
		List<String> wzlist = new ArrayList<String>();//战区
		List<String> arealist = new ArrayList<String>();//片区
		List<String> baglist = new ArrayList<String>();//行动组
		String opeBd = null;
		String opeZone = null;
		String opeArea = null;
		String opeBag = null;
		SsmnZyLevel leveld = null;
		
		try {
			leveld = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(operMod.getLevelid());
			opeBd = leveld.getBusinessdepartment();
			bdparam= leveld.getBusinessdepartment();
			opeZone = leveld.getWarzone();
			zoneparam= leveld.getWarzone();
			opeArea = leveld.getArea();
			areaparam = leveld.getArea();
			opeBag = leveld.getBranchactiongroup();
			bagparam = leveld.getBranchactiongroup();
		} catch (DaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//区域级别的名字是配置 的，这里要根据配置显示 
		String businName = new UtilFunctionDao().getLevelName(1, leveld);//事业部
		String warName = new UtilFunctionDao().getLevelName(2, leveld);//战区
		String areaName = new UtilFunctionDao().getLevelName(3, leveld);//片区
		String branName = new UtilFunctionDao().getLevelName(4, leveld);//行动组
		req.setAttribute("businName", businName);
		req.setAttribute("warName", warName);
		req.setAttribute("areaName", areaName);
		req.setAttribute("branName", branName);
		
		//获取区域列表
		//if (opeBd == null) { 
			// 操作员对应省份和公司下的所有事业部都可选
			bdlist = leveldao.getLevelByTypeAndID(1, opera.getLevelid());

			wzlist = leveldao.getSelectLevel(1, opera.getLevelid(), StringUtils.isBlank(bdparam)?opeBd:bdparam, null, null);

			arealist = leveldao.getSelectLevel(2, opera.getLevelid(), StringUtils.isBlank(bdparam)?opeBd:bdparam, 
					StringUtils.isBlank(zoneparam)?opeZone:zoneparam, null);

			baglist = leveldao.getSelectLevel(3, opera.getLevelid(), StringUtils.isBlank(bdparam)?opeBd:bdparam, 
					StringUtils.isBlank(zoneparam)?opeZone:zoneparam, StringUtils.isBlank(areaparam)?opeArea:areaparam);
	
		
		req.setAttribute("opermodify", operMod);
		req.setAttribute("grouplist",grouplist);
		req.setAttribute("bdparam", bdparam);
		req.setAttribute("zoneparam", zoneparam);
		req.setAttribute("areaparam", areaparam);
		req.setAttribute("bagparam", bagparam);
		req.setAttribute("bdlist", bdlist);
		req.setAttribute("wzlist", wzlist);
		req.setAttribute("arealist", arealist);
		req.setAttribute("baglist", baglist);
		req.setAttribute("groupId", operMod.getGroupId());
		req.setAttribute("seopeno1", sopen1);
		req.setAttribute("selectbdbox1", selectbdbox1);
		req.setAttribute("selectzonebox1", selectzonebox1);
		req.setAttribute("selectareabox1", selectareabox1);
		req.setAttribute("selectbagbox1", selectbagbox1);
		req.setAttribute("pagetemp", pagetemp);
		
		return super.processGet(mapping, form, req, res);
	}
}
