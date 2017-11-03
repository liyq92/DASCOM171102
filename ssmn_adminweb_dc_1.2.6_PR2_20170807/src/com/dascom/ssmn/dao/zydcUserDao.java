package com.dascom.ssmn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dascom.OPadmin.dao.impl.RootDao;
import com.dascom.OPadmin.entity.TyadminOperators;
import com.dascom.OPadmin.util.DBException;
import com.dascom.OPadmin.util.HibernateSessionFactory;
import com.dascom.init.ConfigMgr;
import com.dascom.ssmn.entity.SsmnCancelNum;
import com.dascom.ssmn.entity.SsmnCancelNumId;
import com.dascom.ssmn.entity.SsmnCancelUser;
import com.dascom.ssmn.entity.SsmnCancelUserId;
import com.dascom.ssmn.entity.SsmnEnablenumber;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.ssmn.entity.SsmnNumber;
import com.dascom.ssmn.entity.SsmnUser;
import com.dascom.ssmn.entity.SsmnZyCancelUser;
import com.dascom.ssmn.entity.SsmnZyChannel;
import com.dascom.ssmn.entity.SsmnZyEnablenumber;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.ssmn.entity.ZydcRecord;
import com.dascom.ssmn.entity.zyUserDto;
import com.dascom.ssmn.util.Constants;
import com.dascom.ssmn.dao.EnableNumberDao;


public class zydcUserDao  extends RootDao{

	private static final Logger logger =  Logger.getLogger(zydcUserDao.class);
	private static zydcUserDao dao;
	private static ChannelDao channeldao = ChannelDao.getInstance();
	private EnableNumberDao edao = EnableNumberDao.getInstance();
	private static String sfilename = "";
	
	public synchronized static zydcUserDao getInstance(){
		if(dao == null)
			dao = new zydcUserDao();
		return dao;
	}
	
	@Override
	public Class<SsmnZyUser> getReferenceClass() {
		return SsmnZyUser.class;
	}
	
