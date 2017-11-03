package com.dascom.OPadmin.dao;

import java.util.List;


import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.entity.TyadminOperatorLogs;
import com.dascom.ssmn.entity.SsmnZyLevel;
public interface IAdminOperator extends IEntityManager {
	
	/**
	 * 根据别名精确查找操作�?
	 * @param oName
	 * @return 操作员对象或null
	 */
	public List<TyadminOperators> findByServiceKey(String serviceKey,String rank,String keyword,String bdparam,String zoneparam,String areaparam,String bagparam,SsmnZyLevel zylevel,Integer i,Integer j);
	
	public int findcountByServiceKey(String serviceKey,String bdparam,String zoneparam,String areaparam,String bagparam,String rank,String keyword,SsmnZyLevel zylevel);
	
	public List<TyadminOperators> findByOpeno(String openo, String serviceKey) ;
	
	public int countNameAndPasswordMatch(String name, String password);

	public int countOperatorByGroup(Long groupid);
	
	//查询操作员登录详情
	public List<TyadminOperatorLogs> findOpenoLoginDetail(String starttime,String endtime,String loginType, String busDep,String zoneparam,String areaparam,String bagparam,String openo,SsmnZyLevel zylevel,Integer index,Integer maxcount);
	public int countOpenoLoginDetail(String starttime,String endtime,String loginType, String busDep,String zoneparam,String areaparam,String bagparam,String openo,SsmnZyLevel zylevel);
	public int findCdrCount(String openo,String loginTime,String endTime,int flag,String sqlsub);
	public List<TyadminOperatorLogs> findOpenoLoginDetailByOpeno(String openo,String starttime,String endtime,Integer index,Integer maxcount);
	public int findOpenoLoginDetailByOpenoCount(String openo,String starttime,String endtime);
	public String getLastTime(String stime,String sopera);
	public List<TyadminOperatorLogs> findOpenoLoginDetailByExport(String starttime,String endtime,List<TyadminOperatorLogs> operalist);
	public boolean updateOpera(String operaOld,String operaNew);
}
