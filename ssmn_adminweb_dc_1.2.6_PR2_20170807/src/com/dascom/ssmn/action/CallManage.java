package com.dascom.ssmn.action;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dascom.OPadmin.action.AdminFormAction;
import com.dascom.OPadmin.util.Page;
import com.dascom.ssmn.entity.SsmnZyCdr;
import com.dascom.ssmn.entity.SsmnZyClientnum;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.ssmn.util.Constants;
import com.dascom.ssmn.dao.CDRDao;
import com.dascom.ssmn.dao.CallDao;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.dao.zydcUserDao;

public class CallManage extends AdminFormAction{
	private static final Logger logger = Logger.getLogger(CallManage.class);
	private  CallDao cdao =CallDao.getInstance();
	private zydcUserDao udao =zydcUserDao.getInstance();
	private CDRDao cdrdao =CDRDao.getInstance();
	
	@Override
	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		TyadminOperators opera = (TyadminOperators)req.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		
		Long operalevelid = opera.getLevelid();
		int recordCount = 0;
		SsmnZyLevel zylevel = null;
		String startdate =req.getParameter("startdate");
		String temp = req.getParameter("page");
		String method =req.getParameter("method");
		String cid=req.getParameter("cid");
		Page p = sysUtil.setPageInfo(req, temp, Constants.RECORD_PER_PAGE);
		
		try {
			zylevel = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(operalevelid);
		} catch (DaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(method !=null && !"".equals(method) && method.length()>0 && method.equals("edit"))//编辑
		{
			String sumsisdn=req.getParameter("umsisdn");
			String suname=req.getParameter("uname");
			String scnum=req.getParameter("cnum");
			String scname=req.getParameter("cname");
			String sumsisdnOld =req.getParameter("usermsisdnOld");
			//String sunameOld=req.getParameter("usernameOld");

			if(cid !=null && cid.length()>0)
			{
				boolean isok =false;
				//判断是否修改业务员
				if(sumsisdn !=null && sumsisdn.length()>0 
						&& sumsisdnOld !=null && sumsisdnOld.length()>0 && sumsisdn.equals(sumsisdnOld))
				{
					//没有修改业务员电话，status 为 3，修改
					 isok=cdao.editCall(cid, sumsisdn, suname, scnum, scname,"");
					
				}else
				{
					//修改了业务员电话，需要将该记录删除status=2,再新创建 个 status=1
					isok =cdao.switchToNewUser(cid, sumsisdn, suname, scnum, scname,"");
				}
				if(isok)
					req.setAttribute("msg", "修改成功");
				else
					req.setAttribute("msg", "修改失败");
				
			}else
				req.setAttribute("msg", "序号不能为空!");
		}
		if(method !=null && !"".equals(method) && method.length()>0 && method.equals("del"))//删除
		{
			if(cid !=null && cid.length()>0)
			{
				//这里假删除，只是将status设置成2
				boolean isok =cdao.delCall(cid);
				if(isok)
					req.setAttribute("msg", "删除成功");
				else
					req.setAttribute("msg", "删除失败");
			}else
				req.setAttribute("msg", "序号不能为空!");
		}
		if(method !=null && !"".equals(method) && method.length()>0 && method.equals("addRemark"))//添加备注
		{
			String sremark =req.getParameter("remark");
			if(cid !=null && cid.length()>0)
			{
				boolean isok=cdao.editCall(cid, "", "", "", "",sremark);
				if(isok)
					req.setAttribute("msg", "添加成功");
				else
					req.setAttribute("msg", "添加失败");
			}else
				req.setAttribute("msg", "序号不能为空!");
		}		
		if(method !=null && !"".equals(method) && method.length()>0 && method.equals("importBatch"))//导入
		{
			callDeal(req,res);
		}
		
		List<SsmnZyClientnum> list = cdao.getClientNumAll(zylevel,startdate,p.getFirstResult()-1,p.getSize());
		recordCount = cdao.getClientNumAllCount(zylevel,startdate);
		//查询呼叫状态,去ssmn_zy_cdr表中查
		if(list !=null && list.size()>0)
		{
			for(int i=0;i<list.size();i++)
			{
				SsmnZyClientnum zc =list.get(i);
				if(zc !=null && zc.getClientnum().length()>0)
				{
					//去ssmn_zy_cdr中查，如果有记录，则说明有呼叫，否则没有呼叫
					List<SsmnZyCdr> cl =cdrdao.getCdrByMsisdn(zc.getUsermsisdn(),zc.getClientnum(),zc.getCalltime());
					if(cl !=null && cl.size()>0)
						list.get(i).setCallstatus(true);
					else
						list.get(i).setCallstatus(false);
				}
			}
		}
	    req.setAttribute("page", p);
	    req.setAttribute("recordCount", recordCount);
	    req.setAttribute("llist", list);
	    req.setAttribute("startdate", startdate);
	    	   
		return mapping.findForward(super.formView);
	}
	
	
	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {

		return this.processSubmit(mapping, form, req, res);
	}
	
