/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifdef WIN32
#include <windows.h>
#endif


#ifdef HAVE_TERMIOS_H
#include <termios.h>
                                                      
#define DEVTTY "/dev/tty"
#endif

#include <iostream>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/DBConnectionInfo.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/Db/DbMaintenance/PgSqlMaintenance.h>
#include <konga/Db/DbMaintenance/DbMaintenance.h>

#include <konga/UiBackend/UploadedFilesManager/WsdlCleanup.h>

#include "ETLForgeUtilsDb.h"

using namespace konga;

using std::string;
using std::vector;

namespace {
	const string sIDb("IDb");
	const string sMsgDb("MsgDb");
	const string sPDb("PDb");
	const string sTranDb("TranDb");

	class DbStatusReceiver : public StatusReceiver {
	public:
		DbStatusReceiver(const string& what, bool bQuiet) : m_what(what), m_bQuiet(bQuiet) {}
		virtual ~DbStatusReceiver(void) {}

		virtual void Status(const std::string& stStatus                                        ) {
			if ( m_bQuiet )
				return;
			std::cout << m_what << " " << stStatus << std::endl;
		}

		virtual void Progress(float prog) {
		}

		virtual void ReportError(const std::string& stError) {
			std::cout << stError << std::endl;
		}

	private:
		string m_what;
		bool m_bQuiet;
	};

	string PromptForPwd() {
		using namespace std;

		cout << "Enter the postgres administrative password: ";
#ifdef HAVE_TERMIOS_H
		FILE* termin = fopen(DEVTTY, "r");
		struct termios t_orig;
		if ( termin != 0 ) {
			struct termios t_new;
			tcgetattr(fileno(termin), &t_orig);
			t_new = t_orig;
			t_new.c_lflag &= ~ECHO;
			tcsetattr(fileno(termin), TCSAFLUSH, &t_new);
		}
                                      
#else
#ifdef WIN32
		                                                     
		DWORD oldConsoleMode = 0;
		HANDLE h = GetStdHandle(STD_INPUT_HANDLE);
		if ( h != INVALID_HANDLE_VALUE ) {
			if ( GetConsoleMode(h, &oldConsoleMode) ) {
				DWORD newConsoleMode = oldConsoleMode & ( ~ ENABLE_ECHO_INPUT );
				if ( !SetConsoleMode(h, newConsoleMode) ) {
					cout << "Failed to set console mode" << endl;
				}
			}
		}
#endif
#endif

		char pwd[256];
		cin.getline(pwd, 256);
		cout << endl;

#ifdef HAVE_TERMIOS_H
		tcsetattr(fileno(termin), TCSAFLUSH, &t_orig);
#else
#ifdef WIN32
		if ( oldConsoleMode != 0 ) {
			SetConsoleMode(h, oldConsoleMode);
			CloseHandle(h);
		}
#endif
#endif

		return string(pwd);
	}
}

void konga::VacuumDatabase(const std::string& sDb, bool bQuiet) {
	                 
	if ( sDb.empty() || konga::EqualsIgnoreCase(sDb, "all") ) {
		VacuumDatabase(sIDb, bQuiet);
		VacuumDatabase(sMsgDb, bQuiet);
		VacuumDatabase(sPDb, bQuiet);
		VacuumDatabase(sTranDb, bQuiet);
		return;
	}
	DbStatusReceiver statusReceiver("", bQuiet);
	statusReceiver.Status("Vacuuming database " + sDb);
	konga::PgSqlVacuum(DBConnectionInfo(sDb).connectionString(), true, statusReceiver);
}

void konga::ReindexDatabase(const std::string& sDb, bool bQuiet) {
	                 
	if ( sDb.empty() || konga::EqualsIgnoreCase(sDb, "all") ) {
		ReindexDatabase(sIDb, bQuiet);
		ReindexDatabase(sMsgDb, bQuiet);
		ReindexDatabase(sPDb, bQuiet);
		ReindexDatabase(sTranDb, bQuiet);
		return;
	}
	DbStatusReceiver statusReceiver("", bQuiet);
	statusReceiver.Status("Re-indexing database " + sDb);
	konga::PgSqlReindex(DBConnectionInfo(sDb).connectionString(), statusReceiver);
}

