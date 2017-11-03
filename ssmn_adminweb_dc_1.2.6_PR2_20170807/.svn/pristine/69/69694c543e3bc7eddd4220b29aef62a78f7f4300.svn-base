package com.dascom.ssmn.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.dascom.init.ConfigMgr;
import com.dascom.ssmn.util.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.IAdminAuthority;
import com.dascom.OPadmin.dao.impl.AdminAuthorityImpl;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.service.impl.OperatorLogServImpl;
import com.dascom.OPadmin.util.ExportExcel;
import com.dascom.OPadmin.util.Page;
import com.dascom.ssmn.dao.CDRDao;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.dao.zydcUserDao;
import com.dascom.ssmn.entity.SsmnRecord;
import com.dascom.ssmn.entity.SsmnZyChannel;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.ZydcRecord;
import com.dascom.ssmn.dao.ChannelDao;


public class QuerySSMNInfo extends AdminFormAction{
	private static final Logger logger = Logger.getLogger(QuerySSMNInfo.class);
	private CDRDao cDao = CDRDao.getInstance();
	private LevelDao leveldao = LevelDao.getInstance();
	private zydcUserDao uDao = zydcUserDao.getInstance();
	private IOperatorLogService logServ = new OperatorLogServImpl();
	private IAdminAuthority authDao = new AdminAuthorityImpl();
	
	@Override
	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		TyadminOperators opera = (TyadminOperators)req.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		//权限不能从session中取，因为权限功能，随时要改变（在权限管理中）
		List<String> authMethodList = authDao.findByGroupId(opera.getGroupId().longValue(),(String)req.getSession().getAttribute(Constants.SERVICEKEY_IN_SESSION));
		UtilFunctionDao.showType(authMethodList);
		String saveFilePath = req.getSession().getServletContext().getRealPath("recordFiles");
		String ssmncdrurl = ConfigMgr.getCdrurl();
		SsmnZyLevel level = null;
		String bdparam = req.getParameter("bdbox1");//事业部
		String zoneparam = req.getParameter("zonebox1");//战区
		String areaparam = req.getParameter("areabox1");//片区
		String bagparam = req.getParameter("bagbox1");//行动组
		String channelid = req.getParameter("channelbox");
		
		List<String> bdlist = new ArrayList<String>();
		List<String> wzlist = new ArrayList<String>();
		List<String> arealist = new ArrayList<String>();
		List<String> baglist = new ArrayList<String>();
		String opeBd = null;
		String opeZone = null;
		String opeArea = null;
		String opeBag = null;
		String slevelid = "0";
		
