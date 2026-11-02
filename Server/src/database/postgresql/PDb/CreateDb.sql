
DROP TABLE IF EXISTS ActivityRuleTab CASCADE;
CREATE TABLE ActivityRuleTab (
	ProcessId	int	NOT NULL,
	ProcessVersion	int	NOT NULL,
	ActivityRuleId	int	NOT NULL,
	ActivityId	int	NULL,
	TrueActivityList	varchar(4096)	NULL,
	FalseActivityList	varchar(4096)	NULL,
	ErrorHandlerId	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(ActivityRuleId, ProcessId, ProcessVersion)
);

DROP TABLE IF EXISTS ActivityTab CASCADE;
CREATE TABLE ActivityTab (
	ProcessId	int	NOT NULL,
	ProcessVersion	int	NOT NULL,
	ActivityId	int	NOT NULL,
	ActivityName	varchar(4096)	NULL,
	ActivityMethod	varchar(255)	NOT NULL,
	ActivityLocation	varchar(4096)	NULL,
	ErrorHandlerId	int	NULL,
	HasInstance	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(ActivityId, ProcessId, ProcessVersion)
);

DROP TABLE IF EXISTS ArgumentTab CASCADE;
CREATE TABLE ArgumentTab (
	ProcessId	int	NOT NULL,
	ProcessVersion	int	NOT NULL,
	TaskId	int	NOT NULL,
	CallId	int	NOT NULL,
	ArgumentId	int	NOT NULL,
	DataType	varchar(10)	NOT NULL,
	InputParam	smallint	NOT NULL	DEFAULT 0,
	OutputParam	smallint	NOT NULL	DEFAULT 0,
	ReturnValue	smallint	NOT NULL	DEFAULT 0,
	OptionalParam	smallint	NOT NULL	DEFAULT 0,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(ArgumentId, CallId, ProcessId, ProcessVersion, TaskId)
);

DROP TABLE IF EXISTS AttachmentTab CASCADE;
CREATE TABLE AttachmentTab (
	AttachmentId	int	NOT NULL,
	ProcessId	int	NOT NULL,
	ProcessVersion	int	NOT NULL,
	DocumentId	int	NOT NULL,
	TransferId	int	NULL,
	MimeContentType	varchar(50)	NULL,
	FileName	varchar(260)	NOT NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(AttachmentId, DocumentId, ProcessId, ProcessVersion)
);

