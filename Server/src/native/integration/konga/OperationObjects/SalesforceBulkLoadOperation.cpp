/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <memory>
#include <exception>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/DateUtils.h>
#include <konga/Utils/CancelHandlers.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include <konga/DataElement/DataElement.h>

#include <konga/JavaBackend/BulkLoadEngine/BulkLoadEngineWrapper.h>
#include <konga/JavaBackend/BulkLoadEngine/BulkLoadCancelHandler.h>

#include <konga/OperationContext/OperationDataElementHolder.h>

#include <konga/OperationEngine/SignalHandler.h>


#include "ProcessedFiles.h"
#include "OperationEngineContext.h"
#include "OperationEngineException.h"
#include "SalesforceBulkLoadOperation.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	const string SFDC_SESSION_ID_DE("salesforce.sessionID");
	const string SFDC_SESSION_URL_DE("salesforce.serverURL");
	const string LAST_OPERATION_INSTANCE_ID_DE("salesforce.to-be-determined");

	string getStringValue(OperationDataElementHolder& deHolder, const string& deName) {
		if ( deHolder.isOperationDataElementDefined(deName) ) {
			DataElement* de = deHolder.getOperationDataElement(deName);
			int err;
			return string((const char*)de->GetString(err));
		}
		else {
			return string("");
		}
	}
}

                        
SalesforceBulkLoadOperation* SalesforceBulkLoadOperation::create(long lOperationId, long long llOperationInstanceId, DbConnection& tranDbConn) {

	string sql( "SELECT blt.salesforcebulkload_id, blt.salesforcebulkload_name FROM salesforcebulkloadstab blt " \
		"WHERE blt.operation_id = " + toString(lOperationId) );
	SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&tranDbConn, sql) );

	rs->Open();
	if ( !rs->Next() ) {
		throw InternalErrorException("There is no Salesforce Bulk Load object for operation id " + toString(lOperationId));
	}
	const long long llSfdcBulkLoadId = toLongLong(rs->GetFieldValue(0));
	const string& stSfdcBulkLoadName = rs->GetFieldValue(1);
	rs.CloseAndDelete();

	const CrDateTime& utcNow = CrDateTime::GetCurrentUtcTime();

	sql = "INSERT INTO bulkload_instance_tab (operation_instance_id, last_operation_instance_id, bulkload_id, start_time) VALUES ("
		+ toString(llOperationInstanceId) + ",-1," + toString(llSfdcBulkLoadId) + ",'" + utcNow.toString("yyyy-mm-dd HH:MM:SS") + "')";
	tranDbConn.ExecuteSql(sql);

	return new SalesforceBulkLoadOperation(llSfdcBulkLoadId, stSfdcBulkLoadName, llOperationInstanceId);
}

namespace {
	void UpdateBulkloadInstanceFilename(DbConnection& tranDbConn, long long llOperationInstanceId, const string& filename, const long long fileSize) {
		string sql("UPDATE bulkload_instance_tab SET filename = '" + konga::SqlEscape(filename) + "', file_size = " + toString(fileSize)
			+ " WHERE operation_instance_id = " + toString(llOperationInstanceId));
		tranDbConn.ExecuteSql(sql);
	}
}

void SalesforceBulkLoadOperation::doOperation(ProcessedFiles& files, OperationEngineContext& context, OperationDataElementHolder& deHolder) {

	const long lOperationId = context.getOperationId();
	const long long llOperationInstanceId = context.getOperationInstanceId();
	if ( files.empty() ) {
		throw OperationEngineException("No files to process.", false, "SalesForce Bulk Load", lOperationId, 0);
	}
	else if ( files.getNumberOfProcessedFiles() > 1 ) {
		throw OperationEngineException("You can't process more than one file at the time with the bulk load wizard. Please select a file filter that selects only one file.", lOperationId, "SalesForce Bulk Load");
	}

	long long llLastOperationInstanceId = -1;
	if ( deHolder.isOperationDataElementDefined(LAST_OPERATION_INSTANCE_ID_DE) ) {
		const DataElement* const deLastOperationInstanceId = deHolder.getOperationDataElement(LAST_OPERATION_INSTANCE_ID_DE);
		int err = 0;
		long long lltmp = deLastOperationInstanceId->GetLong(err);
		if ( err == 0 ) {
			llLastOperationInstanceId = lltmp;
		}
	}

	try {
		const string stSFDCSessionId( getStringValue(deHolder, SFDC_SESSION_ID_DE) );
		const string stSFDCUrl( getStringValue(deHolder, SFDC_SESSION_URL_DE) );

		ProcessedFile& bulkLoadFile = files.getProcessedFilesByRef().front();
		const string& sourceFilePath = bulkLoadFile.getLocalSourceLocation();
		UpdateBulkloadInstanceFilename(*context.getTranDbConnection(), llOperationInstanceId, sourceFilePath, bulkLoadFile.getSourceSize());

		std::auto_ptr<BulkLoadEngineWrapper> pBulkLoadEngine( BulkLoadEngineWrapper::createBulkLoadEngineWrapper(*context.getTranDbConnection(), context.getCurrentUser()) );
		pBulkLoadEngine->startNewSession();

		BulkLoadCancelHandler cancelHandler(*pBulkLoadEngine);
		AutoRemoveCancelHandler arch(konga::getCancelHandlerContainer(), &cancelHandler);

		const BulkLoadResult result = pBulkLoadEngine->runBulkLoad(stSFDCSessionId, stSFDCUrl, m_llSfdcBulkLoadId, llOperationInstanceId, llLastOperationInstanceId, sourceFilePath);
		const string& warning = result.warnings;
		if ( !warning.empty() ) {
			bulkLoadFile.addWarning(warning);
		}

		const string& logFile = result.logFile;
		if ( konga::NonEmptyFileExists(logFile) ) {
			string logFileContents;
			konga::read_file_to_string(logFile, logFileContents);
			bulkLoadFile.addWarning(logFileContents);
		}

		pBulkLoadEngine->closeSession();
	}
	catch( const CrException& ex ) {
		throw OperationEngineException(ex.GetErrorString(), lOperationId, "Bulk Load");
	}
	catch( const std::exception& ex ) {
		throw OperationEngineException(string("Unexpected exception in Bulk Load: ") + ex.what(), lOperationId, "Bulk Load");
	}
#ifndef _DEBUG
	catch( ... ) {
		                                   
		throw OperationEngineException("Unexpected, unknown exception in Bulk Load", lOperationId, "Bulk Load");
	}
#endif
}