	/**
	 * 根据主号码查询绑定记录
	 * @param msisdn
	 * @return
	 */
	public List<SsmnZyUser> getZyUserByMsisdn(String msisdn){
        Session session = null;
		List<SsmnZyUser> list = new ArrayList<SsmnZyUser>();
		try {
			StringBuffer strHQL = new StringBuffer("from SsmnZyUser where 1=1 ");
			if(msisdn != null && !"".equals(msisdn)){
				strHQL.append(" and msisdn =:msisdn ");
			}
			
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			if(msisdn != null && !"".equals(msisdn)){
				query.setString("msisdn", msisdn);
			}
			list = query.list(); 
			 
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
		return list;
	}
	
	public List<SsmnZyCancelUser> getZyCancelUserByMsisdn(String msisdn){
        Session session = null;
		List<SsmnZyCancelUser> list = new ArrayList<SsmnZyCancelUser>();
		try {
			StringBuffer strHQL = new StringBuffer("from SsmnZyCancelUser where 1=1 ");
			if(msisdn != null && !"".equals(msisdn)){
				strHQL.append(" and msisdn =:msisdn ");
			}
			
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			if(msisdn != null && !"".equals(msisdn)){
				query.setString("msisdn", msisdn);
			}
			list = query.list(); 
			 
		} catch (Exception he) {
			logger.error("-------------getZyCancelUserByMsisdn---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getZyCancelUserByMsisdn---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
	}
	
	public List<SsmnZyUser> getZyUserInfoByMsisdn(String msisdn,SsmnZyLevel level){
        Session session = null;
        ScrollableResults srs = null;
		List<SsmnZyUser> list = new ArrayList<SsmnZyUser>();
		try {
			StringBuffer strHQL = new StringBuffer(" select t.id,t.name,t.msisdn,t.ssmnnumber,t.empno,c.name ,l.provincecity,");
			strHQL.append(" l.company,l.businessdepartment,l.warzone,l.area,l.branchactiongroup ,l.id,t.channelid ");
			strHQL.append(" from SsmnZyUser t ,SsmnZyChannel c ,SsmnZyLevel l where t.channelid=c.id and t.levelid= l.id ");
			if(msisdn != null && !"".equals(msisdn)){
				strHQL.append(" and msisdn =:msisdn ");
			}
			if(level.getProvincecity() != null && level.getProvincecity().length()>0)
				strHQL.append("  and l.provincecity = :provincecity  ");
			
			if(level.getCompany() !=null && level.getCompany().length()>0)
				strHQL.append(" and l.company = :company ");
			
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			
			if(msisdn != null && !"".equals(msisdn)){
				query.setString("msisdn", msisdn);
			}
			if(level.getProvincecity() != null && level.getProvincecity().length()>0)
				query.setString("provincecity", level.getProvincecity());
			
			if(level.getCompany() !=null && level.getCompany().length()>0)
				query.setString("company", level.getCompany());
			
			srs = query.scroll();
			while(srs.next())
			{
				list.add(new SsmnZyUser(srs.getLong(0),srs.getString(1),srs.getString(2),srs.getString(3),srs.getString(4),srs.getString(5),
						srs.getString(6),srs.getString(7),srs.getString(8),srs.getString(9),srs.getString(10),srs.getString(11),srs.getLong(12),srs.getLong(13)));
			}
			 
		} catch (Exception he) {
			logger.error("-------------getZyUserInfoByMsisdn---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getZyUserInfoByMsisdn---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
	}
	
	
	
	/**
	 * 销户，注销ssmn_zy_user表、ssmn_user表、ssmn_number表，插入ssmn_zy_cancel_user表、ssmn_cancel_user表、ssmn_cancel_num表
	 * @param msisdn
	 * @return
	 */
	public Boolean cancelUserAndNumber(String msisdn){
		Session session = this.adapter.openSession();
		Boolean flag=false;
		Transaction tx = null;
		List<SsmnNumber>  nlist = null;
		List<String> snumberlist = new ArrayList<String>();
		try{
			tx = session.beginTransaction();
			//查出所有待解绑号码
			List<SsmnZyUser>  ulist = this.getZyUserByMsisdn(msisdn); 
			if(ulist.size() < 1){
				return false;
			}
			//删除zyuser表主号绑定的所有副号
			String delzyuser = "delete from SsmnZyUser where msisdn=:msisdn";
			//写入cancluser表记录
			for(int i=0;i<ulist.size();i++){
				//写入canceluser表
				SsmnZyUser u = ulist.get(i);
				SsmnZyCancelUser zycu = new SsmnZyCancelUser();
				zycu.setUserid(u.getId());
				zycu.setName(u.getName());
				zycu.setCancelDate(new Date());
				zycu.setLevelid(u.getLevelid());
				zycu.setChannelid(u.getChannelid());
				zycu.setSsmnnumber(u.getSsmnnumber());
				zycu.setMsisdn(u.getMsisdn());
				zycu.setSubDate(u.getIntime());
				zycu.setGroupname(u.getGroupname());
				zycu.setEmpno(u.getEmpno());
				zycu.setManner(0L);
				zycu.setOperaType(0L);
				zycu.setModeid(u.getModeid());
				zycu.setRemark(StringUtils.defaultIfEmpty(u.getRemark(), ""));
				zycu.setSsmnnumbertype(u.getSsmnnumbertype());
				zycu.setSecondMsisdn(StringUtils.defaultIfEmpty(u.getSecondMsisdn(), ""));
				session.save(zycu);
				snumberlist.add(u.getSsmnnumber());//将副号码暂存
			}
				
			//查出user表
			SsmnUser user = (SsmnUser) session.get(SsmnUser.class, msisdn);
			if(user == null){
				tx.rollback();
				return false;
			}
			
			//删除user表记录
			 String deluser = "delete from SsmnUser where msisdn=:msisdn";
			//写入canceluser表
			 SsmnCancelUser cu = new SsmnCancelUser();
			 SsmnCancelUserId cuid = new SsmnCancelUserId();
			 cuid.setCancelDate(new Date());
			 cuid.setMsisdn(user.getMsisdn());
			 cu.setId(cuid);
			 cu.setAgentId(user.getAgentId());
			 cu.setAnswer(user.getAnswer());
			 cu.setCallingnumber(user.getCallingnumber());
			 cu.setCallingtype(user.getCallingtype());
			 cu.setCancelManner(1L);
			 cu.setCrbtFlag(user.getCrbtFlag());
			 cu.setDevicetoken(user.getDevicetoken());
			 cu.setLogintime(user.getLogintime());
			 cu.setPin(user.getPin());
			 cu.setTempPin(user.getTempPin());
			 cu.setQuestion(user.getQuestion());
			 cu.setServicestatus(user.getServicestatus());
			 cu.setSubDate(user.getSubDate());
			 cu.setSubManner(user.getSubManner());
			 cu.setType(user.getType());
			 cu.setUserid(user.getUserid());
			 cu.setUsername(user.getUsername());
			 cu.setUsertype(user.getUsertype());
			 session.save(cu);
			
			//查出number表的号码
			nlist = NumberDao.getInstance().getNumbersByMsisdn(msisdn);  
			/*if(nlist != null && nlist.size() < 1){
				tx.rollback();
				return false;
			}*/

			//删除number表绑定的所有副号
			String delnum = "delete from SsmnNumber where msisdn=:msisdn";
			//写入cancelnumber表
			if(nlist !=null && nlist.size()>0)
			{
				for(int n =0;n<nlist.size();n++){
					SsmnNumber sn = nlist.get(n);
					SsmnCancelNum num = new SsmnCancelNum();
					SsmnCancelNumId id = new SsmnCancelNumId();
					id.setInitiateDate(sn.getInitiateDate());
					id.setMsisdn(sn.getMsisdn());
					id.setSsmnnumber(sn.getSsmnnumber());
					num.setId(id);
					num.setCancelDate(new Date());
					num.setCancelManner(1L);
					num.setCfstatus(sn.getCfstatus());
					num.setFtstatus(sn.getFtstatus());
					num.setNumberType(sn.getNumberType());
					num.setOrderid(sn.getOrderid());
					num.setPackageid(sn.getPackageid());
					num.setServicestatus(sn.getServicestatus());
					num.setSfstatus(sn.getSfstatus());
					num.setSubManner(sn.getSubManner());
					num.setType(sn.getType());
					session.save(num);
				}
			}
			
			//更新ssmn_zy_enablenumber status为0
			String tem = "";
			for(int i =0;i<snumberlist.size();i++)
			{
				tem  += snumberlist.get(i);
				tem +=",";
			
			}
						
			Query query = session.createQuery(delzyuser);
			query.setString("msisdn", msisdn);
			int delzyu = query.executeUpdate();
			
			query = session.createQuery(deluser);
			query.setString("msisdn", msisdn);
			int delu = query.executeUpdate();
			
			int deln =1;

			query = session.createQuery(delnum);
			query.setString("msisdn", msisdn);
			deln= query.executeUpdate();
			
			
			int upsm = 10;
			if(tem.length() >0 )
			{
				String sqlupdatenumbers = " update SsmnZyEnablenumber  set status=0 where enablenumber in ("+tem.substring(0, tem.length()-1)+" ) ";
				query = session.createQuery(sqlupdatenumbers);
				upsm = query.executeUpdate();
			}
			
			if(delzyu < 1 || deln < 0 || delu < 0 || upsm<0 ){  //有删除失败的则回滚
				tx.rollback();
				return false;
			}
			tx.commit();
			logger.info(msisdn+"用户注销成功");
			flag= true;
		}catch(HibernateException he){
			logger.error("-------------cancelUserAndNumber---出现HibernateException-----",he);
			he.printStackTrace();
			if (tx != null){
				tx.rollback();
				logger.info("操作回滚");
			}
			throw he;
		}finally{
			if(session != null && session.isOpen()){
				session.clear();
				session.close();
			}
		}
		
		if(flag == true)
		{
			////记录到ssmn_zy_useroperation表
			String sRecordUserChange = ConfigMgr.getRecordUserChange();
			if(sRecordUserChange !=null && !"".equals(sRecordUserChange) && sRecordUserChange.equals("1"))
			{
				if(nlist != null && nlist.size()>0)
				{
					for(int i=0;i<nlist.size();i++)
					{
						SsmnNumber sn = nlist.get(i);
						addToUserRecord(sn.getMsisdn(),sn.getSsmnnumber(),"",2,"");
					}
				}
			}
		}
		return flag;
	}
	
	
	/**
	 * 注册业务，包括注册中原地产用户，注册实号码user表和number表
	 * @param msisdn
	 * @param ssmn
	 * @param user
	 * @return
	 */
	public Boolean registeSSMNService(String msisdn,String ssmn, SsmnZyUser user){
		
		//根据ssmn_zy_user表中的levelid，去ssmn_zy_level_mode表中查出放音码
		if(user!=null)
		{
			Long mid = getModeid(user.getLevelid());
			if(mid >0)
				user.setModeid(mid);
		}
		boolean flagSsmnnumberType = false;
		//判断渠道是否在配置的渠道中，如果是，则将ssmnnumber_type置1
		if(ConfigMgr.getChannelNames() !=null && !"".equals(ConfigMgr.getChannelNames()) 
				&& ConfigMgr.getChannelNames().length()>0  && user !=null)
		{
			if(user.getChannelid() !=null && user.getChannelid()>0)
			{
				SsmnZyChannel sname = channeldao.getChannelById(user.getChannelid());
				if(sname !=null && sname.getName() !=null)
				{
					String scname = sname.getName();
					String schannelnames = ConfigMgr.getChannelNames();
					int index = schannelnames.indexOf(scname);					
					if(index >=0)
					{
						user.setSsmnnumbertype(1L);
						flagSsmnnumberType =true;
					}
					else
					{
						user.setSsmnnumbertype(0L);
						flagSsmnnumberType =false;
					}
				}
			}
		}
		
		/*先删除ssmnuser表和ssmnzyuser表*/
		deleteUserByMsisdn(msisdn);
		Session session = this.adapter.openSession();
		Date opeDate=new Date();
		Boolean flag=false;
		//用户信息
		SsmnUser u=new SsmnUser();
		u.setMsisdn(msisdn);
		u.setPin(msisdn.substring(msisdn.length()-6, msisdn.length()));
		u.setSubManner(0L);//避开与计费的触发器冲突,submanner写0
		u.setServicestatus("N");
		u.setCallingtype(0L);
		u.setCallingnumber(ssmn);
		u.setSubDate(opeDate);
		u.setType(0L);
		u.setUserussdflag(0L);
		u.setRemindFlag(1L);
		u.setUsertype("ZYDC");
		
		//副号码信息
		SsmnNumber n=null;
		Boolean numFlag = false;
		SsmnZyEnablenumber ze=null;
		if(!ssmn.equals(Constants.SSMN_NUM_DEFAULT))//开关关闭，或者不是A+渠道，才去判断副号码
		{
			n =(SsmnNumber) session.get(SsmnNumber.class, ssmn);
			
			if(n == null){
				n=new SsmnNumber();
				n.setSsmnnumber(ssmn);
				numFlag = true;
			}
		
			n.setNumberType(1L);
			n.setMsisdn(msisdn);
			n.setCalledType(0L);
			n.setStatus(1L);
			n.setPromptType(1L);
			n.setInitiateDate(opeDate);
			n.setCancelDate(null);
			n.setCancelManner(null);
			n.setSubManner(1L);
			n.setType(1L);
			n.setServicestatus("N");
			n.setCfstatus(1L);
			n.setSfstatus(0L);
			n.setFtstatus(0L);
			n.setOrderid(11L);
			n.setPackageid(0L);

			//更新副号码的状态
			ze = (SsmnZyEnablenumber) session.get(SsmnZyEnablenumber.class, ssmn);
			if(ze == null)
				return false;//说明副号码无效，不在号码池中
			
			ze.setLevelid(user.getLevelid());
			ze.setStatus(1);
		}
		
		//注册User表、更新Number表 
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			if(!ssmn.equals(Constants.SSMN_NUM_DEFAULT))//开关关闭，或者不是A+渠道， 
			{
				if(numFlag){
					session.save(n);
				}else{
					session.update(n);
				}
				
				session.update(ze);//更新副号码状态
			}
			
			session.save(u);
			session.save(user);
			
			tx.commit();
			logger.info(msisdn+"用户注册成功");
			flag= true;
			
		}catch(Exception he){
			logger.error("------------registeSSMNService---出现HibernateException-----",he);
			he.printStackTrace();
			if (tx != null){
				tx.rollback();
				logger.info("操作回滚");
			}
		}finally{
			if(session != null && session.isOpen()){
				session.clear();
				session.close();
			}
		}
		
		if(flag)
		{
			String sRecordUserChange = ConfigMgr.getRecordUserChange();
			if(sRecordUserChange !=null && !"".equals(sRecordUserChange) && sRecordUserChange.equals("1"))
			{
				//记录到ssmn_zy_useroperation表
				if(flagSsmnnumberType ==true)
					addToUserRecord(msisdn,ssmn,"",7,"");//7表示管家APP(双呼)注册经纪人
				else
					addToUserRecord(msisdn,ssmn,"",1,"");
			}
		}
		return flag;
	}
	
	void deleteUserByMsisdn(String msisdn)
	{
		Session session = null;
        int ret = 0;
        Transaction trans = null;
        
        try {
        	session = this.adapter.openSession();
        	trans=session.beginTransaction();
        	String hql="delete from  SsmnUser   where msisdn=:msisdn";
        	Query queryupdate=session.createQuery(hql);
        	queryupdate.setString("msisdn",msisdn);
        	
        	ret=queryupdate.executeUpdate();
        	
        	//变更副号码所属的区域
        	String updateSsmn = " delete from  SsmnZyUser  where  msisdn='"+msisdn+"' )";
        	Query queryupdatessmn=session.createQuery(updateSsmn);
        	queryupdatessmn.executeUpdate();
        	
        	trans.commit();
        	
		} catch (Exception e) {
			logger.error("-------------updateAgentInfo---出现Exception-----",e);
			trans.rollback();
		} finally{
			if(session!=null){
				session.close();
			}
		}
	}
		
	/**
	 * 更新经纪人的信息
	 * @param msisdn
	 * @param name
	 * @param groupname
	 * @param levelid
	 * @return
	 */
	public int updateAgentInfo(String msisdn,String name, String groupname, 
			Long levelid,String sempon,String secondmsisdn,SsmnZyLevel level){
		Session session = null;
        int ret = 0;
        Transaction trans = null;
        String sempnotemp ="";
        
      //查找员工编号是否存在
		List<SsmnZyUser> ulzyem  = getSsmnzyuserByEmpno(sempon,level);
		if(ulzyem != null && ulzyem.size()>0 && sempon!=null && !"".equals(sempon) )
		{
			String stemp = "";
			
			List<SsmnZyUser> ulzyemsisdn = getssmnzyuserbyMsisdn(msisdn);
			if(ulzyemsisdn != null && ulzyemsisdn.size()>0)
				stemp = ulzyemsisdn.get(0).getEmpno();
			
			if( sempon != null && !"".equals(stemp)&& !sempon.equals(stemp))
					return ret=-5;//员工编号已经存在,返回
		}
        
        try {
        	session = this.adapter.openSession();
        	trans=session.beginTransaction();
        	/*String hql="update SsmnZyUser set groupname=:groupname, name=:name,levelid=:levelid ,empno=:empno  where msisdn=:msisdn";
        	Query queryupdate=session.createQuery(hql);
        	queryupdate.setString("groupname",groupname);
        	queryupdate.setString("name",name);
        	queryupdate.setLong("levelid", levelid);
        	queryupdate.setString("msisdn",msisdn);
        	queryupdate.setString("empno",sempon);
        	ret=queryupdate.executeUpdate();*/
        	
        	//变更也要写入ssmn_zy_cancel_user表中,并标记为　更新operatype=1
        	//ssmn_zy_user表中查出所有主号为msisdn的用户　
        	List<SsmnZyUser> lists = getssmnzyuserbyMsisdn(msisdn);
        	sempnotemp = "";
        	//先放入ssmn_zy_cancel表
        	for(int i=0;i<lists.size();i++)
        	{
	        	SsmnZyUser u = lists.get(i);
				SsmnZyCancelUser zycu = new SsmnZyCancelUser();
				zycu.setUserid(u.getId());
				zycu.setName(u.getName());
				zycu.setCancelDate(new Date());
				//zycu.setLevelid(u.getLevelid());//2015-11-19修改，只保留变更后的行动组
				zycu.setLevelid(levelid);
				zycu.setChannelid(u.getChannelid());
				zycu.setSsmnnumber(u.getSsmnnumber());
				zycu.setMsisdn(u.getMsisdn());
				zycu.setSubDate(u.getIntime());
				//zycu.setGroupname(u.getGroupname());
				zycu.setGroupname(groupname);//2015-11-19修改，只保留变更后的行动组
				zycu.setEmpno(u.getEmpno());
				sempnotemp = u.getEmpno(); //保存改前的员工编号
				zycu.setManner(0L);
				zycu.setOperaType(1L);
				zycu.setModeid(u.getModeid());
				zycu.setRemark(StringUtils.defaultIfEmpty(u.getRemark(), ""));
				zycu.setSsmnnumbertype(u.getSsmnnumbertype());
				zycu.setSecondMsisdn(StringUtils.defaultIfEmpty(u.getSecondMsisdn(), ""));
				session.save(zycu);
        	}
        	
        	//将ssmn_zy_canceluser表中的变更行动组的轨迹，刷新成最新的 2015-11-19修改 ,加上区域，不能单独按员工编号
			String supdatessmnzycancelusers = " update SsmnZyCancelUser set ";
			if(levelid !=null && levelid >0)
				supdatessmnzycancelusers +=" levelid= :levelid " ;
			
			supdatessmnzycancelusers +=" , groupname =:groupnm where  empno=:sempno  ";
			if(sempnotemp !=null && !"".equals(sempnotemp) && !sempnotemp.equals(sempon))
			{
				supdatessmnzycancelusers += "  or  empno = :s1 ";
			}
			//加上区域
			if( level !=null && level.getProvincecity() !=null && level.getCompany() !=null )
			supdatessmnzycancelusers +=" and  levelid in (select  id from SsmnZyLevel   where" +
					"  provincecity=:prov  and   company =:comp ) ";
			
			Query  qucanceluser=session.createQuery(supdatessmnzycancelusers);
			if(levelid !=null && levelid >0)
				qucanceluser.setLong("levelid", levelid);
			qucanceluser.setString("groupnm", groupname);
			qucanceluser.setString("sempno", sempon);
			
			if(sempnotemp !=null && !"".equals(sempnotemp) && !sempnotemp.equals(sempon))
			{
				qucanceluser.setString("s1", sempnotemp);
			}
			if( level !=null && level.getProvincecity() !=null && level.getCompany() !=null )
			{
				qucanceluser.setString("prov", level.getProvincecity());
				qucanceluser.setString("comp", level.getCompany());
			}
			qucanceluser.executeUpdate();
			
        	//从ssmn_zy_user表中删除
        	String delzyuser = "delete from SsmnZyUser where msisdn=:msisdn";
        	Query query = session.createQuery(delzyuser);
			 query.setString("msisdn", msisdn);
			ret = query.executeUpdate();
			if(ret < 1 ){  //有失败的则回滚
				trans.rollback();
				return ret;
			}
			//写入ssmn_zy_user表
        	if(lists !=null && lists.size()>0)
        	{
        		for(int i=0;i<lists.size();i++)
        		{
        			SsmnZyUser u = lists.get(i);
        			SsmnZyUser newu = new SsmnZyUser();
        			newu.setSsmnnumber(u.getSsmnnumber());
        			newu.setName(name);
        			newu.setMsisdn(msisdn);
        			newu.setGroupname(groupname);
        			newu.setChannelid(u.getChannelid());       			
        			newu.setIntime(u.getIntime());
        			newu.setLevelid(levelid);
        			newu.setEmpno(sempon);
        			newu.setManner(0L);
        			newu.setModeid(u.getModeid());
        			newu.setSsmnnumbertype(u.getSsmnnumbertype());
        			newu.setSecondMsisdn(secondmsisdn);
        			session.save(newu);
        		}
        	}
        	
        	//变更副号码所属的区域
        	String updateSsmn = " update SsmnZyEnablenumber set levelid="+levelid +" where enablenumber in (select ssmnnumber  from SsmnZyUser where msisdn='"+msisdn+"' )";
        	Query queryupdatessmn=session.createQuery(updateSsmn);
        	ret = queryupdatessmn.executeUpdate();
        	if(ret < 1 ){  //有删除失败的则回滚
				trans.rollback();
				return ret;
			}
        	trans.commit();
        	return ret;
		} catch (Exception e) {
			logger.error("-------------updateAgentInfo---出现Exception-----",e);
			trans.rollback();
		} finally{
			if(session!=null){
				session.close();
			}
		}
		
		return ret;
	}
	
	/**
	 * 编辑经纪人中，当主号码发生变化时
	 * 注销旧主号经纪人(保证能查出旧的主号及旧的主号的语音，短信记录)，创建新的主号经纪人,把旧的主号下面的副号码绑到新的主号上
	 * 	没有用之前的注销函数，因为如果其中有异常，所有的都需要回滚
	 * 
	* */
 public String  editUser(String oldmsisdn,String newmsisdn,String agentname,
		 String branchactiongroup,String sempon,String secondmsisdn,SsmnZyLevel level)
{
		String  isok ="";
		Session session = this.adapter.openSession();
		String snumbers = "";
		String scallingnumber = "";
		String sempnotem = "";
		Transaction tx = null;
		List<SsmnCancelNum> cancelNumsList = new ArrayList<SsmnCancelNum>();
		List<SsmnZyCancelUser> zycancelUserList = new ArrayList<SsmnZyCancelUser>();
		String sempnotemp = "";
		List<SsmnNumber> oldnumbers = null;
		try{ 
			tx = session.beginTransaction();
		
		//先将sub_manner字段改成0，因为这个字段与计费的触发器有冲突
		String supdatessmnusersubmanner = " update SsmnUser set subManner=0 where msisdn='"+oldmsisdn+"'  ";
		//先更新sub_manner字段
		Query  qumaner=session.createQuery(supdatessmnusersubmanner);
		qumaner.executeUpdate();		
		
		//查出旧的主号
		SsmnUser olduser = (SsmnUser)session.get(SsmnUser.class, oldmsisdn);
		if(olduser == null)
			return isok="编辑的旧的主号不存在!";
		//写入ssmncanceluser表
		SsmnCancelUser cu =new SsmnCancelUser();
		SsmnCancelUserId cuid = new SsmnCancelUserId();
		 cuid.setCancelDate(new Date());
		 cuid.setMsisdn(olduser.getMsisdn());
		 cu.setId(cuid);
		 cu.setAgentId(olduser.getAgentId());
		 cu.setAnswer(olduser.getAnswer());
		 cu.setCallingnumber(olduser.getCallingnumber());
		 cu.setCallingtype(olduser.getCallingtype());
		 cu.setCancelManner(1L);
		 cu.setCrbtFlag(olduser.getCrbtFlag());
		 cu.setDevicetoken(olduser.getDevicetoken());
		 cu.setLogintime(olduser.getLogintime());
		 cu.setPin(olduser.getPin());
		 cu.setTempPin(olduser.getTempPin());
		 cu.setQuestion(olduser.getQuestion());
		 cu.setServicestatus(olduser.getServicestatus());
		 cu.setSubDate(olduser.getSubDate());
		 cu.setSubManner(olduser.getSubManner());
		 cu.setType(olduser.getType());
		 cu.setUserid(olduser.getUserid());
		 cu.setUsername(olduser.getUsername());
		 cu.setUsertype(olduser.getUsertype());		
		 
		 //取旧的经纪人所有的副号
		 oldnumbers = getSsmnNumbersByMsisdn(oldmsisdn);
		 
		 //获取ssmn_zy_user
		 List<SsmnZyUser> zyusers =getssmnzyuserbyMsisdn(oldmsisdn);
		 
		//保存ssmn_cancel_number
			for(int i=0;i<oldnumbers.size();i++)
			{
				SsmnNumber sn = oldnumbers.get(i);
				SsmnCancelNum num = new SsmnCancelNum();
				SsmnCancelNumId id = new SsmnCancelNumId();
				id.setInitiateDate(sn.getInitiateDate());
				id.setMsisdn(sn.getMsisdn());
				id.setSsmnnumber(sn.getSsmnnumber());
				num.setId(id);
				num.setCancelDate(new Date());
				num.setCancelManner(1L);
				num.setCfstatus(sn.getCfstatus());
				num.setFtstatus(sn.getFtstatus());
				num.setNumberType(sn.getNumberType());
				num.setOrderid(sn.getOrderid());
				num.setPackageid(sn.getPackageid());
				num.setServicestatus(sn.getServicestatus());
				num.setSfstatus(sn.getSfstatus());
				num.setSubManner(sn.getSubManner());
				num.setType(sn.getType());
				cancelNumsList.add(num);
			}
			
			//获取到新的levelid ,根据行动组名，branchactiongroup，因为每个经纪人必须要选择到行动组的，所以这个值一定会有的
			List<SsmnZyLevel> newlevelidlist = getLevelidbyBranchactiongroup(branchactiongroup);
			if(newlevelidlist == null )
			{
				tx.rollback();
				return isok="选择的行动组不存在!";
			}
			SsmnZyLevel newlevelid = newlevelidlist.get(0);
			
			sempnotemp = "";
			//保存ssmn_zy_cancel_user
			for(int i=0;i<zyusers.size();i++)
			{
				SsmnZyUser zu =  zyusers.get(i);
				SsmnZyCancelUser szu = new SsmnZyCancelUser();
				szu.setUserid(zu.getId());
				szu.setName(zu.getName());
				szu.setMsisdn(zu.getMsisdn());
				szu.setSsmnnumber(zu.getSsmnnumber());
				szu.setGroupname(branchactiongroup);//2015-11-19修改，使用变更后的行动组
				szu.setChannelid(zu.getChannelid());
				szu.setSubDate(zu.getIntime());
				szu.setCancelDate(new Date());
				szu.setLevelid(newlevelid.getId());//2015-11-19修改，使用变更后的行动组
				szu.setEmpno(zu.getEmpno());
				sempnotemp = zu.getEmpno(); //保存改前的员工编号
				szu.setManner(0L);
				szu.setOperaType(0L);
				szu.setModeid(zu.getModeid());
				szu.setRemark(StringUtils.defaultIfEmpty(zu.getRemark(), ""));
				szu.setSsmnnumbertype(zu.getSsmnnumbertype());
				szu.setSecondMsisdn(StringUtils.defaultIfEmpty(zu.getSecondMsisdn(), ""));
				zycancelUserList.add(szu);
			}
			
			//将ssmn_zy_canceluser表中的变更行动组的轨迹，刷新成最新的 2015-11-19修改 
			String supdatessmnzycancelusers = " update SsmnZyCancelUser set levelid="+newlevelid.getId()+" ,groupname =:groupnm where empno=:sempno  ";
			if(sempnotemp !=null && !"".equals(sempnotemp) && !sempnotemp.equals(sempon))
			{
				supdatessmnzycancelusers += "  or empno = :s1 ";
			}
			//加上区域
			if( level !=null && level.getProvincecity() !=null && level.getCompany() !=null )
			supdatessmnzycancelusers +=" and  levelid in (select  id from SsmnZyLevel   where" +
					"  provincecity=:prov  and   company =:comp ) ";
			
			Query  qucanceluser=session.createQuery(supdatessmnzycancelusers);
			qucanceluser.setString("groupnm", branchactiongroup);
			qucanceluser.setString("sempno", sempon);
			if(sempnotemp !=null && !"".equals(sempnotemp) && !sempnotemp.equals(sempon))
			{
				qucanceluser.setString("s1", sempnotemp);
			}
			if( level !=null && level.getProvincecity() !=null && level.getCompany() !=null )
			{
				qucanceluser.setString("prov", level.getProvincecity());
				qucanceluser.setString("comp", level.getCompany());
			}
			qucanceluser.executeUpdate();
		 
		 String sdeletessmnnumber = " delete from SsmnNumber where msisdn='"+oldmsisdn+"'  ";
		 String sdeletessmnzyuser = " delete from SsmnZyUser where msisdn='"+oldmsisdn+"' ";
		  
		 //对于主号码更改的情况，为了保证解绑的号码也能查出来，也需要把cancel表的主号码改过来
		 //更新ssmn_cancel_user
		// String updateSsmncancelUser = " update SsmnCancelUser set msisdn='"+newmsisdn+"' where msisdn='"+oldmsisdn+"' ";
		 //更新ssmn_cancel_number
		// String  updateSsmncancelnumber = " update SsmnCancelNum set msisdn='"+newmsisdn+"' where msisdn='"+oldmsisdn+"' ";
		 //更新ssmn_zy_canceluser
		// String updateSsmnZyCancelUser = " update SsmnZyCancelUser set msisdn='"+newmsisdn+"' where msisdn='"+oldmsisdn+"' ";
				 
				
	//--------------------------------------
		//删除旧经纪人ssmn_user　
		 session.delete(olduser);
		 //删除旧经纪人ssmn_number
		 Query qdeletessmnnumber = session.createQuery(sdeletessmnnumber);
		 qdeletessmnnumber.executeUpdate();
		 //删除旧经纪人ssmn_zy_user
		 Query qdeletessmnzyuser = session.createQuery(sdeletessmnzyuser);
		 qdeletessmnzyuser.executeUpdate();
					
	//-----------------------------------------
		
		//保存新经纪人ssmn_number
		for(int i=0;i<oldnumbers.size();i++)
		{
			SsmnNumber sn = oldnumbers.get(i);
			sn.setMsisdn(newmsisdn);
			sn.setInitiateDate(new Date());
			session.save(sn);
			scallingnumber = sn.getSsmnnumber();//保存一个副号码，赋值给ssmn_user表中的callingnumber
			snumbers +=sn.getSsmnnumber()+",";
		}	
		
		 //保存新的经纪人ssmn_user
		//新的经纪人
		
		olduser.setMsisdn(newmsisdn);
		olduser.setUsername(agentname);
		olduser.setSubDate(new Date());
		if(!scallingnumber.equals(""))
			olduser.setCallingnumber(scallingnumber);
		session.save(olduser);
		
		if(zyusers != null && zyusers.size()>0)
		{
			sempnotem = zyusers.get(0).getEmpno();
		}
		//查找员工编号是否存在
		List<SsmnZyUser> ulzyem  = getSsmnzyuserByEmpno(sempon,level);
		if(ulzyem != null && ulzyem.size()>0 && sempon!=null && !"".equals(sempon) && sempnotem != null && !sempnotem.equals(sempon))
			return isok = "员工编号已经存在!";
				
			//保存新经纪人的ssmn_zy_user
			for(int i=0;i<zyusers.size();i++)
			{
				SsmnZyUser zu = zyusers.get(i);
				zu.setMsisdn(newmsisdn);
				zu.setEmpno(sempon);
				zu.setName(agentname);
				zu.setLevelid(newlevelid.getId());
				zu.setIntime(new Date());
				zu.setManner(0L);
				zu.setSecondMsisdn(secondmsisdn);
				session.save(zu);
			}
			
			//更新 ssmn_zy_enablenumber表中副号码的levelid值
			if(snumbers.length() >0)
			{
				String supdateenable = " update SsmnZyEnablenumber set levelid="+newlevelid.getId()+" where enablenumber in ("+snumbers.substring(0,snumbers.length()-1)+" )  ";
				Query qsupdaen = session.createQuery(supdateenable);
				qsupdaen.executeUpdate();
			}
			
			//更新三个cancel表
			//Query qcanceluser = session.createQuery(updateSsmncancelUser);
			//qcanceluser.executeUpdate();
			//Query qcancelnumber = session.createQuery(updateSsmncancelnumber);
			//qcancelnumber.executeUpdate();
			//Query qzycanceluser = session.createQuery(updateSsmnZyCancelUser);
			//qzycanceluser.executeUpdate();
			
			//做完更新操作后，最后再保存一下更新前的记录
			//保存ssmn_cancel_user
			session.save(cu);
	
			//保存ssmn_cancel_number
			if(cancelNumsList !=null && cancelNumsList.size()>0)
			{
				for(int i=0;i<cancelNumsList.size();i++)
				{
					SsmnCancelNum num = cancelNumsList.get(i);
					session.save(num);
				}
			}
			
			//保存ssmn_zy_cancel_user
			if(zycancelUserList !=null && zycancelUserList.size()>0)
			{
				for(int i=0;i<zycancelUserList.size();i++)
				{
					SsmnZyCancelUser szu = zycancelUserList.get(i);
					session.save(szu);
				}
			}
		 
		 isok = "";
		 tx.commit();
			} catch (Exception he) {
				isok = "编辑操作失败!";
				tx.rollback();
				logger.error("-------------editUser---出现Exception-----",he);
			} finally {
				try {
					if(session != null)
						session.close();
				} catch (HibernateException he2) {
					logger.error("-------------editUser---关闭Session时出现HibernateException-----",he2);
				}
			}
			
			if(isok.equals(""))
			{
				String sRecordUserChange = ConfigMgr.getRecordUserChange();
				if(sRecordUserChange !=null && !"".equals(sRecordUserChange) && sRecordUserChange.equals("1"))
				{
					//记录到ssmn_zy_useroperation表
					//先注销老的
					if(oldnumbers !=null && oldnumbers.size()>0)
					{
						for(int i=0;i<oldnumbers.size();i++)
						{
							SsmnNumber sn = oldnumbers.get(i);
							addToUserRecord(oldmsisdn,sn.getSsmnnumber(),"",2,"");
							//再注册新的
							//先查出来副号码的类型,如果是A+类型，则写7,不是A+的写1
							List<SsmnZyEnablenumber> sen = edao.getSsmnEnableNumber(sn.getSsmnnumber());
							if(sen !=null && sen.size()>0)
							{
								if(sen.get(0).getType() == 2)
									addToUserRecord(newmsisdn,sn.getSsmnnumber(),"",7,"");
								else
									addToUserRecord(newmsisdn,sn.getSsmnnumber(),"",1,"");
							}else
								addToUserRecord(newmsisdn,sn.getSsmnnumber(),"",1,"");
							
						}
					}
				}
			}
		
		return isok;
	 
}
 
  
 public List<SsmnZyLevel> getLevelidbyBranchactiongroup(String branchactiongroup)
 {
	
	 Session session = null;
	 List<SsmnZyLevel> zu = null;
	 String sql = " from SsmnZyLevel where branchactiongroup= :branchactiongroup   ";
	 try {
			session = this.adapter.openSession();
			Query query = session.createQuery(sql.toString());
			query.setString("branchactiongroup", branchactiongroup);
			
			//if(query.list()!= null && query.list().size()>0)
				zu =query.list();
			
		} catch (Exception he) {
			logger.error("-------------getLevelidbyBranchactiongroup---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getLevelidbyBranchactiongroup---关闭Session时出现HibernateException-----",he2);
			}
		}
	 
	 return zu;
 }
 
 
	public List<SsmnNumber> getSsmnNumbersByMsisdn(String msisdn)
	{
		List<SsmnNumber> nums = null;
		Session session = null;
		String sql = " from SsmnNumber where msisdn='"+msisdn+"' ";
		try {
			
			session = this.adapter.openSession();
			Query query = session.createQuery(sql.toString());
			
			nums=query.list();
			
		} catch (Exception he) {
			logger.error("-------------getSsmnNumbersByMsisdn---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getSsmnNumbersByMsisdn---关闭Session时出现HibernateException-----",he2);
			}
		}
		
		return nums;
		
	}
	
	public List<SsmnZyUser> getssmnzyuserbyMsisdn(String msisdn)
	{
		List<SsmnZyUser> users = null;
		
		Session session = null;
		String sql = " from SsmnZyUser where msisdn='"+msisdn+"' ";
		try {
			
			session = this.adapter.openSession();
			Query query = session.createQuery(sql.toString());
			
			users=query.list();
			
		} catch (Exception he) {
			logger.error("-------------getssmnzyuserbyMsisdn---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getssmnzyuserbyMsisdn---关闭Session时出现HibernateException-----",he2);
			}
		}
		
		return users;
		
	}
	
	/**
	 * 查询经纪人的数量，去重
	 * @param name
	 * @param levelid
	 * @return
	 */
	public int getAgentInfoCount(String name,String smsisdn,String ssmnnum,String channelid,String bdparam,String zoneparam,String areaparam,String bagparam, Long levelid,String empno){
		Session session = HibernateSessionFactory.getSession();
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		Integer result = 0;
		try {
			SsmnZyLevel level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(levelid);
			StringBuffer strHQL = new StringBuffer("select u.msisdn,  row_number() over(partition by u.msisdn order by u.intime desc) as rn " +
					" from Ssmn_Zy_User u , Ssmn_Zy_Level l ,Ssmn_zy_channel c  ");
			strHQL.append(" where  u.levelid = l.id  and u.channelid =c.id  ");
			
			conn = session.connection();
			//姓名
			if(name != null && !"".equals(name.trim()))
			{
				strHQL.append(" and u.name like '%").append(name).append("%'  ");
			}
			//员工编号
			if(empno != null && !"".equals(empno))
			{
				strHQL.append(" and u.empno =  '").append(empno).append("'  ");
			}
			//主号码
			if(smsisdn != null && !"".equals(smsisdn.trim()))
			{
				strHQL.append(" and u.msisdn = '").append(smsisdn).append("'  ");
			}
			//副号码
			if(ssmnnum != null && !"".equals(ssmnnum.trim()))
			{
				strHQL.append(" and u.ssmnnumber = '").append(ssmnnum).append("'  ");
			}
			//渠道
			if(channelid != null && !"".equals(channelid))
			{
				strHQL.append(" and u.channelid = ").append(channelid);
			}
			//事业部
			if(bdparam != null && !"".equals(bdparam.trim()))
			{
				strHQL.append(" and l.businessdepartment ='").append(bdparam).append("'  ");
			}
			//战区
			if(zoneparam != null && !"".equals(zoneparam.trim()))
			{
				strHQL.append(" and l.warzone = '").append(zoneparam).append("'  ");
			}
			//片区
			if(areaparam != null && !"".equals(areaparam.trim()))
			{
				strHQL.append(" and l.area = '").append(areaparam).append("' ");
			}
			//行动组
			if(bagparam != null && !"".equals(bagparam.trim()))
			{
				strHQL.append(" and l.branchactiongroup = '").append(bagparam).append("'  ");
			}
			
			String ssql = new UtilFunctionDao().getTyadminOperatorByJdbc(level,"");
			if(Constants.TYPE_SHOWDATE < 2)
				strHQL.append("  and c.type ="+Constants.TYPE_SHOWDATE );
			
			if(ssql.length() >0){
				strHQL.append(" and u.levelid in ");
				strHQL.append(ssql.toString());
			}else{
				return result;
			}	
			
			String ssqll = " select count(*) as cn from ( " + " select msisdn, rn from  ( ";
			ssqll += strHQL;
			ssqll +=" ) where rn =1  ) ";
			
			pstmt = conn.prepareStatement(ssqll);
			
			rs = pstmt.executeQuery();
			 
			if(rs != null) {
				while(rs.next()){
					result = rs.getInt("cn");
					break;
				}
			}
			 
		} catch (Exception he) {
			logger.error("-------------getAgentInfoCount---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getAgentInfoCount---关闭Session时出现HibernateException-----",he2);
			}
		}
		
		return result;
	}
	
	
	/**
	 * 查询经纪人的信息，（以msisdn筛选出重复的信息，只取最新绑定的用户数据）
	 * @param name
	 * @param levelid
	 * @param index
	 * @param maxCount
	 * @return
	 */
	public List<zyUserDto> getAgentInfoList(String name,String smsisdn,String ssmnnum,
			String channelid,String bdparam,String zoneparam,String areaparam,String bagparam, 
			Long levelid,String empno, Integer index, Integer maxCount){
		List<zyUserDto> list = new ArrayList<zyUserDto>();
		Session session = HibernateSessionFactory.getSession();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//检测数据库连接是否成功
		if (session == null) {
			throw new DBException("fail to connect db");
		}
		List<Object> args = new ArrayList<Object>();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			//begin
			conn = session.connection();
			String secondmsis =" (case when secondmsisdn is null or secondmsisdn ='null'  then '' else secondmsisdn end) as secondmsisdn ";//字段名
			SsmnZyLevel level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(levelid);
			StringBuffer sql = new StringBuffer("select id,  name, msisdn,  businessdepartment, warzone,  area," +
					"  branchactiongroup ,empno ");
			sql.append(" , ").append(secondmsis);//要显示第二联系人的
			sql.append("  from  (select id,  name, msisdn,  businessdepartment, warzone,  area,  branchactiongroup,empno , " );
			sql.append(secondmsis).append(" , ");	//要显示第二联系人的
			
			sql.append(" rownum as rn1 from ( select u.id, u.name, u.msisdn, l.businessdepartment, l.warzone, l.area, " +
					" l.branchactiongroup ,u.empno,");
			sql.append(secondmsis).append(" , ");//要显示第二联系人的
			
			sql.append("row_number() over(partition by u.msisdn order by u.intime desc) as rn " +
					" from Ssmn_Zy_User u , Ssmn_Zy_Level l ,Ssmn_zy_channel c ");
			sql.append(" where  u.levelid = l.id  and u.channelid =c.id  ");
			
			//姓名
			if(name != null && !"".equals(name.trim()))
			{
				sql.append(" and u.name like :name");
				args.add("%"+name.trim()+"%");
			}
			//员工编号 
			if(empno != null && !"".equals(empno))
			{
				sql.append(" and u.empno =:empno");
				args.add(empno);
			}
			//主号码
			if(smsisdn != null && !"".equals(smsisdn.trim()))
			{
				sql.append(" and u.msisdn = :msisdn ");
				args.add(smsisdn );
			}
			//副号码
			if(ssmnnum != null && !"".equals(ssmnnum.trim()))
			{
				sql.append(" and u.ssmnnumber = :ssmnnumber ");
				args.add(ssmnnum);
			}
			//渠道
			if(channelid != null && !"".equals(channelid))
			{
				sql.append(" and u.channelid = :channelid ");
				args.add(channelid);
			}
			//事业部
			if(bdparam != null && !"".equals(bdparam.trim()))
			{
				sql.append(" and l.businessdepartment =:bdparam ");
				args.add(bdparam.trim());
			}
			//战区
			if(zoneparam != null && !"".equals(zoneparam.trim()))
			{
				sql.append(" and l.warzone = :warzone ");
				args.add(zoneparam.trim());
			}
			//片区
			if(areaparam != null && !"".equals(areaparam.trim()))
			{
				sql.append(" and l.area = :areaparam ");
				args.add(areaparam.trim());
			}
			//行动组
			if(bagparam != null && !"".equals(bagparam.trim()))
			{
				sql.append(" and l.branchactiongroup = :bagparam ");
				args.add(bagparam.trim());
			}
			if(Constants.TYPE_SHOWDATE <2)
				sql.append(" and c.type= "+ Constants.TYPE_SHOWDATE);
			
			String ssql = new UtilFunctionDao().getTyadminOperatorSelect(level,""," select le.id from SsmnZyLevel le where 1=1  ");
			
			if(ssql.length() >0){
				sql.append(" and u.levelid in ");
				ssql=ssql.replace("SsmnZyLevel", "Ssmn_Zy_Level");
				sql.append(ssql.toString());
				new UtilFunctionDao().setOperatorAttributejdbc(level, args);
			}else{
				return null;
			}	
			sql.append(" order by l.businessdepartment,l.warzone,l.area ,l.branchactiongroup )  where rn = 1 ) where 1 = 1 ");
			if (maxCount != null && maxCount != 0) {
//				index = (index*maxCount)-1;
				sql.append(" and rn1 >=").append(index+1);
				sql.append(" and rn1 <= ").append(index+10);
			}
			//sql.append("  order by  businessdepartment,warzone,area,branchactiongroup ");
//			logger.info("======sql=========="+sql.toString());
			
			pstmt = conn.prepareStatement(sql.toString());
			//pstmt.setMaxRows(maxCount);
			for(int i=0;i<args.size();i++){
				int seq = i+1;
				pstmt.setString(seq,(String) args.get(i));
			}
 
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()){
					list.add(new zyUserDto(rs.getLong("id"),rs.getString("name"), rs.getString("msisdn"),
							rs.getString("businessdepartment"),
							rs.getString("warzone"), rs.getString("area"),rs.getString("branchactiongroup"),
							rs.getString("empno"),rs.getString("secondmsisdn")));
				}
			}
			
			//tx.commit();
			
		}catch(Exception he){
			he.printStackTrace();
			try {
				tx.rollback();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
		finally {
			try{
				if(session != null)
					session.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
				
		return list;
		
	}
	
	
	/**
	 * 查询地产用户信息
	 * TyadminOperators opera 根据操作员的级别查询相应级别的用户信息
	 * @return
	 */
	public List<SsmnZyUser> getZYDCUserList(String msisdn,String sname,String sssmnnumber,String sempno, Integer index, Integer maxCount,TyadminOperators opera){
		Session session = null;
		
		List<SsmnZyUser> list = new ArrayList<SsmnZyUser>();
		ScrollableResults srs = null;
		try {
			//StringBuffer strHQL = new StringBuffer("select u.id, u.name, u.msisdn, u.ssmnnumber, l.branchactiongroup , " +
			//		" u.intime , c.name ,u.empno,u.remark,u.secondMsisdn from SsmnZyUser u ,SsmnZyEnablenumber e, SsmnZyChannel c  ,SsmnZyLevel l ");
			StringBuffer strHQL = new StringBuffer("select u.id, u.name, u.msisdn, (case u.ssmnnumber when '00000000000' then '' else u.ssmnnumber end), l.branchactiongroup , " +
							" u.intime , c.name ,u.empno,u.remark," +
							"(case when u.secondMsisdn is null or u.secondMsisdn ='null'  then '' else u.secondMsisdn end) as secondMsisdn ,c.type " +
							"from SsmnZyUser u , SsmnZyChannel c  ,SsmnZyLevel l ");
			strHQL.append(" where  u.channelid = c.id  and u.levelid = l.id   ");
			if(msisdn != null && !"".equals(msisdn)){
				strHQL.append(" and u.msisdn =:msisdn ");
			}
			if(sname != null && !"".equals(sname))
			{
				strHQL.append(" and u.name =:sname");
			}
			if(sssmnnumber != null && !"".equals(sssmnnumber))
			{
				strHQL.append(" and u.ssmnnumber =:sssmnnumber ");
			}
			if(sempno !=null && !"".equals(sempno))
			{
				strHQL.append(" and u.empno= :empno");
			}
			if(Constants.TYPE_SHOWDATE <2)
				strHQL.append(" and c.type =").append(Constants.TYPE_SHOWDATE);
			
			SsmnZyLevel level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
			String ssql = new UtilFunctionDao().getTyadminOperatorSelect(level, "u"," select le.id from SsmnZyLevel le where 1=1 ");
			
			if(ssql.length() >0){
				strHQL.append(" and u.levelid in ");
				strHQL.append(ssql.toString());
			}else
				return null;
			
			strHQL.append(" order by u.msisdn, u.channelid ");
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			if(msisdn != null && !"".equals(msisdn)){
				query.setString("msisdn", msisdn);
			}
			if(sname != null && !"".equals(sname))
			{
				query.setString("sname", sname);
			}
			if(sssmnnumber != null && !"".equals(sssmnnumber))
			{
				query.setString("sssmnnumber",sssmnnumber);
			}
			if(sempno !=null && !"".equals(sempno))
			{
				query.setString("empno",sempno);
			}
			new UtilFunctionDao().setOperatorAttribute(level, query);

			if (maxCount != null && maxCount != 0) {
				query.setFirstResult(index);
				query.setMaxResults(maxCount);
			}
			srs = query.scroll();
			while (srs.next()) {
					//渠道名称，分割后显示
				    String scname =UtilFunctionDao.splitStr(srs.getString(6),"_");
					list.add(new SsmnZyUser(srs.getLong(0),srs.getString(1), srs.getString(2),srs.getString(3),
							srs.getString(4),srs.getDate(5),scname,srs.getString(7),srs.getString(8),
							srs.getString(9),srs.getLong(10)));
			}
			
		} catch (Exception he) {
			logger.error("-------------getZYDCUserList---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getZYDCUserList---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
		
	}
/*经纪人信息导出到excel中*/
	public List<SsmnZyUser> getZYDCUserExportList(String msisdn,String sname,String sssmnnumber,
			String schannelbox,String sbdbox1,String szonebox1,String sareabox1,String sbagbox1,
			String sempno,TyadminOperators opera){
		Session session = HibernateSessionFactory.getSession();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<SsmnZyUser> list = new ArrayList<SsmnZyUser>();
		ScrollableResults srs = null;
		try {
			conn = session.connection();
			StringBuffer strHQL = new StringBuffer("select l.businessdepartment,l.warzone,l.area," +
					" l.branchactiongroup, u.name as uname, u.msisdn,(case u.ssmnnumber when '00000000000' then '' else u.ssmnnumber end) as ssmnnumber , c.name as cname,u.empno , " +
					" (case when replace(u.remark,'\\n','') ='null' then '' else  replace(u.remark,'\\n','')  end) " +
					" as remark , row_number() over(partition by u.msisdn order by u.intime desc) as rn " );
			strHQL.append(" , secondmsisdn ");
					
			strHQL.append(" from Ssmn_Zy_User u , Ssmn_Zy_Channel c ,Ssmn_Zy_Level l  ");
			strHQL.append(" where  u.channelid = c.id and u.levelid=l.id   ");
			//主号码
			if(msisdn != null && !"".equals(msisdn)){
				strHQL.append("  and u.msisdn ='").append(msisdn).append("'  ");
			}
			//员工编号
			if(sempno != null && !"".equals(sempno))
			{
				strHQL.append(" and u.empno = '").append(sempno).append("'  ");
			}
			//姓名
			if(sname != null && !"".equals(sname))
			{
				strHQL.append(" and u.name like '%").append(sname).append("%'  ");
			}
			//副号码
			if(sssmnnumber != null && !"".equals(sssmnnumber))
			{
				strHQL.append(" and u.ssmnnumber ='").append(sssmnnumber).append("'  ");
			}
			//渠道
			if(schannelbox != null && !"".equals(schannelbox))
			{
				strHQL.append(" and u.channelid =").append(schannelbox);
			}
			//事业部
			if(sbdbox1 != null && !"".equals(sbdbox1))
			{
				strHQL.append(" and l.businessdepartment = '").append(sbdbox1).append("'  ");
			}
			//战区
			if(szonebox1 != null && !"".equals(szonebox1) )
			{
				strHQL.append(" and l.warzone = '").append(szonebox1).append("'  ");
			}
			//片区
			if(sareabox1 != null && !"".equals(sareabox1))
			{
				strHQL.append(" and l.area = '").append(sareabox1).append("'  ");
			}
			//行动组
			if(sbagbox1 != null && !"".equals(sbagbox1))
			{
				strHQL.append(" and l.branchactiongroup = '").append(sbagbox1).append("'  ");
			}
			if(Constants.TYPE_SHOWDATE <2 )
				strHQL.append(" and c.type =").append(Constants.TYPE_SHOWDATE);
			SsmnZyLevel level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
			String ssql = new UtilFunctionDao().getTyadminOperatorByJdbc(level, "u");
			
			if(ssql.length() >0){
				strHQL.append(" and u.levelid in ");
				strHQL.append(ssql.toString());
			}else
				return null;
			
			//strHQL.append(" order by l.businessdepartment,l.warzone,l.area,l.branchactiongroup ");
			
			String ssqll = " select businessdepartment,warzone,area,branchactiongroup, uname, msisdn, "+
			"ssmnnumber, cname,empno ,remark ";
			
			//if(ConfigMgr.getIsAddSecondMsisdn().equals("1"))
			ssqll += " , secondmsisdn ";
			
			ssqll += " from ( ";
			ssqll +=  strHQL ;
			ssqll += "   )  order by businessdepartment,warzone,area,branchactiongroup ";
			
			pstmt = conn.prepareStatement(ssqll.toString());
			rs = pstmt.executeQuery();
			if(rs != null)
			{
				while(rs.next())
				{
					String scname = UtilFunctionDao.splitStr(rs.getString("cname"),"_");
					list.add(new SsmnZyUser(rs.getString("businessdepartment"),rs.getString("warzone"),
							rs.getString("area"),rs.getString("branchactiongroup"),
							rs.getString("uname"),rs.getString("msisdn"),rs.getString("ssmnnumber"),
							scname,rs.getString("empno"),rs.getString("remark")
							,rs.getString("secondmsisdn")));
				}
			}			
			
		} catch (Exception he) {
			logger.error("-------------getZYDCUserExportList---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getZYDCUserExportList---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
		
	}
	
	/**
	 * 查询用户数量
	 * @return
	 */
	public int getZYDCUserCount(String msisdn ,String sname ,String sssmnnumber,TyadminOperators opera){
		Session session = null;
		int count = 0;
		try {
			StringBuffer strHQL = new StringBuffer("select u.id, u.name, u.msisdn," +
					" u.ssmnnumber, u.groupname,u.intime , c.name from  SsmnZyUser u, SsmnZyChannel c ");
			strHQL.append("where u.channelid = c.id ");
			if(msisdn != null && !"".equals(msisdn)){
				strHQL.append(" and u.msisdn =:msisdn ");
			}
			if(sname != null && !"".equals(sname))
			{
				strHQL.append(" and u.name =:sname");
			}
			if(sssmnnumber != null && !"".equals(sssmnnumber))
			{
				strHQL.append(" and u.ssmnnumber =:sssmnnumber ");
			}
			if(Constants.TYPE_SHOWDATE <2)
				strHQL.append(" and c.type =").append(Constants.TYPE_SHOWDATE);
			
			SsmnZyLevel level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
			String ssql = new UtilFunctionDao().getTyadminOperatorSelect(level, "u"," select le.id from SsmnZyLevel le where 1=1  ");
			
			if(ssql.length() >0){
				strHQL.append(" and u.levelid in ");
				strHQL.append(ssql.toString());
			}else
				return 0;
			
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			if(msisdn != null && !"".equals(msisdn)){
				query.setString("msisdn", msisdn);
			}
			if(sname != null && !"".equals(sname))
			{
				query.setString("sname", sname);
			}
			if(sssmnnumber != null && !"".equals(sssmnnumber))
			{
				query.setString("sssmnnumber",sssmnnumber);
			}
			
			new UtilFunctionDao().setOperatorAttribute(level, query);
			
			List<SsmnZyUser> list = query.list();
			count = list.size();
		} catch (Exception he) {
			logger.error("-------------getZYDCUserCount---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getZYDCUserCount---关闭Session时出现HibernateException-----",he2);
			}
		}
		return count;
		
	}
	
	public List<SsmnZyCancelUser> getZYDCCancelUserList(String msisdn,String sname,String sssmnnumber,String sempno, 
			Integer index, Integer maxCount,TyadminOperators opera){
	
		Session session = null;
		
		List<SsmnZyCancelUser> list = new ArrayList<SsmnZyCancelUser>();
		ScrollableResults srs = null;
		try {
			StringBuffer strHQL = new StringBuffer("select u.userid, u.name, u.msisdn, " +
					"(case u.ssmnnumber when '00000000000' then '' else u.ssmnnumber end), " +
					"u.groupname, u.subDate, u.cancelDate , c.name ,u.empno," +
					"(case when u.secondMsisdn='null' then '' else u.secondMsisdn end) as secondMsisdn  " +
					"from SsmnZyCancelUser u , SsmnZyChannel c ");
			strHQL.append(" where u.channelid = c.id ");
			if(msisdn != null && !"".equals(msisdn)){
				strHQL.append(" and u.msisdn =:msisdn ");
			}
			if(sname != null && !"".equals(sname))
			{
				strHQL.append(" and u.name =:sname");
			}
			if(sssmnnumber != null && !"".equals(sssmnnumber))
			{
				strHQL.append(" and u.ssmnnumber =:sssmnnumber ");
			}
			if(sempno !=null && !"".equals(sempno))
			{
				strHQL.append(" and u.empno =:empno");
			}
			if(Constants.TYPE_SHOWDATE <2)
				strHQL.append(" and c.type =").append(Constants.TYPE_SHOWDATE);
			
			SsmnZyLevel level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
			String ssql = new UtilFunctionDao().getTyadminOperatorSelect(level, "u"," select le.id from SsmnZyLevel le where 1=1  ");
			
			if(ssql.length() >0){
				strHQL.append(" and u.levelid in ");
				strHQL.append(ssql.toString());
			}else
				return null;
			
			strHQL.append(" order by u.msisdn, u.channelid ");
			
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			if(msisdn != null && !"".equals(msisdn)){
				query.setString("msisdn", msisdn);
			}
			if(sname != null && !"".equals(sname))
			{
				query.setString("sname", sname);
			}
			if(sssmnnumber != null && !"".equals(sssmnnumber))
			{
				query.setString("sssmnnumber",sssmnnumber);
			}
			if(sempno !=null && !"".equals(sempno))
			{
				query.setString("empno", sempno);
			}
			new UtilFunctionDao().setOperatorAttribute(level, query);

			if (maxCount != null && maxCount != 0) {
				query.setFirstResult(index);
				query.setMaxResults(maxCount);
			}
			srs = query.scroll();
			while (srs.next()) {
					//渠道名称，分割后显示
					String scname = UtilFunctionDao.splitStr(srs.getString(7),"_");
					list.add(new SsmnZyCancelUser(srs.getLong(0),srs.getString(1), srs.getString(2),
							srs.getString(3),srs.getString(4),
							srs.getDate(5),srs.getDate(6),scname,srs.getString(8),
							srs.getString(9)));
			}
		
		} catch (Exception he) {
			logger.error("-------------getZYDCCancelUserList---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getZYDCCancelUserList---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
		
	}
	
	/**
	 * 查询用户数量
	 * @return
	 */
	public int getZYDCCancelUserCount(String msisdn ,String sname ,String sssmnnumber,TyadminOperators opera){
		Session session = null;
		int count = 0;
		try {
			StringBuffer strHQL = new StringBuffer("select u.userid, u.name, u.msisdn, u.ssmnnumber, u.groupname, u.subDate, u.cancelDate , c.name from SsmnZyCancelUser u , SsmnZyChannel c ");
			strHQL.append(" where u.channelid = c.id ");
			if(msisdn != null && !"".equals(msisdn)){
				strHQL.append(" and u.msisdn =:msisdn ");
			}
			if(sname != null && !"".equals(sname))
			{
				strHQL.append(" and u.name =:sname");
			}
			if(sssmnnumber != null && !"".equals(sssmnnumber))
			{
				strHQL.append(" and u.ssmnnumber =:sssmnnumber ");
			}
			if(Constants.TYPE_SHOWDATE <2)
				strHQL.append(" and c.type =").append(Constants.TYPE_SHOWDATE);
			
			SsmnZyLevel level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
			String ssql = new UtilFunctionDao().getTyadminOperatorSelect(level, "u"," select le.id from SsmnZyLevel le where 1=1 ");
			
			if(ssql.length() >0){
				strHQL.append(" and u.levelid in ");
				strHQL.append(ssql.toString());
			}else
				return 0;
			
			//strHQL.append(" order by u.msisdn, u.channelid ");
			
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			if(msisdn != null && !"".equals(msisdn)){
				query.setString("msisdn", msisdn);
			}
			if(sname != null && !"".equals(sname))
			{
				query.setString("sname", sname);
			}
			if(sssmnnumber != null && !"".equals(sssmnnumber))
			{
				query.setString("sssmnnumber",sssmnnumber);
			}
			
			new UtilFunctionDao().setOperatorAttribute(level, query);
			
			List<SsmnZyCancelUser> list = query.list();
			count = list.size();
		} catch (Exception he) {
			logger.error("-------------getZYDCCancelUserCount---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getZYDCCancelUserCount---关闭Session时出现HibernateException-----",he2);
			}
		}
		return count;
		
	}
	
/*	public List<SsmnEnablenumber> getEnableNumberList()
	{
		Session session = null;
		StringBuffer strHQL = new StringBuffer("select t.enablenumber  from SsmnEnablenumber t where t.status =7 and t.enablenumber not in (select distinct(u.ssmnnumber) from SsmnZyUser u ) and " +
												" t.enablenumber not in (select distinct(c.ssmnnumber) from SsmnNumber c) ");
		
		List<SsmnEnablenumber> list = new ArrayList<SsmnEnablenumber>();
		ScrollableResults srs = null;
		try {
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			
			srs = query.scroll();
			while (srs.next()) {
				list.add(new SsmnEnablenumber(srs.getString(0)));
			}
		} catch (Exception he) {
			logger.error("-------------getEnableNumberList---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getEnableNumberList---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
	}*/
	
	public List<SsmnZyChannel> getChannelList(Long levelid,String type,int showtype)
	{
		Session session = null;
		StringBuffer strHQL ;
		
		strHQL = new StringBuffer("select t.id,t.name from SsmnZyChannel t where t.id in( "+
					"select channelid from SsmnZyLevelChannel where levelid in "+
					" (select id from SsmnZyLevel where provincecity =(select provincecity from "+
					" SsmnZyLevel where id="+levelid+") and  company=(select company "+
					" from SsmnZyLevel where id="+levelid+") )  ) ");
		if(type !=null && !"".equals(type))
			strHQL.append(" and t.type = ").append(type) ;
		else
		{
			//根据权限配置
			if(showtype <2 )
				strHQL.append("  and t.type =").append(showtype);
		}
		
		//	strHQL = new StringBuffer("select t.id,t.name from SsmnZyChannel t ");

		List<SsmnZyChannel> list = new ArrayList<SsmnZyChannel>();
		ScrollableResults srs = null;
		try {
			session = this.adapter.openSession();
			Query query = session.createQuery(strHQL.toString());
			
			srs = query.scroll();
			while (srs.next()) {
				//分割字符中，把name中函有_的分开，例如  : 天津_管家APP
				String scname =UtilFunctionDao.splitStr(srs.getString(1),"_");
				list.add(new SsmnZyChannel(srs.getLong(0),scname));
			}
			
		} catch (Exception he) {
			logger.error("-------------getChannelList---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getChannelList---关闭Session时出现HibernateException-----",he2);
			}
		}
		return list;
	}
 
	
	public boolean bindSsmnNumber(String smsisdn, String sssmnumber, String schannel,String suserid)
	{
		
		if(smsisdn == null || "".equals(smsisdn) || sssmnumber == null || "".equals(sssmnumber))
				return false;
		
		SsmnZyUser szy = findZYUserByid(suserid);
		//查找modeid
		String smodid = "-1";
		Long ssmnumberType = 0L;
	    if(szy !=null && szy.getLevelid()>0)
	    {
			Long mid = getModeid(szy.getLevelid());
			if(mid >0)
				smodid = mid+"";
	    }
	    boolean flagSsmnnumberType = false;
		//判断渠道是否在配置的渠道中，如果是，则将ssmnnumber_type置1
		if(ConfigMgr.getChannelNames() !=null && !"".equals(ConfigMgr.getChannelNames()) 
				&& ConfigMgr.getChannelNames().length()>0  )
		{
			if(schannel !=null && !"".equals(schannel) && schannel.length() >0)
			{
				SsmnZyChannel sname = channeldao.getChannelById(Long.parseLong(schannel));
				if(sname !=null && sname.getName() !=null)
				{
					String scname = sname.getName();
					String schannelnames = ConfigMgr.getChannelNames();
					int index = schannelnames.indexOf(scname);					
					if(index >=0)
					{
						ssmnumberType =1L;
						flagSsmnnumberType =true;
					}
					else
					{
						ssmnumberType =0L;
						flagSsmnnumberType =false;
					}
				}
			}
		}
		Session session = HibernateSessionFactory.getSession();
	    Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement insertUser = null;
		PreparedStatement updateenablepre = null;
		String msg = "";
		String ssmnUserSql = "";
		boolean res = true;
	    //检测数据库连接是否成功
	    if (session == null) {
	    	throw new DBException("fail to connect db");
	    }

		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			//begin
			conn = session.connection();
			//往ssmnnumber表中写入			
	    	String ssmnumbersql ="insert into ssmn_number(SSMNNUMBER,TYPE,NUMBER_TYPE,MSISDN,CALLED_TYPE,INITIATE_DATE,SUB_MANNER,SERVICESTATUS,status,PROMPT_TYPE)"+
	    				"values('"+sssmnumber+"' ,1,0,'"+smsisdn+"' ,0,sysdate, 9,0,1,1)";
	    	
	    	
	    	if(szy != null)
	    	{
	    		//ssmn_zy_user表中写入
	    		ssmnUserSql = " insert into SSMN_ZY_USER (ID,groupname,name,msisdn,SSMNNumber,channelid,intime,levelid,empno,manner,modeid,ssmnnumber_type,remark,secondMsisdn) ";
	    		ssmnUserSql +=" values ( SEQ_SSMN_ZY_USER.nextval,'"+szy.getGroupname()+"', '"+szy.getName()+"','"+
	    		smsisdn+"','"+sssmnumber+"',"+schannel+",sysdate,"+szy.getLevelid()+",'"+szy.getEmpno()
	    		+"',0,"+smodid+","+ssmnumberType+",'"+szy.getRemark()+"', '"+
	    		szy.getSecondMsisdn()+"' ) ";
	    	}
	    	
	    	//更新副号码池中副号码的状态
	    	String supdateSsmnenable = "  update ssmn_zy_enablenumber t  set t.status= 1 , t.levelid = "+szy.getLevelid() +"  where t.enablenumber ='"+sssmnumber+"'   ";
	    	
	    	pstmt = conn.prepareStatement(ssmnumbersql);
	    	insertUser = conn.prepareStatement(ssmnUserSql);
	    	updateenablepre = conn.prepareStatement(supdateSsmnenable);
	    	//如果是isHaveSsmnNum开关打开，并且是A+渠道，则不给经纪人分配副号码，所以也不需要写ssmn_number表了 
	    	if(!sssmnumber.equals(Constants.SSMN_NUM_DEFAULT))
	    		pstmt.execute();
	    	insertUser.execute();
	    	updateenablepre.execute();
			
			tx.commit();
			
		}catch(Exception he){
			he.printStackTrace();
			try {
				tx.rollback();
				res = false;
			} catch (HibernateException e) {
				e.printStackTrace();
				res = false;
			}
		}
		finally {
			try{
				session.close();
			}catch(Exception e){
				e.printStackTrace();
				res = false;
			}
		}
		
		if(res)
		{
			String sRecordUserChange = ConfigMgr.getRecordUserChange();
			if(sRecordUserChange !=null && !"".equals(sRecordUserChange) && sRecordUserChange.equals("1"))
			{
				//记录到ssmn_zy_useroperation表
				if(flagSsmnnumberType ==true)
					addToUserRecord(smsisdn,sssmnumber,"",7,"");
				else
					addToUserRecord(smsisdn,sssmnumber,"",1,"");
			}
		}
		return res;
}

public boolean isValidNumber(String sssmnumber)
{
	boolean isOk = false;
	
	Session session = null;
	StringBuffer strHQL = new StringBuffer(" select count(*) from SsmnNumber t where t.ssmnnumber='");
		         strHQL.append(sssmnumber).append("' and t.numberType in (0,1) and servicestatus ='N'"); 
		
	StringBuffer strUser = new StringBuffer(" select count(*) from SsmnZyUser t where t.ssmnnumber='");
		strUser.append(sssmnumber).append("' ");
		
	
	ScrollableResults srs = null;
	ScrollableResults srsUser = null;
	try {
		session = this.adapter.openSession();
		Query query = session.createQuery(strHQL.toString());
		Query queryUser = session.createQuery(strUser.toString());
		
		srs = query.scroll();
		srsUser = queryUser.scroll();
		 
		srs.next();
		srsUser.next();
		int countnum = srs.getInteger(0) ;
		int countnumUser = srsUser.getInteger(0);
		if(countnum >0 || countnumUser >0)
			isOk = false;//能查到，无效
		else
			isOk = true;//查不到，有效
		
	} catch (Exception he) {
		logger.error("-------------isValidNumber---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------isValidNumber---关闭Session时出现HibernateException-----",he2);
		}
	}
	 
	return isOk;
}

public List<SsmnZyUser> checkOtherEnableNumberA(String msisdn, int entype)
{
	Session session = null;
	StringBuffer strHQL = new StringBuffer("from SsmnZyUser t ,SsmnZyEnablenumber e where " +
			" t.ssmnnumber=e.enablenumber and e.type=").append(entype).append("  and t.msisdn ='");
		strHQL.append(msisdn).append("' ");
	
	List<SsmnZyUser> list = new ArrayList<SsmnZyUser>();
	try {
		session = this.adapter.openSession();
		list = session.createQuery(strHQL.toString()).list();
		
		if(list ==null || list.size()<=0)
		{
			if(entype == 2 && ConfigMgr.getIsHaveSsmnNum().equals("1"))//针对重庆还要去查副号码为空（这里是默认值11个0）的A+经纪人
			{
				String sqll = " from SsmnZyUser t where t.msisdn='"+msisdn
				+"' and t.ssmnnumber='"+Constants.SSMN_NUM_DEFAULT+"'  ";
				
				list =session.createQuery(sqll).list();
			}
		}
		
	} catch (Exception he) {
		logger.error("-------------checkOtherEnableNumberA---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------checkOtherEnableNumberA---关闭Session时出现HibernateException-----",he2);
		}
	}
	
	return list;
	
}

public SsmnZyUser findZYUserByid(String newmsisdn)
{
	Session session = null;
	SsmnZyUser resUser = null;
	StringBuffer strHQL = new StringBuffer("from SsmnZyUser t where t.id='");
		strHQL.append(newmsisdn).append("' ");
	
	List<SsmnZyUser> list = new ArrayList<SsmnZyUser>();
	try {
		session = this.adapter.openSession();
		list = session.createQuery(strHQL.toString()).list();
		
		if(list.size()>0)
			resUser= list.get(0);
		
	} catch (Exception he) {
		logger.error("-------------findZYUserByid---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------findZYUserByid---关闭Session时出现HibernateException-----",he2);
		}
	}
	
	return resUser;
}

public SsmnZyUser findZYUserByMsisdn(String newmsisdn)
{
	Session session = null;
	SsmnZyUser resUser = null;
	StringBuffer strHQL = new StringBuffer("from SsmnZyUser t where t.msisdn='");
		strHQL.append(newmsisdn).append("' ");
	
	List<SsmnZyUser> list = new ArrayList<SsmnZyUser>();
	ScrollableResults srs = null;
	try {
		session = this.adapter.openSession();
		list = session.createQuery(strHQL.toString()).list();
		
		if(list.size()>0)
			resUser= list.get(0);
		
	} catch (Exception he) {
		logger.error("-------------getEnableNumberList---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getEnableNumberList---关闭Session时出现HibernateException-----",he2);
		}
	}
	
	return resUser;
}

//解绑
public boolean cancelBindSsmnuber(String smsisdn, String sssmnumber,String suserid)
{
	if(smsisdn == null || "".equals(smsisdn) || sssmnumber == null || "".equals(sssmnumber) || suserid == null || "".equals(suserid))
		return false;
	boolean res = true;
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	PreparedStatement insertUser = null;
	PreparedStatement updateUser = null;
	PreparedStatement deleteSSmnUser = null;
	PreparedStatement inssmncanceluser = null;
	PreparedStatement inssmncancelnumber = null;
	PreparedStatement inssmnzycanceluser = null;
	PreparedStatement updatessmnzyenablenumber = null;
	String ssmnUserSql = "";
	String updateSSmnuser = "";
	String deleteSSmnuser = "";
	//检测数据库连接是否成功
	if (session == null) {
		throw new DBException("fail to connect db");
	}
	
	Transaction tx = null;
	try{
		tx = session.beginTransaction();
		//begin
		conn = session.connection();
		
		SsmnZyUser zyuser = findZYUserByid(suserid);
		if(zyuser == null)
			res = false;
		
		//删除ssmnnumber表中的数据		
		String ssmnumbersql ="delete from ssmn_number t where t.ssmnnumber='";
		ssmnumbersql +=sssmnumber;
		ssmnumbersql +="' and t.msisdn='";
		ssmnumbersql +=smsisdn;
		ssmnumbersql +="' ";
		
		//删除ssmn_zy_user表中的数据
		ssmnUserSql = "delete from ssmn_zy_user t where t.id= ";
		ssmnUserSql += suserid;
		
		//注销用户　写入ssmn_cancel_user表，先删除，以免有主键冲突
		//String deleteSsmnCancelUser = " delete from ssmn_cancel_user t where t.msisdn='"+smsisdn+"' and t.cancel_date=sysdate";
		String insertSsmnCancelUser = "insert into ssmn_cancel_user (msisdn,pin,sub_manner,servicestatus,temp_pin,crbt_flag,username,userid,callingtype,callingnumber,sub_date,question,answer,agent_id,cancel_manner,cancel_date,usertype,devicetoken,logintime,type) "+
                      "  select msisdn,pin,sub_manner,servicestatus,temp_pin,crbt_flag,username,userid,callingtype,callingnumber,sub_date,question,answer,agent_id,cancel_manner,sysdate,usertype,devicetoken,logintime,type  " +
                      " from ssmn_user where msisdn='"+smsisdn+"'";
		//注销用户 写入ssmn_cancel_number表，先删除，以免有主键冲突
		String insertSsmnCancelNumber = " insert into ssmn_cancel_num (ssmnnumber,type,msisdn,initiate_date,cancel_date,number_type,sub_manner,cancel_manner,servicestatus,orderid,cfstatus,sfstatus,ftstatus,packageid) "+
                      " select ssmnnumber,type,msisdn,initiate_date,sysdate,number_type,sub_manner,cancel_manner,servicestatus,orderid,cfstatus,sfstatus,ftstatus,packageid from  "+
                      " ssmn_number where msisdn='"+smsisdn+"' and ssmnnumber='"+sssmnumber+"'";
		
		//注销用户 写入ssmn_zy_cancel_user表，先删除，以免有主键冲突
		String insertSsmnZycanceluser = " insert into ssmn_zy_cancel_user (userid,name,msisdn,ssmnnumber,groupname,channelid,sub_date,cancel_date,levelid,empno,modeid,remark,ssmnnumber_type,secondMsisdn ) "+
                         " select  id,name,msisdn,ssmnnumber,groupname,channelid,intime,sysdate,levelid,empno,modeid,remark,ssmnnumber_type ,secondMsisdn from ssmn_zy_user where id="+suserid;
		
		String updateSsmnZyEnablenumber = " update ssmn_zy_enablenumber t set t.status=0  where t.enablenumber='"+sssmnumber+"'  ";
		
		inssmncanceluser = conn.prepareStatement(insertSsmnCancelUser);
		inssmncancelnumber = conn.prepareStatement(insertSsmnCancelNumber);
		inssmnzycanceluser = conn.prepareStatement(insertSsmnZycanceluser);
		
		//判断解绑的副号码是不是 ssmn_user表中的CALLINGNUMBER（来电显示的号码），如果是，要把这个号码换成其他的 有效的副号码
		boolean isOk = isSSMNUserCallInnumber(smsisdn,sssmnumber);
		if(!isOk)
		{
			//说明解绑的是此副号码
			//查询一个有效的副号码
			String snumber = getOKSSmnnumber(smsisdn,sssmnumber);
			if(snumber != null && !"".equals(snumber))
			{
				updateSSmnuser = " update ssmn_user t set t.callingnumber = '"; 
				updateSSmnuser += snumber;
				updateSSmnuser += "' where t.msisdn='";
				updateSSmnuser +=smsisdn;
				updateSSmnuser +="' ";
			}else
			{
				//注销业务
				//注销用户之前，先写入cancel表
				//写入ssmn_cancel_user表
				inssmncanceluser.execute();	
				
				deleteSSmnuser = " delete ssmn_user t where t.msisdn ='";
				deleteSSmnuser +=smsisdn;
				deleteSSmnuser += "' ";
			}
		} 
		
		//写入ssmn_cancel_number表
		inssmncancelnumber.execute();
		
		//写入ssmn_zy_cancel_user表
		inssmnzycanceluser.execute();	
		
		//insertCanUser = conn.prepareStatement(ssmnCancelUserSql);
		pstmt = conn.prepareStatement(ssmnumbersql);
		insertUser = conn.prepareStatement(ssmnUserSql);
		if(updateSSmnuser != null && !"".equals(updateSSmnuser) )
			updateUser = conn.prepareStatement(updateSSmnuser);
		if(deleteSSmnuser != null && !"".equals(deleteSSmnuser) )
			deleteSSmnUser = conn.prepareStatement(deleteSSmnuser);
		
		updatessmnzyenablenumber = conn.prepareStatement(updateSsmnZyEnablenumber);
		
				
		//更新ssmn_user表中的呼叫号码字段为 查出的有效的副号码
		if(updateSSmnuser != null && !"".equals(updateSSmnuser) )
			updateUser.execute();
		//删除ssmn_user
		if(deleteSSmnuser != null && !"".equals(deleteSSmnuser) )
			deleteSSmnUser.execute();
		
		//删除ssmnnumber表中的数据
		pstmt.execute();
		
		//删除ssmn_zy_user表中的数据
		insertUser.execute();
		
		//更新ssmn_zy_enablenumber表中副号码的状态
		updatessmnzyenablenumber.execute();
		
		tx.commit();
		
	}catch(Exception he){
		he.printStackTrace();
		try {
			tx.rollback();
			res = false;
		} catch (HibernateException e) {
			e.printStackTrace();
			res = false;
		}
	}
	finally {
		try{
			session.close();
		}catch(Exception e){
			e.printStackTrace();
			res = false;
		}
	}
	if(res)
	{
		String sRecordUserChange = ConfigMgr.getRecordUserChange();
		if(sRecordUserChange !=null && !"".equals(sRecordUserChange) && sRecordUserChange.equals("1"))
		{
			//记录到ssmn_zy_useroperation表
			addToUserRecord(smsisdn,sssmnumber,"",2,"");
		}
	}
	return res;
	
}
/**换绑 :
 * 用户Ａ（已经存在的合法的经纪人）　，用户Ｂ（已经存在的合法的经纪人），
 * 换绑：1)　给用户Ｂ新添加一个副号码（Ａ的副号码），
 *     2) 再把Ａ的副号码删除掉，
 *     		a)如果Ａ还有其他其他副号码，只删除换绑的副号码，（注意，如果Ａ的主叫号码是该副号码的情况，也要把A的主叫号码换掉）
 *        	b)如果Ａ没有其他副号码了，注销掉Ａ用户
 *     
**/
public String switchBindSsmnuber(String smsisdn, String sssmnumber, String schannel,String suserid,String newsmsidn)
{
	String reMsg = "";
	if(smsisdn == null || "".equals(smsisdn) || sssmnumber == null || "".equals(sssmnumber))
		return reMsg="换绑的数据不合法!";
	
	SsmnZyUser szy = findZYUserByMsisdn(newsmsidn);
	String smodid = "-1";
	Long ssmnnumType = 0L;
	if(szy !=null && szy.getLevelid()>0)
    {
		Long mid = getModeid(szy.getLevelid());
		if(mid >0)
			smodid = mid+"";
    }
	//判断渠道是否在配置的渠道中，如果是，则将ssmnnumber_type置1
	if(ConfigMgr.getChannelNames() !=null && !"".equals(ConfigMgr.getChannelNames()) 
			&& ConfigMgr.getChannelNames().length()>0 )
	{
		if( schannel !=null && !"".equals(schannel) && schannel.length()>0)
		{
			SsmnZyChannel sname = channeldao.getChannelById(Long.parseLong(schannel));
			if(sname !=null && sname.getName() !=null)
			{
				String scname = sname.getName();
				String schannelnames = ConfigMgr.getChannelNames();
				int index = schannelnames.indexOf(scname);					
				if(index >=0)
					ssmnnumType =1L;
				else
					ssmnnumType = 0L;
			}
		}
	}
	
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	PreparedStatement insertUser = null;
	PreparedStatement deletenumber = null;
	PreparedStatement deleteUser = null;
	PreparedStatement deletessmnuserpre = null;
	//PreparedStatement insertssmnuserpre = null;
	PreparedStatement inssmncanceluser = null;
	PreparedStatement inssmncancelnumber = null;
	PreparedStatement inssmnzycanceluser = null;
	PreparedStatement updateusercallingnumber = null;
	PreparedStatement updatezyenablenumlevelid = null;
	String ssmnUserSql = "";
	String sinsertssmnusersql = "";
	//boolean res = true;
	
	//检测数据库连接是否成功
	if (session == null) {
		throw new DBException("fail to connect db");
	}
	
	Transaction tx = null;
	try{
		tx = session.beginTransaction();
		//begin
		conn = session.connection();
		
		//先查出ssmn_user表中的数据
		SsmnUser suser = getssmnuserByMsisdn(smsisdn);//这个只是验证一下被换绑的用户是否合法
		if(suser == null )//如果连主号码这个用户都不存在，则直接返回失败
			return reMsg="被换绑的经纪人不合法!";
		
		//换绑的用户，必须是已经存在的合法的用户, 先查，换成的号码是否在ssmn_user,ssmn_number,ssmn_zy_user表中存在,
		ZydcRecord zyrec = isExistInUser(newsmsidn);
		if(zyrec == null)//换绑的用户不存在
			return reMsg ="无该经纪人!";
		
				
		//往ssmnnumber表中写入			
		String ssmnumbersql ="insert into ssmn_number(SSMNNUMBER,TYPE,NUMBER_TYPE,MSISDN,CALLED_TYPE,INITIATE_DATE,SUB_MANNER,SERVICESTATUS,status,PROMPT_TYPE)"+
					"values('"+sssmnumber+"' ,1,0,'"+newsmsidn+"' ,0,sysdate, 9,0,1,1)";
		
		//向ssmn_zy_user中添加一条数据，副号码为新换的副号码
		
		if(szy != null)
		{
			//ssmn_zy_user表中写入
			ssmnUserSql = " insert into SSMN_ZY_USER (ID,groupname,name,msisdn,SSMNNumber,channelid,intime,levelid,empno,manner,modeid,ssmnnumber_type,remark,secondMsisdn) ";
			ssmnUserSql +=" values ( SEQ_SSMN_ZY_USER.nextval,'"+szy.getGroupname()+"', '"+szy.getName()
			+"','"+newsmsidn+"','"+sssmnumber+"',"+schannel+",sysdate,"+szy.getLevelid()
			+",'"+szy.getEmpno()+"',0,"+smodid+","+ssmnnumType+",'"+szy.getRemark()+"','"+szy.getSecondMsisdn()+"') ";
		
			//写cancel_user表会出问题，如果换绑完，再解绑，由于 userid已经存在，就会有主键冲突的问题
			//String subtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( szy.getIntime());
			/*ssmnCancelUserSql = "insert into ssmn_zy_cancel_user t (t.userid,t.name,t.msisdn,t.ssmnnumber,t.groupname,t.channelid,t.sub_date,t.cancel_date,t.company,t.businessdepartment,t.warzone,t.area,t.branchactiongroup,t.provincecity ) "
							+" values ("+szy.getId()+",'"+szy.getName()+"', '"+szy.getMsisdn()+"','"+szy.getSsmnnumber()+"','"+
							szy.getGroupname()+"',"+szy.getChannelid()+",to_date('"+subtime+"','yyyy-mm-dd hh24:mi:ss'),sysdate, '"+szy.getCompany()+"','"+szy.getBusinessdepartment()+"','"+
							szy.getWarzone()+"', '"+szy.getArea()+"','"+szy.getBranchactiongroup()+"','"+szy.getProvincecity()+"') ";
		*/
		}else
			return reMsg ="无该经纪人!";
		
		//如果用户Ａ没有其他副号码了,删除的sql，如果有其他副号码，下面有判断
		//删除用户Ａ　的　ssmnnumber表中的数据		
		String deletessmnumbersql ="delete from ssmn_number t where t.ssmnnumber='";
		deletessmnumbersql +=sssmnumber;
		//deletessmnumbersql +="' and t.msisdn='";
		//deletessmnumbersql +=smsisdn;
		deletessmnumbersql +="' ";
		
		//删除用户Ａ　ssmn_zy_user表中的数据
		String deletessmnUserSql = "delete from ssmn_zy_user t where t.id= ";
		deletessmnUserSql += suserid;
		
		//删除用户Ａ　ssmn_user表中的数据
		String deletessmnuser = " delete from ssmn_user t where t.msisdn='";
		deletessmnuser += smsisdn;
		deletessmnuser +="' ";
		
		//注销用户Ａ　写入ssmn_cancel_user表，先删除，以免有主键冲突
		//String deleteSsmnCancelUser = " delete from ssmn_cancel_user t where t.msisdn='"+smsisdn+"' and t.cancel_date=sysdate";
		String insertSsmnCancelUser = "insert into ssmn_cancel_user (msisdn,pin,sub_manner,servicestatus,temp_pin,crbt_flag,username,userid,callingtype,callingnumber,sub_date,question,answer,agent_id,cancel_manner,cancel_date,usertype,devicetoken,logintime,type) "+
                      "  select msisdn,pin,sub_manner,servicestatus,temp_pin,crbt_flag,username,userid,callingtype,callingnumber,sub_date,question,answer,agent_id,cancel_manner,sysdate,usertype,devicetoken,logintime,type  " +
                      " from ssmn_user where msisdn='"+smsisdn+"'";
		//注销用户Ａ 写入ssmn_cancel_number表，先删除，以免有主键冲突
		String insertSsmnCancelNumber = " insert into ssmn_cancel_num (ssmnnumber,type,msisdn,initiate_date,cancel_date,number_type,sub_manner,cancel_manner,servicestatus,orderid,cfstatus,sfstatus,ftstatus,packageid) "+
                      " select ssmnnumber,type,msisdn,initiate_date,sysdate,number_type,sub_manner,cancel_manner,servicestatus,orderid,cfstatus,sfstatus,ftstatus,packageid from  "+
                      " ssmn_number where msisdn='"+smsisdn+"' and ssmnnumber='"+sssmnumber+"'";
		
		//注销用户Ａ 写入ssmn_zy_cancel_user表，先删除，以免有主键冲突
		String insertSsmnZycanceluser = " insert into ssmn_zy_cancel_user (userid,name,msisdn,ssmnnumber,groupname,channelid,sub_date,cancel_date,levelid,empno,manner,operatype,modeid,remark,ssmnnumber_type,secondMsisdn) "+
                         " select  id,name,msisdn,ssmnnumber,groupname,channelid,intime,sysdate,levelid ,empno,0,0 ,modeid,remark,ssmnnumber_type ,secondMsisdn from ssmn_zy_user where id="+suserid;
		
		//更新ssmn_zy_enablenumber表中的levelid 
		String updateenlevelid = " update ssmn_zy_enablenumber set levelid ="+szy.getLevelid()+"  where enablenumber ='"+sssmnumber+"'   ";
		
		pstmt = conn.prepareStatement(ssmnumbersql);
		insertUser = conn.prepareStatement(ssmnUserSql);
		//insertCanUser = conn.prepareStatement(ssmnCancelUserSql);
		deletenumber = conn.prepareStatement(deletessmnumbersql);
		deleteUser = conn.prepareStatement(deletessmnUserSql);
		deletessmnuserpre = conn.prepareStatement(deletessmnuser);
		inssmncanceluser = conn.prepareStatement(insertSsmnCancelUser);
		inssmncancelnumber = conn.prepareStatement(insertSsmnCancelNumber);
		inssmnzycanceluser = conn.prepareStatement(insertSsmnZycanceluser);
		updatezyenablenumlevelid= conn.prepareStatement(updateenlevelid);
				
		//删除用户Ａ在 ssmn_user表时，先判断，该用户是否还有其他的副号码，如果有，则不能删除，如果没有，再删除
		//判断该号码在ssmn_number表中是否有副号码
		boolean isexitssmnnumber = isexistSsmnNumber(sssmnumber,smsisdn);
		//System.out.print(deletessmnuser);
		if(!isexitssmnnumber)//ssmn_number表中没有其他副号码，则注销用户
		{
			//注销用户之前，先写入cancel表
			//写入ssmn_cancel_user表
			inssmncanceluser.execute();
			
			//写入ssmn_cancel_number表
			inssmncancelnumber.execute();
			
			//删除用户Ａ在ssmn_user表
			deletessmnuserpre.execute();
		}
		else
		{
			//用户Ａ还有其他副号码，判断如果ssmn_user表中的主叫号码是否是被换的副号码
			if(suser != null)
			{
				if(!"".equals(suser.getCallingnumber()) && suser.getCallingnumber().equals(sssmnumber)) 
				{
					//取用户Ａ的其他的副号码 
					String ssmnok = getOKSSmnnumber(smsisdn,sssmnumber);
					
					//更新ssmn_user表的　Callingnumber字段
					//String updatessmnuserCallingnumber = " select ssmnnumber from ssmn_number where msisdn='"+smsisdn+"'  and rownum = 1  and ssmnnumber !='sssmnumber"+"' ";
					if(ssmnok.length() >0)
					{
						String updatessmnuserCallingnumber = "  update ssmn_user set callingnumber ='"+ssmnok+"' where msisdn='"+smsisdn+"'";
						updateusercallingnumber = conn.prepareStatement(updatessmnuserCallingnumber);
						updateusercallingnumber.execute();
					}
				}
					
			}
		}
		//写入ssmn_zy_cancel_user表
		inssmnzycanceluser.execute();
		
		//删除ssmn_number表
		//System.out.print(deletessmnumbersql);
		deletenumber.execute();
		
		//删除用户Ａ在ssmn_ZY_user表,换绑后的副号码
		//System.out.print(deletessmnUserSql);
		deleteUser.execute();
		
		//写入ssmn_number表
		//System.out.print(ssmnumbersql);
		pstmt.execute();

		//用户Ｂ　写入ssmn_zy_user表
		//System.out.print(ssmnUserSql);
		insertUser.execute();
		
		//更新ssmn_zy_enablenumber表中的levelid 
		updatezyenablenumlevelid.execute(); 
		
		tx.commit();
		
	}catch(Exception he){
		he.printStackTrace();
		try {
			tx.rollback();
			if(session != null)
				session.close();
			reMsg="换绑失败!";
		} catch (HibernateException e) {
			e.printStackTrace();
			reMsg="换绑失败!";
		}
	}
	finally {
		try{
			session.close();
		}catch(Exception e){
			e.printStackTrace();
			reMsg="换绑失败!";
		}
	}
	
	if(reMsg.equals(""))
	{
		String sRecordUserChange = ConfigMgr.getRecordUserChange();
		if(sRecordUserChange !=null && !"".equals(sRecordUserChange) && sRecordUserChange.equals("1"))
		{
			//记录到ssmn_zy_useroperation表
			addToUserRecord(smsisdn,sssmnumber,newsmsidn,4,"");
		}
	}
	return reMsg;
}

public ZydcRecord isExistInUser(String newSsmnUser)
{
	//boolean isexit = false;//有  false表示没有
	//if(newSsmnUser.length() <=0)
	//	return true;
	
	Session session = null;

	StringBuffer strHQL = new StringBuffer(" select z.msisdn,z.name,z.ssmnnumber,c.name from SsmnUser u,SsmnNumber n,SsmnZyUser z, SsmnZyChannel c "  +
" where u.msisdn=n.msisdn and n.msisdn=z.msisdn and n.ssmnnumber=z.ssmnnumber and z.channelid=c.id and u.msisdn='");
		strHQL.append(newSsmnUser).append("' ");
	
	ZydcRecord zyrec = null;
	ScrollableResults srs = null;

	try {
		session = this.adapter.openSession();
		Query query = session.createQuery(strHQL.toString());
		
		srs = query.scroll();
		while (srs.next()) {
			zyrec =new ZydcRecord(srs.getString(0),srs.getString(1),srs.getString(2),srs.getString(3));
			break;
		}
				
	} catch (Exception he) {
		logger.error("-------------getEnableNumberList---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getEnableNumberList---关闭Session时出现HibernateException-----",he2);
		}
	}
	

	return zyrec;
}

