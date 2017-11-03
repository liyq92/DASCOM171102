package com.dascom.OPadmin.service;


import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.entity.TyadminOperatorsId;

public interface IOperatorService {
	
	public String addOperator(TyadminOperators operator);

	public String modOperator(TyadminOperators operator,String operaNew);
	
	public String deleteOperator(TyadminOperatorsId opId);	

	public TyadminOperators lookForDetail(String openo,String servicekey);

	public String checkOperatorLogin(String name, String password, String serviceKey);
			
}
