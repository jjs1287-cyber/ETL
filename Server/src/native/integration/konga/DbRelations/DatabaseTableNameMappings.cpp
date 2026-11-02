/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <string>

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>

#include "EntityIdConstants.h"
#include "DatabaseTableNameMappings.h"

using std::string;

                                               
namespace konga {

                                                                                          
	const string nameDocuments = "Documents";
	const string nameFolders = "Folders";
	const string nameGroups = "Groups";
	const string nameInterchangeProjects = "InterchangeProjects";
	const string nameHttpEndpoints = "HttpEndpoints";
	const string nameMessages = "Messages";
	const string nameSchedules = "Schedule";
	const string nameProjectVariables = "ProjectVariables"; const string nameXsltTransforms = "XsltTransforms"; const string nameScripts = "Scripts";
	const string nameJmsMessage = "JmsMessages";

                                                                                                 
                                                                        
	const string nameFunctions = "Functions";                                            
	const string nameCallRestrictions = "CallRestrictions";                                 
	const string nameCommonSourceOperations = "CommonSourceOperations";                                   
	const string nameDBFields = "DBFields";                                                 
	const string nameDBTables = "DBTables";                                                 
	const string nameOperationGraphLayout = "OperationGraphLayout";	                                  
	const string nameProcessMessages = "Messages";
	const string nameScheduledWorkflows = "ScheduledWorkflows";                                                            
	const string nameSqlScript = "SqlScript";                                                 
	const string nameTranMessages = "Tran_Messages";                                                                               
	const string nameTranRelations = "Tran_Relations";                                                 
	const string nameTransFilter = "Trans_Filter";                                                                                 
	const string nameTransformationDefinitions = "Transformation_Definitions";                                                 
	const string nameLdapSearchParameters = "LdapSearchParameters";                                                  
	const string nameLdapStructureEntries = "LdapStructureEntries";                                                  
	const string nameUserGroup = "User_Group";                                                  
	const string nameHttpEndpointOperations = "HttpEndpointOperations";	                                        

                                               

	                               
	const string nameSources = "Sources";
	const string nameTargets = "Targets";

	            
	                                        
	const string nameUsers = "Users";

	                          
	                                                                                                        
	                                                                                                                    
	const string nameTransformationsTab_New = "TransformationsTab_New";

	                                                                                               
	                                      
	                                                                                                              
	const string nameDocumentDefinitions = "Document_Definitions";                                 

	                                                                                      
	                                                                  
	const string nameFolderItem = "FolderItem";                                   

	                 
	                                                                                                                                   
	                                                                                                                                                    
	const string nameOperations = "Operations";

	                        
	                                      
	const string nameSalesforceConnectors = "SalesforceConnectors"; const string nameSalesforceQueries = "SalesforceQueries";

	                        
	                                      
	const string nameSalesforceUpserts = "SalesforceUpserts";

	                        
	                                      
	const string nameSalesforceCreates = "SalesforceCreates";

	                        
	                                      
	const string nameSalesforceUpdates = "SalesforceUpdates";

	                          
	                                      
	const string nameSalesforceBulkLoads = "SalesforceBulkLoads";

	                        
	                                      
	const string nameSalesforceDeletes = "SalesforceDeletes";

	const string nameOperationPipelineActivities = "OperationPipelineActivities";

	                            
	                                                                                  
	                                                                                                        
	const string nameTransformationChainLinks = "TransformationChainLinks";


	                                                                                                            
	const string nameWebServices = "WebServices";                        
	const string nameWsdl= "Wsdl";

                                      
	const string nameTransformations_v = "Transformations";
	const string nameWebServiceFunctionTransformations = "WebServiceFunctionTransformation";
	const string nameXMLDtd = "XMLDtd";
	const string nameXMLDtdLinks = "XMLDtdLinks";
	const string nameWsdlDependencies = "WsdlDependencies";

	                                                  
	string getTableNameImpl(const string& stName) {
		if ( stName == nameTransformations_v )
			return "TRANSFORMATIONSTAB_V";	                                                     
		else if ( stName == nameTransformationsTab_New )
			return "TransformationsTab_New";
		else if ( stName == nameWebServiceFunctionTransformations )
			return "WebServiceFunctionTransformationView";	                                 
		else if ( stName == nameXMLDtdLinks )
			return "XMLDtdLinks";	                                                                                
		else
			return stName + "Tab";
	}

	                                         
	string getTableName(const string& stName) {
		string tableName(getTableNameImpl(stName));
		ToLower(tableName);
		return tableName;
	}

	                                              
	string getIdNameImpl(const string& stName) {
		                                                             
		                                                       
		if ( stName == nameXMLDtd )
			return "xml_id";
		else if ( stName == nameFolderItem )
			return "";
		else if ( stName == nameCallRestrictions )
			return "";
		else if ( stName == nameDBFields )
			return "";
		else if ( stName == nameDBTables )
			return "";
		else if ( stName == nameDocumentDefinitions )
			return "";
		else if ( stName == nameScheduledWorkflows )
			return "";
		else if ( stName == nameSqlScript )
			return "";
		else if ( stName == nameTranMessages )
			return "message_id";
		else if ( stName == nameTranRelations )
			return "";
		else if ( stName == nameTransformationDefinitions )
			return "";
		else if ( stName == nameLdapSearchParameters )
			return "";
		else if ( stName == nameLdapStructureEntries )
			return "";
		else if ( stName == nameTransformationsTab_New )
			return "transformation_id";
		else if ( stName == nameUserGroup )
			return "";
		else if ( stName == nameWebServiceFunctionTransformations )
			return "webservice_id";
		else if ( stName == nameXMLDtdLinks )
			return "xmldtdlinksid";
		else if ( stName == nameOperationPipelineActivities )
			return "operationactivity_id";
		else if ( stName == nameCommonSourceOperations )
			return "id";
		else 
		if ( stName == nameSalesforceQueries )
			return "salesforcequery_id";
		if ( *(stName.end()-1) == 's' ) {
			return stName.substr(0, stName.size()-1) + "_id";
		}
		else
			return stName + "_id";
	}

	                                                    
	                                                                                    
