package com.dascom.ssmn.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dascom.OPadmin.action.AdminFormAction;
import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.IAdminAuthority;
import com.dascom.OPadmin.dao.impl.AdminAuthorityImpl;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.util.Page;
import com.dascom.ssmn.dao.EnableNumberDao;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.entity.SsmnZYEnableNumberRecord;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.ssmn.entity.SsmnZyCancelUser;
import com.dascom.ssmn.util.Constants;
import com.dascom.ssmn.entity.SsmnDcRelation;
import com.dascom.ssmn.entity.SsmnDcDelrelation;
import com.dascom.ssmn.dao.RelationDao;
import com.dascom.ssmn.dao.zydcUserDao;

public class ModifySSMNNumber extends AdminFormAction {
	private static final Logger logger = Logger
			.getLogger(ModifySSMNNumber.class);
	private static EnableNumberDao eDao = EnableNumberDao.getInstance();
	private LevelDao leveldao = LevelDao.getInstance();
	private RelationDao rDao = RelationDao.getInstance();
	private zydcUserDao zDao = zydcUserDao.getInstance();
	private IAdminAuthority authDao = new AdminAuthorityImpl();

	@Override
	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		res.setContentType("text/html; charset=UTF-8");
		TyadminOperators opera = (TyadminOperators) req.getSession()
				.getAttribute(Constants.OPERATOR_IN_SESSION);
		List<String> authMethodList = authDao.findByGroupId(opera.getGroupId().longValue(),(String)req.getSession().getAttribute(Constants.SERVICEKEY_IN_SESSION));
		UtilFunctionDao.showType(authMethodList);
		req.setAttribute("showType", Constants.TYPE_SHOWDATE);
		Long operalevelid = opera.getLevelid();
		List<String> bdlist = new ArrayList<String>();
		List<String> wzlist = new ArrayList<String>();
		List<String> arealist = new ArrayList<String>();
		List<String> baglist = new ArrayList<String>();
		String opeBd = null;
		String opeZone = null;
		String opeArea = null;
		String opeBag = null;
		SsmnZyLevel level = null;
		try {
			level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(
					operalevelid);
			opeBd = level.getBusinessdepartment();
			opeZone = level.getWarzone();
			opeArea = level.getArea();
			opeBag = level.getBranchactiongroup();
		} catch (DaoException e1) {
			e1.printStackTrace();
		}
		
		String method = req.getParameter("method");
		String temp = req.getParameter("page");
		String bdparam = (String) req.getParameter("bdbox1");
		String zoneparam = (String) req.getParameter("zonebox1");
		String areaparam = (String) req.getParameter("areabox1");
		String bagparam = (String) req.getParameter("bagbox1");
		String status = (String) req.getParameter("statusbox");
		String ssmnnum = (String) req.getParameter("ssmnnum");
		String senmtype = (String)req.getParameter("enmtype");//类型：　１：业主副号码 0:经纪人副号码
		String selectssmnnum = req.getParameter("selectssmnnum");

		if (status == null || status.equals(""))
			status = null;
		if(senmtype == null || senmtype.equals(""))
			senmtype = null;
		
		//区域级别的名字是配置 的，这里要根据配置显示 
		String businName = new UtilFunctionDao().getLevelName(1, level);//事业部
		String warName = new UtilFunctionDao().getLevelName(2, level);//战区
		String areaName = new UtilFunctionDao().getLevelName(3, level);//片区
		String branName = new UtilFunctionDao().getLevelName(4, level);//行动组
		req.setAttribute("businName", businName);
		req.setAttribute("warName", warName);
		req.setAttribute("areaName", areaName);
		req.setAttribute("branName", branName);
		
