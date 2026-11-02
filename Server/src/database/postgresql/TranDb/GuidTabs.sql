DROP TABLE IF EXISTS GuidsForDocumentsTab CASCADE;
CREATE TABLE GuidsForDocumentsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForFoldersTab CASCADE;
CREATE TABLE GuidsForFoldersTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForGroupsTab CASCADE;
CREATE TABLE GuidsForGroupsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForHttpEndpointsTab CASCADE;
CREATE TABLE GuidsForHttpEndpointsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForInterchangeProjectsTab CASCADE;
CREATE TABLE GuidsForInterchangeProjectsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForJmsMessagesTab CASCADE;
CREATE TABLE GuidsForJmsMessagesTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForMessagesTab CASCADE;
CREATE TABLE GuidsForMessagesTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForOperationsTab CASCADE;
CREATE TABLE GuidsForOperationsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForSalesforceConnectorsTab CASCADE;
CREATE TABLE GuidsForSalesforceConnectorsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForSalesforceQueriesTab CASCADE;
CREATE TABLE GuidsForSalesforceQueriesTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForSalesforceUpsertsTab CASCADE;
CREATE TABLE GuidsForSalesforceUpsertsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForSalesforceCreatesTab CASCADE;
CREATE TABLE GuidsForSalesforceCreatesTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForSalesforceUpdatesTab CASCADE;
CREATE TABLE GuidsForSalesforceUpdatesTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForSalesforceDeletesTab CASCADE;
CREATE TABLE GuidsForSalesforceDeletesTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForSalesforceBulkLoadsTab CASCADE;
CREATE TABLE GuidsForSalesforceBulkLoadsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

--Sap
SELECT drop_if_exists(lower('GuidsForSapEndpointsTab'));
CREATE TABLE GuidsForSapEndpointsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

SELECT drop_if_exists(lower('GuidsForSapFunctionsTab'));
CREATE TABLE GuidsForSapFunctionsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);
--AutoDesk
SELECT drop_if_exists(lower('GuidsForAutoDeskEndpointsTab'));
CREATE TABLE GuidsForAutoDeskEndpointsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

SELECT drop_if_exists(lower('GuidsForAutoDeskFunctionsTab'));
CREATE TABLE GuidsForAutoDeskFunctionsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);
--Ebs
SELECT drop_if_exists(lower('GuidsForEbsEndpointsTab'));
CREATE TABLE GuidsForEbsEndpointsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

SELECT drop_if_exists(lower('GuidsForEbsFunctionsTab'));
CREATE TABLE GuidsForEbsFunctionsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);
--MSAX
SELECT drop_if_exists(lower('GuidsForMSAXEndpointsTab'));
CREATE TABLE GuidsForMSAXEndpointsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

SELECT drop_if_exists(lower('GuidsForMSAXFunctionsTab'));
CREATE TABLE GuidsForMSAXFunctionsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

--MSGP
SELECT drop_if_exists(lower('GuidsForMSGPEndpointsTab'));
CREATE TABLE GuidsForMSGPEndpointsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

SELECT drop_if_exists(lower('GuidsForMSGPFunctionsTab'));
CREATE TABLE GuidsForMSGPFunctionsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

--MSCrm
SELECT drop_if_exists(lower('GuidsForMSCrmEndpointsTab'));
CREATE TABLE GuidsForMSCrmEndpointsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

SELECT drop_if_exists(lower('GuidsForMSCrmFunctionsTab'));
CREATE TABLE GuidsForMSCrmFunctionsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);
--GuidTabs.sql
--NetSuite
SELECT drop_if_exists(lower('GuidsForNetSuiteEndpointsTab'));
CREATE TABLE GuidsForNetSuiteEndpointsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

SELECT drop_if_exists(lower('GuidsForNetSuiteFunctionsTab'));
CREATE TABLE GuidsForNetSuiteFunctionsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);


DROP TABLE IF EXISTS GuidsForSchedulesTab CASCADE;
CREATE TABLE GuidsForSchedulesTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForScriptsTab CASCADE;
CREATE TABLE GuidsForScriptsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForSourcesTab CASCADE;
CREATE TABLE GuidsForSourcesTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForTargetsTab CASCADE;
CREATE TABLE GuidsForTargetsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForTransformationsTab CASCADE;
CREATE TABLE GuidsForTransformationsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForUsersTab CASCADE;
CREATE TABLE GuidsForUsersTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForWebServicesTab CASCADE;
CREATE TABLE GuidsForWebServicesTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForXmlDtdsTab CASCADE;
CREATE TABLE GuidsForXmlDtdsTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);

DROP TABLE IF EXISTS GuidsForWsdlTab CASCADE;
CREATE TABLE GuidsForWsdlTab (
	Guid varchar(36),
	Item_ID int UNIQUE NOT NULL,
	PRIMARY KEY (Guid)
);
