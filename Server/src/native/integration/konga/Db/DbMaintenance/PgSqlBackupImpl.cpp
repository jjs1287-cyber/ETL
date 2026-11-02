/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <fstream>
#include <map>

#include <konga/Utils/AutoPtrCleanup.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/AutoFileCleanup.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/TableColumnsOdbc.h>	              
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include "PgSqlMaintenance.h"

using namespace konga;
using std::string;
using std::vector;
using std::map;

const string konga::DB_NULL_STR = "<NULL>";

namespace {
	const char CHARS_TO_ESCAPE[] = "\\\t";

	                                              
	                                                          
	   
	void EscapePgSqlCopySpecialCharacters(string& value) {
		konga::AddEscapeSequences(value, CHARS_TO_ESCAPE);
		string in(value), out;
		konga::ReplaceCharWithString(in, out, '\n', "\\n");
		konga::ReplaceCharWithString(out, value, '\r', "\\r");
	}
}

void konga::PgSqlBackup(const string& sConnectionString,
						const string& sBackupDir_in,
						StatusReceiver& caller,
						const vector<string>& vTablesToIgnore)
{
	map<string, int> tablesToIgnore;
	for( vector<string>::const_iterator cit = vTablesToIgnore.begin();
		cit != vTablesToIgnore.end(); ++cit )
	{
		string tmp(*cit);
		ToLower(tmp);
		tablesToIgnore.insert( map<string, int>::value_type(tmp, 1) );
	}

	if ( !konga::IsDirectory(sBackupDir_in) )
		throw CrException("Backup failed: \"" + sBackupDir_in + "\" is not a valid directory.");

	string sBackupDirWithSlash(sBackupDir_in);
	AppendPathSeparator(sBackupDirWithSlash);

	vector<DbTable> vTables;
	{
		OdbcColumns odbcSchemaInfo(sConnectionString);
		if( !odbcSchemaInfo.GetTables(vTables, "", "public") )
			throw CrException("Failed to get tables. Reason: " + odbcSchemaInfo.GetLastError());
	}

	SmartDbPtr<DbConnection> pConn( DbConnection::MakeInstance(sConnectionString) );
	const int nTables = vTables.size();
	int iTable = 0;

	vector<DbTable>::const_iterator it = vTables.begin(), end_it = vTables.end();
	for( ; it != end_it; ++it, ++iTable ) {
		const DbTable& dbTable = *it;
		if ( dbTable.Type == DBTABLE_TYPE_TABLE ) {
			const string& sTableName = dbTable.Name;
			string tmp(sTableName);
			ToLower(tmp);
			if ( tablesToIgnore.find(tmp) != tablesToIgnore.end() )
				continue;

			caller.Status(sTableName);
			caller.Progress(static_cast<float>(iTable)/nTables);

			const string sFileName(sBackupDirWithSlash + sTableName + ".txt");

			std::ofstream f;
			f.exceptions(std::ios_base::failbit | std::ios_base::badbit);
			try {
				f.open(sFileName.c_str(), std::ios_base::out);

				string sql = "SELECT * FROM " + sTableName;
				SmartDbPtr<DynamicDbRecordSet> pRS( DynamicDbRecordSet::MakeInstance(pConn) );
				pRS->SetSqlStatement(sql);
				pRS->Open();
				int iFieldCount = -1;
				while ( pRS->Next() ) {
					if ( iFieldCount == -1 )
						iFieldCount = pRS->GetFieldCount();

					for( int iField = 0; iField < iFieldCount; ++iField ) {
						if ( iField > 0 )
							f << "\t";
						if ( pRS->IsFieldNull(iField) )
							f << DB_NULL_STR;

						string field(pRS->GetFieldValue(iField));
						EscapePgSqlCopySpecialCharacters(field);
						f << field;
					}
					f << std::endl;
				}
				f.close();
			}
			catch( const DbException& ex ) {
				caller.ReportError("Database error occurred: " + ex.GetErrorString());
			}
			catch( const CrException& ex ) {
				caller.ReportError("Non-database error occurred: " + ex.GetErrorString());
			}
			catch(const std::exception& e) {
				caller.ReportError("I/O Error occurred: " + string(e.what()));
			}			
		}
	}
}
