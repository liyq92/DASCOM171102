package com.dascom.OPadmin.dbadapter;

import org.hibernate.Session;


public interface IHibernateAdapter {
    public Session openSession();
}
