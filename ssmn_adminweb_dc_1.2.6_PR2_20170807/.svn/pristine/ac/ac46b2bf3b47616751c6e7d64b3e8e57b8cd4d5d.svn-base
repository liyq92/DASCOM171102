package com.dascom.OPadmin.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dascom.OPadmin.service.IGroupService;
import com.dascom.OPadmin.service.impl.GroupServImpl;

/**
 * AbstractTyadminLogs entity provides the base persistence definition of the
 * TyadminLogs entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractTyadminLogs implements java.io.Serializable {

	// Fields

	private Long id;
	private String openo;
	private Long groupid;
	private String servicekey;
	private String logType;
	private Date logTime;
	private String logDes;

	// Constructors

	/** default constructor */
	public AbstractTyadminLogs() {
	}

	/** minimal constructor */
	public AbstractTyadminLogs(String openo, String servicekey, String logType,
			Date logTime, String logDes) {
		this.openo = openo;
		this.servicekey = servicekey;
		this.logType = logType;
		this.logTime = logTime;
		this.logDes = logDes;
	}

	/** full constructor */
	public AbstractTyadminLogs(String openo, Long groupid, String servicekey,
			String logType, Date logTime, String logDes) {
		this.openo = openo;
		this.groupid = groupid;
		this.servicekey = servicekey;
		this.logType = logType;
		this.logTime = logTime;
		this.logDes = logDes;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOpeno() {
		return this.openo;
	}

	public void setOpeno(String openo) {
		this.openo = openo;
	}

	public Long getGroupid() {
		return this.groupid;
	}

	public void setGroupid(Long groupid) {
		this.groupid = groupid;
	}

	public String getServicekey() {
		return this.servicekey;
	}

	public void setServicekey(String servicekey) {
		this.servicekey = servicekey;
	}

	public String getLogType() {
		return this.logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public Date getLogTime() {
		return this.logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public String getLogDes() {
		return this.logDes;
	}

	public void setLogDes(String logDes) {
		this.logDes = logDes;
	}
	public String getLogTimeByString(){
		if(this.getLogTime() == null) return "";
    	return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getLogTime());
	}
	private IGroupService groupServ = new GroupServImpl();

	public String getGroupName(){
		if(this.getGroupid() == null) return "";
		String sgroupname = "";
		TyadminGroups adgroup = groupServ.lookForDetail(this.getGroupid());
		
		if (adgroup != null) sgroupname = adgroup.getGroupName();
		return sgroupname;
	}


}
