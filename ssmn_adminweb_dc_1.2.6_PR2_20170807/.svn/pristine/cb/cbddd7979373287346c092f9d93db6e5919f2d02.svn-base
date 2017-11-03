package com.dascom.OPadmin.dao;

import java.util.List;

import com.dascom.OPadmin.entity.TyadminLogs;

public interface IAdminOperatorLog extends IEntityManager {
	public List<String> findOperationType(String serviceKey);

	public int getTotalCount(String operId,String serviceKey,String startTime, String stopTime, String optType);

	public List<TyadminLogs> findAllLogs(String opeId, String serviceKey, String startTime, String stopTime, String optType);
	
	public List<TyadminLogs> findByPage(String opeId, String serviceKey, String startTime, String stopTime, 
			String optType, int index, int maxCount);
}
