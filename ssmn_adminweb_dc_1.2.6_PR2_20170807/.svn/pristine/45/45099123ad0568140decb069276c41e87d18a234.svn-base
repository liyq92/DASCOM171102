package com.dascom.ssmn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dascom.OPadmin.dao.impl.RootDao;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.OPadmin.entity.*;
import com.dascom.OPadmin.util.DBException;
import com.dascom.OPadmin.util.HibernateSessionFactory;
import com.dascom.ssmn.dao.UtilFunctionDao;


public class zydcOperatorDao  extends RootDao{

	private static final Logger logger =  Logger.getLogger(zydcOperatorDao.class);
	private static zydcOperatorDao dao;
	
	public synchronized static zydcOperatorDao getInstance(){
		if(dao == null)
			dao = new zydcOperatorDao();
		return dao;
	}
	
	@Override
	public Class<TyadminOperators> getReferenceClass() {
		return TyadminOperators.class;
	}
	
	
	public void updateOpera(TyadminOperators opera,String servicekey,String sname) {
		Session session = HibernateSessionFactory.getSession();
	    Connection conn = null;
		PreparedStatement pstmt = null;
	    //检测数据库连接是否成功
	    if (session == null) {
	    	throw new DBException("fail to connect db");
	    }

		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			//begin
			conn = session.connection();
	    	String sql1 = "update tyadmin_operators t  set t.opepwd='"+opera.getOpepwd() + "' where t.servicekey='"+servicekey + "' and t.openo='"+sname +"'";
	    	pstmt = conn.prepareStatement(sql1);
	    	//pstmt.setString(1, opera.getOpepwd());
	    	pstmt.execute();
			
			tx.commit();
		}catch(Exception he){
			he.printStackTrace();
			try {
				tx.rollback();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
		finally {
			try{
				session.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

}
