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


DROP TABLE IF EXISTS UserPermissionsForDocumentsTab;
CREATE TABLE UserPermissionsForDocumentsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForDocumentsTab;
CREATE TABLE GroupPermissionsForDocumentsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForFoldersTab;
CREATE TABLE UserPermissionsForFoldersTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForFoldersTab;
CREATE TABLE GroupPermissionsForFoldersTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForHttpEndpointsTab;
CREATE TABLE UserPermissionsForHttpEndpointsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForHttpEndpointsTab;
CREATE TABLE GroupPermissionsForHttpEndpointsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForMessagesTab;
CREATE TABLE UserPermissionsForMessagesTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForMessagesTab;
CREATE TABLE GroupPermissionsForMessagesTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForOperationsTab;
CREATE TABLE UserPermissionsForOperationsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForOperationsTab;
CREATE TABLE GroupPermissionsForOperationsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForSalesforceConnectorsTab;
CREATE TABLE UserPermissionsForSalesforceConnectorsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForSalesforceConnectorsTab;
CREATE TABLE GroupPermissionsForSalesforceConnectorsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForSalesforceQueriesTab;
CREATE TABLE UserPermissionsForSalesforceQueriesTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForSalesforceQueriesTab;
CREATE TABLE GroupPermissionsForSalesforceQueriesTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForSalesforceUpsertsTab;
CREATE TABLE UserPermissionsForSalesforceUpsertsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForSalesforceUpsertsTab;
CREATE TABLE GroupPermissionsForSalesforceUpsertsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForSalesforceCreatesTab;
CREATE TABLE UserPermissionsForSalesforceCreatesTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForSalesforceCreatesTab;
CREATE TABLE GroupPermissionsForSalesforceCreatesTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForSalesforceBulkLoadsTab;
CREATE TABLE UserPermissionsForSalesforceBulkLoadsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForSalesforceBulkLoadsTab;
CREATE TABLE GroupPermissionsForSalesforceBulkLoadsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForSalesforceUpdatesTab;
CREATE TABLE UserPermissionsForSalesforceUpdatesTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForSalesforceUpdatesTab;
CREATE TABLE GroupPermissionsForSalesforceUpdatesTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForSalesforceDeletesTab;
CREATE TABLE UserPermissionsForSalesforceDeletesTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForSalesforceDeletesTab;
CREATE TABLE GroupPermissionsForSalesforceDeletesTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

--Sap
SELECT drop_if_exists(lower('UserPermissionsForSapEndpointsTab'));
CREATE TABLE UserPermissionsForSapEndpointsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

SELECT drop_if_exists(lower('GroupPermissionsForSapEndpointsTab'));
CREATE TABLE GroupPermissionsForSapEndpointsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

SELECT drop_if_exists(lower('UserPermissionsForSapFunctionsTab'));
CREATE TABLE UserPermissionsForSapFunctionsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

SELECT drop_if_exists(lower('GroupPermissionsForSapFunctionsTab'));
CREATE TABLE GroupPermissionsForSapFunctionsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);
--AutoDesk
SELECT drop_if_exists(lower('UserPermissionsForAutoDeskEndpointsTab'));
CREATE TABLE UserPermissionsForAutoDeskEndpointsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

SELECT drop_if_exists(lower('GroupPermissionsForAutoDeskEndpointsTab'));
CREATE TABLE GroupPermissionsForAutoDeskEndpointsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

SELECT drop_if_exists(lower('UserPermissionsForAutoDeskFunctionsTab'));
CREATE TABLE UserPermissionsForAutoDeskFunctionsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

SELECT drop_if_exists(lower('GroupPermissionsForAutoDeskFunctionsTab'));
CREATE TABLE GroupPermissionsForAutoDeskFunctionsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);
--Ebs
SELECT drop_if_exists(lower('UserPermissionsForEbsEndpointsTab'));
CREATE TABLE UserPermissionsForEbsEndpointsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

SELECT drop_if_exists(lower('GroupPermissionsForEbsEndpointsTab'));
CREATE TABLE GroupPermissionsForEbsEndpointsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

SELECT drop_if_exists(lower('UserPermissionsForEbsFunctionsTab'));
CREATE TABLE UserPermissionsForEbsFunctionsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

SELECT drop_if_exists(lower('GroupPermissionsForEbsFunctionsTab'));
CREATE TABLE GroupPermissionsForEbsFunctionsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

--MSAX
SELECT drop_if_exists(lower('UserPermissionsForMSAXEndpointsTab'));
CREATE TABLE UserPermissionsForMSAXEndpointsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

SELECT drop_if_exists(lower('GroupPermissionsForMSAXEndpointsTab'));
CREATE TABLE GroupPermissionsForMSAXEndpointsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

