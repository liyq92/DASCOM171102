package com.dascom.ssmn.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dascom.OPadmin.action.AdminFormAction;
import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.IAdminAuthority;
import com.dascom.OPadmin.dao.impl.AdminAuthorityImpl;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.util.Page;
import com.dascom.ssmn.dao.ChannelDao;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.zydcUserDao;
import com.dascom.ssmn.dao.EnableNumberDao;
import com.dascom.ssmn.entity.SsmnUser;
import com.dascom.ssmn.entity.SsmnZyChannel;
import com.dascom.ssmn.entity.SsmnZyEnablenumber;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.ssmn.entity.zyUserDto;
import com.dascom.ssmn.util.Constants;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.init.ConfigMgr;

public class ManageAgent extends AdminFormAction{
	private static final Logger logger = Logger.getLogger(ManageAgent.class);
	private zydcUserDao uDao = zydcUserDao.getInstance();
	private LevelDao leveldao = LevelDao.getInstance();
	private ChannelDao cDao = ChannelDao.getInstance();
	private EnableNumberDao enDao = EnableNumberDao.getInstance();
	private IAdminAuthority authDao = new AdminAuthorityImpl();
	
	@Override
	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		TyadminOperators opera = (TyadminOperators)req.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		//权限不能从session中取，因为权限功能，随时要改变（在权限管理中）
		List<String> authMethodList = authDao.findByGroupId(opera.getGroupId().longValue(),(String)req.getSession().getAttribute(Constants.SERVICEKEY_IN_SESSION));
		//初始化 Constants.TYPE_SHOWDATE //默认0：全部显示，1：显示A+数据 ， 2：显示渠道数据
		UtilFunctionDao.showType(authMethodList);
		String temp = req.getParameter("page");
		String method = req.getParameter("method");
		String name = req.getParameter("name");
		String empno = req.getParameter("empno");
		if(empno != null && !empno.equals(""))
			empno = empno.toUpperCase();//员工编号转为大写字母
		String smsisdn = req.getParameter("msisdn");
		String ssmnnum = req.getParameter("ssmnnum");
		String channelid = req.getParameter("channelbox");
		
		String bdparam = (String) req.getParameter("bdbox1");//事业部
		String zoneparam = (String) req.getParameter("zonebox1");//战区
		String areaparam = (String) req.getParameter("areabox1");//片区
		String bagparam = (String) req.getParameter("bagbox1");//行动组
		
		String slevelid = "";//获取操作员的级别，根据级别将条件选择下拉表置灰
		Long operalevelid = opera.getLevelid();
		String strChannels = "";//获取渠道列表，绑定的时候选择使用
		//String sMatchNumberlevel = ConfigMgr.getMatchNumberLevel();//查出配置的分配副号码的级别
		SsmnZyLevel level = null;
		List<String> bdlist = new ArrayList<String>();
		List<String> wzlist = new ArrayList<String>();
		List<String> arealist = new ArrayList<String>();
		List<String> baglist = new ArrayList<String>();
		String opeBd = null;
		String opeZone = null;
		String opeArea = null;
		String opeBag = null;
		List<SsmnZyChannel> channellist = null;
		
		try {
			level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(operalevelid);
			opeBd = level.getBusinessdepartment();
			opeZone = level.getWarzone();
			opeArea = level.getArea();
			opeBag = level.getBranchactiongroup();
		} catch (DaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			//区域级别的名字是配置 的，这里要根据配置显示 
			String businName = new UtilFunctionDao().getLevelName(1, level);//事业部
			String warName = new UtilFunctionDao().getLevelName(2, level);//战区
			String areaName = new UtilFunctionDao().getLevelName(3, level);//片区
			String branName = new UtilFunctionDao().getLevelName(4, level);//行动组
			req.setAttribute("businName", businName);
			req.setAttribute("warName", warName);
			req.setAttribute("areaName", areaName);
			req.setAttribute("branName", branName);
			
			if(method != null && "addview".equals(method)){
				
				String businessdepartment = level.getBusinessdepartment();
				String warzone = level.getWarzone();
				String area = level.getArea();
				String branchactiongroup = level.getBranchactiongroup();
				bdlist.clear();
				wzlist.clear();
				arealist.clear();
				baglist.clear();
				if(businessdepartment == null){  //操作员对应省份和公司下的所有事业部都可选
					//取出事业部集合
					bdlist = leveldao.getLevelByTypeAndID(1, operalevelid);
					
				}else{  //只能选择与操作员对应的事业部
					bdlist.add(businessdepartment);
					if(warzone == null){   //如果对应的事业部下的战区都可选
						wzlist = leveldao.getLevelByTypeAndID(2, operalevelid);
					}else{
						wzlist.add(warzone);
						if(area == null){ //对应事业部和战区下的片区都可选
							arealist = leveldao.getLevelByTypeAndID(3, operalevelid);
						}else{
							arealist.add(area);
							if(branchactiongroup == null){ //对应事业部、战区和片区下的行动组都可选
								baglist = leveldao.getLevelByTypeAndID(4, operalevelid);
							}else{
								baglist.add(branchactiongroup);
							}
						}
					}
				}
				
				//取出渠道
				channellist = uDao.getChannelList(opera.getLevelid(),"",Constants.TYPE_SHOWDATE);
								
				req.setAttribute("businessdepartment", businessdepartment);
				req.setAttribute("bdlist", bdlist);
				req.setAttribute("warzone", warzone);
				req.setAttribute("wzlist", wzlist);
				req.setAttribute("area", area);
				req.setAttribute("arealist", arealist);
				req.setAttribute("branchactiongroup", branchactiongroup);
				req.setAttribute("baglist", baglist);
				req.setAttribute("channellist", channellist);
				req.setAttribute("isSecondMsisdn", ConfigMgr.getIsAddSecondMsisdn());
				req.setAttribute("isHaveSsmnNum", ConfigMgr.getIsHaveSsmnNum());

				return mapping.findForward("addView");
			}else if(method != null && "add".equals(method)){
				String agentname = req.getParameter("aname");
				String aempno = req.getParameter("aempno");

				if(aempno != null && !aempno.equals(""))
					aempno = aempno.toUpperCase();//员工编号转为大写字母

				String msisdn = req.getParameter("msisdn");
				String businessdepartment = req.getParameter("businessdepartment");
				String warzone = req.getParameter("warzone");
				String area = req.getParameter("area");
				String branchactiongroup = req.getParameter("branchactiongroup");
				String ssmn = req.getParameter("ssmn");
				String channel = req.getParameter("channel");
				
				String secondMsisdn = "";
				if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))	//只有第二联系人开关打开，则添加
					secondMsisdn =req.getParameter("secondMsisdn");
				new UtilFunctionDao().setEnablenumberType(Long.parseLong(channel));
				
				if(level == null){
					req.setAttribute("msg", "添加失败！请稍后重新操作！");
					req.setAttribute("name", agentname);
					req.setAttribute("msisdn", msisdn);
					return mapping.findForward("addView");
				}
				String privince = level.getProvincecity();
				String comp = level.getCompany();
				
