package com.dascom.OPadmin.entity;

import java.util.Date;

/**
 * AbstractTyadminOperators entity provides the base persistence definition of
 * the TyadminOperators entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractTyadminOperators implements java.io.Serializable {

	// Fields

	private TyadminOperatorsId id;
	private String opepwd;
	private Long groupId;
	private String createUser;
	private Date createTime;
	private String agentInfo;
	private String note;
	private String provincecity;
	private String company;
	private String businessdepartment;
	private String warzone;
	private String area;
	private String branchactiongroup;
	private Long levelid;
	//private Long type;

	// Constructors

	/** default constructor */
	public AbstractTyadminOperators() {
	}

	/** minimal constructor */
	public AbstractTyadminOperators(TyadminOperatorsId id, String opepwd,
			Long groupId, String createUser, Date createTime) {
		this.id = id;
		this.opepwd = opepwd;
		this.groupId = groupId;
		this.createUser = createUser;
		this.createTime = createTime;
	}

	/** full constructor */
	public AbstractTyadminOperators(TyadminOperatorsId id, String opepwd,
			Long groupId, String createUser, Date createTime, String agentInfo,	String note
			
			, String provincecity,String company,String businessdepartment, 
			String warzone,String area,String branchactiongroup  , Long levelid
			) {
		this.id = id;
		this.opepwd = opepwd;
		this.groupId = groupId;
		this.createUser = createUser;
		this.createTime = createTime;
		this.agentInfo = agentInfo;
		this.note = note;
		this.levelid = levelid;
		this.provincecity = provincecity;
		this.company = company;
		this.businessdepartment = businessdepartment;
		this.warzone = warzone;
		this.area = area;
		this.branchactiongroup = branchactiongroup;
		//this.type = type;
	}
	
	public AbstractTyadminOperators( String opepwd,
			Long groupId, String createUser, Date createTime, String agentInfo,	String note
			, String provincecity,String company,String businessdepartment, 
			String warzone,String area,String branchactiongroup  , Long levelid
			) {
		this.opepwd = opepwd;
		this.groupId = groupId;
		this.createUser = createUser;
		this.createTime = createTime;
		this.agentInfo = agentInfo;
		this.note = note;
		this.levelid = levelid;
		this.provincecity = provincecity;
		this.company = company;
		this.businessdepartment = businessdepartment;
		this.warzone = warzone;
		this.area = area;
		this.branchactiongroup = branchactiongroup;
	}
	
//	public AbstractTyadminOperators(
//			String provincecity,String company,String businessdepartment, 
//			String warzone,String area,String branchactiongroup  ) {
//		this.provincecity = provincecity;
//		this.company = company;
//		this.businessdepartment = businessdepartment;
//		this.warzone = warzone;
//		this.area = area;
//		this.branchactiongroup = branchactiongroup;
//	}

	// Property accessors

	public TyadminOperatorsId getId() {
		return this.id;
	}

	public void setId(TyadminOperatorsId id) {
		this.id = id;
	}

	public String getOpepwd() {
		return this.opepwd;
	}

	public void setOpepwd(String opepwd) {
		this.opepwd = opepwd;
	}

	public Long getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAgentInfo() {
		return this.agentInfo;
	}

	public void setAgentInfo(String agentInfo) {
		this.agentInfo = agentInfo;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}


	public Long getLevelid() {
		return levelid;
	}

	public void setLevelid(Long levelid) {
		this.levelid = levelid;
	}
	
	public String getProvincecity() {
		return provincecity;
	}

	public void setProvincecity(String provincecity) {
		this.provincecity = provincecity;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBusinessdepartment() {
		return businessdepartment;
	}

	public void setBusinessdepartment(String businessdepartment) {
		this.businessdepartment = businessdepartment;
	}

	public String getWarzone() {
		return warzone;
	}

	public void setWarzone(String warzone) {
		this.warzone = warzone;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getBranchactiongroup() {
		return branchactiongroup;
	}

	public void setBranchactiongroup(String branchactiongroup) {
		this.branchactiongroup = branchactiongroup;
	}
}
