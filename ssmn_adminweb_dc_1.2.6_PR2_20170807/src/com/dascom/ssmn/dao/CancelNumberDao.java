package com.dascom.ssmn.dao;

import com.dascom.OPadmin.dao.impl.RootDao;
import com.dascom.ssmn.entity.SsmnCancelNum;

public class CancelNumberDao  extends RootDao{
//	private static final Logger logger =  Logger.getLogger(CancelNumberDao.class);
	private static CancelNumberDao dao;
	
	public synchronized static CancelNumberDao getInstance(){
		if(dao == null)
			dao = new CancelNumberDao();
		return dao;
	}
	
	@Override
	public Class<SsmnCancelNum> getReferenceClass() {
		return SsmnCancelNum.class;
	}
}
