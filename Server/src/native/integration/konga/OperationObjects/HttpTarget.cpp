/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/Settings.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/util.h>
#include <konga/Utils/FileContents.h>
#include <konga/Utils/ErrorResolver.h>
#ifndef WIN32
#include <konga/Utils/PortDefs.h>	                                           
#endif

#include <konga/FileTransfer/HttpBase.h>

#include <konga/DataElement/DataElement.h>

#include <konga/SystemVariables/SystemVariables.h>

#include <konga/OperationContext/OperationDataElementHolder.h>

#include "OperationEngineException.h"
#include "HttpTarget.h"
#include "ProcessedFiles.h"

using namespace std;
using namespace konga;

HttpTarget::HttpTarget() : m_pResponseTarget(0) { }

HttpTarget::~HttpTarget(void) {
	if ( m_pResponseTarget ) {
		delete m_pResponseTarget;
		m_pResponseTarget = 0;
	}
}

string HttpTarget::getHttpUrl() const {
	const string& tmp = getOverrideFileName();
	if ( !tmp.empty() )
		return tmp;
	return HttpLocation::getHttpUrl();
}

std::string HttpTarget::getFileName() const {
	return this->getHttpUrl();
}

long HttpTarget::getTransferTimeOutSeconds() const {
	return konga::GetSystemVariableAsLong("etlforge.target.http.transfer_timeout", m_pDataElementHolder, 3600);
}

namespace {

	                                                                               
	                                                                                  
	                                                                             
	                                 
	string removeTrailingLineBreaks(const string& inputFile) {
		string resultingFile(inputFile);
		FileContents fc(inputFile);
		const size_t len0=fc.size();
		size_t len=len0;
		char c;
		while((c=fc.at(len-1))=='\n' || c=='\r'){
			len--;
		}
		if(len<len0){
			const string trimmedFile(inputFile + "_trimmed");
			FILE*out=fopen(trimmedFile.c_str(),"w+");
			if ( out == 0 ) {
				const long lErr = GetLastError();
				throw OperationEngineException("Failed to trim line-breaks from http request file: Cannot open output file: \""+trimmedFile+"\". "
					+ ErrorResolver(lErr).Resolve(lErr));
			}
			for(size_t n=0; n<len; ++n) {
				fputc(fc.at(n), out);
			}
			fclose(out);
			resultingFile = trimmedFile;
		}
		fc.close();
		return resultingFile;
	}
}

void HttpTarget::doTransfer(const OperationDataElementHolder& deHolder, ProcessedFiles& files) const {

	vector<ProcessedFile>& transfered_files = files.getProcessedFilesByRef();
	for( vector<ProcessedFile>::iterator it = transfered_files.begin();
		 it != transfered_files.end(); ++it ) {

		auto_ptr<HttpBase> pHttp( getHttpForLocation() );
		ProcessedFile& file = *it;
		if ( doNotTransfer(file) )
			continue;

		string stLocalFile(file.getLocalTargetLocation());
		const HttpVerb verb = getHttpVerb();
		switch( verb ) {
			case HttpGet:
			case HttpPost:
			case HttpPut:
			case HttpDelete:
				pHttp->SetHttpVerb(verb);
				break;
			default:
				throw OperationEngineException("The http verb " + toString(verb)
					+ " is not allowed for an http target. Use PUT, POST, GET or DELETE.");
		};

		                                                                         
		                             
		const string formDataPostDE("etlforge.target.http.form_data");
		if ( deHolder.isOperationDataElementDefined(formDataPostDE) ) {
			const DataElement* de = deHolder.getOperationDataElement(formDataPostDE);
			int err = 0;
			if ( de->GetBool(err) ) {
				de = deHolder.getOperationDataElement("etlforge.target.http.form_data.name");
				const string stFormName((const char*)de->GetString(err));
				de = deHolder.getOperationDataElement("etlforge.target.http.form_data.filename");
				const string stFormFileName((const char*)de->GetString(err));
				de = deHolder.getOperationDataElement("etlforge.target.http.form_data.ContentType");
				const string stFormContentType((const char*)de->GetString(err));
				pHttp->useRFC1867MultipartFileUpload(stFormName, stFormFileName, stFormContentType);
			}
		}

		                                                                   
		                             
		const string removeLinebreaksDE("etlforge.target.http.remove_trailing_linebreaks");
		if (deHolder.isOperationDataElementDefined(removeLinebreaksDE)) {
			const DataElement* const de = deHolder.getOperationDataElement(removeLinebreaksDE);
			int err = 0;
			if ( de->GetBool(err) ) {
				stLocalFile = removeTrailingLineBreaks(stLocalFile);
			}
		}

		const string stResponseFile( konga::getTmpDir() + GetGUIDString() + "-http-response.txt" );
		if ( pHttp->PutAsFileGetRespAsFile(stLocalFile, stResponseFile) != Protocol::SUCCESS ) {
			string stErr("Failed to " + string((verb==HttpPut)?"put":"post"));
			stErr += " to the url '" + getHttpUrl() + "'. " + pHttp->GetLastError();
			appendPartOfErrorResponse(stResponseFile, stErr);
			throw OperationEngineException(stErr);
		}

		file.setRemoteTargetLocation(pHttp->getParsedUrl());

		                                                         
		if ( m_pResponseTarget != 0 ) {
			ProcessedFiles responseFiles;
			long lErr = 0;
			long long lSize = konga::GetFileSize(stResponseFile, lErr);
			if ( lErr ) lSize = 0;
			ProcessedFile responseFile(file);

			                                                                                 
			responseFile.setLocalTargetLocation(stResponseFile);
			responseFile.setTargetSize(lSize);
			responseFile.setRemoteTargetLocation("");
			responseFile.setTransferedTime(0);
			responseFile.setInHistory(false);
			responseFile.setTransformationTime(0);

			responseFiles.add(responseFile);
			m_pResponseTarget->doTransfer(deHolder, responseFiles);
		}

		                            
		try {
			kDeleteFile(stResponseFile);
		}
		catch(...) {
			                                                                        
		}
	}
}

void HttpTarget::testFileTargetConnection(bool bCopyTestFile) const {
	getFileListImpl();
}

