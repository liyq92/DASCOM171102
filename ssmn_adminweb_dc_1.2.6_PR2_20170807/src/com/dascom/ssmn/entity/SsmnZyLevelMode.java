package com.dascom.ssmn.entity;

/**
 * SsmnZyLevelMode entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SsmnZyLevelMode implements java.io.Serializable {

	// Fields

	private long id;
	private long modelow;
	private long levelid;

	// Constructors

	/** default constructor */
	public SsmnZyLevelMode() {
	}

	/** minimal constructor */
	public SsmnZyLevelMode(long id) {
		this.id = id;
	}

	/** full constructor */
	public SsmnZyLevelMode(long id, long modelow, long levelid) {
		this.id = id;
		this.modelow = modelow;
		this.levelid = levelid;
	}

	// Property accessors

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getModelow() {
		return this.modelow;
	}

	public void setModelow(long modelow) {
		this.modelow = modelow;
	}

	public long getLevelid() {
		return this.levelid;
	}

	public void setLevelid(long levelid) {
		this.levelid = levelid;
	}

}