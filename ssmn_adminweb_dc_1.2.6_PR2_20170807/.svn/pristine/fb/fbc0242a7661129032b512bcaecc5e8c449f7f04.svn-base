package com.dascom.OPadmin.service;

import java.util.List;

import com.dascom.OPadmin.entity.TyadminGroupAuths;

public interface IAuthorityService {

	public List<TyadminGroupAuths> searchByGroupId(Long groupId);

	/**
	 * 确定用户是否拥有权限
	 * @param groupId
	 * @param serviceMehtod
	 * @return
	 */
	public boolean hasAuthMethod(Long groupId, String serviceMehtod);
}
