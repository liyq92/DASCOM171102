package com.dascom.web.dao;

import java.util.List;

import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.mapper.BaseMapper;

import com.dascom.web.bean.TyadminOperators;

public interface TyadminOperatorsDao extends BaseMapper<TyadminOperators>{

	@Sql(value=" select * from tyadmin_operators where openo = ? and servicekey = ?")
	List<TyadminOperators> findOperatorsByOpen(String open, String servicekey);
}
