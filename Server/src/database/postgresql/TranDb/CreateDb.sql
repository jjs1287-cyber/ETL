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

-- This function drops a view only if it does not already exist.
CREATE OR REPLACE FUNCTION drop_view_if_exists (text) RETURNS INTEGER AS '
DECLARE
    view_name ALIAS FOR $1;
BEGIN
    IF ( SELECT COUNT(*) > 0 FROM pg_views WHERE viewname=view_name AND schemaname=current_schema() ) THEN EXECUTE
        ''DROP VIEW '' || view_name || '' CASCADE'';
        RETURN 1;
    END IF;
    RETURN 0;
END;
' LANGUAGE 'plpgsql';

-- This function executes the second argument if the table specified in the first does not exist.
CREATE OR REPLACE FUNCTION create_if_not_exists (text, text) RETURNS INTEGER AS '
DECLARE
    table_name ALIAS FOR $1;
    create_sql ALIAS FOR $2;
BEGIN
    IF ( SELECT COUNT(*) FROM pg_tables WHERE tablename=table_name ) = 0 THEN EXECUTE
        create_sql;
        RETURN 1;
    END IF;
    RETURN 0;
END;
' LANGUAGE 'plpgsql';

DROP TABLE IF EXISTS CallRestrictionsTab CASCADE;
CREATE TABLE CallRestrictionsTab (
	CallRestriction_ID	serial	PRIMARY KEY,
	Function_ID	int	NOT NULL,
	FunctionType_ID	int	NOT NULL,
	MinCallDelay_ms	int	NOT NULL	DEFAULT 0,
	MaxSize_bytes	bigint	NOT NULL	DEFAULT 0,
	MaxSize_UiUnit	varchar(2)	NOT NULL	DEFAULT 'kb',
	Retries	int	NOT NULL	DEFAULT 0,
	RetryDelay_ms	int	NOT NULL	DEFAULT 0
);

DROP TABLE IF EXISTS CommonSourceOperationsTab CASCADE;
CREATE TABLE CommonSourceOperationsTab (
	Id	serial	PRIMARY KEY,
	Operation_ID	int	NOT NULL,
	SiblingOperation_ID	int	NOT NULL
);
COMMENT ON TABLE CommonSourceOperationsTab IS
'Operations that run at the same time as a specified operation, and with the same source. This was added so that the user can start more than one op to handle hosted web service calls.';

DROP TABLE IF EXISTS DataElementsTab CASCADE;
CREATE TABLE DataElementsTab
(
  DataElementId int PRIMARY KEY,
  OwnerEntityTypeId int NOT NULL,
  OwnerEntityId int NOT NULL,
  DataElementName varchar(1024) NOT NULL,
  DataElementType varchar(512) NOT NULL,
  DataElementDefaultValue text NOT NULL
);
COMMENT ON TABLE DataElementsTab IS
'Binds default values of data elements to other entities, e.g. JMS Messages.';
COMMENT ON COLUMN DataElementsTab.OwnerEntityTypeId IS
'Type of the entity that owns this data element.';
COMMENT ON COLUMN DataElementsTab.OwnerEntityId IS
'The Id of the entity that owns this data element.';