public String isSameArea(String oldmsisdn,String newmsisdn)
{
	
	String res = "";
	
	Session session = null;
	SsmnNumber resNumber = null;
	String sfieldname = "";//字段名

	String smatchnum = ConfigMgr.getMatchNumberLevel();//获取到配置文件配置的分配副号码的级别
	/*
	 * 根据ssmn_zy_user表中的id查出对应的片区名称
	 */
	sfieldname =getFileldNameByLevel();
	if(sfieldname.length() <= 0)
		return res="没有配置限制分配号码区域!";
	
	String tem = " select z.name  from SsmnZyUser z, SsmnZyLevel l  where z.levelid = l.id and z.msisdn='"+oldmsisdn+"'  ";
	tem += "  and "+sfieldname;
	tem += " in ( select "+sfieldname;
	tem +="  from SsmnZyUser z, SsmnZyLevel l  where z.levelid = l.id and z.msisdn='"+newmsisdn+"'  ";
	tem +="  ) and rownum = 1 ";
	
	
	try {
		session = this.adapter.openSession();
		Query querycom = session.createQuery(tem.toString());
		res = (String)querycom.uniqueResult();
		
		if(res == null  || res.equals(""))
		{
			res = "";//为空，说明不在同一区域
		}
		
	} catch (Exception he) {
		logger.error("-------------getEnableNumberList---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getEnableNumberList---关闭Session时出现HibernateException-----",he2);
		}
	}
	
	return res;
}

