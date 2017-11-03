package com.dascom.ssmn.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * SsmnDcOwner entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SsmnDcOwner implements java.io.Serializable {

	// Fields

	private long id;
	private String msisdn;
	private Date createdate;
	private long source;
	private String ownerid;
	private String propertyid;
	private Set ssmnDcRelations = new HashSet(0);

	// Constructors

	/** default constructor */
	public SsmnDcOwner() {
	}

	/** minimal constructor */
	public SsmnDcOwner(long id, String msisdn, Date createdate, long source,
			String ownerid, String propertyid) {
		this.id = id;
		this.msisdn = msisdn;
		this.createdate = createdate;
		this.source = source;
		this.ownerid = ownerid;
		this.propertyid = propertyid;
	}

	/** full constructor */
	public SsmnDcOwner(long id, String msisdn, Date createdate, long source,
			String ownerid, String propertyid, Set ssmnDcRelations) {
		this.id = id;
		this.msisdn = msisdn;
		this.createdate = createdate;
		this.source = source;
		this.ownerid = ownerid;
		this.propertyid = propertyid;
		this.ssmnDcRelations = ssmnDcRelations;
	}

	// Property accessors

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMsisdn() {
		return this.msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public long getSource() {
		return this.source;
	}

	public void setSource(long source) {
		this.source = source;
	}

	public String getOwnerid() {
		return this.ownerid;
	}

	public void setOwnerid(String ownerid) {
		this.ownerid = ownerid;
	}

	public String getPropertyid() {
		return this.propertyid;
	}

	public void setPropertyid(String propertyid) {
		this.propertyid = propertyid;
	}

	public Set getSsmnDcRelations() {
		return this.ssmnDcRelations;
	}

	public void setSsmnDcRelations(Set ssmnDcRelations) {
		this.ssmnDcRelations = ssmnDcRelations;
	}

}