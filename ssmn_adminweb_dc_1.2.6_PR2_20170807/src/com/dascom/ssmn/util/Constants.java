package com.dascom.ssmn.util;

import java.io.IOException;
import java.util.Properties;


public class Constants {
	
    ////////////////////////---------  系统管理参数配置 ---------/////////////////////////////
	/** 该系统的业务键 */
	public static final String servicekey_dc = "50";  
	
	/** 下载文件的日期格式 */
	public static final String DATE_FORMATE = "yyyy-MM-dd";  
	
	/** 下载文件的日期格式 */
	public static final String DATE_FORMATE2 = "yyyyMMdd"; 
	
	/** 可供下载文件的天数 */
	public static Integer DOWNLOAD_INTERVAL ;  
	
	/** 可供下载前一天文件的时间 */
	public static Integer DOWNLOAD_HOUR = 1;  
	
	/**  下载的统计表格名称 换名字后，以前的名字也要读出来*/
	public static String DOWNLOAD_EXECL_NAME2 = "号盾（地产版）语音统计";  
	public static String DOWNLOAD_EXECL_NAME1 = "语音统计";  
	
	/**  统计报表存放文件夹名称*/
	public static String EXECL_FOLDER_NAME = "statexecl";  

	/**  通话录音zip包存放文件夹名称*/
	public static String ZIP_FOLDER_NAME = "zipdownload"; 
	
	public static String RECORD_ZIP_FOLDER_NAME = "recordFiles";
	
	/** 下载的文件名称 换名字后，以前的名字也要读出来*/
	public static String DOWNLOAD_FILENAME2 = "号盾（地产版）通话录音";  
	public static String DOWNLOAD_FILENAME1 = "通话录音";  
	
	/** 来电统计文件名 */
	public static String DOWNLOAD_CALL_STATISTIC_FILENAME = "callstatistics";
	/** 来电统计文件下面的 昨日 */
	public static String DOWNLOAD_CALL_STATISTIC_YESTERDAY_FILENAME = "yesterday";
	/** 来电统计文件下面的 上周 */
	public static String DOWNLOAD_CALL_STATISTIC_LASTWEEK_FILENAME = "lastweek";
	/** 来电统计文件下面的 上个月 */
	public static String DOWNLOAD_CALL_STATISTIC_LASTMONTH_FILENAME = "lastmonth";
	
	
	////////////////////////---------  系统管理begin ---------/////////////////////////////
	public static final String MESSAGE_ADD_OPERATOR_CUSSE = "添加操作员成功";
	public static final String MESSAGE_MOD_OPERATOR_CUSSE = "修改成功";
	public static final String MESSAGE_DEL_OPERATOR_CUSSE = "删除操作员成功";
	public static final String MESSAGE_ADD_GROUP_CUSSE = "添加群组成功";
	public static final String MESSAGE_MOD_GROUP_CUSSE = "修改群组成功";
	public static final String MESSAGE_DEL_GROUP_CUSSE = "删除群组成功";
	public static final String MESSAGE_OPERATOR_EXIST = "操作员已经存在";
	
	public static final String MESSAGE_GROUP_EXIST = "组已经存在";
	public static final String MESSAGE_DIFF_PASSWORD = "密码不一致";
	public static final String MESSAGE_OPERATOR_NOT_EXSIT = "操作员不存在";
	public static final String MESSAGE_WRONG_PASSWORD = "密码错误";	
	public static final String MESSAGE_DATABASE_ERROR = "数据库错误";
	public static final String MESSAGE_EXISTOTHERNUMBER = "该主号已存在一个A+副号码,不能再绑定A+副号码!";
		
	public static final String OPERATOR_IN_SESSION = "loginOperator";
	public static final String SERVICEKEY_IN_SESSION = "loginService";
	public static final String AUTHOR_IN_SESSION = "authors";
	