/*
 * 根据配置的级别，返回字段名称
 */
public String getFileldNameByLevel()
{
	String sfieldname = "";
	String smatchnum = ConfigMgr.getMatchNumberLevel();//获取到配置文件配置的分配副号码的级别
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

public boolean updateChannel(String suid,String channelid,String msisdn,String ssmnumber)
{
	Session session = null;
	String sqltemp = "";
	int sres = 0;
	Long ssmnnumType = 0L;
	List<SsmnZyUser> list = new ArrayList<SsmnZyUser>();
	
	//判断渠道是否在配置的渠道中，如果是，则将ssmnnumber_type置1
	if(ConfigMgr.getChannelNames() !=null && !"".equals(ConfigMgr.getChannelNames()) 
			&& ConfigMgr.getChannelNames().length()>0 )
	{
		if( channelid !=null && !"".equals(channelid) && channelid.length()>0)
		{
			SsmnZyChannel sname = channeldao.getChannelById(Long.parseLong(channelid));
			if(sname !=null && sname.getName() !=null)
			{
				String scname = sname.getName();
				String schannelnames = ConfigMgr.getChannelNames();
				int index = schannelnames.indexOf(scname);					
				if(index >=0)
					ssmnnumType =1L;
				else
					ssmnnumType = 0L;
			}
		}
	}
	//String sql = " update SsmnZyUser set channelid="+channelid +"  where 1=1 ";
	//先查出来
	String sql = " from SsmnZyUser where 1=1 ";
	
	if(suid != null && !"".equals(suid))
		sqltemp += " and id="+suid ;
	
	if(msisdn !=null && !"".equals(msisdn) && ssmnumber!= null && !"".equals(ssmnumber))
	{
		sqltemp += " and msisdn='"+msisdn+"' and ssmnnumber='"+ssmnumber+"' ";
	}
		
	Transaction tx = null;
	try {
		session = this.adapter.openSession();
		tx = session.beginTransaction();
		
		Query query = session.createQuery(sql.toString()+sqltemp.toString());
		list = query.list();
		if(list !=null && list.size()>0)
		{
			//先放入ssmn_zy_cancel表
        	for(int i=0;i<list.size();i++)
        	{
	        	SsmnZyUser u = list.get(i);
				SsmnZyCancelUser zycu = new SsmnZyCancelUser();
				zycu.setUserid(u.getId());
				zycu.setName(u.getName());
				zycu.setCancelDate(new Date());
				zycu.setLevelid(u.getLevelid());
				zycu.setChannelid(u.getChannelid());
				zycu.setSsmnnumber(u.getSsmnnumber());
				zycu.setMsisdn(u.getMsisdn());
				zycu.setSubDate(u.getIntime());
				zycu.setGroupname(u.getGroupname());
				zycu.setEmpno(u.getEmpno());
				zycu.setManner(0L);
				zycu.setOperaType(1L);
				zycu.setModeid(u.getModeid());
				zycu.setRemark(StringUtils.defaultIfEmpty(u.getRemark(), ""));
				zycu.setSsmnnumbertype(u.getSsmnnumbertype());
				zycu.setSecondMsisdn(StringUtils.defaultIfEmpty(u.getSecondMsisdn(), ""));
				session.save(zycu);
        	}
        	//从ssmn_zy_user表中删除
        	String delzyuser = "delete from SsmnZyUser where 1=1" +sqltemp;
        	Query queryd = session.createQuery(delzyuser);
        	sres = queryd.executeUpdate();
			if(sres < 1 ){  //有失败的则回滚
				tx.rollback();
				return false;
			}
			//写入ssmn_zy_user表
        	if(list !=null && list.size()>0)
        	{
        		for(int i=0;i<list.size();i++)
        		{
        			SsmnZyUser u = list.get(i);
        			SsmnZyUser newu = new SsmnZyUser();
        			newu.setSsmnnumber(u.getSsmnnumber());
        			newu.setName(u.getName());
        			newu.setMsisdn(u.getMsisdn());
        			newu.setGroupname(u.getGroupname());
        			newu.setChannelid(Long.parseLong(channelid));       			
        			newu.setIntime(u.getIntime());
        			newu.setLevelid(u.getLevelid());
        			newu.setEmpno(u.getEmpno());
        			newu.setManner(0L);
        			newu.setSsmnnumbertype(ssmnnumType);
       				newu.setRemark(StringUtils.defaultIfEmpty(u.getRemark(), "") );
        			newu.setModeid(u.getModeid());
        			newu.setSecondMsisdn(StringUtils.defaultIfEmpty(u.getSecondMsisdn(), ""));
        			session.save(newu);
        		}
        	}
		}
	
		if(sres <1)
		{
			tx.rollback();
			return false;
		}
		tx.commit();		
	}catch(Exception he){
		he.printStackTrace();
		try {
			tx.rollback();
			
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
	finally {
		try{
			if(session != null)session.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	return true;
}

public boolean isexistSsmnNumber(String ssmnnumber,String smsisdn)
{
	boolean isexit = false;//有  false表示没有
	
	Session session = null;
	SsmnNumber resNumber = null;
	StringBuffer strHQL = new StringBuffer("from SsmnNumber t where t.msisdn= '");
		strHQL.append(smsisdn).append("'  and t.ssmnnumber != '").append(ssmnnumber).append("' ");
	
	List<SsmnNumber> list = new ArrayList<SsmnNumber>();

	try {
		session = this.adapter.openSession();
		list = session.createQuery(strHQL.toString()).list();
		
		if(list.size()>0)
		{
			resNumber=list.get(0);
			isexit = true; //有
		}else
			isexit = false;//没有
		
	} catch (Exception he) {
		logger.error("-------------getEnableNumberList---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getEnableNumberList---关闭Session时出现HibernateException-----",he2);
		}
	}
	

	return isexit;
}

public List<SsmnZyUser> getZyUserlList(SsmnZyLevel level)
{
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//检测数据库连接是否成功
	if (session == null) {
		throw new DBException("fail to connect db");
	}
	
	List<SsmnZyUser> list = new ArrayList<SsmnZyUser>();
	Transaction tx = null;
	try {
		tx = session.beginTransaction();
		//begin
		conn = session.connection();
		
		StringBuffer strHQL1 = new StringBuffer("select distinct(t.msisdn)  as msdn,t.name from Ssmn_Zy_User t  where 1=1 ");
//		SsmnZyLevel level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
		String ssql = new UtilFunctionDao().getTyadminOperatorByJdbc(level, "t");
		
		if(ssql.length() >0){
			strHQL1.append(" and t.levelid in ");
			strHQL1.append(ssql.toString());
		}else
			return null;
		
		//解绑的用户
		StringBuffer strHQL2 = new StringBuffer("select distinct(t.msisdn)  as msdn,t.name from Ssmn_Zy_cancel_User t  where 1=1 ");
//		SsmnZyLevel level = (SsmnZyLevel) LevelDao.getInstance().getByPrimaryKey(opera.getLevelid());
		
		if(ssql.length() >0){
			strHQL2.append(" and t.levelid in ");
			strHQL2.append(ssql.toString());
		}else
			return null;
		
		
		String sqll = " select distinct(msdn) as ms,name from ( "; 
		sqll +=strHQL1;
		sqll += "  union ";
		sqll +=strHQL2;
		sqll +="  ) ";
		
		
		pstmt = conn.prepareStatement(sqll.toString());
		rs = pstmt.executeQuery();
		if(rs != null) {
			while (rs.next()) {
			list.add(new SsmnZyUser(rs.getString("ms"),rs.getString("name")));
		}
		}
	} catch (Exception he) {
		logger.error("-------------getEnableNumberList---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getEnableNumberList---关闭Session时出现HibernateException-----",he2);
		}
	}
	return list;
}


public SsmnUser getssmnuserByMsisdn(String msisid)
{
		Session session = null;
		SsmnUser resUser = null;
		StringBuffer strHQL = new StringBuffer("from SsmnUser t where t.msisdn= '");
			strHQL.append(msisid).append("' ");
		
		List<SsmnUser> list = new ArrayList<SsmnUser>();
		ScrollableResults srs = null;
		try {
			session = this.adapter.openSession();
			list = session.createQuery(strHQL.toString()).list();
							
			if(list != null && list.size()>0)
				resUser=list.get(0);
			
		} catch (Exception he) {
			logger.error("-------------getEnableNumberList---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getEnableNumberList---关闭Session时出现HibernateException-----",he2);
			}
		}
		
		return resUser;
	
}

public boolean isSSMNUserCallInnumber(String smsisdn,String snumber)
{
	Session session = null;
	boolean res = true;
	StringBuffer strHQL = new StringBuffer("select t.msisdn from SsmnUser t where t.msisdn='");
	strHQL.append(smsisdn).append("'  and t.callingnumber='");
	strHQL.append(snumber).append("' ");
	
	//List<SsmnZyUser> list = new ArrayList<SsmnZyUser>();
	ScrollableResults srs = null;
	try {
		session = this.adapter.openSession();
		Query query = session.createQuery(strHQL.toString());
		
		int intsrs = query.list().size();
		if(intsrs >0) {
			
			res = false;
		}
	} catch (Exception he) {
		res = false;
		logger.error("-------------getEnableNumberList---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getEnableNumberList---关闭Session时出现HibernateException-----",he2);
		}
	}
	return res;
}

//查询一个有效的副号码
public String getOKSSmnnumber(String smsisdn,String snumber)
{
	String sresult = "";
	if(smsisdn == null || smsisdn.equals("") || snumber == null || snumber.equals(""))
		return sresult;
	
	Session session = null;
	StringBuffer strHQL = new StringBuffer(" select t.ssmnnumber from SsmnNumber t where t.msisdn='");
	strHQL.append(smsisdn).append("' and t.ssmnnumber != '").append(snumber).append("' and t.numberType in (0,1) and t.type = 1 ");
	 
	
	//List<SsmnZyUser> list = new ArrayList<SsmnZyUser>();
	ScrollableResults srs = null;
	try {
		session = this.adapter.openSession();
		Query query = session.createQuery(strHQL.toString());

		srs = query.scroll();
		while (srs.next()) {
			sresult = srs.getString(0);
			break;
 
		}
	} catch (Exception he) {
		logger.error("-------------getEnableNumberList---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getEnableNumberList---关闭Session时出现HibernateException-----",he2);
		}
	}
	return sresult;
}

