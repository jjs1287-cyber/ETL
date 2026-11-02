/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <algorithm>	            
#include <cassert>

#include <konga/Utils/CrException.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/SourceTypeIdConstants.h>

#include <konga/ItemProperties/ItemProperties.h>
#include <konga/ItemProperties/Definitions.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "DeployUtils.h"
#include "SpecialHandlingDeployOperation.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::string;
using std::map;
using std::vector;

Operation::Operation(DbConnection& dbConnection,
					 GuidToIdMapper& guidToIdMapper,
					 const xsd__string_Array& columnNames,
					 const InterchangeDataTableStruct& commonSourceOperations,
					 map<int, string>& oldSchedulePolicyForOperation)
	: m_bDirty(false), m_bSchedulesUpdated(false), m_bJmsSourceUpdated(false),
	m_iOperationIdIndex(-1), m_iSourceIdIndex(-1),
	m_iSuccessOperationIndex(-1), m_iFailureOperationIndex(-1),
	m_iScheduleIdIndex(-1),
	m_dbConnection(dbConnection),
	m_pItemProperties(new ItemProperties(&dbConnection)),
	m_guidToIdMapper(guidToIdMapper),
	m_oldSchedulePolicyForOperation(oldSchedulePolicyForOperation)
{
	try {
		vector<string> vColNames(5);
		vColNames[0] = "operation_id";
		vColNames[1] = "source_id";
		vColNames[2] = "schedule_id";
		vColNames[3] = "success_operation_id";
		vColNames[4] = "failure_operation_id";
		vColPosT vColPos = GetFieldIndexes(columnNames, vColNames);
		m_iOperationIdIndex = vColPos[0];
		m_iSourceIdIndex = vColPos[1];
		m_iScheduleIdIndex = vColPos[2];
		m_iSuccessOperationIndex = vColPos[3];
		m_iFailureOperationIndex = vColPos[4];

		                                                                 
		vInterchangeDataRowStructT::const_iterator cit = commonSourceOperations.m_vRows.begin();
		for(; cit != commonSourceOperations.m_vRows.end(); ++cit ) {
			const string& opId = cit->m_vFields.at(0).str();
			const string& siblingOpId = cit->m_vFields.at(1).str();

			const CommonSourceOperationsMapT::iterator it = m_commonSourceOperationsMap.find(opId);
			if ( it == m_commonSourceOperationsMap.end() ) {
				vector<string> v(1);
				v[0] = siblingOpId;
				m_commonSourceOperationsMap.insert(CommonSourceOperationsMapT::value_type(opId, v));
			}
			else {
				it->second.push_back(siblingOpId);
			}
		}
	}
	catch(...) {
		                                                                                
		if ( m_pItemProperties ) {
			delete m_pItemProperties;
			m_pItemProperties = 0;
		}
		throw;
	}
}

Operation::~Operation() {
	if ( m_pItemProperties ) {
		delete m_pItemProperties;
		m_pItemProperties = 0;
	}
	assert(!isDirty());
}

namespace {
	const string stEmpty("");

	void GetSomeOperationInfo(long& sourceId, long& scheduleId, const long opId, DbConnection& dbConnection) {
		const string sql("SELECT Source_Id, Schedule_Id FROM OperationsTab WHERE Operation_Id = "
			+ toString(opId));
		SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&dbConnection, sql) );
		rs->Open();
		if ( !rs->Next() )
			throw CrException("Internal Error: No entry in OperationsTab for Operation_Id = " + toString(opId));
		sourceId = rs->IsFieldNull(0) ? -1 : atol(rs->GetFieldValue(0).c_str());
		scheduleId = rs->IsFieldNull(1) ? -1 : atol(rs->GetFieldValue(1).c_str());
	}
}

bool Operation::SchedulesWereUpdated() const {
	return m_bSchedulesUpdated;
}

bool Operation::JmsSourcesWereUpdated() const {
	return m_bJmsSourceUpdated;
}

