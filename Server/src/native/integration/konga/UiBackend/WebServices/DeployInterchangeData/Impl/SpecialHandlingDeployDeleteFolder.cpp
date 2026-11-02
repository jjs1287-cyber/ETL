/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <string>

#include <konga/Utils/StringUtils.h>	           

#include <konga/Db/DbUtils/DbConnection.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "SpecialHandlingDeployDeleteFolder.h"

using namespace konga;
using namespace konga::interchange_deploy;

bool SpecialHandlingDeployDeleteFolder::HandleRow(long folderId) {
	                                                     
	                                                                                    
	const std::string sql("DELETE FROM FolderItemTab WHERE ParentFolder_ID = " + toString(folderId));
	UIBACKEND_TRACE("SpecialHandlingDeployDeleteFolder::HandleRow - " + sql);
	m_dbConnection.ExecuteSql(sql);
	return true;
}

