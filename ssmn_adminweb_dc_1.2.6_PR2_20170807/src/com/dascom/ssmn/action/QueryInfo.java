package com.dascom.ssmn.action;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dascom.OPadmin.action.AdminFormAction;
import com.dascom.OPadmin.dao.IAdminAuthority;
import com.dascom.OPadmin.dao.impl.AdminAuthorityImpl;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.util.Page;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.entity.DownLoadFileEntity;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.util.Constants;

public class QueryInfo  extends AdminFormAction{
	private static final Logger logger = Logger.getLogger(QueryInfo.class);
	private static int icountnum = 0;
	private IAdminAuthority authDao = new AdminAuthorityImpl();
	@Override
	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		HttpSession session = req.getSession();
		String execlPath = session.getServletContext().getRealPath(Constants.EXECL_FOLDER_NAME)+File.separator ;
		//String execlMonthPath = session.getServletContext().getRealPath(Constants.DOWNLOAD_CALL_STATISTIC_FILENAME)+File.separator+Constants.EXECL_FOLDER_NAME+File.separator ;
		String execlMonthPath =execlPath;
		String zipSavePath =Constants.ZIP_DIR; //session.getServletContext().getRealPath(Constants.ZIP_FOLDER_NAME)+File.separator ;
		TyadminOperators opera = (TyadminOperators)req.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		//权限不能从session中取，因为权限功能，随时要改变（在权限管理中）
		List<String> authMethodList = authDao.findByGroupId(opera.getGroupId().longValue(),(String)req.getSession().getAttribute(Constants.SERVICEKEY_IN_SESSION));
		UtilFunctionDao.showType(authMethodList);
		List<DownLoadFileEntity> filelist = new ArrayList<DownLoadFileEntity>();
		Date startdate = null;
		Date enddate = null;
		String tempstartdate = null;
		String tempenddate = null;
		boolean isdatesearch = false;
		String sprovinceName1 = "";
		String sprovinceName2 = "";
		int reccount = 0;
		List<String> dates = null;	
		String sisMonthdown = "0";
		int fileType1=0;//语音文件的显示类型 1:短文件名　2:长文件名
		int fileType2 = 0;//压缩文件显示类型 1:短文件名 2:长文件名
		int fileType3 = 0;//月统计文件显示类型 1:短文件名 2:长文件名
		String stateExtentYesterday = "xlsx";//昨日语音文件扩展名
		String stateExtentMonth = "xlsx";//上月语音文件扩展名
		String showtypetemp ="";
		try {
			String method = req.getParameter("method");
			Date initDate = this.getInitOfToday();
			String temp = req.getParameter("page");
			Page p = sysUtil.setPageInfo(req, temp, Constants.RECORD_PER_PAGE);

			if(Constants.TYPE_SHOWDATE <2)
			{
				if(Constants.TYPE_SHOWDATE == 0)
					showtypetemp ="_C";
				else if(Constants.TYPE_SHOWDATE ==1)
					showtypetemp ="_A";
			}
			
			if(method != null && !"".equals(method) && "datesearch".equals(method) && 
					!"".equals(req.getParameter("enddate")) && !"".equals(req.getParameter("startdate"))){
				//如果选择日期，按日期查询
				String tempstartdate1 = req.getParameter("startdate");
				tempstartdate1 +=" 00:00:00";
				String tempenddate1 = req.getParameter("enddate");
				tempenddate1 += " 23:59:59";
				startdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tempstartdate1);
				enddate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tempenddate1);
				tempstartdate = req.getParameter("startdate");
				tempenddate = req.getParameter("enddate");
				icountnum = 0;
				
