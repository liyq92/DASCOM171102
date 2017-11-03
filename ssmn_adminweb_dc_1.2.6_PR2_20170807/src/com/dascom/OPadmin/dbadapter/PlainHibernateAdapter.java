package com.dascom.OPadmin.dbadapter;

import org.hibernate.Session;

import com.dascom.OPadmin.util.HibernateHelper;


/**
 * @author RubinRuler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PlainHibernateAdapter implements IHibernateAdapter {

	/* (non-Javadoc)
	 */
	public Session openSession() {
		return HibernateHelper.currentSession();

	}
}
