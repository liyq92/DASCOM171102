package com.dascom.OPadmin.action;

import java.io.UnsupportedEncodingException;
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
import com.dascom.OPadmin.dao.IAdminGroup;
import com.dascom.OPadmin.dao.impl.AdminGroupImpl;
import com.dascom.OPadmin.entity.TyadminGroups;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.entity.TyadminOperatorsId;
import com.dascom.OPadmin.service.IGroupService;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.IOperatorService;
import com.dascom.OPadmin.service.impl.GroupServImpl;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.OPadmin.service.impl.OperatorServImpl;
import com.dascom.OPadmin.util.Md5PasswordEncoder;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.util.Constants;
public class OperatorAddAction extends AdminFormAction {

	public OperatorAddAction() {
		super();
	}

	private IOperatorService operatorServ = new OperatorServImpl();
	private IGroupService groupServ = new GroupServImpl();
	private IAdminGroup groupDao = new AdminGroupImpl();
	private IOperatorLogService logServ = new OperatorLogServImpl();
	private LevelDao leveldao = LevelDao.getInstance();
	
	private static final Logger logger = Logger
			.getLogger(OperatorAddAction.class);

	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		
		HttpSession session = req.getSession();
		Long level = (Long)session.getAttribute("level");
		TyadminOperators operator = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);
		String openo = req.getParameter("openo");
		String opepwd = req.getParameter("opepwd");
		String pswdConfirm = req.getParameter("pswdConfirm");
		String agentinfo = req.getParameter("agentinfo1");
		String note = req.getParameter("note");

		String gpid = req.getParameter("groupid");
		String bdbox1 = req.getParameter("bdbox1");//事业部
		String zonebox1 = req.getParameter("zonebox1");//战区
		String areabox1 = req.getParameter("areabox1");//片区
		String bagbox1 = req.getParameter("bagbox1");//行动组
		Long levelid = null;
		SsmnZyLevel szlevel = null;
		boolean issaveok = true;
		try {
			szlevel = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(operator.getLevelid());
			
			//区域级别的名字是配置 的，这里要根据配置显示 
			String businName = new UtilFunctionDao().getLevelName(1, szlevel);//事业部
			String warName = new UtilFunctionDao().getLevelName(2, szlevel);//战区
			String areaName = new UtilFunctionDao().getLevelName(3, szlevel);//片区
			String branName = new UtilFunctionDao().getLevelName(4, szlevel);//行动组
			req.setAttribute("businName", businName);
			req.setAttribute("warName", warName);
			req.setAttribute("areaName", areaName);
			req.setAttribute("branName", branName);
		
		//查到levelid

			levelid = leveldao.getLevelidByarea(bdbox1, zonebox1, areabox1, bagbox1);
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
				issaveok = leveldao.saveLevel(zl);
				//levelid = lid;
				levelid = leveldao.getLevelidByarea(bdbox1, zonebox1, areabox1, bagbox1);
			}
		
		
		} catch (DaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String create_USER = operator.getId().getOpeno();
		
		if (!opepwd.equals(pswdConfirm)) {
			req.setAttribute("msg","添加操员失败：密码不一致");
			req.setAttribute("openo", openo);
			req.setAttribute("agentinfo", agentinfo);
			req.setAttribute("note", note);
			req.setAttribute("groupId", gpid);
			req.setAttribute("bdparam", bdbox1);
			req.setAttribute("zoneparam", zonebox1);
			req.setAttribute("areaparam", areabox1);
			req.setAttribute("bagparam", bagbox1);
			
			List<TyadminGroups> grouplist = groupDao.findAllGroups((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),level);
			//去掉超极管理员
			Iterator<TyadminGroups> it = grouplist.iterator();
			while(it.hasNext())
			{
				TyadminGroups gr = it.next();
				if(gr.getGroupName() != null && gr.getGroupName().trim().equals("超级管理员"))
					it.remove();
			}
			req.setAttribute("grouplist",grouplist );
			return mapping.findForward(this.formView);
		}

		
		Date create_Time = new Date();
		String rtCode = "";
		TyadminOperators operAdded = new TyadminOperators();
		if(levelid != null && issaveok)
		{
			
			TyadminOperatorsId operAddedId = new TyadminOperatorsId();
			operAddedId.setOpeno(openo);
			operAddedId.setServicekey((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION));
			operAdded.setId(operAddedId);
			operAdded.setOpepwd(new Md5PasswordEncoder().encodePassword(opepwd,	"12345"));
			//operAdded.setServicekey(serviceKey);
			operAdded.setGroupId(new Long(gpid));
			operAdded.setCreateTime(create_Time);
			operAdded.setCreateUser(create_USER);
			operAdded.setAgentInfo(agentinfo);
			operAdded.setNote(note);
			operAdded.setLevelid(levelid);
		
	
			rtCode = operatorServ.addOperator(operAdded);
		}
        if (rtCode.equals(Constants.MESSAGE_OPERATOR_EXIST)) {
                req.setAttribute("openo", null);
				req.setAttribute("grouplist", groupDao.findAllGroups((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),level));
				req.setAttribute("msg",rtCode);
				return mapping.findForward(this.formView);				
		} else 
			if (rtCode.equals(Constants.MESSAGE_ADD_OPERATOR_CUSSE)) 
			{
				try{
					req.setAttribute("msg", "添加操作员成功!");
					operAdded.getId().getOpeno();
					Long a=operAdded.getGroupId();
					groupServ.searchGroup(a).getGroupName();
					logger.debug("添加操作员成功，帐号：" + operAdded.getId().getOpeno() + "，分组："	+ groupServ.searchGroup(operAdded.getGroupId()).getGroupName());
					this.logServ.log(operator,Constants.LOG_TYPE_ADD_OPERATOR, "添加操作员成功，帐号：" + operAdded.getId().getOpeno() + "，分组："	+ groupServ.searchGroup(operAdded.getGroupId()).getGroupName());
				}catch(Exception e)
				{
					e.printStackTrace();
				}
					
					// log add operator
					//this.logServ.logAddOperator(operator, operAdded);
					return mapping.findForward("successView");
			}
			else
			{	
			    return mapping.findForward(this.errorView);		
	        }
	   }
	


	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		
		HttpSession session = req.getSession();
		Long level = (Long)session.getAttribute("level");
		req.setAttribute("openo",null);
		TyadminOperators opera = (TyadminOperators)req.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		List<TyadminGroups> grouplist =  groupDao.findAllGroups((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),level);
		//去掉超极管理员
		Iterator<TyadminGroups> it = grouplist.iterator();
		while(it.hasNext())
		{
			TyadminGroups gr = it.next();
			if(gr.getGroupName() != null && gr.getGroupName().trim().equals("超级管理员"))
				it.remove();
		}
		
		String bdparam = req.getParameter("bdbox1");//事业部
		String zoneparam = req.getParameter("zonebox1");//战区
		String areaparam = req.getParameter("areabox1");//片区
		String bagparam = req.getParameter("bagbox1");//行动组
		
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
		String businName = new UtilFunctionDao().getLevelName(1, leveld);//事业部
		String warName = new UtilFunctionDao().getLevelName(2, leveld);//战区
		String areaName = new UtilFunctionDao().getLevelName(3, leveld);//片区
		String branName = new UtilFunctionDao().getLevelName(4, leveld);//行动组
		req.setAttribute("businName", businName);
		req.setAttribute("warName", warName);
		req.setAttribute("areaName", areaName);
		req.setAttribute("branName", branName);
		
		//获取区域列表
		if (opeBd == null) { 
			// 操作员对应省份和公司下的所有事业部都可选
			bdlist = leveldao.getLevelByTypeAndID(1, opera.getLevelid());
		} else { // 只能选择与操作员对应的事业部
			bdlist.add(StringUtils.isBlank(bdparam)?opeBd:bdparam);
		}
		// 根据businessdepartment查出wzlist下级菜单
		if (opeZone == null) {
			wzlist = leveldao.getSelectLevel(1, opera.getLevelid(), StringUtils.isBlank(bdparam)?opeBd:bdparam, null, null);
		} else { 
			wzlist.add(StringUtils.isBlank(zoneparam)?opeZone:zoneparam);
		}
		if (opeArea == null) {
			arealist = leveldao.getSelectLevel(2, opera.getLevelid(), StringUtils.isBlank(bdparam)?opeBd:bdparam, 
					StringUtils.isBlank(zoneparam)?opeZone:zoneparam, null);
		} else {
			arealist.add(StringUtils.isBlank(areaparam)?opeArea:areaparam);
		}
		if (opeBag == null) {
			baglist = leveldao.getSelectLevel(3, opera.getLevelid(), StringUtils.isBlank(bdparam)?opeBd:bdparam, 
					StringUtils.isBlank(zoneparam)?opeZone:zoneparam, StringUtils.isBlank(areaparam)?opeArea:areaparam);
		} else {
			baglist.add(StringUtils.isBlank(bagparam)?opeBag:bagparam);
		}
		
		req.setAttribute("grouplist",grouplist);
		req.setAttribute("bdparam", StringUtils.isBlank(bdparam)?opeBd:bdparam);
		req.setAttribute("zoneparam", StringUtils.isBlank(zoneparam)?opeZone:zoneparam);
		req.setAttribute("areaparam", StringUtils.isBlank(areaparam)?opeArea:areaparam);
		req.setAttribute("bagparam", StringUtils.isBlank(bagparam)?opeBag:bagparam);
		req.setAttribute("bdlist", bdlist);
		req.setAttribute("wzlist", wzlist);
		req.setAttribute("arealist", arealist);
		req.setAttribute("baglist", baglist);
		
		return super.processGet(mapping, form, req, res);
	}

}
