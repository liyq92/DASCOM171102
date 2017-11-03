package com.dascom.ssmn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import com.dascom.OPadmin.util.DBException;
import com.dascom.OPadmin.util.HibernateSessionFactory;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.SsmnZyFeedback;
import com.dascom.ssmn.entity.SsmnZyUser;

public class FeedBackDao extends RootDao{
	private static final Logger logger =  Logger.getLogger(FeedBackDao.class);
	private static FeedBackDao dao;
	
	public synchronized static FeedBackDao getInstance(){
		if(dao == null)
			dao = new FeedBackDao();
		return dao;
	}
	
	
	@Override
	public Class<SsmnZyLevel> getReferenceClass() {
		return SsmnZyLevel.class;
	}
	
	/**
	 * 查询反馈信息
	 * 超级管理员能看所有的信息反馈，其他账号只能看自己的反馈
	 */
	public List<SsmnZyFeedback> getFeedbackList(String feedcontent,String startdate,String enddate,Integer index, Integer maxCount,String serviceKey,String strOpeno,long operaLevelid){
		
		//判断是否是超级管理员,
		boolean issuper = isSuperUser(strOpeno);
		
		Session session = HibernateSessionFactory.getSession();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet srs = null;
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
	
		List<SsmnZyFeedback> list = new ArrayList<SsmnZyFeedback>();
		StringBuffer strHQL = new StringBuffer("");
		try {
			conn = session.connection();
			strHQL.append(" select f.id,f.msisdn,f.feedbackdate, f.feedbackcontent, l.branchactiongroup,f.name ,to_char(f.feedbackdate,'yyyy-mm-dd hh:mi:ss') as st,f.openo,rownum as rn from Ssmn_Zy_Feedback f ,Tyadmin_Operators t, Ssmn_Zy_Level l where  f.openo=t.openo and t.levelid=l.id and t.servicekey='").append(serviceKey).append("'  ");
			 
			if(feedcontent != null && !"".equals(feedcontent))
			{
				strHQL.append(" and f.feedbackcontent like '%").append(feedcontent).append("%'  ");
			}
			if(startdate != null && !"".equals(startdate))
			{
				strHQL.append(" and to_char(f.feedbackdate,'yyyy-mm-dd') >= '").append(startdate).append("' ");
			}
			if(enddate != null && !"".equals(enddate))
			{
				strHQL.append("  and  to_char(f.feedbackdate,'yyyy-mm-dd') <= '").append(enddate).append("' ");
			}
			//公司做限制,只能看本公司范围内的反馈
			strHQL.append(" and l.provincecity =(select provincecity from Ssmn_Zy_Level where id= ").append(operaLevelid).append(" ) and l.company=( select company from Ssmn_Zy_Level where id=").append(operaLevelid).append("  )  ");
						 
			if(!issuper)//不是超级用户，要加上，
				strHQL.append(" and  f.openo='").append(strOpeno).append("' ");
			
			strHQL.append(" order by f.feedbackdate desc ");
			
			String ssqql = " select id,msisdn,feedbackdate, feedbackcontent, branchactiongroup,name , st,openo,rn from ( "+strHQL+" ) p ";
			if(index >=0)
			{
				ssqql +="where  rn >= ";
				ssqql += index+1;
				ssqql += "  and rn <= ";
				ssqql += index+10;
			}
			
			pstmt = conn.prepareStatement(ssqql.toString());
			srs = pstmt.executeQuery();
			while (srs.next()) {
				list.add(new SsmnZyFeedback(srs.getLong("id"),srs.getString("msisdn"), srs.getDate("feedbackdate"),srs.getString("feedbackcontent"),srs.getString("branchactiongroup"),
					srs.getString("name"),srs.getString("st"),srs.getString("openo")));
		}
		} catch (Exception he) {
			logger.error("-------------getFeedbackList---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getFeedbackList---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
	}
	
	//判断是否是超级管理员，因为组别已经定死，所以可以这么写死
	public boolean isSuperUser(String strOpeno)
	{
		boolean isok = false;
		
		if(strOpeno ==null || "".equals(strOpeno))
			return isok;
		Session session = HibernateSessionFactory.getSession();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet srs = null;
		int rescount = 0;
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
		 
		StringBuffer strHQL = new StringBuffer("");
		try {
			conn = session.connection();
			strHQL.append(" select count(t.openo) as cn from tyadmin_operators t ,tyadmin_groups g where t.group_id=g.id and g.group_name='超级管理员' and t.openo='").append(strOpeno).append("' ");
			 		
			pstmt = conn.prepareStatement(strHQL.toString());
			srs = pstmt.executeQuery();
			while (srs.next()) {
				rescount = srs.getInt("cn");
				break;
			}
			if(rescount >0)
				isok = true;
			else
				isok = false;
		} catch (Exception he) {
			logger.error("-------------getFeedbackCount---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getFeedbackCount---关闭Session时出现HibernateException-----",he2);
			}
		}
		
		return isok;
	}
	
	public int getFeedbackCount(String feedcontent,String startdate,String enddate,String servicekey,String strOpeno,long operaLevelid)
	{
		//判断是否是超级管理员,
		boolean issuper = isSuperUser(strOpeno);
		
		Session session = HibernateSessionFactory.getSession();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet srs = null;
		int rescount = 0;
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
		 
		StringBuffer strHQL = new StringBuffer("");
		try {
			conn = session.connection();
			strHQL.append(" select count(*) as co from Ssmn_Zy_Feedback f ,Tyadmin_Operators t, Ssmn_Zy_Level l where  f.openo=t.openo and t.levelid=l.id and t.servicekey='").append(servicekey).append("'  ");
			 
			if(feedcontent != null && !"".equals(feedcontent))
			{
				strHQL.append(" and f.feedbackcontent like '%").append(feedcontent).append("%'  ");
			}
			if(startdate != null && !"".equals(startdate))
			{
				strHQL.append(" and to_char(f.feedbackdate,'yyyy-mm-dd hh:mi:ss') >= '").append(startdate).append("' ");
			}
			if(enddate != null && !"".equals(enddate))
			{
				strHQL.append(" and to_char(f.feedbackdate,'yyyy-mm-dd hh:mi:ss') <= '").append(enddate).append("' ");
			}
			
			//公司做限制,只能看本公司范围内的反馈
			strHQL.append(" and l.provincecity =(select provincecity from Ssmn_Zy_Level where id= ").append(operaLevelid).append(" ) and l.company=( select company from Ssmn_Zy_Level where id=").append(operaLevelid).append("  )  ");
			
			if(!issuper)//不是超级用户，要加上，
				strHQL.append(" and  f.openo='").append(strOpeno).append("' ");
			
			pstmt = conn.prepareStatement(strHQL.toString());
			srs = pstmt.executeQuery();
			while (srs.next()) {
				rescount = srs.getInt("co");
				break;
		}
		} catch (Exception he) {
			logger.error("-------------getFeedbackCount---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getFeedbackCount---关闭Session时出现HibernateException-----",he2);
			}
		}
		return rescount;
	}
	
