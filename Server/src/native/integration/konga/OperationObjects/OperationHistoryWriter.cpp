/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <vector>
#include <string>
#include <sstream>
#include <limits>

#include <konga/Utils/DateUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include "ProcessedFiles.h"
#include "OperationHistoryWriter.h"

using namespace konga;
using std::string;
using std::vector;
using std::stringstream;

OperationHistoryWriter::OperationHistoryWriter() :
m_pStartTimeUtc( new CrDateTime(CrDateTime::GetCurrentUtcTime()) ) {
}

OperationHistoryWriter::~OperationHistoryWriter(void) {
	delete m_pStartTimeUtc;
}

namespace {
	string getDbTimeStamp(const CrDateTime& dateTime) {
		stringstream ssTime;
		ssTime << dateTime.GetYear() << "-" << dateTime.GetMonth() << "-"
			<< dateTime.GetDay() << " " << dateTime.GetHour() << ":"
			<< dateTime.GetMinute() << ":" << dateTime.GetSecond();
		return ssTime.str();
	}
}

                                                        
void OperationHistoryWriter::Write(long lOperationId, ProcessedFiles& files, DbConnection* pDbConn) {
	const CrDateTime& endTime = CrDateTime::GetCurrentUtcTime();
	const CrDateTime& startTime = *m_pStartTimeUtc;

	int iTotalFiles = 0;
	long long lSize = 0;
	long long lSourceSize = 0, lTargetSize = 0;
	long lTotalTransformationTime = 0;
	const vector<ProcessedFile>& fileList = files.getProcessedFilesByRef();
	for( vector<ProcessedFile>::const_iterator cit = fileList.begin();
		cit != fileList.end(); ++ cit )
	{
		const ProcessedFile& file = *cit;
		if ( file.isInHistory() || file.hasError() )
			continue;

		lSize += file.getTotalSize();
		lSourceSize += file.getSourceSize();
		lTargetSize += file.getTargetSize();
		lTotalTransformationTime += file.getTransformationTime();

		if ( !file.getLocalSourceLocation().empty() || !file.getLocalTargetLocation().empty() )
			++iTotalFiles;
	}

	                                                                                       
	                                                                                                         
	long iSize = lSize;
	if( iSize < 0 ) iSize = std::numeric_limits<long>::max();
	long iSourceSize = lSourceSize;
	if( iSourceSize < 0 ) iSourceSize = std::numeric_limits<long>::max();
	long iTargetSize = lTargetSize;
	if( iTargetSize < 0 ) iTargetSize = std::numeric_limits<long>::max();

	stringstream ssSql;
	ssSql << "INSERT INTO BillingTab (operation_id, start_date, end_date, total_files, total_size, "
		  << "total_source_size, total_target_size, total_transformation_time, billacc_id, comments) "
		  << "VALUES ("
		  << lOperationId << ", '"
		  << getDbTimeStamp(startTime) << "', '"
		  << getDbTimeStamp(endTime) << "', "
		  << iTotalFiles << ", "
		  << iSize << ", "
		  << iSourceSize << ", "
		  << iTargetSize << ", "
		  << lTotalTransformationTime << ", "
		  << "0, '')";

	SmartDbPtr<DbConnection> adopt_pDbConnection( 0 );
	if ( pDbConn == 0 ) {
		adopt_pDbConnection.Attach( pDbConn = konga::GetBackEndDbConnection("TranDb") );
	}
	pDbConn->ExecuteSql(ssSql.str());
}
