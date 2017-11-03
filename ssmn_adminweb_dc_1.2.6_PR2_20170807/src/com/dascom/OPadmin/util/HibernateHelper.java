package com.dascom.OPadmin.util;
/**
* Hibernate助手�?
* */
/**
* Hibernate Helper
* */
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;



public class HibernateHelper {
  private static HibernateHelper helper = null;
  private static SessionFactory factory = null;

  //private static final ThreadLocal _session = new ThreadLocal();

  private static Logger logger = Logger.getLogger("framework.dao.HibernateHelper");
  static {
    helper = new HibernateHelper();
  }
  private HibernateHelper() {
    try {
      factory = new Configuration()
          .configure()
          .buildSessionFactory();
    } catch(HibernateException he) {
    	he.printStackTrace();
      logger.severe(he.getMessage());
      factory = null;
    }
  }

  /**
    * 得到session
    * @return Session  若调用失败，则返回null
    * */
   public static Session currentSession() {
     if(factory==null) {
       return null;
     } else {
       Session session = null;
       try {
         session = factory.openSession();
       } catch(HibernateException he) {
         logger.severe(he.getMessage());
       }
       return session;
     }
   }
}
