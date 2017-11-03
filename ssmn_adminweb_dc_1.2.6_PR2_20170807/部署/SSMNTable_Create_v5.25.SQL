/************************************************
**	Create table space			**
************************************************/
/*
CREATE TABLESPACE SSMN_DATA 
DATAFILE '$ORACLE_BASE/oradata/$ORACLE_SID/SSMN_DATA1.DBF' 
SIZE 1024M REUSE AUTOEXTEND ON NEXT 100M MAXSIZE UNLIMITED
EXTENT MANAGEMENT LOCAL UNIFORM SIZE 512K  SEGMENT SPACE MANAGEMENT  AUTO;

CREATE TABLESPACE SSMN_IDX 
DATAFILE '$ORACLE_BASE/oradata/$ORACLE_SID/SSMN_IDX1.DBF' 
SIZE 256M REUSE AUTOEXTEND ON NEXT 20M MAXSIZE UNLIMITED
EXTENT MANAGEMENT LOCAL UNIFORM SIZE 256K  SEGMENT SPACE MANAGEMENT  AUTO;

CREATE TEMPORARY TABLESPACE SSMN_TMP 
TEMPFILE  '$ORACLE_BASE/oradata/$ORACLE_SID/SSMN_TMP1.DBF' 
SIZE 256M REUSE AUTOEXTEND ON NEXT 20M MAXSIZE UNLIMITED
EXTENT MANAGEMENT LOCAL UNIFORM SIZE 512K ;

CREATE UNDO TABLESPACE SSMN_UNDO 
DATAFILE '$ORACLE_BASE/oradata/$ORACLE_SID/SSMN_UNDO1.DBF' 
SIZE 100M REUSE AUTOEXTEND ON NEXT 20M MAXSIZE UNLIMITED
EXTENT MANAGEMENT LOCAL AUTOALLOCATE;

*/


/************************************************
**	Create user script					**
************************************************/
/*

CREATE USER SSMN identified by SSMN 
DEFAULT TABLESPACE SSMN_DATA
TEMPORARY TABLESPACE SSMN_TMP ;

GRANT CONNECT, RESOURCE TO SSMN;
grant execute on dbms_lock to SSMN;
*/


-- SEQUENCE
CREATE SEQUENCE SEQ_SSMN_CDR  INCREMENT BY 2  START WITH 1000000000  MAXVALUE 9999999998  CYCLE CACHE 10; 
CREATE SEQUENCE SEQ_SSMN_MO   INCREMENT BY 2  START WITH 100000000  MAXVALUE 999999998  CYCLE CACHE 10; 
CREATE SEQUENCE SEQ_SSMN_MS   INCREMENT BY 2  START WITH 100000000  MAXVALUE 999999998  CYCLE CACHE 10; 
CREATE SEQUENCE SEQ_SSMN_MT   INCREMENT BY 2  START WITH 100000000  MAXVALUE 999999998  CYCLE CACHE 10;
CREATE SEQUENCE SEQ_SSMN_MA   INCREMENT BY 2  START WITH 100000000  MAXVALUE 999999998  CYCLE CACHE 10;  
CREATE SEQUENCE SEQ_SSMN_MV   INCREMENT BY 2  START WITH 100000000  MAXVALUE 999999998  CYCLE CACHE 10;  
CREATE SEQUENCE SEQ_SSMN_MS_TMP   INCREMENT BY 2  START WITH 100000000  MAXVALUE 999999998  CYCLE CACHE 10; 
CREATE SEQUENCE SEQ_SSMN_BILL   INCREMENT BY 2  START WITH 100000000  MAXVALUE 999999998  CYCLE CACHE 10; 
CREATE SEQUENCE SEQ_SSMN_BILL_TMP   INCREMENT BY 2  START WITH 100000000  MAXVALUE 999999998  CYCLE CACHE 10; 
CREATE SEQUENCE SEQ_SSMN_TEST_OUT  INCREMENT BY 2  START WITH 1000000000  MAXVALUE 9999999998  CYCLE CACHE 10; 
CREATE SEQUENCE SEQ_callID  INCREMENT BY 2  START WITH 1000000000  MAXVALUE 9999999998  CYCLE CACHE 10; 
COMMIT;

-- 系统参数表
-- drop table SSMN_SYSTEM;
create table SSMN_SYSTEM
(
	Max_Number		Number(1)			NOT NULL,
	Max_RealNumber		Number(1)			NOT NULL,
	Retain_Period		Number(3)			NOT NULL,
	CDR_Period		Number(3)			NOT NULL,
	Access_Code		Varchar2(20)			NOT NULL,
	SSMN_Length		Number(2)			NOT NULL,
	Sms_Access_Code		Varchar2(20)			NOT NULL,
	R_Access_Code		Varchar2(20),
	Reserve1		Number(6),
	Reserve2		Number(6),
	Reserve3		Varchar2(20),
	Access_Code3		Varchar2(20),
	u_cin Varchar2(20) default NULL,
	USSDFlag	Number (1) default 0, 
	FlagCPNPrefix	Varchar2(4),
	Net_transfer_flag varchar2(1) default '1' ,
	call_remind_flag varchar2(1) default '0' ,
	Trymaxtime  Number(1) default 3 ,
  IvrCallflag Number(1) default 0 ,
  v_trymaxtime Number(1) default 3 
) tablespace SSMN_DATA;
alter table SSMN_SYSTEM add constraint PK_SSMN_SYSTEM primary key(Max_Number)
using index tablespace SSMN_IDX;
commit;

-- 系统参数表
-- drop table SYSTEM_CFG;
create table SYSTEM_CFG
(
  NAME        VARCHAR2(30) not null,
  SERVICETYPE NUMBER(2) not null,
  VALUE       VARCHAR2(120)
)
tablespace SSMN_DATA;
alter table SYSTEM_CFG add constraint PK_SYSTEM_CFG primary key (NAME,SERVICETYPE)
using index tablespace SSMN_IDX;
commit;

-- 系统用户信息记录表
-- drop table SSMN_USER;
create table SSMN_USER
(
	MSISDN			Varchar2(20)		NOT NULL,
	PIN			Varchar2(6)		NOT NULL,
	Sub_Manner		Number(1)		NOT NULL,
	ServiceStatus		Char(1)			NOT NULL,
	Temp_Pin		Char(6),
	CRBT_Flag		Char(1),
	UserName		Varchar2(20),
	UserID			Varchar2(20),
	CallingType		Number(1)			NOT NULL,
	CallingNumber		Varchar2(18),
	Sub_Date		Date				NOT NULL,
	Question		Varchar2(30),
	Answer			Varchar2(30),
	Agent_ID		Varchar2(16),
	Cancel_Manner		Number(1),
	usertype		Varchar2(10)	default NULL,
	DeviceToken	Varchar2(64)	default NULL,
	Logintime		Date	default NULL,
	PHONE_SYSTEM  VARCHAR2(20),
	VALIDITY      DATE,
	PAYMENT       NUMBER(1) default 0,
	 TYPE          NUMBER(1) default 1 not null ,
	 UserUSSDFlag	 Number(1) default 1 not null ,
	 Remind_Flag	 Number(1) default 1 not null 
) tablespace SSMN_DATA;
alter table SSMN_USER add constraint PK_SSMN_USER primary key(MSISDN)
using index tablespace SSMN_IDX;
Create index IDX1_SSMN_USER on SSMN_USER (MSISDN, ServiceStatus) tablespace SSMN_IDX;
Create index IDX2_SSMN_USER on SSMN_USER (UserName) tablespace SSMN_IDX;
Create index IDX3_SSMN_USER on SSMN_USER (ServiceStatus,Sub_Date) tablespace SSMN_IDX;
create index IDX4_SSMN_USER on SSMN_USER (MSISDN,SUB_DATE) tablespace SSMN_IDX;
commit;

-- drop table Agent_Message;
create table Agent_Message
(
	Agent_ID	VARCHAR2(16)	NOT NULL,
	AgentScale	Number(3),
	AgentDesc	VARCHAR2(40)	NOT NULL,
	Company_Addr	VARCHAR2(60),
	Contactor	VARCHAR2(15),
	Contact_Phone	VARCHAR2(15)	NOT NULL,
	Email_Addr	VARCHAR2(128)	
) tablespace SSMN_DATA;
alter table Agent_Message add constraint PK_Agent_Message primary key(Agent_ID)
using index tablespace SSMN_IDX;
commit;

-- 系统用户信息记录表
-- drop table SSMN_CANCEL_USER;
create table SSMN_CANCEL_USER
(
	MSISDN			Varchar2(20)		NOT NULL,
	PIN			Varchar2(6)		NOT NULL,
	Sub_Manner		Number(2)		NOT NULL,
	ServiceStatus		Char(1)			NOT NULL,
	Temp_Pin		Char(6),
	CRBT_Flag		Char(1),
	UserName		Varchar2(20),
	UserID			Varchar2(20),
	CallingType		Number(1)			NOT NULL,
	CallingNumber		Varchar2(18),
	Sub_Date		Date				NOT NULL,
	Question		Varchar2(30),
	Answer			Varchar2(30),
	Agent_ID		Varchar2(16),
	Cancel_Manner		Number(2),
	Cancel_Date		Date,
	usertype		Varchar2(10)	default NULL,
	DeviceToken	Varchar2(64)	default NULL,
	Logintime		Date	default NULL,
	type	Number(1)
) tablespace SSMN_DATA;
alter table SSMN_CANCEL_USER add constraint PK_SSMN_CANCEL_USER primary key(MSISDN, Cancel_Date)
using index tablespace SSMN_IDX;

Create Index IDX1_SSMN_CANCEL_USER ON SSMN_CANCEL_USER (Sub_Date) tablespace SSMN_IDX;
Create Index IDX2_SSMN_CANCEL_USER ON SSMN_CANCEL_USER (Cancel_Date) tablespace SSMN_IDX;
create index IDX3_SSMN_CANCEL_USER on SSMN_CANCEL_USER (MSISDN,SUB_DATE) tablespace SSMN_IDX;
commit;

-- 系统虚号码记录表
-- drop table SSMN_NUMBER;
create table SSMN_NUMBER
(
	SSMNNumber		Varchar2(18)		NOT NULL,
	Type				Number(1)	 default 0	NOT NULL,
	Number_Type		Number(1)		NOT NULL,
	MSISDN			Varchar2(20),      
	Called_Type		Number(1)		NOT NULL,       
	Restriction_Type	Char(1),
	Status			Number(1),
	Prompt_Type		Number(1),
	Initiate_Date		Date,
	Cancel_Date		Date,
	Sub_Manner		Number(2),
	Cancel_Manner		Number(1),
	ServiceStatus		Char(1)	default 'N'		NOT NULL,
	OrderID		Number(2)	 default 0		NOT NULL,
        CFStatus	Number(2)	 default 0		NOT NULL,
        SFStatus	Number(2)	 default 0		NOT NULL,
        FTStatus	Number(2)	 default 1		NOT NULL,
	PACKAGEID      NUMBER(10)
) tablespace SSMN_DATA;
alter table SSMN_NUMBER add constraint PK_SSMN_NUMBER primary key(SSMNNumber)
using index tablespace SSMN_IDX;

Create Index IDX1_SSMN_NUMBER ON SSMN_NUMBER (Number_Type) tablespace SSMN_IDX;
Create Index IDX2_SSMN_NUMBER ON SSMN_NUMBER (SSMNNumber, Status) tablespace SSMN_IDX;
Create Index IDX3_SSMN_NUMBER ON SSMN_NUMBER (SSMNNumber, Number_Type) tablespace SSMN_IDX;
Create Index IDX4_SSMN_NUMBER ON SSMN_NUMBER (MSISDN,Number_Type,Initiate_date) tablespace SSMN_IDX;
Create Index IDX5_SSMN_NUMBER ON SSMN_NUMBER (Cancel_Date) tablespace SSMN_IDX;
Create Index IDX6_SSMN_NUMBER ON SSMN_NUMBER (length(msisdn)) tablespace SSMN_IDX;
Create Index IDX7_SSMN_NUMBER ON SSMN_NUMBER (sub_manner) tablespace SSMN_IDX;

commit;

-- 系统去话显示号码分配表（根据时间）
-- drop table SSMN_CALLING_TIME;
create table SSMN_CALLING_TIME
(
	MSISDN			Varchar2(20)		NOT NULL,
	StartWeekDay		Number(1)			NOT NULL,
	EndWeekDay		Number(1)			NOT NULL,
	StartTime			Char(4)			NOT NULL,
	EndTime			Char(4)			NOT NULL,
	CallingNumber		Varchar2(18)		NOT NULL
) tablespace SSMN_DATA;

alter table SSMN_CALLING_TIME add constraint PK_SSMN_CALLING_TIME primary key(MSISDN,StartWeekDay,EndWeekDay,StartTime,EndTime)
using index tablespace SSMN_IDX;

Create Index IDX1_SSMN_CALLING_TIME ON SSMN_CALLING_TIME (MSISDN) tablespace SSMN_IDX ;
commit;

-- 系统去话显示号码分配表（根据被叫）
-- drop table SSMN_CALLING_NUM;
create table SSMN_CALLING_NUM
(
	MSISDN			Varchar2(20)		NOT NULL,
	Prefix			Varchar2(18)		NOT NULL,
	CallingNumber		Varchar2(18)		NOT NULL
) tablespace SSMN_DATA;

alter table SSMN_CALLING_NUM add constraint PK_SSMN_CALLING_NUM primary key(MSISDN,Prefix)
using index tablespace SSMN_IDX;

Create Index IDX1_SSMN_CALLING_NUM ON SSMN_CALLING_NUM (MSISDN) tablespace SSMN_IDX; 
commit;

-- 系统来话状态分配表（根据时间）
-- drop table SSMN_CALLED_TIME;
create table SSMN_CALLED_TIME
(
	SSMNNumber		Varchar2(18)		NOT NULL,
	StartWeekDay		Number(1)		NOT NULL,
	EndWeekDay		Number(1)		NOT NULL,
	StartTime		Char(4)			NOT NULL,
	EndTime			Char(4)			NOT NULL,	
	Status			Number(1)		NOT NULL
) tablespace SSMN_DATA;
alter table SSMN_CALLED_TIME add constraint PK_SSMN_CALLED_TIME primary key(SSMNNumber,StartWeekDay,EndWeekDay,StartTime,EndTime)
using index tablespace SSMN_IDX;

Create Index IDX1_SSMN_CALLED_TIME ON SSMN_CALLED_TIME (SSMNNumber) tablespace SSMN_IDX;

commit;

-- 系统来话状态分配表（根据主叫）
-- drop table SSMN_CALLED_NUM;
create table SSMN_CALLED_NUM
(
	SSMNNumber		Varchar2(18)		NOT NULL,
	CallingNumber		Varchar2(18)		NOT NULL,
	Status			Number(1)		NOT NULL
) tablespace SSMN_DATA;	

alter table SSMN_CALLED_NUM add constraint PK_SSMN_CALLED_NUM primary key(SSMNNumber,CallingNumber)
using index tablespace SSMN_IDX;

Create Index IDX1_SSMN_CALLED_NUM ON SSMN_CALLED_NUM (SSMNNumber) tablespace SSMN_IDX;
commit;

-- 系统去话禁拨号码表
-- drop table SSMN_Forbidden;
create table SSMN_Forbidden
(
	Prefix				Varchar2(18)		NOT NULL,
	Type				Number(1)	 default 0	NOT NULL,
	Description			Varchar2(32)
) tablespace SSMN_DATA;

alter table SSMN_Forbidden add constraint PK_SSMN_Forbidden primary key(Prefix)
using index tablespace SSMN_IDX;
commit;

-- 系统话单表
-- drop table SSMN_CDR;
create table SSMN_CDR
(
	Streamnumber		Number(10)		NOT NULL,
	CallingAddress		Varchar2(18),
	CalledAddress		Varchar2(32),
	OriginalCalled		Varchar2(18),
	RedirectPartyID		Varchar2(18),
	MSISDN			Varchar2(20),
	SSMNNumber		Varchar2(18),
	CallType		Number(1)			NOT NULL,
	CallStartTime		Date,
	CallStopTime		Date,
	ChargeClass		Number(6),
	Call_Cost		Number(6),
	Change_Pin		Char(1),
	Apply_Number		Char(1),
	Set_CLI			Char(1),
	Cancel_Number		Char(1),
	Apply_Service		Char(1),
	Cancel_Service		Char(1),
	CallDuration	Number(6)	default NULL,
	Readstatus	Number(1)	default 0,
	EndReason	Number(2)	default NULL,
	Incoming_CIN	Varchar2(32)	default NULL,
	Outgoing_CPN Varchar2(32) default NULL
) tablespace SSMN_DATA;

alter table SSMN_CDR add constraint PK_SSMN_CDR primary key(Streamnumber) using index tablespace SSMN_IDX;

Create index IDX1_SSMN_CDR on SSMN_CDR (MSISDN, CallStartTime, Apply_Service) tablespace SSMN_IDX;
Create index IDX2_SSMN_CDR on SSMN_CDR(MSISDN, CallStartTime, Cancel_Service) tablespace SSMN_IDX;
Create index IDX3_SSMN_CDR on SSMN_CDR (CallingAddress) tablespace SSMN_IDX;
Create index IDX4_SSMN_CDR on SSMN_CDR (CalledAddress) tablespace SSMN_IDX;
Create index IDX5_SSMN_CDR on SSMN_CDR (CallType,CallStopTime) tablespace SSMN_IDX;
Create index IDX6_SSMN_CDR on SSMN_CDR (CallType,CallStopTime,calledaddress,callingaddress) tablespace SSMN_IDX; 
Create index IDX7_SSMN_CDR on SSMN_CDR(MSISDN, CallType) tablespace SSMN_IDX;
Create index IDX8_SSMN_CDR on SSMN_CDR(callstoptime) tablespace SSMN_IDX;
commit;

-- 系统注销号码列表
-- drop table SSMN_CANCEL_NUM;
create table SSMN_CANCEL_NUM
(
	SSMNNumber		Varchar2(18)		NOT NULL,
	Type				Number(1)	 default 0	NOT NULL,
	MSISDN			Varchar2(20)		NOT NULL,
	Initiate_Date		Date				NOT NULL,
	Cancel_Date		Date				NOT NULL,
	Number_Type		Number(1)			NOT NULL,
	Sub_Manner		Number(1),
	Cancel_Manner		Number(1),
	ServiceStatus		Char(1)	default 'N'			NOT NULL,
	OrderID		Number(2)	 default 0		NOT NULL,
        CFStatus	Number(2)	 default 0		NOT NULL,
        SFStatus	Number(2)	 default 0		NOT NULL,
        FTStatus	Number(2)	 default 1		NOT NULL,
	Packageid	Number(10)
) tablespace SSMN_DATA;

alter table SSMN_CANCEL_NUM add constraint PK_SSMN_CANCEL_NUM primary key(SSMNNumber,MSISDN,Initiate_Date)
using index tablespace SSMN_IDX;

Create Index IDX1_SSMN_CANCEL_NUM ON SSMN_CANCEL_NUM (Cancel_Date) tablespace SSMN_IDX;
Create Index IDX2_SSMN_CANCEL_NUM ON SSMN_CANCEL_NUM (Initiate_Date,Cancel_Date,Number_Type,MSISDN) tablespace SSMN_IDX;  

Create Index IDX3_SSMN_CANCEL_NUM ON SSMN_CANCEL_NUM (Length(msisdn)) tablespace SSMN_IDX;
commit;

-- 系统费率表
-- drop table SSMN_Charge;
create table SSMN_Charge
(
	Charge_Period		Number(2)			NOT NULL,
	Charge1			Number(2)			NOT NULL,
	Charge2			Number(2)			NOT NULL,
	Charge3			Number(2)			NOT NULL,
	Charge4			Number(2)			NOT NULL,
	Charge5			Number(2)			NOT NULL,
	Discount_Rate		Number(3)			NOT NULL
) tablespace SSMN_DATA;
alter table SSMN_Charge add constraint PK_SSMN_Charge primary key(Charge_Period)
using index tablespace SSMN_IDX;
commit;

-- drop table SSMN_ChargeClass ;

create table SSMN_ChargeClass
(
	Charge_Type	Number(1) NOT NULL,
	Calling_Prefix	Varchar2(14) NOT NULL,
	Called_Prefix	Varchar2(14) NOT NULL,
	Charge_Class	Number(6) NOT NULL,
	Description	Varchar2(64) NULL
) tablespace SSMN_DATA;

alter table SSMN_ChargeClass add constraint PK_SSMN_ChargeClass primary key (Charge_Type,Calling_Prefix,Called_Prefix) using index tablespace SSMN_IDX;
commit;

-- 系统SMS_OPERATOR
-- drop table SMS_OPERATOR;
create table SMS_OPERATOR
(
	OPENO			VARCHAR2(16)		NOT NULL,
	OPEPWD			VARCHAR2(32),
	GROUPNAME		VARCHAR2(30)		NOT NULL,
	SERVICEKEY		VARCHAR2(20),
	OPEPWDDATE		DATE,
	STARTTIME		CHAR(6),
	ENDTIME			CHAR(6),
	ISLOGIN			NUMBER(1),
	LAST_UPD_USER		VARCHAR2(16),
	LAST_UPD_DATE		DATE,
	OPENAME			VARCHAR2(16)
) tablespace SSMN_DATA;

alter table SMS_OPERATOR add constraint PK_SMS_OPERATOR primary key(OPENO)
using index tablespace SSMN_IDX;

commit;

-- 系统SSMN_GROUP
-- drop table SSMN_GROUP;
create table SSMN_GROUP
(
	GROUPNAME		VARCHAR2(30)		NOT NULL,
	GROUPDESC		VARCHAR2(60),
	LAST_UPD_USER		VARCHAR2(16),
	LAST_UPD_DATE	DATE
) tablespace SSMN_DATA;

alter table SSMN_GROUP add constraint PK_SSMN_GROUP primary key(GROUPNAME)
using index tablespace SSMN_IDX;

commit;

-- 系统 SSMN_GROUP_RIGHT
-- drop table SSMN_GROUP_RIGHT ;
create table SSMN_GROUP_RIGHT
(
	GROUPNAME		VARCHAR2(30)		NOT NULL,
	RIGHT			CHAR(4)			NOT NULL
) tablespace SSMN_DATA;

alter table SSMN_GROUP_RIGHT add constraint PK_SSMN_GROUP_RIGHT primary key(GROUPNAME,RIGHT)
using index tablespace SSMN_IDX;
commit;

-- 系统SSMN_RIGHT
-- drop table SSMN_RIGHT;
create table SSMN_RIGHT
(
	RIGHT			CHAR(4)			NOT NULL,
	RIGHTDESC		VARCHAR2(50)
) tablespace SSMN_DATA;

alter table SSMN_RIGHT add constraint PK_SSMN_RIGHT primary key(RIGHT)
using index tablespace SSMN_IDX;
commit;

-- 系统 sms_operator_log
-- drop table sms_operator_log ;
create table sms_operator_log
(
	OPENO			VARCHAR2(16)		NOT NULL,		
	COMMANDID		VARCHAR2(200),
	COMMANDTIME		DATE,	
	SERVICEKEY		VARCHAR2(20)		NOT NULL,
	RESULTID			NUMBER(3)
) tablespace SSMN_DATA;

alter table sms_operator_log add constraint PK_sms_operator_log primary key(OPENO,COMMANDTIME)
using index tablespace SSMN_IDX;
commit;

-- 短信指令应答表序列
create sequence SEQ_SMS_Transaction minvalue 100000001 maxvalue 9999999999 start with 100000001 increment by 2 cache 20 CYCLE;

-- 存放接收到的短信的数据表 MsgOriginate
-- drop table MsgOriginate ;
create table MsgOriginate
(
	ID				Number(10)		NOT NULL,
	SrcNo			Char(40),
	SrcNoType	Number(1) default 0,
	DestNo			Char(40),
	MsgID 		Varchar2(64),
	MsgFormat			Number(2),	
	MsgLength			Number(3),
	MsgContent		Varchar(200),
	PkID 	Number(5),
	PkTotal 	Number(5),
	PkNumber 	Number(5),
	InTime			Date,
	LinkID			Char(40),
	Reserve			Char(8)
) tablespace SSMN_DATA;

alter table MsgOriginate add constraint PK_MsgOriginate primary key(ID)
using index tablespace DTBL_IDX;

Create Index IDX1_MsgOriginate ON MsgOriginate (DestNo) tablespace DTBL_IDX; 


commit;

-- 存放处理过的平台接收到的短消息的数据表 MsgOriginateBak
-- drop table MsgOriginateBak ;
create table MsgOriginateBak
(
	ID				Number(10)		NOT NULL,
	SrcNo			Char(40),
	SrcNoType	Number(1),
	DestNo			Char(40),
	MsgID 		Varchar2(64),
	MsgFormat			Number(2),	
	MsgLength			Number(3),
	MsgContent		Varchar(200),
	PkID 	Number(5),
	PkTotal 	Number(5),
	PkNumber 	Number(5),
	InTime			Date,
	LinkID			Char(40),
	ActTime			Date,
	MsgType		Number(1),
	State				Number(2),
	Reserve			Char(8)
) tablespace SSMN_DATA;

alter table MsgOriginateBak add constraint PK_MsgOriginateBak primary key(ID)
using index tablespace DTBL_IDX;

Create Index IDX1_MsgOriginateBak ON MsgOriginateBak (DestNo) tablespace DTBL_IDX; 
Create Index IDX2_MsgOriginateBak ON MsgOriginateBak (State) tablespace DTBL_IDX; 


