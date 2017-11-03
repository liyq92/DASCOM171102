package com.dascom.ssmn.entity;

import java.util.Date;

public class SsmnCancelUser implements java.io.Serializable {
	// Fields

	private SsmnCancelUserId id;
	private String pin;
	private Long subManner;
	private String servicestatus;
	private String tempPin;
	private String crbtFlag;
	private String username;
	private String userid;
	private Long callingtype;
	private String callingnumber;
	private Date subDate;
	private String question;
	private String answer;
	private String agentId;
	private Long cancelManner;
	private String usertype;
	private String devicetoken;
	private Date logintime;
	private Long type;

	// Constructors

	public SsmnCancelUser(SsmnCancelUserId id, String pin, Long subManner,
			String servicestatus, String tempPin, String crbtFlag,
			String username, String userid, Long callingtype,
			String callingnumber, Date subDate, String question, String answer,
			String agentId, Long cancelManner, String usertype,
			String devicetoken, Date logintime, Long type ) {
		super();
		this.id = id;
		this.pin = pin;
		this.subManner = subManner;
		this.servicestatus = servicestatus;
		this.tempPin = tempPin;
		this.crbtFlag = crbtFlag;
		this.username = username;
		this.userid = userid;
		this.callingtype = callingtype;
		this.callingnumber = callingnumber;
		this.subDate = subDate;
		this.question = question;
		this.answer = answer;
		this.agentId = agentId;
		this.cancelManner = cancelManner;
		this.usertype = usertype;
		this.devicetoken = devicetoken;
		this.logintime = logintime;
		this.type = type;
	}

	/** default constructor */
	public SsmnCancelUser() {
	}

	/** full constructor */
 

	// Property accessors

 

	public String getPin() {
		return this.pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public Long getSubManner() {
		return this.subManner;
	}

	public void setSubManner(Long subManner) {
		this.subManner = subManner;
	}

	public String getServicestatus() {
		return this.servicestatus;
	}

	public void setServicestatus(String servicestatus) {
		this.servicestatus = servicestatus;
	}

	public String getTempPin() {
		return this.tempPin;
	}

	public void setTempPin(String tempPin) {
		this.tempPin = tempPin;
	}

	public String getCrbtFlag() {
		return this.crbtFlag;
	}

	public void setCrbtFlag(String crbtFlag) {
		this.crbtFlag = crbtFlag;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Long getCallingtype() {
		return this.callingtype;
	}

	public void setCallingtype(Long callingtype) {
		this.callingtype = callingtype;
	}

	public String getCallingnumber() {
		return this.callingnumber;
	}

	public void setCallingnumber(String callingnumber) {
		this.callingnumber = callingnumber;
	}

	public Date getSubDate() {
		return this.subDate;
	}

	public void setSubDate(Date subDate) {
		this.subDate = subDate;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAgentId() {
		return this.agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public Long getCancelManner() {
		return this.cancelManner;
	}

	public void setCancelManner(Long cancelManner) {
		this.cancelManner = cancelManner;
	}

	public String getUsertype() {
		return this.usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getDevicetoken() {
		return this.devicetoken;
	}

	public void setDevicetoken(String devicetoken) {
		this.devicetoken = devicetoken;
	}

	public Date getLogintime() {
		return this.logintime;
	}

	public void setLogintime(Date logintime) {
		this.logintime = logintime;
	}

 

	public Long getType() {
		return this.type;
	}

	public void setType(Long type) {
		this.type = type;
	}


	public SsmnCancelUserId getId() {
		return id;
	}

	public void setId(SsmnCancelUserId id) {
		this.id = id;
	}
}
