package com.dascom.ssmn.action;

import java.awt.Font;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.DefaultCategoryDataset;

import com.dascom.OPadmin.action.AdminFormAction;
import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.IAdminAuthority;
import com.dascom.OPadmin.dao.impl.AdminAuthorityImpl;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.init.ConfigMgr;
import com.dascom.ssmn.dao.CDRDao;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.zydcUserDao;
import com.dascom.ssmn.entity.SsmnZyChannel;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.ssmn.entity.ZydcRecord;
import com.dascom.ssmn.util.Constants;
import com.sun.org.apache.commons.digester.rss.Item;
import com.dascom.ssmn.dao.UtilFunctionDao;

public class DownloadCDRInfo extends AdminFormAction{
	private static final Logger logger = Logger.getLogger(DownloadCDRInfo.class);
	private CDRDao cDao = CDRDao.getInstance();
	private zydcUserDao uDao = zydcUserDao.getInstance();
	private IAdminAuthority authDao = new AdminAuthorityImpl();
	
	@Override
	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		TyadminOperators opera = (TyadminOperators)req.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		String callStisDir = req.getSession().getServletContext().getRealPath(Constants.DOWNLOAD_CALL_STATISTIC_FILENAME)+File.separator;		
		List<String> authMethodList = authDao.findByGroupId(opera.getGroupId().longValue(),(String)req.getSession().getAttribute(Constants.SERVICEKEY_IN_SESSION));
		UtilFunctionDao.showType(authMethodList);
		List<ZydcRecord> recordlist = null;
		List<ZydcRecord> channelcountYijie = null;
		List<SsmnZyChannel> channelList = null;
		List<SsmnZyUser> userList = null;
		String[][] channels = null ; //总的渠道统计(呼入)
		String[][] channelInfo = null;//详细的渠道统计
		String[][] channelOuts = null;//呼出渠道统计
		String[][] channelOutInfo = null;//详细的呼出统计
		String sExcelCdr = "";
		String sExcelDate = "";
		String sExcelFileName = "";
		String yesterday = this.getYesterdayStr(Constants.DATE_FORMATE);
		String yesterdaysource = this.getYesterdayStr(Constants.DATE_FORMATE2);
		String sLastWeekFirstEnd = getLastWeekDay();
		String sLastMonthFirstEnd = getLastMonthDay();
		int intChannelCount = 0;
		int isExists = 0;
		String callExtent= ".xlsx";//扩展名
		long startTimeLong =0;
		long endTimeLong = 0; 
		String showtypetemp ="";
		String stype = req.getParameter("type");//1:昨日 2:上周　3:上个月
		String scallType = req.getParameter("calltype");//1:呼入　2:呼出
		if(stype !=null && stype.length()>0 && stype.equals("1"))
		{
			startTimeLong = cDao.getDateLong(new UtilFunctionDao().getYesterday(),"",0);
			endTimeLong= cDao.getDateLong(new UtilFunctionDao().getYesterday(),"",1);
		}
		if(stype !=null && stype.length()>0 && stype.equals("2"))
		{
			startTimeLong =  cDao.getDateLong(new UtilFunctionDao().getLastWeek(0), "", 0);
			endTimeLong	=  cDao.getDateLong(new UtilFunctionDao().getLastWeek(1), "", 1);
		}
		if(stype !=null && stype.length()>0 && stype.equals("3"))
		{
			startTimeLong = cDao.getDateLong(new UtilFunctionDao().getFirstLastDay("",0,1),"",0);
			endTimeLong =  cDao.getDateLong(new UtilFunctionDao().getFirstLastDay("",1,1),"",1);
		}
		