commit;

-- 存放要发出的短信的数据表 MsgSend
-- drop table MsgSend ;
create table MsgSend
(
	ID				Number(10)		NOT NULL,
	Service	Number(2) default 0 not null,
	BatchSnb		Varchar2(15),
	PkTotal	Number(1) default 1,
	PkNumber	Number(1) default 1,
	SrcNo			Char(40),
	ChargeNo			Char(40),
	ChargeNoType	 Number(1) default 0,
	DestNo			Char(40),
	DestNoType		Number(1) default 0,
	ServiceType		Char(10),
	FeeType			Number(1),
	FeeValue			Char(6) default '0',
	MTFlag			Number(1),
	MsgLevel			Number(1),
	ExpireTime			Char(16),
	ScheduleTime		Char(16),
	ReportFlag			Number(1),
	MsgType			Number(1),
	MsgFormat			Number(2),
	MsgLength			Number(3),
	MsgContent		Varchar(200),
	LinkID			Char(40),
	Reserve			Char(8)
) tablespace SSMN_DATA;
alter table MsgSend add constraint PK_MsgSend primary key(ID)
using index tablespace DTBL_IDX;
commit;

-- 存放提交至短信中心后的信息的数据表 MsgTerminated
-- drop table MsgTerminated ;
create table MsgTerminated
(
	ID				Number(10)		NOT NULL,
	Service	Number(2),
	BatchSnb		Varchar2(15),
	PkTotal	Number(1),
	PkNumber	Number(1),
	SrcNo			Char(40),
	ChargeNo			Char(40),
	ChargeNoType	 Number(1),
	DestNo			Char(40),
	DestNoType		Number(1),
	ServiceType		Char(10),
	FeeType			Number(1),
	FeeValue			Char(6),
	MTFlag			Number(1),
	MsgLevel			Number(1),
	ExpireTime			Char(16),
	ScheduleTime		Char(16),
	ReportFlag			Number(1),
	MsgType			Number(1),
	MsgFormat			Number(2),
	MsgLength			Number(3),
	MsgContent		Varchar(200),
	InTime			Date,
	ActTime			Date,
	MsgID			Varchar2(64),
	State				Number(1),
	ErrorCode			Number(4),
	LinkID			Char(40),
	Reserve			Char(8),
	Readstatus		Number(1) 	default 0
) tablespace SSMN_DATA;
alter table MsgTerminated add constraint PK_MsgTerminated primary key(ID)
using index tablespace DTBL_IDX;

Create Index IDX1_MsgTerminated ON MsgTerminated (MsgID) tablespace DTBL_IDX;
Create Index IDX2_MsgTerminated ON MsgTerminated (FeeType) tablespace DTBL_IDX;
Create Index IDX3_MsgTerminated ON MsgTerminated (State) tablespace DTBL_IDX;
Create Index IDX4_MsgTerminated ON MsgTerminated (InTime) tablespace DTBL_IDX;
Create Index IDX5_MsgTerminated ON MsgTerminated (BatchSnb) tablespace DTBL_IDX;
commit;

-- 存放提交至短信中心回复的SubmitResp的数据表 MsgSubmitResp
-- drop table MsgSubmitResp;
CREATE TABLE MsgSubmitResp
(
	ID			NUMBER(10)		NOT NULL,
	State			NUMBER(4),
	MsgID			Varchar2(64),
	InTime			DATE
) tablespace SSMN_DATA;

alter table MsgSubmitResp add constraint PK_MsgSubmitResp primary key(ID)
using index tablespace SSMN_IDX;

Create Index IDX1_MsgSubmitResp ON MsgSubmitResp (MsgID) tablespace SSMN_IDX;
Create Index IDX2_MsgSubmitResp ON MsgSubmitResp (InTime) tablespace SSMN_IDX;
commit;

-- 存放提交至短信中心回复的Report的数据表 MsgReport
-- drop table MsgReport;
create table MsgReport
(
	ID			Number(10)		NOT NULL,
	State			Number(1),
	ErrorCode		Number(4),
	MsgID			Varchar2(64),
	InTime			Date
) tablespace SSMN_DATA;

alter table MsgReport add constraint PK_MsgReport primary key(ID) using index tablespace SSMN_IDX;

Create Index IDX1_MsgReport ON MsgReport (MsgID) tablespace SSMN_IDX;
Create Index IDX2_MsgReport ON MsgReport (InTime) tablespace SSMN_IDX;
commit;

-- SSMN_MobileType 表
-- drop table ssmn_mobiletype;
create table ssmn_mobiletype
(
	type			number(2)		NOT NULL,
	Descript		varchar2(64),
	reserve			number(4)
) tablespace SSMN_DATA;

alter table ssmn_mobiletype add constraint PK_ssmn_mobiletype primary key(type) using index tablespace SSMN_IDX;

commit;

-- SSMN_MobileStatus 表
-- drop table ssmn_mobilestatus;
create table ssmn_mobilestatus
(
	Status			number(3)		not null,
	Descript		VARCHAR2(64),
	Reserve			number(4)
)tablespace SSMN_DATA;

alter table ssmn_mobilestatus add constraint PK_ssmn_mobilestatus primary key(Status) using index tablespace SSMN_IDX;

commit;

-- 存放拆机失败号码的表 ssmn_unreg_fail
-- drop table ssmn_unreg_fail;
create table ssmn_unreg_fail
(
	MSISDN			Varchar2(20)      NOT NULL,
        FAIL_DATE		DATE		   NOT NULL,
	SUCC_DATE		DATE
)tablespace SSMN_DATA;
alter table ssmn_unreg_fail add constraint PK_ssmn_unreg_fail primary key(MSISDN) using index tablespace SSMN_IDX;
commit;

-- 靓号资源表 SSMN_TTNumber
-- drop table SSMN_TTNumber;
create table SSMN_TTNumber
(
	SSMNTTNumber       Varchar2(18)      NOT NULL,
	Type				Number(1)	 default 0	NOT NULL,
    TT_Type			number(1),
	Comments		Varchar2(20),
	Reserve			number(1)
)tablespace SSMN_DATA;
alter table SSMN_TTNumber add constraint PK_SSMN_TTNumber primary key(SSMNTTNumber) using index tablespace SSMN_IDX;
Create Index IDX1_SSMN_TTNumber ON SSMN_TTNumber (TT_Type) tablespace SSMN_IDX;
commit;

-- 每日统计表 SSMN_STAT_DAY
-- drop table SSMN_STAT_DAY;
create table SSMN_STAT_DAY
(
	STAT_TIME				DATE	NOT NULL,
	UPDATE_TIME				DATE	NOT NULL,
	CUR_USER_NUM1				NUMBER(6),	
	CUR_USER_NUM2				NUMBER(6),	
	CUR_USER_NUM3				NUMBER(6),	
	CUR_USER_NUM4				NUMBER(6),	
	CUR_USER_NUM5				NUMBER(6),	
	CUR_NUMBER_NUM1				NUMBER(8),	
	CUR_NUMBER_NUM2				NUMBER(8),	
	CUR_NUMBER_NUM3				NUMBER(8),	
	CUR_NUMBER_NUM4				NUMBER(8),	
	CUR_NUMBER_NUM5				NUMBER(8),	
	CUR_FREEZE_NUM				NUMBER(8),	
	CUR_KEEP_NUM				NUMBER(8),	
	CXL_USER_NUM				NUMBER(6),	
	CXL_NUMBER_NUM				NUMBER(6),	
	APP_NUMBER_NUM				NUMBER(8),	
	APP_USER_NUM_WEB			NUMBER(6),
	APP_USER_NUM_IVR			NUMBER(6),
	APP_USER_NUM_SMS			NUMBER(6),
	APP_USER_NUM_1001			NUMBER(6),
	APP_USER_NUM_OTHER	 		NUMBER(6),
	APP_USER_NUM_CANCEL	 		NUMBER(6),
	ACTIVE_D1				NUMBER(6),
	ACTIVE_D3				NUMBER(6),
	ACTIVE_D7				NUMBER(6),
	ACTIVE_M1				NUMBER(6),
	ACTIVE_M3				NUMBER(6),
	ACTIVE_1					NUMBER(6),
	APP_NUMBER_U				NUMBER(8),
	CXL_NUMBER_U				NUMBER(8),
	CUR_NUMBER_U				NUMBER(8),
	APP_USER_NUM_OP		NUMBER(6),
	CXL_USER_NUM_WEB		NUMBER(6),
	CXL_USER_NUM_IVR		NUMBER(6),
	CXL_USER_NUM_SMS		NUMBER(6),
	CXL_USER_NUM_OTHER	NUMBER(6),
	CXL_USER_NUM_CAN		NUMBER(6),
	HIDE_ACTIVE_D1			NUMBER(6),
	HIDE_ACTIVE_D3			NUMBER(6),
	HIDE_ACTIVE_D7			NUMBER(6),
	HIDE_ACTIVE_M1			NUMBER(6),
	HIDE_ACTIVE_M3			NUMBER(6),
	total_user				number(6),
	Type	 						NUMBER(1) default 0 Not null,
	Reserve					NUMBER(1)
)tablespace SSMN_DATA;
alter table SSMN_STAT_DAY add constraint PK_SSMN_STAT_DAY primary key(STAT_TIME,type) using index tablespace SSMN_IDX;
commit;

-- 每月统计表 SSMN_STAT_MONTH
-- drop table SSMN_STAT_MONTH;
create table SSMN_STAT_MONTH
(
	STAT_TIME				DATE	NOT NULL,
	UPDATE_TIME				DATE	NOT NULL,
	MIN_TIME_U				NUMBER(4),
	MAX_TIME_U				NUMBER(4),
	AVG_TIME_U				NUMBER(10),
	MIN_TIME_N				NUMBER(4),
	MAX_TIME_N				NUMBER(4),
	AVG_TIME_N				NUMBER(10),
	REVENUE_NUM_U				NUMBER(10),
	REVENUE_NUM_N				NUMBER(10),
	REVENUE					NUMBER(8),
	CALLREVENUE				NUMBER(8),
	SMSREVENUE				NUMBER(8),
	SERVICEREVENUE		NUMBER(8),
	Type	 						NUMBER(1) default 0 Not null,
	Reserve					number(1)
)tablespace SSMN_DATA;
alter table SSMN_STAT_MONTH add constraint PK_SSMN_STAT_MONTH primary key(STAT_TIME,type) using index tablespace SSMN_IDX;
commit;

-- 每小时统计表 SSMN_STAT_HOUR
-- drop table SSMN_STAT_HOUR;
create table SSMN_STAT_HOUR
(
	STAT_TIME				DATE	NOT NULL,
	UPDATE_TIME				DATE	NOT NULL,
	CALLIN_NUM_LOCAL			NUMBER(6),
	CALLIN_TIME_LOCAL			NUMBER(10),
	CALLIN_NUM_FOREIGN			NUMBER(6),
	CALLIN_TIME_FOREIGN			NUMBER(10),
	CALLOUT_NUM_LOCAL			NUMBER(6),
	CALLOUT_TIME_LOCAL			NUMBER(10),
	CALLOUT_NUM_FOREIGN			NUMBER(6),
	CALLOUT_TIME_FOREIGN			NUMBER(10),
	IVR_NUM					NUMBER(6),
	IVR_TIME				NUMBER(10),
	CALLIN_F_NUM				NUMBER(6),
	CALLOUT_F_NUM				NUMBER(6),
	OCALLIN_NUM_LOCAL			NUMBER(6),
	OCALLIN_TIME_LOCAL			NUMBER(10),
	OCALLIN_NUM_FOREIGN			NUMBER(6),
	OCALLIN_TIME_FOREIGN			NUMBER(10),
	OCALLOUT_NUM_LOCAL			NUMBER(6),
	OCALLOUT_TIME_LOCAL			NUMBER(10),
	OCALLOUT_NUM_FOREIGN			NUMBER(6),
	OCALLOUT_TIME_FOREIGN			NUMBER(10),
	Reserve					NUMBER(1)
)tablespace SSMN_DATA;
alter table SSMN_STAT_HOUR add constraint PK_SSMN_STAT_HOUR primary key(STAT_TIME) using index tablespace SSMN_IDX;
commit;


-- 每小时统计表 SSMN_STAT_HOURCALL
--drop table SSMN_STAT_HOURCALL;
create table SSMN_STAT_HOURCALL
(
	STAT_TIME	DATE	NOT NULL,
	UPDATE_TIME	DATE	NOT NULL,
	CALLIN_N	NUMBER(6),
	CALLIN_SUCCESS_N	NUMBER(6),
	CALLIN_T	NUMBER(10),
	CALLOUT_N	NUMBER(6),
	CALLOUT_SUCCESS_N	NUMBER(6),
	CALLOUT_T	NUMBER(10),
	IVR_N		NUMBER(6),
	IVR_T		NUMBER(10),
	CALLALL_N	NUMBER(6),
	CALLALL_T	NUMBER(10),
	T_CALLIN_N	NUMBER(6),
	T_CALLIN_T	NUMBER(10),
	U_CALLIN_N	NUMBER(6),
	U_CALLIN_T	NUMBER(10),
	M_CALLIN_N	NUMBER(6),
	M_CALLIN_T	NUMBER(10),
	CALLOUT_L_T_N	NUMBER(6),
	CALLOUT_L_T_T	NUMBER(10),
	CALLOUT_L_U_N	NUMBER(6),
	CALLOUT_L_U_T	NUMBER(10),
	CALLOUT_L_M_N	NUMBER(6),
	CALLOUT_L_M_T	NUMBER(10),
	CALLOUT_F_T_N	NUMBER(6),
	CALLOUT_F_T_T	NUMBER(10),
	CALLOUT_F_U_N	NUMBER(6),
	CALLOUT_F_U_T	NUMBER(10),
	CALLOUT_F_M_N	NUMBER(6),
	CALLOUT_F_M_T	NUMBER(10),
	HIDE_CALLOUT_L_T_N	NUMBER(6),
	HIDE_CALLOUT_L_T_T	NUMBER(10),
	HIDE_CALLOUT_L_U_N	NUMBER(6),
	HIDE_CALLOUT_L_U_T	NUMBER(10),
	HIDE_CALLOUT_L_M_N	NUMBER(6),
	HIDE_CALLOUT_L_M_T	NUMBER(10),
	HIDE_CALLOUT_F_T_N	NUMBER(6),
	HIDE_CALLOUT_F_T_T	NUMBER(10),
	HIDE_CALLOUT_F_U_N	NUMBER(6),
	HIDE_CALLOUT_F_U_T	NUMBER(10),
	HIDE_CALLOUT_F_M_N	NUMBER(6),
	HIDE_CALLOUT_F_M_T	NUMBER(10),
	HIDE_CALLOUT_SUC_N	NUMBER(6),
	Reserve1	NUMBER(10),
	Reserve2	NUMBER(10),
	Reserve3	NUMBER(10)
)tablespace SSMN_DATA;
alter table SSMN_STAT_HOURCALL add constraint PK_SSMN_STAT_HOURCALL primary key(STAT_TIME) using index tablespace SSMN_IDX;
commit;

--drop table ssmn_stat_tmp;
create table ssmn_stat_tmp
(
        msisdn varchar2(20),
        numbers number(10)
) tablespace SSMN_DATA;
commit;


-- 代理商统计表 SSMN_STAT_Agent
-- drop table SSMN_STAT_Agent;
create table SSMN_STAT_Agent
(
	STAT_TIME			DATE		NOT NULL,
	UPDATE_TIME			DATE		NOT NULL,
	AGENT_ID				VARCHAR2(16)		NOT NULL,
	CUR_USER				NUMBER(6),
	APP_USER				NUMBER(10),
	CXL_USER				NUMBER(6),
	Type	 						NUMBER(1) default 0 Not null,
	Reserve				Number(1)
)tablespace SSMN_DATA;
alter table SSMN_STAT_Agent add constraint PK_SSMN_STAT_Agent primary key(STAT_TIME,AGENT_ID,type) using index tablespace SSMN_IDX;
commit;

-- 虚/实号码呼叫统计
-- drop table SSMN_STAT_H_CALL;
create table SSMN_STAT_H_CALL
(
	STAT_TIME			DATE		NOT NULL,
	UPDATE_TIME			DATE		NOT NULL,
	CALLIN_N			NUMBER(6),
	CALLIN_SUCCESS_N	NUMBER(6),
	CALLIN_T			NUMBER(10),
	CALLOUT_N			NUMBER(6),
	CALLOUT_SUCCESS_N	NUMBER(6),
	CALLOUT_T			NUMBER(10),
	IVR_N				NUMBER(6),
	IVR_T				NUMBER(10),
	Type	 						NUMBER(1) default 0 Not null
) tablespace SSMN_DATA;
alter table SSMN_STAT_H_CALL add constraint PK_SSMN_STAT_H_CALL primary key(STAT_TIME,type)
using index tablespace SSMN_IDX;
commit;

--每天虚、实号码的用户、号码、业务使用统计
--drop table SSMN_STAT_D_VRNUM
create table SSMN_STAT_D_VRNUM
(
	STAT_TIME               DATE        NOT NULL,
	UPDATE_TIME             DATE        NOT NULL,
	TYPE                  	NUMBER(1)   NOT NULL,
	CUR_USER                NUMBER(10),
	SMS_CH_NUM_USER         NUMBER(6),
	PREREGIST_USER          NUMBER(6),
	U_APP_CXL               NUMBER(6),
	U_APP_WEB               NUMBER(6),
	U_APP_IVR               NUMBER(6),
	U_APP_SMS               NUMBER(6),
	U_APP_BOSS              NUMBER(6),
	U_APP_BY_OTHER          NUMBER(6),
	U_APP_OTHER             NUMBER(6),
	U_CXL_WEB               NUMBER(6),
	U_CXL_IVR               NUMBER(6),
	U_CXL_SMS               NUMBER(6),
	U_CXL_BOSS              NUMBER(6),
	U_CXL_BY_OTHER          NUMBER(6),
	U_CXL_OTHER             NUMBER(6),
	CUR_USER_NUM1           NUMBER(6),
	CUR_USER_NUM2           NUMBER(6),
	CUR_USER_NUM3           NUMBER(6),
	CUR_USER_NUM4           NUMBER(6),
	CUR_USER_NUM5           NUMBER(6),
	CUR_NUM                 NUMBER(6),
	CUR_FREE_NUM            NUMBER(10),
	CUR_FREEZE_NUM          NUMBER(10),
	CUR_KEEP_NUM            NUMBER(10),
	APP_NUM                 NUMBER(10),
	APP_FREE_NUM            NUMBER(10),
	CXL_NUM                 NUMBER(10),
	CXL_FREE_NUM            NUMBER(10),
	BH_BEGIN                NUMBER(2),
	BH_END                  NUMBER(2),
	MBH                     NUMBER(2),
	BH_CALL_N               NUMBER(6),
	BH_CALL_SUCCESS_N       NUMBER(6),
	BH_CALL_T               NUMBER(10),
	MBH_CALL_N              NUMBER(6),
	MBH_CALL_SUCCESS_N      NUMBER(6),
	MBH_CALL_T              NUMBER(10),
	IVR_N			NUMBER(6),
	IVR_T			NUMBER(10),
	CALLIN_T_N              NUMBER(6),
	CALLIN_T_SUCCESS_N      NUMBER(6),
	CALLIN_T_T              NUMBER(10),
	CALLIN_U_N              NUMBER(6),
	CALLIN_U_SUCCESS_N      NUMBER(6),
	CALLIN_U_T              NUMBER(10),
	CALLIN_M_N              NUMBER(6),
	CALLIN_M_SUCCESS_N      NUMBER(6),
	CALLIN_M_T              NUMBER(10),
	CALLOUT_L_T_N           NUMBER(6),
	CALLOUT_L_T_SUCCESS_N   NUMBER(6),
	CALLOUT_L_T_T           NUMBER(10),
	CALLOUT_L_U_N           NUMBER(6),
	CALLOUT_L_U_SUCCESS_N   NUMBER(6),
	CALLOUT_L_U_T           NUMBER(10),
	CALLOUT_L_M_N           NUMBER(6),
	CALLOUT_L_M_SUCCESS_N   NUMBER(6),
	CALLOUT_L_M_T           NUMBER(10),
	CALLOUT_F_T_N           NUMBER(6),
	CALLOUT_F_T_SUCCESS_N   NUMBER(6),
	CALLOUT_F_T_T           NUMBER(10),
	CALLOUT_F_U_N           NUMBER(6),
	CALLOUT_F_U_SUCCESS_N   NUMBER(6),
	CALLOUT_F_U_T           NUMBER(10),
	CALLOUT_F_M_N           NUMBER(6),
	CALLOUT_F_M_SUCCESS_N   NUMBER(6),
	CALLOUT_F_M_T           NUMBER(10),
	ACTIVE_D1               NUMBER(10),
	ACTIVE_D3               NUMBER(10),
	ACTIVE_D7               NUMBER(10),
	ACTIVE_M1               NUMBER(10),
	ACTIVE_M3               NUMBER(10),
	ACTIVE_1                NUMBER(10),
	ACTIVE_NONE             NUMBER(10)
) tablespace SSMN_DATA;
alter table SSMN_STAT_D_VRNUM add constraint PK_SSMN_STAT_D_VRNUM  primary key(STAT_TIME,TYPE)
using index tablespace SSMN_IDX;
commit;

-- 每天魔话的用户、号码、业务使用统计
-- drop table SSMN_STAT_D_MV;
create table SSMN_STAT_D_MV
(
	STAT_TIME					DATE		NOT NULL,
	UPDATE_TIME					DATE		NOT NULL,
	CUR_USER				NUMBER(10),
	CUR_NUM					NUMBER(10),
	CALLIN_LN_N				NUMBER(6),
	CALLIN_LN_SUCCESS_N		NUMBER(6),
	CALLIN_LN_T				NUMBER(10),
	CALLIN_FN_N				NUMBER(6),
	CALLIN_FN_SUCCESS_N		NUMBER(6),
	CALLIN_FN_T				NUMBER(10),
	CALLOUT_L_LN_N			NUMBER(6),
	CALLOUT_L_LN_SUCCESS_N	NUMBER(6),
	CALLOUT_L_LN_T			NUMBER(10),
	CALLOUT_L_FN_N			NUMBER(6),
	CALLOUT_L_FN_SUCCESS_N	NUMBER(6),
	CALLOUT_L_FN_T			NUMBER(10),
	CALLOUT_F_LN_N			NUMBER(6),
	CALLOUT_F_LN_SUCCESS_N	NUMBER(6),
	CALLOUT_F_LN_T			NUMBER(10),
	CALLOUT_F_FN_N			NUMBER(6),
	CALLOUT_F_FN_SUCCESS_N	NUMBER(6),
	CALLOUT_F_FN_T			NUMBER(10)
) tablespace SSMN_DATA;
alter table SSMN_STAT_D_MV add constraint PK_SSMN_STAT_D_MV primary key(STAT_TIME)
using index tablespace SSMN_IDX;
commit;

-- 每天隐藏号码业务使用统计
-- drop table SSMN_STAT_D_HIDE_NUM_CALL;
create table SSMN_STAT_D_HIDE_NUM_CALL
(
	STAT_TIME				DATE		NOT NULL,
	UPDATE_TIME				DATE		NOT NULL,
	CALLOUT_L_T_N			NUMBER(6),
	CALLOUT_L_T_SUCCESS_N	NUMBER(6),
	CALLOUT_L_T_T			NUMBER(10),
	CALLOUT_L_U_N			NUMBER(6),
	CALLOUT_L_U_SUCCESS_N	NUMBER(6),
	CALLOUT_L_U_T			NUMBER(10),
	CALLOUT_L_M_N			NUMBER(6),
	CALLOUT_L_M_SUCCESS_N	NUMBER(6),
	CALLOUT_L_M_T			NUMBER(10),
	CALLOUT_F_T_N			NUMBER(6),
	CALLOUT_F_T_SUCCESS_N	NUMBER(6),
	CALLOUT_F_T_T			NUMBER(10),
	CALLOUT_F_U_N			NUMBER(6),
	CALLOUT_F_U_SUCCESS_N	NUMBER(6),
	CALLOUT_F_U_T			NUMBER(10),
	CALLOUT_F_M_N			NUMBER(6),
	CALLOUT_F_M_SUCCESS_N	NUMBER(6),
	CALLOUT_F_M_T			NUMBER(10),
	ACTIVE_D1				NUMBER(10),
	ACTIVE_D3				NUMBER(10),
	ACTIVE_D7				NUMBER(10),
	ACTIVE_M1				NUMBER(10),
	ACTIVE_M3				NUMBER(10),
	ACTIVE_1				NUMBER(10),
	HIDENUM_USER            NUMBER(10)	
) tablespace SSMN_DATA;
alter table SSMN_STAT_D_HIDE_NUM_CALL add constraint SSMN_STAT_D_HIDE_NUM_CALL primary key(STAT_TIME)
using index tablespace SSMN_IDX;
commit;