DROP TABLE IF EXISTS CryptoTab CASCADE;
CREATE TABLE CryptoTab (
	CryptoInfoId	int	PRIMARY KEY,
	CryptoMethod	varchar(20)	NOT NULL,
	EncryptionAlgoritm	int	NULL,
	SigningAlgoritm	int	NULL,
	CertificateId	varchar(256)	NOT NULL,
	Password	varchar(256)	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS DocumentTab CASCADE;
CREATE TABLE DocumentTab (
	ProcessId	int	NOT NULL,
	ProcessVersion	int	NOT NULL,
	TaskId	int	NOT NULL,
	DocumentId	int	NOT NULL,
	MsgTemplateName	varchar(8000)	NULL,
	MsgTemplateLocation	varchar(4096)	NOT NULL,
	URL	varchar(4096)	NULL,
	SendFormat	varchar(20)	NOT NULL,
	ToDocDataElementList	varchar(4096)	NULL,
	FromDocDataElementList	varchar(4096)	NULL,
	MsgSubject	varchar(1024)	NULL,
	MsgCC	varchar(1024)	NULL,
	MsgBCC	varchar(1024)	NULL,
	MsgPriority	smallint	NULL,
	MsgReplyTo	varchar(1024)	NULL,
	MsgFrom	varchar(255)	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(DocumentId, ProcessId, ProcessVersion, TaskId)
);

DROP TABLE IF EXISTS FunctionTab CASCADE;
CREATE TABLE FunctionTab (
	ID	int	PRIMARY KEY,
	Function	varchar(50)	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS MultiServerSetupTab CASCADE;
CREATE TABLE MultiServerSetupTab (
	MachineName	varchar(255)	NOT NULL,
	Enabled	smallint	NOT NULL	DEFAULT 0,
	CurrentRole	int	NOT NULL,
	Capability	smallint	NULL,
	AssetTag	varchar(50)	PRIMARY KEY,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS ObjectPermissionsTab CASCADE;
CREATE TABLE ObjectPermissionsTab (
	ObjectId	serial	PRIMARY KEY,
	ObjectName	varchar(300)	NOT NULL,
	AllowAccess	smallint	NOT NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS OrganizationStructureTab CASCADE;
CREATE TABLE OrganizationStructureTab (
	ProcessName	varchar(50)	NOT NULL,
	HierarchyName	varchar(50)	NOT NULL,
	NodeId	int	NOT NULL,
	OrganizationalUnitName	varchar(50)	NULL,
	LevelInTree	int	NULL,
	LevelName	varchar(50)	NULL,
	SiblingOrder	int	NULL,
	FullPath	varchar(255)	NOT NULL,
	NodeImage	smallint	NULL,
	VisibleStatus	smallint	NULL,
	RoleAssociationType	varchar(50)	NULL,
	RoleMappingId	int	NULL,
	FullTextPath	varchar(255)	NULL,
	IsRoleMultiple	smallint	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(HierarchyName, NodeId, ProcessName)
);

DROP TABLE IF EXISTS ProcessTab CASCADE;
CREATE TABLE ProcessTab (
	ProcessId	int	NOT NULL,
	ProcessVersion	int	NOT NULL,
	ProcessName	varchar(1024)	NOT NULL,
	Description	varchar(4096)	NULL,
	ErrorHandlerId	int	NULL,
	Owner	varchar(255)	NULL,
	ProcessType	smallint	NULL	DEFAULT 0,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(ProcessId, ProcessVersion)
);

DROP TABLE IF EXISTS RouteTab CASCADE;
CREATE TABLE RouteTab (
	ProcessId	int	NOT NULL,
	ProcessVersion	int	NOT NULL,
	OriginTaskId	int	NOT NULL,
	DestinationTaskId	int	NOT NULL,
	DataElementList	varchar(4096)	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(DestinationTaskId, OriginTaskId, ProcessId, ProcessVersion)
);

DROP TABLE IF EXISTS RoutingRuleTab CASCADE;
CREATE TABLE RoutingRuleTab (
	ProcessId	int	NOT NULL,
	ProcessVersion	int	NOT NULL,
	RoutingRuleId	int	NOT NULL,
	SequenceStack	varchar(255)	NULL,
	DestinationTaskId	int	NULL,
	TimeToEscalation	varchar(255)	NULL,
	EscalationDestinationTaskId	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(ProcessId, ProcessVersion, RoutingRuleId)
);

DROP TABLE IF EXISTS TaskDataElementsTab CASCADE;
CREATE TABLE TaskDataElementsTab (
	ProcessId	int	NOT NULL,
	ProcessVersion	int	NOT NULL,
	TaskId	int	NOT NULL,
	DataElementId	int	NOT NULL,
	DataElementName	varchar(255)	NOT NULL,
	DataElementDescription	varchar(4096)	NULL,
	DataElementType	varchar(255)	NOT NULL,
	OriginType	varchar(255)	NULL,
	InitialValue	varchar(4096)	NULL,
	IsRequired	int	NULL,
	DataElementDisplayName	varchar(255)	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(DataElementId, ProcessId, ProcessVersion, TaskId)
);

DROP TABLE IF EXISTS TaskDisplayAttributes CASCADE;
CREATE TABLE TaskDisplayAttributes (
	ProcessId	int	NOT NULL,
	ProcessVersion	int	NOT NULL,
	TaskId	int	NOT NULL,
	TopX	int	NULL,
	TopY	int	NULL,
	BottomX	int	NULL,
	BottomY	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(ProcessId, ProcessVersion, TaskId)
);

DROP TABLE IF EXISTS TaskTab CASCADE;
CREATE TABLE TaskTab (
	ProcessId	int	NOT NULL,
	ProcessVersion	int	NOT NULL,
	TaskId	int	NOT NULL,
	TaskName	varchar(1024)	NULL,
	Description	varchar(4096)	NULL,
	ErrorHandlerId	int	NULL,
	TaskRole	varchar(1024)	NULL,
	DelegationRole	varchar(1024)	NULL,
	RoutingRuleId	int	NULL,
	DocumentIdList	varchar(255)	NULL,
	ActivityRuleId	int	NULL,
	PreRoleActivityRuleId	int	NULL,
	TaskType	int	NULL,
	TaskPriority	smallint	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(ProcessId, ProcessVersion, TaskId)
);

DROP TABLE IF EXISTS TransferTab CASCADE;
CREATE TABLE TransferTab (
	TransferId	int	PRIMARY KEY,
	Protocol	varchar(50)	NOT NULL,
	RemoteServerName	varchar(256)	NULL,
	RemotePort	int	NULL,
	UserName	varchar(256)	NULL,
	Password	varchar(256)	NULL,
	LocalPath	varchar(260)	NOT NULL,
	RemotePath	varchar(4096)	NOT NULL,
	ProxyServerName	varchar(256)	NULL,
	TimeOutSeconds	int	NULL,
	LeaveOnServer	varchar(50)	NULL,
	DownloadCondition	varchar(128)	NULL,
	CryptoInfoId	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);


DROP VIEW IF EXISTS GetLogGUIDProcID;
CREATE VIEW GetLogGUIDProcID
AS
SELECT     ProcessId, ProcessVersion
FROM         TaskDataElementsTab P1
WHERE     (ProcessVersion =
                          (SELECT     MAX(ProcessVersion)
                            FROM          TaskDataElementsTab P2
                            WHERE      p2.ProcessId = P1.ProcessId)) AND (DataElementName LIKE '$LogGUID')
ORDER BY ProcessId, ProcessVersion
;