		try {
			level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
			opeBd = level.getBusinessdepartment();
			opeZone = level.getWarzone();
			opeArea = level.getArea();
			opeBag = level.getBranchactiongroup();
		} catch (DaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//区域级别的名字是配置 的，这里要根据配置显示 
		String businName = new UtilFunctionDao().getLevelName(1, level);//事业部
		String warName = new UtilFunctionDao().getLevelName(2, level);//战区
		String areaName = new UtilFunctionDao().getLevelName(3, level);//片区
		String branName = new UtilFunctionDao().getLevelName(4, level);//行动组
		req.setAttribute("businName", businName);
		req.setAttribute("warName", warName);
		req.setAttribute("areaName", areaName);
		req.setAttribute("branName", branName);
		
		File dir = new File(saveFilePath);
		if (!dir.exists()) {
			if (!dir.mkdirs())
				System.out.println("创建文件失败");
		}
		
		//获取区域列表
		if (opeBd == null) { 
			// 操作员对应省份和公司下的所有事业部都可选
			bdlist = leveldao.getLevelByTypeAndID(1, opera.getLevelid());
		} else { // 只能选择与操作员对应的事业部
			bdlist.add(StringUtils.isBlank(bdparam)?opeBd:bdparam);
		}
		// 根据businessdepartment查出wzlist下级菜单
		if (opeZone == null) {
			wzlist = leveldao.getSelectLevel(1, opera.getLevelid(), StringUtils.isBlank(bdparam)?opeBd:bdparam, null, null);
		} else { 
			wzlist.add(StringUtils.isBlank(zoneparam)?opeZone:zoneparam);
		}
		if (opeArea == null) {
			arealist = leveldao.getSelectLevel(2, opera.getLevelid(), StringUtils.isBlank(bdparam)?opeBd:bdparam, 
					StringUtils.isBlank(zoneparam)?opeZone:zoneparam, null);
		} else {
			arealist.add(StringUtils.isBlank(areaparam)?opeArea:areaparam);
		}
		if (opeBag == null) {
			baglist = leveldao.getSelectLevel(3, opera.getLevelid(), StringUtils.isBlank(bdparam)?opeBd:bdparam, 
					StringUtils.isBlank(zoneparam)?opeZone:zoneparam, StringUtils.isBlank(areaparam)?opeArea:areaparam);
		} else {
			baglist.add(StringUtils.isBlank(bagparam)?opeBag:bagparam);
		}
		
		List<SsmnZyChannel>  channels = uDao.getChannelList(opera.getLevelid(),"",Constants.TYPE_SHOWDATE);//获取渠道
		slevelid = cDao.getlevelid(level);
		
		String stype = req.getParameter("type");
		if(stype !=null && !"".equals(stype) && stype.equals("6"))//添加备注
		{
			String streamtemp =req.getParameter("streamnumRe");
			String remarkstr =req.getParameter("remark");
			if(remarkstr !=null && remarkstr.length()>0)
			{
				//添加备注
				boolean re=cDao.updateCdrRemark(streamtemp,remarkstr);
				if(re)
				{
					//写tyadmin_logs表
					logServ.log(opera,Constants.LOG_TYPE_OPERATOR_REMARK, "操作员" + opera.getId().getOpeno() + "添加备注一次_"+streamtemp);	
					req.setAttribute("msg", "添加成功!");
				}
				else
					req.setAttribute("msg", "添加失败!");
			}
		}
		
		//if(stype != null && !"".equals(stype) && (stype.equals("1") || stype.equals("3") || stype.equals("5")))//1:查询语音 3:导出数据 5:全部下载语音文件
		//{
			String startdate=req.getParameter("startdate");
			String starttime = req.getParameter("starttime");
			String enddate=req.getParameter("enddate");
			String endtime=req.getParameter("endtime");
			String msisdn = req.getParameter("msisdn");
			String sname = req.getParameter("name");
			String sStreamNum = req.getParameter("streamnum");
			String ssmnnum = req.getParameter("ssmnnum");
			String sclientnumber = req.getParameter("clientnumber");
			String strcalltype = req.getParameter("calltype");//呼入/呼出
			String sCallReMis = req.getParameter("callReMis");//已接/未接
			String temp = req.getParameter("page");
			Page p = sysUtil.setPageInfo(req, temp, Constants.RECORD_PER_PAGE);
			List<ZydcRecord> clist = null;
			List<ZydcRecord> channelcount = null;
			
			int num = 0;
			String channelcounts[][] = null;//记录呼入
			String channelOutCounts[][] = null;//记录呼出
				
		//起始时间和终止时间是必选项
		if(startdate != null && !"".equals(startdate) && enddate != null && !"".equals(enddate)) 
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
			
			//先取出分页后的主键streamnumber(为了提高速度)
			clist = cDao.getCdrStatRecordList(msisdn,sname,sStreamNum,startTimeLong,endTimeLong
					,sclientnumber,bdparam,zoneparam,areaparam,bagparam,channelid,ssmnnum,
					strcalltype,sCallReMis,p.getFirstResult()-1, p.getSize(),opera);
			
			//查询用户数量
			num= cDao.getCdrStatRecordCount(msisdn,sname,sStreamNum,opera,startTimeLong,endTimeLong
					,sclientnumber,bdparam,zoneparam,areaparam,bagparam,channelid,ssmnnum,
					strcalltype,sCallReMis);
			
			//-----------------组装数据---------------
			//填详细数据
			if(authMethodList !=null && authMethodList.contains("hideOwnerNumber") )
				Constants.SHOW_CLIENTNUM_OP =1;
			else
				Constants.SHOW_CLIENTNUM_OP =0;
			
			if(clist !=null && clist.size()>0)
			{
				for(int i=0;i<clist.size();i++)
				{
					ZydcRecord z = cDao.getCdrStatRecordByStreamnumber(clist.get(i).getStreamNumber());
					if(z !=null)
					{
						clist.get(i).setUsername(z.getUsername());
						clist.get(i).setEmpno(z.getEmpno());
						clist.get(i).setBranchactiongroup(z.getBranchactiongroup());
						clist.get(i).setMsisdn(z.getMsisdn());
						clist.get(i).setSsmnnumber(z.getSsmnnumber());
						clist.get(i).setClientnumber(z.getClientnumber());
						if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))//添加第一联系人
							clist.get(i).setFirstMsisdn(z.getFirstMsisdn());
						clist.get(i).setRemark(z.getRemark());
						
						
						clist.get(i).setChannelname(UtilFunctionDao.splitStr(z.getChannelname(),"_"));
						clist.get(i).setCalltype(new UtilFunctionDao().getDcCallType(z.getDccalltype().intValue()+""));
						clist.get(i).setCallstarttime(z.getCallstarttime());
						clist.get(i).setStrCallDuration(UtilFunctionDao.getsecondSimple(z.getCallduration()));
						
						//获取录音文件名
						if(null != z.getFileName()  && z.getFileName().length() >0)
						{
							clist.get(i).setFileName(z.getFileName());
							String sfilePath = z.getFilepath();
							//如果filepath是一个url，不用截取，直接写
							int indexfind =sfilePath.indexOf("http");
							if(indexfind >=0)
							{
								clist.get(i).setFilepath(sfilePath);
								clist.get(i).setFilepathOnline(sfilePath);								
							}else
							{
								int filenameIndex = sfilePath.lastIndexOf("/");
								if(filenameIndex >0   )
								{
									if(filenameIndex >8)
									{
										//录音格式： /home/pengjh/VoiceMix/out/20151215/13602098964_1255016_20151215122009.wav
										String sfilename = sfilePath.substring(filenameIndex-8);
										String sfilep =  Constants.AUDIO_PATH+sfilename;
										clist.get(i).setFilepath(sfilep);
										clist.get(i).setFilepathOnline(ssmncdrurl+"/"+sfilename);
									}else
									{
										//录音别家提供一个链接：/5e/A020F8FEC8EC3883B18E60C63679K9U2@fy.mp3
										clist.get(i).setFilepath(ConfigMgr.getCdrurlfy()+"/"+z.getFilepath());
										clist.get(i).setFilepathOnline(ConfigMgr.getCdrurlfy()+"/"+z.getFilepath());
									}
								}
							}
						}else
							clist.get(i).setFilepathOnline("");//文件不存在
						
						//查找是否听过录音
						boolean isread =logServ.isReadCdr("听录音一次。_"+clist.get(i).getStreamNumber(), opera.getId().getOpeno());
						if(isread)
							clist.get(i).setIsRead(1);
						else
							clist.get(i).setIsRead(0);
						
						//查询备注次数
						//boolean isremark =false; //logServ.isReadCdr("添加备注一次_"+clist.get(i).getStreamNumber(), opera.getId().getOpeno());
						if(z.getRemark() !=null && z.getRemark().length()>0)
							clist.get(i).setRemarkCount(1);
						else
							clist.get(i).setRemarkCount(0);
					}
				}
								
