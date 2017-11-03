package com.dascom.OPadmin.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.dascom.OPadmin.dao.IAdminOperatorLog;
import com.dascom.OPadmin.entity.TyadminLogs;

public class AdminOperatorLogImpl extends RootDao implements IAdminOperatorLog {

	public AdminOperatorLogImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Class getReferenceClass() {
		return TyadminLogs.class;
	}

	public List<String> findOperationType(String serviceKey) {
		Session session = this.adapter.openSession();
		List<String> operationType = new ArrayList<String>();
		String sql = "select distinct adminLog.logType from TyadminLogs adminLog ";
		if(serviceKey != null && !serviceKey.equals(""))
			sql = sql + "where adminLog.servicekey = :servicekey";
		try {
			Query query = session.createQuery(sql);
			if(serviceKey != null && !serviceKey.equals(""))
				query.setString("servicekey", serviceKey);
			operationType = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
		return operationType;
	}

	public List<TyadminLogs> findByPage(String operId,String serviceKey,String startTime, String stopTime, 
			String optType, int index,int maxCount) {
		List<TyadminLogs> result = new ArrayList<TyadminLogs>();
		Session session = this.adapter.openSession();
		StringBuffer sb = new StringBuffer();
		sb.append("from TyadminLogs log where 1=1 ");
		if (operId != null && !"".equals(operId)) {
			sb.append("and log.openo = :operId ");
		}
		if (serviceKey != null && !"".equals(serviceKey)) {
			sb.append("and log.servicekey = :serviceKey ");
		}
		if (startTime != null && !"".equals(startTime)) {
			sb.append("and to_char(log.logTime,'YYYY-MM-DD') >= :startTime ");
		}
		if (stopTime != null && !"".equals(stopTime)) {
			sb.append("and to_char(log.logTime,'YYYY-MM-DD') <= :stopTime ");
		}
		if (optType != null && !"".equals(optType)) {
			sb.append("and log.logType=:optType ");
		}
		sb.append("order by log.logTime desc");
		try {
			Query query = session.createQuery(sb.toString());
			if (operId != null && !"".equals(operId)) {
				query.setString("operId", operId);
			}
			if (serviceKey != null && !"".equals(serviceKey)) {
				query.setString("serviceKey", serviceKey);
			}
			if (startTime != null && !"".equals(startTime)) {
				query.setString("startTime", startTime);
			}
			if (stopTime != null && !"".equals(stopTime)) {
				query.setString("stopTime", stopTime);
			}
			if (optType != null && !"".equals(optType)) {
				query.setString("optType", optType);
			}
			query.setFirstResult(index);
			query.setMaxResults(maxCount);
			result = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public int getTotalCount(String operId, String serviceKey,String startTime, String stopTime, String optType) {
		int count = 0;
		Session session = this.adapter.openSession();
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) from TyadminLogs log where 1=1 ");
		if (operId != null && !"".equals(operId)) {
			sb.append("and log.openo = :operId ");
		}
		if (serviceKey != null && !"".equals(serviceKey)) {
			sb.append("and log.servicekey = :serviceKey ");
		}
		if (startTime != null && !"".equals(startTime)) {
			sb
					.append("and to_char(log.logTime,'YYYY-MM-DD') >= :startTime ");
		}
		if (stopTime != null && !"".equals(stopTime)) {
			sb
					.append("and to_char(log.logTime,'YYYY-MM-DD') <= :stopTime ");
		}
		if (optType != null && !"".equals(optType)) {
			sb.append("and log.logType = :optType");
		}
		try {
			Query query = session.createQuery(sb.toString());
			if (operId != null && !"".equals(operId)) {
				query.setString("operId", operId);
			}
			if (serviceKey != null && !"".equals(serviceKey)) {
				query.setString("serviceKey", serviceKey);
			}
			if (startTime != null && !"".equals(startTime)) {
				query.setString("startTime", startTime);
			}
			if (stopTime != null && !"".equals(stopTime)) {
				query.setString("stopTime", stopTime);
			}
			if (optType != null && !"".equals(optType)) {
				query.setString("optType", optType);
			}
			count = ((Integer) query.uniqueResult()).intValue();
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

	public List<TyadminLogs> findAllLogs(String opeId, String serviceKey, String startTime, String stopTime, String optType) {

		List<TyadminLogs> result = new ArrayList<TyadminLogs>();
		Session session = this.adapter.openSession();
		StringBuffer sb = new StringBuffer();
		sb.append("from TyadminLogs log where 1=1 ");
		if (opeId != null && !"".equals(opeId)) {
			sb.append("and log.openo = :operId ");
		}
		if (serviceKey != null && !"".equals(serviceKey)) {
			sb.append("and log.servicekey = :serviceKey ");
		}
		if (startTime != null && !"".equals(startTime)) {
			sb.append("and to_char(log.logTime,'YYYY-MM-DD') >= :startTime ");
		}
		if (stopTime != null && !"".equals(stopTime)) {
			sb.append("and to_char(log.logTime,'YYYY-MM-DD') <= :stopTime ");
		}
		if (optType != null && !"".equals(optType)) {
			sb.append("and log.logType = :optType ");
		}
		sb.append("order by log.logTime desc");
		try {
			Query query = session.createQuery(sb.toString());
			if (opeId != null && !"".equals(opeId)) {
				query.setString("operId", opeId);

			}
			if (serviceKey != null && !"".equals(serviceKey)) {
				query.setString("serviceKey", serviceKey);

			}
			if (startTime != null && !"".equals(startTime)) {
				query.setString("startTime", startTime);

			}
			if (stopTime != null && !"".equals(stopTime)) {
				query.setString("stopTime", stopTime);

			}
			if (optType != null && !"".equals(optType)) {
				query.setString("optType", optType);

			}
			result = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}