	public static final String LOG_TYPE_OPERATOR_LOGIN="操作员登陆";
	public static final String LOG_TYPE_OPERATOR_LOGOUT="操作员退出";
	public static final String LOG_TYPE_OPERATOR_CDR="听录音次数";
	public static final String LOG_TYPE_OPERATOR_REMARK="备注次数";
	public static final String LOG_TYPE_SEARCH_OPERATOR="查询操作员";
	public static final String LOG_TYPE_ADD_OPERATOR="增加操作员";
	public static final String LOG_TYPE_MOD_OPERATOR="修改操作员";
	public static final String LOG_TYPE_DEL_OPERATOR="删除操作员";
	public static final String LOG_TYPE_SEARCH_GROUP="查询群组(系统管理)";
	public static final String LOG_TYPE_ADD_GROUP="增加群组(系统管理)";
	public static final String LOG_TYPE_MOD_GROUP="修改群组(系统管理)";
	public static final String LOG_TYPE_DEL_GROUP="删除群组(系统管理)";
	public static final String LOG_TYPE_SCH_LOG="日志查询";
	public static final String LOG_TYPE_SCH_MYLOG="我的日志查询";
	public static final String LOG_TYPE_EXP_OPE_INFO="日志信息导出";
	public static final String LOG_TYPE_EXP_MYOPE_INFO="我的日志信息导出";
	public static final String LOG_TYPE_unlock_OPERATOR="解冻用户";
		
	public static final String TEST1 = "\u6765\u8bdd";
	
	public static final int RECORD_PER_PAGE = 10;
	
	public static final String CONTENT_TYPE_TEXT_HTML = "text/html; charset=utf-8";

	public static String COMMON_AUTHS = "reginfoModify,mylogSearch,myexportFile,downSubFile,";
	////////////////////////---------  系统管理  end ---------/////////////////////////////
	public static final String password = "123456";
	/**  统计报表下载的路径*/
	public static String DOWNLOAD_EXECL_DIR = null;  
	/**  通话录音zip包下载的路径*/
	public static String DOWNLOAD_ZIP_DIR = null;
	/**  存放录音文件的路径*/
	public static String AUDIO_PATH = null;
	/** 来电统计 文件路径,里面包括(昨日：yesterday, 上周：lastWeek, 上个月lastMonth)*/
	public static String DOWNLOAD_CALL_STATISTIC_DIR = null;
	/**zip文件存放路径*/
	public static String ZIP_DIR=null;

	public static final int maxInt = 65500;//控制生成excel条数,如果小于此条数，并且开关开着，则生成xls格式
	
	/*根据渠道的type值，来判断副号码的类型*/
	public static int TYPE_ENABLENUMBER =0;
	
	public static int TYPE_SHOWDATE =2;//默认2：全部显示，0：显示渠道数据   ,  1：显示A+数据 ，(同ssmn_zy_channel表type同步)
	
	public static String SSMN_NUM_DEFAULT ="00000000000";
	
	//隐藏客户电话权限是否打开： 0：关闭，1：打开
	public static int SHOW_CLIENTNUM_OP =0; 
	
	public static String TYPE_SHOWA ="showAChannel";
	public static String TYPE_SHOWCHANNEL ="showChannel";
	
	static {
		Properties prop = new Properties();

		try {
			prop.load(Constants.class.getResourceAsStream("/cfg.properties"));
			DOWNLOAD_INTERVAL= Integer.parseInt(prop.getProperty("download_interval").trim());
			AUDIO_PATH = prop.getProperty("audiopath").trim();
			ZIP_DIR = prop.getProperty("zipdirpath").trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setExeclDir(String dir) {
		Constants.DOWNLOAD_EXECL_DIR = dir;
	}
	
	public static void setZipDir(String dir) {
		Constants.DOWNLOAD_ZIP_DIR = dir;
	}
	
	public static void setCallStatistic(String dir)
	{
		Constants.DOWNLOAD_CALL_STATISTIC_DIR = dir;
	}
}

