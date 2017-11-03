package com.dascom.OPadmin.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class DesEncrypt extends HttpServlet{
	//private static final Logger logger = Logger.getLogger(ExportOperatorInfo.class);
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response)
	throws ServletException, IOException{
		//TyadminOperators opera = (TyadminOperators)request.getSession().getAttribute(Constants.OPERATOR_IN_SESSION);
		try{
			 HttpSession session = request.getSession();
			response.setHeader("Cache-Control", "no-cashe");			
			response.setContentType("text/plain; charset=UTF-8");
			String strName = request.getParameter("name");
			String strPasword = request.getParameter("password");
			//String strpara =request.getQueryString().substring(0); //request.getParameter("para");
			String strpara = "name="+strName+"&password="+strPasword;
			String xmlName = "";
			String xmlPassword = "";
			
			DesEncryptUtil des = new DesEncryptUtil();
	        // 实例化一个对像
	        String strEncName = des.getEncString(strName);
	        String strEncPasword = des.getEncString(strPasword);
	        String strEncPara = des.getEncString(strpara);
			
			if(strEncName != null && !"".equals(strEncName) && strEncName.length()>0 )
				xmlName = strEncName;
			else 
				xmlName = "";
			if(strEncPasword !=null && !"".equals(strEncPasword) && strEncPasword.length()>0)
				xmlPassword = strEncPasword;
		
				 if(xmlName.length()>0){
			         try {
			        	 PrintWriter out = response.getWriter();
			        	 out.println(strEncPara);
			        	 //out.println("#");
			        	 //out.println(xmlPassword);
					} catch (IOException e) {
						e.printStackTrace();
					}
			     }		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
