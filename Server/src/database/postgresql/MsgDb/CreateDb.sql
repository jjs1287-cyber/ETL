CREATE OR REPLACE FUNCTION drop_if_exists (text) RETURNS INTEGER AS '
DECLARE
    table_name ALIAS FOR $1;
BEGIN
    IF ( SELECT COUNT(*) > 0 FROM pg_tables WHERE tablename=table_name AND schemaname=current_schema() ) THEN EXECUTE
        ''DROP TABLE '' || table_name || '' CASCADE'';
        RETURN 1;
    END IF;
    RETURN 0;
END;
' LANGUAGE 'plpgsql';

DROP TABLE IF EXISTS ArchiveDataElementTab CASCADE;
CREATE TABLE ArchiveDataElementTab (
	ArchiveId	bigserial	PRIMARY KEY,
	MsgId	bigint	NOT NULL,
	DataElementName	varchar(512)	NOT NULL,
	DataType	varchar(20)	NULL,
	IntValue	int	NULL,
	DoubleValue	real	NULL,
	StringValue	varchar(7000)	NULL
);

DROP TABLE IF EXISTS ArchiveJmsPropertiesTab CASCADE;
CREATE TABLE ArchiveJmsPropertiesTab
(
  JmsProperiesId int4,
  JmsPropertyId int4,
  JmsPropertyName varchar(1024),
  JmsPropertyType varchar(512),
  JmsPropertyValue text
); 

DROP TABLE IF EXISTS ArchiveJmsSendingPropertiesTab CASCADE;
CREATE TABLE ArchiveJmsSendingPropertiesTab
(
  SendingPropertyId int4,
  JmsMsgInstanceId int4,
  SendingPropertyKey varchar(1024),  
  SendingPropertyValue text
);