	public void callDeal(HttpServletRequest req,HttpServletResponse res)
	{
		String scdrfilepath = req.getSession().getServletContext().getRealPath("callResult");//导出文件存储
		SimpleDateFormat forma = new SimpleDateFormat("yyyyMMddHHmmss");
		List<SsmnZyClientnum> faildList = new ArrayList<SsmnZyClientnum>();
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
			logger.error("-----网络问题，导入文件失败-----");
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
		List<SsmnZyClientnum> ls = new ArrayList<SsmnZyClientnum>();
		try{
			ls = parseExcel(file,req,res);
		}catch(Exception e){
			e.printStackTrace();
			//msg = e.getMessage();
		}            
        
		 //处理
		if(ls != null && ls.size() >0)
		{
			//批量绑定
			Iterator<SsmnZyClientnum> it = ls.iterator();
			while(it.hasNext())
			{
				SsmnZyClientnum zc =(SsmnZyClientnum)it.next();
				//判断日期
				if(zc.getCalltime() ==null )
				{
					zc.setFailedInfo("日期不能为空!");
					faildList.add(zc);
					it.remove();
					continue;
				}
				//业务员电话
				if(zc.getUsermsisdn() ==null || "".equals(zc.getUsermsisdn()))
				{
					zc.setFailedInfo("业务员电话不能为空!");
					faildList.add(zc);
					it.remove();
					continue;
				}
				//业务员姓名
				if(zc.getUsername() ==null || "".equals(zc.getUsername()))
				{
					zc.setFailedInfo("业务员姓名不能为空!");
					faildList.add(zc);
					it.remove();
					continue;
				}
				//客户电话
				if(zc.getClientnum() ==null || "".equals(zc.getClientnum()))
				{
					zc.setFailedInfo("客户电话不能为空!");
					faildList.add(zc);
					it.remove();
					continue;
				}
				//客户姓名，可以为空
				//判断业务员主号和姓名，是否在ssmn_zy_user表中存在
				List<SsmnZyUser> lus =udao.getssmnzyuserbyMsisdn(zc.getUsermsisdn());
				if(lus ==null || lus.size() <=0)
				{
					zc.setFailedInfo("该业务员不存在!");
					faildList.add(zc);
					it.remove();
					continue;
				}
				if(!lus.get(0).getName().equals(zc.getUsername()))
				{
					zc.setFailedInfo("该业务员姓名与数据库中存在的业务员姓名不一致!");
					faildList.add(zc);
					it.remove();
					continue;
				}
				//数据无误,开始添加
				try {
					SsmnZyClientnum szc =new SsmnZyClientnum();
					szc.setCalltime(zc.getCalltime());
					szc.setUsermsisdn(zc.getUsermsisdn());
					szc.setUsername(zc.getUsername());
					szc.setClientnum(zc.getClientnum());
					szc.setClientname(zc.getClientname());
					szc.setRemark(zc.getRemark());
					szc.setIntime(new Date());
					szc.setUpdatetime(new Date());
					szc.setStatus(1);//新增
					
					boolean isok =cdao.addCall(szc);
					if(!isok)
					{
						zc.setFailedInfo("添加失败!");
						faildList.add(zc);
						it.remove();
						continue;
					}else
						continue;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
				
			}
			//导出结果
			//将两个数组生成excel导出
			String filenamebatchcan = createExcelDealbatch(ls,faildList,scdrfilepath);
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
	 * 解析导入的文件字段
	 */
	public List<SsmnZyClientnum> parseExcel(File file,HttpServletRequest req,HttpServletResponse res)
	{
		List<SsmnZyClientnum> ls =new ArrayList<SsmnZyClientnum>();
		try { 
			Workbook book=null;
		    try {
				book = Workbook.getWorkbook(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Sheet sheet = book.getSheet(0);
			
			if(!("日期").equals(sheet.getCell(0, 0).getContents())||
					!("业务员电话").equals(sheet.getCell(1, 0).getContents())||
					!("业务员姓名").equals(sheet.getCell(2, 0).getContents())||					
					!("客户电话").equals(sheet.getCell(3, 0).getContents()) ||
					!("客户姓名").equals(sheet.getCell(4, 0).getContents())){
					return null;
				}
			
			int colnum = 5;
			int rownum = sheet.getRows();
			//String matchMsisdn = "^(13|15|18)\\d{9}$";
			//String matchAge = "^[0-9.-]*$";
			//String temptext = null; 
			
			for(int i = 1; i < rownum; i ++) {
				
				Date  calldate=null ;//日期
				String umsisdn ="";//业务员电话
				String uname ="";//业务员姓名
				String cnum ="";//客户电话
				String cname ="";//客户姓名
				
				SsmnZyClientnum us = new SsmnZyClientnum();
				for(int j = 0; j < colnum; j ++)
				{
					Cell cell = sheet.getCell(j,i);	
					
					if(j == 0){ 
						//日期
						if(cell.getType() !=CellType.EMPTY && cell.getType() == CellType.DATE )
						{
							DateCell dcell =(DateCell)cell;
							if(dcell !=null)
							{
								calldate= dcell.getDate();
								us.setCalltime(calldate);
							}
						}						
					}
					else if(j == 1){ 
						//业务员电话
						umsisdn = cell.getContents();
						us.setUsermsisdn(umsisdn);
						
					}else if(j ==2){
						//业务员姓名
						uname = cell.getContents();
						us.setUsername(uname);
					}else if(j ==3)
					{
						//客户电话
						cnum = cell.getContents();
						us.setClientnum(cnum);
					}else if(j ==4)
					{
						//客户姓名
						cname = cell.getContents();
						us.setClientname(cname);
					}
		
				}
				if((calldate ==null )&& (umsisdn == null || "".equals(umsisdn)) 
						&& (uname == null || "".equals(uname)) && 
						(cnum == null || "".equals(cnum)) && (cname == null || "".equals(cname)))
					break;
				ls.add(us);
			}

			return ls;
		} catch (BiffException e) {
			e.printStackTrace();
		}
		
		return null;
 
	}
	
	public String 	createExcelDealbatch(List<SsmnZyClientnum> listSucess,List<SsmnZyClientnum> listFaild,String filepath)
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
		String fname= "客户电话导入结果_"+forma.format(new Date())+".xls";

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

			     new UtilFunctionDao().createImportClientNumTitle(0,rowtitle0, cellStyleFont,true);
			    
				 for(int i=0;i<listSucess.size();i++){
					SsmnZyClientnum info = listSucess.get(i);
					HSSFRow rowcdrnum = sheetoutputFirst.createRow(i+1);
					new UtilFunctionDao().createImportClientNumContent(i+1,rowcdrnum, info, cellStylecontent,true);
				 }
				
				//失败的sheet
				firstcount = 0;
				HSSFSheet sheetoutputfaild = wboutput.createSheet("失败记录");
				HSSFRow rowtitle = sheetoutputfaild.createRow(0);
				firstcount++;
				 
				new UtilFunctionDao().createImportClientNumTitle(0,rowtitle, cellStyleFont,false);
					 
				 for(int i=0;i<listFaild.size();i++)
				 {
					 SsmnZyClientnum info = listFaild.get(i);
					HSSFRow rowcdrnum = sheetoutputfaild.createRow(i+1);
					new UtilFunctionDao().createImportClientNumContent(i+1,rowcdrnum, info, cellStylecontent,false);
				}
				
				wboutput.write(fileoutput);
			    fileoutput.close();
			    res = fname;
			 
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("----导入客户电话发生异常-----",e);
			
		}
	 }
		
	return res;
		
	}
}