		//////----------获取总的渠道数-----------------------------------------------------
		channelList = uDao.getChannelList(opera.getLevelid(),"",Constants.TYPE_SHOWDATE);
		SsmnZyLevel level ;
		try {
			level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
		} catch (DaoException e) {
			logger.error("==DaoException==="+e.toString());
			req.setAttribute("msg", "操作失败！");
			return mapping.findForward(super.formView);
		}
		//---------------------------------呼入-----------------------
		if(scallType !=null && scallType.equals("1"))//呼入
		{			
			//初始化数组
			if(channelList.size()>0)
			{
				intChannelCount = channelList.size()+2;
				//初始化二维数组
				channels = new String[4][channelList.size()+2];
				channels[0][0] = "渠道";
				channels[1][0] = "数量";
				channels[2][0] = "已接通";
				channels[3][0] = "未接通";
				for(int i=0;i<channelList.size();i++)
				{
					channels[0][i+1]= channelList.get(i).getName();
					channels[1][i+1] = "0";
					channels[2][i+1] = "0";
					channels[3][i+1] = "0";
				}
				//加总计字段
				channels[0][channelList.size()+1] = "总计";
				channels[1][channelList.size()+1] = "0";
				channels[2][channelList.size()+1] = "0";
				channels[3][channelList.size()+1] = "0";
			}
			
			recordlist = cDao.getCdrChannelCountList("","","",startTimeLong,endTimeLong,"",
					"","","","","","","1","",opera.getLevelid(),"",Constants.TYPE_SHOWDATE);
			//查出来的每个渠道的总数组装到数组中
			if(recordlist.size()>0)
			{
				//总计
				int channelTotalCount = 0;
				for(int t=1;t<channels[0].length;t++)//从1开始是渠道名称
				{
					String schname =channels[0][t];
					for(int j=0; j<recordlist.size();j++)
					{
						ZydcRecord zr = recordlist.get(j);
						if(UtilFunctionDao.splitStr(schname,"_").equals(UtilFunctionDao.splitStr(zr.getChannelname(),"_")))
						{
							channels[1][t] =zr.getChannelCount()+"";
							channelTotalCount +=zr.getChannelCount();
							break;
						}
					}
				}
				channels[1][channelList.size()+1] = channelTotalCount+"";
			}
				
			//已接一行
			channelcountYijie = cDao.getCdrChannelCountList("","","",startTimeLong,endTimeLong
					,"","","","","","","","1","",opera.getLevelid(),"  and z.callduration >0  ",Constants.TYPE_SHOWDATE);	
			if(channelcountYijie !=null && channelcountYijie.size()>0)
			{
				int yijieCountTotal = 0;//已接总数
				int weijieCountTotal =0;//未接总数
				for(int t=1;t<channels[0].length;t++)
				{
					String schname =channels[0][t];
					for(int j=0;j<channelcountYijie.size();j++)
					{
						ZydcRecord zryijie = channelcountYijie.get(j);
						if(UtilFunctionDao.splitStr(schname,"_").equals(UtilFunctionDao.splitStr(zryijie.getChannelname(),"_")))
						{
							channels[2][t] =zryijie.getChannelCount()+"";
							channels[3][t] = (Integer.parseInt(channels[1][t]) -zryijie.getChannelCount())+"";
							yijieCountTotal +=zryijie.getChannelCount();
							weijieCountTotal +=Integer.parseInt(channels[1][t]) -zryijie.getChannelCount();
						}
					}
				}
				channels[2][channelList.size()+1] = yijieCountTotal+"";
				channels[3][channelList.size()+1] = weijieCountTotal+"";
			}
		}
		
		//--------------------------------------呼出-----------------------------------------
	
		int countChan = 3;//呼出的列数,总计列
		int ConfigCol=0;//存放配置呼出个数
		String[] spl=null;//存放配置的呼出类型名字
	