//根据channelid获取到channelname
public String getChannelNamebyId(String schannelid)
{
	String sresult = "";
	if(schannelid == null || schannelid.equals("") )
		return sresult;
	
	Session session = null;
	StringBuffer strHQL = new StringBuffer(" select t.name from SsmnZyChannel t where t.id=");
	strHQL.append(schannelid);
	 
	
	//List<SsmnZyUser> list = new ArrayList<SsmnZyUser>();
	ScrollableResults srs = null;
	try {
		session = this.adapter.openSession();
		Query query = session.createQuery(strHQL.toString());

		srs = query.scroll();
		while (srs.next()) {
			sresult = srs.getString(0);
			break;
 
		}
	} catch (Exception he) {
		logger.error("-------------getEnableNumberList---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getEnableNumberList---关闭Session时出现HibernateException-----",he2);
		}
	}
	return sresult;
}

public String isValidUser(String smsisdn,String ssmnnum)
{
	String res = "";
	if(smsisdn == null || "".equals(smsisdn) || ssmnnum== null || "".equals(ssmnnum))
		return res="数据不合法!";
	Session session = null;
	ScrollableResults srs = null;
		try {
			session = this.adapter.openSession();
			//先查询ssmn_user表
			SsmnUser su = (SsmnUser) session.get(SsmnUser.class, smsisdn);
			if(su == null)
				return res = "该用户不存在!";
			//查询ssmn_number表
			String sqlssmnnumber = "select ssmnnumber from SsmnNumber where ssmnnumber='"+ssmnnum+"' and msisdn='"+smsisdn+"' ";
			Query queryssmnnum = session.createQuery(sqlssmnnumber);

			String ssmnres = (String) queryssmnnum.uniqueResult();
			if(ssmnres == null || "".equals(ssmnres))
				return res="此用户没有该副号码!";
			//查询ssmn_zy_user表
			String sqlzyuser = "select msisdn from SsmnZyUser where ssmnnumber='"+ssmnnum+"' and msisdn='"+smsisdn+"' ";
			Query queryzyuser = session.createQuery(sqlzyuser);
			String sszyuser = (String) queryzyuser.uniqueResult();
			if(sszyuser == null)
				return res="此用户没有该副号码!";
			
		} catch (Exception he) {
			logger.error("-------------getEnableNumberList---出现Exception-----",he);
		} finally {
			try {
				if(session != null)
					session.close();
			} catch (HibernateException he2) {
				logger.error("-------------getEnableNumberList---关闭Session时出现HibernateException-----",he2);
			}
		}
	
	return res;
	
}

