package com.dascom.OPadmin.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.dascom.OPadmin.dao.IAdminGroup;
import com.dascom.OPadmin.entity.TyadminGroups;
import com.dascom.ssmn.util.Constants;

public class AdminGroupImpl extends RootDao implements IAdminGroup , Serializable{

	public Class getReferenceClass() {
		return TyadminGroups.class;
	}
	public TyadminGroups searchByGroupId(Long groupId) {
		TyadminGroups ag = null;
		StringBuffer sb = new StringBuffer();
		List result = new ArrayList();
		Session session = this.adapter.openSession();
		sb.append("from TyadminGroups gp where gp.id = :gpid");
		try {
			result = session.createQuery(sb.toString()).setLong("gpid",
					groupId.longValue()).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
		if (result.size() > 0){
			ag = (TyadminGroups)result.get(0);
			return ag;
		}
		return null;
	}

	public List<TyadminGroups> searchByGroupName(String gpname,String servicekey) {
		StringBuffer sb = new StringBuffer();
		List<TyadminGroups> result = new ArrayList<TyadminGroups>();
		Session session = this.adapter.openSession();
		sb.append("from TyadminGroups gp where gp.groupName = :gpname and gp.servicekey=:servicekey");
		try {
			result = session.createQuery(sb.toString()).setString("gpname",gpname).setString("servicekey", servicekey).list();
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
	
	public List<TyadminGroups> findAllGroups(String serviceKey,Long rank) {
		List<TyadminGroups> result = new ArrayList<TyadminGroups>();
		Session session = this.adapter.openSession();
		StringBuffer sql = new StringBuffer("from TyadminGroups gp ");
		sql.append("where gp.servicekey = :servicekey and gp.rank>=:rank ");
		sql.append(" order by gp.rank desc,gp.id desc");
		try {
			Query q = session.createQuery(sql.toString());
			q.setString("servicekey", serviceKey);
			q.setLong("rank", rank);	
			result = q.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally{
			try {
				session.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	public List<TyadminGroups> findByGroupName(String gpname, String serviceKey, Long rank) {
		StringBuffer sb = new StringBuffer();
		List<TyadminGroups> result = new ArrayList<TyadminGroups>();
		Session session = this.adapter.openSession();
		sb.append("from TyadminGroups gp where gp.groupName = :gpname");
		sb.append(" and gp.servicekey = :servicekey");
		sb.append(" and gp.rank>=:rank ");
		try {
			Query q = session.createQuery(sb.toString()).setString("gpname",gpname);
			q.setString("servicekey", serviceKey);
			q.setLong("rank", rank);
			result = q.list();
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
