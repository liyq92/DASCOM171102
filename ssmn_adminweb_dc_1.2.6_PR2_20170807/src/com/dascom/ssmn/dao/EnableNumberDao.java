package com.dascom.ssmn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dascom.OPadmin.dao.impl.RootDao;
import com.dascom.OPadmin.util.DBException;
import com.dascom.OPadmin.util.HibernateSessionFactory;
import com.dascom.ssmn.entity.SsmnZYEnableNumberRecord;
import com.dascom.ssmn.entity.SsmnZyEnablenumber;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.init.ConfigMgr;
import com.dascom.ssmn.util.Constants;

public class EnableNumberDao extends RootDao{
	private static final Logger logger =  Logger.getLogger(EnableNumberDao.class);
	private static EnableNumberDao dao;
	private static String sfilename = "";
	
	public synchronized static EnableNumberDao getInstance(){
		if(dao == null)
			dao = new EnableNumberDao();
		return dao;
	}
	
	
	@Override
	public Class<SsmnZyEnablenumber> getReferenceClass() {
		return SsmnZyEnablenumber.class;
	}
	
	/*
	 * 随机获取一个可用的副号码，如果在配置的级别中找不到，则去公司级别中找，如果找不到，说明没有可用的副号码
	 * 参数说明：uid:用户的id 对应(ssmn_zy_user)表中的id,给已经存在的用户绑定副号码的时候，知道uid
	 * 
	 * 此方法多个地方用到
	 *ssmnnumber:用于判断已知的副号码是否有效
	 *
	 *branchactiongroup:建经纪人的时候，知道行动组名
	 *
	 *type_enablenumber:根据所选渠道匹配副号码类型
	 *       
	 */
	public String getEnableNumber(String uid,String ssmnnumber,String branchactiongroup,int type_enablenumber)
	{
		Session session = null;
		String ret = "";
		String sfieldname = "";//字段名
		String sql = " select  ";
		String sqll ="" ;
		String tem = "";
		String temWhere = "";
		String tempend = "";
		String smatchnum = ConfigMgr.getMatchNumberLevel();//获取到配置文件配置的分配副号码的级别
		try
		{
		/*
		 * 根据ssmn_zy_user表中的id查出对应的片区名称
		 */
		session = this.adapter.openSession();
		if(uid != null && !"".equals(uid) && uid.length()>0)
			tem = "  from SsmnZyUser z, SsmnZyLevel l  where z.levelid = l.id ";
		else if(branchactiongroup != null && !"".equals(branchactiongroup) && branchactiongroup.length()>0)
			tem = " from  SsmnZyLevel l where 1=1 ";
		
		sqll = "  select e.enablenumber from  SsmnZyEnablenumber e,SsmnZyLevel l where e.levelid=l.id and e.status=0 " +
				" and e.type="+type_enablenumber +" and ";
				
		if(uid != null && uid.length()>0)
		{
			temWhere = " and z.id =:uid ";
		}
		if(branchactiongroup != null && !"".equals(branchactiongroup) && branchactiongroup.length()>0)
		{
			temWhere ="  and l.branchactiongroup= :branchactiongroup0   ";
		}
		if(!"".equals(ssmnnumber) && ssmnnumber.length()>0)//如果副号码有值
			tempend =" and e.enablenumber= :ssmnnumber  ";
		else
			tempend =" and rownum = 1 ";
		
	if(smatchnum !=null || !smatchnum.equals(""))
	{
		int imatchnum = Integer.parseInt(smatchnum);
		if(imatchnum >=3)
		{
				while(imatchnum >=3)//3是servicename.prop配置文件中的级别，如果配置的级别没有可用的副号码，依次找到3事业部级
				{
					sfieldname =getFileldNameByLevel(imatchnum+"");
					if(sfieldname.length() <= 0)
						return ret="";
					
					//先根据配置级别查，如果有，则返回，如果没有，则依次往上查，到事业部			
					String sqlre = sqll+sfieldname+" = ("+sql+ sfieldname+tem+temWhere+
					" ) and l.provincecity= (select l.provincecity "+tem+temWhere+
					"  ) and l.company =(select l.company "+tem+temWhere+" )"+tempend;
				
					ret = sqlResult(sqlre,uid,ssmnnumber,branchactiongroup);
					if(ret != null && ret.length()>0)
						return ret;//根据配置的级别　查到要用的副号码了
					imatchnum --;
		
				}
		}
		else if(smatchnum.equals("1") || smatchnum.equals("2"))
		{
			sfieldname =getFileldNameByLevel(imatchnum+"");
			if(sfieldname.length() <= 0)
				return ret="";
			
			//先根据配置级别查，如果有，则返回，如果没有，则依次往上查，到事业部			
			String sqlre = sqll+sfieldname+" = ("+sql+ sfieldname+tem+temWhere+" ) "+tempend;
		
			ret = sqlResult(sqlre,uid,ssmnnumber,branchactiongroup);
			if(ret != null && ret.length()>0)
				return ret;//根据配置的级别　查到要用的副号码了
		}

	}else
		return "请添加级别的配置项!";


		if(!smatchnum.equals("2"))//上面的级别都没有可用副号码，如果是公司级别的，则不做，如果不是，则断续在公司级别查询
		{
			//从公司级别查
			StringBuffer buf = new StringBuffer(sqll.toString());
			
			buf.append(" l.provincecity =( select  l.provincecity ").append(tem);
			if(uid != null && uid.length()>0)
			{
				buf.append(" and z.id =").append(uid).append(" ) ");
				
			}
			if(branchactiongroup != null && !"".equals(branchactiongroup) && branchactiongroup.length()>0)
			{
				buf.append("  and l.branchactiongroup= :branchactiongroup   ) ");
			}
			
			if(!smatchnum.equals("1"))//如果配置的级别不为２，也不为１，为１，则不写这个条件//此条件基本走不到，因为号码是以公司分配的
			{
				buf.append(" and l.company=(select l.company  ").append(tem);
				
				if(uid != null && uid.length()>0)
				{
					buf.append(" and z.id =").append(uid).append(" ) ");
				}
				if(branchactiongroup != null && !"".equals(branchactiongroup) && branchactiongroup.length()>0)
				{
					buf.append("  and l.branchactiongroup= :branchactiongroup2  ) ");
				}
			}else
			{
				buf.append("  and  l.company is null  ");
			}
			
			buf.append("  and l.businessdepartment is null and l.warzone is null and l.area is null and l.branchactiongroup is null ");
			
			if(!"".equals(ssmnnumber) && ssmnnumber.length()>0)//如果副号码有值
				buf.append(" and e.enablenumber= :ssmnnumber  ");
			else
				buf.append("   and  rownum = 1 ");
			
			Query querycom = session.createQuery(buf.toString());
			if(!"".equals(ssmnnumber) && ssmnnumber.length()>0)
			{
				querycom.setString("ssmnnumber", ssmnnumber);
			}
			if(branchactiongroup != null && !"".equals(branchactiongroup) && branchactiongroup.length()>0)
			{
				querycom.setString("branchactiongroup", branchactiongroup);
			}
			if(!smatchnum.equals("1") && branchactiongroup != null && !"".equals(branchactiongroup) && branchactiongroup.length()>0)
			{
				querycom.setString("branchactiongroup2", branchactiongroup);
			}

			ret = (String)querycom.uniqueResult();
			
		}
		
		} catch (Exception he) {
			logger.error("-------------getEnableNumber---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getEnableNumber---关闭Session时出现HibernateException-----",he2);
			}
		}
		return ret;
	}
	
	public String sqlResult(String sqlre,String uid,String ssmnnumber,String branchactiongroup)
	{
		Session session = null;
		String ret = "";
		try
		{
			session = this.adapter.openSession();
			Query query = session.createQuery(sqlre.toString());
			if(uid != null && uid.length()>0)
			{
				query.setString("uid", uid);
			}
			if(!"".equals(ssmnnumber) && ssmnnumber.length()>0)
			{
				query.setString("ssmnnumber", ssmnnumber);
			}
			if(branchactiongroup != null && !"".equals(branchactiongroup) && branchactiongroup.length()>0)
			{
				query.setString("branchactiongroup0", branchactiongroup);
			}
						
			ret = (String)query.uniqueResult();
			if(ret != null && ret.length()>0)
				return ret;//根据配置的级别　查到要用的副号码了	
			
		} catch (Exception he) {
			logger.error("-------------getEnableNumber---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getEnableNumber---关闭Session时出现HibernateException-----",he2);
			}
		}
		return ret;
	}
	
	/*
	 * 根据配置的级别，返回字段名称
	 */
	public String getFileldNameByLevel(String smatchnum)
	{
		String sfieldname = "";
		//String smatchnum = ConfigMgr.getMatchNumberLevel();//获取到配置文件配置的分配副号码的级别
		if(smatchnum != null && smatchnum.equals("1"))//1:省市
		{
				sfieldname = " l.provincecity ";
				sfilename = "省市";
		}
		if(smatchnum != null && smatchnum.equals("2"))//2:公司
		{
				sfieldname = " l.company ";
				sfilename = "公司";
		}
		if(smatchnum != null  && smatchnum.equals("3"))//3:事业部
		{
				sfieldname = " l.businessdepartment ";
				sfilename = "事业部";
		}
		if(smatchnum != null  && smatchnum.equals("4"))//4:战区
		{
				sfieldname = " l.warzone ";
				sfilename = "战区";
		}
		if(smatchnum != null  && smatchnum.equals("5"))//5:片区
		{
				sfieldname = " l.area ";
				sfilename = "片区";
		}
		if(smatchnum != null  && smatchnum.equals("6"))//6:行动组
		{
				sfieldname = " l.branchactiongroup ";
				sfilename = "行动组";
		}
		return sfieldname;
	}
	
	/*
	 * 绑定、换绑时判断，副号码是否是有效的（包括：是否是用户所在区域的，是否是未使用的副号码等）
	 * channelId ：根据所选择的渠道的类型(ssmn_zy_channel表中的type值)，选择副号码的类型: 渠道type为0:副号码类型0 ,渠道type为1:副号码类型2
	 */
	public String isEnableAreaSsmnnumber(String snumber,String uid,String branchactiongroup,int type_enablenumber)
	{
		Session session = null;
		String ret = "";
		
		session = this.adapter.openSession();
		//判断副号码是否是可用状态
		String sqlEnab = " select t.status from SsmnZyEnablenumber t where t.enablenumber='"+snumber+"' and t.type= "+type_enablenumber;
		try
		{
			Query queryEnab = session.createQuery(sqlEnab);
			if(queryEnab.uniqueResult() == null)
				return ret = "副号码不可用,副号码池中不存在该副号码或者副号码的类型与所选渠道不符!";
			int stua = ((Number)queryEnab.uniqueResult()).intValue();
			if(stua == 1)
				return ret = "副号码不可用!";
			
		
			//判断副号码是否在本公司内			
			//再判断副号码是否在用户所在区域,如片区
			String sqlOk = getEnableNumber(uid,snumber,branchactiongroup,type_enablenumber);
			
			if((sqlOk == null || sqlOk.equals("") ) && !"公司".equals(sfilename))
				ret = "该副号码不属于本"+sfilename+"!";
			if((sqlOk == null || sqlOk.equals("") ) && "公司".equals(sfilename))
				ret = "副号码不属于本公司!";
		} catch (Exception he) {
			logger.error("-------------getZyUserByMsisdn---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getZyUserByMsisdn---关闭Session时出现HibernateException-----",he2);
			}
		}
		return ret;
	}
