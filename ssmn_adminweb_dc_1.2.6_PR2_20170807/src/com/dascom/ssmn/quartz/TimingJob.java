package com.dascom.ssmn.quartz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.impl.AdminOperatorImpl;
import com.dascom.init.ConfigMgr;
import com.dascom.ssmn.dao.CDRDao;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.dao.zydcUserDao;
import com.dascom.ssmn.entity.SsmnZyChannel;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.ssmn.entity.ZydcRecord;
import com.dascom.ssmn.util.Constants;


public class TimingJob implements Job  {
	private static Logger log = Logger.getLogger(TimingJob.class.getName());
	private CDRDao cdao = CDRDao.getInstance();
	private zydcUserDao uDao = zydcUserDao.getInstance();
	private AdminOperatorImpl adminOpera = AdminOperatorImpl.getInstance();
	
	public  TimingJob(){
	}
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			
		String yesterday = this.getYesterdayStr(Constants.DATE_FORMATE);
		String yesterdaysource = this.getYesterdayStr(Constants.DATE_FORMATE2);   		    

   		List<SsmnZyLevel> levelList = adminOpera.groupByProvinOpeno(Constants.servicekey_dc);//获取操作员各个级别
   		//判断定时任务生成的报表 是否 隐藏客户电话
		if(ConfigMgr.getTime_job_showclientnum()!=null && ConfigMgr.getTime_job_showclientnum().equals("1"))
			Constants.SHOW_CLIENTNUM_OP =1;
		else
			Constants.SHOW_CLIENTNUM_OP =0;
   		
   		Calendar cal=Calendar.getInstance(); 
		cal.setTime(new Date());  
		
