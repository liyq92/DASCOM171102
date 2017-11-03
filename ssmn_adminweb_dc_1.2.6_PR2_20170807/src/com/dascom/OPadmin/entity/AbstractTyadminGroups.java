package com.dascom.OPadmin.entity;

import java.util.Date;

/**
 * AbstractTyadminGroups entity provides the base persistence definition of the
 * TyadminGroups entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractTyadminGroups implements java.io.Serializable {

	// Fields

	private Long id;
	private String groupName;
	private String description;
	private String servicekey;
	private Date createDate;
	private String operatorId;
	private Long rank;
	/*private String provincecity;
	private String company;
	private String businessdepartment;
	private String warzone;
	private String area;
	private String branchactiongroup;*/

	// Constructors

	/** default constructor */
	public AbstractTyadminGroups() {
	}

	/** minimal constructor */
	public AbstractTyadminGroups(String groupName, String servicekey,
			Date createDate, String operatorId, Long rank) {
		this.groupName = groupName;
		this.servicekey = servicekey;
		this.createDate = createDate;
		this.operatorId = operatorId;
		this.rank = rank;
	}

	/** full constructor */
	public AbstractTyadminGroups(String groupName, String description,
			String servicekey, Date createDate, String operatorId, Long rank /*, String provincecity,String company,String businessdepartment, 
			String warzone,String area,String branchactiongroup*/ ) {
		this.groupName = groupName;
		this.description = description;
		this.servicekey = servicekey;
		this.createDate = createDate;
		this.operatorId = operatorId;
		this.rank = rank;
	/*	this.provincecity = provincecity;
		this.company = company;
		this.businessdepartment = businessdepartment;
		this.warzone = warzone;
		this.area = area;
		this.branchactiongroup = branchactiongroup;*/
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getServicekey() {
		return this.servicekey;
	}

	public void setServicekey(String servicekey) {
		this.servicekey = servicekey;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getOperatorId() {
		return this.operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public Long getRank() {
		return this.rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

/*	public String getProvincecity()
	{
		return this.provincecity;
	}
	
	public void setProvincecity(String provincecity)
	{
		this.provincecity = provincecity;
	}
	
	public String getCompany()
	{
		return this.company;
	}
	
	public void setCompany(String company)
	{
		this.company = company;
	}
	
	public String getBusinessdepartment()
	{
		return this.businessdepartment;
	}
	
	public void setBusinessdepartment(String businessdepartment)
	{
		this.businessdepartment = businessdepartment;
	}
	
	public String getWarzone()
	{
		return this.warzone;
	}
	
	public void setWarzone(String warzone)
	{
		this.warzone = warzone;
	}
	
	public String getArea()
	{
		return this.area;
	}
	
	public void setArea(String area)
	{
		this.area = area;
	}
	
	public String getBranchactiongroup()
	{
		return this.branchactiongroup;
	}
	
	public void setBranchactiongroup(String branchactiongroup)
	{
		this.branchactiongroup = branchactiongroup;
	}*/
}
