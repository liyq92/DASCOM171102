package com.dascom.OPadmin.dao;

import java.util.List;

import com.dascom.OPadmin.entity.TyadminGroups;

public interface IAdminGroup extends IEntityManager {
	
	/**
	 * 根据组名模糊查询
	 * @param gpname 组名
	 * @return 组列�?
	 */
	public List<TyadminGroups> findAllGroups(String serviceKey,Long rank);
	
	public List<TyadminGroups> findByGroupName(String gpname, String serviceKey, Long rank);
	
	public List<TyadminGroups> searchByGroupName(String gpname,String servicekey);
	
	public TyadminGroups searchByGroupId(Long groupId);
	
}
