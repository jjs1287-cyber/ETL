/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_SPECIAL_HANDLING_OPERATION_H
#define KONGA_WS_SPECIAL_HANDLING_OPERATION_H

#include <string>
#include <vector>
#include <map>

#include "ItemWarningHolder.h"

#include "SpecialHandlingDeploy.h"

namespace konga {

	class DbConnection;
	class GuidToIdMapper;
	class ItemProperties;

namespace interchange_deploy {

                                                                                       
                                                                                                   
                                                                                                  
class Operation : public SpecialHandlingDeploy, public ItemWarningHolder {
public:
	Operation(DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper,
		const xsd__string_Array& columnNames,
		const InterchangeDataTableStruct& commonSourceOperations,
		std::map<int, std::string>& oldSchedulePolicyForOperation);
	virtual ~Operation();

	virtual bool HandleRow(InterchangeDataRowStruct& row);

	virtual bool HandleRowAfterInsert(long newId, InterchangeDataRowStruct& row);

	virtual bool HandleRowAfterUpdate(long operationId, InterchangeDataRowStruct& row);

	                                                        
	                                                               
	                                                                                       
	void Update(DbConnection& dbConnection, const std::string& stTableName, const std::string& stIdName);

	bool SchedulesWereUpdated() const;
	
	bool JmsSourcesWereUpdated() const;

protected:
	void setDirty() { m_bDirty = true; }

	unsigned int GetOperationIdIndex() const;

	                                               
	                                                          
	long GetOperationId(const std::string& stOperationId, bool throwOnError = true);

	std::vector<long> m_updatedOperations;

private:
	bool isDirty() { return m_bDirty; }

private:
	struct SuccessFailure {
		SuccessFailure() {}
		SuccessFailure(const std::string& stSuccessId, const std::string& stFailureId)
			: SuccessOperationId(stSuccessId), FailureOperationId(stFailureId)
		{}
		~SuccessFailure() {}
		std::string SuccessOperationId;
		std::string FailureOperationId;
	};

	bool m_bDirty;
	bool m_bSchedulesUpdated, m_bJmsSourceUpdated;
	int m_iOperationIdIndex, m_iSourceIdIndex;
	int m_iSuccessOperationIndex, m_iFailureOperationIndex;
	int m_iScheduleIdIndex;

	DbConnection& m_dbConnection;
	ItemProperties* m_pItemProperties;
	GuidToIdMapper& m_guidToIdMapper;
	std::map<int, std::string>& m_oldSchedulePolicyForOperation;
	std::string m_stCurrentSuccessOperation, m_stCurrentFailureOperation;

	typedef std::map<long, SuccessFailure> OperationIdToTmpSuccessFailureMapT;
	OperationIdToTmpSuccessFailureMapT m_operationIdToSuccessFailureMap;

	typedef std::map<std::string, std::vector<std::string> > CommonSourceOperationsMapT;
	CommonSourceOperationsMapT m_commonSourceOperationsMap;
};

class OperationInsert : public Operation {
public:
	OperationInsert(DbConnection& dbConnection,
		GuidToIdMapper& guidToIdMapper,
		const xsd__string_Array& columnNames,
		const InterchangeDataTableStruct& commonSourceOperations,
		std::map<int, std::string>& oldSchedulePolicyForOperation) :
	  Operation(dbConnection, guidToIdMapper, columnNames,
		  commonSourceOperations, oldSchedulePolicyForOperation) {}
};

class OperationUpdate : public Operation {
public:
	OperationUpdate(DbConnection& dbConnection,
		GuidToIdMapper& guidToIdMapper,
		const xsd__string_Array& columnNames,
		const InterchangeDataTableStruct& commonSourceOperations,
		std::map<int, std::string>& oldSchedulePolicyForOperation) :
	  Operation(dbConnection, guidToIdMapper, columnNames,
		  commonSourceOperations, oldSchedulePolicyForOperation) {}

	virtual bool HandleRow(InterchangeDataRowStruct& row);
};
}                   
}                                

#endif                                         