		if (method != null && "edit".equals(method)) {
			String msisdn = req.getParameter("msisdn").trim();
			String businessdepartment = req.getParameter("bd").trim();
			String warzone = req.getParameter("warzone").trim();
			String area = req.getParameter("area").trim();
			String branchactiongroup = req.getParameter("bag").trim();
			String strnumtype = req.getParameter("numtype");
			
			if (level == null) {
				req.setAttribute("msg", "编辑失败！请稍后重新操作！");
			} else {
				String privince = level.getProvincecity();
				String comp = level.getCompany();

				// 查出对应的level
				Long levelid = leveldao.getLevelIDByProperty(privince, comp,
						businessdepartment, warzone, area, branchactiongroup);
				int ret = eDao.editSsmnLevel(msisdn, levelid,strnumtype);
				if (ret < 1)
					req.setAttribute("msg", "编辑失败！请稍后重新操作！！");
				else
					req.setAttribute("msg", "编辑成功！");
			}
		}
		if(method != null && "ownerInfo".equals(method))
		{
			String ssmnnumber = (String) req.getParameter("ssmnnumber");
			//业主副号码信息
			//页面中对ssmnnum值有判断,为空返回，所以这里不需要判断
			List<SsmnDcRelation> sdrlist = rDao.getOwnerInfo(ssmnnumber);//有效的信息
			List<SsmnDcDelrelation> sdrdellist = rDao.getOwnerDelInfo(ssmnnumber);//注销的信息
			//处理sdrlist
			if(sdrlist !=null && sdrlist.size()>0)
			{
				for (int i=0;i<sdrlist.size();i++)
				{
					//查找经纪人姓名和行动组
					SsmnDcRelation dr = sdrlist.get(i);
					if(null != dr && null != dr.getAgentMsisdn() && !"".equals(dr.getAgentMsisdn()))
					{
						//添加姓名
						List<SsmnZyUser> zul = zDao.getZyUserByMsisdn(dr.getAgentMsisdn());
						if(zul !=null && zul.size()>0)
						{
							SsmnZyUser zu = zul.get(0);
							sdrlist.get(i).setName(zu.getName());
							//找到人，再找行动组，添加行动组
							SsmnZyLevel zl =leveldao.getBranchactiongroupNameById(zu.getLevelid()+"");
							if(zl !=null)
								sdrlist.get(i).setBranchactiongroup(zl.getBranchactiongroup());
						}
					}
				}
			}
			//处理sdrdellist
			if(sdrdellist !=null && sdrdellist.size()>0)
			{
				for (int i=0;i<sdrdellist.size();i++)
				{
					//查找经纪人姓名和行动组
					SsmnDcDelrelation dr = sdrdellist.get(i);
					if(null != dr && null != dr.getAgentMsisdn() && !"".equals(dr.getAgentMsisdn()))
					{
						//添加姓名
						List<SsmnZyUser> zul = zDao.getZyUserByMsisdn(dr.getAgentMsisdn());
						if(zul !=null && zul.size()>0)
						{
							SsmnZyUser zu = zul.get(0);
							sdrdellist.get(i).setName(zu.getName());
							//找到人，再找行动组，添加行动组
							SsmnZyLevel zl =leveldao.getBranchactiongroupNameById(zu.getLevelid()+"");
							if(zl !=null)
								sdrdellist.get(i).setBranchactiongroup(zl.getBranchactiongroup());
						}else
						{
							//ssmn_zy_user表中找不到人，还要去ssmn_zy_cancel_user表中找
							List<SsmnZyCancelUser> zucl = zDao.getZyCancelUserByMsisdn(dr.getAgentMsisdn());
							if(zucl !=null && zucl.size()>0)
							{
								SsmnZyCancelUser zu = zucl.get(0);
								sdrdellist.get(i).setName(zu.getName());
								//找到人，再找行动组，添加行动组
								SsmnZyLevel zl =leveldao.getBranchactiongroupNameById(zu.getLevelid()+"");
								if(zl !=null)
									sdrdellist.get(i).setBranchactiongroup(zl.getBranchactiongroup());
							}
						}
					}
				}
			}
			req.setAttribute("sdrlist", sdrlist);
			req.setAttribute("sdrdellist", sdrdellist);
			req.setAttribute("selectssmnnum", selectssmnnum);
			req.setAttribute("selectbdbox1",bdparam );
			req.setAttribute("selectzonebox1", zoneparam);
			req.setAttribute("selectareabox1",areaparam );
			req.setAttribute("selectbagbox1",bagparam );
			req.setAttribute("pagetemp",temp );
			req.setAttribute("selectstatusbox",status);
			req.setAttribute("selecttype", senmtype);
			
			return mapping.findForward("formOwnerRelationView");
		}

