package com.dascom.OPadmin.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.IAdminGroup;
import com.dascom.OPadmin.dao.IAdminOperator;
import com.dascom.OPadmin.dao.impl.AdminGroupImpl;
import com.dascom.OPadmin.dao.impl.AdminOperatorImpl;
import com.dascom.OPadmin.entity.TyadminGroups;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.init.ConfigMgr;
import com.dascom.ssmn.dao.CDRDao;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.ZydcRecord;
import com.dascom.ssmn.util.Constants;
import com.dascom.OPadmin.entity.TyadminOperatorLogs;

public class ExportOperatorInfo extends HttpServlet{
	private static final Logger logger = Logger.getLogger(ExportOperatorInfo.class);
	
	private LevelDao leveldao = LevelDao.getInstance();

	private IAdminOperator operatorDao = new AdminOperatorImpl();
	private IAdminGroup groupDao = new AdminGroupImpl();
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response)
	throws ServletException, IOException{
		TyadminOperators opera = (TyadminOperators)request.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		try{
			 HttpSession session = request.getSession();
			response.setHeader("Cache-Control", "no-cashe");			
			response.setContentType("text/plain; charset=UTF-8");
			String keyWord = request.getParameter("openo");
			String stype = request.getParameter("type");
			Long level = (Long)session.getAttribute("level");
			Long operalevelid = opera.getLevelid();
			SsmnZyLevel zylevel = null;
			List<TyadminGroups> gpList = new ArrayList<TyadminGroups>();
		      
	        String bdparam = (String) request.getParameter("bdbox1");//事业部
			String zoneparam = (String) request.getParameter("zonebox1");//战区
			String areaparam = (String) request.getParameter("areabox1");//片区
			String bagparam = (String) request.getParameter("bagbox1");//行动组
			
			//操作员登录详情
			String starttime = (String)request.getParameter("startdate");
			String endtime = (String)request.getParameter("enddate");
			String sislogin = (String)request.getParameter("islogin");
				
			try {
				zylevel = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(operalevelid);
			} catch (DaoException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			String xml = "";
			
			SimpleDateFormat fmt = new SimpleDateFormat("yyMMddHHmmss");
	
			List<TyadminOperators> opeList=new ArrayList<TyadminOperators>();
			List<TyadminOperatorLogs> opeLogList=new ArrayList<TyadminOperatorLogs>();
			//List<TyadminOperatorLogs> opeLogListDetail=new ArrayList<TyadminOperatorLogs>();
			String scdrfilepath = request.getSession().getServletContext().getRealPath("exportOperatorFiles");//导出操作员
			
			
			File direx = new File(scdrfilepath);
			if (!direx.exists()) {
				if (!direx.mkdirs())
					System.out.println("创建文件失败");
			}
			
			 String smsfilename = "";
			 String senable = "";
			 if(stype != null && !"".equals(stype) && stype.equals("1")) //操作员导出
			 {
				opeList = operatorDao.findByServiceKey((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION),level.toString(),keyWord,bdparam,zoneparam,areaparam,bagparam,zylevel,null,null);
				Iterator<TyadminOperators> itlist = opeList.iterator();
				while(itlist.hasNext())
				{
					TyadminOperators ip = itlist.next();
					if(ip.getId().getOpeno().equals(opera.getId().getOpeno()))
					{
						//自己是超级管理员，去掉
						itlist.remove();
						continue;
					}
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
			 }else if(stype != null && !"".equals(stype) && stype.equals("3"))//群组导出
			 {
				String groupname = request.getParameter("groupname");
				if(groupname==null || "".equals(groupname))
				{			
					gpList = groupDao.findAllGroups(opera.getId().getServicekey(),level);
				}
				else
				{
					gpList = groupDao.findByGroupName(groupname, opera.getId().getServicekey(),level);
				}
			 }
			 else
			 {
				 //操作员登录详情
				 opeLogList = operatorDao.findOpenoLoginDetail(starttime, endtime, sislogin, bdparam,zoneparam,areaparam,bagparam, keyWord,zylevel, null,null);
				 //有登录的才去查详情，未登录的，不需要查详情，直接导出
				 //if( sislogin !=null && !"".equals(sislogin) && sislogin.equals("2"))
				//	 opeLogListDetail=opeLogList;
				// else
				//	 opeLogListDetail = operatorDao.findOpenoLoginDetailByExport(starttime,endtime,opeLogList); 
				 Iterator<TyadminOperatorLogs> it = opeLogList.iterator();
				while(it.hasNext())
				{
					TyadminOperatorLogs ip = it.next();
					if(ip.getBusinessdepartment() == null || ip.getBusinessdepartment().equals(""))
						ip.setBusinessdepartment("全部");
					if(ip.getWarzone() == null || ip.getWarzone().equals(""))
						ip.setWarzone("全部");
					if(ip.getArea() == null || ip.getArea().equals(""))
						ip.setArea("全部");
					if(ip.getBranchactiongroup() == null || ip.getBranchactiongroup().equals(""))
						ip.setBranchactiongroup("全部");
					//有登录次数的才增查听录音的次数,没有的不去查
					if(ip.getLoginCount()>0)
					{
						//查找听录音次数,根据时间查次数
						String sendTim = operatorDao.getLastTime(ip.getLastLoginTime(),ip.getOpeno());
						if(ConfigMgr.getDistChannel().equals("1"))//是否区分 A+ 和渠道
						{
							//区分
							int countCdrA = operatorDao.findCdrCount(ip.getOpeno(),starttime,sendTim,0,"A+");
							ip.setCdrCountA(countCdrA);
							int countCdrCh = operatorDao.findCdrCount(ip.getOpeno(),starttime,sendTim,0,"渠道");
							ip.setCdrCountChannel(countCdrCh);
						}else
						{
							int countCdr = operatorDao.findCdrCount(ip.getOpeno(),starttime,sendTim,0,"");
							ip.setCdrCountTotal(countCdr);
						}
						//备注次数
						int remarkc =operatorDao.findCdrCount(ip.getOpeno(),starttime,sendTim,0, "备注次数");
						ip.setCdrRemarkCount(remarkc);
					}
				}
			 }
				try{
					if(new ConfigMgr().getIsExcel2003() !=null && !"".equals(new ConfigMgr().getIsExcel2003()) && new ConfigMgr().getIsExcel2003().equals("1")
							&& opeList!=null && opeList.size()<= new Constants().maxInt)
					{
						if(stype != null && !"".equals(stype) && stype.equals("1"))
							smsfilename = "操作员账号"+"("+opera.getId().getOpeno()+")_"+fmt.format(new Date())+".xls";
						else if(stype != null && !"".equals(stype) && stype.equals("3"))
							smsfilename = "群组_"+fmt.format(new Date())+".xls";
						else
							smsfilename = "操作员登录详情"+"("+opera.getId().getOpeno()+")_"+fmt.format(new Date())+".xls";
					}else
					{
						if(stype != null && !"".equals(stype) && stype.equals("1"))
							smsfilename = "操作员账号"+"("+opera.getId().getOpeno()+")_"+fmt.format(new Date())+".xlsx";
						else if(stype != null && !"".equals(stype) && stype.equals("3"))
							smsfilename = "群组_"+fmt.format(new Date())+".xlsx";
						else
							smsfilename = "操作员登录详情"+"("+opera.getId().getOpeno()+")_"+fmt.format(new Date())+".xlsx";
					}
					
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
									&& opeList!=null && opeList.size()<= new Constants().maxInt)
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
							   
							     if(stype != null && !"".equals(stype) && stype.equals("1"))
							     {
							    	 new UtilFunctionDao().createOperatorTitle(0,rowtitle0, cellStyleFont,zylevel);
									for(int i=0;i<opeList.size();i++){
										TyadminOperators info = opeList.get(i);
										HSSFRow rowcdrnum = sheetoutputFirst.createRow(i+1);
										new UtilFunctionDao().createOperatorContent(i+1,rowcdrnum, info, cellStylecontent);
									}
							     }else if(stype != null && !"".equals(stype) && stype.equals("3"))//群组导出
							     {
							    	 new UtilFunctionDao().createGroupTitle(0,rowtitle0, cellStyleFont);
										for(int i=0;i<gpList.size();i++){
											TyadminGroups info = gpList.get(i);
											HSSFRow rowcdrnum = sheetoutputFirst.createRow(i+1);
											new UtilFunctionDao().createGroupContent(i+1,rowcdrnum, info, cellStylecontent);
										}
							     }							     
							     else
							     {
							    	 new UtilFunctionDao().createOperatorLogTitle(0,rowtitle0, cellStyleFont,zylevel);
							    	 for(int i=0;i<opeLogList.size();i++){
							    		 TyadminOperatorLogs info = opeLogList.get(i);
							    		 HSSFRow rowcdrnum = sheetoutputFirst.createRow(i+1);
											new UtilFunctionDao().createOperatorLogContent(i+1,rowcdrnum, info, cellStylecontent);
										}
							     }
							     wboutput.write(fileoutput);
							     fileoutput.close();
							/*----------------------------------------------------------------------------------------*/
								
							}else
							{
								/*---------------------------excel2007-----------------------------------------------*/
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
							     
							     if(stype != null && !"".equals(stype) && stype.equals("1"))
							     {
							    	 new UtilFunctionDao().createOperatorTitle2007(0,rowtitle0, cellStyleFont,zylevel);
									for(int i=0;i<opeList.size();i++){
										TyadminOperators info = opeList.get(i);
										Row rowcdrnum = sheetoutputFirst.createRow(i+1);
										new UtilFunctionDao().createOperatorContent2007(i+1,rowcdrnum, info, cellStylecontent);
									}
							     }else if(stype != null && !"".equals(stype) && stype.equals("3"))//群组导出
							     {
							    	 new UtilFunctionDao().createGroupTitle2007(0,rowtitle0, cellStyleFont);
										for(int i=0;i<gpList.size();i++){
											TyadminGroups info = gpList.get(i);
											Row rowcdrnum = sheetoutputFirst.createRow(i+1);
											new UtilFunctionDao().createGroupContent2007(i+1,rowcdrnum, info, cellStylecontent);
										}
							     }	
							     else
							     {
							    	 new UtilFunctionDao().createOperatorLogTitle2007(0,rowtitle0, cellStyleFont);
							    	 for(int i=0;i<opeLogList.size();i++){
							    		 TyadminOperatorLogs info = opeLogList.get(i);
											Row rowcdrnum = sheetoutputFirst.createRow(i+1);
											new UtilFunctionDao().createOperatorLogContent2007(i+1,rowcdrnum, info, cellStylecontent);
										}
							     }
							     wboutput.write(fileoutput);
							     fileoutput.close();
							}
							
						senable = smsfilename;//成功，把名称反回去
					} catch (IOException e) {
						e.printStackTrace();
						logger.error("----导出副号码语音查询数据发生异常-----",e);
						senable = "";
					}
					}
					
					//ExportExcel.WebExportExcel(req, res, cdrtitle, data, smsfilename);
					String logMsg = opera.getId().getOpeno()+"导出了操作员账号查询数据！";
					senable = smsfilename;//成功，把名称反回去
					
				} catch (Exception e) {
					logger.error("----导出操作员账号查询数据发生异常-----",e);
					senable = "";
				}
			 
			
			if(senable != null && senable.length()>0)
				xml = senable;
			else 
				xml = "";
		
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
