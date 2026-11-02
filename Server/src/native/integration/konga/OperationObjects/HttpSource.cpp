/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/util.h>

#include <konga/FileTransfer/HttpBase.h>

#include <konga/SystemVariables/SystemVariables.h>

#include "OperationEngineException.h"
#include "HttpSource.h"
#include "ProcessedFiles.h"
#include "OperationEngineContext.h"

using namespace std;
using namespace konga;

HttpSource::HttpSource(void) {}

HttpSource::~HttpSource(void) {}

std::string HttpSource::getHttpUrl() const {
	const string& tmp = getOverrideFileFilter();
	if ( !tmp.empty() )
		return tmp;
	return HttpLocation::getHttpUrl();
}

long HttpSource::getTransferTimeOutSeconds() const {
	return konga::GetSystemVariableAsLong("etlforge.source.http.transfer_timeout", m_pDataElementHolder, 3600);
}

void HttpSource::getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const {
	auto_ptr<HttpBase> pHttp( getHttpForLocation() );

	string stGuid;
	GetGUIDString(stGuid);
	const string stLocalSourceFile(context.getLocalTmpDirForOperation() + stGuid);

	if ( pHttp->GetAsFile(stLocalSourceFile) != Protocol::SUCCESS ) {
		string stErr("Failed to retrieve the url '" + getHttpUrl());
		stErr += "'. " + pHttp->GetLastError();
		appendPartOfErrorResponse(stLocalSourceFile, stErr);
		throw OperationEngineException(stErr);
	}
	ProcessedFile file;
	file.setFileChangeTimeNow();
	file.setInHistory(false);
	file.setLocalSourceLocation(stLocalSourceFile);
	file.setRemoteSourceLocation(getHttpUrl());
	long lErr = 0;
	const long long size = GetFileSize(stLocalSourceFile, lErr);
	file.setSourceSize(size);
	file.setTransferedNow();
	files.add(file);
}

void HttpSource::doTransferImpl(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const {
	auto_ptr<HttpBase> pHttp( getHttpForLocation() );

	                                                                                      
	string stGuid;
	GetGUIDString(stGuid);
	const string& stLocalFile = context.getLocalTmpDirForOperation() + stGuid;

	const HttpVerb verb = getHttpVerb();
	if ( verb == HttpGet || verb == HttpDelete ) {
		pHttp->SetHttpVerb(verb);
		if ( pHttp->GetAsFile(stLocalFile) != Protocol::SUCCESS ) {
			string stErr("Failed to get the url '" + getHttpUrl());
			stErr += "'. " + pHttp->GetLastError();
			appendPartOfErrorResponse(stLocalFile, stErr);
			throw OperationEngineException(stErr);
		}
	}
	else if ( verb == HttpPost || verb == HttpPut )  {
		                                                                               
		                                                        
		pHttp->SetHttpVerb(verb);
		if ( pHttp->PutAsStringGetRespAsFile("", stLocalFile) ) {
			string stErr("Failed to post an empty string to the url '" + getHttpUrl());
			stErr += "'. " + pHttp->GetLastError();
			appendPartOfErrorResponse(stLocalFile, stErr);
			throw OperationEngineException(stErr);
		}
	}
	else {
		throw OperationEngineException("The http verb " + toString(verb)
			+ " is not allowed for an http source. Use GET, POST, PUT or DELETE instead.");
	}

	long lErr = 0;
	long long lSize = GetFileSize(stLocalFile, lErr);
	if ( lSize < 0 || lErr != 0 ) {
		                                          
		lSize = 0;
	}

	ProcessedFile file(pHttp->getParsedUrl(), lSize, 0, 0, 0);
	file.setLocalSourceLocation(stLocalFile);
	files.add(file);
}

std::vector<std::string> HttpSource::getFileList() const {
	return getFileListImpl();
}