		if (opeBd == null) {
			// 操作员对应省份和公司下的所有事业部都可选
			bdlist = leveldao.getLevelByTypeAndID(1, operalevelid);
		} else { // 只能选择与操作员对应的事业部
			bdlist.add(StringUtils.isBlank(bdparam) ? opeBd : bdparam);
		}
		// 根据businessdepartment查出wzlist下级菜单
		if (opeZone == null) {
			wzlist = leveldao.getSelectLevel(1, operalevelid, StringUtils
					.isBlank(bdparam) ? opeBd : bdparam, null, null);
		} else {
			wzlist.add(StringUtils.isBlank(zoneparam) ? opeZone : zoneparam);
		}
		if (opeArea == null) {
			arealist = leveldao.getSelectLevel(2, operalevelid, StringUtils
					.isBlank(bdparam) ? opeBd : bdparam, StringUtils
					.isBlank(zoneparam) ? opeZone : zoneparam, null);
		} else {
			arealist.add(StringUtils.isBlank(areaparam) ? opeArea : areaparam);
		}
		if (opeBag == null) {
			baglist = leveldao.getSelectLevel(3, operalevelid, StringUtils
					.isBlank(bdparam) ? opeBd : bdparam, StringUtils
					.isBlank(zoneparam) ? opeZone : zoneparam, StringUtils
					.isBlank(areaparam) ? opeArea : areaparam);
		} else {
			baglist.add(StringUtils.isBlank(bagparam) ? opeBag : bagparam);
		}

		Page p = sysUtil.setPageInfo(req, temp, Constants.RECORD_PER_PAGE);
		List<SsmnZYEnableNumberRecord> erlist = eDao.getAllzyenablenymberlist(p
				.getFirstResult() - 1, p.getSize(), operalevelid, ssmnnum,
				status, bdparam, zoneparam, areaparam, bagparam,senmtype,0);
		int recordCount = eDao.getAllzyenablenymberCount(operalevelid, ssmnnum,
				status, bdparam, zoneparam, areaparam, bagparam,senmtype);

		List<Long> typeList = eDao.getSsmnnumberAllType();
		req.setAttribute("erlist", erlist);
		req.setAttribute("ssmnnum", ssmnnum);
		req.setAttribute("status", status);
		req.setAttribute("bdparam", StringUtils.isBlank(bdparam) ? opeBd
				: bdparam);
		req.setAttribute("zoneparam", StringUtils.isBlank(zoneparam) ? opeZone
				: zoneparam);
		req.setAttribute("areaparam", StringUtils.isBlank(areaparam) ? opeArea
				: areaparam);
		req.setAttribute("bagparam", StringUtils.isBlank(bagparam) ? opeBag
				: bagparam);
		req.setAttribute("bdlist", bdlist);
		req.setAttribute("wzlist", wzlist);
		req.setAttribute("arealist", arealist);
		req.setAttribute("baglist", baglist);
		req.setAttribute("recordCount", recordCount);
		req.setAttribute("pagetemp", temp);
		req.setAttribute("enmtype", senmtype);
		req.setAttribute("typeList", typeList);
		
		return mapping.findForward(formView);
	}

	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {

		return this.processSubmit(mapping, form, req, res);
	}

}
