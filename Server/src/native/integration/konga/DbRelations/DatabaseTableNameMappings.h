/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DATABASETABLENAMEMAPPINGS_H
#define KONGA_DATABASETABLENAMEMAPPINGS_H

#include <string>

namespace konga {

	extern const std::string nameCallRestrictions;
	extern const std::string nameCommonSourceOperations;
	extern const std::string nameDBFields;
	extern const std::string nameDBTables;
	extern const std::string nameDocumentDefinitions;
	extern const std::string nameDocuments;
	extern const std::string nameFolderItem;
	extern const std::string nameFolders;
	extern const std::string nameFunctions;
	extern const std::string nameGroups;
	extern const std::string nameHttpEndpoints;
	extern const std::string nameHttpEndpointOperations;
	extern const std::string nameInterchangeProjects;
	extern const std::string nameJmsMessage;
	extern const std::string nameLdapSearchParameters;
	extern const std::string nameLdapStructureEntries;
	extern const std::string nameMessages;
	extern const std::string nameOperationGraphLayout;
	extern const std::string nameOperations;
	extern const std::string nameOperationPipelineActivities;
	extern const std::string nameProcessMessages;
	extern const std::string nameSalesforceConnectors; extern const std::string nameSalesforceQueries;
	extern const std::string nameSalesforceUpserts;
	extern const std::string nameSalesforceCreates;
	extern const std::string nameSalesforceUpdates;
	extern const std::string nameSalesforceBulkLoads;
	extern const std::string nameSalesforceDeletes;
	extern const std::string nameScheduledWorkflows;
	extern const std::string nameSchedules;
	extern const std::string nameProjectVariables; extern const std::string nameXsltTransforms; extern const std::string nameScripts;
	extern const std::string nameSources;
	extern const std::string nameSqlScript;
	extern const std::string nameTargets;
	extern const std::string nameTranRelations;
	extern const std::string nameTransFilter;
	extern const std::string nameTransformationDefinitions;
	extern const std::string nameTransformationChainLinks;
	extern const std::string nameTransformations_v;
	extern const std::string nameTransformationsTab_New;
	extern const std::string nameUserGroup;
	extern const std::string nameUsers;
	extern const std::string nameWebServiceFunctionTransformations;
	extern const std::string nameWebServices;
	extern const std::string nameWsdl;
	extern const std::string nameXMLDtd;
	extern const std::string nameXMLDtdLinks;
	extern const std::string nameWsdlDependencies;

	                                         
	std::string getTableName(const std::string& stName);

	                                                    
	                                                                                    
	std::string getIdName(const std::string& stName);

	                                                              
	std::string getNameColumnName(int iEntityTypeId);

	std::string GetUserPermissionTableName(int iEntityTypeId);

	std::string GetGroupPermissionTableName(int iEntityTypeId);

	std::string GetGuidTableName(int iEntityTypeId);

	std::string GetGuidViewName(int iEntityTypeId);

	std::string GetInterchangeProjectEntityTab(int iEntityTypeId);

	                                        
	                                                    
	std::string GetEntityNameByEntityId(int iEntityTypeId);

	                                            
	                                                      
	int GetEntityIdByName(const std::string& stName);

	std::string GetEntityNameByEntityIdSingular(int iEntityTypeId);

	                                       
	std::string getOrderBy(const std::string& stName);

	                                                       
	               
	std::string GetPropertyTableName(int iEntityTypeId);

}                   

#endif                                     

