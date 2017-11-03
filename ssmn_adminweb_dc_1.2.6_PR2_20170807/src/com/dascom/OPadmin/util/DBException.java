package com.dascom.OPadmin.util;

/**
 * 数据库失败的异常
 * */
public class DBException extends RuntimeException {

  /**
   *
   */
  public DBException() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @param message
   */
  public DBException(String message) {
    super(message);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param message
   * @param cause
   */
  public DBException(String message, Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param cause
   */
  public DBException(Throwable cause) {
    super(cause);
    // TODO Auto-generated constructor stub
  }

}
