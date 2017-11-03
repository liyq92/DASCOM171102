package com.dascom.ssmn.entity;

import java.util.Date;

/**
 * SsmnZyUser entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SsmnZyUser implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4555788072080474188L;
	private Long id;
	private String name;
	private String msisdn;
	private String ssmnnumber;
	private String groupname;
	private Long channelid;
	private Date intime;
	private String channelname;
	/**-----查询需要-------*/
	private String provincecity;
	private String company;
	private String businessdepartment;
	private String warzone;
	private String area;
	private String branchactiongroup;
	/**------------*/
	private Long levelid;
	private String empno;
	private Long manner;
	private Long modeid;//区分区域放音 对应 ssmn_zy_level_mode表(只有天津有)
	private String remark;//备注
	private Long ssmnnumbertype;//副号码类型，呼叫用
	private String secondMsisdn;//第二联系人
	private Long channeltype;//渠道类型

	// Constructors

	/** default constructor */
	public SsmnZyUser() {
	}

	/** full constructor */
	public SsmnZyUser(String name, String msisdn, String ssmnnumber,
			String groupname, Long channelid, Date intime
//			, String provincecity,String company,String businessdepartment, 
//			String warzone,String area,String branchactiongroup
			) {
		this.name = name;
		this.msisdn = msisdn;
		this.ssmnnumber = ssmnnumber;
		this.groupname = groupname;
		this.channelid = channelid;
		this.intime = intime;
	}

	// Property accessors

	public SsmnZyUser(Long id, String name, String msisdn, String ssmnnumber,
			String groupname,Date intime, String channelname, Long levelid) {
		super();
		this.id = id;
		this.name = name;
		this.msisdn = msisdn;
		this.ssmnnumber = ssmnnumber;
		this.groupname = groupname;
		this.intime = intime;
		this.channelname = channelname;
		this.levelid = levelid;
	}
	public SsmnZyUser(Long id, String name, String msisdn, String ssmnnumber,
			String groupname,Date intime, String channelname,String empno,
			String remark,String secondMsisdn,Long channeltype) {
		super();
		this.id = id;
		this.name = name;
		this.msisdn = msisdn;
		this.ssmnnumber = ssmnnumber;
		this.groupname = groupname;
		this.intime = intime;
		this.channelname = channelname;
		this.empno = empno;
		this.remark = remark;
		this.secondMsisdn = secondMsisdn;
		this.channeltype = channeltype;
	}
	public SsmnZyUser(String msisdn, String name) {
		super();
		this.name = name;
		this.msisdn = msisdn;
	}
	
	public SsmnZyUser(String businessdepartment, 
			String warzone,String area,String branchactiongroup,String name, String msisdn, String ssmnnumber,
			String channelname,String empno,String remark, String secondMsisdn) {
		super();
		this.businessdepartment = businessdepartment;
		this.warzone = warzone;
		this.area = area;
		this.branchactiongroup = branchactiongroup;
		this.name = name;
		this.msisdn = msisdn;
		this.ssmnnumber = ssmnnumber;
		this.channelname = channelname;
		this.empno=empno;
		this.remark = remark;
		this.secondMsisdn = secondMsisdn;
	}
	

	public SsmnZyUser(long id, 
			String name,String msisdn,String ssmnnumber,String empno, String channelname, String provincecity,
			String company,String businessdepartment,String warzone,String area,String branchactiongroup,
			Long levelid,Long channelid) {
		super();
		this.id = id;
		this.name = name;
		this.msisdn = msisdn;
		this.ssmnnumber = ssmnnumber;
		this.empno=empno;
		this.channelname = channelname;
		this.provincecity = provincecity;
		this.company = company;
		this.businessdepartment = businessdepartment;
		this.warzone = warzone;
		this.area = area;
		this.branchactiongroup = branchactiongroup;
		this.levelid = levelid;
		this.channelid = channelid;
			
	}
	
	public SsmnZyUser(Long id, String name, String msisdn, String empno,
			Long levelid) {
		super();
		this.id = id;
		this.name = name;
		this.msisdn = msisdn;
		this.empno = empno;
		this.levelid =levelid;
	}
	
	public SsmnZyUser(String ssmnnumber) {
		super();
		this.ssmnnumber = ssmnnumber;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMsisdn() {
		return this.msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getSsmnnumber() {
		return this.ssmnnumber;
	}

	public void setSsmnnumber(String ssmnnumber) {
		this.ssmnnumber = ssmnnumber;
	}

	public String getGroupname() {
		return this.groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public Long getChannelid() {
		return this.channelid;
	}

	public void setChannelid(Long channelid) {
		this.channelid = channelid;
	}

	public Date getIntime() {
		return this.intime;
	}

	public void setIntime(Date intime) {
		this.intime = intime;
	}

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
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
	
	public String getEmpno()
	{
		return this.empno;
	}
	
	public void setEmpno(String empno)
	{
		this.empno = empno;
	}
	
	public Long getManner()
	{
		return this.manner;
	}
	
	public void setManner(Long manner)
	{
		this.manner = manner;
	}
	
	public Long getModeid()
	{
		return this.modeid;
	}
	
	public void setModeid(Long modeid)
	{
		this.modeid = modeid;
	}
	
	public String getRemark()
	{
		return this.remark;
	}
	
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	
	public Long getSsmnnumbertype()
	{
		return this.ssmnnumbertype;
	}
	
	public void setSsmnnumbertype(Long ssmnnumbertype)
	{
		this.ssmnnumbertype = ssmnnumbertype;
	}
	
	public String getSecondMsisdn()
	{
		return this.secondMsisdn;
	}
	
	public void setSecondMsisdn(String secondMsisdn)
	{
		this.secondMsisdn = secondMsisdn;
	}
	
	public Long getChanneltype() {
		return this.channeltype;
	}

	public void setChanneltype(Long channeltype) {
		this.channeltype = channeltype;
	}
}