				dates = getBetweenDatesList(tempstartdate,tempenddate,p.getFirstResult()-1, p.getSize());
				reccount = icountnum;
				isdatesearch = true;
				req.setAttribute("startdate", req.getParameter("startdate"));
				req.setAttribute("enddate", req.getParameter("enddate"));
			}else
			{
				//没有选择日期，则显示从昨天开始的最近7天数据
				if(new Date().getTime() >= initDate.getTime()){  //定点之后可以下载昨天文件
					dates = this.getDateList(true);
				}else{
					dates = this.getDateList(false);
				}
			}
			
			
			for(int i=0 ;i<dates.size();i++){
				String date = dates.get(i);
				if(isdatesearch){
					Date tempdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date+" 00:00:00");
						if(tempdate.getTime() >= startdate.getTime() && tempdate.getTime() <= enddate.getTime()){ //在开始时间之后，结束时间之前
							//继续向下执行
						}else{
							//跳过
							continue;
						}
				}
				

				DownLoadFileEntity df = new DownLoadFileEntity();
				df.setDate(date);
				
				//判断统计文件是否存在				
				String execlSavePath1 = Constants.DOWNLOAD_EXECL_DIR+Constants.DOWNLOAD_EXECL_NAME1;
				String execlSavePath2 = Constants.DOWNLOAD_EXECL_DIR+Constants.DOWNLOAD_EXECL_NAME2;
				sprovinceName1 = "";
				sprovinceName2 = "";
				SsmnZyLevel level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
				
				//所有级别组合
				if(level.getProvincecity() != null && !"".equals(level.getProvincecity()))
					sprovinceName2 +=level.getProvincecity();
				if(level.getCompany() != null && !"".equals(level.getCompany()))
					sprovinceName2 +=level.getCompany();
				if(level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment()))
					sprovinceName2 +=level.getBusinessdepartment();
				if(level.getWarzone() != null && !"".equals(level.getWarzone()))
					sprovinceName2 +=level.getWarzone();
				if(level.getArea() != null && !"".equals(level.getArea()))
					sprovinceName2 +=level.getArea();
				if(level.getBranchactiongroup() != null && !"".equals(level.getBranchactiongroup()))
					sprovinceName2 +=level.getBranchactiongroup();
				
				//只取最后一个级别，用此方法
				if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) && (level.getCompany() == null || "".equals(level.getCompany())))//省级
				{
					 
					sprovinceName1 +=level.getProvincecity();
				} 
				if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) && level.getCompany() != null && !"".equals(level.getCompany()) && 
						(level.getBusinessdepartment() == null || "".equals(level.getBusinessdepartment())))//公司级
				{
					sprovinceName1 +=level.getCompany();
				}
				if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) 
						&& level.getCompany() != null && !"".equals(level.getCompany()) && 
						level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment())
						&& (level.getWarzone() == null || "".equals(level.getWarzone())))//事业部级
				{
					sprovinceName1 +=level.getBusinessdepartment();
				}
				if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) 
						&& level.getCompany() != null && !"".equals(level.getCompany()) && 
						level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment())
						&& level.getWarzone() != null && !"".equals(level.getWarzone())
						&& (level.getArea() == null || "".equals(level.getArea())))//战区级
				{
					sprovinceName1 +=level.getWarzone();
				}
				if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) 
						&& level.getCompany() != null && !"".equals(level.getCompany()) && 
						level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment())
						&& level.getWarzone() != null && !"".equals(level.getWarzone()) && 
						level.getArea() != null && !"".equals(level.getArea()) 
						&& (level.getBranchactiongroup() == null || "".equals(level.getBranchactiongroup())))//片区级
				{
					sprovinceName1 +=level.getArea();
				}
				if(level.getProvincecity() != null && !"".equals(level.getProvincecity()) 
						&& level.getCompany() != null && !"".equals(level.getCompany()) && 
						level.getBusinessdepartment() != null && !"".equals(level.getBusinessdepartment())
						&& level.getWarzone() != null && !"".equals(level.getWarzone()) && 
						level.getArea() != null && !"".equals(level.getArea())
						&& level.getBranchactiongroup() != null && !"".equals(level.getBranchactiongroup()))//行动组级 ...写的好恶心...
				{
					sprovinceName1 +=level.getBranchactiongroup();
				}
				
				execlSavePath1 += sprovinceName1;
				execlSavePath2 +=sprovinceName2;
				
