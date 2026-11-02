/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_UTILS_DB_H
#define ETLFORGE_UTILS_DB_H

#include <string>

namespace konga {
	void VacuumDatabase(const std::string& sDb, bool bQuiet);
	void ReindexDatabase(const std::string& sDb, bool bQuiet);

	void RestoreDatabase(const std::string& sDb, const std::string& sDir, bool bQuiet);
	void BackupDatabase(const std::string& sDb, const std::string& sDir, bool bQuiet);

	void CleanWsdlFilesNotInDb(std::string& sWarnings);

	                                                                      
	void ReclaimDiskSpace(bool bQuiet);
}

#endif                       
