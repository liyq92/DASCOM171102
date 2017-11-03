package com.dascom.ssmn.action;

import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFrame;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.dascom.OPadmin.action.AdminFormAction;
import com.dascom.OPadmin.util.ExportExcel;
import com.dascom.OPadmin.util.Page;
import com.dascom.ssmn.dao.zydcUserDao;
import com.dascom.ssmn.entity.SsmnZyCancelUser;
import com.dascom.ssmn.entity.SsmnZyChannel;
import com.dascom.ssmn.entity.SsmnEnablenumber;
import com.dascom.ssmn.entity.SsmnZyFeedback;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.ssmn.entity.ZydcRecord;
import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.IAdminAuthority;
import com.dascom.OPadmin.dao.IAdminOperator;
import com.dascom.OPadmin.dao.impl.AdminAuthorityImpl;
import com.dascom.OPadmin.dao.impl.AdminOperatorImpl;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.ssmn.util.Constants;
import com.dascom.ssmn.util.PieChart;
import com.dascom.ssmn.util.TimeSeriesChart;
import com.dascom.ssmn.dao.CDRDao;
import com.dascom.ssmn.dao.FeedBackDao;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.dao.ChannelDao;

public class StatChannelCallIn extends AdminFormAction{
	private static final Logger logger = Logger.getLogger(StatChannelCallIn.class);
	private zydcUserDao uDao = zydcUserDao.getInstance();
	private LevelDao leveldao = LevelDao.getInstance();
	private CDRDao cDao = CDRDao.getInstance();	
	private ChannelDao chanDao = ChannelDao.getInstance();
	private IAdminAuthority authDao = new AdminAuthorityImpl();
	
