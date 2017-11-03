package com.dascom.ssmn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dascom.OPadmin.dao.impl.RootDao;
import com.dascom.OPadmin.util.HibernateSessionFactory;
import com.dascom.ssmn.entity.SsmnZyClientnum;
import com.dascom.ssmn.entity.SsmnZyLevel;

public class CallDao extends RootDao{
	private static final Logger logger =  Logger.getLogger(CallDao.class);
	private static CallDao dao;
	
	public synchronized static CallDao getInstance(){
		if(dao == null)
			dao = new CallDao();
		return dao;
	}
	
	@Override
	public Class<SsmnZyClientnum> getReferenceClass() {
		return SsmnZyClientnum.class;
	}
	
	public boolean addCall(SsmnZyClientnum le)
	{
		Session session = null;
		boolean isok = true;
		Transaction trans = null;
		try {
			session = this.adapter.openSession();
			trans = session.beginTransaction();
			session.save(le);
		
			trans.commit();

		} catch (Exception he) {
			logger.error("-------------addCall---出现Exception-----",he);
			isok = false;
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------addCall---关闭Session时出现HibernateException-----",he2);
			}
		}	
		return isok;
	}
	
	public boolean editCall(String cid,String sumsisdn,String suname,String scnum,String scname,String remark)
	{
		Session session = null;
		boolean isok = true;
		Transaction trans = null;
		try {
			session = this.adapter.openSession();
			
			SsmnZyClientnum zc=(SsmnZyClientnum)session.get(SsmnZyClientnum.class, Long.parseLong(cid));
			if(sumsisdn !=null && sumsisdn.length()>0)
				zc.setUsermsisdn(sumsisdn);
			if(suname !=null && suname.length()>0)
				zc.setUsername(suname);
			if(scnum !=null && scnum.length()>0)
				zc.setClientnum(scnum);
			if(scname !=null && scname.length()>0)
				zc.setClientname(scname);
			if(remark !=null && remark.length()>0)
				zc.setRemark(remark);
			zc.setUpdatetime(new Date());
			zc.setStatus(3);
			
			trans = session.beginTransaction();
			session.update(zc);		
			trans.commit();

		} catch (Exception he) {
			logger.error("-------------editCall---出现Exception-----",he);
			isok = false;
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------addCall---关闭Session时出现HibernateException-----",he2);
			}
		}	
		return isok;
	}
	
	/*
	 * 将该记录 更换到其他 业务员下
	 * 先新创建个status =1，再假删除 status=2
	 */
	public boolean switchToNewUser(String cid,String sumsisdn,String suname,String scnum,String scname,String remark)
	{
		Session session = null;
		boolean isok = true;
		Transaction trans = null;
		try {
			session = this.adapter.openSession();
			trans =session.beginTransaction();
			SsmnZyClientnum zc=(SsmnZyClientnum)session.get(SsmnZyClientnum.class, Long.parseLong(cid));
			
			//先创建
			SsmnZyClientnum newzc =new SsmnZyClientnum();
			newzc.setUsermsisdn(sumsisdn);
			newzc.setUsername(suname);
			newzc.setClientnum(scnum);
			newzc.setClientname(scname);
			newzc.setIntime(zc.getIntime());
			newzc.setCalltime(zc.getCalltime());
			newzc.setRemark(zc.getRemark());
			newzc.setStatus(1);
			newzc.setUpdatetime(new Date());
			session.save(newzc);
			
			//假删除status=2
			zc.setStatus(2);
			zc.setUpdatetime(new Date());

			session.update(zc);		
			trans.commit();
			isok =true;
		} catch (Exception he) {
			logger.error("-------------switchToNewUser---出现Exception-----",he);
			isok = false;
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------switchToNewUser---关闭Session时出现HibernateException-----",he2);
			}
		}	
		return isok;
	}
	
	/*
	 * 这里假删除，只是将status设置成2
	 */
	public boolean delCall(String cid)
	{
		Session session = null;
		boolean isok = true;
		Transaction trans = null;
		try {
			session = this.adapter.openSession();
			
			SsmnZyClientnum zc=(SsmnZyClientnum)session.get(SsmnZyClientnum.class, Long.parseLong(cid));
			zc.setStatus(2);
			zc.setUpdatetime(new Date());
			trans = session.beginTransaction();
			session.save(zc);		
			trans.commit();

		} catch (Exception he) {
			logger.error("-------------delCall---出现Exception-----",he);
			isok = false;
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------delCall---关闭Session时出现HibernateException-----",he2);
			}
		}	
		return isok;
	}
	
	/**
	 * 查询登录账号区域内经纪人对应的客户电话
	 * @param  
	 * @return
	 */
	public List<SsmnZyClientnum> getClientNumAll(SsmnZyLevel level,String startdate,Integer index, Integer maxCount){
		Session session = null;
		List<SsmnZyClientnum> list = new ArrayList<SsmnZyClientnum>();
		StringBuffer strHQL = new StringBuffer("");
		try {
			strHQL.append(" select distinct c.id ,c.usermsisdn ,c.username," +
					"c.clientnum,c.clientname,c.remark,c.calltime from " +
					" SsmnZyClientnum c,SsmnZyUser u,SsmnZyLevel le where" +
					" c.usermsisdn=u.msisdn and u.levelid=le.id and c.status !=2 ");//2是删除了
			//按天查询,默认显示当天的
			if(startdate !=null && startdate.length()>0)
				strHQL.append(" and to_char(c.calltime,'yyyy-mm-dd') = :startdate ");
			else
				strHQL.append(" and to_char(c.calltime,'yyyy-mm-dd') = to_char(sysdate,'yyyy-mm-dd') ");
			
			String ssql = new UtilFunctionDao().getTyadminOperatorSelect(level,"","");
			if(ssql.length() >0){
				strHQL.append(ssql);
			}
			strHQL.append(" order by c.usermsisdn ,c.id ");
			
			session = this.adapter.openSession();			
			Query query = session.createQuery(strHQL.toString());
			if(startdate !=null && startdate.length()>0)
				query.setString("startdate",startdate );
			new UtilFunctionDao().setOperatorAttribute(level,query);
			if(index !=null && index >0)
				query.setFirstResult(index);
			if(maxCount !=null && maxCount >0)
				query.setMaxResults(maxCount);
			ScrollableResults rs =query.scroll();
			while(rs.next())
			{
				list.add(new SsmnZyClientnum(rs.getLong(0),rs.getString(1),rs.getString(2),rs.getString(3),
						rs.getString(4),rs.getString(5),rs.getDate(6)));
			}
		} catch (Exception he) {
			logger.error("-------------getClientNumAll---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getClientNumAll---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
	}
	
	public int getClientNumAllCount(SsmnZyLevel level,String startdate){
		Session session = null;
		List<SsmnZyClientnum> list = new ArrayList<SsmnZyClientnum>();
		StringBuffer strHQL = new StringBuffer("");
		try {
			//,c.usermsisdn,c.username,c.clientnum,c.clientname,c.remark
			strHQL.append(" select distinct c.id  from " +
					" SsmnZyClientnum c,SsmnZyUser u,SsmnZyLevel le where " +
					"c.usermsisdn=u.msisdn and u.levelid=le.id and c.status !=2 ");
			//按天查询,默认显示当天的
			if(startdate !=null && startdate.length()>0)
				strHQL.append(" and to_char(c.calltime,'yyyy-mm-dd') = :startdate ");
			else
				strHQL.append(" and to_char(c.calltime,'yyyy-mm-dd') = to_char(sysdate,'yyyy-mm-dd') ");
			
			String ssql = new UtilFunctionDao().getTyadminOperatorSelect(level,"","");
			if(ssql.length() >0)
				strHQL.append(ssql);
	
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			new UtilFunctionDao().setOperatorAttribute(level,query);
			if(startdate !=null && startdate.length()>0)
				query.setString("startdate",startdate );
			list = query.list();
		} catch (Exception he) {
			logger.error("-------------getClientNumAllCount---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getClientNumAllCount---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list.size();
	}
	
	public List<SsmnZyClientnum> getClientNumByUsermsisdn(String usermsisdn,String clientnum,String clientname){
		Session session = null;
		List<SsmnZyClientnum> list = new ArrayList<SsmnZyClientnum>();
		StringBuffer strHQL = new StringBuffer("");
		try {
			strHQL.append("  from SsmnZyClientnum c where 1=1 ");
			if(usermsisdn !=null && usermsisdn.length()>0)
				strHQL.append(" and c.usermsisdn =:usermsisdn ");
			if(clientnum !=null && clientnum.length()>0)
				strHQL.append(" and c.clientnum =:clientnum ");
			if(clientname !=null && clientname.length()>0)
				strHQL.append(" and c.clientname =:clientname ");
			session = this.adapter.openSession();			
			Query query = session.createQuery(strHQL.toString());
			
			if(usermsisdn !=null && usermsisdn.length()>0)
				query.setString("usermsisdn", usermsisdn);
			if(clientnum !=null && clientnum.length()>0)
				query.setString("clientnum", clientnum);
			if(clientname !=null && clientname.length()>0)
				query.setString("clientname", clientname);
			list =query.list();
		} catch (Exception he) {
			logger.error("-------------getClientNumAll---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getClientNumAll---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
	}
	
	public static void main(String[] args) {
		
		
	}
	
	
}
