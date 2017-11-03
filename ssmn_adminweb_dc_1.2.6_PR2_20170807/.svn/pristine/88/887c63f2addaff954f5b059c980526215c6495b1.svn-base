package com.dascom.OPadmin.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dascom.OPadmin.dao.IAdminAuthority;
import com.dascom.OPadmin.dao.impl.AdminAuthorityImpl;
import com.dascom.OPadmin.entity.TyadminAuthorities;
import com.dascom.ssmn.util.Constants;

public class LoadAuthAciton extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IAdminAuthority authDao = new AdminAuthorityImpl();
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response)
	throws ServletException, IOException{
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(-1);
		try{
			response.setHeader("Cache-Control", "no-cashe");
		
			response.setContentType("text/plain; charset=UTF-8");
			String flag=(String)request.getParameter("flag");
			String rank=(String)request.getParameter("rank");
			String groupid=(String)request.getParameter("groupid");
			if(flag!=null&&flag.equals("mod")&&groupid!=null)
			{
				List<TyadminAuthorities> authList = authDao.findAuths((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION), Long.valueOf(rank));
				List<TyadminAuthorities> groupauthList = authDao.findByGroup(Long.valueOf(groupid));
				PrintWriter out = response.getWriter();
				StringBuffer s=new StringBuffer();
				Long sectionid = 1L;
				for(int i=0;i<authList.size();i++)
				{
					TyadminAuthorities tempAuth = (TyadminAuthorities)authList.get(i);
					s.append(tempAuth.getId());
					s.append(";");
					s.append(tempAuth.getAuthority());
					
					if(sectionid.longValue() != tempAuth.getSectionId().longValue())
						sectionid = tempAuth.getSectionId();
					//添加子级
					List<TyadminAuthorities> authChildList = authDao.findChildrenAuths((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION), tempAuth.getId());
					if(authChildList !=null && authChildList.size()>0)
					{						
						for (int j=0;j<authChildList.size();j++)
						{
							s.append("%");
							TyadminAuthorities tempChAuth = (TyadminAuthorities)authChildList.get(j);
							s.append(tempChAuth.getId());
							s.append(";");
							s.append(tempChAuth.getAuthority());
						}
					}
					if((i+1)< authList.size() && authList.get(i+1).getSectionId().longValue() == sectionid.longValue())
					{	if(i!=authList.size()-1)
							s.append("#");
					}
					else
					{
						if(i!=authList.size()-1)
							s.append("$");	//冒号 区分每一组,如 信息管理组
					}
				}

				s.append("|");
				for(int i=0;i<groupauthList.size();i++)
				{
					TyadminAuthorities tempAuth = (TyadminAuthorities)groupauthList.get(i);
					s.append(tempAuth.getId());
					s.append("#");
				}
				out.println(s.toString());
			}
			else
			{
				List<TyadminAuthorities> authList = authDao.findAuths((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION), Long.valueOf(rank));
				PrintWriter out = response.getWriter();
				StringBuffer s=new StringBuffer();
				Long sectionid = 1L;
				for(int i=0;i<authList.size();i++)
				{
					TyadminAuthorities tempAuth = (TyadminAuthorities)authList.get(i);
					s.append(tempAuth.getId());
					s.append(";");
					s.append(tempAuth.getAuthority());
					
					if(sectionid.longValue() != tempAuth.getSectionId().longValue())
						sectionid = tempAuth.getSectionId();
					//添加子级
					List<TyadminAuthorities> authChildList = authDao.findChildrenAuths((String)session.getAttribute(Constants.SERVICEKEY_IN_SESSION), tempAuth.getId());
					if(authChildList !=null && authChildList.size()>0)
					{						
						for (int j=0;j<authChildList.size();j++)
						{
							s.append("%");
							TyadminAuthorities tempChAuth = (TyadminAuthorities)authChildList.get(j);
							s.append(tempChAuth.getId());
							s.append(";");
							s.append(tempChAuth.getAuthority());
						}
					}
					if((i+1)< authList.size() && authList.get(i+1).getSectionId().longValue() == sectionid.longValue())
					{	if(i!=authList.size()-1)
							s.append("#");
					}
					else
					{
						if(i!=authList.size()-1)
							s.append("$");	//冒号 区分每一组,如 信息管理组
					}
					
				}
				
				out.println(s.toString());
			}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
