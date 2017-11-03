/*
 * Created on 2005-10-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.dascom.OPadmin.dao.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dbadapter.IHibernateAdapter;
import com.dascom.OPadmin.dbadapter.PlainHibernateAdapter;
import com.dascom.OPadmin.util.DBException;
import com.dascom.OPadmin.util.HibernateHelper;


/**
 * @author RubinRuler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class RootDao implements com.dascom.OPadmin.dao.IEntityManager {

    protected IHibernateAdapter adapter = new PlainHibernateAdapter();
	private static final Logger logger = Logger.getLogger(RootDao.class);

	/* (non-Javadoc)
	 * @see com.dascom.ssmn.dao.IEntityManagerDao#create(java.lang.Object)
	 */
	public Serializable create(Object object) throws DaoException {
		Session session = HibernateHelper.currentSession();
		Serializable id = null;
		
	    //�?测数据库连接是否成功
	    if (session == null) {
	      logger.error("fail to connect db");
	      throw new DBException("fail to connect db");
	    }

		Transaction t = null;
    	try{
    		t = session.beginTransaction();
    		id = session.save(object);
    		t.commit();
    	}catch(HibernateException he){
    		he.printStackTrace();
    		try{
    			if(null != t){
    				t.rollback();
    				id = null;}
    		}catch(Exception e) {
    			 logger.error(e.toString());
    		}
  	      logger.error(he.toString());
    	}
    	finally {
    		try{
    			session.close();
    		}catch(Exception e) {
    			 logger.error(e.toString());
    			 id = null;
    		}
    	}
    	return id;
	}
	
	/* (non-Javadoc)
	 * @see com.dascom.ssmn.dao.IEntityManagerDao#create(java.lang.Object, java.io.Serializable)
	 */
	public void create(Object object, Serializable id) throws DaoException {
		Session session = HibernateHelper.currentSession();

	    //�?测数据库连接是否成功
	    if (session == null) {
	      logger.error("fail to connect db");
	      throw new DBException("fail to connect db");
	    }

		Transaction t = null;
		try{
			session.save(object,id);
			session.close();
    	}catch(HibernateException he){
    		logger.error(he.toString());
    	}finally {
    		try{
    			session.close();
    		}catch(Exception e) {
    			 logger.error(e.toString());
    		}
    	}
    	
	}

	/* (non-Javadoc)
	 * @see com.dascom.ssmn.dao.IEntityManagerDao#delete(java.io.Serializable)
	 */
	public void delete(Serializable pk) throws DaoException {
		Session session = HibernateHelper.currentSession();

	    //�?测数据库连接是否成功
	    if (session == null) {
	      logger.error("fail to connect db");
	      throw new DBException("fail to connect db");
	    }

		Transaction t = null;
		
		try{
			t = session.beginTransaction();
			Object obj = session.get(this.getReferenceClass(),pk);
			if(null != obj)
				session.delete(obj);
			t.commit();
			session.close();
		}catch(HibernateException he){
			logger.error(he.toString());
			try{
				if(null != t)
					t.rollback();
			}catch(Exception e){
				 logger.error(e.toString());
			}
		}finally {
			try{
				session.close();
			}catch(Exception e){
				 logger.error(e.toString());
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.dascom.ssmn.dao.IEntityManagerDao#edit(java.lang.Object)
	 */
	public void edit(Object object) throws DaoException {
		Session session = HibernateHelper.currentSession();

	    //�?测数据库连接是否成功
	    if (session == null) {
	      logger.error("fail to connect db");
	      throw new DBException("fail to connect db");
	    }

		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.update(object);
			tx.commit();
			session.close();
		}catch(HibernateException he){
			logger.error(he.toString());
			try {
				tx.rollback();
			} catch (HibernateException e) {
				 logger.error(e.toString());
			}
		}finally {
			try{
				session.close();
			}catch(Exception e){
				 logger.error(e.toString());
			}
		}
	}


	/* (non-Javadoc)
	 * @see com.dascom.ssmn.dao.IEntityManagerDao#getByPrimaryKey(java.io.Serializable)
	 */
	public Object getByPrimaryKey(Serializable pk) throws DaoException {
		Session session = HibernateHelper.currentSession();

	    //�?测数据库连接是否成功
	    if (session == null) {
	      logger.error("fail to connect db");
	      throw new DBException("fail to connect db");
	    }
	    Object o = null;
		try{
			o =  session.get(this.getReferenceClass(),pk);
		}catch(HibernateException he){
			 logger.error(he.toString());
			throw new DaoException(he);
		}finally {
			try{
				session.close();
			}catch(Exception e){
				 logger.error(e.toString());
			}
		}
		
		return o;
	}
	
	/* (non-Javadoc)
	 * @see com.dascom.ssmn.dao.IEntityManagerDao#getAll()
	 */
	public List getAll() throws DaoException {
		Session session = HibernateHelper.currentSession();

	    //�?测数据库连接是否成功
	    if (session == null) {
	      logger.error("fail to connect db");
	      throw new DBException("fail to connect db");
	    }
	    
	    List ls = new ArrayList();
		try{
			Criteria cri= session.createCriteria(this.getReferenceClass());
			ls =cri.list();
		}catch(HibernateException he){
			 logger.error(he.toString());
			throw new DaoException(he);
		}finally {
			try{
				session.close();
			}catch(Exception e){
				 logger.error(e.toString());
			}
		}
		return ls;
	}
	
	public List getList(String hql,Map<String,Object> argMap ,Integer index, Integer maxCount) {
		Session session = HibernateHelper.currentSession();
		if(session==null){
			logger.error("fail to connect db");
			throw new DBException("fail to connect db");
		}
		List list = null;
		Query query = null;
		try {
			query = session.createQuery(hql);
			if(argMap != null) {
				Set<String> argNameList = argMap.keySet();
				for(String argName:argNameList){
					Object o = argMap.get(argName);
					Class c = o.getClass();
					String cname = c.getName();
					if("java.lang.Long".equals(cname)){
						query.setLong(argName, (Long)o);
					}
					else if("java.lang.String".equals(cname)){
						query.setString(argName, (String)o);
					}
					else if("java.lang.Integer".equals(cname)){
						query.setInteger(argName,(Integer)o);
					}
					else if("java.sql.Timestamp".equals(cname)) 
						query.setTimestamp(argName,((Timestamp)o));
				}
			}
			if (maxCount != null && maxCount != 0) {
				query.setFirstResult(index);
				query.setMaxResults(maxCount);
			}
			list = query.list();
			session.close();
		} catch (HibernateException he) {
			logger.error(he);
		} finally {
			try {
				if (session != null && session.isOpen()) {
					session.close();
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return list;
	}
	
	public void edit(Object object, Serializable pk) throws DaoException {
		Session session = HibernateHelper.currentSession();

	    //�?测数据库连接是否成功
	    if (session == null) {
	      logger.error("fail to connect db");
	      throw new DBException("fail to connect db");
	    }
	    
	    Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.update(object, pk);
			tx.commit();
		}catch(HibernateException he){
			he.printStackTrace();
			try {
				tx.rollback();
			} catch (HibernateException e) {
				 logger.error(e.toString());
			}
			 logger.error(he.toString());
			throw new DaoException(he);
		}finally {
			try{
				session.close();
			}catch(Exception e){
				 logger.error(e.toString());
			}
		}
	}

	/* (non-Javadoc)
	 */
	public Query getQuery(String sql) throws DaoException {
		Session session = HibernateHelper.currentSession();

	    //�?测数据库连接是否成功
	    if (session == null) {
	      logger.error("fail to connect db");
	      throw new DBException("fail to connect db");
	    }
	    Query query = null;
		try{
			query = session.createQuery(sql);
			session.close();
		} catch (HibernateException he) {
			 logger.error(he.toString());
			throw new DaoException(he);
		}finally {
			try{
				session.close();
			}catch(Exception e){
				 logger.error(e.toString());
			}
		}
		return query;
	}
	
	public int executeUpdateByHQL(String hql) {
		Session session = HibernateHelper.currentSession();
		Transaction tran = session.beginTransaction();
		Query query = null;
		int i = 0;
		try {
			query = session.createQuery(hql);
			i = query.executeUpdate();
			tran.commit();
		} catch (HibernateException he) {
			i = 0;
			logger.error(he);
			try {
				tran.rollback();
			} catch (HibernateException e) {
				logger.error(e);
			}
		} finally {
			try {
				if (session != null && session.isOpen()) {
					session.close();
				}
			} catch (Exception e) {
				i = 0;
				logger.error(e);
			}
		}
		return i;
	}
	
	
	public void setAdapter(IHibernateAdapter adapter){
		this.adapter = adapter;
	}
	
    public abstract Class getReferenceClass();
}
