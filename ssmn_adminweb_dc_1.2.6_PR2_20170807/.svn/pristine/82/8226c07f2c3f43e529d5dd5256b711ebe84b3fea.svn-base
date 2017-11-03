package com.dascom.ssmn.action;

import java.text.ParseException;
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
import com.dascom.OPadmin.action.AdminFormAction;
import com.dascom.OPadmin.util.Page;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.IAdminGroup;
import com.dascom.OPadmin.dao.impl.AdminGroupImpl;
import com.dascom.OPadmin.entity.TyadminGroups;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.ssmn.util.Constants;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.dao.zydcUserDao;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.EnableNumberDao;

public class LevelManage extends AdminFormAction{
	private static final Logger logger = Logger.getLogger(LevelManage.class);
	private LevelDao leDao = LevelDao.getInstance();
	private IAdminGroup groupDao = new AdminGroupImpl();
	private zydcUserDao uDao = zydcUserDao.getInstance();
	private EnableNumberDao eDao = EnableNumberDao.getInstance();
	
	@Override
	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		HttpSession session = req.getSession();
		Long level = (Long)session.getAttribute("level");
		TyadminOperators opera = (TyadminOperators)req.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		List<TyadminGroups> grouplist =  groupDao.findAllGroups((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),level);
		Long operalevelid = opera.getLevelid();
		int recordCount = 0;
		String stype = req.getParameter("type");
		SsmnZyLevel zylevel = null;
		
		String temp = req.getParameter("page");
		Page p = sysUtil.setPageInfo(req, temp, Constants.RECORD_PER_PAGE);
		
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
		
