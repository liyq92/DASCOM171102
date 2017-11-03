package com.dascom.ssmn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dascom.OPadmin.dao.impl.RootDao;
import com.dascom.OPadmin.util.HibernateSessionFactory;
import com.dascom.ssmn.entity.SsmnZyLevel;

public class LevelDao extends RootDao{
	private static final Logger logger =  Logger.getLogger(LevelDao.class);
	private static LevelDao dao;
	
	public synchronized static LevelDao getInstance(){
		if(dao == null)
			dao = new LevelDao();
		return dao;
	}
	
	
	@Override
	public Class<SsmnZyLevel> getReferenceClass() {
		return SsmnZyLevel.class;
	}
	
	/**
	 * 查询等级
	 * @param type  查询类型，1：事业部，2：战区，3：片区，4：分行行动组
	 * @param levelid  操作员的等级id
	 * @return
	 */
	public List<String> getLevelByTypeAndID(int type, Long levelid){
		Session session = null;
		List<String> list = new ArrayList<String>();
		StringBuffer strHQL = new StringBuffer("");
		try {
			if(type == 1){  //查询businessdepartment
				strHQL.append("select distinct(e1.businessdepartment) from SsmnZyLevel e1, SsmnZyLevel e2  where e1.provincecity = e2.provincecity and e1.company = e2.company and e2.id =:levelid ");
				strHQL.append("and e1.provincecity is not null and e1.company is not null and e1.businessdepartment is not null ");
			}else if(type == 2){//查询warzone
				strHQL.append("select  distinct(e1.warzone) from SsmnZyLevel e1, SsmnZyLevel e2  where e1.provincecity = e2.provincecity and e1.company = e2.company and e1.businessdepartment = e2.businessdepartment and e2.id =:levelid ");				
				strHQL.append("and e1.provincecity is not null and e1.company is not null and e1.businessdepartment is not null and e1.warzone is not null ");
			}else if(type == 3){//查询area
				strHQL.append("select  distinct(e1.area) from SsmnZyLevel e1, SsmnZyLevel e2  where e1.provincecity = e2.provincecity and e1.company = e2.company and e1.businessdepartment = e2.businessdepartment and e1.warzone = e2.warzone and e2.id =:levelid ");				
				strHQL.append("and e1.provincecity is not null and e1.company is not null and e1.businessdepartment is not null and e1.warzone is not null and e1.area is not null ");
			}else {//查询branchactiongroup
				strHQL.append("select  distinct(e1.branchactiongroup) from SsmnZyLevel e1, SsmnZyLevel e2  where e1.provincecity = e2.provincecity and e1.company = e2.company and e1.businessdepartment = e2.businessdepartment and e1.warzone = e2.warzone and e1.area = e2.area and e2.id =:levelid ");				
				strHQL.append(" and e1.provincecity is not null and e1.company is not null  and e1.businessdepartment is not null and e1.warzone is not null and e1.area is not null and e1.branchactiongroup is not null");
			}
			//排序
			if(type == 1)
				strHQL.append(" order by e1.businessdepartment ");
			else if(type == 2)
				strHQL.append(" order by e1.warzone ");
			else if(type == 3)
				strHQL.append(" order by e1.area ");
			else
				strHQL.append(" order by e1.branchactiongroup ");
			
//			logger.info("======sql======="+strHQL.toString());
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			query.setLong("levelid", levelid);
			list = query.list();
		} catch (Exception he) {
			logger.error("-------------getLevelByTypeAndID---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getLevelByTypeAndID---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
	}
	
	
	
	/**
	 * 查找选中的级别对应的下一级别
	 * @param type 1：事业部，2：战区，3：片区，4：分行行动组
	 * @param levelid  操作员级别ＩＤ
	 * @param businessdepartment　　不能为空
	 * @param warzone　　查出战区时可为空
	 * @param area　　　查询片区可为空
	 * @return
	 */
	public List<String> getSelectLevel(int type, Long levelid, String businessdepartment, String warzone, String area){
		Session session = null;
		List<String> list = new ArrayList<String>();
		StringBuffer strHQL = new StringBuffer("");
		try {
			if(type == 1){//查询businessdepartment的下一级，即warzone
				strHQL.append("select  distinct(e1.warzone)   ");				
			}else if(type == 2){//查询warzone的下一级，即area
				strHQL.append("select  distinct(e1.area)   ");				
			}else {//查询area的下一级，即branchactiongroup
				strHQL.append("select  distinct(e1.branchactiongroup)   ");				
			}
			strHQL.append(" from SsmnZyLevel e1, SsmnZyLevel e2  where e1.provincecity = e2.provincecity and e1.company = e2.company and e2.id =:levelid ");	
			if(businessdepartment != null && !"".equals(businessdepartment)){
				strHQL.append(" and e1.businessdepartment =:businessdepartment ");	
			}
			if(warzone != null && !"".equals(warzone)){
				strHQL.append(" and e1.warzone =:warzone ");	
			}
			if(area != null && !"".equals(area)){
				strHQL.append(" and e1.area =:area ");	
			}
			if(type == 1)
				strHQL.append("and e1.businessdepartment is not null and e1.warzone is not null ");
			else if(type == 2)
				strHQL.append("and e1.businessdepartment is not null and e1.warzone is not null and e1.area is not null ");
			else
				strHQL.append("and e1.businessdepartment is not null and e1.warzone is not null and e1.area is not null and e1.branchactiongroup is not null");
			//排序
			if(type == 1)
				strHQL.append(" order by e1.warzone ");
			else if(type == 2)
				strHQL.append(" order by e1.area    ");				
			else
				strHQL.append(" order by e1.branchactiongroup    ");		
			
//			logger.info("======sql======="+strHQL.toString());
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			
			query.setLong("levelid", levelid);
			if(businessdepartment != null && !"".equals(businessdepartment)){
				query.setString("businessdepartment", businessdepartment);
			}
			if(warzone != null && !"".equals(warzone)){
				query.setString("warzone", warzone);
			}
			if(area != null && !"".equals(area)){
				query.setString("area", area);
			}
			list = query.list();
		} catch (Exception he) {
			logger.error("-------------getLevelByTypeAndID---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getLevelByTypeAndID---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
	}
	
	
	public Long getLevelIDByProperty(String provincecity, String company,String businessdepartment, String warzone, String area, String branchactiongroup){
		Session session = null;
		StringBuffer strHQL = new StringBuffer("");
		Long ret = null;
		try {
			strHQL.append("select e1.id  from SsmnZyLevel e1 where e1.provincecity =:provincecity and e1.company =:company ");
			strHQL.append(" and e1.businessdepartment =:businessdepartment and e1.warzone =:warzone and e1.area =:area and e1.branchactiongroup=:branchactiongroup ");

			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			
			query.setString("provincecity", provincecity);
			query.setString("company", company);
			query.setString("businessdepartment", businessdepartment);
			query.setString("warzone", warzone);
			query.setString("area", area);
			query.setString("branchactiongroup", branchactiongroup);
			logger.info("======sql=====:---=="+strHQL.toString()+"------provincecity:"+provincecity+"------company:"+company+"-------businessdepartment:"+businessdepartment+
					"--warzone---"+warzone+"------area-"+area+"----branchactiongroup"+branchactiongroup);
			ret = (Long) query.uniqueResult();
		} catch (Exception he) {
			logger.error("-------------getLevelIDByProperty---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getLevelIDByProperty---关闭Session时出现HibernateException-----",he2);
			}
		}
		return ret;
	}
	
	/**
	 * 查询等级ID
	 * @param branchactiongroup 分行行动组
	 * @return
	 */
	public Long getLevelIDByActionGroup(String branchactiongroup){
		Session session = null;
		StringBuffer strHQL = new StringBuffer("");
		Long ret = null;
		List<SsmnZyLevel> list = null;
		try {
			strHQL.append(" from SsmnZyLevel e1 where e1.branchactiongroup=:branchactiongroup ");
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			query.setString("branchactiongroup", branchactiongroup);
			list = query.list();
			if(list == null)	
				ret = null;
			if(list != null && list.size() >0)
				ret = list.get(0).getId();
			
		} catch (Exception he) {
			logger.error("-------------getLevelIDByActionGroup---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getLevelIDByActionGroup---关闭Session时出现HibernateException-----",he2);
			}
		}
		return ret;
	}
	
	
	public SsmnZyLevel  getBranchactiongroupNameById(String levelid){
		Session session = null;
		StringBuffer strHQL = new StringBuffer("");
		SsmnZyLevel ret = null;
		List<SsmnZyLevel> list = null;
		try {
			strHQL.append(" from SsmnZyLevel e1 where e1.id = :levelid ");
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			query.setString("levelid", levelid);
			list = query.list();
			if(list == null)	
				ret = null;
			if(list != null && list.size() >0)
				ret = list.get(0);
			
		} catch (Exception he) {
			logger.error("-------------getLevelIDByActionGroup---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getLevelIDByActionGroup---关闭Session时出现HibernateException-----",he2);
			}
		}
		return ret;
	}
	
public Long	getLevelidByarea(String businessdepartment, String warzone, String area, String branchactiongroup)
	{		
		Session session = null;
		StringBuffer strHQL = new StringBuffer("");
		Long ret = null;
		List<SsmnZyLevel> list = null;
		try {
			session = this.adapter.openSession();
			strHQL.append(" from SsmnZyLevel e where 1=1 ");
			
			if(branchactiongroup !=null && !"".equals(branchactiongroup) )
			{
				strHQL.append(" and e.businessdepartment = :businessdepartment ");
				strHQL.append(" and e.warzone = :warzone ");
				strHQL.append(" and e.area = :area ");
				strHQL.append(" and e.branchactiongroup = :branchactiongroup ");
			}
			if(area != null && !"".equals(area) && (branchactiongroup ==null || "".equals(branchactiongroup)) )
			{
				strHQL.append(" and e.businessdepartment = :businessdepartment ");
				strHQL.append(" and e.warzone = :warzone ");
				strHQL.append(" and e.area =:area  ");
				strHQL.append(" and e.branchactiongroup is null ");
			}
			if(warzone != null && !"".equals(warzone) && (area == null ||  "".equals(area)) )
			{
				strHQL.append(" and e.businessdepartment = :businessdepartment ");
				strHQL.append(" and e.warzone = :warzone ");
				strHQL.append(" and e.area is null ");
				strHQL.append(" and e.branchactiongroup is null ");
			}
			if(businessdepartment != null && !"".equals(businessdepartment) && (warzone == null || "".equals(warzone)) )
			{
				strHQL.append(" and e.businessdepartment =:businessdepartment ");
				strHQL.append(" and e.warzone  is null  ");
				strHQL.append(" and e.area is null ");
				strHQL.append(" and e.branchactiongroup is null ");
			}
			
			Query query = session.createQuery(strHQL.toString());
			if(branchactiongroup !=null && !"".equals(branchactiongroup) )
			{
				query.setString("businessdepartment", businessdepartment);
				query.setString("warzone", warzone);
				query.setString("area", area);
				query.setString("branchactiongroup", branchactiongroup);
				
			}
			if(area != null && !"".equals(area) && (branchactiongroup ==null || "".equals(branchactiongroup)) )
			{
				query.setString("businessdepartment", businessdepartment);
				query.setString("warzone", warzone);
				query.setString("area", area);
			}
			if(warzone != null && !"".equals(warzone) && (area == null ||  "".equals(area)) )
			{
				query.setString("businessdepartment", businessdepartment);
				query.setString("warzone", warzone);
			}
			if(businessdepartment != null && !"".equals(businessdepartment) && (warzone == null || "".equals(warzone)) )
			{
				query.setString("businessdepartment", businessdepartment);
			}
			

			list = query.list();
			
			if(list != null && list.size() >0)
				ret = list.get(0).getId();
			
		} catch (Exception he) {
			logger.error("-------------getLevelIDByActionGroup---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getLevelIDByActionGroup---关闭Session时出现HibernateException-----",he2);
			}
		}
		
		return ret;
	}
	
public Long	getMaxLevelidByarea()
{		
	Session session = null;
	StringBuffer strHQL = new StringBuffer("");
	Long ret = null;
	List<SsmnZyLevel> list = null;
	try {
		session = this.adapter.openSession();
		strHQL.append("  from SsmnZyLevel e  order by id desc ");
		
		
		Query query = session.createQuery(strHQL.toString());

		list = query.list();
		
		if(list != null && list.size() >0)
			ret = list.get(0).getId()+1;
		
	} catch (Exception he) {
		logger.error("-------------getLevelIDByActionGroup---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getLevelIDByActionGroup---关闭Session时出现HibernateException-----",he2);
		}
	}
	
