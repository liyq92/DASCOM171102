package com.dascom.OPadmin.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.IAdminOperator;
import com.dascom.OPadmin.dao.impl.AdminOperatorImpl;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.entity.TyadminOperatorsId;
import com.dascom.OPadmin.service.IOperatorService;
import com.dascom.OPadmin.util.Md5PasswordEncoder;
import com.dascom.ssmn.util.Constants;

public class OperatorServImpl implements IOperatorService {
	private static final Logger logger = Logger.getLogger(OperatorServImpl.class);

	private IAdminOperator operatorDao = new AdminOperatorImpl();
	public OperatorServImpl() {
		super();
	}
	public String addOperator(TyadminOperators operator) {
		String rtCode = "";
		try {
			//openo and name must be both unique.
			Object o = operatorDao.getByPrimaryKey(operator.getId());
			
			if (o != null )
				rtCode = Constants.MESSAGE_OPERATOR_EXIST;
			else {
				operatorDao.create(operator);
				rtCode = Constants.MESSAGE_ADD_OPERATOR_CUSSE;
			}
		} catch (DaoException e) {
			rtCode = Constants.MESSAGE_DATABASE_ERROR;
			logger.error(e.toString());
			
			e.printStackTrace();
		}
		return rtCode;
	}
	
//operaNew 是账号新的名字,如果名字没变,新的和老的一样
	public String modOperator(TyadminOperators operator,String operaNew) {
		String rtCode = "";
		try {			
			TyadminOperators  tempoper = (TyadminOperators)operatorDao.getByPrimaryKey(operator.getId());
			tempoper.setGroupId(operator.getGroupId());
			
			if(!(operator.getOpepwd()==null)&&!(operator.getOpepwd().equals("")))
			{
				tempoper.setOpepwd(new Md5PasswordEncoder().encodePassword(operator.getOpepwd(),"12345"));
			}
			tempoper.setAgentInfo(operator.getAgentInfo());
			tempoper.setNote(operator.getNote());
			tempoper.setLevelid(operator.getLevelid());
			//两个create字段无需修改
			
			operatorDao.edit(tempoper, tempoper.getId());
			
			//名字有变化,且不存在,可修改,使用对象主键修改不了,用sql修改
			if(operaNew !=null && !"".equals(operaNew))
			{
				operatorDao.updateOpera(tempoper.getId().getOpeno(), operaNew);
			}			
			
			rtCode = Constants.MESSAGE_MOD_OPERATOR_CUSSE;
			
		} catch (Exception e) {
			
			rtCode = Constants.MESSAGE_DATABASE_ERROR;
			logger.error(e.toString());
			e.printStackTrace();
		}
		return rtCode;
	}
	
	
	/**
	 * 根据主键返回对象
	 */
	public TyadminOperators lookForDetail(String openo,String servicekey) {
		TyadminOperators operator = null;
		try {
			TyadminOperatorsId pk = new TyadminOperatorsId(openo,servicekey);
			operator = (TyadminOperators) operatorDao.getByPrimaryKey(pk);

		} catch (DaoException e) {
			e.printStackTrace();
		}
		return operator;
	}
	
	
	
	public String deleteOperator(TyadminOperatorsId opId) {
		String rtCode = "";
		try {
			operatorDao.delete(opId);
			rtCode = Constants.MESSAGE_DEL_OPERATOR_CUSSE;
		} catch (DaoException e) {
			rtCode = Constants.MESSAGE_DATABASE_ERROR;
			logger.error(e.toString());
			e.printStackTrace();
		}
		return rtCode;
	}
	
	

	/**
	 * 正确返回空，不正确返回相应提示
	 */
	public String checkOperatorLogin(String name, String password, String servicekey) {
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		String pswdEncode = md5.encodePassword(password, "12345");
		String rtCode = "";
		List<TyadminOperators> tempList = operatorDao.findByOpeno(name,servicekey);
		if(tempList.size() <= 0) {
			rtCode = Constants.MESSAGE_OPERATOR_NOT_EXSIT;
		} else {
			TyadminOperators operator = (TyadminOperators)tempList.get(0);
			if (pswdEncode != null&& !md5.isPasswordValid(operator.getOpepwd(), password,"12345")) {
				rtCode = Constants.MESSAGE_WRONG_PASSWORD;
			}		
		}
		return rtCode;
	}
}
