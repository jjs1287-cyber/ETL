/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/StringUtils.h>	                   

#include <konga/ItemAccess/UserInfo.h>

#include <konga/DataElement/DataElement.h>

#include "OperationDataElementHolder.h"
#include "AccessProtectedDataElementsHandler.h"

using namespace konga;
using std::string;

namespace {
	const string SYSDB_CONNECTIONSTRING("__etlforge.sysdb.connectionstring");
}

void AccessProtectedDataElementsHandler::RegisterAll(OperationDataElementHolder& deHolder, AccessProtectedDataElementsHandler& handler) {
	deHolder.registerDataElementHandler(SYSDB_CONNECTIONSTRING, &handler);
}

void AccessProtectedDataElementsHandler::UnRegisterAll(OperationDataElementHolder& deHolder) {
	deHolder.registerDataElementHandler(SYSDB_CONNECTIONSTRING, 0);
}

DataElement* AccessProtectedDataElementsHandler::getDataElement(const string& deName) const {
	if ( deName.size() < 20 ) return 0;
	if ( deName.at(0) != '_' || deName.at(1) != '_' ) return 0;

	if ( konga::EqualsIgnoreCase(deName, SYSDB_CONNECTIONSTRING) ) {
		if ( !m_UserInfo.IsAdministrator() )
			throw CrException("Only administrative users have permission to access the internal operation data element $" +
			SYSDB_CONNECTIONSTRING + ".");
		return new DataElement(DataElement::t_string, DBConnectionInfo("TranDb").connectionString());
	}

	return 0;
}