	return ret;
}

public List<SsmnZyLevel> getLevelList(Integer index, Integer maxCount,Long operalevelid,String bdparam,String zoneparam,String areaparam,String bagparam)
{
	Session session = null;
	List<SsmnZyLevel> list = null;
	try {
		session = this.adapter.openSession();
		String sql=" from SsmnZyLevel l where 1=1 ";
		
		if(operalevelid >0)//保证是登录的操作员公司下面的
			sql +=" and l.provincecity=(select provincecity from SsmnZyLevel where id= :operalevelid1)  and l.company =( select company from SsmnZyLevel where id= :operalevelid2 ) ";
		//事业部
		if(bdparam !=null && !"".equals(bdparam) && bdparam.length()>0)
			sql += " and l.businessdepartment =:bdparam "; 
		//战区
		if(zoneparam !=null && !"".equals(zoneparam) && zoneparam.length()>0)
			sql +=" and l.warzone =:zoneparam ";
		//片区
		if(areaparam !=null && !"".equals(areaparam) && areaparam.length()>0)
			sql +=" and l.area =:areaparam ";
		//行动组
		if(bagparam !=null && !"".equals(bagparam) && bagparam.length()>0)
			sql +=" and l.branchactiongroup =:bagparam ";
		//保证是公司下的,去除公司
		sql +=" and l.businessdepartment is not null ";
		
		Query query = session.createQuery(sql);
		if(index !=null && index >0)
			query.setFirstResult(index);
		if(maxCount !=null && maxCount >0)
			query.setMaxResults(maxCount);
		if(operalevelid >0)
		{
			query.setLong("operalevelid1", operalevelid);
			query.setLong("operalevelid2", operalevelid);
		}
		if(bdparam !=null && !"".equals(bdparam) && bdparam.length()>0)
			query.setString("bdparam", bdparam);
		if(zoneparam !=null && !"".equals(zoneparam) && zoneparam.length()>0)
			query.setString("zoneparam", zoneparam);
		if(areaparam !=null && !"".equals(areaparam) && areaparam.length()>0)
			query.setString("areaparam", areaparam);
		if(bagparam !=null && !"".equals(bagparam) && bagparam.length()>0)
			query.setString("bagparam", bagparam);
		
		list = query.list();
	} catch (Exception he) {
		logger.error("-------------getLevelList---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getLevelList---关闭Session时出现HibernateException-----",he2);
		}
	}
	return list;
}

