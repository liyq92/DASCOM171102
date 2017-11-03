package com.dascom.ssmn.entity;

import java.util.Date;

public class SsmnCancelUserId implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -556327892738896680L;
	private String msisdn;
	private Date cancelDate;
	
	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public SsmnCancelUserId() {
	 
	}

	public SsmnCancelUserId(String msisdn, Date cancelDate) {
		this.msisdn = msisdn;
		this.cancelDate = cancelDate;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SsmnCancelNumId))
			return false;
		SsmnCancelUserId castOther = (SsmnCancelUserId) other;

		return ((this.getCancelDate() == castOther.getCancelDate()) || (this.getCancelDate() != null
				&& castOther.getCancelDate() != null && this.getCancelDate().equals(
				castOther.getCancelDate())))
				&& ((this.getMsisdn() == castOther.getMsisdn()) || (this.getMsisdn() != null
						&& castOther.getMsisdn() != null && this.getMsisdn().equals(
						castOther.getMsisdn())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCancelDate() == null ? 0 : this.getCancelDate().hashCode());
		result = 37 * result
				+ (getMsisdn() == null ? 0 : this.getMsisdn().hashCode());
		return result;
	}
}
