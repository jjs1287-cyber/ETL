/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <sstream>	               
#include <vector>
#include <exception>

#include <konga/Utils/FileUtils.h>	             
#include <konga/Utils/util.h>
#include <konga/Utils/PortDefs.h>	                                                           

#include <konga/DbRelations/TransformationTypeIdConstants.h>

#include <konga/DataElement/DataElement.h>

#include <konga/OperationContext/OperationDataElementHolder.h>

#include <konga/TransformationEngine/TransformationEngine.h>

#include <konga/OperationObjects/ProcessedFiles.h>
#include <konga/OperationObjects/OperationEngineContext.h>
#include <konga/OperationObjects/OperationEngineException.h>

#include "TransformationOperation.h"

using namespace std;
using namespace konga;

string TransformationOperation::FINAL_TARGET_TYPE_DE = "__TransformationOperationFinalTargetType";

void TransformationOperation::doOperation(ProcessedFiles& files, OperationEngineContext& context, OperationDataElementHolder& deHolder) {
	vector<ProcessedFile>& transfered_files = files.getProcessedFilesByRef();
	vector<ProcessedFile>::iterator it = transfered_files.begin();

	                                                              
	if ( it == transfered_files.end() )
		throw OperationEngineException("Internal Error: No source files passed to doOperation");

	int finalTargetType = UnknownStructureType;
	int originalSourceNumber = 0;
	for(; it != transfered_files.end(); ++it ) {
		++originalSourceNumber;
		ProcessedFile& file( *it );
		if ( file.isInHistory() ) {
			continue;
		}

		context.setSourceLocator(file.getRemoteSourceLocation());
		context.setSourceSize(file.getSourceSize());

		                                                                  
		const string& stLocalSource = file.getLocalSourceLocation();

		const string& stGuid = GetGUIDString();
		string stLocalTarget;
		if ( stLocalSource.empty() ) {
			                                                                                   
			stLocalTarget = context.getLocalTmpDirForOperation() + stGuid + "_target";
		}
		else {
			stLocalTarget = stLocalSource + "_target";
		}

		unsigned long lResult = 0;
		int iStatus = 0;
		int iSourceType = 0;
		bool bIsSourceEmpty = false;
#ifndef _DEBUG
		try
#endif
		{                                                   
			TransformationEngine transform(context.getCurrentUser(), &context, &deHolder);
			lResult = transform.doOperation(stLocalSource, stLocalTarget, originalSourceNumber, iStatus, iSourceType, bIsSourceEmpty);
			const bool transformationFailed = FAILED(lResult);
			if( !transformationFailed ) {
				if ( FileExists(stLocalTarget) ) {
					file.setLocalTargetLocation(stLocalTarget);
				}
				TransformationStatistics stats = transform.getTransformationStatistics();
				file.setTransformationTime(stats.TransformationTimeMs);
				if ( stats.SourceSizeBytes > 0 ) file.setSourceSize(stats.SourceSizeBytes);
				file.setSourceRecords(stats.SourceRecords);

				if ( stats.TargetSizeBytes > 0 ) {
					file.setTargetSize(stats.TargetSizeBytes);
				}
				else if ( konga::FileExists(stLocalTarget) ) {
					long lErr = 0;
					const long long lSize = konga::GetFileSize(stLocalTarget, lErr);
					if ( lErr == 0 ) file.setTargetSize(lSize);
				}
				file.setTargetRecords(stats.TargetRecords);
			}

			                                                                   
			string sErr( transform.getErrorsAndWarnings() );

			if ( transformationFailed ) {
				if ( sErr.empty() ) {
					sErr = "Transformation failed but no error was reported.";
				}
				if ( !stLocalSource.empty() ) {
					sErr += "\nFailed to perform transformation using local source file: '" + stLocalSource + "'";
				}
				file.addError(sErr);
			}
			else {
				if ( !sErr.empty() ) {
					file.addWarning(sErr);
				}
			}

			if ( !transformationFailed && finalTargetType == UnknownStructureType ) {
				finalTargetType = transform.getTargetType();
			}
		}
#ifndef _DEBUG
		catch( const CrException& ex ) {
			file.addError("Unexpected CrException thrown by TransformationEngine. Please report this as it may be a bug.\n" \
				"The reported error was:\n"
				+ ex.GetErrorString());
			continue;
		}
		catch( const std::bad_alloc& e ) {
			file.addError("Out of memory: " + string(e.what()));
			continue;
		}
		catch( const std::exception& e) {
			file.addError("Unexpected exception thrown by TransformationEngine: " + string(e.what()));
			continue;
		}
#endif
	}                          

	if ( finalTargetType != UnknownStructureType ) {
		DataElement* de = deHolder.getOperationDataElement(FINAL_TARGET_TYPE_DE);
		de->setType(DataElement::t_int);
		const double d = finalTargetType;
		de->setTypedValue(d);
	}

	                                                                    
	                                                                    
	if ( files.hasOnlyErrors() ) {
		string stErr;
		if ( transfered_files.size() == 1 )
			stErr = "Transformation failed. ";
		else
			stErr = "All transformations failed. ";
		throw OperationEngineException(stErr, "Performing transformation");
	}
}