//1:判断事业部
public List<SsmnZyLevel> checkLevelEst(String aram,int type, Long operalevelid)
{
	Session session = null;
	List<SsmnZyLevel> list = null;
	try {
		session = this.adapter.openSession();
		String sql=" from SsmnZyLevel l where 1=1 ";
		if(operalevelid >0)//保证是登录的操作员公司下面的
			sql +=" and l.provincecity=(select provincecity from SsmnZyLevel where id= :operalevelid1)  and l.company =( select company from SsmnZyLevel where id= :operalevelid2 ) ";
		if(type == 1)
		{
			//事业部
			if(aram !=null && !"".equals(aram) && aram.length()>0)
				sql += " and l.businessdepartment =:aram "; 
		}
		if(type == 2)
		{
			//战区
			if(aram !=null && !"".equals(aram) && aram.length()>0)
				sql +=" and l.warzone =:aram ";
		}
		if(type == 3)
		{
			//片区
			if(aram !=null && !"".equals(aram) && aram.length()>0)
				sql +=" and l.area =:aram ";
		}
		if(type == 4)
		{
			//行动组
			if(aram !=null && !"".equals(aram) && aram.length()>0)
				sql +=" and l.branchactiongroup =:aram ";
		}
		Query query = session.createQuery(sql);
		if(operalevelid >0)
		{
			query.setLong("operalevelid1", operalevelid);
			query.setLong("operalevelid2", operalevelid);
		}
		if(aram !=null && !"".equals(aram) && aram.length()>0)
			query.setString("aram", aram);

		list = query.list();
	} catch (Exception he) {
		logger.error("-------------checkLevelEst---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------checkLevelEst---关闭Session时出现HibernateException-----",he2);
		}
	}
	return list;
}