public String getSsmnzyuserId(String smsisdn,String ssmnnum)
{
	String res = "";
	Session session = null;
	ScrollableResults srs = null;
	List<SsmnZyUser> list = null;
	try {
		session = this.adapter.openSession();
		
		String sqlzyuser = " from SsmnZyUser where ssmnnumber='"+ssmnnum+"' and msisdn='"+smsisdn+"' ";
		Query queryzyuser = session.createQuery(sqlzyuser);
		list= queryzyuser.list();
		if(list != null && list.size()>0)
			res = list.get(0).getId()+"";
		else
			res="";
				
	} catch (Exception he) {
		logger.error("-------------getEnableNumberList---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getEnableNumberList---关闭Session时出现HibernateException-----",he2);
		}
	}
	return res;
}

public List<SsmnZyUser> getSsmnzyuserByEmpno(String sempno,SsmnZyLevel level)
{
	String res = "";
	Session session = null;
	ScrollableResults srs = null;
	List<SsmnZyUser> list = new ArrayList<SsmnZyUser>();
	try {
		session = this.adapter.openSession();
		
		String sqlzyuser = "select t.id,t.name,t.msisdn,t.empno,t.levelid  from SsmnZyUser t,SsmnZyLevel l where t.levelid=l.id and t.empno= :sempno  ";
		sqlzyuser += " and l.provincecity=:provincecity  and l.company= :company ";
		Query queryzyuser = session.createQuery(sqlzyuser);
		if(sempno !=null && sempno.length()>0)
			queryzyuser.setString("sempno", sempno);
		if(level !=null && level.getProvincecity() !=null && level.getProvincecity().length()>0)
			queryzyuser.setString("provincecity", level.getProvincecity());
		if(level !=null && level.getCompany() !=null && level.getCompany().length()>0)
			queryzyuser.setString("company", level.getCompany());
		//list= queryzyuser.list();
		srs =queryzyuser.scroll();
		while(srs.next())
		{
			list.add(new SsmnZyUser(srs.getLong(0),srs.getString(1),srs.getString(2),
					srs.getString(3),srs.getLong(4)));
		}
				
				
	} catch (Exception he) {
		logger.error("-------------getEnableNumberList---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getEnableNumberList---关闭Session时出现HibernateException-----",he2);
		}
	}
	return list;
}

