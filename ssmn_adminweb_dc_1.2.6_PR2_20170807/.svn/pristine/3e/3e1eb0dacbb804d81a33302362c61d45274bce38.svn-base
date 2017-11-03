package com.dascom.ssmn.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.init.ConfigMgr;
import com.dascom.ssmn.dao.CallDao;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.EnableNumberDao;
import com.dascom.ssmn.dao.zydcUserDao;
import com.dascom.ssmn.entity.SsmnEnablenumber;
import com.dascom.ssmn.entity.SsmnUser;
import com.dascom.ssmn.entity.SsmnZyChannel;
import com.dascom.ssmn.entity.SsmnZyClientnum;
import com.dascom.ssmn.entity.SsmnZyEnablenumber;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.ssmn.util.Constants;
import com.dascom.ssmn.dao.UtilFunctionDao;

public class SelectEnableNumber extends HttpServlet{
	private static final Logger logger = Logger.getLogger(SelectEnableNumber.class);
	
	private LevelDao leveldao = LevelDao.getInstance();
	private EnableNumberDao endao = EnableNumberDao.getInstance();
	private zydcUserDao uDao = zydcUserDao.getInstance();
	private CallDao cdao =CallDao.getInstance();
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response)
	throws ServletException, IOException{
		TyadminOperators opera = (TyadminOperators)request.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		try{
			response.setHeader("Cache-Control", "no-cashe");			
			response.setContentType("text/plain; charset=UTF-8");
			String suid = request.getParameter("uid");//给已经存在的用户绑定副号码的时候，知道uid
			String branchactiongroup = request.getParameter("branchactiongroup");//创建经纪人的时候，知道行动组名
			String channelid = request.getParameter("channelid");//副号码所属的渠道
			String ssmnnumber = request.getParameter("ssmnnumber");//对副号码判断是否存在
			String smsisdn = request.getParameter("smsisdn");
			String sempno = request.getParameter("aempno");
			String stype = request.getParameter("type"); 
			String secondm =request.getParameter("secondmsisdn");
			String xml="";
			SsmnZyLevel level = null;
			level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
			if(suid == null)
				suid = "";
			if(branchactiongroup == null)
				branchactiongroup = "";
			if(ssmnnumber == null)
				ssmnnumber = "";
			if(channelid !=null && !"".equals(channelid))
				new UtilFunctionDao().setEnablenumberType(Long.parseLong(channelid));
			//获取到用户的级别
			String senable = "";
			if(stype != null && !stype.equals("") && stype.equals("1"))
			{
				//如果isHaveSsmnNum 开关打开，并且选择的A+渠道，则不分配副号码,Constants.TYPE_ENABLENUMBER 为2是选择A+渠道
				if(ConfigMgr.getIsHaveSsmnNum().equals("1") && Constants.TYPE_ENABLENUMBER ==2)
					senable = Constants.SSMN_NUM_DEFAULT;//反回默认副号码 11个0
				else
					senable= endao.getEnableNumber(suid,"",branchactiongroup,Constants.TYPE_ENABLENUMBER);//选取副号码
			}
			if(stype != null && !stype.equals("") && stype.equals("2"))
			{
				if(ConfigMgr.getIsHaveSsmnNum().equals("0"))//需要分配副号码，对副号码作判断
				{
					//判断员工编号
					List<SsmnZyUser> ls = uDao.getSsmnzyuserByEmpno(sempno,level);
					 if(ls ==null || ls.size()<=0 )
					 {
						senable= endao.getEnableNumber("",ssmnnumber,branchactiongroup,Constants.TYPE_ENABLENUMBER);//判断副号码是否可用
						//如果副号码可用，判断主号码是否存在
						if(senable != null && !senable.equals(""))
						{
							SsmnUser us = uDao.getssmnuserByMsisdn(smsisdn);
							if(us != null)
								senable = "主号码已存在";
							if(us == null )
							{
								//再判断第二联系人有否有效,如果第二联系人有值，才去判断，如果没有，不需要判断
								//第二联系人，是有分配副号码的
								if(secondm !=null && secondm.length()>0)
								{
									List<String> sl= uDao.getZyUserSecondMsis(secondm);
									if(sl !=null && sl.size()>0)
										senable = "ok";
									else
										senable ="填写的第二联系人无效!";
								}else
									senable = "ok";
							}
						}
					 }else
						 senable = "该员工编号已经存在"; 
				}else 
					senable ="00000000000";//如果不需要分配副号码，随便给个默认的
			}
			
			if(stype != null && !stype.equals("") && stype.equals("3"))//判断换绑的两个主号码是否在同一区域
			{
				String oldmsisdn = request.getParameter("oldmsisdn");
				String newmsisdn = request.getParameter("newmsisdn");
				senable = uDao.isSameArea(oldmsisdn, newmsisdn);
			}
			
			if(stype != null && !stype.equals("") && stype.equals("4"))//根据用户id获取该用户类型的渠道
			{		
				List<SsmnZyEnablenumber> list = endao.getSsmnEnableNumber(ssmnnumber);
				String entype ="0";
				if(list !=null && list.size()>0)
				{
					if(list.get(0).getType() == 2)
						entype ="1";//A+副号码，对应ssmn_zy_channel表中的type为1
				}
				List<SsmnZyChannel>  channels = uDao.getChannelList(opera.getLevelid(),entype,Constants.TYPE_SHOWDATE);
				String strChannels = "";
				for(int i = 0; i<channels.size(); i++)
				{
					strChannels +=channels.get(i).getId();
					strChannels +="|";
					strChannels += channels.get(i).getName();
					if(i<channels.size()-1)
						strChannels +=",";
				}
				xml = strChannels;
			}
			if(stype != null && !stype.equals("") && stype.equals("6"))//判断第二联系人是否有效
			{
				String sec =request.getParameter("seconmsisdn");
				if(sec !=null && sec.length()>0)
				{
					List<String> sl= uDao.getZyUserSecondMsis(sec);
					if(sl !=null && sl.size()>0)
						senable = "ok";
					else
						senable ="填写的第二联系人无效!";
				}else
					senable = "ok";
			}
			
			if(stype != null && !stype.equals("") && stype.equals("9"))//呼叫管理页面，编辑业务员时候，判断业务员是否有效
			{
				String susermsisdn =request.getParameter("usermsisdn");
				String susername =request.getParameter("username");
				String sclientnum =request.getParameter("clientnum");
				String sclientname =request.getParameter("clientname");
				List<SsmnZyUser> ul =uDao.getssmnzyuserbyMsisdn(susermsisdn);
				if(ul !=null && ul.size()>0)
				{
					if(ul.get(0).getName().equals(susername))
					{
						//查询这个业务员下面是否有该客户
						List<SsmnZyClientnum> cl  =cdao.getClientNumByUsermsisdn(susermsisdn, sclientnum,sclientname);
						if(cl !=null && cl.size()>0)
							senable ="业务员下面已经有该客户!";
						else
							senable ="";
						
					}else
						senable="业务员电话与姓名不对应!";
				}else
					senable="业务员不存在!";
			}
			
			if(senable != null && senable.length()>0)
				xml = senable;
			//else 
				//xml = "";
		
				 if(xml.length()>0){
			         try {
			        	 PrintWriter out = response.getWriter();
			        	 out.println(xml);
					} catch (IOException e) {
						e.printStackTrace();
					}
			     }		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
