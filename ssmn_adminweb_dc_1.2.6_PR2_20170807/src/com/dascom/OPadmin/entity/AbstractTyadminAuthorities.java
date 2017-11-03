package com.dascom.OPadmin.entity;

/**
 * AbstractTyadminAuthorities entity provides the base persistence definition of
 * the TyadminAuthorities entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractTyadminAuthorities implements
		java.io.Serializable {

	// Fields

	private Long id;
	private String authority;
	private String authMethod;
	private String servicekey;
	private Long rank;
	private Long parentId;
	private Long orderId;
	private Long sectionId;

	// Constructors

	/** default constructor */
	public AbstractTyadminAuthorities() {
	}

	/** full constructor */
	public AbstractTyadminAuthorities(String authority, String authMethod,
			String servicekey, Long rank) {
		this.authority = authority;
		this.authMethod = authMethod;
		this.servicekey = servicekey;
		this.rank = rank;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getAuthMethod() {
		return this.authMethod;
	}

	public void setAuthMethod(String authMethod) {
		this.authMethod = authMethod;
	}

	public String getServicekey() {
		return this.servicekey;
	}

	public void setServicekey(String servicekey) {
		this.servicekey = servicekey;
	}

	public Long getRank() {
		return this.rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}
	
	public Long getParentId()
	{
		return this.parentId;
	}
	
	public void setParentId(Long parentId)
	{
		this.parentId =parentId;
	}
	
	public Long getOrderId()
	{
		return this.orderId;
	}
	
	public void setOrderId(Long orderId)
	{
		this.orderId =orderId;
	}
	
	public Long getSectionId()
	{
		return this.sectionId;
	}
	
	public void setSectionId(Long sectionId)
	{
		this.sectionId =sectionId;
	}

}