//根据levelid查用户是否存在
public boolean getUserByLevelid(String mbusin,String mwarzone,String marea,String mbranch,Long operalevelid)
{
	Session session = null;
	List<SsmnZyUser> list = null;
	try {
		
		session = this.adapter.openSession();
		String sql = "select u.id from SsmnZyUser u,SsmnZyLevel l where u.levelid=l.id  ";
		if(operalevelid >0)//保证是登录的操作员公司下面的
			sql +=" and l.provincecity=(select provincecity from SsmnZyLevel where id= :operalevelid1)  and l.company =( select company from SsmnZyLevel where id= :operalevelid2 ) ";
		//判断组别,如果组别不为空,直接按组别查
		if(mbranch !=null && !"".equals(mbranch) && mbranch.length()>0 && !mbranch.equals("全部"))//去除掉"全部"
			sql += " and l.branchactiongroup =:mbranch";//组别不为空,按组别查
		if(marea !=null && !"".equals(marea) && marea.length()>0 && !marea.equals("全部"))
			sql +=" and l.area =:marea";//片区不为空,按片区查
		if(mwarzone !=null && !"".equals(mwarzone) && mwarzone.length()>0 && !mwarzone.equals("全部"))
			sql +=" and l.warzone =:mwarzone";//战区不为空,按战区查
		if(mbusin !=null && !"".equals(mbusin) && mbusin.length()>0 && !mbusin.equals("全部"))
			sql +=" and l.businessdepartment =:mbusin";//事业部不为空,按事业部查
		
		Query query = session.createQuery(sql);
		if(operalevelid >0)
		{
			query.setLong("operalevelid1", operalevelid);
			query.setLong("operalevelid2", operalevelid);
		}
		if(mbranch !=null && !"".equals(mbranch) && mbranch.length()>0 && !mbranch.equals("全部"))
			query.setString("mbranch",mbranch );
		if(marea !=null && !"".equals(marea) && marea.length()>0 && !marea.equals("全部"))
			query.setString("marea", marea);
		if(mwarzone !=null && !"".equals(mwarzone) && mwarzone.length()>0 && !mwarzone.equals("全部"))
			query.setString("mwarzone",mwarzone );
		if(mbusin !=null && !"".equals(mbusin) && mbusin.length()>0 && !mbusin.equals("全部"))
			query.setString("mbusin",mbusin );

		list = query.list();
	
	} catch (Exception he) {
		logger.error("-------------getUserByLevelid---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getUserByLevelid---关闭Session时出现HibernateException-----",he2);
		}
	}	
	if(list !=null && list.size()>0)
		return true;
	else
		return false;
}