public boolean delLevel(String mid)
{
	Session session = null;
	boolean isok = true;
	Transaction trans = null;
	try {
		session = this.adapter.openSession();
		trans = session.beginTransaction();
		//删除级别
		String sql =" delete from SsmnZyLevel   where 1=1 ";
		if(mid !=null && !"".equals(mid) && mid.length()>0)
			sql += " and  id =:mid"; 
		Query query = session.createQuery(sql);
		if(mid !=null && !"".equals(mid) && mid.length()>0)
			query.setLong("mid",Long.parseLong(mid) );
		int res =query.executeUpdate();
		
		//删除账号
		String sqlOper = " delete from  TyadminOperators where levelid= :lid";
		Query queryOper = session.createQuery(sqlOper);
		queryOper.setLong("lid", Long.parseLong(mid));
		res = queryOper.executeUpdate();
		
		if(res >=0)
			trans.commit();
		else
			trans.rollback();
	} catch (Exception he) {
		logger.error("-------------delLevel---出现Exception-----",he);
		isok = false;
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------delLevel---关闭Session时出现HibernateException-----",he2);
		}
	}	
	return isok;
}

public boolean addLevel(SsmnZyLevel le)
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
		logger.error("-------------addLevel---出现Exception-----",he);
		isok = false;
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------addLevel---关闭Session时出现HibernateException-----",he2);
		}
	}	
	return isok;
}