-- 虚/实号码短信统计
-- drop table SSMN_STAT_D_SMS;
create table SSMN_STAT_D_SMS
(
	STAT_TIME                   DATE        NOT NULL,
	UPDATE_TIME                 DATE        NOT NULL,
	RECV                        NUMBER(6),
	SEND                        NUMBER(6),
	SEND_FAIL                   NUMBER(6),
	SEND_FAIL_NUM_FORBIDDEN     NUMBER(6),
	SEND_FAIL_NUM_INVALID       NUMBER(6),
    SEND_FAIL_NUM_OFF           NUMBER(6),
    SEND_FAIL_BLACK_LIST        NUMBER(6),
    SEND_FAIL_NON_USER          NUMBER(6),
	SEND_FAIL_OTHER             NUMBER(6)
) tablespace SSMN_DATA;
alter table SSMN_STAT_D_SMS add constraint PK_SSMN_STAT_D_SMS primary key(STAT_TIME)
using index tablespace SSMN_IDX;
commit;

-- 月收入统计(不分虚实)
-- drop table SSMN_STAT_M_REVENUE;
create table SSMN_STAT_M_REVENUE
(
	STAT_TIME                   DATE        NOT NULL,
	UPDATE_TIME                 DATE        NOT NULL,
	REVENUE_NUM_U		NUMBER(10),
	REVENUE_NUM_N		NUMBER(10),
	REVENUE			NUMBER(8),
	CALLREVENUE		NUMBER(8),
	SMSREVENUE		NUMBER(8),
	SERVICEREVENUE		NUMBER(8)
) tablespace SSMN_DATA;
alter table SSMN_STAT_M_REVENUE add constraint PK_SSMN_STAT_M_REVENUE primary key(STAT_TIME)
using index tablespace SSMN_IDX;
commit;

-- drop table SSMN_BlackList;
create table SSMN_BlackList
(
	BlackNumber       Varchar2(11)      NOT NULL
) tablespace SSMN_DATA;
alter table SSMN_BlackList add constraint PK_SSMN_BlackList primary key(BlackNumber) using index tablespace SSMN_IDX;
commit;

-- 本网号段表 LOCAL_PREFIX
-- drop table LOCAL_PREFIX;
create table LOCAL_PREFIX
(
	Prefix				VARCHAR2(18)	NOT NULL,
	Description			VARCHAR2(32)
)tablespace SSMN_DATA;
alter table LOCAL_PREFIX add constraint PK_LOCAL_PREFIX primary key(Prefix) using index tablespace SSMN_IDX;
commit;

-- 号段表
-- drop table PREFIX;
create table PREFIX
(
	Prefix				VARCHAR2(20)	NOT NULL,
	Operator			Varchar2(2)	NOT NULL,
	Phone_Type			Varchar2(6)	NOT NULL,
	Service_Type			Varchar2(6),
	Area_Id				Varchar2(10),
	Area_Name			Varchar2(10),
	Description			VARCHAR2(20),
	Reserve1			Number(6),
	Reserve2			Number(6),
	Reserve3			Varchar2(20)
)tablespace SSMN_DATA;
alter table PREFIX add constraint PK_PREFIX primary key(Prefix) using index tablespace SSMN_IDX;
Create Index IDX1_PREFIX ON PREFIX (Operator) tablespace SSMN_IDX;
Create Index IDX2_PREFIX ON PREFIX (Phone_Type) tablespace SSMN_IDX;
Create Index IDX3_PREFIX ON PREFIX (Service_Type) tablespace SSMN_IDX;
Create Index IDX4_PREFIX ON PREFIX (Area_Id) tablespace SSMN_IDX;
commit;

--
-- drop table SSMN_Rest_Number;
create table SSMN_Rest_Number
(
	Rest_Number       Varchar2(20)      	NOT NULL,
	Type		  Char(1)		NOT NULL
) tablespace SSMN_DATA;
alter table SSMN_Rest_Number add constraint PK_SSMN_Rest_Number primary key(Rest_Number) using index tablespace SSMN_IDX;
commit;

-- drop table SSMN_SPREAD;
create table SSMN_SPREAD
(
	MSISDN			Varchar2(20)		NOT NULL,
	Spread_Times		number(5)			NOT NULL,
	Last_Upd_time		Date				NOT NULL,
	Reserve			number(1)
)tablespace SSMN_DATA;
alter table SSMN_SPREAD add constraint PK_SSMN_SPREAD primary key(MSISDN) using index tablespace SSMN_IDX;
commit;

-- drop table SSMN_EnableNumber;
create table SSMN_EnableNumber
(
	EnableNumber			Varchar2(18)		NOT NULL,
	Msisdn				Varchar2(20),
	Type				Number(1)	 default 0	NOT NULL,
	Status			Number(1)		NOT NULL,
	Area_Id			Varchar2(10),
	MIN			Varchar2(18),
	ESN			Varchar2(18),
	HLR_GT			Varchar2(32),
	HLR_SSN			Varchar2(32),
	Reserve			Varchar(11),
	Grade 	Number(1)	 Default 1
)tablespace SSMN_DATA;
alter table SSMN_EnableNumber add constraint PK_SSMN_EnableNumber primary key(EnableNumber) using index tablespace SSMN_IDX;
Create Index IDX1_SSMN_EnableNumber ON SSMN_EnableNumber (Status) tablespace SSMN_IDX;
Create Index IDX2_SSMN_EnableNumber ON SSMN_EnableNumber (Type) tablespace SSMN_IDX;
commit;

-- drop table MsgSendApp;
create table MsgSendApp
(
	ID				Number(10)		NOT NULL,
	Service	Number(2) default 0 not null,
	BatchSnb		Varchar2(15),
	SrcNo			Char(40),
	ChargeNo			Char(40),
	ChargeNoType	 Number(1) default 0,
	DestNo			Char(40),
	DestNoType		Number(1) default 0,
	ServiceType		Char(10),
	FeeType			Number(1),
	FeeValue			Char(6) default '0',
	MTFlag			Number(1),
	MsgLevel			Number(1),
	ExpireTime			Char(16),
	ScheduleTime		Char(16),
	ReportFlag			Number(1),
	MsgType			Number(1),
	MsgFormat			Number(2),
	MsgLength			Number(3),
	MsgID			Varchar2(5) NOT NULL,
	MsgContent		Varchar(200),
	Reserve			Char(8)
) tablespace SSMN_DATA;
alter table MsgSendApp add constraint PK_MsgSendApp primary key(ID)
using index tablespace SSMN_IDX;
commit;

-- drop table MsgList;
create table MsgList
(
	MsgID			Varchar2(5)		NOT NULL,
	MsgContent		Varchar2(500)		NOT NULL,
	Description		Varchar2(250),
	Service		Number(2) default 0 not null,
	MsgLevel		Number(1) default 9 Not Null,
	Status			Number(1) default 1 Not Null
)tablespace SSMN_DATA;
alter table MsgList add constraint PK_MsgList primary key(MsgID,Service) using index tablespace SSMN_IDX;
commit;

-- drop table MsgBW;
create table MsgBW
(
	MSISDN			Varchar2(20)		NOT NULL,
	Service	Number(2) not null,
	BW			Number(1) default 0 Not Null
)tablespace SSMN_DATA;
alter table MsgBW add constraint PK_MsgBW primary key(MSISDN,Service) using index tablespace SSMN_IDX;
commit;

-- drop table MsgTask;
create table MsgTask
(
	ID					Number(10)		NOT NULL,
	Description	Varchar2(120)		NOT NULL,
	Service		Number(2) not null,
	BW				Number(1)	Not Null,
	Status			Number(1) Not Null,
	MsisdnListType	Number(1)	NOT NULL,
	MsisdnList	Varchar2(200)	NOT NULL,
	MsgID			Varchar2(5) NOT NULL,
	BatchLimit	Number(3)	default -1 NOT NULL,
	TestMsisdn	Varchar2(20) NOT NULL,
	PlanStartTime	Date,
	CreateUser	Varchar2(20)	Not Null,
	CreateTime	Date Not Null,
	AuditUser	Varchar2(20),
	AuditTime	Date,
	AuditTimes	Number(2),
	Comments	Varchar2(120),
	StartTime		Date,
	EndTime		Date,
	LastMsisdn	Varchar2(20),
	AllNum			Number(8),
	SuccNum		Number(8),
	FailNum			Number(8),
	NoRespNum	Number(8)
)tablespace SSMN_DATA;
alter table MsgTask add constraint PK_MsgTask primary key(ID) using index tablespace SSMN_IDX;
commit;

-- drop table SSMN_Freetry;
create table SSMN_Freetry
(
	MSISDN			Varchar2(20)		NOT NULL,
	Tried_Num		Number(2)		NOT NULL,
	Reserve			Varchar2(11),
	v_tried_num  Number(2) 
)tablespace SSMN_DATA;
alter table SSMN_Freetry add constraint PK_SSMN_Freetry primary key(MSISDN) using index tablespace SSMN_IDX;
commit;

-- drop table SSMN_SPECIALNUM;
create table SSMN_SPECIALNUM
(
	SSMNNumber		Varchar2(18)		NOT NULL,
	Type				Number(1)	 default 0	NOT NULL,
	Number_Type		Number(1)		NOT NULL,
	Reserve			Varchar2(20)
)tablespace SSMN_DATA;
alter table SSMN_SPECIALNUM add constraint PK_SSMN_SPECIALNUM primary key(SSMNNumber) using index tablespace SSMN_IDX;
Create Index IDX1_SSMN_SPECIALNUM ON SSMN_SPECIALNUM (SSMNNumber, Number_Type) tablespace SSMN_IDX;
commit;

-- drop table SSMN_CHARGE_HIS;
create table SSMN_CHARGE_HIS
(
	MSISDN Varchar2(20) NOT NULL,
	LiveNumber Number(5) NOT NULL,
	CxlNumber Number(5) NOT NULL,
	Charge Number(6) NOT NULL,
	Discount_Rate Number(3) NOT NULL,
	FeeType Number(1) NOT NULL,
	FeeTime Date NOT NULL,
	Fee Number(8) NOT NULL,
	Succ Number(1)
) tablespace SSMN_DATA;
alter table SSMN_CHARGE_HIS add constraint PK_SSMN_CHARGE_HIS primary key (MSISDN,FeeType,FeeTime) using index tablespace SSMN_IDX;
commit;

-- drop table SSMN_CHANGE_NUM;
create table SSMN_CHANGE_NUM
(
	MSISDN		Varchar2(20)		NOT NULL,
	FromNumber	Varchar2(18)		NOT NULL,
	ToNumber	Varchar2(18)		NOT NULL,
	ChangeTime	Date			NOT NULL
)tablespace SSMN_DATA;
alter table 
SSMN_CHANGE_NUM add constraint PK_SSMN_CHANGE_NUM primary key (FromNumber,ChangeTime) using index tablespace SSMN_IDX;
Create Index IDX1_SSMN_CHANGE_NUM ON SSMN_CHANGE_NUM to_char(ChangeTime,'yyyymm') tablespace SSMN_IDX;
commit;

-- drop table SSMN_KeepNumber;
create table SSMN_KeepNumber
(
	MSISDN		Varchar2(20)		NOT NULL,
	NewMSISDN	Varchar2(20),
	StartTime	Date			NOT NULL,
	EndTime		Date,
	Status		Char(1)			NOT NULL
)tablespace SSMN_DATA;
alter table 
SSMN_KeepNumber add constraint PK_SSMN_KeepNumber primary key (MSISDN,StartTime) using index tablespace SSMN_IDX;
commit;

-- drop table SSMN_1258NUM;
create table SSMN_1258NUM
(
	SSMNNumber	Varchar2(18)		NOT NULL,
	Number_Type	Number(1)		NOT NULL,
	Reserve		Varchar2(20)
)tablespace SSMN_DATA;
alter table 
SSMN_1258NUM add constraint PK_SSMN_1258NUM primary key (SSMNNumber) using index tablespace SSMN_IDX;
Create Index IDX1_SSMN_1258NUM ON SSMN_1258NUM (SSMNNumber, Number_Type) tablespace SSMN_IDX;
commit;

-- drop table SSMN_1258User;
create table SSMN_1258User
(
	MSISDN		Varchar2(20)		NOT NULL,
	CallingNumber	Varchar2(18)		NOT NULL,
	Reserve		Varchar2(20)
)tablespace SSMN_DATA;
alter table 
SSMN_1258User add constraint PK_SSMN_1258User primary key (MSISDN) using index tablespace SSMN_IDX;
Create Index IDX1_SSMN_1258User ON SSMN_1258User (MSISDN, CallingNumber) tablespace SSMN_IDX;
commit;

-- drop table Discount_Card;
create table Discount_Card
(
	Card_Number	VarChar2(18)	NOT NULL,
	Card_Status	Number(1)	NOT NULL,
	Card_PIN	VarChar2(20)	NOT NULL,
	Create_Date 	Date  		NOT NULL,
	Expire_Date	Date,
	Registe_Date	Date,
	MSISDN		Varchar(20),
	MonthFee	Number(6),
	InfoFee		Number(6),
	Reserve		Varchar2(20),
	Grade Number(1)	 Default 1,
	pinexpire_date 	date	default Null
)tablespace SSMN_DATA;
alter table 
Discount_Card add constraint PK_Discount_Card primary key (Card_Number) using index tablespace SSMN_IDX;
Create Index IDX1_DISCOUNT_Card ON Discount_Card (Card_Number, Card_Status) tablespace SSMN_IDX;
Create Index IDX2_DISCOUNT_Card ON Discount_Card (MSISDN) tablespace SSMN_IDX;
Create Index IDX3_DISCOUNT_CARD ON DISCOUNT_CARD (registe_date) tablespace SSMN_IDX;
Create index IDX4_DISCOUNT_CARD on DISCOUNT_CARD (MSISDN,REGISTE_DATE) tablespace SSMN_IDX;
commit;

-- drop table SSMN_OUTUSER_REGISTER;
create table SSMN_OUTUSER_REGISTER
(
	ID	VarChar2(32)	NOT NULL,
	Regsiter_Date	Timestamp(4)	NOT NULL,
	Group_Name	VarChar2(30)	NOT NULL,
	Register_By 	VarChar2(16) 	NOT NULL,
	Msisdn		VarChar2(20)	NOT NULL,
	Number_Count	Number(2)	NOT NULL,
	reserve		Varchar(20)
)tablespace SSMN_DATA;
alter table 
SSMN_OUTUSER_REGISTER add constraint PK_SSMN_OUTUSER_REGISTER primary key (ID) using index tablespace SSMN_IDX;
Create Index IDX1_SSMN_OUTUSER_REGISTER ON SSMN_OUTUSER_REGISTER (Regsiter_Date, Register_By) tablespace SSMN_IDX;
Create Index IDX2_SSMN_OUTUSER_REGISTER ON SSMN_OUTUSER_REGISTER (Regsiter_Date, Msisdn, reserve) tablespace SSMN_IDX;
commit;

-- drop table SSMN_MV;
create table SSMN_MV
(
	ID				Number(10)			NOT NULL,
	SSMNNumber		Varchar2(18)			NOT NULL,
	MSISDN			Varchar2(20)			NOT NULL,
	MSLevel			Number(2)				NOT NULL,
	Sub_Date			Date					NOT NULL,
	Reserve1			Varchar2(20),
	Reserve2			Varchar2(20)
) tablespace SSMN_DATA;
alter table SSMN_MV add constraint PK_SSMN_MV primary key(SSMNNumber)
using index tablespace SSMN_IDX;
Create Index IDX1_SSMN_MV ON SSMN_MV (SSMNNumber, MSLevel) tablespace SSMN_IDX;
Create Index IDX2_SSMN_MV ON SSMN_MV (MSISDN) tablespace SSMN_IDX;
commit;

-- drop table SSMN_CANCEL_MV;
create table SSMN_CANCEL_MV
(
	ID				Number(10)			NOT NULL,
	SSMNNumber		Varchar2(18)			NOT NULL,
	MSISDN			Varchar2(20)			NOT NULL,
	MSLevel			Number(2)				NOT NULL,
	Sub_Date			Date					NOT NULL,
	Cancel_Date		Date					NOT NULL,
	Reserve1			Varchar2(20),
	Reserve2			Varchar2(20)
) tablespace SSMN_DATA;
alter table SSMN_CANCEL_MV add constraint PK_SSMN_CANCEL_MV primary key(SSMNNumber, Cancel_Date)
using index tablespace SSMN_IDX;
commit;

-- drop table SSMN_FILESUB;
create table SSMN_FILESUB
(
	MSISDN			Varchar2(20)			NOT NULL,
	Sub_Date			Date					NOT NULL,
	INFILE			Varchar2(40),
	Reserve1			Varchar2(20),
	Reserve2			Varchar2(20)
) tablespace SSMN_DATA;
alter table SSMN_FILESUB add constraint PK_SSMN_FILESUB primary key(MSISDN, Sub_Date)
using index tablespace SSMN_IDX;
commit;

-- drop table SSMN_VPNSUB;
create table SSMN_VPNSUB
(
	MSISDN			Varchar2(20)			NOT NULL,
	Sub_Date			Date					NOT NULL,
	FreeTo_Date		Date					NOT NULL
)tablespace SSMN_DATA;
alter table SSMN_VPNSUB add constraint PK_SSMN_VPNSUB primary key(MSISDN,Sub_Date) 
using index tablespace SSMN_IDX;
commit;

-- drop table USER_BANK;
CREATE TABLE USER_BANK 
(
	MSISDN		Varchar(20)		NOT NULL,
	SSMNNumber	VarChar2(18),
	MonthFee	Number(6)	NOT NULL,
	InfoFee		Number(6)	NOT NULL,
	ChangeFee		Number(6)	NOT NULL,
	KeepFee		Number(6)	NOT NULL,
	LAST_UPDATE_TIME	DATE	NOT NULL,
	LAST_UPDATE_BY	VARCHAR2(32)	NOT NULL,
	Last_update_desc VARCHAR2(32),
	ReserveFee1	Number(6),
	ReserveFee2		Number(6),
	Reserve1		Varchar2(20),
	Reserve2		Varchar2(20)
)tablespace SSMN_DATA;
alter table USER_BANK add constraint PK_USER_BANK primary key (MSISDN) using index tablespace SSMN_IDX;

Create Index IDX1_USER_BANK ON USER_BANK (ssmnnumber) tablespace SSMN_IDX;
Create Index IDX2_USER_BANK ON USER_BANK (infofee,monthfee,changefee) tablespace SSMN_IDX;
commit;

--
-- drop table SSMN_OPERATOR_ACTION;
create table SSMN_OPERATOR_ACTION
(
	ID			varchar2(32)			NOT NULL,
	Operation_Date		date				NOT NULL,
	Ope_No			varchar2(30)			NOT NULL,
	Group_Name		varchar2(30)			NOT NULL,
	Operation_Type		numeric(3)			NOT NULL,
	Msisdn			varchar2(11)			,
	Ssmn_Number		varchar2(120)			,
	Before_Operation	varchar2(255)			,
	After_Operation		varchar2(255)			,
	Ext_Attribute1		varchar2(20),
	Ext_Attribute2		varchar2(20),
	Ext_Attribute3		varchar2(20)
)tablespace SSMN_DATA;
alter table SSMN_OPERATOR_ACTION add constraint PK_OPERATOR_ACTION primary key(ID)
using index tablespace SSMN_IDX;
commit;

--
-- drop table SSMN_OPERATION_TYPE;
create table SSMN_OPERATION_TYPE
(
	ID			numeric(3)			NOT NULL,
	Operation_Type		varchar2(50)			NOT NULL,
	Type			Numeric(3),
	Reserve			Varchar2(50)
)tablespace SSMN_DATA;
alter table SSMN_OPERATION_TYPE add constraint PK_OPERATION_TYPE primary key(ID)
using index tablespace SSMN_IDX;
commit;

--
-- drop table SSMN_freenumbers cascade constraints;
create table ssmn_freenumbers
(
	SSMNNumber		Varchar2(18)			NOT NULL,
	MSISDN			Varchar2(20)			NOT NULL,
	Sub_Date		Date,
	Initiate_Date		Date,
	End_Date		Date,
	Sub_Manner		Number(1)			NOT NULL,
	Status			Number(1)			NOT NULL,
	Setting_By		Varchar2(30),
	Setting_Group		Varchar2(30),
	Setting_Date		Date				NOT NULL,
	Reserve1		Varchar2(20),
	Reserve2		Varchar2(20),
	Comments		Varchar2(200)
)tablespace SSMN_DATA;
alter table ssmn_freenumbers add constraint PK_SSMN_FREENUMBERS primary key(SSMNNumber)
using index tablespace SSMN_IDX;

Create index IDX1_SSMN_FREENUMBERS on SSMN_FREENUMBERS (MSISDN) tablespace SSMN_IDX;
commit;

-- 恶意呼叫号码表
-- drop table BlackCallNumber;
create table BlackCallNumber
(
	Black_Number		Varchar2(30)			NOT NULL,
	Description		Varchar2(32),
	Reserve1		Number(6),
	Reserve2		Varchar2(20)
) tablespace SSMN_DATA;
alter table BlackCallNumber add constraint PK_BlackCallNumber primary key(Black_Number)
using index tablespace SSMN_IDX;
commit;

-- 非自主注册用户表
-- drop table SSMN_OTHER_REG;
create table SSMN_OTHER_REG
(
	MSISDN		Varchar2(20)	NOT NULL,
	SSMNNumber	Varchar2(18)	NOT NULL,
	OperatorID	Varchar2(20)	NOT NULL,
	Sub_Manner	Number(2)	NOT NULL,
	Sub_Date	Date		NOT NULL,
	Type		Number(2)	NOT NULL,
	Reserve1	Number(6),
	Reserve2	Varchar2(20),
	Reserve3	Varchar2(20)
) tablespace SSMN_DATA;
alter table SSMN_OTHER_REG add constraint PK_SSMN_OTHER_REG primary key(MSISDN, SSMNNumber, Sub_Date)
using index tablespace SSMN_IDX;
commit;

-- SSMN预注销虚号码表
-- drop table ssmn_p_cancel_num;
create table ssmn_p_cancel_num
(
	MSISDN		Varchar2(20)	not null,
	SSMNNumber	Varchar2(18)	not null,
	Cancel_Date	Date		not null,
	Cancel_Manner	Number(2)	not null,
	State		Number(2)	not null,
	Reserve1	Number(6),
	Reserve2	Varchar2(20),
	Reserve3	Varchar2(20)
) tablespace SSMN_DATA;
alter table ssmn_p_cancel_num add constraint PK_ssmn_p_cancel_num primary key(MSISDN, SSMNNumber, Cancel_Date)
using index tablespace SSMN_IDX;
commit;

--drop table SSMN_CHANGE_NUMBERTYPE;
create table SSMN_CHANGE_NUMBERTYPE
(
	SSMNNumber			Varchar2(18) NOT NULL,
	Initiate_Date			Date NOT NULL,
	MSISDN					Varchar2(20) NOT NULL,
	User_Sub_Date		Date,
	NewNumberType	Number(1) NOT NULL,
	CHANGETIME			Date NOT NULL,
	Status					Number(1) NOT NULL,
	TimeStamp				Date NOT NULL,
	Reserve					Varchar2(20)
) tablespace SSMN_DATA;
alter table SSMN_CHANGE_NUMBERTYPE add constraint PK_SSMN_CHANGE_NUMBERTYPE primary key (SSMNNumber, ChangeTime) 
using index tablespace SSMN_IDX;
create index IDX1_SSMN_CHANGE_NUMBERTYPE ON ssmn_change_numbertype (user_sub_date,msisdn,status) tablespace SSMN_IDX;
create index IDX2_SSMN_CHANGE_NUMBERTYPE ON ssmn_change_numbertype (status) tablespace SSMN_IDX;
create index IDX4_SSMN_CHANGE_NUMBERTYPE ON ssmn_change_numbertype (Msisdn,NewNumberType,Initiate_Date) tablespace SSMN_IDX;
create index IDX5_SSMN_CHANGE_NUMBERTYPE ON ssmn_change_numbertype (TimeStamp) tablespace SSMN_IDX;
commit;

--drop table SSMN_CHARGE_NUM;
create table SSMN_CHARGE_NUM
(
	SSMNNumber			Varchar2(18) NOT NULL,
	Type				Number(1)	 default 0	NOT NULL,
	Initiate_Date			Date NOT NULL,
	Cancel_Date			Date,
	Number_Type			Number(1) NOT NULL,
	MSISDN					Varchar2(20) NOT NULL,
	User_Sub_Date		Date,
	User_Cxl_Date			Date,
	user_Sub_Manner	Number(1),
	user_Cxl_Manner		Number(1),
	ChangeNumFlag		Number(1) NOT NULL,
	Status					Number(1) NOT NULL,
	TimeStamp				Date NOT NULL,
	Reserve					Varchar2(20)
) tablespace SSMN_DATA;
alter table SSMN_CHARGE_NUM add constraint PK_SSMN_CHARGE_NUM primary key (SSMNNumber, Initiate_Date) 
using index tablespace SSMN_IDX;
create index IDX1_SSMN_CHARGE_NUM ON ssmn_charge_num (user_sub_date,msisdn,status) tablespace SSMN_IDX;
create index IDX2_SSMN_CHARGE_NUM ON ssmn_charge_num (status) tablespace SSMN_IDX;
create index IDX4_SSMN_CHARGE_NUM ON ssmn_charge_num (Msisdn,Number_Type,Initiate_Date,Cancel_Date) tablespace SSMN_IDX;
create index IDX5_SSMN_CHARGE_NUM ON ssmn_charge_num (status,ChangeNumFlag) tablespace SSMN_IDX;
create index IDX6_SSMN_CHARGE_NUM ON ssmn_charge_num (Msisdn,User_Sub_Date,User_Cxl_Date) tablespace SSMN_IDX;
create index IDX8_SSMN_CHARGE_NUM ON ssmn_charge_num (TimeStamp) tablespace SSMN_IDX;
commit;