   		 // 将通话录音文件打成zip包	
		 if(levelList.size()>0)
   		 {
   			for(int i=0; i<levelList.size();i++)
			{
   				SsmnZyLevel level = levelList.get(i);
   				
   				//行动组级别的不给下载权限，所以没必要生成zip文件
   				if(level.getBranchactiongroup() != null && !"".equals(level.getBranchactiongroup()) )
   					continue;
   				  				
   				//String topath = Constants.DOWNLOAD_ZIP_DIR+Constants.DOWNLOAD_FILENAME2;
   				String topath = Constants.ZIP_DIR+Constants.DOWNLOAD_FILENAME2;
				
   				String sprovinces = "";
   				if(level.getProvincecity() != null && !"".equals(level.getProvincecity()))
   					sprovinces +=level.getProvincecity();
				if(level.getCompany() != null && !"".equals(level.getCompany()))
					sprovinces +=level.getCompany();
				if(level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment()))
					sprovinces +=level.getBusinessdepartment();
				if(level.getWarzone() != null && !"".equals(level.getWarzone()))
					sprovinces +=level.getWarzone();
				if(level.getArea() != null && !"".equals(level.getArea()))
					sprovinces +=level.getArea();
				if(level.getBranchactiongroup() != null && !"".equals(level.getBranchactiongroup()))
					sprovinces +=level.getBranchactiongroup();
   					
				topath +=sprovinces+"_"+yesterday+".zip";
   				
   				//先取出所有每个操作员下面的ssmnnumber 注：把已经解绑的语音也要打包
   				List<SsmnZyUser> ssmnnumberzyuser = cdao.getSSmnnumberZyUserProvinceList(level);
				
   				//把这些副号码对应的语音打包
   				String sourcepath = Constants.AUDIO_PATH+yesterdaysource;
   				
   				List<String> zipfilenames = refreshFileList(sourcepath,ssmnnumberzyuser);
   				
   				if(zipfilenames != null && zipfilenames.size()>0)
   					createZipfile(topath,zipfilenames);
			}
   		 }
     		 
   		String lastmonth=getLastMonthDay();
   		 //生成统计报表 
   		if(levelList.size()>0)
		{				
			for(int i=0; i<levelList.size();i++)
			{
				
				SsmnZyLevel level = levelList.get(i);
				//根据level id，查出该区域下有几种类型的账号（A+账号，渠道账号,还是都有)
   				List<String> showTypeList =adminOpera.findShowTypeByLevleid(level.getId());
   				if(showTypeList !=null && showTypeList.size() >0 )
   				{
   					//根据showTypeList查出的几种账号类型，生成相应的报表(0:生成渠道类型的,1:生成A+类型的，2生成所有的)
   					for(int st=0;st<showTypeList.size();st++)
   					{
   						String showtypestr =showTypeList.get(st);
   						String showtypeTemp ="";
   						int stype =2;
   						if(showtypestr.equals("0"))
   						{
   							stype =0;
   							showtypeTemp ="_C";//生成渠道
   						}
   						else if(showtypestr.equals("1"))
   						{
   							stype = 1;
   							showtypeTemp ="_A";
   						}
   						else
   						{
   							stype =2;
   							showtypeTemp ="";
   						}
   						
   						String sprovince2 = "";
   						if(level.getProvincecity() != null && !"".equals(level.getProvincecity()))
   							sprovince2 +=level.getProvincecity();
   						if(level.getCompany() != null && !"".equals(level.getCompany()))
   							sprovince2 +=level.getCompany();
   						if(level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment()))
   							sprovince2 +=level.getBusinessdepartment();
   						if(level.getWarzone() != null && !"".equals(level.getWarzone()))
   							sprovince2 +=level.getWarzone();
   						if(level.getArea() != null && !"".equals(level.getArea()))
   							sprovince2 +=level.getArea();
   						if(level.getBranchactiongroup() != null && !"".equals(level.getBranchactiongroup()))
   							sprovince2 +=level.getBranchactiongroup();
   						
   						//获取昨天的统计报天
   						long startTimeLong =0;
   						long endTimeLong = 0;
   						startTimeLong = cdao.getDateLong(new UtilFunctionDao().getYesterday(),"",0);
   						endTimeLong= cdao.getDateLong(new UtilFunctionDao().getYesterday(),"",1);

   						List<ZydcRecord> cdrlist = cdao.getCdrStatRecordExportList("","","",startTimeLong,endTimeLong
   								,"","","","","","",
   								"","","",level.getId(),stype);
   						List<ZydcRecord> smslist = new ArrayList<ZydcRecord>();
   						
   						//查出来的数据，必须属于某一行动组，所以这里要限制行动组字段非空，
   						//注：这里每个行动组名不能有重复的（业务中也不会有出现重复的），如果出现重复的行动组名，
   						//也就是sheet名相同了，会出问题
   						List<SsmnZyLevel> ssmnzyuser = cdao.getSSmnZyUserProvinceList(level);
   						
   						//第一个sheet是总数，所以判断是否超限，只判断总数即可(超限生成 xlsx，不超限生成xls)
   						int icount = 0;
   						if(cdrlist !=null && cdrlist.size()>0)
   							icount += cdrlist.size();
   						
   						String execlSavePathyesdat = Constants.DOWNLOAD_EXECL_DIR;
   						String execlSavePathlastMonth = Constants.DOWNLOAD_EXECL_DIR+File.separator+lastmonth+File.separator;
   						String  execlSavePath=Constants.DOWNLOAD_EXECL_NAME2;
   						
   						File diryespath = new File(execlSavePathyesdat);
   						if (!diryespath.exists()) {
   							if (!diryespath.mkdirs())
   								System.out.println("创建文件失败");
   						}
   						
   						File dirmonthpath = new File(execlSavePathlastMonth);
   						if (!dirmonthpath.exists()) {
   							if (!dirmonthpath.mkdirs())
   								System.out.println("创建文件失败");
   						}

   						if(new ConfigMgr().getIsExcel2003() !=null && !"".equals(new ConfigMgr().getIsExcel2003()) && new ConfigMgr().getIsExcel2003().equals("1")
   								&& icount>=0 && icount<= new Constants().maxInt)
   						{
   							//showtypeTemp是显示类型_C显示渠道 _A显示A+,没有，显示所有的
   							execlSavePathyesdat=execlSavePathyesdat+execlSavePath+sprovince2 +"_"+yesterday+showtypeTemp+".xls";
   							createExcelSheet2003(cdrlist,smslist,ssmnzyuser,execlSavePathyesdat,1);
   						}
   						else
   						{
   							//showtypeTemp是显示类型_C显示渠道 _A显示A+,没有，显示所有的
   							execlSavePathyesdat=execlSavePathyesdat+execlSavePath+sprovince2 +"_"+yesterday+showtypeTemp+".xlsx";
   							createExcelSheet(cdrlist,smslist,ssmnzyuser,execlSavePathyesdat,1);
   						}
   						
   						//月报表生成--------------------------------------------------------------------------------				
   						//获取上个月的统计报表，每个月1号生成
   						int today = cal.get(cal.DAY_OF_MONTH);
   						if(today == 1)
   						{	
   							//此报表移到其他tomcat上生成暂时
   							startTimeLong = cdao.getDateLong(new UtilFunctionDao().getFirstLastDay("",0,1),"",0);
   							endTimeLong =  cdao.getDateLong(new UtilFunctionDao().getFirstLastDay("",1,1),"",1);
   							List<ZydcRecord> cdrlistlastmonth =cdao.getCdrStatRecordExportList("","","",startTimeLong,endTimeLong
   									,"","","","","","",
   									"","","",level.getId(),stype);
   							
   							//第一个sheet是总数，所以判断是否超限，只判断总数即可(超限生成 xlsx，不超限生成xls)
   							int  count = 0;
   							if(cdrlistlastmonth !=null && cdrlistlastmonth.size()>0)
   								count += cdrlistlastmonth.size();
   							
   							if(new ConfigMgr().getIsExcel2003() !=null && !"".equals(new ConfigMgr().getIsExcel2003()) && new ConfigMgr().getIsExcel2003().equals("1")
   									&& count>=0 && count<= new Constants().maxInt)
   							{
   								execlSavePathlastMonth=execlSavePathlastMonth+execlSavePath+sprovince2 +"_"+lastmonth+showtypeTemp+".xls";
   								createExcelSheet2003(cdrlistlastmonth,smslist,ssmnzyuser,execlSavePathlastMonth,2);
   							}
   							else
   							{
   								execlSavePathlastMonth=execlSavePathlastMonth+execlSavePath+sprovince2 +"_"+lastmonth+showtypeTemp+".xlsx";
   								createExcelSheet(cdrlistlastmonth,smslist,ssmnzyuser,execlSavePathlastMonth,2);
   							}
   						}
   					}
   				}					
									
			}
		}  
   		 
   		 //来电统计报表，每个行动组生成一个
   		 //昨日 , 上周 , 上个月   		 
   		String cSSavePath = Constants.DOWNLOAD_CALL_STATISTIC_DIR;
		
		File dir = new File(cSSavePath);
		if (!dir.exists()) {
			if (!dir.mkdirs())
				System.out.println("创建文件失败");
		}
		//Thread.sleep(100);
		//昨日， 每天执行
		getCallStatisticDatas(levelList,cSSavePath,yesterdaysource,yesterday,"1");
		
		int week = cal.get(Calendar.DAY_OF_WEEK)-1;
		//此报表移到其他tomcat上生成暂时
		if( week == 1)
			getCallStatisticDatas(levelList,cSSavePath,yesterdaysource,yesterday,"2");
		Thread.sleep(100);//留回收内存的时间
		//上个月，每个月的1号执行
		int today = cal.get(cal.DAY_OF_MONTH);
		//此报表移到其他tomcat上生成暂时
		if(today == 1)
			getCallStatisticDatas(levelList,cSSavePath,yesterdaysource,yesterday,"3");
			
		} catch (Exception e) {
			log.error("------------ 执行定时任务发生异常-----",e);
		}			
		
	}

	public void getCallStatisticDatas( List<SsmnZyLevel>  levelList, String cSSavePath,String yesterdaysource, 
			String yesterday, String stype)
	{
		if(levelList.size()>0)
		{		
			String sLastWeekFirstEnd = "";
			String sLastMonthFirstEnd = "";
			sLastWeekFirstEnd = getLastWeekDay();
			sLastMonthFirstEnd = getLastMonthDay();
   			String callSSavePath = cSSavePath;
   			long startTimeLong =0;
			long endTimeLong = 0;
   			
   			if(!"".equals(stype) && stype.equals("1"))//昨天
   			{
			  callSSavePath += Constants.DOWNLOAD_CALL_STATISTIC_YESTERDAY_FILENAME+File.separator;
			  startTimeLong = cdao.getDateLong(new UtilFunctionDao().getYesterday(),"",0);
			  endTimeLong= cdao.getDateLong(new UtilFunctionDao().getYesterday(),"",1);
   			}
   			else if(!"".equals(stype) && stype.equals("2"))//上周
   			{
   				callSSavePath += Constants.DOWNLOAD_CALL_STATISTIC_LASTWEEK_FILENAME+File.separator;
   				startTimeLong = cdao.getDateLong(new UtilFunctionDao().getLastWeek(0), "", 0);
   				endTimeLong = cdao.getDateLong(new UtilFunctionDao().getLastWeek(1), "", 1);
   			}
   			else if(!"".equals(stype) && stype.equals("3"))//上个月
   			{
   				callSSavePath += Constants.DOWNLOAD_CALL_STATISTIC_LASTMONTH_FILENAME+File.separator;
   				startTimeLong = cdao.getDateLong(new UtilFunctionDao().getFirstLastDay("",0,1),"",0);
				endTimeLong =  cdao.getDateLong(new UtilFunctionDao().getFirstLastDay("",1,1),"",1);
   			}
   			
			File diryes = new File(callSSavePath);
			if (!diryes.exists()) {
				if (!diryes.mkdirs())
					System.out.println("创建文件失败");
			}
   			
			for(int i=0; i<levelList.size();i++)
			{
				SsmnZyLevel level = levelList.get(i);
				//if(opera.getBranchactiongroup() == null || opera.getBranchactiongroup().equals(""))
				//	continue;//不是行动组级别的不给生成统计报表 
				//根据level id，查出该区域下有几种类型的账号（A+账号，渠道账号,还是都有)
   				List<String> showTypeList =adminOpera.findShowTypeByLevleid(level.getId());
				for(int st=0;st<showTypeList.size();st++)
				{
					String showtypestr =showTypeList.get(st);
					String showtypeTemp ="";
					int stypetemp =2;
					if(showtypestr.equals("0"))
					{
						stypetemp =0;
						showtypeTemp ="_C";//生成渠道
					}
					else if(showtypestr.equals("1"))
					{
						stypetemp = 1;
						showtypeTemp ="_A";
					}
					else
					{
						stypetemp =2;
						showtypeTemp ="";
					}
					
					String callStaticExeclSavePath = callSSavePath;
					
					if(!"".equals(stype) && stype.equals("1"))
						callStaticExeclSavePath += yesterdaysource+File.separator;
					else if(!"".equals(stype) && stype.equals("2"))
						callStaticExeclSavePath += sLastWeekFirstEnd+File.separator;
					else if(!"".equals(stype) && stype.equals("3"))
						callStaticExeclSavePath += sLastMonthFirstEnd+File.separator;
					
					File diryesday = new File(callStaticExeclSavePath);
					if (!diryesday.exists()) {
						if (!diryesday.mkdirs())
							System.out.println("创建文件失败");
					}
					
					//现在不是行动组级别的也要给生成统计报表
					//报表名称规则：，名称为：统计报表_日期
					//其他级别的，只取最低级别的名称
					 
					if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) && (level.getCompany() == null || "".equals(level.getCompany())))//省级
					{ 
						callStaticExeclSavePath +=level.getProvincecity();
					} 
					if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) && level.getCompany() != null && !"".equals(level.getCompany()) && 
							(level.getBusinessdepartment() == null || "".equals(level.getBusinessdepartment())))//公司级
					{
						callStaticExeclSavePath +=level.getCompany();
					}
					if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) 
							&& level.getCompany() != null && !"".equals(level.getCompany()) && 
							level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment())
							&& (level.getWarzone() == null || "".equals(level.getWarzone())))//事业部级
					{
						callStaticExeclSavePath +=level.getBusinessdepartment();
					}
					if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) 
							&& level.getCompany() != null && !"".equals(level.getCompany()) && 
							level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment())
							&& level.getWarzone() != null && !"".equals(level.getWarzone())
							&& (level.getArea() == null || "".equals(level.getArea())))//战区级
					{
						callStaticExeclSavePath +=level.getWarzone();
					}
					if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) 
							&& level.getCompany() != null && !"".equals(level.getCompany()) && 
							level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment())
							&& level.getWarzone() != null && !"".equals(level.getWarzone()) && 
							level.getArea() != null && !"".equals(level.getArea()) 
							&& (level.getBranchactiongroup() == null || "".equals(level.getBranchactiongroup())))//片区级
					{
						callStaticExeclSavePath +=level.getArea();
					}
					if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) 
							&& level.getCompany() != null && !"".equals(level.getCompany()) && 
							level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment())
							&& level.getWarzone() != null && !"".equals(level.getWarzone()) && 
							level.getArea() != null && !"".equals(level.getArea())
							&& level.getBranchactiongroup() != null && !"".equals(level.getBranchactiongroup()))//行动组级 ...写的好恶心...
					{
						callStaticExeclSavePath +=level.getBranchactiongroup();
					}

					//-------------------------------------------------------------
					//总的统计中 (渠道来电统计)
					List<ZydcRecord> recordlist = null;
					List<SsmnZyChannel> channelList = null;
					List<ZydcRecord> channelcountYijie = null;
					List<SsmnZyUser> userList = null;
					String[][] channels = null ; //总的渠道统计
					String[][] channelInfo = null;//详细的渠道统计
					String[][] channelOuts = null;//呼出渠道统计
					String[][] channelOutInfo = null;//详细的呼出统计
					
					channelList = uDao.getChannelList(level.getId(),"",stypetemp);
					int intChannelCount = 0;
					//-------------------------------呼入--------------------------
					//初始化数组
					if(channelList !=null && channelList.size()>0)
					{
						intChannelCount = channelList.size()+2;
						//初始化二维数组
						channels = new String[4][channelList.size()+2];
						channels[0][0] = "渠道";
						channels[1][0] = "数量";
						channels[2][0] = "已接通";
						channels[3][0] = "未接通";
						for(int q=0;q<channelList.size();q++)
						{
							channels[0][q+1]= channelList.get(q).getName();
							channels[1][q+1] = "0";
							channels[2][q+1] = "0";
							channels[3][q+1] = "0";
						}
						//加总计字段
						channels[0][channelList.size()+1] = "总计";
						channels[1][channelList.size()+1] = "0";
						channels[2][channelList.size()+1] = "0";
						channels[3][channelList.size()+1] = "0";
					}else
						continue;//没有配置渠道的不生成报表
					
					//数组中填数据  呼入
					recordlist = cdao.getCdrChannelCountList("","","",startTimeLong,endTimeLong,"",
							"","","","","","","1","",level.getId(),"",stypetemp);
					//查出来的每个渠道的总数组装到数组中
					if(recordlist !=null && recordlist.size()>0)
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
					channelcountYijie = cdao.getCdrChannelCountList("","","",startTimeLong,endTimeLong
							,"","","","","","","","1","",level.getId(),"  and z.callduration >0  ",stypetemp);	
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
					
					//--------------------------------------呼出-----------------------------------------
					
					//呼出的,按呼出类型分类,还有总计列
					int countChan = 3;//呼出的列数,总计列
					int ConfigCol=0;//存放配置呼出个数
					String[] spl=null;//存放配置的呼出类型名字

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
					for(int t=0;t<ConfigCol;t++)
					{
						channelOuts[0][t+1] = spl[t];
						channelOuts[1][t+1] = "0";
						channelOuts[2][t+1] = "0";
						channelOuts[3][t+1] = "0";
					}
					
					//数量一行
					recordlist = cdao.getCdrChannelCountList("","","",startTimeLong,endTimeLong,"",
							"","","","","","","2","",level.getId(),"",stypetemp);				
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
					channelcountYijie = cdao.getCdrChannelCountList("","","",startTimeLong,endTimeLong
							,"","","","","","","","2","",level.getId(),"  and z.callduration >0  ",stypetemp);	
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
							 boolean isNoUser = cdao.isUserCancel(zu);
							 if(!isNoUser)//已经解绑
							 {
								 //判断是否是已经解绑的，并且无数据的用户
								 boolean isok = cdao.isUserOk(zu,level,startTimeLong,endTimeLong);
								 if(!isok)//解绑的无数据的用户
								 {
									 //还需要判断，解绑时间，是否在style时间获围内，如果是，则显示，如果不是，删除
									 boolean isTimeOk = cdao.isCancelUserTime(stype,zu);
									 if(!isTimeOk)
										 it.remove();
								 }
							 }
						 }
						 
					 }
					
					//****************************************************
					//-------------------------呼入--------------------------------
					if(channelList !=null && channelList.size()>0)
					{
						if(userList !=null && userList.size() >0)
						{
							int chlen = channelList.size()+2;
							int uslen = userList.size()+1;
							//初始化数组
							channelInfo = new String[uslen][chlen];
							
							for(int p = 0; p<uslen; p++)
							{
								for(int j =0 ; j<chlen; j++)
								{
									if(p == 0)
									{
										//第一行写渠道名称
										if(j == 0 )
											channelInfo[p][j] = "姓名/渠道";
										else 
											if(j == channelList.size()+1)//最后一行写总计
											channelInfo[p][j] = "总计";
										else
											channelInfo[p][j] = channelList.get(j-1).getName(); //渠道的名称
									}else
									{
										// 初始化第一列写用户名称
										if(j == 0)
											channelInfo[p][j] = userList.get(p-1).getName();
										else
											channelInfo[p][j] = "0";
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
								List<ZydcRecord> rlist = cdao.getCdrChannelCountList(sUsermsisdn,"","",startTimeLong,endTimeLong,"",
										"","","","","","","1","",level.getId(),"",stypetemp);
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
							  for (int p = 1; p < channelInfo.length-1; p++) 
							  {   
								  for (int j = 1; j < channelInfo.length - p; j++) 
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
					if(userList !=null && userList.size() >0 )
					{
						int uslen = userList.size()+1;
						//初始化数组
						channelOutInfo = new String[uslen][countChan];
						for(int p = 0; p<uslen; p++)
						{
							for(int j =0 ; j<countChan; j++)
							{
								if(p == 0)
								{
									//第一行写渠道名称
									if(j == 0 )
										channelOutInfo[p][j] = "姓名/类别";
									else
										channelOutInfo[p][j] = channelOuts[0][j];
								}else
								{
									// 初始化第一列写用户名称
									if(j == 0)
										channelOutInfo[p][j] = userList.get(p-1).getName();
									else
										channelOutInfo[p][j] = "0";
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
							List<ZydcRecord> rlist = cdao.getCdrChannelCountList(sUsermsisdn,"","",startTimeLong,endTimeLong,"",
									"","","","","","","2","",level.getId(),"",stypetemp);
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
						  for (int p = 1; p < channelOutInfo.length-1; p++) 
						  {   
							  for (int j = 1; j < channelOutInfo.length - p; j++) 
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
					
					List<ZydcRecord> cdrlist = cdao.getCdrStatRecordExportList("","","",startTimeLong,endTimeLong
							,"","","","","","",
							"","","",level.getId(),stypetemp);
					List<SsmnZyLevel> ssmnzyuser = cdao.getSSmnZyUserProvinceList(level);
					List<ZydcRecord> smslist = new ArrayList<ZydcRecord>();
					
					int icountTail = 0;
					int icountUser = 0;
					if(cdrlist !=null && cdrlist.size()>0)
						icountTail += cdrlist.size();
					
					if(ssmnzyuser !=null && ssmnzyuser.size()>0)
						icountUser = ssmnzyuser.size();
					if(new ConfigMgr().getIsExcel2003() !=null && !"".equals(new ConfigMgr().getIsExcel2003()) && new ConfigMgr().getIsExcel2003().equals("1")
							&& icountTail>=0 && icountTail<= new Constants().maxInt && icountUser>=0 && icountUser<=new Constants().maxInt )
					{
						if(!"".equals(stype) && stype.equals("1"))
							callStaticExeclSavePath +="统计报表_"+yesterday+showtypeTemp+".xls";
						else if(!"".equals(stype) && stype.equals("2"))
							callStaticExeclSavePath +="统计报表_"+sLastWeekFirstEnd+showtypeTemp+".xls";
						else if(!"".equals(stype) && stype.equals("3"))
							callStaticExeclSavePath +="统计报表_"+sLastMonthFirstEnd+showtypeTemp+".xls";
						createCallStatisticExcelSheet2003(stype,cdrlist,smslist,ssmnzyuser,callStaticExeclSavePath,level.getBranchactiongroup(),channels,channelInfo,channelOuts ,channelOutInfo);
					}
					else
					{
						if(!"".equals(stype) && stype.equals("1"))
							callStaticExeclSavePath +="统计报表_"+yesterday+showtypeTemp+".xlsx";
						else if(!"".equals(stype) && stype.equals("2"))
							callStaticExeclSavePath +="统计报表_"+sLastWeekFirstEnd+showtypeTemp+".xlsx";
						else if(!"".equals(stype) && stype.equals("3"))
							callStaticExeclSavePath +="统计报表_"+sLastMonthFirstEnd+showtypeTemp+".xlsx";
						createCallStatisticExcelSheet(stype,cdrlist,smslist,ssmnzyuser,callStaticExeclSavePath,level.getBranchactiongroup(),channels,channelInfo,channelOuts ,channelOutInfo);
					}
				}
				
			}
				
		}
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
	
	/** stype: 1:昨日 2:上周 3:上个月 */
	public void createCallStatisticExcelSheet(String stype, List<ZydcRecord> cdrlist,List<ZydcRecord> msglist,List<SsmnZyLevel> ssmnzyuser, String fileRealPath,String groupName,String[][] channels,String[][] channelInfo,String[][] channelOuts ,String[][]channelOutInfo)
	{
		//先判断文件是否存在
		File diryes = new File(fileRealPath);
		if (diryes.exists()) {
			System.out.println("文件已经存在");
			return;
		}
		if( cdrlist.size() <=0 && channels == null && channelInfo == null)
			return;
		String yesterday = this.getYesterdayStr(Constants.DATE_FORMATE);
		FileOutputStream fileoutput = null;
		String sLastWeekFirstEnd = "";
		String sLastMonthFirstEnd = "";
		short rowHeith = 400;
		sLastWeekFirstEnd = getLastWeekDay();
		sLastMonthFirstEnd = getLastMonthDay();
		
		try {
			fileoutput = new FileOutputStream(fileRealPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    
	     SXSSFWorkbook wboutput = new SXSSFWorkbook(10000);
	     
	     Font font = wboutput.createFont();
	     font.setFontName("宋体");
	     font.setFontHeightInPoints((short) 16);//设置字体大小
	     
	    // HSSFPalette palette = wboutput.getCustomPalette();  //wb HSSFWorkbook对象
	    // palette.setColorAtIndex((short)10, (byte) (0xcc), (byte) (0xff), (byte) (0xcc));
	     
	     CellStyle cellStyle = wboutput.createCellStyle();
	     cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle.setFillForegroundColor((short)50);
	     cellStyle.setFont(font);
	     
	     //封面的边框线
	     //上　和　左　
	     CellStyle cellStyle1 = wboutput.createCellStyle();
	     cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
	     cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN); 
	     cellStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle1.setFillForegroundColor((short)50);
	     //上
	     CellStyle cellStyle2 = wboutput.createCellStyle();
	     cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN); 
	     cellStyle2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle2.setFillForegroundColor((short)50);
	     //上　和 右
	     CellStyle cellStyle3 = wboutput.createCellStyle();
	     cellStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);
	     cellStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	     cellStyle3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle3.setFillForegroundColor((short)50);
	     //右
	     CellStyle cellStyle4 = wboutput.createCellStyle();
	     cellStyle4.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	     cellStyle4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle4.setFillForegroundColor((short)50);
	     //下　和　右
	     CellStyle cellStyle5 = wboutput.createCellStyle();
	     cellStyle5.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	     cellStyle5.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
	     cellStyle5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle5.setFillForegroundColor((short)50);
	     //下
	     CellStyle cellStyle6 = wboutput.createCellStyle();
	     cellStyle6.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
	     cellStyle6.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle6.setFillForegroundColor((short)50);
	     //下和左
	     CellStyle cellStyle7 = wboutput.createCellStyle();
	     cellStyle7.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
	     cellStyle7.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
	     cellStyle7.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle7.setFillForegroundColor((short)50);
	     //左
	     CellStyle cellStyle8 = wboutput.createCellStyle();
	     cellStyle8.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
	     cellStyle8.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle8.setFillForegroundColor((short)50);

	     //封面
	     Sheet sheetCover = wboutput.createSheet("封面");
	     //合并单元格
	     //Region region1 = new Region(5, (short) 3, 14, (short) 8);
	     //sheetCover.addMergedRegion(region1);
	     //设置宽度 
	     sheetCover.setColumnWidth(4, 5766); 
	     sheetCover.setColumnWidth(5, 5766); 
	     
	     Row rowCover5 = sheetCover.createRow(5);
	     rowCover5.setHeight(rowHeith);
	     Cell cellCover53 = rowCover5.createCell(3);
	     cellCover53.setCellStyle(cellStyle1);
	     Cell cellCover54 = rowCover5.createCell(4);
	     cellCover54.setCellStyle(cellStyle2);
	     Cell cellCover55 = rowCover5.createCell(5);
	     cellCover55.setCellStyle(cellStyle2);
	     Cell cellCover56 = rowCover5.createCell(6);
	     cellCover56.setCellStyle(cellStyle2);
	     Cell cellCover57 = rowCover5.createCell(7);
	     cellCover57.setCellStyle(cellStyle2);
	     Cell cellCover58 = rowCover5.createCell(8);
	     cellCover58.setCellStyle(cellStyle3);
	     
	     Row rowCover6 = sheetCover.createRow(6);
	     rowCover6.setHeight(rowHeith);
	     Cell cellCover63 = rowCover6.createCell(3);
	     cellCover63.setCellStyle(cellStyle8);
	     Cell cellCover64 = rowCover6.createCell(4);
	     cellCover64.setCellValue("报表名称");
	     cellCover64.setCellStyle(cellStyle);
	     
	     Cell cellCover65 = rowCover6.createCell(5);
	     cellCover65.setCellStyle(cellStyle);
	     if(groupName != null)
	    	 cellCover65.setCellValue(groupName+"统计报表");
	     else
	    	 cellCover65.setCellValue("统计报表");
	     
	     Cell cellCover66 = rowCover6.createCell(6);
	     cellCover66.setCellStyle(cellStyle);
	     Cell cellCover67 = rowCover6.createCell(7);
	     cellCover67.setCellStyle(cellStyle);
	     Cell cellCover68 = rowCover6.createCell(8);
	     cellCover68.setCellStyle(cellStyle4);
	     
	     Row rowCover7 = sheetCover.createRow(7);
	     rowCover7.setHeight(rowHeith);
	     Cell cellCover73 = rowCover7.createCell(3);
	     cellCover73.setCellStyle(cellStyle8);
	     Cell cellCover74 = rowCover7.createCell(4);
	     cellCover74.setCellStyle(cellStyle);
	     Cell cellCover75 = rowCover7.createCell(5);
	     cellCover75.setCellStyle(cellStyle);
	     Cell cellCover76 = rowCover7.createCell(6);
	     cellCover76.setCellStyle(cellStyle);
	     Cell cellCover77 = rowCover7.createCell(7);
	     cellCover77.setCellStyle(cellStyle);
	     Cell cellCover78 = rowCover7.createCell(8);
	     cellCover78.setCellStyle(cellStyle4);
	     
	     Row rowCover8 = sheetCover.createRow(8);
	     rowCover8.setHeight(rowHeith);
	     Cell cellCover83 = rowCover8.createCell(3);
	     cellCover83.setCellStyle(cellStyle8);
	     Cell cellCover84 = rowCover8.createCell(4);
	     cellCover84.setCellStyle(cellStyle);
	     Cell cellCover85 = rowCover8.createCell(5);
	     cellCover85.setCellStyle(cellStyle);
	     Cell cellCover86 = rowCover8.createCell(6);
	     cellCover86.setCellStyle(cellStyle);
	     Cell cellCover87 = rowCover8.createCell(7);
	     cellCover87.setCellStyle(cellStyle);
	     Cell cellCover88 = rowCover8.createCell(8);
	     cellCover88.setCellStyle(cellStyle4);
	     
	     Row rowCover9 = sheetCover.createRow(9);
	     rowCover9.setHeight(rowHeith);
	     Cell cellCover93 = rowCover9.createCell(3);
	     cellCover93.setCellStyle(cellStyle8);
	     Cell cellCover94 = rowCover9.createCell(4);
	     cellCover94.setCellValue("报表来源");
	     cellCover94.setCellStyle(cellStyle);
	     Cell cellCover95 = rowCover9.createCell(5);
	     cellCover95.setCellValue("号盾(地产版)管理系统平台");
	     cellCover95.setCellStyle(cellStyle);
	     Cell cellCover96 = rowCover9.createCell(6);
	     cellCover96.setCellStyle(cellStyle);
	     Cell cellCover97 = rowCover9.createCell(7);
	     cellCover97.setCellStyle(cellStyle);
	     Cell cellCover98 = rowCover9.createCell(8);
	     cellCover98.setCellStyle(cellStyle4);
	     
	     Row rowCover10 = sheetCover.createRow(10);
	     rowCover10.setHeight(rowHeith);
	     Cell cellCover103 = rowCover10.createCell(3);
	     cellCover103.setCellStyle(cellStyle8);
	     Cell cellCover104 = rowCover10.createCell(4);
	     cellCover104.setCellStyle(cellStyle);
	     Cell cellCover105 = rowCover10.createCell(5);
	     cellCover105.setCellStyle(cellStyle);
	     Cell cellCover106 = rowCover10.createCell(6);
	     cellCover106.setCellStyle(cellStyle);
	     Cell cellCover107 = rowCover10.createCell(7);
	     cellCover107.setCellStyle(cellStyle);
	     Cell cellCover108 = rowCover10.createCell(8);
	     cellCover108.setCellStyle(cellStyle4);
	     Row rowCover11 = sheetCover.createRow(11);
	     rowCover11.setHeight(rowHeith);
	     Cell cellCover113 = rowCover11.createCell(3);
	     cellCover113.setCellStyle(cellStyle8);
	     Cell cellCover114 = rowCover11.createCell(4);
	     cellCover114.setCellStyle(cellStyle);
	     Cell cellCover115 = rowCover11.createCell(5);
	     cellCover115.setCellStyle(cellStyle);
	     Cell cellCover116 = rowCover11.createCell(6);
	     cellCover116.setCellStyle(cellStyle);
	     Cell cellCover117 = rowCover11.createCell(7);
	     cellCover117.setCellStyle(cellStyle);
	     Cell cellCover118 = rowCover11.createCell(8);
	     cellCover118.setCellStyle(cellStyle4);
	     
	     Row rowCover12 = sheetCover.createRow(12);
	     rowCover12.setHeight(rowHeith);
	     Cell cellCover123 = rowCover12.createCell(3);
	     cellCover123.setCellStyle(cellStyle8);
	     Cell cellCover124 = rowCover12.createCell(4);
	     cellCover124.setCellValue("统计时间");
	     cellCover124.setCellStyle(cellStyle);
	     Cell cellCover125 = rowCover12.createCell(5);
	     cellCover125.setCellStyle(cellStyle);
	     if(!"".equals(stype) && stype.equals("1"))
				cellCover125.setCellValue(yesterday);
	     else if(!"".equals(stype) && stype.equals("2"))
	    	 cellCover125.setCellValue(sLastWeekFirstEnd);
		 else if(!"".equals(stype) && stype.equals("3"))
			 cellCover125.setCellValue(sLastMonthFirstEnd);
	     Cell cellCover126 = rowCover12.createCell(6);
	     cellCover126.setCellStyle(cellStyle);
	     Cell cellCover127 = rowCover12.createCell(7);
	     cellCover127.setCellStyle(cellStyle);
	     Cell cellCover128 = rowCover12.createCell(8);
	     cellCover128.setCellStyle(cellStyle4);
	     
	     Row rowCover13 = sheetCover.createRow(13);
	     rowCover13.setHeight(rowHeith);
	     Cell cellCover133 = rowCover13.createCell(3);
	     cellCover133.setCellStyle(cellStyle8);
	     Cell cellCover134 = rowCover13.createCell(4);
	     cellCover134.setCellStyle(cellStyle);
	     Cell cellCover135 = rowCover13.createCell(5);
	     cellCover135.setCellStyle(cellStyle);
	     Cell cellCover136 = rowCover13.createCell(6);
	     cellCover136.setCellStyle(cellStyle);
	     Cell cellCover137 = rowCover13.createCell(7);
	     cellCover137.setCellStyle(cellStyle);
	     Cell cellCover138 = rowCover13.createCell(8);
	     cellCover138.setCellStyle(cellStyle4);
	     Row rowCover14 = sheetCover.createRow(14);
	     rowCover14.setHeight(rowHeith);
	     Cell cellCover143 = rowCover14.createCell(3);
	     cellCover143.setCellStyle(cellStyle7);
	     Cell cellCover144 = rowCover14.createCell(4);
	     cellCover144.setCellStyle(cellStyle6);
	     Cell cellCover145 = rowCover14.createCell(5);
	     cellCover145.setCellStyle(cellStyle6);
	     Cell cellCover146 = rowCover14.createCell(6);
	     cellCover146.setCellStyle(cellStyle6);
	     Cell cellCover147 = rowCover14.createCell(7);
	     cellCover147.setCellStyle(cellStyle6);
	     Cell cellCover148 = rowCover14.createCell(8);
	     cellCover148.setCellStyle(cellStyle5);
	     
	     //总统计
	     Sheet sheetTotal = wboutput.createSheet("总统计");
	     /*sheetTotal.setColumnWidth(2, 3766);
	     sheetTotal.setColumnWidth(3, 3766);
	     sheetTotal.setColumnWidth(4, 3766);
	     sheetTotal.setColumnWidth(5, 3766);
	     sheetTotal.setColumnWidth(6, 3766);
	     sheetTotal.setColumnWidth(7, 3766);
	     sheetTotal.setColumnWidth(8, 3766);*/
	     
	     Font fontTotal = wboutput.createFont();
	     fontTotal.setFontName("宋体");
	     fontTotal.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	     fontTotal.setFontHeightInPoints((short) 11);//设置字体大小
	     
	     //第一行，黑体，没有背景色
	     CellStyle cellStyleTotalDate = wboutput.createCellStyle();
	     cellStyleTotalDate.setFont(fontTotal);
	     
	     //第二行，标题行　，黑体，居中，加背景色
	     CellStyle cellStyleTotalTitle = wboutput.createCellStyle();
	     cellStyleTotalTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyleTotalTitle.setFillForegroundColor((short)50);
	     cellStyleTotalTitle.setFont(fontTotal);
	     cellStyleTotalTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中显示
	     cellStyleTotalTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
	     cellStyleTotalTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
	     cellStyleTotalTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
	     cellStyleTotalTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	     
	     //内容行，居中，设置字体大小
	     Font fontContent = wboutput.createFont();
	     fontContent.setFontName("宋体");
	     fontContent.setFontHeightInPoints((short) 11);//设置字体大小
	     
	     CellStyle cellStyleContent = wboutput.createCellStyle();
	     cellStyleContent.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	     cellStyleContent.setFont(fontContent);
	     cellStyleContent.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中显示
	     cellStyleContent.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
	     cellStyleContent.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
	     cellStyleContent.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
	     cellStyleContent.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	     
	     //渠道呼入统计
	     Row rowTotal2 = sheetTotal.createRow(2);
	     Cell cellTotal22 = rowTotal2.createCell(2);
	     cellTotal22.setCellStyle(cellStyleTotalDate);
	     if(!"".equals(stype) && stype.equals("1"))
	    	 cellTotal22.setCellValue("昨日("+yesterday+")渠道呼入统计");
	     else if(!"".equals(stype) && stype.equals("2"))
	    	 cellTotal22.setCellValue("上周("+sLastWeekFirstEnd+")渠道呼入统计");
	     else if(!"".equals(stype) && stype.equals("3"))
	    	 cellTotal22.setCellValue("上个月("+sLastMonthFirstEnd+")渠道呼入统计");
	     
	   //呼出统计
	     int cellOutIndex = 2;
	     if(channels !=null && channels.length>0)
	    	 cellOutIndex += channels[0].length +1;//1是中间空出来的单元格
	     Cell cellOutTotal = rowTotal2.createCell(cellOutIndex);
	     cellOutTotal.setCellStyle(cellStyleTotalDate);
	     if(!"".equals(stype) && stype.equals("1"))
	    	 cellOutTotal.setCellValue("昨日("+yesterday+")呼出统计");
	     else if(!"".equals(stype) && stype.equals("2"))
	    	 cellOutTotal.setCellValue("上周("+sLastWeekFirstEnd+")呼出统计");
	     else if(!"".equals(stype) && stype.equals("3"))
	    	 cellOutTotal.setCellValue("上个月("+sLastMonthFirstEnd+")呼出统计");
	     	    	 
	     int totalIndexRow = 2;
	     totalIndexRow++;
	     if(channels != null && channels.length >0)
	     {
	    	 for(int m =0; m<channels.length; m++)
	    	 {
	    		 int cellIndex = 2;
	    		 Row rowTotalCell = sheetTotal.createRow(totalIndexRow);
	    		// Cell cellTotalCellTitle = rowTotalCell.createCell(cellIndex);
	    		 //呼入    		  		 
	    		 for(int n = 0;n<channels[m].length;n++)
	    		 { 
	    			 Cell cellTotalCell = rowTotalCell.createCell(cellIndex);
	    			 if( m == 0)
	    				 cellTotalCell.setCellStyle(cellStyleTotalTitle);
	    			 else
	    			 {
	    				cellTotalCell.setCellStyle(cellStyleContent);
	    				cellTotalCell.setCellType(XSSFCell.CELL_TYPE_STRING);
	    			 }
	    			 cellTotalCell.setCellType(XSSFCell.CELL_TYPE_STRING);
	    			 cellTotalCell.setCellValue(channels[m][n]+"");
	    			 cellIndex++;
	    		 }
	    		 //呼出
	    		 if(channelOuts != null && channelOuts.length >0)
	    		 {
	    			 cellIndex +=1;
	    			 for(int n = 0;n<channelOuts[m].length;n++)
		    		 { 
		    			 Cell cellTotalCell = rowTotalCell.createCell(cellIndex);
		    			 if( m == 0)
		    				 cellTotalCell.setCellStyle(cellStyleTotalTitle);
		    			 else
		    			 {
		    				cellTotalCell.setCellStyle(cellStyleContent);
		    				cellTotalCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		    			 }
		    			 cellTotalCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		    			 cellTotalCell.setCellValue(channelOuts[m][n]+"");
		    			 cellIndex++;
		    		 }
	    		 }
	    		 totalIndexRow++;
	    	 }
	     }

	     totalIndexRow +=2;
	     
	     Row rowTotaltit = sheetTotal.createRow(totalIndexRow);
	     Cell cellTotaltit = rowTotaltit.createCell(2);
	     cellTotaltit.setCellStyle(cellStyleTotalDate);
	     if(!"".equals(stype) && stype.equals("1"))
	    	 cellTotaltit.setCellValue("昨日("+yesterday+")经纪人渠道呼入统计");
	     else if(!"".equals(stype) && stype.equals("2"))
	    	 cellTotaltit.setCellValue("上周("+sLastWeekFirstEnd+")经纪人渠道呼入统计");
	     else if(!"".equals(stype) && stype.equals("3"))
	    	 cellTotaltit.setCellValue("上个月("+sLastMonthFirstEnd+")经纪人渠道呼入统计");
	   
	     //经纪人呼出统计
	     int cellOutTitleIndex = 2;
	     if(channels !=null && channels.length>0)
	    	 cellOutTitleIndex += channels[0].length +1;//1是中间空出来的单元格
	     Cell cellOutTitle = rowTotaltit.createCell(cellOutTitleIndex);
	     cellOutTitle.setCellStyle(cellStyleTotalDate);
	     if(!"".equals(stype) && stype.equals("1"))
	    	 cellOutTitle.setCellValue("昨日("+yesterday+")经纪人呼出统计");
	     else if(!"".equals(stype) && stype.equals("2"))
	    	 cellOutTitle.setCellValue("上周("+sLastWeekFirstEnd+")经纪人呼出统计");
	     else if(!"".equals(stype) && stype.equals("3"))
	    	 cellOutTitle.setCellValue("上个月("+sLastMonthFirstEnd+")经纪人呼出统计");
	    	 
	     totalIndexRow++;
	     
	     //经纪人渠道来电统计
	     if(channelInfo != null && channelInfo.length >0)
	     {
	    	 for(int m = 0; m<channelInfo.length;m++)
	    	 {
	    		 int cellIndex = 2;
	    		 Row rowTotalCell = sheetTotal.createRow(totalIndexRow);
	    		 //呼入
	    		 for(int n = 0; n<channelInfo[0].length; n++)
	    		 {
	    			 Cell cellTotalCell = rowTotalCell.createCell(cellIndex);
	    			 
	    			 if(m == 0)
	    				 cellTotalCell.setCellStyle(cellStyleTotalTitle);
	    			 else
	    			 {
	    				 cellTotalCell.setCellStyle(cellStyleContent);
	    				 cellTotalCell.setCellType(XSSFCell.CELL_TYPE_STRING);
	    			 }
	    			 
	    			 cellTotalCell.setCellValue(channelInfo[m][n]+"");
	    			 cellIndex++;
	    		 }
	    		 //呼出
	    		 if(channelOutInfo !=null && channelOutInfo.length >0)
	    		 {
		    		 cellIndex +=1;
		    		 for(int n = 0; n<channelOutInfo[0].length; n++)
		    		 {
		    			 Cell cellTotalCell = rowTotalCell.createCell(cellIndex);
		    			 
		    			 if(m == 0)
		    				 cellTotalCell.setCellStyle(cellStyleTotalTitle);
		    			 else
		    			 {
		    				 cellTotalCell.setCellStyle(cellStyleContent);
		    				 cellTotalCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		    			 }
		    			 
		    			 cellTotalCell.setCellValue(channelOutInfo[m][n]+"");
		    			 cellIndex++;
		    		 }
	    		 }
	    		 totalIndexRow++;
	    	 }
	     }
	     
	     //详细记录
	     //总的数据
	    
	     Sheet sheetoutput = wboutput.createSheet("详细记录");
	     sheetoutput.setColumnWidth(0, 3766);
    	 sheetoutput.setColumnWidth(3, 6766);
    	 sheetoutput.setColumnWidth(4, 6766);
    	 sheetoutput.setColumnWidth(5, 3766);
    	 sheetoutput.setColumnWidth(6, 3766);
    	 sheetoutput.setColumnWidth(7, 3766);
    	 sheetoutput.setColumnWidth(10, 6766);
    	 sheetoutput.setColumnWidth(11, 6766);
    	 sheetoutput.setColumnWidth(13, 3766);
    	 sheetoutput.setColumnWidth(14, 3766);
	        
    	 int cdrcount = 0;
    	 
    	 //设置第一行样式
    	 CellStyle cellStyleFont = wboutput.createCellStyle();
    	 Font font2 = wboutput.createFont();
	     font2.setFontName("宋体");
	     font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
	     font2.setFontHeightInPoints((short) 11);
	     cellStyleFont.setFont(font2);
	     
	     //标题样式
	     CellStyle cellStyleTitle = wboutput.createCellStyle();
	     Font fontTitle = wboutput.createFont();
	     fontTitle.setFontHeightInPoints((short) 11);
	     cellStyleTitle.setFont(fontTitle);
	     cellStyleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyleTitle.setFillForegroundColor((short)50);
	     cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
	     cellStyleTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
	     cellStyleTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
	     cellStyleTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
	     cellStyleTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	     
	     //内容样式
	     CellStyle cellStylecontent = wboutput.createCellStyle();
	     Font fontcontent = wboutput.createFont();
	     fontcontent.setFontHeightInPoints((short) 11);
	     cellStylecontent.setFont(fontcontent);

         //语音信息
         Row rowinput0 = sheetoutput.createRow(0);
         Cell celltitlerow0 = rowinput0.createCell(0);
         //celltitlerow0.setEncoding((short)1);
         celltitlerow0.setCellValue("通话记录统计");
         celltitlerow0.setCellStyle(cellStyleFont);
         
         cdrcount++;
         
         Row rowinput1 = sheetoutput.createRow(1);
         cdrcount++;
         SsmnZyLevel l = new SsmnZyLevel();
    	 if(cdrlist !=null && cdrlist.size()>0 && cdrlist.get(0) !=null)
    	 {
    		 l.setProvincecity(cdrlist.get(0).getProvincecity());
    	 	 l.setCompany(cdrlist.get(0).getCompany());
    	 }
         createCdrRowTitle2007(rowinput1,cellStyleTitle,l); 

    	 for(int c= 0; c<cdrlist.size(); c++)
    	 {
    		 ZydcRecord zrecord = cdrlist.get(c);
    		 
    		 Row rowcdrnum = sheetoutput.createRow(cdrcount);
			 createRowContent2007(rowcdrnum, zrecord,cellStylecontent);
			 cdrcount++;
    	 }
 
	     try {
			wboutput.write(fileoutput);
			fileoutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fileoutput != null)
				try {
					fileoutput.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	     
	}
	
	/** stype: 1:昨日 2:上周 3:上个月 */
	public void createCallStatisticExcelSheet2003(String stype, List<ZydcRecord> cdrlist,List<ZydcRecord> msglist,List<SsmnZyLevel> ssmnzyuser, String fileRealPath,String groupName,String[][] channels,String[][] channelInfo,String[][] channelOuts ,String[][]channelOutInfo)
	{
		//先判断文件是否存在
		File diryes = new File(fileRealPath);
		if (diryes.exists()) {
			System.out.println("文件已经存在");
			return;
		}
		if(cdrlist.size() <=0 && channels == null && channelInfo == null)
			return;
		String yesterday = this.getYesterdayStr(Constants.DATE_FORMATE);
		FileOutputStream fileoutput = null;
		String sLastWeekFirstEnd = "";
		String sLastMonthFirstEnd = "";
		short rowHeith = 400;
		 
		sLastWeekFirstEnd = getLastWeekDay();
		sLastMonthFirstEnd = getLastMonthDay();
		
		try {
			fileoutput = new FileOutputStream(fileRealPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    
	     HSSFWorkbook wboutput = new HSSFWorkbook();
	     
	     HSSFFont font = wboutput.createFont();
	     font.setFontName("宋体");
	     font.setFontHeightInPoints((short) 16);//设置字体大小
	     
	    // HSSFPalette palette = wboutput.getCustomPalette();  //wb HSSFWorkbook对象
	    // palette.setColorAtIndex((short)10, (byte) (0xcc), (byte) (0xff), (byte) (0xcc));
	     
	     HSSFCellStyle cellStyle = wboutput.createCellStyle();
	     cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle.setFillForegroundColor((short)50);
	     cellStyle.setFont(font);
	     
	     //封面的边框线
	     //上　和　左　
	     HSSFCellStyle cellStyle1 = wboutput.createCellStyle();
	     cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
	     cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN); 
	     cellStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle1.setFillForegroundColor((short)50);
	     //上
	     HSSFCellStyle cellStyle2 = wboutput.createCellStyle();
	     cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN); 
	     cellStyle2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle2.setFillForegroundColor((short)50);
	     //上　和 右
	     HSSFCellStyle cellStyle3 = wboutput.createCellStyle();
	     cellStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);
	     cellStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	     cellStyle3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle3.setFillForegroundColor((short)50);
	     //右
	     HSSFCellStyle cellStyle4 = wboutput.createCellStyle();
	     cellStyle4.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	     cellStyle4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle4.setFillForegroundColor((short)50);
	     //下　和　右
	     HSSFCellStyle cellStyle5 = wboutput.createCellStyle();
	     cellStyle5.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	     cellStyle5.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
	     cellStyle5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle5.setFillForegroundColor((short)50);
	     //下
	     HSSFCellStyle cellStyle6 = wboutput.createCellStyle();
	     cellStyle6.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
	     cellStyle6.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle6.setFillForegroundColor((short)50);
	     //下和左
	     HSSFCellStyle cellStyle7 = wboutput.createCellStyle();
	     cellStyle7.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
	     cellStyle7.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
	     cellStyle7.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle7.setFillForegroundColor((short)50);
	     //左
	     HSSFCellStyle cellStyle8 = wboutput.createCellStyle();
	     cellStyle8.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
	     cellStyle8.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyle8.setFillForegroundColor((short)50);
  
	     
	     //封面
	     HSSFSheet sheetCover = wboutput.createSheet("封面");
	     //合并单元格
	     //Region region1 = new Region(5, (short) 3, 14, (short) 8);
	     //sheetCover.addMergedRegion(region1);
	     //设置宽度 
	     sheetCover.setColumnWidth(4, 5766); 
	     sheetCover.setColumnWidth(5, 5766); 
	     
	     
	     HSSFRow rowCover5 = sheetCover.createRow(5);
	     rowCover5.setHeight(rowHeith);
	     HSSFCell cellCover53 = rowCover5.createCell(3);
	     cellCover53.setCellStyle(cellStyle1);
	     HSSFCell cellCover54 = rowCover5.createCell(4);
	     cellCover54.setCellStyle(cellStyle2);
	     HSSFCell cellCover55 = rowCover5.createCell(5);
	     cellCover55.setCellStyle(cellStyle2);
	     HSSFCell cellCover56 = rowCover5.createCell(6);
	     cellCover56.setCellStyle(cellStyle2);
	     HSSFCell cellCover57 = rowCover5.createCell(7);
	     cellCover57.setCellStyle(cellStyle2);
	     HSSFCell cellCover58 = rowCover5.createCell(8);
	     cellCover58.setCellStyle(cellStyle3);
	     
	     HSSFRow rowCover6 = sheetCover.createRow(6);
	     rowCover6.setHeight(rowHeith);
	     HSSFCell cellCover63 = rowCover6.createCell(3);
	     cellCover63.setCellStyle(cellStyle8);
	     HSSFCell cellCover64 = rowCover6.createCell(4);
	     cellCover64.setCellValue("报表名称");
	     cellCover64.setCellStyle(cellStyle);
	     
	     HSSFCell cellCover65 = rowCover6.createCell(5);
	     cellCover65.setCellStyle(cellStyle);
	     if(groupName != null)
	    	 cellCover65.setCellValue(groupName+"统计报表");
	     else
	    	 cellCover65.setCellValue("统计报表");
	     
	     HSSFCell cellCover66 = rowCover6.createCell(6);
	     cellCover66.setCellStyle(cellStyle);
	     HSSFCell cellCover67 = rowCover6.createCell(7);
	     cellCover67.setCellStyle(cellStyle);
	     HSSFCell cellCover68 = rowCover6.createCell(8);
	     cellCover68.setCellStyle(cellStyle4);
	     
	     HSSFRow rowCover7 = sheetCover.createRow(7);
	     rowCover7.setHeight(rowHeith);
	     HSSFCell cellCover73 = rowCover7.createCell(3);
	     cellCover73.setCellStyle(cellStyle8);
	     HSSFCell cellCover74 = rowCover7.createCell(4);
	     cellCover74.setCellStyle(cellStyle);
	     HSSFCell cellCover75 = rowCover7.createCell(5);
	     cellCover75.setCellStyle(cellStyle);
	     HSSFCell cellCover76 = rowCover7.createCell(6);
	     cellCover76.setCellStyle(cellStyle);
	     HSSFCell cellCover77 = rowCover7.createCell(7);
	     cellCover77.setCellStyle(cellStyle);
	     HSSFCell cellCover78 = rowCover7.createCell(8);
	     cellCover78.setCellStyle(cellStyle4);
	     
	     HSSFRow rowCover8 = sheetCover.createRow(8);
	     rowCover8.setHeight(rowHeith);
	     HSSFCell cellCover83 = rowCover8.createCell(3);
	     cellCover83.setCellStyle(cellStyle8);
	     HSSFCell cellCover84 = rowCover8.createCell(4);
	     cellCover84.setCellStyle(cellStyle);
	     HSSFCell cellCover85 = rowCover8.createCell(5);
	     cellCover85.setCellStyle(cellStyle);
	     HSSFCell cellCover86 = rowCover8.createCell(6);
	     cellCover86.setCellStyle(cellStyle);
	     HSSFCell cellCover87 = rowCover8.createCell(7);
	     cellCover87.setCellStyle(cellStyle);
	     HSSFCell cellCover88 = rowCover8.createCell(8);
	     cellCover88.setCellStyle(cellStyle4);
	     
	     HSSFRow rowCover9 = sheetCover.createRow(9);
	     rowCover9.setHeight(rowHeith);
	     HSSFCell cellCover93 = rowCover9.createCell(3);
	     cellCover93.setCellStyle(cellStyle8);
	     HSSFCell cellCover94 = rowCover9.createCell(4);
	     cellCover94.setCellValue("报表来源");
	     cellCover94.setCellStyle(cellStyle);
	     HSSFCell cellCover95 = rowCover9.createCell(5);
	     cellCover95.setCellValue("号盾(地产版)管理系统平台");
	     cellCover95.setCellStyle(cellStyle);
	     HSSFCell cellCover96 = rowCover9.createCell(6);
	     cellCover96.setCellStyle(cellStyle);
	     HSSFCell cellCover97 = rowCover9.createCell(7);
	     cellCover97.setCellStyle(cellStyle);
	     HSSFCell cellCover98 = rowCover9.createCell(8);
	     cellCover98.setCellStyle(cellStyle4);
	     
	     HSSFRow rowCover10 = sheetCover.createRow(10);
	     rowCover10.setHeight(rowHeith);
	     HSSFCell cellCover103 = rowCover10.createCell(3);
	     cellCover103.setCellStyle(cellStyle8);
	     HSSFCell cellCover104 = rowCover10.createCell(4);
	     cellCover104.setCellStyle(cellStyle);
	     HSSFCell cellCover105 = rowCover10.createCell(5);
	     cellCover105.setCellStyle(cellStyle);
	     HSSFCell cellCover106 = rowCover10.createCell(6);
	     cellCover106.setCellStyle(cellStyle);
	     HSSFCell cellCover107 = rowCover10.createCell(7);
	     cellCover107.setCellStyle(cellStyle);
	     HSSFCell cellCover108 = rowCover10.createCell(8);
	     cellCover108.setCellStyle(cellStyle4);
	     HSSFRow rowCover11 = sheetCover.createRow(11);
	     rowCover11.setHeight(rowHeith);
	     HSSFCell cellCover113 = rowCover11.createCell(3);
	     cellCover113.setCellStyle(cellStyle8);
	     HSSFCell cellCover114 = rowCover11.createCell(4);
	     cellCover114.setCellStyle(cellStyle);
	     HSSFCell cellCover115 = rowCover11.createCell(5);
	     cellCover115.setCellStyle(cellStyle);
	     HSSFCell cellCover116 = rowCover11.createCell(6);
	     cellCover116.setCellStyle(cellStyle);
	     HSSFCell cellCover117 = rowCover11.createCell(7);
	     cellCover117.setCellStyle(cellStyle);
	     HSSFCell cellCover118 = rowCover11.createCell(8);
	     cellCover118.setCellStyle(cellStyle4);
	     
	     HSSFRow rowCover12 = sheetCover.createRow(12);
	     rowCover12.setHeight(rowHeith);
	     HSSFCell cellCover123 = rowCover12.createCell(3);
	     cellCover123.setCellStyle(cellStyle8);
	     HSSFCell cellCover124 = rowCover12.createCell(4);
	     cellCover124.setCellValue("统计时间");
	     cellCover124.setCellStyle(cellStyle);
	     HSSFCell cellCover125 = rowCover12.createCell(5);
	     cellCover125.setCellStyle(cellStyle);
	     if(!"".equals(stype) && stype.equals("1"))
				cellCover125.setCellValue(yesterday);
	     else if(!"".equals(stype) && stype.equals("2"))
	    	 cellCover125.setCellValue(sLastWeekFirstEnd);
		 else if(!"".equals(stype) && stype.equals("3"))
			 cellCover125.setCellValue(sLastMonthFirstEnd);
	     HSSFCell cellCover126 = rowCover12.createCell(6);
	     cellCover126.setCellStyle(cellStyle);
	     HSSFCell cellCover127 = rowCover12.createCell(7);
	     cellCover127.setCellStyle(cellStyle);
	     HSSFCell cellCover128 = rowCover12.createCell(8);
	     cellCover128.setCellStyle(cellStyle4);
	     
	     HSSFRow rowCover13 = sheetCover.createRow(13);
	     rowCover13.setHeight(rowHeith);
	     HSSFCell cellCover133 = rowCover13.createCell(3);
	     cellCover133.setCellStyle(cellStyle8);
	     HSSFCell cellCover134 = rowCover13.createCell(4);
	     cellCover134.setCellStyle(cellStyle);
	     HSSFCell cellCover135 = rowCover13.createCell(5);
	     cellCover135.setCellStyle(cellStyle);
	     HSSFCell cellCover136 = rowCover13.createCell(6);
	     cellCover136.setCellStyle(cellStyle);
	     HSSFCell cellCover137 = rowCover13.createCell(7);
	     cellCover137.setCellStyle(cellStyle);
	     HSSFCell cellCover138 = rowCover13.createCell(8);
	     cellCover138.setCellStyle(cellStyle4);
	     HSSFRow rowCover14 = sheetCover.createRow(14);
	     rowCover14.setHeight(rowHeith);
	     HSSFCell cellCover143 = rowCover14.createCell(3);
	     cellCover143.setCellStyle(cellStyle7);
	     HSSFCell cellCover144 = rowCover14.createCell(4);
	     cellCover144.setCellStyle(cellStyle6);
	     HSSFCell cellCover145 = rowCover14.createCell(5);
	     cellCover145.setCellStyle(cellStyle6);
	     HSSFCell cellCover146 = rowCover14.createCell(6);
	     cellCover146.setCellStyle(cellStyle6);
	     HSSFCell cellCover147 = rowCover14.createCell(7);
	     cellCover147.setCellStyle(cellStyle6);
	     HSSFCell cellCover148 = rowCover14.createCell(8);
	     cellCover148.setCellStyle(cellStyle5);
	     
	     
	     //总统计
	     HSSFSheet sheetTotal = wboutput.createSheet("总统计");
	     /*sheetTotal.setColumnWidth(2, 3766);
	     sheetTotal.setColumnWidth(3, 3766);
	     sheetTotal.setColumnWidth(4, 3766);
	     sheetTotal.setColumnWidth(5, 3766);
	     sheetTotal.setColumnWidth(6, 3766);
	     sheetTotal.setColumnWidth(7, 3766);
	     sheetTotal.setColumnWidth(8, 3766);*/
	     
	     HSSFFont fontTotal = wboutput.createFont();
	     fontTotal.setFontName("宋体");
	     fontTotal.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	     fontTotal.setFontHeightInPoints((short) 11);//设置字体大小
	     
	     //第一行，黑体，没有背景色
	     HSSFCellStyle cellStyleTotalDate = wboutput.createCellStyle();
	     cellStyleTotalDate.setFont(fontTotal);
	     
	     //第二行，标题行　，黑体，居中，加背景色
	     HSSFCellStyle cellStyleTotalTitle = wboutput.createCellStyle();
	     cellStyleTotalTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyleTotalTitle.setFillForegroundColor((short)50);
	     cellStyleTotalTitle.setFont(fontTotal);
	     cellStyleTotalTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中显示
	     cellStyleTotalTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
	     cellStyleTotalTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
	     cellStyleTotalTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
	     cellStyleTotalTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	     
	     //内容行，居中，设置字体大小
	     HSSFFont fontContent = wboutput.createFont();
	     fontContent.setFontName("宋体");
	     fontContent.setFontHeightInPoints((short) 11);//设置字体大小
	     
	     HSSFCellStyle cellStyleContent = wboutput.createCellStyle();
	     cellStyleContent.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	     cellStyleContent.setFont(fontContent);
	     cellStyleContent.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中显示
	     cellStyleContent.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
	     cellStyleContent.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
	     cellStyleContent.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
	     cellStyleContent.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	     
	     //渠道呼入统计
	     HSSFRow rowTotal2 = sheetTotal.createRow(2);
	     HSSFCell cellTotal22 = rowTotal2.createCell(2);
	     cellTotal22.setCellStyle(cellStyleTotalDate);
	     if(!"".equals(stype) && stype.equals("1"))
	    	 cellTotal22.setCellValue("昨日("+yesterday+")渠道呼入统计");
	     else if(!"".equals(stype) && stype.equals("2"))
	    	 cellTotal22.setCellValue("上周("+sLastWeekFirstEnd+")渠道呼入统计");
	     else if(!"".equals(stype) && stype.equals("3"))
	    	 cellTotal22.setCellValue("上个月("+sLastMonthFirstEnd+")渠道呼入统计");
	     
	   //呼出统计
	     int cellOutIndex = 2;
	     if(channels !=null && channels.length>0)
	    	 cellOutIndex += channels[0].length +1;//1是中间空出来的单元格
	     HSSFCell cellOutTotal = rowTotal2.createCell(cellOutIndex);
	     cellOutTotal.setCellStyle(cellStyleTotalDate);
	     if(!"".equals(stype) && stype.equals("1"))
	    	 cellOutTotal.setCellValue("昨日("+yesterday+")呼出统计");
	     else if(!"".equals(stype) && stype.equals("2"))
	    	 cellOutTotal.setCellValue("上周("+sLastWeekFirstEnd+")呼出统计");
	     else if(!"".equals(stype) && stype.equals("3"))
	    	 cellOutTotal.setCellValue("上个月("+sLastMonthFirstEnd+")呼出统计");
	     	    	 
	     int totalIndexRow = 2;
	     totalIndexRow++;
	     if(channels != null && channels.length >0)
	     {
	    	 for(int m =0; m<channels.length; m++)
	    	 {
	    		 int cellIndex = 2;
	    		 HSSFRow rowTotalCell = sheetTotal.createRow(totalIndexRow);
	    		// Cell cellTotalCellTitle = rowTotalCell.createCell(cellIndex);
	    		 //呼入    		  		 
	    		 for(int n = 0;n<channels[m].length;n++)
	    		 { 
	    			 HSSFCell cellTotalCell = rowTotalCell.createCell(cellIndex);
	    			 if( m == 0)
	    				 cellTotalCell.setCellStyle(cellStyleTotalTitle);
	    			 else
	    			 {
	    				cellTotalCell.setCellStyle(cellStyleContent);
	    				cellTotalCell.setCellType(XSSFCell.CELL_TYPE_STRING);
	    			 }
	    			 cellTotalCell.setCellType(XSSFCell.CELL_TYPE_STRING);
	    			 cellTotalCell.setCellValue(channels[m][n]+"");
	    			 cellIndex++;
	    		 }
	    		 //呼出
	    		 if(channelOuts != null && channelOuts.length >0)
	    		 {
	    			 cellIndex +=1;
	    			 for(int n = 0;n<channelOuts[m].length;n++)
		    		 { 
	    				 HSSFCell cellTotalCell = rowTotalCell.createCell(cellIndex);
		    			 if( m == 0)
		    				 cellTotalCell.setCellStyle(cellStyleTotalTitle);
		    			 else
		    			 {
		    				cellTotalCell.setCellStyle(cellStyleContent);
		    				cellTotalCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		    			 }
		    			 cellTotalCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		    			 cellTotalCell.setCellValue(channelOuts[m][n]+"");
		    			 cellIndex++;
		    		 }
	    		 }
	    		 totalIndexRow++;
	    	 }
	     }

	     totalIndexRow +=2;
	     
	     HSSFRow rowTotaltit = sheetTotal.createRow(totalIndexRow);
	     HSSFCell cellTotaltit = rowTotaltit.createCell(2);
	     cellTotaltit.setCellStyle(cellStyleTotalDate);
	     if(!"".equals(stype) && stype.equals("1"))
	    	 cellTotaltit.setCellValue("昨日("+yesterday+")经纪人渠道呼入统计");
	     else if(!"".equals(stype) && stype.equals("2"))
	    	 cellTotaltit.setCellValue("上周("+sLastWeekFirstEnd+")经纪人渠道呼入统计");
	     else if(!"".equals(stype) && stype.equals("3"))
	    	 cellTotaltit.setCellValue("上个月("+sLastMonthFirstEnd+")经纪人渠道呼入统计");
	   
	     //经纪人呼出统计
	     int cellOutTitleIndex = 2;
	     if(channels !=null && channels.length>0)
	    	 cellOutTitleIndex += channels[0].length +1;//1是中间空出来的单元格
	     HSSFCell cellOutTitle = rowTotaltit.createCell(cellOutTitleIndex);
	     cellOutTitle.setCellStyle(cellStyleTotalDate);
	     if(!"".equals(stype) && stype.equals("1"))
	    	 cellOutTitle.setCellValue("昨日("+yesterday+")经纪人呼出统计");
	     else if(!"".equals(stype) && stype.equals("2"))
	    	 cellOutTitle.setCellValue("上周("+sLastWeekFirstEnd+")经纪人呼出统计");
	     else if(!"".equals(stype) && stype.equals("3"))
	    	 cellOutTitle.setCellValue("上个月("+sLastMonthFirstEnd+")经纪人呼出统计");
	    	 
	     totalIndexRow++;
	     
	     //经纪人渠道来电统计
	     if(channelInfo != null && channelInfo.length >0)
	     {
	    	 for(int m = 0; m<channelInfo.length;m++)
	    	 {
	    		 int cellIndex = 2;
	    		 HSSFRow rowTotalCell = sheetTotal.createRow(totalIndexRow);
	    		 //呼入
	    		 for(int n = 0; n<channelInfo[0].length; n++)
	    		 {
	    			 HSSFCell cellTotalCell = rowTotalCell.createCell(cellIndex);
	    			 
	    			 if(m == 0)
	    				 cellTotalCell.setCellStyle(cellStyleTotalTitle);
	    			 else
	    			 {
	    				 cellTotalCell.setCellStyle(cellStyleContent);
	    				 cellTotalCell.setCellType(XSSFCell.CELL_TYPE_STRING);
	    			 }
	    			 
	    			 cellTotalCell.setCellValue(channelInfo[m][n]+"");
	    			 cellIndex++;
	    		 }
	    		 //呼出
	    		 if(channelOutInfo !=null && channelOutInfo.length >0)
	    		 {
		    		 cellIndex +=1;
		    		 for(int n = 0; n<channelOutInfo[0].length; n++)
		    		 {
		    			 HSSFCell cellTotalCell = rowTotalCell.createCell(cellIndex);
		    			 
		    			 if(m == 0)
		    				 cellTotalCell.setCellStyle(cellStyleTotalTitle);
		    			 else
		    			 {
		    				 cellTotalCell.setCellStyle(cellStyleContent);
		    				 cellTotalCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		    			 }
		    			 
		    			 cellTotalCell.setCellValue(channelOutInfo[m][n]+"");
		    			 cellIndex++;
		    		 }
	    		 }
	    		 totalIndexRow++;
	    	 }
	     }
	     
	     //详细记录
	     //总的数据
	    
	     HSSFSheet sheetoutput = wboutput.createSheet("详细记录");
	     sheetoutput.setColumnWidth(0, 3766);
    	 sheetoutput.setColumnWidth(3, 6766);
    	 sheetoutput.setColumnWidth(4, 6766);
    	 sheetoutput.setColumnWidth(5, 3766);
    	 sheetoutput.setColumnWidth(6, 3766);
    	 sheetoutput.setColumnWidth(7, 3766);
    	 sheetoutput.setColumnWidth(10, 6766);
    	 sheetoutput.setColumnWidth(11, 6766);
    	 sheetoutput.setColumnWidth(13, 3766);
    	 sheetoutput.setColumnWidth(14, 3766);
	        
    	 int cdrcount = 0;
    	 
    	 //设置第一行样式
    	 HSSFCellStyle cellStyleFont = wboutput.createCellStyle();
    	 HSSFFont font2 = wboutput.createFont();
	     font2.setFontName("宋体");
	     font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
	     font2.setFontHeightInPoints((short) 11);
	     cellStyleFont.setFont(font2);
	     
	     //标题样式
	     HSSFCellStyle cellStyleTitle = wboutput.createCellStyle();
	     HSSFFont fontTitle = wboutput.createFont();
	     fontTitle.setFontHeightInPoints((short) 11);
	     cellStyleTitle.setFont(fontTitle);
	     cellStyleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     cellStyleTitle.setFillForegroundColor((short)50);
	     cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
	     cellStyleTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
	     cellStyleTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
	     cellStyleTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
	     cellStyleTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	     
	     //内容样式
	     HSSFCellStyle cellStylecontent = wboutput.createCellStyle();
	     HSSFFont fontcontent = wboutput.createFont();
	     fontcontent.setFontHeightInPoints((short) 11);
	     cellStylecontent.setFont(fontcontent);

         //语音信息
	     HSSFRow rowinput0 = sheetoutput.createRow(0);
	     HSSFCell celltitlerow0 = rowinput0.createCell(0);
         //celltitlerow0.setEncoding((short)1);
         celltitlerow0.setCellValue("通话记录统计");
         celltitlerow0.setCellStyle(cellStyleFont);
         
         cdrcount++;
         
         HSSFRow rowinput1 = sheetoutput.createRow(1);
         cdrcount++;
         //总的，写第一条数据的
    	 
         
         SsmnZyLevel l = new SsmnZyLevel();
    	 if(cdrlist !=null && cdrlist.size()>0 )
    	 {
    		 l.setProvincecity(cdrlist.get(0).getProvincecity());
    	 	 l.setCompany(cdrlist.get(0).getCompany());
    	 }
         createCdrRowTitle(rowinput1,cellStyleTitle,l); 

    	 for(int c= 0; c<cdrlist.size(); c++)
    	 {
    		 ZydcRecord zrecord = cdrlist.get(c);
    		 
    		 HSSFRow rowcdrnum = sheetoutput.createRow(cdrcount);
			 createRowContent(rowcdrnum, zrecord,cellStylecontent);
			 cdrcount++;
    	 }
 
	     try {
			wboutput.write(fileoutput);
			fileoutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fileoutput != null)
				try {
					fileoutput.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	     
	}
	
public void createExcelSheet(List<ZydcRecord> cdrlist,List<ZydcRecord> msglist,List<SsmnZyLevel> ssmnzyuser, String fileRealPath ,int type)  
{  
	//先判断文件是否存在
	File diryes = new File(fileRealPath);
	if (diryes.exists()) {
		System.out.println("文件已经存在");
		return;
	}
	if( cdrlist.size() <=0)
		return;
	String sheetname1 = "";
	if(type == 1)//昨天
		sheetname1= this.getYesterdayStr(Constants.DATE_FORMATE);
	else
		sheetname1 = getLastMonthDay();//上个月
	
	FileOutputStream fileoutput = null;
	try {
		fileoutput = new FileOutputStream(fileRealPath);

     SXSSFWorkbook wboutput = new SXSSFWorkbook(10000);
     
     //总的数据
     Sheet sheetoutputFirst = wboutput.createSheet(sheetname1);
     sheetoutputFirst.setColumnWidth(0, 2766);
     sheetoutputFirst.setColumnWidth(1, 4000);
     sheetoutputFirst.setColumnWidth(2, 3766);
     sheetoutputFirst.setColumnWidth(3, 3766);
     sheetoutputFirst.setColumnWidth(4, 5766);
     sheetoutputFirst.setColumnWidth(5, 2766);
     sheetoutputFirst.setColumnWidth(6, 3766);
     sheetoutputFirst.setColumnWidth(7, 3766);
     sheetoutputFirst.setColumnWidth(8, 3766);
     sheetoutputFirst.setColumnWidth(9, 3766);
     sheetoutputFirst.setColumnWidth(10, 1766);
     sheetoutputFirst.setColumnWidth(11, 1766);
     sheetoutputFirst.setColumnWidth(12, 4766);
     sheetoutputFirst.setColumnWidth(13, 4766);
     sheetoutputFirst.setColumnWidth(14, 2766);
     sheetoutputFirst.setColumnWidth(15, 8766);
     int firstcount = 0;
     
   //设置第一行样式
	 CellStyle cellStyleFont = wboutput.createCellStyle();
	 Font font2 = wboutput.createFont();
     font2.setFontName("宋体");
     font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
     font2.setFontHeightInPoints((short) 11);
     cellStyleFont.setFont(font2);
     
     //语音数据
     //row0
     Row rowinputFirst0 = sheetoutputFirst.createRow(0);
     Cell celltitlerowFirst0 = rowinputFirst0.createCell(0);
     //celltitlerowFirst0.setEncoding((short)1);
     celltitlerowFirst0.setCellValue("通话记录统计");
     celltitlerowFirst0.setCellStyle(cellStyleFont);
     firstcount++;
     
    // Palette palette = wboutput.getCustomPalette();  //wb HSSFWorkbook对象
    // palette.setColorAtIndex((short)10, (byte) (0xcc), (byte) (0xff), (byte) (0xcc));
     
     //标题样式
     CellStyle cellStyleTitle = wboutput.createCellStyle();
     Font fontTitle = wboutput.createFont();
     fontTitle.setFontHeightInPoints((short) 11);
     cellStyleTitle.setFont(fontTitle);
     cellStyleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
     cellStyleTitle.setFillForegroundColor((short)50);
     cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
     cellStyleTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
     cellStyleTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
     cellStyleTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
     cellStyleTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
     
     //内容样式
     CellStyle cellStylecontent = wboutput.createCellStyle();
     Font fontcontent = wboutput.createFont();
     fontcontent.setFontHeightInPoints((short) 11);
     cellStylecontent.setFont(fontcontent);
     
     //row1
     Row rowinputFirst1 = sheetoutputFirst.createRow(1);
     firstcount++;
     
   //总的，写第一条数据的
	 SsmnZyLevel l = new SsmnZyLevel();
	 if(cdrlist !=null && cdrlist.size()>0 && cdrlist.get(0) !=null)
	 {
		 l.setProvincecity(cdrlist.get(0).getProvincecity());
	 	 l.setCompany(cdrlist.get(0).getCompany());
	 }
     createCdrRowTitle2007(rowinputFirst1,cellStyleTitle,l);
     
     for(int f = 0; f<cdrlist.size(); f++)
     {
    	 ZydcRecord zrecordf = cdrlist.get(f);
    	 Row rowcdrnum = sheetoutputFirst.createRow(firstcount);
    	 createRowContent2007(rowcdrnum, zrecordf,cellStylecontent);
    	 firstcount++;
     }
     		 
     for (int m = 0; m<=ssmnzyuser.size()-1; m++) 
     {
    	 
    	 SsmnZyLevel szu = ssmnzyuser.get(m);

    	 String stitlename = szu.getBranchactiongroup();
    	 if(stitlename != null && stitlename.length() >0)
    	 {
	    	 Sheet sheetoutput = wboutput.createSheet(stitlename);
	    	 sheetoutput.setColumnWidth(0, 2766);
	    	 sheetoutput.setColumnWidth(1, 4000);
	    	 sheetoutput.setColumnWidth(2, 3766);
	    	 sheetoutput.setColumnWidth(3, 3766);
	    	 sheetoutput.setColumnWidth(4, 5766);
	    	 sheetoutput.setColumnWidth(5, 2766);
	    	 sheetoutput.setColumnWidth(6, 3766);
	    	 sheetoutput.setColumnWidth(7, 3766);
	    	 sheetoutput.setColumnWidth(8, 3766);
	    	 sheetoutput.setColumnWidth(9, 3766);
	    	 sheetoutput.setColumnWidth(10, 1766);
	    	 sheetoutput.setColumnWidth(11, 1766);
	    	 sheetoutput.setColumnWidth(12, 4766);
	    	 sheetoutput.setColumnWidth(13, 4766);
	    	 sheetoutput.setColumnWidth(14, 2766);
	    	 sheetoutput.setColumnWidth(15, 8766);
	    	     	     	 
	         int cdrcount = 0;
	
	         //语音信息
	         Row rowinput0 = sheetoutput.createRow(0);
	         Cell celltitlerow0 = rowinput0.createCell(0);
	         //celltitlerow0.setEncoding((short)1);
	         celltitlerow0.setCellValue("通话记录统计");
	         cdrcount++;
	         
	         Row rowinput1 = sheetoutput.createRow(1);
	         cdrcount++;
	
	         createCdrRowTitle2007(rowinput1,cellStyleTitle,szu); 
	
	    	 for(int c= 0; c<cdrlist.size(); c++)
	    	 {
	    		 ZydcRecord zrecord = cdrlist.get(c);
	    		 if(!"".equals(zrecord.getProvincecity()) && zrecord.getProvincecity().equals(szu.getProvincecity())
	    			&& !"".equals(zrecord.getCompany()) && zrecord.getCompany().equals(szu.getCompany())
	    			&& !"".equals(zrecord.getBusinessdepartment()) && zrecord.getBusinessdepartment().equals(szu.getBusinessdepartment())
	    			&& !"".equals(zrecord.getWarzone()) && zrecord.getWarzone().equals(szu.getWarzone())
	    			&& !"".equals(zrecord.getArea()) && zrecord.getArea().equals(szu.getArea())
	    			&& !"".equals(zrecord.getBranchactiongroup()) && zrecord.getBranchactiongroup().equals(szu.getBranchactiongroup())
	    		 )
	    		 {
	    			 Row rowcdrnum = sheetoutput.createRow(cdrcount);
	    			 createRowContent2007(rowcdrnum, zrecord,cellStylecontent);
	    			 cdrcount++;
	    		 }   		 
	    	 }
     
    	}
     }
		wboutput.write(fileoutput);
		fileoutput.close();
	} catch (IOException e) {
		e.printStackTrace();
	}finally {
		if(fileoutput != null)
			try {
				fileoutput.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
     
}  

public void createExcelSheet2003(List<ZydcRecord> cdrlist,List<ZydcRecord> msglist,List<SsmnZyLevel> ssmnzyuser, String fileRealPath ,int type)  
{  
	//先判断文件是否存在
	File diryes = new File(fileRealPath);
	if (diryes.exists()) {
		System.out.println("文件已经存在");
		return;
	}
	if(cdrlist.size() <=0)
		return;
	String sheetname1 = "";
	if(type == 1)//昨天
		sheetname1= this.getYesterdayStr(Constants.DATE_FORMATE);
	else
		sheetname1 = getLastMonthDay();//上个月
	
	FileOutputStream fileoutput = null;
	try {
		fileoutput = new FileOutputStream(fileRealPath);
	
	
	 HSSFWorkbook wboutput = new HSSFWorkbook();
	 
	 //总的数据
	 HSSFSheet sheetoutputFirst = wboutput.createSheet(sheetname1);
	 sheetoutputFirst.setColumnWidth(0, 2766);
	 sheetoutputFirst.setColumnWidth(1, 4000);
	 sheetoutputFirst.setColumnWidth(2, 3766);
	 sheetoutputFirst.setColumnWidth(3, 3766);
	 sheetoutputFirst.setColumnWidth(4, 5766);
	 sheetoutputFirst.setColumnWidth(5, 2766);
	 sheetoutputFirst.setColumnWidth(6, 3766);
	 sheetoutputFirst.setColumnWidth(7, 3766);
	 sheetoutputFirst.setColumnWidth(8, 3766);
	 sheetoutputFirst.setColumnWidth(9, 3766);
	 sheetoutputFirst.setColumnWidth(10, 1766);
	 sheetoutputFirst.setColumnWidth(11, 1766);
	 sheetoutputFirst.setColumnWidth(12, 4766);
	 sheetoutputFirst.setColumnWidth(13, 4766);
	 sheetoutputFirst.setColumnWidth(14, 2766);
	 sheetoutputFirst.setColumnWidth(15, 8766);
	 int firstcount = 0;
	 
	//设置第一行样式
	 HSSFCellStyle cellStyleFont = wboutput.createCellStyle();
	 HSSFFont font2 = wboutput.createFont();
	 font2.setFontName("宋体");
	 font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
	 font2.setFontHeightInPoints((short) 11);
	 cellStyleFont.setFont(font2);
	 
	 //语音数据
	 //row0
	 HSSFRow rowinputFirst0 = sheetoutputFirst.createRow(0);
	 HSSFCell celltitlerowFirst0 = rowinputFirst0.createCell(0);
	 //celltitlerowFirst0.setEncoding((short)1);
	 celltitlerowFirst0.setCellValue("通话记录统计");
	 celltitlerowFirst0.setCellStyle(cellStyleFont);
	 firstcount++;
	 
	// Palette palette = wboutput.getCustomPalette();  //wb HSSFWorkbook对象
	// palette.setColorAtIndex((short)10, (byte) (0xcc), (byte) (0xff), (byte) (0xcc));
	 
	 //标题样式
	 HSSFCellStyle cellStyleTitle = wboutput.createCellStyle();
	 HSSFFont fontTitle = wboutput.createFont();
	 fontTitle.setFontHeightInPoints((short) 11);
	 cellStyleTitle.setFont(fontTitle);
	 cellStyleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	 cellStyleTitle.setFillForegroundColor((short)50);
	 cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
	 cellStyleTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
	 cellStyleTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
	 cellStyleTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
	 cellStyleTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	 
	 //内容样式
	 HSSFCellStyle cellStylecontent = wboutput.createCellStyle();
	 HSSFFont fontcontent = wboutput.createFont();
	 fontcontent.setFontHeightInPoints((short) 11);
	 cellStylecontent.setFont(fontcontent);
	 
	 //row1
	 HSSFRow rowinputFirst1 = sheetoutputFirst.createRow(1);
	 firstcount++;
	 //总的，写第一条数据的
	 SsmnZyLevel l = new SsmnZyLevel();
	 if(cdrlist !=null && cdrlist.size()>0 && cdrlist.get(0) !=null)
	 {
		 l.setProvincecity(cdrlist.get(0).getProvincecity());
	 	 l.setCompany(cdrlist.get(0).getCompany());
	 }
	 createCdrRowTitle(rowinputFirst1,cellStyleTitle,l);
	 
	 for(int f = 0; f<cdrlist.size(); f++)
	 {
		 ZydcRecord zrecordf = cdrlist.get(f);
		 HSSFRow rowcdrnum = sheetoutputFirst.createRow(firstcount);
		 createRowContent(rowcdrnum, zrecordf,cellStylecontent);
		 firstcount++;
	 }
	 	 
	 for (int m = 0; m<=ssmnzyuser.size()-1; m++) 
	 {
		 
		 SsmnZyLevel szu = ssmnzyuser.get(m);
	
		 String stitlename = szu.getBranchactiongroup();
		 
		 if(stitlename != null && stitlename.length() >0)
		 {
			 HSSFSheet sheetoutput = wboutput.createSheet(stitlename);
			 sheetoutput.setColumnWidth(0, 2766);
			 sheetoutput.setColumnWidth(1, 4000);
			 sheetoutput.setColumnWidth(2, 3766);
			 sheetoutput.setColumnWidth(3, 3766);
			 sheetoutput.setColumnWidth(4, 5766);
			 sheetoutput.setColumnWidth(5, 2766);
			 sheetoutput.setColumnWidth(6, 3766);
			 sheetoutput.setColumnWidth(7, 3766);
			 sheetoutput.setColumnWidth(8, 3766);
			 sheetoutput.setColumnWidth(9, 3766);
			 sheetoutput.setColumnWidth(10, 1766);
			 sheetoutput.setColumnWidth(11, 1766);
			 sheetoutput.setColumnWidth(12, 4766);
			 sheetoutput.setColumnWidth(13, 4766);
			 sheetoutput.setColumnWidth(14, 2766);
			 sheetoutput.setColumnWidth(15, 8766);
			     	     	 
		     int cdrcount = 0;
		
		     //语音信息
		     HSSFRow rowinput0 = sheetoutput.createRow(0);
		     HSSFCell celltitlerow0 = rowinput0.createCell(0);
		     //celltitlerow0.setEncoding((short)1);
		     celltitlerow0.setCellValue("通话记录统计");
		     cdrcount++;
		     
		     HSSFRow rowinput1 = sheetoutput.createRow(1);
		     cdrcount++;
		
		     createCdrRowTitle(rowinput1,cellStyleTitle,szu); 
		
			 for(int c= 0; c<cdrlist.size(); c++)
			 {
				 ZydcRecord zrecord = cdrlist.get(c);
				 if(!"".equals(zrecord.getProvincecity()) && zrecord.getProvincecity().equals(szu.getProvincecity())
					&& !"".equals(zrecord.getCompany()) && zrecord.getCompany().equals(szu.getCompany())
					&& !"".equals(zrecord.getBusinessdepartment()) && zrecord.getBusinessdepartment().equals(szu.getBusinessdepartment())
					&& !"".equals(zrecord.getWarzone()) && zrecord.getWarzone().equals(szu.getWarzone())
					&& !"".equals(zrecord.getArea()) && zrecord.getArea().equals(szu.getArea())
					&& !"".equals(zrecord.getBranchactiongroup()) && zrecord.getBranchactiongroup().equals(szu.getBranchactiongroup())
				 )
				 {
					 HSSFRow rowcdrnum = sheetoutput.createRow(cdrcount);
					 createRowContent(rowcdrnum, zrecord,cellStylecontent);
					 cdrcount++;
				 }   		 
			 }
	
		}
	 }
		wboutput.write(fileoutput);
		fileoutput.close();
	} catch (IOException e) {
		e.printStackTrace();
	}finally {
		if(fileoutput != null)
			try {
				fileoutput.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
 
}  

public void createXSSFExcelSheet(List<ZydcRecord> cdrlist,List<ZydcRecord> msglist,List<SsmnZyLevel> ssmnzyuser, String fileRealPath ,int type,SsmnZyLevel level)  
{  
	//先判断文件是否存在
	File diryes = new File(fileRealPath);
	if (diryes.exists()) {
		System.out.println("文件已经存在");
		return;
	}

	String sheetname1 = "";
	if(type == 1)//昨天
		sheetname1= this.getYesterdayStr(Constants.DATE_FORMATE);
	else
		sheetname1 = getLastMonthDay();//上个月
	
	FileOutputStream fileoutput = null;
	try {
		fileoutput = new FileOutputStream(fileRealPath);
	
	 XSSFWorkbook wboutput = new XSSFWorkbook();
	 
	 //总的数据
	 XSSFSheet sheetoutputFirst = wboutput.createSheet(sheetname1);
	 sheetoutputFirst.setColumnWidth(0, 3766);
	 sheetoutputFirst.setColumnWidth(3, 6766);
	 sheetoutputFirst.setColumnWidth(5, 3766);
	 sheetoutputFirst.setColumnWidth(6, 3766);
	 sheetoutputFirst.setColumnWidth(9, 6766);
	 sheetoutputFirst.setColumnWidth(10, 6766);
	 sheetoutputFirst.setColumnWidth(12, 3766);
	 sheetoutputFirst.setColumnWidth(13, 3766);
	 int firstcount = 0;
	 
	//设置第一行样式
	 XSSFCellStyle cellStyleFont = wboutput.createCellStyle();
	 XSSFFont font2 = wboutput.createFont();
	 font2.setFontName("宋体");
	 font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
	 font2.setFontHeightInPoints((short) 11);
	 cellStyleFont.setFont(font2);
	 
	 //语音数据
	 //row0
	 XSSFRow rowinputFirst0 = sheetoutputFirst.createRow(0);
	 XSSFCell celltitlerowFirst0 = rowinputFirst0.createCell(0);
	 //celltitlerowFirst0.setEncoding((short)1);
	 celltitlerowFirst0.setCellValue("通话记录统计");
	 celltitlerowFirst0.setCellStyle(cellStyleFont);
	 firstcount++;
	 
	// XSSFPalette palette = wboutput.getCustomPalette();  //wb HSSFWorkbook对象
	 //palette.setColorAtIndex((short)10, (byte) (0xcc), (byte) (0xff), (byte) (0xcc));
	 
	 //标题样式
	 XSSFCellStyle cellStyleTitle = wboutput.createCellStyle();
	 XSSFFont fontTitle = wboutput.createFont();
	 fontTitle.setFontHeightInPoints((short) 11);
	 cellStyleTitle.setFont(fontTitle);
	 cellStyleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	 cellStyleTitle.setFillForegroundColor((short)50);
	 cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
	 cellStyleTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
	 cellStyleTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
	 cellStyleTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
	 cellStyleTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	 
	 //内容样式
	 XSSFCellStyle cellStylecontent = wboutput.createCellStyle();
	 XSSFFont fontcontent = wboutput.createFont();
	 fontcontent.setFontHeightInPoints((short) 11);
	 cellStylecontent.setFont(fontcontent);
	 
	 //row1
	 XSSFRow rowinputFirst1 = sheetoutputFirst.createRow(1);
	 firstcount++;
	 createXSSFCdrRowTitle(rowinputFirst1,cellStyleTitle);
	
	 ////------------------------------------------
	 
	// cdao.getCdrStatRecordToExcel(level, "3",sheetoutputFirst,firstcount,cellStylecontent);
	 
	 
	 for(int f = 0; f<cdrlist.size(); f++)
	 {
		 ZydcRecord zrecordf = cdrlist.get(f);
		 XSSFRow rowcdrnum = sheetoutputFirst.createRow(firstcount);
		 createXSSFRowContent(rowcdrnum, zrecordf,cellStylecontent);
		 firstcount++;
	 }
	 
	 ///-------------------------------------------------
	 
	 //短信数据
	 //写第一行
	 	firstcount += 5;
		 XSSFRow rowsmstitlerowFirst = sheetoutputFirst.createRow(firstcount);
		 XSSFCell celltitlesms0First = rowsmstitlerowFirst.createCell(0);
		 celltitlesms0First.setCellValue("短信内容统计");
		 celltitlesms0First.setCellStyle(cellStyleFont);
		 firstcount++;
		 
		 XSSFRow rowsmsFirst = sheetoutputFirst.createRow(firstcount);
		 createXSSFSmsRowTitle(rowsmsFirst,cellStyleTitle); 
		 firstcount++;
		 
		 //写短信的数据
		 for(int s= 0; s<msglist.size();s++)
		 {
			 ZydcRecord mrecord = msglist.get(s);
			 XSSFRow rowsmsnum = sheetoutputFirst.createRow(firstcount);
			 createXSSFSmsRowContent(rowsmsnum,mrecord,cellStylecontent);
			 
			 firstcount++;
		 }
		 
	 for (int m = 0; m<=ssmnzyuser.size()-1; m++) {
		 
		 SsmnZyLevel szu = ssmnzyuser.get(m);
	
		 String stitlename = szu.getBranchactiongroup();
		 //System.out.print(stitlename);
		 //System.out.print("\n");
		 if(stitlename != null && stitlename.length() >0)
		 {
		 XSSFSheet sheetoutput = wboutput.createSheet(stitlename);
		 sheetoutput.setColumnWidth(0, 3766);
		 sheetoutput.setColumnWidth(3, 6766);
		 sheetoutput.setColumnWidth(5, 3766);
		 sheetoutput.setColumnWidth(6, 3766);
		 sheetoutput.setColumnWidth(9, 6766);
		 sheetoutput.setColumnWidth(10, 6766);
		 sheetoutput.setColumnWidth(12, 3766);
		 sheetoutput.setColumnWidth(13, 3766);
		     	     	 
	     int cdrcount = 0;
	
	     //语音信息
	     XSSFRow rowinput0 = sheetoutput.createRow(0);
	     XSSFCell celltitlerow0 = rowinput0.createCell(0);
	     //celltitlerow0.setEncoding((short)1);
	     celltitlerow0.setCellValue("通话记录统计");
	     cdrcount++;
	     
	     XSSFRow rowinput1 = sheetoutput.createRow(1);
	     cdrcount++;
	
	     createXSSFCdrRowTitle(rowinput1,cellStyleTitle); 
	
		 for(int c= 0; c<cdrlist.size(); c++)
		 {
			 ZydcRecord zrecord = cdrlist.get(c);
			 if(!"".equals(zrecord.getProvincecity()) && zrecord.getProvincecity().equals(szu.getProvincecity())
				&& !"".equals(zrecord.getCompany()) && zrecord.getCompany().equals(szu.getCompany())
				&& !"".equals(zrecord.getBusinessdepartment()) && zrecord.getBusinessdepartment().equals(szu.getBusinessdepartment())
				&& !"".equals(zrecord.getWarzone()) && zrecord.getWarzone().equals(szu.getWarzone())
				&& !"".equals(zrecord.getArea()) && zrecord.getArea().equals(szu.getArea())
				&& !"".equals(zrecord.getBranchactiongroup()) && zrecord.getBranchactiongroup().equals(szu.getBranchactiongroup())
			 )
			 {
				 XSSFRow rowcdrnum = sheetoutput.createRow(cdrcount);
				 createXSSFRowContent(rowcdrnum, zrecord,cellStylecontent);
				 cdrcount++;
			 }   		 
		 }
	 
		//短信信息
			//短信统计的标题,通话记录和短信记录间隔5行
			 int inum = cdrcount +5;
			
			 XSSFRow rowsmstitlerow = sheetoutput.createRow(inum);
			 XSSFCell celltitlesms0 = rowsmstitlerow.createCell(0);
	
			 celltitlesms0.setCellValue("短信内容统计");
			 inum++;
	
			 XSSFRow rowsms = sheetoutput.createRow(inum);
			createXSSFSmsRowTitle(rowsms,cellStyleTitle); 		 
			 
	      int smsIndex = 0;
			 for(int g=0; g<msglist.size();g++)
			 {
				 ZydcRecord mrecord = msglist.get(g);
			 if(!"".equals(mrecord.getProvincecity()) && mrecord.getProvincecity().equals(szu.getProvincecity())
				&& !"".equals(mrecord.getCompany()) && mrecord.getCompany().equals(szu.getCompany())
				&& !"".equals(mrecord.getBusinessdepartment()) && mrecord.getBusinessdepartment().equals(szu.getBusinessdepartment())
				&& !"".equals(mrecord.getWarzone()) && mrecord.getWarzone().equals(szu.getWarzone())
				&& !"".equals(mrecord.getArea()) && mrecord.getArea().equals(szu.getArea())
				&& !"".equals(mrecord.getBranchactiongroup()) && mrecord.getBranchactiongroup().equals(szu.getBranchactiongroup())
			 )
			 {
				 XSSFRow rowsmsnum = sheetoutput.createRow(inum+smsIndex+1);
				
				 createXSSFSmsRowContent(rowsmsnum,mrecord,cellStylecontent);
				 
				 smsIndex++;
			 }
			 }
		 }
	
	 }
	 
		wboutput.write(fileoutput);
		fileoutput.close();
	} catch (IOException e) {
		e.printStackTrace();
	}finally {
		if(fileoutput != null)
			try {
				fileoutput.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	 
}  

public void createCdrRowTitle( HSSFRow rowinput1, HSSFCellStyle cellStyleTitle,SsmnZyLevel level)
{	
	HSSFCell cell0row0 = rowinput1.createCell(0);
    cell0row0.setCellValue("序号");
    cell0row0.setCellStyle(cellStyleTitle);
    
    String businName = new UtilFunctionDao().getLevelName(1, level);//事业部
    HSSFCell cell1row0 = rowinput1.createCell(1);
    cell1row0.setCellValue(businName);
    cell1row0.setCellStyle(cellStyleTitle);
    
    String warName = new UtilFunctionDao().getLevelName(2, level);//战区
    HSSFCell cell2row0 = rowinput1.createCell(2);
    cell2row0.setCellValue(warName);
    cell2row0.setCellStyle(cellStyleTitle);
    
    String areaName = new UtilFunctionDao().getLevelName(3, level);//片区
    HSSFCell cell3row0 = rowinput1.createCell(3);
    cell3row0.setCellValue(areaName);
    cell3row0.setCellStyle(cellStyleTitle);
    
    String branName = new UtilFunctionDao().getLevelName(4, level);//行动组
    HSSFCell cell4row0 = rowinput1.createCell(4);
    cell4row0.setCellValue(branName);
    cell4row0.setCellStyle(cellStyleTitle);
    
    HSSFCell cell5row0 = rowinput1.createCell(5);
    cell5row0.setCellValue("经纪人姓名");
    cell5row0.setCellStyle(cellStyleTitle);
    
    HSSFCell cell6row0 = rowinput1.createCell(6);
    cell6row0.setCellValue("员工编号");
    cell6row0.setCellStyle(cellStyleTitle);
    
    HSSFCell cell7row0 = rowinput1.createCell(7);
    cell7row0.setCellValue("主号码");
    cell7row0.setCellStyle(cellStyleTitle);
    
    HSSFCell cell8row0 = rowinput1.createCell(8);
    cell8row0.setCellValue("副号码");
    cell8row0.setCellStyle(cellStyleTitle);
    
    if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))
    {
    	HSSFCell cell9row0 = rowinput1.createCell(9);
	    cell9row0.setCellValue("第一联系人");
	    cell9row0.setCellStyle(cellStyleTitle);
    	
    	HSSFCell cell10row0 = rowinput1.createCell(10);
	    cell10row0.setCellValue("客户电话");
	    cell10row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell11row0 = rowinput1.createCell(11);
	    cell11row0.setCellValue("渠道");
	    cell11row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell12row0 = rowinput1.createCell(12);
	    cell12row0.setCellValue("通话类型");
	    cell12row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell13row0 = rowinput1.createCell(13);
	    cell13row0.setCellValue("通话时间");
	    cell13row0.setCellStyle(cellStyleTitle);
	
	    HSSFCell cell14row0 = rowinput1.createCell(14);
	    cell14row0.setCellValue("通话时长");
	    cell14row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell15row0 = rowinput1.createCell(15);
	    cell15row0.setCellValue("录音文件名称");
	    cell15row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell16row0 = rowinput1.createCell(16);
	    cell16row0.setCellValue("是否标记为骚扰电话");
	    cell16row0.setCellStyle(cellStyleTitle);
	    
	    if(ConfigMgr.getAddRemark().equals("1"))
	    {
	    	HSSFCell cell17row0 = rowinput1.createCell(17);
		    cell17row0.setCellValue("备注");
		    cell17row0.setCellStyle(cellStyleTitle);
	    }
    }else
    {
	    HSSFCell cell9row0 = rowinput1.createCell(9);
	    cell9row0.setCellValue("客户电话");
	    cell9row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell10row0 = rowinput1.createCell(10);
	    cell10row0.setCellValue("渠道");
	    cell10row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell11row0 = rowinput1.createCell(11);
	    cell11row0.setCellValue("通话类型");
	    cell11row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell12row0 = rowinput1.createCell(12);
	    cell12row0.setCellValue("通话时间");
	    cell12row0.setCellStyle(cellStyleTitle);
	
	    HSSFCell cell13row0 = rowinput1.createCell(13);
	    cell13row0.setCellValue("通话时长");
	    cell13row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell14row0 = rowinput1.createCell(14);
	    cell14row0.setCellValue("录音文件名称");
	    cell14row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell15row0 = rowinput1.createCell(15);
	    cell15row0.setCellValue("是否标记为骚扰电话");
	    cell15row0.setCellStyle(cellStyleTitle);
	    
	    if(ConfigMgr.getAddRemark().equals("1"))
	    {
	    	HSSFCell cell16row0 = rowinput1.createCell(16);
		    cell16row0.setCellValue("备注");
		    cell16row0.setCellStyle(cellStyleTitle);
	    }
    }
}

public void createRowContent(HSSFRow rowcdrnum,ZydcRecord zrecord,HSSFCellStyle cellStylecontent)
{
	 HSSFCell cell0 = rowcdrnum.createCell(0);
	 cell0.setCellValue(zrecord.getStreamNumber()+"");
	 cell0.setCellStyle(cellStylecontent);
	 
	 HSSFCell cell1 = rowcdrnum.createCell(1);
	 cell1.setCellValue(zrecord.getBusinessdepartment());
	 cell1.setCellStyle(cellStylecontent);
	 
	 HSSFCell cell2 = rowcdrnum.createCell(2);
	 cell2.setCellValue(zrecord.getWarzone());
	 cell2.setCellStyle(cellStylecontent);
	 
	 HSSFCell cell3 = rowcdrnum.createCell(3);
	 cell3.setCellValue(zrecord.getArea());
	 cell3.setCellStyle(cellStylecontent);
	 
	 HSSFCell cell4 = rowcdrnum.createCell(4);
	 cell4.setCellValue(zrecord.getBranchactiongroup());
	 cell4.setCellStyle(cellStylecontent);
	 
	 HSSFCell cell5 = rowcdrnum.createCell(5);
	 cell5.setCellValue(zrecord.getUsername());
	 cell5.setCellStyle(cellStylecontent);
	 
	 HSSFCell cell6 = rowcdrnum.createCell(6);
	 cell6.setCellValue(zrecord.getEmpno());
	 cell6.setCellStyle(cellStylecontent);
	 
	 HSSFCell cell7 = rowcdrnum.createCell(7);
	 cell7.setCellValue(zrecord.getMsisdn());
	 cell7.setCellStyle(cellStylecontent);
	 
	 HSSFCell cell8 = rowcdrnum.createCell(8);
	 cell8.setCellValue(zrecord.getSsmnnumber());
	 cell8.setCellStyle(cellStylecontent);
	 
	 if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))
	 {
		 HSSFCell cell9 = rowcdrnum.createCell(9);
		 cell9.setCellValue(zrecord.getFirstMsisdn());
		 cell9.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell10 = rowcdrnum.createCell(10);
		 cell10.setCellValue(zrecord.getClientnumber());
		 cell10.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell11 = rowcdrnum.createCell(11);
		 cell11.setCellValue(zrecord.getChannelname());
		 cell11.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell12 = rowcdrnum.createCell(12);
		 cell12.setCellValue(zrecord.getCalltype());
		 cell12.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell13 = rowcdrnum.createCell(13);
		 cell13.setCellStyle(cellStylecontent); 
		 cell13.setCellValue(zrecord.getCallstarttime());
		 	 
		 HSSFCell cell14 = rowcdrnum.createCell(14);
	     cell14.setCellValue(zrecord.getStrCallDuration());
	     cell14.setCellStyle(cellStylecontent);
		 
	     HSSFCell cell15 = rowcdrnum.createCell(15);
		 cell15.setCellValue(zrecord.getFileName());
		 cell15.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell16 = rowcdrnum.createCell(16);
		 cell16.setCellValue(zrecord.getIsblacknum());
		 cell16.setCellStyle(cellStylecontent);
		 
		 if(ConfigMgr.getAddRemark().equals("1"))
		 {
			 HSSFCell cell17 = rowcdrnum.createCell(17);
			 cell17.setCellValue(zrecord.getRemark());
			 cell17.setCellStyle(cellStylecontent);
		 }
	 }else
	 {
		 HSSFCell cell9 = rowcdrnum.createCell(9);
		 cell9.setCellValue(zrecord.getClientnumber());
		 cell9.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell10 = rowcdrnum.createCell(10);
		 cell10.setCellValue(zrecord.getChannelname());
		 cell10.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell11 = rowcdrnum.createCell(11);
		 cell11.setCellValue(zrecord.getCalltype());
		 cell11.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell12 = rowcdrnum.createCell(12);
		 cell12.setCellStyle(cellStylecontent); 
		 cell12.setCellValue(zrecord.getCallstarttime());
		 	 
		 HSSFCell cell13 = rowcdrnum.createCell(13);
	     cell13.setCellValue(zrecord.getStrCallDuration());
	     cell13.setCellStyle(cellStylecontent);
		 
	     HSSFCell cell14 = rowcdrnum.createCell(14);
		 cell14.setCellValue(zrecord.getFileName());
		 cell14.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell15 = rowcdrnum.createCell(15);
		 cell15.setCellValue(zrecord.getIsblacknum());
		 cell15.setCellStyle(cellStylecontent);
		 
		 if(ConfigMgr.getAddRemark().equals("1"))
		 {
			 HSSFCell cell16 = rowcdrnum.createCell(16);
			 cell16.setCellValue(zrecord.getRemark());
			 cell16.setCellStyle(cellStylecontent);
		 }
	 }
}

public void createXSSFCdrRowTitle( XSSFRow rowinput1, XSSFCellStyle cellStyleTitle)
{	
	XSSFCell cell0row0 = rowinput1.createCell(0);
    cell0row0.setCellValue("来电时间");
    cell0row0.setCellStyle(cellStyleTitle);
    
    XSSFCell cell1row0 = rowinput1.createCell(1);
    cell1row0.setCellValue("房源信息");
    cell1row0.setCellStyle(cellStyleTitle);
    
    XSSFCell cell2row0 = rowinput1.createCell(2);
    cell2row0.setCellValue("事业部");
    cell2row0.setCellStyle(cellStyleTitle);
    
    XSSFCell cell3row0 = rowinput1.createCell(3);
    cell3row0.setCellValue("分行组别");
    cell3row0.setCellStyle(cellStyleTitle);
    
    XSSFCell cell4row0 = rowinput1.createCell(4);
    cell4row0.setCellValue("经纪人姓名");
    cell4row0.setCellStyle(cellStyleTitle);
    
    XSSFCell cell5row0 = rowinput1.createCell(5);
    cell5row0.setCellValue("主号码");
    cell5row0.setCellStyle(cellStyleTitle);
    
    XSSFCell cell6row0 = rowinput1.createCell(6);
    cell6row0.setCellValue("副号码");
    cell6row0.setCellStyle(cellStyleTitle);
    
    XSSFCell cell7row0 = rowinput1.createCell(7);
    cell7row0.setCellValue("渠道标记");
    cell7row0.setCellStyle(cellStyleTitle);
    
    XSSFCell cell8row0 = rowinput1.createCell(8);
    cell8row0.setCellValue("通话类型");
    cell8row0.setCellStyle(cellStyleTitle);
    
    XSSFCell cell9row0 = rowinput1.createCell(9);
    cell9row0.setCellValue("通话开始时间");
    cell9row0.setCellStyle(cellStyleTitle);
    
    XSSFCell cell10row0 = rowinput1.createCell(10);
    cell10row0.setCellValue("通话结束时间");
    cell10row0.setCellStyle(cellStyleTitle);
    
    XSSFCell cell11row0 = rowinput1.createCell(11);
    cell11row0.setCellValue("通话时长");
    cell11row0.setCellStyle(cellStyleTitle);
    
    XSSFCell cell12row0 = rowinput1.createCell(12);
    cell12row0.setCellValue("客户电话");
    cell12row0.setCellStyle(cellStyleTitle);
    
    XSSFCell cell13row0 = rowinput1.createCell(13);
    cell13row0.setCellValue("录音文件名称");
    cell13row0.setCellStyle(cellStyleTitle);
    
    XSSFCell cell14row0 = rowinput1.createCell(14);
    cell14row0.setCellValue("备注（无效/接通/未接通）");
    cell14row0.setCellStyle(cellStyleTitle);
}

static public void createXSSFRowContent(XSSFRow rowcdrnum,ZydcRecord zrecord,XSSFCellStyle cellStylecontent)
{
	XSSFCell cell0 = rowcdrnum.createCell(0);
	 cell0.setCellValue(zrecord.getYesterday());
	 cell0.setCellStyle(cellStylecontent);
	 
	 XSSFCell cell1 = rowcdrnum.createCell(1);
	 cell1.setCellValue("");
	 cell1.setCellStyle(cellStylecontent);
	 
	 XSSFCell cell2 = rowcdrnum.createCell(2);
	 cell2.setCellValue("");
	 cell2.setCellStyle(cellStylecontent);
	 
	 XSSFCell cell3 = rowcdrnum.createCell(3);
	 cell3.setCellValue(zrecord.getGroupname());
	 cell3.setCellStyle(cellStylecontent);
	 
	 XSSFCell cell4 = rowcdrnum.createCell(4);
	 cell4.setCellValue(zrecord.getUsername());
	 cell4.setCellStyle(cellStylecontent);
	 
	 XSSFCell cell5 = rowcdrnum.createCell(5);
	 cell5.setCellValue(zrecord.getMsisdn());
	 cell5.setCellStyle(cellStylecontent);
	 
	 XSSFCell cell6 = rowcdrnum.createCell(6);
	 cell6.setCellValue(zrecord.getSsmnnumber());
	 cell6.setCellStyle(cellStylecontent);
	 
	 XSSFCell cell7 = rowcdrnum.createCell(7);
	 cell7.setCellValue(zrecord.getChannelname());
	 cell7.setCellStyle(cellStylecontent);
	 
	 XSSFCell cell8 = rowcdrnum.createCell(8);
	 cell8.setCellValue(zrecord.getCalltype());
	 cell8.setCellStyle(cellStylecontent);
	 
	 XSSFCell cell9 = rowcdrnum.createCell(9);
	 cell9.setCellValue(zrecord.getCallstarttime());
	 cell9.setCellStyle(cellStylecontent);
	 
	 XSSFCell cell10 = rowcdrnum.createCell(10);
	 cell10.setCellValue(zrecord.getCallstoptime());
	 cell10.setCellStyle(cellStylecontent);
	 
	 XSSFCell cell11 = rowcdrnum.createCell(11);
	 cell11.setCellStyle(cellStylecontent);
	 int caldr = zrecord.getCallduration();
      int N = caldr/3600;
      caldr = caldr%3600;
       int K = caldr/60;
       caldr = caldr%60;
       int M = caldr;
       String scaldr="";
       if(N>=10)
    	   scaldr +=N;
       else
       {
    	   scaldr +="0";
    	   scaldr +=N;
       }
       scaldr +=":";
       if(K>=10)
    	   scaldr +=K;
       else
       {
    	   scaldr +="0";
    	   scaldr +=K;
       }
       scaldr +=":";
       if(M>=10)
    	   scaldr +=M;
       else
       {
    	   scaldr +="0";
    	   scaldr +=M;
       }
      
      // System.out.println("时间是："+N+":"+K+":"+M); 
	 cell11.setCellValue(scaldr);
	 
	 XSSFCell cell12 = rowcdrnum.createCell(12);
	 cell12.setCellValue(zrecord.getClientnumber());
	 cell12.setCellStyle(cellStylecontent);
	 
	 XSSFCell cell13 = rowcdrnum.createCell(13);
	 cell13.setCellValue("");
	 cell13.setCellStyle(cellStylecontent);
	 
	 XSSFCell cell14 = rowcdrnum.createCell(14);
	 cell14.setCellValue(zrecord.getEndreason());
	 cell14.setCellStyle(cellStylecontent);
}

public void createSmsRowTitle (HSSFRow rowsms, HSSFCellStyle cellStyleTitle)
{
	HSSFCell cell1sms0 = rowsms.createCell(0);
	 cell1sms0.setCellValue("序号");
	 cell1sms0.setCellStyle(cellStyleTitle);
	 
	/*Cell cell1sms1 = rowsms.createCell(1);
	cell1sms1.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1sms1.setCellValue("接收日期");
	 cell1sms1.setCellStyle(cellStyleTitle);*/
	 
	 HSSFCell cell1sms1 = rowsms.createCell(1);
	 cell1sms1.setCellValue("接收时间");
	 cell1sms1.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell1sms2 = rowsms.createCell(2);
	 cell1sms2.setCellValue("事业部");
	 cell1sms2.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell1sms3 = rowsms.createCell(3);
	 cell1sms3.setCellValue("战区");
	 cell1sms3.setCellStyle(cellStyleTitle);
 
	 HSSFCell cell1sms4 = rowsms.createCell(4);
	 cell1sms4.setCellValue("片区");
	 cell1sms4.setCellStyle(cellStyleTitle);

	 HSSFCell cell1sms5 = rowsms.createCell(5);
	 cell1sms5.setCellValue("分行组别");
	 cell1sms5.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell1sms6 = rowsms.createCell(6);
	 cell1sms6.setCellValue("经纪人姓名");
	 cell1sms6.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell1sms7 = rowsms.createCell(7);
	 cell1sms7.setCellValue("员工编号");
	 cell1sms7.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell1sms8 = rowsms.createCell(8);
	 cell1sms8.setCellValue("主号码");
	 cell1sms8.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell1sms9 = rowsms.createCell(9);
	 cell1sms9.setCellValue("副号码");
	 cell1sms9.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell1sms10 = rowsms.createCell(10);
	 cell1sms10.setCellValue("客户电话");
	 cell1sms10.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell1sms11 = rowsms.createCell(11);
	 cell1sms11.setCellValue("渠道"); 
	 cell1sms11.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell1sms12 = rowsms.createCell(12);
	 cell1sms12.setCellValue("短信内容"); 
	 cell1sms12.setCellStyle(cellStyleTitle);
}

public void createSmsRowContent(HSSFRow rowsmsnum,ZydcRecord mrecord, HSSFCellStyle cellStyleTitle)
{
	 HSSFCell cell0 = rowsmsnum.createCell(0);
	 cell0.setCellValue(mrecord.getStreamNumber()+"");
	 cell0.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell1 = rowsmsnum.createCell(1);
	 cell1.setCellValue(mrecord.getRcvmsgtime());
	 cell1.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell2 = rowsmsnum.createCell(2);
	 cell2.setCellValue(mrecord.getBusinessdepartment());
	 cell2.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell3 = rowsmsnum.createCell(3);
	 cell3.setCellValue(mrecord.getWarzone());
	 cell3.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell4 = rowsmsnum.createCell(4);
	 cell4.setCellValue(mrecord.getArea());
	 cell4.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell5 = rowsmsnum.createCell(5);
	 cell5.setCellValue(mrecord.getBranchactiongroup());
	 cell5.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell6 = rowsmsnum.createCell(6);
	 cell6.setCellValue(mrecord.getUsername());
	 cell6.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell7 = rowsmsnum.createCell(7);
	 cell7.setCellValue(mrecord.getEmpno());
	 cell7.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell8 = rowsmsnum.createCell(8);
	 cell8.setCellValue(mrecord.getMsisdn());
	 cell8.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell9 = rowsmsnum.createCell(9);
	 cell9.setCellValue(mrecord.getSsmnnumber());
	 cell9.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell10 = rowsmsnum.createCell(10);
	 cell10.setCellValue(mrecord.getClientnumber());
	 cell10.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell11 = rowsmsnum.createCell(11);
	 cell11.setCellValue(mrecord.getChannelname());
	 cell11.setCellStyle(cellStyleTitle);
	 
	 HSSFCell cell12 = rowsmsnum.createCell(12);
	 cell12.setCellValue(mrecord.getMsgcontent());
	 cell12.setCellStyle(cellStyleTitle);
}

public void createXSSFSmsRowTitle (XSSFRow rowsms, XSSFCellStyle cellStyleTitle)
{
	XSSFCell cell1sms0 = rowsms.createCell(0);
	 cell1sms0.setCellValue("通信时间");
	 cell1sms0.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell1sms1 = rowsms.createCell(1);
	 cell1sms1.setCellValue("房源信息");
	 cell1sms1.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell1sms2 = rowsms.createCell(2);
	 cell1sms2.setCellValue("事业部");
	 cell1sms2.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell1sms3 = rowsms.createCell(3);
	 cell1sms3.setCellValue("分行组别");
	 cell1sms3.setCellStyle(cellStyleTitle);
 
	 XSSFCell cell1sms4 = rowsms.createCell(4);
	 cell1sms4.setCellValue("经纪人姓名");
	 cell1sms4.setCellStyle(cellStyleTitle);

	 XSSFCell cell1sms5 = rowsms.createCell(5);
	 cell1sms5.setCellValue("主号码");
	 cell1sms5.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell1sms6 = rowsms.createCell(6);
	 cell1sms6.setCellValue("副号码");
	 cell1sms6.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell1sms7 = rowsms.createCell(7);
	 cell1sms7.setCellValue("渠道标记");
	 cell1sms7.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell1sms8 = rowsms.createCell(8);
	 cell1sms8.setCellValue("客户号码");
	 cell1sms8.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell1sms9 = rowsms.createCell(9);
	 cell1sms9.setCellValue("短信接收时间");
	 cell1sms9.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell1sms10 = rowsms.createCell(10);
	 cell1sms10.setCellValue("短信内容"); 
	 cell1sms10.setCellStyle(cellStyleTitle);
}

public void createXSSFSmsRowContent(XSSFRow rowsmsnum,ZydcRecord mrecord, XSSFCellStyle cellStyleTitle)
{
	 XSSFCell cell0 = rowsmsnum.createCell(0);
	 cell0.setCellValue(mrecord.getYesterday());
	 cell0.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell1 = rowsmsnum.createCell(1);
	 cell1.setCellValue("");
	 cell1.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell2 = rowsmsnum.createCell(2);
	 cell2.setCellValue("");
	 cell2.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell3 = rowsmsnum.createCell(3);
	 cell3.setCellValue(mrecord.getGroupname());
	 cell3.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell4 = rowsmsnum.createCell(4);
	 cell4.setCellValue(mrecord.getUsername());
	 cell4.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell5 = rowsmsnum.createCell(5);
	 cell5.setCellValue(mrecord.getMsisdn());
	 cell5.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell6 = rowsmsnum.createCell(6);
	 cell6.setCellValue(mrecord.getSsmnnumber());
	 cell6.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell7 = rowsmsnum.createCell(7);
	 cell7.setCellValue(mrecord.getChannelname());
	 cell7.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell8 = rowsmsnum.createCell(8);
	 cell8.setCellValue(mrecord.getClientnumber());
	 cell8.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell9 = rowsmsnum.createCell(9);
	 cell9.setCellValue(mrecord.getRcvmsgtime());
	 cell9.setCellStyle(cellStyleTitle);
	 
	 XSSFCell cell10 = rowsmsnum.createCell(10);
	 cell10.setCellValue(mrecord.getMsgcontent());
	 cell10.setCellStyle(cellStyleTitle);
}

public List<String> refreshFileList(String strPath,List<SsmnZyUser> ssmnnumberzyuser) {
	if(ssmnnumberzyuser.size() <=0)
		return null;
	
    File dir = new File(strPath);
    File[] files = dir.listFiles();
    List<String> sfilenames = new ArrayList<String>();
   
    if (files == null)
        	return null;
    for (int i = 0; i < files.length; i++) {
        if (files[i].isDirectory()) {
            refreshFileList(files[i].getAbsolutePath(),ssmnnumberzyuser);
        } else {
            String strFileName = files[i].getAbsolutePath();
            //System.out.println("---"+strFileName);
            //取出文件名中的副号码
            strFileName = strFileName.replace('\\', '/');
            int indexFile = strFileName.lastIndexOf("/");
            if(indexFile >0)
            {
            	String sfilename = strFileName.substring(indexFile+1);
            	//获取副号码
            	if(sfilename.length() >0)
            	{
            		int ssmnindex = sfilename.indexOf("_");
            		if(ssmnindex >0)
            		{
            			String sssmnnumber = sfilename.substring(0,ssmnindex);
            			for(int j = 0;j<ssmnnumberzyuser.size(); j++)
            			{
            				SsmnZyUser zyu = ssmnnumberzyuser.get(j);
            				//System.out.print(zyu.getSsmnnumber()+"       "+sssmnnumber+"\n");
            				if(!"".equals(zyu.getSsmnnumber()) && zyu.getSsmnnumber().equals(sssmnnumber))
            				{
            					sfilenames.add(strFileName);
            				}
            			}
            		}
            	}
            }
           // filelist.add(files[i].getAbsolutePath());                   
        }
    }
    return sfilenames;
}

public void createZipfile(String zipfilePath,List<String> filenames )
{
	 byte[] buffer = new byte[1024];   
	  
    
     ZipOutputStream out = null;
     FileInputStream fis = null;
	try {
			out = new ZipOutputStream(new FileOutputStream(zipfilePath));
	
			 try {
			     for(int i=0;i<filenames.size();i++)
			     {   
			
			    	 File file = new File(filenames.get(i));
			         fis = new FileInputStream(file);  
			         String onefilename = filenames.get(i);
			         onefilename = onefilename.replace('\\', '/');
			         int indexonefile = onefilename.lastIndexOf("/");
			         String sfilename= "";
			         if(indexonefile>0)
			         {
			        	 sfilename = onefilename.substring(indexonefile+1);
			         }
			         if(sfilename.length()>0)
			        	 out.putNextEntry(new ZipEntry(sfilename));
			
			         int len;   
			
			         //读入需要下载的文件的内容，打包到zip文件  
			         while((len = fis.read(buffer))>0) 
			         {   
						out.write(buffer,0,len);    
			         }
					
			         out.closeEntry();   
			         fis.close();   
			
			     }
			     
			     out.close();  
			      
			} catch (IOException e) 
			{
				 	e.printStackTrace();
						try {
							if(out != null)
								out.close();
							if(fis != null)
								fis.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
			} 

      
	} catch (FileNotFoundException e) {
			e.printStackTrace();
	}   

//      System.out.println("生成Demo.zip成功");   
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


public void createCdrRowTitle2007( Row rowinput1, CellStyle cellStyleTitle,SsmnZyLevel level)
{	
	Cell cell0row0 = rowinput1.createCell(0);
	cell0row0.setCellType(XSSFCell.CELL_TYPE_STRING);
    cell0row0.setCellValue("序号");
    cell0row0.setCellStyle(cellStyleTitle);
    
    String businName = new UtilFunctionDao().getLevelName(1, level);//事业部
    Cell cell1row0 = rowinput1.createCell(1);
    cell1row0.setCellType(XSSFCell.CELL_TYPE_STRING);
    cell1row0.setCellValue(businName);
    cell1row0.setCellStyle(cellStyleTitle);
    
    String warName = new UtilFunctionDao().getLevelName(2, level);//战区
    Cell cell2row0 = rowinput1.createCell(2);
    cell2row0.setCellType(XSSFCell.CELL_TYPE_STRING);
    cell2row0.setCellValue(warName);
    cell2row0.setCellStyle(cellStyleTitle);
    
    String areaName = new UtilFunctionDao().getLevelName(3, level);//片区
    Cell cell3row0 = rowinput1.createCell(3);
    cell3row0.setCellType(XSSFCell.CELL_TYPE_STRING);
    cell3row0.setCellValue(areaName);
    cell3row0.setCellStyle(cellStyleTitle);
    
    String branName = new UtilFunctionDao().getLevelName(4, level);//行动组
    Cell cell4row0 = rowinput1.createCell(4);
    cell4row0.setCellType(XSSFCell.CELL_TYPE_STRING);
    cell4row0.setCellValue(branName);
    cell4row0.setCellStyle(cellStyleTitle);
    
    Cell cell5row0 = rowinput1.createCell(5);
    cell5row0.setCellType(XSSFCell.CELL_TYPE_STRING);
    cell5row0.setCellValue("经纪人姓名");
    cell5row0.setCellStyle(cellStyleTitle);
    
    Cell cell6row0 = rowinput1.createCell(6);
    cell6row0.setCellType(XSSFCell.CELL_TYPE_STRING);
    cell6row0.setCellValue("员工编号");
    cell6row0.setCellStyle(cellStyleTitle);
    
    Cell cell7row0 = rowinput1.createCell(7);
    cell7row0.setCellType(XSSFCell.CELL_TYPE_STRING);
    cell7row0.setCellValue("主号码");
    cell7row0.setCellStyle(cellStyleTitle);
    
    Cell cell8row0 = rowinput1.createCell(8);
    cell8row0.setCellType(XSSFCell.CELL_TYPE_STRING);
    cell8row0.setCellValue("副号码");
    cell8row0.setCellStyle(cellStyleTitle);
    
    if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))
    {
    	Cell cell9row0 = rowinput1.createCell(9);
	    cell9row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell9row0.setCellValue("第一联系人");
	    cell9row0.setCellStyle(cellStyleTitle);
	    
    	Cell cell10row0 = rowinput1.createCell(10);
	    cell10row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell10row0.setCellValue("客户电话");
	    cell10row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell11row0 = rowinput1.createCell(11);
	    cell11row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell11row0.setCellValue("渠道");
	    cell11row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell12row0 = rowinput1.createCell(12);
	    cell12row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell12row0.setCellValue("通话类型");
	    cell12row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell13row0 = rowinput1.createCell(13);
	    cell13row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell13row0.setCellValue("通话时间");
	    cell13row0.setCellStyle(cellStyleTitle);
	        
	    Cell cell14row0 = rowinput1.createCell(14);
	    cell14row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell14row0.setCellValue("通话时长");
	    cell14row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell15row0 = rowinput1.createCell(15);
	    cell15row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell15row0.setCellValue("录音文件名称");
	    cell15row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell16row0 = rowinput1.createCell(16);
	    cell16row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell16row0.setCellValue("是否标记为骚扰电话");
	    cell16row0.setCellStyle(cellStyleTitle);
	    
	    if(ConfigMgr.getAddRemark().equals("1"))
	    {
	    	Cell cell17row0 = rowinput1.createCell(17);
		    cell17row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell17row0.setCellValue("备注");
		    cell17row0.setCellStyle(cellStyleTitle);
	    }
    }else
    {
	    Cell cell9row0 = rowinput1.createCell(9);
	    cell9row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell9row0.setCellValue("客户电话");
	    cell9row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell10row0 = rowinput1.createCell(10);
	    cell10row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell10row0.setCellValue("渠道");
	    cell10row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell11row0 = rowinput1.createCell(11);
	    cell11row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell11row0.setCellValue("通话类型");
	    cell11row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell12row0 = rowinput1.createCell(12);
	    cell12row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell12row0.setCellValue("通话时间");
	    cell12row0.setCellStyle(cellStyleTitle);
	        
	    Cell cell13row0 = rowinput1.createCell(13);
	    cell13row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell13row0.setCellValue("通话时长");
	    cell13row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell14row0 = rowinput1.createCell(14);
	    cell14row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell14row0.setCellValue("录音文件名称");
	    cell14row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell15row0 = rowinput1.createCell(15);
	    cell15row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell15row0.setCellValue("是否标记为骚扰电话");
	    cell15row0.setCellStyle(cellStyleTitle);
	    
	    if(ConfigMgr.getAddRemark().equals("1"))
	    {
	    	Cell cell16row0 = rowinput1.createCell(16);
		    cell16row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell16row0.setCellValue("备注");
		    cell16row0.setCellStyle(cellStyleTitle);
	    }
    }
}

public void createRowContent2007(Row rowcdrnum,ZydcRecord zrecord,CellStyle cellStylecontent)
{
	 Cell cell0 = rowcdrnum.createCell(0);
	 cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell0.setCellValue(zrecord.getStreamNumber()+"");
	 cell0.setCellStyle(cellStylecontent);
	 
	 Cell cell1 = rowcdrnum.createCell(1);
	 cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1.setCellValue(zrecord.getBusinessdepartment());
	 cell1.setCellStyle(cellStylecontent);
	 
	 Cell cell2 = rowcdrnum.createCell(2);
	 cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell2.setCellValue(zrecord.getWarzone());
	 cell2.setCellStyle(cellStylecontent);
	 
	 Cell cell3 = rowcdrnum.createCell(3);
	 cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell3.setCellValue(zrecord.getArea());
	 cell3.setCellStyle(cellStylecontent);
	 
	 Cell cell4 = rowcdrnum.createCell(4);
	 cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell4.setCellValue(zrecord.getBranchactiongroup());
	 cell4.setCellStyle(cellStylecontent);
	 
	 Cell cell5 = rowcdrnum.createCell(5);
	 cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell5.setCellValue(zrecord.getUsername());
	 cell5.setCellStyle(cellStylecontent);
	 
	 Cell cell6 = rowcdrnum.createCell(6);
	 cell6.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell6.setCellValue(zrecord.getEmpno());
	 cell6.setCellStyle(cellStylecontent);
	 
	 Cell cell7 = rowcdrnum.createCell(7);
	 cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell7.setCellValue(zrecord.getMsisdn());
	 cell7.setCellStyle(cellStylecontent);
	 
	 Cell cell8 = rowcdrnum.createCell(8);
	 cell8.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell8.setCellValue(zrecord.getSsmnnumber());
	 cell8.setCellStyle(cellStylecontent);
	 
	 if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))
	 {
		 Cell cell9 = rowcdrnum.createCell(9);
		 cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell9.setCellValue(zrecord.getFirstMsisdn());
		 cell9.setCellStyle(cellStylecontent);
		 
		 Cell cell10 = rowcdrnum.createCell(10);
		 cell10.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell10.setCellValue(zrecord.getClientnumber());
		 cell10.setCellStyle(cellStylecontent);
		 
		 Cell cell11 = rowcdrnum.createCell(11);
		 cell11.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell11.setCellValue(zrecord.getChannelname());
		 cell11.setCellStyle(cellStylecontent);
		 
		 Cell cell12 = rowcdrnum.createCell(12);
		 cell12.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell12.setCellValue(zrecord.getCalltype());
		 cell12.setCellStyle(cellStylecontent);
		 
		 Cell cell13 = rowcdrnum.createCell(13);
		 cell13.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell13.setCellStyle(cellStylecontent); 
		 cell13.setCellValue(zrecord.getCallstarttime());
		 
		 Cell cell14 = rowcdrnum.createCell(14);
		 cell14.setCellType(XSSFCell.CELL_TYPE_STRING);
	     cell14.setCellValue(zrecord.getStrCallDuration());
	     cell14.setCellStyle(cellStylecontent);
		 
		 Cell cell15 = rowcdrnum.createCell(15);
		 cell15.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell15.setCellValue(zrecord.getFileName());
		 cell15.setCellStyle(cellStylecontent);
		 
		 Cell cell16 = rowcdrnum.createCell(16);
		 cell16.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell16.setCellValue(zrecord.getIsblacknum());
		 cell16.setCellStyle(cellStylecontent);
		 
		 if(ConfigMgr.getAddRemark().equals("1"))
		 {
			 Cell cell17 = rowcdrnum.createCell(17);
			 cell17.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell17.setCellValue(zrecord.getRemark());
			 cell17.setCellStyle(cellStylecontent); 
		 }
	 }else
	 {
		 Cell cell9 = rowcdrnum.createCell(9);
		 cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell9.setCellValue(zrecord.getClientnumber());
		 cell9.setCellStyle(cellStylecontent);
		 
		 Cell cell10 = rowcdrnum.createCell(10);
		 cell10.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell10.setCellValue(zrecord.getChannelname());
		 cell10.setCellStyle(cellStylecontent);
		 
		 Cell cell11 = rowcdrnum.createCell(11);
		 cell11.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell11.setCellValue(zrecord.getCalltype());
		 cell11.setCellStyle(cellStylecontent);
		 
		 Cell cell12 = rowcdrnum.createCell(12);
		 cell12.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell12.setCellStyle(cellStylecontent); 
		 cell12.setCellValue(zrecord.getCallstarttime());
		 
		 Cell cell13 = rowcdrnum.createCell(13);
		 cell13.setCellType(XSSFCell.CELL_TYPE_STRING);
	     cell13.setCellValue(zrecord.getStrCallDuration());
	     cell13.setCellStyle(cellStylecontent);
		 
		 Cell cell14 = rowcdrnum.createCell(14);
		 cell14.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell14.setCellValue(zrecord.getFileName());
		 cell14.setCellStyle(cellStylecontent);
		 
		 Cell cell15 = rowcdrnum.createCell(15);
		 cell15.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell15.setCellValue(zrecord.getIsblacknum());
		 cell15.setCellStyle(cellStylecontent);
		 
		 if(ConfigMgr.getAddRemark().equals("1"))
		 {
			 Cell cell16 = rowcdrnum.createCell(16);
			 cell16.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell16.setCellValue(zrecord.getRemark());
			 cell16.setCellStyle(cellStylecontent);
		 }
	 }
}

public void createSmsRowTitle2007 (Row rowsms, CellStyle cellStyleTitle)
{
	Cell cell1sms0 = rowsms.createCell(0);
	cell1sms0.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1sms0.setCellValue("序号");
	 cell1sms0.setCellStyle(cellStyleTitle);
	 
	/*Cell cell1sms1 = rowsms.createCell(1);
	cell1sms1.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1sms1.setCellValue("接收日期");
	 cell1sms1.setCellStyle(cellStyleTitle);*/
	 
	 Cell cell1sms1 = rowsms.createCell(1);
	 cell1sms1.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1sms1.setCellValue("接收时间");
	 cell1sms1.setCellStyle(cellStyleTitle);
	 
	 Cell cell1sms2 = rowsms.createCell(2);
	 cell1sms2.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1sms2.setCellValue("事业部");
	 cell1sms2.setCellStyle(cellStyleTitle);
	 
	 Cell cell1sms3 = rowsms.createCell(3);
	 cell1sms3.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1sms3.setCellValue("战区");
	 cell1sms3.setCellStyle(cellStyleTitle);
 
	 Cell cell1sms4 = rowsms.createCell(4);
	 cell1sms4.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1sms4.setCellValue("片区");
	 cell1sms4.setCellStyle(cellStyleTitle);

	 Cell cell1sms5 = rowsms.createCell(5);
	 cell1sms5.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1sms5.setCellValue("分行组别");
	 cell1sms5.setCellStyle(cellStyleTitle);
	 
	 Cell cell1sms6 = rowsms.createCell(6);
	 cell1sms6.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1sms6.setCellValue("经纪人姓名");
	 cell1sms6.setCellStyle(cellStyleTitle);
	 
	 Cell cell1sms7 = rowsms.createCell(7);
	 cell1sms7.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1sms7.setCellValue("员工编号");
	 cell1sms7.setCellStyle(cellStyleTitle);
	 
	 Cell cell1sms8 = rowsms.createCell(8);
	 cell1sms8.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1sms8.setCellValue("主号码");
	 cell1sms8.setCellStyle(cellStyleTitle);
	 
	 Cell cell1sms9 = rowsms.createCell(9);
	 cell1sms9.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1sms9.setCellValue("副号码");
	 cell1sms9.setCellStyle(cellStyleTitle);
	 
	 Cell cell1sms10 = rowsms.createCell(10);
	 cell1sms10.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1sms10.setCellValue("客户电话");
	 cell1sms10.setCellStyle(cellStyleTitle);
	 
	 Cell cell1sms11 = rowsms.createCell(11);
	 cell1sms11.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1sms11.setCellValue("渠道"); 
	 cell1sms11.setCellStyle(cellStyleTitle);
	 
	 Cell cell1sms12 = rowsms.createCell(12);
	 cell1sms12.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1sms12.setCellValue("短信内容"); 
	 cell1sms12.setCellStyle(cellStyleTitle);
}

public void createSmsRowContent2007(Row rowsmsnum,ZydcRecord mrecord, CellStyle cellStyleTitle)
{
	 Cell cell0 = rowsmsnum.createCell(0);
	 cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell0.setCellValue(mrecord.getStreamNumber()+"");
	 cell0.setCellStyle(cellStyleTitle);
	 
	 Cell cell1 = rowsmsnum.createCell(1);
	 cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell1.setCellValue(mrecord.getRcvmsgtime());
	 cell1.setCellStyle(cellStyleTitle);
	 
	 Cell cell2 = rowsmsnum.createCell(2);
	 cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell2.setCellValue(mrecord.getBusinessdepartment());
	 cell2.setCellStyle(cellStyleTitle);
	 
	 Cell cell3 = rowsmsnum.createCell(3);
	 cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell3.setCellValue(mrecord.getWarzone());
	 cell3.setCellStyle(cellStyleTitle);
	 
	 Cell cell4 = rowsmsnum.createCell(4);
	 cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell4.setCellValue(mrecord.getArea());
	 cell4.setCellStyle(cellStyleTitle);
	 
	 Cell cell5 = rowsmsnum.createCell(5);
	 cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell5.setCellValue(mrecord.getBranchactiongroup());
	 cell5.setCellStyle(cellStyleTitle);
	 
	 Cell cell6 = rowsmsnum.createCell(6);
	 cell6.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell6.setCellValue(mrecord.getUsername());
	 cell6.setCellStyle(cellStyleTitle);
	 
	 Cell cell7 = rowsmsnum.createCell(7);
	 cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell7.setCellValue(mrecord.getEmpno());
	 cell7.setCellStyle(cellStyleTitle);
	 
	 Cell cell8 = rowsmsnum.createCell(8);
	 cell8.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell8.setCellValue(mrecord.getMsisdn());
	 cell8.setCellStyle(cellStyleTitle);
	 
	 Cell cell9 = rowsmsnum.createCell(9);
	 cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell9.setCellValue(mrecord.getSsmnnumber());
	 cell9.setCellStyle(cellStyleTitle);
	 
	 Cell cell10 = rowsmsnum.createCell(10);
	 cell10.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell10.setCellValue(mrecord.getClientnumber());
	 cell10.setCellStyle(cellStyleTitle);
	 
	 Cell cell11 = rowsmsnum.createCell(11);
	 cell11.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell11.setCellValue(mrecord.getChannelname());
	 cell11.setCellStyle(cellStyleTitle);
	 
	 Cell cell12 = rowsmsnum.createCell(12);
	 cell12.setCellType(XSSFCell.CELL_TYPE_STRING);
	 cell12.setCellValue(mrecord.getMsgcontent());
	 cell12.setCellStyle(cellStyleTitle);
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

//获取省份
/*if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) && (level.getCompany() == null || "".equals(level.getCompany())))//省级
{ 
	execlSavePath +=level.getProvincecity();
} 
if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) && level.getCompany() != null && !"".equals(level.getCompany()) && 
		(level.getBusinessdepartment() == null || "".equals(level.getBusinessdepartment())))//公司级
{
	execlSavePath +=level.getCompany();
}
if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) 
		&& level.getCompany() != null && !"".equals(level.getCompany()) && 
		level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment())
		&& (level.getWarzone() == null || "".equals(level.getWarzone())))//事业部级
{
	execlSavePath +=level.getBusinessdepartment();
}
if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) 
		&& level.getCompany() != null && !"".equals(level.getCompany()) && 
		level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment())
		&& level.getWarzone() != null && !"".equals(level.getWarzone())
		&& (level.getArea() == null || "".equals(level.getArea())))//战区级
{
	execlSavePath +=level.getWarzone();
}
if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) 
		&& level.getCompany() != null && !"".equals(level.getCompany()) && 
		level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment())
		&& level.getWarzone() != null && !"".equals(level.getWarzone()) && 
		level.getArea() != null && !"".equals(level.getArea()) 
		&& (level.getBranchactiongroup() == null || "".equals(level.getBranchactiongroup())))//片区级
{
	execlSavePath +=level.getArea();
}
if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) 
		&& level.getCompany() != null && !"".equals(level.getCompany()) && 
		level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment())
		&& level.getWarzone() != null && !"".equals(level.getWarzone()) && 
		level.getArea() != null && !"".equals(level.getArea())
		&& level.getBranchactiongroup() != null && !"".equals(level.getBranchactiongroup()))//行动组级 ...写的好恶心...
{
	execlSavePath +=level.getBranchactiongroup();
}*/


public static void main(String[] args)
{
	/*String yesterdaysource = new TimingJob().getYesterdayStr(Constants.DATE_FORMATE2); 
	String yesterday = new TimingJob().getYesterdayStr(Constants.DATE_FORMATE);
	List<SsmnZyLevel>  levelList = new ArrayList<SsmnZyLevel>();
	SsmnZyLevel ls = new SsmnZyLevel();
	ls.setId(1000000146L);
	ls.setProvincecity("天津");
	ls.setCompany("中原");
	levelList.add(ls);*/
	
	//new TimingJob().getCallStatisticDatas(levelList,"D:\\TEST",yesterdaysource,yesterday,"1");
	//String[] spl=null;
	
	
	/*DateFormat df = new SimpleDateFormat("yyyy-MM-dd");/*** 加一天*/
	/*Calendar calendar = Calendar.getInstance();
	calendar.setTime(new Date());
	calendar.add(Calendar.DAY_OF_MONTH, -1);
	calendar.add(Calendar.DAY_OF_WEEK, -6);*/
	//System.out.println(" " + df.format(calendar.getTime()));
		
	//int perIndex = 61%60;
	//System.out.print(perIndex+"");
		try {
			new TimingJob().execute(null);
		} catch (JobExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



}