public int getLevelListCount(Long operalevelid,String bdparam,String zoneparam,String areaparam,String bagparam)
{
	Session session = null;
	List<SsmnZyLevel> list = null;
	int resCount=0;
	try {
		session = this.adapter.openSession();
		String sql=" from SsmnZyLevel l where 1=1 ";
		
		if(operalevelid >0)//保证是登录的操作员公司下面的
			sql +=" and l.provincecity=(select provincecity from SsmnZyLevel where id= :operalevelid1)  and l.company =( select company from SsmnZyLevel where id= :operalevelid2 ) ";
		//事业部
		if(bdparam !=null && !"".equals(bdparam) && bdparam.length()>0)
			sql += " and l.businessdepartment =:bdparam "; 
		//战区
		if(zoneparam !=null && !"".equals(zoneparam) && zoneparam.length()>0)
			sql +=" and l.warzone =:zoneparam ";
		//片区
		if(areaparam !=null && !"".equals(areaparam) && areaparam.length()>0)
			sql +=" and l.area =:areaparam ";
		//行动组
		if(bagparam !=null && !"".equals(bagparam) && bagparam.length()>0)
			sql +=" and l.branchactiongroup =:bagparam ";
		
		//保证是公司下的,去除公司
		sql +=" and l.businessdepartment is not null ";
		
		Query query = session.createQuery(sql);
		if(operalevelid >0)
		{
			query.setLong("operalevelid1", operalevelid);
			query.setLong("operalevelid2", operalevelid);
		}
		if(bdparam !=null && !"".equals(bdparam) && bdparam.length()>0)
			query.setString("bdparam", bdparam);
		if(zoneparam !=null && !"".equals(zoneparam) && zoneparam.length()>0)
			query.setString("zoneparam", zoneparam);
		if(areaparam !=null && !"".equals(areaparam) && areaparam.length()>0)
			query.setString("areaparam", areaparam);
		if(bagparam !=null && !"".equals(bagparam) && bagparam.length()>0)
			query.setString("bagparam", bagparam);
		list = query.list();
	
	} catch (Exception he) {
		logger.error("-------------getLevelListCount---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getLevelListCount---关闭Session时出现HibernateException-----",he2);
		}
	}
	if(list !=null && list.size()>0)
		return list.size();
	else
		return 0;
}