bool Operation::HandleRow(InterchangeDataRowStruct& row) {
	                                                      
	                                                        

	const NullableString& successOp = row.m_vFields.at(m_iSuccessOperationIndex);
	m_stCurrentSuccessOperation = string(successOp==0 ? stEmpty : successOp.str());
	if ( !m_stCurrentSuccessOperation.empty() ) {
		UIBACKEND_TRACE("Operation::HandleRow - Found SuccessOperation: " + m_stCurrentSuccessOperation);
		row.m_vFields[m_iSuccessOperationIndex] = 0;
	}

	const NullableString& failureOp = row.m_vFields.at(m_iFailureOperationIndex);
	m_stCurrentFailureOperation = string(failureOp==0 ? stEmpty : failureOp.str());
	if ( !m_stCurrentFailureOperation.empty() ) {
		UIBACKEND_TRACE("Operation::HandleRow - Found FailureOperation: " + m_stCurrentFailureOperation);
		row.m_vFields[m_iFailureOperationIndex] = 0;
	}

	const string& guid = row.m_vFields.at(m_iOperationIdIndex);
	const long opId = m_guidToIdMapper.GetId(guid, OperationEntityId);

	                                                                                     
	if ( m_bSchedulesUpdated && m_bJmsSourceUpdated )
		return true;


	                                
	string sOldSchedulePolicy;
	m_pItemProperties->GetItemProperty(OperationEntityId, opId, OPERATION_PROPERTY_SCHEDULE_POLICY, sOldSchedulePolicy);
	m_oldSchedulePolicyForOperation.insert( map<int, string>::value_type(opId,sOldSchedulePolicy) );

	                                                                  
	long iOldJmsSourceId = -1, iOldScheduleId = -1;

	                                                                  
	if ( opId == GuidToIdMapper::invalid_id ) {
		                           
		iOldJmsSourceId = -1;
		iOldScheduleId = -1;
	}
	else {
		GetSomeOperationInfo(iOldJmsSourceId, iOldScheduleId, opId, m_dbConnection);
		if ( iOldJmsSourceId == -1 || !IsJmsSource(iOldJmsSourceId, m_dbConnection) )
			iOldJmsSourceId = -1;
	}

	if ( !m_bJmsSourceUpdated ) {
		long newJmsSourceId = -1;
		NullableString& value = row.m_vFields.at(m_iSourceIdIndex);
		if ( value != 0 ) {
			const string& sourceGuid = value.str();
			if ( !sourceGuid.empty() ) {
				newJmsSourceId = m_guidToIdMapper.GetId(sourceGuid, SourceEntityId);
				if ( newJmsSourceId == GuidToIdMapper::invalid_id )
					newJmsSourceId = -1;
			}
		}
		if ( newJmsSourceId == -1 || !IsJmsSource(newJmsSourceId, m_dbConnection) )
			newJmsSourceId = -1;

		if ( iOldJmsSourceId != newJmsSourceId )
			m_bJmsSourceUpdated = true;
	}

	if ( !m_bSchedulesUpdated ) {
		long newScheduleId = -1;
		NullableString& value = row.m_vFields.at(m_iScheduleIdIndex);
		if ( value != 0 ) {
			const string& scheduleGuid = value.str();
			if ( !scheduleGuid.empty() ) {
				newScheduleId = m_guidToIdMapper.GetId(scheduleGuid, ScheduleEntityId);
				if ( newScheduleId == GuidToIdMapper::invalid_id )
					newScheduleId = -1;
			}
		}
		if ( newScheduleId != iOldScheduleId )
			m_bSchedulesUpdated = true;
	}

	return true;
}

bool Operation::HandleRowAfterInsert(long newOperationId, InterchangeDataRowStruct& row) {
	m_updatedOperations.push_back(newOperationId);

	if ( !m_stCurrentSuccessOperation.empty() || !m_stCurrentFailureOperation.empty() ) {
		setDirty();

		                                                                                    
		  
		m_operationIdToSuccessFailureMap[newOperationId] =
						SuccessFailure(m_stCurrentSuccessOperation, m_stCurrentFailureOperation);
		UIBACKEND_TRACE("Found OperationID=" + toString(newOperationId) + " with SuccessOp="
			+ m_stCurrentSuccessOperation + ", FailureOp="
			+ m_stCurrentFailureOperation);
		m_stCurrentSuccessOperation.clear();
		m_stCurrentFailureOperation.clear();
	}

	return true;
}

bool Operation::HandleRowAfterUpdate(long operationId, InterchangeDataRowStruct& row) {
	m_updatedOperations.push_back(operationId);

	                                         
	                                               
	const string sql("DELETE FROM CommonSourceOperationsTab " \
		"WHERE Operation_ID = " + toString(operationId));
	UIBACKEND_TRACE(sql);
	m_dbConnection.ExecuteSql(sql);
	return true;
}

