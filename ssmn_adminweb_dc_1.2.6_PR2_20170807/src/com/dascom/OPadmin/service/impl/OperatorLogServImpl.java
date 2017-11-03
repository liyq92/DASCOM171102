package com.dascom.OPadmin.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.IAdminOperatorLog;
import com.dascom.OPadmin.dao.impl.AdminOperatorLogImpl;

import com.dascom.OPadmin.entity.TyadminLogs;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IOperatorLogService;
import com.dascom.OPadmin.util.DBException;
import com.dascom.OPadmin.util.HibernateHelper;
import com.dascom.OPadmin.util.HibernateSessionFactory;
import com.dascom.OPadmin.util.Page;
import com.dascom.OPadmin.util.SysUtil;
import com.dascom.ssmn.entity.ZydcRecord;
import com.dascom.ssmn.util.Constants;

public class OperatorLogServImpl implements IOperatorLogService {
	private static final Logger logger = Logger.getLogger(OperatorLogServImpl.class);
	
	private IAdminOperatorLog logDao = new AdminOperatorLogImpl();

	public OperatorLogServImpl() {
		super();
	}

	public List<TyadminLogs> searchAllLog(String opeNo, String serviceKey, String startTime,
			String stopTime, String logType) {
		
		return logDao.findAllLogs(opeNo, serviceKey, startTime, stopTime,
				logType);
	}

	public List<TyadminLogs> searchLog(String opeNo, String serviceKey, String startTime,
			String stopTime, String logType, String logDes, Page page) {
		
		return logDao.findByPage(opeNo, serviceKey, startTime, stopTime,
				logType, page.getFirstResult() - 1, page.getSize());
	}

	public void log(TyadminOperators operator,String logtype,String des)
	{
		
		Session session = HibernateSessionFactory.getSession();
		Connection conn = null;
		PreparedStatement pstmt = null;
		Transaction trans = null;
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
		
		try{
			trans=session.beginTransaction();
			conn = session.connection();
		
			StringBuffer sql =new StringBuffer(" insert into TYADMIN_LOGS (id,openo,groupid,servicekey,log_type,log_time,log_des ) values "
									+"(seq_tyadmin_log.nextval, ");
			sql.append("'").append(operator.getId().getOpeno()).append("' ,");
			sql.append(operator.getGroupId());
			sql.append(" , ").append(Constants.servicekey_dc).append(",'").append(logtype).append("' ");
			sql.append(",sysdate ,'").append(des).append("' )");
			//System.out.print(sql.toString());
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.execute();
			
			trans.commit();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}
	}

	public List<String> searchLogType(String serviceKey) {
		return logDao.findOperationType(serviceKey);
	}

	public int getTotalCount(String opeNo, String serviceKey, String startTime,
			String stopTime, String logType) {
		// AdminOperator operator = operatorDao.lookForDetail(opeNo);
		return logDao.getTotalCount(opeNo, serviceKey, startTime, stopTime,
				logType);
	}
	
	public boolean isReadCdr(String descStr,String openo)
	{
		boolean isok = false;
		Session session = HibernateHelper.currentSession();
		List<TyadminLogs> list = new ArrayList<TyadminLogs>();
		 //测数据库连接是否成功
	    if (session == null) {
	      logger.error("fail to connect db");
	      throw new DBException("fail to connect db");
	    }
		try {
			StringBuffer strHQL = new StringBuffer(" from TyadminLogs  where 1=1   ");
			
			strHQL.append(" and logDes like :str ");
			 
			Query query= session.createQuery(strHQL.toString());
			query.setString("str", "%"+openo+descStr+"%");
						
			list = query.list();
			
			if(list != null && list.size() >0)
				isok= true;
			else
				isok= false;
				 					
			 
		} catch (Exception he) {
			logger.error("-------------isReadCdr---出现Exception-----",he);
			isok= false;
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------isReadCdr---关闭Session时出现HibernateException-----",he2);
			}
		}
		return isok;
	}

}
