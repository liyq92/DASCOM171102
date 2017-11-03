package com.dascom.ssmn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dascom.OPadmin.dao.DaoException;
import com.dascom.OPadmin.dao.impl.RootDao;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.util.DBException;
import com.dascom.OPadmin.util.HibernateSessionFactory;
import com.dascom.ssmn.entity.SsmnCDR;
import com.dascom.ssmn.entity.SsmnZyCdr;
import com.dascom.ssmn.entity.SsmnZyChannel;
import com.dascom.ssmn.entity.SsmnZyClientnum;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.ssmn.entity.ZydcRecord;
import com.dascom.ssmn.util.Constants;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.init.ConfigMgr;

public class CDRDao  extends RootDao{
	private static final Logger logger =  Logger.getLogger(CDRDao.class);
	private static CDRDao dao;
	
	public synchronized static CDRDao getInstance(){
		if(dao == null)
			dao = new CDRDao();
		return dao;
	}
	
	
	@Override
	public Class<SsmnCDR> getReferenceClass() {
		return SsmnCDR.class;
	}	

/*
 * 为了提高查询速度,采取的优化: 
 * 	1)只取出分页后的主键字段streamnumber,最后一条一条取出详细信息(每页只有10条,只取10次)
 * 	2)排序也按主键 streamnumber字段倒序,因为按streamnumber与按时间排效果一样
 */
	public List<ZydcRecord> getCdrStatRecordList(String msisdn,String sname,String sStreamNum,long startTimeLong,
			long endTimeLong,String sclientnumber,String bdparam,String zoneparam,String areaparam,
			String bagparam,String channelid,String ssmnnum,String strcalltype,String sCallReMis, 
			Integer index, Integer maxCount,TyadminOperators opera){
		Session session = HibernateSessionFactory.getSession();
		SsmnZyLevel level =new SsmnZyLevel();
		try {
			level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
		} catch (DaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}

		List<ZydcRecord> list = new ArrayList<ZydcRecord>();

		try{
			conn = session.connection();
	
			StringBuffer strSql = new StringBuffer(" select z.streamnumber ");
			
			strSql.append(" from ssmn_zy_cdr z ,ssmn_zy_level l ,ssmn_zy_channel c  where 1=1 " +
					" and z.levelid=l.id  and z.channelid =c.id ");
			strSql.append( getDepartSql().toString());//添加呼入呼出条件	
		  
			//查询条件拼sql
			strSql.append(getSqlBySelect(msisdn,sname,sStreamNum,startTimeLong,
					endTimeLong,sclientnumber,bdparam,zoneparam,areaparam,
					bagparam,channelid,ssmnnum,strcalltype,sCallReMis,level));
			if(Constants.TYPE_SHOWDATE <2)
				strSql.append(" and c.type =").append(Constants.TYPE_SHOWDATE);
			
			//总的sql
			String sqll = " select streamnumber from ( select streamnumber,rownum as rn from ( "; 
			sqll +=strSql ;

			sqll += "  order by z.callStartTime desc    "; 
			sqll += " ) p )q   ";
			if(index >=0)
			{
				sqll +="where   ";
				int st=index+1;
				int en =index+10;
				sqll +=" q.rn >=";
				sqll +=st;
				sqll +=" and q.rn <=";
				sqll +=en;
			}
			
			pstmt = conn.prepareStatement(sqll.toString());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while (rs.next()) {
					list.add(new ZydcRecord(rs.getLong("streamnumber")));
				}
			}
		} catch (Exception he) {
			logger.error("-------------getCdrStatRecord---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
				if(rs !=null)
					rs.close();
				if(conn !=null)
					conn.close();
			} catch (HibernateException he2) {
				logger.error("-------------getCdrStatRecord---关闭Session时出现HibernateException-----",he2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
		
	} 
	
	/*
	 * 获取总数,计算分页
	 */
	public int getCdrStatRecordCount(String msisdn ,String sname,String sStreamNum ,TyadminOperators opera, 
			long startTimeLong,long endTimeLong,String sclientnumber,
			String bdparam,String zoneparam,String areaparam,String bagparam,String channelid,
			String ssmnnum,String strcalltype,String sCallReMis){
		SsmnZyLevel level=null;
		try {
			level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Session session = HibernateSessionFactory.getSession();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		StringBuffer  levelsql=new StringBuffer();
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
		
		List<ZydcRecord> list = new ArrayList<ZydcRecord>();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			//begin
			conn = session.connection();
			//未解绑的
			StringBuffer strSql = new StringBuffer(" select z.streamnumber　 "); 
			strSql.append("   from ssmn_zy_cdr z ,ssmn_zy_level l ,ssmn_zy_channel c where 1=1 and " +
					" z.levelid=l.id  and z.channelid =c.id ");
			strSql.append( getDepartSql().toString());//添加呼入呼出条件	

			//查询条件拼sql
			strSql.append(getSqlBySelect(msisdn,sname,sStreamNum,startTimeLong,
					endTimeLong,sclientnumber,bdparam,zoneparam,areaparam,
					bagparam,channelid,ssmnnum,strcalltype,sCallReMis,level));
			if(Constants.TYPE_SHOWDATE <2)
				strSql.append(" and c.type =").append(Constants.TYPE_SHOWDATE);
			
			//总的sql
			String sqll = " select count(streamnumber) as cn  from  ( ";
				sqll +=strSql+" ) q  "; 

			pstmt = conn.prepareStatement(sqll.toString());
			rs = pstmt.executeQuery();
			if(rs != null) 
			{
				while(rs.next())
				{
					count = rs.getInt("cn");
				}
			}
		} catch (Exception he) {
			logger.error("-------------getCdrStatRecordCount---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getCdrStatRecordCount---关闭Session时出现HibernateException-----",he2);
			}
		}
		return count;
		
	}
	
	/*
	 * 查询条件统一拼sql
	 */
	public String getSqlBySelect(String msisdn,String sname,String sStreamNum,long startTimeLong,
			long endTimeLong,String sclientnumber,String bdparam,String zoneparam,String areaparam,
			String bagparam,String channelid,String ssmnnum,String strcalltype,String sCallReMis,SsmnZyLevel level)
	{
		StringBuffer res =new StringBuffer();
		
		//主号码
		if(msisdn != null && !"".equals(msisdn))
			res.append(getSql(" and z.msisdn = ",msisdn,false));
		
		//姓名
		if(sname != null && !"".equals(sname))
			res.append(getSql(" and z.username =",sname,false));
		
		//序号
		if(sStreamNum != null && !"".equals(sStreamNum))
			res.append(getSql(" and z.streamnumber =",sStreamNum,true));
		
		//起始日期
		if(startTimeLong >0)//日期不为空
			res.append(getSql(" and z.callStartTime >=  ",startTimeLong+"",true));

		//结束日期	
		if(endTimeLong >0)//日期不为空
			res.append(getSql(" and  z.callStartTime <= ",endTimeLong+"",true));

		//客户号码
		if(sclientnumber != null && !"".equals(sclientnumber))
		{
			//if(ConfigMgr.getIsDoubleCall() !=null && ConfigMgr.getIsDoubleCall().equals("1"))//是双呼
			//	res.append(getSql(" and (case when z.dc_calltype in(3,4,5) then z.outgoing_cpn else z.incoming_cin end ) =",sclientnumber,false));
			//else//不是双呼
			//	res.append(getSql(" and (case when z.callType=1 then z.incoming_CIN else z.outgoing_CPN end) =",sclientnumber,false));			
			res.append(getSql(" and z.clientnumber =",sclientnumber,false));
		}
		//副号码
		if(ssmnnum != null && !"".equals(ssmnnum))
			res.append(getSql(" and z.ssmnnumber =",ssmnnum,false));
		
		StringBuffer levelsql =new StringBuffer();
		String ssql = new UtilFunctionDao().getDepartSqlByLevel(level);
		if(ssql.length() >0)
			res.append(ssql.toString());
		
		//事业部
		if(bdparam !=null && !"".equals(bdparam))
			levelsql.append(getSql(" and l.businessdepartment =",bdparam,false));
		
		//战区
		if(zoneparam !=null && !"".equals(zoneparam))
			levelsql.append(getSql(" and l.warzone =",zoneparam,false));
		
		//片区
		if(areaparam !=null && !"".equals(areaparam))
			levelsql.append(getSql(" and l.area=",areaparam,false));
		
		//行动组
		if(bagparam !=null && !"".equals(bagparam))
			levelsql.append(getSql(" and l.branchactiongroup =",bagparam,false));
		
		//渠道
		if(channelid !=null && !"".equals(channelid))
			res.append(getSql(" and z.channelid=",channelid,true));
		
		//呼入:1、呼出:2
		if(strcalltype !=null && !"".equals(strcalltype))
			res.append( getDepartSqlCallInOut(strcalltype));
		
		//已接、未接
		if(sCallReMis !=null && !"".equals(sCallReMis))
		{
			if(sCallReMis.equals("1"))
				res.append(" and z.callDuration >0 ");
			else
				res.append(" and z.callDuration <= 0 ");
		}

		res.append(levelsql.toString());
		res.append(" and z.endreason in (1,2,3,4) ");
		
		return res.toString();
	}
	
	/*
	 * 根据streamnumber取出详细的信息
	 */
	public ZydcRecord getCdrStatRecordByStreamnumber(long streamum)
	{
		Session session =HibernateSessionFactory.getSession();
		Connection conn =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ZydcRecord zy =null;
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
		try{
			conn = session.connection();
			StringBuffer strSql =new StringBuffer(" select z.username,z.empno,l.branchactiongroup,z.msisdn,(case z.ssmnnumber when '00000000000' then '' else z.ssmnnumber end) as ssmnnumber ");
			strSql.append(" , z.clientnumber as clientnumber ");
			strSql.append(" ,c.name ");
			
			//通话类型
			if(ConfigMgr.getIsDoubleCall() !=null && ConfigMgr.getIsDoubleCall().equals("1"))//是双呼
				strSql.append(" , z.dc_calltype as dcalltype ");
			else//不是双呼
				strSql.append(", (case when z.callType=1 then 2 else 4 end) as dcalltype  ");//呼入 dc_calltype为1 呼出dc_calltype为4
			
			strSql.append(" , to_char(to_date(to_char(z.callstarttime),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') as callstarttime ");
			strSql.append(" , z.callduration ,r.filepath, substr(r.filepath,instr(r.filepath,'/',-1)+1) as filename  ,c.type as channeltype ");
			strSql.append(" , z.firstmsisdn ");//添加第一联系人
			strSql.append(" ,(case when replace(z.remark,'\\n','') ='null' then '' else  replace(z.remark,'\\n','') end) as remark ");//添加备注
			
			strSql.append(" from ssmn_zy_cdr z,ssmn_zy_level l,ssmn_zy_channel c,Ssmn_Record r ");
			strSql.append(" where z.levelid=l.id and z.channelid=c.id  and z.streamnumber=r.streamnumber(+) ");
			strSql.append("  and z.streamnumber = ").append(streamum);
			
			pstmt = conn.prepareStatement(strSql.toString());
			rs = pstmt.executeQuery();
			if(rs !=null)
			{
				while(rs.next())
				{
					zy =new ZydcRecord(rs.getString("username"),rs.getString("empno"),rs.getString("branchactiongroup"),
							rs.getString("msisdn"),rs.getString("ssmnnumber"),rs.getString("clientnumber"),rs.getString("name"),
							rs.getLong("dcalltype"),rs.getString("callstarttime"),rs.getInt("callduration"),rs.getString("filepath"),
							rs.getString("filename"),rs.getInt("channeltype"),rs.getString("firstmsisdn"),rs.getString("remark"));
				}
				if(zy !=null)
				{
					//如果隐藏业主号码功能权限有，则在这里隐藏　,双呼： 隐藏dc_calltype为：1 3 4 5  ;,只有A+渠道才隐藏
					//非双呼：隐藏：App呼出 
					if(Constants.SHOW_CLIENTNUM_OP ==1 )
					{
						if(zy.getDccalltype().longValue() !=2)//只判断dccalltype就可以，因为查询的时候，非双呼情况，将该值也同步成双呼了(２：客户呼入，4：app呼出)
						{
							if(zy.getChannelType() ==1)//只有A+渠道才隐藏
							{
								String sclient = zy.getClientnumber();
								if(sclient !=null && sclient.length()>0)
								{
									String strsub = sclient.substring(0, 3);
									for(int j=0;j<sclient.length()-3;j++)
									{
										strsub +="*";
									}
									zy.setClientnumber(strsub);
								}else
									zy.setClientnumber(zy.getClientnumber());
							}else
								zy.setClientnumber(zy.getClientnumber());
						}else
							zy.setClientnumber(zy.getClientnumber());
					}else
						zy.setClientnumber(zy.getClientnumber());
				}
			}
				
		} catch (Exception he) {
			logger.error("-------------getCdrStatRecordByStreamnumber---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
				if(rs !=null)
					rs.close();
				if(conn !=null)
					conn.close();
			} catch (HibernateException he2) {
				logger.error("-------------getCdrStatRecordByStreamnumber---关闭Session时出现HibernateException-----",he2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return zy;
	}
	
	/*
	 * 语音查询页面中,导出通话记录:
	 * 暂时不排序
	 * 
	 * recordcountInt：总数，便于进度条控制
	 * processbar it 进度条对象
	 */
	public List<ZydcRecord> getCdrStatRecordExportList(String msisdn,String sname,String sStreamNum,long startTimeLong,
			long endTimeLong,String sclientnumber,String bdparam,String zoneparam,String areaparam,
			String bagparam,String channelid,String ssmnnum,String strcalltype,String sCallReMis, 
			long levelid,int showtype){
		Session session = HibernateSessionFactory.getSession();
		SsmnZyLevel level =new SsmnZyLevel();
		try {
			level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(levelid);
		} catch (DaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}

		List<ZydcRecord> list = new ArrayList<ZydcRecord>();

		try{
			conn = session.connection();

			StringBuffer strSql = new StringBuffer(" select z.streamnumber ,z.username,z.empno,l.branchactiongroup,z.msisdn,(case z.ssmnnumber when '00000000000' then '' else z.ssmnnumber end) as ssmnnumber ");
			strSql.append(" ,c.name  ");
			strSql.append(" , z.callduration ,l.provincecity,l.company, l.businessdepartment,l.warzone,l.area ");
			strSql.append(" , z.clientnumber as clientnumber  ");
			//通话类型
			if(ConfigMgr.getIsDoubleCall() !=null && ConfigMgr.getIsDoubleCall().equals("1"))//是双呼
				strSql.append(" , z.dc_calltype as dcalltype ");
			else//不是双呼
				strSql.append(", (case when z.callType=1 then 2 else 4 end) as dcalltype  ");//呼入 dc_calltype为1 呼出dc_calltype为4
			
			strSql.append(" , to_char(to_date(to_char(z.callstarttime),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') as callstarttime ");
			strSql.append(" ,  substr(r.filepath,instr(r.filepath,'/',-1)+1) as filename  ");
			//是否标记为黑名单
			strSql.append(" , (select (case when n.black_number is not null then '是'  else '否' end ) " +
					"from BlackCallNumber n where n.black_number =z.clientnumber and rownum =1) as bn ");
			strSql.append(" , c.type as channeltype ");
			strSql.append(" , z.firstmsisdn ");
			strSql.append(" ,(case when replace(z.remark,'\\n','') ='null' then '' else  replace(z.remark,'\\n','') end) as remark ");//添加备注
			
			strSql.append(" from ssmn_zy_cdr z ,ssmn_zy_level l, ssmn_zy_channel c,Ssmn_Record r " +
					"  where 1=1 and z.levelid=l.id ");
			strSql.append(" and z.channelid=c.id and z.streamnumber=r.streamnumber(+)  ");
			strSql.append( getDepartSql().toString());//添加呼入呼出条件
			
			//查询条件拼sql
			strSql.append(getSqlBySelect(msisdn,sname,sStreamNum,startTimeLong,
					endTimeLong,sclientnumber,bdparam,zoneparam,areaparam,
					bagparam,channelid,ssmnnum,strcalltype,sCallReMis,level));
			if(showtype <2 )
				strSql.append(" and c.type =").append( showtype );
			
			pstmt = conn.prepareStatement(strSql.toString());
			rs = pstmt.executeQuery();

			if(rs != null) {
				while (rs.next()) {
					String scalltype = new UtilFunctionDao().getDcCallType(rs.getLong("dcalltype")+"");
					String srccalldur = UtilFunctionDao.getsecondSimple(rs.getInt("callduration"));
					String isblacknum = rs.getString("bn");
					if(isblacknum !=null && isblacknum.length()>0)
						isblacknum ="是";
					else
						isblacknum ="否";
					String scName = UtilFunctionDao.splitStr(rs.getString("name"),"_");
					list.add(new ZydcRecord(rs.getLong("streamnumber"),rs.getString("username"),
							rs.getString("empno"),rs.getString("branchactiongroup"),
							rs.getString("msisdn"),rs.getString("ssmnnumber"),scName,
							rs.getInt("callduration"),rs.getString("provincecity"),rs.getString("company"),
							rs.getString("businessdepartment"),rs.getString("warzone"),
							rs.getString("area"),rs.getString("clientnumber"),rs.getLong("dcalltype"),
							rs.getString("callstarttime"),
							rs.getString("filename"),scalltype,srccalldur,isblacknum,
							rs.getInt("channeltype"),rs.getString("firstmsisdn"),
							rs.getString("remark")));
				}
				
				if(list !=null && list.size()>0)
				{
					//如果隐藏业主号码功能权限有，则在这里隐藏　,双呼： 隐藏dc_calltype为：1 3 4 5  ;,只有A+渠道才隐藏
					//非双呼：隐藏：App呼出 
					if(Constants.SHOW_CLIENTNUM_OP ==1 )
					{
						for(int i=0;i<list.size();i++)
						{
							ZydcRecord z = list.get(i);
							if(z.getDccalltype().longValue() !=2)//只判断dccalltype就可以，因为查询的时候，非双呼情况，将该值也同步成双呼了(２：客户呼入，4：app呼出)
							{
								if(z.getChannelType() ==1)//只有A+渠道才隐藏
								{
									String sclient = z.getClientnumber();
									if(sclient !=null && sclient.length()>0)
									{
										String strsub = sclient.substring(0, 3);
										for(int j=0;j<sclient.length()-3;j++)
										{
											strsub +="*";
										}
										list.get(i).setClientnumber(strsub);
									}else
										list.get(i).setClientnumber(z.getClientnumber());
								}else
									list.get(i).setClientnumber(z.getClientnumber());
							}else
								list.get(i).setClientnumber(z.getClientnumber());
						}
					}
				}
			}
		} catch (Exception he) {
			logger.error("-------------getCdrStatRecordExportList---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
				if(rs !=null)
					rs.close();
				if(conn !=null)
					conn.close();
			} catch (HibernateException he2) {
				logger.error("-------------getCdrStatRecordExportList---关闭Session时出现HibernateException-----",he2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
		
	} 
	
	/*
	 * 导出语音时
	 * 根据streamnumber取出详细的信息,
	 * 这里查的都是需要处理的字段,减少赋值时间
	 */
	public ZydcRecord getCdrStatRecordByStreamnumberExport(long streamum)
	{
		Session session =HibernateSessionFactory.getSession();
		Connection conn =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ZydcRecord zy =null;
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
		try{
			conn = session.connection();
			StringBuffer strSql =new StringBuffer(" select  ");
			strSql.append(" clientnumber ");	
			
			//通话类型
			if(ConfigMgr.getIsDoubleCall() !=null && ConfigMgr.getIsDoubleCall().equals("1"))//是双呼
				strSql.append(" , z.dc_calltype as dcalltype ");
			else//不是双呼
				strSql.append(", (case when z.callType=1 then 2 else 4 end) as dcalltype  ");//呼入 dc_calltype为1 呼出dc_calltype为4
			
			strSql.append(" , to_char(to_date(to_char(z.callstarttime),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') as callstarttime ");
			strSql.append(" , r.filepath, substr(r.filepath,instr(r.filepath,'/',-1)+1) as filename  ");
			strSql.append(" from ssmn_zy_cdr z,Ssmn_Record r ");
			strSql.append(" where  z.streamnumber=r.streamnumber(+) ");
			strSql.append("  and z.streamnumber = ").append(streamum);
			
			pstmt = conn.prepareStatement(strSql.toString());
			rs = pstmt.executeQuery();
			if(rs !=null)
			{
				while(rs.next())
				{
					zy =new ZydcRecord(rs.getString("clientnumber"),
							rs.getLong("dcalltype"),rs.getString("callstarttime"),
							rs.getString("filename"));
				}
			}
				
		} catch (Exception he) {
			logger.error("-------------getCdrStatRecordByStreamnumberExport---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
				if(rs !=null)
					rs.close();
				if(conn !=null)
					conn.close();
			} catch (HibernateException he2) {
				logger.error("-------------getCdrStatRecordByStreamnumberExport---关闭Session时出现HibernateException-----",he2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return zy;
	}
			
	public String getlevelid(SsmnZyLevel opera)
	{
		String res = "";
		res = new UtilFunctionDao().getTyadminOperatorLevelid(opera);
		return res;
	}
	
	public List<SsmnZyChannel> getAllChannelList()
	{
		Session session = null;
		StringBuffer strHQL = new StringBuffer(" select t.id,t.name from SsmnZyChannel t ");

		List<SsmnZyChannel> list = new ArrayList<SsmnZyChannel>();
		ScrollableResults srs = null;
		try {
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			
			
			srs = query.scroll();
			while (srs.next()) {
				list.add(new SsmnZyChannel(srs.getLong(0),srs.getString(1)));
			}
		} catch (Exception he) {
			logger.error("-------------getCdrStatRecord---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getCdrStatRecord---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
		
	}
	
	/**
	 * 呼入/呼出统计
	 * 呼入:按渠道分类,呼出:按呼出类型分页
	 *  
	 * dcCallType表示呼出类型,只有呼出，才起作用,呼入情况不起作用(呼出中，如果为空，则为APP呼出)
	 * 3,PC呼出
	 * 4:APP呼出
	 * 5: 直接呼出
	 * 
	 */
	public List<ZydcRecord> getCdrChannelCountList(String msisdn,String sname,String sStreamNum,long startTimeLong,
			long endTimeLong,String sclientnumber,String bdparam,
			String zoneparam,String areaparam,String bagparam,String channelid,String ssmnnum,
			String strcalltype,String sCallReMis,long levelid ,String strsl ,int showtype){
		Session session = HibernateSessionFactory.getSession();
		SsmnZyLevel level=null;
		try {
			level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(levelid);
		} catch (DaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ZydcRecord> list = new ArrayList<ZydcRecord>();
		String sqll = "";
		
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}

		try 
		{
			conn = session.connection();

			StringBuffer strSql = new StringBuffer(" select  c.name as cname　,z.streamnumber  ");
			strSql.append(getDepartSqlGroupBy(strcalltype," ,z.dc_calltype "));
			strSql.append(" from ssmn_zy_cdr z,ssmn_zy_channel c ,ssmn_zy_level l");
			strSql.append(" where z.channelid=c.id and z.levelid=l.id  ");
			strSql.append( getDepartSql().toString());//添加呼入呼出条件	
			if(showtype <2)
				strSql.append(" and c.type= ").append(showtype);

			//查询条件拼sql
			strSql.append(getSqlBySelect(msisdn,sname,sStreamNum,startTimeLong,
					endTimeLong,sclientnumber,bdparam,zoneparam,areaparam,
					bagparam,channelid,ssmnnum,strcalltype,sCallReMis,level));
			
			//付加sql
			if(strsl !=null && strsl.length()>0)
				strSql.append(strsl);

			//呼出(包括双呼和非双呼),按 dc_calltype分组
			if(strcalltype !=null && !"".equals(strcalltype) && strcalltype.equals("2") )
			{
				sqll = "  select name  as cname ,count(streamnumber) as scn from (  ";
				sqll +=strSql;
				sqll +="   ) group by  name ";
				
			}else//呼入(包括双呼和非双呼),按渠道分组
			{
				//呼入，按渠道分组，查全部也这样查
				sqll = "  select cname,count(streamnumber) as scn from (  ";
				sqll +=strSql;
				sqll +="   ) group by  cname ";
			}
			pstmt = conn.prepareStatement(sqll.toString());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()){			
					list.add(new ZydcRecord(rs.getString("cname"),rs.getInt("scn")));
				}
			}			
		} catch (Exception he) {
			logger.error("-------------getCdrChannelCountList---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
				if(rs !=null)
					rs.close();
				if(conn != null)
					conn.close();
			} catch (HibernateException he2) {
				logger.error("-------------getCdrChannelCountList---关闭Session时出现HibernateException-----",he2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
		
	}
	
	//type:1按天统计  2按月统计
	public List<String> getCdrChannelMaxMinDate(String msisdn,String sname,String sStreamNum,long startTimeLong,
			long endTimeLong,String sclientnumber,String bdparam,String zoneparam,
			String areaparam,String bagparam,String channelid,String ssmnnum,String strcalltype,
			TyadminOperators opera,String strsl,int type){
		Session session = HibernateSessionFactory.getSession();
		SsmnZyLevel level=null;
		try {
			level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
		} catch (DaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
		List<String> list = new ArrayList<String>();
		
		try {
			conn = session.connection();
			
			//未解绑的数据
			StringBuffer strSql = new StringBuffer();		
			if(type == 1)//按天统计
				strSql.append(" select  to_char(to_date(to_char(z.callstarttime),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd') as cl  ");
			else//按月统计
				strSql.append(" select to_char(to_date(to_char(z.callstarttime),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm') as cl  ");
			strSql.append(" from ssmn_zy_cdr z, ssmn_zy_channel c ,ssmn_zy_level l  ");
			strSql.append(" where z.channelid = c.id and z.levelid=l.id   ");
			strSql.append( getDepartSql().toString());//添加呼入呼出条件	
			
			//查询条件拼sql
			strSql.append(getSqlBySelect(msisdn,sname,sStreamNum,startTimeLong,
					endTimeLong,sclientnumber,bdparam,zoneparam,areaparam,
					bagparam,channelid,ssmnnum,strcalltype,"",level));
			//付加sql
			if(strsl !=null && strsl.length()>0)
				strSql.append(strsl);
			
			String sqll = "";

			//呼入，按渠道分组，查全部也这样查
			sqll = "  select distinct(cl) as da from (  ";
			sqll +=strSql;
			sqll +="   )  order by da  ";
		
			pstmt = conn.prepareStatement(sqll.toString());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()){			
					list.add(rs.getString("da"));
				}
			}			
		} catch (Exception he) {
			logger.error("-------------getCdrChannelMaxMinDate---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
				if(rs !=null)
					rs.close();
				if(conn != null)
					conn.close();
			} catch (HibernateException he2) {
				logger.error("-------------getCdrChannelMaxMinDate---关闭Session时出现HibernateException-----",he2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
		
	}
	
	public int getCdrChannelTotalCount(String msisdn,String sname,String sStreamNum,long startTimeLong,
			long endTimeLong,String sclientnumber,String bdparam,String zoneparam,
			String areaparam,String bagparam,String channelid,String ssmnnum,String strcalltype,
			TyadminOperators opera,String strsl){
		Session session = HibernateSessionFactory.getSession();
		SsmnZyLevel level=null;
		try {
			level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
		} catch (DaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Transaction tx = null;
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
		int res =0;
		
		try {
			tx = session.beginTransaction();
			//begin
			conn = session.connection();
			
			//未解绑的数据
			StringBuffer strSql = new StringBuffer(" select z.streamnumber as stream  ");		
			strSql.append(" from ssmn_zy_cdr z, ssmn_zy_channel c ,ssmn_zy_level l ");
			strSql.append(" where z.channelid = c.id and z.levelid=l.id   ");
			strSql.append( getDepartSql().toString());//添加呼入呼出条件	
			if(Constants.TYPE_SHOWDATE <2)
				strSql.append(" and c.type= ").append(Constants.TYPE_SHOWDATE);
			
			//查询条件拼sql
			strSql.append(getSqlBySelect(msisdn,sname,sStreamNum,startTimeLong,
					endTimeLong,sclientnumber,bdparam,zoneparam,areaparam,
					bagparam,channelid,ssmnnum,strcalltype,"",level));
			
			//付加sql
			if(strsl !=null && strsl.length()>0)
				strSql.append(strsl);

			//呼入，按渠道分组，查全部也这样查
			String sqll = "  select count(stream) as scount from (  ";
			sqll +=strSql;
			sqll +="   )  ";
		
			pstmt = conn.prepareStatement(sqll.toString());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()){			
					res = rs.getInt("scount");
				}
			}			
		} catch (Exception he) {
			logger.error("-------------getCdrChannelTotalCount---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
				if(rs !=null)
					rs.close();
				if(conn != null)
					conn.close();
			} catch (HibernateException he2) {
				logger.error("-------------getCdrChannelTotalCount---关闭Session时出现HibernateException-----",he2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
		
	}


	//添加解绑后的电来数据
	public List<ZydcRecord> getCdrChannelListInfo(String stype, SsmnZyLevel level, String userId,String calltype){
		Session session = HibernateSessionFactory.getSession();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
		List<ZydcRecord> list = new ArrayList<ZydcRecord>();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			//begin
			conn = session.connection();
			StringBuffer strHQL1 = new StringBuffer(" select count(*) as cn ");
			strHQL1.append(getDepartSqlGroupBy(calltype," ,c.name as name "));
			strHQL1.append(" from Ssmn_Zy_User b,Ssmn_CDR a,ssmn_zy_cdrsms_user u, Ssmn_Zy_Channel c  ");
			strHQL1.append(" where a.streamnumber=u.cdrsms_id and u.user_id=b.id and b.channelid = c.id ");
			strHQL1.append( getDepartSql().toString());//添加呼入呼出条件
			strHQL1.append( " and u.type=0 ");
			if(calltype != null && !"".equals(calltype))
			{
				strHQL1.append( getDepartSqlCallInOut(calltype));
			}
			if(!"".equals(stype) && stype.equals("1")) //昨天
			{
				//strHQL1.append("  and  a.callStartTime = to_date(to_char(sysdate-1,'yyyy-mm-dd'),'yyyy-mm-dd') ");
				strHQL1.append("  and  a.callStartTime between to_date(to_char(sysdate-1,'yyyy-mm-dd'),'yyyy-mm-dd') and to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') ");
				
			}else if(!"".equals(stype) && stype.equals("2")) //上周
			{
				strHQL1.append(" and  a.callStartTime >= to_date( to_char(sysdate+(2-to_char(sysdate,'d'))-7,'yyyy-mm-dd'),'yyyy-mm-dd')  ");
				strHQL1.append(" and  a.callStartTime <= to_date( to_char(sysdate+(2-to_char(sysdate,'d'))-1,'yyyy-mm-dd') ,'yyyy-mm-dd')+1 ");
				
			}else if(!"".equals(stype) && stype.equals("3")) //上个月
			{
				strHQL1.append(" and  a.callStartTime >= to_date(to_char(trunc(add_months(sysdate,-1),'mm'),'yyyy-mm-dd') ,'yyyy-mm-dd') ");
				strHQL1.append(" and  a.callStartTime <= to_date(to_char(last_day(add_months(sysdate,-1)),'yyyy-mm-dd'),'yyyy-mm-dd')+1 ");
			}
			
			if(userId != null && !"".equals(userId) && userId.length()>0)
			{
				strHQL1.append(" and b.msisdn = '").append(userId).append("'  ");
			}
			strHQL1.append(" and a.endreason in (1,2,3,4) ");
			 String ssql = new UtilFunctionDao().getTyadminOperatorByJdbc(level, "b");

				if(ssql.length() >0){
					strHQL1.append(" and b.levelid in ");
					strHQL1.append(ssql.toString());
				}else
					return null;

				strHQL1.append(getDepartSqlGroupByName(calltype," group by a.dc_calltype ","group by  c.name  ")) ;

			//添加解绑数据
			StringBuffer strHQL2 = new StringBuffer(" select  count(*) as cn ");

			strHQL2.append(getDepartSqlGroupBy(calltype," , c.name as name"));
			
			strHQL2.append(" from Ssmn_Zy_cancel_User b,Ssmn_CDR a,ssmn_zy_cdrsms_user u, Ssmn_Zy_Channel c  ");
			strHQL2.append(" where a.streamnumber=u.cdrsms_id and u.user_id=b.userid   and b.channelid = c.id ");
			strHQL2.append( getDepartSql().toString());//添加呼入呼出条件
			strHQL2.append(" and u.type=0 ");
			if(calltype != null && !"".equals(calltype))
				strHQL2.append( getDepartSqlCallInOut(calltype));
			
			if(!"".equals(stype) && stype.equals("1")) //昨天
			{
				//strHQL2.append("  and  a.callStartTime = to_date(to_char(sysdate-1,'yyyy-mm-dd'),'yyyy-mm-dd') ");
				strHQL2.append("  and  a.callStartTime between to_date(to_char(sysdate-1,'yyyy-mm-dd'),'yyyy-mm-dd') and to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') ");
				
			}else if(!"".equals(stype) && stype.equals("2")) //上周
			{
				strHQL2.append(" and  a.callStartTime >= to_date( to_char(sysdate+(2-to_char(sysdate,'d'))-7,'yyyy-mm-dd') ,'yyyy-mm-dd') ");
				strHQL2.append(" and  a.callStartTime <= to_date( to_char(sysdate+(2-to_char(sysdate,'d'))-1,'yyyy-mm-dd') ,'yyyy-mm-dd')+1 ");
				
			}else if(!"".equals(stype) && stype.equals("3")) //上个月
			{
				strHQL2.append(" and  a.callStartTime >= to_date( to_char(trunc(add_months(sysdate,-1),'mm'),'yyyy-mm-dd') ,'yyyy-mm-dd') ");
				strHQL2.append(" and  a.callStartTime <= to_date( to_char(last_day(add_months(sysdate,-1)),'yyyy-mm-dd'),'yyyy-mm-dd')+1 ");
			}
			
			if(userId != null && !"".equals(userId) && userId.length()>0)
			{
				strHQL2.append(" and b.msisdn = '").append(userId).append("'  ");
			}
			strHQL2.append(" and a.endreason in (1,2,3,4) ");
			if(ssql.length() >0){
				strHQL2.append(" and b.levelid in ");
				strHQL2.append(ssql.toString());
			}else
				return null;				

			strHQL2.append(getDepartSqlGroupByName(calltype," group by a.dc_calltype "," group by  c.name  ")) ;
			
			String sqll = "  select name,sum(cn) as sumcn from ( ";
			sqll += strHQL1;
			sqll += " union  ";
			sqll +=strHQL2;
			sqll += "  )   ";

			sqll += getDepartSqlGroupByName(calltype," group by name "," group by  name  ");
			 
			
			pstmt = conn.prepareStatement(sqll.toString());
			rs = pstmt.executeQuery();
			if(rs != null) {
				while (rs.next()) {
				list.add(new ZydcRecord(rs.getString("name"),rs.getInt("sumcn")));
			}
			}
		} catch (Exception he) {
			logger.error("-------------getCdrStatRecord---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getCdrStatRecord---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
		
	}

	public String getCdrRecordFileName(Long streamnumber){
		Session session = null;
		StringBuffer strHQL = new StringBuffer("select t.filepath from SsmnRecord t where 1=1  ");
		if(streamnumber>0)
			strHQL.append("  and   t.streamnumber =:streamnumber ");
		 
		//List<ZydcRecord> list = new ArrayList<ZydcRecord>();
		ScrollableResults srs = null;
		String strFileName = "";
		try {
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			if(streamnumber>0)
			{
				query.setLong("streamnumber",streamnumber  );
			}
 
			srs = query.scroll();
			
			while (srs.next()) {
				strFileName = srs.getString(0);
				break;						
			}
		} catch (Exception he) {
			logger.error("-------------getCdrStatRecord---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getCdrStatRecord---关闭Session时出现HibernateException-----",he2);
			}
		}
		return strFileName;
		
	}
	
public List<SsmnZyLevel> getSSmnZyUserProvinceList(SsmnZyLevel level){
	Session session = null;
	
	List<SsmnZyLevel> list = new ArrayList<SsmnZyLevel>();
	ScrollableResults srs = null;
	//String strFileName = "";
	try {
		StringBuffer strHQL = new StringBuffer(" select b.id, b.provincecity,b.company,b.businessdepartment,b.warzone,b.area,b.branchactiongroup from SsmnZyLevel b where 1=1 and b.branchactiongroup is not null ");
		String ssql = new UtilFunctionDao().getTyadminOperatorSelect(level, "b"," select le.id from SsmnZyLevel le where 1=1 ");

		if(ssql.length() >0){
			strHQL.append(" and b.id in ");
			strHQL.append(ssql.toString());
		}else
			return null;
		
		strHQL.append("  group by b.provincecity,b.company,b.businessdepartment,b.warzone,b.area,b.branchactiongroup ,b.id ");
		 
		
		session = this.adapter.openSession();
		Query query = session.createQuery(strHQL.toString());
	
		new UtilFunctionDao().setOperatorAttribute(level, query);
		
		srs = query.scroll();
		
		while (srs.next()) {
			list.add(new SsmnZyLevel(srs.getLong(0), srs.getString(1),srs.getString(2), srs.getString(3), srs.getString(4),srs.getString(5),
					srs.getString(6)));
			 					
		}
	} catch (Exception he) {
		logger.error("-------------getSSmnZyUserProvinceList---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getSSmnZyUserProvinceList---关闭Session时出现HibernateException-----",he2);
		}
	}
	
	return list;
}


public List<SsmnZyUser> getSSmnnumberZyUserProvinceList(SsmnZyLevel level){
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	//检测数据库连接是否成功
	if (session == null) {
		throw new DBException("fail to connect db");
	}
	
	List<SsmnZyUser> list = new ArrayList<SsmnZyUser>();

	try {
		//begin
		conn = session.connection();
		
		//未解绑的副号码 /* 查找有通话录音的副号码*/
		StringBuffer strHQL1 = new StringBuffer(" select distinct(u.ssmnnumber)　as ssmnnum from Ssmn_Zy_User u ,ssmn_zy_cdrsms_user cu,ssmn_cdr c ")
								.append(" where u.id=cu.user_id and cu.cdrsms_id=c.streamnumber and cu.type=0  ");
		String ssql = new UtilFunctionDao().getTyadminOperatorByJdbc(level, "u");

		if(ssql.length() >0){
			strHQL1.append(" and u.levelid in ");
			strHQL1.append(ssql.toString());
		}else
			return null;
		
		//已经解绑的副号码 /* 查找有通话录音的副号码*/
		StringBuffer strHQL2 = new StringBuffer(" select distinct(u.ssmnnumber)　as ssmnnum from Ssmn_Zy_cancel_User u ,ssmn_zy_cdrsms_user cu,ssmn_cdr c ")
								.append(" where u.userid=cu.user_id and cu.cdrsms_id=c.streamnumber and cu.type=0 ");
		
		if(ssql.length() >0){
			strHQL2.append(" and u.levelid in ");
			strHQL2.append(ssql.toString());
		}else
			return null;
		
		//总的sql
		String sqll = "  select distinct(ssmnnum) as ssmn from ( ";
		sqll +=strHQL1 ;
		sqll +=" union  ";
		sqll +=strHQL2;
		sqll +=" ) ";
		
		pstmt = conn.prepareStatement(sqll.toString());
		rs = pstmt.executeQuery();
		if(rs != null) {
			while (rs.next())
			{
				list.add(new SsmnZyUser( rs.getString("ssmn")));
			 }
		}
	} catch (Exception he) {
		logger.error("-------------getSSmnZyUserProvinceList---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getSSmnZyUserProvinceList---关闭Session时出现HibernateException-----",he2);
		}
	}
	
	return list;
}

public boolean isUserOk(SsmnZyUser zu,SsmnZyLevel level,long startTimeLong,long endTimeLong)
{
	boolean isOk = false;
	int count = 0;
	
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//检测数据库连接是否成功
	if (session == null) {
		throw new DBException("fail to connect db");
	}
	List<ZydcRecord> list = new ArrayList<ZydcRecord>();
	Transaction tx = null;
	try {
		tx = session.beginTransaction();
		//begin
		conn = session.connection();
		
		String userId = zu.getMsisdn();
		String username = zu.getName();
		 
		
		//添加解绑数据
		StringBuffer strsql = new StringBuffer(" select  count(*) as cn "); 
		strsql.append(" from ssmn_zy_cdr z ,ssmn_zy_level l ");
		strsql.append(" where z.levelid =l.id  ");
		strsql.append( getDepartSql().toString());//添加呼入呼出条件
		
		if(startTimeLong >0)
			strsql.append("  and  z.callStartTime >=").append(startTimeLong);
		
		if(endTimeLong >0)
			strsql.append("  and  z.callStartTime <=").append(endTimeLong);
		 
		if(userId != null && !"".equals(userId) && userId.length()>0)
			strsql.append(" and z.msisdn = '").append(userId).append("'  ");
		
		if(username != null && !"".equals(username) && username.length()>0)
			strsql.append(" and z.username = '").append(username).append("'  ");

		String ssql = new UtilFunctionDao().getDepartSqlByLevel(level);
			if(ssql.length() >0){
				strsql.append(ssql.toString());
			}else
				return false;
			strsql.append(" and z.endReason in(1,2,3,4) ");	
				
		pstmt = conn.prepareStatement(strsql.toString());
		rs = pstmt.executeQuery();
		if(rs != null) {
			while (rs.next()) {
				count = rs.getInt("cn");
		}
		}
		if(count >0)
			isOk = true;
		else
			isOk = false;
		
	} catch (Exception he) {
		logger.error("-------------getCdrStatRecord---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getCdrStatRecord---关闭Session时出现HibernateException-----",he2);
		}
	}
		
	return isOk;
}


public boolean isUserCancel(SsmnZyUser zu)
{
	boolean isok = false;
	
	Session session = null;
	
	List<SsmnZyUser> list = new ArrayList<SsmnZyUser>();
	ScrollableResults srs = null;
	String smsisdn = zu.getMsisdn();
	String sname = zu.getName();
	try {
		StringBuffer strHQL = new StringBuffer(" select b.msisdn  from SsmnZyUser b where 1=1  ");
		 
		if(!"".equals(smsisdn) && smsisdn != null)
		{
			strHQL.append("  and b.msisdn= :smsisdn  ");
		}
		if(!"".equals(sname) && sname != null)
		{
			strHQL.append("  and b.name=  :sname ");
		}
		
		session = this.adapter.openSession();
		Query query= session.createQuery(strHQL.toString());
		
		if(!"".equals(smsisdn) && smsisdn != null)
		{
			query.setString("smsisdn", smsisdn);
		}
		if(!"".equals(sname) && sname != null)
		{
			query.setString("sname",sname);
		}
		list = query.list();
		
		if(list != null && list.size() >0)
			isok = true;
		else
			isok = false;
			 					
		 
	} catch (Exception he) {
		logger.error("-------------getSSmnZyUserProvinceList---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getSSmnZyUserProvinceList---关闭Session时出现HibernateException-----",he2);
		}
	}
	
	return isok;
}

public boolean  isCancelUserTime(String stype,SsmnZyUser zu)
{
	boolean isOk = false;
	Session session = null;
	List<SsmnZyUser> list = new ArrayList<SsmnZyUser>();
	String smsisdn = zu.getMsisdn();
	String sname = zu.getName();
	try {
		StringBuffer strHQL = new StringBuffer(" select b.msisdn  from SsmnZyCancelUser b where 1=1  ");
		 
		if(!"".equals(smsisdn) && smsisdn != null)
		{
			strHQL.append("  and b.msisdn= :smsisdn  ");
		}
		if(!"".equals(sname) && sname != null)
		{
			strHQL.append("  and b.name=  :sname ");
		}
		if(!"".equals(stype) && stype.equals("1")) //昨天
		{
			strHQL.append("  and  to_char(b.cancelDate,'yyyy-mm-dd') >= to_char(sysdate-1,'yyyy-mm-dd') ");
			
		}else if(!"".equals(stype) && stype.equals("2")) //上周
		{
			strHQL.append(" and  to_char(b.cancelDate,'yyyy-mm-dd') >= (   to_char(sysdate+(2-to_char(sysdate,'d'))-7,'yyyy-mm-dd')   ) ");			
		}else if(!"".equals(stype) && stype.equals("3")) //上个月
		{
			strHQL.append(" and  to_char(b.cancelDate,'yyyy-mm-dd') >= (  to_char(trunc(add_months(sysdate,-1),'mm'),'yyyy-mm-dd')    ) ");
		}
		
		session = this.adapter.openSession();
		Query query= session.createQuery(strHQL.toString());
		
		if(!"".equals(smsisdn) && smsisdn != null)
		{
			query.setString("smsisdn", smsisdn);
		}
		if(!"".equals(sname) && sname != null)
		{
			query.setString("sname",sname);
		}
		list = query.list();
		
		if(list != null && list.size() >0)
			isOk = true;
		else
			isOk = false;
			 					
		 
	} catch (Exception he) {
		logger.error("-------------getSSmnZyUserProvinceList---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getSSmnZyUserProvinceList---关闭Session时出现HibernateException-----",he2);
		}
	}
	
	
	return isOk;
}
	
public List<String> getResultBySql(String sql)
{
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//检测数据库连接是否成功
	if (session == null) {
		throw new DBException("fail to connect db");
	}
	List<String> list = new ArrayList<String>();
	 Transaction tx = null;
	try {
		tx = session.beginTransaction();
		//begin
		conn = session.connection();
		
		pstmt = conn.prepareStatement(sql.toString());
		rs = pstmt.executeQuery();
		if(rs != null) 
		{
			while (rs.next()) 
			{
				list.add(rs.getInt("id")+"");
			}
		}
	} catch (Exception he) {
		logger.error("-------------getResultBySql---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getResultBySql---关闭Session时出现HibernateException-----",he2);
		}
	}
	return list;
}

//根据 streamnum去ssmn_dc_relation表中查询客户电话originalcalled和ownerid作为条件 查客户电话 
public String getClientNum(Long streamnum)
{
	String sres = "";
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//检测数据库连接是否成功
	if (session == null) {
		throw new DBException("fail to connect db");
	}
	String sql = " select  r.owner_msisdn as om from Ssmn_CDR c ,ssmn_dc_relation r where "+
	" c.originalcalled=r.ssmnnumber and c.ownerid=r.owner_id and c.streamnumber="+streamnum;
	try {
		
		//begin
		conn = session.connection();
		pstmt = conn.prepareStatement(sql.toString());
		rs = pstmt.executeQuery();
		if(rs != null) 
		{
			while (rs.next()) 
			{
				sres= rs.getString("om");
				break;
			}
		}
	} catch (Exception he) {
		logger.error("-------------getClientNum---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getClientNum---关闭Session时出现HibernateException-----",he2);
		}
	}
	
	return sres;
}

//是否是双呼,判断条件: 是双呼的,全取出,不是双呼的,只取 calltype为1,2的
public String getDepartSql()
{
	String sql = "";
	if(ConfigMgr.getIsDoubleCall() !=null  && ConfigMgr.getIsDoubleCall().equals("1"))//是双呼
		sql = " and z.dc_calltype in(1,2,3,4,5) ";
	else
		sql =" and z.callType in (1,2) ";//不是双呼
	return sql;
}

public String getDepartSqlCallInOut(String strcalltype)
{
	String sql = "";
	if(ConfigMgr.getIsDoubleCall() !=null && ConfigMgr.getIsDoubleCall().equals("1"))//是双呼
	{
		if(strcalltype.equals("1"))
			sql =" and   z.dc_calltype in(1,2) " ;
		else
			sql =" and  z.dc_calltype in (3,4,5) " ;
	}
	else
		sql = " and z.callType = "+strcalltype;
	return sql;
}

//呼入,按渠道(双呼: dc_calltype:1,2  非双呼:calltype:1)
//呼出,按呼出类型: 双呼:dc_calltype:3,4,5 非双呼:calltype:2
public String getDepartSqlGroupBy(String strcalltype ,String str)
{
	String sql ="";
	
	if(strcalltype !=null && !"".equals(strcalltype) && strcalltype.equals("2"))//呼出 
	{
		//判断是否是双呼
		if(ConfigMgr.getIsDoubleCall() !=null && ConfigMgr.getIsDoubleCall().equals("1"))//是双呼,3种呼出类型
		{
			sql = " ,(case when  z.dc_calltype =3 then '"+new UtilFunctionDao().getDcCallType("3")+
			"' when z.dc_calltype =5 then '"+new UtilFunctionDao().getDcCallType("5")+"' else '"+
			 new UtilFunctionDao().getDcCallType("4")+"' end ) as name ";
			return sql;
		}else//非双呼,只有一个 APP呼出
		{
			sql=" , '" + new UtilFunctionDao().getDcCallType("4")+"'  as name ";
			return sql;
		}
	}else//非双呼,呼入
	{
		sql =str;
		return sql;
	}
}

public String getDepartSqlSelectByChannel(String sChannelname,String calltype)
{
	String sql ="";
	/*if(calltype !=null && !"".equals(calltype) && calltype.equals("2") || ConfigMgr.getIsDoubleCall() !=null  && ConfigMgr.getIsDoubleCall().equals("1"))
	{
		if( sChannelname != null && !"".equals(sChannelname) )
		{
			if(sChannelname.equals(new UtilFunctionDao().getDcCallType("业主呼入","1")))
				sql = " and  a.dc_calltype =1 ";
			if(sChannelname.equals(new UtilFunctionDao().getDcCallType("客户呼入","2")))
				sql = " and  a.dc_calltype =2 ";
			if(sChannelname.equals(new UtilFunctionDao().getDcCallType("PC呼出","3")))
				sql = " and  a.dc_calltype =3 ";
			if(sChannelname.equals(new UtilFunctionDao().getDcCallType("直接呼出","5")))
				sql = " and  a.dc_calltype =5 ";
			if(sChannelname.equals(new UtilFunctionDao().getDcCallType("APP呼出","4")))
				sql = " and  a.dc_calltype=4  ";
		}
	}
	else	
	{*/
		if(sChannelname != null && !"".equals(sChannelname)  )
			sql =" and c.name ='"+sChannelname+"'  ";
	//}
	return sql;
}

/*参数说明: 	datestr:日期
			timestr:时间
			type:0:开始时间 1:结束时间
			区别开始时间和结束时间的原因:
			结束时间要包括当天,或当前小时内数据,
			如果天,要算出当天23:59:59,,算法:加1天减1秒
			如果小时:要算出本小时59秒59分,算法:加1小时减1秒
*/
public long getDateLong(String datestr,String timestr,int type)
{
	long lres =0;
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//检测数据库连接是否成功
	if (session == null) {
		throw new DBException("fail to connect db");
	}
	String dStr ="";
	if(type ==0 )//起始时间
	{
		if(datestr !=null && datestr.length()>0)//日期
		{
			if(timestr !=null && timestr.length()>0)//时间不为空
				dStr = "to_date('"+datestr+" "+timestr+"' , 'YYYY-MM-DD HH24:MI:SS')";
			else
				dStr = "to_date('"+datestr+"' , 'YYYY-MM-DD HH24:MI:SS')";
		}
	}else
	{//结束时间
		if(datestr !=null && datestr.length()>0)//日期
		{
			if(timestr !=null && timestr.length()>0)//时间不为空
				dStr = "to_date('"+datestr+" "+timestr+"' , 'YYYY-MM-DD HH24:MI:SS')+1/24-1/(24*60*60)";
			else
				dStr = "to_date('"+datestr+"' , 'YYYY-MM-DD HH24:MI:SS')+1-1/(24*60*60)";
		}		
	}
	 
	String sql = "select to_number(to_char("+dStr+",'YYYYMMDDHH24MISS')) as ltime  from dual  ";
	try {
		
		//begin
		conn = session.connection();
		pstmt = conn.prepareStatement(sql.toString());
		rs = pstmt.executeQuery();
		if(rs != null) 
		{
			while (rs.next()) 
			{
				lres= rs.getLong("ltime");
				break;
			}
		}
	} catch (Exception he) {
		logger.error("-------------getDateLong---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getDateLong---关闭Session时出现HibernateException-----",he2);
		}
	}
	
	return lres;
}

public List<SsmnZyCdr> getCdrByMsisdn(String msisdn,String clientnumber,Date calldate)
{
	Session session = null;
	List<SsmnZyCdr> list = new ArrayList<SsmnZyCdr>();
	StringBuffer strHQL = new StringBuffer("");
	try {
		strHQL.append(" select streamnumber from SsmnZyCdr  where 1=1 ");
		if(msisdn !=null && msisdn.length()>0)
			strHQL.append(" and msisdn =:msisdn ");
		if(clientnumber !=null && clientnumber.length()>0)
			strHQL.append(" and clientnumber =:clientnumber ");
		String strdate =new SimpleDateFormat("yyyy-MM-dd").format(calldate);
		Long lstarttime =getDateLong(strdate,"",0);
		Long lendtime =getDateLong(strdate,"",1);
		if(lstarttime !=null && lstarttime >0)
			strHQL.append(" and callstarttime >= :lstarttime ");
		if(lendtime !=null && lendtime >0)
			strHQL.append(" and callstoptime <= :lendtime " );
		
		session = this.adapter.openSession();			
		Query query = session.createQuery(strHQL.toString());
		
		if(msisdn !=null && msisdn.length()>0)
			query.setString("msisdn", msisdn);
		if(clientnumber !=null && clientnumber.length()>0)
			query.setString("clientnumber", clientnumber);
		if(lstarttime !=null && lstarttime >0)
			query.setLong("lstarttime", lstarttime);
		if(lendtime !=null && lendtime >0)
			query.setLong("lendtime", lendtime);
		
		list =query.list();
	} catch (Exception he) {
		logger.error("-------------getCdrByMsisdn---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getCdrByMsisdn---关闭Session时出现HibernateException-----",he2);
		}
	}
	return list;
}

public boolean updateCdrRemark(String streamnumber,String remark)
{
	boolean res =true;
	if(streamnumber ==null || streamnumber.length()<=0)
		return false;
	String sql =" update SsmnZyCdr set remark=:remark where streamnumber =:streamnumber ";
	Session session = null;
	Transaction tx = null;
	try {
		session =this.adapter.openSession();
		tx =session.beginTransaction();
		Query qu =session.createQuery(sql);
		qu.setString("remark", remark);
		if(streamnumber !=null && streamnumber.length()>0)
			qu.setLong("streamnumber", Long.parseLong(streamnumber));
		int re =qu.executeUpdate();
		tx.commit();
	} catch (Exception he) {
		logger.error("-------------updateCdrRemark---出现Exception-----",he);
		try {
			tx.rollback();
			res = false;
		} catch (HibernateException e) {
			e.printStackTrace();
			res = false;
		}
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------updateCdrRemark---关闭Session时出现HibernateException-----",he2);
		}
	}
	
	return res;
}

public String getDepartSqlGroupByName(String calltype,String str1,String str2)
{
	String sql ="";
	if(calltype !=null && !"".equals(calltype) && calltype.equals("2"))	
		sql =str1;
	else
		sql =str2; 

	return sql;
}

/**
 * 拼接查询sql
 * @param strKey:字段名
 * @param strVal:字段值,如果是数值型,传参的时候转成String
 * @param isNum:是否是数值型数据, true:数值型 , false:字符型(拼串时需要加单引号) , 
 * @return:返回拼接的串
 */
public String getSql(String strKey,String strVal,boolean isNum)
{
	String res ="";
	if(strKey!=null && strKey.length()>0)
	{
		if(isNum)//数值型数据,直接拼接
			res =strKey+strVal;
		else
			res =strKey+" '"+strVal+"' ";
	}	
	return res;
}

	public static void main(String[] args) {
		CDRDao dao = CDRDao.getInstance();
		SsmnZyLevel lev = new SsmnZyLevel();
		 lev.setCompany("中原");
		 lev.setProvincecity("天津");
		 lev.setBusinessdepartment("南区事业部");
		 lev.setWarzone("河西人民公园2战区");
		//List<ZydcRecord> list = dao.getCdrStatRecord(lev, null);
		//for(int i=0;i<list.size();i++){
		//	logger.info("-------type:"+list.get(i).getCalltype()+"--------cientnumber:"+list.get(i).getClientnumber()+"--------endreason:"+list.get(i).getEndreason());
		//}
		
	}


}
