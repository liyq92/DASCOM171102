package com.dascom.OPadmin.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dascom.OPadmin.dao.IAdminOperator;
import com.dascom.OPadmin.dao.impl.AdminOperatorImpl;

/**
 * TyadminGroups entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class TyadminGroups extends AbstractTyadminGroups implements
		java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IAdminOperator operDao = new AdminOperatorImpl();
	// Constructors

	/** default constructor */
	public TyadminGroups() {
	}

	/** minimal constructor */
	public TyadminGroups(String groupName, String servicekey, Date createDate,
			String operatorId, Long rank) {
		super(groupName, servicekey, createDate, operatorId, rank);
	}

	/** full constructor */
	public TyadminGroups(String groupName, String description,
			String servicekey, Date createDate, String operatorId, Long rank /*,String provincecity,
			String company, String businessdepartment, String warzone,String area, String branchactiongroup*/) {
		super(groupName, description, servicekey, createDate, operatorId, rank /*, provincecity, company,businessdepartment, 
				warzone,area,branchactiongroup*/);
	}
	public String getCreateDateByString(){
    	if(this.getCreateDate() == null) return "";
    	return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getCreateDate());
    }
	
	
	public int getTotalOperators(){
		if(this.getOperatorId() == null) return -1;
		int count = operDao.countOperatorByGroup(this.getId());
		return count;
	}
}
