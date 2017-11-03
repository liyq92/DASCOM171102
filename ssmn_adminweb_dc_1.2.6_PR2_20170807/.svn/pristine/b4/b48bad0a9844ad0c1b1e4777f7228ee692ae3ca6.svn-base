package com.dascom.ssmn.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.dascom.OPadmin.dao.impl.RootDao;
import com.dascom.ssmn.entity.SsmnNumber;

public class NumberDao  extends RootDao{
	private static final Logger logger =  Logger.getLogger(NumberDao.class);
	private static NumberDao dao;
	
	public synchronized static NumberDao getInstance(){
		if(dao == null)
			dao = new NumberDao();
		return dao;
	}
	
	@Override
	public Class<SsmnNumber> getReferenceClass() {
		return SsmnNumber.class;
	}
	
	public List<SsmnNumber> getNumbersByMsisdn(String msisdn){
		Session session = null;
		List<SsmnNumber> list = new ArrayList<SsmnNumber>();
		try {
			StringBuffer strHQL = new StringBuffer("from SsmnNumber where 1=1 ");
			if(msisdn != null && !"".equals(msisdn)){
				strHQL.append(" and msisdn =:msisdn ");
			}
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			if(msisdn != null && !"".equals(msisdn)){
				query.setString("msisdn", msisdn);
			}
			list = query.list(); 
			 
		} catch (Exception he) {
			logger.error("-------------getNumbersByMsisdn---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getNumbersByMsisdn---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
		
		
		
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NumberDao dao = NumberDao.getInstance();
		 List<SsmnNumber> list = dao.getNumbersByMsisdn("13100010002");
		 for(int i=0;i<list.size();i++){
				logger.info("-----------getName-- -----"+list.get(i).getSsmnnumber());
			}
	}

}
