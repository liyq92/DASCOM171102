package com.dascom.OPadmin.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dascom.OPadmin.service.IGroupService;
import com.dascom.OPadmin.service.impl.GroupServImpl;

/**
 * TyadminOperators entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class TyadminOperators extends AbstractTyadminOperators implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IGroupService groupServ = new GroupServImpl();
	private transient String loginName = "";
	
	// Constructors

	/** default constructor */
	public TyadminOperators() {
	}

	/** minimal constructor */
	public TyadminOperators(TyadminOperatorsId id, String opepwd, Long groupId,
			String createUser, Date createTime) {
		super(id, opepwd, groupId, createUser, createTime);
	}

	/** full constructor */
	public TyadminOperators(TyadminOperatorsId id, String opepwd, Long groupId,
			String createUser, Date createTime, String agentInfo, String note,
			String provincecity,String company, String businessdepartment, String warzone,String area,
			String branchactiongroup,Long levelid
			) {
		super(id, opepwd, groupId, createUser, createTime, agentInfo, note, 
				provincecity, company,businessdepartment, warzone,area,branchactiongroup,levelid
				);
	}
	
//	public TyadminOperators(String provincecity,
//			String company, String businessdepartment, String warzone,String area, String branchactiongroup )
//	{
//		super( provincecity, company,businessdepartment, 
//				warzone,area,branchactiongroup  );
//	}

	/**
	 * @param loginName The loginName to set.
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginName() {
		return loginName;
	}
	public String getGroupname(){
		if(this.getGroupId() == null) return "";
		TyadminGroups group = groupServ.lookForDetail(this.getGroupId());
		return group.getGroupName();
	}
	
	public String getCreate_timebystring(){
		if(this.getCreateTime() == null) return "";
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getCreateTime());
	}
}