	string getIdName(const string& stName) {
		string stIdName(getIdNameImpl(stName));
		ToLower(stIdName);
		return stIdName;
	}

	string getNameColumnName(int iEntityTypeId) {
		const string& entityName =  GetEntityNameByEntityIdSingular(iEntityTypeId);
		return entityName + "_name";
	}

	string GetUserPermissionTableName(const string& stName) {
		string stTableName( getTableNameImpl(stName) );
		if ( stName == nameTransformationsTab_New )
			stTableName = "TransformationsTab";

		if ( stName == nameSchedules )
			return "UserPermissionsForSchedulesTab";

		if ( stName == nameXMLDtd )
			return "UserPermissionsForXmlDtdsTab";

		return "UserPermissionsFor" + stTableName;
	}

	string GetUserPermissionTableName(int iEntityTypeId) {
		return GetUserPermissionTableName( GetEntityNameByEntityId(iEntityTypeId) );
	}

	string GetGroupPermissionTableName(const string& stName) {
		string stTableName( getTableNameImpl(stName) );

		if ( stName == nameTransformationsTab_New )
			stTableName = "TransformationsTab";

		if ( stName == nameSchedules )
			return "GroupPermissionsForSchedulesTab";

		if ( stName == nameXMLDtd )
			return "GroupPermissionsForXmlDtdsTab";

		return "GroupPermissionsFor" + stTableName;
	}

	string GetGroupPermissionTableName(int iEntityTypeId) {
		return GetGroupPermissionTableName( GetEntityNameByEntityId(iEntityTypeId) );
	}

	string GetGuidTableName(const string& stName) {
		string stTableName( getTableNameImpl(stName) );

		if ( stName == nameTransformationsTab_New )
			stTableName = "TransformationsTab";

		if ( stName == nameSchedules )
			return "GuidsForSchedulesTab";

		if ( stName == nameXMLDtd )
			return "GuidsForXmlDtdsTab";

		return "GuidsFor" + stTableName;
	}

	string GetGuidViewName(int iEntityTypeId) {
		string stGuidTableName( GetGuidTableName(iEntityTypeId) );
		return stGuidTableName.replace(stGuidTableName.end()-3, stGuidTableName.end(), "View");
	}

	string GetGuidTableName(int iEntityTypeId) {
		return GetGuidTableName( GetEntityNameByEntityId(iEntityTypeId) );
	}

	std::string GetInterchangeProjectEntityTab(int iEntityTypeId) {

		if ( iEntityTypeId == TransformationEntityId )
			return "InterchangeProjectTransformationsTab";
		else if ( iEntityTypeId == ScheduleEntityId )
			return "InterchangeProjectSchedulesTab";

		                      
		  
		return std::string("InterchangeProject" + getTableName(GetEntityNameByEntityId(iEntityTypeId)));
	}

	                                       
	string getOrderBy(const string& stName) {
		if ( stName == nameFolders )
			return " ORDER BY ContentsEntityType_ID";
		else if ( stName == nameFolderItem )
			return " ORDER BY EntityType_ID, ParentFolder_ID";
		else if ( stName == nameDBTables )
			return " ORDER BY Transformation_ID, TablesType";
		else if ( stName == nameDBFields )
			return " ORDER BY Transformation_ID";
		else if ( stName == nameSqlScript )
			return " ORDER BY Transformation_ID";
		else if ( stName == nameTranRelations )
			return " ORDER BY Transformation_ID";
		else if ( stName == nameTransFilter )
			return " ORDER BY Transformation_ID";
		else if ( stName == nameTransformationDefinitions )
			return " ORDER BY Transformation_ID";
		else if ( stName == nameDocumentDefinitions )
			return " ORDER BY Document_ID";
		else if ( stName == nameTransformationChainLinks )
			return " ORDER BY Operation_ID, TransformationChainLink_ID";
		else
			return string();
	}

