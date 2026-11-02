/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <map>
#include <memory>	           

#include <konga/Utils/CrException.h>

#include "TableKeyInfo.h"
#include "DependentTableInfo.h"
#include "DatabaseTableNameMappings.h"

#include "DbRelations.h"

using std::string;
using std::vector;
using std::map;

namespace konga {
	namespace dbrelations_impl {

		typedef map<string, TableKeyInfo*> TableKeyInfoMapT;
		typedef map<string, vector<DependentTableInfo>* > DepTablesMapT;
		typedef map<string, string> NameColumnMapT;

		class Impl {
		public:
			Impl(void) {}
			~Impl(void) {
				{
					TableKeyInfoMapT::iterator it = m_tableKeyInfoMap.begin();
					for(; it != m_tableKeyInfoMap.end(); ++ it ) {
						if ( it->second ) delete it->second;
					}
				}

				{
					DepTablesMapT::iterator it = m_depTableInfoMap.begin();
					for(; it != m_depTableInfoMap.end(); ++ it ) {
						if ( it->second ) delete it->second;
					}
				}
			}

			                                   
			TableKeyInfoMapT m_tableKeyInfoMap;

			                                         
			                                                                                           
			DepTablesMapT m_depTableInfoMap;

			                                      
			NameColumnMapT m_nameColumnMap;
		};

	}
}

using namespace konga;
using namespace konga::dbrelations_impl;

DbRelations::DbRelations(Impl* impl) : m_pImpl( impl ) {
}

DbRelations::~DbRelations(void) {
	delete m_pImpl;
}

const TableKeyInfo& DbRelations::GetTableKeyInfo(const std::string& stTableName) const {
	TableKeyInfoMapT::const_iterator cit = m_pImpl->m_tableKeyInfoMap.find(stTableName);
	if ( cit == m_pImpl->m_tableKeyInfoMap.end() )
		throw CrException("Could not find any key information for the table " + stTableName);

	return *(cit->second);
}

const std::vector<DependentTableInfo>& DbRelations::GetDependentTables(const std::string& stTableName) const {

	DepTablesMapT::const_iterator cit = m_pImpl->m_depTableInfoMap.find(stTableName);
	if ( cit != m_pImpl->m_depTableInfoMap.end() )
		return *(cit->second);

	                                      
	std::auto_ptr< vector<DependentTableInfo> > pDependentTables( new vector<DependentTableInfo>() );

	TableKeyInfoMapT::const_iterator citKeyMap = m_pImpl->m_tableKeyInfoMap.begin();
	for(; citKeyMap != m_pImpl->m_tableKeyInfoMap.end(); ++citKeyMap ) {
		const vector<KeyInfo>& vKeyInfo = citKeyMap->second->GetKeyInfo();
		vector<KeyInfo>::const_iterator citKeyInfo = vKeyInfo.begin();
		for(; citKeyInfo != vKeyInfo.end(); ++citKeyInfo ) {
			if ( citKeyInfo->IsDependent() ) {
				if ( citKeyInfo->GetParentTableName() == stTableName ) {
					pDependentTables->push_back( DependentTableInfo(citKeyMap->first, citKeyInfo->GetColumnName()) );
				}
			}
		}
	}

	m_pImpl->m_depTableInfoMap[stTableName] = pDependentTables.get();	                           
	return *(pDependentTables.release());	                       
}

string DbRelations::GetNameColumnName(const std::string& stTableName) const {
	NameColumnMapT::const_iterator cit = m_pImpl->m_nameColumnMap.find(stTableName);
	if ( cit == m_pImpl->m_nameColumnMap.end() )
		return string("");
	return cit->second;
}

