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
import com.dascom.ssmn.entity.SsmnDcRelation;
import com.dascom.ssmn.entity.SsmnDcDelrelation;

public class RelationDao extends RootDao{
	private static final Logger logger =  Logger.getLogger(RelationDao.class);
	private static RelationDao dao;
	
	public synchronized static RelationDao getInstance(){
		if(dao == null)
			dao = new RelationDao();
		return dao;
	}
	
	
	@Override
	public Class<SsmnDcRelation> getReferenceClass() {
		return SsmnDcRelation.class;
	}
	
	/**
	 *  根据副号码查询业主副号码信息  
	 */
	public List<SsmnDcRelation> getOwnerInfo(String ssmnnumber){
		Session session = null;
		List<SsmnDcRelation> list = new ArrayList<SsmnDcRelation>();
		StringBuffer strHQL = new StringBuffer("");
		try {
			strHQL.append("  from  SsmnDcRelation  where ssmnnumber = :ssmnnumber ");
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			query.setString("ssmnnumber", ssmnnumber);
			list = query.list();
		} catch (Exception he) {
			logger.error("-------------getOwnerInfo---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getOwnerInfo---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
	}
	
	public List<SsmnDcDelrelation> getOwnerDelInfo(String ssmnnumber){
		Session session = null;
		List<SsmnDcDelrelation> list = new ArrayList<SsmnDcDelrelation>();
		StringBuffer strHQL = new StringBuffer("");
		try {
			strHQL.append("  from  SsmnDcDelrelation  where ssmnnumber = :ssmnnumber ");
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			query.setString("ssmnnumber", ssmnnumber);
			list = query.list();
		} catch (Exception he) {
			logger.error("-------------getOwnerDelInfo---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getOwnerDelInfo---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
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
