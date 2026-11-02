/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <string>
#include <vector>
#include <map>

#include <konga/Utils/AutoPtrCleanup.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/TableColumnsOdbc.h>	              

#include "PgSqlMaintenance.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	void AddError(string& stError, const string& stNewError) {
		if ( stNewError.empty() )
			return;

		if ( !stError.empty() )
			stError += "\n";
		stError += stNewError;
	}
}

void konga::PgSqlReindex(const std::string& stConnectionString, StatusReceiver& caller) {
	vector<DbTable> vTables;
	{
		OdbcColumns odbcSchemaInfo(stConnectionString);
		if( !odbcSchemaInfo.GetTables(vTables, "", "public") )
			throw CrException("Failed to get tables. Reason: " + odbcSchemaInfo.GetLastError());
	}

	SmartDbPtr<DbConnection> pConn( DbConnection::MakeInstance(stConnectionString) );

	vector<DbTable>::const_iterator it = vTables.begin(), end_it = vTables.end();
	for( ; it != end_it; ++it ) {
		const DbTable& dbTable = *it;
		if ( dbTable.Type == DBTABLE_TYPE_TABLE ) {
			caller.Status(dbTable.Name);
			pConn->ExecuteSql("REINDEX TABLE " + dbTable.Name);
			caller.Status("done");
		}
	}
}

void konga::PgSqlVacuum(const std::string& stConnectionString, bool full, StatusReceiver& caller) {
	SmartDbPtr<DbConnection> pConn( DbConnection::MakeInstance(stConnectionString) );
	caller.Status("Vacuum analyze database...");
	string sql("VACUUM ");
	if ( full ) sql += "FULL ";
	sql += "ANALYZE";
	pConn->ExecuteSql(sql);
	caller.Status("done");
}