SELECT drop_if_exists(lower('UserPermissionsForMSAXFunctionsTab'));
CREATE TABLE UserPermissionsForMSAXFunctionsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

SELECT drop_if_exists(lower('GroupPermissionsForMSAXFunctionsTab'));
CREATE TABLE GroupPermissionsForMSAXFunctionsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

--MSGP
SELECT drop_if_exists(lower('UserPermissionsForMSGPEndpointsTab'));
CREATE TABLE UserPermissionsForMSGPEndpointsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

SELECT drop_if_exists(lower('GroupPermissionsForMSGPEndpointsTab'));
CREATE TABLE GroupPermissionsForMSGPEndpointsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

SELECT drop_if_exists(lower('UserPermissionsForMSGPFunctionsTab'));
CREATE TABLE UserPermissionsForMSGPFunctionsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

SELECT drop_if_exists(lower('GroupPermissionsForMSGPFunctionsTab'));
CREATE TABLE GroupPermissionsForMSGPFunctionsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

--MSCrm
SELECT drop_if_exists(lower('UserPermissionsForMSCrmEndpointsTab'));
CREATE TABLE UserPermissionsForMSCrmEndpointsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

SELECT drop_if_exists(lower('GroupPermissionsForMSCrmEndpointsTab'));
CREATE TABLE GroupPermissionsForMSCrmEndpointsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

SELECT drop_if_exists(lower('UserPermissionsForMSCrmFunctionsTab'));
CREATE TABLE UserPermissionsForMSCrmFunctionsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

SELECT drop_if_exists(lower('GroupPermissionsForMSCrmFunctionsTab'));
CREATE TABLE GroupPermissionsForMSCrmFunctionsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);
--PermissionTabs.sql
--NetSuite
SELECT drop_if_exists(lower('UserPermissionsForNetSuiteEndpointsTab'));
CREATE TABLE UserPermissionsForNetSuiteEndpointsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

SELECT drop_if_exists(lower('GroupPermissionsForNetSuiteEndpointsTab'));
CREATE TABLE GroupPermissionsForNetSuiteEndpointsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

SELECT drop_if_exists(lower('UserPermissionsForNetSuiteFunctionsTab'));
CREATE TABLE UserPermissionsForNetSuiteFunctionsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

SELECT drop_if_exists(lower('GroupPermissionsForNetSuiteFunctionsTab'));
CREATE TABLE GroupPermissionsForNetSuiteFunctionsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);


DROP TABLE IF EXISTS UserPermissionsForSchedulesTab;
CREATE TABLE UserPermissionsForSchedulesTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForSchedulesTab;
CREATE TABLE GroupPermissionsForSchedulesTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForScriptsTab;
CREATE TABLE UserPermissionsForScriptsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForScriptsTab;
CREATE TABLE GroupPermissionsForScriptsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForSourcesTab;
CREATE TABLE UserPermissionsForSourcesTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForSourcesTab;
CREATE TABLE GroupPermissionsForSourcesTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForTargetsTab;
CREATE TABLE UserPermissionsForTargetsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForTargetsTab;
CREATE TABLE GroupPermissionsForTargetsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForTransformationsTab;
CREATE TABLE UserPermissionsForTransformationsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForTransformationsTab;
CREATE TABLE GroupPermissionsForTransformationsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForWebServicesTab;
CREATE TABLE UserPermissionsForWebServicesTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForWebServicesTab;
CREATE TABLE GroupPermissionsForWebServicesTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForXmlDtdsTab;
CREATE TABLE UserPermissionsForXmlDtdsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForXmlDtdsTab;
CREATE TABLE GroupPermissionsForXmlDtdsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForInterchangeProjectsTab;
CREATE TABLE UserPermissionsForInterchangeProjectsTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForInterchangeProjectsTab;
CREATE TABLE GroupPermissionsForInterchangeProjectsTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForWsdlTab;
CREATE TABLE UserPermissionsForWsdlTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForWsdlTab;
CREATE TABLE GroupPermissionsForWsdlTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);

DROP TABLE IF EXISTS UserPermissionsForJmsMessagesTab;
CREATE TABLE UserPermissionsForJmsMessagesTab (
	Item_ID int,
	User_ID int,
	AllowBits int NOT NULL DEFAULT 8,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 8:Owner, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, User_ID)
);

DROP TABLE IF EXISTS GroupPermissionsForJmsMessagesTab;
CREATE TABLE GroupPermissionsForJmsMessagesTab (
	Item_ID int,
	Group_ID int,
	AllowBits int NOT NULL DEFAULT 7,
	DenyBits int NOT NULL DEFAULT 0,
--      1: Execute, 2:Write, 4:Read, 16:Delete, 32:Change Permissions
	PRIMARY KEY (Item_ID, Group_ID)
);
