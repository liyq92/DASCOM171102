package com.dascom.ssmn.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.ssmn.dao.LevelDao;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.util.Constants;

public class SelectLevel extends HttpServlet{
	private static final Logger logger = Logger.getLogger(SelectLevel.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -4765995353753779428L;
	private LevelDao leveldao = LevelDao.getInstance();
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response)
	throws ServletException, IOException{
		TyadminOperators opera = (TyadminOperators)request.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		try{
			response.setHeader("Cache-Control", "no-cashe");			
			response.setContentType("text/plain; charset=UTF-8");
			String method = request.getParameter("method");
			String xml=null;
			if(method != null && "list".equals(method)){
				Long operalevelid = opera.getLevelid();
				SsmnZyLevel level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(operalevelid);
				String businessdepartment = level.getBusinessdepartment();
				List<String> bdlist = new ArrayList<String>();
				String warzone = level.getWarzone();
				List<String> wzlist = new ArrayList<String>();
				String area = level.getArea();
				List<String> arealist = new ArrayList<String>();
				String branchactiongroup = level.getBranchactiongroup();
				List<String> baglist = new ArrayList<String>();
				String businessdepartmentparam=(String)request.getParameter("bd");
				String warzoneparam=(String)request.getParameter("warzone");
				String areaparam=(String)request.getParameter("area");
				String branchactiongroupparam = (String)request.getParameter("bag");
				
				if(businessdepartment == null){  //操作员对应省份和公司下的所有事业部都可选
				   //取出事业部集合
				    bdlist = leveldao.getLevelByTypeAndID(1, operalevelid);
			    }else{  //只能选择与操作员对应的事业部
				    bdlist.add(businessdepartment);
			    }
				//根据businessdepartment查出wzlist下级菜单
				wzlist = leveldao.getSelectLevel(1, operalevelid, businessdepartmentparam, null, null);
				if(warzone != null){   //如果对应的事业部下的战区都可选
					wzlist.add(warzone);
				} 
				arealist = leveldao.getSelectLevel(2, operalevelid, businessdepartmentparam, warzoneparam, null);
				if(area != null){ //对应事业部和战区下的片区都可选
					arealist.add(area);
				}
				baglist =  leveldao.getSelectLevel(3, operalevelid, businessdepartmentparam, warzoneparam, areaparam);
				if(branchactiongroup != null){ //对应事业部、战区和片区下的行动组都可选
					baglist.add(branchactiongroup);
				} 
				
				//返回消息格式，businessdepartment|warzone|area|branchactiongroup#bdlist#wzlist#arealist#baglist
				xml = businessdepartmentparam+"|"+warzoneparam+"|"+areaparam+"|"+branchactiongroupparam+"#";
				for(int b=0;b<bdlist.size();b++){
					xml= xml+bdlist.get(b);
					if(b <bdlist.size()-1){
						xml += "|";
					}
				}
				xml+="#";
				for(int w=0;w<wzlist.size();w++){
					xml= xml+wzlist.get(w);
					if(w <wzlist.size()-1){
						xml += "|";
					}
				}
				xml+="#";
				for(int a=0;a<arealist.size();a++){
					xml= xml+arealist.get(a);
					if(a <arealist.size()-1){
						xml += "|";
					}
				}
				xml+="#";
				for(int g=0;g<baglist.size();g++){
					xml= xml+baglist.get(g);
					if(g <baglist.size()-1){
						xml += "|";
					}
				}
				xml += "#";//防止后面带非法字符
				//logger.info("=====xml===="+xml);
			}else{
				Long levelid = opera.getLevelid();
				String type=(String)request.getParameter("type");
				String businessdepartment=(String)request.getParameter("bd");
				String warzone=(String)request.getParameter("warzone");
				String area=(String)request.getParameter("area");
				xml=type+"#";  //#分隔的第一个元素放类型
				List<String> list = leveldao.getSelectLevel(Integer.parseInt(type), levelid, businessdepartment, warzone, area);
				for(int i=0;i<list.size();i++)
				{
					xml +=list.get(i)+"#";
				}
//				logger.info("=====xml===="+xml);
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