/*
 * 查询所有的副号码,hibernate query分页偶尔会有问题改用sql
 */	
	public List<SsmnZYEnableNumberRecord> getAllzyenablenymberlist(Integer index, Integer maxCount, Long operalevelid, 
			String ssmnnum, String status, String bdparam, String zoneparam, String areaparam, String bagparam,String enmtype,int isImport)
	{
		List<SsmnZYEnableNumberRecord> erlist = new ArrayList<SsmnZYEnableNumberRecord>();
		Session session = HibernateSessionFactory.getSession();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
		try
		{
			conn = session.connection();
			SsmnZyLevel level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(operalevelid);
			
			session = this.adapter.openSession();
			StringBuffer sql = new StringBuffer("select distinct(e.enablenumber) as en,e.status," +
					"l.provincecity,l.company,l.businessdepartment,l.warzone,l.area,l.branchactiongroup,e.type" +
					" from Ssmn_Zy_Enablenumber e,Ssmn_Zy_Level l where e.levelid=l.id  ");
			
			if(ssmnnum != null && !"".equals(ssmnnum.trim())) {
				sql.append(" and e.enablenumber ='").append(ssmnnum).append("'  ");

			}
			if(status != null && !"".equals(status.trim())) {
				sql.append(" and e.status = ").append(status);
			}else
				sql.append(" and e.status in(0,1 ) " ) ;
			if(bdparam != null && !"".equals(bdparam.trim())) {
				sql.append(" and l.businessdepartment = '").append(bdparam).append("' ");
			}
			if(zoneparam != null && !"".equals(zoneparam.trim())) {
				sql.append(" and l.warzone ='").append(zoneparam).append("'  ");
			}
			if(areaparam != null && !"".equals(areaparam.trim())) {
				sql.append(" and l.area ='").append(areaparam).append("'  ");
			}
			if(bagparam != null && !"".equals(bagparam.trim())) {
				sql.append(" and l.branchactiongroup ='").append(bagparam).append("'  ");
			}
			if(enmtype !=null && !"".equals(enmtype))
			{
				sql.append(" and e.type =").append(enmtype);
			}else
			{
				//根据配置的权限查询
				if(Constants.TYPE_SHOWDATE ==0 )//只显示渠道类型
					sql.append(" and e.type =0 ");
				if(Constants.TYPE_SHOWDATE ==1)//只显示A+类型
					sql.append(" and e.type =2 ");
			}
			sql.append(" and l.provincecity ='").append(level.getProvincecity()).append("'  ");//把省市范围内的副号码
			sql.append(" and l.company ='").append(level.getCompany()).append("'  ");//公司

			sql.append(" order by e.status desc,e.type ,l.businessdepartment, l.warzone, l.area, l.branchactiongroup ");
			String sqlTemp = "";
			if(isImport == 0)//分页显示
			{
				 sqlTemp = "  select   p.en, p.status,p.provincecity,p.company,p.businessdepartment,p.warzone,p.area,p.branchactiongroup,p.type,rn from ( "+
								 " select q.en, q.status,q.provincecity,q.company,q.businessdepartment,q.warzone,q.area,q.branchactiongroup,q.type,rownum as rn "+
								 " from (" +sql +" )q )p  ";
				if(index >=0)
				{
					sqlTemp +="where p.rn >= ";
					sqlTemp += index+1;
					sqlTemp += "  and p.rn <= ";
					sqlTemp += index+10;
				}
			}
			if(isImport == 1)//导出副号码，要显示全部
			{
				sqlTemp =sql.toString();	
			}

			pstmt = conn.prepareStatement(sqlTemp.toString());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next())
				{
					erlist.add(new SsmnZYEnableNumberRecord(rs.getString("en"),rs.getLong("status"),rs.getString("provincecity"),rs.getString("company"),rs.getString("businessdepartment"),
							rs.getString("warzone"),rs.getString("area"),rs.getString("branchactiongroup"),rs.getLong("type")));
				}
			}

		} catch (Exception he) {
			logger.error("-------------getAllzyenablenymberlist---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getAllzyenablenymberlist---关闭Session时出现HibernateException-----",he2);
			}
		}
		return erlist;
		
	}
	
	public int getAllzyenablenymberCount(Long operalevelid, String ssmnnum, String status, 
			String bdparam, String zoneparam, String areaparam, String bagparam,String enmtype)
	{
		int icount = 0;
		Session session = null;
		ScrollableResults srs = null;
		try
		{
			session = this.adapter.openSession();
			StringBuffer sql = new StringBuffer("select distinct(e.enablenumber)" +
					" from SsmnZyEnablenumber e,SsmnZyLevel l where e.levelid=l.id  ") ;
			if(ssmnnum != null && !"".equals(ssmnnum.trim())) {

				sql.append(" and e.enablenumber =:name");
			}
			if(status != null && !"".equals(status.trim())) {
				sql.append(" and e.status =:status");
			}else
				sql.append(" and e.status in(0,1 ) ");
			if(bdparam != null && !"".equals(bdparam.trim())) {
				sql.append(" and l.businessdepartment =:bdparam");
			}
			if(zoneparam != null && !"".equals(zoneparam.trim())) {
				sql.append(" and l.warzone =:zoneparam");
			}
			if(areaparam != null && !"".equals(areaparam.trim())) {
				sql.append(" and l.area =:areaparam");
			}
			if(bagparam != null && !"".equals(bagparam.trim())) {
				sql.append(" and l.branchactiongroup =:bagparam");
			}
			if(enmtype !=null && !"".equals(enmtype)){
				sql.append(" and e.type = :enmtype ");
			}else
			{
				//根据配置的权限查询
				if(Constants.TYPE_SHOWDATE ==0 )//只显示渠道类型
					sql.append(" and e.type =0 ");
				if(Constants.TYPE_SHOWDATE ==1)//只显示A+类型
					sql.append(" and e.type =2 ");
			}
			SsmnZyLevel level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(operalevelid);
			sql.append(" and l.provincecity =:provin");//把省市范围内的副号码
			sql.append(" and l.company =:compan  ");//公司
			
			Query query = session.createQuery(sql.toString());
			if(ssmnnum != null && !"".equals(ssmnnum.trim())) {
				query.setString("name", ssmnnum.trim());
			}
			if(status != null && !"".equals(status.trim())) {
				query.setLong("status", Long.parseLong(status.trim()));
			}
			if(bdparam != null && !"".equals(bdparam.trim())) {
				query.setString("bdparam", bdparam.trim());
			}
			if(zoneparam != null && !"".equals(zoneparam.trim())) {
				query.setString("zoneparam", zoneparam.trim());
			}
			if(areaparam != null && !"".equals(areaparam.trim())) {
				query.setString("areaparam", areaparam.trim());
			}
			if(bagparam != null && !"".equals(bagparam.trim())) {
				query.setString("bagparam", bagparam.trim());
			}
			if(enmtype !=null && !"".equals(enmtype)){
				query.setString("enmtype", enmtype);
			}
			query.setString("provin", level.getProvincecity());
			query.setString("compan", level.getCompany());

			icount=query.list().size();
			
		} catch (Exception he) {
			logger.error("-------------getAllzyenablenymberCount---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getAllzyenablenymberCount---关闭Session时出现HibernateException-----",he2);
			}
		}
		return icount;
		
	}
	
	/**
	 * 更新副号码等级
	 * @param msisdn
	 * @param name
	 * @param groupname
	 * @param levelid
	 * @return
	 */
	public int editSsmnLevel(String enablenumber, Long levelid,String numtype){
		Session session = null;
        int ret = 0;
        Transaction trans = null;
        try {
        	session = this.adapter.openSession();
        	trans=session.beginTransaction();
        	String hql="update SsmnZyEnablenumber set levelid=:levelid ,type=:type where enablenumber=:enablenumber";
        	Query queryupdate=session.createQuery(hql);
        	queryupdate.setLong("levelid", levelid);
        	queryupdate.setString("type", numtype);
        	queryupdate.setString("enablenumber",enablenumber);
        	ret=queryupdate.executeUpdate();
        	trans.commit();
        	return ret;
		} catch (Exception e) {
			logger.error("-------------editSsmnLevel---出现Exception-----",e);
			trans.rollback();
		} finally{
			if(session!=null){
				session.close();
			}
		}
		
		return ret;
	}
	
	public int isValidSsmnNumber(String enablenumber){
		Session session = null;
        int ret = 0;
        Transaction trans = null;
        
        try {
        	session = this.adapter.openSession();
        	trans=session.beginTransaction();
        	String hql="select status  from  SsmnZyEnablenumber  where enablenumber=:enablenumber";
        	Query queryupdate=session.createQuery(hql);
        	queryupdate.setString("enablenumber", enablenumber);
        	int stua = ((Number)queryupdate.uniqueResult()).intValue();
        	
        	return ret;
		} catch (Exception e) {
			logger.error("-------------editSsmnLevel---出现Exception-----",e);
			trans.rollback();
		} finally{
			if(session!=null){
				session.close();
			}
		}
		
		return ret;
	}
	
	public	boolean getSsmnNumberByLevelid(String mbusin,String mwarzone,String marea,String mbranch,Long operalevelid)
	{
		Session session = null;
		List<SsmnZyEnablenumber> list = null;
		try {
			
			session = this.adapter.openSession();
			String sql = "select e.id from SsmnZyEnablenumber e,SsmnZyLevel l where e.levelid=l.id ";
			if(operalevelid >0)//保证是登录的操作员公司下面的
				sql +=" and l.provincecity=(select provincecity from SsmnZyLevel where id= :operalevelid1)  and l.company =( select company from SsmnZyLevel where id= :operalevelid2 ) ";
			if(mbranch !=null && !"".equals(mbranch) && mbranch.length()>0)
				sql += " and l.branchactiongroup =:mbranch";//组别不为空,按组别查
			if(marea !=null && !"".equals(marea) && marea.length()>0)
				sql +=" and l.area =:marea";//片区不为空,按片区查
			if(mwarzone !=null && !"".equals(mwarzone) && mwarzone.length()>0)
				sql +=" and l.warzone =:mwarzone";//战区不为空,按战区查
			if(mbusin !=null && !"".equals(mbusin) && mbusin.length()>0)
				sql +=" and l.businessdepartment =:mbusin";//事业部不为空,按事业部查
			
			Query query = session.createQuery(sql);
			if(operalevelid >0)
			{
				query.setLong("operalevelid1", operalevelid);
				query.setLong("operalevelid2", operalevelid);
			}
			if(mbranch !=null && !"".equals(mbranch) && mbranch.length()>0)
				query.setString("mbranch",mbranch );
			if(marea !=null && !"".equals(marea) && marea.length()>0)
				query.setString("marea", marea);
			if(mwarzone !=null && !"".equals(mwarzone) && mwarzone.length()>0)
				query.setString("mwarzone",mwarzone );
			if(mbusin !=null && !"".equals(mbusin) && mbusin.length()>0)
				query.setString("mbusin",mbusin );

			list = query.list();
		
		} catch (Exception he) {
			logger.error("-------------getSsmnNumberByLevelid---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getSsmnNumberByLevelid---关闭Session时出现HibernateException-----",he2);
			}
		}	
		if(list !=null && list.size()>0)
			return true;
		else
			return false;

	}
	
	public	List<SsmnZyEnablenumber> getSsmnEnableNumber(String num)
	{
		Session session = null;
		List<SsmnZyEnablenumber> list = null;
		try {
			
			session = this.adapter.openSession();
			String sql = " from SsmnZyEnablenumber e where e.enablenumber= '"+num+"'  ";

			Query query = session.createQuery(sql);

			list = query.list();
		
		} catch (Exception he) {
			logger.error("-------------getSsmnEnableNumber---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getSsmnEnableNumber---关闭Session时出现HibernateException-----",he2);
			}
		}	

		return list;

	}
	
	public List<Long> getSsmnnumberAllType(){
		Session session = null;
		StringBuffer strHQL = new StringBuffer("");
		List<Long> list = new ArrayList<Long>();
		try {
			strHQL.append(" select distinct(type) from SsmnZyEnablenumber order by type ");
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			list = query.list();
			
		} catch (Exception he) {
			logger.error("-------------getSsmnnumberAllType---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getSsmnnumberAllType---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
	}
		
	public static void main(String[] args) {
//		LevelDao dao = LevelDao.getInstance();
//		try {
//			SsmnZyLevel l = (SsmnZyLevel) dao.getByPrimaryKey(1000000096L );
//			if(l != null){
//				logger.info("====l.war===="+l.getWarzone());
//			}else{
//				logger.info("====null====" );
//			}
//		} catch (DaoException e) {
//			e.printStackTrace();
//		}
//		List<String> list = dao.getLevelByTypeAndID(1, 1000000092L);
//		List<String> list = dao.getSelectLevel(3, 1000000092L, "南区事业部", "河西人民公园3战区", "2片区");
//		long t = dao.getLevelIDByProperty("天津", "中原", "南区事业部", "河西人民公园3战区", "一片区", "分行1b");
//		logger.info("========"+t);
		
	}
	
	
}
