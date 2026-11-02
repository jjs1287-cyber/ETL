-- Create this function here in case somebody wants to use it
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

DROP TABLE IF EXISTS InterchangeProjectFoldersTab;
CREATE TABLE InterchangeProjectFoldersTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectSourcesTab;
CREATE TABLE InterchangeProjectSourcesTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectTargetsTab;
CREATE TABLE InterchangeProjectTargetsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectTransformationsTab;
CREATE TABLE InterchangeProjectTransformationsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectSalesforceConnectorsTab;
CREATE TABLE InterchangeProjectSalesforceConnectorsTab (
	InterchangeProject_Id int NOT NULL,
	Item_ID int NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectSalesforceQueriesTab;
CREATE TABLE InterchangeProjectSalesforceQueriesTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectSalesforceUpsertsTab;
CREATE TABLE InterchangeProjectSalesforceUpsertsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectSalesforceCreatesTab;
CREATE TABLE InterchangeProjectSalesforceCreatesTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectSalesforceUpdatesTab;
CREATE TABLE InterchangeProjectSalesforceUpdatesTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectSalesforceDeletesTab;
CREATE TABLE InterchangeProjectSalesforceDeletesTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectSalesforceBulkLoadsTab;
CREATE TABLE InterchangeProjectSalesforceBulkLoadsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectSapEndpointsTab;
CREATE TABLE InterchangeProjectSapEndpointsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectSapFunctionsTab;
CREATE TABLE InterchangeProjectSapFunctionsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);
--AutoDesk
SELECT drop_if_exists(lower('InterchangeProjectAutoDeskEndpointsTab'));
CREATE TABLE InterchangeProjectAutoDeskEndpointsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

SELECT drop_if_exists(lower('InterchangeProjectAutoDeskFunctionsTab'));
CREATE TABLE InterchangeProjectAutoDeskFunctionsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);
--Ebs
SELECT drop_if_exists(lower('InterchangeProjectEbsEndpointsTab'));
CREATE TABLE InterchangeProjectEbsEndpointsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

SELECT drop_if_exists(lower('InterchangeProjectEbsFunctionsTab'));
CREATE TABLE InterchangeProjectEbsFunctionsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

SELECT drop_if_exists(lower('InterchangeProjectMSAXEndpointsTab'));
CREATE TABLE InterchangeProjectMSAXEndpointsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

SELECT drop_if_exists(lower('InterchangeProjectMSAXFunctionsTab'));
CREATE TABLE InterchangeProjectMSAXFunctionsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);


DROP TABLE IF EXISTS InterchangeProjectMSGPEndpointsTab;
CREATE TABLE InterchangeProjectMSGPEndpointsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectMSGPFunctionsTab;
CREATE TABLE InterchangeProjectMSGPFunctionsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectMSCrmEndpointsTab;
CREATE TABLE InterchangeProjectMSCrmEndpointsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectMSCrmFunctionsTab;
CREATE TABLE InterchangeProjectMSCrmFunctionsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);
--NetSuite
SELECT drop_if_exists(lower('InterchangeProjectNetSuiteEndpointsTab'));
CREATE TABLE InterchangeProjectNetSuiteEndpointsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

SELECT drop_if_exists(lower('InterchangeProjectNetSuiteFunctionsTab'));
CREATE TABLE InterchangeProjectNetSuiteFunctionsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);


DROP TABLE IF EXISTS InterchangeProjectSchedulesTab;
CREATE TABLE InterchangeProjectSchedulesTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectScriptsTab;
CREATE TABLE InterchangeProjectScriptsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectOperationsTab;
CREATE TABLE InterchangeProjectOperationsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectWebServicesTab;
CREATE TABLE InterchangeProjectWebServicesTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectMessagesTab;
CREATE TABLE InterchangeProjectMessagesTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectDocumentsTab;
CREATE TABLE InterchangeProjectDocumentsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectJmsMessagesTab;
CREATE TABLE InterchangeProjectJmsMessagesTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);

DROP TABLE IF EXISTS InterchangeProjectHttpEndpointsTab;
CREATE TABLE InterchangeProjectHttpEndpointsTab (
	InterchangeProject_ID   int	NOT NULL,
	Item_ID			int	NOT NULL
);
