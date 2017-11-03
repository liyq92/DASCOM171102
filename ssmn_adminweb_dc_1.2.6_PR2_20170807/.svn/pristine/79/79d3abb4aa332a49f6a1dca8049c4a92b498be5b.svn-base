package com.dascom.OPadmin.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static SessionFactory sessionFactory;

	static {
		try {
			Configuration configuration = new Configuration().configure();
			sessionFactory = configuration.buildSessionFactory();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError();
		}
	}

	public static Session getSession() {
		return sessionFactory.openSession();
	}

	public static void closeSession(Session session) {
		try {
			if (session != null)
				session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void closeSessionFactory() {
		if (sessionFactory != null)
			sessionFactory.close();
	}

	public static void rollbackTx(Transaction tx) {
		try {
			if (tx != null)
				tx.rollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