//取昨天的语音文件 excel文件
				execlSavePath1 +="_"+date;//".xls";
				File efile = new File(execlSavePath1+showtypetemp+".xls");
								
				if(efile.length()!=0){  //文件存在  //stateExtentYesterday为扩展名
					stateExtentYesterday = "xls";
					fileType1 = 1;
					df.setExeclname(Constants.DOWNLOAD_EXECL_NAME1+sprovinceName1+"_"+date);
				}else
				{
					//如果现用的知的文件不存在，还使用长的文件名的文件
					stateExtentYesterday = "xlsx";
					fileType1 =2;//只要短文件名不存在，默认为长文件名
					execlSavePath2 +="_"+date;
					File efile2 = new File(execlSavePath2+showtypetemp+".xlsx");
					File efile22 = new File(execlSavePath2+showtypetemp+".xls");
					
					if(efile22.length() !=0)
						stateExtentYesterday = "xls";
					
					if(efile2.length() !=0 || efile22.length() !=0)
					{
						
						df.setExeclname(Constants.DOWNLOAD_EXECL_NAME2+sprovinceName2+"_"+date);
					}else
					{
						//文件不存在
					}
					
				}
				df.setExcelExtent(stateExtentYesterday);
				
//判断通话录音文件是否存在 zip文件
				File zfile = new File(zipSavePath+Constants.DOWNLOAD_FILENAME1+sprovinceName1+"_"+date+".zip");
				if(zfile.length()!=0){  //文件存在
					fileType2 =1;
					df.setZipname( Constants.DOWNLOAD_FILENAME1+sprovinceName1+"_"+date);
				}else
				{
					//如果现用的知的文件不存在，还使用长的文件名的文件
					File zfile2 = new File(zipSavePath+Constants.DOWNLOAD_FILENAME2+sprovinceName2+"_"+date+".zip");
					if(zfile2.length()!=0)
					{  //文件存在
						fileType2 =2;
						df.setZipname( Constants.DOWNLOAD_FILENAME2+sprovinceName2+"_"+date);
					}
			   }
				filelist.add(df);
			}
