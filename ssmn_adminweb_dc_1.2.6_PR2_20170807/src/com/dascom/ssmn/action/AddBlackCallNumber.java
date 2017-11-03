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

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dascom.OPadmin.action.AdminFormAction;
import com.dascom.OPadmin.util.ExportExcel;
import com.dascom.OPadmin.util.Page;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.dao.zydcUserDao;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.ssmn.util.Constants;
import com.dascom.ssmn.dao.BlackCallNumberDao;
import com.dascom.ssmn.entity.Blackcallnumber;
import com.dascom.ssmn.entity.SsmnZyUser;

public class AddBlackCallNumber extends AdminFormAction{
	private static final Logger logger = Logger.getLogger(AddBlackCallNumber.class);
	private BlackCallNumberDao bDao = BlackCallNumberDao.getInstance();
		
	@Override
	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		TyadminOperators opera = (TyadminOperators)req.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		String msisdn = req.getParameter("msisdn");
	
		String temp = req.getParameter("page");
		Page page = sysUtil.setPageInfo(req, temp, Constants.RECORD_PER_PAGE);
		String method = req.getParameter("method");
		int recordCount =0;
		String sblackn =req.getParameter("blackNum");
		String startdate =req.getParameter("startdate");
		String enddate = req.getParameter("enddate");
	
		if(method !=null && method.equals("bindBatchDeal"))//批量处理
		{
			String scdrfilepath = req.getSession().getServletContext().getRealPath("batchDealBlackCallNumbers");//批量加入黑名单　导出文件存储
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
			List<Blackcallnumber> ls = null;
			try{
				ls = parseExcel(file,req,res);
			}catch(Exception e){
				e.printStackTrace();
				//msg = e.getMessage();
			}            
				            
          //处理
			if(ls != null && ls.size() >0)
			{
				List<Blackcallnumber> sucessList = new ArrayList<Blackcallnumber>();
				//批量添加黑名单
				Iterator it = ls.iterator();
				while(it.hasNext())
				{
					Blackcallnumber nu = (Blackcallnumber) it.next();
					
					String snum = nu.getBlackNumber();
					//判断主号码是否为空
					if(snum !=null && !snum.equals(""))
					{
						//判断号码是否为数字
						Pattern pattern = Pattern.compile("[0-9]*");
			            Matcher isNum = pattern.matcher(snum);
			            if(isNum.matches())
			            {
			                //合法的号码
			            	//先查一下是否存在
							List<Blackcallnumber> list = bDao.getBlackCallNumberList(snum);
							if(list ==null || list.size() <=0)
							{
								Boolean isok = bDao.addBlackCallNumberInfo(snum);
								if(isok)
								{
									sucessList.add(nu);
									it.remove();
								}
								else
								{
									nu.setDescription("添加黑名单失败!");
								}
							}else
								nu.setDescription("此号码已在黑名单中!");
			            	
			            }else
			            	nu.setDescription("此号码不合法!");//暂用Description字段记录错误信息
						
					}else
						nu.setDescription("号码为空!");//暂用Description字段记录错误信息
				}
				
				//将两个数组生成excel导出
				String filenamebatchcan = createExcelDealbatch(sucessList,ls,scdrfilepath);
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
				}
			}else
			{
				req.setAttribute("msg", "导入的文件没有数据，或者数据格式有误!");
			}
			
		}
		//单个处理
		if(method !=null && method.equals("add"))
		{
			if(msisdn != null && !"".equals(msisdn) )
			{
				String errtext = "";//输入框中号码添加错误说明，使用#分隔开返回给页面

				//msisdn是多个号码用;分开的
				String str[] = msisdn.split(";");
				String sError = "";
				Pattern p = Pattern.compile("^[0-9]*$");

				if(str !=null && str.length>0 && str.length<=10 )
				{
					for(int j=0; j<str.length;j++)
					{
						String smsisdn =str[j];
						if(p.matcher(smsisdn).matches())
						{
							//先查一下是否存在
							List<Blackcallnumber> list = bDao.getBlackCallNumberList(smsisdn);
							if(list ==null || list.size() <=0)
							{
								Boolean isok = bDao.addBlackCallNumberInfo(smsisdn);
								if(!isok)
									errtext +=smsisdn+":长度过长!#";
							}else
								errtext +=smsisdn+":此号码已经在黑名单中!#";	
						}else
							errtext +=smsisdn+":函有非数字字符!#";	
					}
					if(errtext.length()>0)
					{
						req.setAttribute("msg","");//这种情况返回错误列表
						req.setAttribute("errtext", "添加失败的号码有：#"+errtext);
					}
					else
						req.setAttribute("msg", "添加黑名单成功!");
				}else
				{
					if(str.length >10)
						req.setAttribute("msg", "输入的号码已超过10个，建议使用批量添加!");
					else
						req.setAttribute("msg", "没有使用分号隔开号码或函有非数字字符!");
					
				}
			}
			
		    req.setAttribute("msisdn", msisdn);
		}
		if(method !=null && method.equals("del")) 
		{
			String delnum =req.getParameter("delnum");
			if(delnum !=null && delnum.length()>0)
			{
				//先查一下是否存在
				List<Blackcallnumber> list = bDao.getBlackCallNumberList(delnum);
				if(list ==null || list.size() <=0)
				{
					req.setAttribute("msg", "该号码不存在!");
					
				}else
				{
					Boolean isok = bDao.delBlackCallNumber(list.get(0));
					if(isok)
						req.setAttribute("msg", "删除成功!");
					else
						req.setAttribute("msg", "删除失败!");
				}
			}
		}
		
		//查询黑名单
		List<Blackcallnumber> list = bDao.getBlackNumList(sblackn,startdate,enddate,page.getFirstResult()-1,page.getSize());
		recordCount = bDao.getBlackNumListCount(sblackn,startdate,enddate);
		
		req.setAttribute("list", list);
		req.setAttribute("recordCount", recordCount);
		req.setAttribute("blackNum", sblackn);
		req.setAttribute("startdate", startdate);
		req.setAttribute("enddate", enddate);
		
		return mapping.findForward(super.formView);
	}
	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {

		return this.processSubmit(mapping, form, req, res);
	}
	
