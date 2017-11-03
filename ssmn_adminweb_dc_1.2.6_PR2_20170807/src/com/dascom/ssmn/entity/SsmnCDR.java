package com.dascom.ssmn.entity;

import java.util.Date;

public class SsmnCDR {

	private Long streamNumber;
	private String callingAddress;
	private String calledAddress;
	private String originalCalled;
	private Integer callType;
	private String msisdn;
	private String ssmnNumber;
	private Date callStartTime;
	private Date callStopTime;
	private Integer callDuration;
	private Integer endReason;
	private Integer readStatus;
	private String incomingCIN;
	private String outgoingCPN;
	private Long dccalltype;
	
	
	public SsmnCDR() {
		super();
	}

	public SsmnCDR(Long streamNumber, Date callStartTime, Integer callDuration,
			Integer endReason, String incomingCIN, String outgoingCPN) {
		super();
		this.streamNumber = streamNumber;
		this.callStartTime = callStartTime;
		this.callDuration = callDuration;
		this.endReason = endReason;
		this.incomingCIN = incomingCIN;
		this.outgoingCPN = outgoingCPN;
	}

	public SsmnCDR(Long streamNumber, Date callStartTime, Date callStopTime, Integer callDuration,
			Integer endReason, String incomingCIN, String outgoingCPN, String ssmnNumber) {
		super();
		this.streamNumber = streamNumber;
		this.callStartTime = callStartTime;
		this.callStopTime = callStopTime;
		this.callDuration = callDuration;
		this.endReason = endReason;
		this.incomingCIN = incomingCIN;
		this.outgoingCPN = outgoingCPN;
		this.ssmnNumber = ssmnNumber;
	}



	public Date getCallStopTime() {
		return callStopTime;
	}



	public void setCallStopTime(Date callStopTime) {
		this.callStopTime = callStopTime;
	}



	public SsmnCDR(String msisdn, String ssmnNumber) {
		super();
		this.msisdn = msisdn;
		this.ssmnNumber = ssmnNumber;
	}

	public String getIncomingCIN() {
		return incomingCIN;
	}


	public void setIncomingCIN(String incomingCIN) {
		this.incomingCIN = incomingCIN;
	}

	public String getOutgoingCPN() {
		return outgoingCPN;
	}

	public void setOutgoingCPN(String outgoingCPN) {
		this.outgoingCPN = outgoingCPN;
	}

	public String getCalledAddress() {
		return calledAddress;
	}



	public void setCalledAddress(String calledAddress) {
		this.calledAddress = calledAddress;
	}



	public Integer getReadStatus() {
		return readStatus;
	}


	public void setReadStatus(Integer readStatus) {
		this.readStatus = readStatus;
	}

	public Integer getCallType() {
		return callType;
	}


	public void setCallType(Integer callType) {
		this.callType = callType;
	}

	public Long getStreamNumber() {
		return streamNumber;
	}
	public void setStreamNumber(Long streamNumber) {
		this.streamNumber = streamNumber;
	}
	public String getCallingAddress() {
		if(callingAddress==null){
			callingAddress="";
		}
		return callingAddress;
	}
	public void setCallingAddress(String callingAddress) {
		this.callingAddress = callingAddress;
	}
	public String getOriginalCalled() {
		if(originalCalled==null){
			originalCalled="";
		}
		return originalCalled;
	}
	public void setOriginalCalled(String originalCalled) {
		this.originalCalled = originalCalled;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getSsmnNumber() {
		return ssmnNumber;
	}
	public void setSsmnNumber(String ssmnNumber) {
		this.ssmnNumber = ssmnNumber;
	}
	public Date getCallStartTime() {
		return callStartTime;
	}
	public void setCallStartTime(Date callStartTime) {
		this.callStartTime = callStartTime;
	}
	public Integer getCallDuration() {
		return callDuration;
	}
	public void setCallDuration(Integer callDuration) {
		this.callDuration = callDuration;
	}
	
	public Integer getEndReason() {
		return endReason;
	}
	public void setEndReason(Integer endReason) {
		this.endReason = endReason;
	}
	
	public Long getDccalltype()
	{
		return this.dccalltype;
	}
	
	public void setDccalltype(Long dccalltype)
	{
		this.dccalltype = dccalltype;
	}
	
}
