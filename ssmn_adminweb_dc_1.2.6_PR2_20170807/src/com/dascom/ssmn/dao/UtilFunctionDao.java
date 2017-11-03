package com.dascom.ssmn.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.hibernate.Query;

import com.dascom.OPadmin.entity.TyadminGroups;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.ssmn.entity.Blackcallnumber;
import com.dascom.ssmn.entity.SsmnZYEnableNumberRecord;
import com.dascom.ssmn.entity.SsmnZyChannel;
import com.dascom.ssmn.entity.SsmnZyClientnum;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.ssmn.entity.ZydcRecord;
import com.dascom.OPadmin.entity.TyadminOperatorLogs;
import com.dascom.init.*;
import com.dascom.ssmn.util.Constants;

public class UtilFunctionDao   {
	public static String operlevel = "0";//省市级：1 公司级：2 事业部级:3 战区级:4 片区级:5 分行行动组:6 作用：下拉列表条件选择区域，根据各个级别，将上级置灰（不可用状态）
	private ChannelDao cDao = ChannelDao.getInstance();
	 
/*拼sql:   根据操作员级别筛选的条件　的sql
 * 
 */
	
	public String getTyadminOperatorSelect(SsmnZyLevel opera,String tablename ,String subSql )
	{
		String strHQL = "  ";
		if(subSql !=null && subSql.length()>0)
		{
			strHQL +=" ( ";
			strHQL += subSql;
		}
		//----------------------------------------------------------------------------------------------------
		//操作员级别
		String sProvince = opera.getProvincecity();//省市
		String sCompany = opera.getCompany();//公司
		String sBusinessDep = opera.getBusinessdepartment();//事业部
		String sWarz = opera.getWarzone();//战区
		String sArea = opera.getArea();//片区
		String sBranch = opera.getBranchactiongroup();//分行行动组
		if(sProvince != null && !"".equals(sProvince) && (sCompany == null || "".equals(sCompany)))//省市级
		{
			strHQL +=" and  le.provincecity =:sProvince ";
			strHQL +=" and  le.company is not null and le.businessdepartment is not null  and le.warzone is not null and le.area is not null  and le.branchactiongroup is not null ";
			operlevel = "1";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && ( sBusinessDep == null 
				|| "".equals(sBusinessDep)))//公司级
		{
			strHQL +=" and le.provincecity =:sProvince ";
			strHQL +=" and le.company =:sCompany ";
			strHQL +=" and le.businessdepartment is not null and le.warzone is not null and le.area is not null  and le.branchactiongroup is not null ";
			operlevel = "2";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && (sWarz == null || "".equals(sWarz)))//事业部级
		{
			strHQL +=" and le.provincecity =:sProvince ";
			strHQL +=" and le.company =:sCompany ";
			strHQL +=" and le.businessdepartment =:sBusinessDep ";
			strHQL +=" and le.warzone is not null and le.area is not null  and le.branchactiongroup is not null  ";
			operlevel = "3";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && (sArea == null || "".equals(sArea)))//战区级
		{
			strHQL +=" and le.provincecity =:sProvince ";
			strHQL +=" and le.company =:sCompany ";
			strHQL +=" and le.businessdepartment =:sBusinessDep ";
			strHQL +=" and le.warzone =:sWarz ";
			strHQL +=" and le.area is not null and le.branchactiongroup is not null ";
			operlevel = "4";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && sArea != null && !"".equals(sArea)
				&& (sBranch == null || "".equals(sBranch)))//片区级
		{
			strHQL +=" and le.provincecity =:sProvince ";
			strHQL +=" and le.company =:sCompany ";
			strHQL +=" and le.businessdepartment =:sBusinessDep ";
			strHQL +=" and le.warzone =:sWarz ";
			strHQL +=" and le.area =:sArea  ";
			strHQL +=" and le.branchactiongroup is not null  ";
			operlevel = "5";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && sArea != null && !"".equals(sArea)
				&& sBranch != null && !"".equals(sBranch))//分行行动组
		{
			strHQL +=" and le.provincecity =:sProvince ";
			strHQL +=" and le.company =:sCompany ";
			strHQL +=" and le.businessdepartment =:sBusinessDep ";
			strHQL +=" and le.warzone =:sWarz ";
			strHQL +=" and le.area =:sArea  ";
			strHQL +=" and le.branchactiongroup =:sBranch ";
			operlevel = "6";
		}
		else if((sProvince == null || "".equals(sProvince)) && (sCompany == null || "".equals(sCompany)) && (sBusinessDep == null 
				|| "".equals(sBusinessDep)) && (sWarz == null || "".equals(sWarz)) && (sArea == null || "".equals(sArea))
				&& (sBranch == null || "".equals(sBranch)))//全空，最高级别
		{
			strHQL +=" and le.branchactiongroup is not null ";
			operlevel = "1";
		}
		else 
		{
			return "";
		}
//-----------------------------------------------------------------------------------------------------	
		if(subSql !=null && subSql.length()>0)
			strHQL +=" )" ;
		
		return strHQL;
	}
//以jdbc的方法查询
	public String getTyadminOperatorByJdbc(SsmnZyLevel opera,String tablename)
	{
		String strHQL = "( select le.id from Ssmn_Zy_Level le where 1=1 ";
		//----------------------------------------------------------------------------------------------------
		//操作员级别
		String sProvince = opera.getProvincecity();//省市
		String sCompany = opera.getCompany();//公司
		String sBusinessDep = opera.getBusinessdepartment();//事业部
		String sWarz = opera.getWarzone();//战区
		String sArea = opera.getArea();//片区
		String sBranch = opera.getBranchactiongroup();//分行行动组
		if(sProvince != null && !"".equals(sProvince) && (sCompany == null || "".equals(sCompany)))//省市级
		{
//			strHQL +=" and  " +tablename+ ".provincecity =:sProvince ";
//			strHQL +=" and " +tablename+ ".company is not null ";
			strHQL +=" and  le.provincecity ='";
			strHQL +=sProvince;
			strHQL +="' and  le.company is not null and le.businessdepartment is not null  and le.warzone is not null and le.area is not null  and le.branchactiongroup is not null ";
			operlevel = "1";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && ( sBusinessDep == null 
				|| "".equals(sBusinessDep)))//公司级
		{
			strHQL +=" and le.provincecity = '";
			strHQL +=sProvince;
			strHQL +="'  and le.company ='";
			strHQL +=sCompany;
			strHQL +="'  and le.businessdepartment is not null and le.warzone is not null and le.area is not null  and le.branchactiongroup is not null ";
			operlevel = "2";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && (sWarz == null || "".equals(sWarz)))//事业部级
		{
			strHQL +=" and le.provincecity ='";
			strHQL +=sProvince;
			strHQL +="'  and le.company ='";
			strHQL +=sCompany;
			strHQL +="'  and le.businessdepartment ='";
			strHQL +=sBusinessDep;
			strHQL +="'  and le.warzone is not null and le.area is not null  and le.branchactiongroup is not null  ";
			operlevel = "3";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && (sArea == null || "".equals(sArea)))//战区级
		{
			strHQL +=" and le.provincecity ='";
			strHQL +=sProvince;
			strHQL +="'  and le.company ='";
			strHQL +=sCompany;
			strHQL +="'  and le.businessdepartment ='";
			strHQL +=sBusinessDep;
			strHQL +="'  and le.warzone ='";
			strHQL +=sWarz;
			strHQL +="'  and le.area is not null and le.branchactiongroup is not null ";
			operlevel = "4";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && sArea != null && !"".equals(sArea)
				&& (sBranch == null || "".equals(sBranch)))//片区级
		{
			strHQL +=" and le.provincecity ='";
			strHQL +=sProvince;
			strHQL +="' and le.company ='";
			strHQL +=sCompany;
			strHQL +="' and le.businessdepartment ='";
			strHQL +=sBusinessDep;
			strHQL +="'  and le.warzone ='";
			strHQL +=sWarz;
			strHQL +="'  and le.area ='";
			strHQL +=sArea;
			strHQL +="'  and le.branchactiongroup is not null  ";
			operlevel = "5";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && sArea != null && !"".equals(sArea)
				&& sBranch != null && !"".equals(sBranch))//分行行动组
		{
			strHQL +=" and le.provincecity ='";
			strHQL +=sProvince;
			strHQL +="'  and le.company ='";
			strHQL +=sCompany;
			strHQL +="'  and le.businessdepartment ='";
			strHQL +=sBusinessDep;
			strHQL +="'  and le.warzone ='";
			strHQL +=sWarz;
			strHQL +="'  and le.area ='";
			strHQL +=sArea;
			strHQL +="' and le.branchactiongroup ='";
			strHQL +=sBranch;
			strHQL +="'  ";
			operlevel = "6";
		}
		else if((sProvince == null || "".equals(sProvince)) && (sCompany == null || "".equals(sCompany)) && (sBusinessDep == null 
				|| "".equals(sBusinessDep)) && (sWarz == null || "".equals(sWarz)) && (sArea == null || "".equals(sArea))
				&& (sBranch == null || "".equals(sBranch)))//全空，最高级别
		{
			strHQL +=" and le.branchactiongroup is not null ";
			operlevel = "1";
		}
		else 
		{
			return "";
		}
//-----------------------------------------------------------------------------------------------------	
		return strHQL+" )";
	}
	
	public String getDepartSqlByLevel(SsmnZyLevel opera)
	{
		String strHQL = "";
		//----------------------------------------------------------------------------------------------------
		//操作员级别
		String sProvince = opera.getProvincecity();//省市
		String sCompany = opera.getCompany();//公司
		String sBusinessDep = opera.getBusinessdepartment();//事业部
		String sWarz = opera.getWarzone();//战区
		String sArea = opera.getArea();//片区
		String sBranch = opera.getBranchactiongroup();//分行行动组
		if(sProvince != null && !"".equals(sProvince) && (sCompany == null || "".equals(sCompany)))//省市级
		{
			strHQL +=" and  l.provincecity ='";
			strHQL +=sProvince;
			strHQL +="' and  l.company is not null and l.businessdepartment is not null  and l.warzone is not null and l.area is not null  and l.branchactiongroup is not null ";
			operlevel = "1";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && ( sBusinessDep == null 
				|| "".equals(sBusinessDep)))//公司级
		{
			strHQL +=" and l.provincecity = '";
			strHQL +=sProvince;
			strHQL +="'  and l.company ='";
			strHQL +=sCompany;
			strHQL +="'  and l.businessdepartment is not null and l.warzone is not null and l.area is not null  and l.branchactiongroup is not null ";
			operlevel = "2";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && (sWarz == null || "".equals(sWarz)))//事业部级
		{
			strHQL +=" and l.provincecity ='";
			strHQL +=sProvince;
			strHQL +="'  and l.company ='";
			strHQL +=sCompany;
			strHQL +="'  and l.businessdepartment ='";
			strHQL +=sBusinessDep;
			strHQL +="'  and l.warzone is not null and l.area is not null  and l.branchactiongroup is not null  ";
			operlevel = "3";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && (sArea == null || "".equals(sArea)))//战区级
		{
			strHQL +=" and l.provincecity ='";
			strHQL +=sProvince;
			strHQL +="'  and l.company ='";
			strHQL +=sCompany;
			strHQL +="'  and l.businessdepartment ='";
			strHQL +=sBusinessDep;
			strHQL +="'  and l.warzone ='";
			strHQL +=sWarz;
			strHQL +="'  and l.area is not null and l.branchactiongroup is not null ";
			operlevel = "4";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && sArea != null && !"".equals(sArea)
				&& (sBranch == null || "".equals(sBranch)))//片区级
		{
			strHQL +=" and l.provincecity ='";
			strHQL +=sProvince;
			strHQL +="' and l.company ='";
			strHQL +=sCompany;
			strHQL +="' and l.businessdepartment ='";
			strHQL +=sBusinessDep;
			strHQL +="'  and l.warzone ='";
			strHQL +=sWarz;
			strHQL +="'  and l.area ='";
			strHQL +=sArea;
			strHQL +="'  and l.branchactiongroup is not null  ";
			operlevel = "5";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && sArea != null && !"".equals(sArea)
				&& sBranch != null && !"".equals(sBranch))//分行行动组
		{
			strHQL +=" and l.provincecity ='";
			strHQL +=sProvince;
			strHQL +="'  and l.company ='";
			strHQL +=sCompany;
			strHQL +="'  and l.businessdepartment ='";
			strHQL +=sBusinessDep;
			strHQL +="'  and l.warzone ='";
			strHQL +=sWarz;
			strHQL +="'  and l.area ='";
			strHQL +=sArea;
			strHQL +="' and l.branchactiongroup ='";
			strHQL +=sBranch;
			strHQL +="'  ";
			operlevel = "6";
		}
		else if((sProvince == null || "".equals(sProvince)) && (sCompany == null || "".equals(sCompany)) && (sBusinessDep == null 
				|| "".equals(sBusinessDep)) && (sWarz == null || "".equals(sWarz)) && (sArea == null || "".equals(sArea))
				&& (sBranch == null || "".equals(sBranch)))//全空，最高级别
		{
			strHQL +=" and l.branchactiongroup is not null ";
			operlevel = "1";
		}
		else 
		{
			return "";
		}
//-----------------------------------------------------------------------------------------------------	
		return strHQL;
	} 
	//以jdbc的方法查询，查询区域内的所有数据，比如 天津中原，内的所有的
	public String getTyadminOperatorByJdbcArea(SsmnZyLevel opera,String tablename)
	{
		String strHQL = "( select le.id from Ssmn_Zy_Level le where 1=1 ";
		//----------------------------------------------------------------------------------------------------
		//操作员级别
		String sProvince = opera.getProvincecity();//省市
		String sCompany = opera.getCompany();//公司
		String sBusinessDep = opera.getBusinessdepartment();//事业部
		String sWarz = opera.getWarzone();//战区
		String sArea = opera.getArea();//片区
		String sBranch = opera.getBranchactiongroup();//分行行动组
		if(sProvince != null && !"".equals(sProvince) && (sCompany == null || "".equals(sCompany)))//省市级
		{
//			strHQL +=" and  " +tablename+ ".provincecity =:sProvince ";
//			strHQL +=" and " +tablename+ ".company is not null ";
			strHQL +=" and  le.provincecity ='";
			strHQL +=sProvince;
			strHQL +="'  ";
			operlevel = "1";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && ( sBusinessDep == null 
				|| "".equals(sBusinessDep)))//公司级
		{
			strHQL +=" and le.provincecity = '";
			strHQL +=sProvince;
			strHQL +="'  and le.company ='";
			strHQL +=sCompany;
			strHQL +="'   ";
			operlevel = "2";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && (sWarz == null || "".equals(sWarz)))//事业部级
		{
			strHQL +=" and le.provincecity ='";
			strHQL +=sProvince;
			strHQL +="'  and le.company ='";
			strHQL +=sCompany;
			strHQL +="'  and le.businessdepartment ='";
			strHQL +=sBusinessDep;
			strHQL +="'   ";
			operlevel = "3";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && (sArea == null || "".equals(sArea)))//战区级
		{
			strHQL +=" and le.provincecity ='";
			strHQL +=sProvince;
			strHQL +="'  and le.company ='";
			strHQL +=sCompany;
			strHQL +="'  and le.businessdepartment ='";
			strHQL +=sBusinessDep;
			strHQL +="'  and le.warzone ='";
			strHQL +=sWarz;
			strHQL +="'   ";
			operlevel = "4";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && sArea != null && !"".equals(sArea)
				&& (sBranch == null || "".equals(sBranch)))//片区级
		{
			strHQL +=" and le.provincecity ='";
			strHQL +=sProvince;
			strHQL +="' and le.company ='";
			strHQL +=sCompany;
			strHQL +="' and le.businessdepartment ='";
			strHQL +=sBusinessDep;
			strHQL +="'  and le.warzone ='";
			strHQL +=sWarz;
			strHQL +="'  and le.area ='";
			strHQL +=sArea;
			strHQL +="'   ";
			operlevel = "5";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && sArea != null && !"".equals(sArea)
				&& sBranch != null && !"".equals(sBranch))//分行行动组
		{
			strHQL +=" and le.provincecity ='";
			strHQL +=sProvince;
			strHQL +="'  and le.company ='";
			strHQL +=sCompany;
			strHQL +="'  and le.businessdepartment ='";
			strHQL +=sBusinessDep;
			strHQL +="'  and le.warzone ='";
			strHQL +=sWarz;
			strHQL +="'  and le.area ='";
			strHQL +=sArea;
			strHQL +="' and le.branchactiongroup ='";
			strHQL +=sBranch;
			strHQL +="'  ";
			operlevel = "6";
		}
		else if((sProvince == null || "".equals(sProvince)) && (sCompany == null || "".equals(sCompany)) && (sBusinessDep == null 
				|| "".equals(sBusinessDep)) && (sWarz == null || "".equals(sWarz)) && (sArea == null || "".equals(sArea))
				&& (sBranch == null || "".equals(sBranch)))//全空，最高级别
		{
			strHQL +="  ";
			operlevel = "1";
		}
		else 
		{
			return "";
		}
//-----------------------------------------------------------------------------------------------------	
		return strHQL+" )";
	}
	
