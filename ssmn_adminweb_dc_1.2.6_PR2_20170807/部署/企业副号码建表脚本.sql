/************************************************
**	Create DC table script		**
************************************************/


-- SEQUENCE
create sequence SEQ_SSMN_DC_Owner INCREMENT BY 1 START WITH 1000000000 MAXVALUE 9999999998 CYCLE CACHE 10; 
create sequence SEQ_SSMN_DC_Agent INCREMENT BY 1 START WITH 1000000000 MAXVALUE 9999999998 CYCLE CACHE 10; 
create sequence SEQ_SSMN_DC_relation INCREMENT BY 1 START WITH 1000000000 MAXVALUE 9999999998 CYCLE CACHE 10; 
CREATE SEQUENCE seq_ssmn_dc_cdr  INCREMENT BY 1  START WITH 1000000000  MAXVALUE 9999999998  CYCLE CACHE 10; 

-- 业主信息表
-- drop table SSMN_DC_Owner;
create table SSMN_DC_Owner (
  ID      Number(10) constraint PK_SSMN_DC_Owner PRIMARY KEY,
  Msisdn    Varchar2(20) not null,
  "Date"    DATE not null,
  Source    Number(1) not null,
  Ownerid    Varchar2(64) not null,
  PropertyID  Varchar2(64) not null
)tablespace SSMN_DATA;
create index  IDX1_SSMN_DC  on  SSMN_DC_Owner (msisdn);


-- 地产经纪人信息表
-- drop table SSMN_DC_Agent;
create table SSMN_DC_Agent (
	ID			Number(10) constraint PK_SSMN_DC_Agent PRIMARY KEY,
	Msisdn		Varchar2(20) not null,
	"Date"		Date not null,
	EmpID		Varchar2(64) not null,
	EmpName		Varchar2(18) ,
	DeptID		Varchar2(64) not null,
	DeptName	Varchar2(18) 
)tablespace SSMN_DATA;
create index  IDX1_SSMN_Agent  on  SSMN_DC_Agent (msisdn);


-- 业主与经纪人关联表
-- drop table SSMN_DC_relation;
create table SSMN_DC_Relation (
  ID        Number(10) constraint PK_SSMN_DC_Relation PRIMARY KEY,
  ssmnnumber    Varchar2(18) not null,
  Owner_MSISDN  Varchar2(20) not null,
  Agent_MSISDN   Varchar2(20) not null,
  Crateline    Date not null
)tablespace SSMN_DATA;
create index  IDX1_SSMN_DC_relation  on  SSMN_DC_Relation (owner_msisdn);
create index  IDX2_SSMN_DC_relation  on  SSMN_DC_Relation (agent_msisdn);


-- 删除业主与经纪人关联的记录表
-- drop table SSMN_DC_DelRelation;
Create table SSMN_DC_DelRelation (
	ID				Number(10) constraint PK_SSMN_DC_Delrelation PRIMARY KEY,
	ssmnnumber		Varchar2(18) not null,
	Owner_MSISDN	Varchar2(20) not null,
	Agent_MSISDN 	Varchar2(20) not null,
	Crateline		Date not null,
	Deadline		Date not null
)tablespace SSMN_DATA;
create index  IDX1_SSMN_DC_delrelation  on  SSMN_DC_DelRelation (owner_msisdn);
create index  IDX2_SSMN_DC_delrelation  on  SSMN_DC_DelRelation (agent_msisdn);

-- 地产话单表
-- drop table SSMN_DC_CDR;
create table SSMN_DC_CDR
(
ID	Number(10)	not null,
CallingNum	Varchar2(20)	not null,
CalledNum	Date	Varchar2(20) not null,
Calltype	Number(1)	not null,
SSMNNumber	Varchar2(32)	not null,
Manner	Number(1)	not null,
startTime	Date not null,
endTime	date	not null,
peroid	Number(2),
deptName	Varchar2(64),
deptNo	Varchar2(64),
deptID	Varchar2(64),
empID	Varchar2(64),
empName	Varchar2(64),
OwnerID	Varchar2(64),
propertyID	Varchar2(64)	
) tablespace SSMN_DATA;
alter table SSMN_DC_CDR add constraint PK_SSMN_DC_CDR primary key(ID) using index tablespace SSMN_IDX;

Create index IDX1_SSMN_DC_CDR on SSMN_DC_CDR(callingnum) tablespace SSMN_IDX;
Create index IDX2_SSMN_DC_CDR on SSMN_DC_CDR(callednum) tablespace SSMN_IDX;
Create index IDX3_SSMN_DC_CDR on SSMN_DC_CDR (ssmnnumber) tablespace SSMN_IDX;
Create index IDX4_SSMN_DC_CDR on SSMN_DC_CDR(Callingnum,ssmnnumber,starttime) tablespace SSMN_IDX;
commit;

-- 地产话单表
-- drop table SSMN_DC_CDR;
create table SSMN_DC_CDR
(
	id			Number(10)        		NOT NULL,
	callingNum		Varchar2(20)			NOT NULL,
	calledNum		Varchar2(20)			NOT NULL,
	calltype		Number(1)			NOT NULL,
	ssmnNumber		Varchar2(32)        		NOT NULL,
	manner			Number(1)			NOT NULL,
	startTime		Date,
	endTime			Date,
	peroid			Number(10),
	deptName		Varchar2(32),
	deptNo			Varchar2(32),
	deptID			Varchar2(64),
	empName			Varchar2(32),
	empID			Varchar2(64),
	ownerID			Varchar2(32),
	propertyID		Varchar2(32)
) tablespace SSMN_DATA;
alter table SSMN_DC_CDR add constraint PK_SSMN_DC_CDR primary key(id)
using index tablespace SSMN_IDX;
commit;

-- 地产双呼模式话单表
-- drop table SSMN_DC_TwoCallCDR;
create table SSMN_DC_TwoCallCDR
(
ID	Number(10)	not null,
CallingNum	Varchar2(20)	not null,
CalledNum	Varchar2(20)	not null,
SSMNNumber	Varchar2(32)	not null,
createtime	date,	
waitperoid	Number(2),
callperoid	Number(2)

) tablespace SSMN_DATA;
alter table SSMN_DC_TwoCallCDR add constraint PK_SSMN_DC_TwoCallCDR primary key(id) using index tablespace SSMN_IDX;

Create index IDX1_SSMN_DC_TwoCallCDR on SSMN_DC_TwoCallCDR(callingnum) tablespace SSMN_IDX;
Create index IDX2_SSMN_DC_TwoCallCDR on SSMN_DC_TwoCallCDR(callednum) tablespace SSMN_IDX;
Create index IDX3_SSMN_DC_TwoCallCDR on SSMN_DC_TwoCallCDR (Createtime) tablespace SSMN_IDX;
commit;

--下面三个，在SSMNTable_Create_dc_v2.0文档中有创建，这里注掉
--alter table ssmn_zy_user add (ssmnnumber_type number(1) default 0);
--alter table SSMN_ZY_ENABLENUMBER add (type number(1) default 0);
---alter table SSMN_ZY_CHANNEL add (AUTHORITY number(1) default 0);