--drop table SSMN_CHARGE_CDR;
create table SSMN_CHARGE_CDR
(
	Streamnumber		Number(10)	NOT NULL,
	CallingAddress			Varchar2(18) NOT NULL,
	CalledAddress			Varchar2(32) NOT NULL,
	MSISDN					Varchar2(20),
	Sub_Date				Date,
	CallType					Number(1) NOT NULL,
	CallStartTime			Date NOT NULL,
	CallStopTime			Date NOT NULL,
	CallLong					Number(8) NOT NULL,
	Status					Number(1) NOT NULL,
	TimeStamp				Date NOT NULL,
	Reserve					Varchar2(20)
) tablespace SSMN_DATA;
alter table SSMN_CHARGE_CDR add constraint PK_SSMN_CHARGE_CDR primary key (Streamnumber) 
using index tablespace SSMN_IDX;
create index IDX1_SSMN_CHARGE_CDR on ssmn_charge_cdr (sub_date,msisdn,status) tablespace SSMN_IDX;
create index IDX2_SSMN_CHARGE_CDR on ssmn_charge_cdr (status) tablespace SSMN_IDX;
create index IDX4_SSMN_CHARGE_CDR on ssmn_charge_cdr (Status,Callstoptime,callstarttime,CallType) tablespace SSMN_IDX;
create index IDX5_SSMN_CHARGE_CDR on ssmn_charge_cdr (callingAddress,calledAddress,msisdn) tablespace SSMN_IDX;
create index IDX7_SSMN_CHARGE_CDR on ssmn_charge_cdr (TimeStamp) tablespace SSMN_IDX;
commit;

--drop table SSMN_BILL;
create table SSMN_BILL
(
	ID						Number(10)		NOT NULL,
	BillSnb				Varchar2(15),
	MSISDN				Varchar2(20) NOT NULL,
	Sub_Date			Date,
	FeeTime				Date NOT NULL,
	BillTime				Date,
	ActTime				Date,
	OriginalBillSnb		Varchar2(15),
	FeeType			Number(1) NOT NULL,
	FeeElement		Number(5) NOT NULL,
	ServiceChange	Number(1),
	OldService			Number(2),
	ReceivableFee		Number(8) NOT NULL,
	FeeValue			Number(8) NOT NULL,
	BillStamp			Varchar2(120),
	Status				Number(1) NOT NULL,
	ErrorCode			Number(5),
	ErrorMsg				Varchar2(120),
	Comments			Varchar2(120),
	SSMNNumber		Varchar2(18),
	Initiate_Date		Date,
	CHANGETIME		Date,
	Streamnumber	Number(10),
	AccountType		Number(1),
	AccountFee		Number(6),
	Reserve1			Varchar2(20),
	Reserve2			Varchar2(20),
	Reserve3			Number(5)
) tablespace SSMN_DATA;
alter table SSMN_BILL add constraint PK_SSMN_BILL primary key (ID) 
using index tablespace SSMN_IDX;
create index IDX1_SSMN_BILL on SSMN_BILL (MSISDN,FEETIME,FEETYPE) tablespace SSMN_IDX;
create index IDX2_SSMN_BILL on SSMN_BILL (SSMNNumber) tablespace SSMN_IDX;
create index IDX3_SSMN_BILL on SSMN_BILL (Streamnumber) tablespace SSMN_IDX;
create index IDX4_SSMN_BILL on SSMN_BILL (ORIGINALBILLSNB) tablespace SSMN_IDX;
create index IDX5_SSMN_BILL on SSMN_BILL (STATUS,ERRORCODE) tablespace SSMN_IDX;
commit;

--drop table SSMN_BILL_TMP;
create table SSMN_BILL_TMP
(
	ID						Number(10)		NOT NULL,
	BillSnb				Varchar2(15),
	MSISDN				Varchar2(20) NOT NULL,
	Sub_Date			Date,
	FeeTime				Date NOT NULL,
	BillTime				Date,
	ActTime				Date,
	OriginalBillSnb		Varchar2(15),
	FeeType			Number(1) NOT NULL,
	FeeElement		Number(5) NOT NULL,
	ServiceChange	Number(1),
	OldService			Number(2),
	ReceivableFee		Number(8) NOT NULL,
	FeeValue			Number(8) NOT NULL,
	BillStamp			Varchar2(120),
	Status				Number(1) NOT NULL,
	ErrorCode			Number(5),
	ErrorMsg				Varchar2(120),
	Comments			Varchar2(120),
	SSMNNumber		Varchar2(18),
	Initiate_Date		Date,
	CHANGETIME		Date,
	Streamnumber	Number(18),
	AccountType		Number(1),
	AccountFee		Number(6),
	Reserve1			Varchar2(20),
	Reserve2			Varchar2(20),
	Reserve3			Number(5)
) tablespace SSMN_DATA;
alter table SSMN_BILL_TMP add constraint PK_SSMN_BILL_TMP primary key (ID) 
using index tablespace SSMN_IDX;
create index IDX1_SSMN_BILL_TMP on SSMN_BILL_TMP (MSISDN,FEETIME,FEETYPE) tablespace SSMN_IDX;
create index IDX2_SSMN_BILL_TMP on SSMN_BILL_TMP (SSMNNumber) tablespace SSMN_IDX;
create index IDX3_SSMN_BILL_TMP on SSMN_BILL_TMP (Streamnumber) tablespace SSMN_IDX;
create index IDX4_SSMN_BILL_TMP on SSMN_BILL_TMP (ORIGINALBILLSNB) tablespace SSMN_IDX;
create index IDX5_SSMN_BILL_TMP on SSMN_BILL_TMP (STATUS,ERRORCODE) tablespace SSMN_IDX;
commit;

--drop table SSMN_USER_ACCOUNT;
create table SSMN_USER_ACCOUNT
(
	MSISDN				Varchar2(20) NOT NULL,
	Sub_Date			Date NOT NULL,
	FeeType			Number(1) NOT NULL,
	AccountType		Number(1) NOT NULL,
	FreeStartDate		Date NOT NULL,
	FreeMonths		Number(2) NOT NULL,
	MinFee				Number(6) NOT NULL,
	MaxFee				Number(6) NOT NULL,
	Balance				Number(6) NOT NULL,
	FeeUsed				Number(6) NOT NULL,
	Balance_Tmp		Number(6) NOT NULL,
	FeeUsed_Tmp		Number(6) NOT NULL,
	ValidStartDate		Date NOT NULL,
	ValidMonths		Number(2) NOT NULL,
	TimeStamp	 		Date,
	Comments			Varchar2(120) NOT NULL,
	Reserve1			Number(6),
	Reserve2			Varchar2(20),
	Reserve3			Varchar2(20)
) tablespace SSMN_DATA;
alter table SSMN_USER_ACCOUNT add constraint PK_SSMN_USER_ACCOUNT primary key (Msisdn, Sub_Date, FeeType, AccountType) 
using index tablespace SSMN_IDX;
create index IDX1_SSMN_USER_ACCOUNT on SSMN_USER_ACCOUNT (FeeType, AccountType) tablespace SSMN_IDX;
commit;

--drop table MsgSend_TMP ;
create table MsgSend_TMP
(
	ID				Number(10)		NOT NULL,
	Service	Number(2) default 0 not null,
	BatchSnb		Varchar2(15),
	SrcNo			Char(40),
	ChargeNo			Char(40),
	ChargeNoType	 Number(1) default 0,
	DestNo			Char(40),
	DestNoType		Number(1) default 0,
	ServiceType		Char(10),
	FeeType			Number(1),
	FeeValue			Char(6) default '0',
	MTFlag			Number(1),
	MsgLevel			Number(1),
	ExpireTime			Char(16),
	ScheduleTime		Char(16),
	ReportFlag			Number(1),
	MsgType			Number(1),
	MsgFormat			Number(2),
	MsgLength			Number(3),
	MsgContent		Varchar(200),
	LinkID			Char(40),
	Reserve			Char(8)
) tablespace SSMN_DATA;
alter table MsgSend_TMP add constraint PK_MsgSend_TMP primary key(ID)
using index tablespace SSMN_IDX;
commit;


-- drop table SSMN_Recommend;
create table SSMN_Recommend
(
	MSISDN				Varchar2(20)		NOT NULL,
	RecNum				Varchar2(20)		NOT NULL,
	CurrentNum			Varchar2(20)		NOT NULL,
	Rec_Time			Date,
	RecStatus			Number(2)		NOT NULL,
	Reserve1			Number(2),
	Reserve2			Varchar2(10)
) tablespace SSMN_DATA;
alter table SSMN_Recommend add constraint PK_SSMN_Recommend primary key (MSISDN, RecNum) 
using index tablespace SSMN_IDX;
create index IDX1_SSMN_Recommend on SSMN_Recommend (RecNum, RecStatus) tablespace SSMN_IDX;
create index IDX2_SSMN_Recommend on SSMN_Recommend (Rec_Time) tablespace SSMN_IDX;
commit;

-- drop table SSMN_RecUserInfo;
create table SSMN_RecUserInfo
(
	MSISDN				Varchar2(20)		NOT NULL,
	UserCount			Number(3),
	FreeCount			Number(3),
	Free_Time			Date,
	Reserve1			Number(2),
	Reserve2			Varchar2(10)
) tablespace SSMN_DATA;
alter table SSMN_RecUserInfo add constraint PK_SSMN_RecUserInfo primary key (MSISDN) 
using index tablespace SSMN_IDX;
commit;

--drop table SSMN_FreeRecord;
create table SSMN_FreeRecord
(
	MSISDN				Varchar2(20)		NOT NULL,
	FreeNumber			Varchar2(20)		NOT NULL,
	In_Time				Date			NOT NULL,
	Reserve1			Number(2),
	Reserve2			Varchar2(10)
) tablespace SSMN_DATA;
alter table SSMN_FreeRecord add constraint PK_SSMN_FreeRecord primary key (MSISDN, FreeNumber) 
using index tablespace SSMN_IDX;
Create Index IDX1_SSMN_FreeRecord ON SSMN_FreeRecord (In_Time) tablespace SSMN_IDX;
commit;

--drop table SSMN_UserServer;
create table SSMN_UserServer
(
	MSISDN				Varchar2(20)		NOT NULL,
	ALLTIMEACTIVE	Varchar2(1)		NOT NULL,
	START_TIME1	Varchar2(4),
	END_TIME1	Varchar2(4),
	START_TIME2	Varchar2(4),
	END_TIME2	Varchar2(4),
	PRIORITY			Number(2)		NOT NULL,
	status			Number(2)	default 0	NOT NULL
) tablespace SSMN_DATA;
alter table SSMN_UserServer add constraint PK_SSMN_UserServer primary key (MSISDN) 
using index tablespace SSMN_IDX;
commit;

--drop table SSMN_USER_ACTION_RECORD;
create table SSMN_USER_ACTION_RECORD
(
	SSMNNUMBER		VARCHAR2(18) NOT NULL,
	MSISDN				VARCHAR2(20) NOT NULL,
	USER_ACTION		VARCHAR2(8) NOT NULL,
	INITIATE_DATE		DATE NOT NULL,
	RESERVE				VARCHAR2(8)
) tablespace SSMN_DATA;
alter table SSMN_USER_ACTION_RECORD add constraint PK_SSMN_USER_ACTION_RECORD primary key (SSMNNUMBER,INITIATE_DATE) 
using index tablespace SSMN_IDX;
create index IDX1_SSMN_USER_ACTION_RECORD ON ssmn_user_action_record (msisdn,user_action) tablespace SSMN_IDX;
commit;


--drop table SSMN_TEST_NUM_IN;
create table SSMN_Test_Num_in
(
	SSMNNumber		Varchar2(18)		NOT NULL,
	TimeStamp			Date NOT NULL,
	Streamnumber	Number(10)
) tablespace SSMN_DATA;
alter table SSMN_Test_Num_in add constraint PK_SSMN_Test_Num_in primary key(SSMNNumber)
using index tablespace SSMN_IDX;
commit;


--drop table SSMN_TEST_NUM_OUT;
create table SSMN_Test_Num_out
(
	Streamnumber	Number(10)			NOT NULL,
	SSMNNumber		Varchar2(18)		NOT NULL,
	CallStartTime		Date		NOT NULL,
	CallStopTime		Date		NOT NULL,
	Test_Result		Number(2)			NOT NULL,
	Rel_Cause		Number(3)			NOT NULL,
	User_Input		Varchar2(2)
) tablespace SSMN_DATA;
alter table SSMN_Test_Num_out add constraint PK_SSMN_Test_Num_out primary key(Streamnumber)
using index tablespace SSMN_IDX;
Create index IDX1_SSMN_Test_Num_out on SSMN_Test_Num_out(CallStartTime) tablespace SSMN_IDX;
commit;


-- drop table Temp_paras;
create table Temp_paras
(
	seqcallid Number(10),
	Calling_PI     Number(6),
	Calling_NI     Number(6),
	Calling_SI     Number(6),
	CPC       Number(6),
	ACMBCI Varchar2(20),
	ACMOBCI     Number(10),
	ACMCAUSE  Number(10),
	Change_Pin  Char(1),
	Apply_Number     Char(1),
	Set_CLI Char(1),
	Cancel_Number   Char(1),
	Apply_Service      Char(1),
	Cancel_Service    Char(1),
	loc Varchar2(10),
	outgoing_cpn Varchar2(20),
	VR_FLAG	 Varchar2(20)	,
	USSDMsisdn varchar2(32),
	userussdflag number(1),
	USSDOpen  number(1) 
) tablespace SSMN_DATA;
commit;

-- drop table SSMN_VIPUser;
create table  SSMN_VIPUser
(   
  MSISDN     Varchar2(32)     NOT NULL
)
tablespace SSMN_DATA;
alter table SSMN_VIPUser add constraint PK_SSMN_VIPUser primary key(MSISDN)
using index tablespace SSMN_IDX;
commit;


-- /*create table SSMN_USER_Audit*/
create table SSMN_USER_Audit
(
	Old_MSISDN			Varchar2(20),
	Old_PIN			Varchar2(6),
	Old_Sub_Manner		Number(1),
	Old_ServiceStatus		Char(1),
	Old_Temp_Pin		Char(6),
	Old_CRBT_Flag		Char(1),
	Old_UserName		Varchar2(20),
	Old_UserID			Varchar2(20),
	Old_CallingType		Number(1),
	Old_CallingNumber		Varchar2(18),
	Old_Sub_Date		Date,
	Old_Question		Varchar2(30),
	Old_Answer			Varchar2(30),
	Old_Agent_ID		Varchar2(16),
	Old_Cancel_Manner		Number(1),
	New_MSISDN			Varchar2(20),
	New_PIN			Varchar2(6),
	New_Sub_Manner		Number(1),
	New_ServiceStatus		Char(1),
	New_Temp_Pin		Char(6),
	New_CRBT_Flag		Char(1),
	New_UserName		Varchar2(20),
	New_UserID			Varchar2(20),
	New_CallingType		Number(1),
	New_CallingNumber		Varchar2(18),
	New_Sub_Date		Date,
	New_Question		Varchar2(30),
	New_Answer			Varchar2(30),
	New_Agent_ID		Varchar2(16),
	New_Cancel_Manner		Number(1),
	Audit_OpTime	Date,
	Audit_OpIP		varchar2(20),	
	Audit_OpName	varchar2(20),
	Audit_OpType	Number(3)
) tablespace SSMN_DATA;

--/*Create trigger on Update_SSMN_User_Audit */
CREATE OR REPLACE TRIGGER TRG_Update_SsmnUser BEFORE
UPDATE ON SSMN_USER FOR EACH ROW 
begin
	Insert into SSMN_User_Audit
	(Old_MSISDN,		Old_PIN,		Old_Sub_Manner,
	Old_ServiceStatus,	Old_Temp_Pin,	Old_CRBT_Flag,
	Old_UserName,		Old_UserID,		Old_CallingType,
	Old_CallingNumber,	Old_Sub_Date,	Old_Question,
	Old_Answer,			Old_Agent_ID,	Old_Cancel_Manner,
	New_MSISDN,			New_PIN,		New_Sub_Manner,
	New_ServiceStatus,	New_Temp_Pin,	New_CRBT_Flag,
	New_UserName,		New_UserID,		New_CallingType,
	New_CallingNumber,	New_Sub_Date,	New_Question,
	New_Answer,			New_Agent_ID,	New_Cancel_Manner,
	Audit_OpTime,
	Audit_OpIP,		Audit_OpName,		Audit_OpType
	)
	values
	(:old.MSISDN,		:old.PIN,		:old.Sub_Manner,
	:old.ServiceStatus,	:old.Temp_Pin,	:old.CRBT_Flag,
	:old.UserName,		:old.UserID,	:old.CallingType,
	:old.CallingNumber,	:old.Sub_Date,	:old.Question,
	:old.Answer,			:old.Agent_ID,	:old.Cancel_Manner,
	:new.MSISDN,			:new.PIN,		:new.Sub_Manner,
	:new.ServiceStatus,		:new.Temp_Pin,	:new.CRBT_Flag,
	:new.UserName,			:new.UserID,	:new.CallingType,
	:new.CallingNumber,		:new.Sub_Date,	:new.Question,
	:new.Answer,			:new.Agent_ID,	:new.Cancel_Manner,	
	sysdate,
	SYS_CONTEXT ('USERENV','IP_ADDRESS'), 	ora_login_user, 1	
	);
end;
/

--/*Create trigger on DEL_SSMN_User_Audit */
CREATE OR REPLACE TRIGGER TRG_DEL_SsmnUser BEFORE
DELETE ON SSMN_USER FOR EACH ROW 
begin
	Insert into SSMN_User_Audit
	(Old_MSISDN,		Old_PIN,		Old_Sub_Manner,
	Old_ServiceStatus,	Old_Temp_Pin,	Old_CRBT_Flag,
	Old_UserName,		Old_UserID,		Old_CallingType,
	Old_CallingNumber,	Old_Sub_Date,	Old_Question,
	Old_Answer,			Old_Agent_ID,	Old_Cancel_Manner,
	Audit_OpTime,
	Audit_OpIP,		Audit_OpName,		Audit_OpType
	)
	values
	(:old.MSISDN,		:old.PIN,		:old.Sub_Manner,
	:old.ServiceStatus,	:old.Temp_Pin,	:old.CRBT_Flag,
	:old.UserName,		:old.UserID,		:old.CallingType,
	:old.CallingNumber,	:old.Sub_Date,	:old.Question,
	:old.Answer,			:old.Agent_ID,	:old.Cancel_Manner,
	sysdate,
	SYS_CONTEXT ('USERENV','IP_ADDRESS'), 	ora_login_user, 2	
	);
end;
/

--/*Create trigger on Insert_SSMN_User_Audit	(2006-08-29 Add) */
CREATE OR REPLACE TRIGGER TRG_Insert_SsmnUser BEFORE
INSERT ON SSMN_USER FOR EACH ROW 
begin
	Insert into SSMN_User_Audit
	(New_MSISDN,			New_PIN,		New_Sub_Manner,
	New_ServiceStatus,	New_Temp_Pin,	New_CRBT_Flag,
	New_UserName,		New_UserID,		New_CallingType,
	New_CallingNumber,	New_Sub_Date,	New_Question,
	New_Answer,			New_Agent_ID,	New_Cancel_Manner,
	Audit_OpTime,
	Audit_OpIP,		Audit_OpName,		Audit_OpType
	)
	values
	(:new.MSISDN,			:new.PIN,		:new.Sub_Manner,
	:new.ServiceStatus,		:new.Temp_Pin,	:new.CRBT_Flag,
	:new.UserName,			:new.UserID,	:new.CallingType,
	:new.CallingNumber,		:new.Sub_Date,	:new.Question,
	:new.Answer,			:new.Agent_ID,	:new.Cancel_Manner,	
	sysdate,
	SYS_CONTEXT ('USERENV','IP_ADDRESS'), 	ora_login_user, 3	
	);
end;
/

-- /*create table SSMN_NUMBER_Audit*/

create table SSMN_NUMBER_Audit
(
	Old_SSMNNumber		Varchar2(18),
	Old_Type		Number(1),
	Old_Number_Type		Number(1),
	Old_MSISDN			Varchar2(20),      
	Old_Called_Type		Number(1),       
	Old_Restriction_Type	Char(1),
	Old_Status			Number(1),
	Old_Prompt_Type		Number(1),
	Old_Initiate_Date		Date,
	Old_Cancel_Date		Date,
	Old_Sub_Manner		Number(1),
	Old_Cancel_Manner		Number(1),
	New_SSMNNumber		Varchar2(18),
	New_Type		Number(1),
	New_Number_Type		Number(1),
	New_MSISDN			Varchar2(20),      
	New_Called_Type		Number(1),       
	New_Restriction_Type	Char(1),
	New_Status			Number(1),
	New_Prompt_Type		Number(1),
	New_Initiate_Date		Date,
	New_Cancel_Date		Date,
	New_Sub_Manner		Number(1),
	New_Cancel_Manner		Number(1),
	Audit_OpTime	Date,
	Audit_OpIP		varchar2(20),	
	Audit_OpName	varchar2(20),
	Audit_OpType	Number(3),
        CFStatus	Number(2),
        SFStatus	Number(2),
        FTStatus	Number(2)
) tablespace SSMN_DATA;

-- 存放实号码关闭情况下要发出的短信的数据表 SSMN_TEMP_MSG
-- drop table SSMN_TEMP_MSG ;
create table SSMN_TEMP_MSG
(
	ID				Number(10)		NOT NULL,
	T_SrcNo			Char(40),
	T_ChargeNo			Char(40),
	T_DestNo			Char(40),
	T_ServiceType		Char(10),
	T_FeeType			Number(1),
	T_FeeValue			Char(6) default '0',
	T_MTFlag			Number(1),
	T_MsgLevel			Number(1),
	T_ExpireTime			Char(16),
	T_ScheduleTime		Char(16),
	T_ReportFlag			Number(1),
	T_MsgType			Number(1),
	T_MsgFormat			Number(2),
	T_MsgLength			Number(3),
	T_MsgContent		Varchar(200),
	T_Reserve			Char(8)
) tablespace SSMN_DATA;
alter table SSMN_TEMP_MSG add constraint PK_SSMN_TEMP_MSG primary key(ID)
using index tablespace SSMN_IDX;
commit;

--/*Create trigger on Update_SSMN_NUMBER_Audit */
CREATE OR REPLACE TRIGGER TRG_Update_SsmnNUMBER BEFORE
UPDATE ON SSMN_NUMBER FOR EACH ROW 
begin
	Insert into SSMN_NUMBER_Audit
	(
	Old_SSMNNumber,		Old_Number_Type,		Old_MSISDN,   Old_Type,
	Old_Called_Type,	Old_Restriction_Type,	Old_Status,
	Old_Prompt_Type,	Old_Initiate_Date,		Old_Cancel_Date,
	Old_Sub_Manner,		Old_Cancel_Manner,	
	New_SSMNNumber,		New_Number_Type,		New_MSISDN,      New_Type,
	New_Called_Type,	New_Restriction_Type,	New_Status,
	New_Prompt_Type,	New_Initiate_Date,	New_Cancel_Date,
	New_Sub_Manner,		New_Cancel_Manner,
	Audit_OpTime,
	Audit_OpIP,		Audit_OpName,		Audit_OpType
	)
	values
	(
	:old.SSMNNumber,	:old.Number_Type,		:old.MSISDN,     :old.Type, 
	:old.Called_Type,	:old.Restriction_Type,	:old.Status,
	:old.Prompt_Type,	:old.Initiate_Date,		:old.Cancel_Date,
	:old.Sub_Manner,	:old.Cancel_Manner,	
	:new.SSMNNumber,	:new.Number_Type,		:new.MSISDN,     :new.Type,      
	:new.Called_Type,	:new.Restriction_Type,	:new.Status,
	:new.Prompt_Type,	:new.Initiate_Date,		:new.Cancel_Date,
	:new.Sub_Manner,	:new.Cancel_Manner,
	sysdate,
	SYS_CONTEXT ('USERENV','IP_ADDRESS'), 	ora_login_user, 1	
	);
end;
/

--/*Create trigger on DEL_SSMN_NUMBER_Audit */
CREATE OR REPLACE TRIGGER TRG_DEL_SsmnNUMBER BEFORE
DELETE ON SSMN_NUMBER FOR EACH ROW 
begin
	Insert into SSMN_NUMBER_Audit
	(Old_SSMNNumber,		Old_Number_Type,		Old_MSISDN,   Old_Type,      
	Old_Called_Type,	Old_Restriction_Type,	Old_Status,
	Old_Prompt_Type,	Old_Initiate_Date,		Old_Cancel_Date,
	Old_Sub_Manner,		Old_Cancel_Manner,	
	Audit_OpTime,
	Audit_OpIP,		Audit_OpName,		Audit_OpType
	)
	values
	(:old.SSMNNumber,	:old.Number_Type,		:old.MSISDN,     :old.Type,      
	:old.Called_Type,	:old.Restriction_Type,	:old.Status,
	:old.Prompt_Type,	:old.Initiate_Date,		:old.Cancel_Date,
	:old.Sub_Manner,	:old.Cancel_Manner,	
	sysdate,
	SYS_CONTEXT ('USERENV','IP_ADDRESS'), 	ora_login_user, 2	
	);
end;
/

