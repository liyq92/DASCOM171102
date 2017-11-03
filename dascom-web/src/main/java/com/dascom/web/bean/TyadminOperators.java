package com.dascom.web.bean;

import java.util.Date;

import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "TYADMIN_OPERATORS")
public class TyadminOperators {

	private String openo;

	private String servicekey;

	private String opepwd;

	private Long group_Id;

	private String create_user;

	private Date create_time;

	private String agent_info;

	private String note;

	private long levelid;
	// private long update_time;
	// private long login_failed_count;
	// private long login_failed_time;

}