				//------------------呼入/呼出统计--------------------
				List<ZydcRecord> channelcountYijie = null;
				//呼入
				channelcount = cDao.getCdrChannelCountList(msisdn,sname,sStreamNum,startTimeLong,endTimeLong
						,sclientnumber,bdparam,zoneparam,areaparam,bagparam,channelid,
						ssmnnum,"1",sCallReMis,opera.getLevelid(),"",Constants.TYPE_SHOWDATE);
				channelcountYijie = cDao.getCdrChannelCountList(msisdn,sname,sStreamNum,startTimeLong,endTimeLong
						,sclientnumber,bdparam,zoneparam,areaparam,bagparam,
						channelid,ssmnnum,"1",sCallReMis,opera.getLevelid(),"  and z.callduration >0  ",Constants.TYPE_SHOWDATE);
								
				//填充数据channelcounts
				if( channelcount !=null && channelcount.size()>0)//呼入
				{
					channelcounts = new String[4][channelcount.size()+1];
					channelcounts[0][0] = "渠道";
					channelcounts[1][0] = "数量";
					channelcounts[2][0] = "已接通";
					channelcounts[3][0] = "未接通";
					for(int i=0;i<channelcount.size();i++)
					{
						ZydcRecord zy = channelcount.get(i);
						
						channelcounts[0][i+1] = UtilFunctionDao.splitStr(zy.getChannelname(),"_");
						channelcounts[1][i+1] = zy.getChannelCount()+"";
	
						int nocalcount = 0;//记录已接数量
						for(int j=0; j<channelcountYijie.size();j++)
						{
							ZydcRecord zyjie = channelcountYijie.get(j);						
	
							if(zy.getChannelname().equals(zyjie.getChannelname()))
							{
								nocalcount = zyjie.getChannelCount();//有已接
								break;
							} 							 
						}
						
						channelcounts[2][i+1] = nocalcount+"";
						channelcounts[3][i+1] = (zy.getChannelCount()-nocalcount)+"";
					}
				}
				//双呼情况
				//呼出,dc_calltype为 3,4,5（不为3,4,5的，空值或其他都为4 app呼出）
				//3,PC呼出
				//* 4:APP呼出
				// * 5: 直接呼出
				//不双呼情况: 由calltype值判断
				channelcount = cDao.getCdrChannelCountList(msisdn,sname,sStreamNum,startTimeLong,endTimeLong
						,sclientnumber,bdparam,zoneparam,areaparam,bagparam,channelid,
						ssmnnum,"2",sCallReMis,opera.getLevelid(),"",Constants.TYPE_SHOWDATE);
				channelcountYijie = cDao.getCdrChannelCountList(msisdn,sname,sStreamNum,startTimeLong,endTimeLong
						,sclientnumber,bdparam,zoneparam,areaparam,bagparam,channelid,
						ssmnnum,"2",sCallReMis,opera.getLevelid(),"  and z.callduration >0  ",Constants.TYPE_SHOWDATE);
				