				//判断主号码是否已经和经纪人绑定，如绑定返回错误
				int usercount = uDao.getZYDCUserCount(msisdn, null, null, opera);
				SsmnUser usr = uDao.getssmnuserByMsisdn(msisdn);
				List<SsmnZyUser> ls = uDao.getSsmnzyuserByEmpno(aempno,level);
				if(ls ==null || ls.size() <=0)
				{
						if( usercount < 1  && usr == null){ //主号不存在
							//判断副号码是否是本片区的，如果不是，则提示副号码为其他片区,并且判断副号码的类型是否正确
							String sret = "";
							//如果开关打开,并且是A+渠道，不需要分配副号码，也不需要判断副号码情况
							if(ssmn.equals(Constants.SSMN_NUM_DEFAULT))
								sret="";
							else
								sret =enDao.isEnableAreaSsmnnumber(ssmn,"",branchactiongroup,Constants.TYPE_ENABLENUMBER);
							if(sret != null && !sret.equals(""))
							{
								req.setAttribute("msg",sret);
							}else
							{	
								boolean checkUserA = false;
								//判断主号码是否存在A+副号码，一个经纪人只存在一个A+副号
								if(Constants.TYPE_ENABLENUMBER ==2 )
								{
									List<SsmnZyUser> listUser = uDao.checkOtherEnableNumberA(msisdn,2);
									if(listUser !=null && listUser.size()>0)
									{
										checkUserA = false;
										req.setAttribute("msg", Constants.MESSAGE_EXISTOTHERNUMBER);
									}else
										checkUserA  = true;
								}else
									checkUserA = true;
								if(checkUserA)
								{
									boolean isvalid = false;
									if(ssmn.equals(Constants.SSMN_NUM_DEFAULT))//开关打开,并且是A+渠道，不需要判断副号码，直接为true
										isvalid = true;
									else
										isvalid =uDao.isValidNumber(ssmn);//判断副号码是否有效
									if(isvalid){
											//查出对应的level
											Long levelid = leveldao.getLevelIDByProperty(privince, comp , businessdepartment, warzone, area, branchactiongroup);
											
											//判断第二联系人是否存在
											List<SsmnZyUser> l =uDao.getZyUserByMsisdn(secondMsisdn);
											if((secondMsisdn ==null || secondMsisdn.length()<=0) ||(l !=null && l.size()>0))
											{
												SsmnZyUser u = new SsmnZyUser();
												u.setLevelid(levelid);
												u.setChannelid(Long.parseLong(channel));
												u.setGroupname(branchactiongroup);
												u.setIntime(new Date());
												u.setName(agentname);
												u.setEmpno(aempno);
												u.setMsisdn(msisdn);
												u.setSsmnnumber(ssmn);
												u.setManner(0L);
												u.setRemark("");
												u.setSecondMsisdn(StringUtils.defaultIfEmpty(secondMsisdn,""));
												//注册业务，绑定副号码
												boolean success = uDao.registeSSMNService(msisdn, ssmn, u);
												
												if(!success)
													req.setAttribute("msg", "添加失败!");
												else
												{
													req.setAttribute("msg", "添加成功!");
												}
											}else
												req.setAttribute("msg", "第二联系人不存在!");
											
										}else{
											req.setAttribute("msg", "副号码无效!");
										}
								 	}
								}
							}else{
								//主号码已经存在，提示用户在副号码管理界面绑定号码
								req.setAttribute("msg", "添加失败！主号码已存在!");
							}
				}else
					req.setAttribute("msg", "添加失败!员工编号已经存在!");
			}else if(method != null && "edit".equals(method)){
				String businessdepartment = req.getParameter("bd");
				String warzone = req.getParameter("warzone");
				//String area = new String(req.getParameter("area").getBytes("ISO8859-1"),"UTF-8");
				String area = req.getParameter("area");
				String branchactiongroup = req.getParameter("bag");
				String agentname = req.getParameter("agentname");
				String msisdn = req.getParameter("msisdn1");
				String newmsisdn = req.getParameter("newmsisdn");
				String sempon = req.getParameter("aempon");//员工编号
				String sOldEmpno = req.getParameter("oldEmpno");//旧的员工编号
				String sec =req.getParameter("secondmsisdn");//第二联系人
				boolean isExistEmpno = false;//员工编号是否存在
				
				if(sempon != null && !sempon.equals(""))
					sempon = sempon.toUpperCase();//员工编号转为大写字母
				
				if(level == null){
					req.setAttribute("msg", "编辑失败！请稍后重新操作！");
				}else{
					
					//判断员工编号,新旧员工编号都不为空，并且员工编号发生变化（新旧员工编号不致）,要去查新的员工编号是否已经存在
					if(sOldEmpno !=null && !"".equals(sOldEmpno.length()) &&
							sempon !=null && !"".equals(sempon) && !sempon.equals(sOldEmpno) )
					{
						List<SsmnZyUser> zuList =uDao.getSsmnzyuserByEmpno(sempon,level);
						
						if(zuList !=null && zuList.size()>0)
						{
							req.setAttribute("msg","员工编号已存在!");
							isExistEmpno = false;
						}else
							isExistEmpno = true;
					}else
						isExistEmpno = true;
					if(isExistEmpno)
					{
						if(newmsisdn != null && !"".equals(newmsisdn) && msisdn != null && !"".equals(msisdn) && !msisdn.equals(newmsisdn))
						{
							//新旧号码不一样,判断新号码经纪人是否存在
							SsmnUser zu = uDao.getssmnuserByMsisdn(newmsisdn);
							
							if(zu != null )
								req.setAttribute("msg","主号码已存在!");
							else
							{
								//注册新用户，之前用户解绑,行动组唯一，所以只根据行动组取levelid
								String isok =uDao.editUser(msisdn,newmsisdn,agentname,branchactiongroup,sempon,sec,level);
								if(isok.equals(""))
									req.setAttribute("msg", "编辑成功！");
								else
									req.setAttribute("msg", isok);
							}
						}else
						{
							List<SsmnZyUser> zyude = uDao.getssmnzyuserbyMsisdn(msisdn);
							//String stremp = zyude.get(0).getEmpno();
							if(zyude != null && zyude.size()>0  )
							{
								//新旧主号码一样
								String privince = level.getProvincecity();
								String comp = level.getCompany();
								
								//查出对应的level
								Long levelid = leveldao.getLevelIDByProperty(privince, comp , businessdepartment, warzone, area, branchactiongroup);
								int ret = uDao.updateAgentInfo(msisdn,agentname, branchactiongroup, levelid,sempon,sec,level);
								if(ret < 1){
									if(ret == -5)
										req.setAttribute("msg", "员工编号已经存在!");
									else
										req.setAttribute("msg", "编辑失败！请稍后重新操作！！");
								}else
								{
									req.setAttribute("msg", "编辑成功！");
								}
							}
						}
					}
				}
			}if(method != null && "delete".equals(method)){
				String msisdn = req.getParameter("demsisdn").trim();
				
				boolean success = uDao.cancelUserAndNumber(msisdn);
				if(!success){
					req.setAttribute("msg", "刪除失败！请稍后重新操作！！");
				}
			}
			if(method != null && "bind".equals(method))//绑定
			{
				String bindmsisdn = req.getParameter("bindmsisdn");
				String strssmnnumber = req.getParameter("strsmnnumber");
				String schannelId = req.getParameter("channelvalue");
				String suid = req.getParameter("uid");
				new UtilFunctionDao().setEnablenumberType(Long.parseLong(schannelId));
				//先判断副号码是否是本片区的，如果不是，则提示副号码为其他片区
				String sret ="";
				//如果是11个0,说明是isHaveSsmnNum开关打开，并且是 A+渠道的经纪人,不分配副号码，也不需要判断副号码
				if(strssmnnumber.equals(Constants.SSMN_NUM_DEFAULT))
					sret ="";
				else
					sret = enDao.isEnableAreaSsmnnumber(strssmnnumber,suid,"",Constants.TYPE_ENABLENUMBER);
				if(sret != null && !sret.equals(""))
				{
					req.setAttribute("msg",sret);
				}else
				{
					boolean isvalid =false;
					if(strssmnnumber.equals(Constants.SSMN_NUM_DEFAULT))//isHaveSsmnNum开关打开,并且是A+渠道，不需要判断副号码，直接为true
						isvalid = true;
					else
						isvalid =uDao.isValidNumber(strssmnnumber);//判断副号码是否有效
					if(isvalid)
					{
						boolean checkUserA = false;
						//判断主号码是否存在A+副号码，一个经纪人只存在一个A+副号
						if(Constants.TYPE_ENABLENUMBER ==2 )
						{
							List<SsmnZyUser> listUser = uDao.checkOtherEnableNumberA(bindmsisdn,2);
							if(listUser !=null && listUser.size()>0)
							{
								checkUserA = false;
								req.setAttribute("msg", Constants.MESSAGE_EXISTOTHERNUMBER);
							}else
								checkUserA  = true;
						}else
							checkUserA = true;
						if(checkUserA)
						{
							//有效的副号码
							boolean isOk=uDao.bindSsmnNumber(bindmsisdn, strssmnnumber, schannelId, suid);
							if(!isOk)
								req.setAttribute("msg", "绑定失败!");
							else
								req.setAttribute("msg", "绑定成功!");
						}
						
					}else
						req.setAttribute("msg", "副号码无效!");
				}
				
			}
			if(method != null && "cancelBatch".equals(method))//批量解绑
			{
				cancelBatchDeal(req,res,level);
			}
			if(method != null && "bindBatch".equals(method))//批量绑定
			{
				bindBatchDeal(req,res,level);
			}			
			if(method != null && "bindBatchDeal".equals(method))//批量处理
			{
				batchDeal(req,res,level);
				
			}
			
			bdlist.clear();
			wzlist.clear();
			arealist.clear();
			baglist.clear();
			if (opeBd == null) { 
				// 操作员对应省份和公司下的所有事业部都可选
				bdlist = leveldao.getLevelByTypeAndID(1, operalevelid);
			} else { // 只能选择与操作员对应的事业部
				bdlist.add(StringUtils.isBlank(bdparam)?opeBd:bdparam);
			}
			// 根据businessdepartment查出wzlist下级菜单
			if (opeZone == null) {
				wzlist = leveldao.getSelectLevel(1, operalevelid, StringUtils.isBlank(bdparam)?opeBd:bdparam, null, null);
			} else { 
				wzlist.add(StringUtils.isBlank(zoneparam)?opeZone:zoneparam);
			}
			if (opeArea == null) {
				arealist = leveldao.getSelectLevel(2, operalevelid, StringUtils.isBlank(bdparam)?opeBd:bdparam, 
						StringUtils.isBlank(zoneparam)?opeZone:zoneparam, null);
			} else {
				arealist.add(StringUtils.isBlank(areaparam)?opeArea:areaparam);
			}
			if (opeBag == null) {
				baglist = leveldao.getSelectLevel(3, operalevelid, StringUtils.isBlank(bdparam)?opeBd:bdparam, 
						StringUtils.isBlank(zoneparam)?opeZone:zoneparam, StringUtils.isBlank(areaparam)?opeArea:areaparam);
			} else {
				baglist.add(StringUtils.isBlank(bagparam)?opeBag:bagparam);
			}
						
			Page p = sysUtil.setPageInfo(req, temp, Constants.RECORD_PER_PAGE);
			//取出操作员的等级
			List<zyUserDto> agentlist = uDao.getAgentInfoList(name,smsisdn,ssmnnum,channelid,bdparam,zoneparam,areaparam,bagparam, operalevelid,empno, p.getFirstResult()-1, p.getSize());
			int count = uDao.getAgentInfoCount(name,smsisdn,ssmnnum,channelid,bdparam,zoneparam,areaparam,bagparam, operalevelid,empno);
			
			//获取所有的渠道，这里要放到弹出框中的渠道
			List<SsmnZyChannel>  channels = uDao.getChannelList(opera.getLevelid(),"",Constants.TYPE_SHOWDATE);
			strChannels = "";
			for(int i = 0; i<channels.size(); i++)
			{
				strChannels +=channels.get(i).getId();
				strChannels +="|";
				strChannels += channels.get(i).getName();
				strChannels +=",";
			}
			slevelid = uDao.getoperalevel();
			req.setAttribute("bdparam", StringUtils.isBlank(bdparam)?opeBd:bdparam);
			req.setAttribute("zoneparam", StringUtils.isBlank(zoneparam)?opeZone:zoneparam);
			req.setAttribute("areaparam", StringUtils.isBlank(areaparam)?opeArea:areaparam);
			req.setAttribute("bagparam", StringUtils.isBlank(bagparam)?opeBag:bagparam);
			req.setAttribute("bdlist", bdlist);
			req.setAttribute("wzlist", wzlist);
			req.setAttribute("arealist", arealist);
			req.setAttribute("baglist", baglist);
			req.setAttribute("channels", channels);
			req.setAttribute("channelid", channelid);
			req.setAttribute("ssmnnum", ssmnnum);
			req.setAttribute("smsisdn", smsisdn);
			req.setAttribute("name", name);
			req.setAttribute("recordCount", count);
			req.setAttribute("agentlist", agentlist);
			req.setAttribute("authMethodList", authMethodList);
			req.setAttribute("slevelid", slevelid);
			req.setAttribute("pagetemp", temp);
			req.setAttribute("empno", empno);
			req.setAttribute("isSecondMsisdn", ConfigMgr.getIsAddSecondMsisdn());
			
			if(strChannels.length() >0)
		    {
		    	req.setAttribute("strChannels", strChannels.substring(0, strChannels.length()-1));
		    }
						
		} catch (Exception e) {
			logger.error("====Exception====>",e);
		}

		return mapping.findForward(super.formView);
	}
	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		return this.processSubmit(mapping, form, req, res);
	}
	
//批量解绑
public void cancelBatchDeal(HttpServletRequest req,HttpServletResponse res,SsmnZyLevel level)
{
	String scdrfilepath = req.getSession().getServletContext().getRealPath("batchDealUsers");//批量解绑导出文件存储
	SimpleDateFormat forma = new SimpleDateFormat("yyyyMMddHHmmss");

	File direx = new File(scdrfilepath);
	if (!direx.exists()) {
		if (!direx.mkdirs())
			System.out.println("创建文件失败");
	}
	int size = 1024 * 1024 * 10;// 上传文件限制为10M以内
	Vector uploadV = new Vector();
	Hashtable nameList = new Hashtable();
	try {
		uploadV = com.dascom.OPadmin.util.WebImportFile.Upload(req, scdrfilepath, size);
	} catch (Exception e) {
		logger.error("-----网络问题，导入群组失败-----");
		e.printStackTrace();
		req.setAttribute("msg","由于您的网络问题导入失败，请重试！");
		//return mapping.findForward(this.formView);
	}
	String fileName = null;
	if (uploadV.size() != 0) {
		nameList = (Hashtable) uploadV.elementAt(1); // get file name
		fileName = (String) nameList.get("1");
	}else{
		req.setAttribute("msg","对不起上传文件不存在请确认后重新上传！");
		//return mapping.findForward(this.formView);
	}
	
	try {
		Thread.sleep(100);
	} catch (InterruptedException e1) {
		e1.printStackTrace();
	}
	String path = scdrfilepath + File.separatorChar + fileName; // file full
	File file = new File(path);
	List<SsmnZyUser> ls = null;
	try{
		ls = parseExcel(file,req,res,1);
	}catch(Exception e){
		e.printStackTrace();
		//msg = e.getMessage();
	}	            
		            
  //处理
	if(ls != null && ls.size() >0)
	{
		List<SsmnZyUser> sucessList = new ArrayList<SsmnZyUser>();
		//批量解绑
		Iterator it = ls.iterator();
		while(it.hasNext())
		{
			SsmnZyUser zu = (SsmnZyUser) it.next();
			//判断用户是否存在
			String sres="";
			//重庆 IsHaveSsmnNum开关打开，并且副号码为空，要解绑
			if(ConfigMgr.getIsHaveSsmnNum().equals("1") && (zu.getSsmnnumber() ==null || zu.getSsmnnumber().equals("")))
			{
				sres ="";
				zu.setSsmnnumber(Constants.SSMN_NUM_DEFAULT);//将副号码存成11个0，因为数据库中是这样存的
			}
			else
				sres =uDao.isValidUser(zu.getMsisdn(),zu.getSsmnnumber());
			if(sres.equals(""))
			{
				//合法用户，解绑
				String suid = uDao.getSsmnzyuserId(zu.getMsisdn(),zu.getSsmnnumber());
				if(suid != null && !"".equals(suid))
				{
					//解绑
					boolean isok =uDao.cancelBindSsmnuber(zu.getMsisdn(),zu.getSsmnnumber(),suid);
					if(isok)
					{
						//解绑成功
						sucessList.add(zu);
						it.remove();
					}
				}else
					zu.setGroupname(sres);//暂用groupname字段记录错误信息
				
			}else
				zu.setGroupname(sres);//暂用groupname字段记录错误信息
		}
		
		//将两个数组生成excel导出
		String filenamebatchcan = createExcelDealbatch(sucessList,ls,scdrfilepath,1);
		if(filenamebatchcan != null && !"".equals(filenamebatchcan))
		{
			req.setAttribute("filenamebatchcan", filenamebatchcan);
			int sucesscount = 0;
			int faildcount = 0;
			if(sucessList != null && sucessList.size()>0)
				sucesscount = sucessList.size();
			if(ls != null && ls.size()>0)
				faildcount = ls.size();
			req.setAttribute("sucesscount", sucesscount);
			req.setAttribute("faildcount", faildcount);
			req.setAttribute("opertype", "解绑");
		}
	}else
	{
		req.setAttribute("msg", "导入的文件没有数据，或者数据格式有误!");
	}
}

