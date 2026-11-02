/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <string>
#include <memory>	           
#include <exception>

#include <konga/Utils/CrException.h>
#include <konga/Utils/AutoPtrCleanup.h>
#include <konga/Utils/DBConnectionInfo.h>

#include <konga/Db/DbUtils/DbUtils.h>			                                                   
#include <konga/Db/DbUtils/TableColumnsOdbc.h>

#include <konga/Db/SchemaInfo/SchemaInfo.h>
#include <konga/Db/SchemaInfo/DatabaseObject.h>
#include <konga/Db/SchemaInfo/DatabaseColumn.h>

#include <konga/Db/TranDbUtils/TranDbUtils.h>	                               

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/OperationContext/DefaultOperationDataElementHolder.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>
#include <konga/UiBackend/WebServices/Utils/WsAllocations.h>

#include "../WsDatabaseObject.hpp"
#include "../WsDatabaseInfo.hpp"

#include "OdbcInfoProviderImpl.h"

using namespace std;
using namespace konga;

namespace {
	template<typename T>
	void ColumnCollectionToWsColumnArr(const T& cols, ArrayOfWsDatabaseColumn& wsColArr) {
		const int nCols = cols.size();

		                           
		WsDatabaseColumn** arrWsDatabaseColumn = allocate_pointer_array<WsDatabaseColumn>(nCols);
		AutoDeletePointerArray<WsDatabaseColumn> adopt_arrWsDatabaseColumn(arrWsDatabaseColumn, nCols);

		typename T::const_iterator cit = cols.begin();
		xsd__boolean xsd_true = true_, xsd_false = false_;
		for( int i = 0; i < nCols; ++i, ++cit ) {
			auto_ptr<WsDatabaseColumn> pWsDatabaseColumn( new WsDatabaseColumn() );

			const typename T::value_type& t = *cit;
			pWsDatabaseColumn->setName(const_cast<xsd__string>(t->GetName().c_str()), true);
			pWsDatabaseColumn->setType(const_cast<xsd__string>(t->GetType().c_str()), true);
			pWsDatabaseColumn->setSqlType(t->GetSqlType());
			pWsDatabaseColumn->setDefaultValue(const_cast<xsd__string>(t->GetDefaultValue().c_str()), true);
			pWsDatabaseColumn->setIsPrimaryKey(t->IsPrimaryKey() ? &xsd_true : &xsd_false, true);
			pWsDatabaseColumn->setIsNullable(t->IsNullable() ? &xsd_true : &xsd_false, true);
			pWsDatabaseColumn->setCharacterSize(t->GetCharacterSize());
			pWsDatabaseColumn->setDecimalDigits(t->GetDecimalDigits());

			arrWsDatabaseColumn[i] = pWsDatabaseColumn.release();
		}
		wsColArr.item->set(arrWsDatabaseColumn, nCols);
	}

	void AddSemiColonIfNeeded(string& sConnStr) {
		if ( sConnStr.empty() )
			return;
		if ( *(sConnStr.end()-1) != ';' )
			sConnStr += ";";
	}

	string GetPassword(const string& sGuid, int iEntityTypeId) {
		SmartDbPtr<DbConnection> pConnection( konga::GetBackEndDbConnection("TranDb") );
		                      
		DbGuidToIdMapper guidMapper(pConnection);
		long lId = guidMapper.GetId(sGuid, iEntityTypeId);

		                                                                                                   
		if ( lId == DbGuidToIdMapper::invalid_id )
			return "";

		return konga::getDecryptedPasswordFromDb(lId, iEntityTypeId);
	}

	string GetConnectionStringByGuid(const UserInfo& currentUser, const string& sConnectionString_in,
		xsd__string SourceGuid, xsd__string TargetGuid)
	{
		DefaultOperationDataElementHolder deHolder(currentUser);
		string sConnectionString = deHolder.replaceOperationDataElementInString(sConnectionString_in);
		string stRetVal;
		                                                                                                   
		if ( konga::HasPwd(sConnectionString) ) {
			stRetVal = sConnectionString;
		}
		else if( SourceGuid && *SourceGuid ) {
			stRetVal = sConnectionString;
			konga::AppendConnectString(stRetVal, "PWD="
				+ deHolder.replaceOperationDataElementInString( GetPassword(SourceGuid, SourceEntityId) ));
		}
		else if( TargetGuid && *TargetGuid ) {
			stRetVal = sConnectionString;
			konga::AppendConnectString(stRetVal, "PWD="
				+ deHolder.replaceOperationDataElementInString( GetPassword(TargetGuid, TargetEntityId) ));
		}
		else {
			                                                                           
			                                       
		}
		stRetVal = TrimString(stRetVal);
		AddSemiColonIfNeeded(stRetVal);
		return stRetVal;

		                                                                                           
		  
                                    

                                                  
                                    
                                 
                                                                   
                     
                        

                                                                             
                     
                      

                                                                             
                     
                      

                                                                          
                     
                   

                                                                          
                     
                   

                  
    
	}
}

