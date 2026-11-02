DROP VIEW IF EXISTS GuidsForDocumentsView;
CREATE VIEW GuidsForDocumentsView
AS
SELECT g.guid, g.item_id, t.* from DocumentsTab t
INNER JOIN GuidsForDocumentsTab g
on g.item_id = t.Document_ID;


DROP VIEW IF EXISTS GuidsForFoldersView;
CREATE VIEW GuidsForFoldersView
AS
SELECT g.guid, g.item_id, t.* from FoldersTab t
INNER JOIN GuidsForFoldersTab g
on g.item_id = t.Folder_ID;


DROP VIEW IF EXISTS GuidsForGroupsView;
CREATE VIEW GuidsForGroupsView
AS
SELECT g.guid, g.item_id, t.* from GroupsTab t
INNER JOIN GuidsForGroupsTab g
on g.item_id = t.Group_ID;


DROP VIEW IF EXISTS GuidsForHttpEndpointsView;
CREATE VIEW GuidsForHttpEndpointsView
AS
SELECT g.guid, g.item_id, t.* from HttpEndpointsTab t
INNER JOIN GuidsForHttpEndpointsTab g
on g.item_id = t.HttpEndpoint_ID;


DROP VIEW IF EXISTS GuidsForInterchangeProjectsView;
CREATE VIEW GuidsForInterchangeProjectsView
AS
SELECT g.guid, g.item_id, t.* from InterchangeProjectsTab t
INNER JOIN GuidsForInterchangeProjectsTab g
on g.item_id = t.InterchangeProject_ID;


DROP VIEW IF EXISTS GuidsForJmsMessagesView;
CREATE VIEW GuidsForJmsMessagesView
AS
SELECT g.guid, g.item_id, t.* from JmsMessagesTab t
INNER JOIN GuidsForJmsMessagesTab g
on g.item_id = t.JmsMessage_ID;


DROP VIEW IF EXISTS GuidsForMessagesView;
CREATE VIEW GuidsForMessagesView
AS
SELECT g.guid, g.item_id, t.* from MessagesTab t
INNER JOIN GuidsForMessagesTab g
on g.item_id = t.Message_ID;


DROP VIEW IF EXISTS GuidsForOperationsView;
CREATE VIEW GuidsForOperationsView
AS
SELECT g.guid, g.item_id, t.* from OperationsTab t
INNER JOIN GuidsForOperationsTab g
on g.item_id = t.Operation_ID;


DROP VIEW IF EXISTS GuidsForSalesforceConnectorsView;
CREATE VIEW GuidsForSalesforceConnectorsView
AS
SELECT g.guid, g.item_id, t.* from SalesforceConnectorsTab t
INNER JOIN GuidsForSalesforceConnectorsTab g
on g.item_id = t.SalesforceConnector_ID;


DROP VIEW IF EXISTS GuidsForSalesforceQueriesView;
CREATE VIEW GuidsForSalesforceQueriesView
AS
SELECT g.guid, g.item_id, t.* from SalesforceQueriesTab t
INNER JOIN GuidsForSalesforceQueriesTab g
on g.item_id = t.SalesforceQuery_ID;


DROP VIEW IF EXISTS GuidsForSalesforceUpsertsView;
CREATE VIEW GuidsForSalesforceUpsertsView
AS
SELECT g.guid, g.item_id, t.* from SalesforceUpsertsTab t
INNER JOIN GuidsForSalesforceUpsertsTab g
on g.item_id = t.SalesforceUpsert_ID;


SELECT drop_view_if_exists('guidsforsalesforcecreatesview');
CREATE VIEW GuidsForSalesforceCreatesView
AS
SELECT g.guid, g.item_id, t.* from SalesforceCreatesTab t
INNER JOIN GuidsForSalesforceCreatesTab g
on g.item_id = t.SalesforceCreate_ID;


SELECT drop_view_if_exists('guidsforsalesforceupdatesview');
CREATE VIEW GuidsForSalesforceUpdatesView
AS
SELECT g.guid, g.item_id, t.* from SalesforceUpdatesTab t
INNER JOIN GuidsForSalesforceUpdatesTab g
on g.item_id = t.SalesforceUpdate_ID;


SELECT drop_view_if_exists('guidsforsalesforcedeletesview');
CREATE VIEW GuidsForSalesforceDeletesView
AS
SELECT g.guid, g.item_id, t.* from SalesforceDeletesTab t
INNER JOIN GuidsForSalesforceDeletesTab g
on g.item_id = t.SalesforceDelete_ID;


SELECT drop_view_if_exists('guidsforsalesforcebulkloadsview');
CREATE VIEW GuidsForSalesforceBulkLoadsView
AS
SELECT g.guid, g.item_id, t.* from SalesforceBulkLoadsTab t
INNER JOIN GuidsForSalesforceBulkLoadsTab g
on g.item_id = t.SalesforceBulkLoad_ID;

--Sap
SELECT drop_view_if_exists(lower('GuidsForSapEndpointsView'));
CREATE VIEW GuidsForSapEndpointsView
AS
SELECT g.guid, g.item_id, t.* from SapEndpointsTab t
INNER JOIN GuidsForSapEndpointsTab g on g.item_id = t.SapEndpoint_Id;