	@Override
	public ActionForward processSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {
		TyadminOperators opera = (TyadminOperators)req.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		List<String> authMethodList = authDao.findByGroupId(opera.getGroupId().longValue(),(String)req.getSession().getAttribute(Constants.SERVICEKEY_IN_SESSION));
		UtilFunctionDao.showType(authMethodList);
		String serverPath = req.getSession().getServletContext().getRealPath("")+File.separator;
		SimpleDateFormat fm = new SimpleDateFormat("yyMMddHHmmss");
		SimpleDateFormat fmDay = new SimpleDateFormat("yyyy-MM-dd");
		String serverUrl = req.getRequestURL().toString();
		int lastIndex = serverUrl.lastIndexOf("/");
		if(lastIndex >0)
			serverUrl = serverUrl.substring(0, lastIndex);
		SsmnZyLevel level = null;
		String bdparam = req.getParameter("bdbox1");//事业部
		String zoneparam = req.getParameter("zonebox1");//战区
		String areaparam = req.getParameter("areabox1");//片区
		String bagparam = req.getParameter("bagbox1");//行动组
		String channelid = req.getParameter("channelbox");
		String startdate=req.getParameter("startdate");
		String enddate=req.getParameter("enddate");
		String sDateSelect = req.getParameter("dateSelect");
		String startdateMonth = req.getParameter("startdateMonth");
		String enddateMonth = req.getParameter("enddateMonth");
		if(startdateMonth !=null && startdateMonth.length()>0)
			startdate= startdateMonth;
		if(enddateMonth !=null && enddateMonth.length()>0)
			enddate = enddateMonth;
		List<ZydcRecord> channelcount = null;
		String channelcounts[][] = null;//记录呼入
		String channelOutCounts[][] = null;//记录呼出
		
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
		
		if((startdate != null && !"".equals(startdate) && enddate != null && !"".equals(enddate)) ||
			(startdateMonth != null && !"".equals(startdateMonth) && 
			 enddateMonth != null && !"".equals(enddateMonth)) )
		{
			long startTimeLong =0;
			long endTimeLong = 0;
			//根据输入的日期,查出数值型的
			//获取开始时间
			if(startdate !=null && startdate.length()<8)//说明是月份,需要转化为天
				startdate = new UtilFunctionDao().getFirstLastDay(startdate, 0,0);//获取第一天
				
			if(enddate !=null && enddate.length() <8)
				enddate =  new UtilFunctionDao().getFirstLastDay(enddate, 1,0);//获取最后一天
				
			if(startdate !=null && startdate.length()>0)
				startTimeLong =cDao.getDateLong(startdate,"",0);

			//获取结束时间,如果小时,小时+1/24, 如果天,天+1,减100(包括当天,当时的),如果结束时间为空,取当前日期
			if(enddate !=null && enddate.length()>0)
				endTimeLong =cDao.getDateLong(enddate,"",1);
			else
				endTimeLong =cDao.getDateLong("sysdate","",1);
			
			channelcount = cDao.getCdrChannelCountList("","","",startTimeLong,endTimeLong,"",bdparam,zoneparam,
					areaparam,bagparam,channelid,"","1","",opera.getLevelid(),"",Constants.TYPE_SHOWDATE);
			if(channelcount != null && channelcount.size()>0)
			{
			List<ZydcRecord> channelcountYijie = null;
			//呼入
			channelcountYijie = cDao.getCdrChannelCountList("","","",startTimeLong,endTimeLong,"",bdparam,
					zoneparam,areaparam,bagparam,channelid,"","1","",opera.getLevelid(),
					"  and z.callduration >0  ",Constants.TYPE_SHOWDATE);
			
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
//------------------------------------------------按天统计折线图----------------------------------------------
			if(sDateSelect !=null && sDateSelect.equals("1"))
			{
				//查最大的时间和最小的时间
				List<String> MaxMinDate= cDao.getCdrChannelMaxMinDate("","","",startTimeLong,endTimeLong,
						"",bdparam,zoneparam,areaparam,bagparam,channelid,"","1",opera,"",1);
				if(MaxMinDate !=null && MaxMinDate.size()>0)
				{
					//组织数据：Map<渠道名,Map<按天或月查,数值>> (Map<String,Map<String,Integer>>)
					Map<String,Map<String,Integer>> mapData = new LinkedHashMap<String,Map<String,Integer>>();
					for(int i=0;i<channelcount.size();i++)
					{
						ZydcRecord zy = channelcount.get(i);
						String chName = zy.getChannelname();//取到渠道名
						if(chName !=null && chName.length()>0)
						{
							//里层的map
							Map<String,Integer> mapCount = new LinkedHashMap<String,Integer>();
							SsmnZyChannel sChanne = chanDao.getChannelByName(chName);
							if(sChanne !=null)
							{
								//计算从最小的一天到最大的一天
								String strMinDate = MaxMinDate.get(0);
								String strMaxDate = MaxMinDate.get(MaxMinDate.size()-1);
								while(strMinDate.compareTo(strMaxDate)<=0)
								{								
									if(strMinDate !=null && strMinDate.length()>0)
									{
										//先去MaxMinDate中查找，如果有值，去查，否则写0
										int iCount = 0;
										if(MaxMinDate.indexOf(strMinDate) >=0)
										{
											startTimeLong =cDao.getDateLong(strMinDate,"",0);
											endTimeLong =cDao.getDateLong(strMinDate,"",1);
											//去查此渠道的数据
											iCount = cDao.getCdrChannelTotalCount("", "", "", startTimeLong,endTimeLong,
													"", bdparam, zoneparam, areaparam, bagparam, 
													sChanne.getId()+"", "", "1", opera, "");
										}
										mapCount.put(strMinDate, new Integer(iCount));
									}
									strMinDate = DateAdd1(strMinDate,1);
								}
								mapData.put(UtilFunctionDao.splitStr(chName,"_"), mapCount);
							}
						}
					}
					req.setAttribute("mapData", mapData);
					JFreeChart chartLine =new TimeSeriesChart("渠道呼入走势图","","",mapData,1).getChartPanel();    //添加折线图          
					req.setAttribute("chartLine", chartLine);
					FileOutputStream fos_Line = null;
				 	try{  
				 		File outFile = new File(serverPath + "charImgs");
				 		if(!outFile.exists())
				 			outFile.mkdir();
				        String sfileName = "chartLine"+fm.format(new Date())+".png";
				        String graphURLLine =  serverPath+ "charImgs"+File.separator+sfileName;
				        
				        //转成\\ ，否则一个\自动被去掉了，无法下载
				        String graphURLLinePath =replaceStr(graphURLLine);				        
				        req.setAttribute("graphURLLinePath",graphURLLinePath);  
				        
				        fos_Line = new FileOutputStream(graphURLLine);
			            ChartUtilities.writeChartAsPNG(fos_Line,chartLine, 900, 300);

			            fos_Line.flush();
				    } catch (Exception e){ 
				    	
				    }finally{
				    	try {
				    		if(fos_Line !=null)
				    			fos_Line.close();
			             } catch (IOException e1) {
			                 e1.printStackTrace();
			             }    
				    }
			    
				}
			}
			//------------------------------------------------按天统计折线图----------------------------------------------
			//------------------------------------------------按月统计折线图----------------------------------------------
			if(sDateSelect !=null && sDateSelect.equals("2"))
			{
				//查最大的时间和最小的时间
				List<String> MaxMinDateMonth = cDao.getCdrChannelMaxMinDate("","","",startTimeLong,endTimeLong,"",bdparam,zoneparam,areaparam,bagparam,channelid,"","1",opera,"",2);
				if(MaxMinDateMonth !=null && MaxMinDateMonth.size()>0)
				{
					//组织数据：Map<渠道名,Map<按天或月查,数值>> (Map<String,Map<String,Integer>>)
					Map<String,Map<String,Integer>> mapData = new LinkedHashMap<String,Map<String,Integer>>();
					for(int i=0;i<channelcount.size();i++)
					{
						ZydcRecord zy = channelcount.get(i);
						String chName = zy.getChannelname();//取到渠道名
						if(chName !=null && chName.length()>0)
						{
							//里层的map
							Map<String,Integer> mapCount = new LinkedHashMap<String,Integer>();
							SsmnZyChannel sChanne = chanDao.getChannelByName(chName);
							if(sChanne !=null)
							{
								//计算从最小的一个月到最大的一个月
								String strMinDate = MaxMinDateMonth.get(0);
								String strMaxDate = MaxMinDateMonth.get(MaxMinDateMonth.size()-1);
								while(strMinDate.compareTo(strMaxDate)<=0)
								{								
									if(strMinDate !=null && strMinDate.length()>0)
									{
										//先去MaxMinDate中查找，如果有值，去查，否则写0
										int iCount = 0;
										if(MaxMinDateMonth.indexOf(strMinDate) >=0)
										{
											startTimeLong =cDao.getDateLong(new UtilFunctionDao().getFirstLastDay(strMinDate, 0,0),"",0);
											endTimeLong =cDao.getDateLong(new UtilFunctionDao().getFirstLastDay(strMinDate, 1,0),"",1);
											//去查此渠道的数据
											iCount = cDao.getCdrChannelTotalCount("", "", "", startTimeLong,endTimeLong, "", bdparam, zoneparam, areaparam, bagparam, sChanne.getId()+"", "", "1", opera, "");
										}
										mapCount.put(strMinDate, new Integer(iCount));
									}
									strMinDate = DateAdd1(strMinDate,2);
								}
								mapData.put(UtilFunctionDao.splitStr(chName,"_"), mapCount);
							}
						}
					}

					JFreeChart chartLineMonth =new TimeSeriesChart("渠道呼入走势图","","",mapData,2).getChartPanel();    //添加折线图         
					req.setAttribute("chartLineMonth", chartLineMonth);
					
					FileOutputStream fos_Line = null;
				 	try{  
				 		File outFile = new File(serverPath + "charImgs");
				 		if(!outFile.exists())
				 			outFile.mkdir();
				        String sfileName = "chartLineMonth"+fm.format(new Date())+".png";
				        String graphURLLine =  serverPath+ "charImgs"+File.separator+sfileName;
				        
				        String graphURLLinePathMonth =replaceStr(graphURLLine);
				        req.setAttribute("graphURLLinePathMonth",graphURLLinePathMonth);  
				        
				        fos_Line = new FileOutputStream(graphURLLine);
			            ChartUtilities.writeChartAsPNG(fos_Line,chartLineMonth, 900, 300);

			            fos_Line.flush();
				    } catch (Exception e){ 
				    	
				    }finally{
				    	try {
				    		if(fos_Line !=null)
				    			fos_Line.close();
			             } catch (IOException e1) {
			                 e1.printStackTrace();
			             }    
				    }
			    
				}
			}
			//------------------------------------------------按月统计折线图----------------------------------------------
			//------------------------------------------------饼图----------------------------------------------
				Map<String,Integer> mapPie = new LinkedHashMap<String,Integer>();
				for(int i=0;i<channelcount.size();i++)
				{
					ZydcRecord zy = channelcount.get(i);
					String chName = zy.getChannelname();//取到渠道名
					mapPie.put(UtilFunctionDao.splitStr(chName,"_"), zy.getChannelCount());
				}

				JFreeChart chartPie =new PieChart("各渠道呼入统计占比图",mapPie).getChartPanel();    //饼图 
				req.setAttribute("chartPie", chartPie);
				FileOutputStream fos_Line = null;
			 	try{  
			 		File outFile = new File(serverPath + "charImgs");
			 		if(!outFile.exists())
			 			outFile.mkdir();
			        String sfileName = "chartPie"+fm.format(new Date())+".png";
			        String graphURLLine =  serverPath+ "charImgs"+File.separator+sfileName;
			        
			        //由于饼图显示不清楚，这里使用生成的图片tomcat中的显示
			        String graphURLPieShowPath = /*serverUrl+File.separator+*/"charImgs"+File.separator +sfileName;
			        graphURLPieShowPath = graphURLPieShowPath.replaceAll("\\\\", "/");
			        req.setAttribute("graphURLPieShowPath", graphURLPieShowPath);
			        
			        String graphURLPiePath=replaceStr(graphURLLine);
			        req.setAttribute("graphURLPiePath",graphURLPiePath);  
			        
			        fos_Line = new FileOutputStream(graphURLLine);
		            ChartUtilities.writeChartAsPNG(fos_Line,chartPie, 654, 353);

		            fos_Line.flush();
			    } catch (Exception e){ 
			    	
			    }finally{
			    	try {
			    		if(fos_Line !=null)
			    			fos_Line.close();
		             } catch (IOException e1) {
		                 e1.printStackTrace();
		             }    
			    }
		    
			
			//------------------------------------------------饼图----------------------------------------------
			}
		}
		
	    req.setAttribute("startdate", startdate);
	    req.setAttribute("enddate", enddate);
	    req.setAttribute("channeloutcount", channelOutCounts);
	    req.setAttribute("channelcount", channelcounts);

	    if(channelcounts == null)
	    {
	    	if((startdate !=null && startdate.length()>0) || (enddate !=null && enddate.length()>0) 
	    			|| (startdateMonth !=null && startdateMonth.length()>0) || (enddateMonth !=null && enddateMonth.length()>0) )
	    		req.setAttribute("msg", "没有查到数据!");
	    }
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
		if(channelcounts !=null && channelcounts.length >0)
			req.setAttribute("channelcountNumin", channelcounts[0].length+1);
		if(channelcounts != null && channelcounts.length >0)
		    req.setAttribute("numchannelcounts", "1");
		else
		    req.setAttribute("numchannelcounts", "0");
		req.setAttribute("sDateSelect", sDateSelect);
		req.setAttribute("startdateMonth", startdateMonth);
		req.setAttribute("enddateMonth", enddateMonth);
		if(sDateSelect !=null && sDateSelect.length()>0 && sDateSelect.equals("1"))
		{
			req.setAttribute("dayLineShow", "1");
			req.setAttribute("monthLineShow", "0");
		}
		if(sDateSelect !=null && sDateSelect.length()>0 && sDateSelect.equals("2"))
		{
			req.setAttribute("dayLineShow", "0");
			req.setAttribute("monthLineShow", "1");
		}

		return mapping.findForward(super.formView);
	}
	
	
	public ActionForward processGet(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) {

		return this.processSubmit(mapping, form, req, res);
	}	
	
	//实现给一个字符串类型的日期，type:1 加1天 type:2 加一个月
	public String DateAdd1(String sdate,int type)
	{
		String sres = "";
		if(sdate ==null || sdate.equals(""))
			return sres;
		
		SimpleDateFormat df ;
		if(type ==1)
			df =new SimpleDateFormat("yyyy-MM-dd");
		else
			df = new SimpleDateFormat("yyyy-MM");
		/*** 加一天*/
		try {
			Date dd = df.parse(sdate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dd);
			if(type ==1)
				calendar.add(Calendar.DAY_OF_MONTH, 1);//加一天
			else
				calendar.add(Calendar.MONTH, 1);//加一天
			sres = df.format(calendar.getTime());
			
		} catch (ParseException e)
		{e.printStackTrace();}
		return sres;
	}
	
	public String replaceStr(String str)
	{
		String res ="";
		if(str == null || str.length() <=0)
			return res;
		
		if(str.indexOf("\\") >0)
        	res = str.replaceAll("\\\\", "\\\\\\\\");
        else
        	res = str.replaceAll("////", "\\\\\\\\");
		return res;
	}
		
}
