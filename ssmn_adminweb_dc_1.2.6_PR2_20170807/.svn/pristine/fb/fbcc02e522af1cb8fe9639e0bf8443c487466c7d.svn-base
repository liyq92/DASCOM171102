package com.dascom.OPadmin.service.impl;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.dascom.OPadmin.dao.IAdminAuthority;
import com.dascom.OPadmin.dao.IAdminGroupAuth;
import com.dascom.OPadmin.dao.impl.AdminAuthorityImpl;
import com.dascom.OPadmin.dao.impl.AdminGroupAuthImpl;
import com.dascom.OPadmin.entity.TyadminAuthorities;
import com.dascom.OPadmin.entity.TyadminGroupAuths;
import com.dascom.OPadmin.service.IAuthorityService;

public class AuthorityServImpl implements IAuthorityService {

	IAdminGroupAuth groupAuthDao = new AdminGroupAuthImpl();
	IAdminAuthority authDao = new AdminAuthorityImpl();
	public AuthorityServImpl() {
		super();
	}
	
	public boolean hasAuthMethod(Long groupId, String serviceMethod){
		boolean hasauth = false;
		List<TyadminAuthorities> authList = new ArrayList<TyadminAuthorities>();
		authList = groupAuthDao.findAuthByGroupId(groupId);
		Iterator<TyadminAuthorities> it = authList.iterator();
		while(it.hasNext()){
			TyadminAuthorities aga = (TyadminAuthorities)it.next();
				if(serviceMethod.equalsIgnoreCase(aga.getAuthMethod())){
					hasauth = true;
					break;
				}

		}
		return hasauth;
	}
	
	public List<TyadminGroupAuths> searchByGroupId(Long groupId){
		return groupAuthDao.findByGroupId(groupId);
	}
}
