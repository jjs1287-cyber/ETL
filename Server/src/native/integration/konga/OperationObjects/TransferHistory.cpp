/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <time.h>

#include <cassert>
#include <sstream>	               

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbException.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/TranDb/XferHist.h>

#include <konga/Utils/StringUtils.h>		                        
#include <konga/Utils/DateUtils.h>			             
#include <konga/Utils/DbTypeUtils.h>

#include "OperationEngineException.h"
#include "TransferHistory.h"

using namespace std;
using namespace konga;

                
namespace {
	inline string getFileWhereClause(const ProcessedFile& file, int OperationId) {
		stringstream sWhere;
		const string& stRemoteLoc = file.getRemoteSourceLocation();
		sWhere << "WHERE FileName='" << SqlEscape(stRemoteLoc) << "' AND OperationID=" << OperationId << " AND FileSize=" << file.getSourceSize();
		sWhere << " AND FileMonth=" << file.month() << " AND FileDay=" << file.day() << " AND FileTimeOrYear=" << file.time_or_year();
		return sWhere.str();
	}
}

bool TransferHistory::IsAlreadyTransferred(long lOperationId, ProcessedFile& file) const {
	if ( !isActive() )
		throw OperationEngineException("Failed to check transfer history for \"" + file.getRemoteSourceLocation() + "\". No database connection has been established.", "Checking if file is already transfered");

	try {
		SmartDbPtr<TranDb::XferHist> pXferHist( TranDb::XferHist::MakeInstance(m_pDbConnection) );
		pXferHist->Select( getFileWhereClause(file, lOperationId) );
		if ( pXferHist->Next() ) {
			                                                  
			file.setRemoteSourceLocation( pXferHist->IsFileNameNull() ? "" : pXferHist->GetFileName() );
			file.setSourceSize( pXferHist->IsFileSizeNull() ? 0 : pXferHist->GetFileSize() );
			file.month( pXferHist->IsFileMonthNull() ? 0 : pXferHist->GetFileMonth() );
			file.day( pXferHist->IsFileDayNull() ? 0 : pXferHist->GetFileDay() );
			file.time_or_year( pXferHist->IsFileTimeOrYearNull() ? 0 : pXferHist->GetFileTimeOrYear() );

			if ( !pXferHist->IsXferDateTimeNull() ) {
				TIMESTAMP_STRUCT ts = pXferHist->GetXferDateTime();
				file.setTransferedTime(get_time_t(ts));
			}
			file.setLocalSourceLocation( pXferHist->GetLocalFileName(), false );
			file.setInHistory(true);
			return true;
		}
		else {
			return false;
		}
	} catch ( const DbException& ex ) {
		stringstream sErr;
		sErr << "Failed to check transfer history for \"" << file.getRemoteSourceLocation() << "\" because a database error occurred." << endl;
		sErr << ex.GetErrorString();
		throw OperationEngineException(sErr.str(), "Checking if file is already transfered");
	}
}

void TransferHistory::AddToTransferHistory(long lOperationId, ProcessedFiles& files) const {
	files.clearSourceProcessedFlags();
	vector<ProcessedFile>& transfered_files = files.getProcessedFilesByRef();
	for( vector<ProcessedFile>::const_iterator it = transfered_files.begin();
		 it != transfered_files.end(); ++it )
	{
		const ProcessedFile& file = *it;
		if ( files.hasSourceFileBeenProcessed(file) )
			continue;
		AddToTransferHistory(lOperationId, *it);
		files.flagSourceFileAsProcessed(file);
	}
}

void TransferHistory::AddToTransferHistory(long lOperationId, const ProcessedFile& file) const {
	assert(m_pDbConnection);
	                                                       
	                                  
	                                                                                 
	                                                            
	                                                                                    
	if ( file.getLocalSourceLocation().empty() || file.isInHistory() || file.hasError() || file.getRemoteSourceLocation().empty() )
		return;

	if ( !isActive() )
		throw OperationEngineException("Failed to add transfer history for the remote file \"" + file.getRemoteSourceLocation() + "\". No database connection has been established.", "Adding file to transfer history");
	try {
		SmartDbPtr<TranDb::XferHist> pXferHist( TranDb::XferHist::MakeInstance(m_pDbConnection) );
		pXferHist->Select( getFileWhereClause(file, lOperationId) );

		if ( pXferHist->Next() ) {
			                        
			pXferHist->Edit();

			pXferHist->SetRetryCount( pXferHist->GetRetryCount() + 1 );
		} else {
			pXferHist->AddNew();

			pXferHist->SetFileName(file.getRemoteSourceLocation());
			pXferHist->SetOperationID(lOperationId);

			pXferHist->SetFileSize(file.getSourceSize());
			pXferHist->SetFileMonth(file.month());
			pXferHist->SetFileDay(file.day());
			pXferHist->SetFileTimeOrYear(file.time_or_year());

			pXferHist->SetProgressStatus(0);
			pXferHist->SetRetryCount(0);
		}

		TIMESTAMP_STRUCT t;
		setTimeStruct(t, file.transferedTime());
		pXferHist->SetXferDateTime(t);
		                                                                                               
		                                                   
		pXferHist->SetLocalFileName(file.getLocalSourceLocation());

		pXferHist->Update();
	} catch ( const DbException& ex ) {
		stringstream sErr;
		sErr << "Failed to add remote file \"" << file.getRemoteSourceLocation() << "\" to the transfer history. A database error occurred." << endl;
		sErr << ex.GetErrorString();
		throw OperationEngineException(sErr.str(), "Adding file to transfer history");
	}
}

bool TransferHistory::isActive() const {
	return m_pDbConnection != 0;
}

