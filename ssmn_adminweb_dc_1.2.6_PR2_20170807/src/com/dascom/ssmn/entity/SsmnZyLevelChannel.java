package com.dascom.ssmn.entity;

/**
 * SsmnZyLevelChannel entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SsmnZyLevelChannel implements java.io.Serializable {

	// Fields

	private long id;
	private long channelid;
	private long levelid;

	// Constructors

	/** default constructor */
	public SsmnZyLevelChannel() {
	}

	/** minimal constructor */
	public SsmnZyLevelChannel(long id) {
		this.id = id;
	}

	/** full constructor */
	public SsmnZyLevelChannel(long id, long channelid, long levelid) {
		this.id = id;
		this.channelid = channelid;
		this.levelid = levelid;
	}

	// Property accessors

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getChannelid() {
		return this.channelid;
	}

	public void setChannelid(long channelid) {
		this.channelid = channelid;
	}

	public long getLevelid() {
		return this.levelid;
	}

	public void setLevelid(long levelid) {
		this.levelid = levelid;
	}

}