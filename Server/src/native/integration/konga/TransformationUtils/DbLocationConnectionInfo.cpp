/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/Db/TranDb/SourcesTab.h>
#include <konga/Db/TranDb/TargetsTab.h>

#include <konga/OperationContext/OperationDataElementHolder.h>

#include "DbLocationConnectionInfo.h"

using namespace konga;
using namespace TranDb;
using std::string;

DbLocationConnectionInfo DbLocationConnectionInfoFactory::fromSourcesTab(DbConnection *tranDb, int sourceId) {
    konga::SmartDbPtr<SourcesTab> srcTab(SourcesTab::MakeInstance(tranDb));
	string filter("WHERE Source_ID = " + konga::toString(sourceId));
	srcTab->Select(filter);
	if (srcTab->Next()) {
		DbLocationConnectionInfo info;
		if (!srcTab->IsLocatorNull()) {
			info.Server = srcTab->GetLocator();
		}
		if (!srcTab->IsDriverNull()) {
			info.DriverName = srcTab->GetDriver();
		}
		info.DriverType = konga::ODBC;
		if (!srcTab->IsDriverTypeNull() && (srcTab->GetDriverType() == konga::JDBC)) {
			info.DriverType = konga::JDBC;
		}
		if (!srcTab->IsDBNameNull()) {
			info.Database = srcTab->GetDBName();
		}
		if (!srcTab->IsLoginNull()) {
			info.User = srcTab->GetLogin();
		}
		if (!srcTab->IsPasswordNull()) {
			info.Password = srcTab->GetPassword();
		}
		if (!srcTab->IsConnectStringNull()) {
			info.ManualConnectionString = srcTab->GetConnectString();
		}
		if (!srcTab->IsOptional_ConnectStringNull()) {
			info.OptionalConnectionParams = srcTab->GetOptional_ConnectString();
		}
		info.Port = srcTab->IsPortNull() ? -1 : srcTab->GetPort();
		info.Secured = srcTab->IsSecuredNull() ? 0 : srcTab->GetSecured();
		info.Path = srcTab->IsPathNull() ? "" : srcTab->GetPath();
		return info;
	}
	else {
		throw CrException("No source found with the ID " + konga::toString(sourceId));
	}
}

DbLocationConnectionInfo DbLocationConnectionInfoFactory::fromTargetsTab(DbConnection* tranDb, int targetId) {
    konga::SmartDbPtr<TargetsTab> tgtTab(TargetsTab::MakeInstance(tranDb));
	string filter("WHERE Target_ID = " + konga::toString(targetId));
	tgtTab->Select(filter);
	if (tgtTab->Next()) {
		DbLocationConnectionInfo info;
		if (!tgtTab->IsLocatorNull()) {
			info.Server = tgtTab->GetLocator();
		}
		if (!tgtTab->IsDriverNull()) {
			info.DriverName = tgtTab->GetDriver();
		}
		info.DriverType = konga::ODBC;
		if (!tgtTab->IsDriverTypeNull() && (tgtTab->GetDriverType() == konga::JDBC)) {
			info.DriverType = konga::JDBC;
		}
		if (!tgtTab->IsDBNameNull()) {
			info.Database = tgtTab->GetDBName();
		}
		if (!tgtTab->IsLoginNull()) {
			info.User = tgtTab->GetLogin();
		}
		if (!tgtTab->IsPasswordNull()) {
			info.Password = tgtTab->GetPassword();
		}
		if (!tgtTab->IsConnectStringNull()) {
			info.ManualConnectionString = tgtTab->GetConnectString();
		}
		if (!tgtTab->IsOptional_ConnectStringNull()) {
			info.OptionalConnectionParams = tgtTab->GetOptional_ConnectString();
		}
		info.Port = tgtTab->IsPortNull() ? -1 : tgtTab->GetPort();
		info.Secured = tgtTab->IsSecuredNull() ? 0 : tgtTab->GetSecured();
		info.Path = tgtTab->IsPathNull() ? "" : tgtTab->GetPath();
		return info;
	}
	else {
		throw CrException("No target found with the ID " + konga::toString(targetId));
	}
}

void konga::replaceGlobalDataElementsInDbLocationConnectionInfo(DbLocationConnectionInfo& dbLocationConnectionInfo, const OperationDataElementHolder& dh) {
	dbLocationConnectionInfo.Path = dh.replaceOperationDataElementInString(dbLocationConnectionInfo.Path);
	dbLocationConnectionInfo.Server = dh.replaceOperationDataElementInString(dbLocationConnectionInfo.Server);
	dbLocationConnectionInfo.User = dh.replaceOperationDataElementInString(dbLocationConnectionInfo.User);
}
