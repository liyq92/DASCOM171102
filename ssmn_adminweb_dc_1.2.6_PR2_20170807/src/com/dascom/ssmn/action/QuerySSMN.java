package com.dascom.ssmn.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dascom.OPadmin.action.AdminFormAction;
import com.dascom.OPadmin.util.Page;
import com.dascom.init.ConfigMgr;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.dao.zydcUserDao;
import com.dascom.ssmn.entity.SsmnZyCancelUser;
import com.dascom.ssmn.entity.SsmnZyChannel;
import com.dascom.ssmn.entity.SsmnZyEnablenumber;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.ssmn.entity.ZydcRecord;
import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.IAdminAuthority;
import com.dascom.OPadmin.dao.impl.AdminAuthorityImpl;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.ssmn.util.Constants;
import com.dascom.ssmn.dao.EnableNumberDao;

public class QuerySSMN extends AdminFormAction{
	private static final Logger logger = Logger.getLogger(QuerySSMN.class);
	private zydcUserDao uDao = zydcUserDao.getInstance();
	private IAdminAuthority authDao = new AdminAuthorityImpl();
	private EnableNumberDao eDao = new EnableNumberDao().getInstance();
		
	@Override
	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		TyadminOperators opera = (TyadminOperators)req.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		//权限不能从session中取，因为权限功能，随时要改变（在权限管理中）
		List<String> authMethodList = authDao.findByGroupId(opera.getGroupId().longValue(),(String)req.getSession().getAttribute(Constants.SERVICEKEY_IN_SESSION));
		UtilFunctionDao.showType(authMethodList);
		String msisdn = req.getParameter("msisdn");
		String sname = req.getParameter("name");
		String sssmnnumber = req.getParameter("ssmnnumber");
		String sempno = req.getParameter("vempno");
		
		//zydc_agent_list传过来的查询条件
		String selectmsisdn = req.getParameter("selectmsisdn");
		String selectname = req.getParameter("selectname");
		String selectempno = req.getParameter("selectempno");
		String selectssmnnum = req.getParameter("selectssmnnum");
		String selectchannelbox = req.getParameter("selectchannelbox");
		String selectbdbox1 = req.getParameter("selectbdbox1");
		String selectzonebox1 = req.getParameter("selectzonebox1");
		String selectareabox1 = req.getParameter("selectareabox1");
		String selectbagbox1 = req.getParameter("selectbagbox1");
		String selectenmtype = req.getParameter("selectenmtype");
		String pagetemp = req.getParameter("pagetemp");
		String selectstatusbox = req.getParameter("selectstatusbox");
		
		String temp = req.getParameter("page");
		Page p = sysUtil.setPageInfo(req, temp, Constants.RECORD_PER_PAGE);
		String suid = "";
		int isAll = 1;//1:表示显示全部  0:表示显示按条件查询的
		List<SsmnZyUser> ulist = null;
		List<SsmnZyUser> newList = null;
		List<SsmnZyCancelUser> oldList = null;
		int num = 0;
		String strEnable = "";
		String strChannels = "";
		String oldmsisdn = "";
		String switchtomsisdn ="";
		String switchtossmnnumber = "";
		String switchtochannelname = "";
		String switchusername = "";
		String switchtochannelId = "";
		String stype = "";
		SsmnZyLevel level =null;
		
