package com.dascom.ssmn.entity;

import java.util.Date;

public class SsmnCancelNumId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5174778660929903368L;
	private String ssmnnumber;
	private String msisdn;
	private Date initiateDate;

	// Constructors


	/** default constructor */
	public SsmnCancelNumId() {
	}

	/** full constructor */
	public SsmnCancelNumId(String ssmnnumber, String msisdn, Date initiateDate) {
		super();
		this.ssmnnumber = ssmnnumber;
		this.msisdn = msisdn;
		this.initiateDate = initiateDate;
	}
 

	 

	public String getSsmnnumber() {
		return ssmnnumber;
	}

	public void setSsmnnumber(String ssmnnumber) {
		this.ssmnnumber = ssmnnumber;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public Date getInitiateDate() {
		return initiateDate;
	}

	public void setInitiateDate(Date initiateDate) {
		this.initiateDate = initiateDate;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SsmnCancelNumId))
			return false;
		SsmnCancelNumId castOther = (SsmnCancelNumId) other;

		return ((this.getSsmnnumber() == castOther.getSsmnnumber()) || (this.getSsmnnumber() != null
				&& castOther.getSsmnnumber() != null && this.getSsmnnumber().equals(
				castOther.getSsmnnumber())))
				&& ((this.getMsisdn() == castOther.getMsisdn()) || (this.getMsisdn() != null
						&& castOther.getMsisdn() != null && this.getMsisdn()
						.equals(castOther.getMsisdn())))
				&& ((this.getInitiateDate() == castOther.getInitiateDate()) || (this.getInitiateDate() != null
						&& castOther.getInitiateDate() != null && this.getInitiateDate().equals(
						castOther.getInitiateDate())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getSsmnnumber() == null ? 0 : this.getSsmnnumber().hashCode());
		result = 37 * result
				+ (getMsisdn() == null ? 0 : this.getMsisdn().hashCode());
		result = 37 * result
				+ (getInitiateDate() == null ? 0 : this.getInitiateDate().hashCode());
		return result;
	}
}
