/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_SALESFORCE_BULK_LOAD_OPERATION_H
#define KONGA_SALESFORCE_BULK_LOAD_OPERATION_H

#include "Operation.h"

namespace konga {

	class DbConnection;

	class SalesforceBulkLoadOperation : public Operation {
	public:
		static SalesforceBulkLoadOperation* create(long lOperationId, long long llOperationInstanceId, DbConnection& tranDbConn);

	private:
		SalesforceBulkLoadOperation(long long llSfdcBulkLoadId, const std::string& stSfdcBulkLoadName, long long llOperationInstanceId)
			: m_llSfdcBulkLoadId(llSfdcBulkLoadId), m_stBulkLoadName(stSfdcBulkLoadName), m_llOperationInstanceId(llOperationInstanceId) {}

	public:
		~SalesforceBulkLoadOperation(void) {}

		virtual ActivityType getActivityType() const { return ActivityType_BulkLoad; }

		                      
		void doOperation(ProcessedFiles& files, OperationEngineContext& context, OperationDataElementHolder& deHolder);

		friend class OperationCreatorDb;	                                

	private:
		long long m_llSfdcBulkLoadId;
		long long m_llOperationInstanceId;
		std::string m_stBulkLoadName;
	};
}

#endif                                          

