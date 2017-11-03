package com.dascom.OPadmin.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dascom.OPadmin.dao.IAdminOperator;
import com.dascom.OPadmin.entity.TyadminOperatorLogs;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.entity.TyadminOperatorsId;
import com.dascom.OPadmin.util.DBException;
import com.dascom.OPadmin.util.HibernateSessionFactory;
import com.dascom.init.ConfigMgr;
import com.dascom.ssmn.dao.CDRDao;
import com.dascom.ssmn.entity.SsmnZyLevel;
import org.apache.log4j.Logger;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.util.Constants;

public class AdminOperatorImpl extends RootDao implements IAdminOperator {
	private static final Logger logger =  Logger.getLogger(CDRDao.class);
	public static AdminOperatorImpl aoi;
	
	public static AdminOperatorImpl getInstance() {
		if(aoi == null)
			aoi = new AdminOperatorImpl();
		
		return aoi;
	}
	
	public Class getReferenceClass() {
		return TyadminOperators.class;
	}
	public List<TyadminOperators> findByServiceKey(String serviceKey,String rank,String keyword,
			String bdparam,String zoneparam,String areaparam,String bagparam,SsmnZyLevel zylevel,
			Integer index,Integer maxCount) {
		Session session = this.adapter.openSession();
		ScrollableResults srs = null; 
		StringBuffer sb = new StringBuffer();
		sb.append("select operator.id.openo,operator.id.servicekey, operator.opepwd,operator.groupId,operator.createUser,operator.createTime," +
				"operator.agentInfo,operator.note,l.provincecity,l.company,l.businessdepartment,l.warzone," +
				"l.area,l.branchactiongroup,l.id  from TyadminOperators operator,TyadminGroups group,SsmnZyLevel l ");
		sb.append("where operator.id.servicekey = :serviceKey and operator.levelid=l.id and group.servicekey = :serviceKey and group.id=operator.groupId and group.rank>=:rank   ");
		//账号
		if((keyword != null) && (!keyword.equals("")))
			sb.append(" and operator.id.openo = :keyword");
		//事业部
		if(bdparam != null && !"".equals(bdparam))
		{
			sb.append(" and l.businessdepartment = :businessdepartment ");
		}
		//战区
		if(zoneparam != null && !"".equals(zoneparam))
		{
			sb.append(" and l.warzone = :warzone ");
		}
		//片区
		if(areaparam != null && !"".equals(areaparam))
		{
			sb.append("  and l.area = :area ");
		}
		//行动组
		if(bagparam != null && !"".equals(bagparam))
		{
			sb.append(" and l.branchactiongroup = :branchactiongroup ");
		}
		String ssql = new UtilFunctionDao().getTyadminOperatorSelectArea(zylevel, "operator");
		if(ssql !=null && ssql.length()>0)
		{
			sb.append(" and operator.levelid in ( ").append(ssql).append(" ) ") ;
		}
		
		sb.append(" order by l.businessdepartment,l.warzone,l.area, l.branchactiongroup ");
		//sb.append("  order by operator.createTime desc ");
		List<TyadminOperators> opeList = new ArrayList<TyadminOperators>();
		try {
			Query q =  session.createQuery(sb.toString());
			q.setString("serviceKey", serviceKey);
			q.setString("rank", rank);
			if((keyword != null) && (!keyword.equals("")))
				q.setString("keyword", keyword);
			//事业部
			if(bdparam != null && !"".equals(bdparam))
			{
				 q.setString("businessdepartment", bdparam);
			}
			//战区
			if(zoneparam != null && !"".equals(zoneparam))
			{
				q.setString("warzone", zoneparam);
			}
			//片区
			if(areaparam != null && !"".equals(areaparam))
			{
				q.setString("area", areaparam);
			}
			//行动组
			if(bagparam != null && !"".equals(bagparam))
			{
				q.setString("branchactiongroup", bagparam);
			}
			
			new UtilFunctionDao().setOperatorAttribute(zylevel, q);
			
			if(index != null )
			{
				q.setFirstResult(index);
				q.setMaxResults(maxCount);
			}
			
			//opeList = q.list();
			srs = q.scroll();
			while(srs.next())
			{
				opeList.add(new TyadminOperators(new TyadminOperatorsId(srs.getString(0),srs.getString(1)),
						srs.getString(2),srs.getLong(3),srs.getString(4),srs.getDate(5),srs.getString(6),
						srs.getString(7),srs.getString(8),srs.getString(9),srs.getString(10),srs.getString(11),
						srs.getString(12),srs.getString(13),srs.getLong(14)));
			}
			
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
		return opeList;
	}
	public int findcountByServiceKey(String serviceKey,String rank,String keyword,String bdparam,String zoneparam,String areaparam,String bagparam,SsmnZyLevel zylevel) {
		Session session = this.adapter.openSession();
		int count=0;
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) from TyadminOperators operator,TyadminGroups group  ,SsmnZyLevel l ");
		sb.append("where operator.id.servicekey = :serviceKey and operator.levelid=l.id and group.servicekey = :serviceKey and group.id=operator.groupId and group.rank>=:rank ");
		//账号
		if((keyword != null) && (!keyword.equals("")))
			sb.append(" and operator.id.openo = :keyword");
		//事业部
		if(bdparam != null && !"".equals(bdparam))
		{
			sb.append(" and l.businessdepartment = :businessdepartment ");
		}
		//战区
		if(zoneparam != null && !"".equals(zoneparam))
		{
			sb.append(" and l.warzone = :warzone ");
		}
		//片区
		if(areaparam != null && !"".equals(areaparam))
		{
			sb.append("  and l.area = :area ");
		}
		//行动组
		if(bagparam != null && !"".equals(bagparam))
		{
			sb.append(" and l.branchactiongroup = :branchactiongroup ");
		}
		String ssql = new UtilFunctionDao().getTyadminOperatorSelectArea(zylevel, "operator");
		if(ssql !=null && ssql.length()>0)
		{
			sb.append(" and operator.levelid in ( ").append(ssql).append(" ) ") ;
		}

		List<TyadminOperators> opeList = new ArrayList<TyadminOperators>();
		try {
			Query q =  session.createQuery(sb.toString());
			q.setString("serviceKey", serviceKey);
			q.setString("rank", rank);
			//账号
			if((keyword != null) && (!keyword.equals("")))
				q.setString("keyword", keyword);
			//事业部
			if(bdparam != null && !"".equals(bdparam))
			{
				 q.setString("businessdepartment", bdparam);
			}
			//战区
			if(zoneparam != null && !"".equals(zoneparam))
			{
				q.setString("warzone", zoneparam);
			}
			//片区
			if(areaparam != null && !"".equals(areaparam))
			{
				q.setString("area", areaparam);
			}
			//行动组
			if(bagparam != null && !"".equals(bagparam))
			{
				q.setString("branchactiongroup", bagparam);
			}
			new UtilFunctionDao().setOperatorAttribute(zylevel, q);
			
			count = ((Integer) q.uniqueResult()).intValue();
			
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
		return count;
	}
	
	public List<TyadminOperators> findByOpeno(String openo, String serviceKey) {
		List<TyadminOperators> operatorList = new ArrayList<TyadminOperators>();
		Session session = this.adapter.openSession();
		StringBuffer sb = new StringBuffer();
		sb.append("from TyadminOperators operator ");
		sb.append("where operator.id.openo = :openo ");
		sb.append("and operator.id.servicekey = :servicekey");
		try {
			Query q = session.createQuery(sb.toString()).setString("openo", openo);
			q.setString("servicekey", serviceKey);
			
			operatorList = q.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally 
		{
			try {
				session.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
		return operatorList;
	}

	public int countOperatorByGroup(Long groupid){
		Session session = this.adapter.openSession();
		StringBuffer sb = new StringBuffer();
		sb.append("from TyadminOperators operator ");
		sb.append("where operator.groupId = :gpid ");
		List<TyadminOperators> opeList = new ArrayList<TyadminOperators>();
		int count = 0;
		try {
			opeList =  session.createQuery(sb.toString()).setLong("gpid", groupid.longValue()).list();
			session.close();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			if(opeList != null)
				count = opeList.size();
			try {
				session.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
		return count;
	}
	public int countNameAndPasswordMatch(String name, String password) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	//统计出各个操作员的级别,定时任务中筛选数据使用
	public List<SsmnZyLevel> groupByProvinOpeno(String serviceKey) {
		List<SsmnZyLevel> levelList = new ArrayList<SsmnZyLevel>();
		Session session = this.adapter.openSession();
		StringBuffer sb = new StringBuffer();
		
		ScrollableResults srs = null; 
		
		sb.append(" select le.id, le.provincecity, le.company, le.businessdepartment, le.warzone, le.area, le.branchactiongroup from TyadminOperators t, SsmnZyLevel le ");
		sb.append(" where t.levelid = le.id ");
		if(serviceKey != null && !"".equals(serviceKey))
		{
			sb.append(" and t.id.servicekey = :servicekey ");
		}
		//筛选出 配置文件中配置的省，只有配置了该省市，才会生成报表
		if(ConfigMgr.getTimingTask_provincecity() !=null && ConfigMgr.getTimingTask_provincecity().size()>0)
		{
			sb.append(" and ");
			for(int i=0;i<ConfigMgr.getTimingTask_provincecity().size();i++)
			{
				sb.append("  le.provincecity = :arg"+i);
				if(i< ConfigMgr.getTimingTask_provincecity().size()-1)
					sb.append("  or ");
			}
			 
				 
		}
		sb.append(" group by le.provincecity,le.company,le.businessdepartment,le.warzone,le.area,le.branchactiongroup,le.id ");
		try {
			Query q = session.createQuery(sb.toString());
			q.setString("servicekey", serviceKey);
			if(ConfigMgr.getTimingTask_provincecity() !=null && ConfigMgr.getTimingTask_provincecity().size()>0)
			{
				for(int i=0;i<ConfigMgr.getTimingTask_provincecity().size();i++)
				{
					q.setString("arg"+i, ConfigMgr.getTimingTask_provincecity().get(i));
				}
			}
			
			srs = q.scroll();
			while(srs.next())
			{
				levelList.add(new SsmnZyLevel(srs.getLong(0), srs.getString(1),srs.getString(2),srs.getString(3),
						srs.getString(4),srs.getString(5),srs.getString(6)));
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
		return levelList;
	}
	
	/*
	 * 查询操作员登录详情
	 * loginType: 登录类型分为：　0全部，1登录，2未登录 
	 *  
	 */
	public List<TyadminOperatorLogs> findOpenoLoginDetail(String starttime,String endtime,String loginType, String busDep,String zoneparam,String areaparam,String bagparam ,String openo,SsmnZyLevel zylevel,Integer index,Integer maxcount)
	{
		String sql="";
		String sqlTemp1 = "";
		String sqlTemp2 = "";
		
		Session session = HibernateSessionFactory.getSession();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
		List<TyadminOperatorLogs> list = new ArrayList<TyadminOperatorLogs>();
		
		if(loginType !=null && loginType.equals("0"))//全部
		{
			String stemp = "";
			String scgtm = "";
			String sctop = "";
			if(starttime !=null && !"".equals(starttime))//如果选择时间，则，只有在限制的时间范围内的时间显示，其他显示空
			{
				stemp +="(case when to_char(to_date(c.lgtm,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd') >='";
				stemp +=starttime;
				stemp +="' ";
			}
			if(endtime !=null && !"".equals(endtime))
			{
				stemp += " and to_char(to_date(c.lgtm,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd') <='";
				stemp +=endtime;
				stemp +="' ";
			}
			if(starttime !=null && !"".equals(starttime) || endtime !=null && !"".equals(endtime))
			{
				scgtm = stemp +"   then c.lgtm else '' end   )as lgtm  ";
				sctop = stemp +"   then c.ctop else 0 end) as ctop ";
			}
			else
			{
				scgtm = " c.lgtm ";
				sctop = "  (case when c.ctop>0  then c.ctop else 0 end) as ctop  ";
			}
			//时间条件要加到从　tyadmin_operators中，时间范围内的登录次数
			/*sql = "select o.openo,l.businessdepartment,l.warzone,l.area,l.branchactiongroup,(case when c.lgtm is null then  "+
				" (select to_char(max(p.log_time),'yyyy-mm-dd hh24:mi:ss') from tyadmin_logs p where p.openo=o.openo  ) else c.lgtm end ) as lgtm, "+sctop+" from tyadmin_operators o "+
				" left join   (select t.openo as op ,to_char(max(t.log_time),'yyyy-mm-dd hh24:mi:ss') as lgtm, count(t.openo) as ctop from tyadmin_logs t "+
				" where t.log_des like'%登陆系统%' and t.servicekey=50 " ;*/
			sql = "select o.openo,l.businessdepartment,l.warzone,l.area,l.branchactiongroup,c.lgtm , "+sctop+" from tyadmin_operators o "+
			" left join   (select t.openo as op ,to_char(max(t.log_time),'yyyy-mm-dd hh24:mi:ss') as lgtm, count(t.openo) as ctop from tyadmin_logs t "+
			" where t.log_des like'%登陆系统%' and t.servicekey=50 " ;
				//登录时间
				if(starttime !=null && !"".equals(starttime))
				{
					sql +=" and to_char(t.log_time,'yyyy-mm-dd') >='";
					sql += starttime;
					sql +="' ";
				}
				if(endtime !=null && !"".equals(endtime))
				{
					sql +=" and to_char(t.log_time,'yyyy-mm-dd') <='";
					sql += endtime;
					sql +="' ";
				}
				sql += " group by t.openo order by lgtm desc ) c on o.openo=c.op "+
				" inner join ssmn_zy_level l on o.levelid=l.id  ";
				//事业部,可以加到分组后，
				if(busDep != null && !busDep.equals("")  )
					sql += " and l.businessdepartment ='"+busDep+"' ";
				//战区
				if(zoneparam !=null && !"".equals(zoneparam) && zoneparam.length()>0)
					sql += " and l.warzone ='"+zoneparam+"' ";
				//片区
				if(areaparam !=null && !"".equals(areaparam) && areaparam.length()>0)
					sql += " and l.area ='"+areaparam+"' ";
				//行动组别
				if(bagparam !=null && !"".equals(bagparam) && bagparam.length()>0)
					sql += " and l.branchactiongroup ='"+bagparam+"' ";
				
				//登录账号,可以加到分组后，
				if(openo !=null && !"".equals(openo))
				{
					sql += " and openo ='"+openo+"' ";
				}
				//加入登录账号的区域限制
				String ssql = new UtilFunctionDao().getTyadminOperatorByJdbcArea(zylevel, "o");
				if(ssql !=null && ssql.length() >0){
					sql +="  and o.levelid in ( ";
					sql += ssql.toString();
					sql += " ) ";
				}
				
		}
		if(loginType !=null && loginType.equals("1"))//登录
		{
			sql = " select o.openo,l.businessdepartment,l.warzone,l.area,l.branchactiongroup, "+
				 " to_char(max(c.lgtm),'yyyy-mm-dd hh24:mi:ss') as lgtm ,count(o.openo) as ctop  from  "+ 
				 "  (select t.openo as op ,t.log_time as lgtm  from tyadmin_logs t  "+
				 "  where t.log_des like'%登陆系统%' and t.servicekey=50  ) c,  tyadmin_operators o,ssmn_zy_level l "+
				 "  where c.op=o.openo and o.levelid=l.id ";			
			//登录时间
			if(starttime !=null && !"".equals(starttime))
			{
				sql +=" and to_char(c.lgtm,'yyyy-mm-dd') >='";
				sql += starttime;
				sql +="' ";
			}
			if(endtime !=null && !"".equals(endtime))
			{
				sql +=" and to_char(c.lgtm,'yyyy-mm-dd') <='";
				sql += endtime;
				sql +="' ";
			}
			//事业部
			if(busDep != null && !busDep.equals("")  )
			{
				sql += " and l.businessdepartment ='"+busDep+"' ";
			}
			//战区
			if(zoneparam !=null && !"".equals(zoneparam) && zoneparam.length()>0)
				sql += " and l.warzone ='"+zoneparam+"' ";
			//片区
			if(areaparam !=null && !"".equals(areaparam) && areaparam.length()>0)
				sql += " and l.area ='"+areaparam+"' ";
			//行动组别
			if(bagparam !=null && !"".equals(bagparam) && bagparam.length()>0)
				sql += " and l.branchactiongroup ='"+bagparam+"' ";
			//登录账号
			if(openo !=null && !"".equals(openo))
			{
				sql += " and openo ='"+openo+"' ";
			}
			
			//加入登录账号的区域限制
			String ssql = new UtilFunctionDao().getTyadminOperatorByJdbcArea(zylevel, "o");
			if(ssql !=null && ssql.length() >0){
				sql +="  and o.levelid in ( ";
				sql += ssql.toString();
				sql += " ) ";
			}
			
			sql += " group by o.openo ,l.businessdepartment,l.warzone,l.area,l.branchactiongroup ";
			
		}
		if(loginType !=null && loginType.equals("2"))//未登录
		{			
			sql =" select   openo,businessdepartment,warzone,area,branchactiongroup , lgtm  ,0 as ctop from ( "+
				 " select o.openo,l.businessdepartment,l.warzone,l.area,l.branchactiongroup ,to_char(max(p.log_time),'yyyy-mm-dd hh24:mi:ss') as lgtm  from tyadmin_operators o "+
				 " inner join ssmn_zy_level l on o.levelid=l.id  "+
				 " and  o.openo not in (select distinct(t.openo)  from tyadmin_logs t "+  
				 " where t.log_des like'%登陆系统%' and t.servicekey=50 ";
			//登录时间
			if(starttime !=null && !"".equals(starttime))
			{
				sql +=" and to_char(t.log_time,'yyyy-mm-dd') >='";
				sql += starttime;
				sql +="' ";
			}
			if(endtime !=null && !"".equals(endtime))
			{
				sql +=" and to_char(t.log_time,'yyyy-mm-dd') <='";
				sql += endtime;
				sql +="' ";
			}
			sql += " ) ";
			
			//加入登录账号的区域限制
			String ssql = new UtilFunctionDao().getTyadminOperatorByJdbcArea(zylevel, "o");
			if(ssql !=null && ssql.length() >0){
				sql +="  and o.levelid in ( ";
				sql += ssql.toString();
				sql += " ) ";
			}
			 
          sql += "  left join tyadmin_logs p  on o.openo =p.openo  "+
          		" group by o.openo,l.businessdepartment,l.warzone,l.area,l.branchactiongroup ) where 1=1 ";
          //事业部
			if(busDep != null && !busDep.equals(""))
			{
				sql += " and  businessdepartment ='"+busDep+"' ";
			}
			//战区
			if(zoneparam !=null && !"".equals(zoneparam) && zoneparam.length()>0)
				sql += " and warzone ='"+zoneparam+"' ";
			//片区
			if(areaparam !=null && !"".equals(areaparam) && areaparam.length()>0)
				sql += " and area ='"+areaparam+"' ";
			//行动组别
			if(bagparam !=null && !"".equals(bagparam) && bagparam.length()>0)
				sql += " and branchactiongroup ='"+bagparam+"' ";
			//登录账号
			if(openo !=null && !"".equals(openo))
			{
				sql += " and openo ='"+openo+"' ";
			}
		}

		sql += "  order by lgtm desc nulls last ";
		
		if(loginType !=null && loginType.equals("0"))//全部
		{
			sqlTemp1 =" select openo,businessdepartment,warzone,area,branchactiongroup,lgtm, ctop from ( "+sql +" ) where lgtm is not null ";
			sqlTemp2 = " select openo,businessdepartment,warzone,area,branchactiongroup, lgtm ,ctop from (  "+
						" select openo,businessdepartment,warzone,area,branchactiongroup, lgtm ,ctop from (  "+
						" select openo,businessdepartment,warzone,area,branchactiongroup,(case when  lgtm is null then  "+ 
						" (select to_char(max(p.log_time),'yyyy-mm-dd hh24:mi:ss') from tyadmin_logs p where p.openo=q.openo  ) else  lgtm end ) as lgtm , ctop from ( "+sql +
						" ) q where lgtm is null  ) order by lgtm desc nulls last ) ";
			sql = sqlTemp1 +" union all "+sqlTemp2;
		}
		
		String sqll = " select openo,businessdepartment,warzone,area,branchactiongroup,lgtm,ctop,rn from (" +
				"select openo,businessdepartment,warzone,area,branchactiongroup,lgtm,ctop,rownum as rn from ( ";
		sqll += sql;
		sqll +=" ) ) where 1=1 ";
		if(index !=null && index >=0)
		{
			sqll +=" and rn >= ";
			sqll += index+1;
			sqll += "  and rn <= ";
			sqll += index+maxcount;
		}
		try {
			conn = session.connection();
		
			pstmt = conn.prepareStatement(sqll);
			rs = pstmt.executeQuery();
			if(rs != null) {
				while (rs.next()) {
				list.add(new TyadminOperatorLogs(rs.getString("openo"),rs.getString("businessdepartment"), 
						rs.getString("warzone"),rs.getString("area"),rs.getString("branchactiongroup"),
						rs.getString("lgtm"),rs.getInt("ctop")));
				}
				}
		} catch (Exception he) {
			logger.error("-------------findOpenoLoginDetail---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
				if(rs !=null)
					rs.close();
				if(conn !=null)
					conn.close();
			} catch (HibernateException he2) {
				logger.error("-------------findOpenoLoginDetail---关闭Session时出现HibernateException-----",he2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public int countOpenoLoginDetail(String starttime,String endtime,String loginType, String busDep,String zoneparam,String areaparam,String bagparam,String openo,SsmnZyLevel zylevel)
	{
		String sql="";
		
		Session session = HibernateSessionFactory.getSession();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
		
		if(loginType !=null && loginType.equals("0"))//全部
		{		
			sql = "select count(o.openo) as cn from tyadmin_operators o "+
				" left join   (select t.openo as op ,to_char(max(t.log_time),'yyyy-mm-dd hh24:mi:ss') as lgtm, count(t.openo) as ctop from tyadmin_logs t "+
				" where t.log_des like'%登陆系统%' and t.servicekey=50 "; //登录时间
				if(starttime !=null && !"".equals(starttime))
				{
					sql +=" and to_char(t.log_time,'yyyy-mm-dd') >='";
					sql += starttime;
					sql +="' ";
				}
				if(endtime !=null && !"".equals(endtime))
				{
					sql +=" and to_char(t.log_time,'yyyy-mm-dd') <='";
					sql += endtime;
					sql +="' ";
				}
				sql += " group by t.openo order by lgtm desc ) c on o.openo=c.op "+
				" inner join ssmn_zy_level l on o.levelid=l.id  ";
				//事业部,可以加到分组后，
				if(busDep != null && !busDep.equals("")  )
				{
					sql += " and l.businessdepartment ='"+busDep+"' ";
				}
				//战区
				if(zoneparam !=null && !"".equals(zoneparam) && zoneparam.length()>0)
					sql += " and l.warzone ='"+zoneparam+"' ";
				//片区
				if(areaparam !=null && !"".equals(areaparam) && areaparam.length()>0)
					sql += " and l.area ='"+areaparam+"' ";
				//行动组别
				if(bagparam !=null && !"".equals(bagparam) && bagparam.length()>0)
					sql += " and l.branchactiongroup ='"+bagparam+"' ";
				
				//登录账号,可以加到分组后，
				if(openo !=null && !"".equals(openo))
				{
					sql += " and openo ='"+openo+"' ";
				}
				//加入登录账号的区域限制
				String ssql = new UtilFunctionDao().getTyadminOperatorByJdbcArea(zylevel, "o");
				if(ssql !=null && ssql.length() >0){
					sql +="  and o.levelid in ( ";
					sql += ssql.toString();
					sql += " ) ";
				}
		}
		if(loginType !=null && loginType.equals("1"))//登录
		{
			sql = " select o.openo,l.businessdepartment,l.warzone,l.area,l.branchactiongroup, "+
				 " to_char(max(c.lgtm),'yyyy-mm-dd hh24:mi:ss') as lgtm ,count(o.openo) as ctop  from  "+ 
			 "  (select t.openo as op ,t.log_time as lgtm  from tyadmin_logs t  "+
			 "  where t.log_des like'%登陆系统%' and t.servicekey=50  ) c,  tyadmin_operators o,ssmn_zy_level l "+
			 "  where c.op=o.openo and o.levelid=l.id ";			
			//登录时间
			if(starttime !=null && !"".equals(starttime))
			{
				sql +=" and to_char(c.lgtm,'yyyy-mm-dd') >='";
				sql += starttime;
				sql +="' ";
			}
			if(endtime !=null && !"".equals(endtime))
			{
				sql +=" and to_char(c.lgtm,'yyyy-mm-dd') <='";
				sql += endtime;
				sql +="' ";
			}
			//事业部
			if(busDep != null && !busDep.equals("")  )
			{
				sql += " and l.businessdepartment ='"+busDep+"' ";
			}
			//战区
			if(zoneparam !=null && !"".equals(zoneparam) && zoneparam.length()>0)
				sql += " and l.warzone ='"+zoneparam+"' ";
			//片区
			if(areaparam !=null && !"".equals(areaparam) && areaparam.length()>0)
				sql += " and l.area ='"+areaparam+"' ";
			//行动组别
			if(bagparam !=null && !"".equals(bagparam) && bagparam.length()>0)
				sql += " and l.branchactiongroup ='"+bagparam+"' ";
			//登录账号
			if(openo !=null && !"".equals(openo))
			{
				sql += " and openo ='"+openo+"' ";
			}
			//加入登录账号的区域限制
			String ssql = new UtilFunctionDao().getTyadminOperatorByJdbcArea(zylevel, "o");
			if(ssql !=null && ssql.length() >0){
				sql +="  and o.levelid in ( ";
				sql += ssql.toString();
				sql += " ) ";
			}
			
			sql += " group by o.openo ,l.businessdepartment,l.warzone,l.area,l.branchactiongroup ";
			
			sql = " select count(openo) as cn from (" +sql +" ) ";
		}
		if(loginType !=null && loginType.equals("2"))//未登录
		{
			sql =" select   count(openo) as cn from ( "+
			 " select o.openo,l.businessdepartment,l.warzone,l.area,l.branchactiongroup ,max(p.log_time) as lgtm  from tyadmin_operators o "+
			 " inner join ssmn_zy_level l on o.levelid=l.id  "+
			 " and  o.openo not in (select distinct(t.openo)  from tyadmin_logs t "+  
			 " where t.log_des like'%登陆系统%' and t.servicekey=50 ";
		//登录时间
		if(starttime !=null && !"".equals(starttime))
		{
			sql +=" and to_char(t.log_time,'yyyy-mm-dd') >='";
			sql += starttime;
			sql +="' ";
			}
			if(endtime !=null && !"".equals(endtime))
			{
				sql +=" and to_char(t.log_time,'yyyy-mm-dd') <='";
				sql += endtime;
				sql +="' ";
			}
			
			sql += " ) ";
			
			//加入登录账号的区域限制
			String ssql = new UtilFunctionDao().getTyadminOperatorByJdbcArea(zylevel, "o");
			if(ssql !=null && ssql.length() >0){
				sql +="  and o.levelid in ( ";
				sql += ssql.toString();
				sql += " ) ";
			}
			 
	     sql += "  left join tyadmin_logs p  on o.openo =p.openo  "+
	     		" group by o.openo,l.businessdepartment,l.warzone,l.area,l.branchactiongroup ) where 1=1 ";
	     //事业部
			if(busDep != null && !busDep.equals(""))
			{
				sql += " and  businessdepartment ='"+busDep+"' ";
			}
			//战区
			if(zoneparam !=null && !"".equals(zoneparam) && zoneparam.length()>0)
				sql += " and warzone ='"+zoneparam+"' ";
			//片区
			if(areaparam !=null && !"".equals(areaparam) && areaparam.length()>0)
				sql += " and area ='"+areaparam+"' ";
			//行动组别
			if(bagparam !=null && !"".equals(bagparam) && bagparam.length()>0)
				sql += " and branchactiongroup ='"+bagparam+"' ";
			//登录账号
			if(openo !=null && !"".equals(openo))
			{
				sql += " and openo ='"+openo+"' ";
			}
		}
						
		try {
			conn = session.connection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs != null) {
				while (rs.next()) {
					count += rs.getInt("cn");
				}
				}
		} catch (Exception he) {
			logger.error("-------------countOpenoLoginDetail---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
				if(rs !=null)
					rs.close();
				if(conn !=null)
					conn.close();
			} catch (HibernateException he2) {
				logger.error("-------------countOpenoLoginDetail---关闭Session时出现HibernateException-----",he2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}
//flag 表示: 如果是1,表示登录详情的首页中查询听录音的次数,这时传的时间是查询条件,loginTime与endTime一样时,endTime要加等号
//flag 为0时候, 是详情查询,loginTime与endTime一样时 ,endTim要写当前时间
// sqlsub :为"", "A+" ,"渠道" ,这里区分 A+还是渠道的
public int findCdrCount(String openo,String loginTime,String endTime,int flag,String sqlsub)
{
	String sql="";
	
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	int count = 0;
	//检测数据库连接是否成功
	if (session == null) {
		throw new DBException("fail to connect db");
	}
	
	sql = "select count(o.openo) as cn from tyadmin_logs o "+
			"  where o.openo='";
	sql +=openo;
	sql +="' and  o.log_type like '%"+sqlsub+"%' and o.servicekey=50 "; //听录音次数	
	if(loginTime !=null && !"".equals(loginTime) && loginTime.length()>0)
	{
		//年月日时分秒格式
		if(loginTime.length() >10)
			sql += " and to_char(o.log_time,'yyyy-mm-dd hh24:mi:ss') >='"+loginTime+"' ";
		else
			sql += " and to_char(o.log_time,'yyyy-mm-dd') >='"+loginTime+"' ";
		
	}
	if(flag ==0)
	{//查登录详情,这时是按登录时间算出来的,所以不能加等号
		if(endTime !=null && !endTime.equals("") && endTime.length()>0 && !endTime.equals(loginTime))//开始时间等于结束时间时,将结束时间视为当前时间(等同于不写)
		{
			if(endTime.length()>10)
				sql += " and to_char(o.log_time,'yyyy-mm-dd hh24:mi:ss') < '"+endTime+"' ";
			else
				sql += " and to_char(o.log_time,'yyyy-mm-dd') < '"+endTime+"' ";
		}
	}else
	{//这时间是查询条件,要加等号的
		if(endTime !=null && !endTime.equals("") && endTime.length()>0)
		{
			if(endTime.length()>10)
				sql += " and to_char(o.log_time,'yyyy-mm-dd hh24:mi:ss') <= '"+endTime+"' ";
			else
				sql += " and to_char(o.log_time,'yyyy-mm-dd') <= '"+endTime+"' ";
		}
	}
						
	try {
		conn = session.connection();
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		if(rs != null) 
		{
			while (rs.next()) 
			{
				count = rs.getInt("cn");
				break;
			}
		}
	} catch (Exception he) {
		logger.error("-------------findCdrCount---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
			if(rs !=null)
				rs.close();
			if(conn !=null)
				conn.close();
		} catch (HibernateException he2) {
			logger.error("-------------findCdrCount---关闭Session时出现HibernateException-----",he2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return count;
	
}

public String getLastTime(String stime,String sopera)
{
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String slogtime = "";
	if(sopera.equals("tfmyfh001"))
	{
		System.out.print("aaaa");
	}
	
	if(stime == null || stime.equals(""))
		return "";
	String sql = "select to_char(log_time,'yyyy-mm-dd hh24:mi:ss') as lt from(  select t.log_time from  tyadmin_logs t where "+
			"t.log_time>to_date('"+stime+"','yyyy-mm-dd hh24:mi:ss')  and t.log_type like '%操作员登陆%'  and t.servicekey=50 and t.openo='"+sopera + 
			"' order by t.log_time ) where rownum = 1 ";
	
	try {
		conn = session.connection();
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		if(rs != null) 
		{
			while (rs.next()) 
			{
				slogtime = rs.getString("lt");
				break;
			}
		}
		
	} catch (Exception he) {
		logger.error("-------------findCdrCount---出现Exception-----",he);
	} finally 
	{
		try 
		{
			if(session != null)
				session.close();
			if(rs !=null)
				rs.close();
			if(conn !=null)
				conn.close();
		} catch (HibernateException he2) {
			logger.error("-------------findCdrCount---关闭Session时出现HibernateException-----",he2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		return slogtime;
}

public List<TyadminOperatorLogs> findOpenoLoginDetailByOpeno(String openo,String starttime,String endtime,Integer index,Integer maxcount)
{
	String sql="";
	
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//检测数据库连接是否成功
	if (session == null) {
		throw new DBException("fail to connect db");
	}
	List<TyadminOperatorLogs> list = new ArrayList<TyadminOperatorLogs>();
	
	
	sql = " select lgtm, p.openo,l.businessdepartment,l.warzone,l.area,l.branchactiongroup ,ctop from ( ";
	sql += "select t.openo, to_char(t.log_time,'yyyy-mm-dd hh24:mi:ss') as lgtm ,0 as ctop from  "+
		" tyadmin_logs t where t.openo='" + openo +"'";
	
	if(starttime !=null && !starttime.equals("") && starttime.length()>0)
		sql += " and to_char(t.log_time,'yyyy-mm-dd') >= to_char(to_date('"+starttime+"','yyyy-mm-dd'),'yyyy-mm-dd') ";
	if(endtime != null && !endtime.equals("") && endtime.length()>0)
		sql += " and to_char(t.log_time,'yyyy-mm-dd') <= to_char(to_date('"+endtime+"','yyyy-mm-dd'),'yyyy-mm-dd') ";
	sql += "and t.log_type like '%操作员登陆%'  and t.servicekey="+Constants.servicekey_dc; 
	
	sql += " ) p left join tyadmin_operators s on p.openo=s.openo and s.servicekey= "+Constants.servicekey_dc+
		" left join ssmn_zy_level l on s.levelid =l.id  ";
	sql +=" order by p.openo desc  ,p.lgtm desc ";
	
	String sqll = "select  openo,businessdepartment,warzone,area,branchactiongroup, lgtm , ctop from ( "+
				" select openo,businessdepartment,warzone,area,branchactiongroup, lgtm , ctop ,rownum as rn from (" +
				sql +" ) ) where 1=1  ";
	if(index !=null && index >=0)
	{
		sqll +=" and rn >= ";
		sqll += index+1;
		sqll += "  and rn <= ";
		sqll += index+maxcount;
	}
			
	try {
		conn = session.connection();
		pstmt = conn.prepareStatement(sqll);
		rs = pstmt.executeQuery();
		if(rs != null) 
		{
			while (rs.next()) 
			{
				list.add(new TyadminOperatorLogs(rs.getString("openo"),rs.getString("businessdepartment"), 
					rs.getString("warzone"),rs.getString("area"),rs.getString("branchactiongroup"),
					rs.getString("lgtm"),rs.getInt("ctop")));
			}
		}
	} catch (Exception he) {
		logger.error("-------------findOpenoLoginDetailByOpeno---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
			if(rs !=null)
				rs.close();
			if(conn !=null)
				conn.close();
		} catch (HibernateException he2) {
			logger.error("-------------findOpenoLoginDetailByOpeno---关闭Session时出现HibernateException-----",he2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	return list;
	
}

public int findOpenoLoginDetailByOpenoCount(String openo,String starttime,String endtime)
{
	String sql="";
	
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	int count=0;
	//检测数据库连接是否成功
	if (session == null) {
		throw new DBException("fail to connect db");
	}
	List<TyadminOperatorLogs> list = new ArrayList<TyadminOperatorLogs>();
	
	
	sql = " select count(p.openo) as cn from ( ";
	sql += "select t.openo, to_char(t.log_time,'yyyy-mm-dd hh24:mi:ss') as lgtm ,0 as ctop from  "+
		" tyadmin_logs t where t.openo='" + openo +"'";
	
	if(starttime !=null && !starttime.equals("") && starttime.length()>0)
		sql += " and to_char(t.log_time,'yyyy-mm-dd') >= to_char(to_date('"+starttime+"','yyyy-mm-dd'),'yyyy-mm-dd') ";
	if(endtime != null && !endtime.equals("") && endtime.length()>0)
		sql += " and to_char(t.log_time,'yyyy-mm-dd') <= to_char(to_date('"+endtime+"','yyyy-mm-dd'),'yyyy-mm-dd') ";
	sql += "and t.log_type like '%操作员登陆%'  and t.servicekey=50 "; 
	
	sql += " ) p left join tyadmin_operators s on p.openo=s.openo and s.servicekey=50 "+
		" left join ssmn_zy_level l on s.levelid =l.id  ";
				
	try {
		conn = session.connection();
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		if(rs != null) 
		{
			while (rs.next()) 
			{
				count= rs.getInt("cn");
				break;
			}
		}
	} catch (Exception he) {
		logger.error("-------------findOpenoLoginDetailByOpenoCount---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
			if(rs !=null)
				rs.close();
			if(conn !=null)
				conn.close();
		} catch (HibernateException he2) {
			logger.error("-------------findOpenoLoginDetailByOpenoCount---关闭Session时出现HibernateException-----",he2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	return count;
}

public List<TyadminOperatorLogs> findOpenoLoginDetailByExport(String starttime,String endtime,List<TyadminOperatorLogs> operalist)
{
	String sql="";
	
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//检测数据库连接是否成功
	if (session == null) {
		throw new DBException("fail to connect db");
	}
	List<TyadminOperatorLogs> list = new ArrayList<TyadminOperatorLogs>();
	if(operalist == null || operalist.size()<=0 )
		return list;//没有数据，不需要继续查
	
	sql = " select p.openo,l.businessdepartment,l.warzone,l.area,l.branchactiongroup ,lgtm,ctop from ( ";
	sql += "select t.openo, to_char(t.log_time,'yyyy-mm-dd hh24:mi:ss') as lgtm ,1 as ctop from  "+
		" tyadmin_logs t where 1=1  and ";
	
	String sqlTemp = "";
	for(int i=0;i< operalist.size(); i++)
	{
		TyadminOperatorLogs op = operalist.get(i);
		//有登录次数的才查详情，没有登录次数的，不需要查详情
		if(op.getLoginCount()>0 )
		{
			if(i==0)
				sqlTemp += "  t.openo='" + op.getOpeno() +"'";
			else
				sqlTemp += " or t.openo='" + op.getOpeno() +"'";
		}
	}
	if(!sqlTemp.equals("") && sqlTemp.length()>0)
		sql += "( " + sqlTemp +" ) ";
	
	if(starttime !=null && !starttime.equals("") && starttime.length()>0)
		sql += " and to_char(t.log_time,'yyyy-mm-dd') >= to_char(to_date('"+starttime+"','yyyy-mm-dd'),'yyyy-mm-dd') ";
	if(endtime != null && !endtime.equals("") && endtime.length()>0)
		sql += " and to_char(t.log_time,'yyyy-mm-dd') <= to_char(to_date('"+endtime+"','yyyy-mm-dd'),'yyyy-mm-dd') ";
	sql += "and t.log_type like '%操作员登陆%'  and t.servicekey=50 "; 
	
	sql += " ) p left join tyadmin_operators s on p.openo=s.openo and s.servicekey= "+Constants.servicekey_dc+
		" left join ssmn_zy_level l on s.levelid =l.id  ";
	sql +=" order by p.openo ,lgtm desc ";
	
	
			
	try {
		conn = session.connection();
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		if(rs != null) 
		{
			while (rs.next()) 
			{
				list.add(new TyadminOperatorLogs(rs.getString("openo"),rs.getString("businessdepartment"), 
					rs.getString("warzone"),rs.getString("area"),rs.getString("branchactiongroup"),
					rs.getString("lgtm"),rs.getInt("ctop")));
			}
		}
		//需要把其他账号，登录时间为空的，添加到后面
		for(int i=0;i< operalist.size(); i++)
		{
			TyadminOperatorLogs op = operalist.get(i);
			//if(op.getLastLoginTime() == null || op.getLastLoginTime().equals("") )
			if(op.getLoginCount() ==0 )
				list.add(op);
		}

		
	} catch (Exception he) {
		logger.error("-------------findOpenoLoginDetailByExport---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
			if(rs !=null)
				rs.close();
			if(conn !=null)
				conn.close();
		} catch (HibernateException he2) {
			logger.error("-------------findOpenoLoginDetailByExport---关闭Session时出现HibernateException-----",he2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	return list;
	
}

public List<String> findShowTypeByLevleid(long levelid)
{
	List<String> list =new ArrayList<String>();
	
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//检测数据库连接是否成功
	if (session == null) {
		throw new DBException("fail to connect db");
	}
	
	String sql = " select count(id) as cn ,max(auth_method) as aumeth from ( "+
		" select a.auth_method,g.id  from  tyadmin_authorities a,tyadmin_group_auths ga , "+
		" tyadmin_groups g where a.id =ga.auth_id "+
		" and ga.group_id =g.id and g.id in(select distinct(t.group_id)  from TYADMIN_OPERATORS t "+
		" where t.levelid="+levelid+" and t.servicekey ="+Constants.servicekey_dc
			+" ) and a.auth_method in ('showAChannel' ,'showChannel') ) group by id   ";
			
	try {
		conn = session.connection();
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		list.add("2");//初始化2(显示所有的)
		if(rs != null) 
		{
			while (rs.next()) 
			{
				int cn =rs.getInt("cn");
				String aumeth =rs.getString("aumeth");
				if(cn == 1 && aumeth.equals(Constants.TYPE_SHOWCHANNEL))
				{
					if(!list.contains("0"))
						list.add("0" );//0表示，只显示渠道数据
				}
				else if(cn ==1 && aumeth.equals(Constants.TYPE_SHOWA))
				{
					if(!list.contains("1"))
						list.add("1");//只显示A+数据
				}
				else
				{
					if(!list.contains("2"))
						list.add("2");//显示所有
				}
			}
		}
				
	} catch (Exception he) {
		logger.error("-------------findShowTypeByLevleid---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
			if(rs !=null)
				rs.close();
			if(conn !=null)
				conn.close();
		} catch (HibernateException he2) {
			logger.error("-------------findShowTypeByLevleid---关闭Session时出现HibernateException-----",he2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	return list;
}

public boolean updateOpera(String operaOld,String operaNew)
{
	boolean res = false;
	String sql="";
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	Transaction tx = null;

	//检测数据库连接是否成功
	if (session == null) {
		throw new DBException("fail to connect db");
	}
	
	sql = " update tyadmin_operators t set t.openo='"+operaNew+"' where t.openo='"+operaOld+"' ";
	
	try {
		
		conn = session.connection();
		tx = session.beginTransaction();
		pstmt = conn.prepareStatement(sql);
		pstmt.executeUpdate();
		 tx.commit();
		 res =true;		
	} catch (Exception he) {
		logger.error("-------------updateOpera---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
			if(conn !=null)
				conn.close();
		} catch (HibernateException he2) {
			tx.rollback();
			res =false;
			logger.error("-------------updateOpera---关闭Session时出现HibernateException-----",he2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			tx.rollback();
			res=false;
			e.printStackTrace();
		}
	}
	
	return res;
	
}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AdminOperatorImpl dao = AdminOperatorImpl.getInstance();
		List<SsmnZyLevel> list = dao.groupByProvinOpeno("50");
			for(int i=0;i<list.size();i++){
				System.out.println("-----------getName-- -----"+list.get(i).getProvincecity());
			}
	}
}