SELECT drop_view_if_exists(lower('GuidsForSapFunctionsView'));
CREATE VIEW GuidsForSapFunctionsView
AS
SELECT g.guid, g.item_id, t.* from SapFunctionsTab t
INNER JOIN GuidsForSapFunctionsTab g on g.item_id = t.SapFunction_Id;
--AutoDesk
SELECT drop_view_if_exists(lower('GuidsForAutoDeskEndpointsView'));
CREATE VIEW GuidsForAutoDeskEndpointsView
AS
SELECT g.guid, g.item_id, t.* from AutoDeskEndpointsTab t
INNER JOIN GuidsForAutoDeskEndpointsTab g on g.item_id = t.AutoDeskEndpoint_Id;

SELECT drop_view_if_exists(lower('GuidsForAutoDeskFunctionsView'));
CREATE VIEW GuidsForAutoDeskFunctionsView
AS
SELECT g.guid, g.item_id, t.* from AutoDeskFunctionsTab t
INNER JOIN GuidsForAutoDeskFunctionsTab g on g.item_id = t.AutoDeskFunction_Id;
--Ebs
SELECT drop_view_if_exists(lower('GuidsForEbsEndpointsView'));
CREATE VIEW GuidsForEbsEndpointsView
AS
SELECT g.guid, g.item_id, t.* from EbsEndpointsTab t
INNER JOIN GuidsForEbsEndpointsTab g on g.item_id = t.EbsEndpoint_Id;

SELECT drop_view_if_exists(lower('GuidsForEbsFunctionsView'));
CREATE VIEW GuidsForEbsFunctionsView
AS
SELECT g.guid, g.item_id, t.* from EbsFunctionsTab t
INNER JOIN GuidsForEbsFunctionsTab g on g.item_id = t.EbsFunction_Id;

--MSAX
SELECT drop_view_if_exists(lower('GuidsForMSAXEndpointsView'));
CREATE VIEW GuidsForMSAXEndpointsView
AS
SELECT g.guid, g.item_id, t.* from MSAXEndpointsTab t
INNER JOIN GuidsForMSAXEndpointsTab g on g.item_id = t.MSAXEndpoint_Id;

SELECT drop_view_if_exists(lower('GuidsForMSAXFunctionsView'));
CREATE VIEW GuidsForMSAXFunctionsView
AS
SELECT g.guid, g.item_id, t.* from MSAXFunctionsTab t
INNER JOIN GuidsForMSAXFunctionsTab g on g.item_id = t.MSAXFunction_Id;

--MSGP
SELECT drop_view_if_exists(lower('GuidsForMSGPEndpointsView'));
CREATE VIEW GuidsForMSGPEndpointsView
AS
SELECT g.guid, g.item_id, t.* from MSGPEndpointsTab t
INNER JOIN GuidsForMSGPEndpointsTab g on g.item_id = t.MSGPEndpoint_Id;

SELECT drop_view_if_exists(lower('GuidsForMSGPFunctionsView'));
CREATE VIEW GuidsForMSGPFunctionsView
AS
SELECT g.guid, g.item_id, t.* from MSGPFunctionsTab t
INNER JOIN GuidsForMSGPFunctionsTab g on g.item_id = t.MSGPFunction_Id;

--MSCrm
SELECT drop_view_if_exists(lower('GuidsForMSCrmEndpointsView'));
CREATE VIEW GuidsForMSCrmEndpointsView
AS
SELECT g.guid, g.item_id, t.* from MSCrmEndpointsTab t
INNER JOIN GuidsForMSCrmEndpointsTab g on g.item_id = t.MSCrmEndpoint_Id;

SELECT drop_view_if_exists(lower('GuidsForMSCrmFunctionsView'));
CREATE VIEW GuidsForMSCrmFunctionsView
AS
SELECT g.guid, g.item_id, t.* from MSCrmFunctionsTab t
INNER JOIN GuidsForMSCrmFunctionsTab g on g.item_id = t.MSCrmFunction_Id;
--NetSuite
SELECT drop_view_if_exists(lower('GuidsForNetSuiteEndpointsView'));
CREATE VIEW GuidsForNetSuiteEndpointsView
AS
SELECT g.guid, g.item_id, t.* from NetSuiteEndpointsTab t
INNER JOIN GuidsForNetSuiteEndpointsTab g on g.item_id = t.NetSuiteEndpoint_Id;

SELECT drop_view_if_exists(lower('GuidsForNetSuiteFunctionsView'));
CREATE VIEW GuidsForNetSuiteFunctionsView
AS
SELECT g.guid, g.item_id, t.* from NetSuiteFunctionsTab t
INNER JOIN GuidsForNetSuiteFunctionsTab g on g.item_id = t.NetSuiteFunction_Id;


DROP VIEW IF EXISTS GuidsForSchedulesView;
CREATE VIEW GuidsForSchedulesView
AS
SELECT g.guid, g.item_id, t.* from ScheduleTab t
INNER JOIN GuidsForSchedulesTab g
on g.item_id = t.Schedule_ID;

