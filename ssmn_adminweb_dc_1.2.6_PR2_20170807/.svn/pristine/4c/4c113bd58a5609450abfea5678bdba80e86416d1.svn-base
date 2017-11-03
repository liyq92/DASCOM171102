package com.dascom.ssmn.action;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

import com.dascom.OPadmin.dao.IAdminAuthority;
import com.dascom.OPadmin.dao.impl.AdminAuthorityImpl;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.IOperatorService;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.OPadmin.service.impl.OperatorServImpl;
import com.dascom.ssmn.dao.BlackCallNumberDao;
import com.dascom.ssmn.dao.CDRDao;
import com.dascom.ssmn.dao.CallDao;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.EnableNumberDao;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.dao.zydcUserDao;
import com.dascom.ssmn.entity.Blackcallnumber;
import com.dascom.ssmn.entity.SsmnZYEnableNumberRecord;
import com.dascom.ssmn.entity.SsmnZyCdr;
import com.dascom.ssmn.entity.SsmnZyClientnum;
import com.dascom.ssmn.entity.SsmnZyEnablenumber;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.ssmn.entity.ZydcRecord;
import com.dascom.ssmn.util.Constants;
import com.dascom.ssmn.util.HttpTool;
import com.dascom.init.ConfigMgr;

public class SelectCDRSMSInfo extends HttpServlet{
	private static final Logger logger = Logger.getLogger(SelectCDRSMSInfo.class);
	
