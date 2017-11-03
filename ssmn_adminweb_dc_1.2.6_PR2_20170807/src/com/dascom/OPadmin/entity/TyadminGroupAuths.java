package com.dascom.OPadmin.entity;

/**
 * TyadminGroupAuths entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class TyadminGroupAuths extends AbstractTyadminGroupAuths implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** default constructor */
	public TyadminGroupAuths() {
	}

	/** full constructor */
	public TyadminGroupAuths(Long groupId, Long authId) {
		super(groupId, authId);
	}

}
