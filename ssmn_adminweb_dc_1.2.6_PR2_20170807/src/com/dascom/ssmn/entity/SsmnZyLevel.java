package com.dascom.ssmn.entity;

public class SsmnZyLevel implements java.io.Serializable{

 
	private static final long serialVersionUID = -4910393204994007491L;
	private Long id;
	private String provincecity;
	private String company;
	private String businessdepartment;
	private String warzone;
	private String area;
	private String branchactiongroup;
	
	public SsmnZyLevel() {
	 
	}

	public SsmnZyLevel( String provincecity, String company,
			String businessdepartment, String warzone, String area,
			String branchactiongroup,Long id) {
		this.id = id;
		this.provincecity = provincecity;
		this.company = company;
		this.businessdepartment = businessdepartment;
		this.warzone = warzone;
		this.area = area;
		this.branchactiongroup = branchactiongroup;
	}
	

	public SsmnZyLevel(Long id,String provincecity, String company,
			String businessdepartment, String warzone, String area,
			String branchactiongroup) {
		this.id = id;
		this.provincecity = provincecity;
		this.company = company;
		this.businessdepartment = businessdepartment;
		this.warzone = warzone;
		this.area = area;
		this.branchactiongroup = branchactiongroup;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
