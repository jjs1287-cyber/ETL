/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <string.h>
#include <iostream>

#include <konga/Utils/CrException.h>

#include <konga/Utils/PortDefs.h>	                    
#include <GetPot>

#include <konga/Server/Server.h>

#include "ETLForgeUtilsDb.h"
#include "ETLForgeUtilsOperation.h"
#include "ETLForgeUtilsCommands.h"
#include "ETLForgeUtilsJUEIP.h"
#include "HttpProxyConfig.h"
#include "ListOperations.h"
#include "ServerLabel.h"

using namespace konga;
using namespace std;

namespace {
	const char restoreDb[] = "--restore-db";
	const char backupDb[] = "--backup-db";
	const char vacuumDbOption[] = "--vacuum-db";
	const char reindexDbOption[] = "--reindex-db";

	const char reclaimDiskSpace[] = "--reclaim-disk-space";

	const char addScheduledOperations[] = "--run-scheduled-operations";
	const char addOperationToQueue[] = "--add-operation-to-queue";

	const char listOperations[] = "--list-operations";
	const char projectOnly[] = "--project";

	const char disableSchedule[] = "--disable-schedule";
	const char enableSchedule[] = "--enable-schedule";

	const char sendCommand[] = "--send-command";

	const char setHttpProxyHost[] = "--set-http-proxy-host";
	const char setHttpProxyUserName[] = "--set-http-proxy-username";
	const char setHttpProxyPwd[] = "--set-http-proxy-pwd";
	const char setHttpProxyExceptions[] = "--set-http-proxy-exceptions";

	const char disableHttpProxy[] = "--disable-http-proxy";
	const char enableHttpProxy[] = "--enable-http-proxy";
	const char printHttpProxyConfiguration[] = "--print-http-proxy-configuration";

	const char setServerLabel[] = "--set-server-label";
	const char printServerLabel[] = "--print-server-label";

	const char resetInstallation[] = "--reset-installation";

	const char cleanWsdl[] = "--clean-wsdl-files";

	const char enableJUEIP[] = "--enable-jueip";
	const char disableJUEIP[] = "--disable-jueip";

	const char help[] = "--help";
	const char quiet[] = "--quiet";
	const char empty[] = "";

	void printSpaces(const char* command, const char* option) {
		const size_t max = 28;
		size_t i = strlen(command) + strlen(option);
		for( ; i < max; ++i ) {
			cout << " ";
		}
	}

	void printLine(const char* command, const char* option, const char* comment) {
		cout << " " << command << " " << option;
		printSpaces(command, option);
		cout << " " << comment << endl;
	}

	int PrintUsage() {
		cout << "Usage: ETLForgeUtils [options]" << endl;
		cout << "Options:" << endl;

		printLine(restoreDb, "<db> <dir>", "Restore database <db> from files in directory <dir>");
		printLine(backupDb, "<db> <dir>", "Backup database <db> to directory <dir>.");
		printLine(vacuumDbOption, "[<db>]", "Vacuum specified database.");
		printLine("", "", "If no database or \"all\" is specified, all databases are vacuumed.");
		printLine(reindexDbOption, "[<database>]", "Re-indexes the specified database.");
		printLine("", "", "If no database or \"all\" is specified, all databases are re-indexed.");

		printLine(reclaimDiskSpace, "", "Aggressively reclaim disk-space used by the back-end.");
		printLine("", "", "Archive tables will be truncated, all tables vacuumed.");

		printLine(addScheduledOperations, "", "Adds all scheduled operatins to the process queue.");
		printLine(addOperationToQueue, "<operation>", "Adds the specified operation to the process queue.");
		printLine("", "", "Specify the operation name, GUID or database id.");

		printLine(listOperations, "--project <name>", "List all operations (optionally only search one project).");

		printLine(sendCommand, "<module> <command>", "Send command <command> to module <module>");

		printLine(setHttpProxyHost, "<hostname>[:<port>]", "Set http proxy host name (port is optional).");
		printLine(setHttpProxyUserName, "<user name>", "User name for proxy server authentication.");
		printLine(setHttpProxyPwd, "<password>", "Set password for proxy server authentication.");
		printLine(setHttpProxyExceptions, "<exceptions>", "Comma separated list of host names for which the proxy configuration should not be used.");

		printLine(disableHttpProxy, "", "Disable any http proxy configuration.");
		printLine(enableHttpProxy, "", "Enable a previously disabled proxy configuration.");
		printLine(printHttpProxyConfiguration, "", "Prints the current http proxy configuration.");

		printLine(setServerLabel, "[<label>]", "Label the server as \"Production\", \"Test\", etc.");
		printLine(printServerLabel, "", "Print current server label.");

		printLine(cleanWsdl, "", "Remove unused WSDL files.");

		printLine(enableJUEIP, "", "Enable the ETLForge User Experience Improvement Program.");
		printLine(disableJUEIP, "", "Disable the ETLForge User Experience Improvement Program.");

		printLine(help, "", "Print this message.");
		printLine(quiet, "", "Only print fatal error messages.");

		return 1;
	}

