/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include "PgSqlMaintenance.h"

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/FilesTraverser.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/TableColumnsOdbc.h>	              

using namespace konga;
using std::string;

namespace {
	class BackupFileMatcher : public Matcher {
	public:
		BackupFileMatcher(void) :
		  Matcher(), m_ourFileExt("txt") {}
		virtual ~BackupFileMatcher(void) {}

		virtual bool isMatch(const std::string& sFileName) const {
			const string& fileExt = GetFileExtension(sFileName);
			return EqualsIgnoreCase(fileExt, m_ourFileExt);
		}

		const string m_ourFileExt;
	};

	class RestoreFileAction : public FileAction {
	public:
		RestoreFileAction(const std::string& sConnectionString, StatusReceiver& caller)
			: FileAction(), m_sConnStr(sConnectionString), m_pConn(0),
			m_caller(caller)
		{
			m_pConn.Attach( DbConnection::MakeInstance(sConnectionString) );
		}

		~RestoreFileAction(void) {}

		virtual void doAction(const std::string& sFilePath) {
			const string& sLeaf = GetLeafPath(sFilePath);
			string::size_type ipos = sLeaf.find_last_of(".");
			if ( ipos == string::npos )
				return;

			const string& sTableName = sLeaf.substr(0, sLeaf.size()-4);

			m_caller.Status(sTableName);

			string sql("TRUNCATE " + sTableName + ";");
			string kosherFilePath(sFilePath);
			ReplaceCharWithChar(kosherFilePath, '\\', '/');
			sql += "COPY " + sTableName + " FROM '" + kosherFilePath
				+ "' DELIMITER AS '\t' NULL AS '" + DB_NULL_STR + "'";

			try {
				m_pConn->ExecuteSql(sql);
			}
			catch( const DbException& ex ) {
				m_caller.ReportError("A database error occurred: " + ex.GetErrorString());
				RenewConnection();
			}
		}

	private:
		                                       
		void RenewConnection() {
			m_pConn.CloseAndDelete();
			m_pConn.Attach( DbConnection::MakeInstance(m_sConnStr) );
		}

	private:
		const string m_sConnStr;
		SmartDbPtr<DbConnection> m_pConn;
		StatusReceiver& m_caller;
	};
}

void konga::PgSqlRestore(const string& sConnectionString,
						 const string& sBackupDir, StatusReceiver& caller)
{
	if ( !konga::IsDirectory(sBackupDir) )
		throw CrException("Backup failed: \"" + sBackupDir + "\" is not a valid directory.");

	BackupFileMatcher matcher;
	RestoreFileAction action(sConnectionString, caller);

	FilesTraverser traverser(action, matcher);
	traverser.traverseDirectory(sBackupDir, false);
}