	private LevelDao leveldao = LevelDao.getInstance();
	private EnableNumberDao endao = EnableNumberDao.getInstance();
	private zydcUserDao uDao = zydcUserDao.getInstance();
	private CDRDao cDao = CDRDao.getInstance();
	private BlackCallNumberDao bDao = BlackCallNumberDao.getInstance();
	private IOperatorLogService logServ = new OperatorLogServImpl();
	private IOperatorService operatorServ = new OperatorServImpl();
	private CallDao callDao =CallDao.getInstance();
	private IAdminAuthority authDao = new AdminAuthorityImpl();
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response)
	throws ServletException, IOException{
		TyadminOperators opera = (TyadminOperators)request.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		List<String> authMethodList = authDao.findByGroupId(opera.getGroupId().longValue(),(String)request.getSession().getAttribute(Constants.SERVICEKEY_IN_SESSION));
		UtilFunctionDao.showType(authMethodList);
		try{
			response.setHeader("Cache-Control", "no-cashe");			
			response.setContentType("text/plain; charset=UTF-8");
			String startdate=request.getParameter("startdate");
			String starttime = request.getParameter("starttime");
			String enddate=request.getParameter("enddate");
			String endtime=request.getParameter("endtime");
			String msisdn = request.getParameter("msisdn");
			String streamnum = request.getParameter("streamnum");
			String sname = request.getParameter("name");
			String ssmnnum = request.getParameter("ssmnnum");
			String sclientnumber = request.getParameter("clientnumber");
			String stype = request.getParameter("type");
			String bdparam = request.getParameter("bdbox1");//事业部
			String zoneparam = request.getParameter("zonebox1");//战区
			String areaparam = request.getParameter("areabox1");//片区
			String bagparam = request.getParameter("bagbox1");//行动组
			String channelid = request.getParameter("channelbox");
			String strcalltype = request.getParameter("calltype");
			String sCallReMis = request.getParameter("callReMis");
			
			String xml=null;
			SimpleDateFormat fmt = new SimpleDateFormat("yyMMddHHmmss");
			SsmnZyLevel level = null;
			String smsfilename="" ;
			String senable = "";
			List<ZydcRecord> exportlist = null;

			level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());

			String scdrfilepath = request.getSession().getServletContext().getRealPath("exportrecordFiles");//导出语音
			
			File direx = new File(scdrfilepath);
			if (!direx.exists()) {
				if (!direx.mkdirs())
					System.out.println("创建文件失败");
			}
			
			if(stype != null && !stype.equals("") && stype.equals("1"))//录音生成报表
			{
				long startTimeLong =0;
				long endTimeLong = 0;
				//根据输入的日期,查出数值型的
				//获取开始时间
				if(startdate !=null && startdate.length()>0)
				{
					if(starttime !=null && starttime.length()>0)
						startTimeLong =cDao.getDateLong(startdate,starttime,0);
					else
						startTimeLong =cDao.getDateLong(startdate,"",0);
				}
				//获取结束时间,如果小时,小时+1/24, 如果天,天+1,减100(包括当天,当时的),如果结束时间为空,取当前日期
				if(enddate !=null && enddate.length()>0)
				{
					if(endtime !=null && endtime.length()>0)
						endTimeLong =cDao.getDateLong(enddate,endtime,1);
					else
						endTimeLong =cDao.getDateLong(enddate,"",1);
				}else
					endTimeLong =cDao.getDateLong("sysdate","",1);

				if(authMethodList !=null && authMethodList.contains("hideOwnerNumber"))
					Constants.SHOW_CLIENTNUM_OP =1;
				else
					Constants.SHOW_CLIENTNUM_OP =0;
				
				exportlist = cDao.getCdrStatRecordExportList(msisdn,sname,streamnum,startTimeLong,endTimeLong
						,sclientnumber,bdparam,zoneparam,areaparam,bagparam,channelid,
						ssmnnum,strcalltype,sCallReMis,opera.getLevelid(),Constants.TYPE_SHOWDATE);	
								
				try{
					if(new ConfigMgr().getIsExcel2003() !=null && !"".equals(new ConfigMgr().getIsExcel2003()) && new ConfigMgr().getIsExcel2003().equals("1")
							&& exportlist!=null && exportlist.size()<= new Constants().maxInt)
						smsfilename = "副号码语音查询"+"("+opera.getId().getOpeno()+")_"+fmt.format(new Date())+".xls";
					else
						smsfilename = "副号码语音查询"+"("+opera.getId().getOpeno()+")_"+fmt.format(new Date())+".xlsx";
					
					String sfilepath = scdrfilepath +File.separator+smsfilename;
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
							if(new ConfigMgr().getIsExcel2003() !=null && !"".equals(new ConfigMgr().getIsExcel2003()) && new ConfigMgr().getIsExcel2003().equals("1")
									&& exportlist!=null && exportlist.size()<= new Constants().maxInt)
							{
								/*----------------------------excel 2003实现方式---------------------------------*/
								     HSSFWorkbook wboutput = new HSSFWorkbook();
								     
								     //总的数据
								     HSSFSheet sheetoutputFirst = wboutput.createSheet(smsfilename);
								     HSSFRow rowtitle0 = sheetoutputFirst.createRow(0);
								     firstcount++;
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
								     
								     new UtilFunctionDao().createCdrRowTitle(0,rowtitle0, cellStyleFont,level);
								     for(int i=0;i<exportlist.size();i++){
										ZydcRecord info = exportlist.get(i);
										HSSFRow rowcdrnum = sheetoutputFirst.createRow(i+1);
										new UtilFunctionDao().createRowContent(i+1,rowcdrnum, info, cellStylecontent);
								     }
									wboutput.write(fileoutput);
									fileoutput.close();
								/*----------------------------------------------------------------------------------------*/
							}else
							{
								/*-------------------------excel 2007实现方式-------------------------------------------*/
								
								SXSSFWorkbook wboutput = new SXSSFWorkbook(10000);//内存中保留 10000 条数据，以免内存溢出，其余写入 硬盘
								
								//总的数据
							     Sheet sheetoutputFirst = wboutput.createSheet(smsfilename);
							     Row rowtitle0 = sheetoutputFirst.createRow(0);
							     firstcount++;
							   //内容样式
							     CellStyle cellStylecontent = wboutput.createCellStyle();
							     Font fontcontent = wboutput.createFont();
							     fontcontent.setFontHeightInPoints((short) 11);
							     cellStylecontent.setFont(fontcontent);
							     
							     CellStyle cellStyleFont = wboutput.createCellStyle();
								 Font font2 = wboutput.createFont();
							     font2.setFontName("宋体");
							     font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
							     font2.setFontHeightInPoints((short) 11);
							     
							     new UtilFunctionDao().createCdrRowTitle2007(0,rowtitle0, cellStyleFont,level);	
								for(int i=0;i<exportlist.size();i++){
									ZydcRecord info = exportlist.get(i);
									Row rowcdrnum = sheetoutputFirst.createRow(i+1);
									new UtilFunctionDao().createRowContent2007(i+1,rowcdrnum, info, cellStylecontent);
								}
								wboutput.write(fileoutput);
								fileoutput.close();
								
								/*-----------------------------------------------------------------------------*/
							}

						senable = smsfilename;//成功，把名称反回去
					} catch (IOException e) {
						e.printStackTrace();
						logger.error("----导出副号码语音查询数据发生异常-----",e);
						senable = "";
					}
					}

					String logMsg = opera.getId().getOpeno()+"导出了副号码语音查询数据！";
					senable = smsfilename;//成功，把名称反回去
					
				} catch (Exception e) {
					logger.error("----导出副号码语音查询数据发生异常-----",e);
					senable = "";
				}
			}

			if(stype != null && !stype.equals("") && stype.equals("3"))//经纪人信息生成报表
			{
				//参数
				String sssmnnum = request.getParameter("ssmnnum");//副号码
				String schannelbox = request.getParameter("channelbox");//渠道
				String sbdbox1 = request.getParameter("bdbox1");//事业部
				String szonebox1= request.getParameter("zonebox1");//战区
				String sareabox1 = request.getParameter("areabox1");//片区
				String sbagbox1 = request.getParameter("bagbox1");//行动组
				String sempno = request.getParameter("vempno");//员工编号
				
				List<SsmnZyUser> userExport = uDao.getZYDCUserExportList(msisdn,sname,sssmnnum,schannelbox,sbdbox1,
						szonebox1,sareabox1,sbagbox1,sempno, opera);

				try{
					if(new ConfigMgr().getIsExcel2003() !=null && !"".equals(new ConfigMgr().getIsExcel2003()) && new ConfigMgr().getIsExcel2003().equals("1")
							&& userExport!=null && userExport.size()<= new Constants().maxInt)
						smsfilename = "经纪人信息"+"("+opera.getId().getOpeno()+")_"+fmt.format(new Date())+".xls";
					else
						smsfilename = "经纪人信息"+"("+opera.getId().getOpeno()+")_"+fmt.format(new Date())+".xlsx";
					String sfilepath = scdrfilepath +File.separator+smsfilename;
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
							//如果　IsExcel2003　开关开启，并且行数小于 65535条数据，则生成xls格式
							if(new ConfigMgr().getIsExcel2003() !=null && !"".equals(new ConfigMgr().getIsExcel2003()) && new ConfigMgr().getIsExcel2003().equals("1")
									&& userExport!=null && userExport.size()<= new Constants().maxInt)
							{
								//生成xls格式
								/*------------------------------excel2003-------------------------------------*/
								     HSSFWorkbook wboutput = new HSSFWorkbook();
								     
								     //总的数据
								     HSSFSheet sheetoutputFirst = wboutput.createSheet(smsfilename);
								     HSSFRow rowtitle0 = sheetoutputFirst.createRow( 0);
								     firstcount++;
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
								     
								     new UtilFunctionDao().createSSMNTitle(0,rowtitle0, cellStyleFont,level);
								     
									for(int i=0;i<userExport.size();i++){
										SsmnZyUser info = userExport.get(i);
										HSSFRow rowcdrnum = sheetoutputFirst.createRow(i+1);
										new UtilFunctionDao().createSSMNRowContent(0,rowcdrnum, info, cellStylecontent);
									}
									wboutput.write(fileoutput);
									fileoutput.close();
								
							}else
							{
								/**--------------------------excel 2007--------------------------------------------------**/
								SXSSFWorkbook wboutput = new SXSSFWorkbook(10000);
							     
							     //总的数据
							     Sheet sheetoutputFirst = wboutput.createSheet(smsfilename);
							     Row rowtitle0 = sheetoutputFirst.createRow( 0);
							     firstcount++;
							   //内容样式
							     CellStyle cellStylecontent = wboutput.createCellStyle();
							     Font fontcontent = wboutput.createFont();
							     fontcontent.setFontHeightInPoints((short) 11);
							     cellStylecontent.setFont(fontcontent);
							     
							     CellStyle cellStyleFont = wboutput.createCellStyle();
								 Font font2 = wboutput.createFont();
							     font2.setFontName("宋体");
							     font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
							     font2.setFontHeightInPoints((short) 11);
							     
							     new UtilFunctionDao().createSSMNTitle2007(0,rowtitle0, cellStyleFont,level);
							     
								for(int i=0;i<userExport.size();i++){
									SsmnZyUser info = userExport.get(i);
									Row rowcdrnum = sheetoutputFirst.createRow(i+1);
									new UtilFunctionDao().createSSMNRowContent2007(0,rowcdrnum, info, cellStylecontent);
								}
								wboutput.write(fileoutput);
								fileoutput.close();
							}	
							
						/*****************************************************************************************/
						
						senable = smsfilename;//成功，把名称反回去
					} catch (IOException e) {
						e.printStackTrace();
						logger.error("----导出经纪人查询数据发生异常-----",e);
						senable = "";
					}
					}
					
					//ExportExcel.WebExportExcel(req, res, cdrtitle, data, smsfilename);
					String logMsg = opera.getId().getOpeno()+"导出了经纪人查询数据！";
					senable = smsfilename;//成功，把名称反回去
					
				} catch (Exception e) {
					logger.error("----导出经纪人查询数据发生异常-----",e);
					senable = "";
				}
			}	
			
			if(stype != null && !stype.equals("") && stype.equals("4"))//副号码信息生成报表
			{
				//参数
				String sssmnnum = request.getParameter("ssmnnum");//副号码
				String status = (String) request.getParameter("statusbox");
				String sbdbox1 = request.getParameter("bdbox1");//事业部
				String szonebox1= request.getParameter("zonebox1");//战区
				String sareabox1 = request.getParameter("areabox1");//片区
				String sbagbox1 = request.getParameter("bagbox1");//行动组
				String senmtype = request.getParameter("enmtype");//类型：　１：业主副号码 0:经纪人副号码
				
				List<SsmnZYEnableNumberRecord> erlist = endao.getAllzyenablenymberlist(0, null, 
						opera.getLevelid(), ssmnnum, status, sbdbox1, szonebox1, sareabox1, sbagbox1,senmtype,1);

				try{
					if(new ConfigMgr().getIsExcel2003() !=null && !"".equals(new ConfigMgr().getIsExcel2003()) && new ConfigMgr().getIsExcel2003().equals("1")
							&& erlist!=null && erlist.size()<= new Constants().maxInt)
						smsfilename = "副号码信息"+"("+opera.getId().getOpeno()+")_"+fmt.format(new Date())+".xls";
					else
						smsfilename = "副号码信息"+"("+opera.getId().getOpeno()+")_"+fmt.format(new Date())+".xlsx";
					String sfilepath = scdrfilepath +File.separator+smsfilename;
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
							
							if(new ConfigMgr().getIsExcel2003() !=null && !"".equals(new ConfigMgr().getIsExcel2003()) && new ConfigMgr().getIsExcel2003().equals("1")
									&& erlist!=null && erlist.size()<= new Constants().maxInt)
							{
								/**********--------excel2003-----------------------------------------------******************/
							     HSSFWorkbook wboutput = new HSSFWorkbook();
							     
							     //总的数据
							     HSSFSheet sheetoutputFirst = wboutput.createSheet(smsfilename);
							     HSSFRow rowtitle0 = sheetoutputFirst.createRow(0);
							     firstcount++;
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
							     
							     new UtilFunctionDao().createSSMNNumTitle(0,rowtitle0, cellStyleFont,level);
							     
								for(int i=0;i<erlist.size();i++){
									SsmnZYEnableNumberRecord info = erlist.get(i);
									HSSFRow rowcdrnum = sheetoutputFirst.createRow(i+1);
									new UtilFunctionDao().createSSMNNumRowContent(0,rowcdrnum, info, cellStylecontent);
								}
							wboutput.write(fileoutput);
							fileoutput.close();
							/**---------------------------------------------------------------------**/
							}else
							{
								/**--------------------------------excel 2007-------------------------------------**/
								SXSSFWorkbook wboutput = new SXSSFWorkbook(10000);
							     
							     //总的数据
							     Sheet sheetoutputFirst = wboutput.createSheet(smsfilename);
							     Row rowtitle0 = sheetoutputFirst.createRow(0);
							     firstcount++;
							   //内容样式
							     CellStyle cellStylecontent = wboutput.createCellStyle();
							     Font fontcontent = wboutput.createFont();
							     fontcontent.setFontHeightInPoints((short) 11);
							     cellStylecontent.setFont(fontcontent);
							     
							     CellStyle cellStyleFont = wboutput.createCellStyle();
								 Font font2 = wboutput.createFont();
							     font2.setFontName("宋体");
							     font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
							     font2.setFontHeightInPoints((short) 11);
							     
							     new UtilFunctionDao().createSSMNNumTitle2007(0,rowtitle0, cellStyleFont,level);
							     
								for(int i=0;i<erlist.size();i++){
									SsmnZYEnableNumberRecord info = erlist.get(i);
									Row rowcdrnum = sheetoutputFirst.createRow(i+1);
									new UtilFunctionDao().createSSMNNumRowContent2007(0,rowcdrnum, info, cellStylecontent);
								}
								wboutput.write(fileoutput);
								fileoutput.close();
							}
							
						senable = smsfilename;//成功，把名称反回去
					} catch (IOException e) {
						e.printStackTrace();
						logger.error("----导出副号码信息查询数据发生异常-----",e);
						senable = "";
					}
					}
					
					//ExportExcel.WebExportExcel(req, res, cdrtitle, data, smsfilename);
					String logMsg = opera.getId().getOpeno()+"导出了副号码信息查询数据！";
					senable = smsfilename;//成功，把名称反回去
					
				} catch (Exception e) {
					logger.error("----导出副号码信息查询数据发生异常-----",e);
					senable = "";
				}
			}
			
			if(stype != null && !stype.equals("") && stype.equals("5"))//写log表
			{
				HttpSession session = request.getSession();
				TyadminOperators ope = (TyadminOperators)session.getAttribute(Constants.OPERATOR_IN_SESSION);
				String streamnums = request.getParameter("streamnumber");
				String ssmnnumberstr =request.getParameter("ssmnnumberstr");
				List<SsmnZyEnablenumber> szl =endao.getSsmnEnableNumber(ssmnnumberstr);
				String sdiscritp ="";
				if(szl !=null && szl.size()>0)
				{
					SsmnZyEnablenumber sz =szl.get(0);
					if(sz.getType() ==2)
						sdiscritp ="A+";
					if(sz.getType() ==0)
						sdiscritp ="渠道";
				}
				logServ.log(ope,Constants.LOG_TYPE_OPERATOR_CDR+sdiscritp, "操作员" + ope.getId().getOpeno() + "听录音一次。"+"_"+streamnums);	
			}
			
			if(stype != null && !stype.equals("") && stype.equals("6"))//点击登录按钮，写log表
			{
				
				String name = request.getParameter("name");
				String password = request.getParameter("password");
				String service = Constants.servicekey_dc;
				
				String rtCode = operatorServ.checkOperatorLogin(name, password, service);
				
				if (rtCode.equals("")) {
					logger.debug("操作员" + name + "登陆系统。");
					TyadminOperators operator = operatorServ.lookForDetail(name,service);
					operator.setLoginName(name);	
					logServ.log(operator,Constants.LOG_TYPE_OPERATOR_LOGIN, "操作员" + name + "登陆系统。");	
				}
								
			}
			
			if(stype != null && !stype.equals("") && stype.equals("8"))//架构导出
			{
				//参数
				String sbdbox1 = request.getParameter("bdbox1");//事业部
				String szonebox1= request.getParameter("zonebox1");//战区
				String sareabox1 = request.getParameter("areabox1");//片区
				String sbagbox1 = request.getParameter("bagbox1");//行动组
				String sempno = request.getParameter("vempno");//员工编号
				
				List<SsmnZyLevel> levelExport = leveldao.getLevelList(null,null,opera.getLevelid(),sbdbox1,szonebox1,sareabox1,sbagbox1);
				Iterator<SsmnZyLevel> itlist = levelExport.iterator();
				while(itlist.hasNext())
				{
					SsmnZyLevel ip = itlist.next();
					if(ip.getBranchactiongroup() == null || "".equals(ip.getBranchactiongroup()))
						ip.setBranchactiongroup("全部");
					if(ip.getArea() == null || "".equals(ip.getArea()))
						ip.setArea("全部");
					if(ip.getWarzone() == null || "".equals(ip.getWarzone()))
						ip.setWarzone("全部");
					if(ip.getBusinessdepartment() == null || "".equals(ip.getBusinessdepartment()))
						ip.setBusinessdepartment("全部");
					if(ip.getCompany() == null || "".equals(ip.getCompany()))
						ip.setCompany("全部");
				}
				try{
					if(new ConfigMgr().getIsExcel2003() !=null && !"".equals(new ConfigMgr().getIsExcel2003()) && new ConfigMgr().getIsExcel2003().equals("1")
							&& levelExport!=null && levelExport.size()<= new Constants().maxInt)
						smsfilename = "架构信息"+"("+opera.getId().getOpeno()+")_"+fmt.format(new Date())+".xls";
					else
						smsfilename = "架构信息"+"("+opera.getId().getOpeno()+")_"+fmt.format(new Date())+".xlsx";
					String sfilepath = scdrfilepath +File.separator+smsfilename;
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
							//如果　IsExcel2003　开关开启，并且行数小于 65535条数据，则生成xls格式
							if(new ConfigMgr().getIsExcel2003() !=null && !"".equals(new ConfigMgr().getIsExcel2003()) && new ConfigMgr().getIsExcel2003().equals("1")
									&& levelExport!=null && levelExport.size()<= new Constants().maxInt)
							{
								//生成xls格式
								/*------------------------------excel2003-------------------------------------*/
								     HSSFWorkbook wboutput = new HSSFWorkbook();
								     
								     //总的数据
								     HSSFSheet sheetoutputFirst = wboutput.createSheet(smsfilename);
								     HSSFRow rowtitle0 = sheetoutputFirst.createRow( 0);
								     firstcount++;
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
								     
								     new UtilFunctionDao().createSSMNLevelTitle(0,rowtitle0, cellStyleFont,level);
								     
									for(int i=0;i<levelExport.size();i++){
										SsmnZyLevel info = levelExport.get(i);
										HSSFRow rowcdrnum = sheetoutputFirst.createRow(i+1);
										new UtilFunctionDao().createSSMNLevelRowContent(0,rowcdrnum, info, cellStylecontent);
									}
									wboutput.write(fileoutput);
									fileoutput.close();
								
							}else
							{
								/**--------------------------excel 2007--------------------------------------------------**/
								SXSSFWorkbook wboutput = new SXSSFWorkbook(10000);
							     
							     //总的数据
							     Sheet sheetoutputFirst = wboutput.createSheet(smsfilename);
							     Row rowtitle0 = sheetoutputFirst.createRow( 0);
							     firstcount++;
							   //内容样式
							     CellStyle cellStylecontent = wboutput.createCellStyle();
							     Font fontcontent = wboutput.createFont();
							     fontcontent.setFontHeightInPoints((short) 11);
							     cellStylecontent.setFont(fontcontent);
							     
							     CellStyle cellStyleFont = wboutput.createCellStyle();
								 Font font2 = wboutput.createFont();
							     font2.setFontName("宋体");
							     font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
							     font2.setFontHeightInPoints((short) 11);
							     
							     new UtilFunctionDao().createSSMNLevelTitle2007(0,rowtitle0, cellStyleFont,level);
							     
								for(int i=0;i<levelExport.size();i++){
									SsmnZyLevel info = levelExport.get(i);
									Row rowcdrnum = sheetoutputFirst.createRow(i+1);
									new UtilFunctionDao().createSSMNLevelRowContent2007(0,rowcdrnum, info, cellStylecontent);
								}
								wboutput.write(fileoutput);
								fileoutput.close();
							}	
							
						/*****************************************************************************************/
						
						senable = smsfilename;//成功，把名称反回去
					} catch (IOException e) {
						e.printStackTrace();
						logger.error("----导出架构信息查询数据发生异常-----",e);
						senable = "";
					}
					}
					
					//ExportExcel.WebExportExcel(req, res, cdrtitle, data, smsfilename);
					String logMsg = opera.getId().getOpeno()+"导出了经纪人查询数据！";
					senable = smsfilename;//成功，把名称反回去
					
				} catch (Exception e) {
					logger.error("----导出经纪人查询数据发生异常-----",e);
					senable = "";
				}
			}
						
			if(stype != null && !stype.equals("") && stype.equals("28"))//黑名单信息导出
			{
				//参数
				String sblackn =request.getParameter("blackNum");
				String startdatestr =request.getParameter("startdate");
				String enddatestr = request.getParameter("enddate");
				
				List<Blackcallnumber> levelExport = bDao.getBlackNumList(sblackn,startdatestr,enddatestr,-1,-1);
				
				try{
					if(new ConfigMgr().getIsExcel2003() !=null && !"".equals(new ConfigMgr().getIsExcel2003()) && new ConfigMgr().getIsExcel2003().equals("1")
							&& levelExport!=null && levelExport.size()<= new Constants().maxInt)
						smsfilename = "黑名单信息"+"("+opera.getId().getOpeno()+")_"+fmt.format(new Date())+".xls";
					else
						smsfilename = "黑名单信息"+"("+opera.getId().getOpeno()+")_"+fmt.format(new Date())+".xlsx";
					String sfilepath = scdrfilepath +File.separator+smsfilename;
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
							//如果　IsExcel2003　开关开启，并且行数小于 65535条数据，则生成xls格式
							if(new ConfigMgr().getIsExcel2003() !=null && !"".equals(new ConfigMgr().getIsExcel2003()) && new ConfigMgr().getIsExcel2003().equals("1")
									&& levelExport!=null && levelExport.size()<= new Constants().maxInt)
							{
								//生成xls格式
								/*------------------------------excel2003-------------------------------------*/
								     HSSFWorkbook wboutput = new HSSFWorkbook();
								     
								     //总的数据
								     HSSFSheet sheetoutputFirst = wboutput.createSheet(smsfilename);
								     HSSFRow rowtitle0 = sheetoutputFirst.createRow( 0);
								     firstcount++;
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
								     
								     new UtilFunctionDao().createBlackNumTitle(0,rowtitle0, cellStyleFont);
								     
									for(int i=0;i<levelExport.size();i++){
										Blackcallnumber info = levelExport.get(i);
										HSSFRow rowcdrnum = sheetoutputFirst.createRow(i+1);
										new UtilFunctionDao().createBlackNumTRowContent(0,rowcdrnum, info, cellStylecontent);
									}
									wboutput.write(fileoutput);
									fileoutput.close();
								
							}else
							{
								/**--------------------------excel 2007--------------------------------------------------**/
								SXSSFWorkbook wboutput = new SXSSFWorkbook(10000);
							     
							     //总的数据
							     Sheet sheetoutputFirst = wboutput.createSheet(smsfilename);
							     Row rowtitle0 = sheetoutputFirst.createRow( 0);
							     firstcount++;
							   //内容样式
							     CellStyle cellStylecontent = wboutput.createCellStyle();
							     Font fontcontent = wboutput.createFont();
							     fontcontent.setFontHeightInPoints((short) 11);
							     cellStylecontent.setFont(fontcontent);
							     
							     CellStyle cellStyleFont = wboutput.createCellStyle();
								 Font font2 = wboutput.createFont();
							     font2.setFontName("宋体");
							     font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
							     font2.setFontHeightInPoints((short) 11);
							     
							     new UtilFunctionDao().createBlackNumTitle2007(0,rowtitle0, cellStyleFont);
							     
								for(int i=0;i<levelExport.size();i++){
									Blackcallnumber info = levelExport.get(i);
									Row rowcdrnum = sheetoutputFirst.createRow(i+1);
									new UtilFunctionDao().createBlackNumTRowContent2007(0,rowcdrnum, info, cellStylecontent);
								}
								wboutput.write(fileoutput);
								fileoutput.close();
							}	
							
						/*****************************************************************************************/
						
						senable = smsfilename;//成功，把名称反回去
					} catch (IOException e) {
						e.printStackTrace();
						logger.error("----导出黑名单信息查询数据发生异常-----",e);
						senable = "";
					}
					}
					
					//ExportExcel.WebExportExcel(req, res, cdrtitle, data, smsfilename);
					String logMsg = opera.getId().getOpeno()+"导出了黑名单查询数据！";
					senable = smsfilename;//成功，把名称反回去
					
				} catch (Exception e) {
					logger.error("----导出黑名单查询数据发生异常-----",e);
					senable = "";
				}
			}
			if(stype != null && !stype.equals("") && stype.equals("30"))//呼叫信息导出
			{
				String stdate =request.getParameter("startdate");
				List<SsmnZyClientnum> szclist =callDao.getClientNumAll(level,stdate,null,null);
				//查询呼叫状态,去ssmn_zy_cdr表中查
				if(szclist !=null && szclist.size()>0)
				{
					for(int i=0;i<szclist.size();i++)
					{
						SsmnZyClientnum zc =szclist.get(i);
						if(zc !=null && zc.getClientnum().length()>0)
						{
							//去ssmn_zy_cdr中查，如果有记录，则说明有呼叫，否则没有呼叫
							List<SsmnZyCdr> cl =cDao.getCdrByMsisdn(zc.getUsermsisdn(),zc.getClientnum(),zc.getCalltime());
							if(cl !=null && cl.size()>0)
								szclist.get(i).setCallstatus(true);
							else
								szclist.get(i).setCallstatus(false);
						}
					}
				}
				try{
					if(new ConfigMgr().getIsExcel2003() !=null && !"".equals(new ConfigMgr().getIsExcel2003()) && new ConfigMgr().getIsExcel2003().equals("1")
							&& szclist!=null && szclist.size()<= new Constants().maxInt)
						smsfilename = "呼叫信息"+"("+opera.getId().getOpeno()+")_"+fmt.format(new Date())+".xls";
					else
						smsfilename = "呼叫信息"+"("+opera.getId().getOpeno()+")_"+fmt.format(new Date())+".xlsx";
					String sfilepath = scdrfilepath +File.separator+smsfilename;
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
							//如果　IsExcel2003　开关开启，并且行数小于 65535条数据，则生成xls格式
							if(new ConfigMgr().getIsExcel2003() !=null && !"".equals(new ConfigMgr().getIsExcel2003()) && new ConfigMgr().getIsExcel2003().equals("1")
									&& szclist!=null && szclist.size()<= new Constants().maxInt)
							{
								//生成xls格式
								/*------------------------------excel2003-------------------------------------*/
								     HSSFWorkbook wboutput = new HSSFWorkbook();
								     
								     //总的数据
								     HSSFSheet sheetoutputFirst = wboutput.createSheet(smsfilename);
								     HSSFRow rowtitle0 = sheetoutputFirst.createRow( 0);
								     firstcount++;
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
								     
								     new UtilFunctionDao().createCallInfoTitle(0,rowtitle0, cellStyleFont);
								     
									for(int i=0;i<szclist.size();i++){
										SsmnZyClientnum info = szclist.get(i);
										HSSFRow rowcdrnum = sheetoutputFirst.createRow(i+1);
										new UtilFunctionDao().createCallInfoContent(0,rowcdrnum, info, cellStylecontent);
									}
									wboutput.write(fileoutput);
									fileoutput.close();
								
							}else
							{
								/**--------------------------excel 2007--------------------------------------------------**/
								SXSSFWorkbook wboutput = new SXSSFWorkbook(10000);
							     
							     //总的数据
							     Sheet sheetoutputFirst = wboutput.createSheet(smsfilename);
							     Row rowtitle0 = sheetoutputFirst.createRow( 0);
							     firstcount++;
							   //内容样式
							     CellStyle cellStylecontent = wboutput.createCellStyle();
							     Font fontcontent = wboutput.createFont();
							     fontcontent.setFontHeightInPoints((short) 11);
							     cellStylecontent.setFont(fontcontent);
							     
							     CellStyle cellStyleFont = wboutput.createCellStyle();
								 Font font2 = wboutput.createFont();
							     font2.setFontName("宋体");
							     font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
							     font2.setFontHeightInPoints((short) 11);
							     
							     new UtilFunctionDao().createCallInfoTitle2007(0,rowtitle0, cellStyleFont);
							     
								for(int i=0;i<szclist.size();i++){
									SsmnZyClientnum info = szclist.get(i);
									Row rowcdrnum = sheetoutputFirst.createRow(i+1);
									new UtilFunctionDao().createCallInfoContent2007(0,rowcdrnum, info, cellStylecontent);
								}
								wboutput.write(fileoutput);
								fileoutput.close();
							}	
							
						/*****************************************************************************************/
						
						senable = smsfilename;//成功，把名称反回去
					} catch (IOException e) {
						e.printStackTrace();
						logger.error("----导出黑名单信息查询数据发生异常-----",e);
						senable = "";
					}
					}
					
					//ExportExcel.WebExportExcel(req, res, cdrtitle, data, smsfilename);
					String logMsg = opera.getId().getOpeno()+"导出了黑名单查询数据！";
					senable = smsfilename;//成功，把名称反回去
					
				} catch (Exception e) {
					logger.error("----导出黑名单查询数据发生异常-----",e);
					senable = "";
				}
			}
			if(stype != null && !stype.equals("") && stype.equals("33"))//呼叫管理 ，呼叫按钮
			{
				String umsisdn =request.getParameter("umsisdn");
				String clientnum =request.getParameter("clientnum");
				String url = ConfigMgr.getCallurl();
				if(url !=null && url.length()>0)
				{
			    	try {
				    	JSONObject jsonObject = new JSONObject();
						jsonObject.put("caller", umsisdn);
				        jsonObject.put("callee", clientnum);
				        jsonObject.put("flag", "0");
				        logger.info("----req----------------"+jsonObject.toString());
				        senable =HttpTool.sendPost(jsonObject.toString(),url,"test");
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						senable ="呼叫失败!";
						e.printStackTrace();
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						senable ="呼叫失败!";
						e.printStackTrace();
					}
				}else
					senable ="请配置呼叫地址!";
			}
			
			if(stype != null && !stype.equals("") && !stype.equals("10"))
			{
				if(senable != null && senable.length()>0)
					xml = senable;
				else 
					xml = "";
			}
					
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
