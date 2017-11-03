package com.dascom.OPadmin.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dbadapter.IHibernateAdapter;

public interface IEntityManager {
	
    public Serializable create(Object object) throws DaoException;
    
    public void create(Object object,Serializable id) throws DaoException;
    
    public void edit(Object object) throws DaoException;
    
    public void edit(Object object, Serializable pk) throws DaoException ;
    
    public void delete(Serializable pk) throws DaoException;
    
    public Object getByPrimaryKey(Serializable pk) throws DaoException;
    
    public List getAll() throws DaoException;
    
    public List getList(String hql,Map<String,Object> argMap ,Integer index, Integer maxCount) ;
    
    public Query getQuery(String sql) throws DaoException;
    
    public void setAdapter(IHibernateAdapter adapter);
}