public String createExcelDealbatch(List<Blackcallnumber> listSucess,List<Blackcallnumber> listFaild,String filepath)
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
	String fname = "批量添加结果_"+forma.format(new Date())+".xls";
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
		  		
		     HSSFCell cell1sms0 = rowtitle0.createCell(0);
			 cell1sms0.setCellValue("黑名单");
			 cell1sms0.setCellStyle(cellStyleFont);
		     
			for(int i=0;i<listSucess.size();i++){
				Blackcallnumber info = listSucess.get(i);
				HSSFRow rowcdrnum = sheetoutputFirst.createRow(i+1);
				HSSFCell cell0 = rowcdrnum.createCell(0);
				cell0.setCellValue(info.getBlackNumber());
				cell0.setCellStyle(cellStylecontent);
			}
			
			//失败的sheet
			firstcount = 0;
			 HSSFSheet sheetoutputfaild = wboutput.createSheet("失败记录");
			 HSSFRow rowtitle = sheetoutputfaild.createRow(0);
			 firstcount++;
			 
			 HSSFCell cell1smsf = rowtitle.createCell(0);
			 cell1smsf.setCellValue("黑名单");
			 cell1smsf.setCellStyle(cellStyleFont);
			 
			 HSSFCell cell1smsft = rowtitle.createCell(1);
			 cell1smsft.setCellValue("结果");
			 cell1smsft.setCellStyle(cellStyleFont);
				 
			 for(int i=0;i<listFaild.size();i++)
			 {
				 Blackcallnumber info = listFaild.get(i);
				 HSSFRow rowcdrnum = sheetoutputfaild.createRow(i+1);
				 HSSFCell cell0 = rowcdrnum.createCell(0);
				 cell0.setCellValue(info.getBlackNumber());
				 cell0.setCellStyle(cellStylecontent);
				 
				 HSSFCell cellt = rowcdrnum.createCell(1);
				 cellt.setCellValue(info.getDescription());
				 cellt.setCellStyle(cellStylecontent);
			}
			
		wboutput.write(fileoutput);
		fileoutput.close();
		res = fname;
		 
	} catch (IOException e) {
		e.printStackTrace();
		logger.error("----批量添加黑名单发生异常-----",e);
		
	}
	}
	
	return res;
	
}
	
/*
 * 处理上传的文件
 * 
 **/
//适用于2003的excel 
public List<Blackcallnumber> parseExcel(File file,HttpServletRequest req,HttpServletResponse res)
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
		
		//if(!("号码").equals(sheet.getCell(0, 0).getContents())) 
				//return null;
				
		List<Blackcallnumber> ulist = new ArrayList<Blackcallnumber>();
		
		int rownum = sheet.getRows();
		System.out.println(rownum);
		String matchMsisdn = "^(13|15|18)\\d{9}$";
		String matchAge = "^[0-9]*";
		String temptext = null;
		
		List<Integer> fal=new ArrayList<Integer>();  
		
		for(int i = 1; i < rownum; i ++) 
		{
			Blackcallnumber num = new Blackcallnumber();
			Cell cell = sheet.getCell(0,i);	
			//只有一列 号码	
			String sxcmsisdn = cell.getContents().trim();//主号码
			num.setBlackNumber(sxcmsisdn);			
	
			if( (sxcmsisdn == null || "".equals(sxcmsisdn)))
				break;
			ulist.add(num);
		}

		return ulist;
	} catch (BiffException e) {
		e.printStackTrace();
	}
	
	return null;
}

}
