package com.dascom.web.dao;

import java.util.List;

import org.beetl.sql.core.annotatoin.SqlStatement;
import org.beetl.sql.core.mapper.BaseMapper;

import com.dascom.web.bean.TyadminAuthorities;

public interface TyadminAuthoritiesDao extends BaseMapper<TyadminAuthorities>{

	@SqlStatement(params="groupId,servicekey")
	List<TyadminAuthorities> findAuthMethodList(Long groupId,String servicekey);
}
