package com.dascom.web.bean;

import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "TYADMIN_AUTHORITIES")
public class TyadminAuthorities {
	
	private int id;
	private String authority;
	private String auth_method;
	private String servicekey;
	private int rank;
	private int parentid;
	private int orderid;
	private int sectionid;

}