		try {
			level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if((msisdn == null || "".equals(msisdn)) && (sname == null || "".equals(sname)) && (sssmnnumber == null || "".equals(sssmnnumber)) )
		{
			isAll = 1;//显示全部
			
			//查询用户副号码信息
			ulist = uDao.getZYDCUserList(msisdn,sname,sssmnnumber,"",p.getFirstResult()-1, p.getSize(),opera);
			//查询用户数量
			if(ulist.size()>0)
				num= uDao.getZYDCUserCount(msisdn,sname,sssmnnumber,opera);
	
		}else
		{
			isAll = 0;//显示按条件查询的
								
			stype = req.getParameter("type");
			if(stype != null && !"".equals(stype) && stype.equals("2"))//解绑
			{
				String strsmsisdn = req.getParameter("smsisdn");
				String strsssmnuber = req.getParameter("sssmnuber");
				suid = req.getParameter("uid");
				boolean isvalid = uDao.isValidNumber(strsssmnuber);//判断副号码是否有效
				if(!isvalid)
				{
					//解绑
					boolean iscancelok = uDao.cancelBindSsmnuber(strsmsisdn, strsssmnuber, suid);
					if(iscancelok)
					{
						req.setAttribute("msg", "解绑成功!");
					}
					else
						req.setAttribute("msg", "解绑失败!");
					
				}else
					req.setAttribute("msg", "该副号码没有绑定!");
					
			}else if(stype != null && !"".equals(stype) && stype.equals("3"))//换绑,查询换绑了经纪人是否存在
			{
				String strsmsisdn = req.getParameter("smsisdn");
				String strsssmnuber = req.getParameter("sssmnuber");
				suid = req.getParameter("uid");
				String switchmsisdn = req.getParameter("bindmsisdn");
				String schannelId = req.getParameter("channelvalue");
				String schannelName = uDao.getChannelNamebyId(schannelId);
				//先判断换绑经纪人是否存在
				ZydcRecord zyrec = uDao.isExistInUser(switchmsisdn);
				if(zyrec == null)
					req.setAttribute("msg","无该经纪人");
				else
				{
					//判断要换绑的副号码是否是A+的，如果是，要判断，换绑后的经纪人是否已经存在A+副号码，如果存在，
					//则不让换，如果不存在，可以换绑
					List<SsmnZyEnablenumber> enli = eDao.getSsmnEnableNumber(strsssmnuber);
					if(enli !=null && enli.size()>0)
					{
						boolean checkUserA = true;
						if(enli.get(0).getType() == 2)
						{
							//是A+副号码，需要判断换绑的经纪人是否已存在A+副号码
							List<SsmnZyUser> listUser = uDao.checkOtherEnableNumberA(switchmsisdn,2);
							if(listUser !=null && listUser.size()>0)
							{
								checkUserA = false;
								req.setAttribute("msg", Constants.MESSAGE_EXISTOTHERNUMBER);
							}else
								checkUserA  = true;
						}
						if(checkUserA)
						{
							oldmsisdn = strsmsisdn;
							switchtomsisdn =switchmsisdn;
							switchtossmnnumber = strsssmnuber;
							switchtochannelname = UtilFunctionDao.splitStr(schannelName,"_");
							switchusername = zyrec.getUsername();
							switchtochannelId = schannelId;
						}
					}else
						req.setAttribute("msg", "换绑的副号码无效!");
				}
			}else if(stype != null && !"".equals(stype) && stype.equals("4"))//真正换绑
			{
				String strsmsisdn = req.getParameter("smsisdn");
				String strsssmnuber = req.getParameter("sssmnuber");
				suid = req.getParameter("uid");
				String switchmsisdn = req.getParameter("bindmsisdn");
				String schannelId = req.getParameter("channelvalue");

				//换绑
				String isOk=uDao.switchBindSsmnuber(strsmsisdn, strsssmnuber, schannelId,suid,switchmsisdn);
				if(isOk.equals(""))
				{
					req.setAttribute("msg", "换绑成功!");
					
				}else
					req.setAttribute("msg", isOk);
				
			}else if(stype != null && !"".equals(stype) && stype.equals("5"))//变更渠道
			{
				suid = req.getParameter("uid");
				String schannelId = req.getParameter("channelvalue");
				
				boolean isok = uDao.updateChannel(suid,schannelId,"","");
				if(isok)
					req.setAttribute("msg", "变更渠道成功!");
				else
					req.setAttribute("msg", "变更渠道失败!");
			}else if (stype != null && !"".equals(stype) && stype.equals("6"))//添加备注
			{
				String sremark = req.getParameter("remark");
				boolean isok = uDao.updateRemark(sempno, sremark,level);
				if(isok)
					req.setAttribute("msg", "添加备注成功!");
				else
					req.setAttribute("msg", "添加备注失败!");
			}
			
			newList = uDao.getZYDCUserList("", sname, sssmnnumber,sempno, 0, 0, opera);//不分页
			oldList = uDao.getZYDCCancelUserList("", sname, sssmnnumber,sempno, p.getFirstResult()-1, p.getSize(), opera);
			if(oldList != null && oldList.size()>0)
				num = uDao.getZYDCCancelUserCount(msisdn, sname, sssmnnumber, opera);
			
			strEnable = "";
						
			//获取所有的渠道
			List<SsmnZyChannel>  channels = uDao.getChannelList(opera.getLevelid(),"",Constants.TYPE_SHOWDATE);
			strChannels = "";
			for(int i = 0; i<channels.size(); i++)
			{
				strChannels +=channels.get(i).getId();
				strChannels +="|";
				strChannels += channels.get(i).getName();
				strChannels +=",";
			}
		}
		
		
	    req.setAttribute("ulist",ulist);
	    req.setAttribute("msisdn", msisdn);
	    req.setAttribute("name", sname);
	    req.setAttribute("ssmnnumber", sssmnnumber);
	    req.setAttribute("recordCount", num);
	    req.setAttribute("isAll", isAll);
	    req.setAttribute("newList", newList);
	    req.setAttribute("oldList", oldList);
	    req.setAttribute("authMethodList", authMethodList);
	    req.setAttribute("stype", stype);
	    req.setAttribute("oldmsisdn", oldmsisdn);
	    req.setAttribute("switchtomsisdn", switchtomsisdn);  
		req.setAttribute("switchtossmnnumber", switchtossmnnumber);
		req.setAttribute("switchtochannelname", switchtochannelname);
		req.setAttribute("switchtochannelId", switchtochannelId);
		req.setAttribute("switchusername", switchusername);
		req.setAttribute("suid", suid);
		req.setAttribute("selectmsisdn", selectmsisdn);
		req.setAttribute("selectname", selectname);
		req.setAttribute("selectssmnnum", selectssmnnum);
		req.setAttribute("selectchannelbox", selectchannelbox);
		req.setAttribute("selectbdbox1", selectbdbox1);
		req.setAttribute("selectzonebox1", selectzonebox1);
		req.setAttribute("selectareabox1", selectareabox1);
		req.setAttribute("selectbagbox1", selectbagbox1);
		req.setAttribute("pagetemp", pagetemp);
		req.setAttribute("selectstatusbox", selectstatusbox);
		req.setAttribute("selectenmtype", selectenmtype);
		req.setAttribute("selectempno", selectempno);
		req.setAttribute("sempno", sempno);
		req.setAttribute("isSecondMsisdn", ConfigMgr.getIsAddSecondMsisdn());
		req.setAttribute("isHaveSsmnNum", ConfigMgr.getIsHaveSsmnNum());
		
	    if(strEnable.length() >0)
	    {
	    	req.setAttribute("strEnable", strEnable.substring(0,strEnable.length()-1));
	    }
	    if(strChannels.length() >0)
	    {
	    	req.setAttribute("strChannels", strChannels.substring(0, strChannels.length()-1));
	    }
		return mapping.findForward(super.formView);
	}
	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {

		return this.processSubmit(mapping, form, req, res);
	}

}
