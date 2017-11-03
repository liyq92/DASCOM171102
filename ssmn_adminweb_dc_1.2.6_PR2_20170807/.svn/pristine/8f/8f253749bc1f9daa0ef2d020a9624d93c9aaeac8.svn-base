package com.dascom.OPadmin.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.dascom.OPadmin.dao.IAdminAuthority;
import com.dascom.OPadmin.entity.TyadminAuthorities;

public class AdminAuthorityImpl extends RootDao implements IAdminAuthority {

	public List<String> findByGroupId(long gpid, String serviceKey) {
		List<String> authList = new ArrayList<String>();
		Session session = this.adapter.openSession();
		StringBuffer sb = new StringBuffer();
		sb.append("select auth.authMethod from TyadminAuthorities auth, TyadminGroupAuths gpauth ");
		sb.append("where auth.id = gpauth.authId ");
		sb.append("and gpauth.groupId = :gourpid ");
		sb.append("and auth.servicekey = :servicekey ");
		try {
			authList = session.createQuery(sb.toString()).setLong("gourpid", gpid).setString("servicekey", serviceKey).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (HibernateException e1) {
				e1.printStackTrace();
			}
		}
		return authList;

	}
	public List<TyadminAuthorities> findByGroup(long gpid) {
		List<TyadminAuthorities> authList = new ArrayList<TyadminAuthorities>();
		Session session = this.adapter.openSession();
		StringBuffer sb = new StringBuffer();
		sb.append("select auth from TyadminAuthorities auth, TyadminGroupAuths gpauth ");
		sb.append("where auth.id = gpauth.authId ");
		
		sb.append("and gpauth.groupId = :gourpid");
		try {
			authList = session.createQuery(sb.toString()).setLong("gourpid", gpid).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (HibernateException e1) {
				e1.printStackTrace();
			}
		}
		return authList;

	}
	


	public Class getReferenceClass() {
		return TyadminAuthorities.class;
	}
	/*
	 *  取rank为1 的，并且按sectionId, orderId排 序(全升序)
	 */
	public List<TyadminAuthorities> findAuths(String serviceKey,Long rank) {
		// TODO Auto-generated method stub
		List<TyadminAuthorities> authList = new ArrayList<TyadminAuthorities>();
		Session session = this.adapter.openSession();
		StringBuffer sb = new StringBuffer();
		sb.append("from TyadminAuthorities auth ");
		//sb.append("where auth.servicekey = :servicekey and rank>=:rank ");
		//20160713 修改
		sb.append("where auth.servicekey = :servicekey and rank =1 and orderId is not null and sectionId is not null order by sectionId, orderId ");
		try {
			authList = session.createQuery(sb.toString()).setString("servicekey",serviceKey).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (HibernateException e1) {
				e1.printStackTrace();
			}
		}
		return authList;
	}
	
	//查找子类,查子类不需要排 序 ,rank为2的
	public List<TyadminAuthorities> findChildrenAuths(String serviceKey,Long parentId) {
		// TODO Auto-generated method stub
		List<TyadminAuthorities> authList = new ArrayList<TyadminAuthorities>();
		Session session = this.adapter.openSession();
		StringBuffer sb = new StringBuffer();
		sb.append("from TyadminAuthorities auth ");
		//20160713 修改
		sb.append("where auth.servicekey = :servicekey and rank =2 and parentId = :parentId ");
		try {
			authList = session.createQuery(sb.toString()).setString("servicekey",serviceKey).setLong("parentId", parentId).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (HibernateException e1) {
				e1.printStackTrace();
			}
		}
		return authList;
	}

	public List<TyadminAuthorities> findServiceAuths(String serviceKey) {
		List<TyadminAuthorities> authList = new ArrayList<TyadminAuthorities>();
		Session session = this.adapter.openSession();
		StringBuffer sb = new StringBuffer();
		sb.append("from TyadminAuthorities auth ");
		sb.append("where auth.servicekey = :servicekey");
		try {
			authList = session.createQuery(sb.toString()).setString("servicekey",serviceKey).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (HibernateException e1) {
				e1.printStackTrace();
			}
		}
		return authList;
	}
	
//	public boolean isAuthExistByKey1(String serviceKey, String authmethod){
//		List authList = new ArrayList();
//		Session session = this.adapter.openSession();
//		StringBuffer sb = new StringBuffer();
//		sb.append("from AdminAuthorities auth ");
//		sb.append("where auth.servicekey = :servicekey and auth.authMethod = :authmethod");
//		try {
//			authList = session.createQuery(sb.toString()).setString("servicekey",serviceKey).setString("authmethod", authmethod).list();
//		} catch (HibernateException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				session.close();
//			} catch (HibernateException e1) {
//				e1.printStackTrace();
//			}
//		}
//		return authList.size()>0 ? true:false;
//	}


}
