/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_UPLOADED_FILES_MANAGER_H
#define KONGA_UPLOADED_FILES_MANAGER_H

#include <vector>
#include <string>

#include "UploadedFile.h"

namespace konga {

	class DbConnection;

class UploadedFilesManager {
public:
	UploadedFilesManager(DbConnection& tranDbConnection) : m_dbConnection(tranDbConnection) {}
	virtual ~UploadedFilesManager() {}

	void AddFile(const ZippedUploadedFile& file);
	void UpdateFile(const ZippedUploadedFile& file);
	void RemoveFile(const UploadedFile& file);

	                                                            
	                                                     
	                                                                             
	bool ExistsFile(const UploadedFile& incomingFile, UploadedFile& existingFile,
		bool bHasToExistOnDisk) const;

	                                                                     
	                                                                              
	                                                                  
	std::string GetFile(ZippedUploadedFile& file, char*& pZippedAndBase64) const;

protected:
	void DoAddFile(const ZippedUploadedFile& file);
	void DoUpdateFile(const ZippedUploadedFile& file);
	void PlaceFileOnDisk(const ZippedUploadedFile& file);

private:
	DbConnection& m_dbConnection;
};

}                   

#endif                                  
