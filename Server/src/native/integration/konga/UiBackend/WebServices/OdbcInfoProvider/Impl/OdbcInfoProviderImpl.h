/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef OdbcInfoProviderImplE_H
#define OdbcInfoProviderImplE_H

#include <konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.h>

#include "../WsDatabaseInfo.hpp"
#include "../WsDatabaseDriverInfo.hpp"

namespace konga {

class OdbcInfoProviderImpl : public WsImplBaseAuthenticator {
public:
	OdbcInfoProviderImpl(const xsd__string user, const xsd__string password);
	virtual ~OdbcInfoProviderImpl(void) {}

	                                                                         
	WsDatabaseInfo* getDatabaseObjects(
		const xsd__string DriverName,
		const xsd__string ConnectionString,
		const xsd__string SourceGuid,
		const xsd__string TargetGuid,
		const xsd__string_Array* const ObjectTypes,
		const xsd__string TableSearchString,
		const xsd__string SchemaSearchString
		);

	                                                          
	WsDatabaseInfo* getTableInfo(
		const xsd__string DriverName,
		const xsd__string ConnectionString,
		const xsd__string SourceGuid,
		const xsd__string TargetGuid,
		const WsDatabaseObject_Array* const tables
		);

	WsDatabaseDriverInfo* getDrivers();
};

}                   

#endif                           
