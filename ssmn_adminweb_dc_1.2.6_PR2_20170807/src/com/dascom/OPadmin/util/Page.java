package com.dascom.OPadmin.util;

/**
 * 分页对象
 * */
public class Page {
  public Page() {
  }

  /**
   * 设置每页显示的行�?
   * */
  public void setSize(int size) {
    this.size = size;
  }
  /**
   * 设置要显示的页数。从1页开�?
   * */
  public void setCurrentindex(int currentindex) {
    this.currentindex = currentindex;
  }
  /**
   * 得到�?始行�?
   * */
  public int getFirstResult() {
    return (this.currentindex - 1)*size + 1;
  }
  /**
   * 得到显示的最大数�?
   * */
  public int getSize() {
    return size;
  }
  private int size;
  private int currentindex;
  
  private int totalcount;
  private int previousindex;
  private int nextindex;
  private int lastindex;
 
  
  /**
   * 得到结束行数
   * */
  public int getEndResult() {
    return this.currentindex * size;
  }
  public int getCurrentindex(){
	  return this.currentindex;
  }
/**
 * @return Returns the totalcount.
 */
public int getTotalcount() {
	return totalcount;
}
/**
 * @param totalcount The totalcount to set.
 */
public void setTotalcount(int totalcount) {
	this.totalcount = totalcount;
}
/**
 * @return Returns the lastindex.
 */
public int getLastindex() {
	lastindex = totalcount / size; 
    if (totalcount % size > 0) lastindex++; 
	return lastindex;
}
/**
 * @return Returns the nextindex.
 */
public int getNextindex() {
	
	if ((currentindex * size) >= totalcount)
	{
		nextindex = currentindex;
	} else
	{
		nextindex = currentindex + 1;
	}

	return nextindex; 
}
/**
 * @return Returns the previousindex.
 */
public int getPreviousindex() {
	
	if (currentindex>2) 
	{
		previousindex = currentindex -1;
	}
	else
	{
		previousindex = 1;
	}
	return previousindex;
}
}