OdbcInfoProviderImpl::OdbcInfoProviderImpl(const xsd__string user, const xsd__string password)
: WsImplBaseAuthenticator(user, password)
{
}

namespace {
	WsDatabaseObject* AllocateWsDatabaseObject(DatabaseObject& dbObject) {
		auto_ptr<WsDatabaseObject> pWsDatabaseObject(new WsDatabaseObject());
		pWsDatabaseObject->setObjectType( const_cast<xsd__string>(dbObject.GetObjectType().c_str()) , true);
		pWsDatabaseObject->setObjectName( const_cast<xsd__string>(dbObject.GetName().c_str()), true);
		pWsDatabaseObject->setSchemaName( const_cast<xsd__string>(dbObject.GetSchemaName().c_str()), true);

		              
		const vector<DatabaseColumn*>& vCols = dbObject.GetColumns();
		auto_ptr<ArrayOfWsDatabaseColumn> pColumns( new ArrayOfWsDatabaseColumn() );

		ColumnCollectionToWsColumnArr< vector<DatabaseColumn*> >(vCols, *pColumns);
		pWsDatabaseObject->Columns = pColumns.release();	                          

		return pWsDatabaseObject.release();
	}

	WsDatabaseInfo* AllocateWsDatabaseInfo(const vector<DatabaseObject*>& dbObjs, const string& beginQuoteChar, const string& endQuoteChar) {
		                                              
		const int nDbObjectSize = dbObjs.size();
		WsDatabaseObject** arrWsDatabaseObject = allocate_pointer_array<WsDatabaseObject>(nDbObjectSize);
		AutoDeletePointerArray<WsDatabaseObject> adopt_arrWsDatabaseObject(arrWsDatabaseObject, nDbObjectSize);

		int iObj = 0;
		for(vector<DatabaseObject*>::const_iterator citObject = dbObjs.begin(); citObject != dbObjs.end(); ++citObject, ++iObj) {
			arrWsDatabaseObject[iObj] = AllocateWsDatabaseObject(*(*citObject));
		}
		auto_ptr<WsDatabaseInfo> pRetVal( new WsDatabaseInfo() );
		pRetVal->setDbInfo( new ArrayOfWsDatabaseObject() );	                         
		pRetVal->DbInfo->item->set(arrWsDatabaseObject, nDbObjectSize);	                                  

		pRetVal->setQuoteBegin(const_cast<xsd__string>(beginQuoteChar.c_str()), true);
		pRetVal->setQuoteEnd(const_cast<xsd__string>(endQuoteChar.c_str()), true);
		return pRetVal.release();
	}
}

