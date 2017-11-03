package com.dascom.ssmn.entity;

import java.util.Date;

/**
 * SsmnRecord entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SsmnRecord implements java.io.Serializable {

	// Fields

	private Long streamnumber;
	private Long callingmsghigh;
	private Long callingmsglow;
	private Long msglen;
	private Long calledmsghigh;
	private Long calledmsglow;
	private Date recordTime;
	private Long handleFlag;
	private Date handleTime;
	private String filepath;
	private String filename;

	// Constructors

	/** default constructor */
	public SsmnRecord() {
	}

	/** minimal constructor */
	public SsmnRecord(Long callingmsghigh, Long callingmsglow, Long msglen,
			Long calledmsghigh, Long calledmsglow, Date recordTime,
			Long handleFlag) {
		this.callingmsghigh = callingmsghigh;
		this.callingmsglow = callingmsglow;
		this.msglen = msglen;
		this.calledmsghigh = calledmsghigh;
		this.calledmsglow = calledmsglow;
		this.recordTime = recordTime;
		this.handleFlag = handleFlag;
	}

	/** full constructor */
	public SsmnRecord(Long callingmsghigh, Long callingmsglow, Long msglen,
			Long calledmsghigh, Long calledmsglow, Date recordTime,
			Long handleFlag, Date handleTime, String filepath) {
		this.callingmsghigh = callingmsghigh;
		this.callingmsglow = callingmsglow;
		this.msglen = msglen;
		this.calledmsghigh = calledmsghigh;
		this.calledmsglow = calledmsglow;
		this.recordTime = recordTime;
		this.handleFlag = handleFlag;
		this.handleTime = handleTime;
		this.filepath = filepath;
	}
	
	public SsmnRecord(String filepath,String filename)
	{
		 this.filepath = filepath;
		 this.filename = filename;		
	}

	// Property accessors

	public Long getStreamnumber() {
		return this.streamnumber;
	}

	public void setStreamnumber(Long streamnumber) {
		this.streamnumber = streamnumber;
	}

	public Long getCallingmsghigh() {
		return this.callingmsghigh;
	}

	public void setCallingmsghigh(Long callingmsghigh) {
		this.callingmsghigh = callingmsghigh;
	}

	public Long getCallingmsglow() {
		return this.callingmsglow;
	}

	public void setCallingmsglow(Long callingmsglow) {
		this.callingmsglow = callingmsglow;
	}

	public Long getMsglen() {
		return this.msglen;
	}

	public void setMsglen(Long msglen) {
		this.msglen = msglen;
	}

	public Long getCalledmsghigh() {
		return this.calledmsghigh;
	}

	public void setCalledmsghigh(Long calledmsghigh) {
		this.calledmsghigh = calledmsghigh;
	}

	public Long getCalledmsglow() {
		return this.calledmsglow;
	}

	public void setCalledmsglow(Long calledmsglow) {
		this.calledmsglow = calledmsglow;
	}

	public Date getRecordTime() {
		return this.recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	public Long getHandleFlag() {
		return this.handleFlag;
	}

	public void setHandleFlag(Long handleFlag) {
		this.handleFlag = handleFlag;
	}

	public Date getHandleTime() {
		return this.handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public String getFilepath() {
		return this.filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
	public String getFilename()
	{
		return this.filename;
	}
	
	public void setFilename(String filename)
	{
		this.filename = filename;
	}

}