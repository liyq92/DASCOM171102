package com.dascom.init;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigMgr {
	
	private static Logger logger = Logger.getLogger(ConfigMgr.class.getName());
	private static String servicename;
	private static String groupname;
	private static String matchNumberLevel;
	private static String cdrurl;
	private static String recordUserChange;//是否记录用户变化信息（作用,同步到其他数据库通过接口）
	private static String isExcel2003;//1: 小于65536条的生成xls格式
	private static String channelNames;//该渠道的用户，将ssmn_zy_user表中的ssmnnumber_type值设置成1
	private static String dc_calltypes;//1:业主呼入|2:客户呼入|3:PC呼出|4:APP呼出|5:直接呼出
	private static Map<String,String> dcCalltypeMap=null;
	private static String isDoubleCall;//是否是双呼业务
	private static Map<String,List<String>> levelShowMap = null;//存放每个公司的地区级别显示
	private static String levelShowStr ;//存放地区级别的串
	private static String cdrurlfy;//飞语录音地址
	private static String isAddSecondMsisdn;//是否添加第二联系人
	private static String isHaveSsmnNum;//是否分配副号码
	private static String timingTask_provincecity_str;//配置定时任务生成报表的省市，以“|”分隔
	private static List<String> timingTask_provincecity =null;//timingTask_provincecity_str分割
	private static String addRemark;//语音查询页面，是否添加备注 0不添加，1：添加
	private static String distChannel;//是否(登录详情里)听录音次数，是否 区分 A+和渠道
	private static String callurl;//呼叫管理里面的接口地址
	private static String time_job_showclientnum;//#定时任务生成的所有的报表强制隐藏客户电话开关（只控制定时任务中的通话记录）
	
	public static String getSrvicename() {
		return servicename;
	}
	
	public static String getGroupname() {
		return groupname;
	}
	
	public static String getMatchNumberLevel()
	{
		return matchNumberLevel;
	}
	
	public static String getCdrurl()
	{
		return cdrurl;
	}
	
	public static String getRecordUserChange()
	{
		return recordUserChange;
	}
	
	public static String getIsExcel2003()
	{
		return isExcel2003;
	}
	
	public static String getChannelNames()
	{
		return channelNames;
	}
	
	public static String getDc_calltypes()
	{
		return dc_calltypes;
	}
	public static Map<String,String> getDcCalltypeMap()
	{
		return dcCalltypeMap;
	}
	
	public static String getIsDoubleCall()
	{
		return isDoubleCall;
	}
	
	public static  Map<String,List<String>> getLevelShow()
	{
		return levelShowMap;
	}
	
	public static String getCdrurlfy()
	{
		return cdrurlfy;
	}
	
	public static String getIsAddSecondMsisdn()
	{
		return isAddSecondMsisdn;
	}
	
	public static String getIsHaveSsmnNum()
	{
		return isHaveSsmnNum;
	}
	
	public static List<String> getTimingTask_provincecity()
	{
		return timingTask_provincecity;
	}
	
	public static String getAddRemark()
	{
		return addRemark;
	}
	public static String getDistChannel()
	{
		return distChannel;
	}
	public static String getCallurl()
	{
		return callurl;
	}
	public static String getTime_job_showclientnum()
	{
		return time_job_showclientnum;
	}
	
	
	public static void config_params()
	{		
		Properties prop = new Properties();
		
		try 
		{
			try {
				prop.load(ConfigMgr.class.getResourceAsStream("/servicename.prop"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			servicename	= new String(prop.getProperty("servicename"));
			groupname = new String(prop.getProperty("groupname"));
			matchNumberLevel = new String(prop.getProperty("matchNumberLevel"));
			cdrurl = new String(prop.getProperty("cdrurl"));
			recordUserChange = new String(prop.getProperty("recordUserChange"));
			isExcel2003 = new String(prop.getProperty("isExcel2003"));
			channelNames = new String(prop.getProperty("channelNames"));
			dc_calltypes = new String(prop.getProperty("dc_calltypes"));
			isDoubleCall = new String(prop.getProperty("isDoubleCall"));
			levelShowStr = new String(prop.getProperty("levelShow"));
			cdrurlfy = new String(prop.getProperty("cdrurlfy"));
			isAddSecondMsisdn = new String(prop.getProperty("isAddSecondMsisdn"));
			isHaveSsmnNum = new String(prop.getProperty("isHaveSsmnNum"));
			timingTask_provincecity_str =new String(prop.getProperty("timingTask_provincecity"));
			addRemark =new String(prop.getProperty("addRemark"));
			distChannel =new String(prop.getProperty("distChannel"));
			callurl =new String(prop.getProperty("callurl"));
			time_job_showclientnum =new String(prop.getProperty("time_job_showclientnum"));
							
			if(channelNames !=null && !"".equals(channelNames))
			{
				try {
					channelNames = new String(channelNames.getBytes("ISO-8859-1"), "gbk");
				} catch (UnsupportedEncodingException e) {
					logger.error("## Configuration Loading Error ##");
					e.printStackTrace();
				}
			}
			
			if(dc_calltypes !=null && !"".equals(dc_calltypes))
			{
				try {
					dc_calltypes = new String(dc_calltypes.getBytes("ISO-8859-1"), "gbk");
					//解析到map中 
					//1:业主呼入|2:客户呼入|3:PC呼出|4:APP呼出|5:直接呼出
					dcCalltypeMap = new HashMap<String,String>();
					String[] sctypes = dc_calltypes.split("\\|");
					if(sctypes !=null && sctypes.length>0)
					{
						for(int i=0;i<sctypes.length;i++)
						{
							String stemp = sctypes[i];
							if(stemp !=null && !"".equals(stemp) && stemp.length()>0)
							{
								String[] skv = stemp.split(":");
								if(skv !=null && skv.length==2)
								{
									dcCalltypeMap.put(skv[0], skv[1]);
								}
							}
						}
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if(levelShowStr !=null && !"".equals(levelShowStr))
			{
				levelShowStr = new String(levelShowStr.getBytes("ISO-8859-1"), "gbk");
				//解析到map中
				levelShowMap = new HashMap<String,List<String>>();
				String[] splitStr = levelShowStr.split("\\|");
				if(splitStr !=null && splitStr.length>0)
				{
					for(int i=0;i<splitStr.length;i++)
					{
						String temp = splitStr[i];
						if(temp !=null && temp.length()>0)
						{
							String[] levelName = temp.split(",");
							if(levelName !=null && levelName.length>0 && levelName.length == 6)//6个是全的，才给处理 ,否则不处理
							{
								List<String> lt = new ArrayList<String>();
								lt.add(levelName[2]);
								lt.add(levelName[3]);
								lt.add(levelName[4]);
								lt.add(levelName[5]);
								levelShowMap.put(levelName[0]+levelName[1], lt);
							}
						}
					
					}
				}
			}
			if(timingTask_provincecity_str !=null && !"".equals(timingTask_provincecity_str))
			{
				timingTask_provincecity_str = new String(timingTask_provincecity_str.getBytes("ISO-8859-1"),"gbk");
				timingTask_provincecity = new ArrayList<String>();
				String[] splitStr = timingTask_provincecity_str.split("\\|");
				if(splitStr !=null && splitStr.length>0)
				{
					for(int i=0;i<splitStr.length;i++)
					{
						String temp = splitStr[i];
						if(temp !=null && temp.length()>0)
						{
							timingTask_provincecity.add(temp);
						}
					}
				}
			}
			
		} 
		
		catch (Exception e) 
		{
			//logger.error("## Configuration Loading Error ##");
			//e.printStackTrace();//有的配置不需要配置，这里不当错误处理
		}
	}
}
