package com.dascom.OPadmin.entity;

/**
 * TyadminAuthorities entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class TyadminAuthorities extends AbstractTyadminAuthorities implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** default constructor */
	public TyadminAuthorities() {
	}

	/** full constructor */
	public TyadminAuthorities(String authority, String authMethod,
			String servicekey, Long rank) {
		super(authority, authMethod, servicekey, rank);
	}

}
