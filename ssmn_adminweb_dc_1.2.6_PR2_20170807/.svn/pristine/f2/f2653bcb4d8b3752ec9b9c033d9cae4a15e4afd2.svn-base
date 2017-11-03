package com.dascom.OPadmin.dao;

import java.util.List;

import com.dascom.OPadmin.entity.TyadminAuthorities;

public interface IAdminAuthority extends IEntityManager {
	
	public List<String> findByGroupId(long gpid,String serviceKey);

	public List<TyadminAuthorities> findByGroup(long gpid);

	public List<TyadminAuthorities> findServiceAuths(String serviceKey);
	
	public List<TyadminAuthorities> findAuths(String serviceKey,Long rank);
	
	public List<TyadminAuthorities> findChildrenAuths(String serviceKey,Long parentId);
}
