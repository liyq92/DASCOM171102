package com.dascom.ssmn.entity;

public class ZydcRecord {

	private String yesterday;
	private String groupname;
	private String username;
	private String msisdn;
	private String ssmnnumber;
	private String channelname;
	private String calltype;
	private String callstarttime;
	private String callstoptime;
	private Integer callduration;
	private String strCallDuration;
	private String clientnumber;
	private String endreason;
	private String rcvmsgtime;
	private String msgcontent;
	private Long streamNumber; //CDR表的流水号
	private String filepath;//录音文件路径
	private String fileName;//录音文件名
	private String filepathOnline;//在线收听的文件路径
	private String provincecity;
	private String company;
	private String businessdepartment;
	private String warzone;
	private String area;
	private String branchactiongroup;
	private int channelCount;//按渠道统计的数量
	private String empno;
	private int isRead;//是否听过录音
	private Long dccalltype;//判断呼叫类型,比如 PC呼出，
	private int channelType;//渠道类型
	private String isblacknum;//是否是骚扰电话
	private String firstMsisdn;//第一联系人
	private String remark;//添加备注
	private int remarkCount;//添加备注的次数
	
	
	public ZydcRecord() {
		super();
	}
	
	/**
	 * 统计通话记录
	 * @param yesterday
	 * @param groupname
	 * @param username
	 * @param msisdn
	 * @param ssmnnumber
	 * @param channelname
	 * @param calltype
	 * @param callstarttime
	 * @param callstoptime
	 * @param callduration
	 * @param clientnumber
	 * @param endreason
	 */
	public ZydcRecord(String yesterday, String groupname, String username,
			String msisdn, String ssmnnumber, String channelname,
			String calltype, String callstarttime, String callstoptime,
			Integer callduration, String clientnumber,String endreason) {
		super();
		this.yesterday = yesterday;
		this.groupname = groupname;
		this.username = username;
		this.msisdn = msisdn;
		this.ssmnnumber = ssmnnumber;
		this.channelname = channelname;
		this.calltype = calltype;
		this.callstarttime = callstarttime;
		this.callstoptime = callstoptime;
		this.callduration = callduration;
		this.clientnumber = clientnumber;
		this.endreason = endreason;
	}
	
	public ZydcRecord( Long streamNumber) {
		super();
		this.streamNumber =streamNumber;
	}
	
	public ZydcRecord(Long streamNumber,String yesterday, String groupname, String username,
			String msisdn, String ssmnnumber, String channelname,
			String calltype, String callstarttime, String callstoptime,
			Integer callduration, String clientnumber,String endreason,String provincecity,String company,String businessdepartment
			,String warzone,String area, String branchactiongroup ,String fileName,String empno,Long dccalltype) {
		super();
		this.streamNumber = streamNumber;
		this.yesterday = yesterday;
		this.groupname = groupname;
		this.username = username;
		this.msisdn = msisdn;
		this.ssmnnumber = ssmnnumber;
		this.channelname = channelname;
		this.calltype = calltype;
		this.callstarttime = callstarttime;
		this.callstoptime = callstoptime;
		this.callduration = callduration;
		this.clientnumber = clientnumber;
		this.endreason = endreason;
		this.provincecity = provincecity;
		this.company = company;
		this.businessdepartment = businessdepartment;
		this.warzone = warzone;
		this.area = area;
		this.branchactiongroup = branchactiongroup;
		this.fileName = fileName;
		this.empno = empno;
		this.dccalltype = dccalltype;
	}
	
	public ZydcRecord(Long streamNumber,  String username, String groupname,
			String msisdn, String ssmnnumber,String clientnumber, String channelname,
			String calltype, String callstarttime, Integer callduration, String filepath ,
			String businessdepartment,String warzone,String area,String branchactiongroup ,String filename,String empno
			,Long dccalltype,int channelType
			) {
		super();
		this.streamNumber = streamNumber;
		this.username = username;
		this.groupname = groupname;
		this.msisdn = msisdn;
		this.ssmnnumber = ssmnnumber;
		this.clientnumber = clientnumber;
		this.channelname = channelname;
		this.calltype = calltype;
		this.callstarttime = callstarttime;
		this.callduration = callduration;
		this.filepath = filepath;
		this.businessdepartment = businessdepartment;
		this.warzone = warzone;
		this.area = area;
		this.branchactiongroup = branchactiongroup;
		this.fileName = filename;
		this.empno = empno;
		this.dccalltype = dccalltype;
		this.channelType= channelType;
	}
	
	
	