DROP TABLE IF EXISTS DBFieldsTab CASCADE;
CREATE TABLE DBFieldsTab (
	DBFieldID	serial	PRIMARY KEY,
	Transformation_ID	int	NULL,
	KeyFields	varchar(7000)	NULL,
	KeyFieldsType	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS DBTablesTab CASCADE;
CREATE TABLE DBTablesTab (
	DBTableID	serial	PRIMARY KEY,
	Transformation_ID	int	NULL,
	Tables	varchar(7000)	NULL,
	TablesType	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

SELECT drop_if_exists('deployhistory');
CREATE TABLE DeployHistory (
  DeployHistory_Id	serial PRIMARY KEY,
  Item_Id			int NOT NULL,
  EntityType_Id		int NOT NULL,
  User_Guid			varchar(36) NOT NULL,
  DeployTime		timestamp NOT NULL
);
CREATE INDEX DeployHistory_Item_Id_EntityType_Id_Idx ON DeployHistory (Item_Id, EntityType_Id);

DROP TABLE IF EXISTS Document_DefinitionsTab CASCADE;
CREATE TABLE Document_DefinitionsTab (
	Document_ID	int	NOT NULL,
	Field_ID	int	NOT NULL,
	Parent_ID	int	NOT NULL,
	Field_Name	varchar(256)	NOT NULL,
	Sequence	int	NULL	DEFAULT 0,
	Var_Type_ID	int	NULL	DEFAULT 0,
	Start_Pos	int	NULL	DEFAULT 0,
	End_Pos		int	NULL	DEFAULT 0,
	Required	int	NULL,
	Default_value	varchar(256)	NULL,
	Format	varchar(256)	NULL,
	Uni_ID	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(Document_ID, Field_ID, Parent_ID)
);

DROP TABLE IF EXISTS DocumentFieldValidationTab CASCADE;
CREATE TABLE DocumentFieldValidationTab (
  Document_Id int NOT NULL,
  Field_Id int NOT NULL,
  Parent_Id int NOT NULL,
  NotEmpty int NOT NULL,
  FieldLength varchar(128),
  TrimToLength int,
  IgnoreCase int,
  FieldOperator varchar(128),
  OperatorArgument text,
  ActionOnInvalid varchar(128),
  CONSTRAINT DocumentFieldValidationTab_PK PRIMARY KEY (Document_Id, Field_Id, Parent_Id)
);

SELECT drop_if_exists('documentpropertiestab');
CREATE TABLE DocumentPropertiesTab
(
  DocumentProperties_Id serial PRIMARY KEY,
  Document_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX DocumentPropertiesTab_Document_Id_IDX ON DocumentPropertiesTab (Document_Id);

DROP TABLE IF EXISTS DocumentsTab CASCADE;
CREATE TABLE DocumentsTab (
	Document_ID	int	PRIMARY KEY,
	Document_Name	varchar(256)	NULL,
	Format_Type_ID	int	NULL,
	Delimiter	varchar(4)	NULL,
	Text_QualIFier	varchar(4)	NULL,
	Numeric_QualIFier	varchar(4)	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS FolderItemTab CASCADE;
CREATE TABLE FolderItemTab (
	FolderItem_ID	serial	PRIMARY KEY,
	ParentFolder_ID	int	NOT NULL,
	EntityType_ID	int	NOT NULL,
	Item_ID	int	NOT NULL
);

DROP TABLE IF EXISTS FoldersTab CASCADE;
CREATE TABLE FoldersTab (
	Folder_ID	serial	PRIMARY KEY,
	Folder_Name	varchar(256)	NOT NULL,
	ContentsEntityType_ID	int	NOT NULL
);

SELECT drop_if_exists('folderpropertiestab');
CREATE TABLE FolderPropertiesTab
(
  FolderProperties_Id serial PRIMARY KEY,
  Folder_ID int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX FolderPropertiesTab_Folder_Id_IDX ON FolderPropertiesTab (Folder_Id);

DROP TABLE IF EXISTS FunctionTypesTab CASCADE;
CREATE TABLE FunctionTypesTab (
	FunctionType_ID	serial	PRIMARY KEY,
	FunctionType_Name	varchar(50)	NOT NULL
);

DROP TABLE IF EXISTS FunctionsTab CASCADE;
CREATE TABLE FunctionsTab (
	Function_ID	serial	PRIMARY KEY,
	FunctionType_ID	int	NOT NULL,
	ForeignKeyFunction_ID	int	NOT NULL
);

DROP TABLE IF EXISTS GroupsTab CASCADE;
CREATE TABLE GroupsTab (
	Group_ID	int	PRIMARY KEY,
	Group_Name	varchar(256)	NULL,
	Description	varchar(2048)	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime		timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS HttpEndpointsTab CASCADE;
CREATE TABLE HttpEndpointsTab (
  HttpEndpoint_ID			int PRIMARY KEY,
  HttpEndpoint_Name			varchar(256),
  HttpEndpoint_Description	text,
  Endpoint_Url				varchar(1024) NOT NULL,
  Response_Type				int NOT NULL,
  Request_Variable			varchar(50),
  Response_Variable			varchar(50),
  Response_Content_Type		varchar(50),
  Base_Url					int,
  Relative_Url_Variable		varchar(50)
);

DROP TABLE IF EXISTS HttpEndpointOperationsTab CASCADE;
CREATE TABLE HttpEndpointOperationsTab (
  HttpEndpointOperation_ID	serial PRIMARY KEY,
  HttpEndpoint_ID			int NOT NULL,
  Operation_ID				int NOT NULL,
  Synchronous				int NOT NULL,
  Use_Request_As_Source		int,
  Final_Target_As_Response	int,
  Http_Verb					varchar(16)
);
CREATE INDEX HttpEndpointOperationsTab_HttpEndpoint_ID_IDX ON HttpEndpointOperationsTab (HttpEndpoint_ID);

DROP TABLE IF EXISTS HttpTriggersTab CASCADE;
CREATE TABLE HttpTriggersTab (
  HttpTrigger_ID			serial PRIMARY KEY,
  Url						varchar(1024) NOT NULL,
  HttpTriggerType			int NOT NULL,
  Item_ID					int
);
CREATE INDEX HttpTriggersTab_Url_IDX ON HttpTriggersTab (Url);

DROP TABLE IF EXISTS InterchangeProjectsTab CASCADE;
CREATE TABLE InterchangeProjectsTab (
        InterchangeProject_ID   int             PRIMARY KEY,
        InterchangeProject_Name varchar(256)    NOT NULL,
        Description             varchar(4096)
);

DROP TABLE IF EXISTS InterchangeProjectItemsTab CASCADE;
CREATE TABLE InterchangeProjectItemsTab (
        InterchangeProject_ID   int     NOT NULL,
        EntityType_ID           int     NOT NULL,
        Item_ID                 int     NOT NULL
);

DROP TABLE IF EXISTS JmsLocationPropertiesTab CASCADE;
CREATE TABLE JmsLocationPropertiesTab
(
  JmsLocationPropertyId int PRIMARY KEY,
  JmsLocationEntityTypeId int NOT NULL,
  JmsLocationId int NOT NULL,
  KeyName varchar(512) NOT NULL,
  Value text NOT NULL
);
COMMENT ON TABLE JmsLocationPropertiesTab IS
'Key-values table with properties needed when listening for a JMS message (e.g., JNDI URL)';
COMMENT ON COLUMN JmsLocationPropertiesTab. JmsLocationEntityTypeId IS
'Specifies if this location is a source or target.';
COMMENT ON COLUMN JmsLocationPropertiesTab.JmsLocationId IS
'Either Source or Target ID depending on the value of JmsLocationEntityTypeId.';

DROP TABLE IF EXISTS JmsMessagesTab CASCADE;
CREATE TABLE JmsMessagesTab
(
  JmsMessage_ID int PRIMARY KEY,
  JmsMessageName varchar(1024) NOT NULL,
  PayloadEntityTypeId int NOT NULL,
  PayloadDocumentId int NOT NULL
);
COMMENT ON TABLE JmsMessagesTab IS 'Populated by deploy. 
Used to connect the payload of incoming JMS message, to its structure (DTD, XSD, or Text)';
COMMENT ON COLUMN JmsMessagesTab.PayloadEntityTypeId IS 'Type of payload.';
COMMENT ON COLUMN JmsMessagesTab.PayloadDocumentId IS
'Use PayloadEntityTypeId to determine if the payload is XML or Text.';

DROP TABLE IF EXISTS JmsProvidersTab CASCADE;
CREATE TABLE JmsProvidersTab (
	ProviderId serial PRIMARY KEY,
	ProviderName varchar(256) NOT NULL,
	Classpath text NOT NULL,
	ProviderDescription text
);
COMMENT ON TABLE JmsProvidersTab IS 'This is where the user interface gets its list of JMS providers. 
JBoss Messaging and WebSphere MQ are examples of providers. The Classpath field is used by the 
JMS Service to start the JMS Engine.';

DROP TABLE IF EXISTS Key_Values CASCADE;
CREATE TABLE Key_Values (
	Key_ValuesID	serial	PRIMARY KEY,
	Key_Name	varchar(256)	NULL,
	Key_Value	varchar(512)	NULL,
	Long_Key_Value	text	NULL,
	Key_Type	varchar(256)	NULL,
	Group_Id	int	NULL,
	Key_Value_Description	varchar(256)	NULL,
	Key_Display_Name	varchar(256)	NULL,
	Sort_Sequence	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);
-- Make room for some values to be loaded via copy (which does not reset serials).
SELECT setval('key_values_key_valuesid_seq', 1000);

DROP TABLE IF EXISTS LdapSearchParametersTab CASCADE;
CREATE TABLE LdapSearchParametersTab
(
	Ldap_SearchParameter_ID	serial PRIMARY KEY,
	Transformation_ID		int	NULL,
	ClassName				varchar(512) NOT NULL,
	Path					text NOT NULL,
	Filter					text NOT NULL,
	Scope					int NOT NULL,
	Oid						varchar(50)
)
WITHOUT OIDS;

DROP TABLE IF EXISTS LdapStructureEntriesTab CASCADE;
CREATE TABLE LdapStructureEntriesTab
(
  Ldap_Structure_Entry_ID	serial PRIMARY KEY,
  Transformation_ID			int NOT NULL,
  Type						int NOT NULL, -- 0 for source/1 for target
  oid						varchar(50) NOT NULL,
  Attributes				text NOT NULL,
  Auxiliary_Classes			text NOT NULL
)
WITHOUT OIDS;

SELECT drop_if_exists('locktab');
CREATE TABLE locktab (
  id serial NOT NULL,
  who varchar(50),
  CONSTRAINT lock_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS MessagesTab CASCADE;
CREATE TABLE MessagesTab (
	Message_ID		int PRIMARY KEY,
	Message_Name	varchar(256) NOT NULL,
	Smtp_Servers	varchar(1024),
	Use_SSL			int,
	Account			varchar(128),
	Account_Password	varchar(256),
	Send_To				varchar(1024) NOT NULL,
	Sent_From			varchar(1024),
	Cc				varchar(1024),
	Bcc				varchar(1024),
	Reply_To		varchar(1024),
	Subject			varchar(256),
	Message_Text	text,
	InsertTime 		timestamp DEFAULT now(),
	ModTime 		timestamp DEFAULT now()
);

SELECT drop_if_exists('operationgraphlayouttab');
CREATE TABLE OperationGraphLayoutTab (
  OperationGraphLayout_Id	serial PRIMARY KEY,
  Operation_Id				int NOT NULL,
  Layout					text NOT NULL
);
CREATE INDEX OperationGraphLayoutTab_Operation_Id_Idx ON OperationGraphLayoutTab (Operation_Id);

SELECT drop_if_exists('operationpropertiestab');
CREATE TABLE OperationPropertiesTab
(
  OperationProperties_Id serial PRIMARY KEY,
  Operation_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX OperationPropertiesTab_Operation_Id_IDX ON OperationPropertiesTab (Operation_Id);

SELECT drop_if_exists('operationpipelineactivitiestab');
CREATE TABLE OperationPipelineActivitiesTab (
  OperationActivity_ID		serial PRIMARY KEY,
  Operation_ID				int NOT NULL,
  ActivityType_ID			int NOT NULL,
  Activity_ID				int NOT NULL
);
CREATE INDEX OperationPipelineActivitiesTab_Operation_ID_IDX ON OperationPipelineActivitiesTab (operation_id);

DROP TABLE IF EXISTS OperationsTab CASCADE;
CREATE TABLE OperationsTab (
	Operation_ID	int	PRIMARY KEY,
	Operation_Name	varchar(256)	NULL,
	Operation_Type_ID	int	NULL,
	Project_ID	int	NULL,
	Source_ID	int	NULL,
	Target_ID	int	NULL,
	Schedule_ID	int	NULL,
	Transformation_ID	int	NULL,
	Success_Operation_ID	int	NULL,
	Success_Message_ID	int	NULL,
	Success_Workflow	varchar(255)	NULL,
	Failure_Operation_ID	int	NULL,
	Failure_Message_ID	int	NULL,
	Failure_Workflow	varchar(255)	NULL,
	User_ID	int	NULL,
	Description	varchar(2048)	NULL,
	Trace	int	NULL,
	DIFf	int	NULL	DEFAULT 0,
	Ignore_History	int	NULL,
	EDI_997_Ack	int	NULL,
	Ignore_EDI_Rules	int	NULL,
	TPAGUID	varchar(50)	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

SELECT drop_if_exists('operationinstancetab');
CREATE TABLE OperationInstanceTab
(
  operation_instance_id bigserial NOT NULL,
  operation_id int,
  msgid bigint NOT NULL default -1,
  parent_operation_instance_id bigint NOT NULL default -1,
  received_from varchar(50),
  CONSTRAINT OperationInstanceTab_pkey PRIMARY KEY (operation_instance_id)
);
CREATE INDEX OperationInstanceTab_Idx_parent_operation_instance_id ON OperationInstanceTab(parent_operation_instance_id);
CREATE INDEX OperationInstanceTab_Idx_operation_id ON OperationInstanceTab(operation_id);
CREATE INDEX OperationInstanceTab_Idx_msgid ON OperationInstanceTab(msgid);

SELECT drop_if_exists('operationlogtab');
CREATE TABLE OperationLogTab
(
  operation_instance_id bigint NOT NULL,
  operation_id int NOT NULL,
  status int NOT NULL,
  entered_ts timestamp without time zone NOT NULL,
  entered_tm bigint NOT NULL,
  started_ts timestamp without time zone,
  started_tm bigint,
  status_ts timestamp without time zone NOT NULL,
  status_tm bigint NOT NULL,
  CONSTRAINT OperationLogTab_pkey PRIMARY KEY (operation_instance_id)
);
CREATE INDEX OperationLogTab_Idx_Operation_Id ON OperationLogTab(operation_id);
CREATE INDEX OperationLogTab_Idx_entered_tm ON OperationLogTab(entered_tm);
CREATE INDEX OperationLogTab_Idx_started_tm ON OperationLogTab(started_tm);
CREATE INDEX OperationLogTab_Idx_status_tm ON OperationLogTab(status_tm);

SELECT drop_if_exists('archiveoperationlogtab');
CREATE TABLE ArchiveOperationLogTab
(
  archive_operation_instance_id bigserial NOT NULL,
  operation_instance_id bigint NOT NULL,
  operation_id int NOT NULL,
  status int NOT NULL,
  entered_ts timestamp without time zone NOT NULL,
  entered_tm bigint NOT NULL,
  started_ts timestamp without time zone,
  started_tm bigint,
  status_ts timestamp without time zone NOT NULL,
  status_tm bigint NOT NULL,
  CONSTRAINT ArchiveOperationLogTab_pkey PRIMARY KEY (archive_operation_instance_id)
);
CREATE INDEX ArchiveOperationLogTab_Idx_OperationInstance_Id ON ArchiveOperationLogTab(operation_instance_id);

SELECT drop_if_exists('operationfilestatustab');
CREATE TABLE  OperationFileStatusTab(
  Operation_ID int NOT NULL,
  Operation_Instance_ID bigint NOT NULL,
  Records 		bigint NOT NULL DEFAULT -1,
  FileCategory 	int NOT NULL,
  FileSize 	bigint NOT NULL DEFAULT 0,
  FileRelativePathName 	character varying(4096),
  FileCharEncode 	character varying(128),
  FileGUID character varying(36) NOT NULL,
  FileStatusMessage character varying(2048),
  CONSTRAINT OperationFileStatusTab_pkey PRIMARY KEY (FileGUID)
);
CREATE INDEX OperationFileStatusTab_Operation_Id_Operation_Instance_Id_Idx ON OperationFileStatusTab(Operation_Id, Operation_Instance_Id);

SELECT drop_if_exists('operationmessagetab');
CREATE TABLE OperationMessageTab
(
  operation_message_id bigserial NOT NULL,
  operation_instance_id bigint NOT NULL,
  field_name varchar(255) NOT NULL,
  message text NOT NULL,
  CONSTRAINT OperationMessageTab_pkey PRIMARY KEY (operation_message_id)
);
CREATE INDEX OperationMessageTab_Idx_operation_instance_id ON OperationMessageTab(operation_instance_id);

SELECT drop_if_exists('archiveoperationmessagetab');
CREATE TABLE ArchiveOperationMessageTab
(
  archive_operation_message_id bigserial NOT NULL,
  operation_message_id bigint NOT NULL,
  operation_instance_id bigint NOT NULL,
  field_name varchar(255) NOT NULL,
  message text NOT NULL,
  CONSTRAINT ArchiveOperationMessageTab_pkey PRIMARY KEY (archive_operation_message_id)
);
CREATE INDEX ArchiveOperationMessageTab_Idx_operation_instance_id ON ArchiveOperationMessageTab(operation_instance_id);

SELECT drop_if_exists('operationsummarytab');
CREATE TABLE OperationSummaryTab
(
  operation_instance_id bigint NOT NULL,
  source_files int,
  target_files int,
  source_size bigint,
  target_size bigint,
  source_records bigint,
  target_records bigint,
  total_transformation_time bigint,
  CONSTRAINT OperationSummaryTab_pkey PRIMARY KEY (operation_instance_id)
);

SELECT drop_if_exists('archiveoperationsummarytab');
CREATE TABLE ArchiveOperationSummaryTab
(
  archive_operation_summary_id bigserial NOT NULL,
  operation_instance_id bigint NOT NULL,
  source_files int,
  target_files int,
  source_size bigint,
  target_size bigint,
  source_records bigint,
  target_records bigint,
  total_transformation_time bigint,
  CONSTRAINT ArchiveOperationSummaryTab_pkey PRIMARY KEY (archive_operation_summary_id)
);
CREATE INDEX ArchiveOperationSummaryTab_Idx_operation_instance_id ON ArchiveOperationSummaryTab(operation_instance_id);

SELECT drop_if_exists('pluginstab');
CREATE TABLE PluginsTab
(
  PluginId int PRIMARY KEY,
  PluginName varchar(1024) NOT NULL,
  PluginPath varchar(512) NOT NULL
); 
COMMENT ON TABLE PluginsTab IS 'Description of plugin.';

SELECT drop_if_exists('projectbackuptab');
CREATE TABLE ProjectBackupTab
(
  ProjectBackup_Id			serial PRIMARY KEY,
  InterchangeProject_Guid	varchar(36) NOT NULL,
  UploadedBy_User_Guid		varchar(36) NOT NULL,
  UploadTime				timestamp NOT NULL,
  FileSize					bigint NOT NULL,
  Tag						text,
  UploadedFilename			varchar(1024) NOT NULL
);
CREATE INDEX ProjectBackupTab_InterchangeProject_Guid_IDX
ON ProjectBackupTab (InterchangeProject_Guid);
CREATE INDEX ProjBackupTab_InterchangeProject_Guid_UploadedBy_User_Guid_IDX
ON ProjectBackupTab (InterchangeProject_Guid, UploadedBy_User_Guid);

SELECT drop_if_exists('pipelineplugininstancetab');
CREATE TABLE PipelinePluginInstanceTab
(
  PipelinePluginInstanceId int PRIMARY KEY,
  PluginId int NOT NULL,
  EntityTypeId int NOT NULL,
  EntityId int NOT NULL
); 
COMMENT ON TABLE PipelinePluginInstanceTab IS 'Keeps track of for what operations, sources, targets, etc. Pipeline Plugins should be invoked.';

CREATE INDEX PipelinePluginInstanceTab_Entity_idx ON PipelinePluginInstanceTab (EntityTypeId, EntityId);

SELECT drop_if_exists('pipelinepluginpositiontab');
CREATE TABLE PipelinePluginPositionTab
(
  PipelinePluginPositionId int PRIMARY KEY,
  PluginId int NOT NULL,
  PipelinePluginInstanceId int NOT NULL,
  PipelineEntityTypeId int NOT NULL,
  RelativePositionId int DEFAULT 0
); 
COMMENT ON COLUMN PipelinePluginPositionTab.PipelineEntityTypeId IS 'The position in the ETLForge pipeline where this Pipeline Plugin will be called.';
COMMENT ON COLUMN PipelinePluginPositionTab.RelativePositionId IS 'Where Pipeline Plugin will be called relative to the entity type specified. (Pre, post, etc. depending on the entity type)';

CREATE INDEX PipelinePluginPositionTab_PipelinePluginEntityTypeId_idx ON PipelinePluginPositionTab (PipelineEntityTypeId);

SELECT drop_if_exists('salesforceconnectorstab');
CREATE TABLE SalesforceConnectorsTab
(
  SalesforceConnector_Id int NOT NULL,
  SalesforceConnector_Name varchar(256),
  Api_Version varchar(50),
  Server_Host varchar(1024),
  User_Name varchar(256),
  Salesforce_Password varchar(256),
  Security_Token varchar(256),
  Sandbox smallint,
  auth_type varchar(32) NOT NULL DEFAULT 'basicAuth',
  instance_url varchar(1024),
  client_id text,
  client_secret text,
  CONSTRAINT SalesforceConnectorsTab_pkey PRIMARY KEY (SalesforceConnector_Id)
);

SELECT drop_if_exists('salesforceconnectorpropertiestab');
CREATE TABLE SalesforceConnectorPropertiesTab
(
  SalesforceConnectorProperties_Id serial PRIMARY KEY,
  SalesforceConnector_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX SalesforceConnectorPropertiesTab_SalesforceConnector_Id_IDX ON SalesforceConnectorPropertiesTab (SalesforceConnector_Id);

SELECT drop_if_exists('salesforcequeriestab');
CREATE TABLE salesforcequeriestab
(
  salesforcequery_id integer NOT NULL,
  salesforcequery_name character varying(256),
  query_string text,
  salesforce_object text,
  operation_id integer,
  CONSTRAINT salesforcequeriestab_pkey PRIMARY KEY (salesforcequery_id)
);

SELECT drop_if_exists('salesforcequerypropertiestab');
CREATE TABLE SalesforceQueryPropertiesTab
(
  SalesforceQueryProperties_Id serial PRIMARY KEY,
  SalesforceQuery_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX SalesforceQueryPropertiesTab_SalesforceQuery_Id_IDX ON SalesforceQueryPropertiesTab (SalesforceQuery_Id);

SELECT drop_if_exists('salesforceupsertstab');
CREATE TABLE salesforceupsertstab
(
  salesforceupsert_id integer NOT NULL,
  salesforceupsert_name character varying(256),
  salesforce_object text,
  operation_id integer,
  truncate_fields boolean,
  trigger_email boolean,
  debug_mode text,
  external_id_field text,
  CONSTRAINT salesforceupsertstab_pkey PRIMARY KEY (salesforceupsert_id)
);

SELECT drop_if_exists('salesforceupsertpropertiestab');
CREATE TABLE SalesforceUpsertPropertiesTab
(
  SalesforceUpsertProperties_Id serial PRIMARY KEY,
  SalesforceUpsert_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX SalesforceUpsertPropertiesTab_SalesforceUpsert_Id_IDX ON SalesforceUpsertPropertiesTab (SalesforceUpsert_Id);

SELECT drop_if_exists('salesforcecreatestab');
CREATE TABLE salesforcecreatestab
(
  salesforcecreate_id integer NOT NULL,
  salesforcecreate_name character varying(256),
  salesforce_object text,
  operation_id integer,
  truncate_fields boolean,
  trigger_email boolean,
  debug_mode text,
  CONSTRAINT salesforcecreatestab_pkey PRIMARY KEY (salesforcecreate_id)
);

SELECT drop_if_exists('salesforcecreatepropertiestab');
CREATE TABLE SalesforceCreatePropertiesTab
(
  SalesforceCreateProperties_Id serial PRIMARY KEY,
  SalesforceCreate_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX SalesforceCreatePropertiesTab_SalesforceCreate_Id_IDX ON SalesforceCreatePropertiesTab (SalesforceCreate_Id);

SELECT drop_if_exists('salesforceupdatestab');
CREATE TABLE salesforceupdatestab
(
  salesforceupdate_id integer NOT NULL,
  salesforceupdate_name character varying(256),
  salesforce_object text,
  operation_id integer,
  truncate_fields boolean,
  trigger_email boolean,
  debug_mode text,
  CONSTRAINT salesforceupdatestab_pkey PRIMARY KEY (salesforceupdate_id)
);

SELECT drop_if_exists('salesforceupdatepropertiestab');
CREATE TABLE SalesforceUpdatePropertiesTab
(
  SalesforceUpdateProperties_Id serial PRIMARY KEY,
  SalesforceUpdate_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX SalesforceUpdatePropertiesTab_SalesforceUpdate_Id_IDX ON SalesforceUpdatePropertiesTab (SalesforceUpdate_Id);

SELECT drop_if_exists('salesforcedeletestab');
CREATE TABLE salesforcedeletestab
(
  salesforcedelete_id integer NOT NULL,
  salesforcedelete_name character varying(256),
  operation_id integer,
  truncate_fields boolean,
  trigger_email boolean,
  debug_mode text,
  CONSTRAINT salesforcedeletestab_pkey PRIMARY KEY (salesforcedelete_id)
);

SELECT drop_if_exists('salesforcedeletepropertiestab');
CREATE TABLE SalesforceDeletePropertiesTab
(
  SalesforceDeleteProperties_Id serial PRIMARY KEY,
  SalesforceDelete_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX SalesforceDeletePropertiesTab_SalesforceDelete_Id_IDX ON SalesforceDeletePropertiesTab (SalesforceDelete_Id);

SELECT drop_if_exists('salesforceoperationstatustab');
CREATE TABLE SalesforceOperationStatusTab (
  Operation_ID int NOT NULL,
  Operation_Instance_ID bigint NOT NULL,
  Source_File_Index integer,
  Source_File character varying(1024),
  Start_Time timestamp without time zone,
  End_Time timestamp without time zone,
  Records bigint NOT NULL,
  SuccessRecords bigint NOT NULL DEFAULT 0,
  FailureRecords bigint NOT NULL DEFAULT 0,
  Message 	character varying(1024),
  CONSTRAINT SalesforceOperationStatusTab_pkey PRIMARY KEY (Operation_Instance_ID,Source_File_Index)
);


SELECT drop_if_exists('salesforcebulkloadstab');
CREATE TABLE salesforcebulkloadstab
(
  salesforcebulkload_id integer NOT NULL,
  salesforcebulkload_name character varying(256),
  operation_id integer,
  salesforce_object text,
  action text,
  external_id text,
  source_header text,
  source_column_indices text,
  target_fields text,
  recs_per_batch integer,
  chars_per_batch integer,
  timezone text,
  date_fmt text,
  datetime_fmt text,
  CONSTRAINT salesforcebulkloadstab_pkey PRIMARY KEY (salesforcebulkload_id)
);

SELECT drop_if_exists('salesforcebulkloadpropertiestab');
CREATE TABLE SalesforceBulkLoadPropertiesTab
(
  SalesforceBulkLoadProperties_Id serial PRIMARY KEY,
  SalesforceBulkLoad_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX SalesforceBulkLoadPropertiesTab_SalesforceBulkLoad_Id_IDX ON SalesforceBulkLoadPropertiesTab (SalesforceBulkLoad_Id);

--Sap
SELECT drop_if_exists(lower('SapEndpointsTab'));
CREATE TABLE SapEndpointsTab
(
  SapEndpoint_Id 			int NOT NULL,
  SapEndpoint_Name 			varchar(256) NOT NULL,
  SapHost 					varchar(1024) NOT NULL,
  SapUser					varchar(256),
  SapPassword 				varchar(256),
  CONSTRAINT SapEndpointsTab_pkey PRIMARY KEY (SapEndpoint_Id)
);

SELECT drop_if_exists(lower('SapEndpointPropertiesTab'));
CREATE TABLE SapEndpointPropertiesTab
(
  SapEndpointProperties_Id serial PRIMARY KEY,
  SapEndpoint_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX SapEndpointPropertiesTab_SapEndpoint_Id_IDX ON SapEndpointPropertiesTab (SapEndpoint_Id);

SELECT drop_if_exists(lower('SapFunctionsTab'));
CREATE TABLE SapFunctionsTab
(
	SapFunction_Id					int NOT NULL,
	SapFunction_Name				varchar(256) NOT NULL,
	SapEndpoint_Id					int NOT NULL,
	SapType         				varchar(256) NOT NULL,
	MessageTypeName					varchar(1024), --IDOC only
	MessageTypeDescription      	varchar(1024),
	FunctionModuleName     			varchar(1024) NOT NULL, --IDOC type
	FunctionModuleDescription     	varchar(1024),
	InputXsdFilename          		varchar(2048),
	InputXsdRoot              		varchar(2048),
	OutputXsdFilename          		varchar(2048),
	OutputXsdRoot              		varchar(2048),
	CONSTRAINT SapFunctionsTab_pkey PRIMARY KEY (SapFunction_Id)
);
--AutoDesk
SELECT drop_if_exists(lower('AutoDeskEndpointsTab'));
CREATE TABLE AutoDeskEndpointsTab
(
  AutoDeskEndpoint_Id 			int NOT NULL,
  AutoDeskEndpoint_Name 			varchar(256) NOT NULL,
  AutoDeskHost 					varchar(1024) NOT NULL,
  AutoDeskUser					varchar(256),
  AutoDeskPassword 				varchar(256),
  CONSTRAINT AutoDeskEndpointsTab_pkey PRIMARY KEY (AutoDeskEndpoint_Id)
);

SELECT drop_if_exists(lower('AutoDeskEndpointPropertiesTab'));
CREATE TABLE AutoDeskEndpointPropertiesTab
(
  AutoDeskEndpointProperties_Id serial PRIMARY KEY,
  AutoDeskEndpoint_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX AutoDeskEndpointPropertiesTab_AutoDeskEndpoint_Id_IDX ON AutoDeskEndpointPropertiesTab (AutoDeskEndpoint_Id);
SELECT drop_if_exists(lower('AutoDeskFunctionsTab'));
CREATE TABLE AutoDeskFunctionsTab
(
	AutoDeskFunction_Id					int NOT NULL,
	AutoDeskFunction_Name				varchar(256) NOT NULL,
	AutoDeskEndpoint_Id					int NOT NULL,
	AutoDeskOperation					varchar(1024), 
	AutoDeskWorkspace      				varchar(1024),
	InputXsdFilename          		varchar(2048),
	InputXsdRoot              		varchar(2048),
	OutputXsdFilename          		varchar(2048),
	OutputXsdRoot              		varchar(2048),
	CONSTRAINT AutoDeskFunctionsTab_pkey PRIMARY KEY (AutoDeskFunction_Id)
);
--Ebs
SELECT drop_if_exists(lower('EbsEndpointsTab'));
CREATE TABLE EbsEndpointsTab
(
  EbsEndpoint_Id 			int NOT NULL,
  EbsEndpoint_Name 			varchar(256) NOT NULL,
  EbsHost 					varchar(1024),
  EbsUser					varchar(256),
  EbsPassword 				varchar(256),
  CONSTRAINT EbsEndpointsTab_pkey PRIMARY KEY (EbsEndpoint_Id)
);

SELECT drop_if_exists(lower('EbsEndpointPropertiesTab'));
CREATE TABLE EbsEndpointPropertiesTab
(
  EbsEndpointProperties_Id serial PRIMARY KEY,
  EbsEndpoint_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX EbsEndpointPropertiesTab_EbsEndpoint_Id_IDX ON EbsEndpointPropertiesTab (EbsEndpoint_Id);
SELECT drop_if_exists(lower('EbsFunctionsTab'));
CREATE TABLE EbsFunctionsTab
(
	EbsFunction_Id					int NOT NULL,
	EbsFunction_Name				varchar(256) NOT NULL,
	EbsEndpoint_Id					int NOT NULL,
	EbsCatalog						varchar(1024),
	EbsCatalogDescription			varchar(1024),
	EbsProgram						varchar(1024),
	EbsProgramDescription			varchar(1024),
	InputXsdFilename          		varchar(2048),
	InputXsdRoot              		varchar(2048),
	OutputXsdFilename          		varchar(2048),
	OutputXsdRoot              		varchar(2048),
	CONSTRAINT EbsFunctionsTab_pkey PRIMARY KEY (EbsFunction_Id)
);

--MSAX
SELECT drop_if_exists(lower('MSAXEndpointsTab'));
CREATE TABLE MSAXEndpointsTab
(
  MSAXEndpoint_Id 			int NOT NULL,
  MSAXEndpoint_Name 			varchar(256) NOT NULL,
  MSAXHost 					varchar(1024),
  MSAXUser					varchar(256) NOT NULL,
  MSAXPassword 				varchar(256),
  CONSTRAINT MSAXEndpointsTab_pkey PRIMARY KEY (MSAXEndpoint_Id)
);

SELECT drop_if_exists(lower('MSAXEndpointPropertiesTab'));
CREATE TABLE MSAXEndpointPropertiesTab
(
  MSAXEndpointProperties_Id serial PRIMARY KEY,
  MSAXEndpoint_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX MSAXEndpointPropertiesTab_MSAXEndpoint_Id_IDX ON MSAXEndpointPropertiesTab (MSAXEndpoint_Id);

SELECT drop_if_exists(lower('MSAXFunctionsTab'));
CREATE TABLE MSAXFunctionsTab
(
	MSAXFunction_Id					int NOT NULL,
	MSAXFunction_Name				varchar(256) NOT NULL,
	MSAXEndpoint_Id					int NOT NULL,
	MSAXWsdlUrl						varchar(1024),
	MSAXServiceAndOperation			varchar(2048), 
	InputXsdFilename          		varchar(2048),
	InputXsdRoot              		varchar(2048),
	OutputXsdFilename          		varchar(2048),
	OutputXsdRoot              		varchar(2048),
	CONSTRAINT MSAXFunctionsTab_pkey PRIMARY KEY (MSAXFunction_Id)
);
--MSGP
SELECT drop_if_exists(lower('MSGPEndpointsTab'));
CREATE TABLE MSGPEndpointsTab
(
  MSGPEndpoint_Id 			int NOT NULL,
  MSGPEndpoint_Name 			varchar(256) NOT NULL,
  MSGPHost 					varchar(1024) NOT NULL,
  MSGPUser					varchar(256),
  MSGPPassword 				varchar(256),
  CONSTRAINT MSGPEndpointsTab_pkey PRIMARY KEY (MSGPEndpoint_Id)
);

SELECT drop_if_exists(lower('MSGPEndpointPropertiesTab'));
CREATE TABLE MSGPEndpointPropertiesTab
(
  MSGPEndpointProperties_Id serial PRIMARY KEY,
  MSGPEndpoint_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX MSGPEndpointPropertiesTab_MSGPEndpoint_Id_IDX ON MSGPEndpointPropertiesTab (MSGPEndpoint_Id);
--The connector may be different for connectors
SELECT drop_if_exists(lower('MSGPFunctionsTab'));
CREATE TABLE MSGPFunctionsTab
(
	MSGPFunction_Id					int NOT NULL,
	MSGPFunction_Name				varchar(256) NOT NULL,
	MSGPEndpoint_Id					int NOT NULL,
	MSGPOperation					varchar(1024), 
	MSGPModule      				varchar(1024),
	InputXsdFilename          		varchar(2048),
	InputXsdRoot              		varchar(2048),
	OutputXsdFilename          		varchar(2048),
	OutputXsdRoot              		varchar(2048),
	CONSTRAINT MSGPFunctionsTab_pkey PRIMARY KEY (MSGPFunction_Id)
);

--MSCrm
SELECT drop_if_exists(lower('MSCrmEndpointsTab'));
CREATE TABLE MSCrmEndpointsTab
(
  MSCrmEndpoint_Id 			int NOT NULL,
  MSCrmEndpoint_Name 			varchar(256) NOT NULL,
  MSCrmHost 					varchar(1024) NOT NULL,
  MSCrmUser					varchar(256),
  MSCrmPassword 				varchar(256),
  CONSTRAINT MSCrmEndpointsTab_pkey PRIMARY KEY (MSCrmEndpoint_Id)
);

SELECT drop_if_exists(lower('MSCrmEndpointPropertiesTab'));
CREATE TABLE MSCrmEndpointPropertiesTab
(
  MSCrmEndpointProperties_Id serial PRIMARY KEY,
  MSCrmEndpoint_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX MSCrmEndpointPropertiesTab_MSCrmEndpoint_Id_IDX ON MSCrmEndpointPropertiesTab (MSCrmEndpoint_Id);

SELECT drop_if_exists(lower('MSCrmFunctionsTab'));
CREATE TABLE MSCrmFunctionsTab
(
	MSCrmFunction_Id					int NOT NULL,
	MSCrmFunction_Name				varchar(256) NOT NULL,
	MSCrmEndpoint_Id					int NOT NULL,
	MSCrmOperation					varchar(1024), 
	MSCrmObject      				varchar(1024),
	MSCrmQueryXml 					text,
	MSCrmUpsertKeys    				varchar(8000),
	InputXsdFilename          		varchar(2048),
	InputXsdRoot              		varchar(2048),
	OutputXsdFilename          		varchar(2048),
	OutputXsdRoot              		varchar(2048),
	CONSTRAINT MSCrmFunctionsTab_pkey PRIMARY KEY (MSCrmFunction_Id)
);

DROP TABLE IF EXISTS SQLScriptTab CASCADE;
CREATE TABLE SQLScriptTab (
	SQLID	serial	PRIMARY KEY,
	Transformation_ID	int	NULL,
	SQLScript	text	NULL,
	SQLScriptType	int	NULL,
	SQLWhere	text	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);
--CreateDb.sql
--NetSuite
SELECT drop_if_exists(lower('NetSuiteEndpointsTab'));
CREATE TABLE NetSuiteEndpointsTab
(
  NetSuiteEndpoint_Id 			int NOT NULL,
  NetSuiteEndpoint_Name 			varchar(1024) NOT NULL,
  NetSuiteHost 					varchar(1024),
  NetSuiteUser					varchar(1024),
  NetSuitePassword 				varchar(1024),
  CONSTRAINT NetSuiteEndpointsTab_pkey PRIMARY KEY (NetSuiteEndpoint_Id)
);

SELECT drop_if_exists(lower('NetSuiteEndpointPropertiesTab'));
CREATE TABLE NetSuiteEndpointPropertiesTab
(
  NetSuiteEndpointProperties_Id serial PRIMARY KEY,
  NetSuiteEndpoint_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX NetSuiteEndpointPropertiesTab_NetSuiteEndpoint_Id_IDX ON NetSuiteEndpointPropertiesTab (NetSuiteEndpoint_Id);
SELECT drop_if_exists(lower('NetSuiteFunctionsTab'));
CREATE TABLE NetSuiteFunctionsTab
(
	NetSuiteFunction_Id					int NOT NULL,
	NetSuiteFunction_Name				varchar(256) NOT NULL,
	NetSuiteEndpoint_Id					int NOT NULL,
	NetSuiteOperation					varchar(1024), 
	NetSuiteObject      				varchar(1024),
	NetSuiteQueryXml      				text,
	NetSuitePageSize      				varchar(16),
	NetSuiteAdvancedSearch   			text,
	InputXsdFilename          		varchar(2048),
	InputXsdRoot              		varchar(2048),
	OutputXsdFilename          		varchar(2048),
	OutputXsdRoot              		varchar(2048),
	CONSTRAINT NetSuiteFunctionsTab_pkey PRIMARY KEY (NetSuiteFunction_Id)
);

SELECT drop_if_exists('schedulepropertiestab');
CREATE TABLE SchedulePropertiesTab
(
  ScheduleProperties_Id serial PRIMARY KEY,
  Schedule_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX SchedulePropertiesTab_Schedule_Id_IDX ON SchedulePropertiesTab (Schedule_Id);

DROP TABLE IF EXISTS ScheduleTab CASCADE;
CREATE TABLE ScheduleTab (
	Schedule_ID	int	PRIMARY KEY,
	Schedule_Name	varchar(256)	NULL,
	Occurs	int	NULL,
	Daily	int	NULL,
	Weekly	int	NULL,
	Mon	int	NULL,
	Tue	int	NULL,
	Wed	int	NULL,
	Thu	int	NULL,
	Fri	int	NULL,
	Sat	int	NULL,
	Sun	int	NULL,
	Monthly	int	NULL,
	Monthly_A_Day			int	NULL,
	Monthly_A_of_every_X_month	int	NULL,
	Monthly_B_Num			int	NULL,
	Monthly_B_Day			int	NULL,
	Monthly_B_of_every_x_month	int	NULL,
	Frequency			int	NULL,
	Frequency_once_time		varchar(50)	NULL,
	Frequency_every_num		int	NULL,
	Frequency_every_HM		int	NULL,
	Frequency_Start_time		varchar(50)	NULL,
	Frequency_End_time		varchar(50)	NULL,
	Duration_Start_date		varchar(50)	NULL,
	Duration_End_date		varchar(50)	NULL,
	Duration_YesNo_END		int	NULL,
	Priority			int	NULL,
	InsertTime			timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp		NULL	DEFAULT NOW()
);

SELECT drop_if_exists('scriptpropertiestab');
CREATE TABLE ScriptPropertiesTab
(
  ScriptProperties_Id serial PRIMARY KEY,
  Script_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX ScriptPropertiesTab_Script_Id_IDX ON ScriptPropertiesTab (Script_Id);

DROP TABLE IF EXISTS ScriptsTab CASCADE;
CREATE TABLE ScriptsTab (
  Script_ID		int	PRIMARY KEY,
  Script_Name	varchar(256) NULL,
  Script_Text	text NOT NULL
);

SELECT drop_if_exists('sourcepropertiestab');
CREATE TABLE SourcePropertiesTab
(
  SourceProperties_Id serial PRIMARY KEY,
  Source_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX SourcePropertiesTab_Source_Id_IDX ON SourcePropertiesTab (Source_Id);

DROP TABLE IF EXISTS SourcesTab CASCADE;
CREATE TABLE SourcesTab (
	Source_ID	int	PRIMARY KEY,
	Source_Name	varchar(256)	NULL,
	Source_Type_ID	int	NULL,
	Document_ID	int	NULL,
	Locator	varchar(1024)	NULL,
	Path	varchar(1024)	NULL,
	File_Name	varchar(512)	NULL,
	Timeout	int	NULL,
	Login	varchar(256)	NULL,
	Password	varchar(1024)	NULL,
	Filter	varchar(50)	NULL,
	Ignore_First	int	NULL	DEFAULT 0,
	Ignore_Last	int	NULL	DEFAULT 0,
	DSN	varchar(256)	NULL,
	Transfer_Type	int	NULL,
	Encrypt_text	varchar(50)	NULL,
	Encrypt_choice	int	NULL,
	Driver	varchar(256)	NULL,
	DriverType	int	NULL,
	DBType_Id	int	NULL,
	Port	int	NULL,
	Passive_Mode	smallint	NULL,
	DBName	varchar(256)	NULL,
	ConnectString	varchar(1024)	NULL,
	RenameFileTo	varchar(256)	NULL,
	Secured	int	NULL,
	HTTPURL	text	NULL,
	CertIFicateName	varchar(256)	NULL,
	Optional_ConnectString	varchar(256)	NULL,
	ODBC_UNC_UserID	varchar(256)	NULL,
	ODBC_UNC_Password	varchar(1024)	NULL,
	Http_Verb	int	NULL	DEFAULT 0,
	Jms_Message_Selector	text NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime		timestamp	NULL	DEFAULT NOW()
);

SELECT drop_if_exists('targetpropertiestab');
CREATE TABLE TargetPropertiesTab
(
  TargetProperties_Id serial PRIMARY KEY,
  Target_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX TargetPropertiesTab_Target_Id_IDX ON TargetPropertiesTab (Target_Id);

DROP TABLE IF EXISTS TargetsTab CASCADE;
CREATE TABLE TargetsTab (
	Target_ID	int	PRIMARY KEY,
	Target_Name	varchar(256)	NULL,
	Source_Type_ID	int	NULL,
	Document_ID	int	NULL,
	Locator	varchar(1024)	NULL,
	Path	varchar(1024)	NULL,
	File_Name	varchar(512)	NULL,
	Timeout	int	NULL,
	Login	varchar(256)	NULL,
	Password	varchar(1024)	NULL,
	SMTP_Message	int	NULL,
	DSN	varchar(256)	NULL,
	Transfer_Type	int	NULL,
	Encrypt_text	varchar(50)	NULL,
	Encrypt_choice	int	NULL,
	Driver	varchar(256)	NULL,
	DriverType	int	NULL,
	User_Client	varchar(25)	NULL,
	User_Password	varchar(1024)	NULL,
	User_ID	varchar(50)	NULL,
	Developer_Password	varchar(1024)	NULL,
	Developer_Client	varchar(25)	NULL,
	Developer_ID	varchar(50)	NULL,
	System_Number	varchar(25)	NULL,
	App_Server	varchar(50)	NULL,
	Lang	varchar(2)	NULL,
	Port	int	NULL,
	Passive_Mode	smallint	NULL,
	DBName	varchar(256)	NULL,
	Target_Target_ID	int	NULL,
	ConnectString	varchar(1024)	NULL,
	Secured	int	NULL,
	HTTPURL	text	NULL,
	CertIFicateName	varchar(256)	NULL,
	Optional_ConnectString	varchar(256)	NULL,
	ODBC_UNC_UserID	varchar(256)	NULL,
	ODBC_UNC_Password	varchar(1024)	NULL,
	Http_Verb	int	NULL	DEFAULT 2,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime		timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS Tran_RelationsTab CASCADE;
CREATE TABLE Tran_RelationsTab (
	Relation_ID	serial	NOT NULL,
	Transformation_ID	int	NOT NULL,
	Doc_Type	varchar(2)	NOT NULL,
	Parent_Name	varchar(256)	NULL,
	Parent_Field	varchar(256)	NULL,
	Child_Name	varchar(256)	NULL,
	Child_Field	varchar(256)	NULL,
	Outer_Join	smallint	NULL	DEFAULT 0,
	OneToOne	int	NULL	DEFAULT 0,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW(),
	PRIMARY KEY(Relation_ID, Transformation_ID)
);

DROP TABLE IF EXISTS Trans_FilterTab CASCADE;
CREATE TABLE Trans_FilterTab (
	Trans_Filter_ID	serial	PRIMARY KEY,
	Transformation_ID	int	NULL,
	Group_ID	int	NULL,
	TName	varchar(256)	NULL,
	CName	varchar(256)	NULL,
	Condition	text	NULL,
	SortBy	varchar(50)	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS TransformationChainLinksTab CASCADE;
CREATE TABLE TransformationChainLinksTab (
	TransformationChainLink_ID	serial 	PRIMARY KEY,
	Operation_ID			int	NULL,
	Transformation_ID		int	NOT NULL,
	NextTransformation_ID		int	NOT NULL
);

DROP TABLE IF EXISTS TransformationDataStore CASCADE;
CREATE TABLE TransformationDataStore (
	Guid varchar(36) PRIMARY KEY,
	Started timestamp NULL DEFAULT NOW()
);

DROP TABLE IF EXISTS TransformationDataStoreInstance CASCADE;
CREATE TABLE TransformationDataStoreInstance (
	id serial PRIMARY KEY,
	Guid varchar(36),
	OperationId int,
	TransformationId int,
	OriginalSourceNumber int,
	ChunkNumber int,
	SourceFile varchar(1024),
	LinkFile varchar(1024),
	TargetFile varchar(1024),
	Added timestamp NULL DEFAULT NOW()
);

DROP TABLE IF EXISTS TransformationFunctionTab CASCADE;
CREATE TABLE TransformationFunctionTab (
	Transformation_ID	int	PRIMARY KEY,
	Function_ID	int	NOT NULL
);

DROP TABLE IF EXISTS Transformation_DefinitionsTab CASCADE;
CREATE TABLE Transformation_DefinitionsTab (
	TranDiffID	serial	PRIMARY KEY,
	Transformation_ID	int	NULL,
	Target_Field_ID	varchar(900)	NULL,
	Transformation	text	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

SELECT drop_if_exists('transformationpropertiestab');
CREATE TABLE TransformationPropertiesTab
(
  TransformationProperties_Id serial PRIMARY KEY,
  Transformation_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX TransformationPropertiesTab_Transformation_Id_IDX ON TransformationPropertiesTab (Transformation_Id);

DROP TABLE IF EXISTS TransformationsTab_New CASCADE;
CREATE TABLE TransformationsTab_New (
	Transformation_ID	int	PRIMARY KEY,
	Transformation_Name	varchar(256)	NULL,
	Source_Type_ID	int	NULL,
	Target_Type_ID	int	NULL,
	Source_Text_Doc	varchar(50)	NULL,
	Source_EDI	varchar(255)	NULL,
	Source_SourceID	varchar(50)	NULL,
	Target_Text_Doc	varchar(50)	NULL,
	Target_EDI	varchar(255)	NULL,
	Target_TargetID	varchar(50)	NULL,
	Source_XML	varchar(255)	NULL,
	Target_XML	varchar(255)	NULL,
	NatureOfSource	varchar(10)	NULL,
	NatureOfTarget	varchar(10)	NULL,
	Insert_Mode	smallint	NULL	DEFAULT 1,
	Target_DTD_URL	varchar(500)	NULL,
	Source_EDI_Default	int	NULL,
	Target_EDI_Default	int	NULL,
	IsValid	int	NULL,
	SourceDTD_Root	varchar(512)	NULL,
	TargetDTD_Root	varchar(512)	NULL,
	SourceJmsMessageId	int	NULL,
	TargetJmsMessageId	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS UploadedFilesTab;
CREATE TABLE UploadedFilesTab
(
	FileId int PRIMARY KEY, 
	FileTypeId int, 
	Guid varchar(36),
	Path varchar(1024) NOT NULL, 
	FileName varchar(256) NOT NULL, 
	MD5 varchar(32) NOT NULL, 
	ModifiedDate timestamp NOT NULL
);

DROP TABLE IF EXISTS User_GroupTab CASCADE;
CREATE TABLE User_GroupTab (
	User_GroupID	serial	PRIMARY KEY,
	User_ID	int	NULL,
	Group_ID	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS UsersTab CASCADE;
CREATE TABLE UsersTab (
	User_ID	int	PRIMARY KEY,
	First_Name	varchar(256)	NULL,
	Last_Name	varchar(256)	NULL,
	Login		varchar(64)		NULL,
	Password	varchar(2048)	NULL,
	Email		varchar(256)	NULL,
	Access_Level int			NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime		timestamp	NULL	DEFAULT NOW()
);

SELECT drop_if_exists('webservicepropertiestab');
CREATE TABLE WebservicePropertiesTab
(
  WebserviceProperties_Id serial PRIMARY KEY,
  Webservice_Id int NOT NULL,
  KeyName varchar(256) NOT NULL,
  KeyValue text NOT NULL
);
CREATE INDEX WebservicePropertiesTab_Webservice_Id_IDX ON WebservicePropertiesTab (Webservice_Id);

DROP TABLE IF EXISTS WebServicesTab CASCADE;
CREATE TABLE WebServicesTab (
	WebService_ID	serial	PRIMARY KEY,
	Wsdl_ID	int	NOT NULL,
	WebService_Name	varchar(256)	NOT NULL,
	WebService_Url	text	NULL,
	Service	varchar(256)	NULL,
	Port	varchar(256)	NULL,
	Binding	varchar(256)	NULL,
	Operation	varchar(256)	NULL,
	SoapVersion	int	NULL	DEFAULT 0,
	SoapAction	text	NULL,
	SoapStyle	varchar(256)	NULL,
	SoapBodyUse	varchar(256)	NULL,
	InputXSDFileName	varchar(512)	NULL,
	OutputXSDFileName	varchar(512)	NULL,
	HttpUserName	varchar(256)	NULL,
	HttpPassword	varchar(1024)	NULL,
	Certificate	varchar(100)	NULL,
	TargetNamespace	text	NULL,
	OtherNamespaces	text	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS WsdlDependenciesTab CASCADE;
CREATE TABLE WsdlDependenciesTab
(
	Wsdl_Dependency_Id	serial PRIMARY KEY,
	Parent_Wsdl_Id		integer NOT NULL,
	Child_Wsdl_Id		integer NOT NULL
);

DROP TABLE IF EXISTS WsdlTab CASCADE;
CREATE TABLE WsdlTab (
	Wsdl_ID			int		PRIMARY KEY,
	WsdlName		varchar(1024)	NOT NULL,
	WsdlUrl			text	NOT NULL DEFAULT '',
	WsdlPath		varchar(1024)	NOT NULL,
	Status			int		NOT NULL DEFAULT 0,
	ApplicationMessage	varchar(4096)	NULL,
	UserComment		varchar(4096)	NULL,
	WsdlType		int		NOT NULL DEFAULT 0
);

DROP TABLE IF EXISTS XMLDtdLinks CASCADE;
CREATE TABLE XMLDtdLinks (
	XMLDtdLinksID	serial	PRIMARY KEY,
	Parent_DTD	int	NULL,
	Child_DTD	int	NULL,
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);
SELECT setval('xmldtdlinks_xmldtdlinksid_seq', 1000);

DROP TABLE IF EXISTS XMLDtdTab CASCADE;
CREATE TABLE XMLDtdTab (
	XMLDtdID	serial	PRIMARY KEY,
	XML_ID	int	NOT NULL,
	XML_FileName	varchar(128)	NULL,
	XML_Path	varchar(512)	NULL	DEFAULT '',
	InsertTime	timestamp	NULL	DEFAULT NOW(),
	ModTime	timestamp	NULL	DEFAULT NOW()
);

DROP TABLE IF EXISTS XferHist CASCADE;
CREATE TABLE XferHist (
	ID		serial	PRIMARY KEY,
	DirEntry	varchar(255)	NULL,
	FileName	text	NULL,
	OperationID	int	NULL,
	FileSize	int	NULL,
	FileMonth	int	NULL,
	FileDay	int	NULL,
	FileTimeOrYear	int	NULL,
	XferDateTime	timestamp	NULL,
	ProgressStatus	int	NULL,
	RetryCount	int	NULL,
	LocalFileName	text	NULL,
	InsertTime	timestamp	NULL,
	ModTime	timestamp	NULL
);
CREATE INDEX XferHist_Operation_ID_IDX ON XferHist (OperationID);
-- The XferHist_Compound_IDX index helps with the queries from the Operantion Engine.
CREATE INDEX XferHist_Compound_IDX ON XferHist (FileName, OperationID, FileSize, FileMonth, FileDay, FileTimeOrYear);

DROP TABLE IF EXISTS ArchiveXferHist CASCADE;
CREATE TABLE ArchiveXferHist (
	ID		serial	PRIMARY KEY,
	DirEntry	varchar(255)	NULL,
	FileName	text	NULL,
	OperationID	int	NULL,
	FileSize	int	NULL,
	FileMonth	int	NULL,
	FileDay	int	NULL,
	FileTimeOrYear	int	NULL,
	XferDateTime	timestamp	NULL,
	ProgressStatus	int	NULL,
	RetryCount	int	NULL,
	LocalFileName	text	NULL,
	InsertTime	timestamp	NULL,
	ModTime	timestamp	NULL
);

SELECT drop_if_exists('bulkload_batch_tab');
CREATE TABLE bulkload_batch_tab
(
  operation_instance_id bigint NOT NULL,
  last_operation_instance_id bigint,
  bulkload_id integer NOT NULL,
  batch_index integer NOT NULL,
  batch_id character varying(256) NOT NULL,
  start_pos bigint NOT NULL,
  end_pos bigint NOT NULL,
  records bigint NOT NULL,
  time_sent timestamp without time zone,
  processedrecords bigint,
  successrecords bigint,
  failurerecords bigint,
  status integer,
  statusMessage character varying(4096),
  CONSTRAINT bulkload_batch_tab_pkey PRIMARY KEY (operation_instance_id, batch_id)
);

SELECT drop_if_exists('bulkload_instance_tab');
CREATE TABLE bulkload_instance_tab
(
  operation_instance_id bigint NOT NULL,
  last_operation_instance_id bigint,
  bulkload_id integer NOT NULL,
  sfdc_job_id character varying(1024),
  start_time timestamp without time zone,
  end_time timestamp without time zone,
  filename character varying(1024),
  filecharencode character varying(128),
  file_size bigint,
  start_pos integer,
  status integer,
  CONSTRAINT bulkload_instance_tab_pkey PRIMARY KEY (operation_instance_id)
);

SELECT drop_if_exists('operation_progress_tab');
CREATE TABLE operation_progress_tab
(
  operation_instance_id bigint NOT NULL,
  source_file character varying(1024),
  source_file_index integer,
  number_of_source_files integer,
  section character varying(1024),
  message text,
  percentage_done integer DEFAULT -1,
  estimated_end_time timestamp without time zone,
  CONSTRAINT operation_progress_tab_pkey PRIMARY KEY (operation_instance_id)
);

-------------------- VIEWS

DROP VIEW IF EXISTS FIELDS1;
CREATE VIEW FIELDS1 AS
       SELECT Transformation_ID, KeyFields AS Source_DiffKeys FROM DBFieldsTab WHERE (KeyFieldsType =  1)
;


DROP VIEW IF EXISTS FIELDS2;
CREATE VIEW FIELDS2 AS
       SELECT Transformation_ID, KeyFields AS UpdateFields FROM DBFieldsTab WHERE (KeyFieldsType = 2)
;


DROP VIEW IF EXISTS FIELDS3;
CREATE VIEW FIELDS3 AS
       SELECT Transformation_ID, KeyFields AS Target_UpdateKeys FROM DBFieldsTab WHERE (KeyFieldsType = 3)
;


DROP VIEW IF EXISTS SQL1;
CREATE VIEW SQL1 AS
       SELECT Transformation_ID, SQLScript AS Source_SQL, SQLWhere AS Source_Where FROM SQLScriptTab WHERE (SQLScriptType = 1)
;


DROP VIEW IF EXISTS SQL2;
CREATE VIEW SQL2 AS
       SELECT Transformation_ID, SQLScript AS Target_SQL FROM SQLScriptTab WHERE (SQLScriptType = 2)
;


DROP VIEW IF EXISTS TABLES1;
CREATE VIEW TABLES1 AS  
       SELECT Transformation_ID, Tables AS Source_Tables FROM DBTablesTab WHERE (TablesType = 1)
;


DROP VIEW IF EXISTS TABLES2;
CREATE VIEW TABLES2 AS
       SELECT Transformation_ID, Tables AS Target_Tables FROM DBTablesTab WHERE (TablesType = 2)
;


DROP VIEW IF EXISTS WebServiceView2;
CREATE VIEW WebServiceView2
AS
SELECT     FunctionsTab.Function_ID, WebServicesTab.WebService_ID, WebServicesTab.WebService_Url, WebServicesTab.SoapAction, 
                      WebServicesTab.SoapStyle, WebServicesTab.SoapBodyUse, WebServicesTab.TargetNamespace, 
                      WebServicesTab.OtherNamespaces, WebServicesTab.InputXSDFileName, WebServicesTab.OutputXSDFileName, 
                      FunctionsTab.FunctionType_ID, WebServicesTab.HttpUserName, WebServicesTab.HttpPassword, 
                      CallRestrictionsTab.MinCallDelay_ms, CallRestrictionsTab.MaxSize_bytes, WebServicesTab.Wsdl_ID
FROM         FunctionsTab INNER JOIN
                      WebServicesTab ON FunctionsTab.ForeignKeyFunction_ID = WebServicesTab.WebService_ID LEFT OUTER JOIN
                      CallRestrictionsTab ON FunctionsTab.Function_ID = CallRestrictionsTab.Function_ID AND 
                      FunctionsTab.FunctionType_ID = CallRestrictionsTab.FunctionType_ID
WHERE     (FunctionsTab.FunctionType_ID = 1)
;


DROP VIEW IF EXISTS WebServicesView;
CREATE VIEW WebServicesView
AS
SELECT     WebServicesTab.*, 1 AS FunctionType_ID
FROM         WebServicesTab
;




DROP VIEW IF EXISTS TRANSFORMATIONSTAB;
CREATE VIEW TRANSFORMATIONSTAB
AS
SELECT     TransformationsTab_New.Transformation_ID, TransformationsTab_New.Transformation_Name, 
                      TransformationsTab_New.Source_Type_ID, TransformationsTab_New.Target_Type_ID, TransformationsTab_New.Source_Text_Doc, 
                      TransformationsTab_New.Source_EDI, '' AS Source_EDI_Ver, TransformationsTab_New.Source_SourceID, 
                      TransformationsTab_New.Target_Text_Doc, TransformationsTab_New.Target_EDI, '' AS Target_EDI_Ver, 
                      TransformationsTab_New.Target_TargetID, TransformationsTab_New.Source_XML, TransformationsTab_New.Target_XML, 
                      '' AS Source_DSN, '' AS Target_DSN, TABLES1.Source_Tables, TABLES2.Target_Tables, FIELDS2.UpdateFields, SQL1.Source_SQL, 
                      SQL2.Target_SQL, SUBSTRING(TransformationsTab_New.NatureOfSource, 1, 4) AS NatureOfSource, TransformationsTab_New.NatureOfTarget, 
                      SQL1.Source_Where, TransformationsTab_New.Insert_Mode, TransformationsTab_New.Target_DTD_URL, 
                      TransformationsTab_New.Source_EDI_Default, TransformationsTab_New.Target_EDI_Default, FIELDS3.Target_UpdateKeys, 
                      FIELDS1.Source_DiffKeys, TransformationsTab_New.IsValid, TransformationsTab_New.SourceDTD_Root, 
                      TransformationsTab_New.TargetDTD_Root, TransformationsTab_New.InsertTime, TransformationsTab_New.ModTime
FROM         TransformationsTab_New LEFT OUTER JOIN
                      FIELDS1 ON TransformationsTab_New.Transformation_ID = FIELDS1.Transformation_ID LEFT OUTER JOIN
                      FIELDS2 ON TransformationsTab_New.Transformation_ID = FIELDS2.Transformation_ID LEFT OUTER JOIN
                      FIELDS3 ON TransformationsTab_New.Transformation_ID = FIELDS3.Transformation_ID LEFT OUTER JOIN
                      SQL1 ON TransformationsTab_New.Transformation_ID = SQL1.Transformation_ID LEFT OUTER JOIN
                      SQL2 ON TransformationsTab_New.Transformation_ID = SQL2.Transformation_ID LEFT OUTER JOIN
                      TABLES1 ON TransformationsTab_New.Transformation_ID = TABLES1.Transformation_ID LEFT OUTER JOIN
                      TABLES2 ON TransformationsTab_New.Transformation_ID = TABLES2.Transformation_ID
;


DROP VIEW IF EXISTS TransformationTab_Appended;
CREATE VIEW TransformationTab_Appended
AS
SELECT     TransformationFunctionTab.Transformation_ID, FunctionsTab.Function_ID, FunctionTypesTab.FunctionType_Name, 
                      WebServicesView.InputXSDFileName, WebServicesView.OutputXSDFileName
FROM         FunctionsTab INNER JOIN
                      TransformationFunctionTab ON FunctionsTab.Function_ID = TransformationFunctionTab.Function_ID INNER JOIN
                      FunctionTypesTab ON FunctionsTab.FunctionType_ID = FunctionTypesTab.FunctionType_ID INNER JOIN
                      WebServicesView ON FunctionTypesTab.FunctionType_ID = WebServicesView.FunctionType_ID AND 
                      FunctionsTab.ForeignKeyFunction_ID = WebServicesView.WebService_ID
;

DROP VIEW IF EXISTS TRANSFORMATIONSTAB_V;
CREATE VIEW TRANSFORMATIONSTAB_V
AS
SELECT     TransformationsTab_New.Transformation_ID, TransformationsTab_New.Transformation_Name, 
                      TransformationsTab_New.Source_Type_ID, TransformationsTab_New.Target_Type_ID, TransformationsTab_New.Source_Text_Doc, 
                      TransformationsTab_New.Source_EDI, '' AS Source_EDI_Ver, TransformationsTab_New.Source_SourceID, 
                      TransformationsTab_New.Target_Text_Doc, TransformationsTab_New.Target_EDI, '' AS Target_EDI_Ver, 
                      TransformationsTab_New.Target_TargetID, TransformationsTab_New.Source_XML, TransformationsTab_New.Target_XML,
                      TransformationsTab_New.SourceJmsMessageId, TransformationsTab_New.TargetJmsMessageId,
                      '' AS Source_DSN, '' AS Target_DSN, TABLES1.Source_Tables, TABLES2.Target_Tables, FIELDS2.UpdateFields, SQL1.Source_SQL, 
                      SQL2.Target_SQL, TransformationsTab_New.NatureOfSource, TransformationsTab_New.NatureOfTarget, SQL1.Source_Where, 
                      TransformationsTab_New.Insert_Mode, TransformationsTab_New.Target_DTD_URL, TransformationsTab_New.Source_EDI_Default, 
                      TransformationsTab_New.Target_EDI_Default, FIELDS3.Target_UpdateKeys, FIELDS1.Source_DiffKeys, 
                      TransformationsTab_New.IsValid, TransformationsTab_New.SourceDTD_Root, TransformationsTab_New.TargetDTD_Root, 
                      TransformationTab_Appended.Function_ID, TransformationTab_Appended.InputXSDFileName, 
                      TransformationTab_Appended.OutputXSDFileName, TransformationTab_Appended.FunctionType_Name
FROM         TransformationsTab_New LEFT OUTER JOIN
                      TransformationTab_Appended ON 
                      TransformationsTab_New.Transformation_ID = TransformationTab_Appended.Transformation_ID LEFT OUTER JOIN
                      FIELDS1 ON TransformationsTab_New.Transformation_ID = FIELDS1.Transformation_ID LEFT OUTER JOIN
                      FIELDS2 ON TransformationsTab_New.Transformation_ID = FIELDS2.Transformation_ID LEFT OUTER JOIN
                      FIELDS3 ON TransformationsTab_New.Transformation_ID = FIELDS3.Transformation_ID LEFT OUTER JOIN
                      SQL1 ON TransformationsTab_New.Transformation_ID = SQL1.Transformation_ID LEFT OUTER JOIN
                      SQL2 ON TransformationsTab_New.Transformation_ID = SQL2.Transformation_ID LEFT OUTER JOIN
                      TABLES1 ON TransformationsTab_New.Transformation_ID = TABLES1.Transformation_ID LEFT OUTER JOIN
                      TABLES2 ON TransformationsTab_New.Transformation_ID = TABLES2.Transformation_ID LEFT OUTER JOIN
                      TransformationFunctionTab ON TransformationsTab_New.Transformation_ID = TransformationFunctionTab.Transformation_ID
;


DROP VIEW IF EXISTS TransformtionTab_noFunction;
CREATE VIEW TransformtionTab_noFunction
AS
SELECT TRANSFORMATIONSTAB.*
FROM  TRANSFORMATIONSTAB LEFT OUTER JOIN
               TransformationFunctionTab ON TRANSFORMATIONSTAB.Transformation_ID = TransformationFunctionTab.Transformation_ID
WHERE (TransformationFunctionTab.Function_ID IS NULL)
;


DROP VIEW IF EXISTS WebServiceFunctionTransformationView;
CREATE VIEW WebServiceFunctionTransformationView
AS
SELECT     WebServicesTab.WebService_ID, WebServicesTab.WebService_Name, FunctionsTab.Function_ID, 
                      TransformationFunctionTab.Transformation_ID
FROM         TransformationFunctionTab INNER JOIN
                      FunctionsTab ON TransformationFunctionTab.Function_ID = FunctionsTab.Function_ID INNER JOIN
                      WebServicesTab ON FunctionsTab.ForeignKeyFunction_ID = WebServicesTab.WebService_ID
WHERE     (FunctionsTab.FunctionType_ID = 1)
;

DROP VIEW IF EXISTS FunctionServiceFunctionTransformationView;
CREATE VIEW FunctionServiceFunctionTransformationView
AS
  SELECT transformationfunctiontab.transformation_id,transformationfunctiontab.function_id
	,functionstab.functiontype_id,functionstab.foreignkeyfunction_id as functionservice_id
  FROM transformationfunctiontab
  JOIN functionstab ON transformationfunctiontab.function_id = functionstab.function_id
;

DROP VIEW IF EXISTS ArchiveSourceFiles;
CREATE VIEW ArchiveSourceFiles
AS
SELECT     SourcesTab.File_Name, SourcesTab.Source_Type_ID, SourcesTab.Locator, SourcesTab.Path, SourcesTab.Login, 
                      SourcesTab.Password, SourcesTab.Port, XferHist.InsertTime
FROM         XferHist INNER JOIN
                      OperationsTab ON XferHist.OperationID = OperationsTab.Operation_ID INNER JOIN
                      SourcesTab ON OperationsTab.Source_ID = SourcesTab.Source_ID
WHERE     (SourcesTab.Source_Type_ID IN (1, 3)) AND (SourcesTab.Source_ID IN
                          (SELECT DISTINCT Source_ID
                            FROM          OperationsTab
                            WHERE      Operation_ID IN
                                                       (SELECT DISTINCT OperationID
                                                         FROM          XferHist)))
;


DROP VIEW IF EXISTS CRTRANDB_BackendOperation;
CREATE VIEW CRTRANDB_BackendOperation
AS
SELECT     OperationsTab.Operation_ID, OperationsTab.Operation_Name, TransformationsTab_New.Source_EDI, 
                      TransformationsTab_New.Target_EDI
FROM         TransformationsTab_New INNER JOIN
                      OperationsTab ON TransformationsTab_New.Transformation_ID = OperationsTab.Transformation_ID
WHERE     (TransformationsTab_New.Source_Type_ID = '11') OR
                      (TransformationsTab_New.Target_Type_ID = '11')


;


DROP VIEW IF EXISTS PreTrans_view1;
CREATE VIEW PreTrans_view1
AS
SELECT TransformationChainLinksTab.Operation_ID, TransformationChainLinksTab.Transformation_ID, 
               TransformationChainLinksTab.NextTransformation_ID, TransformationFunctionTab.Function_ID, 
               TransformationFunctionTab_1.Function_ID AS Next_Function_ID
FROM  TransformationChainLinksTab LEFT OUTER JOIN
               TransformationFunctionTab TransformationFunctionTab_1 ON 
               TransformationChainLinksTab.NextTransformation_ID = TransformationFunctionTab_1.Transformation_ID LEFT OUTER JOIN
               TransformationFunctionTab ON TransformationChainLinksTab.Transformation_ID = TransformationFunctionTab.Transformation_ID
;


DROP VIEW IF EXISTS PreTransformationInfoView;
CREATE VIEW PreTransformationInfoView
AS
SELECT  TransformationChainLinksTab.Operation_ID, TransformationChainLinksTab.Transformation_ID, 
               TransformationChainLinksTab.NextTransformation_ID, TransformationFunctionTab.Function_ID, WebServicesView.WebService_ID, 
               WebServicesView.FunctionType_ID, WebServicesView.WebService_Url, WebServicesView.InputXSDFileName, 
               WebServicesView.OutputXSDFileName
FROM  FunctionsTab INNER JOIN
               TransformationFunctionTab ON FunctionsTab.Function_ID = TransformationFunctionTab.Function_ID LEFT OUTER JOIN
               WebServicesView ON FunctionsTab.ForeignKeyFunction_ID = WebServicesView.WebService_ID AND 
               FunctionsTab.FunctionType_ID = WebServicesView.FunctionType_ID FULL OUTER JOIN
               TransformationChainLinksTab ON TransformationFunctionTab.Transformation_ID = TransformationChainLinksTab.Transformation_ID
;


DROP VIEW IF EXISTS ProjectOperationsView;
CREATE VIEW ProjectOperationsView
AS
SELECT interchangeprojectoperationstab.interchangeproject_id, OperationsTab.Operation_ID, OperationsTab.Operation_Name
FROM  OperationsTab LEFT OUTER JOIN
               interchangeprojectoperationstab ON OperationsTab.Operation_ID = interchangeprojectoperationstab.item_id
;

DROP VIEW IF EXISTS XferHistoryLogsView;
CREATE VIEW XferHistoryLogsView
AS
SELECT OperationsTab.Operation_ID AS InterchangeProject_ID, OperationsTab.Operation_ID, OperationsTab.Operation_Name,
XferHist.ID, XferHist.FileName, XferHist.FileSize, XferHist.XferDateTime
FROM XferHist
JOIN OperationsTab ON XferHist.OperationID = OperationsTab.Operation_ID
;

DROP VIEW IF EXISTS XmlDtdChildrenView;
CREATE VIEW XmlDtdChildrenView
AS
SELECT XMLDtdTab.XML_ID, XMLDtdLinks.Child_DTD AS Child_ID, XMLDtdTab_1.XML_Path || XMLDtdTab_1.XML_FileName AS XML_Path
FROM  XMLDtdLinks INNER JOIN
               XMLDtdTab ON XMLDtdLinks.Parent_DTD = XMLDtdTab.XML_ID INNER JOIN
               XMLDtdTab XMLDtdTab_1 ON XMLDtdLinks.Child_DTD = XMLDtdTab_1.XML_ID
;


DROP VIEW IF EXISTS TargetDtdView;
CREATE VIEW TargetDtdView
AS
SELECT TransformationsTab_New.Transformation_ID, TransformationsTab_New.Target_XML AS XML_Path, XMLDtdTab.XML_ID
FROM  TransformationsTab_New LEFT OUTER JOIN
               XMLDtdTab ON TransformationsTab_New.Target_XML LIKE XMLDtdTab.XML_Path || XMLDtdTab.XML_FileName
WHERE (TransformationsTab_New.Target_Type_ID = 4)
;


DROP VIEW IF EXISTS SourceDtdView;
CREATE VIEW SourceDtdView
AS
SELECT TransformationsTab_New.Transformation_ID, TransformationsTab_New.Source_XML AS XML_Path, XMLDtdTab.XML_ID
FROM  TransformationsTab_New LEFT OUTER JOIN
               XMLDtdTab ON TransformationsTab_New.Source_XML LIKE XMLDtdTab.XML_Path || XMLDtdTab.XML_FileName
WHERE (TransformationsTab_New.Source_Type_ID = 4)
;


DROP VIEW IF EXISTS ScheduledOperationView;
CREATE VIEW ScheduledOperationView
AS
SELECT OperationsTab.Operation_ID AS Operation_ID, ScheduleTab.*
FROM  OperationsTab INNER JOIN
               ScheduleTab ON OperationsTab.Schedule_ID = ScheduleTab.Schedule_ID
WHERE (OperationsTab.Schedule_ID <> 0)
;

CREATE VIEW DocumentFieldValidationView AS
 SELECT dd.Document_Id, dd.Field_Id, dd.Parent_Id, dd.Field_Name, dv.NotEmpty, dv.FieldLength,
   dv.TrimToLength, dv.IgnoreCase, dv.FieldOperator, dv.OperatorArgument, dv.ActionOnInvalid
 FROM Document_DefinitionsTab dd
 JOIN DocumentFieldValidationTab dv
 ON dd.Document_Id = dv.Document_Id AND dd.Field_Id = dv.Field_Id AND dd.Parent_Id = dv.Parent_Id
;

CREATE OR REPLACE FUNCTION SpInsertOperationInstanceTab(integer, bigint, varchar(50)) RETURNS bigint AS '
DECLARE
  operation_id_val ALIAS FOR $1;
  parent_operation_instance_id_val ALIAS FOR $2;
  msg_val ALIAS FOR $3;
  retVal bigint;

BEGIN

INSERT INTO operationinstancetab (operation_id, parent_operation_instance_id, received_from)
  VALUES (operation_id_val, parent_operation_instance_id_val, msg_val);

SELECT INTO retVal currval(''operationinstancetab_operation_instance_id_seq'');

RETURN retVal;

END;

' LANGUAGE 'plpgsql';
