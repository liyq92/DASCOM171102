package com.dascom.OPadmin.service;

import com.dascom.OPadmin.entity.TyadminGroups;

public interface IGroupService {
	
	public String addGroup(TyadminGroups group, long[] rights);
	public String modGroup(TyadminGroups group, long[] rights);
	public TyadminGroups searchGroup(Long groupId);
	public String deleteGroup(Long groupId);
	public TyadminGroups lookForDetail(Long groupId);
}