				if( channelcount !=null && channelcount.size()>0)//呼出
				{
					channelOutCounts = new String[4][channelcount.size()+1];
					channelOutCounts[0][0] = "类别";
					channelOutCounts[1][0] = "数量";
					channelOutCounts[2][0] = "已接通";
					channelOutCounts[3][0] = "未接通";
					
					for(int i=0; i<channelcount.size();i++)
					{
						ZydcRecord zy = channelcount.get(i);
						//初始化类别、数量两列
						channelOutCounts[0][i+1] = UtilFunctionDao.splitStr(zy.getChannelname(),"_");
						channelOutCounts[1][i+1] = zy.getChannelCount()+"";
						
						int nocalcount = 0;//记录已接数量
						for(int j=0; j<channelcountYijie.size();j++)
						{
							ZydcRecord zyjie = channelcountYijie.get(j);						
	
							if(zy.getChannelname().equals(zyjie.getChannelname()))
							{
								nocalcount = zyjie.getChannelCount();//有已接
								break;
							} 							 
						}
						
						channelOutCounts[2][i+1] = nocalcount+"";
						channelOutCounts[3][i+1] = (zy.getChannelCount()-nocalcount)+"";
					}
					
				}	
			
		}
	}
		    req.setAttribute("clist",clist);
		    req.setAttribute("msisdn", msisdn);
		    req.setAttribute("streamnum", sStreamNum);
		    req.setAttribute("ssmnnum", ssmnnum);
		    req.setAttribute("startdate", startdate);
		    req.setAttribute("starttime", starttime);
		    req.setAttribute("enddate", enddate);
		    req.setAttribute("endtime", endtime);
		    req.setAttribute("recordCount", num);
		    req.setAttribute("clientnumber", sclientnumber);
		    req.setAttribute("channelcount", channelcounts);
		    req.setAttribute("channeloutcount", channelOutCounts);
		    req.setAttribute("bdparam", StringUtils.isBlank(bdparam)?opeBd:bdparam);
			req.setAttribute("zoneparam", StringUtils.isBlank(zoneparam)?opeZone:zoneparam);
			req.setAttribute("areaparam", StringUtils.isBlank(areaparam)?opeArea:areaparam);
			req.setAttribute("bagparam", StringUtils.isBlank(bagparam)?opeBag:bagparam);
			req.setAttribute("bdlist", bdlist);
			req.setAttribute("wzlist", wzlist);
			req.setAttribute("arealist", arealist);
			req.setAttribute("baglist", baglist);
			req.setAttribute("channels", channels);
			req.setAttribute("channelid", channelid);
			req.setAttribute("slevelid", slevelid);//
			req.setAttribute("calltype", strcalltype);
			req.setAttribute("callReMis", sCallReMis);
			req.setAttribute("name", sname);
			req.setAttribute("isSecondMsisdn", ConfigMgr.getIsAddSecondMsisdn());

			if(channelcounts !=null && channelcounts.length >0)
				req.setAttribute("channelcountNumin", channelcounts[0].length+1);
			if(channelOutCounts !=null && channelOutCounts.length >0)
				req.setAttribute("channelcountNumout", channelOutCounts[0].length);
		    if(channelcounts != null && channelcounts.length >0)
		    	req.setAttribute("numchannelcounts", "1");
		    else
		    	req.setAttribute("numchannelcounts", "0");
		    if(channelOutCounts !=null && channelOutCounts.length >0)
		    	req.setAttribute("numchannelOutCounts", "1");
		    else
		    	req.setAttribute("numchannelOutCounts", "0");
		    
		    req.setAttribute("authMethodList", authMethodList);
		    req.setAttribute("isAddRemark",ConfigMgr.getAddRemark());
		    req.setAttribute("pagenumber", temp);
			//return mapping.findForward("formCDRView");
	//}
		
		return mapping.findForward("formCDRView");
}
	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {

		return this.processSubmit(mapping, form, req, res);
	}
	
