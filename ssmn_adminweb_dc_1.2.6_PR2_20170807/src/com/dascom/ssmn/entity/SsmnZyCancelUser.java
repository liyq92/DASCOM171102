package com.dascom.ssmn.entity;

import java.util.Date;

/**
 * SsmnZyCancelUser entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SsmnZyCancelUser implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6697199542595805388L;
	private Long userid;
	private String name;
	private String msisdn;
	private String ssmnnumber;
	private String groupname;
	private Long channelid;
	private String channelname;
	private Date subDate;
	private Date cancelDate;
	private Long levelid;
	private String empno;
	private Long manner;
	private Long operaType;
	private Long modeid;
	private String remark;//备注
	private Long ssmnnumbertype;//副号码类型，呼叫用
	private String secondMsisdn;//第二联系人
	
	// Constructors

	public Long getLevelid() {
		return levelid;
	}

	public void setLevelid(Long levelid) {
		this.levelid = levelid;
	}

	/** default constructor */
	public SsmnZyCancelUser() {
	}

	/** full constructor */
	public SsmnZyCancelUser(String name, String msisdn, String ssmnnumber,
			String groupname, Long channelid, Date subDate, Date cancelDate,Long levelid
			) {
		this.name = name;
		this.msisdn = msisdn;
		this.ssmnnumber = ssmnnumber;
		this.groupname = groupname;
		this.channelid = channelid;
		this.subDate = subDate;
		this.cancelDate = cancelDate;
		this.levelid = levelid;
	}
	
	public SsmnZyCancelUser(Long userid,String name,String msisdn,String ssmnnumber,
			String groupname,Date subDate,Date cancelDate,String channelname,String empno,
			String secondMsisdn)
	{
		this.userid = userid;
		this.name = name;
		this.msisdn = msisdn;
		this.ssmnnumber = ssmnnumber;
		this.groupname = groupname;
		this.subDate = subDate;
		this.cancelDate = cancelDate;
		this.channelname = channelname;
		this.empno = empno;
		this.secondMsisdn = secondMsisdn;
	}

	// Property accessors

	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
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

	public Date getSubDate() {
		return this.subDate;
	}

	public void setSubDate(Date subDate) {
		this.subDate = subDate;
	}

	public Date getCancelDate() {
		return this.cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getChannelname()
	{
		return this.channelname;
	}
	
	public void setChannelname(String channelname)
	{
		this.channelname = channelname;
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

	public Long getOperaType()
	{
		return this.operaType;
	}
	
	public void setOperaType(Long operaType)
	{
		this.operaType = operaType;
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
}