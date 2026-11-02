/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <string>

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>		                  
#include <konga/Utils/ProcessUtils.h>
#include <konga/Utils/AutoFileCleanup.h>

#include "Deployer.h"

using namespace konga;
using namespace std;

void konga::DeployJitterpak(const std::string& user, const std::string& pwdHash, const std::string& host,
							const std::string& jitterpakFileName, const std::string& jarFileName,
							const std::string& jvmArgs            )
{
	const string argsWithoutPwd( jvmArgs + " -jar \"" + jarFileName + "\" -s \"" + host + "\" -u \"" + user
		+ "\" -jp \"" + jitterpakFileName + "\"" );

	string sFinalError;
	{
		                                                                                    
		AutoDeleteFileSet fileCleanup;
		try {
			const string args( argsWithoutPwd + " -ph \"" + pwdHash + "\"" );
			const string stdOutFileName(getTmpFileName("deployStdOut"));
			const string stdErrFileName(getTmpFileName("deployStdErr"));
			ProcessIdT pid = RunProc("java", args, ".", stdOutFileName, stdErrFileName);
			const int exitCode = WaitForProc(pid);

			string sErrLog;
			if ( FileExists(stdOutFileName) ) {
				fileCleanup.AddFile(stdOutFileName);
			}

			if ( FileExists(stdErrFileName) ) {
				read_file_to_string(stdErrFileName, sErrLog);
				fileCleanup.AddFile(stdErrFileName);
			}

			if( exitCode != 0 || !sErrLog.empty())
				throw CrException("Command returned exit code " + toString(exitCode) + ". " + sErrLog);
		} catch(const CrException& e) {
			sFinalError = "Failed to deploy the Jitterpak '" + jitterpakFileName +
				"' for user '" + user + "' on host '" + host + "' using the arguments '" + 
				argsWithoutPwd + "'. \nReason: " + e.GetErrorString();
		}
	}
	if ( !sFinalError.empty() )
		throw CrException(sFinalError);
}