	public boolean addFeedback(SsmnZyFeedback zyfb)
	{
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
		try{
			
			tx = session.beginTransaction();
			//begin
			conn = session.connection();
			
			//session.save(zyfb);不知道为什么，日期都不带时间!!
			String supdatetime = " insert into Ssmn_Zy_Feedback values(SEQ_SSMN_ZY_FEEDBACK.NEXTVAL,'"+zyfb.getMsisdn()+"' ,sysdate,'"+zyfb.getFeedbackcontent()+"', '"+zyfb.getName()+"', '"+zyfb.getOpeno()+"' )";
			pstmt = conn.prepareStatement(supdatetime.toString());
			pstmt.execute();
			tx.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(session != null && session.isOpen()){
				session.clear();
				session.close();
				return false;
			}
		}finally{
			if(session != null && session.isOpen()){
				session.clear();
				session.close();
			}
		}

		return true;
	}
	
	public boolean deleFeedback(String uid)
	{
		Session session = this.adapter.openSession();
		Transaction tx = null;
		try{
			
			tx = session.beginTransaction();

			String supdatetime = " delete from SsmnZyFeedback where id="+uid;
			Query qu=session.createQuery(supdatetime);
			qu.executeUpdate();
			tx.commit();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(session != null && session.isOpen()){
				session.clear();
				session.close();
				return false;
			}
		}finally{
			if(session != null && session.isOpen()){
				session.clear();
				session.close();
			}
		}

		return true;
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
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");Date date = new Date(System.currentTimeMillis());
		System.out.print(df.format(new Date()));
	}
	
	
}
