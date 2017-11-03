package com.dascom.web.service;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.ext.jfinal.JFinalBeetlSql;

import com.dascom.web.bean.TyadminAuthorities;
import com.dascom.web.bean.TyadminOperators;
import com.dascom.web.dao.TyadminAuthoritiesDao;
import com.dascom.web.dao.TyadminOperatorsDao;
import com.dascom.web.util.BaseConsts;
import com.jfinal.aop.Enhancer;

public class TyadminOperatorsService {

	public static final TyadminOperatorsService tos = Enhancer.enhance(TyadminOperatorsService.class);
	
	private SQLManager dao = JFinalBeetlSql.dao();
	
	public TyadminOperators checkOperatorLogin(String name, String password, String service) throws Exception{
		String md5Hex = DigestUtils.md5Hex(password + BaseConsts.SALT);
		TyadminOperatorsDao mapper = dao.getMapper(TyadminOperatorsDao.class);
		List<TyadminOperators> findOperatorsByOpen = mapper.findOperatorsByOpen(name, service);
		if(null != findOperatorsByOpen && !findOperatorsByOpen.isEmpty()){
			TyadminOperators operators = findOperatorsByOpen.get(0);
			if(operators.getOpepwd().equals(md5Hex)){
				return operators ;
			}
			return null ;
		}
		
		return null ;
	}

	public TyadminOperators lookForDetail(String name, String service) {
		
		return null;
	}

	public List<TyadminAuthorities> findMethodList(Long group_Id, String service) {
		
		TyadminAuthoritiesDao mapper = dao.getMapper(TyadminAuthoritiesDao.class);
		List<TyadminAuthorities> findAuthMethodList = mapper.findAuthMethodList(group_Id, service);
		return findAuthMethodList ;
	}
	
}
