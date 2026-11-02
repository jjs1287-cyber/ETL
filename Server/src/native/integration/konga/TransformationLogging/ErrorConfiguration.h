/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_TRANSFORMATION_ERROR_CONFIGURATION_H
#define ETLFORGE_TRANSFORMATION_ERROR_CONFIGURATION_H

#include <string>

                                                                            

namespace konga {

	namespace ErrorConfiguration {

		                                                          
		const std::string NO_ROWS_AFFECTED_ON_DBUPDATES("NoRowsAffectedOnDbUpdates");
		const std::string NO_TARGET_DATA_GENERATED("NoTargetDataGenerated");
		const std::string NO_WEBSERVICE_REQUEST_DATA("NoWebServiceRequestData");
		const std::string EMPTY_DATABASE_SOURCE("EmptyDatabaseSource");

		bool isOk(const std::string& name, bool isDefault = true);

		bool isWarning(const std::string& name, bool isDefault = false);

		bool isError(const std::string& name, bool isDefaul = false);
	}
}

#endif                                                 

