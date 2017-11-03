package com.dascom.ssmn.dao;

import org.apache.log4j.Logger;

import com.dascom.OPadmin.dao.impl.RootDao;
import com.dascom.ssmn.entity.SsmnCancelUser;

public class CancelUserDao  extends RootDao{
	private static final Logger logger =  Logger.getLogger(CancelUserDao.class);
	private static CancelUserDao dao;
	
	public synchronized static CancelUserDao getInstance(){
		if(dao == null)
			dao = new CancelUserDao();
		return dao;
	}
	
	@Override
	public Class<SsmnCancelUser> getReferenceClass() {
		return SsmnCancelUser.class;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		CancelUserDao dao = CancelUserDao.getInstance();
	}
}
