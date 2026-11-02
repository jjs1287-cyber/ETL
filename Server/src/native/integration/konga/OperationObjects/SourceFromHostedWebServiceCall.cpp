/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>

#include "OperationEngineException.h"
#include "ProcessedFiles.h"
#include "OperationEngineContext.h"
#include "SourceFromHostedWebServicecall.h"

using namespace konga;
using std::string;

const string SourceFromHostedWebServiceCall::GLOBAL_DE_NAME_FOR_REQUEST_PATH = "__HostedWebServiceRequest";

void SourceFromHostedWebServiceCall::doTransfer(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const {
	if ( !FileExists(m_stRequestAsLocalFile) )
		throw OperationEngineException("The request file for a hosted web service call does not exist. " \
		"Has it been removed? It should have been here: " + m_stRequestAsLocalFile);

	const string sLocalFile(context.getLocalTmpDirForOperation() + "request.xml");
	kCopyFile(m_stRequestAsLocalFile, sLocalFile, true);
	kDeleteFile(m_stRequestAsLocalFile);
	long lErr = 0;
	long long lSize = GetFileSize(sLocalFile, lErr);

	ProcessedFile file;
	file.setFileChangeTimeNow();
	file.setInHistory(false);
	file.setLocalSourceLocation(sLocalFile);
	file.setSourceSize(lSize);
	file.setTransferedNow();

	files.add(file);
}

void SourceFromHostedWebServiceCall::getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const {
	throw OperationEngineException("Cannot get first source file for a hosted web service call source!");
}