	//拼sql:   查询区域内的所有数据，比如 天津中原，内的所有的
	public String getTyadminOperatorSelectArea(SsmnZyLevel opera,String tablename)
	{
		String strHQL = "( select le.id from SsmnZyLevel le where 1=1 ";
		//----------------------------------------------------------------------------------------------------
		//操作员级别
		String sProvince = opera.getProvincecity();//省市
		String sCompany = opera.getCompany();//公司
		String sBusinessDep = opera.getBusinessdepartment();//事业部
		String sWarz = opera.getWarzone();//战区
		String sArea = opera.getArea();//片区
		String sBranch = opera.getBranchactiongroup();//分行行动组
		if(sProvince != null && !"".equals(sProvince) && (sCompany == null || "".equals(sCompany)))//省市级
		{
//			strHQL +=" and  " +tablename+ ".provincecity =:sProvince ";
//			strHQL +=" and " +tablename+ ".company is not null ";
			strHQL +=" and  le.provincecity =:sProvince ";
			operlevel = "1";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && ( sBusinessDep == null 
				|| "".equals(sBusinessDep)))//公司级
		{
			strHQL +=" and le.provincecity =:sProvince ";
			strHQL +=" and le.company =:sCompany ";
			operlevel = "2";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && (sWarz == null || "".equals(sWarz)))//事业部级
		{
			strHQL +=" and le.provincecity =:sProvince ";
			strHQL +=" and le.company =:sCompany ";
			strHQL +=" and le.businessdepartment =:sBusinessDep ";
			operlevel = "3";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && (sArea == null || "".equals(sArea)))//战区级
		{
			strHQL +=" and le.provincecity =:sProvince ";
			strHQL +=" and le.company =:sCompany ";
			strHQL +=" and le.businessdepartment =:sBusinessDep ";
			strHQL +=" and le.warzone =:sWarz ";
			operlevel = "4";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && sArea != null && !"".equals(sArea)
				&& (sBranch == null || "".equals(sBranch)))//片区级
		{
			strHQL +=" and le.provincecity =:sProvince ";
			strHQL +=" and le.company =:sCompany ";
			strHQL +=" and le.businessdepartment =:sBusinessDep ";
			strHQL +=" and le.warzone =:sWarz ";
			strHQL +=" and le.area =:sArea  ";
			operlevel = "5";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && sArea != null && !"".equals(sArea)
				&& sBranch != null && !"".equals(sBranch))//分行行动组
		{
			strHQL +=" and le.provincecity =:sProvince ";
			strHQL +=" and le.company =:sCompany ";
			strHQL +=" and le.businessdepartment =:sBusinessDep ";
			strHQL +=" and le.warzone =:sWarz ";
			strHQL +=" and le.area =:sArea  ";
			strHQL +=" and le.branchactiongroup =:sBranch ";
			operlevel = "6";
		}
		else if((sProvince == null || "".equals(sProvince)) && (sCompany == null || "".equals(sCompany)) && (sBusinessDep == null 
				|| "".equals(sBusinessDep)) && (sWarz == null || "".equals(sWarz)) && (sArea == null || "".equals(sArea))
				&& (sBranch == null || "".equals(sBranch)))//全空，最高级别
		{
			strHQL +="  ";
			operlevel = "1";
		}
		else 
		{
			return "";
		}
//-----------------------------------------------------------------------------------------------------	
		return strHQL+" )";
	}
	
	public void setOperatorAttribute(SsmnZyLevel opera, Query query)
	{
		String sProvince = opera.getProvincecity();//省市
		String sCompany = opera.getCompany();//公司
		String sBusinessDep = opera.getBusinessdepartment();//事业部
		String sWarz = opera.getWarzone();//战区
		String sArea = opera.getArea();//片区
		String sBranch = opera.getBranchactiongroup();//分行行动组
		
		//----------------------------------------------------------------------------------------------------------
		if(sProvince != null && !"".equals(sProvince) && (sCompany == null || "".equals(sCompany)))//省市级
		{
			query.setString("sProvince", sProvince);
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && (sBusinessDep == null 
				|| "".equals(sBusinessDep)))//公司级
		{
			query.setString("sProvince",sProvince);
			query.setString("sCompany",sCompany);
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && (sWarz == null || "".equals(sWarz)))//事业部级
		{
			query.setString("sProvince",sProvince);
			query.setString("sCompany",sCompany);
			query.setString("sBusinessDep",sBusinessDep);
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && (sArea == null || "".equals(sArea)))//战区级
		{
			query.setString("sProvince",sProvince);
			query.setString("sCompany",sCompany);
			query.setString("sBusinessDep",sBusinessDep);
			query.setString("sWarz",sWarz);
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && sArea != null && !"".equals(sArea)
				&& (sBranch == null || "".equals(sBranch)))//片区级
		{
			query.setString("sProvince",sProvince);
			query.setString("sCompany",sCompany);
			query.setString("sBusinessDep",sBusinessDep);
			query.setString("sWarz",sWarz);
			query.setString("sArea",sArea);
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && sArea != null && !"".equals(sArea)
				&& sBranch != null && !"".equals(sBranch))//分行行动组
		{
			query.setString("sProvince",sProvince);
			query.setString("sCompany",sCompany);
			query.setString("sBusinessDep",sBusinessDep);
			query.setString("sWarz",sWarz);
			query.setString("sArea",sArea);
			query.setString("sBranch",sBranch);
		}
		
//----------------------------------------------------------------------------------------------------------
	}