--/*Create trigger on Insert_SSMN_NUMBER_Audit 	(2006-08-25 Add)*/
CREATE OR REPLACE TRIGGER TRG_Insert_SsmnNUMBER BEFORE
INSERT ON SSMN_NUMBER FOR EACH ROW 
begin
	Insert into SSMN_NUMBER_Audit
	(New_SSMNNumber,		New_Number_Type,		New_MSISDN,      New_Type,      
	New_Called_Type,	New_Restriction_Type,	New_Status,
	New_Prompt_Type,	New_Initiate_Date,	New_Cancel_Date,
	New_Sub_Manner,		New_Cancel_Manner,
	Audit_OpTime,
	Audit_OpIP,		Audit_OpName,		Audit_OpType
	)
	values
	(:new.SSMNNumber,	:new.Number_Type,		:new.MSISDN,     :new.Type,      
	:new.Called_Type,	:new.Restriction_Type,	:new.Status,
	:new.Prompt_Type,	:new.Initiate_Date,		:new.Cancel_Date,
	:new.Sub_Manner,	:new.Cancel_Manner,
	sysdate,
	SYS_CONTEXT ('USERENV','IP_ADDRESS'), 	ora_login_user, 3	
	);
end;
/

-- /*create table SSMN_OTHER_REG_Audit (2006-08-29 Add)*/
create table SSMN_OTHER_REG_Audit
(
	Old_MSISDN		Varchar2(20),
	Old_SSMNNumber	Varchar2(18),
	Old_OperatorID	Varchar2(20),
	Old_Sub_Manner	Number(2),
	Old_Sub_Date	Date,
	Old_Type		Number(2),
	Old_Reserve1	Number(6),
	Old_Reserve2	Varchar2(20),
	Old_Reserve3	Varchar2(20),
	New_MSISDN		Varchar2(20),
	New_SSMNNumber	Varchar2(18),
	New_OperatorID	Varchar2(20),
	New_Sub_Manner	Number(2),
	New_Sub_Date	Date,
	New_Type		Number(2),
	New_Reserve1	Number(6),
	New_Reserve2	Varchar2(20),
	New_Reserve3	Varchar2(20),
	Audit_OpTime	Date,
	Audit_OpIP		varchar2(20),	
	Audit_OpName	varchar2(20),
	Audit_OpType	Number(3)
)tablespace SSMN_DATA;

Create OR Replace Trigger TRG_INSERT_SSMN_OTHER_REG
BEFORE INSERT ON SSMN_OTHER_REG 
FOR EACH ROW
BEGIN
  INSERT INTO SSMN_OTHER_REG_Audit
   (
    New_MSISDN,New_SSMNNumber,New_OperatorID,New_Sub_Manner,
    New_Sub_Date,New_Type,New_Reserve1,New_Reserve2,
    New_Reserve3,Audit_OpTime,Audit_OpIP,Audit_OpName,Audit_OpType
    )
 values
   (
	:new.MSISDN,:new.SSMNNumber,:new.OperatorID,:new.Sub_Manner,
	:new.Sub_Date,:new.Type,:new.Reserve1,:new.Reserve2,
	:new.Reserve3,sysdate,SYS_CONTEXT ('USERENV','IP_ADDRESS'), 	
	ora_login_user, 3
	);
END;
/

Create OR Replace Trigger TRG_DEL_SSMN_OTHER_REG
BEFORE DELETE ON SSMN_OTHER_REG 
FOR EACH ROW
BEGIN
  INSERT INTO SSMN_OTHER_REG_Audit
  (
    Old_MSISDN,Old_SSMNNumber,Old_OperatorID,Old_Sub_Manner,
	Old_Sub_Date,Old_Type,Old_Reserve1,Old_Reserve2,
	Old_Reserve3,Audit_OpTime,Audit_OpIP,Audit_OpName,
	Audit_OpType
  )
 values
 (
   :old.MSISDN,:old.SSMNNumber,:old.OperatorID,
   :old.Sub_Manner,:old.Sub_Date,:old.Type,
   :old.Reserve1,:old.Reserve2,:old.Reserve3,
	sysdate,SYS_CONTEXT ('USERENV','IP_ADDRESS'),
   ora_login_user,2
  );
END;
/

Create OR Replace Trigger TRG_UPDATE_SSMN_OTHER_REG
BEFORE UPDATE ON SSMN_OTHER_REG 
FOR EACH ROW
BEGIN
  INSERT INTO SSMN_OTHER_REG_Audit
  (
    Old_MSISDN,Old_SSMNNumber,Old_OperatorID,
	Old_Sub_Manner,Old_Sub_Date,Old_Type,
	Old_Reserve1,Old_Reserve2,Old_Reserve3,
    New_MSISDN,New_SSMNNumber,New_OperatorID,
	New_Sub_Manner,New_Sub_Date,New_Type,
	New_Reserve1,New_Reserve2,New_Reserve3,
	Audit_OpTime,Audit_OpIP,Audit_OpName,
	Audit_OpType
  )
  values
  (
    :old.MSISDN,:old.SSMNNumber,:old.OperatorID,
	:old.Sub_Manner,:old.Sub_Date,:old.Type,
	:old.Reserve1,:old.Reserve2,:old.Reserve3,
	:new.MSISDN,:new.SSMNNumber,:new.OperatorID,
	:new.Sub_Manner,:new.Sub_Date,:new.Type,
	:new.Reserve1,:new.Reserve2,:new.Reserve3,
	sysdate,SYS_CONTEXT ('USERENV','IP_ADDRESS'), 	
    ora_login_user, 1
   );
END;
/

--/*Create SSMN_CANCEL_NUM_Audit (2006-08-29 Add)*/
CREATE TABLE SSMN_CANCEL_NUM_Audit
(
	Old_SSMNNumber		Varchar2(18),
	Old_Type		Number(1),
	Old_MSISDN			Varchar2(20),
	Old_Initiate_Date		Date,
	Old_Cancel_Date		Date,
	Old_Number_Type		Number(1),
	Old_Sub_Manner		Number(1),
	Old_Cancel_Manner		Number(1),
	New_SSMNNumber		Varchar2(18),
	New_Type		Number(1),
	New_MSISDN			Varchar2(20),
	New_Initiate_Date		Date,
	New_Cancel_Date		Date,
	New_Number_Type		Number(1),
	New_Sub_Manner		Number(1),
	New_Cancel_Manner		Number(1),
	Audit_OpTime	Date,
	Audit_OpIP		varchar2(20),	
	Audit_OpName	varchar2(20),
	Audit_OpType	Number(3),
        CFStatus	Number(2),
        SFStatus	Number(2),
        FTStatus	Number(2)
) tablespace SSMN_DATA;

CREATE OR REPLACE TRIGGER TRG_INSERT_SSMN_CANCEL_NUM
BEFORE INSERT ON SSMN_CANCEL_NUM
FOR EACH ROW
BEGIN
  INSERT INTO SSMN_CANCEL_NUM_Audit
  (  
    New_SSMNNumber,New_MSISDN,New_Initiate_Date,      New_Type,
	New_Cancel_Date,New_Number_Type,New_Sub_Manner,
	New_Cancel_Manner,Audit_OpTime,Audit_OpIP,	
	Audit_OpName,Audit_OpType
  )
  values
  ( 
    :new.SSMNNumber,:new.MSISDN,:new.Initiate_Date,     :new.Type,
    :new.Cancel_Date,:new.Number_Type,:new.Sub_Manner,
	:new.Cancel_Manner,sysdate,
	SYS_CONTEXT ('USERENV','IP_ADDRESS'),
	ora_login_user, 3
  );
END;
/

CREATE OR REPLACE TRIGGER TRG_DEL_SSMN_CANCEL_NUM
BEFORE DELETE ON SSMN_CANCEL_NUM
FOR EACH ROW
BEGIN
  INSERT INTO SSMN_CANCEL_NUM_Audit
  (  
    Old_SSMNNumber,Old_MSISDN,Old_Initiate_Date,   Old_Type,
	Old_Cancel_Date,Old_Number_Type,Old_Sub_Manner,
	Old_Cancel_Manner,Audit_OpTime,Audit_OpIP,	
    Audit_OpName,Audit_OpType
  )
  values
 ( 
   :old.SSMNNumber,:old.MSISDN,:old.Initiate_Date,     :old.Type,
   :old.Cancel_Date,:old.Number_Type,:old.Sub_Manner,
   :old.Cancel_Manner,sysdate,SYS_CONTEXT ('USERENV','IP_ADDRESS'),
   ora_login_user, 2
 );

END;
/

CREATE OR REPLACE TRIGGER TRG_UPDATE_SSMN_CANCEL_NUM
BEFORE UPDATE ON SSMN_CANCEL_NUM
FOR EACH ROW
BEGIN
  INSERT INTO SSMN_CANCEL_NUM_Audit
  (  
    Old_SSMNNumber,Old_MSISDN,Old_Initiate_Date,   Old_Type,
	Old_Cancel_Date,Old_Number_Type,Old_Sub_Manner,
	Old_Cancel_Manner,New_SSMNNumber,New_MSISDN,      New_Type,
	New_Initiate_Date,New_Cancel_Date,New_Number_Type,
	New_Sub_Manner,New_Cancel_Manner,Audit_OpTime,
	Audit_OpIP,Audit_OpName,Audit_OpType
  )
  values
 ( 
   :old.SSMNNumber,:old.MSISDN,:old.Initiate_Date,     :old.Type,
   :old.Cancel_Date,:old.Number_Type,:old.Sub_Manner,
   :old.Cancel_Manner,:new.SSMNNumber,:new.MSISDN,     :new.Type,
   :new.Initiate_Date,:new.Cancel_Date,:new.Number_Type,
   :new.Sub_Manner,:new.Cancel_Manner,sysdate,
   SYS_CONTEXT ('USERENV','IP_ADDRESS'),ora_login_user, 1
 );
END;
/

--/*Create SSMN_CANCEL_USER_Audit (2006-08-29 Add)*/
CREATE TABLE SSMN_CANCEL_USER_Audit
(	Old_MSISDN			Varchar2(20),
	Old_PIN			Varchar2(6),
	Old_Sub_Manner		Number(1),
	Old_ServiceStatus		Char(1),
	Old_Temp_Pin		Char(6),
	Old_CRBT_Flag		Char(1),
	Old_UserName		Varchar2(20),
	Old_UserID			Varchar2(20),
	Old_CallingType		Number(1),
	Old_CallingNumber		Varchar2(18),
	Old_Sub_Date		Date,
	Old_Question		Varchar2(30),
	Old_Answer			Varchar2(30),
	Old_Agent_ID		Varchar2(16),
	Old_Cancel_Manner		Number(1),
	Old_Cancel_Date		Date,
	New_MSISDN			Varchar2(20),
	New_PIN			Varchar2(6),
	New_Sub_Manner		Number(1),
	New_ServiceStatus		Char(1),
	New_Temp_Pin		Char(6),
	New_CRBT_Flag		Char(1),
	New_UserName		Varchar2(20),
	New_UserID			Varchar2(20),
	New_CallingType		Number(1),
	New_CallingNumber		Varchar2(18),
	New_Sub_Date		Date,
	New_Question		Varchar2(30),
	New_Answer			Varchar2(30),
	New_Agent_ID		Varchar2(16),
	New_Cancel_Manner		Number(1),
	New_Cancel_Date		Date,
	Audit_OpTime	Date,
	Audit_OpIP		varchar2(20),	
	Audit_OpName	varchar2(20),
	Audit_OpType	Number(3)
)tablespace SSMN_DATA;

CREATE OR REPLACE TRIGGER TRG_INSERT_SSMN_CANCEL_USER
BEFORE INSERT ON SSMN_CANCEL_USER
FOR EACH ROW
BEGIN
  INSERT INTO SSMN_CANCEL_USER_Audit
  ( 
    New_MSISDN,New_PIN,New_Sub_Manner,New_ServiceStatus,
	New_Temp_Pin,New_CRBT_Flag,New_UserName,New_UserID,
	New_CallingType,New_CallingNumber,New_Sub_Date,
	New_Question,New_Answer,New_Agent_ID,New_Cancel_Manner,
	New_Cancel_Date,Audit_OpTime,Audit_OpIP,Audit_OpName,
	Audit_OpType
  )
  VALUES
  (
    :new.MSISDN,:new.PIN,:new.Sub_Manner,:new.ServiceStatus,
	:new.Temp_Pin,:new.CRBT_Flag,:new.UserName,:new.UserID,
	:new.CallingType,:new.CallingNumber,:new.Sub_Date,
	:new.Question,:new.Answer,:new.Agent_ID,:new.Cancel_Manner,
	:new.Cancel_Date,sysdate,SYS_CONTEXT ('USERENV','IP_ADDRESS'),
	ora_login_user, 3
  );
END;
/

CREATE OR REPLACE TRIGGER TRG_DEL_SSMN_CANCEL_USER
BEFORE DELETE ON SSMN_CANCEL_USER
FOR EACH ROW
BEGIN
  INSERT INTO SSMN_CANCEL_USER_Audit
  ( 
    Old_MSISDN,Old_PIN,Old_Sub_Manner,Old_ServiceStatus,
	Old_Temp_Pin,Old_CRBT_Flag,Old_UserName,Old_UserID,
	Old_CallingType,Old_CallingNumber,Old_Sub_Date,
    Old_Question,Old_Answer,Old_Agent_ID,Old_Cancel_Manner,
	Old_Cancel_Date,Audit_OpTime,Audit_OpIP,Audit_OpName,
    Audit_OpType
  )
  VALUES
  (
    :old.MSISDN,:old.PIN,:old.Sub_Manner,:old.ServiceStatus,
	:old.Temp_Pin,:old.CRBT_Flag,:old.UserName,:old.UserID,
	:old.CallingType,:old.CallingNumber,:old.Sub_Date,
	:old.Question,:old.Answer,:old.Agent_ID,:old.Cancel_Manner,
	:old.Cancel_Date,sysdate,SYS_CONTEXT ('USERENV','IP_ADDRESS'),
	ora_login_user, 2
  );
END;
/

CREATE OR REPLACE TRIGGER TRG_UPDATE_SSMN_CANCEL_USER
BEFORE UPDATE ON SSMN_CANCEL_USER
FOR EACH ROW
BEGIN
  INSERT INTO SSMN_CANCEL_USER_Audit
  ( 
    Old_MSISDN,Old_PIN,Old_Sub_Manner,Old_ServiceStatus,
	Old_Temp_Pin,Old_CRBT_Flag,Old_UserName,Old_UserID,
	Old_CallingType,Old_CallingNumber,Old_Sub_Date,
	Old_Question,Old_Answer,Old_Agent_ID,Old_Cancel_Manner,
    Old_Cancel_Date,New_MSISDN,New_PIN,New_Sub_Manner,
	New_ServiceStatus,New_Temp_Pin,New_CRBT_Flag,
	New_UserName,New_UserID,New_CallingType,New_CallingNumber,
	New_Sub_Date,New_Question,New_Answer,New_Agent_ID,
	New_Cancel_Manner,New_Cancel_Date,Audit_OpTime,
	Audit_OpIP,	Audit_OpName,Audit_OpType
  )
  VALUES
  (
    :old.MSISDN,:old.PIN,:old.Sub_Manner,:old.ServiceStatus,
	:old.Temp_Pin,:old.CRBT_Flag,:old.UserName,:old.UserID,
	:old.CallingType,:old.CallingNumber,:old.Sub_Date,
	:old.Question,:old.Answer,:old.Agent_ID,:old.Cancel_Manner,
	:old.Cancel_Date,:new.MSISDN,:new.PIN,:new.Sub_Manner,
	:new.ServiceStatus,:new.Temp_Pin,:new.CRBT_Flag,
	:new.UserName,:new.UserID,:new.CallingType,:new.CallingNumber,
	:new.Sub_Date,:new.Question,:new.Answer,:new.Agent_ID,
	:new.Cancel_Manner,:new.Cancel_Date,sysdate,
	SYS_CONTEXT ('USERENV','IP_ADDRESS'),ora_login_user, 1
  );
END;
/

CREATE OR REPLACE TRIGGER SSMN_DEL_NUMBER_TRG BEFORE
UPDATE OF NUMBER_TYPE ON SSMN_NUMBER FOR EACH ROW
declare
num1 number(6);
num2 number(6);
begin
  if :new.number_type=3 and :old.number_type<=1 and DBMS_REPUTIL.FROM_REMOTE = FALSE then
    delete SSMN_MV where ssmnnumber=:old.ssmnnumber;
    delete ssmn_called_num where ssmnnumber=:old.ssmnnumber;
    delete ssmn_called_time where ssmnnumber=:old.ssmnnumber;
    delete ssmn_calling_num where callingnumber=:old.ssmnnumber;
    delete ssmn_calling_time where callingnumber=:old.ssmnnumber;
    select count(*) into num1 from ssmn_user a where a.msisdn=:old.msisdn;
	if num1>0 then
		select callingtype into num2 from ssmn_user a where a.msisdn=:old.msisdn;
		if num2=2 then
					select count(*) into num1 from ssmn_calling_num
											  where msisdn=:old.msisdn;
					if num1=0 then
						update ssmn_user set callingtype=0 where msisdn=:old.msisdn;
				end if;
		end if;
		if num2=1 then
					select count(*) into num1 from ssmn_calling_time a
											  where a.msisdn=:old.msisdn;
					if num1=0 then
						update ssmn_user set callingtype=0 where msisdn=:old.msisdn;
				end if;	
		end if;
	end if;
  end if;
end;
/

-- 初始化system_cfg数据
insert into system_cfg values ('access_code',1,'0');
insert into system_cfg values ('retain_period',1,'0');
insert into system_cfg values ('CDR_period',1,'0');
insert into system_cfg values ('sms_access_code',1,'0');
insert into system_cfg values ('ssmn_length',1,'0');
insert into system_cfg values ('r_ssmn_length',1,'11');
insert into system_cfg values ('max_number',1,'0');
insert into system_cfg values ('max_realnumber',1,'0');
insert into system_cfg values ('preregist',1,'0');
insert into system_cfg values ('cancelservice',1,'0');
insert into system_cfg values ('applyssmn',1,'0');
insert into system_cfg values ('cancelssmn',1,'0');
insert into system_cfg values ('changessmn',1,'0');
insert into system_cfg values ('changepin',1,'0');
insert into system_cfg values ('cancelpin',1,'0');
insert into system_cfg values ('smspin',1,'0');
insert into system_cfg values ('trymaxtime',1,'0');
insert into system_cfg values ('regservice',1,'0');
insert into system_cfg values ('cancelservice_sms',1,'0');
insert into system_cfg values ('confirmreg',1,'0');
insert into system_cfg values ('servicetype',1,'0');
insert into system_cfg values ('local_operator',1,'0');
insert into system_cfg values ('customer',1,'0');
insert into system_cfg values ('cancelserviceDB',1,'0');
insert into system_cfg values ('bosstype',1,'0');
insert into system_cfg values ('changenumtime',1,'0');
insert into system_cfg values ('hidingcin',1,'0');
insert into system_cfg values ('maxerrortimes',1,'0');
insert into system_cfg values ('maxovertimes',1,'0');
insert into system_cfg values ('help_access_code',1,'0');
insert into system_cfg values ('cancelnum_sms',1,'0');
insert into system_cfg values ('monchangenum',1,'0');
insert into system_cfg values ('promptservice',1,'0');
insert into system_cfg values ('ordertype',1,'0');
insert into system_cfg values ('regservicesms',1,'0');
insert into system_cfg values ('r_access_code',1,'0');
insert into system_cfg values ('sms_servicetype',1,'0');
insert into system_cfg values ('sms_prefixcode',0,'0');
insert into system_cfg values ('net_transfer_flag',1,'1');
insert into system_cfg values ('ussdflag',1,'0');
insert into system_cfg values ('flagcpnprefix',1,'86');
insert into system_cfg values ('call_remind_flag',1,'0');
insert into system_cfg values ('local_areacode',1,'本地区号');
insert into system_cfg values ('access_code3',1,'当地接入码');
insert into system_cfg values ('prefix_cin',1,'来话呼叫副号码前所加标识值');
commit;

