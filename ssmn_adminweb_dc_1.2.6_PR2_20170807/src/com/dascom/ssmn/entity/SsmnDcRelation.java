package com.dascom.ssmn.entity;

import java.util.Date;

/**
 * SsmnDcRelation entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SsmnDcRelation implements java.io.Serializable {

	// Fields

	private long id;
	private SsmnDcAgent ssmnDcAgent;
	private SsmnDcOwner ssmnDcOwner;
	private String ssmnnumber;
	private String ownerMsisdn;
	private String agentMsisdn;
	private Date createline;
	private String name;
	private String branchactiongroup;

	// Constructors

	/** default constructor */
	public SsmnDcRelation() {
	}

	/** full constructor */
	public SsmnDcRelation(long id, SsmnDcAgent ssmnDcAgent,
			SsmnDcOwner ssmnDcOwner, String ssmnnumber, String ownerMsisdn,
			String agentMsisdn, Date createline) {
		this.id = id;
		this.ssmnDcAgent = ssmnDcAgent;
		this.ssmnDcOwner = ssmnDcOwner;
		this.ssmnnumber = ssmnnumber;
		this.ownerMsisdn = ownerMsisdn;
		this.agentMsisdn = agentMsisdn;
		this.createline = createline;
	}

	// Property accessors

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SsmnDcAgent getSsmnDcAgent() {
		return this.ssmnDcAgent;
	}

	public void setSsmnDcAgent(SsmnDcAgent ssmnDcAgent) {
		this.ssmnDcAgent = ssmnDcAgent;
	}

	public SsmnDcOwner getSsmnDcOwner() {
		return this.ssmnDcOwner;
	}

	public void setSsmnDcOwner(SsmnDcOwner ssmnDcOwner) {
		this.ssmnDcOwner = ssmnDcOwner;
	}

	public String getSsmnnumber() {
		return this.ssmnnumber;
	}

	public void setSsmnnumber(String ssmnnumber) {
		this.ssmnnumber = ssmnnumber;
	}

	public String getOwnerMsisdn() {
		return this.ownerMsisdn;
	}

	public void setOwnerMsisdn(String ownerMsisdn) {
		this.ownerMsisdn = ownerMsisdn;
	}

	public String getAgentMsisdn() {
		return this.agentMsisdn;
	}

	public void setAgentMsisdn(String agentMsisdn) {
		this.agentMsisdn = agentMsisdn;
	}

	public Date getCreateline() {
		return this.createline;
	}

	public void setCreateline(Date createline) {
		this.createline = createline;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getBranchactiongroup() {
		return branchactiongroup;
	}

	public void setBranchactiongroup(String branchactiongroup) {
		this.branchactiongroup = branchactiongroup;
	}

}