	public void setOperatorAttributejdbc(SsmnZyLevel opera, List<Object> args)
	{
		String sProvince = opera.getProvincecity();//省市
		String sCompany = opera.getCompany();//公司
		String sBusinessDep = opera.getBusinessdepartment();//事业部
		String sWarz = opera.getWarzone();//战区
		String sArea = opera.getArea();//片区
		String sBranch = opera.getBranchactiongroup();//分行行动组
		
		//----------------------------------------------------------------------------------------------------------
		if(sProvince != null && !"".equals(sProvince) && (sCompany == null || "".equals(sCompany)))//省市级
		{
			args.add(sProvince);
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && (sBusinessDep == null 
				|| "".equals(sBusinessDep)))//公司级
		{
			args.add(sProvince);
			args.add(sCompany);
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && (sWarz == null || "".equals(sWarz)))//事业部级
		{
			args.add(sProvince);
			args.add(sCompany);
			args.add(sBusinessDep);
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && (sArea == null || "".equals(sArea)))//战区级
		{
			args.add(sProvince);
			args.add(sCompany);
			args.add(sBusinessDep);
			args.add(sWarz);
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && sArea != null && !"".equals(sArea)
				&& (sBranch == null || "".equals(sBranch)))//片区级
		{
			args.add(sProvince);
			args.add(sCompany);
			args.add(sBusinessDep);
			args.add(sWarz);
			args.add(sArea);
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && sArea != null && !"".equals(sArea)
				&& sBranch != null && !"".equals(sBranch))//分行行动组
		{
			
			args.add(sProvince);
			args.add(sCompany);
			args.add(sBusinessDep);
			args.add(sWarz);
			args.add(sArea);
			args.add(sBranch);
		}
		
//----------------------------------------------------------------------------------------------------------
	}
	
	
	public String getTyadminOperatorLevelid(SsmnZyLevel opera)
	{
		
		//----------------------------------------------------------------------------------------------------
		//操作员级别
		String sProvince = opera.getProvincecity();//省市
		String sCompany = opera.getCompany();//公司
		String sBusinessDep = opera.getBusinessdepartment();//事业部
		String sWarz = opera.getWarzone();//战区
		String sArea = opera.getArea();//片区
		String sBranch = opera.getBranchactiongroup();//分行行动组
		if(sProvince != null && !"".equals(sProvince) && (sCompany == null || "".equals(sCompany)))//省市级
		{
			operlevel = "1";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && ( sBusinessDep == null 
				|| "".equals(sBusinessDep)))//公司级
		{
			operlevel = "2";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && (sWarz == null || "".equals(sWarz)))//事业部级
		{
			operlevel = "3";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && (sArea == null || "".equals(sArea)))//战区级
		{
			operlevel = "4";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && sArea != null && !"".equals(sArea)
				&& (sBranch == null || "".equals(sBranch)))//片区级
		{
			operlevel = "5";
		}
		else if(sProvince != null && !"".equals(sProvince) && sCompany != null && !"".equals(sCompany) && sBusinessDep != null 
				&& !"".equals(sBusinessDep) && sWarz != null && !"".equals(sWarz) && sArea != null && !"".equals(sArea)
				&& sBranch != null && !"".equals(sBranch))//分行行动组
		{
			operlevel = "6";
		}
		else if((sProvince == null || "".equals(sProvince)) && (sCompany == null || "".equals(sCompany)) && (sBusinessDep == null 
				|| "".equals(sBusinessDep)) && (sWarz == null || "".equals(sWarz)) && (sArea == null || "".equals(sArea))
				&& (sBranch == null || "".equals(sBranch)))//全空，最高级别
		{
			operlevel = "1";
		}
		else 
		{
			operlevel= "";
		}
//-----------------------------------------------------------------------------------------------------	
		return operlevel;
	}
	
	
	/**
	 * 
	 * 适用于录音页面的查询导出 excel 2003
	 */
	public void createCdrRowTitle(int index, HSSFRow rowinput1, HSSFCellStyle cellStyleTitle,SsmnZyLevel level)
	{	
		HSSFCell cell0row0 = rowinput1.createCell(0);
		cell0row0.setCellValue("序号");
	    cell0row0.setCellStyle(cellStyleTitle);
		
	    String  businName= new UtilFunctionDao().getLevelName(1, level);//事业部
	    HSSFCell cell1row0 = rowinput1.createCell(1);
	    cell1row0.setCellValue(businName);
	    cell1row0.setCellStyle(cellStyleTitle);
	    
	    String warName = new UtilFunctionDao().getLevelName(2, level);//战区
	    HSSFCell cell2row0 = rowinput1.createCell(2);
	    cell2row0.setCellValue(warName);
	    cell2row0.setCellStyle(cellStyleTitle);
	    
	    String areaName = new UtilFunctionDao().getLevelName(3, level);//片区
	    HSSFCell cell3row0 = rowinput1.createCell(3);
	    cell3row0.setCellValue(areaName);
	    cell3row0.setCellStyle(cellStyleTitle);
	    
	    String branName = new UtilFunctionDao().getLevelName(4, level);//行动组
	    HSSFCell cell4row0 = rowinput1.createCell(4);
	    cell4row0.setCellValue(branName);
	    cell4row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell5row0 = rowinput1.createCell(5);
	    cell5row0.setCellValue("经纪人姓名");
	    cell5row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell6row0 = rowinput1.createCell(6);
	    cell6row0.setCellValue("员工编号");
	    cell6row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell7row0 = rowinput1.createCell(7);
	    cell7row0.setCellValue("主号码");
	    cell7row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell8row0 = rowinput1.createCell(8);
	    cell8row0.setCellValue("副号码");
	    cell8row0.setCellStyle(cellStyleTitle);
	    
	    if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))
	    {
	    	HSSFCell cell9row0 = rowinput1.createCell(9);
		    cell9row0.setCellValue("第一联系人");
		    cell9row0.setCellStyle(cellStyleTitle);
	    
		    HSSFCell cell10row0 = rowinput1.createCell(10);
		    cell10row0.setCellValue("客户电话");
		    cell10row0.setCellStyle(cellStyleTitle);
		   
		    HSSFCell cell11row0 = rowinput1.createCell(11);
		    cell11row0.setCellValue("渠道");
		    cell11row0.setCellStyle(cellStyleTitle);
		    
		    HSSFCell cell12row0 = rowinput1.createCell(12);
		    cell12row0.setCellValue("通话类型");
		    cell12row0.setCellStyle(cellStyleTitle);
		    
		    HSSFCell cell13row0 = rowinput1.createCell(13);
		    cell13row0.setCellValue("通话时间");
		    cell13row0.setCellStyle(cellStyleTitle);
		    
		    HSSFCell cell14row0 = rowinput1.createCell(14);
		    cell14row0.setCellValue("通话时长");
		    cell14row0.setCellStyle(cellStyleTitle);
		   
		    HSSFCell cell15row0 = rowinput1.createCell(15);
		    cell15row0.setCellValue("录音文件名称");
		    cell15row0.setCellStyle(cellStyleTitle);
		    
		    HSSFCell cell16row0 = rowinput1.createCell(16);
		    cell16row0.setCellValue("是否标记为骚扰电话");
		    cell16row0.setCellStyle(cellStyleTitle);
		    
		    if(ConfigMgr.getAddRemark().equals("1"))
		    {
		    	HSSFCell cell17row0 = rowinput1.createCell(17);
			    cell17row0.setCellValue("备注");
			    cell17row0.setCellStyle(cellStyleTitle);
		    }
	    }else
	    {
	    	HSSFCell cell9row0 = rowinput1.createCell(9);
		    cell9row0.setCellValue("客户电话");
		    cell9row0.setCellStyle(cellStyleTitle);
		   
		    HSSFCell cell10row0 = rowinput1.createCell(10);
		    cell10row0.setCellValue("渠道");
		    cell10row0.setCellStyle(cellStyleTitle);
		    
		    HSSFCell cell11row0 = rowinput1.createCell(11);
		    cell11row0.setCellValue("通话类型");
		    cell11row0.setCellStyle(cellStyleTitle);
		    
		    HSSFCell cell12row0 = rowinput1.createCell(12);
		    cell12row0.setCellValue("通话时间");
		    cell12row0.setCellStyle(cellStyleTitle);
		    
		    HSSFCell cell13row0 = rowinput1.createCell(13);
		    cell13row0.setCellValue("通话时长");
		    cell13row0.setCellStyle(cellStyleTitle);
		   
		    HSSFCell cell14row0 = rowinput1.createCell(14);
		    cell14row0.setCellValue("录音文件名称");
		    cell14row0.setCellStyle(cellStyleTitle);
		    
		    HSSFCell cell15row0 = rowinput1.createCell(15);
		    cell15row0.setCellValue("是否标记为骚扰电话");
		    cell15row0.setCellStyle(cellStyleTitle);
		    
		    if(ConfigMgr.getAddRemark().equals("1"))
		    {
		    	HSSFCell cell16row0 = rowinput1.createCell(16);
			    cell16row0.setCellValue("备注");
			    cell16row0.setCellStyle(cellStyleTitle);
		    }
	    }
	    

	}

	public void createRowContent(int index,HSSFRow rowcdrnum,ZydcRecord info,HSSFCellStyle cellStylecontent)
	{
		 HSSFCell cell0 = rowcdrnum.createCell(0);
		 cell0.setCellValue(info.getStreamNumber()+"");
		 cell0.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell1 = rowcdrnum.createCell(1);
		 cell1.setCellValue(info.getBusinessdepartment());
		 cell1.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell2 = rowcdrnum.createCell(2);
		 cell2.setCellValue(info.getWarzone());
		 cell2.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell3 = rowcdrnum.createCell(3);
		 cell3.setCellValue(info.getArea());
		 cell3.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell4 = rowcdrnum.createCell(4);
		 cell4.setCellValue(info.getBranchactiongroup());
		 cell4.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell5 = rowcdrnum.createCell(5);
		 cell5.setCellValue(info.getUsername());
		 cell5.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell6 = rowcdrnum.createCell(6);
		 cell6.setCellValue(info.getEmpno());
		 cell6.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell7 = rowcdrnum.createCell(7);
		 cell7.setCellValue(info.getMsisdn());
		 cell7.setCellStyle(cellStylecontent);
		 
		 HSSFCell cell8 = rowcdrnum.createCell(8);
		 cell8.setCellValue(info.getSsmnnumber());
		 cell8.setCellStyle(cellStylecontent);
		 
		 if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))
		 {
			 HSSFCell cell9 = rowcdrnum.createCell(9);
			 cell9.setCellValue(info.getFirstMsisdn());
			 cell9.setCellStyle(cellStylecontent);
			 
			 HSSFCell cell10 = rowcdrnum.createCell(10);
			 cell10.setCellValue(info.getClientnumber());
			 cell10.setCellStyle(cellStylecontent);
			 
			 HSSFCell cell11 = rowcdrnum.createCell(11);
			 cell11.setCellValue(info.getChannelname());
			 cell11.setCellStyle(cellStylecontent);
			 
			 HSSFCell cell12 = rowcdrnum.createCell(12);
			 cell12.setCellValue(info.getCalltype());
			 cell12.setCellStyle(cellStylecontent);
			 
			 HSSFCell cell13 = rowcdrnum.createCell(13);
			 cell13.setCellValue(info.getCallstarttime());
			 cell13.setCellStyle(cellStylecontent);
					 
			 HSSFCell cell14 = rowcdrnum.createCell(14);
			 cell14.setCellStyle(cellStylecontent);
		     cell14.setCellValue(info.getStrCallDuration());
			 
			 HSSFCell cell15 = rowcdrnum.createCell(15);
			 cell15.setCellValue(info.getFileName());
			 cell15.setCellStyle(cellStylecontent);
			 
			 HSSFCell cell16 = rowcdrnum.createCell(16);
			 cell16.setCellValue(info.getIsblacknum());
			 cell16.setCellStyle(cellStylecontent);
			 
			 if(ConfigMgr.getAddRemark().equals("1"))
		    {
				 HSSFCell cell17 = rowcdrnum.createCell(17);
				 cell17.setCellValue(info.getRemark());
				 cell17.setCellStyle(cellStylecontent); 
		    }
		 }else
		 {
			 HSSFCell cell9 = rowcdrnum.createCell(9);
			 cell9.setCellValue(info.getClientnumber());
			 cell9.setCellStyle(cellStylecontent);
			 
			 HSSFCell cell10 = rowcdrnum.createCell(10);
			 cell10.setCellValue(info.getChannelname());
			 cell10.setCellStyle(cellStylecontent);
			 
			 HSSFCell cell11 = rowcdrnum.createCell(11);
			 cell11.setCellValue(info.getCalltype());
			 cell11.setCellStyle(cellStylecontent);
			 
			 HSSFCell cell12 = rowcdrnum.createCell(12);
			 cell12.setCellValue(info.getCallstarttime());
			 cell12.setCellStyle(cellStylecontent);
					 
			 HSSFCell cell13 = rowcdrnum.createCell(13);
			 cell13.setCellStyle(cellStylecontent);
		     cell13.setCellValue(info.getStrCallDuration());
			 
			 HSSFCell cell14 = rowcdrnum.createCell(14);
			 cell14.setCellValue(info.getFileName());
			 cell14.setCellStyle(cellStylecontent);
			 
			 HSSFCell cell15 = rowcdrnum.createCell(15);
			 cell15.setCellValue(info.getIsblacknum());
			 cell15.setCellStyle(cellStylecontent);
			 
			 if(ConfigMgr.getAddRemark().equals("1"))
			 {
				 HSSFCell cell16 = rowcdrnum.createCell(16);
				 cell16.setCellValue(info.getRemark());
				 cell16.setCellStyle(cellStylecontent); 
			 }
		 }
		
		 
	}
