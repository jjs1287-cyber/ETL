/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WDILOGFILEUTILS_H
#define KONGA_WDILOGFILEUTILS_H

#include <cstdio>	        
#include <string>

class KString;

namespace konga {

	namespace WDILogFileUtils {

		bool GenerateLogFile();
		std::string GenerateErrLogFileName(int transformation_id, const std::string& guid);
		bool GetWDITempFileName(std::string& sDatFile, const std::string& tag = "");

		                                                   
		bool GetWDITempFileName(KString& sDatFile, const std::string& tag = "");
		bool CreateWDITempDirectory(int iOperationId, std::string& stTempDirectory, bool& bRemoveTempDir);

		                                                                                       
		                                    
		FILE* GetDebugFileHandle(const std::string& stDebugFilePrefix);
		std::string getWDILogDir();
	}
};

#endif                           
