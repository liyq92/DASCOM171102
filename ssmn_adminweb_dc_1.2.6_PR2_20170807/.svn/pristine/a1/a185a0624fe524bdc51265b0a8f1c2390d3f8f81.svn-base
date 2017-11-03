package com.dascom.OPadmin.entity;

/**
 * AbstractTyadminOperatorsId entity provides the base persistence definition of
 * the TyadminOperatorsId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractTyadminOperatorsId implements
		java.io.Serializable {

	// Fields

	private String openo;
	private String servicekey;

	// Constructors

	/** default constructor */
	public AbstractTyadminOperatorsId() {
	}

	/** full constructor */
	public AbstractTyadminOperatorsId(String openo, String servicekey) {
		this.openo = openo;
		this.servicekey = servicekey;
	}

	// Property accessors

	public String getOpeno() {
		return this.openo;
	}

	public void setOpeno(String openo) {
		this.openo = openo;
	}

	public String getServicekey() {
		return this.servicekey;
	}

	public void setServicekey(String servicekey) {
		this.servicekey = servicekey;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractTyadminOperatorsId))
			return false;
		AbstractTyadminOperatorsId castOther = (AbstractTyadminOperatorsId) other;

		return ((this.getOpeno() == castOther.getOpeno()) || (this.getOpeno() != null
				&& castOther.getOpeno() != null && this.getOpeno().equals(
				castOther.getOpeno())))
				&& ((this.getServicekey() == castOther.getServicekey()) || (this
						.getServicekey() != null
						&& castOther.getServicekey() != null && this
						.getServicekey().equals(castOther.getServicekey())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getOpeno() == null ? 0 : this.getOpeno().hashCode());
		result = 37
				* result
				+ (getServicekey() == null ? 0 : this.getServicekey()
						.hashCode());
		return result;
	}

}
