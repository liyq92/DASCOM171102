package com.dascom.ssmn.entity;

import java.util.Date;

/**
 * SsmnZyChannel entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SsmnZyChannel implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private Date createtime;
	private Long type;

	// Constructors

	/** default constructor */
	public SsmnZyChannel() {
	}

	/** full constructor */
	public SsmnZyChannel(String name, Date createtime) {
		this.name = name;
		this.createtime = createtime;
	}

	public SsmnZyChannel(Long id,String name) {
		this.id = id;
		this.name = name;
	}
	
	// Property accessors

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

	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	public Long getType() {
		return this.type;
	}

	public void setType(Long type) {
		this.type = type;
	}

}