		if(scallType !=null && scallType.equals("2"))//双呼情况下呼出,呼出要根据配置了几个呼出，显示几列,非双呼只显示 APP呼出
		{
			//双呼:呼出时显示配置的, 非双呼,只显示APP呼出
			if(ConfigMgr.getIsDoubleCall() !=null  && ConfigMgr.getIsDoubleCall().equals("1") )
			{
				String stypes = new UtilFunctionDao().getCallOutTypes();
				if(stypes !=null && stypes.length()>0)
				{
					spl =stypes.split("\\|");
					if(spl!=null && spl.length>0)
						ConfigCol = spl.length; //这里存着配置的数字
				}
			}else
			{
				spl = new String[1];
				spl[0] ="APP呼出";
				ConfigCol =1;//非双呼,只有APP呼出
			}
			
			if(ConfigCol>0)
				countChan = ConfigCol+2;//如果配置了呼出，则配置了几个，显示几列，,+2:一列显示名字，一列显示总计
			else
			{
				//如果没配置，则按原来规则，哪些有数据，显示哪些列
				if(recordlist != null && recordlist.size()>0)
					countChan = recordlist.size()+2;
			}
			channelOuts = new String[4][countChan];
			channelOuts[0][0] = "类别";
			channelOuts[1][0] = "数量";
			channelOuts[2][0] = "已接通";
			channelOuts[3][0] = "未接通";
			channelOuts[0][1] = "APP呼出";
			channelOuts[1][1] = "0";
			channelOuts[2][1] = "0";
			channelOuts[3][1] = "0";
			//加总计字段
			channelOuts[0][countChan-1] = "总计";
			channelOuts[1][countChan-1] = "0";
			channelOuts[2][countChan-1] = "0";
			channelOuts[3][countChan-1] = "0";
			//初始化配置的列
			for(int i=0;i<ConfigCol;i++)
			{
				channelOuts[0][i+1] = spl[i];
				channelOuts[1][i+1] = "0";
				channelOuts[2][i+1] = "0";
				channelOuts[3][i+1] = "0";
			}
			
			//数量一行
			recordlist = cDao.getCdrChannelCountList("","","",startTimeLong,endTimeLong,"",
					"","","","","","","2","",opera.getLevelid(),"",Constants.TYPE_SHOWDATE);
			if(recordlist.size()>0)
			{			
				int totalSum = 0;//总数
				for(int t=1;t<channelOuts[0].length;t++)
				{
					String schname =channelOuts[0][t];
					for(int j=0; j<recordlist.size();j++)
					{
						ZydcRecord zr = recordlist.get(j);
						if(UtilFunctionDao.splitStr(schname,"_").equals(UtilFunctionDao.splitStr(zr.getChannelname(),"_")))
						{
							//总计
							totalSum += zr.getChannelCount();
							channelOuts[1][t] = zr.getChannelCount()+"";
							break;
						}
					}	
				}
				channelOuts[1][countChan-1] = totalSum+"";
			}
			
			//已接一行
			channelcountYijie = cDao.getCdrChannelCountList("","","",startTimeLong,endTimeLong
					,"","","","","","","","2","",opera.getLevelid(),"  and z.callduration >0  ",Constants.TYPE_SHOWDATE);	
			if(channelcountYijie !=null && channelcountYijie.size()>0)
			{
				int yijieCountTotal = 0;//已接总数
				int weijieCountTotal =0;//未接总数
				for(int t=1;t<channelOuts[0].length;t++)
				{
					String schname =channelOuts[0][t];
					for(int j=0;j<channelcountYijie.size();j++)
					{
						ZydcRecord zryijie = channelcountYijie.get(j);
						if(UtilFunctionDao.splitStr(schname,"_").equals(UtilFunctionDao.splitStr(zryijie.getChannelname(),"_")))
						{
							channelOuts[2][t] =zryijie.getChannelCount()+"";
							channelOuts[3][t] = (Integer.parseInt(channelOuts[1][t])-zryijie.getChannelCount())+"";
							yijieCountTotal +=zryijie.getChannelCount();
							weijieCountTotal +=Integer.parseInt(channelOuts[1][t]) -zryijie.getChannelCount();
						}
					}
				}
				channelOuts[2][countChan-1] = yijieCountTotal+"";
				channelOuts[3][countChan-1] = weijieCountTotal+"";
			}
		}
		
		//------------------------------经纪人-----------------------
		userList = uDao.getZyUserlList(level);
		///--------- 获取详细的渠道数---------------------------------------------------
		//*****************判断，已解绑的用户，如果有数据，则显示，无数据，则不显示***********************************
		 if(userList != null && userList.size()>0)
		 {
			
			 Iterator<SsmnZyUser> it = userList.iterator();
			 while(it.hasNext())
			 {
				 SsmnZyUser zu = it.next();
				 //先判断是否在已解绑的用户
				 boolean isNoUser = cDao.isUserCancel(zu);
				 if(!isNoUser)//已经解绑
				 {
					 //判断是否是已经解绑的，并且无数据的用户
					 boolean isok = cDao.isUserOk(zu,level,startTimeLong,endTimeLong);
					 if(!isok)//解绑的无数据的用户
					 {
						 //还需要判断，解绑时间，是否在style时间获围内，如果是，则显示，如果不是，删除
						 boolean isTimeOk = cDao.isCancelUserTime(stype,zu);
						 if(!isTimeOk)
							 it.remove();
					 }
				 }
			 }
		 }
		
