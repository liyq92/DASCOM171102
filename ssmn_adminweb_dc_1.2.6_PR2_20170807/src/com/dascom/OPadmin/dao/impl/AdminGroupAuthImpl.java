package com.dascom.OPadmin.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.IAdminGroupAuth;
import com.dascom.OPadmin.entity.TyadminAuthorities;
import com.dascom.OPadmin.entity.TyadminGroupAuths;

public class AdminGroupAuthImpl extends RootDao implements IAdminGroupAuth {

	public Class getReferenceClass() {
		return TyadminGroupAuths.class;
	}

	public List<TyadminGroupAuths> findByGroupId(Long groupId) {
		StringBuffer sb = new StringBuffer();
		List<TyadminGroupAuths> result = new ArrayList<TyadminGroupAuths>();
		Session session = this.adapter.openSession();
		sb.append("from TyadminGroupAuths aga ");
		sb.append("where aga.groupId = :gpid");
		try {
			result = session.createQuery(sb.toString()).setLong("gpid", groupId.longValue()).list();
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

	/**
	 * 
	 * @param groupId
	 * @return 根据组名返回AdminAuthorities
	 */
	public List<TyadminAuthorities> findAuthByGroupId(Long groupId) {
		StringBuffer sb = new StringBuffer();
		List<TyadminAuthorities> result = new ArrayList<TyadminAuthorities>();
		Session session = this.adapter.openSession();
		Connection connect=session.connection();
		PreparedStatement ps=null;
		ResultSet rs=null;
		sb.append(" select * from TYADMIN_GROUP_AUTHS aga,TYADMIN_AUTHORITIES eaa ");
		sb.append("where aga.group_Id = ? and aga.auth_id=eaa.id");
		try {
			 ps= connect.prepareStatement(sb.toString());
			 ps.setLong(1,groupId.longValue() );
			 rs= ps.executeQuery();
			 while(rs.next()){
				 TyadminAuthorities aa=new TyadminAuthorities();
				 aa.setAuthMethod(rs.getString("auth_method"));
				 aa.setAuthority(rs.getString("authority"));
				 aa.setId(rs.getLong("id"));
				 aa.setServicekey(rs.getString("servicekey"));
				 result.add(aa);
			 }
			 
			 
		} catch (SQLException e) {
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
	
	public List<TyadminGroupAuths> getAllAuth() {
		// TODO Auto-generated method stub
		List<TyadminGroupAuths> authList = new ArrayList<TyadminGroupAuths>();
		try {
			authList = getAll();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return authList;
	}

	public List findAuthsByGroupID(Long groupId) {
		// TODO Auto-generated method stub
		return null;
	}

}