DROP TABLE IF EXISTS ArchiveMsgTab CASCADE;
CREATE TABLE ArchiveMsgTab (
	ArchiveId	bigserial	PRIMARY KEY,
	MsgFrom	varchar(255)	NULL,
	MsgTo	varchar(255)	NULL,
	MsgSubject	varchar(255)	NULL,
	ProcessVariables	varchar(4096)	NULL,
	ReceivedAt	int	NULL,
	MsgId	int	NULL,
	MsgReplyTo	varchar(1024)	NULL,
	MsgPriority	smallint	NULL,
	Owner	varchar(255)	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS ArchiveOutgoingJmsMsgTab CASCADE;
CREATE TABLE ArchiveOutgoingJmsMsgTab
(
  JmsMsgInstanceId int4,
  SendingProgress int4,
  JmsPropertiesId int4,
  JmsPayloadPath varchar(1024)
); 
COMMENT ON TABLE ArchiveOutgoingJmsMsgTab IS
'Messages that have been sent are moved here from OutgoingJmsMsgTab.';

DROP TABLE IF EXISTS DataElementTab CASCADE;
CREATE TABLE DataElementTab (
	MsgId	bigint	NOT NULL,
	DataElementName	varchar(512)	NOT NULL,
	DataType	varchar(20)	NOT NULL,
	IntValue	int	NULL,
	DoubleValue	real	NULL,
	StringValue	varchar(7000)	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(DataElementName, MsgId)
);

DROP TABLE IF EXISTS DataElementTextTab CASCADE;
CREATE TABLE DataElementTextTab (
	MsgId	bigint	NOT NULL,
	DataElementName	varchar(512)	NOT NULL,
	StringValue	text	NOT NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(DataElementName, MsgId)
);

DROP TABLE IF EXISTS ErrorDataElementTab CASCADE;
CREATE TABLE ErrorDataElementTab (
	MsgId	bigint	NOT NULL,
	DataElementName	varchar(512)	NOT NULL,
	DataType	varchar(20)	NULL,
	IntValue	int	NULL,
	DoubleValue	real	NULL,
	StringValue	varchar(7000)	NULL,
	ErrorId	serial	PRIMARY KEY,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS ErrorDataElementTextTab CASCADE;
CREATE TABLE ErrorDataElementTextTab (
	MsgId	bigint	NOT NULL,
	DataElementName	varchar(512)	NOT NULL,
	StringValue	text	NULL,
	ErrorId	serial	PRIMARY KEY,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS ErrorMsgTab CASCADE;
CREATE TABLE ErrorMsgTab (
	ErrorMsgId	bigserial	PRIMARY KEY,
	MsgFrom	varchar(255)	NULL,
	MsgTo	varchar(255)	NULL,
	MsgSubject	varchar(255)	NULL,
	ProcessVariables	varchar(4096)	NULL,
	ReceivedAt	int	NULL,
	MsgId	bigint	NOT NULL,
	ProcessId	int	NULL,
	ProcessInstanceId	int	NULL,
	TaskId	int	NULL,
	TaskInstanceId	int	NULL,
	EngineErrorId	int	NULL,
	EngineErrorText	text	NULL,
	Owner	varchar(255)	NULL,
	MsgPriority	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS IncomingMsgTab CASCADE;
CREATE TABLE IncomingMsgTab (
	MsgId bigserial PRIMARY KEY,
	MsgFrom	varchar(255) NULL,
	MsgTo varchar(255) NULL,
	MsgSubject varchar(255) NULL,
	ProcessVariables varchar(4096) NULL,
	OperationId	bigint NULL,
	ReceivedAt int	NOT NULL DEFAULT 0,
	IsExecuting int	NULL DEFAULT 0,
	Owner varchar(255) NULL,
	MsgPriority	smallint NOT NULL DEFAULT 5,
	NumThreads smallint NOT NULL DEFAULT (-1),
	MsgType	bigint NOT NULL	DEFAULT 0,
	InsertTime timestamp NULL DEFAULT NOW(),
	ModTime timestamp NULL DEFAULT NOW()
);

DROP TABLE IF EXISTS JmsPropertiesTab CASCADE;
CREATE TABLE JmsPropertiesTab
(
  JmsProperiesId int4 NOT NULL,
  JmsPropertyId int4 NOT NULL,
  JmsPropertyName varchar(1024) NOT NULL,
  JmsPropertyType varchar(512) NOT NULL,
  JmsPropertyValue text NOT NULL,
  CONSTRAINT JmsPropertiesTab_PKEY PRIMARY KEY (JmsProperiesId, JmsPropertyId)
); 
COMMENT ON TABLE JmsPropertiesTab IS
'JmsPropertyId starts counting at 1 for each JmsProperiesId. This table is a (1-N) child table of OutgoingJmsMsgTab';
COMMENT ON COLUMN JmsPropertiesTab.JmsProperiesId IS
'Identifies a group of JMS properties.';

DROP TABLE IF EXISTS JmsSendingPropertiesTab CASCADE;
CREATE TABLE JmsSendingPropertiesTab
(
  SendingPropertyId serial PRIMARY KEY,
  JmsMsgInstanceId int4 NOT NULL,
  SendingPropertyKey varchar(1024),  
  SendingPropertyValue text
);
COMMENT ON TABLE JmsSendingPropertiesTab IS
'Key-values table with properties needed when sending a JMS message (e.g., JNDI URL)';

DROP TABLE IF EXISTS OutgoingJmsMsgTab CASCADE;
CREATE TABLE OutgoingJmsMsgTab
(
  JmsMsgInstanceId int4 PRIMARY KEY,
  SendingProgress int4,
  JmsPropertiesId int4,
  JmsPayloadPath varchar(1024)
); 
COMMENT ON COLUMN OutgoingJmsMsgTab.SendingProgress IS
'0 = Not sent, 1 = Sending in progress, 2 = Sent, Negative numbers represent errors';

select drop_if_exists('operationdataelementtab2');
CREATE TABLE OperationDataElementTab2 (
	SessionId bigint NOT NULL,
	DEId int NOT NULL,
	DEType int NOT NULL,
	ParentDEId int NOT NULL,
	DEName text NULL,
	DEValue text NULL,
	PRIMARY KEY(SessionId, DEId)
);
CREATE INDEX OperationDataElementTab2_SessionId_IDX ON OperationDataElementTab2 (SessionId);

select drop_if_exists('sessionidtab');
CREATE TABLE SessionIdTab (
	SessionId bigserial PRIMARY KEY,
	Note varchar(16) NOT NULL
);

CREATE OR REPLACE FUNCTION SpInsertSessionIdTab(varchar) RETURNS integer AS '
DECLARE
  sNote ALIAS FOR $1;
  iSessionId integer;

BEGIN

INSERT INTO SessionIdTab (Note) VALUES (sNote);
SELECT INTO iSessionId currval(''sessionidtab_sessionid_seq'');
RETURN iSessionId;

END;
' LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION SpInsertOperationMsg(
						varchar,
						varchar,
						varchar,
						bigint,
						integer,
						integer,
						varchar,
						integer,
						integer) RETURNS bigint AS '
DECLARE
  sMsgFrom ALIAS FOR $1;
  sMsgTo ALIAS FOR $2;
  sMsgSubject ALIAS FOR $3;
  iOperationId ALIAS FOR $4;
  iReceivedAt ALIAS FOR $5;
  iIsExecuting ALIAS FOR $6;
  sOwner ALIAS FOR $7;
  iMsgPriority ALIAS FOR $8;
  iNumThreads ALIAS FOR $9;
  iMsgId bigint;

BEGIN

INSERT INTO IncomingMsgTab (MsgFrom,MsgTo,MsgSubject,ProcessVariables,OperationId,ReceivedAt,IsExecuting,Owner,MsgPriority,NumThreads)
VALUES (sMsgFrom,sMsgTo,sMsgSubject,null,iOperationId,iReceivedAt,iIsExecuting,sOwner,iMsgPriority,iNumThreads);

SELECT INTO iMsgId currval(''incomingmsgtab_msgid_seq'');

RETURN iMsgId;

END;
' LANGUAGE 'plpgsql';