	/**
	 * 统计短信记录
	 * @param yesterday
	 * @param groupname
	 * @param username
	 * @param msisdn
	 * @param ssmnnumber
	 * @param channelname
	 * @param clientnumber
	 * @param rcvmsgtime
	 * @param msgcontent
	 */
	public ZydcRecord(Long streamNumber,String yesterday, String groupname, String username,
			String msisdn, String ssmnnumber, String channelname,
			String clientnumber, String rcvmsgtime, String msgcontent,String businessdepartment
			,String warzone,String area, String branchactiongroup,String empno) {
		super();
		this.streamNumber = streamNumber;
		this.yesterday = yesterday;
		this.groupname = groupname;
		this.username = username;
		this.msisdn = msisdn;
		this.ssmnnumber = ssmnnumber;
		this.channelname = channelname;
		this.clientnumber = clientnumber;
		this.rcvmsgtime = rcvmsgtime;
		this.msgcontent = msgcontent;
		this.businessdepartment = businessdepartment;
		this.warzone = warzone;
		this.area = area;
		this.branchactiongroup = branchactiongroup;
		this.empno = empno;
	}
	
	public ZydcRecord(String clientnumber,Long dccalltype,String callstarttime
			,String fileName) {
		super();
		this.clientnumber = clientnumber;
		this.dccalltype = dccalltype;
		this.callstarttime = callstarttime;
		this.fileName = fileName;
	}
	
	public ZydcRecord(Long streamNumber, String username,String empno,String branchactiongroup,
			String msisdn, String ssmnnumber, String channelname
			,Integer callduration,String provincecity,String company, String businessdepartment
			,String warzone,String area,String clientnumber,Long dccalltype,String callstarttime
			,String fileName, String calltype,String strCallDuration,String isblacknum,
			int channelType,String firstMsisdn,String remark) {
		super();
		this.streamNumber = streamNumber;
		this.username = username;
		this.empno = empno;
		this.msisdn = msisdn;
		this.ssmnnumber = ssmnnumber;
		this.channelname = channelname;
		this.provincecity = provincecity;
		this.company = company;
		this.businessdepartment = businessdepartment;
		this.warzone = warzone;
		this.area = area;
		this.branchactiongroup = branchactiongroup;
		this.callduration = callduration;
		this.clientnumber =clientnumber;
		this.dccalltype = dccalltype;
		this.callstarttime = callstarttime;
		this.fileName = fileName;
		this.calltype = calltype;
		this.strCallDuration = strCallDuration;
		this.isblacknum = isblacknum;
		this.channelType =channelType;
		this.firstMsisdn = firstMsisdn;
		this.remark =remark;
	}
	
	public ZydcRecord(String username,String empno,String branchactiongroup, String msisdn, String ssmnnumber,
			String clientnumber,String channelname,Long dccalltype,String callstarttime,Integer callduration,
			String filepath,String fileName,int channelType,String firstMsisdn,String remark)
	{
		this.username = username;
		this.empno = empno;
		this.branchactiongroup = branchactiongroup;
		this.msisdn = msisdn;
		this.ssmnnumber = ssmnnumber;
		this.clientnumber = clientnumber;
		this.channelname = channelname;
		this.dccalltype = dccalltype;
		this.callstarttime = callstarttime;
		this.callduration = callduration;
		this.filepath = filepath;
		this.fileName = fileName;
		this.channelType = channelType;
		this.firstMsisdn = firstMsisdn;
		this.remark =remark;
	}
	