//批量绑定
public void bindBatchDeal(HttpServletRequest req,HttpServletResponse res,SsmnZyLevel level)
{

	String scdrfilepath = req.getSession().getServletContext().getRealPath("batchDealUsers");//批量绑定导出文件存储
	SimpleDateFormat forma = new SimpleDateFormat("yyyyMMddHHmmss");
	List<SsmnZyUser> faildList = new ArrayList<SsmnZyUser>();
	File direx = new File(scdrfilepath);
	if (!direx.exists()) {
		if (!direx.mkdirs())
			System.out.println("创建文件失败");
	}
	int size = 1024 * 1024 * 10;// 上传文件限制为10M以内
	Vector uploadV = new Vector();
	Hashtable nameList = new Hashtable();
	try {
		uploadV = com.dascom.OPadmin.util.WebImportFile.Upload(req, scdrfilepath, size);
	} catch (Exception e) {
		logger.error("-----网络问题，导入群组失败-----");
		e.printStackTrace();
		req.setAttribute("msg","由于您的网络问题导入失败，请重试！");
		//return mapping.findForward(this.formView);
	}
	String fileName = null;
	if (uploadV.size() != 0) {
		nameList = (Hashtable) uploadV.elementAt(1); // get file name
		fileName = (String) nameList.get("1");
	}else{
		req.setAttribute("msg","对不起上传文件不存在请确认后重新上传！");
		//return mapping.findForward(this.formView);
	}
	
	try {
		Thread.sleep(100);
	} catch (InterruptedException e1) {
		e1.printStackTrace();
	}
	String path = scdrfilepath + File.separatorChar + fileName; // file full
	File file = new File(path);
	List<SsmnZyUser> ls = new ArrayList<SsmnZyUser>();
	try{
		ls = parseExcel(file,req,res,2);
	}catch(Exception e){
		e.printStackTrace();
		//msg = e.getMessage();
	}            
    
	 //处理
	if(ls != null && ls.size() >0)
	{
		//List<SsmnZyUser> sucessList = new ArrayList<SsmnZyUser>();
		//批量绑定
		Iterator<SsmnZyUser> it = ls.iterator();
		while(it.hasNext())
		{
			//String sres = "";
			SsmnZyUser zu = (SsmnZyUser) it.next();
			//判断员工编号，没有，数据不合法
			if(zu.getEmpno() == null || "".equals(zu.getEmpno()))
			{
				zu.setGroupname("数据不合法，没有员工编号!");
				faildList.add(zu);
				it.remove();
				continue;
			}
			
			Pattern p = Pattern.compile("^[0-9]{11}$");
			Matcher m = p.matcher(zu.getMsisdn());
			boolean b = m.matches();
			if(!b)
			{
				//主号码数据不合法
				zu.setGroupname("新增时，主号码数据不合法!");
				faildList.add(zu);
				it.remove();
				continue;
			}
			//判断主号码,没有主号码，数据不合法
			if(zu.getMsisdn() == null || "".equals(zu.getMsisdn()))
			{
				zu.setGroupname("数据不合法，没有主号码!");
				faildList.add(zu);
				it.remove();
				continue;
			}
			
			SsmnUser su = uDao.getssmnuserByMsisdn(zu.getMsisdn());
			List<SsmnZyUser> szulist = uDao.getssmnzyuserbyMsisdn(zu.getMsisdn());
			//根据员工编号查出用户
			List<SsmnZyUser> empnozulist = uDao.getSsmnzyuserByEmpno(zu.getEmpno(),level);
			if((su != null && szulist != null && szulist.size()>0 && (empnozulist == null || empnozulist.size()<=0)) || (su == null &&  ( szulist == null || szulist.size()<=0) && (empnozulist != null && empnozulist.size()>0)))
			{
				zu.setGroupname("数据不合法，员工编号与主号码不对应!");
				faildList.add(zu);
				it.remove();
				continue;
			}
			
			//判断分行行动组是否存在,主号码与员工编号都不存在
			if( su == null && (szulist == null || szulist.size() <=0) && (empnozulist==null || empnozulist.size()<=0)  && (zu.getBranchactiongroup() == null || "".equals(zu.getBranchactiongroup())))
			{
				zu.setGroupname("数据不合法，没有行动组!");
				faildList.add(zu);
				it.remove();
				continue;
			}
			Long levelid = leveldao.getLevelIDByActionGroup(zu.getBranchactiongroup());
			//当用户不存在的时候，如果行动组不存在，则不处理,当用户存在的时候，使用用户当前所在的行动组
			if(su == null && (szulist == null || szulist.size() <=0) && (empnozulist==null || empnozulist.size()<=0)   && levelid ==null)
			{
				zu.setGroupname("该行动组不存在!");
				faildList.add(zu);
				it.remove();
				continue;
			}
			
			//判断渠道是否存在
			if(zu.getChannelname() == null || "".equals(zu.getChannelname()))
			{
				zu.setGroupname("数据不合法，没有渠道!");
				faildList.add(zu);
				it.remove();
				continue;
			}
			SsmnZyChannel szc = cDao.getChannelByName(zu.getChannelname());
			if(szc == null)
			{
				zu.setGroupname("该渠道不存在!");
				faildList.add(zu);
				it.remove();
				continue;
			}
			new UtilFunctionDao().setEnablenumberType(szc.getId());//根据渠道type确定副号码的类型						
			
			//行动组和渠道都符合,判断主号码，副号码,
			if(zu.getSsmnnumber() != null && !"".equals(zu.getSsmnnumber()))
			{
				//副号码，主号码均不为空
				//判断该副号码与该主号码是否存在
				String isexist = uDao.isValidUser(zu.getMsisdn(),zu.getSsmnnumber());
				//判断副号码和渠道是否是同一类型的 A+副号码对应A+渠道，渠道副号码对应经纪人渠道
				List<SsmnZyEnablenumber> listen = enDao.getSsmnEnableNumber(zu.getSsmnnumber());
				if(listen !=null && listen.size()>0)
				{
					//类型不匹配，不处理　，报错
					if(listen.get(0).getType() != Constants.TYPE_ENABLENUMBER)
					{
						zu.setGroupname("副号码与渠道类型不匹配!");
						faildList.add(zu);
						it.remove();
						continue;
					}
				}else
				{
					zu.setGroupname("副号码不存在!");
					faildList.add(zu);
					it.remove();
					continue;
				}
				if("".equals(isexist))
				{
					//zu.setGroupname("该副号码已经绑定到此人");
					//faildList.add(zu);
					//it.remove();
					//这种情况改为更换渠道
					boolean isok = uDao.updateChannel("",szc.getId()+"" ,zu.getMsisdn(),zu.getSsmnnumber());
					if(isok)
						zu.setGroupname("变更渠道成功!");
					else
					{
						zu.setGroupname("变更渠道失败!");
						faildList.add(zu);
						it.remove();
					}
					continue;
				}
											
				//判断副号码是否有效
				//判断用户是否存在
				if(((su == null && szulist != null && szulist.size() <=0) || (su != null && (szulist == null || szulist.size() <=0))) && (empnozulist==null || empnozulist.size()<=0))
				{
					//如果用户不存在，以填写的行动组为区域分配
					//判断副号码是否是本片区的，如果不是，则提示副号码为其他片区,判断副号码是否与所选择渠道相匹配
					
					String sret = enDao.isEnableAreaSsmnnumber(zu.getSsmnnumber(),"",zu.getBranchactiongroup(),Constants.TYPE_ENABLENUMBER);
					
					if(sret != null && !sret.equals(""))
					{
						zu.setGroupname(sret);
						faildList.add(zu);
						it.remove();
						continue;
					}
					
					//注册（副号码为可用的，并且主号码不存在，重新注册）,新创建用户，不需要判断用户还有其他A+副号码
					SsmnZyUser u = new SsmnZyUser();
					u.setLevelid(levelid);
					u.setChannelid(szc.getId());
					u.setGroupname(zu.getBranchactiongroup());
					u.setIntime(new Date());
					u.setName(zu.getName());
					u.setMsisdn(zu.getMsisdn());
					u.setSsmnnumber(zu.getSsmnnumber());
					u.setEmpno(zu.getEmpno());
					u.setSecondMsisdn(StringUtils.defaultIfEmpty(zu.getSecondMsisdn(), ""));
					
					//注册业务，绑定副号码
					boolean success = uDao.registeSSMNService(zu.getMsisdn(), zu.getSsmnnumber(), u);
					if(!success)
					{
						zu.setGroupname("注册失败!");
						faildList.add(zu);
						it.remove();
						continue;
					}else
						continue;
					
				}
				//如果用户存在，以用户所在的区域判断副号码是否合法
				if(su != null && (szulist != null && szulist.size() >0)  && empnozulist !=null && empnozulist.size()>0 )
				{
					//判断副号码，以用户所在的区域判断副号码是否有效
					
					if(szulist != null && szulist.size()>0 && empnozulist != null && empnozulist.size()>0)
					{
						
						SsmnZyUser szu = szulist.get(0);
						SsmnZyUser emzu = (SsmnZyUser)empnozulist.get(0);
						if(szu != null && emzu != null && szu.getEmpno().equals(emzu.getEmpno())  && szu.getMsisdn().equals(emzu.getMsisdn()))
						{
							//在下面做
						}else
						{
							zu.setGroupname("该经纪人已经存在，但员工编号与主号码不对应!");
							faildList.add(zu);
							it.remove();
							continue;
						}
						//判断副号码是否是本片区的，如果不是，则提示副号码为其他片区
						String sret = enDao.isEnableAreaSsmnnumber(zu.getSsmnnumber(),szu.getId()+"",szu.getBranchactiongroup(),Constants.TYPE_ENABLENUMBER);
						if(sret != null && !sret.equals(""))
						{
							zu.setGroupname(sret);
							faildList.add(zu);
							it.remove();
							continue;
						}
						
					}else
					{
						zu.setGroupname("该用户不合法!");
						faildList.add(zu);
						it.remove();
						continue;
					}
					
					//注册（副号码为可用的，并且主号码已存在，给主号码再绑定此副号码）
					if(szulist != null && szulist.size()>0 && empnozulist !=null && empnozulist.size()>0)
					{
						SsmnZyUser szu = szulist.get(0);
						//判断副号码是A+副号码? 如果是，需要判断该主号下面还有其他A+副号码，如果有，不给绑定，如果没有做绑定
						List<SsmnZyEnablenumber> enli = enDao.getSsmnEnableNumber(zu.getSsmnnumber());
						if(enli !=null && enli.size()>0)
						{
							boolean checkUserA = true;
							if(enli.get(0).getType() == 2)
							{
								//是A+副号码，需要判断换绑的经纪人是否已存在A+副号码
								List<SsmnZyUser> listUser = uDao.checkOtherEnableNumberA(szu.getMsisdn(),2);
								if(listUser !=null && listUser.size()>0)
								{
									checkUserA = false;
									zu.setGroupname(Constants.MESSAGE_EXISTOTHERNUMBER);
									faildList.add(zu);
									it.remove();
									continue;
								}else
									checkUserA  = true;
							}
							if(checkUserA)
							{
								boolean isOk=uDao.bindSsmnNumber(szu.getMsisdn(),zu.getSsmnnumber(), szc.getId()+"", szu.getId()+"");
								if(!isOk)
								{
									zu.setGroupname("绑定失败!");
									faildList.add(zu);
									it.remove();
									continue;
								}else
									continue;
							}
						}else
						{
							zu.setGroupname("副号码无效!");
							faildList.add(zu);
							it.remove();
							continue;
						}
				}else
				{
					zu.setGroupname("该用户不合法!");
					faildList.add(zu);
					it.remove();
					continue;
				}
					
			}
		}
			//如果副号码为空
			if(su != null && szulist != null && szulist.size()>0 &&  empnozulist !=null && empnozulist.size()>0)
			{
				SsmnZyUser szu = szulist.get(0);
				SsmnZyUser emzu = empnozulist.get(0);
				if(szu != null && emzu != null && szu.getEmpno().equals(emzu.getEmpno())  && szu.getMsisdn().equals(emzu.getMsisdn()))
				{
					//在下面做
				}else
				{
					zu.setGroupname("该经纪人已经存在，但员工编号与主号码不对应!");
					faildList.add(zu);
					it.remove();
					continue;
				}
				//用户存在,副号码为空，随机分配一个副号码
				String senable="";
				if(Constants.TYPE_ENABLENUMBER ==2 && ConfigMgr.getIsHaveSsmnNum().equals("1"))//表示A+渠道
					senable =Constants.SSMN_NUM_DEFAULT;//给默认值，11个0
				else	
					senable= enDao.getEnableNumber(szu.getId()+"","",szu.getBranchactiongroup(),Constants.TYPE_ENABLENUMBER);//选取副号码
				if(senable != null && !"".equals(senable))
				{
					//判断用户是否存在其他A+副号码
					boolean checkUserA = true;
					if(Constants.TYPE_ENABLENUMBER == 2  )
					{
						//是A+副号码，需要判断换绑的经纪人是否已存在A+副号码
						List<SsmnZyUser> listUser = uDao.checkOtherEnableNumberA(szu.getMsisdn(),2);
						if(listUser !=null && listUser.size()>0)
						{
							checkUserA = false;
							zu.setGroupname(Constants.MESSAGE_EXISTOTHERNUMBER);
							faildList.add(zu);
							it.remove();
							continue;
						}else
							checkUserA  = true;
					}
					if(checkUserA)
					{
						boolean isOk=uDao.bindSsmnNumber(szu.getMsisdn(),senable, szc.getId()+"", szu.getId()+"");
						if(!isOk)
						{
							zu.setGroupname("绑定失败!");
							faildList.add(zu);
							it.remove();
							continue;
						}else
						{
							zu.setSsmnnumber(senable);
							continue;//绑定成功
						}
					}
				}else
				{
					zu.setGroupname("没有可用的副号码!");
					faildList.add(zu);
					it.remove();
					continue;
				}
			}
			
			//用户不存在，副号码为空，随机分配一个副号码
			if(((su == null && szulist != null && szulist.size() <=0) || (su != null && (szulist == null || szulist.size() <=0))) && (empnozulist ==null || empnozulist.size()<=0) )
			{
				String senable ="";
				if(Constants.TYPE_ENABLENUMBER ==2 && ConfigMgr.getIsHaveSsmnNum().equals("1"))//表示A+渠道
					senable =Constants.SSMN_NUM_DEFAULT;
				else
					senable= enDao.getEnableNumber("","",zu.getBranchactiongroup(),Constants.TYPE_ENABLENUMBER);//选取副号码
				if(senable != null && !"".equals(senable))
				{
					//注册（副号码为可用的，并且主号码不存在，重新注册）
					SsmnZyUser u = new SsmnZyUser();
					u.setLevelid(levelid);
					u.setChannelid(szc.getId());
					u.setGroupname(zu.getBranchactiongroup());
					u.setIntime(new Date());
					u.setName(zu.getName());
					u.setEmpno(zu.getEmpno());
					u.setMsisdn(zu.getMsisdn());
					u.setSecondMsisdn(StringUtils.defaultIfEmpty(zu.getSecondMsisdn(), ""));
					u.setSsmnnumber(senable);
					zu.setSsmnnumber(senable);								

					//注册业务，绑定副号码
					boolean success = uDao.registeSSMNService(zu.getMsisdn(), senable, u);
					if(!success)
					{
						zu.setGroupname("注册失败!");
						faildList.add(zu);
						it.remove();
						continue;
					}else
					{
						zu.setSsmnnumber(senable);
						continue;
					}
					
				}else
				{
					zu.setGroupname("没有可用的副号码!");
					faildList.add(zu);
					it.remove();
					continue;
				}
			}
			
		}	
			//将两个数组生成excel导出
			String filenamebatchcan = createExcelDealbatch(ls,faildList,scdrfilepath,2);
			if(filenamebatchcan != null && !"".equals(filenamebatchcan))
			{
				req.setAttribute("filenamebatchcan", filenamebatchcan);
				int sucesscount = 0;
				int faildcount = 0;
				if(ls != null && ls.size()>0)
					sucesscount = ls.size();
				if(faildList != null && faildList.size()>0)
					faildcount = faildList.size();
				req.setAttribute("sucesscount", sucesscount);
				req.setAttribute("faildcount", faildcount);
				req.setAttribute("opertype", "绑定");
			}
		else
		{
			req.setAttribute("msg", "导入的文件没有数据，或者数据格式有误!");
		}
	
	}else
	{
		req.setAttribute("msg", "导入的文件没有数据，或者数据格式有误!");
	}

}
	
