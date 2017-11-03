package com.dascom.OPadmin.service;

import java.util.List;

import com.dascom.OPadmin.entity.TyadminLogs;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.util.Page;


public interface IOperatorLogService {
	//日志查询
	public List<TyadminLogs> searchAllLog(String opeNo,String serviceKey,String startTime,String stopTime,String logType);
    public List<String> searchLogType(String serviceKey);		
    public int getTotalCount(String opeNo,String serviceKey,String startTime, String stopTime,String logType);
	public List<TyadminLogs> searchLog(String opeNo, String serviceKey, String startTime,String stopTime,String logType,String logDes,Page page);
	public void log(TyadminOperators operator,String logtype,String des);
	public boolean isReadCdr(String descStr,String openo);
	
}