		if(stype !=null && !"".equals(stype) && stype.length()>0 && stype.equals("preAdd"))
		{
			//去掉超极管理员
			Iterator<TyadminGroups> it = grouplist.iterator();
			while(it.hasNext())
			{
				TyadminGroups gr = it.next();
				if(gr.getGroupName() != null && gr.getGroupName().trim().equals("超级管理员"))
					it.remove();
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
			return mapping.findForward("addFormView");
		}
		
		if(stype !=null && !"".equals(stype) && stype.length()>0 && stype.equals("add"))
		{
			String sgroup = req.getParameter("groupid");
			if(sgroup !=null && !"".equals(sgroup) && sgroup.equals("事业部"))//事业部
			{
				String sbdparam = req.getParameter("bdbox1Txt");
				if(sbdparam !=null && !"".equals(sbdparam) && sbdparam.length()>0)
					sbdparam = sbdparam.replaceAll(" ", "");
				//判断事业部是否存在
				List<SsmnZyLevel> le =leDao.checkLevelEst(sbdparam,1,operalevelid);//1:判断事业部
				if(le !=null && le.size()>0)
					req.setAttribute("msg", "该事业部已经存在!");
				else
				{
					//添加事业部
					SsmnZyLevel lv = new SsmnZyLevel();
					lv.setProvincecity(zylevel.getProvincecity());
					lv.setCompany(zylevel.getCompany());
					lv.setBusinessdepartment(sbdparam);
					boolean saveOk = leDao.addLevel(lv);
					if(saveOk)
						req.setAttribute("msg", "事业部添加成功");
					else
						req.setAttribute("msg", "事业部添加失败");
				}
			}
			if(sgroup !=null && !"".equals(sgroup) && sgroup.equals("战区"))//战区
			{
				String strbdbox1 = req.getParameter("bdbox1");//事业部
				String szonetxt = req.getParameter("zonebox1Txt");//战区
				if(szonetxt !=null && !"".equals(szonetxt) && szonetxt.length()>0)
					szonetxt = szonetxt.replaceAll(" ", "");
				//判断战区是否存在
				List<SsmnZyLevel> le =leDao.checkLevelEst(szonetxt,2,operalevelid);//2:判断战区
				if(le !=null && le.size()>0)
					req.setAttribute("msg", "该战区已经存在!");
				else
				{
					//添加战区
					SsmnZyLevel lv = new SsmnZyLevel();
					lv.setProvincecity(zylevel.getProvincecity());
					lv.setCompany(zylevel.getCompany());
					lv.setBusinessdepartment(strbdbox1);
					lv.setWarzone(szonetxt);
					boolean saveOk = leDao.addLevel(lv);
					if(saveOk)
						req.setAttribute("msg", "战区添加成功");
					else
						req.setAttribute("msg", "战区添加失败");
				}
			}
			if(sgroup !=null && !"".equals(sgroup) && sgroup.equals("片区"))//片区
			{
				String strbdbox1 = req.getParameter("bdbox1");//事业部
				String szonebox1 = req.getParameter("zonebox1");//战区
				String sareatxt = req.getParameter("areabox1Txt");//片区
				if(sareatxt !=null && !"".equals(sareatxt) && sareatxt.length()>0)
					sareatxt = sareatxt.replaceAll(" ", "");
				//判断片区是否存在
				List<SsmnZyLevel> le =leDao.checkLevelEst(sareatxt,3,operalevelid);//3:判断片区
				if(le !=null && le.size()>0)
					req.setAttribute("msg", "该片区已经存在!");
				else
				{
					//添加片区
					SsmnZyLevel lv = new SsmnZyLevel();
					lv.setProvincecity(zylevel.getProvincecity());
					lv.setCompany(zylevel.getCompany());
					lv.setBusinessdepartment(strbdbox1);
					lv.setWarzone(szonebox1);
					lv.setArea(sareatxt);
					boolean saveOk = leDao.addLevel(lv);
					if(saveOk)
						req.setAttribute("msg", "片区添加成功");
					else
						req.setAttribute("msg", "片区添加失败");
				}
			}
			if(sgroup !=null && !"".equals(sgroup) && sgroup.equals("分行组别"))//行动组
			{
				String strbdbox1 = req.getParameter("bdbox1");//事业部
				String szonebox1 = req.getParameter("zonebox1");//战区
				String sareabox1 = req.getParameter("areabox1");//片区
				String sbrantxt = req.getParameter("bagbox1Txt");//行动组别
				if(sbrantxt !=null && !"".equals(sbrantxt) && sbrantxt.length()>0)
					sbrantxt = sbrantxt.replaceAll(" ", "");
				//判断行动组别是否存在
				List<SsmnZyLevel> le =leDao.checkLevelEst(sbrantxt,4,0L);//3:判断分行组别,检查分行行动组时候,不做公司限制(要求各公司所有的组都不能重名)
				if(le !=null && le.size()>0)
					req.setAttribute("msg", "该分行组别已经存在!");
				else
				{
					//添加行动组别
					SsmnZyLevel lv = new SsmnZyLevel();
					lv.setProvincecity(zylevel.getProvincecity());
					lv.setCompany(zylevel.getCompany());
					lv.setBusinessdepartment(strbdbox1);
					lv.setWarzone(szonebox1);
					lv.setArea(sareabox1);
					lv.setBranchactiongroup(sbrantxt);
					boolean saveOk = leDao.addLevel(lv);
					if(saveOk)
						req.setAttribute("msg", "分行组别添加成功");
					else
						req.setAttribute("msg", "分行组别添加失败");
				}
			}
			
		}
		if(stype !=null && !"".equals(stype) && stype.length()>0 && stype.equals("del"))//删除
		{
			String sid = req.getParameter("mid");
			String sbusin = req.getParameter("mbusin");
			String swarezone = req.getParameter("mwarzone");
			String sarea = req.getParameter("marea");
			String sbrahch = req.getParameter("mbranch");
			//检查该级别下是否存在用户 ,是否存在副号码
			boolean userExist = uDao.getUserByLevelid(sbusin,swarezone,sarea,sbrahch,operalevelid);
			if(userExist)
			{
				req.setAttribute("msg", "该级别下面还有经纪人正在使用,请全部解绑后再删除!");
			}else
			{
				//判断该级别下面是否有副号码
				boolean ssmnNumberExist = eDao.getSsmnNumberByLevelid(sbusin,swarezone,sarea,sbrahch,operalevelid);
				if(ssmnNumberExist)
					req.setAttribute("msg", "该级别下面还有副号码正在使用,请全部解绑后再删除!");
				else
				{
					//删除
					boolean delOk =leDao.delLevel(sid);
					if(delOk)
						req.setAttribute("msg", "删除成功!");
					else
						req.setAttribute("msg", "删除失败!");
				}
			}
		}
		if(stype !=null && !"".equals(stype) && stype.length()>0 && stype.equals("edit"))//编辑
		{
			//编辑功能,1)可以修改名称,2) 组别,片区,战区,事业部之间可以更换
			boolean isupdateOk = false;
			String schtype = req.getParameter("chtype");
			if(schtype !=null && !"".equals(schtype) && schtype.equals("1"))//事业部级,修改事业部名称
			{
				String sbusinNew = req.getParameter("mbusinNew");
				String sbusinOld = req.getParameter("vbusinOld");
				if(sbusinNew !=null && !"".equals(sbusinNew) && sbusinNew.length()>0)
					sbusinNew = sbusinNew.replaceAll(" ", "");
				//判断新的事业部是否存在
				List<SsmnZyLevel> isExist= leDao.isExist(operalevelid,sbusinNew,schtype);
				if(isExist == null || isExist.size() <=0)
				{
					//不存在,则修改,修改事业部名称
					isupdateOk =leDao.updateLevelBranch(zylevel.getCompany(),schtype,sbusinNew,
							sbusinOld,"","","","","","");
					if(isupdateOk )
						req.setAttribute("msg", "变更成功!");
					else
						req.setAttribute("msg","变更失败");
					
				}else
					req.setAttribute("msg", "该事业部已经存在!");
			}
			if(schtype !=null && !"".equals(schtype) && schtype.equals("2"))//战区级,修改战区名称,及调换事业部
			{
				String sbusinNew = req.getParameter("mbusinNew");//新事业部
				String sbusinOld = req.getParameter("mbusinOld");//改变前事业部
				String swarzoneNew = req.getParameter("mwarzoneNew");//新战区
				String swarzoneOld = req.getParameter("mwarzoneOld");//改变前战区
				if(swarzoneNew !=null && !"".equals(swarzoneNew) && swarzoneNew.length()>0)
					swarzoneNew = swarzoneNew.replaceAll(" ", "");
				//新的战区名字和旧的不一样,说明是改名了,需要判断新的战区是否存在
				List<SsmnZyLevel> isExist = null;
				if(swarzoneNew !=null && swarzoneOld !=null && !swarzoneNew.equals(swarzoneOld))
					isExist= leDao.isExist(operalevelid,swarzoneNew,schtype);
				//如果新战区名字和旧的一样,说明是在调换,不需要做判断
				if( ((isExist == null || isExist.size() <=0) && !swarzoneNew.equals(swarzoneOld) )
						|| (swarzoneNew.equals(swarzoneOld) ))
				{
					//不存在,则修改,修改战区名称,调换的事业部
					isupdateOk =leDao.updateLevelBranch(zylevel.getCompany(),schtype,sbusinNew,
							sbusinOld,swarzoneNew,swarzoneOld,"","","","");
					if(isupdateOk )
						req.setAttribute("msg", "变更成功!");
					else
						req.setAttribute("msg","变更失败");
					
				}else
					req.setAttribute("msg", "该战区已经存在!");
			}
			if(schtype !=null && !"".equals(schtype) && schtype.equals("3"))//片区级,修改片区名称,及调换事业部,战区
			{
				String sbusinNew = req.getParameter("mbusinNew");//新事业部
				String sbusinOld = req.getParameter("mbusinOld");//改变前事业部
				String swarzoneNew = req.getParameter("mwarzoneNew");//新战区
				String swarzoneOld = req.getParameter("mwarzoneOld");//改变前战区
				String sareaNew = req.getParameter("mareaNew");//新片区
				String sareaOld = req.getParameter("mareaOld");//改变前片区
				if(sareaNew !=null && !"".equals(sareaNew) && sareaNew.length()>0)
					sareaNew = sareaNew.replaceAll(" ", "");
				//新的片区名字和旧的不一样,说明是改名了,需要判断新的片区是否存在
				//判断新的片区是否存在
				List<SsmnZyLevel> isExist = null;
				if(sareaNew !=null && sareaOld !=null && !sareaNew.equals(sareaOld))
					isExist= leDao.isExist(operalevelid,sareaNew,schtype);
				if(((isExist == null || isExist.size() <=0) && !sareaNew.equals(sareaOld) )
						|| (sareaNew.equals(sareaOld) ))
				{
					//不存在,则修改,修改片区名称,调换的事业部,战区
					isupdateOk =leDao.updateLevelBranch(zylevel.getCompany(),schtype,sbusinNew,
							sbusinOld,swarzoneNew,swarzoneOld,sareaNew,sareaOld,"","");
					if(isupdateOk )
						req.setAttribute("msg", "变更成功!");
					else
						req.setAttribute("msg","变更失败");
				}else
					req.setAttribute("msg", "该片区已经存在!");
			}
			if(schtype !=null && !"".equals(schtype) && schtype.equals("4"))//行动组级,修改行动组名称,及调换事业部,战区,片区
			{
				String sbusinNew = req.getParameter("mbusinNew");//新事业部
				String sbusinOld = req.getParameter("mbusinOld");//改变前事业部
				String swarzoneNew = req.getParameter("mwarzoneNew");//新战区
				String swarzoneOld = req.getParameter("mwarzoneOld");//改变前战区
				String sareaNew = req.getParameter("mareaNew");//新片区
				String sareaOld = req.getParameter("mareaOld");//改变前片区
				String sbranchNew = req.getParameter("mbranchNew");//新行动组
				String sbranchOld = req.getParameter("mbranchOld");//改前行动组
				if(sbranchNew !=null && !"".equals(sbranchNew) && sbranchNew.length()>0)
					sbranchNew = sbranchNew.replaceAll(" ", "");
				
				//新的行动组名字和旧的不一样,说明是改名了,需要判断新的行动组是否存在
				List<SsmnZyLevel> isExist= null;
				if(sbranchNew !=null && sbranchOld !=null && !sbranchNew.equals(sbranchOld))
					isExist =leDao.isExist(operalevelid,sbranchNew,schtype);
				if(((isExist == null || isExist.size() <=0) && !sbranchNew.equals(sbranchOld) )
						|| (sbranchNew.equals(sbranchOld) ))
				{
					//不存在,则修改,修改战区名称,调换的事业部
					isupdateOk =leDao.updateLevelBranch(zylevel.getCompany(),schtype,sbusinNew,
							sbusinOld,swarzoneNew,swarzoneOld,sareaNew,sareaOld,sbranchNew,sbranchOld);
					if(isupdateOk )
						req.setAttribute("msg", "变更成功!");
					else
						req.setAttribute("msg","变更失败");
				}else
					req.setAttribute("msg", "该行动组已经存在!");
			}
						
		}	
		//保证编辑完能更新过来
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
		
		List<SsmnZyLevel> llist = leDao.getLevelList(p.getFirstResult()-1,p.getSize(),operalevelid,bdparam,zoneparam,areaparam,bagparam);
		recordCount = leDao.getLevelListCount(operalevelid,bdparam,zoneparam,areaparam,bagparam);
		Iterator<SsmnZyLevel> it = llist.iterator();
		while(it.hasNext())
		{
			SsmnZyLevel sname = it.next();
			if(sname != null)
			{
				if(sname.getBranchactiongroup() == null || "".equals(sname.getBranchactiongroup()))
					sname.setBranchactiongroup("全部");
				if(sname.getArea() == null || "".equals(sname.getArea()))
					sname.setArea("全部");
				if(sname.getWarzone() == null || "".equals(sname.getWarzone()))
					sname.setWarzone("全部");
				if(sname.getBusinessdepartment() == null || "".equals(sname.getBusinessdepartment()))
					sname.setBusinessdepartment("全部");
				if(sname.getCompany() == null || "".equals(sname.getCompany()))
					sname.setCompany("全部");
			}
		}
	    
	    req.setAttribute("page", p);
	    req.setAttribute("recordCount", recordCount);
	    req.setAttribute("llist", llist);
	    req.setAttribute("bdlist", bdlist);
	    req.setAttribute("wzlist", wzlist);
	    req.setAttribute("arealist",arealist );
	    req.setAttribute("baglist", baglist);
	    req.setAttribute("bdparam", bdparam);
	    req.setAttribute("zoneparam", zoneparam);
	    req.setAttribute("areaparam", areaparam);
	    req.setAttribute("bagparam", bagparam);
	    
	   
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
