package com.dascom.ssmn.entity;

import java.util.Date;

public class SsmnCancelNum  implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 899891655719678709L;
	private SsmnCancelNumId id;
	private Long type;
	private Long numberType;
	private Date cancelDate;
	private Long subManner;
	private Long cancelManner;
	private String servicestatus;
	private Long orderid;
	private Long cfstatus;
	private Long sfstatus;
	private Long ftstatus;
	private Long packageid;

	// Constructors

	

	/** default constructor */
	public SsmnCancelNum() {
	}


	/** full constructor */
	public SsmnCancelNum(SsmnCancelNumId id, Long type, Long numberType,
			Date cancelDate, Long subManner, Long cancelManner,
			String servicestatus, Long orderid, Long cfstatus, Long sfstatus,
			Long ftstatus, Long packageid) {
		super();
		this.id = id;
		this.type = type;
		this.numberType = numberType;
		this.cancelDate = cancelDate;
		this.subManner = subManner;
		this.cancelManner = cancelManner;
		this.servicestatus = servicestatus;
		this.orderid = orderid;
		this.cfstatus = cfstatus;
		this.sfstatus = sfstatus;
		this.ftstatus = ftstatus;
		this.packageid = packageid;
	}

	// Property accessors


	public Long getType() {
		return this.type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getNumberType() {
		return this.numberType;
	}

	public void setNumberType(Long numberType) {
		this.numberType = numberType;
	}

	public Date getCancelDate() {
		return this.cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Long getSubManner() {
		return this.subManner;
	}

	public void setSubManner(Long subManner) {
		this.subManner = subManner;
	}

	public Long getCancelManner() {
		return this.cancelManner;
	}

	public void setCancelManner(Long cancelManner) {
		this.cancelManner = cancelManner;
	}

	public String getServicestatus() {
		return this.servicestatus;
	}

	public void setServicestatus(String servicestatus) {
		this.servicestatus = servicestatus;
	}

	public Long getOrderid() {
		return this.orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public Long getCfstatus() {
		return this.cfstatus;
	}

	public void setCfstatus(Long cfstatus) {
		this.cfstatus = cfstatus;
	}

	public Long getSfstatus() {
		return this.sfstatus;
	}

	public void setSfstatus(Long sfstatus) {
		this.sfstatus = sfstatus;
	}

	public Long getFtstatus() {
		return this.ftstatus;
	}

	public void setFtstatus(Long ftstatus) {
		this.ftstatus = ftstatus;
	}

	public Long getPackageid() {
		return this.packageid;
	}

	public void setPackageid(Long packageid) {
		this.packageid = packageid;
	}


	public SsmnCancelNumId getId() {
		return id;
	}


	public void setId(SsmnCancelNumId id) {
		this.id = id;
	}
}
