/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <cassert>

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/FileLineReadWriter.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/KeyValues.h>

#include <konga/ItemProperties/Definitions.h>

#include <konga/DataElement/DataElement.h>
#include <konga/OperationContext/OperationDataElementHolder.h>

#include <konga/SystemVariables/SystemVariables.h>	                       

#include "ProcessedFiles.h"
#include "OperationEngineContext.h"
#include "FileTypeSourceLocation.h"

using namespace konga;
using std::string;
using std::vector;


void FileTypeSourceLocation::doTransfer(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const {
	                                                                      
	context.getLocalTmpDirForOperation();

	doTransferImpl(context, files, progress);
}

bool FileTypeSourceLocation::deleteAfterProcessing() const {
	return (m_SourceData.m_stRenameFileTo.compare("/\\delete/\\") == 0);
}

bool FileTypeSourceLocation::useCaseInsensitiveFileFilter() const {
	assert(m_SourceData.m_pExtendedProperties);
	return m_SourceData.m_pExtendedProperties->GetBool(SOURCE_CASE_INSENSITIVE_FILE_FILTER, false);
}

bool FileTypeSourceLocation::useRegExFileFilter() const {
	assert(m_SourceData.m_pExtendedProperties);
	return m_SourceData.m_pExtendedProperties->GetBool(SOURCE_REGULAR_EXPRESSION_FILE_FILTER, false);
}

namespace {
	const string FILE_LIST_LIMIT_DE_NAME("etlforge.source.file_limit");
}

int FileTypeSourceLocation::getFileListLimit(const OperationDataElementHolder* pDeHolder) const {
	return konga::GetSystemVariableAsLong(FILE_LIST_LIMIT_DE_NAME, pDeHolder, -1);
}

