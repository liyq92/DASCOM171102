package com.dascom.ssmn.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import com.dascom.OPadmin.dao.impl.RootDao;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.ssmn.entity.SsmnCDR;
import com.dascom.ssmn.entity.SsmnUser;
import com.dascom.ssmn.entity.ZydcRecord;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.entity.SsmnZyChannel;

public class SsmnUserDao  extends RootDao{
	private static final Logger logger =  Logger.getLogger(SsmnUserDao.class);
	private static SsmnUserDao dao;
	
	public synchronized static SsmnUserDao getInstance(){
		if(dao == null)
			dao = new SsmnUserDao();
		return dao;
	}
	
	
	@Override
	public Class<SsmnUser> getReferenceClass() {
		return SsmnUser.class;
	}
	
	


}
