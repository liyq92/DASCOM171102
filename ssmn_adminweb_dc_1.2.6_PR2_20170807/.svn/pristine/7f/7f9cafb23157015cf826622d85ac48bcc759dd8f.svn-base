package com.dascom.OPadmin.util;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil
{
	private static java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
	private static java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
	
	private static TimeUtil timeUtil = null;
	
	/**
	 * Get static instance
	 * @return TimeUtil
	 */
	public static TimeUtil getInstance()
	{
		if(timeUtil == null)
			timeUtil = new TimeUtil();
		return timeUtil;
	}
	
	/**
	 * get GMT Date
	 * @return Date
	 */
	public Date getGMTDate()
    {
        Calendar calendar = Calendar.getInstance();
        int offset = calendar.get(Calendar.ZONE_OFFSET)/3600000 +
                calendar.get(Calendar.DST_OFFSET)/3600000;
        calendar.add(Calendar.HOUR,-offset);
        return calendar.getTime();
    }
	
	
	public String getGMTDateTimeStr()
	{
	    return this.formatDateAsYMDHMSString(this.getGMTDate());
	}
	
	public String getGMTDateStr()
	{
		return this.formatDateAsYMDString(this.getGMTDate());
	}
	
	/**
	 * Local Date
	 * @return Date
	 */
	public Date getLocalDate()
	{
		return new Date();
	}
	
	public String getLocalDateTimeStr()
	{
		return this.formatDateAsYMDHMSString(this.getLocalDate());
	}
	
	public String getLocalDateStr()
	{
		return this.formatDateAsYMDString(this.getLocalDate());
	}
	
	/**
	 * Zone offset on this Server
	 * @return int
	 */
	public int getServerZoneOffset()
    {
        Calendar calendar = Calendar.getInstance();
        int offset = calendar.get(Calendar.ZONE_OFFSET) / 1000;
        return offset;
    }
	
	/**
	 * Date Format
	 * @param date
	 * @return String
	 */
	private String formatDateAsYMDString(Date date)
	{
		String datestr = "";
    	datestr = df.format(date);
    	return datestr ;
	}
	
	public String formatDateAsYMDHMSString(Date date)
	{
		String datestr = "";
    	datestr = df2.format(date);
    	return datestr ;
	}
}