/**
 * 
 * 适用于短信查询中的导出 2003excel
 */
	public void createSmsRowTitle (int index,HSSFRow rowsms, HSSFCellStyle cellStyleTitle)
	{
		HSSFCell cell1sms0 = rowsms.createCell(0);
		 cell1sms0.setCellValue("序号");
		 cell1sms0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms1 = rowsms.createCell(1);
		 cell1sms1.setCellValue("接收时间");
		 cell1sms1.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms2 = rowsms.createCell(2);
		 cell1sms2.setCellValue("事业部");
		 cell1sms2.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms3 = rowsms.createCell(3);
		 cell1sms3.setCellValue("战区");
		 cell1sms3.setCellStyle(cellStyleTitle);
	 
		 HSSFCell cell1sms4 = rowsms.createCell(4);
		 cell1sms4.setCellValue("片区");
		 cell1sms4.setCellStyle(cellStyleTitle);

		 HSSFCell cell1sms5 = rowsms.createCell(5);
		 cell1sms5.setCellValue("分行组别");
		 cell1sms5.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms6 = rowsms.createCell(6);
		 cell1sms6.setCellValue("经纪人姓名");
		 cell1sms6.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms7 = rowsms.createCell(7);
		 cell1sms7.setCellValue("员工编号");
		 cell1sms7.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms8 = rowsms.createCell(8);
		 cell1sms8.setCellValue("主号码");
		 cell1sms8.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms9 = rowsms.createCell(9);
		 cell1sms9.setCellValue("副号码");
		 cell1sms9.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms10 = rowsms.createCell(10);
		 cell1sms10.setCellValue("客户电话");
		 cell1sms10.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms11 = rowsms.createCell(11);
		 cell1sms11.setCellValue("渠道");
		 cell1sms11.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms12 = rowsms.createCell(12);
		 cell1sms12.setCellValue("短信内容");
		 cell1sms12.setCellStyle(cellStyleTitle);

	}

	public void createSmsRowContent(int index,HSSFRow rowsmsnum,ZydcRecord info, HSSFCellStyle cellStyleTitle)
	{
		 HSSFCell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellValue(info.getStreamNumber()+"");
		 cell0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellValue(info.getRcvmsgtime());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellValue(info.getBusinessdepartment());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellValue(info.getWarzone());
		 cell3.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell4 = rowsmsnum.createCell(4);
		 cell4.setCellValue(info.getArea());
		 cell4.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell5 = rowsmsnum.createCell(5);
		 cell5.setCellValue(info.getBranchactiongroup());
		 cell5.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell6 = rowsmsnum.createCell(6);
		 cell6.setCellValue(info.getUsername());
		 cell6.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell7 = rowsmsnum.createCell(7);
		 cell7.setCellValue(info.getEmpno());
		 cell7.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell8 = rowsmsnum.createCell(8);
		 cell8.setCellValue(info.getMsisdn());
		 cell8.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell9 = rowsmsnum.createCell(9);
		 cell9.setCellValue(info.getStreamNumber());
		 cell9.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell10 = rowsmsnum.createCell(10);
		 cell10.setCellValue(info.getClientnumber());
		 cell10.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell11 = rowsmsnum.createCell(11);
		 cell11.setCellValue(info.getChannelname());
		 cell11.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell12 = rowsmsnum.createCell(12);
		 cell12.setCellValue(info.getMsgcontent());
		 cell12.setCellStyle(cellStyleTitle);

	}
	
	/**
	 * 
	 * 适用于经纪人信息导出
	 */
	public void createSSMNTitle (int index,HSSFRow rowsms, HSSFCellStyle cellStyleTitle,SsmnZyLevel level)
	{
		String businName = new UtilFunctionDao().getLevelName(1, level);//事业部
		HSSFCell cell1sms0 = rowsms.createCell(0);
		 cell1sms0.setCellValue(businName);
		 cell1sms0.setCellStyle(cellStyleTitle);
		 
		 String warName = new UtilFunctionDao().getLevelName(2, level);//战区
		 HSSFCell cell1sms1 = rowsms.createCell(1);
		 cell1sms1.setCellValue(warName);
		 cell1sms1.setCellStyle(cellStyleTitle);
		 
		 String areaName = new UtilFunctionDao().getLevelName(3, level);//片区
		 HSSFCell cell1sms2 = rowsms.createCell(2);
		 cell1sms2.setCellValue(areaName);
		 cell1sms2.setCellStyle(cellStyleTitle);
		 
		 String branName = new UtilFunctionDao().getLevelName(4, level);//行动组
		 HSSFCell cell1sms3 = rowsms.createCell(3);
		 cell1sms3.setCellValue(branName);
		 cell1sms3.setCellStyle(cellStyleTitle);
	 
		 HSSFCell cell1sms4 = rowsms.createCell(4);
		 cell1sms4.setCellValue("姓名");
		 cell1sms4.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms5 = rowsms.createCell(5);
		 cell1sms5.setCellValue("员工编号");
		 cell1sms5.setCellStyle(cellStyleTitle);

		 HSSFCell cell1sms6 = rowsms.createCell(6);
		 cell1sms6.setCellValue("主号码");
		 cell1sms6.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms7 = rowsms.createCell(7);
		 cell1sms7.setCellValue("副号码");
		 cell1sms7.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms8 = rowsms.createCell(8);
		 cell1sms8.setCellValue("渠道");
		 cell1sms8.setCellStyle(cellStyleTitle);
		 
		 if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))
		 {
			 HSSFCell cell1sms9 = rowsms.createCell(9);
			 cell1sms9.setCellValue("第二联系人");
			 cell1sms9.setCellStyle(cellStyleTitle);
			 
			 HSSFCell cell1sms10 = rowsms.createCell(10);
			 cell1sms10.setCellValue("备注");
			 cell1sms10.setCellStyle(cellStyleTitle);
		
		 }else
		 {
			 HSSFCell cell1sms9 = rowsms.createCell(9);
			 cell1sms9.setCellValue("备注");
			 cell1sms9.setCellStyle(cellStyleTitle);
		 }
	}

	public void createSSMNRowContent(int index,HSSFRow rowsmsnum,SsmnZyUser info, HSSFCellStyle cellStyleTitle)
	{
		 HSSFCell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellValue(info.getBusinessdepartment());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellValue(info.getWarzone());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellValue(info.getArea());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellValue(info.getBranchactiongroup());
		 cell3.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell4 = rowsmsnum.createCell(4);
		 cell4.setCellValue(info.getName());
		 cell4.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell5 = rowsmsnum.createCell(5);
		 cell5.setCellValue(info.getEmpno());
		 cell5.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell6 = rowsmsnum.createCell(6);
		 cell6.setCellValue(info.getMsisdn());
		 cell6.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell7 = rowsmsnum.createCell(7);
		 cell7.setCellValue(info.getSsmnnumber());
		 cell7.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell8 = rowsmsnum.createCell(8);
		 cell8.setCellValue(info.getChannelname());
		 cell8.setCellStyle(cellStyleTitle);
		 
		 if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))
		 {
			 HSSFCell cell9 = rowsmsnum.createCell(9);
			 cell9.setCellValue(info.getSecondMsisdn());
			 cell9.setCellStyle(cellStyleTitle);
			 
			 HSSFCell cell10 = rowsmsnum.createCell(10);
			 cell10.setCellValue(info.getRemark());
			 cell10.setCellStyle(cellStyleTitle);
		 }else
		 {
			 HSSFCell cell9 = rowsmsnum.createCell(9);
			 cell9.setCellValue(info.getRemark());
			 cell9.setCellStyle(cellStyleTitle);
		 }
	
	}
	
	/*
	 * 导出副号码信息
	 */
	public void createSSMNNumTitle (int index,HSSFRow rowsms, HSSFCellStyle cellStyleTitle,SsmnZyLevel level)
	{
		HSSFCell cell1sms0 = rowsms.createCell(0);
		 cell1sms0.setCellValue("副号码");
		 cell1sms0.setCellStyle(cellStyleTitle);
		 
		 String businName = new UtilFunctionDao().getLevelName(1, level);//事业部
		 HSSFCell cell1sms1 = rowsms.createCell(1);
		 cell1sms1.setCellValue(businName);
		 cell1sms1.setCellStyle(cellStyleTitle);
		
		 String warName = new UtilFunctionDao().getLevelName(2, level);//战区
		 HSSFCell cell1sms2 = rowsms.createCell(2);
		 cell1sms2.setCellValue(warName);
		 cell1sms2.setCellStyle(cellStyleTitle);
		 
		 String areaName = new UtilFunctionDao().getLevelName(3, level);//片区
		 HSSFCell cell1sms3 = rowsms.createCell(3);
		 cell1sms3.setCellValue(areaName);
		 cell1sms3.setCellStyle(cellStyleTitle);
	 
		 String branName = new UtilFunctionDao().getLevelName(4, level);//行动组
		 HSSFCell cell1sms4 = rowsms.createCell(4);
		 cell1sms4.setCellValue(branName);
		 cell1sms4.setCellStyle(cellStyleTitle);

		 HSSFCell cell1sms5 = rowsms.createCell(5);
		 cell1sms5.setCellValue("状态");
		 cell1sms5.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms6 = rowsms.createCell(6);
		 cell1sms6.setCellValue("类型");
		 cell1sms6.setCellStyle(cellStyleTitle);
	}
	
	public void createSSMNNumRowContent(int index,HSSFRow rowsmsnum,SsmnZYEnableNumberRecord info, HSSFCellStyle cellStyleTitle)
	{
		 HSSFCell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellValue(info.getEnablenumber());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellValue(info.getBusinessdepartment());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellValue(info.getWarzone());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellValue(info.getArea());
		 cell3.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell4 = rowsmsnum.createCell(4);
		 cell4.setCellValue(info.getBranchactiongroup());
		 cell4.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell5 = rowsmsnum.createCell(5);
		 if(info.getStatus() == 0)
			 cell5.setCellValue("未使用");
		 else
			 cell5.setCellValue("已使用");
		 cell5.setCellStyle(cellStyleTitle); 
		 
		 HSSFCell cell6 = rowsmsnum.createCell(6);
		 if(info.getType() == 1)
			 cell6.setCellValue("业主副号码");
		 else
			 cell6.setCellValue("经纪人副号码");
		 cell6.setCellStyle(cellStyleTitle);

	}
	
	/*
	 * 批量解绑导出记录
	 * 
	 */
	public void createCancelBatchTitle(int index,HSSFRow rowsms, HSSFCellStyle cellStyleTitle,boolean issuccess)
	{
		HSSFCell cell1sms0 = rowsms.createCell(0);
		 cell1sms0.setCellValue("姓名");
		 cell1sms0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms1 = rowsms.createCell(1);
		 cell1sms1.setCellValue("主号码");
		 cell1sms1.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms2 = rowsms.createCell(2);
		 cell1sms2.setCellValue("副号码");
		 cell1sms2.setCellStyle(cellStyleTitle);
		 
		 if(!issuccess)//失败原因
		 {
			 HSSFCell cell1sms3 = rowsms.createCell(3);
			 cell1sms3.setCellValue("失败原因");
			 cell1sms3.setCellStyle(cellStyleTitle);
		 }
	}
	
	public void createCancelBatchContent(int index,HSSFRow rowsmsnum,SsmnZyUser info, HSSFCellStyle cellStyleTitle,boolean issuccess)
	{
		 HSSFCell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellValue(info.getName());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellValue(info.getMsisdn());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellValue(info.getSsmnnumber());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 if(!issuccess)//错误原因暂存放到groupname
		 {
			 HSSFCell cell3 = rowsmsnum.createCell(3);
			 cell3.setCellValue(info.getGroupname());
			 cell3.setCellStyle(cellStyleTitle);
		 }
		 
		
	}
	
	/*
	 * 批量绑定，批量解绑，统一后的导出记录
	 * type:1 :批量解绑
	 * 		2:批量绑定,批量绑定，有的地方有 第二联系人，有的地方没有
	 */
	public void createBatchTitle(int index,HSSFRow rowsms, HSSFCellStyle cellStyleTitle,boolean issuccess,int type)
	{
		HSSFCell cell1sms0 = rowsms.createCell(0);
		 cell1sms0.setCellValue("事业部");
		 cell1sms0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms1 = rowsms.createCell(1);
		 cell1sms1.setCellValue("战区");
		 cell1sms1.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms2 = rowsms.createCell(2);
		 cell1sms2.setCellValue("片区");
		 cell1sms2.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms3 = rowsms.createCell(3);
		 cell1sms3.setCellValue("分行行动组");
		 cell1sms3.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms4 = rowsms.createCell(4);
		 cell1sms4.setCellValue("姓名");
		 cell1sms4.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms5 = rowsms.createCell(5);
		 cell1sms5.setCellValue("员工编号");
		 cell1sms5.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms6 = rowsms.createCell(6);
		 cell1sms6.setCellValue("主号码");
		 cell1sms6.setCellStyle(cellStyleTitle);
		 
		 if(issuccess)//只有成功的，才显示副号码，失败的不显示
		 {
			 HSSFCell cell1sms7 = rowsms.createCell(7);
			 cell1sms7.setCellValue("副号码");
			 cell1sms7.setCellStyle(cellStyleTitle);
			 
			 HSSFCell cell1sms8 = rowsms.createCell(8);
			 cell1sms8.setCellValue("渠道");
			 cell1sms8.setCellStyle(cellStyleTitle);
			 
			 if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))//添加第二联系人，只有批量绑定才有
			 {
				 if(type == 2)//批量绑定
				 {
					 HSSFCell cell1sms9 = rowsms.createCell(9);
					 cell1sms9.setCellValue("第二联系人");
					 cell1sms9.setCellStyle(cellStyleTitle);
				 }
			 }
		 }else
		 {
			 HSSFCell cell1sms7 = rowsms.createCell(7);
			 cell1sms7.setCellValue("渠道");
			 cell1sms7.setCellStyle(cellStyleTitle);

			 if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))//添加第二联系人，只有批量绑定才有
			 {
				 if(type == 2)//批量绑定
				 {
					 HSSFCell cell1sms8 = rowsms.createCell(8);
					 cell1sms8.setCellValue("第二联系人");
					 cell1sms8.setCellStyle(cellStyleTitle);
				 }
			 }
			 
			 HSSFCell cell1sms9 = rowsms.createCell(9);
			 cell1sms9.setCellValue("失败原因");
			 cell1sms9.setCellStyle(cellStyleTitle);
		 }

	}
	/*
	 * type 1:批量解绑
	 * 		2:批量绑定,批量绑定的时候，，并且 配置文件中，第二联系人开关打开，才有第二联系人
	 */
	public void createBatchContent(int index,HSSFRow rowsmsnum,SsmnZyUser info, 
			HSSFCellStyle cellStyleTitle,boolean issuccess,int type)
	{
		 //事业部
		 HSSFCell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellValue(info.getBusinessdepartment());
		 cell0.setCellStyle(cellStyleTitle);
		 //战区
		 HSSFCell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellValue(info.getWarzone());
		 cell1.setCellStyle(cellStyleTitle);
		 //片区
		 HSSFCell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellValue(info.getArea());
		 cell2.setCellStyle(cellStyleTitle);
		 //分行行动组
		 HSSFCell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellValue(info.getBranchactiongroup());
		 cell3.setCellStyle(cellStyleTitle);
		 //姓名
		 HSSFCell cell4 = rowsmsnum.createCell(4);
		 cell4.setCellValue(info.getName());
		 cell4.setCellStyle(cellStyleTitle);
		 //员工编号
		 HSSFCell cell5 = rowsmsnum.createCell(5);
		 cell5.setCellValue(info.getEmpno());
		 cell5.setCellStyle(cellStyleTitle);
		 //主号码
		 HSSFCell cell6 = rowsmsnum.createCell(6);
		 cell6.setCellValue(info.getMsisdn());
		 cell6.setCellStyle(cellStyleTitle);
		 if(issuccess)//只有成功的才显示副号码，失败的不显示
		 {
			 //副号码
			 HSSFCell cell7 = rowsmsnum.createCell(7);
			 if(info.getSsmnnumber()!=null && info.getSsmnnumber().equals(Constants.SSMN_NUM_DEFAULT))
				 cell7.setCellValue("");
			 else
				 cell7.setCellValue(info.getSsmnnumber());
			 cell7.setCellStyle(cellStyleTitle);
		 
			 //渠道
			 HSSFCell cell8 = rowsmsnum.createCell(8);
			 cell8.setCellValue(info.getChannelname());
			 cell8.setCellStyle(cellStyleTitle);
			 
			 //第二联系人
			 if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))
			 {
				 if(type ==2)//批量绑定，才有第二联系人
				 {
					 HSSFCell cell9 = rowsmsnum.createCell(9);
					 cell9.setCellValue(info.getSecondMsisdn());
					 cell9.setCellStyle(cellStyleTitle);
				 }
			 }
		 }else
		 {
			//渠道
			 HSSFCell cell7 = rowsmsnum.createCell(7);
			 cell7.setCellValue(info.getChannelname());
			 cell7.setCellStyle(cellStyleTitle);
			 
			//第二联系人
			 if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))
			 {
				 if(type ==2)//批量绑定，才有第二联系人
				 {
					 HSSFCell cell8 = rowsmsnum.createCell(8);
					 cell8.setCellValue(info.getSecondMsisdn());
					 cell8.setCellStyle(cellStyleTitle);
				 }
			 }
			 
			 //错误信息
			 HSSFCell cell9 = rowsmsnum.createCell(9);
			 cell9.setCellValue(info.getGroupname());
			 cell9.setCellStyle(cellStyleTitle);
		 }
		
	}
	
	public void createOperatorTitle(int index, HSSFRow rowinput1, HSSFCellStyle cellStyleTitle,SsmnZyLevel zylevel)
	{	
		HSSFCell cell0row0 = rowinput1.createCell(0);
		cell0row0.setCellValue("操作员帐号");
	    cell0row0.setCellStyle(cellStyleTitle);
		
	    HSSFCell cell1row0 = rowinput1.createCell(1);
	    cell1row0.setCellValue("真实姓名");
	    cell1row0.setCellStyle(cellStyleTitle);
	    
	    String businName = new UtilFunctionDao().getLevelName(1, zylevel);//事业部
	    HSSFCell cell2row0 = rowinput1.createCell(2);
	    cell2row0.setCellValue(businName);
	    cell2row0.setCellStyle(cellStyleTitle);
	    
	    String warName = new UtilFunctionDao().getLevelName(2, zylevel);//战区
	    HSSFCell cell3row0 = rowinput1.createCell(3);
	    cell3row0.setCellValue(warName);
	    cell3row0.setCellStyle(cellStyleTitle);
	    
	    String areaName = new UtilFunctionDao().getLevelName(3, zylevel);//片区
	    HSSFCell cell4row0 = rowinput1.createCell(4);
	    cell4row0.setCellValue(areaName);
	    cell4row0.setCellStyle(cellStyleTitle);
	    
	    String branName = new UtilFunctionDao().getLevelName(4, zylevel);//行动组
	    HSSFCell cell5row0 = rowinput1.createCell(5);
	    cell5row0.setCellValue(branName);
	    cell5row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell6row0 = rowinput1.createCell(6);
	    cell6row0.setCellValue("创建时间");
	    cell6row0.setCellStyle(cellStyleTitle);
	}
	
	public void createOperatorContent(int index,HSSFRow rowsmsnum,TyadminOperators info, HSSFCellStyle cellStyleTitle)
	{
		 HSSFCell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellValue(info.getId().getOpeno());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellValue(info.getAgentInfo());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellValue(info.getBusinessdepartment());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellValue(info.getWarzone());
		 cell3.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell4 = rowsmsnum.createCell(4);
		 cell4.setCellValue(info.getArea());
		 cell4.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell5 = rowsmsnum.createCell(5);
		 cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell5.setCellValue(info.getBranchactiongroup());
		 cell5.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell6 = rowsmsnum.createCell(6);
		 cell6.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell6.setCellValue(info.getCreate_timebystring());
		 cell6.setCellStyle(cellStyleTitle);
	}
	
	//群组导出
	public void createGroupTitle(int index, HSSFRow rowinput1, HSSFCellStyle cellStyleTitle)
	{	
		HSSFCell cell0row0 = rowinput1.createCell(0);
		cell0row0.setCellValue("组名");
	    cell0row0.setCellStyle(cellStyleTitle);
		
	    HSSFCell cell1row0 = rowinput1.createCell(1);
	    cell1row0.setCellValue("组描述");
	    cell1row0.setCellStyle(cellStyleTitle);
	    	    
	    HSSFCell cell6row0 = rowinput1.createCell(2);
	    cell6row0.setCellValue("创建时间");
	    cell6row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell5row0 = rowinput1.createCell(3);
	    cell5row0.setCellValue("创建人");
	    cell5row0.setCellStyle(cellStyleTitle);
	}
	public void createGroupContent(int index,HSSFRow rowsmsnum,TyadminGroups info, HSSFCellStyle cellStyleTitle)
	{
		HSSFCell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell0.setCellValue(info.getGroupName());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1.setCellValue(info.getDescription());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell2.setCellValue(info.getCreateDateByString());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell3.setCellValue(info.getOperatorId());
		 cell3.setCellStyle(cellStyleTitle);
	}
	/**
	 * 批量绑定
	 * @param index
	 * @param rowsms
	 * @param cellStyleTitle
	 * @param issuccess
	 * 
	 */
	public void createbindBatchTitle(int index,HSSFRow rowsms, HSSFCellStyle cellStyleTitle,boolean issuccess)
	{
		HSSFCell cell1sms0 = rowsms.createCell(0);
		 cell1sms0.setCellValue("姓名");
		 cell1sms0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms1 = rowsms.createCell(1);
		 cell1sms1.setCellValue("主号码");
		 cell1sms1.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms2 = rowsms.createCell(2);
		 cell1sms2.setCellValue("员工编号");
		 cell1sms2.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms3 = rowsms.createCell(3);
		 cell1sms3.setCellValue("副号码");
		 cell1sms3.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms4 = rowsms.createCell(4);
		 cell1sms4.setCellValue("渠道");
		 cell1sms4.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms5 = rowsms.createCell(5);
		 cell1sms5.setCellValue("分行行动组");
		 cell1sms5.setCellStyle(cellStyleTitle);
		 
		 if(!issuccess)//失败原因
		 {
			 HSSFCell cell1sms6 = rowsms.createCell(6);
			 cell1sms6.setCellValue("失败原因");
			 cell1sms6.setCellStyle(cellStyleTitle);
		 }
	}
	
	public void createBindBatchContent(int index,HSSFRow rowsmsnum,SsmnZyUser info, HSSFCellStyle cellStyleTitle,boolean issuccess)
	{
		 HSSFCell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellValue(info.getName());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellValue(info.getMsisdn());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellValue(info.getEmpno());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellValue(info.getSsmnnumber());
		 cell3.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell4 = rowsmsnum.createCell(4);
		 cell4.setCellValue(info.getChannelname());
		 cell4.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell5 = rowsmsnum.createCell(5);
		 cell5.setCellValue(info.getBranchactiongroup());
		 cell5.setCellStyle(cellStyleTitle);
		 
		 if(!issuccess)//错误原因暂存放到groupname
		 {
			 HSSFCell cell6 = rowsmsnum.createCell(6);
			 cell6.setCellValue(info.getGroupname());
			 cell6.setCellStyle(cellStyleTitle);
		 }
		 
		
	}
	
	//公用的　查找　配置的分配副号码的级别
	public String getMatchNumberLevelField(String level)
	{
		
		String sreturn = "";
		if(level.equals("1"))
			sreturn = "getProvincecity";
		if(level.equals("2"))
			sreturn = "getCompany";
		if(level.equals("3"))
			sreturn = "getBusinessdepartment";
		if(level.equals("4"))
			sreturn = "getWarzone";
		if(level.equals("5"))
			sreturn = "getArea";
		if(level.equals("6"))
			sreturn = "getBranchactiongroup";
		
		return sreturn;
	}
	
	/**
	 * 
	 * 适用于录音页面的查询导出 excel 2007
	 */
	public void createCdrRowTitle2007(int index, Row rowinput1, CellStyle cellStyleTitle,SsmnZyLevel level)
	{	
		Cell cell0row0 = rowinput1.createCell(0);
		cell0row0.setCellType(XSSFCell.CELL_TYPE_STRING);//文本格式   
		cell0row0.setCellValue("序号");
	    cell0row0.setCellStyle(cellStyleTitle);
				
	    String businName = new UtilFunctionDao().getLevelName(1, level);//事业部
	    Cell cell1row0 = rowinput1.createCell(1);
	    cell1row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell1row0.setCellValue(businName);
	    cell1row0.setCellStyle(cellStyleTitle);
	    
	    String warName = new UtilFunctionDao().getLevelName(2, level);//战区
	    Cell cell2row0 = rowinput1.createCell(2);
	    cell2row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell2row0.setCellValue(warName);
	    cell2row0.setCellStyle(cellStyleTitle);
	    
	    String areaName = new UtilFunctionDao().getLevelName(3, level);//片区
	    Cell cell3row0 = rowinput1.createCell(3);
	    cell3row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell3row0.setCellValue(areaName);
	    cell3row0.setCellStyle(cellStyleTitle);
	    
	    String branName = new UtilFunctionDao().getLevelName(4, level);//行动组
	    Cell cell4row0 = rowinput1.createCell(4);
	    cell4row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell4row0.setCellValue(branName);
	    cell4row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell5row0 = rowinput1.createCell(5);
	    cell5row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell5row0.setCellValue("经纪人姓名");
	    cell5row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell6row0 = rowinput1.createCell(6);
	    cell6row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell6row0.setCellValue("员工编号");
	    cell6row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell7row0 = rowinput1.createCell(7);
	    cell7row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell7row0.setCellValue("主号码");
	    cell7row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell8row0 = rowinput1.createCell(8);
	    cell8row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell8row0.setCellValue("副号码");
	    cell8row0.setCellStyle(cellStyleTitle);
	    
	    if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))
	    {
	    	Cell cell9row0 = rowinput1.createCell(9);
		    cell9row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell9row0.setCellValue("第二联系人");
		    cell9row0.setCellStyle(cellStyleTitle);
		    
		    Cell cell10row0 = rowinput1.createCell(10);
		    cell10row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell10row0.setCellValue("客户电话");
		    cell10row0.setCellStyle(cellStyleTitle);
		   
		    Cell cell11row0 = rowinput1.createCell(11);
		    cell11row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell11row0.setCellValue("渠道");
		    cell11row0.setCellStyle(cellStyleTitle);
		   
		    Cell cell12row0 = rowinput1.createCell(12);
		    cell12row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell12row0.setCellValue("通话类型");
		    cell12row0.setCellStyle(cellStyleTitle);
		    
		    Cell cell13row0 = rowinput1.createCell(13);
		    cell13row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell13row0.setCellValue("通话时间");
		    cell13row0.setCellStyle(cellStyleTitle);
		    	    
		    Cell cell14row0 = rowinput1.createCell(14);
		    cell14row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell14row0.setCellValue("通话时长");
		    cell14row0.setCellStyle(cellStyleTitle);
		   
		    Cell cell15row0 = rowinput1.createCell(15);
		    cell15row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell15row0.setCellValue("录音文件名称");
		    cell15row0.setCellStyle(cellStyleTitle);
		    
		    Cell cell16row0 = rowinput1.createCell(16);
		    cell16row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell16row0.setCellValue("备注");
		    cell16row0.setCellStyle(cellStyleTitle);
		    
			 if(ConfigMgr.getAddRemark().equals("1"))
			 {
				Cell cell17row0 = rowinput1.createCell(17);
			    cell17row0.setCellType(XSSFCell.CELL_TYPE_STRING);
			    cell17row0.setCellValue("备注");
			    cell17row0.setCellStyle(cellStyleTitle); 
			 }
	    }else
	    {
		    Cell cell9row0 = rowinput1.createCell(9);
		    cell9row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell9row0.setCellValue("客户电话");
		    cell9row0.setCellStyle(cellStyleTitle);
		   
		    Cell cell10row0 = rowinput1.createCell(10);
		    cell10row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell10row0.setCellValue("渠道");
		    cell10row0.setCellStyle(cellStyleTitle);
		   
		    Cell cell11row0 = rowinput1.createCell(11);
		    cell11row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell11row0.setCellValue("通话类型");
		    cell11row0.setCellStyle(cellStyleTitle);
		    
		    Cell cell12row0 = rowinput1.createCell(12);
		    cell12row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell12row0.setCellValue("通话时间");
		    cell12row0.setCellStyle(cellStyleTitle);
		    	    
		    Cell cell13row0 = rowinput1.createCell(13);
		    cell13row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell13row0.setCellValue("通话时长");
		    cell13row0.setCellStyle(cellStyleTitle);
		   
		    Cell cell14row0 = rowinput1.createCell(14);
		    cell14row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell14row0.setCellValue("录音文件名称");
		    cell14row0.setCellStyle(cellStyleTitle);
		    
		    Cell cell15row0 = rowinput1.createCell(15);
		    cell15row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell15row0.setCellValue("是否标记为骚扰电话");
		    cell15row0.setCellStyle(cellStyleTitle);
		    
		    if(ConfigMgr.getAddRemark().equals("1"))
			{
		    	Cell cell16row0 = rowinput1.createCell(16);
			    cell16row0.setCellType(XSSFCell.CELL_TYPE_STRING);
			    cell16row0.setCellValue("备注");
			    cell16row0.setCellStyle(cellStyleTitle);
			}
	    }
	    

	}
	
	public void createRowContent2007(int index,Row rowcdrnum,ZydcRecord info,CellStyle cellStylecontent)
	{
		 Cell cell0 = rowcdrnum.createCell(0);
		 cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell0.setCellValue(info.getStreamNumber()+"");
		 cell0.setCellStyle(cellStylecontent);

		 Cell cell1 = rowcdrnum.createCell(1);
		 cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1.setCellValue(info.getBusinessdepartment());
		 cell1.setCellStyle(cellStylecontent);
		 
		 Cell cell2 = rowcdrnum.createCell(2);
		 cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell2.setCellValue(info.getWarzone());
		 cell2.setCellStyle(cellStylecontent);
		 
		 Cell cell3 = rowcdrnum.createCell(3);
		 cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell3.setCellValue(info.getArea());
		 cell3.setCellStyle(cellStylecontent);
		 
		 Cell cell4 = rowcdrnum.createCell(4);
		 cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell4.setCellValue(info.getBranchactiongroup());
		 cell4.setCellStyle(cellStylecontent);
		 
		 Cell cell5 = rowcdrnum.createCell(5);
		 cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell5.setCellValue(info.getUsername());
		 cell5.setCellStyle(cellStylecontent);
		 
		 Cell cell6 = rowcdrnum.createCell(6);
		 cell6.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell6.setCellValue(info.getEmpno());
		 cell6.setCellStyle(cellStylecontent);
		 
		 Cell cell7 = rowcdrnum.createCell(7);
		 cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell7.setCellValue(info.getMsisdn());
		 cell7.setCellStyle(cellStylecontent);
		 
		 Cell cell8 = rowcdrnum.createCell(8);
		 cell8.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell8.setCellValue(info.getSsmnnumber());
		 cell8.setCellStyle(cellStylecontent);
		 
		 
		 if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))
		 {
			 Cell cell9 = rowcdrnum.createCell(9);
			 cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell9.setCellValue(info.getFirstMsisdn());
			 cell9.setCellStyle(cellStylecontent);
			 
			 Cell cell10 = rowcdrnum.createCell(10);
			 cell10.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell10.setCellValue(info.getClientnumber());
			 cell10.setCellStyle(cellStylecontent);
			 
			 Cell cell11 = rowcdrnum.createCell(11);
			 cell11.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell11.setCellValue(info.getChannelname());
			 cell11.setCellStyle(cellStylecontent);
			 
			 Cell cell12 = rowcdrnum.createCell(12);
			 cell12.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell12.setCellValue(info.getCalltype());
			 cell12.setCellStyle(cellStylecontent);
			 
			 Cell cell13 = rowcdrnum.createCell(13);
			 cell13.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell13.setCellValue(info.getCallstarttime());
			 cell13.setCellStyle(cellStylecontent);
		 
			 Cell cell14 = rowcdrnum.createCell(14);
			 cell14.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell14.setCellStyle(cellStylecontent);
		     cell14.setCellValue(info.getStrCallDuration());
			 
			 Cell cell15 = rowcdrnum.createCell(15);
			 cell15.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell15.setCellValue(info.getFileName());
			 cell15.setCellStyle(cellStylecontent);
			 
			 Cell cell16 = rowcdrnum.createCell(16);
			 cell16.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell16.setCellValue(info.getIsblacknum());
			 cell16.setCellStyle(cellStylecontent);
			 
			 if(ConfigMgr.getAddRemark().equals("1"))
			 {
				 Cell cell17 = rowcdrnum.createCell(17);
				 cell17.setCellType(XSSFCell.CELL_TYPE_STRING);
				 cell17.setCellValue(info.getRemark());
				 cell17.setCellStyle(cellStylecontent);
			 }
		 }else
		 {
			 Cell cell9 = rowcdrnum.createCell(9);
			 cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell9.setCellValue(info.getClientnumber());
			 cell9.setCellStyle(cellStylecontent);
			 
			 Cell cell10 = rowcdrnum.createCell(10);
			 cell10.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell10.setCellValue(info.getChannelname());
			 cell10.setCellStyle(cellStylecontent);
			 
			 Cell cell11 = rowcdrnum.createCell(11);
			 cell11.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell11.setCellValue(info.getCalltype());
			 cell11.setCellStyle(cellStylecontent);
			 
			 Cell cell12 = rowcdrnum.createCell(12);
			 cell12.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell12.setCellValue(info.getCallstarttime());
			 cell12.setCellStyle(cellStylecontent);
		 
			 Cell cell13 = rowcdrnum.createCell(13);
			 cell13.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell13.setCellStyle(cellStylecontent);
		     cell13.setCellValue(info.getStrCallDuration());
			 
			 Cell cell14 = rowcdrnum.createCell(14);
			 cell14.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell14.setCellValue(info.getFileName());
			 cell14.setCellStyle(cellStylecontent);
			 
			 Cell cell15 = rowcdrnum.createCell(15);
			 cell15.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell15.setCellValue(info.getIsblacknum());
			 cell15.setCellStyle(cellStylecontent);
			 
			 if(ConfigMgr.getAddRemark().equals("1"))
			 {
				 Cell cell16 = rowcdrnum.createCell(16);
				 cell16.setCellType(XSSFCell.CELL_TYPE_STRING);
				 cell16.setCellValue(info.getRemark());
				 cell16.setCellStyle(cellStylecontent);
			 }
		 }
		 
		 
	}
	
	/*
	 * 
	 * 适用于2007 的excel
	 * 
	 */	
	public void createSmsRowTitle2007 (int index,Row rowsms, CellStyle cellStyleTitle)
	{
		Cell cell1sms0 = rowsms.createCell(0);
		cell1sms0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms0.setCellValue("序号");
		 cell1sms0.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms1 = rowsms.createCell(1);
		 cell1sms1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms1.setCellValue("接收时间");
		 cell1sms1.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms2 = rowsms.createCell(2);
		 cell1sms2.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms2.setCellValue("事业部");
		 cell1sms2.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms3 = rowsms.createCell(3);
		 cell1sms3.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms3.setCellValue("战区");
		 cell1sms3.setCellStyle(cellStyleTitle);
	 
		 Cell cell1sms4 = rowsms.createCell(4);
		 cell1sms4.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms4.setCellValue("片区");
		 cell1sms4.setCellStyle(cellStyleTitle);

		 Cell cell1sms5 = rowsms.createCell(5);
		 cell1sms5.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms5.setCellValue("分行组别");
		 cell1sms5.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms6 = rowsms.createCell(6);
		 cell1sms6.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms6.setCellValue("经纪人姓名");
		 cell1sms6.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms7 = rowsms.createCell(7);
		 cell1sms7.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms7.setCellValue("员工编号");
		 cell1sms7.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms8 = rowsms.createCell(8);
		 cell1sms8.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms8.setCellValue("主号码");
		 cell1sms8.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms9 = rowsms.createCell(9);
		 cell1sms9.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms9.setCellValue("副号码");
		 cell1sms9.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms10 = rowsms.createCell(10);
		 cell1sms10.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms10.setCellValue("客户电话");
		 cell1sms10.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms11 = rowsms.createCell(11);
		 cell1sms11.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms11.setCellValue("渠道");
		 cell1sms11.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms12 = rowsms.createCell(12);
		 cell1sms12.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms12.setCellValue("短信内容");
		 cell1sms12.setCellStyle(cellStyleTitle);

	}
	
	public void createSmsRowContent2007(int index,Row rowsmsnum,ZydcRecord info, CellStyle cellStyleTitle)
	{
		 Cell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell0.setCellValue(info.getStreamNumber()+"");
		 cell0.setCellStyle(cellStyleTitle);
		 		 
		 Cell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1.setCellValue(info.getRcvmsgtime());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 Cell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell2.setCellValue(info.getBusinessdepartment());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 Cell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell3.setCellValue(info.getWarzone());
		 cell3.setCellStyle(cellStyleTitle);
		 
		 Cell cell4 = rowsmsnum.createCell(4);
		 cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell4.setCellValue(info.getArea());
		 cell4.setCellStyle(cellStyleTitle);
		 
		 Cell cell5 = rowsmsnum.createCell(5);
		 cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell5.setCellValue(info.getBranchactiongroup());
		 cell5.setCellStyle(cellStyleTitle);
		 
		 Cell cell6 = rowsmsnum.createCell(6);
		 cell6.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell6.setCellValue(info.getUsername());
		 cell6.setCellStyle(cellStyleTitle);
		 
		 Cell cell7 = rowsmsnum.createCell(7);
		 cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell7.setCellValue(info.getEmpno());
		 cell7.setCellStyle(cellStyleTitle);
		 
		 Cell cell8 = rowsmsnum.createCell(8);
		 cell8.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell8.setCellValue(info.getMsisdn());
		 cell8.setCellStyle(cellStyleTitle);
		 
		 Cell cell9 = rowsmsnum.createCell(9);
		 cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell9.setCellValue(info.getSsmnnumber());
		 cell9.setCellStyle(cellStyleTitle);
		 
		 Cell cell10 = rowsmsnum.createCell(10);
		 cell10.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell10.setCellValue(info.getClientnumber());
		 cell10.setCellStyle(cellStyleTitle);
		 
		 Cell cell11 = rowsmsnum.createCell(11);
		 cell11.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell11.setCellValue(info.getChannelname());
		 cell11.setCellStyle(cellStyleTitle);
		 
		 Cell cell12 = rowsmsnum.createCell(12);
		 cell12.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell12.setCellValue(info.getMsgcontent());
		 cell12.setCellStyle(cellStyleTitle);

	}
	
	/**
	 * 
	 * 适用于经纪人信息导出 excel 2007
	 */
	public void createSSMNTitle2007 (int index,Row rowsms, CellStyle cellStyleTitle,SsmnZyLevel level)
	{
		String businName = new UtilFunctionDao().getLevelName(1, level);//事业部
		Cell cell1sms0 = rowsms.createCell(0);
		cell1sms0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms0.setCellValue(businName);
		 cell1sms0.setCellStyle(cellStyleTitle);
		 
		 String warName = new UtilFunctionDao().getLevelName(2, level);//战区
		 Cell cell1sms1 = rowsms.createCell(1);
		 cell1sms1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms1.setCellValue(warName);
		 cell1sms1.setCellStyle(cellStyleTitle);
		 
		 String areaName = new UtilFunctionDao().getLevelName(3, level);//片区
		 Cell cell1sms2 = rowsms.createCell(2);
		 cell1sms2.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms2.setCellValue(areaName);
		 cell1sms2.setCellStyle(cellStyleTitle);
		 
		 String branName = new UtilFunctionDao().getLevelName(4, level);//行动组
		 Cell cell1sms3 = rowsms.createCell(3);
		 cell1sms3.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms3.setCellValue(branName);
		 cell1sms3.setCellStyle(cellStyleTitle);
	 
		 Cell cell1sms4 = rowsms.createCell(4);
		 cell1sms4.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms4.setCellValue("姓名");
		 cell1sms4.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms5 = rowsms.createCell(5);
		 cell1sms5.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms5.setCellValue("员工编号");
		 cell1sms5.setCellStyle(cellStyleTitle);

		 Cell cell1sms6 = rowsms.createCell(6);
		 cell1sms6.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms6.setCellValue("主号码");
		 cell1sms6.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms7 = rowsms.createCell(7);
		 cell1sms7.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms7.setCellValue("副号码");
		 cell1sms7.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms8 = rowsms.createCell(8);
		 cell1sms8.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms8.setCellValue("渠道");
		 cell1sms8.setCellStyle(cellStyleTitle);
		 
		 if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))
		 {
			 Cell cell1sms9 = rowsms.createCell(9);
			 cell1sms9.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell1sms9.setCellValue("第二联系人");
			 cell1sms9.setCellStyle(cellStyleTitle);
			 
			 Cell cell1sms10 = rowsms.createCell(10);
			 cell1sms10.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell1sms10.setCellValue("备注");
			 cell1sms10.setCellStyle(cellStyleTitle);
		 }else
		 {
			 Cell cell1sms9 = rowsms.createCell(9);
			 cell1sms9.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell1sms9.setCellValue("备注");
			 cell1sms9.setCellStyle(cellStyleTitle);
		 }
		 
	}
	
	public void createSSMNRowContent2007(int index,Row rowsmsnum,SsmnZyUser info, CellStyle cellStyleTitle)
	{
		 Cell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell0.setCellValue(info.getBusinessdepartment());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 Cell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1.setCellValue(info.getWarzone());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 Cell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell2.setCellValue(info.getArea());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 Cell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell3.setCellValue(info.getBranchactiongroup());
		 cell3.setCellStyle(cellStyleTitle);
		 
		 Cell cell4 = rowsmsnum.createCell(4);
		 cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell4.setCellValue(info.getName());
		 cell4.setCellStyle(cellStyleTitle);
		 
		 Cell cell5 = rowsmsnum.createCell(5);
		 cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell5.setCellValue(info.getEmpno());
		 cell5.setCellStyle(cellStyleTitle);
		 
		 Cell cell6 = rowsmsnum.createCell(6);
		 cell6.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell6.setCellValue(info.getMsisdn());
		 cell6.setCellStyle(cellStyleTitle);
		 
		 Cell cell7 = rowsmsnum.createCell(7);
		 cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell7.setCellValue(info.getSsmnnumber());
		 cell7.setCellStyle(cellStyleTitle);
		 
		 Cell cell8 = rowsmsnum.createCell(8);
		 cell8.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell8.setCellValue(info.getChannelname());
		 cell8.setCellStyle(cellStyleTitle);
		 
		 if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))
		 {
			 Cell cell9 = rowsmsnum.createCell(9);
			 cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell9.setCellValue(info.getSecondMsisdn());
			 cell9.setCellStyle(cellStyleTitle);
			 
			 Cell cell10 = rowsmsnum.createCell(10);
			 cell10.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell10.setCellValue(info.getRemark());
			 cell10.setCellStyle(cellStyleTitle);
		 }else
		 {
			 Cell cell9 = rowsmsnum.createCell(9);
			 cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell9.setCellValue(info.getRemark());
			 cell9.setCellStyle(cellStyleTitle);
		 }
		 
	}
	
	/*
	 * 导出副号码信息 excel 2007
	 */
	public void createSSMNNumTitle2007 (int index,Row rowsms, CellStyle cellStyleTitle,SsmnZyLevel level)
	{
		Cell cell1sms0 = rowsms.createCell(0);
		cell1sms0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms0.setCellValue("副号码");
		 cell1sms0.setCellStyle(cellStyleTitle);
		 
		 String businName = new UtilFunctionDao().getLevelName(1, level);//事业部
		 Cell cell1sms1 = rowsms.createCell(1);
		 cell1sms1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms1.setCellValue(businName);
		 cell1sms1.setCellStyle(cellStyleTitle);
		 
		 String warName = new UtilFunctionDao().getLevelName(2, level);//战区
		 Cell cell1sms2 = rowsms.createCell(2);
		 cell1sms2.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms2.setCellValue(warName);
		 cell1sms2.setCellStyle(cellStyleTitle);
		 
		 String areaName = new UtilFunctionDao().getLevelName(3, level);//片区
		 Cell cell1sms3 = rowsms.createCell(3);
		 cell1sms3.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms3.setCellValue(areaName);
		 cell1sms3.setCellStyle(cellStyleTitle);
	 
		 String branName = new UtilFunctionDao().getLevelName(4, level);//行动组
		 Cell cell1sms4 = rowsms.createCell(4);
		 cell1sms4.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms4.setCellValue(branName);
		 cell1sms4.setCellStyle(cellStyleTitle);

		 Cell cell1sms5 = rowsms.createCell(5);
		 cell1sms5.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms5.setCellValue("状态");
		 cell1sms5.setCellStyle(cellStyleTitle);
	}
	
	public void createSSMNNumRowContent2007(int index,Row rowsmsnum,SsmnZYEnableNumberRecord info, CellStyle cellStyleTitle)
	{
		 Cell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell0.setCellValue(info.getEnablenumber());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 Cell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1.setCellValue(info.getBusinessdepartment());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 Cell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell2.setCellValue(info.getWarzone());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 Cell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell3.setCellValue(info.getArea());
		 cell3.setCellStyle(cellStyleTitle);
		 
		 Cell cell4 = rowsmsnum.createCell(4);
		 cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell4.setCellValue(info.getBranchactiongroup());
		 cell4.setCellStyle(cellStyleTitle);
		 
		 Cell cell5 = rowsmsnum.createCell(5);
		 cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
		 if(info.getStatus() == 0)
			 cell5.setCellValue("未使用");
		 else
			 cell5.setCellValue("已使用");
		 cell5.setCellStyle(cellStyleTitle); 

	}
	
	public void createOperatorTitle2007(int index, Row rowinput1, CellStyle cellStyleTitle, SsmnZyLevel zylevel)
	{	
		Cell cell0row0 = rowinput1.createCell(0);
		cell0row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell0row0.setCellValue("操作员帐号");
	    cell0row0.setCellStyle(cellStyleTitle);
		
	    Cell cell1row0 = rowinput1.createCell(1);
	    cell1row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell1row0.setCellValue("真实姓名");
	    cell1row0.setCellStyle(cellStyleTitle);
	    
	    String businName = new UtilFunctionDao().getLevelName(1, zylevel);//事业部
	    Cell cell2row0 = rowinput1.createCell(2);
	    cell2row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell2row0.setCellValue(businName);
	    cell2row0.setCellStyle(cellStyleTitle);
	    
	    String warName = new UtilFunctionDao().getLevelName(2, zylevel);//战区
	    Cell cell3row0 = rowinput1.createCell(3);
	    cell3row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell3row0.setCellValue(warName);
	    cell3row0.setCellStyle(cellStyleTitle);
	    
	    String areaName = new UtilFunctionDao().getLevelName(3, zylevel);//片区
	    Cell cell4row0 = rowinput1.createCell(4);
	    cell4row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell4row0.setCellValue(areaName);
	    cell4row0.setCellStyle(cellStyleTitle);
	    
	    String branName = new UtilFunctionDao().getLevelName(4, zylevel);//行动组
	    Cell cell5row0 = rowinput1.createCell(5);
	    cell5row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell5row0.setCellValue(branName);
	    cell5row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell6row0 = rowinput1.createCell(6);
	    cell6row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell6row0.setCellValue("创建时间");
	    cell6row0.setCellStyle(cellStyleTitle);
	}
	
	public void createOperatorContent2007(int index,Row rowsmsnum,TyadminOperators info, CellStyle cellStyleTitle)
	{
		 Cell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell0.setCellValue(info.getId().getOpeno());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 Cell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1.setCellValue(info.getAgentInfo());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 Cell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell2.setCellValue(info.getBusinessdepartment());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 Cell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell3.setCellValue(info.getWarzone());
		 cell3.setCellStyle(cellStyleTitle);
		 
		 Cell cell4 = rowsmsnum.createCell(4);
		 cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell4.setCellValue(info.getArea());
		 cell4.setCellStyle(cellStyleTitle);
		 
		 Cell cell5 = rowsmsnum.createCell(5);
		 cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell5.setCellValue(info.getBranchactiongroup());
		 cell5.setCellStyle(cellStyleTitle);
		 
		 Cell cell6 = rowsmsnum.createCell(6);
		 cell6.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell6.setCellValue(info.getCreate_timebystring());
		 cell6.setCellStyle(cellStyleTitle);
		  
	}
	public void createGroupTitle2007(int index, Row rowinput1, CellStyle cellStyleTitle)
	{	
		Cell cell0row0 = rowinput1.createCell(0);
		cell0row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell0row0.setCellValue("组名");
	    cell0row0.setCellStyle(cellStyleTitle);
		
	    Cell cell1row0 = rowinput1.createCell(1);
	    cell1row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell1row0.setCellValue("组描述");
	    cell1row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell2row0 = rowinput1.createCell(2);
	    cell2row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell2row0.setCellValue("创建时间");
	    cell2row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell3row0 = rowinput1.createCell(3);
	    cell3row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell3row0.setCellValue("创建人");
	    cell3row0.setCellStyle(cellStyleTitle);
	}
	public void createGroupContent2007(int index,Row rowsmsnum,TyadminGroups info, CellStyle cellStyleTitle)
	{
		 Cell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell0.setCellValue(info.getGroupName());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 Cell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1.setCellValue(info.getDescription());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 Cell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell2.setCellValue(info.getCreateDateByString());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 Cell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell3.setCellValue(info.getOperatorId());
		 cell3.setCellStyle(cellStyleTitle);
	}	
	
	public void createOperatorLogTitle(int index, HSSFRow rowinput1, HSSFCellStyle cellStyleTitle,SsmnZyLevel zylevel)
	{	
		HSSFCell cell0row0 = rowinput1.createCell(0);
		cell0row0.setCellValue("登录账号");
	    cell0row0.setCellStyle(cellStyleTitle);
		
	    String businName = new UtilFunctionDao().getLevelName(1, zylevel);//事业部
	    HSSFCell cell1row0 = rowinput1.createCell(1);
	    cell1row0.setCellValue(businName);
	    cell1row0.setCellStyle(cellStyleTitle);
	    
	    String warName = new UtilFunctionDao().getLevelName(2, zylevel);//战区
	    HSSFCell cell2row0 = rowinput1.createCell(2);
	    cell2row0.setCellValue(warName);
	    cell2row0.setCellStyle(cellStyleTitle);
	    
	    String areaName = new UtilFunctionDao().getLevelName(3, zylevel);//片区
	    HSSFCell cell3row0 = rowinput1.createCell(3);
	    cell3row0.setCellValue(areaName);
	    cell3row0.setCellStyle(cellStyleTitle);
	    
	    String branName = new UtilFunctionDao().getLevelName(4, zylevel);//行动组
	    HSSFCell cell4row0 = rowinput1.createCell(4);
	    cell4row0.setCellValue(branName);
	    cell4row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell5row0 = rowinput1.createCell(5);
	    cell5row0.setCellValue("最近登录时间");
	    cell5row0.setCellStyle(cellStyleTitle);
	    
	    HSSFCell cell6row0 = rowinput1.createCell(6);
	    cell6row0.setCellValue("登录次数");
	    cell6row0.setCellStyle(cellStyleTitle);
	    
	    if(ConfigMgr.getDistChannel().equals("1"))//区分A+和渠道
	    {
	    	HSSFCell cell7row0 = rowinput1.createCell(7);
		    cell7row0.setCellValue("听录音A+次数");
		    cell7row0.setCellStyle(cellStyleTitle);
		    
		    HSSFCell cell8row0 = rowinput1.createCell(8);
		    cell8row0.setCellValue("听录音渠道次数");
		    cell8row0.setCellStyle(cellStyleTitle);
		    
		    HSSFCell cell9row0 = rowinput1.createCell(9);
		    cell9row0.setCellValue("备注次数");
		    cell9row0.setCellStyle(cellStyleTitle);
	    }else
	    {
		    HSSFCell cell7row0 = rowinput1.createCell(7);
		    cell7row0.setCellValue("听录音次数");
		    cell7row0.setCellStyle(cellStyleTitle);
		    
		    HSSFCell cell8row0 = rowinput1.createCell(8);
		    cell8row0.setCellValue("备注次数");
		    cell8row0.setCellStyle(cellStyleTitle);
	    }
	}
	
	public void createOperatorLogContent(int index,HSSFRow rowsmsnum,TyadminOperatorLogs info, HSSFCellStyle cellStyleTitle)
	{
		HSSFCell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell0.setCellValue(info.getOpeno());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1.setCellValue(info.getBusinessdepartment());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell2.setCellValue(info.getWarzone());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell3.setCellValue(info.getArea());
		 cell3.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell4 = rowsmsnum.createCell(4);
		 cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell4.setCellValue(info.getBranchactiongroup());
		 cell4.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell5 = rowsmsnum.createCell(5);
		 cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell5.setCellValue(info.getLastLoginTime());
		 cell5.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell6 = rowsmsnum.createCell(6);
		 cell6.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell6.setCellValue(info.getLoginCount()+"");
		 cell6.setCellStyle(cellStyleTitle);
		 
		 if(ConfigMgr.getDistChannel().equals("1"))//区分A+和渠道
		 {
			 HSSFCell cell7 = rowsmsnum.createCell(7);
			 cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell7.setCellValue(info.getCdrCountA()+"");
			 cell7.setCellStyle(cellStyleTitle);
			 
			 HSSFCell cell8 = rowsmsnum.createCell(8);
			 cell8.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell8.setCellValue(info.getCdrCountChannel()+"");
			 cell8.setCellStyle(cellStyleTitle);
			 
			 HSSFCell cell9 = rowsmsnum.createCell(9);
			 cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell9.setCellValue(info.getCdrRemarkCount()+"");
			 cell9.setCellStyle(cellStyleTitle);
			 
		 }else
		 {
			 HSSFCell cell7 = rowsmsnum.createCell(7);
			 cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell7.setCellValue(info.getCdrCountTotal()+"");
			 cell7.setCellStyle(cellStyleTitle);
			 
			 HSSFCell cell8 = rowsmsnum.createCell(8);
			 cell8.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell8.setCellValue(info.getCdrRemarkCount()+"");
			 cell8.setCellStyle(cellStyleTitle);
		 }
	}
	
	public void createOperatorLogTitle2007(int index, Row rowinput1, CellStyle cellStyleTitle)
	{	
		Cell cell0row0 = rowinput1.createCell(0);
		cell0row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell0row0.setCellValue("登录账号");
	    cell0row0.setCellStyle(cellStyleTitle);
		
	    Cell cell1row0 = rowinput1.createCell(1);
	    cell1row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell1row0.setCellValue("所属事业部");
	    cell1row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell2row0 = rowinput1.createCell(2);
	    cell2row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell2row0.setCellValue("所属战区");
	    cell2row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell3row0 = rowinput1.createCell(3);
	    cell3row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell3row0.setCellValue("所属片区");
	    cell3row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell4row0 = rowinput1.createCell(4);
	    cell4row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell4row0.setCellValue("所属行动组");
	    cell4row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell5row0 = rowinput1.createCell(5);
	    cell5row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell5row0.setCellValue("最近登录时间");
	    cell5row0.setCellStyle(cellStyleTitle);
	    
	    Cell cell6row0 = rowinput1.createCell(6);
	    cell6row0.setCellType(XSSFCell.CELL_TYPE_STRING);
	    cell6row0.setCellValue("登录次数");
	    cell6row0.setCellStyle(cellStyleTitle);
	    
	    if(ConfigMgr.getDistChannel().equals("1"))//区分A+和渠道
	    {
	    	Cell cell7row0 = rowinput1.createCell(7);
		    cell7row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell7row0.setCellValue("听录音A+次数");
		    cell7row0.setCellStyle(cellStyleTitle);
		    
		    Cell cell8row0 = rowinput1.createCell(8);
		    cell8row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell8row0.setCellValue("听录音渠道次数");
		    cell8row0.setCellStyle(cellStyleTitle);
		    
		    Cell cell9row0 = rowinput1.createCell(9);
		    cell9row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell9row0.setCellValue("备注次数");
		    cell9row0.setCellStyle(cellStyleTitle);
	    }else
	    {
		    Cell cell7row0 = rowinput1.createCell(7);
		    cell7row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell7row0.setCellValue("听录音次数");
		    cell7row0.setCellStyle(cellStyleTitle);
		    
		    Cell cell8row0 = rowinput1.createCell(8);
		    cell8row0.setCellType(XSSFCell.CELL_TYPE_STRING);
		    cell8row0.setCellValue("备注次数");
		    cell8row0.setCellStyle(cellStyleTitle);
	    }
	}
	
	public void createOperatorLogContent2007(int index,Row rowsmsnum,TyadminOperatorLogs info, CellStyle cellStyleTitle)
	{
		 Cell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell0.setCellValue(info.getOpeno());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 Cell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1.setCellValue(info.getBusinessdepartment());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 Cell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell2.setCellValue(info.getWarzone());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 Cell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell3.setCellValue(info.getArea());
		 cell3.setCellStyle(cellStyleTitle);
		 
		 Cell cell4 = rowsmsnum.createCell(4);
		 cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell4.setCellValue(info.getBranchactiongroup());
		 cell4.setCellStyle(cellStyleTitle);
		 
		 Cell cell5 = rowsmsnum.createCell(5);
		 cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell5.setCellValue(info.getLastLoginTime());
		 cell5.setCellStyle(cellStyleTitle);
		 
		 Cell cell6 = rowsmsnum.createCell(6);
		 cell6.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell6.setCellValue(info.getLoginCount()+"");
		 cell6.setCellStyle(cellStyleTitle);
		 
		 if(ConfigMgr.getDistChannel().equals("1"))//区分A+和渠道
		 {
			 Cell cell7 = rowsmsnum.createCell(7);
			 cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell7.setCellValue(info.getCdrCountA()+"");
			 cell7.setCellStyle(cellStyleTitle);
			 
			 Cell cell8 = rowsmsnum.createCell(8);
			 cell8.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell8.setCellValue(info.getCdrCountChannel()+"");
			 cell8.setCellStyle(cellStyleTitle);
			 
			 Cell cell9 = rowsmsnum.createCell(9);
			 cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell9.setCellValue(info.getCdrRemarkCount()+"");
			 cell9.setCellStyle(cellStyleTitle);
		 }else
		 {
			 Cell cell7 = rowsmsnum.createCell(7);
			 cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell7.setCellValue(info.getCdrCountTotal()+"");
			 cell7.setCellStyle(cellStyleTitle);
			 
			 Cell cell8 = rowsmsnum.createCell(8);
			 cell8.setCellType(XSSFCell.CELL_TYPE_STRING);
			 cell8.setCellValue(info.getCdrRemarkCount()+"");
			 cell8.setCellStyle(cellStyleTitle);
		 }
	}
	
	//架构信息
	public void createSSMNLevelTitle (int index,HSSFRow rowsms, HSSFCellStyle cellStyleTitle,SsmnZyLevel level)
	{
		String businName = new UtilFunctionDao().getLevelName(1, level);//事业部
		HSSFCell cell1sms0 = rowsms.createCell(0);
		 cell1sms0.setCellValue(businName);
		 cell1sms0.setCellStyle(cellStyleTitle);
		 
		 String warName = new UtilFunctionDao().getLevelName(2, level);//战区
		 HSSFCell cell1sms1 = rowsms.createCell(1);
		 cell1sms1.setCellValue(warName);
		 cell1sms1.setCellStyle(cellStyleTitle);
		 
		 String areaName = new UtilFunctionDao().getLevelName(3, level);//片区
		 HSSFCell cell1sms2 = rowsms.createCell(2);
		 cell1sms2.setCellValue(areaName);
		 cell1sms2.setCellStyle(cellStyleTitle);
		 
		 String branName = new UtilFunctionDao().getLevelName(4, level);//行动组
		 HSSFCell cell1sms3 = rowsms.createCell(3);
		 cell1sms3.setCellValue(branName);
		 cell1sms3.setCellStyle(cellStyleTitle);
		
	}
		
	public void createSSMNLevelRowContent(int index,HSSFRow rowsmsnum,SsmnZyLevel info, HSSFCellStyle cellStyleTitle)
	{
		 HSSFCell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellValue(info.getBusinessdepartment());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellValue(info.getWarzone());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellValue(info.getArea());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellValue(info.getBranchactiongroup());
		 cell3.setCellStyle(cellStyleTitle);
			
	}
	
	public void createSSMNLevelTitle2007 (int index,Row rowsms, CellStyle cellStyleTitle,SsmnZyLevel level)
	{
		String businName = new UtilFunctionDao().getLevelName(1, level);//事业部
		Cell cell1sms0 = rowsms.createCell(0);
		cell1sms0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms0.setCellValue(businName);
		 cell1sms0.setCellStyle(cellStyleTitle);
		 
		 String warName = new UtilFunctionDao().getLevelName(2, level);//战区
		 Cell cell1sms1 = rowsms.createCell(1);
		 cell1sms1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms1.setCellValue(warName);
		 cell1sms1.setCellStyle(cellStyleTitle);
		 
		 String areaName = new UtilFunctionDao().getLevelName(3, level);//片区
		 Cell cell1sms2 = rowsms.createCell(2);
		 cell1sms2.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms2.setCellValue(areaName);
		 cell1sms2.setCellStyle(cellStyleTitle);
		 
		 String branName = new UtilFunctionDao().getLevelName(4, level);//行动组
		 Cell cell1sms3 = rowsms.createCell(3);
		 cell1sms3.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms3.setCellValue(branName);
		 cell1sms3.setCellStyle(cellStyleTitle);
		 
	}
	
	public void createSSMNLevelRowContent2007(int index,Row rowsmsnum,SsmnZyLevel info, CellStyle cellStyleTitle)
	{
		 Cell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell0.setCellValue(info.getBusinessdepartment());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 Cell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1.setCellValue(info.getWarzone());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 Cell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell2.setCellValue(info.getArea());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 Cell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell3.setCellValue(info.getBranchactiongroup());
		 cell3.setCellStyle(cellStyleTitle);		 
		 
	}
	
	//黑名单信息
	public void createBlackNumTitle (int index,HSSFRow rowsms, HSSFCellStyle cellStyleTitle)
	{
		HSSFCell cell1sms0 = rowsms.createCell(0);
		 cell1sms0.setCellValue("号码");
		 cell1sms0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms1 = rowsms.createCell(1);
		 cell1sms1.setCellValue("导入时间");
		 cell1sms1.setCellStyle(cellStyleTitle);		
	}
	
	public void createBlackNumTitle2007 (int index,Row rowsms, CellStyle cellStyleTitle)
	{
		Cell cell1sms0 = rowsms.createCell(0);
		cell1sms0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms0.setCellValue("号码");
		 cell1sms0.setCellStyle(cellStyleTitle);

		 Cell cell1sms1 = rowsms.createCell(1);
		 cell1sms1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms1.setCellValue("导入时间");
		 cell1sms1.setCellStyle(cellStyleTitle);
		 
	}
	
	public void createImportClientNumTitle(int index,HSSFRow rowsms, HSSFCellStyle cellStyleTitle,boolean issuccess)
	{
		HSSFCell cell1sms0 = rowsms.createCell(0);
		 cell1sms0.setCellValue("日期");
		 cell1sms0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms1 = rowsms.createCell(1);
		 cell1sms1.setCellValue("业务员电话");
		 cell1sms1.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms2 = rowsms.createCell(2);
		 cell1sms2.setCellValue("业务员姓名");
		 cell1sms2.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms3 = rowsms.createCell(3);
		 cell1sms3.setCellValue("客户电话");
		 cell1sms3.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms4 = rowsms.createCell(4);
		 cell1sms4.setCellValue("客户姓名");
		 cell1sms4.setCellStyle(cellStyleTitle);
		 		 
		 if(!issuccess)
		 {			 
			 HSSFCell cell1sms5 = rowsms.createCell(5);
			 cell1sms5.setCellValue("失败原因");
			 cell1sms5.setCellStyle(cellStyleTitle);
		 }

	}
	
	public void createImportClientNumContent(int index,HSSFRow rowsmsnum,SsmnZyClientnum info, 
			HSSFCellStyle cellStyleTitle,boolean issuccess)
	{
		 //日期
		 HSSFCell cell0 = rowsmsnum.createCell(0);
		 if(info.getCalltime() !=null)
			 cell0.setCellValue(info.getCalltime());
		 else
			 cell0.setCellValue("");
		 cell0.setCellStyle(cellStyleTitle);
		 //业务员电话
		 HSSFCell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellValue(info.getUsermsisdn());
		 cell1.setCellStyle(cellStyleTitle);
		 //业务员姓名
		 HSSFCell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellValue(info.getUsername());
		 cell2.setCellStyle(cellStyleTitle);
		 //客户电话
		 HSSFCell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellValue(info.getClientnum());
		 cell3.setCellStyle(cellStyleTitle);
		 //客户姓名
		 HSSFCell cell4 = rowsmsnum.createCell(4);
		 cell4.setCellValue(info.getClientname());
		 cell4.setCellStyle(cellStyleTitle);
		 
		 if(!issuccess)
		 {			 
			 //错误信息
			 HSSFCell cell5 = rowsmsnum.createCell(5);
			 cell5.setCellValue(info.getFailedInfo());
			 cell5.setCellStyle(cellStyleTitle);
		 }
		
	}
	
	public void createBlackNumTRowContent(int index,HSSFRow rowsmsnum,Blackcallnumber info, HSSFCellStyle cellStyleTitle)
	{
		 HSSFCell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellValue(info.getBlackNumber());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 
		 HSSFCell cell1 = rowsmsnum.createCell(1);
		 if(info.getIntime() !=null)
			 cell1.setCellValue(info.getIntime().toLocaleString());
		 else
			 cell1.setCellValue("");
		 cell1.setCellStyle(cellStyleTitle);			
	}
	
	public void createBlackNumTRowContent2007(int index,Row rowsmsnum,Blackcallnumber info, CellStyle cellStyleTitle)
	{
		 Cell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell0.setCellValue(info.getBlackNumber());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 Cell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 if(info.getIntime() !=null)
			 cell1.setCellValue(info.getIntime().toLocaleString());
		 else
			 cell1.setCellValue("");
		 cell1.setCellStyle(cellStyleTitle);
	}
	
	//呼叫信息
	public void createCallInfoTitle (int index,HSSFRow rowsms, HSSFCellStyle cellStyleTitle)
	{
		HSSFCell cell1sms0 = rowsms.createCell(0);
		 cell1sms0.setCellValue("序号");
		 cell1sms0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms1 = rowsms.createCell(1);
		 cell1sms1.setCellValue("业务员电话");
		 cell1sms1.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms2 = rowsms.createCell(2);
		 cell1sms2.setCellValue("业务员姓名");
		 cell1sms2.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms3 = rowsms.createCell(3);
		 cell1sms3.setCellValue("客户电话");
		 cell1sms3.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms4 = rowsms.createCell(4);
		 cell1sms4.setCellValue("客户姓名");
		 cell1sms4.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms5 = rowsms.createCell(5);
		 cell1sms5.setCellValue("状态");
		 cell1sms5.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms6 = rowsms.createCell(6);
		 cell1sms6.setCellValue("备注");
		 cell1sms6.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1sms7 = rowsms.createCell(7);
		 cell1sms7.setCellValue("呼叫日期");
		 cell1sms7.setCellStyle(cellStyleTitle);
	}
	
	public void createCallInfoContent(int index,HSSFRow rowsmsnum,SsmnZyClientnum info, HSSFCellStyle cellStyleTitle)
	{
		 HSSFCell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellValue(info.getId());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellValue(info.getUsermsisdn());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellValue(info.getUsername());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellValue(info.getClientnum());
		 cell3.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell4 = rowsmsnum.createCell(4);
		 cell4.setCellValue(info.getClientname());
		 cell4.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell5 = rowsmsnum.createCell(5);
		 if(info.getCallstatus())
			 cell5.setCellValue("已拨");
		 else
			 cell5.setCellValue("未拨");
		 cell5.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell6 = rowsmsnum.createCell(6);
		 cell6.setCellValue(info.getRemark());
		 cell6.setCellStyle(cellStyleTitle);
		 
		 HSSFCell cell7 = rowsmsnum.createCell(7);
		 if(info.getCalltime() !=null)
			 cell7.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(info.getCalltime()));
		 else
			 cell7.setCellValue("");
		 cell7.setCellStyle(cellStyleTitle);			
	}
	
	public void createCallInfoTitle2007 (int index,Row rowsms, CellStyle cellStyleTitle)
	{
		Cell cell1sms0 = rowsms.createCell(0);
		cell1sms0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms0.setCellValue("序号");
		 cell1sms0.setCellStyle(cellStyleTitle);

		 Cell cell1sms1 = rowsms.createCell(1);
		 cell1sms1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms1.setCellValue("业务员电话");
		 cell1sms1.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms2 = rowsms.createCell(2);
		 cell1sms2.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms2.setCellValue("业务员姓名");
		 cell1sms2.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms3 = rowsms.createCell(3);
		 cell1sms3.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms3.setCellValue("客户电话");
		 cell1sms3.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms4 = rowsms.createCell(4);
		 cell1sms4.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms4.setCellValue("客户姓名");
		 cell1sms4.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms5 = rowsms.createCell(5);
		 cell1sms5.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms5.setCellValue("状态");
		 cell1sms5.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms6 = rowsms.createCell(6);
		 cell1sms6.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms6.setCellValue("备注");
		 cell1sms6.setCellStyle(cellStyleTitle);
		 
		 Cell cell1sms7 = rowsms.createCell(7);
		 cell1sms7.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1sms7.setCellValue("呼叫日期");
		 cell1sms7.setCellStyle(cellStyleTitle);
		 
	}
	
	public void createCallInfoContent2007(int index,Row rowsmsnum,SsmnZyClientnum info, CellStyle cellStyleTitle)
	{
		 Cell cell0 = rowsmsnum.createCell(0);
		 cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell0.setCellValue(info.getId());
		 cell0.setCellStyle(cellStyleTitle);
		 
		 Cell cell1 = rowsmsnum.createCell(1);
		 cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell1.setCellValue(info.getUsermsisdn());
		 cell1.setCellStyle(cellStyleTitle);
		 
		 Cell cell2 = rowsmsnum.createCell(2);
		 cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell2.setCellValue(info.getUsername());
		 cell2.setCellStyle(cellStyleTitle);
		 
		 Cell cell3 = rowsmsnum.createCell(3);
		 cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell3.setCellValue(info.getClientnum());
		 cell3.setCellStyle(cellStyleTitle);
		 
		 Cell cell4 = rowsmsnum.createCell(4);
		 cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell4.setCellValue(info.getClientname());
		 cell4.setCellStyle(cellStyleTitle);
		 
		 Cell cell5 = rowsmsnum.createCell(5);
		 cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
		 if(info.getCallstatus())
			 cell5.setCellValue("已拨");
		 else 
			 cell5.setCellValue("未拨");
		 cell5.setCellStyle(cellStyleTitle);
		 
		 Cell cell6 = rowsmsnum.createCell(6);
		 cell6.setCellType(XSSFCell.CELL_TYPE_STRING);
		 cell6.setCellValue(info.getRemark());
		 cell6.setCellStyle(cellStyleTitle);
		 
		 Cell cell7 = rowsmsnum.createCell(7);
		 cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
		 if(info.getCalltime() !=null)
			 cell7.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(info.getCalltime()));
		 else
			 cell7.setCellValue("");
		 cell1.setCellStyle(cellStyleTitle);
	}
	
	//获取呼入呼出类型，其实能简化成一个，但这样比较好理解
	public String getDcCallType(String type)
	{
		String res = "";
		//这里设置一下默认值,防止从下面配置中取不到值,
		if(type == "1")
			res ="业主呼入";
		if(type == "2")
			res ="客户呼入";
		if(type == "3")
			res ="PC呼出";
		if(type == "4")
			res ="APP呼出";
		if(type == "5")
			res ="直接呼出";
		
		//从配置中取
		if(new ConfigMgr().getDcCalltypeMap() !=null && null != new ConfigMgr().getDcCalltypeMap().get(type) && new ConfigMgr().getDcCalltypeMap().get(type).length()>0)
			res = new ConfigMgr().getDcCalltypeMap().get(type);
		return res;
	}
		
	//定义死了：　3,4,5是三种呼出类型
	public String getCallOutTypes()
	{
		String sres="";
		if(new ConfigMgr().getDcCalltypeMap() !=null && null != new ConfigMgr().getDcCalltypeMap().get("3") && new ConfigMgr().getDcCalltypeMap().get("3").length()>0)
			sres +=new ConfigMgr().getDcCalltypeMap().get("3")+"|";
		if(new ConfigMgr().getDcCalltypeMap() !=null && null != new ConfigMgr().getDcCalltypeMap().get("4") && new ConfigMgr().getDcCalltypeMap().get("4").length()>0)
			sres +=new ConfigMgr().getDcCalltypeMap().get("4")+"|";
		if(new ConfigMgr().getDcCalltypeMap() !=null && null != new ConfigMgr().getDcCalltypeMap().get("5") && new ConfigMgr().getDcCalltypeMap().get("5").length()>0)
			sres +=new ConfigMgr().getDcCalltypeMap().get("5");
		return sres;
	}
	
	//根据省和公司，查出事业部，战区，片区，行动组配置的显示名称
	//type: 1:事业部 2:战区 3:片区 4:行动组
	public String getLevelName(int type,SsmnZyLevel opera)
	{
		String res="";
		if(opera == null )
			return res;
		if(ConfigMgr.getLevelShow() !=null)
		{
			List<String> lens = ConfigMgr.getLevelShow().get(opera.getProvincecity()+opera.getCompany());
			if(lens !=null && lens.size()>0)
			{
				res = lens.get(type-1);
			}			
		}
		if(res ==null || res.length() <=0)
		{
			if(type == 1)
				res = "事业部";
			if(type ==2)
				res = "战区";
			if(type ==3)
				res = "片区";
			if(type == 4)
				res = "行动组";
		}
		return res;
	}
	
	/*根据渠道的type，决定所选择副号码的type*/
	public void setEnablenumberType(long channelId)
	{
		SsmnZyChannel zc =cDao.getChannelById(channelId);
		if(zc !=null && zc.getType().longValue() == 1) //A+渠道，应该选择A+副号码类型2
			Constants.TYPE_ENABLENUMBER = 2;
		else
			Constants.TYPE_ENABLENUMBER = 0;
	}
	
	/*
	 * /取出呼叫类型值 1:业主呼入 2:客户呼入 3:PC呼出 4:APP呼出 5:直接呼出
	 */
	public String getCalltypeName(int dccalltype)
	{
		String res="";
		if(dccalltype == 1)
			res = "业主呼入";
		if(dccalltype == 2)
			res = "客户呼入";
		if(dccalltype == 3)
			res = "PC呼出";
		if(dccalltype == 4)
			res = "APP呼出";
		if(dccalltype == 5)
			res = "直接呼出";	
		return res;
	}
	
	/*
	 * 将通话时长转换成时分秒格式
	 */
	public static String getsecondSimple(int duration)
	{
		 int caldr = duration;
    	 int N = caldr/3600;
    	 caldr = caldr%3600;
     	 int K = caldr/60;
     	 caldr = caldr%60;
     	 int M = caldr;
     	 String scaldr="";
     	 if(N>=10)
   		   scaldr +=N;
     	 else
      	{
   	 	  scaldr +="0";
   	 	  scaldr +=N;
     	 }
      scaldr +=":";
      if(K>=10)
   	   scaldr +=K;
      else
      {
   	   scaldr +="0";
   	   scaldr +=K;
      }
      scaldr +=":";
      if(M>=10)
   	   scaldr +=M;
      else
      {
   	   scaldr +="0";
   	   scaldr +=M;
      }
      return scaldr;	 
	}
	
	/*
	 * 根据月份,获取一个月的第一天和最后一天
	 * firstOrLast: 0:一个月的第一天
	 * 		 1:一个月的最后一天
	 * sDate没给值,判断type
	 * 
	 * type:0:不起作用
	 * 		1:上个月(sDate为空,type为1)
	 * 	
	 */
	public String getFirstLastDay(String sDate,int firstOrLast,int type)
	{
		String res ="";
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat fmr = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Calendar c = Calendar.getInstance();
			
			Date d =null;
			if(sDate !=null && sDate.length()>0 && sDate.length()<8)
			{
				d= fm.parse(sDate);
				c.setTime(d);
			}
			else if(sDate !=null && sDate.length()>0 && sDate.length()>8)
			{
				d =fmr.parse(sDate);
				c.setTime(d);
			}
			else
			{
				//sDate没给值
				if(type ==1)//上个月
				{
					d = new Date();
					c.setTime(d);
					c.add(Calendar.MONTH, -1);
				}
			}
			
			if(firstOrLast == 0)
				c.set(c.DAY_OF_MONTH,1);//第一天
			else
				c.set(c.DAY_OF_MONTH,c.getActualMaximum(Calendar.DAY_OF_MONTH));//最后一天
			res =fmr.format(c.getTime());
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	//获取昨天的日期
	public String getYesterday()
	{
		String res = "";
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, -1);
		res =fm.format(c.getTime());
		return res;
	}
	/**
	 * 获取上周,周一和周日
	 * type :0 上周一
	 * 	     1:上周日
	 */	
	public String getLastWeek(int type)
	{
		String res ="";
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -1);
		if(type == 0)
			c.add(Calendar.DAY_OF_WEEK, -6);//上周一
		else
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//上周日
		res =fm.format(c.getTime());
		return res;
	}
	
	/*
	 * 分割截取字符串
	 * 
	 */
	public static String splitStr(String sname,String str)
	{
		String res ="";
		if(sname !=null && sname.length()>0 )
		{
			int indexNum =sname.indexOf(str);
			if(indexNum > 0)
				res = sname.substring(indexNum+1);
			else
				res =sname;
		}
		return res;
	}
	
	public static void showType(List<String> list)
	{
		//默认 2 显示全部,同ssmn_zy_channel表中的type值对应,0:只显示渠道，1：只显示A+  
		if(list ==null || list.size() <=0)
			Constants.TYPE_SHOWDATE =2;
		boolean showA =true;
		boolean showC =true;
		if(list.contains("showAChannel"))
			showA =true;
		else
			showA = false;
		if(list.contains("showChannel"))
			showC =true;
		else
			showC =false;
		if(showA ==true && showC==false)
			Constants.TYPE_SHOWDATE =1;//只显示A+数据
		if(showA ==false && showC ==true)
			Constants.TYPE_SHOWDATE =0;//只显示渠道数据
		if(showA ==true && showC == true)//显示所有的
			Constants.TYPE_SHOWDATE =2;
			
	}
	
	
}