public boolean saveLevel(SsmnZyLevel zl)
{
	Session session = null;
	StringBuffer strHQL = new StringBuffer("");
	boolean ret = true;
	List<SsmnZyLevel> list = null;
	Transaction tx = null;
	PreparedStatement pstmt = null;
	Connection conn = null;
	try {
		session = HibernateSessionFactory.getSession();
		tx = session.beginTransaction();
		conn = session.connection();
		
		//session.save(zl);
		String sql = " insert into ssmn_zy_level values((select max(id)+1 from ssmn_zy_level), '"+zl.getProvincecity()+"' ";
		sql +=" ,'"+zl.getCompany()+"' ";
		sql +=" ,'"+zl.getBusinessdepartment()+"' ";
		sql +=" ,'"+zl.getWarzone()+"' ";
		sql +=" ,'"+zl.getArea()+"' ";
		sql +=" ,'"+zl.getBranchactiongroup()+"' ";
		sql +=	")";
		
		pstmt = conn.prepareStatement(sql.toString());
		ret = pstmt.execute();
		
		tx.commit();
		ret = true;
	} catch (Exception he) {
		ret = false;
		if (tx != null){
			tx.rollback();
			logger.info("操作回滚");
		}
		logger.error("-------------getLevelIDByActionGroup---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getLevelIDByActionGroup---关闭Session时出现HibernateException-----",he2);
		}
	}
	
	return ret;
}

public List<SsmnZyLevel> isExist(Long operalevelid,String sparam,String schtype)
{
	Session session = null;
	List<SsmnZyLevel> list = null;
	try {
	
		session = this.adapter.openSession();
		String sql = " from SsmnZyLevel l where 1=1 ";
		if(operalevelid >0 )
			sql +=" and l.provincecity=(select provincecity from SsmnZyLevel where id= :operalevelid1)  and l.company =( select company from SsmnZyLevel where id= :operalevelid2 ) ";
		
		if(sparam !=null && !"".equals(sparam) && sparam.length()>0 )
		{
			if(schtype !=null && !"".equals(schtype) && schtype.equals("1"))//事业部
				sql += " and businessdepartment = :sparam ";
			if(schtype !=null && !"".equals(schtype) && schtype.equals("2"))//战区
				sql += " and warzone = :sparam ";
			if(schtype !=null && !"".equals(schtype) && schtype.equals("3"))//片区
				sql += " and area = :sparam ";
			if(schtype !=null && !"".equals(schtype) && schtype.equals("4"))//行动组别
				sql += " and branchactiongroup = :sparam ";
		}
		
		Query query = session.createQuery(sql);
		if(operalevelid >0)
		{
			query.setLong("operalevelid1", operalevelid);
			query.setLong("operalevelid2", operalevelid);
		}
		if(sparam !=null && !"".equals(sparam) && sparam.length()>0 )
			query.setString("sparam", sparam);
			
		
		list = query.list();
	
	} catch (Exception he) {
		logger.error("-------------isExist---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------isExist---关闭Session时出现HibernateException-----",he2);
		}
	}	
	return list;

}