	bool IsArg(const string& str) {
		static vector<string> args;
		if ( args.empty() ) {
			args.push_back(setHttpProxyHost);
			args.push_back(setHttpProxyPwd);
			args.push_back(setHttpProxyUserName);
			args.push_back(setHttpProxyExceptions);
			args.push_back(disableHttpProxy);
			args.push_back(enableHttpProxy);
		}
		return std::find(args.begin(), args.end(), str) != args.end();
	}

	bool IsEmpty(const string& arg) {
		return arg.empty() || IsArg(arg);
	}
}

int main(int argc, char* argv[])
{
	                                
	GetPot args(argc, argv);

	if ( argc == 1 || args.search(2, help, "-h") )
		return PrintUsage();

	const bool bQuiet = args.search(2, quiet, "-q");

	bool bPrintUsage = true;
	try {
		                                                          
		if ( args.search(vacuumDbOption) ) {
			string db(args.next(empty));
			if ( !db.empty() && *db.begin() == '-' ) db.clear();
			VacuumDatabase(db, bQuiet);
			bPrintUsage = false;
		}
		if ( args.search(reindexDbOption) ) {
			string db(args.next(empty));
			if ( !db.empty() && *db.begin() == '-' ) db.clear();
			ReindexDatabase(db, bQuiet);
			bPrintUsage = false;
		}

		if ( args.search(restoreDb) ) {
			const string db(args.next(empty));
			if ( db.empty() )
				throw CrException("No database to restore was specified.");
			const string dir(args.next(empty));
			if ( dir.empty() )
				throw CrException("No directory to restore database from was specified.");
			RestoreDatabase(db, dir, bQuiet);
			bPrintUsage = false;
		}
		if ( args.search(backupDb) ) {
			const string db(args.next(empty));
			if ( db.empty() )
				throw CrException("No database to back up was specified.");
			const string dir(args.next(empty));
			if ( dir.empty() )
				throw CrException("No directory to back up database to was specified.");
			BackupDatabase(db, dir, bQuiet);
			bPrintUsage = false;
		}

		if ( args.search(reclaimDiskSpace) ) {
			ReclaimDiskSpace(bQuiet);
			bPrintUsage = false;
		}

		if ( args.search(setServerLabel) ) {
			const string label(args.next(empty));
			ETLForgeUtils::SetServerLabel(label, bQuiet);
			bPrintUsage = false;
		}
		if ( args.search(printServerLabel) ) {
			ETLForgeUtils::PrintServerLabel(bQuiet);
			bPrintUsage = false;
		}

		if ( args.search(cleanWsdl) ) {
			string w;
			CleanWsdlFilesNotInDb(w);
			if ( !w.empty() ) {
				std::cerr << w << std::endl;
			}
			bPrintUsage = false;
		}

		if ( args.search(sendCommand) ) {
			const string module(args.next(empty));
			if ( module.empty() )
				throw CrException("No module name specified for sending command to.");
			const string command(args.next(empty));
			if ( command.empty() )
				throw CrException("No command specified to be sent to module \"" + module + "\".");

			SendCommandToModule(module, command, bQuiet);
			bPrintUsage = false;
		}

		if ( args.search(addScheduledOperations) ) {
			AddScheduledOperationsToQueue(bQuiet);
			bPrintUsage = false;
		}
		if ( args.search(addOperationToQueue) ) {
			const string operation(args.next(empty));
			if ( operation.empty() )
				throw CrException("No operation specified");
			AddOperationToQueue(operation, bQuiet);
			bPrintUsage = false;
		}

		if ( args.search(listOperations) ) {
			string project;
			if ( args.search(projectOnly) ) project = args.next(empty);
			konga::ListOperations(project);
			bPrintUsage = false;
		}

		if ( args.search(enableJUEIP) ) {
			EnableJUEIP();
			bPrintUsage = false;
		}
		else if ( args.search(disableJUEIP) ) {
			DisableJUEIP();
			bPrintUsage = false;
		}

		if ( args.search(resetInstallation) ) {
			konga::ResetServerGuid();
			std::cout << "server guid was reset" << std::endl;
			bPrintUsage = false;
		}

		{
			                                       
			bool bDisable = false;
			if ( args.search(disableHttpProxy) ) {
				bDisable = true;
			}
			bool bEnable = false;
			if ( args.search(enableHttpProxy) ) {
				bEnable = true;
			}
			if ( bDisable && bEnable ) {
				throw CrException("Conflicting options: " + string(disableHttpProxy) + " and " + string(enableHttpProxy) + ".");
			}

			bool bPrintConfig = false;
			if ( args.search(printHttpProxyConfiguration) ) {
				bPrintConfig = true;
			}

			bool bHost = false, bUser = false, bPwd = false, bExceptions = false;
			string host, user, pwd, exceptions;
			if ( args.search(setHttpProxyHost) ) {
				bHost = true;
				host = args.next(empty);
				if ( IsEmpty(host) )
					throw CrException(string(setHttpProxyHost) + " option requires a hostname (host[:port])");
			}
			if ( args.search(setHttpProxyUserName) ) {
				bUser = true;
				user = args.next(empty);
				if ( IsEmpty(user) ) user.clear();
			}
			if ( args.search(setHttpProxyPwd) ) {
				bPwd = true;
				pwd = args.next(empty);
				if ( IsEmpty(pwd) ) pwd.clear();
			}
			if ( args.search(setHttpProxyExceptions) ) {
				bExceptions = true;
				exceptions = args.next(empty);
				if ( IsEmpty(exceptions) ) exceptions.clear();
			}

			if ( bHost || bUser || bPwd || bExceptions) {
				konga::ETLForgeUtils::SetProxyConfiguration(bHost, host, bUser, user, bPwd, pwd, bExceptions, exceptions);
				bPrintUsage = false;
				bPrintConfig = true;
			}

			if ( bEnable ) {
				konga::ETLForgeUtils::EnableProxyConfiguration();
				bPrintUsage = false;
				bPrintConfig = true;
			}
			else if ( bDisable ) {
				konga::ETLForgeUtils::DisableProxyConfiguration();
				bPrintUsage = false;
				bPrintConfig = true;
			}
			if ( bPrintConfig ) {
				cout << konga::ETLForgeUtils::GetProxyConfiguration();
				bPrintUsage = false;
			}
		}
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl << endl;
		return 1;
	}

	if ( bPrintUsage ) {
		if ( !args.search("utils") ) {
			                                                                                                                                               
			cout << "Invalid argument: " << argv[1] << endl;
		}
		return PrintUsage();
	}

	return 0;
}

