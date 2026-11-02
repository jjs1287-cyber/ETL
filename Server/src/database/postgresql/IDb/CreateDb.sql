-- This function drops a table only if it does not already exist.
-- Note that this function has to be added first since it is used below.
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


DROP TABLE IF EXISTS ActivityTab CASCADE;
CREATE TABLE ActivityTab (
	ProcessId	int	NOT NULL,
	TaskId	int	NOT NULL,
	ActivityId	int	NOT NULL,
	ProcessInstanceId	int	NOT NULL,
	TaskInstanceId	int	NOT NULL,
	ActivityInstanceId	int	NOT NULL,
	StartTime	int	NULL,
	EndTime	int	NULL,
	StatusTime	int	NULL,
	Status	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(ActivityId, ActivityInstanceId, ProcessId, ProcessInstanceId, TaskId, TaskInstanceId)
);

DROP TABLE IF EXISTS ArchiveActivityTab CASCADE;
CREATE TABLE ArchiveActivityTab (
	ArchiveId	serial	PRIMARY KEY,
	ProcessId	int	NOT NULL,
	TaskId	int	NOT NULL,
	ActivityId	int	NOT NULL,
	ProcessInstanceId	int	NOT NULL,
	TaskInstanceId	int	NOT NULL,
	ActivityInstanceId	int	NOT NULL,
	StartTime	int	NULL,
	EndTime	int	NULL,
	StatusTime	int	NULL,
	Status	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS ArchiveDataElementTab CASCADE;
CREATE TABLE ArchiveDataElementTab (
	ProcessId	int	NOT NULL,
	ProcessInstanceId	int	NOT NULL,
	TaskId	int	NOT NULL,
	TaskInstanceId	int	NOT NULL,
	DataElementId	int	NOT NULL,
	DataType	int	NOT NULL,
	DataElementName	varchar(512)	NOT NULL,
	IntValue	int	NULL,
	DoubleValue	real	NULL,
	StringValue	varchar(7000)	NULL,
	ArchiveId	serial	PRIMARY KEY,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS ArchiveDataElementTextTab CASCADE;
CREATE TABLE ArchiveDataElementTextTab (
	ProcessId	int	NOT NULL,
	ProcessInstanceId	int	NOT NULL,
	TaskId	int	NOT NULL,
	TaskInstanceId	int	NOT NULL,
	DataElementId	int	NOT NULL,
	StringValue	text	NOT NULL,
	ArchiveId	serial	PRIMARY KEY,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS ArchiveProcessTab CASCADE;
CREATE TABLE ArchiveProcessTab (
	ArchiveId	serial	PRIMARY KEY,
	ProcessId	int	NOT NULL,
	ProcessInstanceId	int	NOT NULL,
	ProcessVersion	int	NOT NULL,
	InitialTask	int	NULL,
	StartTime	int	NULL,
	EndTime	int	NULL,
	Status	int	NULL,
	StatusTime	int	NULL,
	UpdateFlag	int	NULL,
	ProcessDataElementValueList	varchar(7000)	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS ArchiveRouteTab CASCADE;
CREATE TABLE ArchiveRouteTab (
	ArchiveId	serial	PRIMARY KEY,
	ProcessId	int	NOT NULL,
	ProcessInstanceId	int	NOT NULL,
	DestinationTaskId	int	NOT NULL,
	DestinationTaskInstanceId	int	NOT NULL,
	OriginProcessId	int	NOT NULL,
	OriginProcessInstanceId	int	NOT NULL,
	OriginTaskId	int	NOT NULL,
	OriginTaskInstanceId	int	NOT NULL,
	RouteUser	varchar(255)	NULL,
	SentTime	int	NULL,
	Status	int	NULL,
	RouteInstanceId	int	NOT NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS ArchiveTaskTab CASCADE;
CREATE TABLE ArchiveTaskTab (
	ArchiveId	serial	PRIMARY KEY,
	ProcessId	int	NOT NULL,
	ProcessInstanceId	int	NOT NULL,
	TaskId	int	NOT NULL,
	TaskInstanceId	int	NOT NULL,
	TaskUser	varchar(255)	NULL,
	InitTime	int	NULL,
	StartTime	int	NULL,
	Status	int	NULL,
	DataElementValueList	varchar(4096)	NULL,
	FromDocDataElementValueList	varchar(4096)	NULL,
	EscalationId	varchar(255)	NULL,
	EndTime	int	NULL,
	MsgId	int	NULL,
	MsgPriority	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS DataElementTab CASCADE;
CREATE TABLE DataElementTab (
	ProcessId	int	NOT NULL,
	ProcessInstanceId	int	NOT NULL,
	TaskId	int	NOT NULL,
	TaskInstanceId	int	NOT NULL,
	DataElementId	int	NOT NULL,
	DataType	int	NOT NULL,
	DataElementName	varchar(512)	NOT NULL,
	IntValue	int	NULL,
	DoubleValue	real	NULL,
	StringValue	varchar(7000)	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(DataElementId, ProcessId, ProcessInstanceId, TaskId, TaskInstanceId)
);

DROP TABLE IF EXISTS DataElementTextTab CASCADE;
CREATE TABLE DataElementTextTab (
	ProcessId	int	NOT NULL,
	ProcessInstanceId	int	NOT NULL,
	TaskId	int	NOT NULL,
	TaskInstanceId	int	NOT NULL,
	DataElementId	int	NOT NULL,
	StringValue	text	NOT NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(DataElementId, ProcessId, ProcessInstanceId, TaskId, TaskInstanceId)
);

DROP TABLE IF EXISTS InstanceInfoTab CASCADE;
CREATE TABLE InstanceInfoTab (
	ProcessId	int	NOT NULL,
	ProcessVersion	int	NOT NULL,
	HighestProcessInstanceId	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(ProcessId, ProcessVersion)
);

select drop_if_exists('operationdataelementtab');
CREATE TABLE OperationDataElementTab (
	SessionId	int	NOT NULL,
	DataElementName	varchar(512)	NOT NULL,
	ArrayIndex varchar(1024)	NOT NULL,
	StringValue	text	NOT NULL,
	PRIMARY KEY(SessionId, DataElementName, ArrayIndex)
);
CREATE INDEX OperationDataElementTab_SessionId_IDX ON OperationDataElementTab (SessionId);

select drop_if_exists('sessionidtab');
CREATE TABLE SessionIdTab (
	SessionId	serial	PRIMARY KEY,
	Note		varchar(16) NOT NULL
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

DROP TABLE IF EXISTS ProcessTab CASCADE;
CREATE TABLE ProcessTab (
	ProcessId	int	NOT NULL,
	ProcessInstanceId	serial	NOT NULL,
	ProcessVersion	int	NOT NULL,
	InitialTask	int	NULL,
	StartTime	int	NULL,
	EndTime	int	NULL,
	Status	int	NULL,
	StatusTime	int	NULL,
	UpdateFlag	int	NULL,
	ProcessDataElementValueList	varchar(7000)	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(ProcessId, ProcessInstanceId, ProcessVersion)
);

DROP TABLE IF EXISTS RouteDataElementTab CASCADE;
CREATE TABLE RouteDataElementTab (
	RouteInstanceId	int	NOT NULL,
	DataElementId	int	NOT NULL,
	DataType	int	NOT NULL,
	DataElementName	varchar(512)	NOT NULL,
	IntValue	int	NULL,
	DoubleValue	real	NULL,
	StringValue	varchar(7000)	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(DataElementId, RouteInstanceId)
);

DROP TABLE IF EXISTS RouteDataElementTextTab CASCADE;
CREATE TABLE RouteDataElementTextTab (
	RouteInstanceId	int	NOT NULL,
	DataElementId	int	NOT NULL,
	StringValue	text	NOT NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(DataElementId, RouteInstanceId)
);

DROP TABLE IF EXISTS RouteTab CASCADE;
CREATE TABLE RouteTab (
	ProcessId	int	NOT NULL,
	ProcessInstanceId	int	NOT NULL,
	DestinationTaskId	int	NOT NULL,
	DestinationTaskInstanceId	int	NOT NULL,
	OriginProcessId	int	NOT NULL,
	OriginProcessInstanceId	int	NOT NULL,
	OriginTaskId	int	NOT NULL,
	OriginTaskInstanceId	int	NOT NULL,
	RouteUser	varchar(7000)	NULL,
	SentTime	int	NULL,
	Status	int	NULL,
	RouteInstanceId	serial	NOT NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(DestinationTaskId, DestinationTaskInstanceId, OriginProcessId, OriginProcessInstanceId, OriginTaskId, OriginTaskInstanceId, ProcessId, ProcessInstanceId)
);

DROP TABLE IF EXISTS StatusTab CASCADE;
CREATE TABLE StatusTab (
	ProcessId	int	NOT NULL,
	ProcessVersion	int	NOT NULL,
	Status	int	NULL,
	StatusTime	int	NULL,
	UpdateFlag	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(ProcessId, ProcessVersion)
);

DROP TABLE IF EXISTS TaskTab CASCADE;
CREATE TABLE TaskTab (
	ProcessId	int	NOT NULL,
	ProcessInstanceId	int	NOT NULL,
	TaskId	int	NOT NULL,
	TaskInstanceId	serial	NOT NULL,
	TaskUser	varchar(255)	NULL,
	InitTime	int	NULL,
	StartTime	int	NULL,
	Status	int	NULL,
	DataElementValueList	varchar(4096)	NULL,
	FromDocDataElementValueList	varchar(4096)	NULL,
	EscalationId	varchar(255)	NULL,
	EndTime	int	NULL,
	MsgId	int	NULL,
	MsgPriority	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(ProcessId, ProcessInstanceId, TaskId, TaskInstanceId)
);

DROP TABLE IF EXISTS TemporaryFileTab CASCADE;
CREATE TABLE TemporaryFileTab (
	FileId	serial	PRIMARY KEY,
	ProcessId	int	NOT NULL,
	ProcessInstanceId	int	NOT NULL,
	FileName	varchar(512)	NOT NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);


CREATE OR REPLACE FUNCTION SpInsertTaskTab2(integer,
					   integer,
					   integer,
					   varchar,
					   integer,
					   integer,
					   integer,
					   varchar,
					   varchar,
					   varchar,
					   integer,
					   integer,
					   integer) RETURNS integer AS '

DECLARE
  iProcessId ALIAS FOR $1;
  iProcessInstanceId ALIAS FOR $2;
  iTaskId ALIAS FOR $3;
  sTaskUser ALIAS FOR $4;
  iInitTime ALIAS FOR $5;
  iStartTime ALIAS FOR $6;
  iStatus ALIAS FOR $7;
  sDataElementValueList ALIAS FOR $8;
  sFromDocDataElementValueList ALIAS FOR $9;
  sEscalationId ALIAS FOR $10;
  iEndTime ALIAS FOR $11;
  iMsgId ALIAS FOR $12;
  iMsgPriority ALIAS FOR $13;
  iTaskInstanceId integer;

BEGIN

INSERT INTO TaskTab (ProcessId,
		     ProcessInstanceId,
		     TaskId,
		     TaskUser,
		     InitTime,
		     StartTime,
		     Status,
		     DataElementValueList,
		     FromDocDataElementValueList,
		     EscalationId,
		     EndTime,
		     MsgId,
		     MsgPriority) 
	VALUES (iProcessId,
		iProcessInstanceId,
		iTaskId,
		sTaskUser,
		iInitTime,
		iStartTime,
		iStatus,
		sDataElementValueList,
		sFromDocDataElementValueList,
		sEscalationId,
		iEndTime,
		iMsgId,
		iMsgPriority);
					     
SELECT INTO iTaskInstanceId currval(''tasktab_taskinstanceid_seq'');

RETURN iTaskInstanceId;

END;
' LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION SpInsertProcessTab(integer,
					   integer,
					   integer,
					   integer,
					   integer,
					   integer,
					   integer) RETURNS integer AS '

DECLARE
  iProcessId ALIAS FOR $1;
  iProcessVersion ALIAS FOR $2;
  iInitialTask ALIAS FOR $3;
  iStartTime ALIAS FOR $4;
  iStatus ALIAS FOR $5;
  iStatusTime ALIAS FOR $6;
  iUpdateFlag ALIAS FOR $7;
  iProcessInstanceId integer;

BEGIN

INSERT INTO ProcessTab (ProcessId,
			ProcessVersion,
			InitialTask,
			StartTime,
			EndTime,
			Status,
			StatusTime,
			UpdateFlag)
	VALUES (iProcessId,
		iProcessVersion,
		iInitialTask,
		iStartTime,
		0,
		iStatus,
		iStatusTime,
		iUpdateFlag);
					     
SELECT INTO iProcessInstanceId currval(''processtab_processinstanceid_seq'');

RETURN iProcessInstanceId;

END;
' LANGUAGE 'plpgsql';
