package com.dascom.ssmn.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * SsmnDcAgent entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SsmnDcAgent implements java.io.Serializable {

	// Fields

	private long id;
	private String msisdn;
	private Date createdate;
	private String empid;
	private String empname;
	private String deptid;
	private String deptname;
	private Set ssmnDcRelations = new HashSet(0);

	// Constructors

	/** default constructor */
	public SsmnDcAgent() {
	}

	/** minimal constructor */
	public SsmnDcAgent(long id, String msisdn, Date createdate, String empid,
			String deptid) {
		this.id = id;
		this.msisdn = msisdn;
		this.createdate = createdate;
		this.empid = empid;
		this.deptid = deptid;
	}

	/** full constructor */
	public SsmnDcAgent(long id, String msisdn, Date createdate, String empid,
			String empname, String deptid, String deptname, Set ssmnDcRelations) {
		this.id = id;
		this.msisdn = msisdn;
		this.createdate = createdate;
		this.empid = empid;
		this.empname = empname;
		this.deptid = deptid;
		this.deptname = deptname;
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

	public String getEmpid() {
		return this.empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	public String getEmpname() {
		return this.empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	public String getDeptid() {
		return this.deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getDeptname() {
		return this.deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public Set getSsmnDcRelations() {
		return this.ssmnDcRelations;
	}

	public void setSsmnDcRelations(Set ssmnDcRelations) {
		this.ssmnDcRelations = ssmnDcRelations;
	}

}