DbRelations* DbRelations::GetTranDbRelations() {
	std::auto_ptr<Impl> pimpl( new Impl() );

	vector<KeyInfo> vKeyInfo;

	                    
	                                       
	vKeyInfo.clear();
	pimpl->m_tableKeyInfoMap[ getTableName(nameCallRestrictions) ] = new TableKeyInfo(vKeyInfo);

	                              
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameTransformationsTab_New), getTableName(nameTransformationsTab_New), true) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameDBFields) ] = new TableKeyInfo(vKeyInfo);

	                              
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameTransformationsTab_New), getTableName(nameTransformationsTab_New), true) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameDBTables) ] = new TableKeyInfo(vKeyInfo);

	                                    
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameDocuments), getTableName(nameDocuments), true) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameDocumentDefinitions) ] = new TableKeyInfo(vKeyInfo);

	            
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameDocuments), GetEntityIdByName(nameDocuments)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameDocuments) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameDocuments) ] = "document_name";

	                         
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameDocuments), getTableName(nameDocuments), true) );
	pimpl->m_tableKeyInfoMap[ GetPropertyTableName(GetEntityIdByName(nameDocuments)) ] = new TableKeyInfo(vKeyInfo);

	          
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameFolders), GetEntityIdByName(nameFolders)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameFolders) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameFolders) ] = "folder_name";

	                                                                       
	vKeyInfo.clear();
	pimpl->m_tableKeyInfoMap[ getTableName(nameFolderItem) ] = new TableKeyInfo(vKeyInfo);

	         
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameGroups), GetEntityIdByName(nameGroups)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameGroups) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameGroups) ] = "group_name";

	                       
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameInterchangeProjects), GetEntityIdByName(nameInterchangeProjects)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameInterchangeProjects) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameInterchangeProjects) ] = "interchangeproject_name";

	           
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameMessages), GetEntityIdByName(nameMessages)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameMessages) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameMessages) ] = "message_name";

	             
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameOperations), GetEntityIdByName(nameOperations)) );
	vKeyInfo.push_back( KeyInfo(getIdName(nameSources), getTableName(nameSources)) );
	vKeyInfo.push_back( KeyInfo(getIdName(nameTargets), getTableName(nameTargets)) );
	vKeyInfo.push_back( KeyInfo(getIdName(nameSchedules), getTableName(nameSchedules)) );
	vKeyInfo.push_back( KeyInfo(getIdName(nameTransformationsTab_New), getTableName(nameTransformationsTab_New)) );
	vKeyInfo.push_back( KeyInfo("success_operation_id", getTableName(nameOperations)) );
	vKeyInfo.push_back( KeyInfo("success_message_id", getTableName(nameMessages)) );
	vKeyInfo.push_back( KeyInfo("failure_operation_id", getTableName(nameOperations)) );
	vKeyInfo.push_back( KeyInfo("failure_message_id", getTableName(nameMessages)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameOperations) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameOperations) ] = "operation_name";

	                         
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameOperations), getTableName(nameOperations), true) );
	pimpl->m_tableKeyInfoMap[ GetPropertyTableName(GetEntityIdByName(nameOperations)) ] = new TableKeyInfo(vKeyInfo);

	                          
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameOperations), getTableName(nameOperations), true) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameOperationGraphLayout) ] = new TableKeyInfo(vKeyInfo);

	                       
   vKeyInfo.clear();
   vKeyInfo.push_back(KeyInfo(getIdName(nameSalesforceConnectors), GetEntityIdByName(nameSalesforceConnectors)));
   pimpl->m_tableKeyInfoMap[getTableName(nameSalesforceConnectors)] = new TableKeyInfo(vKeyInfo);
   pimpl->m_nameColumnMap[getTableName(nameSalesforceConnectors)] = "salesforceconnector_name";
                       
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameSalesforceQueries), GetEntityIdByName(nameSalesforceQueries)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameSalesforceQueries) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameSalesforceQueries) ] = "salesforcequery_name";

	                    
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameSalesforceUpserts), GetEntityIdByName(nameSalesforceUpserts)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameSalesforceUpserts) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameSalesforceUpserts) ] = "salesforceupsert_name";

	                    
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameSalesforceCreates), GetEntityIdByName(nameSalesforceCreates)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameSalesforceCreates) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameSalesforceCreates) ] = "salesforcecreate_name";

	                    
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameSalesforceUpdates), GetEntityIdByName(nameSalesforceUpdates)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameSalesforceUpdates) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameSalesforceUpdates) ] = "salesforceupdates_name";

	                    
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameSalesforceDeletes), GetEntityIdByName(nameSalesforceDeletes)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameSalesforceDeletes) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameSalesforceDeletes) ] = "salesforcedelete_name";

	                      
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameSalesforceBulkLoads), GetEntityIdByName(nameSalesforceBulkLoads)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameSalesforceBulkLoads) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameSalesforceBulkLoads) ] = "salesforcebulkload_name";

	                
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameHttpEndpoints), GetEntityIdByName(nameHttpEndpoints)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameHttpEndpoints) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameHttpEndpoints) ] = "httpendpoint_name";

	                                          
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameHttpEndpoints), getTableName(nameHttpEndpoints), true) );
	vKeyInfo.push_back( KeyInfo(getIdName(nameOperations), getTableName(nameOperations)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameHttpEndpointOperations) ] = new TableKeyInfo(vKeyInfo);

	               
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameJmsMessage), GetEntityIdByName(nameJmsMessage)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameJmsMessage) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameJmsMessage) ] = "jmsmessagename";

	            
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameSchedules), GetEntityIdByName(nameSchedules)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameSchedules) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameSchedules) ] = "schedule_name";

	          
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameSources), GetEntityIdByName(nameSources)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameSources) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameSources) ] = "source_name";

	                       
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameSources), getTableName(nameSources), true) );
	pimpl->m_tableKeyInfoMap[ GetPropertyTableName(GetEntityIdByName(nameSources)) ] = new TableKeyInfo(vKeyInfo);

	          
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameScripts), GetEntityIdByName(nameScripts)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameScripts) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameScripts) ] = "script_name";
    vKeyInfo.clear();
    vKeyInfo.push_back(KeyInfo(getIdName(nameXsltTransforms), GetEntityIdByName(nameXsltTransforms)));
    pimpl->m_tableKeyInfoMap[getTableName(nameXsltTransforms)] = new TableKeyInfo(vKeyInfo);
    pimpl->m_nameColumnMap[getTableName(nameXsltTransforms)] = "xslttransform_name";
    {
        vector<KeyInfo> keys;
        keys.push_back(KeyInfo(getIdName(nameProjectVariables), GetEntityIdByName(nameProjectVariables)));
        pimpl->m_tableKeyInfoMap[getTableName(nameProjectVariables)] = new TableKeyInfo(keys);
        pimpl->m_nameColumnMap[getTableName(nameProjectVariables)] = "projectvariable_name";
        keys.clear();
        keys.push_back(KeyInfo(getIdName(nameProjectVariables), getTableName(nameProjectVariables), true));
        pimpl->m_tableKeyInfoMap[GetPropertyTableName(GetEntityIdByName(nameProjectVariables))] = new TableKeyInfo(keys);
    }
    vKeyInfo.clear();
    vKeyInfo.push_back(KeyInfo(getIdName(nameXsltTransforms), getTableName(nameXsltTransforms), true));
    pimpl->m_tableKeyInfoMap[GetPropertyTableName(GetEntityIdByName(nameXsltTransforms))] = new TableKeyInfo(vKeyInfo);

	                       
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameScripts), getTableName(nameScripts), true) );
	pimpl->m_tableKeyInfoMap[ GetPropertyTableName(GetEntityIdByName(nameScripts)) ] = new TableKeyInfo(vKeyInfo);

	                                
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameTransformationsTab_New), getTableName(nameTransformationsTab_New), true) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameSqlScript) ] = new TableKeyInfo(vKeyInfo);

	          
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameTargets), GetEntityIdByName(nameTargets)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameTargets) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameTargets) ] = "target_name";

	                       
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameTargets), getTableName(nameTargets), true) );
	pimpl->m_tableKeyInfoMap[ GetPropertyTableName(GetEntityIdByName(nameTargets)) ] = new TableKeyInfo(vKeyInfo);

	                                   
	  
	                                                                                          
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameTransformationsTab_New), getTableName(nameTransformationsTab_New), true) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameTranRelations) ] = new TableKeyInfo(vKeyInfo);

	                                  
	  
	pimpl->m_tableKeyInfoMap[ getTableName(nameTransFilter) ] = new TableKeyInfo(vKeyInfo);

	                                                
	  
	pimpl->m_tableKeyInfoMap[ getTableName(nameTransformationDefinitions) ] = new TableKeyInfo(vKeyInfo);

	                                             
	  
	pimpl->m_tableKeyInfoMap[ getTableName(nameLdapSearchParameters) ] = new TableKeyInfo(vKeyInfo);

	                                             
	  
	pimpl->m_tableKeyInfoMap[ getTableName(nameLdapStructureEntries) ] = new TableKeyInfo(vKeyInfo);

	                  
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameTransformationsTab_New), GetEntityIdByName(nameTransformationsTab_New)) );
	vKeyInfo.push_back( KeyInfo("sourcejmsmessageid", getTableName(nameJmsMessage)) );
	vKeyInfo.push_back( KeyInfo("targetjmsmessageid", getTableName(nameJmsMessage)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameTransformationsTab_New) ] = new TableKeyInfo(vKeyInfo);
	                                                                                               
	                                                                                                    
	                                          
	pimpl->m_nameColumnMap[ getTableName(nameTransformationsTab_New) ] = "transformation_name";

	                              
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameOperations), getTableName(nameOperations), true) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameTransformationChainLinks) ] = new TableKeyInfo(vKeyInfo);
	                                                                                                  
	                                                                                

	                               
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameTransformationsTab_New), getTableName(nameTransformationsTab_New), true) );
	pimpl->m_tableKeyInfoMap[ GetPropertyTableName(GetEntityIdByName(nameTransformationsTab_New)) ] = new TableKeyInfo(vKeyInfo);

	              
	  
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameWebServices), GetEntityIdByName(nameWebServices)) );
	vKeyInfo.push_back( KeyInfo(getIdName(nameWsdl), getTableName(nameWsdl), false) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameWebServices) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameWebServices) ] = "webservice_name";

	                           
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameWebServices), getTableName(nameWebServices), true) );
	pimpl->m_tableKeyInfoMap[ GetPropertyTableName(GetEntityIdByName(nameWebServices)) ] = new TableKeyInfo(vKeyInfo);

	                                        
	                                                                                       
	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameWebServices), getTableName(nameWebServices), false) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameWebServiceFunctionTransformations) ] = new TableKeyInfo(vKeyInfo);

	vKeyInfo.clear();
	vKeyInfo.push_back( KeyInfo(getIdName(nameWsdl), GetEntityIdByName(nameWsdl)) );
	pimpl->m_tableKeyInfoMap[ getTableName(nameWsdl) ] = new TableKeyInfo(vKeyInfo);
	pimpl->m_nameColumnMap[ getTableName(nameWsdl) ] = "wsdlname";

	return new DbRelations(pimpl.release());
}

