package com.dascom.OPadmin.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.IAdminGroup;
import com.dascom.OPadmin.dao.impl.AdminGroupImpl;
import com.dascom.OPadmin.entity.TyadminGroupAuths;
import com.dascom.OPadmin.entity.TyadminGroups;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.service.IGroupService;
import com.dascom.OPadmin.util.HibernateHelper;

import com.dascom.OPadmin.util.DBException;
import com.dascom.ssmn.util.Constants;

public class GroupServImpl implements IGroupService  , java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	private static final Logger logger = Logger.getLogger(GroupServImpl.class);
	
	private IAdminGroup groupDao = new AdminGroupImpl();

	public GroupServImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String addGroup(TyadminGroups group, long[] rights) {
		String rtCode = "";
		
		if (checkGroupExist(group.getGroupName(),group.getServicekey())) {
			rtCode = Constants.MESSAGE_GROUP_EXIST;
			return rtCode;
		}
		
		Session session = HibernateHelper.currentSession();
	    //�?测数据库连接是否成功
	    if (session == null) {
	      logger.error("fail to connect db");
	      throw new DBException("fail to connect db");
	    }
	    Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(group);
			
			addGroupRight(group.getId(), rights);
			tx.commit();
			rtCode = Constants.MESSAGE_ADD_GROUP_CUSSE;
		} catch (HibernateException e) {
			
			logger.error(e.toString());
		}catch (Exception e) {
			logger.error(e.toString());
		
		} finally {
			try {
				session.close();
			} catch (HibernateException e1) {
				e1.printStackTrace();
			}
		}
		return rtCode;
	}

	public String modGroup(TyadminGroups group, long[] rights) {
		String rtCode = "";
		Session session = HibernateHelper.currentSession();

	    //�?测数据库连接是否成功
	    if (session == null) {
	      logger.error("fail to connect db");
	      throw new DBException("fail to connect db");
	    }
	    Transaction tx = null;
		try {
			tx = session.beginTransaction();
			TyadminGroups oldGroup = (TyadminGroups) session.createQuery(
					"from TyadminGroups ag where ag.id= :id")
					.setLong("id", group.getId().longValue())
					.uniqueResult();
			oldGroup.setDescription(group.getDescription());
			oldGroup.setGroupName(group.getGroupName());
			oldGroup.setRank(group.getRank());
			session.update(oldGroup);
			deleteGroupRight(group.getId());
			
			addGroupRight(group.getId(), rights);
			
			tx.commit();
			rtCode = Constants.MESSAGE_MOD_GROUP_CUSSE;
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception e1) {
				logger.error(e1.toString());
			}
			logger.error(e.toString());
			rtCode = Constants.MESSAGE_DATABASE_ERROR;
		}finally {
			try {
				session.close();
			} catch (HibernateException e1) {
				logger.error(e1.toString());
			}
		}
		return rtCode;
	}

	
	public TyadminGroups searchGroup(Long groupId) {
		TyadminGroups ag = null;
		if(groupId != null){
			ag = groupDao.searchByGroupId(groupId);
			return ag;
		}else
			return null;
	}

	public String deleteGroup(Long groupId) {
		String rtCode = "";
		Session session = HibernateHelper.currentSession();

	    //�?测数据库连接是否成功
	    if (session == null) {
	      logger.error("fail to connect db");
	      throw new DBException("fail to connect db");
	    }
	    Transaction tx = null;
		try {
			tx = session.beginTransaction();
			//delete operator under this group
			
			Query q=session.createQuery(" from TyadminOperators operator where operator.groupId =:groupId").setLong("groupId", groupId);
			List<TyadminOperators> result=q.list();
			for(int i=0;i<result.size();i++){
				session.delete((TyadminOperators)result.get(i));
			}
			//delete auths bined with this group
			deleteGroupRight(groupId);
			//delete group
			q=session.createQuery(" from TyadminGroups gp where gp.id =:gpid").setLong("gpid", groupId);
			List<TyadminGroups> result1=q.list();
			for(int i=0;i<result1.size();i++){
				session.delete((TyadminGroups)result1.get(i));
			}
			tx.commit();
			rtCode = Constants.MESSAGE_DEL_GROUP_CUSSE;
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception e1) {
				logger.error(e1.toString());
			}
			logger.error(e.toString());
			rtCode = Constants.MESSAGE_DATABASE_ERROR;
		}finally {
			try {
				session.close();
			} catch (HibernateException e1) {
				logger.error(e1.toString());
			}
		}
		return rtCode;
	}
	
	public TyadminGroups lookForDetail(Long groupId) {
		Object o = null;
		try {
			o = groupDao.getByPrimaryKey(groupId);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return (TyadminGroups) o;
	}
	

	private void deleteGroupRight(Long groupId)
			throws HibernateException {
		Session session = HibernateHelper.currentSession();

	    //�?测数据库连接是否成功
	    if (session == null) {
	      logger.error("fail to connect db");
	      throw new DBException("fail to connect db");
	    }
	    Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query q=session.createQuery("from TyadminGroupAuths aga where aga.groupId =:groupId").setLong("groupId", groupId);
			List<TyadminGroupAuths> result=q.list();
			for(int i=0;i<result.size();i++){
				session.delete((TyadminGroupAuths)result.get(i));
			}
			tx.commit();
	    }catch (Exception e) {
	    	logger.error(e.toString());
	    }finally {
			try {
				session.close();
			} catch (HibernateException e1) {
				logger.error(e1.toString());
			}
		}
	    
	}

	private void addGroupRight(Long groupId, long[] rights)
			throws HibernateException {
		
		Session session = HibernateHelper.currentSession();

	    //�?测数据库连接是否成功
	    if (session == null) {
	      logger.error("fail to connect db");
	      throw new DBException("fail to connect db");
	    }
	    Transaction tx = null;

		try {
			tx = session.beginTransaction();
			for (int i = 0; i < rights.length; i++) {
				session.save(new TyadminGroupAuths(groupId,new Long(rights[i])));
			}
			tx.commit();
	    }catch (Exception e) {
	    	logger.error(e.toString());
	    }finally {
			try {
				session.close();
			} catch (HibernateException e1) {
				logger.error(e1.toString());
			}
		}

	}
	
	private boolean checkGroupExist(String groupName, String servicekey) {
		boolean flg = false;
		
		List<TyadminGroups> o = groupDao.searchByGroupName(groupName, servicekey);
		if (o.size() != 0) {
			flg = true;
		}
		return flg;
	}


}
