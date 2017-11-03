package com.dascom.OPadmin.dao;

import java.util.List;

import com.dascom.OPadmin.entity.TyadminAuthorities;
import com.dascom.OPadmin.entity.TyadminGroupAuths;

public interface IAdminGroupAuth extends IEntityManager {
	/**
	 * 根据组ID精确查找组权�?
	 * @param gpid 组ID
	 * @return 该组的权限列�?
	 */
	public List<TyadminGroupAuths> findByGroupId(Long gpid);
	
	public List<TyadminAuthorities> findAuthByGroupId(Long gpid);
	
	public List<TyadminGroupAuths> getAllAuth();
}