WsDatabaseInfo* OdbcInfoProviderImpl::getDatabaseObjects(const xsd__string DriverName,
														 const xsd__string ConnectionString,
														 const xsd__string SourceGuid,
														 const xsd__string TargetGuid,
														 const xsd__string_Array* const pObjectTypes,
														 xsd__string TableSearchString,
														 xsd__string SchemaSearchString)
{
	                   
	if ( pObjectTypes == 0 ) {
		throw CrException("BAD DATA: Provided ObjectTypes array is NULL. Internal Error: Probably a client bug.");
	}
	const xsd__string_Array& ObjectTypes = *pObjectTypes;

	int nObjectTypesSize = 0;
	const xsd__string* pObjectTypesArr = ObjectTypes.get(nObjectTypesSize);

	if( !pObjectTypesArr || !nObjectTypesSize ) throw CrException("Must specify object type(s).");
	string stTableSearchString( toString(TableSearchString) );
	string stSchemaSearchString( toString(SchemaSearchString) );
	if ( stSchemaSearchString.empty() ) stSchemaSearchString = "%";
	if ( stTableSearchString.empty() ) stTableSearchString = "%";
	string stObjTypes;

	const string stDriverName( toString(DriverName) );
	const string& stConnectionString = GetConnectionStringByGuid(m_UserInfo, ConnectionString?ConnectionString:"", SourceGuid, TargetGuid);

	try {		
		SchemaInfo schema(stConnectionString);
	
		                                                                 
		map<string, bool> mObjTypes;
		for(int i=0; i<nObjectTypesSize; ++i) {
			if( i ) stObjTypes += ", ";
			stObjTypes += pObjectTypesArr[i];
			mObjTypes[ pObjectTypesArr[i] ] = true;
		}
	
		vector<DatabaseObject*> dbObjs;
		schema.GetObjects(dbObjs, mObjTypes, stTableSearchString, false, stSchemaSearchString);
		AutoPtrDeleteVector<DatabaseObject> adopt_dbObjs(dbObjs);

		const string& quoteCharacter = schema.GetQuoteCharacter();
		return AllocateWsDatabaseInfo(dbObjs, quoteCharacter, quoteCharacter);
	} catch( const CrException& e ) {
		throw CrException( e.GetErrorString() + "\nAdditional info: Failed to read schema from database '" + konga::ReplacePassword(stConnectionString, "<REMOVED>") + 
						  "', for the object types " + stObjTypes + 
						  " Using table search string '" + stTableSearchString + 
						  "' and schema search string '" + stSchemaSearchString + "'.");
	} catch( const std::exception& e ) {
		throw CrException("Failed to read schema from database '" + konga::ReplacePassword(stConnectionString, "<REMOVED>") + 
						  "', for the object types " + stObjTypes + 
						  " Using table search string '" + stTableSearchString + 
						  "' and schema search string '" + stSchemaSearchString + 
						  "'. Reason:\n" + string(e.what()?e.what():"<no reason given>"));
	}
}

WsDatabaseInfo* OdbcInfoProviderImpl::getTableInfo(const xsd__string DriverName,
												   const xsd__string ConnectionString,
												   const xsd__string SourceGuid,
												   const xsd__string TargetGuid,
												   const WsDatabaseObject_Array* const tables
												   )
{

	if ( tables == 0 ) {
		throw InternalErrorException("BAD DATA: TableNames array is NULL. Internal Error: Probably a client bug.");
	}

	                   
	int nTables = 0;
	WsDatabaseObject** arrTables = tables->get(nTables);
	if( !arrTables || nTables< 1 )
		throw InternalErrorException("Must specify at least one table or view name.");

	const string& stConnectionString = GetConnectionStringByGuid(m_UserInfo, ConnectionString?ConnectionString:"", SourceGuid, TargetGuid);
	const string stDriverName( toString(DriverName) );

	try {
		SchemaInfo schema(stConnectionString);
	
		                                                                 
		map<string, bool> mObjTypes;
		mObjTypes[ DBTABLE_TYPE_TABLE ] = true;
		mObjTypes[ DBTABLE_TYPE_SYSTEM_TABLE ] = true;
		mObjTypes[ DBTABLE_TYPE_VIEW ] = true;

		WsDatabaseObject** arrWsDatabaseObject = allocate_pointer_array<WsDatabaseObject>(nTables);
		AutoDeletePointerArray<WsDatabaseObject> adopt_arrWsDatabaseObject(arrWsDatabaseObject, nTables);

		for(int iTable = 0; iTable < nTables; ++iTable) {
			WsDatabaseObject* pTable = arrTables[iTable];
			if ( pTable == 0 ) continue;

			const string sTableName(toString(pTable->getObjectName()));
			const string sOwner(toString(pTable->getSchemaName()));

			vector<DatabaseObject*> dbObjs;
			schema.GetObjects(dbObjs, mObjTypes, sTableName, true, sOwner);
			AutoPtrDeleteVector<DatabaseObject> adopt_dbObjs(dbObjs);

			for(vector<DatabaseObject*>::iterator it = dbObjs.begin(); it != dbObjs.end(); ++it) {
				DatabaseObject* dbObj = *it;
				                                                                                  
				                                                            
				if ( stricmp(dbObj->GetName().c_str(), sTableName.c_str()) == 0 ) {
					arrWsDatabaseObject[iTable] = AllocateWsDatabaseObject(*dbObj);
				}
			}
		}

		auto_ptr<ArrayOfWsDatabaseObject> pArrayOfWsDatabaseObject( new ArrayOfWsDatabaseObject() );
		pArrayOfWsDatabaseObject->item->set(arrWsDatabaseObject, nTables);

		auto_ptr<WsDatabaseInfo> pRetVal( new WsDatabaseInfo() );
		pRetVal->setDbInfo(pArrayOfWsDatabaseObject.release());	                          
		const string& tmp = schema.GetQuoteCharacter();
		if ( !tmp.empty() ) {
			pRetVal->setQuoteBegin(const_cast<xsd__string>(tmp.c_str()), true);
			pRetVal->setQuoteEnd(const_cast<xsd__string>(tmp.c_str()), true);
		}

		return pRetVal.release();
	} catch( const CrException& e ) {
		throw CrException("Failed to read schema from database '" + stConnectionString + 
						  "', for the object types TABLE & VIEW." + 
						  " Reason:\n" + e.GetErrorString());
	} catch( const std::exception& e ) {
		throw CrException("Failed to read schema from database '" + stConnectionString + 
						  "', for the object types TABLE & VIEW." + 
						  " Reason:\n" + string(e.what()?e.what():"<no reason given>"));
	}
}