		//****************************************************
		if(channelList.size()>0 && scallType.equals("1"))//呼入的
		{
			if(userList.size() >0)
			{
				int chlen = channelList.size()+2;
				int uslen = userList.size()+1;
				//初始化数组
				channelInfo = new String[uslen][chlen];
				
				for(int i = 0; i<uslen; i++)
				{
					for(int j =0 ; j<chlen; j++)
					{
						if(i == 0)
						{
							//第一行写渠道名称
							if(j == 0 )
								channelInfo[i][j] = "姓名/渠道";
							else 
								if(j == channelList.size()+1)//最后一行写总计
								channelInfo[i][j] = "总计";
							else
								channelInfo[i][j] = channelList.get(j-1).getName(); //渠道的名称
						}else
						{
							// 初始化第一列写用户名称
							if(j == 0)
								channelInfo[i][j] = userList.get(i-1).getName();
							else
								channelInfo[i][j] = "0";
						}
					}
				}
				
				//数组中填数据 
				for(int p = 1; p<uslen; p++)
				{
					SsmnZyUser zy = userList.get(p-1);
					//总计
					int userChannelTotalCount = 0;
						 
					//根据 主号查出数据
					String sUsermsisdn = zy.getMsisdn();
					List<ZydcRecord> rlist = cDao.getCdrChannelCountList(sUsermsisdn,"","",startTimeLong,endTimeLong,"",
							"","","","","","",scallType,"",level.getId(),"",Constants.TYPE_SHOWDATE);
					if(rlist.size() >0)
					{
						for(int t=1;t<channelInfo[0].length;t++)
						{
							String schname =channelInfo[0][t]; 
							for(int j = 0; j<rlist.size(); j++)
							{
								ZydcRecord rz = rlist.get(j);
								if(UtilFunctionDao.splitStr(schname,"_").equals(UtilFunctionDao.splitStr(rz.getChannelname(),"_")))
								{
									channelInfo[p][t] = rz.getChannelCount()+"";
									userChannelTotalCount +=rz.getChannelCount();
									break;
								}
							}
						}
						channelInfo[p][channelList.size()+1] = userChannelTotalCount+"";
					}
					
				}
				
				//给二维数据按总计倒序排序
				int lastIndex = channelInfo[0].length-1;
				  for (int i = 1; i < channelInfo.length-1; i++) 
				  {   
					  for (int j = 1; j < channelInfo.length - i; j++) 
					  {    
						  if (Integer.parseInt(channelInfo[j][lastIndex])< Integer.parseInt(channelInfo[j + 1][lastIndex])) 
						  {     
							  String[] d = channelInfo[j];     
							  channelInfo[j] = channelInfo[j + 1];     
							  channelInfo[j + 1] = d;    
						  }   
					  }  
				 }
			}
		}
		/**-----------------------------------------*/
		//呼出的
		if(userList.size() >0 && scallType.equals("2"))
		{
			int uslen = userList.size()+1;
			//初始化数组
			channelOutInfo = new String[uslen][countChan];
			for(int i = 0; i<uslen; i++)
			{
				for(int j =0 ; j<countChan; j++)
				{
					if(i == 0)
					{
						//第一行写渠道名称
						if(j == 0 )
							channelOutInfo[i][j] = "姓名/类别";
						else
							channelOutInfo[i][j] = channelOuts[0][j];
					}else
					{
						// 初始化第一列写用户名称
						if(j == 0)
							channelOutInfo[i][j] = userList.get(i-1).getName();
						else
							channelOutInfo[i][j] = "0";
					}
				}
			}
			
			//数组中填数据 
			for(int p = 1; p<uslen; p++)
			{
				SsmnZyUser zy = userList.get(p-1);
				//总计
				int userChannelTotalCount = 0;
					 
				//根据 主号查出数据
				String sUsermsisdn = zy.getMsisdn();
				List<ZydcRecord> rlist = cDao.getCdrChannelCountList(sUsermsisdn,"","",startTimeLong,endTimeLong,"",
						"","","","","","",scallType,"",level.getId(),"",Constants.TYPE_SHOWDATE);
				if(rlist.size() >0)
				{
					for(int t=1;t<channelOutInfo[0].length;t++)
					{
						String schname =channelOutInfo[0][t]; 
						for(int j = 0; j<rlist.size(); j++)
						{
							ZydcRecord rz = rlist.get(j);
							if(UtilFunctionDao.splitStr(schname,"_").equals(UtilFunctionDao.splitStr(rz.getChannelname(),"_")))
							{
								channelOutInfo[p][t] = rz.getChannelCount()+"";
								userChannelTotalCount +=rz.getChannelCount();
								break;
							}
						}
					}
					channelOutInfo[p][countChan-1] = userChannelTotalCount+"";
				}
			}
			
			//给二维数据按总计倒序排序
			int lastIndex = channelOutInfo[0].length-1;
			  for (int i = 1; i < channelOutInfo.length-1; i++) 
			  {   
				  for (int j = 1; j < channelOutInfo.length - i; j++) 
				  {    
					  if (Integer.parseInt(channelOutInfo[j][lastIndex])< Integer.parseInt(channelOutInfo[j + 1][lastIndex])) 
					  {     
						  String[] d = channelOutInfo[j];     
						  channelOutInfo[j] = channelOutInfo[j + 1];     
						  channelOutInfo[j + 1] = d;    
					  }   
				  }  
			 }
		}
		/**-----------------------------------------*/
		
