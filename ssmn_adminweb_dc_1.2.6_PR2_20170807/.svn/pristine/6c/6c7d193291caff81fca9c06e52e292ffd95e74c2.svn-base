package com.dascom.ssmn.entity;

import java.util.Date;

/**
 * SsmnZyClientnum entity. @author MyEclipse Persistence Tools
 */

public class SsmnZyClientnum implements java.io.Serializable {

	// Fields

	private long id;
	private String clientnum;
	private String clientname;
	private Date intime;
	private Date calltime;
	private String remark;
	private String usermsisdn;
	private String username;
	private int status;
	private Date updatetime;
	private boolean callstatus;//已拨/未拨
	private String failedInfo;//暂存导入失败的原因

	// Constructors

	/** default constructor */
	public SsmnZyClientnum() {
	}

	/** minimal constructor */
	public SsmnZyClientnum(long id, String clientnum, Date intime,
			Date calltime, String usermsisdn, String username, int status) {
		this.id = id;
		this.clientnum = clientnum;
		this.intime = intime;
		this.calltime = calltime;
		this.usermsisdn = usermsisdn;
		this.username = username;
		this.status = status;
	}

	/** full constructor */
	public SsmnZyClientnum(long id, String clientnum, String clientname,
			Date intime, Date calltime, String remark, String usermsisdn,
			String username, int status, Date updatetime) {
		this.id = id;
		this.clientnum = clientnum;
		this.clientname = clientname;
		this.intime = intime;
		this.calltime = calltime;
		this.remark = remark;
		this.usermsisdn = usermsisdn;
		this.username = username;
		this.status = status;
		this.updatetime = updatetime;
	}
	
	public SsmnZyClientnum(long id,String usermsisdn,
			String username, String clientnum, String clientname,
			 String remark,Date calltime) {
		this.id = id;
		this.clientnum = clientnum;
		this.clientname = clientname;
		this.remark = remark;
		this.usermsisdn = usermsisdn;
		this.username = username;
		this.calltime =calltime;
	}
	// Property accessors

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getClientnum() {
		return this.clientnum;
	}

	public void setClientnum(String clientnum) {
		this.clientnum = clientnum;
	}

	public String getClientname() {
		return this.clientname;
	}

	public void setClientname(String clientname) {
		this.clientname = clientname;
	}

	public Date getIntime() {
		return this.intime;
	}

	public void setIntime(Date intime) {
		this.intime = intime;
	}

	public Date getCalltime() {
		return this.calltime;
	}

	public void setCalltime(Date calltime) {
		this.calltime = calltime;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUsermsisdn() {
		return this.usermsisdn;
	}

	public void setUsermsisdn(String usermsisdn) {
		this.usermsisdn = usermsisdn;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	
	public boolean getCallstatus(){
		return this.callstatus;
	}
	
	public void setCallstatus(boolean callstatus){
		this.callstatus =callstatus;
	}
	
	public String getFailedInfo(){
		return this.failedInfo;
	}
	
	public void setFailedInfo(String failedInfo){
		this.failedInfo =failedInfo;
	}
	
}