//记录用户变化表，方便同步数据到其他数据库，目前只有北京才用到
public boolean addToUserRecord(String msisdn, String ssmnnumber,String changemsisdn,int opertype,String blackcallnumber)
{
	boolean isok = false;
	
	if(( msisdn == null || "".equals(msisdn) || ssmnnumber == null || "".equals(ssmnnumber) ) && (blackcallnumber ==null || blackcallnumber.equals("")))
		return false;
		
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//检测数据库连接是否成功
	if (session == null) {
		throw new DBException("fail to connect db");
	}
	
	Transaction tx = null;
	try {
		tx = session.beginTransaction();
		//begin
		conn = session.connection();
		
		StringBuffer sql = new StringBuffer("insert into ssmn_zy_useroperation values(SEQ_SSMN_ZY_USEROPERATIO.NEXTVAL,");
		
		//操作类型 
		sql.append(opertype).append(" , ");
		
		//主号码
		if(msisdn != null && !"".equals(msisdn))
			sql.append(" '").append(msisdn).append("' , ");
		else
			sql.append(" '' , ");
		
		//更换主号码 ，此值只有 opertype为3(变更主号)时才有值
		if(changemsisdn != null && !"".equals(changemsisdn))
			sql.append(" '").append(changemsisdn).append("' , ");
		else
			sql.append(" '' , ");
		
		//副号码
		if(ssmnnumber != null && !"".equals(ssmnnumber))
			sql.append(" '").append(ssmnnumber).append("' , ");
		else
			sql.append(" '' , ");
		
		sql.append(" 0,sysdate ");
		
		if(blackcallnumber !=null && !"".equals(blackcallnumber))
			sql.append(" , '").append(blackcallnumber).append("'  ");
		else
			sql.append(" , '' ");
	
		sql.append(" ) ");		
		
		pstmt = conn.prepareStatement(sql.toString());
		rs = pstmt.executeQuery();
		tx.commit();
		
		isok = true;		
		
	} catch (Exception he) {
		logger.error("-------------addToUserRecord---出现Exception-----",he);
		isok = false;
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------addToUserRecord---关闭Session时出现HibernateException-----",he2);
		}
	}
	
	return isok;
}

public boolean updateRemark(String sempno,String sremark,SsmnZyLevel level)
{
	boolean isok = false;
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	Transaction tx = null;
	ResultSet rs = null;
	PreparedStatement pstmt = null;
	String sql = " update Ssmn_Zy_User set remark='"+sremark+"' where empno='"+sempno+"' ";
		sql += " and levelid in (select l.id from Ssmn_Zy_Level l where l.provincecity='"+
		level.getProvincecity()+"'  and  l.company ='"+level.getCompany()+"' )";
	
	try {
		tx = session.beginTransaction();
		//begin
		conn = session.connection();
				
		pstmt = conn.prepareStatement(sql.toString());
		rs = pstmt.executeQuery();
		tx.commit();
		isok = true;		
	} catch (Exception he) {
		logger.error("-------------updateRemark---出现Exception-----",he);
		isok = false;
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------updateRemark---关闭Session时出现HibernateException-----",he2);
			isok= false;
		}
	}
	return isok;
}

public Long getModeid(Long levelid)
{
	Long res =-1L; 
	if(levelid<=0 )
		return res;
	
	Session session = HibernateSessionFactory.getSession();
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<Long> list = null;
	//检测数据库连接是否成功
	if (session == null) {
		throw new DBException("fail to connect db");
	}
	String sql = "select id from ssmn_zy_level_mode  where levelid in (select id from ssmn_zy_level t" +
			" where t.provincecity=(select provincecity from ssmn_zy_level t where id="+levelid+") and " +
			" t.company=(select company from ssmn_zy_level t where id="+levelid+") and" +
			" t.businessdepartment is null )";
	
	try {
		conn = session.connection();
		pstmt = conn.prepareStatement(sql.toString());
		rs = pstmt.executeQuery();
		if(rs != null) {
			while (rs.next())
			{
				res = rs.getLong("id");
				break;
			 }
		}
		
	} catch (Exception he) {
		logger.error("-------------getModeid---出现Exception-----",he);
		res = -1L;
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getModeid---关闭Session时出现HibernateException-----",he2);
		}
	}
	return res;
}

public int getSsmnnumberType(SsmnZyUser user)
{
	//判断渠道是否在配置的渠道中，如果是，则将ssmnnumber_type置1
	if(ConfigMgr.getChannelNames() !=null && !"".equals(ConfigMgr.getChannelNames()) 
			&& ConfigMgr.getChannelNames().length()>0  && user !=null)
	{
		if(user.getChannelid() !=null && user.getChannelid()>0)
		{
			SsmnZyChannel sname = channeldao.getChannelById(user.getChannelid());
			if(sname !=null && sname.getName() !=null)
			{
				String scname = sname.getName();
				String schannelnames = ConfigMgr.getChannelNames();
				int index = schannelnames.indexOf(scname);					
				if(index >=0)
				{
					return 1;
				}
				else
				{
					return 0;
				}
			}
		}
	}
	return 0;
}

/*
 * 查询所有可作为第二联系人的主号码
 * 条件：必须要有一个渠道号码的经纪人
 */
public List<String> getZyUserSecondMsis(String secondmsisdn){
    Session session = null;
	List<String> list = new ArrayList<String>();
	try {
		StringBuffer strHQL = new StringBuffer("select distinct(z.msisdn) from SsmnZyUser z,SsmnZyEnablenumber e " +
				"where z.ssmnnumber=e.enablenumber and e.type=0 ");
		if(secondmsisdn !=null && secondmsisdn.length()>0)
		{
			strHQL.append(" and z.msisdn ='").append(secondmsisdn).append("' ");
		}
		
		session = this.adapter.openSession();
		Query query = session.createQuery(strHQL.toString());
		list = query.list(); 
		 
	} catch (Exception he) {
		logger.error("-------------getZyUserSecondMsis---出现Exception-----",he);
	} finally {
		try {
			if(session != null)
				session.close();
		} catch (HibernateException he2) {
			logger.error("-------------getZyUserSecondMsis---关闭Session时出现HibernateException-----",he2);
		}
	}
	return list;
}

public String getoperalevel()
{
	String res = "";
		res = UtilFunctionDao.operlevel;
	return res;
}

public String insertintossmnuserSql(SsmnUser user)
{
	String sres = "";
	String sql= "insert into ssmn_user (";
	String svalues = " values ( ";
	
	//MSISDN
	sql += "msisdn, ";
	svalues +=" '";
	if(user.getMsisdn() != null)
		svalues +=user.getMsisdn();
	svalues +="' , ";
	
	//PIN
	sql +=" pin, ";
	svalues +=" '";
	if(user.getPin() != null)
		svalues +=user.getPin();
	svalues +="' , ";

	
	//SUB_MANNER
	sql += " sub_manner , ";
	svalues += user.getSubManner() +" , ";
	
	//SERVICESTATUS
	sql +=" servicestatus, ";
	svalues += " '";
	if(user.getServicestatus() != null)
		svalues +=user.getServicestatus();
	svalues +="' , ";
	
	//TEMP_PIN
	sql +=" temp_pin, ";
	svalues +=" '";
	if(user.getTempPin() != null)
		svalues +=user.getTempPin();
	svalues +="' , ";
	
	//CRBT_FLAG
	sql += " crbt_flag, ";
	svalues +=" '";
	if(user.getCrbtFlag() != null)
		svalues +=user.getCrbtFlag();
	svalues +="' , ";
	
	//USERNAME
	sql +=" username, ";
	svalues +=" '";
	if(user.getUsername() != null)
		svalues +=user.getUsername();
	svalues +="' , ";
	
	//USERID
	sql += " userid, ";
	svalues +=" '";
	if(user.getUserid() != null)
		svalues +=user.getUserid();
	svalues +="' , ";
	
	//CALLINGTYPE
	sql += " callingtype, ";
	svalues += user.getCallingtype()+" , ";
	//CALLINGNUMBER
	sql += " callingnumber, ";
	svalues +=" '";
	if(user.getCallingnumber() != null)
		svalues +=user.getCallingnumber();
	svalues +="' , ";
	
	//SUB_DATE
	sql +=" sub_date , ";
	svalues +=" sysdate, ";
	//QUESTION
	sql +=" question , ";
	svalues +=" '";
	if(user.getQuestion() != null)
		svalues +=user.getQuestion();
	svalues +="' , ";
	
	//ANSWER
	sql += " answer, ";
	svalues +=" '";
	if(user.getAnswer() != null)
		svalues +=user.getAnswer();
	svalues +="' , ";
	
	//AGENT_ID
	sql += " agent_id, ";
	svalues +=" '";
	if(user.getAgentId() != null)
		svalues +=user.getAgentId();
	svalues +="' , ";
	
	//CANCEL_MANNER
	if(user.getCancelManner() != null)
	{
		sql +=" cancel_manner, ";
		svalues += user.getCancelManner()+" , ";
	}
	
	//TYPE
	if(user.getType() != null)
	{
		sql += " type , ";
		svalues +=user.getType()+" , ";
	}
	//USERUSSDFLAG
	if(user.getUserussdflag() != null)
	{
		sql += " userussdflag , ";
		svalues += user.getUserussdflag()+ " , ";
	}
	//REMIND_FLAG
	if(user.getRemindFlag() != null)
	{
		sql +=" remind_flag, ";
		svalues += user.getRemindFlag()+" , " ;
	}
	//PAYMENT
	if(user.getPayment() != null)
	{
		sql +=" payment , ";
		svalues +=user.getPayment() +" , ";		
	}
	//USERTYPE
	sql += " usertype, ";
	svalues += " '";
	if(user.getUsertype() != null)
		svalues +=user.getUsertype();
	svalues +="' , ";
	//DEVICETOKEN
	sql +=" devicetoken, ";
	svalues += " '";
	if(user.getDevicetoken() != null)
		svalues +=user.getDevicetoken();
	svalues +="' , ";
	//PHONE_SYSTEM
	sql += " phone_system  ";
	svalues +=" '";
	if(user.getPhoneSystem() != null)
		svalues +=user.getPhoneSystem();
	svalues +="' ";
	
	sres = sql +") " +svalues +")";
	
	return sres;
}

	public static void main(String[] args) {
	 zydcUserDao dao = zydcUserDao.getInstance();
	 SsmnZyLevel lev = new SsmnZyLevel();
	 lev.setCompany("中原");
	 lev.setProvincecity("天津");
	 lev.setBusinessdepartment("南区事业部");
	 lev.setWarzone("河西人民公园2战区");
//	 int a = dao.getAgentInfoCount(null, 1000000092L);
//	 TyadminOperators opera = new TyadminOperators();
//	 opera.setLevelid(1000000092L);
//	 int a = dao.getZYDCUserCount("13015002858", null,null, opera);
//		logger.info("-----------count-- -----"+a);
		//List<zyUserDto> list = dao.getAgentInfoList(null, 1000000092L, 2, 2);
		//logger.info("-----------list-- -----"+list.size());
//		int count = dao.dao.getZYDCUserCount("15600002222");
//		List<SsmnZyUser> list = dao.getZyUserByMsisdn("13811110000");
		//for(int i=0;i<list.size();i++){
		//	logger.info("-----------getName-- -----"+list.get(i).getName());
		//}
// String test = "abdgdfdfdsfsabckfsdlfsdabcdfdsklfabc";
// int r = test.split("abc").length;
// int t = test.indexOf("abc");
// logger.info("-----------count-- -----"+r);
//		SsmnZyUser u = new SsmnZyUser();
//		u.setLevelid(1000000008L);
//		u.setChannelid(1L);
//		u.setGroupname("dd");
//		u.setIntime(new Date());
//		u.setName("水电费等所发生的");
//		u.setMsisdn("13811110000");
//		u.setSsmnnumber("13108680018");
//		boolean falg = dao.registeSSMNService("13811110000", "13108680018",u );
//		boolean flag = dao.cancelUserAndNumber("13100010002");
// logger.info("-----------falg-- ----="+flag);
		
	}

}