//编辑功能
public boolean updateLevelBranch(String company,String schtype,String sbusinNew,String sbusinOld,
		String swarzoneNew,String swarzoneOld,String sareaNew,String sareaOld,String sbranchNew,String sbranchOld)
{
	Session session = null;
	boolean isok = true;
	 Transaction trans = null;
	try {
		session = this.adapter.openSession();
		trans = session.beginTransaction();
		String sql ="";
		if(schtype !=null && !"".equals(schtype) && schtype.equals("1"))//修改事业部级
			sql = "update SsmnZyLevel set businessdepartment =:sbusinNew  where company=:company and businessdepartment=:sbusinOld ";
		if(schtype !=null && !"".equals(schtype) && schtype.equals("2"))//修改战区级
			sql = "update SsmnZyLevel set businessdepartment =:sbusinNew ,warzone=:swarzoneNew where company=:company and businessdepartment=:sbusinOld and warzone=:swarzoneOld ";
		if(schtype !=null && !"".equals(schtype) && schtype.equals("3"))//修改片区级
			sql = "update SsmnZyLevel set businessdepartment =:sbusinNew ,warzone=:swarzoneNew ,area=:sareaNew where"+
			" company=:company and businessdepartment=:sbusinOld and warzone=:swarzoneOld  and area=:sareaOld ";
		if(schtype !=null && !"".equals(schtype) && schtype.equals("4"))//修改行动组级
			sql = "update SsmnZyLevel set businessdepartment =:sbusinNew ,warzone=:swarzoneNew ,area=:sareaNew , branchactiongroup=:sbranchNew  where"+
			" company=:company and businessdepartment=:sbusinOld and warzone=:swarzoneOld  and area=:sareaOld and branchactiongroup=:sbranchOld ";
		
		Query query = session.createQuery(sql);
		query.setString("company", company);
		if(sbusinNew !=null && !"".equals(sbusinNew) && sbusinNew.length()> 0) 
			query.setString("sbusinNew", sbusinNew);
		if(sbusinOld !=null && !"".equals(sbusinOld) && sbusinOld.length()> 0) 
			query.setString("sbusinOld", sbusinOld);
		if(swarzoneNew !=null && !"".equals(swarzoneNew) && swarzoneNew.length()> 0) 
			query.setString("swarzoneNew", swarzoneNew);
		if(swarzoneOld !=null && !"".equals(swarzoneOld) && swarzoneOld.length()> 0) 
			query.setString("swarzoneOld", swarzoneOld);
		if(sareaNew !=null && !"".equals(sareaNew) && sareaNew.length()> 0) 
			query.setString("sareaNew", sareaNew);
		if(sareaOld !=null && !"".equals(sareaOld) && sareaOld.length()> 0) 
			query.setString("sareaOld", sareaOld);
		if(sbranchNew !=null && !"".equals(sbranchNew) && sbranchNew.length()> 0) 
			query.setString("sbranchNew", sbranchNew);
		if(sbranchOld !=null && !"".equals(sbranchOld) && sbranchOld.length()> 0) 
			query.setString("sbranchOld", sbranchOld);
		
		int res =query.executeUpdate();
		
		trans.commit();
	} catch (Exception he) {
		logger.error("-------------updateLevelBranch---出现Exception-----",he);
		isok = false;
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------updateLevelBranch---关闭Session时出现HibernateException-----",he2);
		}
	}	
	return isok;
}

	public static void main(String[] args) {
//		LevelDao dao = LevelDao.getInstance();
//		try {
//			SsmnZyLevel l = (SsmnZyLevel) dao.getByPrimaryKey(1000000096L );
//			if(l != null){
//				logger.info("====l.war===="+l.getWarzone());
//			}else{
//				logger.info("====null====" );
//			}
//		} catch (DaoException e) {
//			e.printStackTrace();
//		}
//		List<String> list = dao.getLevelByTypeAndID(1, 1000000092L);
//		List<String> list = dao.getSelectLevel(3, 1000000092L, "南区事业部", "河西人民公园3战区", "2片区");
//		long t = dao.getLevelIDByProperty("天津", "中原", "南区事业部", "河西人民公园3战区", "一片区", "分行1b");
//		logger.info("========"+t);
		
	}
	
	
}
