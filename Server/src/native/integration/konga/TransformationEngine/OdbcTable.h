/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef SOURCEPREPSTMT_H
#define SOURCEPREPSTMT_H

#include <string>
#include <vector>

#include "DbAccess.h"

class DbRecord;
class CROM;

namespace konga {
	class Utf8Conv;
}

class OdbcTable {
	friend class OdbcSource;
	friend class OdbcColumn;
protected:
	OdbcSource*m_dbSource;
	std::string m_sql;
	SQLHANDLE m_stmtHandle;
	CROM* m_pTable;
	FILE* m_debug_log;

	std::vector<CROM*> m_bindableColumns, m_notypeColumns;
	std::vector<CROM*> m_foreignKeys;
	std::vector<CROM*>* m_columns;
	std::vector<OdbcColumn*> m_odbcColumns;
	std::vector<OdbcColumn*> m_markers;

	KString m_LastErr;

protected:
	OdbcTable(OdbcSource*dbSource, CROM*pTable, SQLHDBC hConn);
	~OdbcTable();

	void execute();
	bool execute_with_params();
	SQLRETURN fetch();
	void loadDbRecord(DbRecord*record, bool bFirstRow, konga::Utf8Conv* conv);

private:
	void bindColumns();
	void bindMarkers();
	CViewLog* getViewLog();
	CErrorLog* getErrorLog();

protected:
	SQLRETURN GetSQLState(char* sState);

	const char*getDriverName();
	unsigned long CloseCursor();
	void writeLog(const char* msg);
	
};


#endif                    