namespace {

	const string QUOTE_UNDEFINED("<undefinded>");

	WsOdbcDriverDescriptor* AllocateWsOdbcDriverDescriptor(const string& sDriverName) {
		std::auto_ptr<WsOdbcDriverDescriptor> pWsOdbcDriverDescriptor( new WsOdbcDriverDescriptor() );

		pWsOdbcDriverDescriptor->setName(const_cast<xsd__string>(sDriverName.c_str()), true);

		const string& beginQuote = konga::GetQuoteBeginForDriver(sDriverName, QUOTE_UNDEFINED);
		if ( beginQuote != QUOTE_UNDEFINED ) {
			pWsOdbcDriverDescriptor->setQuoteBegin(const_cast<xsd__string>(beginQuote.c_str()), true);
		}

		const string& endQuote = konga::GetQuoteEndForDriver(sDriverName, QUOTE_UNDEFINED);
		if ( endQuote != QUOTE_UNDEFINED ) {
			pWsOdbcDriverDescriptor->setQuoteEnd(const_cast<xsd__string>(endQuote.c_str()), true);
		}

		return pWsOdbcDriverDescriptor.release();
	}


	class WsOdbcDriverDescriptorCreator {
	public:
		WsOdbcDriverDescriptorCreator(const vector<string>& drivers) : m_vDriverNames(drivers) {}

		int size() const {
			return static_cast<int>(m_vDriverNames.size());
		}

		WsOdbcDriverDescriptor* create(int i) {
			return AllocateWsOdbcDriverDescriptor(m_vDriverNames.at(i));
		}

	private:
		const vector<string>& m_vDriverNames;
	};

	WsDatabaseDriverInfo* AllocateWsDatabaseDriverInfo(const vector<string>& vDriverNames) {
		std::auto_ptr<WsDatabaseDriverInfo> pWsDatabaseDriverInfo( new WsDatabaseDriverInfo() );

		WsOdbcDriverDescriptorCreator creator(vDriverNames);
		pWsDatabaseDriverInfo->setReturnedDrivers(
			allocateArrayOf<ArrayOfWsOdbcDriverDescriptor, WsOdbcDriverDescriptor_Array, WsOdbcDriverDescriptor, WsOdbcDriverDescriptorCreator>(creator)
			);
		return pWsDatabaseDriverInfo.release();
	}
}

WsDatabaseDriverInfo* OdbcInfoProviderImpl::getDrivers() {

	try {

		                             
		vector<string> vDriverNames;
		OdbcColumns odbcInfo;
		if( !odbcInfo.GetDrivers(vDriverNames) )
			throw CrException(odbcInfo.GetLastError());

		return AllocateWsDatabaseDriverInfo(vDriverNames);
	} catch( const CrException& e ) {
		throw CrException("Failed to get a list of supported drivers. Reason:\n" + e.GetErrorString());
	} catch( const std::exception& e ) {
		throw CrException("Failed to get a list of supported drivers. Reason:\n" + 
						  string(e.what()?e.what():"<no reason given>"));
	}
}