//批量处理
public void batchDeal(HttpServletRequest req,HttpServletResponse res,SsmnZyLevel level)
{
    
	List<SsmnZyUser> newList = new ArrayList<SsmnZyUser>();//新建的用户
	List<SsmnZyUser> deleteList = new ArrayList<SsmnZyUser>();//删除的用户
	List<SsmnZyUser> faildList = new ArrayList<SsmnZyUser>();//处理失败的用户
	List<SsmnZyUser> changeChannelList = new ArrayList<SsmnZyUser>();//变更渠道的用户
	List<SsmnZyUser> noChangeList = new ArrayList<SsmnZyUser>();//不变的用户
	
	List<SsmnZyUser> listTempD = new ArrayList<SsmnZyUser>();//暂存数据库中的数据
	List<SsmnZyUser> listTempE = new ArrayList<SsmnZyUser>();//暂存导入的表中的数据
	
	String scdrfilepath = req.getSession().getServletContext().getRealPath("batchDealUsers");//批量(绑定)导出文件存储
	SimpleDateFormat forma = new SimpleDateFormat("yyyyMMddHHmmss");
	
	File direx = new File(scdrfilepath);
	if (!direx.exists()) {
		if (!direx.mkdirs())
			System.out.println("创建文件失败");
	}
	int size = 1024 * 1024 * 10;// 上传文件限制为10M以内
	Vector uploadV = new Vector();
	Hashtable nameList = new Hashtable();
	try {
		uploadV = com.dascom.OPadmin.util.WebImportFile.Upload(req, scdrfilepath, size);
	} catch (Exception e) {
		logger.error("-----网络问题，上传批量处理数据失败-----");
		e.printStackTrace();
		req.setAttribute("msg","由于您的网络问题导入失败，请重试！");
		//return mapping.findForward(this.formView);
	}
	String fileName = null;
	if (uploadV.size() != 0) {
		nameList = (Hashtable) uploadV.elementAt(1); // get file name
		fileName = (String) nameList.get("1");
	}else{
		req.setAttribute("msg","对不起上传文件不存在请确认后重新上传！");
		//return mapping.findForward(this.formView);
	}
	
	try {
		Thread.sleep(100);
	} catch (InterruptedException e1) {
		e1.printStackTrace();
	}
	String path = scdrfilepath + File.separatorChar + fileName; // file full
	File file = new File(path);
	List<SsmnZyUser> ls = new ArrayList<SsmnZyUser>();
	try{
		ls = parseExcelDeal(file,req,res);
	}catch(Exception e){
		e.printStackTrace();
		//msg = e.getMessage();
	} 
	
	 //处理
	if(ls != null && ls.size() >0)
	{
		//查出所有的用户信息
		List<SsmnZyUser> listAll = uDao.getZyUserInfoByMsisdn("",level);
		List<SsmnZyUser> listRemoveE = new ArrayList<SsmnZyUser>();//暂存ls 中处理完应该删除的数据，因为iterator不支持遍历的时间删除或增加，所以只能暂存一下
		List<SsmnZyUser> listRemoveD = new ArrayList<SsmnZyUser>();	//暂存listAll　中处理完应该删除的数据			
		
		//先把导入的表中没有的,数据库中有的，解绑掉，腾出副号码，还有：　若导入的表中的主号码，员工编号给换掉了，这样做，也按注销处理掉了，表中再新增
		listRemoveD.clear();
		List<SsmnZyUser> listAllTemp = new ArrayList<SsmnZyUser>();
		listAllTemp.addAll(listAll);
		Iterator<SsmnZyUser> itrdmove = listAllTemp.iterator();
		while(itrdmove.hasNext())
		{
			SsmnZyUser zurdmove = itrdmove.next();
			
			Iterator<SsmnZyUser> itremove = ls.iterator();
			while(itremove.hasNext())
			{
				SsmnZyUser zuremove = itremove.next();
				if(zurdmove.getEmpno() !=null && zurdmove.getEmpno().length()>0 &&
						zuremove.getEmpno() !=null && zuremove.getEmpno().length()>0 
						&& zurdmove.getEmpno().toUpperCase().equals(zuremove.getEmpno().toUpperCase()))
				{
					listRemoveD.add(zurdmove);
					itrdmove.remove();
					break;								
				}
					
			}
									
		}
		
		//注销 listRemoveD中的数据
		if(listAllTemp !=null && listAllTemp.size()>0)
		{
			listAll.removeAll(listAllTemp);//把已经注销的数据，从listAll中删除，下面处理其他数据
			List<SsmnZyUser> listRemoveResult = new ArrayList<SsmnZyUser>();
			deleteFunction(listAllTemp,deleteList,faildList,listRemoveResult,level);
			
		}
//--------------------------------------------------------------------------------------------------					
		listRemoveD.clear();
		Iterator<SsmnZyUser> it = ls.iterator();
		while(it.hasNext())
		{
			SsmnZyUser zu = it.next();
			
			//比如有两条数据的，最下面只删除了一条，所以这里要判断，如果前面已经处理过了，则这里直接删除，不再处理
			int indexE = listRemoveE.indexOf(zu);
			if(indexE >=0)//找到是大于等于0
			{
				it.remove();
				listRemoveE.remove(zu);
				continue;
			}
										
			//判断员工编号，没有，数据不合法
			if(zu.getEmpno() == null || "".equals(zu.getEmpno()))
			{
				zu.setGroupname("数据不合法，没有员工编号!");
				faildList.add(zu);
				it.remove();
				continue;
			}
			
			//从库中查，是否存在此用户，根据员工编号
			Iterator<SsmnZyUser> itData = listAll.iterator();
			listTempD.clear();//暂存数组，清空一下
			listTempE.clear();//暂存导入的表中的数据
			while(itData.hasNext())
			{
				SsmnZyUser dzu = itData.next();
				if(dzu.getEmpno()!= null && dzu.getEmpno().length()>0 && dzu.getEmpno().toUpperCase().equals(zu.getEmpno().toUpperCase()))
				{
					listTempD.add(dzu);
				}	
			}
			
			//从文件中查，所有此员工编号的记录
			for(int itd = 0;itd<ls.size();itd++)
			{
				SsmnZyUser izu = ls.get(itd);
				if(izu.getEmpno()!= null && izu.getEmpno().length()>0 && izu.getEmpno().toUpperCase().equals(zu.getEmpno().toUpperCase()))
				{
					listTempE.add(izu);//这个肯定是有数据的,因为轮循到此条了
				}
			}
			
			//如果 listTempD有数据，说明该用户已经存在,只比对渠道　
//----开始----筛选不变的-----------------------------------------------------------------------------------------------
				Iterator<SsmnZyUser> ite = listTempE.iterator(); 
				while(ite.hasNext())
				{
					SsmnZyUser tzue = ite.next();
					
					Iterator<SsmnZyUser> itd = listTempD.iterator();
					while(itd.hasNext())
					{
						SsmnZyUser tzud = itd.next();
						
						//先判断与操作员的公司级别是否一致，这里只判断到公司级别，这里员工编号一定一样的
						if(tzud.getCompany() !=null && tzud.getCompany().length()>0 && 
								level.getCompany()!=null && level.getCompany().length()>0 &&
								tzud.getCompany().equals(level.getCompany()))
						{
							//只处理与操作员同一公司的数据
							//员工编号肯定一样，比较渠道
							//把员工编号和渠道不变的，筛选出来
							if(tzue.getChannelname()!=null && tzue.getChannelname().length()>0 && tzud.getChannelname() != null 
									&& tzud.getChannelname().length()>0 && tzue.getChannelname().equals(tzud.getChannelname()))
							{
								System.out.print("======id:  "+tzue.getId()+"\n");
								//渠道一致
								noChangeList.add(tzud);//放不变的数组里,选择放tzud，是因为这里有副号码
								itd.remove();
								listRemoveD.add(tzud);
								ite.remove();
								listRemoveE.add(tzue);
								break;
							}else
							{
								//渠道不一致,检查渠道是否存在，如果不存在，直接提示不存在，如果存在，后面处理
								SsmnZyChannel schan = cDao.getChannelByName(tzue.getChannelname());
								if(schan == null)
								{
									tzue.setGroupname("该渠道不存在!");
									faildList.add(tzue);
									itd.remove();
									listRemoveD.add(tzud);
									ite.remove();
									listRemoveE.add(tzue);
									break;
								}
							}
						}else
						{
							tzue.setGroupname("不是该公司的用户!");
							faildList.add(tzue);//放不变的数组里,选择放tzud，是因为这里有副号码
							itd.remove();
							listRemoveD.add(tzud);
							ite.remove();
							listRemoveE.add(tzue);
							break;
						}
					}
				}
				
				//System.out.print("listAll.size: "+listAll.size()+"   ls.size:"+ls.size()+"\n");
//----结束----筛选不变的-------------------------------------------------------------------------------------------------							
				//去除完渠道一致的情况
				//比对渠道不一致的
				//若两个数组个数一样，则变更渠道 ,以导入的表listTempE的条数为主
//-------开始------变更渠道-----------------------------------------------------------------------------------------------								
					Iterator<SsmnZyUser> itae = listTempE.iterator(); 
					while(itae.hasNext())
					{
						SsmnZyUser tzuae = itae.next();
						Iterator<SsmnZyUser> itad = listTempD.iterator();
						boolean flagSameMsisdn = false;//是否有主号码一致，变更渠道的时候，如果有主号码一致的，最好要对应,如果没有，则随机对应
						SsmnZyUser zuSameMsisdn = null;//若找到主号码一致的，暂存一下
						SsmnZyChannel szc = null;//渠道
						while(itad.hasNext())
						{
							SsmnZyUser tzuad = itad.next();
							
							//两个数组中都存在的，　变更渠道 
							//先判断表中给出的渠道，是否存在
							if(tzuae.getChannelname() ==null || tzuae.getChannelname().length() <=0)
							{
								//渠道不存在
								tzuae.setGroupname("变更渠道，数据不合法，没有填写渠道!");
								faildList.add(tzuae);
								itae.remove();
								listRemoveD.add(tzuad);
								itad.remove();
								listRemoveE.add(tzuae);
								break;
							}

							szc = cDao.getChannelByName(tzuae.getChannelname());
							if(szc == null)
							{
								tzuae.setGroupname("变更渠道，该渠道不存在!");
								faildList.add(tzuae);
								itae.remove();
								listRemoveD.add(tzuad);
								itad.remove();
								listRemoveE.add(tzuae);
								break;
							}
							//根据渠道id设置副号码类型
							new UtilFunctionDao().setEnablenumberType(szc.getId());
							
							if(tzuae.getMsisdn() != null && tzuae.getMsisdn().length()>0 && tzuad.getMsisdn()!=null &&
									tzuad.getMsisdn().length()>0 && tzuad.getMsisdn().equals(tzuae.getMsisdn()))
							{
								flagSameMsisdn = true;
								zuSameMsisdn = tzuad;
								break;
							}
							
						}
						if(listTempD != null && listTempD.size()>0)
						{
							if(flagSameMsisdn && zuSameMsisdn!= null)
							{
								//有主号码相同的,使用zuSameMsisdn
							}else
								zuSameMsisdn = listTempD.get(0);
							
							//判断与操作员是否在同一公司
							if(zuSameMsisdn.getCompany() !=null && zuSameMsisdn.getCompany().length()>0 && 
									level.getCompany()!=null && level.getCompany().length()>0 &&
									zuSameMsisdn.getCompany().equals(level.getCompany()))
							{
								//首先判断要变更的渠道类型与该用户的副号码是同一类型的不？如果不是，提示错误不处理，如果是，则处理　
								List<SsmnZyEnablenumber> lisen = enDao.getSsmnEnableNumber(zuSameMsisdn.getSsmnnumber());
								if(lisen !=null && lisen.size()>0)
								{
									//判断副号码类型与渠道类型是否匹配
									if(lisen.get(0).getType() != Constants.TYPE_ENABLENUMBER)
									{
										//不匹配
										tzuae.setGroupname("变更的渠道类型与该用户的副号码类型不匹配!");
										faildList.add(tzuae);
										itae.remove();
										listRemoveD.add(zuSameMsisdn);
										itad.remove();
										listRemoveE.add(tzuae);
										continue;
									}
								}else
								{
									tzuae.setGroupname("数据有错误!");
									faildList.add(tzuae);
									itae.remove();
									listRemoveD.add(zuSameMsisdn);
									itad.remove();
									listRemoveE.add(tzuae);
									continue;
								}
								//变更的渠道必须与该渠道是同一类型的
								SsmnZyChannel zyold = cDao.getChannelById(zuSameMsisdn.getChannelid());
								if(zyold !=null && zyold.getType().longValue() != szc.getType().longValue())
								{
									//不匹配
									tzuae.setGroupname("变更的渠道类型与该用户的渠道类型不匹配!");
									faildList.add(tzuae);
									itae.remove();
									listRemoveD.add(zuSameMsisdn);
									itad.remove();
									listRemoveE.add(tzuae);
									continue;
								}
								//变更渠道　，因为上面已经去掉渠道一样的了，这里肯定渠道都不一样了，所以，直接变渠道
								boolean isok = uDao.updateChannel(zuSameMsisdn.getId()+"",szc.getId()+"" ,"","");
								if(isok)
								{
									//变更渠道成功!
									SsmnZyLevel zl=leveldao.getBranchactiongroupNameById(zuSameMsisdn.getLevelid()+"");
									if(zl !=null)//这里肯定不为空
									{
										//结果文档中存　最终更换完的结果
										tzuae.setBranchactiongroup(zl.getBranchactiongroup());
										tzuae.setArea(zl.getArea());
										tzuae.setWarzone(zl.getWarzone());
										tzuae.setBusinessdepartment(zl.getBusinessdepartment());
										tzuae.setCompany(zl.getCompany());
										tzuae.setProvincecity(zl.getProvincecity());
									}
									tzuae.setName(zuSameMsisdn.getName());//姓名
									tzuae.setSsmnnumber(zuSameMsisdn.getSsmnnumber());//副号码
									changeChannelList.add(tzuae);//放变更渠道数组里
									itae.remove();
									listRemoveD.add(zuSameMsisdn);
									itad.remove();
									listRemoveE.add(tzuae);
									continue;
								}
								else
								{
									tzuae.setGroupname("变更渠道失败!");
									faildList.add(tzuae);
									itae.remove();
									listRemoveD.add(zuSameMsisdn);
									itad.remove();
									listRemoveE.add(tzuae);
									continue;
								}
							}else
							{
								tzuae.setGroupname("不是该公司的用户!");
								faildList.add(tzuae);
								itae.remove();
								listRemoveD.add(zuSameMsisdn);
								itad.remove();
								listRemoveE.add(tzuae);
								continue;
							}
						}
					}
//-----结束--------变更渠道-----------------------------------------------------------------------------------------------								
//------开始-----注销--------------------------------------------------------------------------------------------	
					//先注销，否则，有可能影响下面的注册
					//listTempD剩于的，要注销掉
					if(listTempD != null && listTempD.size() >0)
					{
						deleteFunction(listTempD,deleteList,faildList,listRemoveD,level);
					}
//------结束-----注销--------------------------------------------------------------------------------------------		
//------开始-----注册--------------------------------------------------------------------------------------------	
					//分别判断，　listTempE剩于的，要注册
					if(listTempE != null && listTempE.size()>0)
					{
						reginFunction(listTempE,faildList,newList,listRemoveE,level);
					}
//------结束-----注册--------------------------------------------------------------------------------------------									
					
			//统一删除　ls 和listAll中的数据（这些数据在上面已经处理完，避免重复处理，所以这里要删除）
			//删除ls 中的处理过的数据，只能使用iterator中的remove
			int indexF = listRemoveE.indexOf(zu);
			if(indexF >=0)//找到是大于等于0
			{
				it.remove();
				listRemoveE.remove(zu);
			}
			//删除　listAll中处理过的数据,listAll这里可以统一删除，因为这个没有遍历
			listAll.removeAll(listRemoveD);
			listRemoveD.clear();
		}
		
		System.out.print("ls.size :  "+ls.size()+"   listAll.size:  "+listAll.size());
		//把ls中剩下的就是注册的
		if(ls != null && ls.size() >0)
		{
			reginFunction(ls,faildList,newList,listRemoveE,level);
			
		}

		//分别判断，　listAll剩于的，要注销
		if(listAll != null && listAll.size()>0)
		{
			deleteFunction(listAll,deleteList,faildList,listRemoveD,level);
		}
		
		//将两个数组生成excel导出
		String filenamebatchcan = createExcelDealbatchNew(noChangeList,changeChannelList,newList,deleteList,faildList,scdrfilepath);
		if(filenamebatchcan != null && !"".equals(filenamebatchcan))
		{
			req.setAttribute("filenamebatchcan", filenamebatchcan);
			int sucesscount = 0;
			int faildcount = 0;
			//不变的
			if(noChangeList != null && noChangeList.size()>0)
				sucesscount += noChangeList.size();
			//变更渠道
			if(changeChannelList != null && changeChannelList.size() >0)
				sucesscount +=changeChannelList.size();
			//新增的
			if(newList !=null && newList.size()>0)
				sucesscount +=newList.size();
			//解绑的
			if(deleteList !=null && deleteList.size()>0)
				sucesscount += deleteList.size();
			
			if(faildList != null && faildList.size()>0)
				faildcount = faildList.size();
			req.setAttribute("sucesscount", sucesscount);
			req.setAttribute("faildcount", faildcount);
			req.setAttribute("opertype", "绑定");
		}
		else
		{
			req.setAttribute("msg", "导入的文件没有数据，或者数据格式有误!");
		}
	}else
	{
		req.setAttribute("msg", "导入的文件没有数据，或者数据格式有误!");
	}

}
	
	/*
	 * 处理上传的文件
	 * flag: 1:批量解绑
	 * 	     2:批量绑定
	 */
	//适用于2003的excel 
