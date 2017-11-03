package com.dascom.OPadmin.util;

import java.awt.Image;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.dascom.OPadmin.dbadapter.IHibernateAdapter;
import com.dascom.OPadmin.dbadapter.PlainHibernateAdapter;
import com.dascom.ssmn.util.Constants;


public class SysUtil {
	
	private IHibernateAdapter adapter = new PlainHibernateAdapter();
	private static final Logger logger = Logger.getLogger(SysUtil.class);
	private static SysUtil sysUtil;
	
	public static SysUtil getInstance(){
		if(sysUtil == null)
			sysUtil = new SysUtil();
		
		return sysUtil;
	}


	public Date getDbDate() {
		Session session = null;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Date dbdate = new Date();
		try {
			session = adapter.openSession();
			con = session.connection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select sysdate from dual");
			rs.next();
			dbdate = rs.getTimestamp(1);
			rs.close();
			stmt.close();
			//con.close();
			session.close();
		} catch (Exception e) {
			logger.error(e.toString());
		}finally{
			try {
				rs.close();
				con.close();
				session.close();
			} catch (Exception e) {
				
			}
		}
		return dbdate;
	}
	

	public Page setPageInfo(HttpServletRequest req, String pageNum) {
		int iPageNum = 1;
		if (pageNum != null && !pageNum.equals("")) {
			try {
				iPageNum = Integer.parseInt(pageNum);
			} catch (Exception e) {
				iPageNum = 1;
			}
		}
		req.setAttribute("pageNum", new java.lang.Integer(iPageNum));
		req.setAttribute("pageSize", new java.lang.Integer(Constants.RECORD_PER_PAGE));
		Page page = new Page();
		page.setSize(Constants.RECORD_PER_PAGE);
		page.setCurrentindex(iPageNum);
		return page;
	}
	
	public void setBackParam(HttpServletRequest req, Map param) {
		Set entries = param.entrySet();
		for (Iterator iter = entries.iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			req.setAttribute((String) entry.getKey(), entry.getValue());
		}
	}
	
	public Page setPageInfo(HttpServletRequest req, String pageNum, Integer numPerPage) {
		int iPageNum = 1;
		if (pageNum != null && !pageNum.equals("")) {
			try {
				iPageNum = Integer.parseInt(pageNum);
			} catch (Exception e) {
				iPageNum = 1;
			}
		}
		req.setAttribute("pageNum", new java.lang.Integer(iPageNum));
		req.setAttribute("pageSize", new java.lang.Integer(numPerPage));
		Page page = new Page();
		page.setSize(numPerPage);
		page.setCurrentindex(iPageNum);
		return page;
	}
	
	public Page setPageInfo1(String pageNum, Integer numPerPage) {
		int iPageNum = 1;
		if (pageNum != null && !pageNum.equals("")) {
			try {
				iPageNum = Integer.parseInt(pageNum);
			} catch (Exception e) {
				iPageNum = 1;
			}
		}
		Page page = new Page();
		page.setSize(numPerPage);
		page.setCurrentindex(iPageNum);
		return page;
	}
//	add by aliujie
	public String replaceBlank(String str)
	{
		Pattern p = Pattern.compile("\\s*");
		Matcher m = p.matcher(str);
		String after = m.replaceAll(""); 
		return after;
	}
	
 
	public static boolean isStringNotEmpty(String name) {
		if(name != null && !"".equals(name.trim()))
			return true;
		
		return false;
	}
}