	string GetEntityNameByEntityId(int iEntityId) {
		switch(iEntityId) {
		case FolderEntityId:
			return nameFolders;
		case SourceEntityId:
			return nameSources;
		case TargetEntityId:
			return nameTargets;
		case TransformationEntityId:
			return nameTransformationsTab_New ;
		case ScheduleEntityId:
			return nameSchedules;
		case OperationEntityId:
			return nameOperations;
		case ProcessEntityId:
			throw CrException("Internal Error: Processes need special handling. Not implemented");
		case 8:
			throw CrException("Entity Id 8 (Account) is not used anymore, please use 18 instead (Partner).");
		case WebServiceEntityId:
			return nameWebServices;
		case CallRestrictionEntityId:
			return nameCallRestrictions;
		case GroupEntityId:
			return nameGroups;
		case UserEntityId:
			return nameUsers;
                          
                         
                          
                             
		case EmailMessageEntityId:
			return nameMessages;
		case DocumentEntityId:
			return nameDocuments;
		case XmlEntityId:
			return nameXMLDtd;
		case InterchangeProjectEntityId:
			return nameInterchangeProjects;
		case WsdlFileEntityId:
			return nameWsdl;
		case JmsMessageEntityId:
			return nameJmsMessage;
		case ProjectVariableEntityId: return nameProjectVariables; case XsltTransformEntityId: return nameXsltTransforms; case ScriptEntityId:
			return nameScripts;
		case HttpEndpointEntityId:
			return nameHttpEndpoints;
		case SalesforceConnectorEntityId: return nameSalesforceConnectors;
		case SalesforceQueryEntityId:
			return nameSalesforceQueries;
		case SalesforceUpsertEntityId:
			return nameSalesforceUpserts;
		case SalesforceCreateEntityId:
			return nameSalesforceCreates;
		case SalesforceUpdateEntityId:
			return nameSalesforceUpdates;
		case SalesforceBulkLoadEntityId:
			return nameSalesforceBulkLoads;
		case SalesforceDeleteEntityId:
			return nameSalesforceDeletes;
		default:
			throw CrException("Internal Error: Unknown entity id " + toString(iEntityId));
		}
	}

	int GetEntityIdByName(const string& stName) {
		if ( stName == nameFolders )
			return FolderEntityId;
		if ( stName == nameSources )
			return SourceEntityId;
		if ( stName == nameTargets )
			return TargetEntityId;
		if ( stName == nameTransformationsTab_New )
			return TransformationEntityId;
		if ( stName == nameSchedules )
			return ScheduleEntityId;
		if ( stName == nameOperations )
			return OperationEntityId;
		if ( stName == nameWebServices )
			return WebServiceEntityId;
		if ( stName == nameGroups )
			return GroupEntityId;
		if ( stName == nameUsers )
			return UserEntityId;
		if ( stName == nameMessages )
			return EmailMessageEntityId;
		if ( stName == nameDocuments )
			return DocumentEntityId;
		if ( stName == nameXMLDtd )
			return XmlEntityId;
		if ( stName == nameInterchangeProjects )
			return InterchangeProjectEntityId;
		if ( stName == nameWsdl )
			return WsdlFileEntityId;
		if ( stName == nameJmsMessage )
			return JmsMessageEntityId;
		if ( stName == nameProjectVariables ) return ProjectVariableEntityId; if ( stName == nameXsltTransforms ) return XsltTransformEntityId; if ( stName == nameScripts )
			return ScriptEntityId;
		if ( stName == nameHttpEndpoints )
			return HttpEndpointEntityId;
		if ( stName == nameSalesforceConnectors ) return SalesforceConnectorEntityId;
		if ( stName == nameSalesforceQueries )
			return SalesforceQueryEntityId;
		if ( stName == nameSalesforceUpserts )
			return SalesforceUpsertEntityId;
		if ( stName == nameSalesforceCreates )
			return SalesforceCreateEntityId;
		if ( stName == nameSalesforceUpdates )
			return SalesforceUpdateEntityId;
		if ( stName == nameSalesforceBulkLoads )
			return SalesforceBulkLoadEntityId;
		if ( stName == nameSalesforceDeletes )
			return SalesforceDeleteEntityId;
		return 0;
	}

	string GetEntityNameByEntityIdSingular(int iEntityId) {
		if ( iEntityId == SalesforceQueryEntityId )
			return "SalesforceQuery";
		const string& stName = GetEntityNameByEntityId(iEntityId);
		if ( !stName.empty() && *(stName.end()-1) == 's' )
			return stName.substr(0, stName.size()-1);
		return stName;
	}
	
	std::string GetPropertyTableName(int iEntityTypeId) {
		switch(iEntityTypeId) {
			case TransformationEntityId:
				return "TransformationPropertiesTab";
			default:
				break;
		}
		                       
		return GetEntityNameByEntityIdSingular(iEntityTypeId) + "PropertiesTab";
	}

}                   

