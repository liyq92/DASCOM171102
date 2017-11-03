package com.dascom.ssmn.entity;

import java.util.Date;

/**
 * Blackcallnumber entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Blackcallnumber implements java.io.Serializable {

	// Fields

	private String blackNumber;
	private String description;
	private Long reserve1;
	private String reserve2;
	private Date intime;
	private int orderNum;//序号

	// Constructors

	/** default constructor */
	public Blackcallnumber() {
	}

	/** minimal constructor */
	public Blackcallnumber(String blackNumber) {
		this.blackNumber = blackNumber;
	}

	/** full constructor */
	public Blackcallnumber(String blackNumber, String description,
			long reserve1, String reserve2) {
		this.blackNumber = blackNumber;
		this.description = description;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
	}
	
	public Blackcallnumber(int orderNum, String blackNumber, Date intime) {
		this.orderNum = orderNum;
		this.blackNumber = blackNumber;
		this.intime = intime;
	}

	// Property accessors

	public String getBlackNumber() {
		return this.blackNumber;
	}

	public void setBlackNumber(String blackNumber) {
		this.blackNumber = blackNumber;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getReserve1() {
		return this.reserve1;
	}

	public void setReserve1(Long reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return this.reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
	
	public Date getIntime(){
		return this.intime;
	}
	
	public void setIntime(Date intime){
		this.intime =intime;
	}
	
	public int getOrderNum(){
		return this.orderNum;
	}
	
	public void setOrderNum(int orderNum){
		this.orderNum =orderNum;
	}
}