public  String downLoadFile(List<String> downloadURLs,String savePath) throws IOException{
	int bytesum = 0;
	       int byteread = 0;
	       Date date=new Date();
	       
	SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
	String dateFloder= sf1.format(date);
	       
	   InputStream inStream=null;
	 
	   String sfilepath = savePath+File.separator+"recordZip"+dateFloder+".zip";

	   ZipOutputStream out = new ZipOutputStream(new FileOutputStream(sfilepath)); 
	try {
		for(int i = 0;i<downloadURLs.size(); i++)
		{
			String surl = downloadURLs.get(i);
			if(surl!= null && !"".equals(surl) && surl.length() >0)
			{
				int filenameIndex = surl.lastIndexOf("/");
				String sfilename = surl.substring(filenameIndex+1);
				 out.putNextEntry(new ZipEntry(sfilename));   
				URL url = new URL(surl);
				URLConnection conn = (URLConnection) url.openConnection();
				inStream = conn.getInputStream();
				
				byte[] buffer = new byte[4028];
				while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				out.write(buffer, 0, byteread);
				}
		          
		        out.closeEntry();   
		          
		        inStream.close();   
		}
	
		}
		
		   out.close();   
		   
	     System.out.println("生成.zip成功");  
	     
	} finally{
		try {
		if(out != null)
			out.close();
			
		if(inStream!=null){
		inStream.close();
		}
		} catch (IOException e) {
		inStream=null;
		}
	}
	
	return sfilepath;
		     
	}

	public boolean dowloadCDRFiles(String sfilePath,String saveFilePath,String sfilename)
	{
		boolean isok = false;
		//判断文件是否存在
		File file=new File(saveFilePath+"/"+sfilename);    
		if(file.exists())    
		{    
			isok =true;
		}    
		BufferedInputStream bis = null;
	    BufferedOutputStream bos = null;
        try {
        	//先判断要读取的文件是否存在
        	File fileout=new File(sfilePath);
        	if(!fileout.exists())//文件不存在，直接返回
        		return  isok=false;
            byte[] bytes = new byte[1024];
            bis = new BufferedInputStream(new FileInputStream(sfilePath));//BufferedInputStream会构造一个背部缓冲区数组，将FileInputStream中的数据存放在缓冲区中，提升了读取的性能
            bos = new BufferedOutputStream(new FileOutputStream(saveFilePath+"/"+sfilename));//同理
            int length = bis.read(bytes);
            while (length != -1) {
               // System.out.println("length: " + length);
                bos.write(bytes, 0, length);
                length = bis.read(bytes);
            }
            isok = true;
            //System.out.print("aaaa");
        } catch (Exception e) {
            e.printStackTrace();
            isok= false;
        } finally {
            try {
            	if(bis != null)
            		bis.close();
            	if(bos != null)
            		bos.close();
                return isok;
            } catch (IOException ex) {
                ex.printStackTrace();
                return isok;
            }
        }
}
	

	


}
