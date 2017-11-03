package com.dascom.ssmn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dascom.OPadmin.dao.impl.RootDao;
import com.dascom.OPadmin.util.DBException;
import com.dascom.OPadmin.util.HibernateSessionFactory;
import com.dascom.init.ConfigMgr;
import com.dascom.ssmn.entity.Blackcallnumber;
import com.dascom.ssmn.entity.ZydcRecord;
import com.dascom.ssmn.dao.zydcUserDao;

public class BlackCallNumberDao  extends RootDao{

	private static final Logger logger =  Logger.getLogger(BlackCallNumberDao.class);
	private static BlackCallNumberDao dao;
	private zydcUserDao udao = zydcUserDao.getInstance();
	
	public synchronized static BlackCallNumberDao getInstance(){
		if(dao == null)
			dao = new BlackCallNumberDao();
		return dao;
	}
	
	@Override
	public Class<Blackcallnumber> getReferenceClass() {
		return Blackcallnumber.class;
	}
	
	public List<Blackcallnumber> getBlackCallNumberList(String msisdn){
        Session session = null;
        List<Blackcallnumber> list = new ArrayList<Blackcallnumber>();
		try {
			StringBuffer strHQL = new StringBuffer(" from Blackcallnumber where 1=1  ");
			if(msisdn != null && !"".equals(msisdn)){
				strHQL.append(" and blackNumber =:msisdn ");
			}			
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			if(msisdn != null && !"".equals(msisdn)){
				query.setString("msisdn", msisdn);
			}
			list = query.list(); 
			 
		} catch (Exception he) {
			logger.error("-------------getBlackCallNumberList---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getBlackCallNumberList---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list ;
	}	
	
	/**
	 * 添加黑名单
	 * @param msisdn
	 * @return
	 */
	public Boolean addBlackCallNumberInfo(String msisdn){
        Session session = null;
        Transaction tx = null;
        boolean re = false;
		try {
			session = this.adapter.openSession();
			tx = session.beginTransaction();
			
			Blackcallnumber bl = new Blackcallnumber();
			bl.setBlackNumber(msisdn);
			bl.setIntime(new Date());
			session.save(bl);
			tx.commit();
			re = true;
		} catch (Exception he) {
			re = false;
			logger.error("-------------addBlackCallNumberInfo---出现Exception-----",he);
			tx.rollback();
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------addBlackCallNumberInfo---关闭Session时出现HibernateException-----",he2);
				return false;
			}
		}
		
		if(re)
		{
			////记录到ssmn_zy_useroperation表
			String sRecordUserChange = ConfigMgr.getRecordUserChange();
			if(sRecordUserChange !=null && !"".equals(sRecordUserChange) && sRecordUserChange.equals("1"))
			{
				udao.addToUserRecord(" ", " ", "", 5,msisdn);
			}
		}
		return re ;
	}
	
	public Boolean delBlackCallNumber(Blackcallnumber num){
        Session session = null;
        Transaction tx = null;
        boolean re = false;
		try {
			session = this.adapter.openSession();
			tx = session.beginTransaction();
		
			session.delete(num);
			tx.commit();
			re = true;
		} catch (Exception he) {
			re = false;
			logger.error("-------------delBlackCallNumber---出现Exception-----",he);
			tx.rollback();
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------delBlackCallNumber---关闭Session时出现HibernateException-----",he2);
				return false;
			}
		}

		return re ;
	}
	
//查询黑名单号码
	public List<Blackcallnumber> getBlackNumList(String blacknum,String startdate,
			String enddate,Integer index, Integer maxCount)
	{
		List<Blackcallnumber> list =new ArrayList<Blackcallnumber>();
		Session session = HibernateSessionFactory.getSession();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
		
		try{
			conn = session.connection();
			StringBuffer bf =new StringBuffer(" select black_number, intime as it ,rownum as rn from blackcallnumber where 1=1 ");
			if(blacknum !=null && blacknum.length()>0)
				bf.append(" and black_number ='").append(blacknum).append("'  ");
			if(startdate !=null && startdate.length()>0)
				bf.append("  and  to_char(intime,'yyyy-mm-dd') >= '").append(startdate).append("'  ");
			if(enddate !=null && enddate.length()>0)
				bf.append("  and  to_char(intime,'yyyy-mm-dd') <= '").append(enddate).append("'  ");
		 	
			String sql =" select rn, black_number,it from ( ";
			sql +=bf.toString();
			sql +=" )q  ";
			if(index >=0)
			{
				sql +="where   ";
				int st=index+1;
				int en =index+10;
				sql +=" q.rn >=";
				sql +=st;
				sql +=" and q.rn <=";
				sql +=en;
			}
			
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while (rs.next()) {
					list.add(new Blackcallnumber(rs.getInt("rn"),rs.getString("black_number"),
							rs.getTimestamp("it")));
				}
			}
			
		} catch (Exception he) {
			logger.error("-------------getBlackNumList---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
				if(rs !=null)
					rs.close();
				if(conn !=null)
					conn.close();
			} catch (HibernateException he2) {
				logger.error("-------------getBlackNumList---关闭Session时出现HibernateException-----",he2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}	
	
	public int getBlackNumListCount(String blacknum,String startdate,
			String enddate)
	{
		List<Blackcallnumber> list =new ArrayList<Blackcallnumber>();
		Session session = HibernateSessionFactory.getSession();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int res =0;
		
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
		
		try{
			conn = session.connection();
			StringBuffer bf =new StringBuffer(" select count(black_number) as cn  from blackcallnumber where 1=1 ");
			if(blacknum !=null && blacknum.length()>0)
				bf.append(" and black_number ='").append(blacknum).append("'  ");
			if(startdate !=null && startdate.length()>0)
				bf.append("  and  to_char(intime,'yyyy-mm-dd') >= '").append(startdate).append("'  ");
			if(enddate !=null && enddate.length()>0)
				bf.append("  and  to_char(intime,'yyyy-mm-dd') <= '").append(enddate).append("'  ");
			
			pstmt = conn.prepareStatement(bf.toString());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while (rs.next()) {
					res = rs.getInt("cn");
					break;
				}
			}
			
		} catch (Exception he) {
			logger.error("-------------getBlackNumListCount---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
				if(rs !=null)
					rs.close();
				if(conn !=null)
					conn.close();
			} catch (HibernateException he2) {
				logger.error("-------------getBlackNumListCount---关闭Session时出现HibernateException-----",he2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}

	public static void main(String[] args) {
	 
		
	}

}