public List<SsmnZyUser> parseExcel(File file,HttpServletRequest req,HttpServletResponse res,int flag)
{
	try { 
		Workbook book=null;
	    try {
			book = Workbook.getWorkbook(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Sheet sheet = book.getSheet(0);
		if(flag == 1)//解绑
		{
			if(/*!("姓名").equals(sheet.getCell(4, 0).getContents())||*/
				!("主号码").equals(sheet.getCell(6, 0).getContents())||
				!("副号码").equals(sheet.getCell(7, 0).getContents())	) {
				return null;
			}
		}
		if(flag == 2)//绑定
		{
			if(!("姓名").equals(sheet.getCell(4, 0).getContents())||
					!("员工编号").equals(sheet.getCell(5, 0).getContents())||
					!("主号码").equals(sheet.getCell(6, 0).getContents())||					
					!("副号码").equals(sheet.getCell(7, 0).getContents()) ||
					!("渠道").equals(sheet.getCell(8, 0).getContents()) ||
					!("所属门店").equals(sheet.getCell(3, 0).getContents())) {
					return null;
				}
		}
			
		List<SsmnZyUser> ulist = new ArrayList<SsmnZyUser>();
		
		int colnum = 9;
		if(ConfigMgr.getIsAddSecondMsisdn().equals("1")) //在最后一列添加第二联系人一列
			colnum = 10;
		int rownum = sheet.getRows();
		System.out.println(rownum);
		String matchMsisdn = "^(13|15|18)\\d{9}$";
		String matchAge = "^[0-9.-]*$";
		String temptext = null;
		
		List<Integer> fal=new ArrayList<Integer>();  
		
		for(int i = 1; i < rownum; i ++) {
			
			String  sxcname= "";//姓名
			String sempno = "";//员工编号
			String sxcmsisdn = "";//主号码
			String sxcssmnnum = "";//副号码
			String sxcchannel= "";//渠道
			String sxbusiness = "";//事业部
			String sxwazone ="";//战区
			String sxarea ="";//片区
			String sxcbran = "";//行动组
			String sxsecondmsisdn = "";//第二联系人列,目前只有广州有
			SsmnZyUser us = new SsmnZyUser();
			for(int j = 0; j < colnum; j ++)
			{
				Cell cell = sheet.getCell(j,i);	
				
				if(j == 0){ //事业部
					sxbusiness= cell.getContents().trim();
					us.setBusinessdepartment(sxbusiness);
				}
				else if(j == 1){ //战区
					
					sxwazone = cell.getContents().trim();
					us.setWarzone(sxwazone);
				}else if(j ==2)
				{
					//片区
					sxarea = cell.getContents().trim();
					us.setArea(sxarea);
				}else if(j ==3)
				{
					//行动组
					sxcbran = cell.getContents().trim();
					us.setBranchactiongroup(sxcbran);
				}else if(j ==4)
				{
					//姓名
					sxcname = cell.getContents().trim();
					us.setName(sxcname);
				}else if(j == 5)
				{ 
					//员工编号
					sempno = cell.getContents().trim();
					if(sempno != null && !"".equals(sempno))
						sempno = sempno.toUpperCase();
					us.setEmpno(sempno);
				}else if(j == 6)
				{
					//主号码
					sxcmsisdn = cell.getContents().trim();
					us.setMsisdn(sxcmsisdn);
				}else if(j == 7)
				{
					//副号码
					sxcssmnnum = cell.getContents().trim();
					us.setSsmnnumber(sxcssmnnum);
				}else if(j == 8)
				{
					//渠道
					sxcchannel = cell.getContents().trim();
					us.setChannelname(sxcchannel);
				}
				if(ConfigMgr.getIsAddSecondMsisdn().equals("1")) //在最后一列添加第二联系人一列
				{
					if(j == 9)
					{
						//第二联系人列
						sxsecondmsisdn = cell.getContents().trim();
						us.setSecondMsisdn(sxsecondmsisdn);
					}
				}
	
			}
			if((sxbusiness == null || "".equals(sxbusiness)) && (sxwazone == null || "".equals(sxwazone)) && (sxarea == null || "".equals(sxarea)) && 
					(sxcbran == null || "".equals(sxcbran)) &&	(sxcname == null || "".equals(sxcname)) && (sempno == null || "".equals(sempno)) && (sxcmsisdn == null || "".equals(sxcmsisdn))
					&& (sxcssmnnum == null || "".equals(sxcssmnnum)) && (sxcchannel == null || "".equals(sxcchannel))  )
				break;
			ulist.add(us);
		}


		return ulist;
	} catch (BiffException e) {
		e.printStackTrace();
	}
	
	return null;
}

/*
 * 统一处理批量上传的文件
 * 1. 所有的字段，要去掉尾部空格
 * 2.手机号码要判断（正则表达式判断）
 *  
 *  */
//适用于2003的excel 
public List<SsmnZyUser> parseExcelDeal(File file,HttpServletRequest req,HttpServletResponse res)
{
try { 
	Workbook book=null;
    try {
		book = Workbook.getWorkbook(file);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	Sheet sheet = book.getSheet(0);
	
	if(	!("所属区域").equals(sheet.getCell(0, 0).getContents())||
		!("所属战区").equals(sheet.getCell(1, 0).getContents()) || 
		!("所属片区").equals(sheet.getCell(2, 0).getContents())||
		!("所属门店").equals(sheet.getCell(3, 0).getContents())||
		!("姓名").equals(sheet.getCell(4, 0).getContents())||
		!("员工编号").equals(sheet.getCell(5, 0).getContents())||					
		!("主号码").equals(sheet.getCell(6, 0).getContents()) ||
		!("渠道").equals(sheet.getCell(7, 0).getContents()))
	{
			return null;
	}
		
	List<SsmnZyUser> ulist = new ArrayList<SsmnZyUser>();
	
	int colnum = 8;
	int rownum = sheet.getRows();
	//List<Integer> fal=new ArrayList<Integer>();  
	
	for(int i = 1; i < rownum; i ++) {
		
		String  sxcname= "";//姓名
		String sempno = "";//员工编号
		String sxcmsisdn = "";//主号码
		//String sxcssmnnum = "";//副号码
		String sxcchannel= "";//渠道
		String sxbusiness = "";//事业部
		String sxwazone ="";//战区
		String sxarea ="";//片区
		String sxcbran = "";//行动组
		SsmnZyUser us = new SsmnZyUser();
		us.setId(Long.parseLong(i+""));
		for(int j = 0; j < colnum; j ++)
		{
			Cell cell = sheet.getCell(j,i);	
			
			if(j == 0)
			{ 
				//事业部
				sxbusiness= cell.getContents().trim();
				us.setBusinessdepartment(sxbusiness);
			}
			else if(j == 1)
			{ 
				//战区
				sxwazone = cell.getContents().trim();
				us.setWarzone(sxwazone);
			}
			else if(j ==2)
			{
				//片区
				sxarea = cell.getContents().trim();
				us.setArea(sxarea);
			}
			else if(j ==3)
			{
				//行动组
				sxcbran = cell.getContents().trim();
				us.setBranchactiongroup(sxcbran);
			}
			else if(j ==4)
			{
				//姓名
				sxcname = cell.getContents().trim();
				us.setName(sxcname);
			}
			else if(j == 5)
			{ 
				//员工编号
				sempno = cell.getContents().trim();
				if(sempno != null && !"".equals(sempno))
					sempno = sempno.toUpperCase();
				us.setEmpno(sempno);
			}
			else if(j == 6)
			{
				//主号码
				sxcmsisdn = cell.getContents().trim();
				us.setMsisdn(sxcmsisdn);					
			}
			else if(j == 7)
			{
				//渠道
				sxcchannel = cell.getContents().trim();
				us.setChannelname(sxcchannel);
			}

		}
		if((sxbusiness == null || "".equals(sxbusiness)) && (sxwazone == null || "".equals(sxwazone)) && (sxarea == null || "".equals(sxarea)) && 
				(sxcbran == null || "".equals(sxcbran)) &&	(sxcname == null || "".equals(sxcname)) && (sempno == null || "".equals(sempno)) && (sxcmsisdn == null || "".equals(sxcmsisdn))
				 && (sxcchannel == null || "".equals(sxcchannel))  )
			break;
		ulist.add(us);
	}

	return ulist;
} catch (BiffException e) {
	e.printStackTrace();
}

return null;
}


/*适用于 2007 excel*/
	/*public List<SsmnZyUser> parseExcel(HttpServletRequest req,String scdrfilepath,int flag)
	{
		List<SsmnZyUser> list = null;
		
		XSSFWorkbook readwb = null;  
		InputStream inFileStrean = null;
		BufferedInputStream in = null;
				
		int filesize = 30*1024000; // 上传文件大小限制
		Vector uploadV = new Vector();
		//先上传
		try{
			uploadV = UploadFile.Upload(req, scdrfilepath, filesize);
		}catch (Exception e){
			logger.error("-----网络问题，上传图片失败-----");
			e.printStackTrace();
			//req.setAttribute("msg","由于您的网络问题导入失败，请重试！");
			
		}
		if (uploadV.size() != 0) {
			Hashtable paramList = (Hashtable) uploadV.elementAt(1);
			String filename  = (String) paramList.get("1");
			if(filename != null && filename.length()>0)
			{
				list = new ArrayList<SsmnZyUser>();
				//读取文件
				 try    
			        {
					 //构建Workbook对象, 只读Workbook对象   		  
			            //直接从本地文件创建Workbook   
					 	inFileStrean =new FileInputStream(scdrfilepath+File.separator+filename);
			            in = new BufferedInputStream(inFileStrean);
			            
			            readwb = new XSSFWorkbook(in);
			            XSSFSheet readsheet = readwb.getSheetAt(0);
			            int rsRows = readsheet.getLastRowNum();
			            //获取指定单元格的对象引用 		  
			            for (int i = 0; i <= rsRows; i++)   
			            {   
			            	//创建一个行对象
			            	XSSFRow row = readsheet.getRow(i);
			            	if(row == null)
			            		continue;
			            	// 如果已经开始读取，那么忽略第一行(表头)
							if (i == 0 ) {
								continue;
							}
			            	//把一行里的每一个字段遍历出来
			            	//解绑只取１（主号码），２（副号码）列，姓名不用管
							SsmnZyUser us = new SsmnZyUser();
							if(flag == 1)//解绑
							{
								String sxcname = getcellvalue(row.getCell(0));
								String sxcmsisdn = getcellvalue(row.getCell(1));
								String sxcssmnnum  = getcellvalue(row.getCell(2));
								us.setMsisdn(sxcmsisdn);
								us.setSsmnnumber(sxcssmnnum);
								us.setName(sxcname);
								System.out.println(sxcmsisdn+"====="+sxcssmnnum);
							}else
							{
								String sxcname = getcellvalue(row.getCell(0));//姓名
								String sxcmsisdn = getcellvalue(row.getCell(1));//主号码
								String sxcssmnnum  = getcellvalue(row.getCell(2));//副号码
								String sxcchannel = getcellvalue(row.getCell(3));//渠道
								String sxcbran = getcellvalue(row.getCell(4));//行动组
								us.setMsisdn(sxcmsisdn);
								us.setSsmnnumber(sxcssmnnum);
								us.setName(sxcname);
								us.setChannelname(sxcchannel);
								us.setBranchactiongroup(sxcbran);
							}
							list.add(us);
			            	//
			            	/*for(int j=0;j<row.getLastCellNum();j++) 
			            	{
				            	//创建一个行里的一个字段的对象，也就是获取到的一个单元格中的值
				            	XSSFCell cell = row.getCell(j);
				            	//在这里我们就可以做很多自己想做的操作了，比如往数据库中添加数据等
				            	System.out.println(cell.getRichStringCellValue());
			            	}*/
			            	//System.out.println("=================================================");
			  
/*			            }
			        } catch (Exception e) {   
			            e.printStackTrace(); 
			           // req.setAttribute("msg", "批量解绑失败!");
			  
			        } finally {   
			        	if(inFileStrean != null)
							try {
								inFileStrean.close();
								if(in != null)
					        		in.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			        	
			        } 
			}
		}
		
		return list;
	}*/
	
	public String getcellvalue(XSSFCell cell)
	{
		String res = "";
		if(cell == null)
			return res;
		
		if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) 
    	{
			res = String.valueOf(cell.getNumericCellValue());
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) 
		{
			res = String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) 
		{
			res = String.valueOf(cell.getCellFormula());
		} else {
			res = cell.getStringCellValue();
		}
		return res;
	}
	
public String 	createExcelDealbatch(List<SsmnZyUser> listSucess,List<SsmnZyUser> listFaild,String filepath,int type)
{
	String res = "";
	if((listSucess == null || listSucess.size()<=0) && (listFaild == null || listFaild.size()<=0) )
		return res;
	
	File direx = new File(filepath);
	if (!direx.exists()) {
		if (!direx.mkdirs())
		{
			System.out.println("创建文件失败");
			return res;
		}
	}
	SimpleDateFormat forma = new SimpleDateFormat("yyyyMMddHHmmss");
	String fname ="";
	if(type == 1)//解绑
		fname= "批量解绑结果_"+forma.format(new Date())+".xls";
	else//绑定
		fname= "批量绑定结果_"+forma.format(new Date())+".xls";
	String sfilepath = filepath +File.separator+fname;
	//先判断文件是否存在
	File diryes = new File(sfilepath);
	if (diryes.exists()) {
		System.out.println("文件已经存在");
	}else
	{
		//创建文件
		FileOutputStream fileoutput = null;
		try {
			fileoutput = new FileOutputStream(sfilepath);
			int firstcount = 0;
		     HSSFWorkbook wboutput = new HSSFWorkbook();
		     
		     //内容样式
		     HSSFCellStyle cellStylecontent = wboutput.createCellStyle();
		     HSSFFont fontcontent = wboutput.createFont();
		     fontcontent.setFontHeightInPoints((short) 11);
		     cellStylecontent.setFont(fontcontent);
		     
		     HSSFCellStyle cellStyleFont = wboutput.createCellStyle();
			 HSSFFont font2 = wboutput.createFont();
		     font2.setFontName("宋体");
		     font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		     font2.setFontHeightInPoints((short) 11);
		     
		     //成功sheet
		     //总的数据
		     HSSFSheet sheetoutputFirst = wboutput.createSheet("成功记录");
		     HSSFRow rowtitle0 = sheetoutputFirst.createRow(0);
		     firstcount++;
		  		
		    // if(type == 1)
		    	 new UtilFunctionDao().createBatchTitle(0,rowtitle0, cellStyleFont,true,type);
		    // else
		    	// new UtilFunctionDao().createbindBatchTitle(0, rowtitle0, cellStyleFont,true);
		     
			for(int i=0;i<listSucess.size();i++){
				SsmnZyUser info = listSucess.get(i);
				HSSFRow rowcdrnum = sheetoutputFirst.createRow(i+1);
				//if(type == 1)
					new UtilFunctionDao().createBatchContent(i+1,rowcdrnum, info, cellStylecontent,true,type);
				//else
					//new UtilFunctionDao().createBindBatchContent(i+1,rowcdrnum, info, cellStylecontent,true);
			}
			
			//失败的sheet
			firstcount = 0;
			 HSSFSheet sheetoutputfaild = wboutput.createSheet("失败记录");
			 HSSFRow rowtitle = sheetoutputfaild.createRow(0);
			 firstcount++;
			 
			// if(type == 1)
				 new UtilFunctionDao().createBatchTitle(0,rowtitle, cellStyleFont,false,type);
			// else
			//	 new UtilFunctionDao().createbindBatchTitle(0,rowtitle, cellStyleFont,false);
				 
			 for(int i=0;i<listFaild.size();i++){
					SsmnZyUser info = listFaild.get(i);
					HSSFRow rowcdrnum = sheetoutputfaild.createRow(i+1);
					//if(type == 1)
						new UtilFunctionDao().createBatchContent(i+1,rowcdrnum, info, cellStylecontent,false,type);
					//else
					//	new UtilFunctionDao().createBindBatchContent(i+1,rowcdrnum, info, cellStylecontent,false);
				}
			
		wboutput.write(fileoutput);
		fileoutput.close();
		res = fname;
		 
	} catch (IOException e) {
		e.printStackTrace();
		logger.error("----批量解绑数据发生异常-----",e);
		
	}
	}
	
	return res;
	
}

public String createExcelDealbatchNew(List<SsmnZyUser> noChangeList,List<SsmnZyUser> changeChannelList,List<SsmnZyUser> newList,List<SsmnZyUser> deleteList,List<SsmnZyUser> faildList,String filepath)
{
	String res = "";
	if((noChangeList == null || noChangeList.size()<=0) && (changeChannelList == null || changeChannelList.size()<=0)
			&& (newList == null || newList.size()<=0) && (deleteList == null || deleteList.size()<=0)
			&& (faildList == null || faildList.size()<=0))
		return res;
	
	File direx = new File(filepath);
	if (!direx.exists()) {
		if (!direx.mkdirs())
		{
			System.out.println("创建文件失败");
			return res;
		}
	}
	SimpleDateFormat forma = new SimpleDateFormat("yyyyMMddHHmmss");
	String fname= "批量处理结果_"+forma.format(new Date())+".xls";
	
	String sfilepath = filepath +File.separator+fname;
	//先判断文件是否存在
	File diryes = new File(sfilepath);
	if (diryes.exists()) {
		System.out.println("文件已经存在");
	}else
	{
		//创建文件
		FileOutputStream fileoutput = null;
		try {
			fileoutput = new FileOutputStream(sfilepath);
			int firstcount = 0;
		     HSSFWorkbook wboutput = new HSSFWorkbook();
		     
		     //内容样式
		     HSSFCellStyle cellStylecontent = wboutput.createCellStyle();
		     HSSFFont fontcontent = wboutput.createFont();
		     fontcontent.setFontHeightInPoints((short) 11);
		     cellStylecontent.setFont(fontcontent);
		     
		     HSSFCellStyle cellStyleFont = wboutput.createCellStyle();
			 HSSFFont font2 = wboutput.createFont();
		     font2.setFontName("宋体");
		     font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		     font2.setFontHeightInPoints((short) 11);
		     
		     //不变的数据
		    // if(noChangeList != null && noChangeList.size() >0)
		    // {
			     HSSFSheet sheetoutputFirst = wboutput.createSheet("不变");
			     HSSFRow rowtitle0 = sheetoutputFirst.createRow(0);
			     firstcount++;
			     
			  	 new UtilFunctionDao().createBatchTitle(0,rowtitle0, cellStyleFont,true,3);
			    			     
				for(int i=0;i<noChangeList.size();i++)
				{
					SsmnZyUser info = noChangeList.get(i);
					HSSFRow rowcdrnum = sheetoutputFirst.createRow(i+1);
					new UtilFunctionDao().createBatchContent(i+1,rowcdrnum, info, cellStylecontent,true,3);
				}
		    // }
			
			firstcount = 0;
			//变更渠道
			//if(changeChannelList != null && changeChannelList.size() >0)
		    // {
				 HSSFSheet sheetoutputSecond = wboutput.createSheet("变更渠道");
				 HSSFRow rowtitle1 = sheetoutputSecond.createRow(0);
				 firstcount++;

				 new UtilFunctionDao().createBatchTitle(0,rowtitle1, cellStyleFont,true,3);
				 
				 for(int i=0;i<changeChannelList.size();i++)
				 {
						SsmnZyUser info = changeChannelList.get(i);
						HSSFRow rowcdrnum = sheetoutputSecond.createRow(i+1);
						new UtilFunctionDao().createBatchContent(i+1,rowcdrnum, info, cellStylecontent,true,3);
	
				  }
		    // }
				 
				 firstcount = 0;
				//新增
				 HSSFSheet sheetoutputThird = wboutput.createSheet("新增");
				 HSSFRow rowtitle2 = sheetoutputThird.createRow(0);
				 firstcount++;

				 new UtilFunctionDao().createBatchTitle(0,rowtitle2, cellStyleFont,true,3);
				 
				 for(int i=0;i<newList.size();i++)
				 {
						SsmnZyUser info = newList.get(i);
						HSSFRow rowcdrnum = sheetoutputThird.createRow(i+1);
						new UtilFunctionDao().createBatchContent(i+1,rowcdrnum, info, cellStylecontent,true,3);
	
				  }
				 
				 firstcount = 0;
				//解绑
				 HSSFSheet sheetoutputFour = wboutput.createSheet("解绑");
				 HSSFRow rowtitle3 = sheetoutputFour.createRow(0);
				 firstcount++;

				 new UtilFunctionDao().createBatchTitle(0,rowtitle3, cellStyleFont,true,3);
				 
				 for(int i=0;i<deleteList.size();i++)
				 {
						SsmnZyUser info = deleteList.get(i);
						HSSFRow rowcdrnum = sheetoutputFour.createRow(i+1);
						new UtilFunctionDao().createBatchContent(i+1,rowcdrnum, info, cellStylecontent,true,3);
	
				  }
				 
				 firstcount = 0;
				//失败
				 HSSFSheet sheetoutputFive = wboutput.createSheet("失败");
				 HSSFRow rowtitle4 = sheetoutputFive.createRow(0);
				 firstcount++;

				 new UtilFunctionDao().createBatchTitle(0,rowtitle4, cellStyleFont,false,3);
				 
				 for(int i=0;i<faildList.size();i++)
				 {
						SsmnZyUser info = faildList.get(i);
						HSSFRow rowcdrnum = sheetoutputFive.createRow(i+1);
						new UtilFunctionDao().createBatchContent(i+1,rowcdrnum, info, cellStylecontent,false,3);
	
				  }
			  
			
		wboutput.write(fileoutput);
		fileoutput.close();
		res = fname;
		 
	} catch (IOException e) {
		e.printStackTrace();
		logger.error("----批量解绑数据发生异常-----",e);
		
	}
	}
	
	return res;
}

//批量处理，注销
public void deleteFunction(List<SsmnZyUser> listTempD,List<SsmnZyUser> deleteList,List<SsmnZyUser> faildList,List<SsmnZyUser> listRemoveD,SsmnZyLevel level)
{
	Iterator<SsmnZyUser> itatd = listTempD.iterator();
	while(itatd.hasNext())
	{
		SsmnZyUser tzuad = itatd.next();
		//判断与操作员是否在同一公司
		if(tzuad.getCompany() !=null && tzuad.getCompany().length()>0 && 
				level.getCompany()!=null && level.getCompany().length()>0 &&
				tzuad.getCompany().equals(level.getCompany()))
		{
			//注销掉
			//先查员工编号是否存在，并且与主号码是否匹配
			
			String sres =uDao.isValidUser(tzuad.getMsisdn(),tzuad.getSsmnnumber());//这里其实都不需要判断，因为这是从数据库中查的，以防万一
			if(sres.equals(""))
			{
				//合法用户，解绑
				String suid = uDao.getSsmnzyuserId(tzuad.getMsisdn(),tzuad.getSsmnnumber());//肯定是带副号码
				if(suid != null && !"".equals(suid))
				{
					//解绑
					boolean isok =uDao.cancelBindSsmnuber(tzuad.getMsisdn(),tzuad.getSsmnnumber(),suid);
					if(isok)
					{
						//解绑成功
						deleteList.add(tzuad);
						itatd.remove();
						listRemoveD.add(tzuad);
						continue;
					}
					else
					{
						tzuad.setGroupname("解绑失败!");//暂用groupname字段记录错误信息
						faildList.add(tzuad);
						itatd.remove();
						listRemoveD.add(tzuad);
						continue;
					}
				}
				else
				{
					tzuad.setGroupname("该用户不存在,无法解绑!");//暂用groupname字段记录错误信息
					faildList.add(tzuad);
					itatd.remove();
					listRemoveD.add(tzuad);
					continue;
				}	
				
			}else
			{
				tzuad.setGroupname(sres);//暂用groupname字段记录错误信息
				faildList.add(tzuad);
				itatd.remove();
				listRemoveD.add(tzuad);
				continue;
			}
		}else
		{
			//数据库中多余的，不需要记录了
			//tzuad.setGroupname("不是该公司的用户!");//暂用groupname字段记录错误信息
			//faildList.add(tzuad);
			itatd.remove();
			listRemoveD.add(tzuad);
			continue;
		}
	}

}

public void reginFunction(List<SsmnZyUser> listTempE,List<SsmnZyUser> faildList,List<SsmnZyUser> newList,
		List<SsmnZyUser> listRemoveE,SsmnZyLevel level)
{	
	Iterator<SsmnZyUser> itate = listTempE.iterator(); 
	while(itate.hasNext())
	{
		SsmnZyUser tzuae = itate.next();
		//要注册
		
		//判断主号码,没有主号码，数据不合法
		if(tzuae.getMsisdn() == null || "".equals(tzuae.getMsisdn()))
		{
			tzuae.setGroupname("新增时，数据不合法，没有主号码!");
			faildList.add(tzuae);
			itate.remove();
			listRemoveE.add(tzuae);
			continue;
		}
		
		//判断主号码是否合法
		String strmsisdn = tzuae.getMsisdn();
		Pattern p = Pattern.compile("^[0-9]{11}$");
		Matcher m = p.matcher(strmsisdn);
		boolean b = m.matches();
		if(!b)
		{
			//主号码数据不合法
			tzuae.setGroupname("新增时，主号码数据不合法!");
			faildList.add(tzuae);
			itate.remove();
			listRemoveE.add(tzuae);
			continue;
		}
		
		//判断渠道
		if(tzuae.getChannelname() ==null || tzuae.getChannelname().length() <=0)
		{
			//渠道不存在
			tzuae.setGroupname("新增时，数据不合法，没有填写渠道!");
			faildList.add(tzuae);
			itate.remove();
			listRemoveE.add(tzuae);
			continue;
		}
		SsmnZyChannel szc = cDao.getChannelByName(tzuae.getChannelname());
		if(szc == null)
		{
			tzuae.setGroupname("新增时，该渠道不存在!");
			faildList.add(tzuae);
			itate.remove();
			listRemoveE.add(tzuae);
			continue;
		}
		new UtilFunctionDao().setEnablenumberType(szc.getId());
		//判断分行行动组
		//判断分行行动组是否存在,主号码与员工编号都不存在
		if( tzuae.getBranchactiongroup() == null || "".equals(tzuae.getBranchactiongroup()))
		{
			tzuae.setGroupname("新增时，数据不合法，没有行动组!");
			faildList.add(tzuae);
			itate.remove();
			listRemoveE.add(tzuae);
			continue;
		}
		Long levelid = leveldao.getLevelIDByActionGroup(tzuae.getBranchactiongroup());
		//当用户不存在的时候，如果行动组不存在，则不处理,当用户存在的时候，使用用户当前所在的行动组
		if(levelid ==null)
		{
			tzuae.setGroupname("新增时，该行动组不存在!");
			faildList.add(tzuae);
			itate.remove();
			listRemoveE.add(tzuae);
			continue;
		}
		SsmnZyLevel tzuaeLevel = leveldao.getBranchactiongroupNameById(levelid+"");
		//判断与操作员是否在同一公司
		if(tzuaeLevel.getCompany() !=null && tzuaeLevel.getCompany().length()>0 && 
				level.getCompany()!=null && level.getCompany().length()>0 &&
				tzuaeLevel.getCompany().equals(level.getCompany()))
		{
			//开始注册　,分两种，一种，用户已经存在，再绑定一个副号码，一种用户不存在，
			//先获取一个副号码
			//判断员工编号是否存在
			List<SsmnZyUser> ls = uDao.getSsmnzyuserByEmpno(tzuae.getEmpno(),level);
			if(ls !=null && ls.size()<=0)
			{
				//不存在,要注册　
				//还要查找一下主号码，万一，填写了一个已经存在的主号码呢，
				List<SsmnZyUser> lzu = uDao.getssmnzyuserbyMsisdn(tzuae.getMsisdn());
				if(lzu != null && lzu.size()>0)
				{
					tzuae.setGroupname("新增时，数据不合法，该主号码已经存在，且员工编号不存在!");
					faildList.add(tzuae);
					itate.remove();
					listRemoveE.add(tzuae);
					continue;
				}
				//不存在,则根据填写的区域选副号码(区域在前面有判断，这里肯定合法的了)
				String senable ="";
				if(Constants.TYPE_ENABLENUMBER ==2 && ConfigMgr.getIsHaveSsmnNum().equals("1"))
					senable =Constants.SSMN_NUM_DEFAULT;
				else
					senable= enDao.getEnableNumber("","",tzuae.getBranchactiongroup(),Constants.TYPE_ENABLENUMBER);//选取副号码，根据区域
				if(senable!= null && senable.length()>0)
				{
					//去注册
					//注册（副号码为可用的，并且主号码不存在，重新注册）
					SsmnZyUser u = new SsmnZyUser();
					u.setLevelid(levelid);
					u.setChannelid(szc.getId());
					u.setGroupname(tzuae.getBranchactiongroup());
					u.setIntime(new Date());
					u.setName(tzuae.getName());
					u.setEmpno(tzuae.getEmpno());
					u.setMsisdn(tzuae.getMsisdn());
					u.setSsmnnumber(senable);
					tzuae.setSsmnnumber(senable);
	
					//注册业务，绑定副号码
					boolean success = uDao.registeSSMNService(tzuae.getMsisdn(), senable, u);
					if(!success)
					{
						tzuae.setGroupname("注册失败!");
						faildList.add(tzuae);
						itate.remove();
						listRemoveE.add(tzuae);
						continue;
					}else
					{
						tzuae.setSsmnnumber(senable);
						newList.add(tzuae);//放新增的里
						itate.remove();
						listRemoveE.add(tzuae);
						continue;
					}
				}else
				{
					//没有可用的副号码
					tzuae.setGroupname("新增时，没有可用的副号码!");
					faildList.add(tzuae);
					itate.remove();
					listRemoveE.add(tzuae);
					continue;
				}
			}else
			{
				//已经存在，需要绑定一个副号码 ,则根据查出来的用户所在的区域填写副号码
				//先判断主号码和员工编号是否一致
				 List<SsmnZyUser> szulist =uDao.getSsmnzyuserByEmpno(tzuae.getEmpno(),level);
				 //szulist一定大于０，因为上面已经判断了
				 SsmnZyUser szu = szulist.get(0);
				 //获取副号码
				 String senable="";
				 if(Constants.TYPE_ENABLENUMBER ==2 && ConfigMgr.getIsHaveSsmnNum().equals("1"))
					senable =Constants.SSMN_NUM_DEFAULT;
				else
					senable= enDao.getEnableNumber(szu.getId()+"","","",Constants.TYPE_ENABLENUMBER);//选取副号码，根据区域
				 if(senable == null || senable.length()<=0)
				 {
					//没有可用的副号码
					tzuae.setGroupname("新增时，没有可用的副号码!");
					faildList.add(tzuae);
					itate.remove();
					listRemoveE.add(tzuae);
					continue;
				 }
				 //A+副号码，要判断该主号是否还有其他副号码
				 boolean checkUserA = true;
				 if(Constants.TYPE_ENABLENUMBER ==2)
				 {
					//是A+副号码，需要判断换绑的经纪人是否已存在A+副号码
					List<SsmnZyUser> listUser = uDao.checkOtherEnableNumberA(szu.getMsisdn(),2);
					if(listUser !=null && listUser.size()>0)
					{
						checkUserA = false;
						tzuae.setGroupname(Constants.MESSAGE_EXISTOTHERNUMBER);
						faildList.add(tzuae);
						itate.remove();
						listRemoveE.add(tzuae);
						continue;
					}else
						checkUserA  = true;
				 }
				 if(checkUserA)
				 {
					 boolean isOk=uDao.bindSsmnNumber(szu.getMsisdn(),senable, szc.getId()+"", szu.getId()+"");
						if(!isOk)
						{
							tzuae.setGroupname("绑定失败!");
							faildList.add(tzuae);
							itate.remove();
							listRemoveE.add(tzuae);
							continue;
						}else
						{
							tzuae.setSsmnnumber(senable);
							newList.add(tzuae);
							itate.remove();
							listRemoveE.add(tzuae);
							continue;
						}
				 }
				
			}
		}else
		{
			tzuae.setGroupname("不是该公司的用户!");
			faildList.add(tzuae);
			itate.remove();
			listRemoveE.add(tzuae);
			continue;
		}		
	}
}

	
//先存一下读到的文件	
/*	public boolean saveBatchExcelFile(InputStream in, String sfilepath)
	{
		boolean isok =false;
		FileOutputStream fos = null;
		int BUFFER_SIZE = 1024; 
	    byte[] bytes = new byte[BUFFER_SIZE];    
	    int size = 0; 
	    BufferedInputStream bis = null;
	    BufferedOutputStream bos = null;
		if(in == null)
			return isok;
		try
		{
			 bis = new BufferedInputStream(in);
			 bos = new BufferedOutputStream(new FileOutputStream(sfilepath));
			 int length = bis.read(bytes);
	            while (length != -1) {
	               // System.out.println("length: " + length);
	                bos.write(bytes, 0, length);
	                length = bis.read(bytes);
	            }
	            isok = true;
         
		} catch (Exception e) {   
			  
            e.printStackTrace();   
            isok = false;
        } finally {   
			try {
				if(bis != null)
				bis.close();
				if(bos != null)
	        		bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }  
		
		return isok;
	}*/

}
