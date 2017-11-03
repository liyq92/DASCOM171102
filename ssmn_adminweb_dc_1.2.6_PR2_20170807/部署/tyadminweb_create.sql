/************************************************
**	Create table 			**
************************************************/



create sequence SEQ_TYAdmin_GROUP INCREMENT BY 1  START WITH 10000000001 MAXVALUE 99999999999 ;
create sequence SEQ_TYAdmin_AUTH INCREMENT BY 1  START WITH 10000000001 MAXVALUE 99999999999 ;
create sequence SEQ_TYAdmin_GROUP_AUTH INCREMENT BY 1  START WITH 10000000001 MAXVALUE 99999999999 ;
create sequence SEQ_TYAdmin_LOG INCREMENT BY 1  START WITH 10000000001 MAXVALUE 99999999999 ;

COMMIT;




-- 权限表
create table TYADMIN_AUTHORITIES
(
  ID          NUMBER(11) not null,
  AUTHORITY   VARCHAR2(128) not null,
  AUTH_METHOD VARCHAR2(128) not null,
  SERVICEKEY  VARCHAR2(20) not null,
  RANK        NUMBER(2) not null
) tablespace ISDMD_DATA;
alter table TYADMIN_AUTHORITIES
  		add constraint PK_TYADMIN_AUTHORITIES primary key (ID) using index  tablespace ISDMD_IDX;



-- 群组表
create table TYADMIN_GROUPS
(
  ID          NUMBER(11) not null,
  GROUP_NAME  VARCHAR2(64) not null,
  DESCRIPTION VARCHAR2(128),
  SERVICEKEY  VARCHAR2(20) not null,
  CREATE_DATE DATE not null,
  OPERATOR_ID VARCHAR2(32) not null,
  RANK        NUMBER(2) not null
) tablespace ISDMD_DATA;
alter table TYADMIN_GROUPS
  		add constraint PK_TYADMIN_GROUPS primary key (ID) using index tablespace ISDMD_IDX;



-- 群组权限对应表
create table TYADMIN_GROUP_AUTHS
(
  ID       NUMBER(11) not null,
  GROUP_ID NUMBER(11) not null,
  AUTH_ID  NUMBER(11) not null
) tablespace ISDMD_DATA;
alter table TYADMIN_GROUP_AUTHS
  		add constraint PK_TYADMIN_GROUP_AUTHS primary key (ID) using index tablespace ISDMD_IDX;



-- 操作日志表
create table TYADMIN_LOGS
(
  ID         NUMBER(11) not null,
  OPENO      VARCHAR2(32) not null,
  GROUPID    NUMBER(11),
  SERVICEKEY VARCHAR2(20) not null,
  LOG_TYPE   VARCHAR2(64) not null,
  LOG_TIME   DATE not null,
  LOG_DES    VARCHAR2(512) not null
)
tablespace ISDMD_DATA;
alter table TYADMIN_LOGS
  		add constraint PK_TYADMIN_LOGS primary key (ID) using index tablespace ISDMD_IDX;



-- 操作员表
create table TYADMIN_OPERATORS
(
  OPENO       VARCHAR2(32) not null,
  SERVICEKEY  VARCHAR2(20) not null,
  OPEPWD      VARCHAR2(32) not null,
  GROUP_ID    NUMBER(11) not null,
  CREATE_USER VARCHAR2(32) not null,
  CREATE_TIME DATE not null,
  AGENT_INFO  VARCHAR2(64),
  Note        VARCHAR2(128)
) tablespace ISDMD_DATA;
alter table TYADMIN_OPERATORS
  		add constraint PK_TYADMIN_OPERATORS primary key (OPENO, SERVICEKEY) using index tablespace ISDMD_IDX;
  

commit;

-----------------------------------------------------------------------------------------------------------------