void Operation::Update(DbConnection& dbConnection, const string& stTableName, const string& stIdName)
{
	                                   
	for( CommonSourceOperationsMapT::const_iterator cit = m_commonSourceOperationsMap.begin();
		cit != m_commonSourceOperationsMap.end(); ++cit )
	{
		const string& guid = cit->first;
		const vector<string>& siblings = cit->second;
		if ( siblings.empty() )
			continue;

		const long lOperationId = m_guidToIdMapper.GetId(guid, OperationEntityId);
		if ( lOperationId == GuidToIdMapper::invalid_id ) {
			throw CrException("Internal error: No operation with GUID \"" + guid + "\" has been deployed to this server. Please report this since it is probably a bug.");
		}

		if ( std::find(m_updatedOperations.begin(), m_updatedOperations.end(), lOperationId) == m_updatedOperations.end() )
			continue;

		const string sql("INSERT INTO CommonSourceOperationsTab (Operation_ID, SiblingOperation_ID) " \
			"VALUES (" + toString(lOperationId) + ", ");
		for( vector<string>::const_iterator cit2 = siblings.begin();
			cit2 != siblings.end(); ++cit2 )
		{
			const string& siblingOpId = *cit2;
			const long lSiblingOpId = m_guidToIdMapper.GetId(siblingOpId, OperationEntityId);
			if ( lSiblingOpId == GuidToIdMapper::invalid_id ) {
				throw CrException("Internal error: No operation with GUID \"" + guid + "\" has been deployed to this server. Please report this since it is probably a bug.");
			}
			m_dbConnection.ExecuteSql(sql + toString(lSiblingOpId) + ")");
		}
	}

	if ( isDirty() ) {
		m_bDirty = false;
		OperationIdToTmpSuccessFailureMapT::const_iterator cit
			= m_operationIdToSuccessFailureMap.begin();
		for(; cit != m_operationIdToSuccessFailureMap.end(); ++cit ) {
			const long operationId = cit->first;
			UIBACKEND_TRACE("Operation::Update - Handling success/failure for OperationId = " + toString(operationId));
			const SuccessFailure& successFailure = cit->second;
			UIBACKEND_TRACE("Success: " + successFailure.SuccessOperationId);
			UIBACKEND_TRACE("Failure: " + successFailure.FailureOperationId);

			const string& stSuccessOperationId = successFailure.SuccessOperationId;
			long lSuccessOperationId = GuidToIdMapper::invalid_id;
			if ( !stSuccessOperationId.empty() ) {
				lSuccessOperationId = GetOperationId(stSuccessOperationId, false);
				if ( lSuccessOperationId == GuidToIdMapper::invalid_id ) {
					AddWarning(OperationEntityId, m_guidToIdMapper.GetGuid(operationId, OperationEntityId),
						"The success operation assigned to this operation (guid="
						+ stSuccessOperationId + ") has not been deployed.");
				}
			}

			const string& stFailureOperationId = successFailure.FailureOperationId;
			long lFailureOperationId = GuidToIdMapper::invalid_id;
			if ( !stFailureOperationId.empty() ) {
				lFailureOperationId = GetOperationId(stFailureOperationId, false);
				if ( lFailureOperationId == GuidToIdMapper::invalid_id ) {
					AddWarning(OperationEntityId, m_guidToIdMapper.GetGuid(operationId, OperationEntityId),
						"The failure operation assigned to this operation (guid="
						+ stFailureOperationId + ") has not been deployed.");
				}
			}

			if ( lSuccessOperationId == GuidToIdMapper::invalid_id && lFailureOperationId == GuidToIdMapper::invalid_id )
				continue;

			stringstream ssUpdateSql;
			ssUpdateSql << "UPDATE " << stTableName << " SET ";
			if ( lSuccessOperationId != GuidToIdMapper::invalid_id )
				ssUpdateSql << " success_operation_id = " << lSuccessOperationId;
			if ( lFailureOperationId != GuidToIdMapper::invalid_id ) {
				if ( lSuccessOperationId != GuidToIdMapper::invalid_id )
					ssUpdateSql << ",";
				ssUpdateSql << " failure_operation_id = " << lFailureOperationId;
			}
			ssUpdateSql << " WHERE " << stIdName << " = " << operationId;
			const string stUpdateSql( ssUpdateSql.str() );
			UIBACKEND_TRACE("Operation Update call: " + stUpdateSql);
			dbConnection.ExecuteSql(stUpdateSql);
		}
	}

	m_bDirty = false;
}

long Operation::GetOperationId(const string& stOperationId, bool throwOnError) {
	const long lOperationId = m_guidToIdMapper.GetId(stOperationId, OperationEntityId);
	if ( lOperationId == GuidToIdMapper::invalid_id && throwOnError ) {
		throw CrException("Failed to find OperationId for the GUID " + stOperationId);
	}
	return lOperationId;
}

unsigned int Operation::GetOperationIdIndex() const {
	return static_cast<unsigned int>(m_iOperationIdIndex);
}

bool OperationUpdate::HandleRow(InterchangeDataRowStruct& row) {
	                        
	if ( Operation::HandleRow(row) ) {
		                                                            
		const NullableString& opId = row.m_vFields.at(this->GetOperationIdIndex());
		if ( opId == 0 || opId.str().empty() )
			throw CrException("Bad Data: No OperationId specified for update.");

		UIBACKEND_TRACE("OperationUpdate::HandleRow called with OperationId = " + opId.str());

		const long lOperationId = GetOperationId(opId.str());
		Operation::HandleRowAfterInsert(lOperationId, row);
		return true;
	}
	return false;
}