-- 初始化MsgList数据
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('ZC001','短信业务注册','sub— 业务注册成功短信',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('ZC002','短信业务注册','sub— 号码被占用提示',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('ZC003','短信业务注册','sub— 输入号码不在可分配号段内',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('ZC004','短信业务注册','sub— 输入号码格式不正确',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('ZC005','短信业务注册','sub— 已经是注册用户',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('ZC006','短信业务注册','sub— 身份不合法',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('ZC007','短信业务注册','sub— 二次确认',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('ZX001','短信业务注销','sub— 密码错误',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('ZX002','短信业务注销','sub— 格式错误',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('ZX003','短信业务注销','sub— 非业务用户或者帐号挂起',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('ZX004','短信业务注销','sub— 短信注销成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('SQ001','短信号码申请','sub— 申请号码数量超过限制提示',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('SQ002','短信号码申请','sub— 申请号码成功提示',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('SQ003','短信号码申请','sub— 密码不正确',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('SQ004','短信号码申请','sub— 虚号码格式不正确',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('SQ005','短信号码申请','sub— 格式不正确',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('SQ006','短信号码申请','sub— 非业务用户或者帐号挂起',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('SQ007','短信号码申请','sub— 号码占用',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('SQ008','短信号码申请','sub— 无号码资源',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('SQ009','短信号码申请','sub— 输入号码不在可分配号段内',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('XH001','短信号码注销','sub— 非业务用户',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('XH002','短信号码注销','sub— 不能注销默认号码',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('XH003','短信号码注销','sub— 注销号码同时注销业务成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('XH004','短信号码注销','sub— 注销的号码不是用户的号码',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('XH005','短信号码注销','sub— 注销虚号码成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('XH006','短信号码注销','sub— 删除默认号码',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('XH007','短信号码注销','sub— 密码错误',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('XH008','短信号码注销','sub— 虚号码格式错误',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('XH009','短信号码注销','sub— 输入格式错误',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('XH010','短信号码注销','sub— 提醒用户注销号码会注销业务',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('MM001','短信取回密码','sub— 非业务用户',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('MM002','短信取回密码','sub— 取回密码成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('MM003','短信取回密码','sub— 格式不正确',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('KJ001','短信开机 ','sub— 非业务用户',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('KJ002','短信开机 ','sub— 开启默认号码',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('KJ003','短信开机 ','sub— 用户只有一个号码',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('KJ004','短信开机 ','sub— 指定号码不是业务用户号码',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('KJ005','短信开机 ','sub— 号码格式错误',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('GJ001','短信关机','sub— 非业务用户',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('GJ002','短信关机','sub— 关闭默认号码',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('GJ003','短信关机','sub— 用户只有一个号码',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('GJ004','短信关机','sub— 指定号码不是业务用户号码',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('GJ005','短信关机','sub— 号码格式错误',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('BZ001','短信帮助','sub— 获取帮助信息成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('BZ002','短信帮助','sub— 不满足业务申请条件的非业务用户',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('HH001','换号','sub— 非业务用户',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('HH002','换号','sub— 为默认号码换号',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('HH003','换号','sub— 指定的被替换号码不是用户号码',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('HH004','换号','sub— 指定号码不可用',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('HH005','换号','sub— 换号成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('HH006','换号','sub— 指定号码不在可分配号段内',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('HH007','换号','sub— 号码格式错误',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('JS001','业务介绍','sub— 成功索取业务介绍',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('ER001','任何短信命令','sub— 命令不可识别',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('WE001','业务注册','web— 注册成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('WE002','申请号码','web— 申请号码',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('WE003','业务注册','web— 验证码',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('WE004','业务注册','web— 客服热线注册',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('WE008','Admin WEB注销号码','web— 注销成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('WE009','Admin WEB注销业务','web— 注销成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('IV001','关联短信','ivr— 关联短信',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('IV002','业务注册','ivr— 注册成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('IV003','号码申请','ivr— 申请成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('IV004','关机提示','ivr— 关机来电下发',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('IV005','白名单','ivr— 白名单下发',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('IV006','黑名单','ivr— 黑名单下发',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('IV007','保号','ivr— 保号成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('IV008','保号','ivr— 激活成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('IV009','注销号码','ivr— 注销号码成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('IV010','注销业务','ivr— 注销业务成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('IV012','换号','ivr— 换号成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('IV014','体验满','ivr— 用户体验满n次发送广告短信',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('IV021','当月注册用户注销','ivr— 注销业务成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('IV022','非当月注册用户注销','ivr— 注销业务成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('OT001','用户注册','other— 批量绑定',1,8,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('OT002','短信杂志','other— 注册成功',1,0,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('OT003','随意','other— 随意',1,0,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('OT004','计费短信','other— 计费短信',1,8,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('OT005','注册业务','Provision— 注册业务成功',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('OT006','注销业务','Provision— 注销业务',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('OT007','免费转收费','other— 免费转收费',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('OT008','注销业务','Provision— 注销业务0',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('OT009','注销业务','Provision— 注销业务1',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('OT010','注销业务','Provision— 注销业务2',1,9,1);
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('OT011','注销业务','Provision— 注销业务3',1,9,1);
commit;

-- 初始化SSMN_RIGHT表,系统权限
insert into ssmn_right(right,rightdesc) values('SUPE','系统超级用户管理员');
insert into ssmn_right(right,rightdesc) values('SSYS','系统配置');
insert into ssmn_right(right,rightdesc) values('CPWD','操作员修改密码');
insert into ssmn_right(right,rightdesc) values('LOGV','操作员日志查询');
insert into ssmn_right(right,rightdesc) values('USRV','用户查看功能');
insert into ssmn_right(right,rightdesc) values('USRA','代理注册用户');
insert into ssmn_right(right,rightdesc) values('USRM','用户修改功能');
insert into ssmn_right(right,rightdesc) values('USRD','用户删除功能');
insert into ssmn_right(right,rightdesc) values('CUNS','用户停机挂起功能');
insert into ssmn_right(right,rightdesc) values('UPDC','用户密码修改功能');
insert into ssmn_right(right,rightdesc) values('NUMV','查看');
insert into ssmn_right(right,rightdesc) values('NUMA','代理申请号码功能');
insert into ssmn_right(right,rightdesc) values('NUMM','修改');
insert into ssmn_right(right,rightdesc) values('NUMD','删除');
insert into ssmn_right(right,rightdesc) values('CUFR','设置免费和收费');
insert into ssmn_right(right,rightdesc) values('CACV','历史信息查询');
insert into ssmn_right(right,rightdesc) values('INFV','信息费查询');
insert into ssmn_right(right,rightdesc) values('CGFV','收费记录查询');
insert into ssmn_right(right,rightdesc) values('NCGF','新收费记录查询');
insert into ssmn_right(right,rightdesc) values('UCOV','用户呼叫记录查询');
insert into ssmn_right(right,rightdesc) values('UMGV','短信查询');
insert into ssmn_right(right,rightdesc) values('FREN','解冻功能');
insert into ssmn_right(right,rightdesc) values('APSR','代理注册查询');

-- 创建Admin组
Insert into ssmn_group(groupname,groupdesc,last_upd_user,last_upd_date)
values('AdminGroup','系统管理员组','System',sysdate);

-- 创建CM管理的管理员所在组。
Insert into ssmn_group(groupname,groupdesc,last_upd_user,last_upd_date)
values('TTOPE0','靓号管理员组','System',sysdate);
Insert into ssmn_group(groupname,groupdesc,last_upd_user,last_upd_date)
values('TTOPE1','靓号管理员组','System',sysdate);
Insert into ssmn_group(groupname,groupdesc,last_upd_user,last_upd_date)
values('TTOPE2','靓号管理员组','System',sysdate);

-- 创建操作员
insert into
sms_operator(openo,opename,opepwd,groupname,last_upd_user,last_upd_date)
values('0000000000','admin','6ef80433e6d0dbf6fa4651fe108bee3f','AdminGroup','System',sysdate);

-- 在没有外呼群组管理功能的点，不要运行此条语句！！
-- insert into ssmn_group_right(groupname,right)
--     values('AdminGroup','OUMG');

insert into ssmn_group_right(groupname,right)
     values('AdminGroup','USRV');
insert into ssmn_group_right(groupname,right)
     values('AdminGroup','USRA');
insert into ssmn_group_right(groupname,right)
     values('AdminGroup','USRM');
insert into ssmn_group_right(groupname,right)
     values('AdminGroup','USRD');
insert into ssmn_group_right(groupname,right)
     values('AdminGroup','NUMV');
insert into ssmn_group_right(groupname,right)
     values('AdminGroup','NUMA');
insert into ssmn_group_right(groupname,right)
     values('AdminGroup','NUMM');
insert into ssmn_group_right(groupname,right)
     values('AdminGroup','NUMD');
insert into ssmn_group_right(groupname,right)
     values('AdminGroup','CDRV');
insert into ssmn_group_right(groupname,right)
     values('AdminGroup','CACV');
insert into ssmn_group_right(groupname,right)
     values('AdminGroup','STAT');
insert into ssmn_group_right(groupname,right)
     values('AdminGroup','SUPE');
insert into ssmn_group_right(groupname,right)
     values('AdminGroup','SSYS');
insert into ssmn_group_right(groupname,right)
     values('AdminGroup','CPWD');
insert into ssmn_group_right(groupname,right)
     values('AdminGroup','CUNS');
insert into ssmn_group_right(groupname,right)
     values('AdminGroup','CUFR');
insert into ssmn_group_right(groupname,right)
     values('AdminGroup','UPDC');


-- 创建其他操作员组的初始化脚本
Insert into ssmn_group(groupname,groupdesc,last_upd_user,last_upd_date)
	values('SYSOperGroup','系统操作员组','System',sysdate);
Insert into ssmn_group(groupname,groupdesc,last_upd_user,last_upd_date)
	values('CustomerServiceGroup','客户服务操作员组','System',sysdate);
Insert into ssmn_group(groupname,groupdesc,last_upd_user,last_upd_date)
	values('CutomerCreateGroup','用户开户操作员组','System',sysdate);
Insert into ssmn_group(groupname,groupdesc,last_upd_user,last_upd_date)
	values('CustomerChangeGroup','用户信息维护操作员组','System',sysdate);
Insert into ssmn_group(groupname,groupdesc,last_upd_user,last_upd_date)
	values('CustomerDeleteGroup','用户销户操作员组','System',sysdate);
Insert into ssmn_group(groupname,groupdesc,last_upd_user,last_upd_date)
	values('PasswordChangeGroup','用户密码维护操作员组','System',sysdate);
Insert into ssmn_group(groupname,groupdesc,last_upd_user,last_upd_date)
	values('CustomerStopGroupp','用户停机维护操作员组','System',sysdate);
Insert into ssmn_group(groupname,groupdesc,last_upd_user,last_upd_date)
	values('CstomerDerateGroup','用户免资费维护操作员组','System',sysdate);

-- 初始化系统操作员组权限
insert into ssmn_group_right(groupname,right)
     values('SYSOperGroup','USRV');
insert into ssmn_group_right(groupname,right)
     values('SYSOperGroup','USRA');
insert into ssmn_group_right(groupname,right)
     values('SYSOperGroup','USRM');
insert into ssmn_group_right(groupname,right)
     values('SYSOperGroup','USRD');
insert into ssmn_group_right(groupname,right)
     values('SYSOperGroup','NUMV');
insert into ssmn_group_right(groupname,right)
     values('SYSOperGroup','NUMA');
insert into ssmn_group_right(groupname,right)
     values('SYSOperGroup','NUMM');
insert into ssmn_group_right(groupname,right)
     values('SYSOperGroup','NUMD');
insert into ssmn_group_right(groupname,right)
     values('SYSOperGroup','CDRV');
insert into ssmn_group_right(groupname,right)
     values('SYSOperGroup','CACV');
insert into ssmn_group_right(groupname,right)
     values('SYSOperGroup','STAT');
insert into ssmn_group_right(groupname,right)
     values('SYSOperGroup','UPDC');

-- 客户服务操作员组
insert into ssmn_group_right(groupname,right)
     values('CustomerServiceGroup','USRV');
insert into ssmn_group_right(groupname,right)
     values('CustomerServiceGroup','USRA');
insert into ssmn_group_right(groupname,right)
     values('CustomerServiceGroup','USRM');
insert into ssmn_group_right(groupname,right)
     values('CustomerServiceGroup','USRD');
insert into ssmn_group_right(groupname,right)
     values('CustomerServiceGroup','NUMV');
insert into ssmn_group_right(groupname,right)
     values('CustomerServiceGroup','NUMA');
insert into ssmn_group_right(groupname,right)
     values('CustomerServiceGroup','NUMM');
insert into ssmn_group_right(groupname,right)
     values('CustomerServiceGroup','NUMD');
-- 用户开户操作员组
insert into ssmn_group_right(groupname,right)
     values('CutomerCreateGroup','USRV');
insert into ssmn_group_right(groupname,right)
     values('CutomerCreateGroup','USRA');
insert into ssmn_group_right(groupname,right)
     values('CutomerCreateGroup','NUMV');
insert into ssmn_group_right(groupname,right)
     values('CutomerCreateGroup','NUMA');


-- 用户信息维护操作员组
insert into ssmn_group_right(groupname,right)
     values('CustomerChangeGroup','USRV');
insert into ssmn_group_right(groupname,right)
     values('CustomerChangeGroup','USRM');
insert into ssmn_group_right(groupname,right)
     values('CustomerChangeGroup','NUMV');
insert into ssmn_group_right(groupname,right)
     values('CustomerChangeGroup','NUMM');
-- 用户销户操作员组
insert into ssmn_group_right(groupname,right)
     values('CustomerDeleteGroup','USRV');
insert into ssmn_group_right(groupname,right)
     values('CustomerDeleteGroup','USRD');
insert into ssmn_group_right(groupname,right)
     values('CustomerDeleteGroup','NUMV');
insert into ssmn_group_right(groupname,right)
     values('CustomerDeleteGroup','NUMD');
-- 用户密码维护操作员组
insert into ssmn_group_right(groupname,right)
     values('PasswordChangeGroup','USRV');
insert into ssmn_group_right(groupname,right)
     values('PasswordChangeGroup','UPDC');

-- 用户停机维护操作员组
insert into ssmn_group_right(groupname,right)
     values('CustomerStopGroupp','USRV');
insert into ssmn_group_right(groupname,right)
     values('CustomerStopGroupp','CUNS');

-- 用户免资费维护操作员组
insert into ssmn_group_right(groupname,right)
     values('CstomerDerateGroup','USRV');
insert into ssmn_group_right(groupname,right)
     values('CstomerDerateGroup','CUFR');

insert into sms_operator(openo, opepwd, groupname,last_upd_user,last_upd_date) 
values ('vip0', 'vip0', 'TTOPE0','System',sysdate);
insert into sms_operator(openo, opepwd, groupname,last_upd_user,last_upd_date) 
values ('vip1', 'vip1', 'TTOPE1','System',sysdate);
insert into sms_operator(openo, opepwd, groupname,last_upd_user,last_upd_date) 
values ('vip2', 'vip2', 'TTOPE2','System',sysdate);

-- 创建CM Web不分级时的用户
insert into sms_operator(openo, opepwd, groupname,last_upd_user,last_upd_date) 
values ('vip', 'vip', 'CUSTOMMNG','System',sysdate);

insert into ssmn_number (ssmnnumber, number_type, called_type, servicestatus) values ('777777',5,0,'N');
update ssmn_number set number_type = 5 where ssmnnumber = '777777';

--
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(1,'注册用户');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(2,'注册外呼群组用户');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(3,'注册靓号用户');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(4,'注销用户');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(5,'注销号码');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(6,'修改用户密码');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(7,'修改用户状态');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(8,'修改用户信息');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(9,'查看用户信息');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(10,'查询用户信息');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(11,'修改号码呼入屏蔽方式');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(12,'根据时间设置呼入属性');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(13,'根据时间删除呼入属性');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(14,'设置允许呼入名单');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(15,'删除允许呼入名单');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(16,'设置禁止呼入名单');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(17,'删除禁止呼入名单');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(18,'修改号码收费方式');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(19,'申请号码');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(20,'历史信息查询');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(21,'外呼群组注册信息查询');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(22,'增加操作员');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(23,'删除操作员');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(24,'修改操作员信息');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(25,'增加组');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(26,'删除组');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(27,'修改组信息');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(28,'修改操作员密码');

insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(29,'Admin增加号码');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(30,'CM增加号码');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(31,'外呼群组增加号码');

insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(32,'根据被叫设置呼出显示方式');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(33,'删除根据被叫设置呼出显示方式');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(34,'根据时间设置呼出显示方式');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(35,'删除根据时间设置呼出显示方式');
insert into SSMN_OPERATION_TYPE (id,Operation_Type)values(36,'解冻号码');

INSERT INTO SSMN_OPERATION_TYPE (ID, OPERATION_TYPE) VALUES ('37','新收费记录查询' );
INSERT INTO SSMN_OPERATION_TYPE (ID, OPERATION_TYPE) VALUES ('38','用户呼叫记录查询' );
INSERT INTO SSMN_OPERATION_TYPE (ID, OPERATION_TYPE) VALUES ('39','短信查询' );
INSERT INTO SSMN_OPERATION_TYPE (ID, OPERATION_TYPE) VALUES ('40','代理注册用户查询' );
INSERT INTO SSMN_OPERATION_TYPE (ID, OPERATION_TYPE) VALUES ('41','代理申请号码查询' );
INSERT INTO SSMN_OPERATION_TYPE (ID, OPERATION_TYPE) VALUES ('42','我的日志查询' );
INSERT INTO SSMN_OPERATION_TYPE (ID, OPERATION_TYPE) VALUES ('43','日志信息查询' );

commit;


验证码表脚本：
-- drop table SSMN_ConfirmCode;
create table SSMN_ConfirmCode
(
MSISDN            Varchar2(20)    NOT NULL,
Code              Varchar2(20),
SendTime          Date                 NOT NULL,
LastActiveTime    Date             NOT NULL,
TempCode          Varchar2(20),
getnum            Number(1),
usertype          number(2)        default 0   not null 
) tablespace SSMN_DATA;
alter table SSMN_ConfirmCode add constraint PK_SSMN_ConfirmCode primary key(MSISDN, usertype)
using index tablespace SSMN_IDX;
commit;

--  终端版本信息表
--drop table SSMN_UpgradeInfo;
create table SSMN_UpgradeInfo
(
  ID              Varchar2(10)     NOT NULL,
  APPCODE         Varchar2(32),
  APPNAME         Varchar2(64),
  VERSIONTYPE     Number(2),
  APPURL          Varchar2(500)     NOT NULL ,
  VERSIONNAME     Varchar2(32)     NOT NULL ,
  VERSIONCODE     Number(3)         NOT NULL ,
  OSTYPE          Number(2)  ,
  UPDATELOG       Varchar2(512) ,
  CREATEUSER      Varchar2(32)  ,
  INTIME          DATE     NOT NULL 
) tablespace SSMN_DATA;
ALTER TABLE SSMN_UpgradeInfo ADD CONSTRAINT PK_SSMN_UpgradeInfo primary key(ID)
using index tablespace SSMN_DATA;

-- 设备基本信息表
--drop table SSMN_deviceInfo;
create table  SSMN_deviceInfo
(
     Id                Number(10) NOT NULL ,
     servicekey        Number(4) not null ,
     intime            DATE not null ,
     appname           VARCHAR2(32)  ,
     model             VARCHAR2(128) ,
     imei              VARCHAR2(16)  ,
     sdkversion        Number(3),
     sdkrelease        VARCHAR2(32),
     codename          VARCHAR2(16) ,
     brand             VARCHAR2(16),
     cpuabi            VARCHAR2(16),
     device            VARCHAR2(128),
     display           VARCHAR2(256) ,
     host              VARCHAR2(128) ,
     manufacturer      VARCHAR2(64) ,
     product           VARCHAR2(32) ,
     factorytime       VARCHAR2(16) ,
     ssmnuser          VARCHAR2(16),
     callstate         Number(1) ,
     celllocation      VARCHAR2(32) ,
     softwareversion   VARCHAR2(32) ,
     countryiso        VARCHAR2(16) ,
     networkoperator   VARCHAR2(32) ,
     operatorname      VARCHAR2(64) ,
     networktype       Number(2),
     phonetype         Number(1) ,
     simcountryiso     VARCHAR2(16) ,
     simserianumber    VARCHAR2(24) ,
     simstate          Number(1) ,
     subscriberid      VARCHAR2(16) ,
     hasicccard        Number(1) ,
     isnetworkroaming  Number(1) 
)tablespace SSMN_DATA;
alter table SSMN_deviceInfo add constraint PK_SSMN_deviceInfo primary key(ID)
using index tablespace SSMN_IDX;
--  异常登记表
create table  SSMN_ExceptionRecord
(
       id              Number(10) not null,
       servicekey      Number(4) not null,
       occurtime       DATE not null,
       sendtime        DATE not null,
       errordetail     VARCHAR2(1024),
       appversion      VARCHAR2(16),
       deciveinfoid    Number(10),
       errorlevel      Number(3)
)tablespace SSMN_DATA;
alter table SSMN_ExceptionRecord add constraint PK_SSMN_ExceptionRecord primary key(ID)
using index tablespace SSMN_IDX;

--序列
create sequence SEQ_SSMN_UgdInfo minvalue 1
maxvalue 9999999999
start with 1000000001
increment by 2
cache 10
cycle;
create sequence SEQ_SSMN_DeviceInfo
minvalue 1
maxvalue 9999999999
start with 1000000001
increment by 2
cache 10
cycle;
create sequence SEQ_SSMN_EXCEPTION
minvalue 1
maxvalue 9999999999
start with 1000000001
increment by 2
cache 10
cycle;
commit;

--江西电信临时存放体验用户表（其他点可以不建）
-- drop table SSMN_FreetryUser;
create table  SSMN_FreetryUser
(   
  MSISDN     Varchar2(20)     NOT NULL
)
tablespace SSMN_DATA;
alter table SSMN_FreetryUser add constraint PK_SSMN_FreetryUser primary key(MSISDN)
using index tablespace SSMN_IDX;
commit;
 
手工插入体验用户号码
insert into SSMN_FreetryUser(MSISDN) Values('用户号码');
commit;

insert into system_cfg values ('TYKJFlag',1,'0');
commit;


------实号码客户端用表
-- 系统参数表
-- drop table client_CFG;
create table client_CFG
(
  NAME        VARCHAR2(30) not null,
  SERVICETYPE NUMBER(2) not null,
  VALUE       VARCHAR2(256)
)
tablespace SSMN_DATA;
alter table client_CFG add constraint PK_client_CFG primary key (NAME,SERVICETYPE)
using index tablespace SSMN_IDX;
commit;

--客户端问题答案表
-- drop table SSMN_clientqa ;
create table SSMN_clientqa
(
Id	Number(10)	not null,
Question	Varchar2(32)	not null,
answer	Varchar2(500)	not null
)
tablespace SSMN_DATA;
alter table SSMN_clientqa add constraint PK_SSMN_clientqa  primary key (id)
using index tablespace SSMN_IDX;
commit;

-- 归属地号段表                                                                                      
-- drop table SSMN_HL_Prefix;                                                                             
create table SSMN_HL_Prefix                                                                               
(                                                                                                          
	Prefix			Varchar2(10)		        NOT NULL,                  
	Area_Id	                Varchar2(10)			NOT NULL,  
	Area_Name 	        Varchar2(26)                    NOT NULL,      
	Description	Varchar2(64)	NOT NULL,
	Operator		Varchar2(2)	NOT NULL
) tablespace SSMN_DATA;                                                                                     
alter table SSMN_HL_Prefix add constraint PK_SSMN_HL_Prefix primary key(Prefix)                          
using index tablespace SSMN_IDX;   
Create Index IDX1_SSMN_HL_Prefix ON SSMN_HL_Prefix (Area_Id) tablespace SSMN_IDX;                                                                        
commit; 


/************************************************

20121123客户端三期开发新增表和序列以及初始化语句	

************************************************/
--创建ssmn_infocenter表
-- drop table SSMN_InfoCenter ;
create table SSMN_InfoCenter
(
Id	Number(10)	not null,
Title	Varchar2(32)	not null,
Content	Varchar2(500)	not null,
intime	Date	not null
)
tablespace SSMN_DATA;
alter table SSMN_InfoCenter add constraint PK_SSMN_InfoCenter  primary key (id)
using index tablespace SSMN_IDX;
commit;
CREATE SEQUENCE SEQ_InfoCenter_ID  INCREMENT BY 2  START WITH 1000000000  MAXVALUE 9999999998  CYCLE CACHE 10; 
commit;

--创建ssmn_feedback表
-- drop table SSMN_feedback;
create table SSMN_feedback
(
Id	Number(10)	not null,
Msisdn	Varchar2(11)	not null,
intime	Date	not null,
Content	Varchar2(500)	not null,
Type	Number(1)	not null,
Status	Number(1)	Default 0,
description	Varchar2(500)
)
tablespace SSMN_DATA;
alter table SSMN_feedback add constraint PK_SSMN_feedback  primary key (id)
using index tablespace SSMN_IDX;
commit;
CREATE SEQUENCE SEQ_feedback_ID  INCREMENT BY 2  START WITH 1000000000  MAXVALUE 9999999998  CYCLE CACHE 10; 
commit;

--创建ssmn_payment表
-- drop table SSMN_Payment;
create table SSMN_Payment
(
Id	Number(10)	not null,
Type	Number(1)	not null,
Content	Varchar2(500)	not null
)
tablespace SSMN_DATA;
alter table SSMN_Payment add constraint PK_SSMN_Payment  primary key (id)
using index tablespace SSMN_IDX;
commit;
CREATE SEQUENCE SEQ_Payment_ID  INCREMENT BY 2  START WITH 1000000000  MAXVALUE 9999999998  CYCLE CACHE 10; 
commit;

--创建ssmn_pushtask表
-- drop table SSMN_pushtask;
create table SSMN_pushtask
(
  ID           NUMBER(10) not null,
  CREATEDATE   DATE not null,
  SCHEDULEDATE DATE not null,
  CONTENT      VARCHAR2(300) not null,
  PUSHSTATUS   NUMBER(1) default 0,
  NAME         VARCHAR2(20)	not null
)
tablespace SSMN_DATA;
alter table SSMN_pushtask add constraint PK_SSMN_pushtask  primary key (ID)
using index tablespace SSMN_IDX;
commit;

--创建ssmn_pushlog表
-- drop table SSMN_pushlog;
create table SSMN_pushlog
(
  ID         NUMBER(10) not null,
  MSISDN     VARCHAR2(20) not null,
  TASKID     NUMBER(10) not null,
  PUSHDATE   DATE not null,
  PUSHSTATUS NUMBER(1) default 0
)
tablespace SSMN_DATA;
alter table SSMN_pushlog add constraint PK_SSMN_pushlog primary key (id)
using index tablespace SSMN_IDX;
commit;
CREATE SEQUENCE SEQ_pushlog_ID  INCREMENT BY 2  START WITH 1000000000  MAXVALUE 9999999998  CYCLE CACHE 10; 
commit;

--创建ssmn_DownLoad_DAYSTAT表
-- drop table SSMN_DownLoad_DAYSTAT;
create table SSMN_DownLoad_DAYSTAT
(
  ID       NUMBER(10) not null,
  STATDATE DATE not null,
  TOTALNUM NUMBER(10) default 0,
  NEWNUM   NUMBER(10) default 0,
  INTIME   DATE not null
)
tablespace SSMN_DATA;
alter table SSMN_DownLoad_DAYSTAT add constraint PK_SSMN_DownLoad_DAYSTAT primary key (id,statdate)
using index tablespace SSMN_IDX;
commit;
CREATE SEQUENCE SEQ_download_ID  INCREMENT BY 2  START WITH 2  MAXVALUE 9999999998  CYCLE CACHE 10; 
commit;

-- 用户信息记录表
-- drop table SSMN_USER_push;
create table SSMN_USER_push
(
  MSISDN        VARCHAR2(20) not null,
  PIN           VARCHAR2(6),
  SUB_MANNER    NUMBER(1) not null,
  SERVICESTATUS CHAR(1) not null,
  TEMP_PIN      CHAR(6),
  CRBT_FLAG     CHAR(1),
  USERNAME      VARCHAR2(20),
  USERID        VARCHAR2(20),
  CALLINGTYPE   NUMBER(1) not null,
  CALLINGNUMBER VARCHAR2(18),
  SUB_DATE      DATE not null,
  QUESTION      VARCHAR2(30),
  ANSWER        VARCHAR2(30),
  AGENT_ID      VARCHAR2(16),
  CANCEL_MANNER NUMBER(1),
  USERTYPE      VARCHAR2(20),
  DEVICETOKEN   VARCHAR2(64),
  LOGINTIME     DATE,
  PHONE_SYSTEM  VARCHAR2(20),
  VALIDITY      DATE,
  PAYMENT       NUMBER(1) default 0,
  PUSHSTATUS    NUMBER(1) default 0
) tablespace SSMN_DATA;
alter table SSMN_USER_push add constraint PK_SSMN_USER_push primary key(MSISDN)
using index tablespace SSMN_IDX;
Create index IDX1_SSMN_USER_Push on SSMN_USER_Push(MSISDN, ServiceStatus) tablespace SSMN_IDX;
Create index IDX2_SSMN_USER_Push on SSMN_USER_Push(UserName) tablespace SSMN_IDX;
Create index IDX3_SSMN_USER_Push on SSMN_USER_Push(ServiceStatus,Sub_Date) tablespace SSMN_IDX;
create index IDX4_SSMN_USER_Push on SSMN_USER_Push(MSISDN,SUB_DATE) tablespace SSMN_IDX;
create index IDX5_SSMN_USER_Push on SSMN_USER_Push(pushstatus) tablespace SSMN_IDX;
commit;

-- 创建特殊用户
-- drop table SSMN_SpecialUser;
create table SSMN_SpecialUser
(
  MSISDN VARCHAR2(20) not null,
  PIN    VARCHAR2(6) default 888888,
  NAME   VARCHAR2(20)
 ) tablespace SSMN_DATA;
alter table SSMN_SpecialUser add constraint PK_SSMN_SpecialUser primary key(MSISDN)
using index tablespace SSMN_IDX;
commit;

--初始一条数据
insert into ssmn_download_daystat (id,statdate,totalnum,newnum,intime) values('0',sysdate,0,0,sysdate);
commit;
--初始ssmn_InfoCenter
insert into ssmn_InfoCenter (Id,Title,Content,intime) values(SEQ_InfoCenter_ID.nextval,'网站上线','一卡多号网站已经顺利上线啦，欢迎来网站体验，点击进入',to_date('2012-12-1','YYYY-MM-DD'));
commit;
---msglist短信模板表中增加用于客户端无归属地非业务用户注册成功短信通知
insert into MsgList (MsgID, MsgContent, Description, Service, MsgLevel, Status) values ('AP001','尊敬的用户您好，恭喜您的号码卡已经成功激活，您的副号码是<v01>，如有疑问详询4006517517。','app—客户端非归属地用户注册成功短信通知',1,9,1);
commit;
--客户端三期临时插入ssmn_pushtask表类型
insert into ssmn_pushtask(ID,CreateDate,ScheduleDate,name,Content,PushStatus) values (1,sysdate,sysdate,'号码卡到期通知','尊敬的用户您好：您的业务使用期限已到，如果继续使用请续订该业务。每月收取固定业务功能费，通信费按现行资费从主号码收取，续订方式详询4006517517。',0);
commit;


-- 初始化Client_cfg数据
insert into Client_cfg (name,servicetype,value)values ('ServiceName',1,'一卡多号');
insert into Client_cfg (name,servicetype,value)values ('Access_code',1,'当地的业务接入码'); 
insert into Client_cfg (name,servicetype,value)values ('Sms_access_code',1,'当地的短信接入码');
insert into Client_cfg (name,servicetype,value)values ('turnoff_call',1,'1');
insert into Client_cfg (name,servicetype,value)values ('turnoff_sms',1,'1');
insert into Client_cfg (name,servicetype,value)values ('sendsms',1,'1');
insert into Client_cfg (name,servicetype,value)values ('Autosecondcall',1,'1');
insert into Client_cfg (name,servicetype,value)values ('second_Access_code',1,'部署点业务接入码扩展码');
insert into Client_cfg (name,servicetype,value)values ('reg_msgid',1,'业务注册时的短信指令');
---三期客户端配置表增加配置项，部分value值各地自定义修改
insert into Client_cfg (name,servicetype,value)values ('Autosecondcall',1,'1');
------自动二次呼叫接入码=部署点业务接入码+扩展码
insert into Client_cfg (name,servicetype,value)values ('second_Access_code',1,'自动二次呼叫接入码');
insert into Client_cfg (name,servicetype,value)values ('reg_msgid',1,'业务注册时的短信指令');
insert into Client_cfg (name,servicetype,value)values ('RegSucMsg',1,'注册成功后，您可以使用副号码打电话、发短信哦！每月收取10元号码使用费，通信费按现行资费收取！');
insert into Client_cfg (name,servicetype,value)values ('FreeUseMsg',1,'恭喜您选号成功，三个月内免交号码使用费哦！通话费将从您的主号码中扣除。');
insert into Client_cfg (name,servicetype,value)values ('hotline',1,'4006517517');
---client_cfg表增加业务部署地区号配置项，天津移动配置022，云南联通配置0871
insert into Client_cfg (name,servicetype,value)values ('Local_areacode',1,'022');
commit;

---初始化问题答案表
CREATE SEQUENCE SEQ_client_QA_ID  INCREMENT BY 2  START WITH 1000000000  MAXVALUE 9999999998  CYCLE CACHE 10; 
commit;
insert into ssmn_clientqa (Id,Question,answer) values (SEQ_client_QA_ID.nextval,'一卡多号是什么？',' 一卡多号是为您提供一个真实的手机号码，可以打电话、发短信、开/关机、设置呼叫限制等功能。您不需要更换SIM卡就可以拥有两个手机号码。');
insert into ssmn_clientqa (Id,Question,answer) values (SEQ_client_QA_ID.nextval,'使用一卡多号收费吗？','  使用一卡多号时，打电话和发短信的费用全部计在您手机SIM卡的号码中，资费与您号码归属地一致。');
insert into ssmn_clientqa (Id,Question,answer) values (SEQ_client_QA_ID.nextval,'为什么要绑定手机号并输验证码？','  输入验证码绑定手机号码是为了在您开/关机时，系统可以根据手机号码关联开/关机状态，并提供关机提示音的播放。');
insert into ssmn_clientqa (Id,Question,answer) values (SEQ_client_QA_ID.nextval,'为什么不能使用短信和开/关机功能','  不能使用短信和开/关机功能是因为您的SIM卡号码归属地暂时无法提供一卡多号所有服务，因此只能拨打电话');
commit;

-- 创建iPhone用区域临界点表
-- drop table SSMN_regionalpoint;
create table SSMN_regionalpoint
(
ID	Number(10)	NOT NULL,
Region_ID	Number(10)	NOT NULL,
Area_Name	Varchar2(26)	NOT NULL,
Logitude	Number(10,6)	NOT NULL,
Latitude	Number(10,6)	NOT NULL,
Region_Name	Varchar2(26)
 ) tablespace SSMN_DATA;
alter table SSMN_regionalpoint add constraint PK_SSMN_regionalpoint primary key(ID,Region_ID)
using index tablespace SSMN_IDX;
commit;

-- 创建android用位置区域号码表
-- drop table SSMN_LAC;
create table SSMN_LAC
(
ID	Number(10)	NOT NULL,
Area_Name	Varchar2(26)	NOT NULL,
AreaCode		Number(10)	NOT NULL,
Operator	Varchar2(2),
G_Type	Varchar2(6),
  LAC       VARCHAR2(32) default -1 not null,
  SID       NUMBER(20,6) default -1 not null
 ) tablespace SSMN_DATA;
alter table SSMN_LAC add constraint PK_SSMN_LAC primary key(ID)
using index tablespace SSMN_IDX;
commit;

--创建ssmn_regionalpoint和ssmn_LAC表的序列
CREATE SEQUENCE SEQ_Lac_ID INCREMENT BY 1  START WITH 1  MAXVALUE 9999999999  CYCLE CACHE 10; 
CREATE SEQUENCE SEQ_RegionalPoint_ID INCREMENT BY 1  START WITH 1  MAXVALUE 9999999999 CYCLE CACHE 10;
commit; 

-- 创建ssmn_local_package表
-- drop table ssmn_local_package;
create table ssmn_local_package
(
ID	Number(5)	NOT NULL,
service	Number(2)	NOT NULL,
name	VARCHAR2(100)	NOT NULL,
type	Number(2)	NOT NULL,
price	VARCHAR2(16)	NOT NULL,
discount	NUMBER(3),	
note	VARCHAR2(1000),
userType	Number(1)	NOT NULL,
 ) tablespace SSMN_DATA;
alter table SSMN_local_package add constraint PK_ssmn_local_package primary key(ID)
using index tablespace SSMN_IDX;
commit;
CREATE SEQUENCE SEQ_SSMN_Local_PACKAGE INCREMENT BY 2  START WITH 1000000001  MAXVALUE 9999999999 CYCLE CACHE 10;
commit;

--创建SSMN_UMProxy用到的向中央系统重发消息的数据库表
-- drop table SSMN_CMSSYN_RESEND;
create table SSMN_CMSSYN_RESEND
(
  MSISDN      VARCHAR2(20),
  SSMNNUMBER  VARCHAR2(20),
  TRANSCODE   VARCHAR2(4),
  RESENDTIMES NUMBER(2),
  INTIME      DATE default sysdate,
  UPDATETIME  DATE
)
tablespace SSMN_DATA;
commit;

---client_cfg新增配置项
insert into Client_cfg (name,servicetype,value)values ('Monchangetime',1,'1');
---如果业务部署地号码资源归运营商管理且不支持短信注册，支持营业厅办理的配置下面信息；不支持短信和营业厅注册方式的则配置“注册有点难，请联系客服4001117571
insert into Client_cfg (name,servicetype,value)values ('UnReg_Msg',1,'您到附近营业厅办理一卡多号业务');
insert into Client_cfg (name,servicetype,value)values ('Reg_NoEnableNum',1,'尊敬的用户你好，目前号码库中暂无号码可选，请您24小时后再试。如需联系客服解决，请拨打4001117517。感谢您对我们的支持！');
insert into Client_cfg (name,servicetype,value)values ('Reg_Area',1,'目前只支持天津移动、云南联通、江西电信用户办理使用。其他地区将陆续开通，敬请期待！');
---部署点提供短信注册方式则配置为1，否则配置为0 
insert into Client_cfg (name,servicetype,value)values ('RegService',1,'1');
---部署地注销指令配置项，各点根据实际注销指令配置
insert into Client_cfg (name,servicetype,value)values ('Cancel_MsgID',1,'ZX');
---部署地订购或退订时用的接入码，与点对点短信接入码区分开，有的地区可能与点对点短信接入码一致，请根据实际各地情况进行配置
insert into Client_cfg (name,servicetype,value)values ('regsms_access_code',1,'注册业务短信接入码');
insert into Client_cfg (name,servicetype,value)values ('cxlsms_access_code',1,'注销业务短信接入码');
---客户端使用关机提示音功能开关，0：关闭；1：打开
insert into Client_cfg (name,servicetype,value)values ('prompt_switch',1,'0');


-----2.0版本地方接口新增字段 sedconfirmmsg,payTime,Cyclic_Time,error_report_switch sms2Switch
---用户发送完一次短信后客户端引导发送二次确认短信的提示，根据各地不同的情况进行配置
insert into Client_cfg (name,servicetype,value)values ('sedconfirmmsg',1,'引导发送二次确认短信的提示信息');
---用户使用客户端注册时，选择完副号码和套餐后，在支付前给用户的提示，例如：请在N小时内完成注册，否则副号码将被收回
insert into Client_cfg (name,servicetype,value)values ('payTime',1,'等待付费的有效时长');
---Android客户端轮询查询的时间，单位是秒。
insert into Client_cfg (name,servicetype,value)values ('Cyclic_Time',1,'Android客户端轮询查询的间隔时间，单位是秒。');
---Android客户端异常信息上报开关，如无配置则认为是关闭的，0亦为关闭。
insert into Client_cfg (name,servicetype,value)values ('error_report_switch',1,'Android异常信息上报开关（不配置默认是关闭的）：1：打开 0：关闭');
---客户端依据此项判断是否发送引导短信，如果开启，则显示引导发送二次确认短信内容，如关闭，则不显示。
insert into Client_cfg (name,servicetype,value)values ('sms2Switch',1,'回复二次确认短信开关 0：关闭，不回复 1：打开，回复（默认情况）');
commit;

----根据实号码客户端需求增加套餐类型
insert into ssmn_local_package (ID,service,name,type,price,discount,note,userType) values (SEQ_SSMN_Local_PACKAGE.nextval,1,'月套餐',1,'8月/月',Null,Null,1);
insert into ssmn_local_package (ID,service,name,type,price,discount,note,userType) values (SEQ_SSMN_Local_PACKAGE.nextval,1,'体验套餐',2,'0元',Null,'恭喜您，可以免费使用1个月',0);
commit;

-----原ssmn_number表中在用的用户套餐ID为套餐表中月套餐类型的套餐ID
update ssmn_number set packageid  =(select id from ssmn_local_package where type=1) where number_type <=1;
commit;

----将ssmn_number表中packageid为默认null的用户，更改为默认的套餐值。
update ssmn_number set packageid=(select id from ssmn_local_package where type=1) where packageid is null;
---选出ssmn_local_package表中type=1的套餐ID值
select id from ssmn_local_package where type=1;
----特别注意！！将ssmn_number表的packageid默认值修改为上面的id值,例如id为1000000003！！
alter table ssmn_number modify packageid default 1000000003 not null;
commit;

/************************************************
本次修改，主要配合实号码客户端市场四期添加统计表、
广东移动CDR表增加一个客服休息时间的字段。
对应数据库设计PC18
1)	新增SSMN_LOGIN_STAT表。
2)	ssmn_cdr表增加resttime字段。
************************************************/
---ssmn_cdr表增加resttime字段
alter table SSMN_CDR add resttime VARCHAR2(10) default NULL;
commit;

-- 创建SSMN_LOGIN_STAT表
-- drop table SSMN_LOGIN_STAT;
create table SSMN_LOGIN_STAT
(
  ID	 Number(10) 	NOT NULL,
  MSISDN     VARCHAR2(18) NOT NULL,
  SSMNNUMBER VARCHAR2(18) NOT NULL,
  LOGINTIME  DATE NOT NULL,
  PLATFORM NUMBER(1)
) tablespace SSMN_DATA;
alter table SSMN_LOGIN_STAT add constraint PK_SSMN_LOGIN_STAT primary key(ID)
using index tablespace SSMN_IDX;
commit;

CREATE SEQUENCE SEQ_LOGIN_STAT INCREMENT BY 2  START WITH 1000000001  MAXVALUE 9999999999 CYCLE CACHE 10;
commit;

-------20130904DB新增表项
--未读接收短信表
-- drop table MsgTerminated_UNRead;
create MsgTerminated_UNRead
(
	ID		Number(10)			NOT NULL,
	srcNo		Varchar2(30)			NOT NULL,
  destNo		Varchar2(30)			NOT NULL,
	inTime		Date			        NOT NULL 
) tablespace SSMN_DATA;
alter table MsgTerminated_UNRead add constraint PK_MsgTerminated_UNRead primary key(ID)
using index tablespace SSMN_IDX;
commit;


--未读短信trigger
create or replace TRIGGER TRG_INSERT_MSGTERMINATED
after insert on MSGTERMINATED
for each row
begin
	if :new.state=-2 AND (:new.service=0 OR :new.service=1) then
		insert into MSGTERMINATED_UNREAD
		(ID,SRCNO,DESTNO,INTIME)
		values
		(:new.ID,trim(:new.SRCNO),trim(:new.DESTNO),:new.INTIME);
	end if;
end;

-- 送号记录表
-- drop table SSMN_TransferNum;
create table SSMN_TransferNum
(
	msisdn		      Varchar2(25)			NOT NULL,
	calledNumber		Varchar2(25)			NOT NULL,
	inTime		      Date			        NOT NULL
) tablespace SSMN_DATA;
alter table SSMN_TransferNum add constraint PK_SSMN_TransferNum primary key(msisdn)
using index tablespace SSMN_IDX;
commit;


-- 客户端黑名单表
-- drop table SSMN_ClientBlack;
create table SSMN_ClientBlack
(
	msisdn		      Varchar2(25)			NOT NULL,
	status		      Number(2)			    default 1,
	inTime		      Date			        NOT NULL
) tablespace SSMN_DATA;
alter table SSMN_ClientBlack add constraint PK_SSMN_ClientBlack primary key(msisdn)
using index tablespace SSMN_IDX;
commit;


-- 漏话提醒表
-- drop table SSMN_Remind;
create table SSMN_Remind
(
  id              Number(10)        NOT NULL,
	msisdn		      Varchar2(20)			NOT NULL,
	content		      Varchar2(200)		  NOT NULL,
	inTime		      Date			        NOT NULL 
) tablespace SSMN_DATA;
alter table SSMN_Remind add constraint PK_SSMN_Remind primary key(ID)
using index tablespace SSMN_IDX;
commit; 
 
--漏话提醒消息序列
create sequence SEQ_SSMN_Remind minvalue 1
maxvalue 9999999999
start with 1000000001
increment by 2
cache 10
cycle;


-- 抽奖码临时记录表
-- drop table SSMN_Lottery_Temp;
create table SSMN_Lottery_Temp
(
  id              number(10)        NOT NULL,
	msisdn		      Varchar2(18)			NOT NULL,
	inTime		      Date			        NOT NULL
) tablespace SSMN_DATA;
alter table SSMN_Lottery_Temp add constraint PK_SSMN_Lottery_Temp primary key(id)
using index tablespace SSMN_IDX;
commit;

--抽奖序列
CREATE SEQUENCE SEQ_SSMN_LOTTERY  INCREMENT BY 1  START WITH 1000000000  MAXVALUE 9999999999  CYCLE CACHE 10;


-- 跨省转移短信记录表
-- drop table SSMN_SMSAGENT;
create table SSMN_SMSAGENT
(
	id		          Number(10)			NOT NULL,
	srcNo		        Varchar2(25)		NOT NULL,
	DestNo          Varchar2(25)		NOT NULL,
	MsgContent      Varchar2(200)		NOT NULL,
	inTime		      Date			      NOT NULL,
	state           Number(10)			default 0
) tablespace SSMN_DATA;
alter table SSMN_SMSAGENT add constraint PK_SSMN_SMSAGENT primary key(id)
using index tablespace SSMN_IDX;
commit;

转移短信序列：
CREATE SEQUENCE SEQ_SSMN_SMSAGENT  INCREMENT BY 2  START WITH 1000000000  MAXVALUE 9999999998  CYCLE CACHE 10;

-- 通话录音记录表
-- drop table SSMN_Record;
create table SSMN_RECORD
(
  STREAMNUMBER   NUMBER not null,
  CALLINGMSGHIGH NUMBER not null,
  CALLINGMSGLOW  NUMBER not null,
  MSGLEN         NUMBER not null,
  CALLEDMSGHIGH  NUMBER not null,
  CALLEDMSGLOW   NUMBER not null,
  RECORD_TIME    DATE 	not null,
  HANDLE_FLAG    NUMBER not null,
  HANDLE_TIME    DATE,
  FILEPATH       VARCHAR2(512)
) tablespace SSMN_DATA;
alter table SSMN_RECORD  add constraint PK_SSMN_RECORD primary key (STREAMNUMBER)
using index tablespace SSMN_IDX;

-- 转移中央发送短信记录表
-- drop table SSMN_SMS_toMI;
create table SSMN_SMS_toMI
( id		          Number(10)			NOT NULL,
	srcNo		        Varchar2(25)		NOT NULL,
	DestNo          Varchar2(25)		NOT NULL,
	Ssmnnumber      Varchar2(25)		NOT NULL,
	MsgContent      Varchar2(200)		NOT NULL,
	inTime		      Date			      NOT NULL,
	state           Number(10)			default 0,
	Transfer_Flag		Char(2)					NOT NULL,
	Handle_Time			Date
 ) tablespace SSMN_DATA;
 
alter table SSMN_SMS_toMI add constraint PK_SSMN_SMS_toMI primary key(id)
using index tablespace SSMN_IDX;

create index idx_SMStoMI_idx1 on SSMN_SMS_toMI(Ssmnnumber);
create index idx_SMStoMI_idx2 on SSMN_SMS_toMI(DestNo);

-- 短信过滤条件表
-- drop table SSMN_SMS_FILTER;
create table SSMN_SMS_FILTER
(
  id         number(10) not null,
  filtertype number(1) not null,
  keywords   varchar2(32) not null,
  intime     date default sysdate not null
)
;
-- Create/Recreate primary, unique and foreign key constraints 
alter table SSMN_SMS_FILTER add constraint PK_SSMN_SMSFILTER primary key (ID);


CREATE SEQUENCE SEQ_SMS_FILTER  INCREMENT BY 2  START WITH 1000000000  MAXVALUE 9999999998  CYCLE CACHE 10; 


-- 虚拟业务话单表
-- drop table SSMN_VCDR;
create table SSMN_VCDR
(
  Streamnumber		  Number(10)		NOT NULL,
	CallingAddress		Varchar2(18),
	CalledAddress		  Varchar2(32),
	OriginalCalled		Varchar2(18),
	RedirectPartyID		Varchar2(18),
	MSISDN			      Varchar2(20),
	SSMNNumber		    Varchar2(18),
	CallType		      Number(1)			NOT NULL,
	CallStartTime		  Date,
	CallStopTime		  Date,
	CallDuration	    Number(6)	default NULL,
	Readstatus	      Number(1)	default 0,
	EndReason	        Number(2)	default NULL,
	Incoming_CIN	    Varchar2(32)	default NULL,
	Outgoing_CPN      Varchar2(32) default NULL
) tablespace SSMN_DATA;
alter table SSMN_VCDR add constraint PK_SSMN_VCDR primary key(Streamnumber) using index tablespace SSMN_IDX;

--虚拟话单序列
CREATE SEQUENCE SEQ_SSMN_VCDR  INCREMENT BY 2  START WITH 1000000000  MAXVALUE 9999999998  CYCLE CACHE 10; 

--虚拟话单索引
Create index IDX1_SSMN_VCDR on SSMN_VCDR (MSISDN, SSMNNumber, ReadStatus) tablespace SSMN_IDX;




-- 接入码与业务对应表
-- drop table SSMN_ACODE_Service;
create table SSMN_ACODE_Service
(
  ID		          Number(10)		  NOT NULL,	
	AccessCode			Varchar2(20)    NOT NULL,
	Service        	Number(2)	      NOT NULL
) tablespace SSMN_DATA;
alter table SSMN_ACODE_Service add constraint PK_SSMN_ACODE_Service primary key(ID) using index tablespace SSMN_IDX;

--接入码与业务对应表序列
CREATE SEQUENCE SEQ_SSMN_ACODE_SERVICE  INCREMENT BY 2  START WITH 1000000000  MAXVALUE 9999999998  CYCLE CACHE 10; 




--虚拟运营业务短信发送表
-- drop table MsgTerminated_Virtual ;
create table MsgTerminated_Virtual
(
	ID				    Number(10)		NOT NULL,
	Service	      Number(2)     NOT NULL,
	ssmnnumber		Varchar2(20)  NOT NULL,
	SrcNo			    Varchar2(20),
	ChargeNo			Varchar2(20),
	DestNo			  Varchar2(20),
	MsgContent		Varchar(200),
	InTime			  Date,
	Readstatus		Number(1) 	default 0
) tablespace SSMN_DATA;
alter table MsgTerminated_Virtual add constraint PK_MsgTerminated_Virtual primary key(ID) using index tablespace SSMN_IDX;

--虚拟业务短信序列
CREATE SEQUENCE SEQ_VMT   INCREMENT BY 2  START WITH 100000000  MAXVALUE 999999998  CYCLE CACHE 10; 

--虚拟业务短信索引
Create index IDX1_MsgTerminated_Virtual on MsgTerminated_Virtual (service,readstatus,ssmnnumber) tablespace SSMN_IDX;



-- 虚拟运营用户信息记录表
-- drop table SSMN_VUSER;
create table SSMN_VUSER
(
	MSISDN			Varchar2(20)		NOT NULL,
	PIN			Varchar2(6)		NOT NULL,
	Sub_Manner		Number(2)		NOT NULL,
	ServiceStatus		Char(1)			NOT NULL,
	Temp_Pin		Char(6),
	CRBT_Flag		Char(1),
	UserName		Varchar2(20),
	UserID			Varchar2(20),
	CallingType		Number(1)			NOT NULL,
	CallingNumber		Varchar2(18),
	Sub_Date		Date				NOT NULL,
	Question		Varchar2(30),
	Answer			Varchar2(30),
	Agent_ID		Varchar2(16),
	Cancel_Manner		Number(2),
	usertype		Varchar2(10)	default NULL,
	DeviceToken	Varchar2(64)	default NULL,
	Logintime		Date	default NULL,
	PHONE_SYSTEM  VARCHAR2(20),
	VALIDITY      DATE,
	PAYMENT       NUMBER(1)  ,
	TYPE          NUMBER(1) default 0 not null ,
	UserUSSDFlag	 Number(1) default 1 not null ,
	Remind_Flag	 Number(1) default 1 not null 
) tablespace SSMN_DATA;
alter table SSMN_VUSER add constraint PK_SSMN_VUSER primary key(MSISDN)
using index tablespace SSMN_IDX;


-- 虚拟运营副号码记录表
-- drop table SSMN_VNUMBER;
create table SSMN_VNUMBER
(
	SSMNNumber		Varchar2(18)		NOT NULL,
	Type				Number(1)	 default 1	NOT NULL,
	Number_Type		Number(1)		NOT NULL,
	MSISDN			Varchar2(20),      
	Called_Type		Number(1)		NOT NULL,       
	Restriction_Type	Char(1),
	Status			Number(1),
	Prompt_Type		Number(1),
	Initiate_Date		Date,
	Cancel_Date		Date,
	Sub_Manner		Number(2),
	Cancel_Manner		Number(2),
	ServiceStatus		Char(1)	default 'N'		NOT NULL,
	OrderID		Number(2)	 default 0		NOT NULL,
  CFStatus	Number(2)	 default 0		NOT NULL,
  SFStatus	Number(2)	 default 0		NOT NULL,
  FTStatus	Number(2)	 default 1		NOT NULL,
	PACKAGEID      NUMBER(10)
) tablespace SSMN_DATA;
alter table SSMN_VNUMBER add constraint PK_SSMN_VNUMBER primary key(SSMNNumber)
using index tablespace SSMN_IDX;

/**--------地产版管理系统 --20140903对应DB PC32------**/

-- 地产用户副号码信息表
-- drop table SSMN_ZY_USER;
-- Create table
create table SSMN_ZY_USER
(
  ID                 NUMBER(10) not null,
  NAME               VARCHAR2(20) not null,
  MSISDN             VARCHAR2(20) not null,
  SSMNNUMBER         VARCHAR2(20) not null,
  GROUPNAME          VARCHAR2(50) not null,
  CHANNELID          NUMBER(10) not null,
  INTIME             DATE not null,
  PROVINCECITY       VARCHAR2(32) default '省市(区分操作员职位的级别)',
  COMPANY            VARCHAR2(32) default '公司(区分操作员职位的级别)',
  BUSINESSDEPARTMENT VARCHAR2(32) default '事业部(区分操作员职位的级别)',
  WARZONE            VARCHAR2(32) default '战区(区分操作员职位的级别)',
  AREA               VARCHAR2(32) default '片区(区分操作员职位的级别)',
  BRANCHACTIONGROUP  VARCHAR2(32) default '分行行动组(区分操作员职位的级别)',
  LEVELID            NUMBER(10) not null,
  EMPNO              VARCHAR2(32),
  MANNER             NUMBER(1) default 0
)
tablespace TJLTZYDC_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 1M
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table SSMN_ZY_USER
  add constraint PK_SSMN_ZY_USER primary key (ID)
  using index 
  tablespace TJLTZYDC_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Create/Recreate indexes 
create index PK_SSMN_ZY_USER1 on SSMN_ZY_USER (CHANNELID)
  tablespace TJLTZYDC_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
create index PK_SSMN_ZY_USER2 on SSMN_ZY_USER (LEVELID)
  tablespace TJLTZYDC_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
create index PK_SSMN_ZY_USER3 on SSMN_ZY_USER (MSISDN)
  tablespace TJLTZYDC_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
create index PK_SSMN_ZY_USER4 on SSMN_ZY_USER (SSMNNUMBER)
  tablespace TJLTZYDC_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
create index PK_SSMN_ZY_USER5 on SSMN_ZY_USER (NAME)
  tablespace TJLTZYDC_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );

--地产用户副号码信息序列
CREATE SEQUENCE SEQ_SSMN_ZY_USER  INCREMENT BY 2  START WITH 1000000000  MAXVALUE 9999999998  CYCLE CACHE 10; 



-- 地产用户渠道表
-- drop table SSMN_ZY_CHANNEL;
create table SSMN_ZY_CHANNEL
(
  id		        Number(10)		   NOT NULL,
	name		      Varchar2(20)     NOT NULL,
	createtime    Date             NOT NULL
) tablespace SSMN_DATA;
alter table SSMN_ZY_CHANNEL add constraint PK_SSMN_ZY_CHANNEL primary key(id) using index tablespace SSMN_IDX;

-- 地产用户渠道序列
CREATE SEQUENCE SEQ_SSMN_ZY_CHANNEL  INCREMENT BY 2  START WITH 1000000000  MAXVALUE 9999999998  CYCLE CACHE 10; 


-----------------------------------------------------------------------------------------------------------------
--2014-10-22 针对天津地产业务修改


delete from tyadmin_authorities t where t.servicekey='50' ;

delete from tyadmin_groups t where t.servicekey='50'; 

delete from tyadmin_operators t where t.servicekey ='50'; 

delete    from tyadmin_group_auths t where t.group_id  in ( select id   from tyadmin_groups t where t.servicekey='50')  ;




-- 添加组
insert into TYADMIN_GROUPS (ID, GROUP_NAME, DESCRIPTION, SERVICEKEY, CREATE_DATE, OPERATOR_ID, RANK);
values (SEQ_TYAdmin_GROUP.Nextval, '超级管理员', '具有全部系统权限', '50', sysdate,'system', 0);

-- 添加账号
insert into TYADMIN_OPERATORS (OPENO, SERVICEKEY, OPEPWD, GROUP_ID, CREATE_USER, CREATE_TIME, AGENT_INFO, NOTE);
values ('0000000000', '50', 'c11f7a59a670f96579208806555492f3', SEQ_TYAdmin_GROUP.Currval, 'system', sysdate, 'admin','超级管理员');


--  添加用户权限
insert into tyadmin_authorities(id,authority, auth_method, servicekey, rank ) values(SEQ_TYAdmin_AUTH.Nextval,'副号码信息查询' , 'queryssmn', 50, 1);
insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval, SEQ_TYAdmin_GROUP.Currval, SEQ_TYAdmin_AUTH.Currval);

--  添加用户权限
insert into tyadmin_authorities(id,authority, auth_method, servicekey, rank ) values(SEQ_TYAdmin_AUTH.Nextval,'副号码通信信息查询' , 'queryinfo', 50, 1);
insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval, SEQ_TYAdmin_GROUP.Currval, SEQ_TYAdmin_AUTH.Currval);


-----------------------------------------------------------------------------------------------------------------------
--2014-10-22  添加权限

insert into tyadmin_authorities (id,authority, auth_method, servicekey, rank ) values( SEQ_TYADMIN_AUTH.NEXTVAL,'语音下载(统计报表下载)', 'downloadCDR','50',1);
insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval, SEQ_TYAdmin_GROUP.Currval, SEQ_TYAdmin_AUTH.Currval);

insert into tyadmin_authorities (id,authority, auth_method, servicekey, rank ) values(SEQ_TYADMIN_AUTH.NEXTVAL,'短信下载(统计报表下载)', 'downloadSMS','50',1);
insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval, SEQ_TYAdmin_GROUP.Currval, SEQ_TYAdmin_AUTH.Currval);

insert into tyadmin_authorities (id,authority, auth_method, servicekey, rank ) values(SEQ_TYADMIN_AUTH.NEXTVAL,'语音短信查询(副号码通讯信息)', 'queryssmninfo','50',1);
insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval, SEQ_TYAdmin_GROUP.Currval, SEQ_TYAdmin_AUTH.Currval);

insert into tyadmin_authorities (id,authority, auth_method, servicekey, rank ) values(SEQ_TYADMIN_AUTH.NEXTVAL,'下载通信信息', 'downloadinfo','50',1);
insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval, SEQ_TYAdmin_GROUP.Currval, SEQ_TYAdmin_AUTH.Currval);








--3.检查是否这个表，如果没有，创建上

-- Create table
create table SSMN_RECORD
(
  STREAMNUMBER   NUMBER not null,
  CALLINGMSGHIGH NUMBER not null,
  CALLINGMSGLOW  NUMBER not null,
  MSGLEN         NUMBER not null,
  CALLEDMSGHIGH  NUMBER not null,
  CALLEDMSGLOW   NUMBER not null,
  RECORD_TIME    DATE not null,
  HANDLE_FLAG    NUMBER not null,
  HANDLE_TIME    DATE,
  FILEPATH       VARCHAR2(512)
)
tablespace SSMN_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table SSMN_RECORD
  add constraint PK_SSMN_RECORD primary key (STREAMNUMBER)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
  
 -- 添加注销用户表

-- Create table
create table SSMN_ZY_CANCEL_USER
(
  USERID             NUMBER(10) not null,
  NAME               VARCHAR2(20) not null,
  MSISDN             VARCHAR2(20) not null,
  SSMNNUMBER         VARCHAR2(20) not null,
  GROUPNAME          VARCHAR2(50) not null,
  CHANNELID          NUMBER(10) not null,
  SUB_DATE           DATE not null,
  CANCEL_DATE        DATE not null,
  LEVELID            NUMBER(10) not null,
  EMPNO              VARCHAR2(32)
)
tablespace SSMN_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );

-- Create/Recreate primary, unique and foreign key constraints 
alter table SSMN_ZY_CANCEL_USER
  add constraint PK_SSMN_ZY_CANCEL_USER primary key (USERID,CANCEL_DATE)
  using index 
  tablespace SSMN_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
  
  --2014-11-27 添加修改密码权限
insert into tyadmin_authorities (id,authority, auth_method, servicekey, rank ) values( SEQ_TYADMIN_AUTH.NEXTVAL,'我的信息', 'modifyoperatorinfo','50',1);
insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval, SEQ_TYAdmin_GROUP.Currval, SEQ_TYAdmin_AUTH.Currval);

-------------------------------------------
--2014-12-10 添加解绑，换绑，绑定权限
insert into tyadmin_authorities (id,authority, auth_method, servicekey, rank ) values( SEQ_TYADMIN_AUTH.NEXTVAL,'副号码管理', 'managessmnnumbers','50',1);
insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval, SEQ_TYAdmin_GROUP.Currval, SEQ_TYAdmin_AUTH.Currval);

--------------------------------------------------
--2014-12-15 添加来电统计表
insert into tyadmin_authorities (id,authority, auth_method, servicekey, rank ) values( SEQ_TYADMIN_AUTH.NEXTVAL,'来电统计', 'downloadCDRinfo','50',1);
insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval, SEQ_TYAdmin_GROUP.Currval, SEQ_TYAdmin_AUTH.Currval);


 
 
  -----2015-03-25 添加　密码重置　功能　
  insert into tyadmin_authorities (id,authority, auth_method, servicekey, rank ) values(SEQ_TYADMIN_AUTH.NEXTVAL,'密码重置(我的信息)', 'resetpassword','50',1);
insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval, (select t.group_id from tyadmin_operators t where t.servicekey='50' and t.openo='0000000000' ), SEQ_TYAdmin_AUTH.Currval);
 --------------------------------------------------------------------------------------
-- 2015-03-30 第二迭代　 
 
-- 创建副号码池: SSMN_ZY_ENABLENUMBER
-- Create table
create table SSMN_ZY_ENABLENUMBER
(
  ENABLENUMBER VARCHAR2(18) not null,
  STATUS       NUMBER(1) not null,
  AREA_ID      VARCHAR2(10),
  LEVELID      NUMBER(10) not null
)
tablespace SSMN_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table SSMN_ZY_ENABLENUMBER
  add constraint PK_SSMN_ZY_ENABLENUMBER primary key (ENABLENUMBER)
  using index 
  tablespace SSMN_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Create/Recreate indexes 
create index IDX_SSMN_ZY_ENABLENUMBER on SSMN_ZY_ENABLENUMBER (STATUS)
  tablespace SSMN_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


----初始化号码池　SSMN_ZY_ENABLENUMBER　的数据：把现有的副号码放入号码池中
insert into ssmn_zy_enablenumber  (select distinct(ssmnnumber),1,'022', levelid from ssmn_zy_user );

--初始化原号码池的副号码状态（之前是７，修改成１：已分配使用状态）
--  update ssmn_enablenumber t set t.status=1 where   t.enablenumber in ( select distinct(ssmnnumber)  from ssmn_zy_user )


------副号码管理重新添加权限,因为之前权限名字，用在其他功能上了，副号码管理重新定义

insert into tyadmin_authorities (id,authority, auth_method, servicekey, rank ) values( SEQ_TYADMIN_AUTH.NEXTVAL,'副号码管理', 'modifyssmnnumber','50',1);
insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval, (select group_id from tyadmin_operators where  openo  ='0000000000' ), (select id from tyadmin_authorities t where t.auth_method='modifyssmnnumber' ) ) ;


-----2014-04-23---加操作员管理,管理员权限

insert into tyadmin_authorities (id,authority, auth_method, servicekey, rank ) values( SEQ_TYADMIN_AUTH.NEXTVAL,'操作员管理', 'operatorSearch','50',1);
insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval, 
(select group_id from tyadmin_operators where  openo  ='dascom' ), (select id from tyadmin_authorities t where t.auth_method='operatorSearch' ) ) ;


insert into tyadmin_authorities (id,authority, auth_method, servicekey, rank ) values( SEQ_TYADMIN_AUTH.NEXTVAL,'添加操作员(操作员管理)', 'operatorAdd','50',1);
insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval, 
(select group_id from tyadmin_operators where  openo  ='dascom' ), (select id from tyadmin_authorities t where t.auth_method='operatorAdd' ) ) ;

insert into tyadmin_authorities (id,authority, auth_method, servicekey, rank ) values( SEQ_TYADMIN_AUTH.NEXTVAL,'修改操作员(操作员管理)', 'operatorModify','50',1);
insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval, 
(select group_id from tyadmin_operators where  openo  ='dascom' ), (select id from tyadmin_authorities t where t.auth_method='operatorModify' ) ) ;

insert into tyadmin_authorities (id,authority, auth_method, servicekey, rank ) values( SEQ_TYADMIN_AUTH.NEXTVAL,'删除操作员(操作员管理)', 'operatorDelete','50',1);
insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval, 
(select group_id from tyadmin_operators where  openo  ='dascom' ), (select id from tyadmin_authorities t where t.auth_method='operatorDelete' ) ) ;







----tyadmin_groups组，规定死，分为：分行行动组，片区组，　战区组（级别同片区组），事业部组（级别同片区组），　sql如下，id为片区组的groupid

 


insert into TYADMIN_GROUP_AUTHS  (id,GROUP_ID,AUTH_ID)  select SEQ_TYAdmin_GROUP_AUTH.Nextval,10000000042, g.auth_id  from tyadmin_group_auths g where g.group_id='10000000044' 

-------2015-04-23，升级的sql，初始化　组

delete from tyadmin_groups t where t.id=10000000061

update tyadmin_groups  t set t.group_name='分行行动组'  where t.id=10000000041 

update tyadmin_groups  t set t.group_name='片区组'   where t.id=10000000081


insert into tyadmin_groups t (t.id, t.group_name, t.description, t.servicekey, t.create_date,t.operator_id,t.rank,t.provincecity,
t.company,t.businessdepartment,t.warzone,t.area,t.branchactiongroup) values
(SEQ_TYAdmin_GROUP.Nextval,'战区组','战区组','50',sysdate,'system',0,'天津','中原','','','','' );

insert into tyadmin_groups t (t.id, t.group_name, t.description, t.servicekey, t.create_date,t.operator_id,t.rank,t.provincecity,
t.company,t.businessdepartment,t.warzone,t.area,t.branchactiongroup) values
(SEQ_TYAdmin_GROUP.Nextval,'事业部组','事业部组','50',sysdate,'system',0,'天津','中原','','','','' );


insert into TYADMIN_GROUP_AUTHS  (id,GROUP_ID,AUTH_ID)  select SEQ_TYAdmin_GROUP_AUTH.Nextval,(select id from tyadmin_groups t where t.group_name='战区组'), g.auth_id  from tyadmin_group_auths g where g.group_id='10000000081' 

insert into TYADMIN_GROUP_AUTHS  (id,GROUP_ID,AUTH_ID)  select SEQ_TYAdmin_GROUP_AUTH.Nextval,(select id from tyadmin_groups t where t.group_name='事业部组'), g.auth_id  from tyadmin_group_auths g where g.group_id='10000000081' 

-------2015-04-23，升级的sql，初始化　组



----------------------第四个迭带---------------------------------------------------------------------
------1.给ssmn_cdr表加索引
create index  IDX9_SSMN_CDR  on  ssmn_cdr (CALLSTARTTIME);

-----2.给ssmn_zy_user表添加员工编号字段empno
alter table ssmn_zy_user add empno varchar2(32) ;
alter table ssmn_zy_cancel_user add empno varchar2(32) ;



---------添加员发送状态----与中原公司接口中发通话记录消息使用------------------------
alter table ssmn_record add status Number(1);

----------------添加信息反馈表--------------------------------------
-- Create table
create table SSMN_ZY_FEEDBACK
(
  ID              NUMBER(10) not null,
  MSISDN          VARCHAR2(20) not null,
  FEEDBACKDATE    DATE not null,
  FEEDBACKCONTENT VARCHAR2(1024) not null,
  NAME            VARCHAR2(32) not null,
  OPENO           VARCHAR2(32) not null
)
tablespace SSMN_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table SSMN_ZY_FEEDBACK
  add constraint PK_SSMN_ZY_FEEDBACK primary key (ID)
  using index 
  tablespace SSMN_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


-- Create sequence 
create sequence SEQ_SSMN_ZY_FEEDBACK
minvalue 1
maxvalue 9999999998
start with 1
increment by 2
cache 10
cycle;

---------------------添加用户反馈的权限--所有权限都要显示-------
insert into tyadmin_authorities (id,authority, auth_method, servicekey, rank ) values( SEQ_TYADMIN_AUTH.NEXTVAL,'信息反馈', 'searchfeedback','50',1);

insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval,
 (select id from tyadmin_groups where group_name='超级管理员' ), 
(select id from tyadmin_authorities t where t.auth_method='searchfeedback' ) ) ;

insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval,
 (select id from tyadmin_groups where group_name='片区组' ), 
(select id from tyadmin_authorities t where t.auth_method='searchfeedback' ) ) ;

insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval,
 (select id from tyadmin_groups where group_name='战区组' ), 
(select id from tyadmin_authorities t where t.auth_method='searchfeedback' ) ) ;

insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval,
 (select id from tyadmin_groups where group_name='事业部组' ), 
(select id from tyadmin_authorities t where t.auth_method='searchfeedback' ) ) ;

insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval,
 (select id from tyadmin_groups where group_name='分行行动组' ), 
(select id from tyadmin_authorities t where t.auth_method='searchfeedback' ) ) ;


-------------------添加ssmn_zy_level的触发器,保证表中行动组必须唯一-------------------------------------------------
CREATE OR REPLACE TRIGGER "SSMNR".TRG_INSERT_SSMNZYLEVEL BEFORE
 INSERT  ON SSMN_ZY_LEVEL FOR EACH ROW

declare 
v_cnt NUMBER;
v_branchactiongroup char(32);
v_area char(32);
v_warzone char(32);
v_businessdepartment char(32);
v_company char(32);
v_provincecity char(32);

begin

v_branchactiongroup := :NEW.branchactiongroup;
v_area := :NEW.area;
v_warzone := :NEW.warzone;
v_businessdepartment := :NEW.businessdepartment;
v_company := :NEW.company;
v_provincecity := :NEW.provincecity;

--所有的都为空，则不能添加
IF (v_provincecity is null and v_company is null  and v_businessdepartment is null   and v_warzone is null and v_area is null and  v_branchactiongroup is null  )
THEN
   
     RAISE_APPLICATION_ERROR(-20001,'数据值无效,请至少填写省名称!');
   
END IF; 

--行动组是否存在
IF ( v_branchactiongroup is not null )
THEN
    select count(*) into v_cnt from ssmn_zy_level where branchactiongroup =:NEW.branchactiongroup and branchactiongroup is not null;

    IF v_cnt >0 
       THEN RAISE_APPLICATION_ERROR(-20001,'该行动组已经存在!');
    END IF;
END IF;  
--片区是否存在
IF ( v_area is not null  and v_branchactiongroup is null )
THEN
    select count(*) into v_cnt from ssmn_zy_level where area =:NEW.area and area is not null  and branchactiongroup is  null ;

    IF v_cnt >0 
       THEN RAISE_APPLICATION_ERROR(-20001,'该片区已经存在!');
    END IF;
END IF;  
--战区是否存在
 IF ( v_warzone is not null  and v_area is null and  v_branchactiongroup is null )
THEN
    select count(*) into v_cnt from ssmn_zy_level where warzone =:NEW.warzone and warzone is not null and area is  null  and branchactiongroup is  null ;

    IF v_cnt >0 
       THEN RAISE_APPLICATION_ERROR(-20001,'该战区已经存在!');
    END IF;
END IF; 
--事业部
 IF ( v_businessdepartment is not null  and v_warzone is null and v_area is null and  v_branchactiongroup is null )
THEN
    select count(*) into v_cnt from ssmn_zy_level where businessdepartment =:NEW.businessdepartment and businessdepartment is not null  and warzone is null and area is  null  and branchactiongroup is  null ;

    IF v_cnt >0 
       THEN RAISE_APPLICATION_ERROR(-20001,'该事业部已经存在!');
    END IF;
END IF; 
--公司
 IF ( v_company is not null and v_businessdepartment is null   and v_warzone is null and v_area is null and  v_branchactiongroup is null )
THEN
    select count(*) into v_cnt from ssmn_zy_level where company =:NEW.company and company is not null  and businessdepartment is null  and warzone is null and area is  null  and branchactiongroup is  null ;

    IF v_cnt >0 
       THEN RAISE_APPLICATION_ERROR(-20001,'该公司已经存在!');
    END IF;
END IF; 
--省市
IF ( v_provincecity is not null and v_company is null  and v_businessdepartment is null   and v_warzone is null and v_area is null and  v_branchactiongroup is null )
THEN
    select count(*) into v_cnt from ssmn_zy_level where provincecity =:NEW.provincecity and provincecity is not null  and company is null  and businessdepartment is null  and warzone is null and area is  null  and branchactiongroup is  null ;

    IF v_cnt >0 
       THEN RAISE_APPLICATION_ERROR(-20001,'该省已经存在!');
    END IF;
END IF;
  
end;


----------------------------------------------------
---2015-08-10 添加表 SSMN_ZY_CDRSMS_USER
--1
-- Create table
create table SSMN_ZY_CDRSMS_USER
(
  ID        NUMBER(10) not null,
  USER_ID   NUMBER(10),
  CDRSMS_ID NUMBER(10),
  TYPE      NUMBER(1)
)
tablespace SSMN_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Add comments to the columns 
comment on column SSMN_ZY_CDRSMS_USER.TYPE
  is '0:语音 1:短信';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SSMN_ZY_CDRSMS_USER
  add constraint PK_SSNN_ZY_CDRSMS_USER primary key (ID)
  using index 
  tablespace SSMN_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Create/Recreate indexes 
create index PK_SSMN_ZY_CDRSMS_USER1 on SSMN_ZY_CDRSMS_USER (CDRSMS_ID)
  tablespace SSMN_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
create index PK_SSMN_ZY_CDRSMS_USER2 on SSMN_ZY_CDRSMS_USER (USER_ID)
  tablespace SSMN_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );

  
--创建序列
  -- Create sequence 
create sequence SEQ_SSNN_ZY_CDRSMS_USER
minvalue 1
maxvalue 99999999999
start with 1
increment by 1
cache 20;
  
--2.给ssmn_cdr表添加触发器
CREATE OR REPLACE TRIGGER "SSMNR".TRG_INSERT_SSMNCDR BEFORE
 INSERT  ON SSMN_CDR FOR EACH ROW

declare 
v_msisdn char(32);
v_ssmnnumber char(32);
v_uid NUMBER;
v_num NUMBER;

begin

v_msisdn := :NEW.msisdn;
v_ssmnnumber := :NEW.ssmnnumber;

--插入到通话记录与用户对应表中
IF ( v_msisdn is not null and v_ssmnnumber is not null )
THEN
    select count(*) into v_num  from ssmn_zy_user where msisdn= :NEW.msisdn and ssmnnumber =:NEW.ssmnnumber ;
    
    IF (v_num >0 )
    THEN
       select id into v_uid from ssmn_zy_user where msisdn= :NEW.msisdn and ssmnnumber =:NEW.ssmnnumber and rownum=1;
       insert into ssmn_zy_cdrsms_user (id, user_id,cdrsms_id,type ) values(SEQ_SSNN_ZY_CDRSMS_USER.NEXTVAL, v_uid, :NEW.streamnumber ,0);
    
    END IF;
END IF;  
  
end;


--3.给ssmn_sms_tomi添加触发器
CREATE OR REPLACE TRIGGER "SSMNR".TRG_INSERT_SSMNSMSTOMI BEFORE
 INSERT  ON SSMN_SMS_TOMI FOR EACH ROW

declare 
v_msisdn char(32);
v_ssmnnumber char(32);
v_uid NUMBER;
v_num NUMBER;

begin

v_msisdn := :NEW.destno;
v_ssmnnumber := :NEW.ssmnnumber;

--插入到通话记录与用户对应表中
IF ( v_msisdn is not null and v_ssmnnumber is not null )
THEN
    select count(*) into v_num  from ssmn_zy_user where msisdn= :NEW.destno and ssmnnumber =:NEW.ssmnnumber ;
    
    IF (v_num >0 )
    THEN
       select id into v_uid from ssmn_zy_user where msisdn= :NEW.destno and ssmnnumber =:NEW.ssmnnumber and rownum=1;
       insert into ssmn_zy_cdrsms_user (id, user_id,cdrsms_id,type ) values(SEQ_SSNN_ZY_CDRSMS_USER.NEXTVAL, v_uid, :NEW.id ,1);
    END IF;
END IF;  
  
end;

--4.
--ssmn_zy_user,ssmn_zy_cancel_user表添加字段MANNER　，标识哪种操作,默认０：web操作，1:手机操作

alter table ssmn_zy_user add ( MANNER number(1) default 0 );

alter table ssmn_zy_cancel_user add ( MANNER number(1) default 0 );

alter table ssmn_zy_cancel_user add ( OPERATYPE number(1) default 0 );

----5.初始ssmn_zy_cdrsms_user表

insert into ssmn_zy_cdrsms_user (id,user_id,cdrsms_id,type) select SEQ_SSNN_ZY_CDRSMS_USER.NEXTVAL, z.id ,c.streamnumber ,0 from ssmn_cdr c,ssmn_zy_user z where c.msisdn=z.msisdn and c.ssmnnumber=z.ssmnnumber and c.callstarttime>z.intime;


insert into ssmn_zy_cdrsms_user (id,user_id,cdrsms_id,type) select SEQ_SSNN_ZY_CDRSMS_USER.NEXTVAL, z.id ,c.id ,1 from ssmn_sms_tomi c,ssmn_zy_user z where c.destNo=z.msisdn and c.ssmnnumber=z.ssmnnumber and c.intime>z.intime ;



----------------2015-10-12 第9个有迭带-------------------------------------------------------
---1.添加登录详情查询 

insert into tyadmin_authorities (id,authority, auth_method, servicekey, rank ) values( SEQ_TYADMIN_AUTH.NEXTVAL,'登录详情查询', 'operatorloginsearch','50',1);

insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval,
 (select id from tyadmin_groups where group_name='超级管理员' ), 
(select id from tyadmin_authorities t where t.auth_method='operatorloginsearch' ) ) ;

-- Create/Recreate indexes 
create index PK_TYADMIN_LOGS1 on TYADMIN_LOGS (OPENO)
  tablespace DASCOM_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );



--------2015-11-20 添加　添加黑名单　功能
insert into tyadmin_authorities (id,authority, auth_method, servicekey, rank ) values(SEQ_TYADMIN_AUTH.NEXTVAL,'添加黑名单(操作员管理)', 'addblackcallnumber','50',1);
insert into TYADMIN_GROUP_AUTHS (ID, GROUP_ID, AUTH_ID) values(SEQ_TYAdmin_GROUP_AUTH.Nextval, (select t.group_id from tyadmin_operators t where t.servicekey='50' and t.openo='0000000000' ), SEQ_TYAdmin_AUTH.Currval);





