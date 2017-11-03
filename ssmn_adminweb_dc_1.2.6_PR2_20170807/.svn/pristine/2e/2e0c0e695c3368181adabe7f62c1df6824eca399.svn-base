package com.dascom.ssmn.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.impl.RootDao;
import com.dascom.ssmn.entity.SsmnZyChannel;

public class ChannelDao  extends RootDao{
	private static final Logger logger =  Logger.getLogger(ChannelDao.class);
	private static ChannelDao dao;
	
	public synchronized static ChannelDao getInstance(){
		if(dao == null)
			dao = new ChannelDao();
		return dao;
	}
	
	@Override
	public Class<SsmnZyChannel> getReferenceClass() {
		return SsmnZyChannel.class;
	}
	
	public SsmnZyChannel getChannelByName(String channelname){
		Session session = null;
		StringBuffer strHQL = new StringBuffer("");
		SsmnZyChannel ret = null;
		List<SsmnZyChannel> list = null;
		try {
			strHQL.append(" from SsmnZyChannel e1 where e1.name=:name ");
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			query.setString("name", channelname);
			list = query.list();
			if(list != null && list.size() >0)			
				ret = list.get(0);
		} catch (Exception he) {
			logger.error("-------------getLevelIDByActionGroup---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getLevelIDByActionGroup---关闭Session时出现HibernateException-----",he2);
			}
		}
		return ret;
	}
	
	public SsmnZyChannel getChannelById(Long channelId){
		Session session = null;
		StringBuffer strHQL = new StringBuffer("");
		SsmnZyChannel ret = null;
		List<SsmnZyChannel> list = null;
		try {
			strHQL.append(" from SsmnZyChannel e1 where e1.id=:id ");
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			query.setLong("id", channelId);
			list = query.list();
			if(list != null && list.size() >0)			
				ret = list.get(0);
		} catch (Exception he) {
			logger.error("-------------getChannelById---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getChannelById---关闭Session时出现HibernateException-----",he2);
			}
		}
		return ret;
	}
		
	public static void main(String[] args) {
		ChannelDao dao = ChannelDao.getInstance();
		SsmnZyChannel sc;
		try {
			sc = (SsmnZyChannel) dao.getByPrimaryKey(1000000000L);
			sc.setName("58买卖");
			dao.edit(sc, 1000000000L);
//			List<SsmnZyChannel> list = dao.getAll();
			
//			if(sc != null){
//				logger.info("========"+sc.getName());
//				logger.info("========"+sc.getCreatetime());
//				sc.setName(sc.getName()+"11");
//				dao.edit(sc, sc.getId());
//			}else{
//				logger.info("=====null==="+sc);
//			}
//			for(int i=0;i<list.size();i++){
//				logger.info("========"+list.get(i).getName());
//			}
			
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
