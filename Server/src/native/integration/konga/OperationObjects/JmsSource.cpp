/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/FileUtils.h>	           

#include <konga/Server/Server.h>	                              

#include "OperationEngineException.h"
#include "OperationEngineContext.h"
#include "ProcessedFiles.h"
#include "JmsSource.h"

using namespace konga;
using std::string;
using std::vector;

JmsSource::JmsSource(const std::string& sLocalJmsPayloadPath) {
	m_sLocalJmsPayloadPath = sLocalJmsPayloadPath;
}
JmsSource::~JmsSource(void) {}

std::string JmsSource::getLocalPayloadPath() const {
	return m_sLocalJmsPayloadPath;
}

void JmsSource::doTransferImpl(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const
{
	                                                               
	const string& sLocalDownloadPath = getLocalPayloadPath();
	if ( !FileExists(sLocalDownloadPath) )
		throw OperationEngineException("The downloaded jms payload does not exist." \
		" It is supposed to be located here: \"" + sLocalDownloadPath + "\".");

	const string& sTmpDir = context.getLocalTmpDirForOperation();
	const string sLocalSource( sTmpDir + GetLeafPath(sLocalDownloadPath) );
	kCopyFile(sLocalDownloadPath, sLocalSource);

	ProcessedFile payload;
	payload.setFileChangeTimeNow();
	payload.setTransferedNow();
	payload.setLocalSourceLocation(sLocalSource);
	long lErr = 0;
	long long llSize = GetFileSize(sLocalSource, lErr);
	if ( lErr )
		llSize = 0;
	payload.setSourceSize(llSize);

	files.add(payload);
}

std::vector<std::string> JmsSource::getFileList() const {
	JmsLocation::testJmsLocation();
	return vector<string>();
}