	public ZydcRecord(String channelname, int channelCount){
		super();
		this.channelname = channelname;
		this.channelCount = channelCount;
	}
	public ZydcRecord(long  dccalltype, int channelCount){
		super();
		this.dccalltype = dccalltype;
		this.channelCount = channelCount;
	}
	
	public ZydcRecord(String msisdn,String username, String ssmnnumber, String channelname )
	{
		super();
		this.msisdn = msisdn;
		this.username = username;
		this.ssmnnumber = ssmnnumber;
		this.channelname = channelname;
	}
	
	public String getRcvmsgtime() {
		return rcvmsgtime;
	}
	public void setRcvmsgtime(String rcvmsgtime) {
		this.rcvmsgtime = rcvmsgtime;
	}
	public String getMsgcontent() {
		return msgcontent;
	}
	public void setMsgcontent(String msgcontent) {
		this.msgcontent = msgcontent;
	}
	public String getEndreason() {
		return endreason;
	}
	public void setEndreason(String endreason) {
		this.endreason = endreason;
	}
	public String getYesterday() {
		return yesterday;
	}
	public void setYesterday(String yesterday) {
		this.yesterday = yesterday;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getSsmnnumber() {
		return ssmnnumber;
	}
	public void setSsmnnumber(String ssmnnumber) {
		this.ssmnnumber = ssmnnumber;
	}
	public String getChannelname() {
		return channelname;
	}
	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}
	public String getCalltype() {
		return calltype;
	}
	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}
	public String getCallstarttime() {
		return callstarttime;
	}
	public void setCallstarttime(String callstarttime) {
		this.callstarttime = callstarttime;
	}
	public String getCallstoptime() {
		return callstoptime;
	}
	public void setCallstoptime(String callstoptime) {
		this.callstoptime = callstoptime;
	}
	public Integer getCallduration() {
		return callduration;
	}
	public void setCallduration(Integer callduration) {
		this.callduration = callduration;
	}
	public String getClientnumber() {
		return clientnumber;
	}
	public void setClientnumber(String clientnumber) {
		this.clientnumber = clientnumber;
	}
	
	public Long getStreamNumber()
	{
		return streamNumber;
	}
	public void setStreamNumber(Long streamNumber)
	{
		this.streamNumber = streamNumber;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	public String  getStrCallDuration(){
		return strCallDuration;
	}
	public void setStrCallDuration(String strCallDuration)
	{
		this.strCallDuration = strCallDuration;
	}
	public int getChannelCount()
	{
		return channelCount;
	}
	public void setChannelCount(int channelCount)
	{
		this.channelCount = channelCount;
	}
	public String getFilepathOnline()
	{
		return this.filepathOnline;
	}
	public void setFilepathOnline(String filepathOnline)
	{
		this.filepathOnline = filepathOnline;
	}
	public String getEmpno()
	{
		return this.empno;
	}
	
	public void setEmpno(String empno)
	{
		this.empno = empno;
	}
	
	public int getIsRead()
	{
		return this.isRead;
	}
	public void setIsRead(int isRead)
	{
		this.isRead = isRead;
	}
	public Long getDccalltype()
	{
		return this.dccalltype;
	}
	
	public void setDccalltype(Long dccalltype)
	{
		this.dccalltype = dccalltype;
	}
	
	public int getChannelType()
	{
		return this.channelType;
	}
	
	public void setChannelType(int channelType)
	{
		this.channelType = channelType;
	}
	
	public String getIsblacknum()
	{
		return this.isblacknum;
	}
	
	public void setIsblacknum(String isblacknum)
	{
		this.isblacknum =isblacknum;
	}
	
	public String getFirstMsisdn()
	{
		return this.firstMsisdn;
	}
	
	public void setFirstMsisdn(String firstMsisdn)
	{
		this.firstMsisdn = firstMsisdn;
	}
	
	public String getRemark()
	{
		return this.remark;
	}
	
	public void setRemark(String remark)
	{
		this.remark =remark;
	}
	
	public int getRemarkCount()
	{
		return this.remarkCount;
	}
	
	public void setRemarkCount(int remarkCount)
	{
		this.remarkCount =remarkCount;
	}
 
}
