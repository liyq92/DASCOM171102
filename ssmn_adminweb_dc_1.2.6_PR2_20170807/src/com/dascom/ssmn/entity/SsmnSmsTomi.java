package com.dascom.ssmn.entity;

import java.util.Date;

/**
 * SsmnSmsTomi entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SsmnSmsTomi implements java.io.Serializable {

	// Fields

	private Long id;
	private String srcno;
	private String destno;
	private String ssmnnumber;
	private String msgcontent;
	private Date intime;
	private Long state;
	private String transferFlag;
	private Date handleTime;

	// Constructors

	/** default constructor */
	public SsmnSmsTomi() {
	}

	/** minimal constructor */
	public SsmnSmsTomi(String srcno, String destno, String ssmnnumber,
			String msgcontent, Date intime, String transferFlag) {
		this.srcno = srcno;
		this.destno = destno;
		this.ssmnnumber = ssmnnumber;
		this.msgcontent = msgcontent;
		this.intime = intime;
		this.transferFlag = transferFlag;
	}

	/** full constructor */
	public SsmnSmsTomi(String srcno, String destno, String ssmnnumber,
			String msgcontent, Date intime, Long state, String transferFlag,
			Date handleTime) {
		this.srcno = srcno;
		this.destno = destno;
		this.ssmnnumber = ssmnnumber;
		this.msgcontent = msgcontent;
		this.intime = intime;
		this.state = state;
		this.transferFlag = transferFlag;
		this.handleTime = handleTime;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSrcno() {
		return this.srcno;
	}

	public void setSrcno(String srcno) {
		this.srcno = srcno;
	}

	public String getDestno() {
		return this.destno;
	}

	public void setDestno(String destno) {
		this.destno = destno;
	}

	public String getSsmnnumber() {
		return this.ssmnnumber;
	}

	public void setSsmnnumber(String ssmnnumber) {
		this.ssmnnumber = ssmnnumber;
	}

	public String getMsgcontent() {
		return this.msgcontent;
	}

	public void setMsgcontent(String msgcontent) {
		this.msgcontent = msgcontent;
	}

	public Date getIntime() {
		return this.intime;
	}

	public void setIntime(Date intime) {
		this.intime = intime;
	}

	public Long getState() {
		return this.state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public String getTransferFlag() {
		return this.transferFlag;
	}

	public void setTransferFlag(String transferFlag) {
		this.transferFlag = transferFlag;
	}

	public Date getHandleTime() {
		return this.handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

}