void konga::BackupDatabase(const std::string& sDb, const std::string& sDir, bool bQuiet) {
	DBConnectionInfo dbInfo(sDb);

	if ( !bQuiet )
		std::cout << "Trying to back up database " << sDb << std::endl;
	DbStatusReceiver caller("Dumping table: ", bQuiet);
	vector<string> v;

	kCreateDirectory(sDir);
	konga::PgSqlBackup(dbInfo.connectionString(), sDir, caller, v);
}

void konga::RestoreDatabase(const std::string& sDb, const std::string& sDir, bool bQuiet) {

	string pwd = PromptForPwd();
                
                                                                     
         

	DBConnectionInfo dbInfo(sDb);
	dbInfo.pwd(pwd);
	dbInfo.uid("postgres");

	std::cout << "Trying to restore database " << sDb << std::endl;
	DbStatusReceiver caller("Restoring table: ", bQuiet);
	konga::PgSqlRestore(dbInfo.connectionString(), sDir, caller);
}

void konga::CleanWsdlFilesNotInDb(string& sWarnings) {
	 SmartDbPtr<DbConnection> pDbConn( DbConnection::MakeInstance(DBConnectionInfo(sTranDb).connectionString()) );
	 konga::CleanWsdlFiles(*pDbConn, sWarnings);
}

namespace {
	void ReclaimImpl(const string& db, bool bQuite) {
		VacuumDatabase(db, bQuite);
		ReindexDatabase(db, bQuite);
	}

	void TruncateImpl(DbConnection& dbConn, const string& table) {
		dbConn.ExecuteSql("truncate table " + table);
	}
}

void konga::ReclaimDiskSpace(bool bQuiet) {

	SmartDbPtr<DbConnection> pIDbConn( DbConnection::MakeInstance(DBConnectionInfo(sIDb).connectionString()) );
	TruncateImpl(*pIDbConn, "archiveactivitytab");
	TruncateImpl(*pIDbConn, "archivedataelementtab");
	TruncateImpl(*pIDbConn, "archivedataelementtexttab");
	TruncateImpl(*pIDbConn, "archiveprocesstab");
	TruncateImpl(*pIDbConn, "archiveroutetab");
	TruncateImpl(*pIDbConn, "archivetasktab");
	TruncateImpl(*pIDbConn, "archivetasktab");

	SmartDbPtr<DbConnection> pDbConn( DbConnection::MakeInstance(DBConnectionInfo(sMsgDb).connectionString()) );
	TruncateImpl(*pDbConn, "archiveattachmenttab");
	TruncateImpl(*pDbConn, "archivedataelementtab");
	TruncateImpl(*pDbConn, "archivedataelementtexttab");
	TruncateImpl(*pDbConn, "archivedistributionjobinstancetab");
	TruncateImpl(*pDbConn, "archivejmspropertiestab");
	TruncateImpl(*pDbConn, "archivejmssendingpropertiestab");
	TruncateImpl(*pDbConn, "archivemsgtab");
	TruncateImpl(*pDbConn, "archiveoutgoingjmsmsgtab");
	TruncateImpl(*pDbConn, "archiveoutgoingmsgtab");

	konga::CleanOperationDataElementTab(*pIDbConn, *pDbConn);
	pIDbConn.CloseAndDelete();

	pDbConn.Attach( DbConnection::MakeInstance(DBConnectionInfo(sTranDb).connectionString()) );
	TruncateImpl(*pDbConn, "archivebillingtab");
	TruncateImpl(*pDbConn, "archivelogauxtab");
	TruncateImpl(*pDbConn, "archivelogtab");
	TruncateImpl(*pDbConn, "archivexferhist");
	
	pDbConn.CloseAndDelete();

	ReclaimImpl(sIDb, bQuiet);
	ReclaimImpl(sMsgDb, bQuiet);
	ReclaimImpl(sTranDb, bQuiet);
}