//取上月统计文件
			
			String slastmonthday = getLastMonthDay();
			String sMonthpath1 = execlMonthPath+slastmonthday+File.separator+Constants.DOWNLOAD_EXECL_NAME1+sprovinceName1+"_"+slastmonthday+showtypetemp+".xls";
			String sMonthpath2 = execlMonthPath+slastmonthday+File.separator+Constants.DOWNLOAD_EXECL_NAME2+sprovinceName2+"_"+slastmonthday+showtypetemp+".xlsx";// 
			String sMonthpath22 = execlMonthPath+slastmonthday+File.separator+Constants.DOWNLOAD_EXECL_NAME2+sprovinceName2+"_"+slastmonthday+showtypetemp+".xls";// 
			File zmonth = new File(sMonthpath1);
			if(zmonth.length() !=0)//文件存在
			{
				stateExtentMonth = "xls";
				sisMonthdown = "1";
				fileType3 =1;
			}else
			{
				stateExtentMonth = "xlsx";  
				//再判断长文件名是否存在，如果存在，也要显示
				File zmonth2 = new File(sMonthpath2);
				File zmonth22 = new File(sMonthpath22);
				if(zmonth2.exists())
				{
					stateExtentMonth = "xlsx";
					fileType3 =2;
					sisMonthdown = "1";
				}
				if(zmonth22.exists())
				{
					stateExtentMonth = "xls";
					fileType3 =2;
					sisMonthdown = "1";
				}

				if(!zmonth2.exists() && !zmonth22.exists())
					sisMonthdown = "0";
			}
				
				
		} catch (Exception e) {
			logger.error("----Exception-----",e);
		}
		
		String lastmonth = getLastMonthDay();
				
		req.setAttribute("filelist", filelist);
		req.setAttribute("showtypetemp", showtypetemp);		
		req.setAttribute("sprovinceFileName1", sprovinceName1);
		req.setAttribute("sprovinceFileName2", sprovinceName2);
		req.setAttribute("fileType1", fileType1);
		req.setAttribute("fileType2", fileType2);
		req.setAttribute("fileType3", fileType3);
		req.setAttribute("stateExtentMonth", stateExtentMonth);			
		req.setAttribute("authMethodList", authMethodList);
		req.setAttribute("recordCount", reccount);
		req.setAttribute("lastmonth", lastmonth);
		req.setAttribute("sisMonthdown", sisMonthdown);
		return mapping.findForward(super.formView);
	}

	
	/**
	 * 取出当前日期之前的若干天的时间字符串的集合
	 * param isyestoday  是否包含昨天的记录
	 * @return
	 */
	public List<String> getDateList(boolean isyestoday){
		List<String> dates = new ArrayList<String>();
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMATE);
		Calendar cal = Calendar.getInstance();
		int day_of_month = cal.get(Calendar.DAY_OF_MONTH);  
		if(!isyestoday){
			day_of_month = day_of_month-1;
		}
		
		for(int i=1;i<(Constants.DOWNLOAD_INTERVAL+1);i++){
			cal.set(Calendar.DAY_OF_MONTH, day_of_month-1);
			dates.add(dateFormat.format(cal.getTime()).trim());
		    day_of_month = cal.get(Calendar.DAY_OF_MONTH);
		}
		
		return dates;
	}
	
	public List<String> getBetweenDatesList(String startdate, String enddate,int firstResult,int intSize)
	{
		icountnum = 0;
		List<String> dates = new ArrayList<String>();
		Calendar startDay = Calendar.getInstance();
		Calendar endDay = Calendar.getInstance();
		
		Calendar cyestoday = Calendar.getInstance();
		cyestoday.setTime(new Date());
		cyestoday.set(Calendar.DATE, cyestoday.get(Calendar.DATE) - 1);
		int icount = 0;
				
		 try {
			 startDay.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(startdate));
			 endDay.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(enddate));
		
			 // 给出的日期开始日比终了日大则不执行打印
			  if (startDay.compareTo(endDay) > 0) {
				  return null;
			  }
			  // 现在打印中的日期
			  if(endDay.compareTo(cyestoday) >0)
				  endDay = cyestoday;
			  Calendar currentPrintDay = endDay;
			  while (true) {
				   // 日期加一后判断是否达到终了日，达到则终止
				   /*if (currentPrintDay.compareTo(endDay) > 0) {
				    break;
				   }*/
				  // 日期减一后判断是否达到起始日，达到则终止
				   if (currentPrintDay.compareTo(startDay) < 0) {
				    break;
				   }
				   //如果选择的日期超过了昨天，则把enddate设置为昨天的日期 //这里基本没用
				  /* if(currentPrintDay.compareTo(cyestoday) >=0)
				   {
				   		break;
				   }*/
				   // 打印日期
				   if(icount >= firstResult && icount < firstResult+intSize)
					   dates.add(new SimpleDateFormat("yyyy-MM-dd").format(currentPrintDay.getTime()));
				   // 日期加一
				   currentPrintDay.set(Calendar.DATE, currentPrintDay.get(Calendar.DATE) - 1);
				   icount++;
			  }
		 
		  } catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		if(icount >0)
			icountnum = icount;
		return dates;
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
	
	/**
	 * 获取能够下载前一天文件的时间
	 * @return
	 */
	public static Date getInitOfToday() {        
		Calendar cal = Calendar.getInstance();        
		cal.setTime(new Date());        
		cal.set(Calendar.HOUR_OF_DAY, Constants.DOWNLOAD_HOUR);        
		cal.set(Calendar.MINUTE, 0);        
		cal.set(Calendar.SECOND, 0);        
		cal.set(Calendar.MILLISECOND, 0);        
		return  cal.getTime();    
		
	}
	
	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		return this.processSubmit(mapping, form, req, res);
	}
}