		//现在不是行动组级别的也要给生成统计报表
		//报表名称规则：０（超级管理员账号），名称为：统计报表_日期
		//其他级别的，只取最低级别的名称
//		SsmnZyLevel level;
		String strfilename = "";
//			level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
		if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) && (level.getCompany() == null || "".equals(level.getCompany())))//省级
		{
			
			strfilename =level.getProvincecity();
		} 
		if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) && level.getCompany() != null && !"".equals(level.getCompany()) && 
				(level.getBusinessdepartment() == null || "".equals(level.getBusinessdepartment())))//公司级
		{
			strfilename =level.getCompany();
		}
		if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) 
				&& level.getCompany() != null && !"".equals(level.getCompany()) && 
				level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment())
				&& (level.getWarzone() == null || "".equals(level.getWarzone())))//事业部级
		{
			strfilename =level.getBusinessdepartment();
		}
		if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) 
				&& level.getCompany() != null && !"".equals(level.getCompany()) && 
				level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment())
				&& level.getWarzone() != null && !"".equals(level.getWarzone())
				&& (level.getArea() == null || "".equals(level.getArea())))//战区级
		{
			strfilename =level.getWarzone();
		}
		if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) 
				&& level.getCompany() != null && !"".equals(level.getCompany()) && 
				level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment())
				&& level.getWarzone() != null && !"".equals(level.getWarzone()) && 
				level.getArea() != null && !"".equals(level.getArea()) 
				&& (level.getBranchactiongroup() == null || "".equals(level.getBranchactiongroup())))//片区级
		{
			strfilename =level.getArea();
		}
		if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) 
				&& level.getCompany() != null && !"".equals(level.getCompany()) && 
				level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment())
				&& level.getWarzone() != null && !"".equals(level.getWarzone()) && 
				level.getArea() != null && !"".equals(level.getArea())
				&& level.getBranchactiongroup() != null && !"".equals(level.getBranchactiongroup()))//行动组级 ...写的好恶心... 
		{
			strfilename=level.getBranchactiongroup();
		}
		
		//------------------------------------------------------------
		if(Constants.TYPE_SHOWDATE <2)
		{
			if(Constants.TYPE_SHOWDATE == 0)
				showtypetemp ="_C";
			else if(Constants.TYPE_SHOWDATE ==1)
				showtypetemp ="_A";
		}
		
		//获取excel的路径文件
		if(stype.equals("1"))
		{
			sExcelFileName = strfilename+"统计报表_"+yesterday;
			sExcelCdr  = Constants.DOWNLOAD_CALL_STATISTIC_YESTERDAY_FILENAME;
			sExcelDate = yesterdaysource;
			
		}else if(stype.equals("2"))
		{
			sExcelFileName = strfilename+"统计报表_"+sLastWeekFirstEnd;
			sExcelCdr  = Constants.DOWNLOAD_CALL_STATISTIC_LASTWEEK_FILENAME;
			sExcelDate = sLastWeekFirstEnd;
 
		}else if(stype.equals("3"))
		{
			sExcelFileName = strfilename+"统计报表_"+sLastMonthFirstEnd;
			sExcelCdr = Constants.DOWNLOAD_CALL_STATISTIC_LASTMONTH_FILENAME;
			sExcelDate = sLastMonthFirstEnd;

		}
		
		String excelfilepath = callStisDir+sExcelCdr+File.separator+sExcelDate+File.separator+sExcelFileName+showtypetemp+".xlsx";
		String excelfilepath2 = callStisDir+sExcelCdr+File.separator+sExcelDate+File.separator+sExcelFileName+showtypetemp+".xls";
		File filenew = new File(excelfilepath);
		File filenew2 = new File(excelfilepath2);
		if(filenew.exists())
		{
			isExists = 1;
		}
		else
		{
			if(filenew2.exists())
			{
				isExists = 1;
				callExtent = ".xls";
			}
			else
				isExists = 0;
		}
								
		req.setAttribute("channels", channels);
		req.setAttribute("channelOuts",channelOuts);
		req.setAttribute("channelInfo", channelInfo);
		req.setAttribute("channelOutInfo",channelOutInfo);
		req.setAttribute("intChannelCount", intChannelCount);
		req.setAttribute("outChannelCount", countChan);
		req.setAttribute("type", stype);
		req.setAttribute("sExcelFileName", sExcelFileName);
		req.setAttribute("sExcelCdr", sExcelCdr);
		req.setAttribute("sExcelDate", sExcelDate);
		req.setAttribute("isExists", isExists);
		req.setAttribute("callExtent", callExtent);
		req.setAttribute("calltype", scallType);
		req.setAttribute("showtypetemp", showtypetemp);
		
		return mapping.findForward(super.formView);
	}

	
	/**
	 * 获取昨天时间的字符串
	 * formattype  日期格式
	 * @return
	 */
	public String getYesterdayStr(String formattype){
		SimpleDateFormat dateFormat = new SimpleDateFormat(formattype);
		Calendar cal = Calendar.getInstance();
		int day_of_month = cal.get(Calendar.DAY_OF_MONTH);  
		cal.set(Calendar.DAY_OF_MONTH, day_of_month-1);
		
		return dateFormat.format(cal.getTime()).trim();
	}
	
	public String getLastWeekDay()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		int dayOfWeek=calendar1.get(Calendar.DAY_OF_WEEK)-1;
		int offset1=1-dayOfWeek;
		int offset2=7-dayOfWeek;
		calendar1.add(Calendar.DATE, offset1-7);
		calendar2.add(Calendar.DATE, offset2-7);
		String firstDay = df.format(calendar1.getTime());
		String endDay = df.format(calendar2.getTime());
		
		return firstDay+"至"+endDay;
		//System.out.println(calendar1.getTime());//last Monday
		//System.out.println(calendar2.getTime());//last Sunday
	}

	public String getLastMonthDay()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		  Calendar cal = Calendar.getInstance();
		  GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		  Calendar calendar = Calendar.getInstance();
		  calendar.setTime(new Date());

		  calendar.add(Calendar.MONTH, -1);
		  Date theDate = calendar.getTime();
		  gcLast.setTime(theDate);
		  gcLast.set(Calendar.DAY_OF_MONTH, 1);
		  String day_first_prevM = df.format(gcLast.getTime());
		  StringBuffer str = new StringBuffer().append(day_first_prevM);
		  day_first_prevM = str.toString();

		  calendar.add(cal.MONTH, 1);
		  calendar.set(cal.DATE, 1);
		  calendar.add(cal.DATE, -1);
		  String day_end_prevM = df.format(calendar.getTime());
		  StringBuffer endStr = new StringBuffer().append(day_end_prevM);
		  day_end_prevM = endStr.toString();
		  
		 String firstDay = day_first_prevM;
		 String  endDay = day_end_prevM;
		 
		 return firstDay+"至"+endDay;
		  
	}

	
	
	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {

		return this.processSubmit(mapping, form, req, res);
	}

}