DROP VIEW IF EXISTS GuidsForScriptsView;
CREATE VIEW GuidsForScriptsView
AS
SELECT g.guid, g.item_id, t.* from ScriptsTab t
INNER JOIN GuidsForScriptsTab g
on g.item_id = t.Script_ID;


DROP VIEW IF EXISTS GuidsForSourcesView;
CREATE VIEW GuidsForSourcesView
AS
SELECT g.guid, g.item_id, t.* from SourcesTab t
INNER JOIN GuidsForSourcesTab g
on g.item_id = t.Source_ID;


DROP VIEW IF EXISTS GuidsForTargetsView;
CREATE VIEW GuidsForTargetsView
AS
SELECT g.guid, g.item_id, t.* from TargetsTab t
INNER JOIN GuidsForTargetsTab g
on g.item_id = t.Target_ID;


DROP VIEW IF EXISTS GuidsForTransformationsView;
CREATE VIEW GuidsForTransformationsView
AS
SELECT g.guid, g.item_id, t.* from TransformationsTab_New t
INNER JOIN GuidsForTransformationsTab g
on g.item_id = t.Transformation_ID;


DROP VIEW IF EXISTS GuidsForUsersView;
CREATE VIEW GuidsForUsersView
AS
SELECT g.guid, g.item_id, t.* from UsersTab t
INNER JOIN GuidsForUsersTab g
on g.item_id = t.User_ID;


DROP VIEW IF EXISTS GuidsForWebServicesView;
CREATE VIEW GuidsForWebServicesView
AS
SELECT g.guid, g.item_id, t.* from WebServicesTab t
INNER JOIN GuidsForWebServicesTab g
on g.item_id = t.WebService_ID;


DROP VIEW IF EXISTS GuidsForXmlDtdsView;
CREATE VIEW GuidsForXmlDtdsView
AS
SELECT g.guid, g.item_id, t.* from XmlDtdTab t
INNER JOIN GuidsForWebServicesTab g
on g.item_id = t.Xml_ID;


DROP VIEW IF EXISTS GuidsForWsdlView;
CREATE VIEW GuidsForWsdlView
AS
SELECT g.guid, g.item_id, t.* from WsdlTab t
INNER JOIN GuidsForWsdlTab g
on g.item_id = t.Wsdl_ID;

CREATE OR REPLACE VIEW operationlogview AS
SELECT olt.operation_instance_id, olt.operation_id, olt.status, olt.entered_ts, olt.started_ts, olt.status_ts, olt.entered_tm,
  oit.parent_operation_instance_id, oit.msgid, oit.received_from,
  gov.guid, gov.operation_name,
  omt.field_name,omt.message, omt.operation_message_id,
  ost.source_files, ost.target_files, ost.source_size, ost.target_size, ost.source_records, ost.target_records, ost.total_transformation_time
  FROM operationlogtab olt
  JOIN operationinstancetab oit ON oit.operation_instance_id = olt.operation_instance_id
  JOIN guidsforoperationsview gov ON gov.item_id = olt.operation_id
  LEFT OUTER JOIN operationmessagetab omt ON omt.operation_instance_id = olt.operation_instance_id
  LEFT OUTER JOIN operationsummarytab ost ON ost.operation_instance_id = olt.operation_instance_id
;

CREATE OR REPLACE VIEW operationprogressview AS
SELECT opt.operation_instance_id, opt.section, opt.message, opt.percentage_done, opt.estimated_end_time,
  oit.operation_id, gov.guid
  FROM operation_progress_tab opt
  JOIN operationinstancetab oit ON oit.operation_instance_id = opt.operation_instance_id
  JOIN guidsforoperationsview gov ON gov.item_id = oit.operation_id
;

CREATE OR REPLACE VIEW salesforceoperationstatusview AS 
 SELECT g.guid AS operation_guid, g.operation_name, s.*
   FROM salesforceoperationstatustab s
   JOIN guidsforoperationsview g ON g.item_id = s.operation_id;

SELECT drop_view_if_exists('operationfilestatusview');
CREATE OR REPLACE VIEW OperationFileStatusView AS 
 SELECT s.FileCategory, s.Records, s.FileSize, s.FileRelativePathName,s.FileCharEncode,s.FileStatusMessage,s.FileGUID, g.*
   FROM OperationFileStatusTab s INNER JOIN operationlogview g ON g.operation_id = s.operation_id and g.operation_instance_id=s.operation_instance_id;

CREATE OR REPLACE VIEW bulkload_instance_view AS 
SELECT o.operation_id, b.operation_instance_id, last_operation_instance_id, bulkload_id, 
       sfdc_job_id, start_time, end_time, filename, filecharencode, file_size, 
       start_pos, status,h.action as bulkloadaction, h.source_header
  FROM bulkload_instance_tab b inner join operationinstancetab o on b.operation_instance_id=o.operation_instance_id
       left join salesforcebulkloadstab h on o.operation_id=h.operation_id;      
   
